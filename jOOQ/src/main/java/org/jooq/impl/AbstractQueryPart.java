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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
abstract class AbstractQueryPart implements QueryPartInternal {

    private static final long serialVersionUID = 2078114876079493107L;

    @Override
    public final <I> I internalAPI(Class<I> internalType) {
        return internalType.cast(this);
    }

    // -------------------------------------------------------------------------
    // [#1544] The deprecated Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    @Override
    @Deprecated
    public void attach(Configuration configuration) {
    }

    Configuration getConfiguration() {
        return DefaultConfiguration.DEFAULT_CONFIGURATION;
    }

    // -------------------------------------------------------------------------
    // The QueryPart and QueryPart internal API
    // -------------------------------------------------------------------------

    @Override
    @Deprecated
    public final SQLDialect getDialect() {
        throw new UnsupportedOperationException("This method is no longer supported");
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

    /**
     * This method is also declared as {@link Query#getBindValues()}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final List<Object> getBindValues() {
        List<Object> result = new ArrayList<Object>();

        for (Param<?> param : getParams().values()) {
            result.add(param.getValue());
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * This method is also declared as {@link Query#getParams()}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final Map<String, Param<?>> getParams() {
        ParamCollector collector = new ParamCollector(getConfiguration());
        collector.bind(this);
        return Collections.unmodifiableMap(collector.result);
    }

    /**
     * This method is also declared as {@link Query#getParam(String)}
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final Param<?> getParam(String name) {
        return getParams().get(name);
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresFields() {
        return false;
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresTables() {
        return false;
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // This is a working default implementation. It should be overridden by
        // concrete subclasses, to improve performance
        if (that instanceof QueryPart) {
            String sql1 = create().renderInlined(this);
            String sql2 = create().renderInlined((QueryPart) that);

            return sql1.equals(sql2);
        }

        return false;
    }

    @Override
    public int hashCode() {
        // This is a working default implementation. It should be overridden by
        // concrete subclasses, to improve performance

        return create().renderInlined(this).hashCode();
    }

    @Override
    public String toString() {
        try {
            return create().renderInlined(this);
        }
        catch (SQLDialectNotSupportedException e) {
            return "[ ... " + e.getMessage() + " ... ]";
        }
    }

    // -------------------------------------------------------------------------
    // Internal convenience methods
    // -------------------------------------------------------------------------

    /**
     * Internal convenience method
     */
    protected final Factory create() {
        return create(getConfiguration());
    }

    /**
     * Internal convenience method
     */
    protected final Factory create(Configuration configuration) {
        return Factory.getNewFactory(configuration);
    }

    /**
     * Internal convenience method
     *
     * @deprecated - 2.3.0 - Do not reuse
     */
    @SuppressWarnings("unused")
    @Deprecated
    protected final DataAccessException translate(String task, String sql, SQLException e) {
        return translate(sql, e);
    }

    /**
     * Internal convenience method
     */
    protected final DataAccessException translate(String sql, SQLException e) {
        return Utils.translate(sql, e);
    }
}
