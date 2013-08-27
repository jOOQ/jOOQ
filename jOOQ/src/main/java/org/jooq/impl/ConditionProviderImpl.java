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

package org.jooq.impl;

import static org.jooq.impl.DSL.trueCondition;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.ConditionProvider;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
class ConditionProviderImpl extends AbstractQueryPart implements ConditionProvider, Condition {

    private static final long serialVersionUID = 6073328960551062973L;

    private Condition         condition;

    ConditionProviderImpl() {
    }

    final Condition getWhere() {
        if (condition == null) {
            return trueCondition();
        }

        return condition;
    }

    // -------------------------------------------------------------------------
    // ConditionProvider API
    // -------------------------------------------------------------------------

    @Override
    public final void addConditions(Condition... conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Collection<Condition> conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        addConditions(operator, Arrays.asList(conditions));
    }

    @Override
    public final void addConditions(Operator operator, Collection<Condition> conditions) {
        if (!conditions.isEmpty()) {
            Condition c;

            if (conditions.size() == 1) {
                c = conditions.iterator().next();
            }
            else {
                c = new CombinedCondition(operator, conditions);
            }

            if (getWhere() instanceof TrueCondition) {
                condition = c;
            }
            else {
                condition = new CombinedCondition(operator, Arrays.asList(getWhere(), c));
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(getWhere());
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(getWhere());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Condition API
    // -------------------------------------------------------------------------

    @Override
    public final Condition and(Condition other) {
        return getWhere().and(other);
    }

    @Override
    public final Condition and(Field<Boolean> other) {
        return getWhere().and(other);
    }

    @Override
    public final Condition and(String sql) {
        return getWhere().and(sql);
    }

    @Override
    public final Condition and(String sql, Object... bindings) {
        return getWhere().and(sql, bindings);
    }

    @Override
    public final Condition and(String sql, QueryPart... parts) {
        return getWhere().and(sql, parts);
    }

    @Override
    public final Condition andNot(Condition other) {
        return getWhere().andNot(other);
    }

    @Override
    public final Condition andNot(Field<Boolean> other) {
        return getWhere().andNot(other);
    }

    @Override
    public final Condition andExists(Select<?> select) {
        return getWhere().andExists(select);
    }

    @Override
    public final Condition andNotExists(Select<?> select) {
        return getWhere().andNotExists(select);
    }

    @Override
    public final Condition or(Condition other) {
        return getWhere().or(other);
    }

    @Override
    public final Condition or(Field<Boolean> other) {
        return getWhere().or(other);
    }

    @Override
    public final Condition or(String sql) {
        return getWhere().or(sql);
    }

    @Override
    public final Condition or(String sql, Object... bindings) {
        return getWhere().or(sql, bindings);
    }

    @Override
    public final Condition or(String sql, QueryPart... parts) {
        return getWhere().or(sql, parts);
    }

    @Override
    public final Condition orNot(Condition other) {
        return getWhere().orNot(other);
    }

    @Override
    public final Condition orNot(Field<Boolean> other) {
        return getWhere().orNot(other);
    }

    @Override
    public final Condition orExists(Select<?> select) {
        return getWhere().orExists(select);
    }

    @Override
    public final Condition orNotExists(Select<?> select) {
        return getWhere().orNotExists(select);
    }

    @Override
    public final Condition not() {
        return getWhere().not();
    }
}
