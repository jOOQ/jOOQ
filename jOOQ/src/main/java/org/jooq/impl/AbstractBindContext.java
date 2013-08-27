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

import static org.jooq.impl.Utils.visitAll;

import java.sql.SQLException;
import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;

/**
 * A base class for {@link BindContext} implementations
 *
 * @author Lukas Eder
 */
abstract class AbstractBindContext extends AbstractContext<BindContext> implements BindContext {

    AbstractBindContext(Configuration configuration) {
        super(configuration);
    }

    // ------------------------------------------------------------------------
    // BindContext API
    // ------------------------------------------------------------------------

    @Override
    @Deprecated
    public final BindContext bind(Collection<? extends QueryPart> parts) {
        return visitAll(this, parts);
    }

    @Override
    @Deprecated
    public final BindContext bind(QueryPart[] parts) {
        return visitAll(this, parts);
    }

    @Override
    @Deprecated
    public final BindContext bind(QueryPart part) {
        return visit(part);
    }

    @Override
    protected void visit0(QueryPartInternal internal) {
        bindInternal(internal);
    }

    @Override
    public final BindContext bindValues(Object... values) {

        // [#724] When values is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (values == null) {
            bindValues(new Object[] { null });
        }
        else {
            for (Object value : values) {
                Class<?> type = (value == null) ? Object.class : value.getClass();
                bindValue(value, type);
            }
        }

        return this;
    }

    @Override
    public final BindContext bindValue(Object value, Class<?> type) {
        try {
            return bindValue0(value, type);
        }
        catch (SQLException e) {
            throw Utils.translate(null, e);
        }
    }

    // ------------------------------------------------------------------------
    // AbstractBindContext template methods
    // ------------------------------------------------------------------------

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    protected void bindInternal(QueryPartInternal internal) {
        internal.bind(this);
    }

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    @SuppressWarnings("unused")
    protected BindContext bindValue0(Object value, Class<?> type) throws SQLException {
        return this;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }
}
