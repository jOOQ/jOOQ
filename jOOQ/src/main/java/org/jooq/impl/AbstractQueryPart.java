/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.impl;

import java.sql.SQLException;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
abstract class AbstractQueryPart implements QueryPartInternal {

    private static final long serialVersionUID = 2078114876079493107L;

    // -------------------------------------------------------------------------
    // [#1544] The deprecated Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    Configuration configuration() {
        return new DefaultConfiguration();
    }

    // -------------------------------------------------------------------------
    // The QueryPart and QueryPart internal API
    // -------------------------------------------------------------------------

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
    protected final DSLContext create() {
        return create(configuration());
    }

    /**
     * Internal convenience method
     */
    protected final DSLContext create(Configuration configuration) {
        return DSL.using(configuration);
    }

    /**
     * Internal convenience method
     */
    protected final DSLContext create(RenderContext ctx) {
        return DSL.using(ctx.configuration());
    }

    /**
     * Internal convenience method
     */
    protected final DSLContext create(BindContext ctx) {
        return DSL.using(ctx.configuration());
    }

    /**
     * Internal convenience method
     */
    protected final DataAccessException translate(String sql, SQLException e) {
        return Utils.translate(sql, e);
    }
}
