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
import static org.jooq.impl.DSL.table;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Generated;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Operator;
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
import org.jooq.Row1;
import org.jooq.Row10;
import org.jooq.Row11;
import org.jooq.Row12;
import org.jooq.Row13;
import org.jooq.Row14;
import org.jooq.Row15;
import org.jooq.Row16;
import org.jooq.Row17;
import org.jooq.Row18;
import org.jooq.Row19;
import org.jooq.Row2;
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
import org.jooq.RowN;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateFromStep;
import org.jooq.UpdateQuery;
import org.jooq.UpdateResultStep;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.UpdateWhereStep;

/**
 * A wrapper for an {@link UpdateQuery}
 *
 * @author Lukas Eder
 */
final class UpdateImpl<R extends Record>
    extends AbstractDelegatingQuery<UpdateQuery<R>>
    implements

    // Cascading interface implementations for Update behaviour
    UpdateSetFirstStep<R>,
    UpdateSetMoreStep<R>,
    UpdateConditionStep<R>,
    UpdateResultStep<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -2444876472650065331L;

    UpdateImpl(Configuration configuration, WithImpl with, Table<R> table) {
        super(new UpdateQueryImpl<R>(configuration, with, table));
    }

    @Override
    public final <T> UpdateImpl<R> set(Field<T> field, T value) {
        getDelegate().addValue(field, value);
        return this;
    }

    @Override
    public final <T> UpdateImpl<R> set(Field<T> field, Field<T> value) {
        getDelegate().addValue(field, value);
        return this;
    }

    @Override
    public final <T> UpdateImpl<R> set(Field<T> field, Select<? extends Record1<T>> value) {
        if (value == null)
            return set(field, (T) null);
        else
            return set(field, value.<T>asField());
    }

    @Override
    public final UpdateImpl<R> set(Map<? extends Field<?>, ?> map) {
        getDelegate().addValues(map);
        return this;
    }

    @Override
    public final UpdateImpl<R> set(Record record) {
        return set(Tools.mapOfChangedValues(record));
    }

