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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.TRUE;
import static org.jooq.conf.SettingsTools.executeStaticStatements;
import static org.jooq.impl.Tools.map;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.tools.JooqLogger;

import org.reactivestreams.Subscriber;

/**
 * @author Lukas Eder
 */
final class BatchCRUD extends AbstractBatch {

    private static final JooqLogger log = JooqLogger.getLogger(BatchCRUD.class);
    private final TableRecord<?>[]  records;
    private final Action            action;

    BatchCRUD(Configuration configuration, Action action, TableRecord<?>[] records) {
        super(configuration);

        this.action = action;
        this.records = records;
    }

    @Override
    public final int size() {
        return records.length;
    }

    @Override
    public void subscribe0(Subscriber<? super R2DBC.RowCount> s) {

        // [#11700] TODO: Implement this
        throw new UnsupportedOperationException("BatchCRUD operations are not yet supported in a reactive way. Use ordinary batch operations, instead, or avoid batching. See https://github.com/jOOQ/jOOQ/issues/14874");
    }

    @Override
    public final int[] execute() {

        // [#1180] Run batch queries with BatchMultiple, if no bind variables
        // should be used...
        if (executeStaticStatements(configuration.settings()))
            return executeStatic();
        else
            return executePrepared();
    }

    @Override
    public final long[] executeLarge() {
        return IntStream.of(execute()).mapToLong(i -> (long) i).toArray();
    }

    private final Configuration deriveConfiguration(QueryCollector collector) {
        Configuration local = configuration.deriveAppending(collector);

        local.settings()

            // [#1529] Avoid DEBUG logging of single INSERT / UPDATE statements
            .withExecuteLogging(false)

            // [#3327] [#11509] [#14573] We can't return generated keys from batches (yet)
            .withReturnAllOnUpdatableRecord(false)
            .withReturnDefaultOnUpdatableRecord(false)
            .withReturnComputedOnUpdatableRecord(false)
            .withReturnIdentityOnUpdatableRecord(false);

        return local;
    }

    private final int[] executePrepared() {
        boolean optimisticLocking = TRUE.equals(configuration.settings().isExecuteWithOptimisticLocking());
        Map<String, List<QueryCollectorSignal>> queries = new LinkedHashMap<>();
        List<QueryCollectorSignal> signals = new ArrayList<>();
        QueryCollector collector = new QueryCollector();

        // Add the QueryCollector to intercept query execution after rendering
        Configuration local = deriveConfiguration(collector);

        for (int i = 0; i < records.length; i++) {
            Configuration previous = records[i].configuration();

            try {
                records[i].attach(local);
                executeAction(i);

                if (optimisticLocking)
                    signals.add(null);
            }
            catch (QueryCollectorSignal e) {
                if (optimisticLocking)
                    signals.add(e);

                // Aggregate executable queries by identical SQL
                if (e.getQuery().isExecutable())
                    queries.computeIfAbsent(e.getSQL(), s -> new ArrayList<>()).add(e);
            }
            finally {
                records[i].attach(previous);
            }
        }

        if (log.isDebugEnabled())
            log.debug("Batch " + action + " of " + records.length + " records using " + queries.size() + " distinct queries (lower is better) with an average number of bind variable sets of " + queries.values().stream().mapToInt(List::size).average().orElse(0.0) + " (higher is better)");

        // Execute one batch statement for each identical SQL statement. Every
        // SQL statement may have several queries with different bind values.
        // The order is preserved as much as possible
        List<Integer> result = new ArrayList<>();
        queries.forEach((k, v) -> {
            BatchBindStep batch = dsl.batch(v.get(0).getQuery());

            for (QueryCollectorSignal signal : v)
                batch.bind(map(signal.getParams(), p -> p.getValue(), Object[]::new));

            int[] array = batch.execute();
            for (int i : array)
                result.add(i);
        });

        int[] array = new int[result.size()];
        for (int i = 0; i < result.size(); i++)
            array[i] = result.get(i);

        // [#8283] Store back optimistic locking values to updated records
        if (optimisticLocking)
            updateRecordVersionsAndTimestamps(signals, array);

        updateChangedFlag();
        return array;
    }

