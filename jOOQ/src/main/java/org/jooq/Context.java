/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
     * @param value The custom data
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
     * Whether the current context is rendering a SQL window declaration (e.g. a
     * {@link WindowDefinition} in the <code>WINDOW</code> clause of the query).
     */
    boolean declareWindows();

    /**
     * Set the new context value for {@link #declareWindows()}
     */
    C declareWindows(boolean declareWindows);

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
