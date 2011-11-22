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
import static org.jooq.impl.Factory.falseCondition;
import static org.jooq.impl.Factory.nullSafe;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.vals;

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
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;

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
    public abstract void bind(BindContext context);

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
        CaseValueStep<T> decode = Factory.decode().value(this);
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
    // Arithmetic operations
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> neg() {
        return new Neg<T>(this, ExpressionOperator.SUBTRACT);
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
        return new Expression<T>(ADD, this, nullSafe(value));
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
        return new Expression<T>(SUBTRACT, this, nullSafe(value));
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
        return new Expression<T>(MULTIPLY, this, nullSafe(value));
    }

    @Override
    public final Field<T> div(Number value) {
        return div(val(value));
    }

    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return new Expression<T>(DIVIDE, this, nullSafe(value));
    }

    @Override
    public final Field<T> mod(Number value) {
        return mod(val(value));
    }

    @Override
    public final Field<T> mod(Field<? extends Number> value) {
        return new Mod<T>(this, nullSafe(value));
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
    public final Condition like(T value) {
        return like(val(value));
    }

    @Override
    public final Condition like(Field<T> value) {
        return new CompareCondition<T>(this, nullSafe(value), Comparator.LIKE);
    }

    @Override
    public final Condition notLike(T value) {
        return notLike(val(value));
    }

    @Override
    public final Condition notLike(Field<T> value) {
        return new CompareCondition<T>(this, nullSafe(value), Comparator.NOT_LIKE);
    }

    @Override
    public final Condition in(T... values) {
        return in(vals(values).toArray(new Field<?>[0]));
    }

    @Override
    public final Condition in(Field<?>... values) {
        if (values == null || values.length == 0) {
            return falseCondition();
        }
        else {
            return new InCondition<T>(this, nullSafe(values), InOperator.IN);
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
            return trueCondition();
        }
        else {
            return notIn(vals(values).toArray(new Field<?>[0]));
        }
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return new InCondition<T>(this, nullSafe(values), InOperator.NOT_IN);
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
        return new BetweenCondition<T>(this, nullSafe(minValue), nullSafe(maxValue));
    }

    @Override
    public final Condition equal(T value) {
        return equal(val(value));
    }

    @Override
    public final Condition equal(Field<T> field) {
        return new CompareCondition<T>(this, nullSafe(field), Comparator.EQUALS);
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
        return new CompareCondition<T>(this, nullSafe(field), Comparator.NOT_EQUALS);
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
        return new CompareCondition<T>(this, nullSafe(field), Comparator.LESS);
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
        return new CompareCondition<T>(this, nullSafe(field), Comparator.LESS_OR_EQUAL);
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
    public final Condition greaterThan(T value) {
        return greaterThan(val(value));
    }

    @Override
    public final Condition greaterThan(Field<T> field) {
        return new CompareCondition<T>(this, nullSafe(field), Comparator.GREATER);
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
        return new CompareCondition<T>(this, nullSafe(field), Comparator.GREATER_OR_EQUAL);
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
