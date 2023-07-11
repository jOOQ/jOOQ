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

import static java.lang.Boolean.TRUE;
import static org.jooq.Constants.FULL_VERSION;
import static org.jooq.ExecuteType.DDL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executePreparedStatements;
import static org.jooq.conf.SettingsTools.getParamType;
import static org.jooq.conf.ThrowExceptions.THROW_NONE;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.Tools.EMPTY_PARAM;
import static org.jooq.impl.Tools.blocking;
import static org.jooq.impl.Tools.consumeExceptions;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COUNT_BIND_VALUES;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_STATIC_STATEMENT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.CloseableQuery;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.conf.QueryPoolable;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.StatementType;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.impl.DefaultUnwrapperProvider.DefaultUnwrapper;
import org.jooq.tools.Ints;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.BatchedPreparedStatement;

/**
 * @author Lukas Eder
 */
abstract class AbstractQuery<R extends Record> extends AbstractAttachableQueryPart implements CloseableQuery {

    private static final JooqLogger      log                                 = JooqLogger.getLogger(AbstractQuery.class);
    private static final Set<SQLDialect> SET_AUTOCOMMIT_ON_START_TRANSACTION = SQLDialect.supportedBy(FIREBIRD, HSQLDB);

    private int                          timeout;
    private QueryPoolable                poolable                            = QueryPoolable.DEFAULT;
    private boolean                      keepStatement;
    transient PreparedStatement          statement;
    transient int                        statementExecutionCount;
    transient Rendered                   rendered;

