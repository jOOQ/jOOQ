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

import org.jooq.impl.DSL;


/**
 * An intermediate type for the construction of a relational division
 *
 * @author Lukas Eder
 */
public interface DivideByOnStep {

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause, connecting
     * them with each other with {@link Operator#AND}.
     */
    @Support
    DivideByOnConditionStep on(Condition... conditions);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
     */
    @Support
    DivideByOnConditionStep on(Field<Boolean> condition);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #on(Condition...)} or
     *             {@link #on(Field)} instead. Due to ambiguity between calling
     *             this method using {@link Field#equals(Object)} argument, vs.
     *             calling the other method via a {@link Field#equal(Object)}
     *             argument, this method will be removed in the future.
     */
    @Deprecated
    @Support
    DivideByOnConditionStep on(Boolean condition);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
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
    DivideByOnConditionStep on(SQL sql);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
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
    DivideByOnConditionStep on(String sql);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
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
    DivideByOnConditionStep on(String sql, Object... bindings);

    /**
     * Add a division condition to the <code>DIVIDE BY</code> clause
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
    DivideByOnConditionStep on(String sql, QueryPart... parts);
}
