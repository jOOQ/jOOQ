/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.TableLike;
import org.jooq.TableOnConditionStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOptions;
import org.jooq.TableOuterJoinStep;
import org.jooq.TablePartitionByStep;
import org.jooq.impl.QOM.JoinHint;

/**
 * A base implementation for actual join tables and dummy join tables.
 *
 * @author Lukas Eder
 */
abstract class AbstractJoinTable<J extends AbstractJoinTable<J>>
extends
    AbstractTable<Record>
implements
    TableOuterJoinStep<Record>,
    TableOptionalOnStep<Record>,
    TablePartitionByStep<Record>,
    TableOnConditionStep<Record>
{
    AbstractJoinTable(TableOptions options, Name name) {
        super(options, name);
    }

    // ------------------------------------------------------------------------
    // Join API
    // ------------------------------------------------------------------------















    abstract J partitionBy0(Collection<? extends Field<?>> fields);

    @Override
    public abstract J on(Condition conditions);

    @Override
    public abstract J on(Condition... conditions);

    @Override
    public final J on(Field<Boolean> c) {
        return on(condition(c));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(SQL sql) {
        and(sql);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql) {
        and(sql);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql, Object... bindings) {
        and(sql, bindings);
        return (J) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final J on(String sql, QueryPart... parts) {
        and(sql, parts);
        return (J) this;
    }

    @Override
    public final J using(Field<?>... fields) {
        return using(asList(fields));
    }

    @Override
    public abstract J using(Collection<? extends Field<?>> fields);

    @Override
    public abstract J and(Condition c);

    @Override
    public final J and(Field<Boolean> c) {
        return and(condition(c));
    }

    @Override
    public final J and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final J and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final J and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final J and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final J andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final J andNot(Field<Boolean> c) {
        return andNot(condition(c));
    }

    @Override
    public final J andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final J andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public abstract J or(Condition c);

    @Override
    public final J or(Field<Boolean> c) {
        return or(condition(c));
    }

    @Override
    public final J or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final J or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final J or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final J or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final J orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final J orNot(Field<Boolean> c) {
        return orNot(condition(c));
    }

    @Override
    public final J orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final J orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    // [#14906] Re-declare internal-type-returning method here, to prevent J
    //          from leaking into client code.
    @SuppressWarnings("unchecked")
    @Override
    public final J join(TableLike<?> table, JoinType type) {
        return (J) super.join(table, type);
    }

    // [#14906] Re-declare internal-type-returning method here, to prevent J
    //          from leaking into client code.
    @SuppressWarnings("unchecked")
    @Override
    public final J join(TableLike<?> table, JoinType type, JoinHint hint) {
        return (J) super.join(table, type, hint);
    }

}
