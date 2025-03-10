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
import java.util.function.Function;

import org.jooq.BetweenAndStepN;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.RowN;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Statement;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class RowImplN
extends
    AbstractRow<Record>
implements
    RowN {

    RowImplN(SelectField<?>... fields) {
        super(fields);
    }

    RowImplN(Collection<? extends SelectField<?>> fields) {
        super(fields);
    }

    RowImplN(FieldsImpl<?> fields) {
        super((FieldsImpl) fields);
    }

    // ------------------------------------------------------------------------
    // Mapping convenience methods
    // ------------------------------------------------------------------------

    @Override
    public final SelectField mapping(Function function) {
        return convertFrom(r -> r == null ? null : function.apply(r.intoArray()));
    }

    @Override
    public final SelectField mapping(Class uType, Function function) {
        return convertFrom(uType, r -> r == null ? null : function.apply(r.intoArray()));
    }

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition compare(Comparator comparator, RowN row) {
        return compare(this, comparator, row);
    }

    @Override
    public final Condition compare(Comparator comparator, Record record) {
        return compare(this, comparator, record.valuesRow());
    }

    @Override
    public final Condition compare(Comparator comparator, Object... values) {
        return compare(comparator, row(Tools.fields(values, dataTypes())));
    }

    @Override
    public final Condition compare(Comparator comparator, Field<?>... values) {
        return compare(comparator, row(Tools.fields(values, dataTypes())));
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record> select) {
        return new RowSubqueryCondition(this, select, comparator);
    }

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(RowN row) {
        return compare(Comparator.EQUALS, row);
    }

    @Override
    public final Condition equal(Record record) {
        return compare(Comparator.EQUALS, record);
    }

    @Override
    public final Condition equal(Object... values) {
        return compare(Comparator.EQUALS, values);
    }

    @Override
    public final Condition equal(Field<?>... values) {
        return compare(Comparator.EQUALS, values);
    }

    @Override
    public final Condition eq(RowN row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Record record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Object... values) {
        return equal(values);
    }

    @Override
    public final Condition eq(Field<?>... values) {
        return equal(values);
    }

    @Override
    public final Condition notEqual(RowN row) {
        return compare(Comparator.NOT_EQUALS, row);
    }

    @Override
    public final Condition notEqual(Record record) {
        return compare(Comparator.NOT_EQUALS, record);
    }

    @Override
    public final Condition notEqual(Object... values) {
        return compare(Comparator.NOT_EQUALS, values);
    }

    @Override
    public final Condition notEqual(Field<?>... values) {
        return compare(Comparator.NOT_EQUALS, values);
    }

    @Override
    public final Condition ne(RowN row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Record record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Object... values) {
        return notEqual(values);
    }

    @Override
    public final Condition ne(Field<?>... values) {
        return notEqual(values);
    }

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition lessThan(RowN row) {
        return compare(Comparator.LESS, row);
    }

    @Override
    public final Condition lessThan(Record record) {
        return compare(Comparator.LESS, record);
    }

    @Override
    public final Condition lessThan(Object... values) {
        return compare(Comparator.LESS, values);
    }

    @Override
    public final Condition lessThan(Field<?>... values) {
        return compare(Comparator.LESS, values);
    }

    @Override
    public final Condition lt(RowN row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Record record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Object... values) {
        return lessThan(values);
    }

    @Override
    public final Condition lt(Field<?>... values) {
        return lessThan(values);
    }

    @Override
    public final Condition lessOrEqual(RowN row) {
        return compare(Comparator.LESS_OR_EQUAL, row);
    }

    @Override
    public final Condition lessOrEqual(Record record) {
        return compare(Comparator.LESS_OR_EQUAL, record);
    }

    @Override
    public final Condition lessOrEqual(Object... values) {
        return compare(Comparator.LESS_OR_EQUAL, values);
    }

    @Override
    public final Condition lessOrEqual(Field<?>... values) {
        return compare(Comparator.LESS_OR_EQUAL, values);
    }

    @Override
    public final Condition le(RowN row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Record record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Object... values) {
        return lessOrEqual(values);
    }

    @Override
    public final Condition le(Field<?>... values) {
        return lessOrEqual(values);
    }

    @Override
    public final Condition greaterThan(RowN row) {
        return compare(Comparator.GREATER, row);
    }

    @Override
    public final Condition greaterThan(Record record) {
        return compare(Comparator.GREATER, record);
    }

    @Override
    public final Condition greaterThan(Object... values) {
        return compare(Comparator.GREATER, values);
    }

    @Override
    public final Condition greaterThan(Field<?>... values) {
        return compare(Comparator.GREATER, values);
    }

    @Override
    public final Condition gt(RowN row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Record record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Object... values) {
        return greaterThan(values);
    }

    @Override
    public final Condition gt(Field<?>... values) {
        return greaterThan(values);
    }

    @Override
    public final Condition greaterOrEqual(RowN row) {
        return compare(Comparator.GREATER_OR_EQUAL, row);
    }

    @Override
    public final Condition greaterOrEqual(Record record) {
        return compare(Comparator.GREATER_OR_EQUAL, record);
    }

    @Override
    public final Condition greaterOrEqual(Object... values) {
        return compare(Comparator.GREATER_OR_EQUAL, values);
    }

    @Override
    public final Condition greaterOrEqual(Field<?>... values) {
        return compare(Comparator.GREATER_OR_EQUAL, values);
    }

    @Override
    public final Condition ge(RowN row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Record record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Object... values) {
        return greaterOrEqual(values);
    }

    @Override
    public final Condition ge(Field<?>... values) {
        return greaterOrEqual(values);
    }

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    @Override
    public final BetweenAndStepN between(Object... values) {
        return between(row(Tools.fieldsArray(values, dataTypes())));
    }

    @Override
    public final BetweenAndStepN between(Field<?>... values) {
        return between(row(values));
    }

    @Override
    public final BetweenAndStepN between(RowN row) {
        return new RowBetweenCondition<>(this, row, false, false);
    }

    @Override
    public final BetweenAndStepN between(Record record) {
        return between(record.valuesRow());
    }

    @Override
    public final Condition between(RowN minValue, RowN maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final Condition between(Record minValue, Record maxValue) {
        return between(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStepN betweenSymmetric(Object... values) {
        return betweenSymmetric(row(Tools.fieldsArray(values, dataTypes())));
    }

    @Override
    public final BetweenAndStepN betweenSymmetric(Field<?>... values) {
        return betweenSymmetric(row(values));
    }

    @Override
    public final BetweenAndStepN betweenSymmetric(RowN row) {
        return new RowBetweenCondition<>(this, row, false, true);
    }

    @Override
    public final BetweenAndStepN betweenSymmetric(Record record) {
        return betweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition betweenSymmetric(RowN minValue, RowN maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition betweenSymmetric(Record minValue, Record maxValue) {
        return betweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStepN notBetween(Object... values) {
        return notBetween(row(Tools.fieldsArray(values, dataTypes())));
    }

    @Override
    public final BetweenAndStepN notBetween(Field<?>... values) {
        return notBetween(row(values));
    }

    @Override
    public final BetweenAndStepN notBetween(RowN row) {
        return new RowBetweenCondition<>(this, row, true, false);
    }

    @Override
    public final BetweenAndStepN notBetween(Record record) {
        return notBetween(record.valuesRow());
    }

    @Override
    public final Condition notBetween(RowN minValue, RowN maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetween(Record minValue, Record maxValue) {
        return notBetween(minValue).and(maxValue);
    }

    @Override
    public final BetweenAndStepN notBetweenSymmetric(Object... values) {
        return notBetweenSymmetric(row(Tools.fieldsArray(values, dataTypes())));
    }

    @Override
    public final BetweenAndStepN notBetweenSymmetric(Field<?>... values) {
        return notBetweenSymmetric(row(values));
    }

    @Override
    public final BetweenAndStepN notBetweenSymmetric(RowN row) {
        return new RowBetweenCondition<>(this, row, true, true);
    }

    @Override
    public final BetweenAndStepN notBetweenSymmetric(Record record) {
        return notBetweenSymmetric(record.valuesRow());
    }

    @Override
    public final Condition notBetweenSymmetric(RowN minValue, RowN maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(Record minValue, Record maxValue) {
        return notBetweenSymmetric(minValue).and(maxValue);
    }

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNotDistinctFrom(RowN row) {
        return new RowIsDistinctFrom(this, row, true);
    }

    @Override
    public final Condition isNotDistinctFrom(Record record) {
        return isNotDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isNotDistinctFrom(Object... values) {
        return isNotDistinctFrom(Tools.fieldsArray(values, dataTypes()));
    }

    @Override
    public final Condition isNotDistinctFrom(Field<?>... values) {
        return isNotDistinctFrom(row(values));
    }

    @Override
    public final Condition isNotDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, true);
    }

    @Override
    public final Condition isDistinctFrom(RowN row) {
        return new RowIsDistinctFrom(this, row, false);
    }

    @Override
    public final Condition isDistinctFrom(Record record) {
        return isDistinctFrom(record.valuesRow());
    }

    @Override
    public final Condition isDistinctFrom(Object... values) {
        return isDistinctFrom(Tools.fieldsArray(values, dataTypes()));
    }

    @Override
    public final Condition isDistinctFrom(Field<?>... values) {
        return isDistinctFrom(row(values));
    }

    @Override
    public final Condition isDistinctFrom(Select select) {
        return new RowIsDistinctFrom(this, select, false);
    }

    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition in(RowN... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Record... records) {
        QueryPartList<Row> rows = new QueryPartList<>();

        for (Record record : records)
            rows.add(record.valuesRow());

        return new RowInCondition(this, rows, false);
    }

    @Override
    public final Condition notIn(RowN... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Record... records) {
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
