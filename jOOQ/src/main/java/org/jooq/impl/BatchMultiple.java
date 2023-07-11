/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.AbstractQuery.connection;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jooq.Configuration;
import org.jooq.ExecuteContext.BatchMode;
import org.jooq.ExecuteListener;
import org.jooq.Query;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.impl.R2DBC.BatchMultipleSubscriber;
import org.jooq.impl.R2DBC.BatchSubscription;

import org.reactivestreams.Subscriber;

import io.r2dbc.spi.ConnectionFactory;

/**
 * @author Lukas Eder
 */
final class BatchMultiple extends AbstractBatch {

    final Query[] queries;

    public BatchMultiple(Configuration configuration, Query... queries) {
        super(configuration);

        this.queries = queries;
    }

    @Override
    public final int size() {
        return queries.length;
    }

    @Override
    public final void subscribe(Subscriber<? super Integer> subscriber) {
        ConnectionFactory cf = configuration.connectionFactory();

        if (!(cf instanceof NoConnectionFactory))
            subscriber.onSubscribe(new BatchSubscription<>(this, subscriber, s -> new BatchMultipleSubscriber(this, s)));

        // TODO: [#11700] Implement this
        else
            throw new UnsupportedOperationException("The blocking, JDBC backed implementation of reactive batching has not yet been implemented. Use the R2DBC backed implementation, instead, or avoid batching.");
    }

    @Override
    public final int[] execute() {
        return
        Tools.chunks(Arrays.asList(queries), SettingsTools.getBatchSize(Tools.settings(configuration)))
             .stream()
             .map(chunk -> execute(Tools.configuration(configuration), chunk.toArray(Tools.EMPTY_QUERY)))
             .flatMapToInt(IntStream::of)
             .toArray();
    }

    static int[] execute(Configuration configuration, Query[] queries) {

        // [#14784] TODO: Make this configurable also for other dialects
        if (NO_SUPPORT_BATCH.contains(configuration.dialect()))
            return Stream.of(queries).mapToInt(configuration.dsl()::execute).toArray();

        DefaultExecuteContext ctx = new DefaultExecuteContext(configuration, BatchMode.MULTIPLE, queries);
        ExecuteListener listener = ExecuteListeners.get(ctx);

        try {

            // [#8968] Keep start() event inside of lifecycle management
            listener.start(ctx);
            ctx.transformQueries(listener);

            if (ctx.statement() == null)
                ctx.statement(new SettingsEnabledPreparedStatement(connection(ctx)));

            // [#9295] use query timeout from settings
            int t = SettingsTools.getQueryTimeout(0, ctx.settings());
            if (t != 0)
                ctx.statement().setQueryTimeout(t);

            String[] batchSQL = ctx.batchSQL();
            for (int i = 0; i < ctx.batchQueries().length; i++) {
                ctx.sql(null);
                listener.renderStart(ctx);
                batchSQL[i] = DSL.using(configuration).renderInlined(ctx.batchQueries()[i]);
                ctx.sql(batchSQL[i]);
                listener.renderEnd(ctx);
            }

            for (int i = 0; i < ctx.batchQueries().length; i++) {
                ctx.sql(batchSQL[i]);
                listener.prepareStart(ctx);
                ctx.statement().addBatch(batchSQL[i]);
                listener.prepareEnd(ctx);
            }

            listener.executeStart(ctx);

            int[] result = ctx.statement().executeBatch();
            int[] batchRows = ctx.batchRows();
            for (int i = 0; i < batchRows.length && i < result.length; i++)
                batchRows[i] = result[i];

            listener.executeEnd(ctx);
            return result;
        }

        // [#3427] ControlFlowSignals must not be passed on to ExecuteListners
        catch (ControlFlowSignal e) {
            throw e;
        }
        catch (RuntimeException e) {
            ctx.exception(e);
            listener.exception(ctx);
            throw ctx.exception();
        }
        catch (SQLException e) {
            ctx.sqlException(e);
            listener.exception(ctx);
            throw ctx.exception();
        }
        finally {
            Tools.safeClose(listener, ctx);
        }
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return dsl.queries(queries).toString();
    }
}
