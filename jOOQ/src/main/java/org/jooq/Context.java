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
import java.util.Map;

import org.jooq.exception.DataAccessException;

/**
 * A context type that is used for rendering SQL or for binding.
 *
 * @author Lukas Eder
 * @see BindContext
 * @see RenderContext
 */
public interface Context<C extends Context<C>> {

    /**
     * The configuration wrapped by this context.
     */
    Configuration configuration();

    /**
     * Get all custom data from this <code>Context</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link RenderContext} or
     * {@link BindContext}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a render or bind context.
     *
     * @return The custom data. This is never <code>null</code>
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>Context</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link RenderContext} or
     * {@link BindContext}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a render or bind context.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>ExecuteContext</code>
     * @see ExecuteListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>Context</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link RenderContext} or
     * {@link BindContext}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a render or bind context.
     *
     * @param key A key to identify the custom data
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see ExecuteListener
     */
    Object data(Object key, Object value);

    /**
     * Visit a <code>QueryPart</code> in the current <code>Context</code>.
     * <p>
     * This method is called by certain <code>QueryPart</code> implementations
     * to recursively visit component <code>QueryPart</code>s.
     *
     * @param part The component <code>QueryPart</code>
     * @throws DataAccessException If something went wrong while visiting the
     *             component <code>QueryPart</code>, e.g. when binding a
     *             variable
     */
    C visit(QueryPart part) throws DataAccessException;

    /**
     * TODO [#2667]
     * 
     * Properties of these methods:
     * - A clause is always started / ended, even if it isn't rendered or if it's empty!
     */
    C start(Clause clause);
    C end(Clause clause);

    /**
     * Whether the current context is rendering a SQL field declaration (e.g. a
     * {@link Field} in the <code>SELECT</code> clause of the query).
     */
    boolean declareFields();

    /**
     * Set the new context value for {@link #declareFields()}
     */
    C declareFields(boolean declareFields);

    /**
     * Whether the current context is rendering a SQL table declaration (e.g. a
     * {@link Table} in the <code>FROM</code> or <code>JOIN</code> clause of the
     * query).
     */
    boolean declareTables();

    /**
     * Set the new context value for {@link #declareTables()}
     */
    C declareTables(boolean declareTables);

    /**
     * Whether the current context is rendering a sub-query (nested query)
     */
    boolean subquery();

    /**
     * Set the new context value for {@link #subquery()}
     */
    C subquery(boolean subquery);

    /**
     * Get the next bind index. This increments an internal counter. This is
     * relevant for two use-cases:
     * <ul>
     * <li>When binding variables to a {@link PreparedStatement}. Client code
     * must assure that calling {@link #nextIndex()} is followed by setting a
     * bind value to {@link BindContext#statement()}</li>
     * <li>When rendering unnamed bind variables with
     * {@link RenderContext#paramType()} being to <code>NAMED</code></li>
     * </ul>
     */
    int nextIndex();

    /**
     * Peek the next bind index. This won't increment the internal counter,
     * unlike {@link #nextIndex()}
     */
    int peekIndex();

}
