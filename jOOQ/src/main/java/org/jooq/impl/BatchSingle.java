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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executeStaticStatements;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.dataTypes;
import static org.jooq.impl.Tools.fields;
import static org.jooq.impl.Tools.visitAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
final class BatchSingle implements BatchBindStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID = 3793967258181493207L;
    private static final JooqLogger          log              = JooqLogger.getLogger(BatchSingle.class);

    private final DSLContext                 create;
    private final Configuration              configuration;
    private final Query                      query;
    private final Map<String, List<Integer>> nameToIndexMapping;
    private final List<Object[]>             allBindValues;
    private final int                        expectedBindValues;

    public BatchSingle(Configuration configuration, Query query) {
        int i = 0;

        ParamCollector collector = new ParamCollector(configuration, false);
        collector.visit(query);

        this.create = DSL.using(configuration);
        this.configuration = configuration;
        this.query = query;
        this.allBindValues = new ArrayList<Object[]>();
        this.nameToIndexMapping = new LinkedHashMap<String, List<Integer>>();
        this.expectedBindValues = collector.resultList.size();

        for (Entry<String, Param<?>> entry : collector.resultList) {
            List<Integer> list = nameToIndexMapping.get(entry.getKey());

            if (list == null) {
                list = new ArrayList<Integer>();
                nameToIndexMapping.put(entry.getKey(), list);
            }

            list.add(i++);
        }
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
        List<Object> defaultValues = query.getBindValues();

        Object[][] bindValues = new Object[namedBindValues.length][];
        for (int row = 0; row < bindValues.length; row++) {
            bindValues[row] = defaultValues.toArray();

            for (Entry<String, Object> entry : namedBindValues[row].entrySet()) {
                List<Integer> indexes = nameToIndexMapping.get(entry.getKey());

                if (indexes != null) {
                    for (int index : indexes) {
                        bindValues[row][index] = entry.getValue();
                    }
                }
            }
        }

        bind(bindValues);
        return this;
    }

    @Override
    public final int size() {
        return allBindValues.size();
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
        if (executeStaticStatements(configuration.settings())) {
            return executeStatic();
        }
        else {
            return executePrepared();
        }
    }

    private final void checkBindValues() {

        // [#4071] Help users debug cases where bind value counts don't match the expected number
        // [#5362] Don't do this for plain SQL queries
        if (expectedBindValues > 0)
            for (int i = 0; i < allBindValues.size(); i++)
                if (allBindValues.get(i).length != expectedBindValues)
                    log.info("Bind value count", "Batch bind value set " + i + " has " + allBindValues.get(i).length + " values when " + expectedBindValues + " values were expected");
    }

    private final int[] executePrepared() {
        ExecuteContext ctx = new DefaultExecuteContext(configuration, new Query[] { query });
        ExecuteListener listener = new ExecuteListeners(ctx);
        Connection connection = ctx.connection();

        // [#1371] fetch bind variables to restore them again, later
        // [#3940] Don't include inlined bind variables
        // [#4062] Make sure we collect also repeated named parameters
        ParamCollector collector = new ParamCollector(configuration, false);
        collector.visit(query);
        List<Param<?>> params = new ArrayList<Param<?>>();
        for (Entry<String, Param<?>> entry : collector.resultList)
            params.add(entry.getValue());

        DataType<?>[] paramTypes = dataTypes(params.toArray(EMPTY_FIELD));

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
                visitAll(new DefaultBindContext(configuration, ctx.statement()),
                    (paramTypes.length > 0)
                        ? fields(bindValues, paramTypes)
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
