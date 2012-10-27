/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.impl;

import static org.jooq.conf.SettingsTools.executePreparedStatements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.exception.DetachedException;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
abstract class AbstractQuery extends AbstractQueryPart implements Query, AttachableInternal {

    private static final long           serialVersionUID = -8046199737354507547L;
    private static final JooqLogger     log              = JooqLogger.getLogger(AbstractQuery.class);

    private Configuration               configuration;
    private int                         timeout;
    private boolean                     keepStatement;
    private transient PreparedStatement statement;
    private transient String            sql;

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
    public final Configuration getConfiguration() {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // The Query API
    // -------------------------------------------------------------------------

    @Override
    public final List<Object> getBindValues() {
        List<Object> result = new ArrayList<Object>();

        for (Param<?> param : getParams().values()) {
            result.add(param.getValue());
        }

        return Collections.unmodifiableList(result);
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        ParamCollector collector = new ParamCollector(getConfiguration());
        collector.bind(this);
        return Collections.unmodifiableMap(collector.result);
    }

    @Override
    public final Param<?> getParam(String name) {
        return getParams().get(name);
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query bind(String param, Object value) {
        try {
            int index = Integer.valueOf(param);
            return bind(index, value);
        }
        catch (NumberFormatException e) {
            Param<?> p = getParam(param);

            if (p == null) {
                throw new IllegalArgumentException("No such parameter : " + param);
            }

            p.setConverted(value);
            return this;
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query bind(int index, Object value) {
        Param<?>[] array = getParams().values().toArray(new Param[0]);

        if (index < 1 || index > array.length) {
            throw new IllegalArgumentException("Index out of range for Query parameters : " + index);
        }

        array[index - 1].setConverted(value);
        return this;
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

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            }
            catch (SQLException e) {
                throw Utils.translate(sql, e);
            }
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final void cancel() {
        if (statement != null) {
            try {
                statement.cancel();
            }
            catch (SQLException e) {
                throw Utils.translate(sql, e);
            }
        }
    }

    @Override
    public final int execute() {
        if (isExecutable()) {

            // Get the attached configuration of this query
            Configuration c = getConfiguration();

            // [#1191] The following triggers a start event on all listeners.
            // This may be used to provide jOOQ with a JDBC connection, in case
            // this Query / Configuration was previously deserialised
            ExecuteContext ctx = new DefaultExecuteContext(c, this);
            ExecuteListener listener = new ExecuteListeners(ctx);

            Connection connection = c.getConnection();
            if (connection == null) {
                throw new DetachedException("Cannot execute query. No Connection configured");
            }

            int result = 0;
            try {

                // [#385] If a statement was previously kept open
                if (keepStatement() && statement != null) {
                    ctx.sql(sql);
                    ctx.statement(statement);
                }

                // [#385] First time statement preparing
                else {
                    listener.renderStart(ctx);
                    ctx.sql(getSQL());
                    listener.renderEnd(ctx);

                    sql = ctx.sql();

                    listener.prepareStart(ctx);
                    prepare(ctx);
                    listener.prepareEnd(ctx);

                    statement = ctx.statement();
                }

                // [#1856] Set the query timeout onto the Statement
                if (timeout != 0) {
                    ctx.statement().setQueryTimeout(timeout);
                }

                // [#1145] Bind variables only for true prepared statements
                if (executePreparedStatements(c.getSettings())) {
                    listener.bindStart(ctx);
                    create(c).bind(this, ctx.statement());
                    listener.bindEnd(ctx);
                }

                result = execute(ctx, listener);
                return result;
            }
            catch (SQLException e) {
                ctx.sqlException(e);
                listener.exception(ctx);
                throw ctx.exception();
            }
            finally {

                // ResultQuery.fetchLazy() needs to keep open resources
                if (!keepResult()) {
                    Utils.safeClose(listener, ctx, keepStatement());
                }

                if (!keepStatement()) {
                    statement = null;
                    sql = null;
                }
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Query is not executable", this);
            }

            return 0;
        }
    }

    /**
     * Default implementation to indicate whether this query should close the
     * {@link ResultSet} after execution. Subclasses may override this method.
     */
    protected boolean keepResult() {
        return false;
    }

    /**
     * Default implementation for preparing a statement. Subclasses may override
     * this method.
     */
    protected void prepare(ExecuteContext ctx) throws SQLException {
        ctx.statement(ctx.getConnection().prepareStatement(ctx.sql()));
    }

    /**
     * Default implementation for query execution using a prepared statement.
     * Subclasses may override this method.
     */
    protected int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        int result = 0;
        listener.executeStart(ctx);

        if (!ctx.statement().execute()) {
            result = ctx.statement().getUpdateCount();
        }

        listener.executeEnd(ctx);
        return result;
    }

    /**
     * Default implementation for executable check. Subclasses may override this
     * method.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable() {
        return true;
    }

    /**
     * This method is also declared as {@link Query#getSQL()}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final String getSQL() {
        if (executePreparedStatements(getConfiguration().getSettings())) {
            return getSQL(false);
        }
        else {
            return getSQL(true);
        }
    }

    /**
     * This method is also declared as {@link Query#getSQL(boolean)}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final String getSQL(boolean inline) {
        if (inline) {
            return create().renderInlined(this);
        }
        else {
            return create().render(this);
        }
    }
}
