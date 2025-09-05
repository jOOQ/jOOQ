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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executeStaticStatements;
import static org.jooq.impl.AbstractQuery.connection;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitAll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.ExecuteContext.BatchMode;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.impl.R2DBC.BatchSingleSubscriber;
import org.jooq.impl.R2DBC.BatchSubscription;
import org.jooq.tools.JooqLogger;

import org.reactivestreams.Subscriber;

import io.r2dbc.spi.ConnectionFactory;

/**
 * @author Lukas Eder
 */
final class BatchSingle extends AbstractBatch implements BatchBindStep {
    private static final JooqLogger  log              = JooqLogger.getLogger(BatchSingle.class);

    final Query                      query;
    final Map<String, List<Integer>> nameToIndexMapping;
    final List<Object[]>             allBindValues;
    final int                        expectedBindValues;

    public BatchSingle(Configuration configuration, Query query) {
        super(configuration);

        int i = 0;

        ParamCollector collector = new ParamCollector(configuration, false);
        collector.visit(query);

        this.query = query;
        this.allBindValues = new ArrayList<>();
        this.nameToIndexMapping = new LinkedHashMap<>();
        this.expectedBindValues = collector.resultList.size();

        for (Entry<String, Param<?>> entry : collector.resultList)
            nameToIndexMapping.computeIfAbsent(entry.getKey(), e -> new ArrayList<>()).add(i++);
    }

    @Override
    public final BatchSingle bind(Object... bindValues) {
        allBindValues.add(bindValues);
        return this;
    }

    @Override
    public final BatchSingle bind(Object[]... bindValues) {
        for (Object[] v : bindValues)
            bind(v);

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final BatchSingle bind(Map<String, Object> namedBindValues) {
        return bind(new Map[] { namedBindValues });
    }

    @Override
    @SafeVarargs
    public final BatchSingle bind(Map<String, Object>... namedBindValues) {
        List<Object> defaultValues = dsl.extractBindValues(query);

        Object[][] bindValues = new Object[namedBindValues.length][];
        for (int i = 0; i < bindValues.length; i++) {
            Object[] row = bindValues[i] = defaultValues.toArray();

            namedBindValues[i].forEach((k, v) -> {
                List<Integer> indexes = nameToIndexMapping.get(k);

                if (indexes != null)
                    for (int index : indexes)
                        row[index] = v;
            });
        }

        bind(bindValues);
        return this;
    }

    @Override
    public final int size() {
        return allBindValues.size();
    }

    @Override
    public final void subscribe(Subscriber<? super Integer> subscriber) {
        ConnectionFactory cf = configuration.connectionFactory();

        if (!(cf instanceof NoConnectionFactory))
            subscriber.onSubscribe(new BatchSubscription<>(this, subscriber, s -> new BatchSingleSubscriber(this, s)));

        // TODO: [#11700] Implement this
        else
            throw new UnsupportedOperationException("The blocking, JDBC backed implementation of reactive batching has not yet been implemented. Use the R2DBC backed implementation, instead, or avoid batching.");
    }

    @Override
    public final int[] execute() {

        // [#4554] If no variables are bound this should be treated like a
        // BatchMultiple as the intention was most likely to call the varargs
        // version of DSLContext#batch(Query... queries) with a single parameter.
        if (allBindValues.isEmpty()) {
            log.info("Single batch", "No bind variables have been provided with a single statement batch execution. This may be due to accidental API misuse");
            return BatchMultiple.execute(configuration, new Query[] { query });
        }

        checkBindValues();

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(configuration.settings()))
            return executeStatic();
        else
            return executePrepared();
    }

    final void checkBindValues() {

        // [#4071] Help users debug cases where bind value counts don't match the expected number
        // [#5362] Don't do this for plain SQL queries
        if (expectedBindValues > 0)
            for (int i = 0; i < allBindValues.size(); i++)
                if (allBindValues.get(i).length != expectedBindValues)
                    log.info("Bind value count", "Batch bind value set " + i + " has " + allBindValues.get(i).length + " values when " + expectedBindValues + " values were expected");
    }

    private final int[] executePrepared() {
        DefaultExecuteContext ctx = new DefaultExecuteContext(configuration, BatchMode.SINGLE, new Query[] { query });
        ExecuteListener listener = ExecuteListeners.get(ctx, true);

        try {
            // [#8968] Keep start() event inside of lifecycle management
            listener.start(ctx);
            ctx.transformQueries(listener);

            listener.renderStart(ctx);
            Rendered.rendered(configuration, ctx, ctx.batchQueries()[0], false, false).setSQLAndParams(ctx);
            listener.renderEnd(ctx);

            listener.prepareStart(ctx);
            if (ctx.statement() == null)
                ctx.statement(connection(ctx).prepareStatement(ctx.sql()));
            listener.prepareEnd(ctx);

            // [#9295] use query timeout from settings
            int t = SettingsTools.getQueryTimeout(0, ctx.settings());
            if (t != 0)
                ctx.statement().setQueryTimeout(t);

            for (Object[] bindValues : allBindValues) {
                listener.bindStart(ctx);

                // [#1371] [#2139] Don't bind variables directly onto statement, bind them through the collected params
                //                 list to preserve type information
                // [#3547]         The original query may have no Params specified - e.g. when it was constructed with
                //                 plain SQL. In that case, infer the bind value type directly from the bind value
                visitAll(new DefaultBindContext(configuration, ctx, ctx.statement()),
                    (ctx.params().length > 0)
                        ? fields(bindValues, ctx.params())
                        : fields(bindValues));

                listener.bindEnd(ctx);
                ctx.statement().addBatch();
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

    final Param<?>[] extractParams() {
        // [#1371] fetch bind variables to restore them again, later
        // [#3940] Don't include inlined bind variables
        // [#4062] Make sure we collect also repeated named parameters
        ParamCollector collector = new ParamCollector(configuration, false);
        collector.visit(query);
        return map(collector.resultList, e -> e.getValue(), Param[]::new);
    }

    private final int[] executeStatic() {
        return batchMultiple().execute();
    }

    private final Batch batchMultiple() {
        List<Query> queries = new ArrayList<>(allBindValues.size());

        for (Object[] bindValues : allBindValues) {
            for (int i = 0; i < bindValues.length; i++)
                query.bind(i + 1, bindValues[i]);

            queries.add(dsl.query(query.getSQL(INLINED)));
        }

        return dsl.batch(queries);
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return batchMultiple().toString();
    }
}
