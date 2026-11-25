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

import java.sql.PreparedStatement;
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
import org.jooq.conf.Settings;
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
    private transient int                  fetchSizeHint;
    private transient boolean              fetchSizeHintApplied;
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
                    ctx.statement(DefaultExecuteContext.connection(ctx).prepareStatement(ctx.sql(), type, concurrency));
                else
                    ctx.statement(DefaultExecuteContext.connection(ctx).prepareStatement(ctx.sql(), type, concurrency, resultSetHoldability));
            }

            // Regular behaviour
            else
                ctx.statement(DefaultExecuteContext.connection(ctx).prepareStatement(ctx.sql()));
        }

        Tools.setFetchSize(ctx, fetchSize);

        // [#1854] [#4753] [#19261] Set the max number of rows for this result query
        long m = maxRows(ctx);
        if (m > 0L) {
            if (m > Integer.MAX_VALUE)
                ctx.statement().setLargeMaxRows(m);
            else
                ctx.statement().setMaxRows(truncateUpdateCount(m));
        }
    }

    /**
     * [#8882] Apply a fetch size hint to the statement, if applicable, or reset
     * a previously applied hint on a statement that has been kept open.
     */
    private final void applyFetchSizeHint(ExecuteContext ctx) throws SQLException {
        PreparedStatement s = ctx.statement();

        if (s == null)
            return;

        int h = fetchSizeHint(ctx.settings());
        if (h != 0) {
            if (log.isDebugEnabled())
                log.debug("Setting fetch size hint", h);

            s.setFetchSize(h);
            fetchSizeHintApplied = keepStatement();
        }

        // [#8882] Statements may be kept open and re-executed with fetch() or
        //         fetchLazy(), after a fetchOne() or fetchSingle() call
        else if (fetchSizeHintApplied) {
            s.setFetchSize(SettingsTools.getFetchSize(fetchSize, ctx.settings()));
            fetchSizeHintApplied = false;
        }
    }

    private final int fetchSizeHint(Settings settings) {

        // [#8882] An explicit fetchSize on the query takes precedence
        if (fetchSizeHint == 0 || fetchSize != 0 || Boolean.FALSE.equals(settings.isFetchSizeOnFetchOne()))
            return 0;

        // [#8882] Don't override driver specific (e.g. negative) or smaller
        //         fetch sizes from Settings.fetchSize
        int f = SettingsTools.getFetchSize(0, settings);
        return f == 0 || f > fetchSizeHint ? fetchSizeHint : 0;
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
        if (REPORT_FETCH_SIZE_WITH_AUTOCOMMIT.contains(ctx.dialect()) && f != 0 && DefaultExecuteContext.connection(ctx).getAutoCommit())
            log.info("Fetch Size", "A fetch size of " + f + " was set on an auto-commit PostgreSQL connection, which is not recommended. See https://jdbc.postgresql.org/documentation/query/#getting-results-based-on-a-cursor");

        // [#8882] fetchOne() and fetchSingle() never fetch more than 2 rows
        applyFetchSizeHint(ctx);

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

    /**
     * [#8882] Like {@link #fetchLazyNonAutoClosing()}, but pass a fetch size
     * hint to the JDBC driver, if no explicit fetch size was specified.
     */
    final Cursor<R> fetchLazyNonAutoClosing(int hint) {
        final int previousFetchSizeHint = fetchSizeHint;

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        fetchSizeHint = hint;

        try {
            return fetchLazyNonAutoClosing();
        }
        finally {
            fetchSizeHint = previousFetchSizeHint;
        }
    }

    @Override
    public final R fetchOne() {
        boolean l = hasLimit1();

        // [#7001] [#7430] [#8882] At most 1 or 2 rows are fetched
        return Tools.fetchOne(fetchLazyNonAutoClosing(l ? 1 : 2), l);
    }

    @Override
    public final R fetchSingle() {
        boolean l = hasLimit1();

        // [#7001] [#7430] [#8882] At most 1 or 2 rows are fetched
        return Tools.fetchSingle(fetchLazyNonAutoClosing(l ? 1 : 2), l);
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
