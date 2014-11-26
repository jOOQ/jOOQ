/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.table;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.annotation.Generated;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.SelectConnectByConditionStep;
import org.jooq.SelectFinalStep;
import org.jooq.SelectForUpdateOfStep;
import org.jooq.SelectHavingConditionStep;
import org.jooq.SelectIntoStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOffsetStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.SelectOptionalOnStep;
import org.jooq.SelectQuery;
import org.jooq.SelectSeekLimitStep;
import org.jooq.SelectSeekStep1;
import org.jooq.SelectSeekStep10;
import org.jooq.SelectSeekStep11;
import org.jooq.SelectSeekStep12;
import org.jooq.SelectSeekStep13;
import org.jooq.SelectSeekStep14;
import org.jooq.SelectSeekStep15;
import org.jooq.SelectSeekStep16;
import org.jooq.SelectSeekStep17;
import org.jooq.SelectSeekStep18;
import org.jooq.SelectSeekStep19;
import org.jooq.SelectSeekStep2;
import org.jooq.SelectSeekStep20;
import org.jooq.SelectSeekStep21;
import org.jooq.SelectSeekStep22;
import org.jooq.SelectSeekStep3;
import org.jooq.SelectSeekStep4;
import org.jooq.SelectSeekStep5;
import org.jooq.SelectSeekStep6;
import org.jooq.SelectSeekStep7;
import org.jooq.SelectSeekStep8;
import org.jooq.SelectSeekStep9;
import org.jooq.SelectSeekStepN;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.WindowDefinition;
import org.jooq.exception.DataAccessException;

