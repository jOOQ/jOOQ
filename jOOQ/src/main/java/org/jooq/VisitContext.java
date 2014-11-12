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


/**
 * A context object for {@link QueryPart} traversal passed to registered
 * {@link VisitListener}'s.
 *
 * @author Lukas Eder
 * @see VisitListener
 */
public interface VisitContext extends Scope {

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
     * This is the same as calling {@link #clauses()}<code>.length</code>.
     */
    int clausesLength();

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
     * This is the same as calling {@link #queryParts()}<code>.length</code>.
     */
    int queryPartsLength();

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
