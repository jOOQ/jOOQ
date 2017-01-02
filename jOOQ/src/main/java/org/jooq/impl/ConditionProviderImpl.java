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

package org.jooq.impl;

import static org.jooq.impl.DSL.trueCondition;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.ConditionProvider;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
final class ConditionProviderImpl extends AbstractQueryPart implements ConditionProvider, Condition {

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
    public final void addConditions(Collection<? extends Condition> conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        addConditions(operator, Arrays.asList(conditions));
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        if (!conditions.isEmpty()) {
            Condition c;

            if (conditions.size() == 1) {
                c = conditions.iterator().next();
            }
            else {
                c = DSL.condition(operator, conditions);
            }

            if (getWhere() instanceof TrueCondition) {
                condition = c;
            }
            else {
                condition = DSL.condition(operator, getWhere(), c);
            }
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getWhere());
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
    public final Condition and(Boolean other) {
        return getWhere().and(other);
    }

    @Override
    public final Condition and(SQL sql) {
        return getWhere().and(sql);
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
    public final Condition andNot(Boolean other) {
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
    public final Condition or(Boolean other) {
        return getWhere().or(other);
    }

    @Override
    public final Condition or(SQL sql) {
        return getWhere().or(sql);
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
    public final Condition orNot(Boolean other) {
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
