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
