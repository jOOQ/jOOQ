/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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
 * <h3>A remark about performance</h3>
 * <p>
 * Implementors of this SPI should be wary of performance implications of their
 * implementations. The below methods are called for every AST element of every
 * query, which produces a lot of calls throughout an application. What would
 * otherwise be premature optimisations may have great effect inside the
 * <code>VisitListener</code>. For more details, please refer to this article:
 * <a href=
 * "http://blog.jooq.org/2015/02/05/top-10-easy-performance-optimisations-in-java/">
 * http://blog.jooq.org/2015/02/05/top-10-easy-performance-optimisations-in-
 * java/</a>.
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
