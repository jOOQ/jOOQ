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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Tools.map;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteResultStep;
import org.jooq.DeleteUsingStep;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.OrderField;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableLike;
// ...
import org.jooq.impl.QOM.Delete;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.With;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class DeleteImpl<R extends Record>
extends
    AbstractDelegatingDMLQuery<R, DeleteQueryImpl<R>>
implements

    // Cascading interface implementations for Delete behaviour
    DeleteUsingStep<R>,
    DeleteConditionStep<R>,
    QOM.Delete<R>
{
    private boolean           returningResult;

    DeleteImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(new DeleteQueryImpl<>(configuration, with, table));
    }

    @Override
    public final DeleteImpl<R> using(TableLike<?> table) {
        getDelegate().addUsing(table);
        return this;
    }

    @Override
    public final DeleteImpl<R> using(TableLike<?>... tables) {
        getDelegate().addUsing(tables);
        return this;
    }

    @Override
    public final DeleteImpl<R> using(Collection<? extends TableLike<?>> tables) {
        getDelegate().addUsing(tables);
        return this;
    }

    @Override
    public final DeleteImpl<R> using(SQL sql) {
        return using(table(sql));
    }

    @Override
    public final DeleteImpl<R> using(String sql) {
        return using(table(sql));
    }

    @Override
    public final DeleteImpl<R> using(String sql, Object... bindings) {
        return using(table(sql, bindings));
    }

    @Override
    public final DeleteImpl<R> using(String sql, QueryPart... parts) {
        return using(table(sql, parts));
    }

    @Override
    public final DeleteImpl<R> using(Name name) {
        return using(table(name));
    }

    @Override
    public final DeleteImpl<R> where(Condition conditions) {
        getDelegate().addConditions(conditions);
        return this;
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
    public final DeleteImpl<R> whereExists(TableLike<?> select) {
        return andExists(select);
    }

    @Override
    public final DeleteImpl<R> whereNotExists(TableLike<?> select) {
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
    public final DeleteImpl<R> andExists(TableLike<?> select) {
        return and(exists(select));
    }

    @Override
    public final DeleteImpl<R> andNotExists(TableLike<?> select) {
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
    public final DeleteImpl<R> orExists(TableLike<?> select) {
        return or(exists(select));
    }

    @Override
    public final DeleteImpl<R> orNotExists(TableLike<?> select) {
        return or(notExists(select));
    }

    @Override
    public final DeleteImpl<R> orderBy(OrderField<?>... fields) {
        getDelegate().addOrderBy(fields);
        return this;
    }

    @Override
    public final DeleteImpl<R> orderBy(Collection<? extends OrderField<?>> fields) {
        getDelegate().addOrderBy(fields);
        return this;
    }

    @Override
    public final DeleteImpl<R> orderBy(int... fieldIndexes) {
        return orderBy(map(fieldIndexes, v -> DSL.inline(v)));
    }

    @Override
    public final DeleteImpl<R> limit(Number numberOfRows) {
        getDelegate().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final DeleteImpl<R> limit(Field<? extends Number> numberOfRows) {
        getDelegate().addLimit(numberOfRows);
        return this;
    }

    @Override
    public final DeleteResultStep<R> returning() {
        getDelegate().setReturning();
        return new DeleteAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final DeleteResultStep<R> returning(SelectFieldOrAsterisk... f) {
        getDelegate().setReturning(f);
        return new DeleteAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final DeleteResultStep<R> returning(Collection<? extends SelectFieldOrAsterisk> f) {
        getDelegate().setReturning(f);
        return new DeleteAsResultQuery<>(getDelegate(), returningResult);
    }

    @Override
    public final DeleteResultStep<Record> returningResult(SelectFieldOrAsterisk... f) {
        returningResult = true;
        getDelegate().setReturning(f);
        return new DeleteAsResultQuery(getDelegate(), returningResult);
    }

    @Override
    public final DeleteResultStep<Record> returningResult(Collection<? extends SelectFieldOrAsterisk> f) {
        returningResult = true;
        getDelegate().setReturning(f);
        return new DeleteAsResultQuery(getDelegate(), returningResult);
    }



    @Override
    public final DeleteResultStep returningResult(SelectField field1) {
        return returningResult(new SelectField[] { field1 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2) {
        return returningResult(new SelectField[] { field1, field2 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3) {
        return returningResult(new SelectField[] { field1, field2, field3 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4) {
        return returningResult(new SelectField[] { field1, field2, field3, field4 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final DeleteResultStep returningResult(SelectField field1, SelectField field2, SelectField field3, SelectField field4, SelectField field5, SelectField field6, SelectField field7, SelectField field8, SelectField field9, SelectField field10, SelectField field11, SelectField field12, SelectField field13, SelectField field14, SelectField field15, SelectField field16, SelectField field17, SelectField field18, SelectField field19, SelectField field20, SelectField field21, SelectField field22) {
        return returningResult(new SelectField[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final With $with() {
        return getDelegate().$with();
    }

    @Override
    public final Table<R> $from() {
        return getDelegate().$from();
    }

    @Override
    public final Delete<?> $from(Table<?> from) {
        return getDelegate().$from(from);
    }

    @Override
    public final UnmodifiableList<? extends Table<?>> $using() {
        return getDelegate().$using();
    }

    @Override
    public final Delete<R> $using(Collection<? extends Table<?>> using) {
        return getDelegate().$using(using);
    }

    @Override
    public final Condition $where() {
        return getDelegate().$where();
    }

    @Override
    public final Delete<R> $where(Condition condition) {
        return getDelegate().$where(condition);
    }

    @Override
    public final UnmodifiableList<? extends SortField<?>> $orderBy() {
        return getDelegate().$orderBy();
    }

    @Override
    public final Delete<R> $orderBy(Collection<? extends SortField<?>> orderBy) {
        return getDelegate().$orderBy(orderBy);
    }

    @Override
    public final Field<? extends Number> $limit() {
        return getDelegate().$limit();
    }

    @Override
    public final Delete<R> $limit(Field<? extends Number> limit) {
        return getDelegate().$limit(limit);
    }














}
