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

import static org.jooq.impl.DSL.row;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.BetweenAndStep10;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function10;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Record10;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.Row10;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Statement;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class RowImpl10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
extends
    AbstractRow<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>
implements
    Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {

    RowImpl10(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10) {
        super(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    RowImpl10(FieldsImpl<?> fields) {
        super((FieldsImpl) fields);
    }

    // ------------------------------------------------------------------------
    // Mapping convenience methods
    // ------------------------------------------------------------------------

    @Override
    public final SelectField mapping(Function10 function) {
        return convertFrom(r -> r == null ? null : function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10()));
    }

    @Override
    public final SelectField mapping(Class uType, Function10 function) {
        return convertFrom(uType, r -> r == null ? null : function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10()));
    }

    // ------------------------------------------------------------------------
    // XXX: Row accessor API
    // ------------------------------------------------------------------------

    @Override
    public final Field<T1> field1() {
        return (@NotNull Field<T1>) fields.field(0);
    }

    @Override
    public final Field<T2> field2() {
        return (@NotNull Field<T2>) fields.field(1);
    }

    @Override
    public final Field<T3> field3() {
        return (@NotNull Field<T3>) fields.field(2);
    }

    @Override
    public final Field<T4> field4() {
        return (@NotNull Field<T4>) fields.field(3);
    }

    @Override
    public final Field<T5> field5() {
        return (@NotNull Field<T5>) fields.field(4);
    }

    @Override
    public final Field<T6> field6() {
        return (@NotNull Field<T6>) fields.field(5);
    }

    @Override
    public final Field<T7> field7() {
        return (@NotNull Field<T7>) fields.field(6);
    }

    @Override
    public final Field<T8> field8() {
        return (@NotNull Field<T8>) fields.field(7);
    }

    @Override
    public final Field<T9> field9() {
        return (@NotNull Field<T9>) fields.field(8);
    }

    @Override
    public final Field<T10> field10() {
        return (@NotNull Field<T10>) fields.field(9);
    }

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition compare(Comparator comparator, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return compare(this, comparator, row);
    }

    @Override
    public final Condition compare(Comparator comparator, Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return compare(this, comparator, record.valuesRow());
    }

    @Override
    public final Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(comparator, row(Tools.field(t1, (DataType<T1>) dataType(0)), Tools.field(t2, (DataType<T2>) dataType(1)), Tools.field(t3, (DataType<T3>) dataType(2)), Tools.field(t4, (DataType<T4>) dataType(3)), Tools.field(t5, (DataType<T5>) dataType(4)), Tools.field(t6, (DataType<T6>) dataType(5)), Tools.field(t7, (DataType<T7>) dataType(6)), Tools.field(t8, (DataType<T8>) dataType(7)), Tools.field(t9, (DataType<T9>) dataType(8)), Tools.field(t10, (DataType<T10>) dataType(9))));
    }

    @Override
    public final Condition compare(Comparator comparator, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(comparator, row(Tools.nullSafe(t1, dataType(0)), Tools.nullSafe(t2, dataType(1)), Tools.nullSafe(t3, dataType(2)), Tools.nullSafe(t4, dataType(3)), Tools.nullSafe(t5, dataType(4)), Tools.nullSafe(t6, dataType(5)), Tools.nullSafe(t7, dataType(6)), Tools.nullSafe(t8, dataType(7)), Tools.nullSafe(t9, dataType(8)), Tools.nullSafe(t10, dataType(9))));
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Row10 row) {
        return compare(Comparator.EQUALS, row);
    }

    @Override
    public final Condition equal(Record10 record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.EQUALS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition equal(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.EQUALS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition eq(Row10 row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Record10 record) {
        return equal(record);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition eq(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition notEqual(Row10 row) {
        return compare(Comparator.NOT_EQUALS, row);
    }

    @Override
    public final Condition notEqual(Record10 record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition notEqual(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ne(Row10 row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Record10 record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ne(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition lessThan(Row10 row) {
        return compare(Comparator.LESS, row);
    }

    @Override
    public final Condition lessThan(Record10 record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.LESS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lessThan(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.LESS, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lt(Row10 row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Record10 record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lt(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lessOrEqual(Row10 row) {
        return compare(Comparator.LESS_OR_EQUAL, row);
    }

    @Override
    public final Condition lessOrEqual(Record10 record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lessOrEqual(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition le(Row10 row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Record10 record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition le(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition greaterThan(Row10 row) {
        return compare(Comparator.GREATER, row);
    }

    @Override
    public final Condition greaterThan(Record10 record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.GREATER, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition greaterThan(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.GREATER, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition gt(Row10 row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Record10 record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition gt(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition greaterOrEqual(Row10 row) {
        return compare(Comparator.GREATER_OR_EQUAL, row);
    }

    @Override
    public final Condition greaterOrEqual(Record10 record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition greaterOrEqual(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ge(Row10 row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Record10 record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ge(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    @Override
    public final BetweenAndStep10 between(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return between(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9))));
    }

    @Override
    public final BetweenAndStep10 between(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return between(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final BetweenAndStep10 between(Row10 row) {
        return new RowBetweenCondition<>(this, row, false, false);
    }

    @Override
    public final BetweenAndStep10 between(Record10 record) {
        return between(record.valuesRow());
    }

    @Override
    public final Condition between(Row10 minValue, Row10 maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final Condition between(Record10 minValue, Record10 maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep10 betweenSymmetric(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return betweenSymmetric(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9))));
    }

    @Override
    public final BetweenAndStep10 betweenSymmetric(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return betweenSymmetric(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final BetweenAndStep10 betweenSymmetric(Row10 row) {
        return new RowBetweenCondition<>(this, row, false, true);
    }

    @Override
    public final BetweenAndStep10 betweenSymmetric(Record10 record) {
        return betweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition betweenSymmetric(Row10 minValue, Row10 maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition betweenSymmetric(Record10 minValue, Record10 maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep10 notBetween(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return notBetween(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9))));
    }

    @Override
    public final BetweenAndStep10 notBetween(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return notBetween(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final BetweenAndStep10 notBetween(Row10 row) {
        return new RowBetweenCondition<>(this, row, true, false);
    }

    @Override
    public final BetweenAndStep10 notBetween(Record10 record) {
        return notBetween(record.valuesRow());
    }

    @Override
    public final Condition notBetween(Row10 minValue, Row10 maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetween(Record10 minValue, Record10 maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep10 notBetweenSymmetric(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return notBetweenSymmetric(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9))));
    }

    @Override
    public final BetweenAndStep10 notBetweenSymmetric(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return notBetweenSymmetric(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final BetweenAndStep10 notBetweenSymmetric(Row10 row) {
        return new RowBetweenCondition<>(this, row, true, true);
    }

    @Override
    public final BetweenAndStep10 notBetweenSymmetric(Record10 record) {
        return notBetweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition notBetweenSymmetric(Row10 minValue, Row10 maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(Record10 minValue, Record10 maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNotDistinctFrom(Row10 row) {
        return new RowIsDistinctFrom(this, row, true);
    }

    @Override
    public final Condition isNotDistinctFrom(Record10 record) {
        return isNotDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isNotDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return isNotDistinctFrom(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9)));
    }

    @Override
    public final Condition isNotDistinctFrom(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return isNotDistinctFrom(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition isNotDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, true);
    }

    @Override
    public final Condition isDistinctFrom(Row10 row) {
        return new RowIsDistinctFrom(this, row, false);
    }

    @Override
    public final Condition isDistinctFrom(Record10 record) {
        return isDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return isDistinctFrom(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)), Tools.field(t4, (DataType) dataType(3)), Tools.field(t5, (DataType) dataType(4)), Tools.field(t6, (DataType) dataType(5)), Tools.field(t7, (DataType) dataType(6)), Tools.field(t8, (DataType) dataType(7)), Tools.field(t9, (DataType) dataType(8)), Tools.field(t10, (DataType) dataType(9)));
    }

    @Override
    public final Condition isDistinctFrom(Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return isDistinctFrom(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition isDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, false);
    }

    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition in(Row10... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Record10... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, false);
    }

    @Override
    public final Condition notIn(Row10... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Record10... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, true);
    }

    @Override
    public final Condition in(Collection rows) {
        return new RowInCondition(this, new QueryPartList<Row>(rows), false);
    }

    @Override
    public final Condition in(Result result) {
        return new RowInCondition(this, new QueryPartList<Row>(Tools.rows(result)), false);
    }

    @Override
    public final Condition notIn(Collection rows) {
        return new RowInCondition(this, new QueryPartList<Row>(rows), true);
    }

    @Override
    public final Condition notIn(Result result) {
        return new RowInCondition(this, new QueryPartList<Row>(Tools.rows(result)), true);
    }

    // ------------------------------------------------------------------------
    // Predicates involving subqueries
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Select select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition equal(QuantifiedSelect select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition eq(Select select) {
        return equal(select);
    }

    @Override
    public final Condition eq(QuantifiedSelect select) {
        return equal(select);
    }

    @Override
    public final Condition notEqual(Select select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition notEqual(QuantifiedSelect select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition ne(Select select) {
        return notEqual(select);
    }

    @Override
    public final Condition ne(QuantifiedSelect select) {
        return notEqual(select);
    }

    @Override
    public final Condition greaterThan(Select select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition gt(Select select) {
        return greaterThan(select);
    }

    @Override
    public final Condition gt(QuantifiedSelect select) {
        return greaterThan(select);
    }

    @Override
    public final Condition greaterOrEqual(Select select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition ge(Select select) {
        return greaterOrEqual(select);
    }

    @Override
    public final Condition ge(QuantifiedSelect select) {
        return greaterOrEqual(select);
    }

    @Override
    public final Condition lessThan(Select select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lt(Select select) {
        return lessThan(select);
    }

    @Override
    public final Condition lt(QuantifiedSelect select) {
        return lessThan(select);
    }

    @Override
    public final Condition lessOrEqual(Select select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition le(Select select) {
        return lessOrEqual(select);
    }

    @Override
    public final Condition le(QuantifiedSelect select) {
        return lessOrEqual(select);
    }

    @Override
    public final Condition in(Select select) {
        return compare(Comparator.IN, select);
    }

    @Override
    public final Condition notIn(Select select) {
        return compare(Comparator.NOT_IN, select);
    }

































}