    private final int[] executeStatic() {
        boolean optimisticLocking = TRUE.equals(configuration.settings().isExecuteWithOptimisticLocking());
        List<Query> queries = new ArrayList<>();
        List<QueryCollectorSignal> signals = new ArrayList<>();
        QueryCollector collector = new QueryCollector();
        Configuration local = deriveConfiguration(collector);

        for (int i = 0; i < records.length; i++) {
            Configuration previous = records[i].configuration();

            try {
                records[i].attach(local);
                executeAction(i);

                if (optimisticLocking)
                    signals.add(null);
            }
            catch (QueryCollectorSignal e) {
                if (optimisticLocking)
                    signals.add(e);

                Query query = e.getQuery();

                if (query.isExecutable())
                    queries.add(query);
            }
            finally {
                records[i].attach(previous);
            }
        }

        // Resulting statements can be batch executed in their requested order
        int[] result = dsl.batch(queries).execute();

        // [#8283] Store back optimistic locking values to updated records
        if (optimisticLocking)
            updateRecordVersionsAndTimestamps(signals, result);

        updateChangedFlag();
        return result;
    }

    private final void updateRecordVersionsAndTimestamps(List<QueryCollectorSignal> signals, int[] array) {
        for (int i = 0; i < records.length && i < array.length; i++) {
            QueryCollectorSignal signal = signals.get(i);

            if (signal != null && array[i] > 0)
                ((TableRecordImpl<?>) records[i]).setRecordVersionAndTimestamp(signal.version, signal.timestamp);
        }
    }

    private final void executeAction(int i) {
        switch (action) {
            case STORE:
                ((UpdatableRecord<?>) records[i]).store();
                break;
            case INSERT:
                records[i].insert();
                break;
            case UPDATE:
                ((UpdatableRecord<?>) records[i]).update();
                break;
            case MERGE:
                ((UpdatableRecord<?>) records[i]).merge();
                break;
            case DELETE:
                ((UpdatableRecord<?>) records[i]).delete();
                break;
        }
    }

    private final void updateChangedFlag() {
        // 1. Deleted records should be marked as changed, such that subsequent
        //    calls to store() will insert them again
        // 2. Stored records should be marked as unchanged
        for (TableRecord<?> record : records) {
            record.touched(action == Action.DELETE);

            // [#3362] If new records (fetched = false) are batch-stored twice in a row, the second
            // batch-store needs to generate an UPDATE statement.
            if (record instanceof AbstractRecord r)
                r.fetched = action != Action.DELETE;
        }
    }

    /**
     * The action to be performed by this operation.
     */
    enum Action {

        /**
         * Corresponds to {@link UpdatableRecord#store()}.
         */
        STORE,

        /**
         * Corresponds to {@link UpdatableRecord#insert()}.
         */
        INSERT,

        /**
         * Corresponds to {@link UpdatableRecord#update()}.
         */
        UPDATE,

        /**
         * Corresponds to {@link UpdatableRecord#merge()}.
         */
        MERGE,

        /**
         * Corresponds to {@link UpdatableRecord#delete()}.
         */
        DELETE
    }

    /**
     * Collect queries
     * <p>
     * The query collector intercepts query execution after rendering. This
     * allows for rendering SQL according to the specific logic contained in
     * TableRecords without actually executing that SQL
     */
    private static class QueryCollector implements ExecuteListener {

        @Override
        public void renderEnd(ExecuteContext ctx) {
            throw new QueryCollectorSignal(ctx.sql(), ctx.params(), ctx.query());
        }
    }

    /**
     * A query execution interception signal.
     * <p>
     * This exception is used as a signal for jOOQ's internals to abort query
     * execution, and return generated SQL back to batch execution.
     */
    static class QueryCollectorSignal extends ControlFlowSignal {
        final String     sql;
        final Param<?>[] params;
        final Query      query;
        BigInteger       version;
        Timestamp        timestamp;

        QueryCollectorSignal(String sql, Param<?>[] params, Query query) {
            this.sql = sql;
            this.params = params;
            this.query = query;
        }

        String getSQL() {
            return sql;
        }

        Param<?>[] getParams() {
            return params;
        }

        Query getQuery() {
            return query;
        }
    }
}
