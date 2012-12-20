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
import static org.jooq.impl.Factory.table;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.SelectConnectByConditionStep;
import org.jooq.SelectForUpdateOfStep;
import org.jooq.SelectHavingConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOffsetStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.SelectOptionalOnStep;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.exception.DataAccessException;

/**
 * A wrapper for a {@link SelectQuery}
 *
 * @author Lukas Eder
 */
class SelectImpl<R extends Record> extends AbstractDelegatingSelect<R> implements

    // Cascading interface implementations for Select behaviour
    SelectSelectStep<R>,
    SelectOptionalOnStep<R>,
    SelectOnConditionStep<R>,
    SelectConditionStep<R>,
    SelectConnectByConditionStep<R>,
    SelectHavingConditionStep<R>,
    SelectOffsetStep<R>,
    SelectForUpdateOfStep<R> {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = -5425308887382166448L;

    /**
     * A temporary member holding a join table
     */
    private transient TableLike<?>          joinTable;

    /**
     * A temporary member holding a join partition by expression
     */
    private transient Field<?>[]            joinPartitionBy;

    /**
     * A temporary member holding a join type
     */
    private transient JoinType              joinType;

    /**
     * A temporary member holding a join condition
     */
    private transient ConditionProviderImpl joinConditions;

    /**
     * The step that is currently receiving new conditions
     */
    private transient ConditionStep         conditionStep;

    /**
     * The limit that has been added in a limit(int).offset(int) construct
     */
    private transient Integer               limit;
    private transient Param<Integer>        limitParam;

    SelectImpl(Configuration configuration) {
        this(configuration, false);
    }

    @SuppressWarnings("unchecked")
    SelectImpl(Configuration configuration, boolean distinct) {
        this((Select<R>) new SelectQueryImpl(configuration, distinct));
    }

    SelectImpl(Select<R> query) {
        super(query);
    }

    @Override
    public final SelectQuery getQuery() {
        return (SelectQuery) getDelegate();
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record> and SelectSelectStep&lt;R>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final SelectImpl select(Field<?>... fields) {
        getQuery().addSelect(fields);
        return this;
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record> and SelectSelectStep&lt;R>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final SelectImpl select(Collection<? extends Field<?>> fields) {
        getQuery().addSelect(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> hint(String hint) {
        getQuery().addHint(hint);
        return this;
    }

    @Override
    public final SelectImpl<R> from(TableLike<?>... tables) {
        getQuery().addFrom(tables);
        return this;
    }

    @Override
    public final SelectImpl<R> from(Collection<? extends TableLike<?>> tables) {
        getQuery().addFrom(tables);
        return this;
    }

    @Override
    public final SelectImpl<R> from(String sql) {
        return from(table(sql));
    }

    @Override
    public final SelectImpl<R> from(String sql, Object... bindings) {
        return from(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> from(String sql, QueryPart... parts) {
        return from(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> where(Condition... conditions) {
        conditionStep = ConditionStep.WHERE;
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl<R> where(Collection<Condition> conditions) {
        conditionStep = ConditionStep.WHERE;
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl<R> where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final SelectImpl<R> where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> whereExists(Select<?> select) {
        conditionStep = ConditionStep.WHERE;
        return andExists(select);
    }

    @Override
    public final SelectImpl<R> whereNotExists(Select<?> select) {
        conditionStep = ConditionStep.WHERE;
        return andNotExists(select);
    }

    @Override
    public final SelectImpl<R> and(Condition condition) {
        switch (conditionStep) {
            case WHERE:
                getQuery().addConditions(condition);
                break;
            case CONNECT_BY:
                getQuery().addConnectBy(condition);
                break;
            case HAVING:
                getQuery().addHaving(condition);
                break;
            case ON:
                joinConditions.addConditions(condition);
                break;
        }

        return this;
    }

    @Override
    public final SelectImpl<R> and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final SelectImpl<R> and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final SelectImpl<R> andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final SelectImpl<R> andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final SelectImpl<R> or(Condition condition) {
        switch (conditionStep) {
            case WHERE:
                getQuery().addConditions(Operator.OR, condition);
                break;
            case CONNECT_BY:
                throw new IllegalStateException("Cannot connect conditions for the CONNECT BY clause using the OR operator");
            case HAVING:
                getQuery().addHaving(Operator.OR, condition);
                break;
            case ON:
                joinConditions.addConditions(Operator.OR, condition);
                break;
        }

        return this;
    }

    @Override
    public final SelectImpl<R> or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final SelectImpl<R> or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final SelectImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final SelectImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final SelectImpl<R> connectBy(Condition condition) {
        conditionStep = ConditionStep.CONNECT_BY;
        getQuery().addConnectBy(condition);
        return this;
    }

    @Override
    public final SelectImpl<R> connectBy(String sql) {
        return connectBy(condition(sql));
    }

    @Override
    public final SelectImpl<R> connectBy(String sql, Object... bindings) {
        return connectBy(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> connectBy(String sql, QueryPart... parts) {
        return connectBy(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> connectByNoCycle(Condition condition) {
        conditionStep = ConditionStep.CONNECT_BY;
        getQuery().addConnectByNoCycle(condition);
        return this;
    }

    @Override
    public final SelectImpl<R> connectByNoCycle(String sql) {
        return connectByNoCycle(condition(sql));
    }

    @Override
    public final SelectImpl<R> connectByNoCycle(String sql, Object... bindings) {
        return connectByNoCycle(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> connectByNoCycle(String sql, QueryPart... parts) {
        return connectByNoCycle(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> startWith(Condition condition) {
        getQuery().setConnectByStartWith(condition);
        return this;
    }

    @Override
    public final SelectImpl<R> startWith(String sql) {
        return startWith(condition(sql));
    }

    @Override
    public final SelectImpl<R> startWith(String sql, Object... bindings) {
        return startWith(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> startWith(String sql, QueryPart... parts) {
        return startWith(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> groupBy(GroupField... fields) {
        getQuery().addGroupBy(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> groupBy(Collection<? extends GroupField> fields) {
        getQuery().addGroupBy(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> orderBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> orderBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> orderBy(Collection<SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> orderBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        return this;
    }

    @Override
    public final SelectImpl<R> orderSiblingsBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl<R> orderSiblingsBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl<R> orderSiblingsBy(Collection<SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl<R> orderSiblingsBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(int numberOfRows) {
        this.limit = numberOfRows;
        this.limitParam = null;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(Param<Integer> numberOfRows) {
        this.limit = null;
        this.limitParam = numberOfRows;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(int offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(int offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(Param<Integer> offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> limit(Param<Integer> offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl<R> offset(int offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SelectImpl<R> offset(Param<Integer> offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SelectImpl<R> forUpdate() {
        getQuery().setForUpdate(true);
        return this;
    }

    @Override
    public final SelectImpl<R> of(Field<?>... fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> of(Collection<Field<?>> fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SelectImpl<R> of(Table<?>... tables) {
        getQuery().setForUpdateOf(tables);
        return this;
    }

    @Override
    public final SelectImpl<R> wait(int seconds) {
        getQuery().setForUpdateWait(seconds);
        return this;
    }

    @Override
    public final SelectImpl<R> noWait() {
        getQuery().setForUpdateNoWait();
        return this;
    }

    @Override
    public final SelectImpl<R> skipLocked() {
        getQuery().setForUpdateSkipLocked();
        return this;
    }

    @Override
    public final SelectImpl<R> forShare() {
        getQuery().setForShare(true);
        return this;
    }

    @Override
    public final SelectImpl<R> union(Select<? extends R> select) {
        return new SelectImpl<R>(getDelegate().union(select));
    }

    @Override
    public final SelectImpl<R> unionAll(Select<? extends R> select) {
        return new SelectImpl<R>(getDelegate().unionAll(select));
    }

    @Override
    public final SelectImpl<R> except(Select<? extends R> select) {
        return new SelectImpl<R>(getDelegate().except(select));
    }

    @Override
    public final SelectImpl<R> intersect(Select<? extends R> select) {
        return new SelectImpl<R>(getDelegate().intersect(select));
    }

    @Override
    public final SelectImpl<R> having(Condition... conditions) {
        conditionStep = ConditionStep.HAVING;
        getQuery().addHaving(conditions);
        return this;
    }

    @Override
    public final SelectImpl<R> having(Collection<Condition> conditions) {
        conditionStep = ConditionStep.HAVING;
        getQuery().addHaving(conditions);
        return this;
    }

    @Override
    public final SelectImpl<R> having(String sql) {
        return having(condition(sql));
    }

    @Override
    public final SelectImpl<R> having(String sql, Object... bindings) {
        return having(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> having(String sql, QueryPart... parts) {
        return having(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> on(Condition... conditions) {
        conditionStep = ConditionStep.ON;
        joinConditions = new ConditionProviderImpl();
        joinConditions.addConditions(conditions);
        getQuery().addJoin(joinTable, joinType, new Condition[] { joinConditions }, joinPartitionBy);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl<R> on(String sql) {
        return on(condition(sql));
    }

    @Override
    public final SelectImpl<R> on(String sql, Object... bindings) {
        return on(condition(sql, bindings));
    }

    @Override
    public final SelectImpl<R> on(String sql, QueryPart... parts) {
        return on(condition(sql, parts));
    }

    @Override
    public final SelectImpl<R> onKey() throws DataAccessException {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl<R> onKey(TableField<?, ?>... keyFields) throws DataAccessException {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType, keyFields);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl<R> onKey(ForeignKey<?, ?> key) {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType, key);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;

    }

    @Override
    public final SelectImpl<R> using(Field<?>... fields) {
        return using(Arrays.asList(fields));
    }

    @Override
    public final SelectImpl<R> using(Collection<? extends Field<?>> fields) {
        getQuery().addJoinUsing(joinTable, joinType, fields);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl<R> join(TableLike<?> table) {
        return join(table, JoinType.JOIN);
    }

    @Override
    public final SelectImpl<R> leftOuterJoin(TableLike<?> table) {
        return join(table, JoinType.LEFT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl<R> rightOuterJoin(TableLike<?> table) {
        return join(table, JoinType.RIGHT_OUTER_JOIN);
    }

    @Override
    public final SelectOnStep<R> fullOuterJoin(TableLike<?> table) {
        return join(table, JoinType.FULL_OUTER_JOIN);
    }

    @Override
    public final SelectImpl<R> join(TableLike<?> table, JoinType type) {
        switch (type) {
            case CROSS_JOIN:
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN: {
                getQuery().addJoin(table, type);
                joinTable = null;
                joinPartitionBy = null;
                joinType = null;

                return this;
            }

            default: {
                conditionStep = ConditionStep.ON;
                joinTable = table;
                joinType = type;
                joinPartitionBy = null;
                joinConditions = null;

                return this;
            }
        }
    }

    @Override
    public final SelectJoinStep<R> crossJoin(TableLike<?> table) {
        return join(table, JoinType.CROSS_JOIN);
    }

    @Override
    public final SelectImpl<R> naturalJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_JOIN);
    }

    @Override
    public final SelectImpl<R> naturalLeftOuterJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_LEFT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl<R> naturalRightOuterJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_RIGHT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl<R> join(String sql) {
        return join(table(sql));
    }

    @Override
    public final SelectImpl<R> join(String sql, Object... bindings) {
        return join(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> join(String sql, QueryPart... parts) {
        return join(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl<R> leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> leftOuterJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl<R> rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> rightOuterJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectOnStep<R> fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final SelectOnStep<R> fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectOnStep<R> fullOuterJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectJoinStep<R> crossJoin(String sql) {
        return crossJoin(table(sql));
    }

    @Override
    public final SelectJoinStep<R> crossJoin(String sql, Object... bindings) {
        return crossJoin(table(sql, bindings));
    }

    @Override
    public final SelectJoinStep<R> crossJoin(String sql, QueryPart... parts) {
        return crossJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> naturalJoin(String sql) {
        return naturalJoin(table(sql));
    }

    @Override
    public final SelectImpl<R> naturalJoin(String sql, Object... bindings) {
        return naturalJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> naturalJoin(String sql, QueryPart... parts) {
        return naturalJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> naturalLeftOuterJoin(String sql) {
        return naturalLeftOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl<R> naturalLeftOuterJoin(String sql, Object... bindings) {
        return naturalLeftOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> naturalLeftOuterJoin(String sql, QueryPart... parts) {
        return naturalLeftOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> naturalRightOuterJoin(String sql) {
        return naturalRightOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl<R> naturalRightOuterJoin(String sql, Object... bindings) {
        return naturalRightOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl<R> naturalRightOuterJoin(String sql, QueryPart... parts) {
        return naturalRightOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl<R> partitionBy(Field<?>... fields) {
        joinPartitionBy = fields;
        return this;
    }

    @Override
    public final SelectImpl<R> partitionBy(Collection<? extends Field<?>> fields) {
        return partitionBy(fields.toArray(new Field[fields.size()]));
    }

    /**
     * The {@link SelectImpl} current condition step
     * <p>
     * This enumeration models the step that is currently receiving new
     * conditions via the {@link SelectImpl#and(Condition)},
     * {@link SelectImpl#or(Condition)}, etc methods
     *
     * @author Lukas Eder
     */
    private static enum ConditionStep {

        /**
         * Additional conditions go to the <code>JOIN</code> clause that is
         * currently being added.
         */
        ON,

        /**
         * Additional conditions go to the <code>WHERE</code> clause that is
         * currently being added.
         */
        WHERE,

        /**
         * Additional conditions go to the <code>CONNECT BY</code> clause that
         * is currently being added.
         */
        CONNECT_BY,

        /**
         * Additional conditions go to the <code>HAVING</code> clause that is
         * currently being added.
         */
        HAVING
    }
}
