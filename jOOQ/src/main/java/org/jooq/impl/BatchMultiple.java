/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
class BatchMultiple implements Batch {

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
            Utils.safeClose(listener, ctx);
        }
    }
}
