/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class DeleteImpl<R extends Record>
    extends AbstractDelegatingQuery<DeleteQueryImpl<R>>
    implements

    // Cascading interface implementations for Delete behaviour
    DeleteWhereStep<R>,
    DeleteConditionStep<R> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = 2747566322757517382L;

    DeleteImpl(Configuration configuration, Table<R> table) {
        super(new DeleteQueryImpl<R>(configuration, table));
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
    public final DeleteImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final DeleteImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }
}
