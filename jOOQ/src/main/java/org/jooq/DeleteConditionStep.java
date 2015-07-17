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

import org.jooq.impl.DSL;


/**
 * This type is used for the {@link Delete}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.delete(table)
 *       .where(field1.greaterThan(100))
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface DeleteConditionStep<R extends Record> extends DeleteFinalStep<R>, DeleteReturningStep<R> {

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> and(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> and(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> and(Boolean condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
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
    DeleteConditionStep<R> and(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
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
    DeleteConditionStep<R> and(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator
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
    DeleteConditionStep<R> and(String sql, QueryPart... parts);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> andNot(Condition condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> andNot(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> andNot(Boolean condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#AND} operator
     */
    @Support
    DeleteConditionStep<R> andExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#AND} operator
     */
    DeleteConditionStep<R> andNotExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> or(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> or(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> or(Boolean condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
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
    DeleteConditionStep<R> or(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
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
    DeleteConditionStep<R> or(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator
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
    DeleteConditionStep<R> or(String sql, QueryPart... parts);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> orNot(Condition condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> orNot(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> orNot(Boolean condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> orExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#OR} operator
     */
    @Support
    DeleteConditionStep<R> orNotExists(Select<?> select);
}
