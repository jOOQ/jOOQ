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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executeStaticStatements;
import static org.jooq.impl.Utils.dataTypes;
import static org.jooq.impl.Utils.fields;
import static org.jooq.impl.Utils.visitAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.exception.ControlFlowSignal;

/**
 * @author Lukas Eder
 */
class BatchSingle implements BatchBindStep {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 3793967258181493207L;

    private final DSLContext       create;
    private final Configuration  configuration;
    private final Query          query;
    private final List<Object[]> allBindValues;

    public BatchSingle(Configuration configuration, Query query) {
        this.create = DSL.using(configuration);
        this.configuration = configuration;
        this.query = query;
        this.allBindValues = new ArrayList<Object[]>();
    }

    @Override
    public final BatchSingle bind(Object... bindValues) {
        allBindValues.add(bindValues);
        return this;
    }

    @Override
    public final BatchSingle bind(Object[][] bindValues) {
        for (Object[] v : bindValues) {
            bind(v);
        }

        return this;
    }

    @Override
    public final int size() {
        return allBindValues.size();
    }

    @Override
    public final int[] execute() {

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(configuration.settings())) {
            return executeStatic();
        }
        else {
            return executePrepared();
        }
    }

    private final int[] executePrepared() {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, new Query[] { query });
        ExecuteListener listener = new ExecuteListeners(ctx);
        Connection connection = ctx.connection();

        // [#1371] fetch bind variables to restore them again, later
        DataType<?>[] paramTypes = dataTypes(query.getParams().values().toArray(new Field[0]));

        try {
            listener.renderStart(ctx);
            // [#1520] TODO: Should the number of bind values be checked, here?
            ctx.sql(create.render(query));
            listener.renderEnd(ctx);

            listener.prepareStart(ctx);
            ctx.statement(connection.prepareStatement(ctx.sql()));
            listener.prepareEnd(ctx);

            for (Object[] bindValues : allBindValues) {
                listener.bindStart(ctx);

                // [#1371] [#2139] Don't bind variables directly onto statement, bind them through the collected params
                //                 list to preserve type information
                // [#3547]         The original query may have no Params specified - e.g. when it was constructed with
                //                 plain SQL. In that case, infer the bind value type directly from the bind value
                List<Field<?>> params = (paramTypes.length > 0)
                    ? fields(bindValues, paramTypes)
                    : fields(bindValues);

                visitAll(new DefaultBindContext(configuration, ctx.statement()), params);

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
            Utils.safeClose(listener, ctx);
        }
    }

    private final int[] executeStatic() {
        List<Query> queries = new ArrayList<Query>();

        for (Object[] bindValues : allBindValues) {
            for (int i = 0; i < bindValues.length; i++) {
                query.bind(i + 1, bindValues[i]);
            }

            queries.add(create.query(query.getSQL(INLINED)));
        }

        return create.batch(queries).execute();
    }
}
