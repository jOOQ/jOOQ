/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import org.jooq.exception.DataAccessException;

/**
 * Base functionality declaration for all query objects
 * <p>
 * This interface is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public interface QueryPartInternal extends QueryPart {

    /**
     * This {@link QueryPart} can <code>accept</code> a {@link Context} object
     * in order to render a SQL string or to bind its variables.
     */
    void accept(Context<?> ctx);

    /**
     * Render this {@link QueryPart} to a SQL string contained in
     * <code>context.sql()</code>. The <code>context</code> will contain
     * additional information about how to render this <code>QueryPart</code>,
     * e.g. whether this <code>QueryPart</code> should be rendered as a
     * declaration or reference, whether this <code>QueryPart</code>'s contained
     * bind variables should be inlined or replaced by <code>'?'</code>, etc.
     *
     * @deprecated - 3.4.0 - [#2694] - Use {@link #accept(Context)} instead.
     */
    @Deprecated
    void toSQL(RenderContext ctx);

    /**
     * Bind all parameters of this {@link QueryPart} to a PreparedStatement
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @param ctx The context holding the next bind index and other information
     *            for variable binding
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.4.0 - [#2694] - Use {@link #accept(Context)} instead.
     */
    @Deprecated
    void bind(BindContext ctx) throws DataAccessException;

    /**
     * The {@link Clause}s that are represented by this query part.
     * <p>
     * {@link QueryPart}s can specify several <code>Clause</code>s for which an
     * event will be emitted {@link Context#start(Clause) before} (in forward
     * order) and {@link Context#end(Clause) after} (in reverse order) visiting
     * the the query part through {@link Context#visit(QueryPart)}
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     *
     * @return The <code>Clause</code>s represented by this query part or
     *         <code>null</code> or an empty array if this query part does not
     *         represent a clause.
     */
    Clause[] clauses(Context<?> ctx);

    /**
     * Check whether this {@link QueryPart} is able to declare fields in a
     * <code>SELECT</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresFields();

    /**
     * Check whether this {@link QueryPart} is able to declare tables in a
     * <code>FROM</code> clause or <code>JOIN</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresTables();

    /**
     * Check whether this {@link QueryPart} is able to declare windows in a
     * <code>WINDOW</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresWindows();

    /**
     * Check whether this {@link QueryPart} is able to declare common table
     * expressions in a <code>WITH</code> clause.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean declaresCTE();

    /**
     * Check whether this {@link QueryPart} is able to generate
     * <code>CAST</code> expressions around bind variables.
     * <p>
     * This method can be used by any {@link Context} to check how a certain SQL
     * clause should be rendered.
     * <p>
     * This method is for JOOQ INTERNAL USE only. Do not reference directly
     */
    boolean generatesCast();
}
