/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.DIVIDE;
import static org.jooq.impl.ExpressionOperator.MULTIPLY;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.BindContext;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.WindowPartitionByStep;

abstract class AbstractField<T> extends AbstractNamedTypeProviderQueryPart<T> implements Field<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2884811923648354905L;

    AbstractField(String name, DataType<T> type) {
        super(name, type);
    }

    // ------------------------------------------------------------------------
    // API (not implemented)
    // ------------------------------------------------------------------------

    @Override
    public abstract void toSQL(RenderContext context);

    @Override
    public abstract void bind(BindContext context) throws SQLException;

    @Override
    public abstract boolean isNullLiteral();

    // ------------------------------------------------------------------------
    // API
    // ------------------------------------------------------------------------

    @Override
    public Field<T> as(String alias) {
        return new FieldAlias<T>(this, alias);
    }

    // ------------------------------------------------------------------------
    // Type casts
    // ------------------------------------------------------------------------

    @Override
    public final <Z> Field<Z> cast(Field<Z> field) {
        return cast(field.getDataType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Field<Z> cast(DataType<Z> type) {

        // [#473] Prevent unnecessary casts
        if (getDataType().equals(type)) {
            return (Field<Z>) this;
        }
        else {
            return new Cast<Z>(this, type);
        }
    }

    @Override
    public final <Z> Field<Z> cast(Class<? extends Z> type) {
        return cast(SQLDataType.getDataType(null, type));
    }

    // ------------------------------------------------------------------------
    // Conversion of field into a sort field
    // ------------------------------------------------------------------------

    @Override
    public final SortField<T> asc() {
        return new SortFieldImpl<T>(this, SortOrder.ASC);
    }

    @Override
    public final SortField<T> desc() {
        return new SortFieldImpl<T>(this, SortOrder.DESC);
    }

    @Override
    public final SortField<Integer> sortAsc(List<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<T, Integer>();

        for (int i = 0; i < sortList.size(); i++) {
            map.put(sortList.get(i), i);
        }

        return sort(map);
    }

    @Override
    public final SortField<Integer> sortAsc(T... sortList) {
        return sortAsc(Arrays.asList(sortList));
    }

    @Override
    public final SortField<Integer> sortDesc(List<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<T, Integer>();

        for (int i = 0; i < sortList.size(); i++) {
            map.put(sortList.get(i), -i);
        }

        return sort(map);
    }

    @Override
    public final SortField<Integer> sortDesc(T... sortList) {
        return sortDesc(Arrays.asList(sortList));
    }

    @Override
    public final <Z> SortField<Z> sort(Map<T, Z> sortMap) {
        CaseValueStep<T> decode = create().decode().value(this);
        CaseWhenStep<T, Z> result = null;

        for (Entry<T, Z> entry : sortMap.entrySet()) {
            if (result == null) {
                result = decode.when(entry.getKey(), entry.getValue());
            }
            else {
                result.when(entry.getKey(), entry.getValue());
            }
        }

        if (result == null) {
            return null;
        }
        else {
            return result.asc();
        }
    }

    // ------------------------------------------------------------------------
    // Arithmetic expressions
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> neg() {
        return new Neg<T>(this);
    }

    @Override
    public final Field<T> add(Number value) {

        // Date time arithmetic
        if (java.util.Date.class.isAssignableFrom(getType())) {
            return new DateAdd<T>(this, value);
        }

        // Numeric arithmetic
        else {
            return add(val(value));
        }
    }

    /**
     * This default implementation is known to be overridden by
     * {@link Expression} to generate neater expressions
     */
    @Override
    public Field<T> add(Field<? extends Number> value) {
        return new Expression<T>(ADD, this, value);
    }

    @Override
    public final Field<T> sub(Number value) {

        // Date time arithmetic
        if (java.util.Date.class.isAssignableFrom(getType())) {
            return new DateSub<T>(this, value);
        }

        // Numeric arithmetic
        else {
            return sub(val(value));
        }
    }

    @Override
    public final Field<T> sub(Field<? extends Number> value) {
        return new Expression<T>(SUBTRACT, this, value);
    }

    @Override
    public final Field<T> mul(Number value) {
        return mul(val(value));
    }

    /**
     * This default implementation is known to be overridden by
     * {@link Expression} to generate neater expressions
     */
    @Override
    public Field<T> mul(Field<? extends Number> value) {
        return new Expression<T>(MULTIPLY, this, value);
    }

    @Override
    public final Field<T> div(Number value) {
        return div(val(value));
    }

    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return new Expression<T>(DIVIDE, this, value);
    }

    @Override
    public final Field<T> mod(Number value) {
        return mod(val(value));
    }

    @Override
    public final Field<T> mod(Field<? extends Number> value) {
        return new Mod<T>(this, value);
    }

    // ------------------------------------------------------------------------
    // Window functions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final WindowPartitionByStep<Integer> countOver() {
        return new WindowFunction<Integer>("count", SQLDataType.INTEGER, this);
    }

    @Override
    public final WindowPartitionByStep<T> maxOver() {
        return new WindowFunction<T>("max", getDataType(), this);
    }

    @Override
    public final WindowPartitionByStep<T> minOver() {
        return new WindowFunction<T>("min", getDataType(), this);
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> sumOver() {
        return new WindowFunction<BigDecimal>("sum", SQLDataType.NUMERIC, this);
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> avgOver() {
        return new WindowFunction<BigDecimal>("avg", SQLDataType.NUMERIC, this);
    }

    @Override
    public final WindowFunction<T> firstValue() {
        return new WindowFunction<T>("first_value", getDataType(), this);
    }

    @Override
    public final WindowFunction<T> lastValue() {
        return new WindowFunction<T>("last_value", getDataType(), this);
    }

    @Override
    public final WindowFunction<T> lead() {
        return new WindowFunction<T>("lead", getDataType(), this);
    }

    @Override
    public final WindowFunction<T> lead(int offset) {
        return new WindowFunction<T>("lead", getDataType(), this, literal(offset));
    }

    @Override
    public final WindowFunction<T> lead(int offset, T defaultValue) {
        return lead(offset, val(defaultValue));
    }

    @Override
    public final WindowFunction<T> lead(int offset, Field<T> defaultValue) {
        if (defaultValue == null) {
            return lead(offset, (T) null);
        }

        return new WindowFunction<T>("lead", getDataType(), this, literal(offset), defaultValue);
    }

    @Override
    public final WindowFunction<T> lag() {
        return new WindowFunction<T>("lag", getDataType(), this);
    }

    @Override
    public final WindowFunction<T> lag(int offset) {
        return new WindowFunction<T>("lag", getDataType(), this, literal(offset));
    }

    @Override
    public final WindowFunction<T> lag(int offset, T defaultValue) {
        return lead(offset, val(defaultValue));
    }

    @Override
    public final WindowFunction<T> lag(int offset, Field<T> defaultValue) {
        if (defaultValue == null) {
            return lead(offset, (T) null);
        }

        return new WindowFunction<T>("lag", getDataType(), this, literal(offset), defaultValue);
    }

    // ------------------------------------------------------------------------
    // Functions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final Field<Integer> count() {
        return new Count(this, false);
    }

    @Override
    public final Field<Integer> countDistinct() {
        return new Count(this, true);
    }

    @Override
    public final Field<T> max() {
        return new Function<T>("max", getDataType(), this);
    }

    @Override
    public final Field<T> min() {
        return new Function<T>("min", getDataType(), this);
    }

    @Override
    public final Field<BigDecimal> sum() {
        return new Function<BigDecimal>("sum", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> avg() {
        return new Function<BigDecimal>("avg", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<Integer> sign() {
        return new Sign(this);
    }

    @Override
    public final Field<T> abs() {
        return new Function<T>("abs", getDataType(), this);
    }

    @Override
    public final Field<T> round() {
        return new Round<T>(this);
    }

    @Override
    public final Field<T> round(int decimals) {
        return new Round<T>(this, decimals);
    }

    @Override
    public final Field<T> floor() {
        return new Floor<T>(this);
    }

    @Override
    public final Field<T> ceil() {
        return new Ceil<T>(this);
    }

    // ------------------------------------------------------------------------
    // Mathematical functions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> greatest(T... others) {
        return greatest(vals(others).toArray(new Field<?>[0]));
    }

    @Override
    public final Field<T> greatest(Field<?>... others) {
        return new Greatest<T>(getDataType(), JooqUtil.combine(this, others));
    }

    @Override
    public final Field<T> least(T... others) {
        return least(vals(others).toArray(new Field<?>[0]));
    }

    @Override
    public final Field<T> least(Field<?>... others) {
        return new Least<T>(getDataType(), JooqUtil.combine(this, others));
    }

    @Override
    public final Field<BigDecimal> sqrt() {
        return new Sqrt(this);
    }

    @Override
    public final Field<BigDecimal> exp() {
        return new Function<BigDecimal>("exp", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> ln() {
        return new Ln(this);
    }

    @Override
    public final Field<BigDecimal> log(int base) {
        return new Ln(this, base);
    }

    @Override
    public final Field<BigDecimal> power(Number exponent) {
        return new Power(this, exponent);
    }

    @Override
    public final Field<BigDecimal> acos() {
        return new Function<BigDecimal>("acos", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> asin() {
        return new Function<BigDecimal>("asin", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> atan() {
        return new Function<BigDecimal>("atan", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> atan2(Number y) {
        return atan2(val(y));
    }

    @Override
    public final Field<BigDecimal> atan2(Field<? extends Number> y) {
        if (y == null) {
            return atan2((Number) null);
        }

        return new Atan2(this, y);
    }

    @Override
    public final Field<BigDecimal> cos() {
        return new Function<BigDecimal>("cos", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> sin() {
        return new Function<BigDecimal>("sin", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> tan() {
        return new Function<BigDecimal>("tan", SQLDataType.NUMERIC, this);
    }

    @Override
    public final Field<BigDecimal> cot() {
        return new Cot(this);
    }

    @Override
    public final Field<BigDecimal> sinh() {
        return new Sinh(this);
    }

    @Override
    public final Field<BigDecimal> cosh() {
        return new Cosh(this);
    }

    @Override
    public final Field<BigDecimal> tanh() {
        return new Tanh(this);
    }

    @Override
    public final Field<BigDecimal> coth() {
        return mul(2).exp().add(1).div(mul(2).exp().sub(1));
    }

    @Override
    public final Field<BigDecimal> deg() {
        return new Degrees(this);
    }

    @Override
    public final Field<BigDecimal> rad() {
        return new Radians(this);
    }

    // ------------------------------------------------------------------------
    // Other functions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final Field<String> upper() {
        return new Function<String>("upper", SQLDataType.VARCHAR, this);
    }

    @Override
    public final Field<String> lower() {
        return new Function<String>("lower", SQLDataType.VARCHAR, this);
    }

    @Override
    public final Field<String> trim() {
        return new Trim(this);
    }

    @Override
    public final Field<String> rtrim() {
        return new Function<String>("rtrim", SQLDataType.VARCHAR, this);
    }

    @Override
    public final Field<String> ltrim() {
        return new Function<String>("ltrim", SQLDataType.VARCHAR, this);
    }

    @Override
    public final Field<String> rpad(Field<? extends Number> length) {
        return new Rpad(this, length, null);
    }

    @Override
    public final Field<String> rpad(int length) {
        return rpad(val(length));
    }

    @Override
    public final Field<String> rpad(Field<? extends Number> length, Field<String> c) {
        return new Rpad(this, length, c);
    }

    @Override
    public final Field<String> rpad(int length, char c) {
        return rpad(val(length), val("" + c));
    }

    @Override
    public final Field<String> lpad(Field<? extends Number> length) {
        return new Lpad(this, length, null);
    }

    @Override
    public final Field<String> lpad(int length) {
        return lpad(val(length));
    }

    @Override
    public final Field<String> lpad(Field<? extends Number> length, Field<String> c) {
        return new Lpad(this, length, c);
    }

    @Override
    public final Field<String> lpad(int length, char c) {
        return lpad(val(length), val("" + c));
    }

    @Override
    public final Field<String> repeat(Number count) {
        return repeat(val(count));
    }

    @Override
    public final Field<String> repeat(Field<? extends Number> count) {
        return new Repeat(this, count);
    }

    @Override
    public final Field<String> replace(Field<String> search) {
        return new Replace(this, search);
    }

    @Override
    public final Field<String> replace(String search) {
        return replace(val(search));
    }

    @Override
    public final Field<String> replace(Field<String> search, Field<String> replace) {
        return new Replace(this, search, replace);
    }

    @Override
    public final Field<String> replace(String search, String replace) {
        return replace(val(search), val(replace));
    }

    @Override
    public final Field<Integer> position(String search) throws SQLDialectNotSupportedException {
        return position(val(search));
    }

    @Override
    public final Field<Integer> position(Field<String> search) throws SQLDialectNotSupportedException {
        return new Position(search, this);
    }

    @Override
    public final Field<Integer> ascii() {
        return new Function<Integer>("ascii", SQLDataType.INTEGER, this);
    }

    /**
     * This default implementation is known to be overridden by
     * {@link Expression} to generate neater expressions
     */
    @Override
    public final Field<String> concat(Field<?>... fields) {
        return new Concat(JooqUtil.combine(this, fields));
    }

    @Override
    public final Field<String> concat(String... values) {
        return concat(create().vals((Object[]) values).toArray(new Field[0]));
    }

    @Override
    public final Field<String> substring(int startingPosition) {
        return substring(val(startingPosition));
    }

    @Override
    public final Field<String> substring(int startingPosition, int length) {
        return substring(val(startingPosition), val(length));
    }

    @Override
    public final Field<String> substring(Field<? extends Number> startingPosition) {
        return new Substring(this, startingPosition);
    }

    @Override
    public final Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return new Substring(this, startingPosition, length);
    }

    @Override
    public final Field<Integer> length() {
        return charLength();
    }

    @Override
    public final Field<Integer> charLength() {
        return new CharLength(this);
    }

    @Override
    public final Field<Integer> bitLength() {
        return new BitLength(this);
    }

    @Override
    public final Field<Integer> octetLength() {
        return new OctetLength(this);
    }

    @Override
    public final Field<Integer> extract(DatePart datePart) {
        return new Extract(this, datePart);
    }

    @Override
    public final Field<T> nvl(Field<T> defaultValue) {
        if (defaultValue == null) {
            return nvl((T) null);
        }

        return new Nvl<T>(this, defaultValue);
    }

    @Override
    public final Field<T> nvl(T defaultValue) {
        return nvl(val(defaultValue));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        if (valueIfNotNull == null || valueIfNull == null) {
            return nvl2(val((Z) valueIfNotNull), val((Z) valueIfNull));
        }

        return new Nvl2<Z>(this, valueIfNotNull, valueIfNull);
    }

    @Override
    public final <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull) {
        return nvl2(val(valueIfNotNull), val(valueIfNull));
    }

    @Override
    public final Field<T> nullif(T other) {
        return nullif(val(other));
    }

    @Override
    public final Field<T> nullif(Field<T> other) {
        return new Function<T>("nullif", getDataType(), this, other);
    }

    @Override
    public final <Z> Field<Z> decode(T search, Z result) {
        return decode(search, result, new Object[0]);
    }

    @Override
    public final <Z> Field<Z> decode(T search, Z result, Object... more) {
        return decode(
            val(search),
            val(result),
            vals(more).toArray(new Field<?>[0]));
    }

    @Override
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result) {
        return decode(search, result, new Field<?>[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more) {
        if (search == null || result == null) {
            return decode(val((T) search), val((Z) result), more);
        }

        return new Decode<T, Z>(this, search, result, more);
    }

    @Override
    public final Field<T> coalesce(T option, T... options) {
        return coalesce(val(option), vals(options).toArray(new Field<?>[0]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> coalesce(Field<T> option, Field<?>... options) {
        if (option == null) {
            return coalesce(val((T) option), options);
        }

        Field<?>[] arguments = new Field<?>[options.length + 2];
        arguments[0] = this;
        arguments[1] = option;
        System.arraycopy(options, 0, arguments, 2, options.length);
        return new Function<T>("coalesce", getDataType(), arguments);
    }

    // ------------------------------------------------------------------------
    // Conditions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNull() {
        return equal((T) null);
    }

    @Override
    public final Condition isNotNull() {
        return notEqual((T) null);
    }

    @Override
    public final Condition like(T value) {
        return new CompareCondition<T>(this, val(value), Comparator.LIKE);
    }

    @Override
    public final Condition notLike(T value) {
        return new CompareCondition<T>(this, val(value), Comparator.NOT_LIKE);
    }

    @Override
    public final Condition in(T... values) {
        return in(vals(values).toArray(new Field<?>[0]));
    }

    @Override
    public final Condition in(Field<?>... values) {
        if (values == null || values.length == 0) {
            return create().falseCondition();
        }
        else {
            return new InCondition<T>(this, values, InOperator.IN);
        }
    }

    @Override
    public final Condition in(Collection<T> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (T value : values) {
            fields.add(val(value));
        }

        return in(fields.toArray(new Field<?>[0]));
    }

    @Override
    public final Condition in(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.IN);
    }

    @Override
    public final Condition notIn(T... values) {
        if (values == null || values.length == 0) {
            return create().trueCondition();
        }
        else {
            return notIn(vals(values).toArray(new Field<?>[0]));
        }
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return new InCondition<T>(this, values, InOperator.NOT_IN);
    }

    @Override
    public final Condition notIn(Collection<T> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (T value : values) {
            fields.add(val(value));
        }

        return notIn(fields.toArray(new Field<?>[0]));
    }

    @Override
    public final Condition notIn(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(
            query, this, SubQueryOperator.NOT_IN);
    }

    @Override
    public final Condition between(T minValue, T maxValue) {
        return between(val(minValue), val(maxValue));
    }

    @Override
    public final Condition between(Field<T> minValue, Field<T> maxValue) {
        return new BetweenCondition<T>(this, minValue, maxValue);
    }

    @Override
    public final Condition equal(T value) {
        return equal(val(value));
    }

    @Override
    public final Condition equal(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.EQUALS);
    }

    @Override
    public final Condition equalAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.EQUALS_ANY);
    }

    @Override
    public final Condition equalSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.EQUALS_SOME);
    }

    @Override
    public final Condition equalAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.EQUALS_ALL);
    }

    @Override
    public final Condition notEqual(T value) {
        return notEqual(val(value));
    }

    @Override
    public final Condition notEqual(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqualAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.NOT_EQUALS_ALL);
    }

    @Override
    public final Condition notEqualSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.NOT_EQUALS_SOME);
    }

    @Override
    public final Condition notEqualAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.NOT_EQUALS_ALL);
    }

    @Override
    public final Condition lessThan(T value) {
        return lessThan(val(value));
    }

    @Override
    public final Condition lessThan(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS);
    }

    @Override
    public final Condition lessThanAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_THAN_ANY);
    }

    @Override
    public final Condition lessThanSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_THAN_SOME);
    }

    @Override
    public final Condition lessThanAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_THAN_ALL);
    }

    @Override
    public final Condition lessOrEqual(T value) {
        return lessOrEqual(val(value));
    }

    @Override
    public final Condition lessOrEqual(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqualAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_OR_EQUAL_ANY);
    }

    @Override
    public final Condition lessOrEqualSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_OR_EQUAL_SOME);
    }

    @Override
    public final Condition lessOrEqualAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.LESS_OR_EQUAL_ALL);
    }

    @Override
    @Deprecated
    public final Condition lessOrEqualToAny(Select<?> query) {
        return lessOrEqualAny(query);
    }

    @Override
    @Deprecated
    public final Condition lessOrEqualToSome(Select<?> query) {
        return lessOrEqualSome(query);
    }

    @Override
    @Deprecated
    public final Condition lessOrEqualToAll(Select<?> query) {
        return lessOrEqualAll(query);
    }

    @Override
    public final Condition greaterThan(T value) {
        return greaterThan(val(value));
    }

    @Override
    public final Condition greaterThan(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER);
    }

    @Override
    public final Condition greaterThanAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_THAN_ANY);
    }

    @Override
    public final Condition greaterThanSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_THAN_SOME);
    }

    @Override
    public final Condition greaterThanAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_THAN_ALL);
    }

    @Override
    public final Condition greaterOrEqual(T value) {
        return greaterOrEqual(val(value));
    }

    @Override
    public final Condition greaterOrEqual(Field<T> field) {
        return new CompareCondition<T>(this, field, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqualAny(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_OR_EQUAL_ANY);
    }

    @Override
    public final Condition greaterOrEqualSome(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_OR_EQUAL_SOME);
    }

    @Override
    public final Condition greaterOrEqualAll(Select<?> query) {
        return new SelectQueryAsSubQueryCondition(query, this, SubQueryOperator.GREATER_OR_EQUAL_ALL);
    }
}
