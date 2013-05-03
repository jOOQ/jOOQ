/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq;

import java.sql.PreparedStatement;
import java.util.Map;

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
