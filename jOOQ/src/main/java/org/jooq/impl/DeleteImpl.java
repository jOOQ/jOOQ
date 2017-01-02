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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.Collection;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteResultStep;
import org.jooq.DeleteWhereStep;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DeleteImpl<R extends Record>
    extends AbstractDelegatingQuery<DeleteQueryImpl<R>>
    implements

    // Cascading interface implementations for Delete behaviour
    DeleteWhereStep<R>,
    DeleteConditionStep<R>,
    DeleteResultStep<R> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 2747566322757517382L;

    DeleteImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(new DeleteQueryImpl<R>(configuration, with, table));
    }

    @Override
    public final DeleteImpl<R> where(Condition... conditions) {
        getDelegate().addConditions(conditions);
        return this;
    }

    @Override
    public final DeleteImpl<R> where(Collection<? extends Condition> conditions) {
        getDelegate().addConditions(conditions);
        return this;
    }

    @Override
    public final DeleteImpl<R> where(Field<Boolean> condition) {
        return where(condition(condition));
    }

    @Override
    @Deprecated
    public final DeleteImpl<R> where(Boolean condition) {
        return where(condition(condition));
    }

    @Override
    public final DeleteImpl<R> where(SQL sql) {
        return where(condition(sql));
    }

    @Override
    public final DeleteImpl<R> where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final DeleteImpl<R> where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final DeleteImpl<R> where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final DeleteImpl<R> whereExists(Select<?> select) {
        return andExists(select);
    }

    @Override
    public final DeleteImpl<R> whereNotExists(Select<?> select) {
        return andNotExists(select);
    }

    @Override
    public final DeleteImpl<R> and(Condition condition) {
        getDelegate().addConditions(condition);
        return this;
    }

    @Override
    public final DeleteImpl<R> and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    @Deprecated
    public final DeleteImpl<R> and(Boolean condition) {
        return and(condition(condition));
    }

    @Override
    public final DeleteImpl<R> and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final DeleteImpl<R> and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final DeleteImpl<R> and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final DeleteImpl<R> and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final DeleteImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final DeleteImpl<R> andNot(Field<Boolean> condition) {
        return andNot(condition(condition));
    }

    @Override
    @Deprecated
    public final DeleteImpl<R> andNot(Boolean condition) {
        return andNot(condition(condition));
    }

    @Override
    public final DeleteImpl<R> andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final DeleteImpl<R> andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final DeleteImpl<R> or(Condition condition) {
        getDelegate().addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final DeleteImpl<R> or(Field<Boolean> condition) {
        return or(condition(condition));
    }

    @Override
    @Deprecated
    public final DeleteImpl<R> or(Boolean condition) {
        return or(condition(condition));
    }

    @Override
    public final DeleteImpl<R> or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final DeleteImpl<R> or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final DeleteImpl<R> or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final DeleteImpl<R> or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final DeleteImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final DeleteImpl<R> orNot(Field<Boolean> condition) {
        return orNot(condition(condition));
    }

    @Override
    @Deprecated
    public final DeleteImpl<R> orNot(Boolean condition) {
        return orNot(condition(condition));
    }

    @Override
    public final DeleteImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final DeleteImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final DeleteImpl<R> returning() {
        getDelegate().setReturning();
        return this;
    }

    @Override
    public final DeleteImpl<R> returning(Field<?>... f) {
        getDelegate().setReturning(f);
        return this;
    }

    @Override
    public final DeleteImpl<R> returning(Collection<? extends Field<?>> f) {
        getDelegate().setReturning(f);
        return this;
    }

    @Override
    public final Result<R> fetch() {
        getDelegate().execute();
        return getDelegate().getReturnedRecords();
    }

    @Override
    public final R fetchOne() {
        getDelegate().execute();
        return getDelegate().getReturnedRecord();
    }


    @Override
    public final Optional<R> fetchOptional() {
        return Optional.ofNullable(fetchOne());
    }

}
