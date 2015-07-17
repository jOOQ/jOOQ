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

import java.util.Collection;

import org.jooq.impl.DSL;

/**
 * The step in the specification of aggregate functions where the SQL:2003
 * standard <code>FILTER clause</code> can be added.
 *
 * @author Lukas Eder
 */
public interface AggregateFilterStep<T> extends WindowBeforeOverStep<T> {

    /**
     * Add a <code>FILTER clause</code> to the aggregate function, connecting
     * conditions with each other with {@link Operator#AND}.
     */
    @Support
    WindowBeforeOverStep<T> filterWhere(Condition... conditions);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function, connecting
     * conditions with each other with {@link Operator#AND}.
     */
    @Support
    WindowBeforeOverStep<T> filterWhere(Collection<? extends Condition> conditions);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function.
     */
    @Support
    WindowBeforeOverStep<T> filterWhere(Field<Boolean> field);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function.
     */
    @Support
    WindowBeforeOverStep<T> filterWhere(Boolean field);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    WindowBeforeOverStep<T> filterWhere(String sql);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    WindowBeforeOverStep<T> filterWhere(String sql, Object... bindings);

    /**
     * Add a <code>FILTER clause</code> to the aggregate function.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    WindowBeforeOverStep<T> filterWhere(String sql, QueryPart... parts);

}
