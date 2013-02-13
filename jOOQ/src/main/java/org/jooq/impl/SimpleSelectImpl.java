/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.condition;
import static org.jooq.impl.Factory.exists;
import static org.jooq.impl.Factory.notExists;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.SimpleSelectForUpdateOfStep;
import org.jooq.SimpleSelectOffsetStep;
import org.jooq.SimpleSelectQuery;
import org.jooq.SimpleSelectWhereStep;
import org.jooq.SortField;
import org.jooq.Table;

/**
 * A wrapper for a {@link SelectQuery}
 *
 * @author Lukas Eder
 */
class SimpleSelectImpl<R extends Record> extends AbstractDelegatingSelect<R>
    implements

    // Cascading interface implementations for typed Select behaviour
    SimpleSelectWhereStep<R>,
    SimpleSelectConditionStep<R>,
    SimpleSelectOffsetStep<R>,
    SimpleSelectForUpdateOfStep<R> {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = -5425308887382166448L;

    /**
     * The limit that has been added in a limit(int).offset(int) construct
     */
    private transient Integer        limit;
    private transient Param<Integer> limitParam;

    SimpleSelectImpl(Configuration configuration) {
        this(new SimpleSelectQueryImpl<R>(configuration));
    }

    SimpleSelectImpl(Configuration configuration, Table<R> table) {
        this(configuration, table, false);
    }

    SimpleSelectImpl(Configuration configuration, Table<R> table, boolean distinct) {
        this(new SimpleSelectQueryImpl<R>(configuration, table, distinct));
    }

    SimpleSelectImpl(Select<R> query) {
        super(query);
    }

    @Override
    public final SimpleSelectQuery<R> getQuery() {
        return (SimpleSelectQuery<R>) getDelegate();
    }

    @Override
    public final SimpleSelectImpl<R> where(Condition... conditions) {
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> where(Collection<Condition> conditions) {
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final SimpleSelectImpl<R> where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final SimpleSelectImpl<R> where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final SimpleSelectImpl<R> whereExists(Select<?> select) {
        return andExists(select);
    }

    @Override
    public final SimpleSelectImpl<R> whereNotExists(Select<?> select) {
        return andNotExists(select);
    }

    @Override
    public final SimpleSelectImpl<R> and(Condition condition) {
        getQuery().addConditions(condition);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final SimpleSelectImpl<R> and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final SimpleSelectImpl<R> and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final SimpleSelectImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final SimpleSelectImpl<R> andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final SimpleSelectImpl<R> andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final SimpleSelectImpl<R> or(Condition condition) {
        getQuery().addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final SimpleSelectImpl<R> or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final SimpleSelectImpl<R> or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final SimpleSelectImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final SimpleSelectImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final SimpleSelectImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final SimpleSelectImpl<R> orderBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderBy(Collection<SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderSiblingsBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderSiblingsBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderSiblingsBy(Collection<SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> orderSiblingsBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(int numberOfRows) {
        this.limit = numberOfRows;
        this.limitParam = null;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(Param<Integer> numberOfRows) {
        this.limit = null;
        this.limitParam = numberOfRows;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(int offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(int offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(Param<Integer> offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> limit(Param<Integer> offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> offset(int offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SimpleSelectImpl<R> offset(Param<Integer> offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SimpleSelectImpl<R> forUpdate() {
        getQuery().setForUpdate(true);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> of(Field<?>... fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> of(Collection<Field<?>> fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> of(Table<?>... tables) {
        getQuery().setForUpdateOf(tables);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> wait(int seconds) {
        getQuery().setForUpdateWait(seconds);
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> noWait() {
        getQuery().setForUpdateNoWait();
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> skipLocked() {
        getQuery().setForUpdateSkipLocked();
        return this;
    }

    @Override
    public final SimpleSelectImpl<R> forShare() {
        getQuery().setForShare(true);
        return this;
    }

    @Override
    public final Select<R> union(Select<R> select) {
        return new SimpleSelectImpl<R>(getDelegate().union(select));
    }

    @Override
    public final Select<R> unionAll(Select<R> select) {
        return new SimpleSelectImpl<R>(getDelegate().unionAll(select));
    }

    @Override
    public final Select<R> except(Select<R> select) {
        return new SimpleSelectImpl<R>(getDelegate().except(select));
    }

    @Override
    public final Select<R> intersect(Select<R> select) {
        return new SimpleSelectImpl<R>(getDelegate().intersect(select));
    }
}