/**
 * A wrapper for a {@link SelectQuery}
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
class SelectImpl<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractDelegatingQuery<Select<R>> implements

    // Cascading interface implementations for Select behaviour
    SelectSelectStep<R>,
    SelectOptionalOnStep<R>,
    SelectOnConditionStep<R>,
    SelectConditionStep<R>,
    SelectConnectByConditionStep<R>,
    SelectHavingConditionStep<R>,
    // [jooq-tools] START [implements-select-seek-step]
    SelectSeekStep1<R, T1>,
    SelectSeekStep2<R, T1, T2>,
    SelectSeekStep3<R, T1, T2, T3>,
    SelectSeekStep4<R, T1, T2, T3, T4>,
    SelectSeekStep5<R, T1, T2, T3, T4, T5>,
    SelectSeekStep6<R, T1, T2, T3, T4, T5, T6>,
    SelectSeekStep7<R, T1, T2, T3, T4, T5, T6, T7>,
    SelectSeekStep8<R, T1, T2, T3, T4, T5, T6, T7, T8>,
    SelectSeekStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    SelectSeekStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    SelectSeekStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    SelectSeekStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    SelectSeekStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    SelectSeekStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    SelectSeekStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    SelectSeekStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    SelectSeekStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    SelectSeekStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    SelectSeekStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    SelectSeekStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    SelectSeekStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    SelectSeekStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,

// [jooq-tools] END [implements-select-seek-step]
    SelectSeekStepN<R>,
    SelectSeekLimitStep<R>,
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

    SelectImpl(WithImpl with, Configuration configuration) {
        this(with, configuration, false);
    }

    SelectImpl(WithImpl with, Configuration configuration, boolean distinct) {
        this(new SelectQueryImpl<R>(with, configuration, distinct));
    }

    SelectImpl(Select<R> query) {
        super(query);
    }

    @Override
    public final SelectQuery<R> getQuery() {
        return (SelectQuery<R>) getDelegate();
    }

    @Override
    @Deprecated
    public final int fetchCount() {
        return getDelegate().fetchCount();
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record> and SelectSelectStep&lt;R>
     */
    @Override
    public final SelectImpl select(Field<?>... fields) {
        getQuery().addSelect(fields);
        return this;
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record> and SelectSelectStep&lt;R>
     */
    @Override
    public final SelectImpl select(Collection<? extends Field<?>> fields) {
        getQuery().addSelect(fields);
        return this;
    }

    @Override
    public final SelectIntoStep<R> on(Field<?>... fields) {
        return distinctOn(Arrays.asList(fields));
    }

    @Override
    public final SelectIntoStep<R> on(Collection<? extends Field<?>> fields) {
        return distinctOn(fields);
    }

    @Override
    public final SelectIntoStep<R> distinctOn(Field<?>... fields) {
        getQuery().addDistinctOn(fields);
        return this;
    }

    @Override
    public final SelectIntoStep<R> distinctOn(Collection<? extends Field<?>> fields) {
        getQuery().addDistinctOn(fields);
        return this;
    }

    @Override
    public final SelectImpl into(Table<?> table) {
        getQuery().setInto(table);
        return this;
    }

    @Override
    public final SelectImpl hint(String hint) {
        getQuery().addHint(hint);
        return this;
    }

    @Override
    public final SelectImpl option(String hint) {
        getQuery().addOption(hint);
        return this;
    }

    @Override
    public final SelectImpl from(TableLike<?> table) {
        getQuery().addFrom(table);
        return this;
    }

    @Override
    public final SelectImpl from(TableLike<?>... tables) {
        getQuery().addFrom(tables);
        return this;
    }

    @Override
    public final SelectImpl from(Collection<? extends TableLike<?>> tables) {
        getQuery().addFrom(tables);
        return this;
    }

    @Override
    public final SelectImpl from(String sql) {
        return from(table(sql));
    }

    @Override
    public final SelectImpl from(String sql, Object... bindings) {
        return from(table(sql, bindings));
    }

    @Override
    public final SelectImpl from(String sql, QueryPart... parts) {
        return from(table(sql, parts));
    }

    @Override
    public final SelectImpl where(Condition... conditions) {
        conditionStep = ConditionStep.WHERE;
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl where(Collection<? extends Condition> conditions) {
        conditionStep = ConditionStep.WHERE;
        getQuery().addConditions(conditions);
        return this;
    }

    @Override
    public final SelectImpl where(Field<Boolean> condition) {
        return where(condition(condition));
    }

    @Override
    public final SelectImpl where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final SelectImpl where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final SelectImpl where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final SelectImpl whereExists(Select<?> select) {
        conditionStep = ConditionStep.WHERE;
        return andExists(select);
    }

    @Override
    public final SelectImpl whereNotExists(Select<?> select) {
        conditionStep = ConditionStep.WHERE;
        return andNotExists(select);
    }

    @Override
    public final SelectImpl and(Condition condition) {
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
    public final SelectImpl and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final SelectImpl and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final SelectImpl and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final SelectImpl and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final SelectImpl andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final SelectImpl andNot(Field<Boolean> condition) {
        return andNot(condition(condition));
    }

    @Override
    public final SelectImpl andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final SelectImpl andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final SelectImpl or(Condition condition) {
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
    public final SelectImpl or(Field<Boolean> condition) {
        return or(condition(condition));
    }

    @Override
    public final SelectImpl or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final SelectImpl or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final SelectImpl or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final SelectImpl orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final SelectImpl orNot(Field<Boolean> condition) {
        return orNot(condition(condition));
    }

    @Override
    public final SelectImpl orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final SelectImpl orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final SelectImpl connectBy(Condition condition) {
        conditionStep = ConditionStep.CONNECT_BY;
        getQuery().addConnectBy(condition);
        return this;
    }

    @Override
    public final SelectImpl connectBy(Field<Boolean> condition) {
        return connectBy(condition(condition));
    }

    @Override
    public final SelectImpl connectBy(String sql) {
        return connectBy(condition(sql));
    }

    @Override
    public final SelectImpl connectBy(String sql, Object... bindings) {
        return connectBy(condition(sql, bindings));
    }

    @Override
    public final SelectImpl connectBy(String sql, QueryPart... parts) {
        return connectBy(condition(sql, parts));
    }

    @Override
    public final SelectImpl connectByNoCycle(Condition condition) {
        conditionStep = ConditionStep.CONNECT_BY;
        getQuery().addConnectByNoCycle(condition);
        return this;
    }

    @Override
    public final SelectImpl connectByNoCycle(Field<Boolean> condition) {
        return connectByNoCycle(condition(condition));
    }

    @Override
    public final SelectImpl connectByNoCycle(String sql) {
        return connectByNoCycle(condition(sql));
    }

    @Override
    public final SelectImpl connectByNoCycle(String sql, Object... bindings) {
        return connectByNoCycle(condition(sql, bindings));
    }

    @Override
    public final SelectImpl connectByNoCycle(String sql, QueryPart... parts) {
        return connectByNoCycle(condition(sql, parts));
    }

    @Override
    public final SelectImpl startWith(Condition condition) {
        getQuery().setConnectByStartWith(condition);
        return this;
    }

    @Override
    public final SelectImpl startWith(Field<Boolean> condition) {
        return startWith(condition(condition));
    }

    @Override
    public final SelectImpl startWith(String sql) {
        return startWith(condition(sql));
    }

    @Override
    public final SelectImpl startWith(String sql, Object... bindings) {
        return startWith(condition(sql, bindings));
    }

    @Override
    public final SelectImpl startWith(String sql, QueryPart... parts) {
        return startWith(condition(sql, parts));
    }

    @Override
    public final SelectImpl groupBy(GroupField... fields) {
        getQuery().addGroupBy(fields);
        return this;
    }

    @Override
    public final SelectImpl groupBy(Collection<? extends GroupField> fields) {
        getQuery().addGroupBy(fields);
        return this;
    }

// [jooq-tools] START [order-by-field-array]

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep1 orderBy(Field t1) {
        return orderBy(new Field[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep2 orderBy(Field t1, Field t2) {
        return orderBy(new Field[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep3 orderBy(Field t1, Field t2, Field t3) {
        return orderBy(new Field[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep4 orderBy(Field t1, Field t2, Field t3, Field t4) {
        return orderBy(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep5 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep6 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep7 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep8 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep9 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep10 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep11 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep12 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep13 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep14 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep15 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep16 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep17 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep18 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep19 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep20 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep21 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep22 orderBy(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return orderBy(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

// [jooq-tools] END [order-by-field-array]

    @Override
    public final SelectImpl orderBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

// [jooq-tools] START [order-by-sortfield-array]

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep1 orderBy(SortField t1) {
        return orderBy(new SortField[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep2 orderBy(SortField t1, SortField t2) {
        return orderBy(new SortField[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep3 orderBy(SortField t1, SortField t2, SortField t3) {
        return orderBy(new SortField[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep4 orderBy(SortField t1, SortField t2, SortField t3, SortField t4) {
        return orderBy(new SortField[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep5 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep6 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep7 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep8 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep9 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep10 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep11 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep12 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep13 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep14 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep15 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep16 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep17 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep18 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17, SortField t18) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep19 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17, SortField t18, SortField t19) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep20 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17, SortField t18, SortField t19, SortField t20) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep21 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17, SortField t18, SortField t19, SortField t20, SortField t21) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekStep22 orderBy(SortField t1, SortField t2, SortField t3, SortField t4, SortField t5, SortField t6, SortField t7, SortField t8, SortField t9, SortField t10, SortField t11, SortField t12, SortField t13, SortField t14, SortField t15, SortField t16, SortField t17, SortField t18, SortField t19, SortField t20, SortField t21, SortField t22) {
        return orderBy(new SortField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

// [jooq-tools] END [order-by-sortfield-array]

    @Override
    public final SelectImpl orderBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl orderBy(Collection<? extends SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl orderBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        return this;
    }

    @Override
    public final SelectImpl orderSiblingsBy(Field<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl orderSiblingsBy(SortField<?>... fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl orderSiblingsBy(Collection<? extends SortField<?>> fields) {
        getQuery().addOrderBy(fields);
        getQuery().setOrderBySiblings(true);
        return this;
    }

    @Override
    public final SelectImpl orderSiblingsBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        getQuery().setOrderBySiblings(true);
        return this;
    }

// [jooq-tools] START [seek]

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1) {
        return seek(new Object[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1) {
        return seekBefore(new Object[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1) {
        return seekAfter(new Object[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2) {
        return seek(new Object[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2) {
        return seekBefore(new Object[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2) {
        return seekAfter(new Object[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3) {
        return seek(new Object[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3) {
        return seekBefore(new Object[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3) {
        return seekAfter(new Object[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4) {
        return seek(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4) {
        return seekBefore(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4) {
        return seekAfter(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seek(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1) {
        return seek(new Field[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1) {
        return seekBefore(new Field[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1) {
        return seekAfter(new Field[] { t1 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2) {
        return seek(new Field[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2) {
        return seekBefore(new Field[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2) {
        return seekAfter(new Field[] { t1, t2 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3) {
        return seek(new Field[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3) {
        return seekBefore(new Field[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3) {
        return seekAfter(new Field[] { t1, t2, t3 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4) {
        return seek(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4) {
        return seekBefore(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4) {
        return seekAfter(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seek(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Generated("This method was generated using jOOQ-tools")
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

// [jooq-tools] END [seek]

    @Override
    public final SelectSeekLimitStep<R> seek(Object... values) {
        getQuery().addSeekAfter(Utils.fields(values));
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field<?>... fields) {
        getQuery().addSeekAfter(fields);
        return this;
    }

    @Override
    public SelectSeekLimitStep<R> seekAfter(Object... values) {
        getQuery().addSeekAfter(Utils.fields(values));
        return this;
    }

    @Override
    public SelectSeekLimitStep<R> seekAfter(Field<?>... fields) {
        getQuery().addSeekAfter(fields);
        return this;
    }

    @Override
    public SelectSeekLimitStep<R> seekBefore(Object... values) {
        getQuery().addSeekBefore(Utils.fields(values));
        return this;
    }

    @Override
    public SelectSeekLimitStep<R> seekBefore(Field<?>... fields) {
        getQuery().addSeekBefore(fields);
        return this;
    }

    @Override
    public final SelectImpl limit(int numberOfRows) {
        this.limit = numberOfRows;
        this.limitParam = null;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl limit(Param<Integer> numberOfRows) {
        this.limit = null;
        this.limitParam = numberOfRows;
        getQuery().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl limit(int offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl limit(int offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl limit(Param<Integer> offset, int numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl limit(Param<Integer> offset, Param<Integer> numberOfRows) {
        getQuery().addLimit(offset, numberOfRows);
        return this;
    }

    @Override
    public final SelectImpl offset(int offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SelectImpl offset(Param<Integer> offset) {
        if (limit != null) {
            getQuery().addLimit(offset, limit);
        }
        else if (limitParam != null) {
            getQuery().addLimit(offset, limitParam);
        }

        return this;
    }

    @Override
    public final SelectImpl forUpdate() {
        getQuery().setForUpdate(true);
        return this;
    }

    @Override
    public final SelectImpl of(Field<?>... fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SelectImpl of(Collection<? extends Field<?>> fields) {
        getQuery().setForUpdateOf(fields);
        return this;
    }

    @Override
    public final SelectImpl of(Table<?>... tables) {
        getQuery().setForUpdateOf(tables);
        return this;
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxx xxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x

    xx [/pro] */
    @Override
    public final SelectImpl noWait() {
        getQuery().setForUpdateNoWait();
        return this;
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x

    xx [/pro] */
    @Override
    public final SelectImpl forShare() {
        getQuery().setForShare(true);
        return this;
    }

    /* [pro] xx
    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx x
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxx xxxxx
    x
    xx [/pro] */

    @Override
    public final SelectImpl union(Select<? extends R> select) {
        return new SelectImpl(getDelegate().union(select));
    }

    @Override
    public final SelectImpl unionAll(Select<? extends R> select) {
        return new SelectImpl(getDelegate().unionAll(select));
    }

    @Override
    public final SelectImpl except(Select<? extends R> select) {
        return new SelectImpl(getDelegate().except(select));
    }

    @Override
    public final SelectImpl intersect(Select<? extends R> select) {
        return new SelectImpl(getDelegate().intersect(select));
    }

    @Override
    public final SelectImpl having(Condition... conditions) {
        conditionStep = ConditionStep.HAVING;
        getQuery().addHaving(conditions);
        return this;
    }

    @Override
    public final SelectImpl having(Collection<? extends Condition> conditions) {
        conditionStep = ConditionStep.HAVING;
        getQuery().addHaving(conditions);
        return this;
    }

    @Override
    public final SelectImpl having(Field<Boolean> condition) {
        return having(condition(condition));
    }

    @Override
    public final SelectImpl having(String sql) {
        return having(condition(sql));
    }

    @Override
    public final SelectImpl having(String sql, Object... bindings) {
        return having(condition(sql, bindings));
    }

    @Override
    public final SelectImpl having(String sql, QueryPart... parts) {
        return having(condition(sql, parts));
    }

    @Override
    public final SelectImpl window(WindowDefinition... definitions) {
        getQuery().addWindow(definitions);
        return this;
    }

    @Override
    public final SelectImpl window(Collection<? extends WindowDefinition> definitions) {
        getQuery().addWindow(definitions);
        return this;
    }

    @Override
    public final SelectImpl on(Condition... conditions) {
        conditionStep = ConditionStep.ON;
        joinConditions = new ConditionProviderImpl();
        joinConditions.addConditions(conditions);

        /* [pro] xx
        xx xxxxxxxxxxxxxxxx xx xxxx xx xxxxxxxxxxxxxxxxxxxxxx x xx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx xxx xxxxxxxxxxx x xxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxx
        xxxx
        xx [/pro] */
            getQuery().addJoin(joinTable, joinType, new Condition[] { joinConditions });

        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl on(Field<Boolean> condition) {
        return on(condition(condition));
    }

    @Override
    public final SelectImpl on(String sql) {
        return on(condition(sql));
    }

    @Override
    public final SelectImpl on(String sql, Object... bindings) {
        return on(condition(sql, bindings));
    }

    @Override
    public final SelectImpl on(String sql, QueryPart... parts) {
        return on(condition(sql, parts));
    }

    @Override
    public final SelectImpl onKey() throws DataAccessException {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl onKey(TableField<?, ?>... keyFields) throws DataAccessException {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType, keyFields);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl onKey(ForeignKey<?, ?> key) {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType, key);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;

    }

    @Override
    public final SelectImpl using(Field<?>... fields) {
        return using(Arrays.asList(fields));
    }

    @Override
    public final SelectImpl using(Collection<? extends Field<?>> fields) {
        getQuery().addJoinUsing(joinTable, joinType, fields);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl join(TableLike<?> table) {
        return join(table, JoinType.JOIN);
    }

    @Override
    public final SelectImpl leftOuterJoin(TableLike<?> table) {
        return join(table, JoinType.LEFT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl rightOuterJoin(TableLike<?> table) {
        return join(table, JoinType.RIGHT_OUTER_JOIN);
    }

    @Override
    public final SelectOnStep<R> fullOuterJoin(TableLike<?> table) {
        return join(table, JoinType.FULL_OUTER_JOIN);
    }

    @Override
    public final SelectImpl join(TableLike<?> table, JoinType type) {
        switch (type) {
            case CROSS_JOIN:
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
            case CROSS_APPLY:
            case OUTER_APPLY: {
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
    public final SelectImpl naturalJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_JOIN);
    }

    @Override
    public final SelectImpl naturalLeftOuterJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_LEFT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl naturalRightOuterJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_RIGHT_OUTER_JOIN);
    }

    /* [pro] xx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */

    @Override
    public final SelectImpl join(String sql) {
        return join(table(sql));
    }

    @Override
    public final SelectImpl join(String sql, Object... bindings) {
        return join(table(sql, bindings));
    }

    @Override
    public final SelectImpl join(String sql, QueryPart... parts) {
        return join(table(sql, parts));
    }

    @Override
    public final SelectImpl leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl leftOuterJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl rightOuterJoin(String sql, QueryPart... parts) {
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
    public final SelectImpl naturalJoin(String sql) {
        return naturalJoin(table(sql));
    }

    @Override
    public final SelectImpl naturalJoin(String sql, Object... bindings) {
        return naturalJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl naturalJoin(String sql, QueryPart... parts) {
        return naturalJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl naturalLeftOuterJoin(String sql) {
        return naturalLeftOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl naturalLeftOuterJoin(String sql, Object... bindings) {
        return naturalLeftOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl naturalLeftOuterJoin(String sql, QueryPart... parts) {
        return naturalLeftOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl naturalRightOuterJoin(String sql) {
        return naturalRightOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl naturalRightOuterJoin(String sql, Object... bindings) {
        return naturalRightOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl naturalRightOuterJoin(String sql, QueryPart... parts) {
        return naturalRightOuterJoin(table(sql, parts));
    }

    /* [pro] xx

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxxxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxx x xxxxxxx
        xxxxxx xxxxx
    x

    xxxxxxxxx
    xxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxx xxxxxxx x
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx
    x

    xx [/pro] */
    @Override
    public final ResultQuery<R> maxRows(int rows) {
        return getDelegate().maxRows(rows);
    }

    @Override
    public final ResultQuery<R> fetchSize(int rows) {
        return getDelegate().fetchSize(rows);
    }

    @Override
    public final ResultQuery<R> resultSetConcurrency(int resultSetConcurrency) {
        return getDelegate().resultSetConcurrency(resultSetConcurrency);
    }

    @Override
    public final ResultQuery<R> resultSetType(int resultSetType) {
        return getDelegate().resultSetType(resultSetType);
    }

    @Override
    public final ResultQuery<R> resultSetHoldability(int resultSetHoldability) {
        return getDelegate().resultSetHoldability(resultSetHoldability);
    }

    @Override
    public final ResultQuery<R> intern(Field<?>... fields) {
        return getDelegate().intern(fields);
    }

    @Override
    public final ResultQuery<R> intern(int... fieldIndexes) {
        return getDelegate().intern(fieldIndexes);
    }

    @Override
    public final ResultQuery<R> intern(String... fieldNames) {
        return getDelegate().intern(fieldNames);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return getDelegate().getRecordType();
    }

    @Override
    public final List<Field<?>> getSelect() {
        return getDelegate().getSelect();
    }

    @Override
    public final Result<R> getResult() {
        return getDelegate().getResult();
    }

    @Override
    public final Result<R> fetch() {
        return getDelegate().fetch();
    }

    @Override
    public final ResultSet fetchResultSet() {
        return getDelegate().fetchResultSet();
    }

    @Override
    public final Iterator<R> iterator() {
        return getDelegate().iterator();
    }

    @Override
    public final Cursor<R> fetchLazy() {
        return getDelegate().fetchLazy();
    }

    @Override
    @Deprecated
    public final Cursor<R> fetchLazy(int fetchSize) {
        return getDelegate().fetchLazy(fetchSize);
    }

    @Override
    public final List<Result<Record>> fetchMany() {
        return getDelegate().fetchMany();
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return getDelegate().fetch(field);
    }

    @Override
    public final <T> List<T> fetch(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetch(field, type);
    }

    @Override
    public final <T, U> List<U> fetch(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetch(field, converter);
    }

    @Override
    public final List<?> fetch(int fieldIndex) {
        return getDelegate().fetch(fieldIndex);
    }

    @Override
    public final <T> List<T> fetch(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetch(fieldIndex, type);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetch(fieldIndex, converter);
    }

    @Override
    public final List<?> fetch(String fieldName) {
        return getDelegate().fetch(fieldName);
    }

    @Override
    public final <T> List<T> fetch(String fieldName, Class<? extends T> type) {
        return getDelegate().fetch(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetch(fieldName, converter);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        return getDelegate().fetchOne(field);
    }

    @Override
    public final <T> T fetchOne(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchOne(field, type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchOne(field, converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        return getDelegate().fetchOne(fieldIndex);
    }

    @Override
    public final <T> T fetchOne(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchOne(fieldIndex, type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchOne(fieldIndex, converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        return getDelegate().fetchOne(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchOne(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchOne(fieldName, converter);
    }

    @Override
    public final R fetchOne() {
        return getDelegate().fetchOne();
    }

    @Override
    public final <E> E fetchOne(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchOne(mapper);
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        return getDelegate().fetchOneMap();
    }

    @Override
    public final Object[] fetchOneArray() {
        return getDelegate().fetchOneArray();
    }

    @Override
    public final <E> E fetchOneInto(Class<? extends E> type) {
        return getDelegate().fetchOneInto(type);
    }

    @Override
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        return getDelegate().fetchOneInto(table);
    }

    @Override
    public final <T> T fetchAny(Field<T> field) {
        return getDelegate().fetchAny(field);
    }

    @Override
    public final <T> T fetchAny(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchAny(field, type);
    }

    @Override
    public final <T, U> U fetchAny(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchAny(field, converter);
    }

    @Override
    public final Object fetchAny(int fieldIndex) {
        return getDelegate().fetchAny(fieldIndex);
    }

    @Override
    public final <T> T fetchAny(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchAny(fieldIndex, type);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchAny(fieldIndex, converter);
    }

    @Override
    public final Object fetchAny(String fieldName) {
        return getDelegate().fetchAny(fieldName);
    }

    @Override
    public final <T> T fetchAny(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchAny(fieldName, type);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchAny(fieldName, converter);
    }

    @Override
    public final R fetchAny() {
        return getDelegate().fetchAny();
    }

    @Override
    public final Map<String, Object> fetchAnyMap() {
        return getDelegate().fetchAnyMap();
    }

    @Override
    public final Object[] fetchAnyArray() {
        return getDelegate().fetchAnyArray();
    }

    @Override
    public final <E> E fetchAnyInto(Class<? extends E> type) {
        return getDelegate().fetchAnyInto(type);
    }

    @Override
    public final <Z extends Record> Z fetchAnyInto(Table<Z> table) {
        return getDelegate().fetchAnyInto(table);
    }

    @Override
    public final <K> Map<K, R> fetchMap(Field<K> key) {
        return getDelegate().fetchMap(key);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return getDelegate().fetchMap(key, value);
    }

    @Override
    public final Map<Record, R> fetchMap(Field<?>[] keys) {
        return getDelegate().fetchMap(keys);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchMap(keys, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keys, mapper);
    }

    @Override
    public final <S extends Record> Map<S, R> fetchMap(Table<S> table) {
        return getDelegate().fetchMap(table);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, Class<? extends E> type) {
        return getDelegate().fetchMap(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(table, mapper);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchMap(key, type);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(key, mapper);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        return getDelegate().fetchMaps();
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return getDelegate().fetchGroups(key);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return getDelegate().fetchGroups(key, value);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return getDelegate().fetchGroups(keys);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchGroups(keys, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keys, mapper);
    }

    @Override
    public final <S extends Record> Map<S, Result<R>> fetchGroups(Table<S> table) {
        return getDelegate().fetchGroups(table);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, Class<? extends E> type) {
        return getDelegate().fetchGroups(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(table, mapper);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchGroups(key, type);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(key, mapper);
    }

    @Override
    public final Object[][] fetchArrays() {
        return getDelegate().fetchArrays();
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) {
        return getDelegate().fetchArray(fieldIndex);
    }

    @Override
    public final <T> T[] fetchArray(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchArray(fieldIndex, type);
    }

    @Override
    public final <U> U[] fetchArray(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchArray(fieldIndex, converter);
    }

    @Override
    public final Object[] fetchArray(String fieldName) {
        return getDelegate().fetchArray(fieldName);
    }

    @Override
    public final <T> T[] fetchArray(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchArray(fieldName, type);
    }

    @Override
    public final <U> U[] fetchArray(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchArray(fieldName, converter);
    }

    @Override
    public final <T> T[] fetchArray(Field<T> field) {
        return getDelegate().fetchArray(field);
    }

    @Override
    public final <T> T[] fetchArray(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchArray(field, type);
    }

    @Override
    public final <T, U> U[] fetchArray(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchArray(field, converter);
    }

    @Override
    public final Set<?> fetchSet(int fieldIndex) {
        return getDelegate().fetchSet(fieldIndex);
    }

    @Override
    public final <T> Set<T> fetchSet(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchSet(fieldIndex, type);
    }

    @Override
    public final <U> Set<U> fetchSet(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchSet(fieldIndex, converter);
    }

    @Override
    public final Set<?> fetchSet(String fieldName) {
        return getDelegate().fetchSet(fieldName);
    }

    @Override
    public final <T> Set<T> fetchSet(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchSet(fieldName, type);
    }

    @Override
    public final <U> Set<U> fetchSet(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchSet(fieldName, converter);
    }

    @Override
    public final <T> Set<T> fetchSet(Field<T> field) {
        return getDelegate().fetchSet(field);
    }

    @Override
    public final <T> Set<T> fetchSet(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchSet(field, type);
    }

    @Override
    public final <T, U> Set<U> fetchSet(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchSet(field, converter);
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) {
        return getDelegate().fetchInto(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return getDelegate().fetchInto(table);
    }

    @Override
    public final <H extends RecordHandler<? super R>> H fetchInto(H handler) {
        return getDelegate().fetchInto(handler);
    }

    @Override
    public final <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetch(mapper);
    }

    @Override
    @Deprecated
    public final org.jooq.FutureResult<R> fetchLater() {
        return getDelegate().fetchLater();
    }

    @Override
    @Deprecated
    public final org.jooq.FutureResult<R> fetchLater(ExecutorService executor) {
        return getDelegate().fetchLater(executor);
    }

    @Override
    public final Table<R> asTable() {
        return getDelegate().asTable();
    }

    @Override
    public final Table<R> asTable(String alias) {
        return getDelegate().asTable(alias);
    }

    @Override
    public final Table<R> asTable(String alias, String... fieldAliases) {
        return getDelegate().asTable(alias, fieldAliases);
    }

    @Override
    public final <T> Field<T> asField() {
        return getDelegate().asField();
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return getDelegate().asField(alias);
    }

    @Override
    public final Row fieldsRow() {
        return getDelegate().fieldsRow();
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return getDelegate().field(field);
    }

    @Override
    public final Field<?> field(String string) {
        return getDelegate().field(string);
    }

    @Override
    public final Field<?> field(int index) {
        return getDelegate().field(index);
    }

    @Override
    public final Field<?>[] fields() {
        return getDelegate().fields();
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
