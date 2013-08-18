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

import java.util.Map;

/**
 * A context object for {@link QueryPart} traversal passed to registered
 * {@link VisitListener}'s.
 *
 * @author Lukas Eder
 * @see VisitListener
 */
public interface VisitContext {

    /**
     * Get all custom data from this <code>VisitContext</code>.
     * <p>
     * This corresponds to {@link Context#data()} returned from
     * {@link #context()}.
     *
     * @return The custom data. This is never <code>null</code>
     * @see VisitListener
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>VisitContext</code>.
     * <p>
     * This corresponds to {@link Context#data(Object)} returned from
     * {@link #context()}.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>VisitListener</code>
     * @see VisitListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>VisitContext</code>.
     * <p>
     * This corresponds to {@link Context#data(Object, Object)} returned from
     * {@link #context()}.
     *
     * @param key A key to identify the custom data
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see VisitContext
     */
    Object data(Object key, Object value);

    /**
     * The configuration wrapped by this context.
     */
    Configuration configuration();

    /**
     * The most recent clause that was encountered through
     * {@link Context#start(Clause)}.
     */
    Clause clause();

    /**
     * A path of clauses going through the visiting tree.
     * <p>
     * This returns all previous clauses that were encountered through
     * {@link Context#start(Clause)} and that haven't been removed yet through
     * {@link Context#end(Clause)}. In other words, <code>VisitContext</code>
     * contains a stack of clauses.
     */
    Clause[] clauses();

    /**
     * The most recent {@link QueryPart} that was encountered through
     * {@link Context#visit(QueryPart)}.
     */
    QueryPart queryPart();

    /**
     * Replace the most recent {@link QueryPart} that was encountered through
     * {@link Context#visit(QueryPart)}.
     * <p>
     * This method can be called by {@link VisitListener} implementation
     * methods, in particular by {@link VisitListener#visitStart(VisitContext)}.
     *
     * @param part The new <code>QueryPart</code>.
     */
    void queryPart(QueryPart part);

    /**
     * A path of {@link QueryPart}s going through the visiting tree.
     * <p>
     * This returns all previous <code>QueryParts</code> that were encountered
     * through {@link Context#visit(QueryPart)}. In other words,
     * <code>VisitContext</code> contains a stack of <code>QueryParts</code>.
     */
    QueryPart[] queryParts();

    /**
     * The underlying {@link RenderContext} or {@link BindContext} object.
     */
    Context<?> context();

    /**
     * The underlying {@link RenderContext} or <code>null</code>, if the
     * underlying context is a {@link BindContext}.
     * <p>
     * [#2694] [#2695] As of jOOQ 3.2, the {@link QueryPart} traversal SPI
     * through {@link VisitListener} is only implemented for
     * {@link RenderContext}. Hence, you may need to inline bind values if
     * applicable.
     */
    RenderContext renderContext();

    /**
     * The underlying {@link BindContext} or <code>null</code>, if the
     * underlying context is a {@link RenderContext}.
     *
     * @throws UnsupportedOperationException [#2694] [#2695] As of jOOQ 3.2,
     *             this method is not yet implemented as {@link QueryPart}
     *             traversal SPI through {@link VisitListener} is only
     *             implemented for {@link RenderContext}
     */
    BindContext bindContext() throws UnsupportedOperationException;
}
