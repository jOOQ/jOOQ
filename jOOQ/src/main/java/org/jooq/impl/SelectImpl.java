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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.map;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jooq.BetweenAndStep;
import org.jooq.BetweenAndStepR;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.GroupField;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
// ...
// ...
// ...
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectFinalStep;
// ...
// ...
import org.jooq.SelectForUpdateOfStep;
// ...
// ...
// ...
import org.jooq.SelectHavingConditionStep;
import org.jooq.SelectIntoStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectLimitAfterOffsetStep;
import org.jooq.SelectLimitPercentAfterOffsetStep;
import org.jooq.SelectLimitPercentStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.SelectOptionalOnStep;
import org.jooq.SelectQualifyConditionStep;
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
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
// ...
import org.jooq.WindowDefinition;

/**
 * A wrapper for a {@link SelectQuery}
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
final class SelectImpl<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>
extends AbstractDelegatingQuery<R, SelectQueryImpl<R>>
implements
    ResultQueryTrait<R>,

    // Cascading interface implementations for Select behaviour
    SelectSelectStep<R>,
    SelectOptionalOnStep<R>,
    SelectOnConditionStep<R>,
    SelectConditionStep<R>,





    SelectHavingConditionStep<R>,
    SelectQualifyConditionStep<R>,


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











    SelectSeekStepN<R>,
    SelectSeekLimitStep<R>,
    SelectLimitPercentStep<R>,
    SelectLimitAfterOffsetStep<R>,
    SelectLimitPercentAfterOffsetStep<R>,
    SelectForUpdateOfStep<R> {

    /**
     * A temporary member holding a join table
     */
    private transient TableLike<?>            joinTable;

    /**
     * A temporary member holding a join partition by expression
     */
    private transient Field<?>[]              joinPartitionBy;

    /**
     * A temporary member holding a join type
     */
    private transient JoinType                joinType;

    /**
     * A temporary member holding a join condition
     */
    private transient ConditionProviderImpl   joinConditions;

    /**
     * The step that is currently receiving new conditions
     */
    private transient ConditionStep           conditionStep;

    /**
     * The limit that has been added in a limit(Number).offset(Number) construct
     */
    private transient Number                  limit;
    private transient Param<? extends Number> limitParam;
    private transient Number                  offset;
    private transient Param<? extends Number> offsetParam;








    SelectImpl(Configuration configuration, WithImpl with) {
        this(configuration, with, false);
    }

    SelectImpl(Configuration configuration, WithImpl with, boolean distinct) {
        this(new SelectQueryImpl<>(configuration, with, distinct));
    }

    SelectImpl(SelectQueryImpl<R> query) {
        super(query);
    }

    @Override
    public final SelectQuery<R> getQuery() {
        return getDelegate();
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record&gt; and SelectSelectStep&lt;R&gt;
     */
    @Override
    public final SelectImpl select(SelectFieldOrAsterisk... fields) {
        getQuery().addSelect(fields);
        return this;
    }

    /**
     * This method must be able to return both incompatible types
     * SelectSelectStep&lt;Record&gt; and SelectSelectStep&lt;R&gt;
     */
    @Override
    public final SelectImpl select(Collection<? extends SelectFieldOrAsterisk> fields) {
        getQuery().addSelect(fields);
        return this;
    }

    @Override
    public final SelectIntoStep<R> on(SelectFieldOrAsterisk... fields) {
        return distinctOn(Arrays.asList(fields));
    }

    @Override
    public final SelectIntoStep<R> on(Collection<? extends SelectFieldOrAsterisk> fields) {
        return distinctOn(fields);
    }

    @Override
    public final SelectIntoStep<R> distinctOn(SelectFieldOrAsterisk... fields) {
        getQuery().addDistinctOn(fields);
        return this;
    }

    @Override
    public final SelectIntoStep<R> distinctOn(Collection<? extends SelectFieldOrAsterisk> fields) {
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
    public final SelectImpl from(SQL sql) {
        return from(table(sql));
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
    public final SelectJoinStep<R> from(Name name) {
        return from(table(name));
    }

    @Override
    public final SelectImpl where(Condition conditions) {
        conditionStep = ConditionStep.WHERE;
        getQuery().addConditions(conditions);
        return this;
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
    public final SelectImpl where(SQL sql) {
        return where(condition(sql));
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





            case HAVING:
                getQuery().addHaving(condition);
                break;
            case ON:
                joinConditions.addConditions(condition);
                break;
            case QUALIFY:
                getQuery().addQualify(condition);
                break;
        }

        return this;
    }

    @Override
    public final SelectImpl and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    public final SelectImpl and(SQL sql) {
        return and(condition(sql));
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




            case HAVING:
                getQuery().addHaving(Operator.OR, condition);
                break;
            case ON:
                joinConditions.addConditions(Operator.OR, condition);
                break;
            case QUALIFY:
                getQuery().addQualify(Operator.OR, condition);
                break;
        }

        return this;
    }

    @Override
    public final SelectImpl or(Field<Boolean> condition) {
        return or(condition(condition));
    }

    @Override
    public final SelectImpl or(SQL sql) {
        return or(condition(sql));
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
    public final SelectImpl groupBy(GroupField... fields) {
        getQuery().addGroupBy(fields);
        return this;
    }

    @Override
    public final SelectImpl groupBy(Collection<? extends GroupField> fields) {
        getQuery().addGroupBy(fields);
        return this;
    }



    @Override
    public final SelectSeekStep1 orderBy(OrderField t1) {
        return orderBy(new OrderField[] { t1 });
    }

    @Override
    public final SelectSeekStep2 orderBy(OrderField t1, OrderField t2) {
        return orderBy(new OrderField[] { t1, t2 });
    }

    @Override
    public final SelectSeekStep3 orderBy(OrderField t1, OrderField t2, OrderField t3) {
        return orderBy(new OrderField[] { t1, t2, t3 });
    }

    @Override
    public final SelectSeekStep4 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4) {
        return orderBy(new OrderField[] { t1, t2, t3, t4 });
    }

    @Override
    public final SelectSeekStep5 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final SelectSeekStep6 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final SelectSeekStep7 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final SelectSeekStep8 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final SelectSeekStep9 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final SelectSeekStep10 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final SelectSeekStep11 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final SelectSeekStep12 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final SelectSeekStep13 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final SelectSeekStep14 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final SelectSeekStep15 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final SelectSeekStep16 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final SelectSeekStep17 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final SelectSeekStep18 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17, OrderField t18) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final SelectSeekStep19 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17, OrderField t18, OrderField t19) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final SelectSeekStep20 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17, OrderField t18, OrderField t19, OrderField t20) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final SelectSeekStep21 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17, OrderField t18, OrderField t19, OrderField t20, OrderField t21) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final SelectSeekStep22 orderBy(OrderField t1, OrderField t2, OrderField t3, OrderField t4, OrderField t5, OrderField t6, OrderField t7, OrderField t8, OrderField t9, OrderField t10, OrderField t11, OrderField t12, OrderField t13, OrderField t14, OrderField t15, OrderField t16, OrderField t17, OrderField t18, OrderField t19, OrderField t20, OrderField t21, OrderField t22) {
        return orderBy(new OrderField[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }



    @Override
    public final SelectImpl orderBy(OrderField<?>... fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl orderBy(Collection<? extends OrderField<?>> fields) {
        getQuery().addOrderBy(fields);
        return this;
    }

    @Override
    public final SelectImpl orderBy(int... fieldIndexes) {
        getQuery().addOrderBy(fieldIndexes);
        return this;
    }































    @Override
    public final SelectSeekLimitStep<R> seek(Object t1) {
        return seek(new Object[] { t1 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1) {
        return seekBefore(new Object[] { t1 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1) {
        return seekAfter(new Object[] { t1 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2) {
        return seek(new Object[] { t1, t2 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2) {
        return seekBefore(new Object[] { t1, t2 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2) {
        return seekAfter(new Object[] { t1, t2 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3) {
        return seek(new Object[] { t1, t2, t3 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3) {
        return seekBefore(new Object[] { t1, t2, t3 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3) {
        return seekAfter(new Object[] { t1, t2, t3 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4) {
        return seek(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4) {
        return seekBefore(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4) {
        return seekAfter(new Object[] { t1, t2, t3, t4 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seek(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seek(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seekBefore(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object t1, Object t2, Object t3, Object t4, Object t5, Object t6, Object t7, Object t8, Object t9, Object t10, Object t11, Object t12, Object t13, Object t14, Object t15, Object t16, Object t17, Object t18, Object t19, Object t20, Object t21, Object t22) {
        return seekAfter(new Object[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1) {
        return seek(new Field[] { t1 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1) {
        return seekBefore(new Field[] { t1 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1) {
        return seekAfter(new Field[] { t1 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2) {
        return seek(new Field[] { t1, t2 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2) {
        return seekBefore(new Field[] { t1, t2 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2) {
        return seekAfter(new Field[] { t1, t2 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3) {
        return seek(new Field[] { t1, t2, t3 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3) {
        return seekBefore(new Field[] { t1, t2, t3 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3) {
        return seekAfter(new Field[] { t1, t2, t3 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4) {
        return seek(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4) {
        return seekBefore(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4) {
        return seekAfter(new Field[] { t1, t2, t3, t4 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seek(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seek(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    @Deprecated
    public final SelectSeekLimitStep<R> seekBefore(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seekBefore(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return seekAfter(new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }



    private final List<? extends Field<?>> seekValues(Object[] values) {
        if (getQuery() instanceof SelectQueryImpl)
            return Tools.fields(values, map(
                ((SelectQueryImpl<R>) getQuery()).getOrderBy().fields(),
                (Field<?> f) -> f.getDataType(),
                DataType[]::new
            ));
        else
            return Tools.fields(values);
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Object... values) {
        getQuery().addSeekAfter(seekValues(values));
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seek(Field<?>... fields) {
        getQuery().addSeekAfter(fields);
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Object... values) {
        getQuery().addSeekAfter(seekValues(values));
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seekAfter(Field<?>... fields) {
        getQuery().addSeekAfter(fields);
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seekBefore(Object... values) {
        getQuery().addSeekBefore(Tools.fields(values));
        return this;
    }

    @Override
    public final SelectSeekLimitStep<R> seekBefore(Field<?>... fields) {
        getQuery().addSeekBefore(fields);
        return this;
    }

    @Override
    public final SelectImpl limit(int l) {
        return limit((Number) l);
    }

    @Override
    public final SelectImpl limit(Number l) {
        limit = l;
        limitParam = null;
        return limitOffset();
    }

    @Override
    public final SelectImpl limit(Param<? extends Number> l) {
        limit = null;
        limitParam = l;
        return limitOffset();
    }

    @Override
    public final SelectImpl limit(int o, int l) {
        return limit((Number) o, (Number) l);
    }

    @Override
    public final SelectImpl limit(Number o, Number l) {
        offset = o;
        offsetParam = null;
        limit = l;
        limitParam = null;
        return limitOffset();
    }

    @Override
    public final SelectImpl limit(int o, Param<Integer> l) {
        return limit((Number) o, l);
    }

    @Override
    public final SelectImpl limit(Number o, Param<? extends Number> l) {
        offset = o;
        offsetParam = null;
        limit = null;
        limitParam = l;
        return limitOffset();
    }

    @Override
    public final SelectImpl limit(Param<Integer> o, int l) {
        return limit(o, (Number) l);
    }

    @Override
    public final SelectImpl limit(Param<? extends Number> o, Number l) {
        offset = null;
        offsetParam = o;
        limit = l;
        limitParam = null;
        return limitOffset();
    }

    @Override
    public final SelectImpl limit(Param<? extends Number> o, Param<? extends Number> l) {
        offset = null;
        offsetParam = o;
        limit = null;
        limitParam = l;
        return limitOffset();
    }

    @Override
    public final SelectImpl offset(int o) {
        return offset((Number) o);
    }

    @Override
    public final SelectImpl offset(Number o) {
        offset = o;
        offsetParam = null;
        return limitOffset();
    }

    @Override
    public final SelectImpl offset(Param<? extends Number> o) {
        offset = null;
        offsetParam = o;
        return limitOffset();
    }

    private final SelectImpl limitOffset() {
        if (limit != null) {
            if (offset != null)
                getQuery().addLimit(offset, limit);
            else if (offsetParam != null)
                getQuery().addLimit(offsetParam, limit);
            else
                getQuery().addLimit(limit);
        }
        else if (limitParam != null) {
            if (offset != null)
                getQuery().addLimit(offset, limitParam);
            else if (offsetParam != null)
                getQuery().addLimit(offsetParam, limitParam);
            else
                getQuery().addLimit(limitParam);
        }
        else {
            if (offset != null)
                getQuery().addOffset(offset);
            else if (offsetParam != null)
                getQuery().addOffset(offsetParam);
        }

        return this;
    }

    @Override
    public final SelectImpl percent() {
        getQuery().setLimitPercent(true);
        return this;
    }

    @Override
    public final SelectImpl withTies() {
        getQuery().setWithTies(true);
        return this;
    }

    @Override
    public final SelectImpl forUpdate() {
        getQuery().setForUpdate(true);
        return this;
    }

    @Override
    public final SelectImpl forNoKeyUpdate() {
        getQuery().setForNoKeyUpdate(true);
        return this;
    }

    @Override
    public final SelectImpl of(Field<?>... fields) {
        getQuery().setForLockModeOf(fields);
        return this;
    }

    @Override
    public final SelectImpl of(Collection<? extends Field<?>> fields) {
        getQuery().setForLockModeOf(fields);
        return this;
    }

    @Override
    public final SelectImpl of(Table<?>... tables) {
        getQuery().setForLockModeOf(tables);
        return this;
    }

    @Override
    public final SelectImpl wait(int seconds) {
        getQuery().setForLockModeWait(seconds);
        return this;
    }

    @Override
    public final SelectImpl noWait() {
        getQuery().setForLockModeNoWait();
        return this;
    }

    @Override
    public final SelectImpl skipLocked() {
        getQuery().setForLockModeSkipLocked();
        return this;
    }

    @Override
    public final SelectImpl forShare() {
        getQuery().setForShare(true);
        return this;
    }

    @Override
    public final SelectImpl forKeyShare() {
        getQuery().setForKeyShare(true);
        return this;
    }















































































































































































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
    public final SelectImpl exceptAll(Select<? extends R> select) {
        return new SelectImpl(getDelegate().exceptAll(select));
    }

    @Override
    public final SelectImpl intersect(Select<? extends R> select) {
        return new SelectImpl(getDelegate().intersect(select));
    }

    @Override
    public final SelectImpl intersectAll(Select<? extends R> select) {
        return new SelectImpl(getDelegate().intersectAll(select));
    }

    @Override
    public final SelectImpl having(Condition conditions) {
        conditionStep = ConditionStep.HAVING;
        getQuery().addHaving(conditions);
        return this;
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
    public final SelectImpl having(SQL sql) {
        return having(condition(sql));
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
    public final SelectImpl qualify(Condition conditions) {
        conditionStep = ConditionStep.QUALIFY;
        getQuery().addQualify(conditions);
        return this;
    }

    @Override
    public final SelectImpl qualify(Condition... conditions) {
        conditionStep = ConditionStep.QUALIFY;
        getQuery().addQualify(conditions);
        return this;
    }

    @Override
    public final SelectImpl qualify(Collection<? extends Condition> conditions) {
        conditionStep = ConditionStep.QUALIFY;
        getQuery().addQualify(conditions);
        return this;
    }

    @Override
    public final SelectImpl qualify(Field<Boolean> condition) {
        return qualify(condition(condition));
    }

    @Override
    public final SelectImpl qualify(SQL sql) {
        return qualify(condition(sql));
    }

    @Override
    public final SelectImpl qualify(String sql) {
        return qualify(condition(sql));
    }

    @Override
    public final SelectImpl qualify(String sql, Object... bindings) {
        return qualify(condition(sql, bindings));
    }

    @Override
    public final SelectImpl qualify(String sql, QueryPart... parts) {
        return qualify(condition(sql, parts));
    }

    @Override
    public final SelectImpl on(Condition conditions) {
        conditionStep = ConditionStep.ON;
        joinConditions = new ConditionProviderImpl();
        joinConditions.addConditions(conditions);






            getQuery().addJoin(joinTable, joinType, joinConditions);

        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl on(Condition... conditions) {
        conditionStep = ConditionStep.ON;
        joinConditions = new ConditionProviderImpl();
        joinConditions.addConditions(conditions);






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
    public final SelectImpl on(SQL sql) {
        return on(condition(sql));
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
    public final SelectImpl onKey() {
        conditionStep = ConditionStep.ON;
        getQuery().addJoinOnKey(joinTable, joinType);
        joinTable = null;
        joinPartitionBy = null;
        joinType = null;
        return this;
    }

    @Override
    public final SelectImpl onKey(TableField<?, ?>... keyFields) {
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
        return innerJoin(table);
    }

    @Override
    public final SelectImpl innerJoin(TableLike<?> table) {
        return join(table, JoinType.JOIN);
    }

    @Override
    public final SelectImpl leftJoin(TableLike<?> table) {
        return leftOuterJoin(table);
    }

    @Override
    public final SelectImpl leftOuterJoin(TableLike<?> table) {
        return join(table, JoinType.LEFT_OUTER_JOIN);
    }

    @Override
    public final SelectImpl rightJoin(TableLike<?> table) {
        return rightOuterJoin(table);
    }

    @Override
    public final SelectImpl rightOuterJoin(TableLike<?> table) {
        return join(table, JoinType.RIGHT_OUTER_JOIN);
    }

    @Override
    public final SelectOnStep<R> fullJoin(TableLike<?> table) {
        return fullOuterJoin(table);
    }

    @Override
    public final SelectImpl fullOuterJoin(TableLike<?> table) {
        return join(table, JoinType.FULL_OUTER_JOIN);
    }

    @Override
    public final SelectImpl join(TableLike<?> table, JoinType type) {
        switch (type) {
            case CROSS_JOIN:
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
            case NATURAL_FULL_OUTER_JOIN:
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
    public final SelectImpl crossJoin(TableLike<?> table) {
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

    @Override
    public final SelectImpl naturalFullOuterJoin(TableLike<?> table) {
        return join(table, JoinType.NATURAL_FULL_OUTER_JOIN);
    }

    @Override
    public final SelectImpl leftSemiJoin(TableLike<?> table) {
        return join(table, JoinType.LEFT_SEMI_JOIN);
    }

    @Override
    public final SelectImpl leftAntiJoin(TableLike<?> table) {
        return join(table, JoinType.LEFT_ANTI_JOIN);
    }

    @Override
    public final SelectImpl crossApply(TableLike<?> table) {
        return join(table, JoinType.CROSS_APPLY);
    }

    @Override
    public final SelectImpl outerApply(TableLike<?> table) {
        return join(table, JoinType.OUTER_APPLY);
    }

    @Override
    public final SelectImpl straightJoin(TableLike<?> table) {
        return join(table, JoinType.STRAIGHT_JOIN);
    }

    @Override
    public final SelectImpl join(SQL sql) {
        return innerJoin(sql);
    }

    @Override
    public final SelectImpl join(String sql) {
        return innerJoin(sql);
    }

    @Override
    public final SelectImpl join(String sql, Object... bindings) {
        return innerJoin(sql, bindings);
    }

    @Override
    public final SelectImpl join(String sql, QueryPart... parts) {
        return innerJoin(sql, parts);
    }

    @Override
    public final SelectImpl join(Name name) {
        return innerJoin(table(name));
    }

    @Override
    public final SelectImpl innerJoin(SQL sql) {
        return innerJoin(table(sql));
    }

    @Override
    public final SelectImpl innerJoin(String sql) {
        return innerJoin(table(sql));
    }

    @Override
    public final SelectImpl innerJoin(String sql, Object... bindings) {
        return innerJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl innerJoin(String sql, QueryPart... parts) {
        return innerJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl innerJoin(Name name) {
        return innerJoin(table(name));
    }

    @Override
    public final SelectImpl leftJoin(SQL sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final SelectImpl leftJoin(String sql) {
        return leftOuterJoin(sql);
    }

    @Override
    public final SelectImpl leftJoin(String sql, Object... bindings) {
        return leftOuterJoin(sql, bindings);
    }

    @Override
    public final SelectImpl leftJoin(String sql, QueryPart... parts) {
        return leftOuterJoin(sql, parts);
    }

    @Override
    public final SelectImpl leftJoin(Name name) {
        return leftOuterJoin(table(name));
    }

    @Override
    public final SelectImpl leftOuterJoin(SQL sql) {
        return leftOuterJoin(table(sql));
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
    public final SelectImpl leftOuterJoin(Name name) {
        return leftOuterJoin(table(name));
    }

    @Override
    public final SelectImpl rightJoin(SQL sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final SelectImpl rightJoin(String sql) {
        return rightOuterJoin(sql);
    }

    @Override
    public final SelectImpl rightJoin(String sql, Object... bindings) {
        return rightOuterJoin(sql, bindings);
    }

    @Override
    public final SelectImpl rightJoin(String sql, QueryPart... parts) {
        return rightOuterJoin(sql, parts);
    }

    @Override
    public final SelectImpl rightJoin(Name name) {
        return rightOuterJoin(table(name));
    }

    @Override
    public final SelectImpl rightOuterJoin(SQL sql) {
        return rightOuterJoin(table(sql));
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
    public final SelectImpl rightOuterJoin(Name name) {
        return rightOuterJoin(table(name));
    }

    @Override
    public final SelectImpl fullJoin(SQL sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final SelectImpl fullJoin(String sql) {
        return fullOuterJoin(sql);
    }

    @Override
    public final SelectImpl fullJoin(String sql, Object... bindings) {
        return fullOuterJoin(sql, bindings);
    }

    @Override
    public final SelectImpl fullJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(sql, parts);
    }

    @Override
    public final SelectImpl fullJoin(Name name) {
        return fullOuterJoin(name);
    }

    @Override
    public final SelectImpl fullOuterJoin(SQL sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl fullOuterJoin(String sql, QueryPart... parts) {
        return fullOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl fullOuterJoin(Name name) {
        return fullOuterJoin(table(name));
    }

    @Override
    public final SelectJoinStep<R> crossJoin(SQL sql) {
        return crossJoin(table(sql));
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
    public final SelectImpl crossJoin(Name name) {
        return crossJoin(table(name));
    }

    @Override
    public final SelectImpl naturalJoin(SQL sql) {
        return naturalJoin(table(sql));
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
    public final SelectImpl naturalJoin(Name name) {
        return naturalJoin(table(name));
    }

    @Override
    public final SelectImpl naturalLeftOuterJoin(SQL sql) {
        return naturalLeftOuterJoin(table(sql));
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
    public final SelectImpl naturalLeftOuterJoin(Name name) {
        return naturalLeftOuterJoin(table(name));
    }

    @Override
    public final SelectImpl naturalRightOuterJoin(SQL sql) {
        return naturalRightOuterJoin(table(sql));
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

    @Override
    public final SelectImpl naturalRightOuterJoin(Name name) {
        return naturalRightOuterJoin(table(name));
    }

    @Override
    public final SelectImpl naturalFullOuterJoin(SQL sql) {
        return naturalFullOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl naturalFullOuterJoin(String sql) {
        return naturalFullOuterJoin(table(sql));
    }

    @Override
    public final SelectImpl naturalFullOuterJoin(String sql, Object... bindings) {
        return naturalFullOuterJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl naturalFullOuterJoin(String sql, QueryPart... parts) {
        return naturalFullOuterJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl naturalFullOuterJoin(Name name) {
        return naturalFullOuterJoin(table(name));
    }

    @Override
    public final SelectImpl crossApply(SQL sql) {
        return crossApply(table(sql));
    }

    @Override
    public final SelectImpl crossApply(String sql) {
        return crossApply(table(sql));
    }

    @Override
    public final SelectImpl crossApply(String sql, Object... bindings) {
        return crossApply(table(sql, bindings));
    }

    @Override
    public final SelectImpl crossApply(String sql, QueryPart... parts) {
        return crossApply(table(sql, parts));
    }

    @Override
    public final SelectImpl crossApply(Name name) {
        return crossApply(table(name));
    }

    @Override
    public final SelectImpl outerApply(SQL sql) {
        return outerApply(table(sql));
    }

    @Override
    public final SelectImpl outerApply(String sql) {
        return outerApply(table(sql));
    }

    @Override
    public final SelectImpl outerApply(String sql, Object... bindings) {
        return outerApply(table(sql, bindings));
    }

    @Override
    public final SelectImpl outerApply(String sql, QueryPart... parts) {
        return outerApply(table(sql, parts));
    }

    @Override
    public final SelectImpl outerApply(Name name) {
        return outerApply(table(name));
    }


















    @Override
    public final SelectImpl straightJoin(SQL sql) {
        return straightJoin(table(sql));
    }

    @Override
    public final SelectImpl straightJoin(String sql) {
        return straightJoin(table(sql));
    }

    @Override
    public final SelectImpl straightJoin(String sql, Object... bindings) {
        return straightJoin(table(sql, bindings));
    }

    @Override
    public final SelectImpl straightJoin(String sql, QueryPart... parts) {
        return straightJoin(table(sql, parts));
    }

    @Override
    public final SelectImpl straightJoin(Name name) {
        return straightJoin(table(name));
    }

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
    public final ResultQuery<R> intern(Name... fieldNames) {
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
    public final void subscribe(Flow.Subscriber<? super R> subscriber) {
        getDelegate().subscribe(subscriber);
    }



    @Override
    public final void subscribe(org.reactivestreams.Subscriber<? super R> subscriber) {
        getDelegate().subscribe(subscriber);
    }

    @Override
    public final Cursor<R> fetchLazy() {
        return getDelegate().fetchLazy();
    }

    @Override
    public final Cursor<R> fetchLazyNonAutoClosing() {
        return getDelegate().fetchLazyNonAutoClosing();
    }

    @Override
    public final Results fetchMany() {
        return getDelegate().fetchMany();
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
    public final Table<R> asTable(Name alias) {
        return getDelegate().asTable(alias);
    }

    @Override
    public final Table<R> asTable(Name alias, Name... fieldAliases) {
        return getDelegate().asTable(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(Table<?> alias) {
        return getDelegate().asTable(alias);
    }

    @Override
    public final Table<R> asTable(Table<?> alias, Field<?>... fieldAliases) {
        return getDelegate().asTable(alias, fieldAliases);
    }

    @Override
    public final Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction) {
        return getDelegate().asTable(alias, aliasFunction);
    }

    @Override
    public final Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction) {
        return getDelegate().asTable(alias, aliasFunction);
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
    public final <T> Field<T> asField(Function<? super Field<T>, ? extends String> aliasFunction) {
        return getDelegate().asField(aliasFunction);
    }

    @Override
    public final Row fieldsRow() {
        return getDelegate().fieldsRow();
    }

    @Override
    public final <X extends Record> ResultQuery<X> coerce(Table<X> table) {
        return getDelegate().coerce(table);
    }

    @Override
    public final ResultQuery<Record> coerce(Collection<? extends Field<?>> fields) {
        return getDelegate().coerce(fields);
    }

    @Override
    public final Condition compare(Comparator comparator, R record) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).compare(comparator, record.get(0, field(0).getType()));
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends R> select) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).compare(comparator, select);
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends R> select) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).compare(comparator, select);
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition eq(R record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition eq(Select<? extends R> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition eq(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition equal(R record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition equal(Select<? extends R> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition equal(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition ne(R record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition ne(Select<? extends R> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition ne(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition notEqual(R record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition notEqual(Select<? extends R> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition notEqual(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition lt(R record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lt(Select<? extends R> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lt(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lessThan(R record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lessThan(Select<? extends R> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition le(R record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition le(Select<? extends R> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition le(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition lessOrEqual(R record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition lessOrEqual(Select<? extends R> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition gt(R record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition gt(Select<? extends R> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition gt(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition greaterThan(R record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition greaterThan(Select<? extends R> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition ge(R record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition ge(Select<? extends R> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition ge(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition greaterOrEqual(R record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition greaterOrEqual(Select<? extends R> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect<? extends R> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    private final Object[] values(int index, R... records) {
        Class<?> type = field(0).getType();
        return map(records, r -> r.get(index, type), Object[]::new);
    }

    @Override
    public final Condition in(R... records) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).in(values(0, records));
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition in(Select<? extends R> select) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).in(select);
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition notIn(R... records) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).notIn(values(0, records));
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition notIn(Select<? extends R> select) {
        if (Tools.degree(this) == 1)
            return DSL.field((Select) this).notIn(select);
        else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final Condition isDistinctFrom(R record) {
        return null;
    }

    @Override
    public final Condition isDistinctFrom(Select<? extends R> select) {
        return null;
    }

    @Override
    public final Condition isDistinctFrom(QuantifiedSelect<? extends R> select) {
        return null;
    }

    @Override
    public final Condition isNotDistinctFrom(R record) {
        return null;
    }

    @Override
    public final Condition isNotDistinctFrom(Select<? extends R> select) {
        return null;
    }

    @Override
    public final Condition isNotDistinctFrom(QuantifiedSelect<? extends R> select) {
        return null;
    }

    @Override
    public final BetweenAndStep<R> between(R minValue) {
        return null;
    }

    @Override
    public final Condition between(R minValue, R maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStep<R> between(Select<? extends R> minValue) {
        return null;
    }

    @Override
    public final Condition between(Select<? extends R> minValue, Select<? extends R> maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> betweenSymmetric(R minValue) {
        return null;
    }

    @Override
    public final Condition betweenSymmetric(R minValue, R maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> betweenSymmetric(Select<? extends R> minValue) {
        return null;
    }

    @Override
    public final Condition betweenSymmetric(Select<? extends R> minValue, Select<? extends R> maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> notBetween(R minValue) {
        return null;
    }

    @Override
    public final Condition notBetween(R minValue, R maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> notBetween(Select<? extends R> minValue) {
        return null;
    }

    @Override
    public final Condition notBetween(Select<? extends R> minValue, Select<? extends R> maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> notBetweenSymmetric(R minValue) {
        return null;
    }

    @Override
    public final Condition notBetweenSymmetric(R minValue, R maxValue) {
        return null;
    }

    @Override
    public final BetweenAndStepR<R> notBetweenSymmetric(Select<? extends R> minValue) {
        return null;
    }

    @Override
    public final Condition notBetweenSymmetric(Select<? extends R> minValue, Select<? extends R> maxValue) {
        return null;
    }

    @Override
    public final Condition isNull() {
        return new RowIsNull(this, true);
    }

    @Override
    public final Condition isNotNull() {
        return new RowIsNull(this, false);
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
         * Additional conditions go to the <code>HAVING</code> clause that is
         * currently being added.
         */
        HAVING,

        /**
         * Additional conditions go to the <code>QUALIFY</code> clause that is
         * currently being added.
         */
        QUALIFY
    }

    @Override
    public final Field<?>[] getFields(ResultSetMetaData rs) throws SQLException {
        return getDelegate().getFields(rs);
    }

    @Override
    public final Field<?>[] getFields() {
        return getDelegate().getFields();
    }
}
