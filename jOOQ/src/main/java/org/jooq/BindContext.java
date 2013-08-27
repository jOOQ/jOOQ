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
package org.jooq;

import java.sql.PreparedStatement;
import java.util.Collection;

import org.jooq.exception.DataAccessException;

/**
 * The bind context is used for binding {@link QueryPart}'s and their contained
 * values to a {@link PreparedStatement}'s bind variables. A new bind context is
 * instanciated every time a {@link Query} is bound. <code>QueryPart</code>'s
 * will then pass the same context to their components
 * <p>
 * This interface is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 * @see RenderContext
 */
public interface BindContext extends Context<BindContext> {

    /**
     * Retrieve the context's underlying {@link PreparedStatement}
     */
    PreparedStatement statement();

    /**
     * Bind values from a {@link QueryPart}. This will also increment the
     * internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(QueryPart part) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(Collection<? extends QueryPart> parts) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(QueryPart[] parts) throws DataAccessException;

    /**
     * Bind a value using a specific type. This will also increment the internal
     * counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bindValue(Object value, Class<?> type) throws DataAccessException;

    /**
     * Bind several values. This will also increment the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bindValues(Object... values) throws DataAccessException;
}
