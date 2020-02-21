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

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.val;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.BetweenAndStep6;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Record6;
import org.jooq.Row;
import org.jooq.Row6;
import org.jooq.Result;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class RowImpl6<T1, T2, T3, T4, T5, T6> extends AbstractRow implements Row6<T1, T2, T3, T4, T5, T6> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -929427349071556318L;
    
    RowImpl6(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        super(t1, t2, t3, t4, t5, t6);
    }

    // ------------------------------------------------------------------------
    // XXX: Row accessor API
    // ------------------------------------------------------------------------

    @Override
    public final Field<T1> field1() {
        return (Field<T1>) fields.field(0);
    }

    @Override
    public final Field<T2> field2() {
        return (Field<T2>) fields.field(1);
    }

    @Override
    public final Field<T3> field3() {
        return (Field<T3>) fields.field(2);
    }

    @Override
    public final Field<T4> field4() {
        return (Field<T4>) fields.field(3);
    }

    @Override
    public final Field<T5> field5() {
        return (Field<T5>) fields.field(4);
    }

    @Override
    public final Field<T6> field6() {
        return (Field<T6>) fields.field(5);
    }

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition compare(Comparator comparator, Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(comparator, row(val(t1, (DataType<T1>) dataType(0)), val(t2, (DataType<T2>) dataType(1)), val(t3, (DataType<T3>) dataType(2)), val(t4, (DataType<T4>) dataType(3)), val(t5, (DataType<T5>) dataType(4)), val(t6, (DataType<T6>) dataType(5))));
    }

    @Override
    public final Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(comparator, row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.EQUALS, row);
    }

    @Override
    public final Condition equal(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.EQUALS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.EQUALS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition eq(Row6<T1, T2, T3, T4, T5, T6> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Record6<T1, T2, T3, T4, T5, T6> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return equal(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return equal(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition notEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.NOT_EQUALS, row);
    }

    @Override
    public final Condition notEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.NOT_EQUALS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ne(Row6<T1, T2, T3, T4, T5, T6> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Record6<T1, T2, T3, T4, T5, T6> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notEqual(t1, t2, t3, t4, t5, t6);
    }

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition lessThan(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.LESS, row);
    }

    @Override
    public final Condition lessThan(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.LESS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.LESS, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lt(Row6<T1, T2, T3, T4, T5, T6> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Record6<T1, T2, T3, T4, T5, T6> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lessOrEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.LESS_OR_EQUAL, row);
    }

    @Override
    public final Condition lessOrEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.LESS_OR_EQUAL, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition le(Row6<T1, T2, T3, T4, T5, T6> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Record6<T1, T2, T3, T4, T5, T6> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition greaterThan(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.GREATER, row);
    }

    @Override
    public final Condition greaterThan(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.GREATER, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.GREATER, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition gt(Row6<T1, T2, T3, T4, T5, T6> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Record6<T1, T2, T3, T4, T5, T6> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition greaterOrEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return compare(Comparator.GREATER_OR_EQUAL, row);
    }

    @Override
    public final Condition greaterOrEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return compare(Comparator.GREATER_OR_EQUAL, t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ge(Row6<T1, T2, T3, T4, T5, T6> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Record6<T1, T2, T3, T4, T5, T6> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6);
    }

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return between(row(val(t1, (DataType) dataType(0)), val(t2, (DataType) dataType(1)), val(t3, (DataType) dataType(2)), val(t4, (DataType) dataType(3)), val(t5, (DataType) dataType(4)), val(t6, (DataType) dataType(5))));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return between(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowBetweenCondition<>(this, row, false, false);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> between(Record6<T1, T2, T3, T4, T5, T6> record) {
        return between(record.valuesRow());
    }

    @Override
    public final Condition between(Row6<T1, T2, T3, T4, T5, T6> minValue, Row6<T1, T2, T3, T4, T5, T6> maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final Condition between(Record6<T1, T2, T3, T4, T5, T6> minValue, Record6<T1, T2, T3, T4, T5, T6> maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return betweenSymmetric(row(val(t1, (DataType) dataType(0)), val(t2, (DataType) dataType(1)), val(t3, (DataType) dataType(2)), val(t4, (DataType) dataType(3)), val(t5, (DataType) dataType(4)), val(t6, (DataType) dataType(5))));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return betweenSymmetric(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowBetweenCondition<>(this, row, false, true);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> betweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> record) {
        return betweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition betweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue, Row6<T1, T2, T3, T4, T5, T6> maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition betweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue, Record6<T1, T2, T3, T4, T5, T6> maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notBetween(row(val(t1, (DataType) dataType(0)), val(t2, (DataType) dataType(1)), val(t3, (DataType) dataType(2)), val(t4, (DataType) dataType(3)), val(t5, (DataType) dataType(4)), val(t6, (DataType) dataType(5))));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notBetween(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowBetweenCondition<>(this, row, true, false);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetween(Record6<T1, T2, T3, T4, T5, T6> record) {
        return notBetween(record.valuesRow());
    }

    @Override
    public final Condition notBetween(Row6<T1, T2, T3, T4, T5, T6> minValue, Row6<T1, T2, T3, T4, T5, T6> maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetween(Record6<T1, T2, T3, T4, T5, T6> minValue, Record6<T1, T2, T3, T4, T5, T6> maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notBetweenSymmetric(row(val(t1, (DataType) dataType(0)), val(t2, (DataType) dataType(1)), val(t3, (DataType) dataType(2)), val(t4, (DataType) dataType(3)), val(t5, (DataType) dataType(4)), val(t6, (DataType) dataType(5))));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notBetweenSymmetric(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowBetweenCondition<>(this, row, true, true);
    }

    @Override
    public final BetweenAndStep6<T1, T2, T3, T4, T5, T6> notBetweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> record) {
        return notBetweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition notBetweenSymmetric(Row6<T1, T2, T3, T4, T5, T6> minValue, Row6<T1, T2, T3, T4, T5, T6> maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(Record6<T1, T2, T3, T4, T5, T6> minValue, Record6<T1, T2, T3, T4, T5, T6> maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNotDistinctFrom(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowIsDistinctFrom(this, row, true);
    }

    @Override
    public final Condition isNotDistinctFrom(Record6<T1, T2, T3, T4, T5, T6> record) {
        return isNotDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isNotDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return isNotDistinctFrom(val(t1, (DataType<T1>) dataType(0)), val(t2, (DataType<T2>) dataType(1)), val(t3, (DataType<T3>) dataType(2)), val(t4, (DataType<T4>) dataType(3)), val(t5, (DataType<T5>) dataType(4)), val(t6, (DataType<T6>) dataType(5)));
    }

    @Override
    public final Condition isNotDistinctFrom(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return isNotDistinctFrom(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition isDistinctFrom(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowIsDistinctFrom(this, row, false);
    }

    @Override
    public final Condition isDistinctFrom(Record6<T1, T2, T3, T4, T5, T6> record) {
        return isDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isDistinctFrom(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return isDistinctFrom(val(t1, (DataType<T1>) dataType(0)), val(t2, (DataType<T2>) dataType(1)), val(t3, (DataType<T3>) dataType(2)), val(t4, (DataType<T4>) dataType(3)), val(t5, (DataType<T5>) dataType(4)), val(t6, (DataType<T6>) dataType(5)));
    }

    @Override
    public final Condition isDistinctFrom(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return isDistinctFrom(row(t1, t2, t3, t4, t5, t6));
    }

    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition in(Row6<T1, T2, T3, T4, T5, T6>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Record6<T1, T2, T3, T4, T5, T6>... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, false);
    }

    @Override
    public final Condition notIn(Row6<T1, T2, T3, T4, T5, T6>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Record6<T1, T2, T3, T4, T5, T6>... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, true);
    }

    @Override
    public final Condition in(Collection<? extends Row6<T1, T2, T3, T4, T5, T6>> rows) {
        return new RowInCondition(this, new QueryPartList<Row>(rows), false);
    }

    @Override
    public final Condition in(Result<? extends Record6<T1, T2, T3, T4, T5, T6>> result) {
        return new RowInCondition(this, new QueryPartList<Row>(Tools.rows(result)), false);
    }

    @Override
    public final Condition notIn(Collection<? extends Row6<T1, T2, T3, T4, T5, T6>> rows) {
        return new RowInCondition(this, new QueryPartList<Row>(rows), true);
    }

    @Override
    public final Condition notIn(Result<? extends Record6<T1, T2, T3, T4, T5, T6>> result) {
        return new RowInCondition(this, new QueryPartList<Row>(Tools.rows(result)), true);
    }

    // ------------------------------------------------------------------------
    // Predicates involving subqueries
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition equal(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.EQUALS, select);
    }

    @Override
    public final Condition eq(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return equal(select);
    }

    @Override
    public final Condition eq(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return equal(select);
    }

    @Override
    public final Condition notEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition notEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.NOT_EQUALS, select);
    }

    @Override
    public final Condition ne(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return notEqual(select);
    }

    @Override
    public final Condition ne(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return notEqual(select);
    }

    @Override
    public final Condition greaterThan(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.GREATER, select);
    }

    @Override
    public final Condition gt(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return greaterThan(select);
    }

    @Override
    public final Condition gt(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return greaterThan(select);
    }

    @Override
    public final Condition greaterOrEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.GREATER_OR_EQUAL, select);
    }

    @Override
    public final Condition ge(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return greaterOrEqual(select);
    }

    @Override
    public final Condition ge(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return greaterOrEqual(select);
    }

    @Override
    public final Condition lessThan(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.LESS, select);
    }

    @Override
    public final Condition lt(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return lessThan(select);
    }

    @Override
    public final Condition lt(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return lessThan(select);
    }

    @Override
    public final Condition lessOrEqual(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.LESS_OR_EQUAL, select);
    }

    @Override
    public final Condition le(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return lessOrEqual(select);
    }

    @Override
    public final Condition le(QuantifiedSelect<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return lessOrEqual(select);
    }

    @Override
    public final Condition in(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.IN, select);
    }

    @Override
    public final Condition notIn(Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select) {
        return compare(Comparator.NOT_IN, select);
    }
}
