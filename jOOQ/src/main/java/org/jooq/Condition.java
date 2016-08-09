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
 * A condition to be used in a query's where part
 *
 * @author Lukas Eder
 */
public interface Condition extends QueryPart {

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition and(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition and(Field<Boolean> other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #and(Condition)} or
     *             {@link #and(Field)} instead. Due to ambiguity between calling
     *             this method using {@link Field#equals(Object)} argument, vs.
     *             calling the other method via a {@link Field#equal(Object)}
     *             argument, this method will be removed in the future.
     */
    @Deprecated
    @Support
    Condition and(Boolean other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition and(SQL sql);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition and(String sql);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @param bindings The bindings
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition and(String sql, Object... bindings);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The combined condition
     * @see DSL#condition(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition and(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition andNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition andNot(Field<Boolean> other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #andNot(Condition)} or
     *             {@link #andNot(Field)} instead. Due to ambiguity between
     *             calling this method using {@link Field#equals(Object)}
     *             argument, vs. calling the other method via a
     *             {@link Field#equal(Object)} argument, this method will be
     *             removed in the future.
     */
    @Deprecated
    @Support
    Condition andNot(Boolean other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    Condition andExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    Condition andNotExists(Select<?> select);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition or(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition or(Field<Boolean> other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #or(Condition)} or
     *             {@link #or(Field)} instead. Due to ambiguity between calling
     *             this method using {@link Field#equals(Object)} argument, vs.
     *             calling the other method via a {@link Field#equal(Object)}
     *             argument, this method will be removed in the future.
     */
    @Deprecated
    @Support
    Condition or(Boolean other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition or(SQL sql);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @return The combined condition
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition or(String sql);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The other condition
     * @param bindings The bindings
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition or(String sql, Object... bindings);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The combined condition
     * @see DSL#condition(String, Object...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Condition or(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition orNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    Condition orNot(Field<Boolean> other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #orNot(Condition)} or
     *             {@link #orNot(Boolean)} instead. Due to ambiguity between
     *             calling this method using {@link Field#equals(Object)}
     *             argument, vs. calling the other method via a
     *             {@link Field#equal(Object)} argument, this method will be
     *             removed in the future.
     */
    @Deprecated
    @Support
    Condition orNot(Boolean other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    Condition orExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    Condition orNotExists(Select<?> select);

    /**
     * Invert this condition
     * <p>
     * This is the same as calling {@link DSL#not(Condition)}
     *
     * @return This condition, inverted
     */
    @Support
    Condition not();
}
