/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.IS_DISTINCT_FROM;
import static org.jooq.Comparator.IS_NOT_DISTINCT_FROM;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.DIVIDE;
import static org.jooq.impl.ExpressionOperator.MULTIPLY;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.BetweenAndStep;
import org.jooq.BindContext;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.Record1;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
abstract class AbstractField<T> extends AbstractQueryPart implements Field<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2884811923648354905L;

    private final String      name;
    private final DataType<T> dataType;

    AbstractField(String name, DataType<T> type) {
        super();

        this.name = name;
        this.dataType = type;
    }

    // ------------------------------------------------------------------------
    // XXX: API (not implemented)
    // ------------------------------------------------------------------------

    @Override
    public abstract void toSQL(RenderContext context);

    @Override
    public abstract void bind(BindContext context);

    // ------------------------------------------------------------------------
    // XXX: API
    // ------------------------------------------------------------------------

    @Override
    public Field<T> as(String alias) {
        return new FieldAlias<T>(this, alias);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final DataType<T> getDataType() {
        return dataType;
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {
        return dataType.getDataType(configuration);
    }

    @Override
    public final Class<T> getType() {
        return dataType.getType();
    }

    // ------------------------------------------------------------------------
    // XXX: Type casts
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
    public final <Z> Field<Z> cast(Class<Z> type) {
        return cast(DefaultDataType.getDataType(null, type));
    }

    // ------------------------------------------------------------------------
    // XXX: Type coercions
    // ------------------------------------------------------------------------

    @Override
    public final <Z> Field<Z> coerce(Field<Z> field) {
        return coerce(field.getDataType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Field<Z> coerce(DataType<Z> type) {

        // [#473] Prevent unnecessary coercions
        if (getDataType().equals(type)) {
            return (Field<Z>) this;
        }
        else {
            return new Coerce<Z>(this, type);
        }
    }

    @Override
    public final <Z> Field<Z> coerce(Class<Z> type) {
        return coerce(DefaultDataType.getDataType(null, type));
    }

    // ------------------------------------------------------------------------
    // XXX: Conversion of field into a sort field
    // ------------------------------------------------------------------------

    @Override
    public final SortField<T> asc() {
        return sort(SortOrder.ASC);
    }

    @Override
    public final SortField<T> desc() {
        return sort(SortOrder.DESC);
    }

    @Override
    public final SortField<T> sort(SortOrder order) {
        return new SortFieldImpl<T>(this, order);
    }

    @Override
    public final SortField<Integer> sortAsc(Collection<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<T, Integer>();

        int i = 0;
        for (T value : sortList) {
            map.put(value, i++);
        }

        return sort(map);
    }

    @Override
    public final SortField<Integer> sortAsc(T... sortList) {
        return sortAsc(Arrays.asList(sortList));
    }

    @Override
    public final SortField<Integer> sortDesc(Collection<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<T, Integer>();

        int i = 0;
        for (T value : sortList) {
            map.put(value, i--);
        }

        return sort(map);
    }

    @Override
    public final SortField<Integer> sortDesc(T... sortList) {
        return sortDesc(Arrays.asList(sortList));
    }

    @Override
    public final <Z> SortField<Z> sort(Map<T, Z> sortMap) {
        CaseValueStep<T> decode = DSL.decode().value(this);
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
    // XXX: Arithmetic operations
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> neg() {
        return new Neg<T>(this, ExpressionOperator.SUBTRACT);
    }

    @Override
    public final Field<T> add(Number value) {
        return add(Utils.field(value));
    }

    /*
     * This default implementation is known to be overridden by
     * Expression to generate neater expressions
     */
    @Override
    public Field<T> add(Field<?> value) {
        return new Expression<T>(ADD, this, nullSafe(value));
    }

    @Override
    public final Field<T> sub(Number value) {
        return sub(Utils.field(value));
    }

    @Override
    public final Field<T> sub(Field<?> value) {
        return new Expression<T>(SUBTRACT, this, nullSafe(value));
    }

    @Override
    public final Field<T> mul(Number value) {
        return mul(Utils.field(value));
    }

    /**
     * This default implementation is known to be overridden by
     * <code>Expression</code> to generate neater expressions
     */
    @Override
    public Field<T> mul(Field<? extends Number> value) {
        return new Expression<T>(MULTIPLY, this, nullSafe(value));
    }

    @Override
    public final Field<T> div(Number value) {
        return div(Utils.field(value));
    }

    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return new Expression<T>(DIVIDE, this, nullSafe(value));
    }

    @Override
    public final Field<T> mod(Number value) {
        return mod(Utils.field(value));
    }

    @Override
    public final Field<T> mod(Field<? extends Number> value) {
        return new Mod<T>(this, nullSafe(value));
    }

    // ------------------------------------------------------------------------
    // XXX: Arithmetic operation aliases
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> plus(Number value) {
        return add(value);
    }

    @Override
    public final Field<T> plus(Field<?> value) {
        return add(value);
    }

    @Override
    public final Field<T> subtract(Number value) {
        return sub(value);
    }

    @Override
    public final Field<T> subtract(Field<?> value) {
        return sub(value);
    }

    @Override
    public final Field<T> minus(Number value) {
        return sub(value);
    }

    @Override
    public final Field<T> minus(Field<?> value) {
        return sub(value);
    }

    @Override
    public final Field<T> multiply(Number value) {
        return mul(value);
    }

    @Override
    public final Field<T> multiply(Field<? extends Number> value) {
        return mul(value);
    }

    @Override
    public final Field<T> divide(Number value) {
        return div(value);
    }

    @Override
    public final Field<T> divide(Field<? extends Number> value) {
        return div(value);
    }

    @Override
    public final Field<T> modulo(Number value) {
        return mod(value);
    }

    @Override
    public final Field<T> modulo(Field<? extends Number> value) {
        return mod(value);
    }

    // ------------------------------------------------------------------------
    // XXX: Conditions created from this field
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNull() {
        return new IsNull(this, true);
    }

    @Override
    public final Condition isNotNull() {
        return new IsNull(this, false);
    }

    @Override
    public final Condition isDistinctFrom(T value) {
        return isDistinctFrom(Utils.field(value, this));
    }

    @Override
    public final Condition isDistinctFrom(Field<T> field) {
        return compare(IS_DISTINCT_FROM, field);
    }

    @Override
    public final Condition isNotDistinctFrom(T value) {
        return isNotDistinctFrom(Utils.field(value, this));
    }

    @Override
    public final Condition isNotDistinctFrom(Field<T> field) {
        return compare(IS_NOT_DISTINCT_FROM, field);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Condition isTrue() {
        Class<?> type = getType();

        if (type == String.class) {
            return ((Field<String>) this).in(Convert.TRUE_VALUES);
        }
        else if (Number.class.isAssignableFrom(type)) {
            return ((Field<Number>) this).equal((Number) getDataType().convert(1));
        }
        else if (Boolean.class.isAssignableFrom(type)) {
            return ((Field<Boolean>) this).equal(true);
        }
        else {
            return cast(String.class).in(Convert.TRUE_VALUES);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Condition isFalse() {
        Class<?> type = getType();

        if (type == String.class) {
            return ((Field<String>) this).in(Convert.FALSE_VALUES);
        }
        else if (Number.class.isAssignableFrom(type)) {
            return ((Field<Number>) this).equal((Number) getDataType().convert(0));
        }
        else if (Boolean.class.isAssignableFrom(type)) {
            return ((Field<Boolean>) this).equal(false);
        }
        else {
            return cast(String.class).in(Convert.FALSE_VALUES);
        }
    }

    @Override
    public final Condition like(String value) {
        return like(Utils.field(value, String.class));
    }

    @Override
    public final Condition like(String value, char escape) {
        return like(Utils.field(value, String.class), escape);
    }

    @Override
    public final Condition like(Field<String> field) {
        return new CompareCondition(this, nullSafe(field), LIKE);
    }

    @Override
    public final Condition like(Field<String> field, char escape) {
        return new CompareCondition(this, nullSafe(field), LIKE, escape);
    }

    @Override
    public final Condition likeIgnoreCase(String value) {
        return likeIgnoreCase(Utils.field(value, String.class));
    }

    @Override
    public final Condition likeIgnoreCase(String value, char escape) {
        return likeIgnoreCase(Utils.field(value, String.class), escape);
    }

    @Override
    public final Condition likeIgnoreCase(Field<String> field) {
        return new CompareCondition(this, nullSafe(field), LIKE_IGNORE_CASE);
    }

    @Override
    public final Condition likeIgnoreCase(Field<String> field, char escape) {
        return new CompareCondition(this, nullSafe(field), LIKE_IGNORE_CASE, escape);
    }

    @Override
    public final Condition likeRegex(String pattern) {
        return likeRegex(Utils.field(pattern, String.class));
    }

    @Override
    public final Condition likeRegex(Field<String> pattern) {
        return new RegexpLike(this, nullSafe(pattern));
    }

    @Override
    public final Condition notLike(String value) {
        return notLike(Utils.field(value, String.class));
    }

    @Override
    public final Condition notLike(String value, char escape) {
        return notLike(Utils.field(value, String.class), escape);
    }

    @Override
    public final Condition notLike(Field<String> field) {
        return new CompareCondition(this, nullSafe(field), NOT_LIKE);
    }

    @Override
    public final Condition notLike(Field<String> field, char escape) {
        return new CompareCondition(this, nullSafe(field), NOT_LIKE, escape);
    }

    @Override
    public final Condition notLikeIgnoreCase(String value) {
        return notLikeIgnoreCase(Utils.field(value, String.class));
    }

    @Override
    public final Condition notLikeIgnoreCase(String value, char escape) {
        return notLikeIgnoreCase(Utils.field(value, String.class), escape);
    }

    @Override
    public final Condition notLikeIgnoreCase(Field<String> field) {
        return new CompareCondition(this, nullSafe(field), NOT_LIKE_IGNORE_CASE);
    }

    @Override
    public final Condition notLikeIgnoreCase(Field<String> field, char escape) {
        return new CompareCondition(this, nullSafe(field), NOT_LIKE_IGNORE_CASE, escape);
    }

    @Override
    public final Condition notLikeRegex(String pattern) {
        return likeRegex(pattern).not();
    }

    @Override
    public final Condition notLikeRegex(Field<String> pattern) {
        return likeRegex(pattern).not();
    }

    @Override
    public final Condition contains(T value) {
        return new Contains<T>(this, value);
    }

    @Override
    public final Condition contains(Field<T> value) {
        return new Contains<T>(this, value);
    }

    @Override
    public final Condition startsWith(T value) {
        Field<String> concat = DSL.concat(Utils.escapeForLike(value), inline("%"));
        return like(concat, Utils.ESCAPE);
    }

    @Override
    public final Condition startsWith(Field<T> value) {
        Field<String> concat = DSL.concat(Utils.escapeForLike(value), inline("%"));
        return like(concat, Utils.ESCAPE);
    }

    @Override
    public final Condition endsWith(T value) {
        Field<String> concat = DSL.concat(inline("%"), Utils.escapeForLike(value));
        return like(concat, Utils.ESCAPE);
    }

    @Override
    public final Condition endsWith(Field<T> value) {
        Field<String> concat = DSL.concat(inline("%"), Utils.escapeForLike(value));
        return like(concat, Utils.ESCAPE);
    }

    @Override
    public final Condition in(T... values) {
        return in(Utils.fields(values, this).toArray(new Field<?>[0]));
    }

    @Override
    public final Condition in(Field<?>... values) {
        if (values == null || values.length == 0) {
            return falseCondition();
        }
        else {
            return new InCondition<T>(this, nullSafe(values), IN);
        }
    }

    @Override
    public final Condition in(Collection<T> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (T value : values) {
            fields.add(Utils.field(value, this));
        }

        return in(fields.toArray(new Field<?>[0]));
    }

    @Override
    public final Condition in(Select<? extends Record1<T>> query) {
        return compare(IN, query);
    }

    @Override
    public final Condition notIn(T... values) {
        if (values == null || values.length == 0) {
            return trueCondition();
        }
        else {
            return notIn(Utils.fields(values, this).toArray(new Field<?>[0]));
        }
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return new InCondition<T>(this, nullSafe(values), NOT_IN);
    }

    @Override
    public final Condition notIn(Collection<T> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (T value : values) {
            fields.add(Utils.field(value, this));
        }

        return notIn(fields.toArray(new Field<?>[0]));
    }

    @Override
    public final Condition notIn(Select<? extends Record1<T>> query) {
        return compare(NOT_IN, query);
    }

    @Override
    public final Condition between(T minValue, T maxValue) {
        return between(Utils.field(minValue, this), Utils.field(maxValue, this));
    }

    @Override
    public final Condition between(Field<T> minValue, Field<T> maxValue) {
        return between(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition betweenSymmetric(T minValue, T maxValue) {
        return betweenSymmetric(Utils.field(minValue, this), Utils.field(maxValue, this));
    }

    @Override
    public final Condition betweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return betweenSymmetric(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition notBetween(T minValue, T maxValue) {
        return notBetween(Utils.field(minValue, this), Utils.field(maxValue, this));
    }

    @Override
    public final Condition notBetween(Field<T> minValue, Field<T> maxValue) {
        return notBetween(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition notBetweenSymmetric(T minValue, T maxValue) {
        return notBetweenSymmetric(Utils.field(minValue, this), Utils.field(maxValue, this));
    }

    @Override
    public final Condition notBetweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return notBetweenSymmetric(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final BetweenAndStep<T> between(T minValue) {
        return between(Utils.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> between(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), false, false);
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(T minValue) {
        return betweenSymmetric(Utils.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), false, true);
    }

    @Override
    public final BetweenAndStep<T> notBetween(T minValue) {
        return notBetween(Utils.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> notBetween(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), true, false);
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(T minValue) {
        return notBetweenSymmetric(Utils.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), true, true);
    }

    @Override
    public final Condition eq(T value) {
        return equal(value);
    }

    @Override
    public final Condition eq(Field<T> field) {
        return equal(field);
    }

    @Override
    public final Condition eq(Select<? extends Record1<T>> query) {
        return equal(query);
    }

    @Override
    public final Condition eq(QuantifiedSelect<? extends Record1<T>> query) {
        return equal(query);
    }

    @Override
    public final Condition ne(T value) {
        return notEqual(value);
    }

    @Override
    public final Condition ne(Field<T> field) {
        return notEqual(field);
    }

    @Override
    public final Condition ne(Select<? extends Record1<T>> query) {
        return notEqual(query);
    }

    @Override
    public final Condition ne(QuantifiedSelect<? extends Record1<T>> query) {
        return notEqual(query);
    }

    @Override
    public final Condition lt(T value) {
        return lessThan(value);
    }

    @Override
    public final Condition lt(Field<T> field) {
        return lessThan(field);
    }

    @Override
    public final Condition lt(Select<? extends Record1<T>> query) {
        return lessThan(query);
    }

    @Override
    public final Condition lt(QuantifiedSelect<? extends Record1<T>> query) {
        return lessThan(query);
    }

    @Override
    public final Condition le(T value) {
        return lessOrEqual(value);
    }

    @Override
    public final Condition le(Field<T> field) {
        return lessOrEqual(field);
    }

    @Override
    public final Condition le(Select<? extends Record1<T>> query) {
        return lessOrEqual(query);
    }

    @Override
    public final Condition le(QuantifiedSelect<? extends Record1<T>> query) {
        return lessOrEqual(query);
    }

    @Override
    public final Condition gt(T value) {
        return greaterThan(value);
    }

    @Override
    public final Condition gt(Field<T> field) {
        return greaterThan(field);
    }

    @Override
    public final Condition gt(Select<? extends Record1<T>> query) {
        return greaterThan(query);
    }

    @Override
    public final Condition gt(QuantifiedSelect<? extends Record1<T>> query) {
        return greaterThan(query);
    }

    @Override
    public final Condition ge(T value) {
        return greaterOrEqual(value);
    }

    @Override
    public final Condition ge(Field<T> field) {
        return greaterOrEqual(field);
    }

    @Override
    public final Condition ge(Select<? extends Record1<T>> query) {
        return greaterOrEqual(query);
    }

    @Override
    public final Condition ge(QuantifiedSelect<? extends Record1<T>> query) {
        return greaterOrEqual(query);
    }

    @Override
    public final Condition equal(T value) {
        return equal(Utils.field(value, this));
    }

    @Override
    public final Condition equal(Field<T> field) {
        return compare(EQUALS, nullSafe(field));
    }

    @Override
    public final Condition equalIgnoreCase(String value) {
        return equalIgnoreCase(Utils.field(value, String.class));
    }

    @Override
    public final Condition equalIgnoreCase(Field<String> value) {
        return DSL.lower(cast(String.class)).equal(DSL.lower(value));
    }

    @Override
    public final Condition equal(Select<? extends Record1<T>> query) {
        return compare(EQUALS, query);
    }

    @Override
    public final Condition equal(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(EQUALS, query);
    }

    @Override
    public final Condition notEqual(T value) {
        return notEqual(Utils.field(value, this));
    }

    @Override
    public final Condition notEqual(Field<T> field) {
        return compare(NOT_EQUALS, nullSafe(field));
    }

    @Override
    public final Condition notEqualIgnoreCase(String value) {
        return notEqualIgnoreCase(Utils.field(value, String.class));
    }

    @Override
    public final Condition notEqualIgnoreCase(Field<String> value) {
        return DSL.lower(cast(String.class)).notEqual(DSL.lower(value));
    }

    @Override
    public final Condition notEqual(Select<? extends Record1<T>> query) {
        return compare(NOT_EQUALS, query);
    }

    @Override
    public final Condition notEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(NOT_EQUALS, query);
    }

    @Override
    public final Condition lessThan(T value) {
        return lessThan(Utils.field(value, this));
    }

    @Override
    public final Condition lessThan(Field<T> field) {
        return compare(LESS, nullSafe(field));
    }

    @Override
    public final Condition lessThan(Select<? extends Record1<T>> query) {
        return compare(LESS, query);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(LESS, query);
    }

    @Override
    public final Condition lessOrEqual(T value) {
        return lessOrEqual(Utils.field(value, this));
    }

    @Override
    public final Condition lessOrEqual(Field<T> field) {
        return compare(LESS_OR_EQUAL, nullSafe(field));
    }

    @Override
    public final Condition lessOrEqual(Select<? extends Record1<T>> query) {
        return compare(LESS_OR_EQUAL, query);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(LESS_OR_EQUAL, query);
    }

    @Override
    public final Condition greaterThan(T value) {
        return greaterThan(Utils.field(value, this));
    }

    @Override
    public final Condition greaterThan(Field<T> field) {
        return compare(GREATER, nullSafe(field));
    }

    @Override
    public final Condition greaterThan(Select<? extends Record1<T>> query) {
        return compare(GREATER, query);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(GREATER, query);
    }

    @Override
    public final Condition greaterOrEqual(T value) {
        return greaterOrEqual(Utils.field(value, this));
    }

    @Override
    public final Condition greaterOrEqual(Field<T> field) {
        return compare(GREATER_OR_EQUAL, nullSafe(field));
    }

    @Override
    public final Condition greaterOrEqual(Select<? extends Record1<T>> query) {
        return compare(GREATER_OR_EQUAL, query);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(GREATER_OR_EQUAL, query);
    }

    @Override
    public final Condition compare(Comparator comparator, T value) {
        return compare(comparator, Utils.field(value, this));
    }

    @Override
    public final Condition compare(Comparator comparator, Field<T> field) {
        switch (comparator) {
            case IS_DISTINCT_FROM:
            case IS_NOT_DISTINCT_FROM:
                return new IsDistinctFrom<T>(this, nullSafe(field), comparator);

            default:
                return new CompareCondition(this, nullSafe(field), comparator);
        }
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record1<T>> query) {
        return new SelectQueryAsSubQueryCondition(query, this, comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record1<T>> query) {
        return new QuantifiedComparisonCondition(query, this, comparator);
    }

    // ------------------------------------------------------------------------
    // XXX: Pre-2.0 API. This API is maintained for backwards-compatibility. It
    // will be removed in the future. Consider using equivalent methods from
    // org.jooq.impl.DSL
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private final <Z extends Number> Field<Z> numeric() {
        if (getDataType().isNumeric()) {
            return (Field<Z>) this;
        }
        else {
            return (Field<Z>) cast(BigDecimal.class);
        }
    }

    @SuppressWarnings("unchecked")
    private final Field<String> varchar() {
        if (getDataType().isString()) {
            return (Field<String>) this;
        }
        else {
            return cast(String.class);
        }
    }

    @SuppressWarnings("unchecked")
    private final <Z extends java.util.Date> Field<Z> date() {
        if (getDataType().isTemporal()) {
            return (Field<Z>) this;
        }
        else {
            return (Field<Z>) cast(Timestamp.class);
        }
    }

    @Override
    @Deprecated
    public final Field<Integer> sign() {
        return DSL.sign(numeric());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final Field<T> abs() {
        return (Field<T>) DSL.abs(numeric());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final Field<T> round() {
        return (Field<T>) DSL.round(numeric());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final Field<T> round(int decimals) {
        return (Field<T>) DSL.round(numeric(), decimals);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final Field<T> floor() {
        return (Field<T>) DSL.floor(numeric());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public final Field<T> ceil() {
        return (Field<T>) DSL.ceil(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> sqrt() {
        return DSL.sqrt(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> exp() {
        return DSL.exp(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> ln() {
        return DSL.ln(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> log(int base) {
        return DSL.log(numeric(), base);
    }

    @Override
    public final Field<BigDecimal> pow(Number exponent) {
        return DSL.power(numeric(), exponent);
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> power(Number exponent) {
        return pow(exponent);
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> acos() {
        return DSL.acos(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> asin() {
        return DSL.asin(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> atan() {
        return DSL.atan(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> atan2(Number y) {
        return DSL.atan2(numeric(), y);
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> atan2(Field<? extends Number> y) {
        return DSL.atan2(numeric(), y);
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> cos() {
        return DSL.cos(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> sin() {
        return DSL.sin(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> tan() {
        return DSL.tan(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> cot() {
        return DSL.cot(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> sinh() {
        return DSL.sinh(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> cosh() {
        return DSL.cosh(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> tanh() {
        return DSL.tanh(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> coth() {
        return DSL.coth(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> deg() {
        return DSL.deg(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> rad() {
        return DSL.rad(numeric());
    }

    @Override
    @Deprecated
    public final Field<Integer> count() {
        return DSL.count(this);
    }

    @Override
    @Deprecated
    public final Field<Integer> countDistinct() {
        return DSL.countDistinct(this);
    }

    @Override
    @Deprecated
    public final Field<T> max() {
        return DSL.max(this);
    }

    @Override
    @Deprecated
    public final Field<T> min() {
        return DSL.min(this);
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> sum() {
        return DSL.sum(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> avg() {
        return DSL.avg(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> median() {
        return DSL.median(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> stddevPop() {
        return DSL.stddevPop(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> stddevSamp() {
        return DSL.stddevSamp(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> varPop() {
        return DSL.varPop(numeric());
    }

    @Override
    @Deprecated
    public final Field<BigDecimal> varSamp() {
        return DSL.varSamp(numeric());
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<Integer> countOver() {
        return DSL.count(this).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<T> maxOver() {
        return DSL.max(this).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<T> minOver() {
        return DSL.min(this).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> sumOver() {
        return DSL.sum(numeric()).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> avgOver() {
        return DSL.avg(numeric()).over();
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> firstValue() {
        return DSL.firstValue(this);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lastValue() {
        return DSL.lastValue(this);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lead() {
        return DSL.lead(this);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lead(int offset) {
        return DSL.lead(this, offset);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lead(int offset, T defaultValue) {
        return DSL.lead(this, offset, defaultValue);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lead(int offset, Field<T> defaultValue) {
        return DSL.lead(this, offset, defaultValue);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lag() {
        return DSL.lag(this);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lag(int offset) {
        return DSL.lag(this, offset);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lag(int offset, T defaultValue) {
        return DSL.lag(this, offset, defaultValue);
    }

    @Override
    @Deprecated
    public final WindowIgnoreNullsStep<T> lag(int offset, Field<T> defaultValue) {
        return DSL.lag(this, offset, defaultValue);
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> stddevPopOver() {
        return DSL.stddevPop(numeric()).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> stddevSampOver() {
        return DSL.stddevSamp(numeric()).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> varPopOver() {
        return DSL.varPop(numeric()).over();
    }

    @Override
    @Deprecated
    public final WindowPartitionByStep<BigDecimal> varSampOver() {
        return DSL.varSamp(numeric()).over();
    }

    @Override
    @Deprecated
    public final Field<String> upper() {
        return DSL.upper(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> lower() {
        return DSL.lower(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> trim() {
        return DSL.trim(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> rtrim() {
        return DSL.rtrim(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> ltrim() {
        return DSL.ltrim(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> rpad(Field<? extends Number> length) {
        return DSL.rpad(varchar(), length);
    }

    @Override
    @Deprecated
    public final Field<String> rpad(int length) {
        return DSL.rpad(varchar(), length);
    }

    @Override
    @Deprecated
    public final Field<String> rpad(Field<? extends Number> length, Field<String> character) {
        return DSL.rpad(varchar(), length, character);
    }

    @Override
    @Deprecated
    public final Field<String> rpad(int length, char character) {
        return DSL.rpad(varchar(), length, character);
    }

    @Override
    @Deprecated
    public final Field<String> lpad(Field<? extends Number> length) {
        return DSL.lpad(varchar(), length);
    }

    @Override
    @Deprecated
    public final Field<String> lpad(int length) {
        return DSL.lpad(varchar(), length);
    }

    @Override
    @Deprecated
    public final Field<String> lpad(Field<? extends Number> length, Field<String> character) {
        return DSL.lpad(varchar(), length, character);
    }

    @Override
    @Deprecated
    public final Field<String> lpad(int length, char character) {
        return DSL.lpad(varchar(), length, character);
    }

    @Override
    @Deprecated
    public final Field<String> repeat(Number count) {
        return DSL.repeat(varchar(), count == null ? 0 : count.intValue());
    }

    @Override
    @Deprecated
    public final Field<String> repeat(Field<? extends Number> count) {
        return DSL.repeat(varchar(), count);
    }

    @Override
    @Deprecated
    public final Field<String> replace(Field<String> search) {
        return DSL.replace(varchar(), search);
    }

    @Override
    @Deprecated
    public final Field<String> replace(String search) {
        return DSL.replace(varchar(), search);
    }

    @Override
    @Deprecated
    public final Field<String> replace(Field<String> search, Field<String> replace) {
        return DSL.replace(varchar(), search, replace);
    }

    @Override
    @Deprecated
    public final Field<String> replace(String search, String replace) {
        return DSL.replace(varchar(), search, replace);
    }

    @Override
    @Deprecated
    public final Field<Integer> position(String search) {
        return DSL.position(varchar(), search);
    }

    @Override
    @Deprecated
    public final Field<Integer> position(Field<String> search) {
        return DSL.position(varchar(), search);
    }

    @Override
    @Deprecated
    public final Field<Integer> ascii() {
        return DSL.ascii(varchar());
    }

    @Override
    @Deprecated
    public final Field<String> concat(Field<?>... fields) {
        return DSL.concat(Utils.combine(this, fields));
    }

    @Override
    @Deprecated
    public final Field<String> concat(String... values) {
        return DSL.concat(Utils.combine(this, Utils.fields(values).toArray(new Field[0])));
    }

    @Override
    @Deprecated
    public final Field<String> substring(int startingPosition) {
        return DSL.substring(varchar(), startingPosition);
    }

    @Override
    @Deprecated
    public final Field<String> substring(Field<? extends Number> startingPosition) {
        return DSL.substring(varchar(), startingPosition);
    }

    @Override
    @Deprecated
    public final Field<String> substring(int startingPosition, int length) {
        return DSL.substring(varchar(), startingPosition, length);
    }

    @Override
    @Deprecated
    public final Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return DSL.substring(varchar(), startingPosition, length);
    }

    @Override
    @Deprecated
    public final Field<Integer> length() {
        return DSL.length(varchar());
    }

    @Override
    @Deprecated
    public final Field<Integer> charLength() {
        return DSL.charLength(varchar());
    }

    @Override
    @Deprecated
    public final Field<Integer> bitLength() {
        return DSL.bitLength(varchar());
    }

    @Override
    @Deprecated
    public final Field<Integer> octetLength() {
        return DSL.octetLength(varchar());
    }

    @Override
    @Deprecated
    public final Field<Integer> extract(DatePart datePart) {
        return DSL.extract(date(), datePart);
    }

    @Override
    @Deprecated
    public final Field<T> greatest(T... others) {
        return DSL.greatest(this, Utils.fields(others).toArray(new Field[0]));
    }

    @Override
    @Deprecated
    public final Field<T> greatest(Field<?>... others) {
        return DSL.greatest(this, others);
    }

    @Override
    @Deprecated
    public final Field<T> least(T... others) {
        return DSL.least(this, Utils.fields(others).toArray(new Field[0]));
    }

    @Override
    @Deprecated
    public final Field<T> least(Field<?>... others) {
        return DSL.least(this, others);
    }

    @Override
    @Deprecated
    public final Field<T> nvl(T defaultValue) {
        return DSL.nvl(this, defaultValue);
    }

    @Override
    @Deprecated
    public final Field<T> nvl(Field<T> defaultValue) {
        return DSL.nvl(this, defaultValue);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull) {
        return DSL.nvl2(this, valueIfNotNull, valueIfNull);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        return DSL.nvl2(this, valueIfNotNull, valueIfNull);
    }

    @Override
    @Deprecated
    public final Field<T> nullif(T other) {
        return DSL.nullif(this, other);
    }

    @Override
    @Deprecated
    public final Field<T> nullif(Field<T> other) {
        return DSL.nullif(this, other);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> decode(T search, Z result) {
        return DSL.decode(this, search, result);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> decode(T search, Z result, Object... more) {
        return DSL.decode(this, search, result, more);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result) {
        return DSL.decode(this, search, result);
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more) {
        return DSL.decode(this, search, result, more);
    }

    @Override
    @Deprecated
    public final Field<T> coalesce(T option, T... options) {
        return DSL.coalesce(this, Utils.combine(Utils.field(option), Utils.fields(options).toArray(new Field[0])));
    }

    @Override
    @Deprecated
    public final Field<T> coalesce(Field<T> option, Field<?>... options) {
        return DSL.coalesce(this, Utils.combine(option, options));
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#2144] Non-equality can be decided early, without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof AbstractField) {
            if (StringUtils.equals(name, (((AbstractField<?>) that).name))) {
                return super.equals(that);
            }

            return false;
        }

        return false;
    }

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name.hashCode();
    }
}
