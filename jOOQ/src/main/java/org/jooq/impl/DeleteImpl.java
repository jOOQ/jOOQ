/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
    public final DeleteImpl<R> where(Collection<Condition> conditions) {
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
