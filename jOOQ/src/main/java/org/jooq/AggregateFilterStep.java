/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
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
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #filterWhere(Condition...)} or
     *             {@link #filterWhere(Field)} instead. Due to ambiguity between
     *             calling this method using {@link Field#equals(Object)}
     *             argument, vs. calling the other method via a
     *             {@link Field#equal(Object)} argument, this method will be
     *             removed in the future.
     */
    @Deprecated
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
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    WindowBeforeOverStep<T> filterWhere(SQL sql);

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
     * @see DSL#sql(String, Object...)
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
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    WindowBeforeOverStep<T> filterWhere(String sql, QueryPart... parts);

}
