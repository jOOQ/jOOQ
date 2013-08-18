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
import java.util.EventListener;

/**
 * A listener for {@link QueryPart} traversal events.
 * <p>
 * Users may want to centrally inject custom behaviour when rendering their
 * {@link QueryPart} objects or when binding values to {@link PreparedStatement}
 * s. This service provider allows to hook in callback method implementations
 * before or after these events:
 * <ul>
 * <li>The visit of a {@link Clause}</li>
 * <li>The visit of a {@link QueryPart}</li>
 * </ul>
 * <p>
 * The following rules apply to visiting clauses and query parts:
 * <ul>
 * <li>Clauses may "surround" a query part. See an example below.</li>
 * <li>Not every query part is "surrounded" by a clause</li>
 * </ul>
 * <p>
 * An example is given here:
 * <code><pre>SELECT 1 FROM [A CROSS JOIN B]</pre></code>
 * <p>
 * The above example will create the following set of events:
 *
 * <pre>
 * {@link Clause#SELECT}
 * +-{@link Clause#SELECT_SELECT}
 * | +-{@link Clause#FIELD}
 * |   +-val(1)
 * +-{@link Clause#SELECT_FROM}
 *   +-{@link Clause#TABLE_JOIN}
 *     +-{@link Clause#TABLE}
 *     | +-table("A")
 *     +-{@link Clause#TABLE_JOIN_CROSS}
 *       +-{@link Clause#TABLE}
 *         +-table("B")
 * </pre>
 * <p>
 * Whatever is not a {@link Clause} in the above example is a {@link QueryPart}.
 * <p>
 * Note: [#2694] [#2695] As of jOOQ 3.2, {@link VisitListener} receive events
 * only in the context of a {@link RenderContext}, not of a {@link BindContext}.
 *
 * @author Lukas Eder
 */
public interface VisitListener extends EventListener {

    /**
     * Called before entering a {@link Clause}.
     *
     * @see Context#start(Clause)
     */
    void clauseStart(VisitContext context);

    /**
     * Called after leaving a {@link Clause}.
     *
     * @see Context#end(Clause)
     */
    void clauseEnd(VisitContext context);

    /**
     * Called before visiting a {@link QueryPart}.
     * <p>
     * Certain <code>VisitListener</code> implementations may chose to replace
     * the {@link QueryPart} contained in the argument {@link VisitContext}
     * through {@link VisitContext#queryPart(QueryPart)}. This can be used for
     * many use-cases, for example to add a <code>CHECK OPTION</code> to an
     * Oracle <code>INSERT</code> statement: <code><pre>
     * -- Original query
     * INSERT INTO book (id, author_id, title)
     * VALUES (10, 15, '1984')
     *
     * -- Transformed query
     * INSERT INTO (
     *   SELECT * FROM book
     *   WHERE author_id IN (1, 2, 3)
     *   WITH CHECK OPTION
     * ) (id, author_id, title)
     * VALUES (10, 15, '1984')
     * </pre></code> The above SQL transformation allows to prevent inserting
     * new books for authors other than those with
     * <code>author_id IN (1, 2, 3)</code>
     *
     * @see Context#visit(QueryPart)
     */
    void visitStart(VisitContext context);

    /**
     * Called after visiting a {@link QueryPart}.
     *
     * @see Context#visit(QueryPart)
     */
    void visitEnd(VisitContext context);
}
