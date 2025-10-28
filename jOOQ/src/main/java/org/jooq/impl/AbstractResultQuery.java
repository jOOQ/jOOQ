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

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Internal.truncateUpdateCount;
import static org.jooq.impl.Tools.consumeResultSets;
import static org.jooq.impl.Tools.executeStatementAndGetFirstResultSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jooq.CloseableResultQuery;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.conf.SettingsTools;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.MockResultSet;

/**
 * A query that returns a {@link Result}
 *
 * @author Lukas Eder
 */
abstract class AbstractResultQuery<R extends Record>
extends
    AbstractQuery<R>
implements
    ResultQueryTrait<R>
{
    private static final JooqLogger        log                               = JooqLogger.getLogger(AbstractResultQuery.class);

    private static final Set<SQLDialect>   REPORT_FETCH_SIZE_WITH_AUTOCOMMIT = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    private long                           maxRows;
    private int                            fetchSize;
    private int                            resultSetConcurrency;
    private int                            resultSetType;
    private int                            resultSetHoldability;
    private Table<?>                       coerceTable;
    private Collection<? extends Field<?>> coerceFields;
    private transient boolean              lazy;
    private transient boolean              many;
    private transient Cursor<R>            cursor;
    private transient boolean              autoclosing                       = true;
    private Result<R>                      result;
    private ResultsImpl                    results;

    AbstractResultQuery(Configuration configuration) {
        super(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<R> bind(String param, Object value) {
        return (CloseableResultQuery<R>) super.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<R> bind(int index, Object value) {
        return (CloseableResultQuery<R>) super.bind(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<R> poolable(boolean poolable) {
        return (CloseableResultQuery<R>) super.poolable(poolable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<R> queryTimeout(int timeout) {
        return (CloseableResultQuery<R>) super.queryTimeout(timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<R> keepStatement(boolean k) {
        return (CloseableResultQuery<R>) super.keepStatement(k);
    }

    @Override
    public final CloseableResultQuery<R> maxRows(int rows) {
        this.maxRows = rows;
        return this;
    }

    @Override
    public final CloseableResultQuery<R> largeMaxRows(long rows) {
        this.maxRows = rows;
        return this;
    }

    @Override
    public final CloseableResultQuery<R> fetchSize(int rows) {
        this.fetchSize = rows;
        return this;
    }

    final int fetchSize() {
        return fetchSize;
    }

    @Override
    public final CloseableResultQuery<R> resultSetConcurrency(int concurrency) {
        this.resultSetConcurrency = concurrency;
        return this;
    }

    @Override
    public final CloseableResultQuery<R> resultSetType(int type) {
        this.resultSetType = type;
        return this;
    }

    @Override
    public final CloseableResultQuery<R> resultSetHoldability(int holdability) {
        this.resultSetHoldability = holdability;
        return this;
    }

    @Override
    protected final void prepare(ExecuteContext ctx) throws SQLException {
        if (ctx.statement() == null) {

            // [#1846] [#2265] [#2299] Users may explicitly specify how ResultSets
            // created by jOOQ behave. This will override any other default behaviour
            if (resultSetConcurrency != 0 || resultSetType != 0 || resultSetHoldability != 0) {
                int type = resultSetType != 0 ? resultSetType : ResultSet.TYPE_FORWARD_ONLY;
                int concurrency = resultSetConcurrency != 0 ? resultSetConcurrency : ResultSet.CONCUR_READ_ONLY;

                // Sybase doesn't support holdability. Avoid setting it!
                if (resultSetHoldability == 0)
                    ctx.statement(ctx.connection().prepareStatement(ctx.sql(), type, concurrency));
                else
                    ctx.statement(ctx.connection().prepareStatement(ctx.sql(), type, concurrency, resultSetHoldability));
            }

            // Regular behaviour
            else
                ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
        }

        Tools.setFetchSize(ctx, fetchSize);

        // [#1854] [#4753] [#19261] Set the max number of rows for this result query
        long m = maxRows(ctx);
        if (m > 0L) {
            if (m > Integer.MAX_VALUE)
                ctx.statement().setLargeMaxRows(maxRows);
            else
                ctx.statement().setMaxRows(truncateUpdateCount(maxRows));
        }
    }

    final long maxRows(ExecuteContext ctx) {
        if (maxRows > 0L)
            return maxRows;
        else if (ctx.settings().getLargeMaxRows() != null && ctx.settings().getLargeMaxRows() != 0)
            return ctx.settings().getLargeMaxRows();
        else if (ctx.settings().getMaxRows() != null && ctx.settings().getMaxRows() != 0)
            return ctx.settings().getMaxRows();
        else
            return 0L;
    }

    @Override
    final long execute(ExecuteContext ctx, ExecuteListener listener, boolean large) throws SQLException {
        listener.executeStart(ctx);

        // [#4511] [#4753] PostgreSQL doesn't like fetchSize with autoCommit == true
        int f = SettingsTools.getFetchSize(fetchSize, ctx.settings());
        if (REPORT_FETCH_SIZE_WITH_AUTOCOMMIT.contains(ctx.dialect()) && f != 0 && ctx.connection().getAutoCommit())
            log.info("Fetch Size", "A fetch size of " + f + " was set on a auto-commit PostgreSQL connection, which is not recommended. See https://jdbc.postgresql.org/documentation/query/#getting-results-based-on-a-cursor");

        SQLException e = executeStatementAndGetFirstResultSet(ctx, rendered.skipUpdateCounts);
        listener.executeEnd(ctx);

        // Fetch a single result set
        notManyIf:
        if (!many) {

            // [#6413] If the first execution yielded an exception, rather than an update count or result set
            //         and that exception is not thrown because of Settings.throwExceptions == THROW_NONE, we can stop
            if (e != null)
                break notManyIf;

            // [#5617] This may happen when using plain SQL API or a MockConnection and expecting a result set where
            //         there is none. The cursor / result is patched into the ctx only for single result sets, where
            //         access to the cursor / result is possible.
            // [#5818] It may also happen in case we're fetching from a batch and the first result is an update count,
            //         not a result set.
            if (ctx.resultSet() == null) {
                DSLContext dsl = DSL.using(ctx.configuration());
                Field<Integer> c = DSL.field(name("UPDATE_COUNT"), int.class);
                Result<Record1<Integer>> r = dsl.newResult(c);
                r.add(dsl.newRecord(c).values(ctx.rows()));
                ctx.resultSet(new MockResultSet(r));
            }

            Field<?>[] fields = getFields(() -> ctx.resultSet().getMetaData());
            cursor = new CursorImpl<>(ctx, listener, fields, keepStatement(), keepResultSet(), getTable(), getRecordType(), maxRows(ctx), autoclosing);

            if (!lazy) {
                result = cursor.fetch();
                cursor = null;
            }
        }

        // Fetch several result sets
        else {
            results = new ResultsImpl(ctx.configuration());
            consumeResultSets(ctx, listener, results, e, large);
        }

        return result != null ? result.size() : 0;
    }

    @Override
    protected final boolean keepResultSet() {
        return lazy;
    }

    final Collection<? extends Field<?>> coerce() {
        return coerceFields;
    }

    @Override
    public final Result<R> fetch() {
        execute();
        return result;
    }

    @Override
    public final Cursor<R> fetchLazy() {

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        lazy = true;

        try {
            execute();
        }
        finally {
            lazy = false;
        }

        return cursor;
    }

    /**
     * When we manage the lifecycle of a returned {@link Cursor} internally in
     * jOOQ, then the cursor must not be auto-closed.
     */
    @Override
    public final Cursor<R> fetchLazyNonAutoClosing() {
        final boolean previousAutoClosing = autoclosing;

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        autoclosing = false;

        try {
            return fetchLazy();
        }
        finally {
            autoclosing = previousAutoClosing;
        }
    }

    @Override
    public final Results fetchMany() {

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        many = true;

        try {
            execute();
        }
        finally {
            many = false;
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    final Table<? extends R> getTable() {
        if (coerceTable != null)
            return (Table<? extends R>) coerceTable;
        else
            return getTable0();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        if (coerceTable != null)
            return (Class<? extends R>) coerceTable.getRecordType();
        else
            return getRecordType0();
    }

    abstract Table<? extends R> getTable0();
    abstract Class<? extends R> getRecordType0();

    @Override
    public final Result<R> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <X extends Record> CloseableResultQuery<X> coerce(Table<X> table) {
        this.coerceTable = table;
        return (CloseableResultQuery<X>) coerce(Arrays.asList(table.fields()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final CloseableResultQuery<Record> coerce(Collection<? extends Field<?>> fields) {
        this.coerceFields = fields;
        return (CloseableResultQuery<Record>) this;
    }
}
