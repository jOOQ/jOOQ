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

import static org.jooq.Clause.CONDITION;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
abstract class AbstractCondition extends AbstractQueryPart implements Condition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -6683692251799468624L;
    private static final Clause[] CLAUSES          = { CONDITION };

    AbstractCondition() {}

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Condition and(Condition other) {
        return DSL.and(this, other);
    }

    /*
     * Subclasses may override this implementation when implementing
     * A BETWEEN B AND C
     */
    @Override
    public Condition and(Field<Boolean> other) {
        return and(condition(other));
    }

    @Override
    public Condition and(Boolean other) {
        return and(condition(other));
    }

    @Override
    public final Condition or(Condition other) {
        return DSL.or(this, other);
    }

    @Override
    public final Condition or(Field<Boolean> other) {
        return or(condition(other));
    }

    @Override
    public final Condition or(Boolean other) {
        return or(condition(other));
    }

    @Override
    public final Condition and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final Condition and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final Condition and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final Condition and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final Condition or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final Condition or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final Condition or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final Condition or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final Condition andNot(Condition other) {
        return and(other.not());
    }

    @Override
    public final Condition andNot(Field<Boolean> other) {
        return andNot(condition(other));
    }

    @Override
    public final Condition andNot(Boolean other) {
        return andNot(condition(other));
    }

    @Override
    public final Condition orNot(Condition other) {
        return or(other.not());
    }

    @Override
    public final Condition orNot(Field<Boolean> other) {
        return orNot(condition(other));
    }

    @Override
    public final Condition orNot(Boolean other) {
        return orNot(condition(other));
    }

    @Override
    public final Condition andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final Condition andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final Condition orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final Condition orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final Condition not() {
        return new NotCondition(this);
    }
}
