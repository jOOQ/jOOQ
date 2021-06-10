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
 *
 *
 *
 */
package org.jooq.impl;

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.consumeResultSets;
import static org.jooq.impl.Tools.executeStatementAndGetFirstResultSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

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
abstract class AbstractResultQuery<R extends Record> extends AbstractQuery<R> implements ResultQueryTrait<R> {
    private static final JooqLogger        log                               = JooqLogger.getLogger(AbstractResultQuery.class);

    private static final Set<SQLDialect>   REPORT_FETCH_SIZE_WITH_AUTOCOMMIT = SQLDialect.supportedBy(POSTGRES);

    private int                            maxRows;
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

    // Some temp variables for String interning
    private final Intern                   intern                            = new Intern();

    AbstractResultQuery(Configuration configuration) {
        super(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(String param, Object value) {
        return (ResultQuery<R>) super.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(int index, Object value) {
        return (ResultQuery<R>) super.bind(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> poolable(boolean poolable) {
        return (ResultQuery<R>) super.poolable(poolable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> queryTimeout(int timeout) {
        return (ResultQuery<R>) super.queryTimeout(timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> keepStatement(boolean k) {
        return (ResultQuery<R>) super.keepStatement(k);
    }

    @Override
    public final ResultQuery<R> maxRows(int rows) {
        this.maxRows = rows;
        return this;
    }

    @Override
    public final ResultQuery<R> fetchSize(int rows) {
        this.fetchSize = rows;
        return this;
    }

    final int fetchSize() {
        return fetchSize;
    }

    @Override
    public final ResultQuery<R> resultSetConcurrency(int concurrency) {
        this.resultSetConcurrency = concurrency;
        return this;
    }

    @Override
    public final ResultQuery<R> resultSetType(int type) {
        this.resultSetType = type;
        return this;
    }

    @Override
    public final ResultQuery<R> resultSetHoldability(int holdability) {
        this.resultSetHoldability = holdability;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(Field<?>... fields) {
        intern.internFields = fields;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(int... fieldIndexes) {
        intern.internIndexes = fieldIndexes;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(String... fieldNameStrings) {
        intern.internNameStrings = fieldNameStrings;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(Name... fieldNames) {
        intern.internNames = fieldNames;
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

        // [#1854] [#4753] Set the max number of rows for this result query
        int m = SettingsTools.getMaxRows(maxRows, ctx.settings());
        if (m != 0)
            ctx.statement().setMaxRows(m);
    }

    @Override
    protected final int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        listener.executeStart(ctx);

        // [#4511] [#4753] PostgreSQL doesn't like fetchSize with autoCommit == true
        int f = SettingsTools.getFetchSize(fetchSize, ctx.settings());
        if (REPORT_FETCH_SIZE_WITH_AUTOCOMMIT.contains(ctx.dialect()) && f != 0 && ctx.connection().getAutoCommit())
            log.info("Fetch Size", "A fetch size of " + f + " was set on a auto-commit PostgreSQL connection, which is not recommended. See http://jdbc.postgresql.org/documentation/head/query.html#query-with-cursor");

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

            Field<?>[] fields = getFields(ctx.resultSet().getMetaData());
            cursor = new CursorImpl<>(ctx, listener, fields, intern.internIndexes(fields), keepStatement(), keepResultSet(), getRecordType(), SettingsTools.getMaxRows(maxRows, ctx.settings()), autoclosing);

            if (!lazy) {
                result = cursor.fetch();
                cursor = null;
            }
        }

        // Fetch several result sets
        else {
            results = new ResultsImpl(ctx.configuration());
            consumeResultSets(ctx, listener, results, intern, e);
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
    @Override
    public final Class<? extends R> getRecordType() {
        if (coerceTable != null)
            return (Class<? extends R>) coerceTable.getRecordType();

        return getRecordType0();
    }

    abstract Class<? extends R> getRecordType0();

    @Override
    public final Result<R> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <X extends Record> ResultQuery<X> coerce(Table<X> table) {
        this.coerceTable = table;
        return (ResultQuery<X>) coerce(Arrays.asList(table.fields()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<Record> coerce(Collection<? extends Field<?>> fields) {
        this.coerceFields = fields;
        return (ResultQuery<Record>) this;
    }
}
