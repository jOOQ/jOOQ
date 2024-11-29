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

import org.jooq.BetweenAndStep3;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function3;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.Row3;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Statement;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class RowImpl3<T1, T2, T3>
extends
    AbstractRow<Record3<T1, T2, T3>>
implements
    Row3<T1, T2, T3> {

    RowImpl3(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3) {
        super(field1, field2, field3);
    }

    RowImpl3(FieldsImpl<?> fields) {
        super((FieldsImpl) fields);
    }

    // ------------------------------------------------------------------------
    // Mapping convenience methods
    // ------------------------------------------------------------------------

    @Override
    public final SelectField mapping(Function3 function) {
        return convertFrom(r -> r == null ? null : function.apply(r.value1(), r.value2(), r.value3()));
    }

    @Override
    public final SelectField mapping(Class uType, Function3 function) {
        return convertFrom(uType, r -> r == null ? null : function.apply(r.value1(), r.value2(), r.value3()));
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

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition compare(Comparator comparator, Row3<T1, T2, T3> row) {
        return compare(this, comparator, row);
    }

    @Override
    public final Condition compare(Comparator comparator, Record3<T1, T2, T3> record) {
        return compare(this, comparator, record.valuesRow());
    }

    @Override
    public final Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3) {
        return compare(comparator, row(Tools.field(t1, (DataType<T1>) dataType(0)), Tools.field(t2, (DataType<T2>) dataType(1)), Tools.field(t3, (DataType<T3>) dataType(2))));
    }

    @Override
    public final Condition compare(Comparator comparator, Field t1, Field t2, Field t3) {
        return compare(comparator, row(Tools.nullSafe(t1, dataType(0)), Tools.nullSafe(t2, dataType(1)), Tools.nullSafe(t3, dataType(2))));
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record3<T1, T2, T3>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record3<T1, T2, T3>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Row3 row) {
        return compare(Comparator.EQUALS, row);
    }

    @Override
    public final Condition equal(Record3 record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.EQUALS, t1, t2, t3);
    }

    @Override
    public final Condition equal(Field t1, Field t2, Field t3) {
        return compare(Comparator.EQUALS, t1, t2, t3);
    }

    @Override
    public final Condition eq(Row3 row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Record3 record) {
        return equal(record);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3) {
        return equal(t1, t2, t3);
    }

    @Override
    public final Condition eq(Field t1, Field t2, Field t3) {
        return equal(t1, t2, t3);
    }

    @Override
    public final Condition notEqual(Row3 row) {
        return compare(Comparator.NOT_EQUALS, row);
    }

    @Override
    public final Condition notEqual(Record3 record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3);
    }

    @Override
    public final Condition notEqual(Field t1, Field t2, Field t3) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3);
    }

    @Override
    public final Condition ne(Row3 row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Record3 record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3) {
        return notEqual(t1, t2, t3);
    }

    @Override
    public final Condition ne(Field t1, Field t2, Field t3) {
        return notEqual(t1, t2, t3);
    }

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition lessThan(Row3 row) {
        return compare(Comparator.LESS, row);
    }

    @Override
    public final Condition lessThan(Record3 record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.LESS, t1, t2, t3);
    }

    @Override
    public final Condition lessThan(Field t1, Field t2, Field t3) {
        return compare(Comparator.LESS, t1, t2, t3);
    }

    @Override
    public final Condition lt(Row3 row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Record3 record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3) {
        return lessThan(t1, t2, t3);
    }

    @Override
    public final Condition lt(Field t1, Field t2, Field t3) {
        return lessThan(t1, t2, t3);
    }

    @Override
    public final Condition lessOrEqual(Row3 row) {
        return compare(Comparator.LESS_OR_EQUAL, row);
    }

    @Override
    public final Condition lessOrEqual(Record3 record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3);
    }

    @Override
    public final Condition lessOrEqual(Field t1, Field t2, Field t3) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3);
    }

    @Override
    public final Condition le(Row3 row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Record3 record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3) {
        return lessOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition le(Field t1, Field t2, Field t3) {
        return lessOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition greaterThan(Row3 row) {
        return compare(Comparator.GREATER, row);
    }

    @Override
    public final Condition greaterThan(Record3 record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.GREATER, t1, t2, t3);
    }

    @Override
    public final Condition greaterThan(Field t1, Field t2, Field t3) {
        return compare(Comparator.GREATER, t1, t2, t3);
    }

    @Override
    public final Condition gt(Row3 row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Record3 record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3) {
        return greaterThan(t1, t2, t3);
    }

    @Override
    public final Condition gt(Field t1, Field t2, Field t3) {
        return greaterThan(t1, t2, t3);
    }

    @Override
    public final Condition greaterOrEqual(Row3 row) {
        return compare(Comparator.GREATER_OR_EQUAL, row);
    }

    @Override
    public final Condition greaterOrEqual(Record3 record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3);
    }

    @Override
    public final Condition greaterOrEqual(Field t1, Field t2, Field t3) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3);
    }

    @Override
    public final Condition ge(Row3 row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Record3 record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3) {
        return greaterOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition ge(Field t1, Field t2, Field t3) {
        return greaterOrEqual(t1, t2, t3);
    }

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    @Override
    public final BetweenAndStep3 between(T1 t1, T2 t2, T3 t3) {
        return between(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2))));
    }

    @Override
    public final BetweenAndStep3 between(Field t1, Field t2, Field t3) {
        return between(row(t1, t2, t3));
    }

    @Override
    public final BetweenAndStep3 between(Row3 row) {
        return new RowBetweenCondition<>(this, row, false, false);
    }

    @Override
    public final BetweenAndStep3 between(Record3 record) {
        return between(record.valuesRow());
    }

    @Override
    public final Condition between(Row3 minValue, Row3 maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final Condition between(Record3 minValue, Record3 maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep3 betweenSymmetric(T1 t1, T2 t2, T3 t3) {
        return betweenSymmetric(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2))));
    }

    @Override
    public final BetweenAndStep3 betweenSymmetric(Field t1, Field t2, Field t3) {
        return betweenSymmetric(row(t1, t2, t3));
    }

    @Override
    public final BetweenAndStep3 betweenSymmetric(Row3 row) {
        return new RowBetweenCondition<>(this, row, false, true);
    }

    @Override
    public final BetweenAndStep3 betweenSymmetric(Record3 record) {
        return betweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition betweenSymmetric(Row3 minValue, Row3 maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition betweenSymmetric(Record3 minValue, Record3 maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep3 notBetween(T1 t1, T2 t2, T3 t3) {
        return notBetween(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2))));
    }

    @Override
    public final BetweenAndStep3 notBetween(Field t1, Field t2, Field t3) {
        return notBetween(row(t1, t2, t3));
    }

    @Override
    public final BetweenAndStep3 notBetween(Row3 row) {
        return new RowBetweenCondition<>(this, row, true, false);
    }

    @Override
    public final BetweenAndStep3 notBetween(Record3 record) {
        return notBetween(record.valuesRow());
    }

    @Override
    public final Condition notBetween(Row3 minValue, Row3 maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetween(Record3 minValue, Record3 maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep3 notBetweenSymmetric(T1 t1, T2 t2, T3 t3) {
        return notBetweenSymmetric(row(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2))));
    }

    @Override
    public final BetweenAndStep3 notBetweenSymmetric(Field t1, Field t2, Field t3) {
        return notBetweenSymmetric(row(t1, t2, t3));
    }

    @Override
    public final BetweenAndStep3 notBetweenSymmetric(Row3 row) {
        return new RowBetweenCondition<>(this, row, true, true);
    }

    @Override
    public final BetweenAndStep3 notBetweenSymmetric(Record3 record) {
        return notBetweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition notBetweenSymmetric(Row3 minValue, Row3 maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(Record3 minValue, Record3 maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNotDistinctFrom(Row3 row) {
        return new RowIsDistinctFrom(this, row, true);
    }

    @Override
    public final Condition isNotDistinctFrom(Record3 record) {
        return isNotDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isNotDistinctFrom(T1 t1, T2 t2, T3 t3) {
        return isNotDistinctFrom(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)));
    }

    @Override
    public final Condition isNotDistinctFrom(Field t1, Field t2, Field t3) {
        return isNotDistinctFrom(row(t1, t2, t3));
    }

    @Override
    public final Condition isNotDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, true);
    }

    @Override
    public final Condition isDistinctFrom(Row3 row) {
        return new RowIsDistinctFrom(this, row, false);
    }

    @Override
    public final Condition isDistinctFrom(Record3 record) {
        return isDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isDistinctFrom(T1 t1, T2 t2, T3 t3) {
        return isDistinctFrom(Tools.field(t1, (DataType) dataType(0)), Tools.field(t2, (DataType) dataType(1)), Tools.field(t3, (DataType) dataType(2)));
    }

    @Override
    public final Condition isDistinctFrom(Field t1, Field t2, Field t3) {
        return isDistinctFrom(row(t1, t2, t3));
    }

    @Override
    public final Condition isDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, false);
    }

    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition in(Row3... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Record3... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, false);
    }

    @Override
    public final Condition notIn(Row3... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Record3... records) {
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