    AbstractQuery(Configuration configuration) {
        super(configuration);
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    final void toSQLSemiColon(RenderContext ctx) {





    }

    // -------------------------------------------------------------------------
    // The Query API
    // -------------------------------------------------------------------------

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public CloseableQuery bind(String param, Object value) {
        Integer index = Ints.tryParse(param);
        if (index != null)
            return bind(index, value);

        ParamCollector collector = new ParamCollector(configuration(), true);
        collector.visit(this);
        List<Param<?>> params = collector.result.get(param);

        if (params == null || params.size() == 0)
            throw new IllegalArgumentException("No such parameter : " + param);

        for (Param<?> p : params) {
            ((AbstractParamX<?>) p).setConverted0(value);
            closeIfNecessary(p);
        }

        return this;
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public CloseableQuery bind(int index, Object value) {
        Param<?>[] params = getParams().values().toArray(EMPTY_PARAM);

        if (index < 1 || index > params.length)
            throw new IllegalArgumentException("Index out of range for Query parameters : " + index);

        AbstractParamX<?> param = (AbstractParamX<?>) params[index - 1];
        param.setConverted0(value);
        closeIfNecessary(param);
        return this;
    }

    /**
     * Close the statement if necessary.
     * <p>
     * [#1886] If there is an open (cached) statement and its bind values are
     * inlined due to a {@link StatementType#STATIC_STATEMENT} setting, the
     * statement should be closed.
     *
     * @param param The param that was changed
     */
    private final void closeIfNecessary(Param<?> param) {

        // This is relevant when there is an open statement, only
        if (keepStatement() && statement != null) {

            // When an inlined param is being changed, the previous statement
            // has to be closed, regardless if variable binding is performed
            if (param.isInline()) {
                close();
            }

            // If all params are inlined, the previous statement always has to
            // be closed
            else if (getParamType(configuration().settings()) == INLINED) {
                close();
            }
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public CloseableQuery poolable(boolean p) {
        this.poolable = p ? QueryPoolable.TRUE : QueryPoolable.FALSE;
        return this;
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public CloseableQuery queryTimeout(int t) {
        this.timeout = t;
        return this;
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public CloseableQuery keepStatement(boolean k) {
        this.keepStatement = k;
        return this;
    }

    protected final boolean keepStatement() {
        return keepStatement;
    }

    @Override
    public final void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            }
            catch (SQLException e) {
                throw Tools.translate(rendered.sql, e);
            }
        }
    }

    @Override
    public final void cancel() {
        if (statement != null) {
            try {
                statement.cancel();
            }
            catch (SQLException e) {
                throw Tools.translate(rendered.sql, e);
            }
        }
    }

    @Override
    public final int execute() {
        if (isExecutable()) {

            // Get the attached configuration of this query
            Configuration c = configurationOrDefault();

            // [#1191] The following triggers a start event on all listeners.
            //         This may be used to provide jOOQ with a JDBC connection,
            //         in case this Query / Configuration was previously
            //         deserialised
            DefaultExecuteContext ctx = new DefaultExecuteContext(c, this);
            ExecuteListener listener = ExecuteListeners.get(ctx);

            int result = 0;
            try {

                // [#8968] Keep start() event inside of lifecycle management
                listener.start(ctx);

                // [#385] If a statement was previously kept open
                if (keepStatement() && statement != null) {
                    ctx.sql(rendered.sql);
                    ctx.statement(statement);

                    // [#3191] Pre-initialise the ExecuteContext with a previous connection, if available.
                    ctx.connection(c.connectionProvider(), statement.getConnection());

                    // [#6903] Increment and set the new statement execution count on re-execution
                    ctx.withStatementExecutionCount(++statementExecutionCount);
                }

                // [#385] First time statement preparing
                else {
                    ctx.transformQueries(listener);

                    listener.renderStart(ctx);
                    rendered = getSQL0(ctx);
                    ctx.sql(rendered.sql);
                    listener.renderEnd(ctx);
                    rendered.sql = ctx.sql();

                    connection(ctx);

                    // [#7106] In some SQL dialects, starting a transaction requires JDBC interaction
                    if (this instanceof StartTransaction && SET_AUTOCOMMIT_ON_START_TRANSACTION.contains(ctx.dialect()))
                        ctx.connection().setAutoCommit(false);

                    listener.prepareStart(ctx);
                    prepare(ctx);
                    listener.prepareEnd(ctx);

                    statement = ctx.statement();
                }

                // [#1856] [#4753] Set the query timeout onto the Statement
                int t = SettingsTools.getQueryTimeout(timeout, ctx.settings());
                if (t != 0)
                    ctx.statement().setQueryTimeout(t);

                QueryPoolable p = SettingsTools.getQueryPoolable(poolable, ctx.settings());
                if (p == QueryPoolable.TRUE)
                    ctx.statement().setPoolable(true);
                else if (p == QueryPoolable.FALSE)
                    ctx.statement().setPoolable(false);

                if (

                    // [#1145] Bind variables only for true prepared statements
                    // [#2414] Even if parameters are inlined here, child
                    //         QueryParts may override this behaviour!
                    executePreparedStatements(c.settings()) &&

                    // [#1520] Renderers may enforce static statements, too
                    !TRUE.equals(ctx.data(DATA_FORCE_STATIC_STATEMENT))) {

                    listener.bindStart(ctx);
                    if (rendered.bindValues != null)
                        using(c).bindContext(ctx.statement()).visit(rendered.bindValues);
                    listener.bindEnd(ctx);
                }

                result = execute(ctx, listener);
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

                // [#2385] Successful fetchLazy() needs to keep open resources
                if (!keepResultSet() || ctx.exception() != null) {
                    Tools.safeClose(listener, ctx, keepStatement());
                }

                if (!keepStatement()) {
                    statement = null;
                    rendered = null;
                }
            }
        }
        else {
            if (log.isDebugEnabled())
                log.debug("Query is not executable", this);

            return 0;
        }
    }

    static final Connection connection(DefaultExecuteContext ctx) {
        Connection result = ctx.connection();

        // [#3234] Defer initialising of a connection until the prepare step
        // This optimises unnecessary ConnectionProvider.acquire() calls when
        // ControlFlowSignals are thrown
        if (result == null)
            if (ctx.configuration().connectionFactory() instanceof NoConnectionFactory)
                throw new DetachedException("Cannot execute query. No JDBC Connection configured");
            else
                throw new DetachedException(
                    "Attempt to execute a blocking method (e.g. Query.execute() or ResultQuery.fetch()) "
                  + "when only an R2BDC ConnectionFactory was configured. jOOQ's RowCountQuery and ResultQuery "
                  + "extend Publisher, which allows for reactive streams implementations to subscribe to the "
                  + "results of a jOOQ query. Simply embed your query in the stream, e.g. using Flux.from(query). "
                  + "See also: https://www.jooq.org/doc/latest/manual/sql-execution/fetching/reactive-fetching/");
        else
            return result;
    }

    @Override
    public final CompletionStage<Integer> executeAsync() {
        return executeAsync(Tools.configuration(this).executorProvider().provide());
    }

    @Override
    public final CompletionStage<Integer> executeAsync(Executor executor) {
        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(blocking(this::execute), executor), () -> executor);
    }

    /**
     * Default implementation to indicate whether this query should close the
     * {@link ResultSet} after execution. Subclasses may override this method.
     */
    protected boolean keepResultSet() {
        return false;
    }

    /**
     * Default implementation for preparing a statement. Subclasses may override
     * this method.
     */
    protected void prepare(ExecuteContext ctx) throws SQLException {
        if (ctx.statement() == null)
            ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
    }

    /**
     * Make sure a {@link PreparedStatement}, which may be a
     * {@link BatchedPreparedStatement}, is executed immediately, not batched.
     */
    final PreparedStatement executeImmediate(PreparedStatement s) throws SQLException {
        if (DefaultUnwrapper.isWrapperFor(s, BatchedPreparedStatement.class))
            s.unwrap(BatchedPreparedStatement.class).setExecuteImmediate(true);

        return s;
    }

    /**
     * Default implementation for query execution using a prepared statement.
     * Subclasses may override this method.
     */
    protected int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        int result = 0;
        PreparedStatement stmt = ctx.statement();

        try {
            listener.executeStart(ctx);

            // [#1829] Statement.execute() is preferred over Statement.executeUpdate(), as
            // we might be executing plain SQL and returning results.
            if (!stmt.execute()) {
                result = stmt.getUpdateCount();
                ctx.rows(result);
            }











            listener.executeEnd(ctx);
            return result;
        }

        // [#3011] [#3054] [#6390] [#6413] Consume additional exceptions if there are any
        catch (SQLException e) {
            consumeExceptions(ctx.configuration(), stmt, e);

            if (ctx.settings().getThrowExceptions() != THROW_NONE)
                throw e;
            else
                return stmt.getUpdateCount();
        }
    }

    /**
     * Default implementation for executable check. Subclasses may override this
     * method.
     */
    @Override
    public /* non-final */ boolean isExecutable() {
        return true;
    }

    private static final Rendered getSQL0(DefaultExecuteContext ctx) {
        Rendered result;
        DefaultRenderContext render;
        Configuration c = ctx.originalConfiguration();

        // [#3542] [#4977] Some dialects do not support bind values in DDL statements
        // [#6474] [#6929] Can this be communicated in a leaner way?
        if (ctx.type() == DDL) {
            ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
            render = new DefaultRenderContext(c, ctx);
            result = new Rendered(render.paramType(INLINED).visit(ctx.query()).render(), null, render.skipUpdateCounts());
        }
        else if (executePreparedStatements(c.settings())) {
            try {
                render = new DefaultRenderContext(c, ctx);
                render.data(DATA_COUNT_BIND_VALUES, true);
                result = new Rendered(render.visit(ctx.query()).render(), render.bindValues(), render.skipUpdateCounts());
            }
            catch (DefaultRenderContext.ForceInlineSignal e) {
                ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                render = new DefaultRenderContext(c, ctx);
                result = new Rendered(render.paramType(INLINED).visit(ctx.query()).render(), null, render.skipUpdateCounts());
            }
        }
        else {
            render = new DefaultRenderContext(c, ctx);
            result = new Rendered(render.paramType(INLINED).visit(ctx.query()).render(), null, render.skipUpdateCounts());
        }

























        return result;
    }








}
