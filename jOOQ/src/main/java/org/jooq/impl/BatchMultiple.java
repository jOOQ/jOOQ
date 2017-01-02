/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.Batch;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Query;
import org.jooq.exception.ControlFlowSignal;

/**
 * @author Lukas Eder
 */
final class BatchMultiple implements Batch {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7337667281292354043L;

    private final Configuration configuration;
    private final Query[]       queries;

    public BatchMultiple(Configuration configuration, Query... queries) {
        this.configuration = configuration;
        this.queries = queries;
    }

    @Override
    public final int size() {
        return queries.length;
    }

    @Override
    public final int[] execute() {
        return execute(configuration, queries);
    }

    static int[] execute(final Configuration configuration, final Query[] queries) {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, queries);
        ExecuteListener listener = new ExecuteListeners(ctx);
        Connection connection = ctx.connection();

        try {
            ctx.statement(new SettingsEnabledPreparedStatement(connection));

            String[] batchSQL = ctx.batchSQL();
            for (int i = 0; i < queries.length; i++) {
                listener.renderStart(ctx);
                batchSQL[i] = DSL.using(configuration).renderInlined(queries[i]);
                listener.renderEnd(ctx);
            }

            for (String sql : batchSQL) {
                ctx.sql(sql);
                listener.prepareStart(ctx);
                ctx.statement().addBatch(sql);
                listener.prepareEnd(ctx);
                ctx.sql(null);
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
}
