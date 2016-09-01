/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.Clause.FIELD;
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
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.DIVIDE;
import static org.jooq.impl.ExpressionOperator.MULTIPLY;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.tools.Convert.FALSE_VALUES;
import static org.jooq.tools.Convert.TRUE_VALUES;
import static org.jooq.tools.StringUtils.defaultString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.jooq.BetweenAndStep;
import org.jooq.Binding;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
abstract class AbstractField<T> extends AbstractQueryPart implements Field<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 2884811923648354905L;
    private static final Clause[] CLAUSES          = { FIELD };

    private final String          name;
    private final String          comment;
    private final DataType<T>     dataType;

    AbstractField(String name, DataType<T> type) {
        this(name, type, null, type.getBinding());
    }

    @SuppressWarnings("unchecked")
    AbstractField(String name, DataType<T> type, String comment, Binding<?, T> binding) {
        super();

        this.name = name;
        this.comment = defaultString(comment);
        this.dataType = type.asConvertedDataType((Binding<T, T>) binding);
    }

    // ------------------------------------------------------------------------
    // XXX: API (not implemented)
    // ------------------------------------------------------------------------

    @Override
    public abstract void accept(Context<?> ctx);

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // ------------------------------------------------------------------------
    // [#5518] Record method inversions, e.g. for use as method references
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> field(Record record) {
        return record.field(this);
    }

    @Override
    public final T get(Record record) {
        return record.get(this);
    }

    @Override
    public final T getValue(Record record) {
        return record.getValue(this);
    }

    @Override
    public final T original(Record record) {
        return record.original(this);
    }

    @Override
    public final boolean changed(Record record) {
        return record.changed(this);
    }

    @Override
    public final void reset(Record record) {
        record.reset(this);
    }

    @Override
    public final Record1<T> from(Record record) {
        return record.into(this);
    }

    // ------------------------------------------------------------------------
    // XXX: API
    // ------------------------------------------------------------------------

    @Override
    public Field<T> as(String alias) {
        return new FieldAlias<T>(this, alias);
    }

    @Override
    public final Field<T> as(Field<?> otherField) {
        return as(otherField.getName());
    }

    @Override
    public final Field<T> as(Function<? super Field<T>, ? extends String> aliasFunction) {
        return as(aliasFunction.apply(this));
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getComment() {
        return comment;
    }

    @Override
    public final Converter<?, T> getConverter() {
        return getBinding().converter();
    }

    @Override
    public final Binding<?, T> getBinding() {
        return dataType.getBinding();
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

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Field<Z> cast(Class<Z> type) {

        // [#2597] Prevent unnecessary casts
        if (getType() == type) {
            return (Field<Z>) this;
        }
        else {
            return cast(DefaultDataType.getDataType(null, type));
        }
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

    @SafeVarargs

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

    @SafeVarargs

    public final SortField<Integer> sortDesc(T... sortList) {
        return sortDesc(Arrays.asList(sortList));
    }

    @Override
    public final <Z> SortField<Z> sort(Map<T, Z> sortMap) {
        CaseValueStep<T> decode = DSL.choose(this);
        CaseWhenStep<T, Z> result = null;

        for (Entry<T, Z> entry : sortMap.entrySet()) {
            if (result == null) {
                result = decode.when(entry.getKey(), inline(entry.getValue()));
            }
            else {
                result.when(entry.getKey(), inline(entry.getValue()));
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
        return add(Tools.field(value));
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
        return sub(Tools.field(value));
    }

    @Override
    public final Field<T> sub(Field<?> value) {
        return new Expression<T>(SUBTRACT, this, nullSafe(value));
    }

    @Override
    public final Field<T> mul(Number value) {
        return mul(Tools.field(value));
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
        return div(Tools.field(value));
    }

    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return new Expression<T>(DIVIDE, this, nullSafe(value));
    }

    @Override
    public final Field<T> mod(Number value) {
        return mod(Tools.field(value));
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
    // XXX: Bitwise operations
    // ------------------------------------------------------------------------
    // Unsafe casting is needed here, as bitwise operations only work on
    // numeric values...

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitNot() {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitNot((Field) this);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitAnd(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitAnd((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitAnd(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitAnd((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitNand(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitNand((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitNand(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitNand((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitOr(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitOr((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitOr(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitOr((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitNor(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitNor((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitNor(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitNor((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitXor(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitXor((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitXor(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitXor((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitXNor(T value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitXNor((Field) this, (Field) val(value, this));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> bitXNor(Field<T> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.bitXNor((Field) this, (Field) value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> shl(Number value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.shl((Field) this, (Field) Tools.field(value));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> shl(Field<? extends Number> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.shl((Field) this, value);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> shr(Number value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.shr((Field) this, (Field) Tools.field(value));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Field<T> shr(Field<? extends Number> value) {
        // Workaround assignment for https://bugs.eclipse.org/bugs/show_bug.cgi?id=473657
        final Field result = DSL.shr((Field) this, (Field) value);
        return result;
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
        return isDistinctFrom(Tools.field(value, this));
    }

    @Override
    public final Condition isDistinctFrom(Field<T> field) {
        return compare(IS_DISTINCT_FROM, field);
    }

    @Override
    public final Condition isNotDistinctFrom(T value) {
        return isNotDistinctFrom(Tools.field(value, this));
    }

    @Override
    public final Condition isNotDistinctFrom(Field<T> field) {
        return compare(IS_NOT_DISTINCT_FROM, field);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public final Condition isTrue() {
        Class<?> type = getType();

        if (type == String.class)
            return ((Field<String>) this).in(Tools.inline(TRUE_VALUES.toArray(EMPTY_STRING)));
        else if (Number.class.isAssignableFrom(type))
            return ((Field<Number>) this).equal(inline((Number) getDataType().convert(1)));
        else if (Boolean.class.isAssignableFrom(type))
            return ((Field<Boolean>) this).equal(inline(true));
        else
            return cast(String.class).in(TRUE_VALUES);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public final Condition isFalse() {
        Class<?> type = getType();

        if (type == String.class)
            return ((Field<String>) this).in(Tools.inline(FALSE_VALUES.toArray(EMPTY_STRING)));
        else if (Number.class.isAssignableFrom(type))
            return ((Field<Number>) this).equal(inline((Number) getDataType().convert(0)));
        else if (Boolean.class.isAssignableFrom(type))
            return ((Field<Boolean>) this).equal(inline(false));
        else
            return cast(String.class).in(Tools.inline(FALSE_VALUES.toArray(EMPTY_STRING)));
    }

    @Override
    public final Condition like(String value) {
        return like(Tools.field(value, String.class));
    }

    @Override
    public final Condition like(String value, char escape) {
        return like(Tools.field(value, String.class), escape);
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
        return likeIgnoreCase(Tools.field(value, String.class));
    }

    @Override
    public final Condition likeIgnoreCase(String value, char escape) {
        return likeIgnoreCase(Tools.field(value, String.class), escape);
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
        return likeRegex(Tools.field(pattern, String.class));
    }

    @Override
    public final Condition likeRegex(Field<String> pattern) {
        return new RegexpLike(this, nullSafe(pattern));
    }

    @Override
    public final Condition notLike(String value) {
        return notLike(Tools.field(value, String.class));
    }

    @Override
    public final Condition notLike(String value, char escape) {
        return notLike(Tools.field(value, String.class), escape);
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
        return notLikeIgnoreCase(Tools.field(value, String.class));
    }

    @Override
    public final Condition notLikeIgnoreCase(String value, char escape) {
        return notLikeIgnoreCase(Tools.field(value, String.class), escape);
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
        Field<String> concat = DSL.concat(Tools.escapeForLike(value), inline("%"));
        return like(concat, Tools.ESCAPE);
    }

    @Override
    public final Condition startsWith(Field<T> value) {
        Field<String> concat = DSL.concat(Tools.escapeForLike(value), inline("%"));
        return like(concat, Tools.ESCAPE);
    }

    @Override
    public final Condition endsWith(T value) {
        Field<String> concat = DSL.concat(inline("%"), Tools.escapeForLike(value));
        return like(concat, Tools.ESCAPE);
    }

    @Override
    public final Condition endsWith(Field<T> value) {
        Field<String> concat = DSL.concat(inline("%"), Tools.escapeForLike(value));
        return like(concat, Tools.ESCAPE);
    }

    private final boolean isAccidentalSelect(T[] values) {
        return (values != null && values.length == 1 && values[0] instanceof Select);
    }

    private final boolean isAccidentalCollection(T[] values) {
        return (values != null && values.length == 1 && values[0] instanceof Collection);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Condition in(T... values) {

        // [#3362] Prevent "rogue" API usage when using Field<Object>.in(Object... values)
        if (isAccidentalSelect(values))
            return in((Select<Record1<T>>) values[0]);

        // [#3347] Prevent "rogue" API usage when using Field<Object>.in(Object... values)
        if (isAccidentalCollection(values))
            return in((Collection<?>) values[0]);

        return in(Tools.fields(values, this).toArray(EMPTY_FIELD));
    }

    @Override
    public final Condition in(Field<?>... values) {
        return new InCondition<T>(this, nullSafe(values), IN);
    }

    @Override
    public final Condition in(Collection<?> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (Object value : values)
            fields.add(Tools.field(value, this));

        return in(fields.toArray(EMPTY_FIELD));
    }

    @Override
    public final Condition in(Result<? extends Record1<T>> result) {
        return in(result.getValues(0, getType()));
    }

    @Override
    public final Condition in(Select<? extends Record1<T>> query) {
        return compare(IN, query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Condition notIn(T... values) {

        // [#3362] Prevent "rogue" API usage when using Field<Object>.in(Object... values)
        if (isAccidentalSelect(values))
            return notIn((Select<Record1<T>>) values[0]);

        // [#3347] Prevent "rogue" API usage when using Field<Object>.in(Object... values)
        if (isAccidentalCollection(values))
            return notIn((Collection<?>) values[0]);

        return notIn(Tools.fields(values, this).toArray(EMPTY_FIELD));
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return new InCondition<T>(this, nullSafe(values), NOT_IN);
    }

    @Override
    public final Condition notIn(Collection<?> values) {
        List<Field<?>> fields = new ArrayList<Field<?>>();

        for (Object value : values)
            fields.add(Tools.field(value, this));

        return notIn(fields.toArray(EMPTY_FIELD));
    }

    @Override
    public final Condition notIn(Result<? extends Record1<T>> result) {
        return notIn(result.getValues(0, getType()));
    }

    @Override
    public final Condition notIn(Select<? extends Record1<T>> query) {
        return compare(NOT_IN, query);
    }

    @Override
    public final Condition between(T minValue, T maxValue) {
        return between(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition between(Field<T> minValue, Field<T> maxValue) {
        return between(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition betweenSymmetric(T minValue, T maxValue) {
        return betweenSymmetric(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition betweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return betweenSymmetric(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition notBetween(T minValue, T maxValue) {
        return notBetween(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition notBetween(Field<T> minValue, Field<T> maxValue) {
        return notBetween(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final Condition notBetweenSymmetric(T minValue, T maxValue) {
        return notBetweenSymmetric(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition notBetweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return notBetweenSymmetric(nullSafe(minValue)).and(nullSafe(maxValue));
    }

    @Override
    public final BetweenAndStep<T> between(T minValue) {
        return between(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> between(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), false, false);
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(T minValue) {
        return betweenSymmetric(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), false, true);
    }

    @Override
    public final BetweenAndStep<T> notBetween(T minValue) {
        return notBetween(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> notBetween(Field<T> minValue) {
        return new BetweenCondition<T>(this, nullSafe(minValue), true, false);
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(T minValue) {
        return notBetweenSymmetric(Tools.field(minValue, this));
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
        return equal(Tools.field(value, this));
    }

    @Override
    public final Condition equal(Field<T> field) {
        return compare(EQUALS, nullSafe(field));
    }

    @Override
    public final Condition equalIgnoreCase(String value) {
        return equalIgnoreCase(Tools.field(value, String.class));
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
        return notEqual(Tools.field(value, this));
    }

    @Override
    public final Condition notEqual(Field<T> field) {
        return compare(NOT_EQUALS, nullSafe(field));
    }

    @Override
    public final Condition notEqualIgnoreCase(String value) {
        return notEqualIgnoreCase(Tools.field(value, String.class));
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
        return lessThan(Tools.field(value, this));
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
        return lessOrEqual(Tools.field(value, this));
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
        return greaterThan(Tools.field(value, this));
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
        return greaterOrEqual(Tools.field(value, this));
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
        return compare(comparator, Tools.field(value, this));
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
        return compare(comparator, new ScalarSubquery<T>(query, getDataType()));
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
        return DSL.concat(Tools.combine(this, fields));
    }

    @Override
    @Deprecated
    public final Field<String> concat(String... values) {
        return DSL.concat(Tools.combine(this, Tools.fields(values).toArray(EMPTY_FIELD)));
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

    @SafeVarargs

    public final Field<T> greatest(T... others) {
        return DSL.greatest(this, Tools.fields(others).toArray(EMPTY_FIELD));
    }

    @Override
    @Deprecated
    public final Field<T> greatest(Field<?>... others) {
        return DSL.greatest(this, others);
    }

    @Override
    @Deprecated

    @SafeVarargs

    public final Field<T> least(T... others) {
        return DSL.least(this, Tools.fields(others).toArray(EMPTY_FIELD));
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

    @SafeVarargs

    public final Field<T> coalesce(T option, T... options) {
        return DSL.coalesce(this, Tools.combine(Tools.field(option), Tools.fields(options).toArray(EMPTY_FIELD)));
    }

    @Override
    @Deprecated
    public final Field<T> coalesce(Field<T> option, Field<?>... options) {
        return DSL.coalesce(this, Tools.combine(option, options));
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