// [jooq-tools] START [set]

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1> UpdateFromStep<R> set(Row1<T1> row, Row1<T1> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2> UpdateFromStep<R> set(Row2<T1, T2> row, Row2<T1, T2> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3> UpdateFromStep<R> set(Row3<T1, T2, T3> row, Row3<T1, T2, T3> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4> UpdateFromStep<R> set(Row4<T1, T2, T3, T4> row, Row4<T1, T2, T3, T4> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5> UpdateFromStep<R> set(Row5<T1, T2, T3, T4, T5> row, Row5<T1, T2, T3, T4, T5> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6> UpdateFromStep<R> set(Row6<T1, T2, T3, T4, T5, T6> row, Row6<T1, T2, T3, T4, T5, T6> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> UpdateFromStep<R> set(Row7<T1, T2, T3, T4, T5, T6, T7> row, Row7<T1, T2, T3, T4, T5, T6, T7> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> UpdateFromStep<R> set(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Row8<T1, T2, T3, T4, T5, T6, T7, T8> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> UpdateFromStep<R> set(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> UpdateFromStep<R> set(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> UpdateFromStep<R> set(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> UpdateFromStep<R> set(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> UpdateFromStep<R> set(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> UpdateFromStep<R> set(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> UpdateFromStep<R> set(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> UpdateFromStep<R> set(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> UpdateFromStep<R> set(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> UpdateFromStep<R> set(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> UpdateFromStep<R> set(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> UpdateFromStep<R> set(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> UpdateFromStep<R> set(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> UpdateFromStep<R> set(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final UpdateFromStep<R> set(RowN row, RowN value) {
        getDelegate().addValues(row, value);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1> UpdateFromStep<R> set(Row1<T1> row, Select<? extends Record1<T1>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2> UpdateFromStep<R> set(Row2<T1, T2> row, Select<? extends Record2<T1, T2>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3> UpdateFromStep<R> set(Row3<T1, T2, T3> row, Select<? extends Record3<T1, T2, T3>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4> UpdateFromStep<R> set(Row4<T1, T2, T3, T4> row, Select<? extends Record4<T1, T2, T3, T4>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5> UpdateFromStep<R> set(Row5<T1, T2, T3, T4, T5> row, Select<? extends Record5<T1, T2, T3, T4, T5>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6> UpdateFromStep<R> set(Row6<T1, T2, T3, T4, T5, T6> row, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> UpdateFromStep<R> set(Row7<T1, T2, T3, T4, T5, T6, T7> row, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> UpdateFromStep<R> set(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> UpdateFromStep<R> set(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> UpdateFromStep<R> set(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> UpdateFromStep<R> set(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> UpdateFromStep<R> set(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> UpdateFromStep<R> set(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> UpdateFromStep<R> set(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> UpdateFromStep<R> set(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> UpdateFromStep<R> set(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> UpdateFromStep<R> set(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> UpdateFromStep<R> set(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> UpdateFromStep<R> set(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> UpdateFromStep<R> set(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> UpdateFromStep<R> set(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> UpdateFromStep<R> set(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select) {
        getDelegate().addValues(row, select);
        return this;
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final UpdateFromStep<R> set(RowN row, Select<?> select) {
        getDelegate().addValues(row, select);
        return this;
    }

// [jooq-tools] END [set]

    @Override
    public final UpdateWhereStep<R> from(TableLike<?> table) {
        getDelegate().addFrom(table);
        return this;
    }

    @Override
    public final UpdateWhereStep<R> from(TableLike<?>... tables) {
        getDelegate().addFrom(tables);
        return this;
    }

    @Override
    public final UpdateWhereStep<R> from(Collection<? extends TableLike<?>> tables) {
        getDelegate().addFrom(tables);
        return this;
    }

    @Override
    public final UpdateWhereStep<R> from(SQL sql) {
        return from(table(sql));
    }

    @Override
    public final UpdateWhereStep<R> from(String sql) {
        return from(table(sql));
    }

    @Override
    public final UpdateWhereStep<R> from(String sql, Object... bindings) {
        return from(table(sql, bindings));
    }

    @Override
    public final UpdateWhereStep<R> from(String sql, QueryPart... parts) {
        return from(table(sql, parts));
    }

    @Override
    public final UpdateWhereStep<R> from(Name name) {
        return from(table(name));
    }

    @Override
    public final UpdateImpl<R> where(Condition... conditions) {
        getDelegate().addConditions(conditions);
        return this;
    }

    @Override
    public final UpdateImpl<R> where(Collection<? extends Condition> conditions) {
        getDelegate().addConditions(conditions);
        return this;
    }

    @Override
    public final UpdateImpl<R> where(Field<Boolean> condition) {
        return where(condition(condition));
    }

    @Override
    @Deprecated
    public final UpdateImpl<R> where(Boolean condition) {
        return where(condition(condition));
    }

    @Override
    public final UpdateImpl<R> where(SQL sql) {
        return where(condition(sql));
    }

    @Override
    public final UpdateImpl<R> where(String sql) {
        return where(condition(sql));
    }

    @Override
    public final UpdateImpl<R> where(String sql, Object... bindings) {
        return where(condition(sql, bindings));
    }

    @Override
    public final UpdateImpl<R> where(String sql, QueryPart... parts) {
        return where(condition(sql, parts));
    }

    @Override
    public final UpdateImpl<R> whereExists(Select<?> select) {
        return andExists(select);
    }

    @Override
    public final UpdateImpl<R> whereNotExists(Select<?> select) {
        return andNotExists(select);
    }

    @Override
    public final UpdateImpl<R> and(Condition condition) {
        getDelegate().addConditions(condition);
        return this;
    }

    @Override
    public final UpdateImpl<R> and(Field<Boolean> condition) {
        return and(condition(condition));
    }

    @Override
    @Deprecated
    public final UpdateImpl<R> and(Boolean condition) {
        return and(condition(condition));
    }

    @Override
    public final UpdateImpl<R> and(SQL sql) {
        return and(condition(sql));
    }

    @Override
    public final UpdateImpl<R> and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final UpdateImpl<R> and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final UpdateImpl<R> and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final UpdateImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final UpdateImpl<R> andNot(Field<Boolean> condition) {
        return andNot(condition(condition));
    }

    @Override
    @Deprecated
    public final UpdateImpl<R> andNot(Boolean condition) {
        return andNot(condition(condition));
    }

    @Override
    public final UpdateImpl<R> andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final UpdateImpl<R> andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final UpdateImpl<R> or(Condition condition) {
        getDelegate().addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final UpdateImpl<R> or(Field<Boolean> condition) {
        return or(condition(condition));
    }

    @Override
    @Deprecated
    public final UpdateImpl<R> or(Boolean condition) {
        return or(condition(condition));
    }

    @Override
    public final UpdateImpl<R> or(SQL sql) {
        return or(condition(sql));
    }

    @Override
    public final UpdateImpl<R> or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final UpdateImpl<R> or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final UpdateImpl<R> or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final UpdateImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final UpdateImpl<R> orNot(Field<Boolean> condition) {
        return orNot(condition(condition));
    }

    @Override
    @Deprecated
    public final UpdateImpl<R> orNot(Boolean condition) {
        return orNot(condition(condition));
    }

    @Override
    public final UpdateImpl<R> orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final UpdateImpl<R> orNotExists(Select<?> select) {
        return or(notExists(select));
    }

    @Override
    public final UpdateImpl<R> returning() {
        getDelegate().setReturning();
        return this;
    }

    @Override
    public final UpdateImpl<R> returning(Field<?>... f) {
        getDelegate().setReturning(f);
        return this;
    }

    @Override
    public final UpdateImpl<R> returning(Collection<? extends Field<?>> f) {
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
