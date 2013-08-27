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

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.impl.DSL;


/**
 * A condition to be used in a query's where part
 *
 * @author Lukas Eder
 */
@State(
    name = "Condition",
    aliases = {
        "CombinedPredicate",
        "ComparisonPredicate",
        "LikePredicate",
        "InPredicate",
        "ExistsPredicate",
        "NullPredicate",
        "DistinctPredicate",
        "BetweenPredicate"
    },
    terminal = true
)
public interface Condition extends QueryPart {

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition and(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#AND}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition and(Field<Boolean> other);

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
     */
    @Support
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
     */
    @Support
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
     */
    @Support
    Condition and(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition andNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#AND} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition andNot(Field<Boolean> other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition andExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#AND} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "AND NOT EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition andNotExists(Select<?> select);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition or(Condition other);

    /**
     * Combine this condition with another one using the {@link Operator#OR}
     * operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition or(Field<Boolean> other);

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
     */
    @Support
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
     */
    @Support
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
     */
    @Support
    Condition or(String sql, QueryPart... parts);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition orNot(Condition other);

    /**
     * Combine this condition with a negated other one using the
     * {@link Operator#OR} operator.
     *
     * @param other The other condition
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT",
        args = "Condition",
        to = "CombinedPredicate"
    )
    Condition orNot(Field<Boolean> other);

    /**
     * Combine this condition with an EXISTS clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition orExists(Select<?> select);

    /**
     * Combine this condition with a NOT EXIST clause using the
     * {@link Operator#OR} operator.
     *
     * @param select The EXISTS's subquery
     * @return The combined condition
     */
    @Support
    @Transition(
        name = "OR NOT EXISTS",
        args = "Select",
        to = "CombinedPredicate"
    )
    Condition orNotExists(Select<?> select);

    /**
     * Invert this condition
     * <p>
     * This is the same as calling {@link DSL#not(Condition)}
     *
     * @return This condition, inverted
     */
    @Support
    @Transition(
        name = "NOT",
        to = "CombinedPredicate"
    )
    Condition not();
}
