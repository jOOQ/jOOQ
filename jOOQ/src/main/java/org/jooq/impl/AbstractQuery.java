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

import static java.util.Arrays.asList;
import static org.jooq.Constants.FULL_VERSION;
import static org.jooq.ExecuteType.DDL;
// ...
// ...
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executePreparedStatements;
import static org.jooq.conf.SettingsTools.getParamType;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.Tools.EMPTY_PARAM;
import static org.jooq.impl.Tools.blocking;
import static org.jooq.impl.Tools.consumeExceptions;
import static org.jooq.impl.Tools.DataKey.DATA_COUNT_BIND_VALUES;
import static org.jooq.impl.Tools.DataKey.DATA_FORCE_STATIC_STATEMENT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.StatementType;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DetachedException;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
abstract class AbstractQuery extends AbstractQueryPart implements Query, AttachableInternal {

    private static final long       serialVersionUID = -8046199737354507547L;
    private static final JooqLogger log              = JooqLogger.getLogger(AbstractQuery.class);

    private Configuration           configuration;
    private int                     timeout;
    private boolean                 keepStatement;
    transient PreparedStatement     statement;
    transient Rendered              rendered;

    AbstractQuery(Configuration configuration) {
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // The Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;
    }

    @Override
    public final void detach() {
        attach(null);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    final void toSQLSemiColon(RenderContext ctx) {





    }

    // -------------------------------------------------------------------------
    // The Query API
    // -------------------------------------------------------------------------

    @Override
    public final List<Object> getBindValues() {
        return create().extractBindValues(this);
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return create().extractParams(this);
    }

    @Override
    public final Param<?> getParam(String name) {
        return create().extractParam(this, name);
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public Query bind(String param, Object value) {
        try {
            int index = Integer.valueOf(param);
            return bind(index, value);
        }
        catch (NumberFormatException e) {
            ParamCollector collector = new ParamCollector(configuration(), true);
            collector.visit(this);
            List<Param<?>> params = collector.result.get(param);

            if (params == null || params.size() == 0)
                throw new IllegalArgumentException("No such parameter : " + param);

            for (Param<?> p : params) {
                p.setConverted(value);
                closeIfNecessary(p);
            }

            return this;
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public Query bind(int index, Object value) {
        Param<?>[] params = getParams().values().toArray(EMPTY_PARAM);

        if (index < 1 || index > params.length)
            throw new IllegalArgumentException("Index out of range for Query parameters : " + index);

        Param<?> param = params[index - 1];
        param.setConverted(value);
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
    public Query queryTimeout(int t) {
        this.timeout = t;
        return this;
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query keepStatement(boolean k) {
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
            Configuration c = configuration();

            // [#1191] The following triggers a start event on all listeners.
            //         This may be used to provide jOOQ with a JDBC connection,
            //         in case this Query / Configuration was previously
            //         deserialised
            DefaultExecuteContext ctx = new DefaultExecuteContext(c, this);
            ExecuteListener listener = new ExecuteListeners(ctx);

            int result = 0;
            try {

                // [#385] If a statement was previously kept open
                if (keepStatement() && statement != null) {
                    ctx.sql(rendered.sql);
                    ctx.statement(statement);

                    // [#3191] Pre-initialise the ExecuteContext with a previous connection, if available.
                    ctx.connection(c.connectionProvider(), statement.getConnection());
                }

                // [#385] First time statement preparing
                else {
                    listener.renderStart(ctx);
                    rendered = getSQL0(ctx);
                    ctx.sql(rendered.sql);
                    listener.renderEnd(ctx);
                    rendered.sql = ctx.sql();

                    // [#3234] Defer initialising of a connection until the prepare step
                    // This optimises unnecessary ConnectionProvider.acquire() calls when
                    // ControlFlowSignals are thrown
                    if (ctx.connection() == null) {
                        throw new DetachedException("Cannot execute query. No Connection configured");
                    }

                    listener.prepareStart(ctx);
                    prepare(ctx);
                    listener.prepareEnd(ctx);

                    statement = ctx.statement();
                }

                // [#1856] [#4753] Set the query timeout onto the Statement
                int t = SettingsTools.getQueryTimeout(timeout, ctx.settings());
                if (t != 0) {
                    ctx.statement().setQueryTimeout(t);
                }

                if (

                    // [#1145] Bind variables only for true prepared statements
                    // [#2414] Even if parameters are inlined here, child
                    //         QueryParts may override this behaviour!
                    executePreparedStatements(c.settings()) &&

                    // [#1520] Renderers may enforce static statements, too
                    !Boolean.TRUE.equals(ctx.data(DATA_FORCE_STATIC_STATEMENT))) {

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
        ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
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

        // [#3011] [#3054] Consume additional exceptions if there are any
        catch (SQLException e) {
            consumeExceptions(ctx.configuration(), stmt, e);
            throw e;
        }
    }

    /**
     * Default implementation for executable check. Subclasses may override this
     * method.
     */
    @Override
    public boolean isExecutable() {
        return true;
    }

    static class Rendered {
        String                  sql;
        QueryPartList<Param<?>> bindValues;

        Rendered(String sql) {
            this(sql, null);
        }

        Rendered(String sql, QueryPartList<Param<?>> bindValues) {
            this.sql = sql;
            this.bindValues = bindValues;
        }

        @Override
        public String toString() {
            return sql;
        }
    }

    private final Rendered getSQL0(ExecuteContext ctx) {
        Rendered result;

        // [#3542] [#4977] Some dialects do not support bind values in DDL statements
        if (ctx.type() == DDL) {
            ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
            result = new Rendered(getSQL(INLINED));
        }
        else if (executePreparedStatements(configuration().settings())) {
            try {
                DefaultRenderContext render = new DefaultRenderContext(configuration);
                render.data(DATA_COUNT_BIND_VALUES, true);
                render.visit(this);
                result = new Rendered(render.render(), render.bindValues());
            }
            catch (DefaultRenderContext.ForceInlineSignal e) {
                ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                result = new Rendered(getSQL(INLINED));
            }
        }
        else {
            result = new Rendered(getSQL(INLINED));
        }

































        return result;
    }











    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSQL() {
        return getSQL(getParamType(Tools.settings(configuration())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSQL(ParamType paramType) {
        switch (paramType) {
            case INDEXED:
                return create().render(this);
            case INLINED:
                return create().renderInlined(this);
            case NAMED:
                return create().renderNamedParams(this);
            case NAMED_OR_INLINED:
                return create().renderNamedOrInlinedParams(this);
        }

        throw new IllegalArgumentException("ParamType not supported: " + paramType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final String getSQL(boolean inline) {
        return getSQL(inline ? INLINED : INDEXED);
    }
}
