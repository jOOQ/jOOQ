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

import static org.jooq.Clause.FIELD;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_SIMILAR_TO;
import static org.jooq.Comparator.SIMILAR_TO;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.DIVIDE;
import static org.jooq.impl.ExpressionOperator.MULTIPLY;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.fieldsArray;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.nullSafe;
import static org.jooq.impl.Tools.nullSafeList;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jooq.BetweenAndStep;
import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
import org.jooq.Name;
// ...
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowPartitionByStep;

/**
 * @author Lukas Eder
 */
abstract class AbstractField<T> extends AbstractTypedNamed<T> implements Field<T>, ScopeMappable {

    private static final Clause[] CLAUSES = { FIELD };

    AbstractField(Name name, DataType<T> type) {
        this(name, type, null);
    }

    AbstractField(Name name, DataType<T> type, Comment comment) {
        this(name, type, comment, type.getBinding());
    }

    @SuppressWarnings("unchecked")
    AbstractField(Name name, DataType<T> type, Comment comment, Binding<?, T> binding) {
        super(name, comment, type.asConvertedDataType((Binding<T, T>) binding));
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

    /* non-final */ boolean isPossiblyNullable() {
        return true;
    }

    /* non-final */ int projectionSize() {
        return 1;
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
    public final <U> Field<U> convert(Binding<T, U> binding) {
        return coerce(getDataType().asConvertedDataType(binding));
    }

    @Override
    public final <U> Field<U> convert(Converter<T, U> converter) {
        return coerce(getDataType().asConvertedDataType(converter));
    }

    @Override
    public final <U> Field<U> convert(
        Class<U> toType,
        Function<? super T, ? extends U> from,
        Function<? super U, ? extends T> to
    ) {
        return coerce(getDataType().asConvertedDataType(toType, from, to));
    }

    @Override
    public final <U> Field<U> convertFrom(Class<U> toType, Function<? super T, ? extends U> from) {
        return coerce(getDataType().asConvertedDataTypeFrom(toType, from));
    }

    @Override
    public final <U> Field<U> convertFrom(Function<? super T, ? extends U> from) {
        return coerce(getDataType().asConvertedDataTypeFrom(from));
    }

    @Override
    public final <U> Field<U> convertTo(Class<U> toType, Function<? super U, ? extends T> to) {
        return coerce(getDataType().asConvertedDataTypeTo(toType, to));
    }

    @Override
    public final <U> Field<U> convertTo(Function<? super U, ? extends T> to) {
        return coerce(getDataType().asConvertedDataTypeTo(to));
    }

    @Override
    public final Field<T> as(String alias) {
        return as(DSL.name(alias));
    }

    @Override
    public Field<T> as(Name alias) {
        return new FieldAlias<>(this, alias);
    }

    @Override
    public final Field<T> as(Field<?> otherField) {
        return as(otherField.getUnqualifiedName());
    }

    @Override
    public final Field<T> as(Function<? super Field<T>, ? extends String> aliasFunction) {
        return as(aliasFunction.apply(this));
    }

    // ------------------------------------------------------------------------
    // XXX: Type casts
    // ------------------------------------------------------------------------

    @Override
    public final <Z> Field<Z> cast(Field<Z> field) {
        return cast(field.getDataType());
    }

    @Override
    public final <Z> Field<Z> cast(DataType<Z> type) {
        return new Cast<>(this, type);
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

    @Override
    public final <Z> Field<Z> coerce(DataType<Z> type) {
        return new Coerce<>(this, type);
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
    public final SortField<T> sortDefault() {
        return sort(SortOrder.DEFAULT);
    }

    @Override
    public final SortField<T> sort(SortOrder order) {
        return new SortFieldImpl<>(this, order);
    }

    @Override
    public final SortField<Integer> sortAsc(Collection<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<>();

        int i = 0;
        for (T value : sortList)
            map.put(value, i++);

        return sort(map);
    }

    @Override
    @SafeVarargs
    public final SortField<Integer> sortAsc(T... sortList) {
        return sortAsc(Arrays.asList(sortList));
    }

    @Override
    public final SortField<Integer> sortDesc(Collection<T> sortList) {
        Map<T, Integer> map = new LinkedHashMap<>();

        int i = 0;
        for (T value : sortList)
            map.put(value, i--);

        return sort(map);
    }

    @Override
    @SafeVarargs
    public final SortField<Integer> sortDesc(T... sortList) {
        return sortDesc(Arrays.asList(sortList));
    }

    @Override
    public final <Z> SortField<Z> sort(Map<T, Z> sortMap) {
        return sortMap == null || sortMap.isEmpty() ? sortConstant() : DSL.case_(this).mapValues(sortMap).asc();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final <Z> SortField<Z> sortConstant() {
        return new SortFieldImpl<>(new ConstantSortField<>((Field) this), SortOrder.DEFAULT);
    }



    // -------------------------------------------------------------------------
    // Generic predicates
    // -------------------------------------------------------------------------

    @Override
    public final Condition eq(T arg2) {
        return new Eq<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition eq(Select<? extends Record1<T>> arg2) {
        return new Eq<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition eq(Field<T> arg2) {
        return new Eq<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition equal(T arg2) {
        return eq(arg2);
    }

    @Override
    public final Condition equal(Select<? extends Record1<T>> arg2) {
        return eq(arg2);
    }

    @Override
    public final Condition equal(Field<T> arg2) {
        return eq(arg2);
    }

    @Override
    public final Condition ge(T arg2) {
        return new Ge<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition ge(Select<? extends Record1<T>> arg2) {
        return new Ge<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition ge(Field<T> arg2) {
        return new Ge<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition greaterOrEqual(T arg2) {
        return ge(arg2);
    }

    @Override
    public final Condition greaterOrEqual(Select<? extends Record1<T>> arg2) {
        return ge(arg2);
    }

    @Override
    public final Condition greaterOrEqual(Field<T> arg2) {
        return ge(arg2);
    }

    @Override
    public final Condition greaterThan(T arg2) {
        return gt(arg2);
    }

    @Override
    public final Condition greaterThan(Select<? extends Record1<T>> arg2) {
        return gt(arg2);
    }

    @Override
    public final Condition greaterThan(Field<T> arg2) {
        return gt(arg2);
    }

    @Override
    public final Condition gt(T arg2) {
        return new Gt<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition gt(Select<? extends Record1<T>> arg2) {
        return new Gt<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition gt(Field<T> arg2) {
        return new Gt<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition in(Select<? extends Record1<T>> arg2) {
        return new In<>(this, arg2);
    }

    @Override
    public final Condition isDistinctFrom(T arg2) {
        return new IsDistinctFrom<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition isDistinctFrom(Select<? extends Record1<T>> arg2) {
        return new IsDistinctFrom<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition isDistinctFrom(Field<T> arg2) {
        return new IsDistinctFrom<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition isNull() {
        return new IsNull(this);
    }

    @Override
    public final Condition isNotDistinctFrom(T arg2) {
        return new IsNotDistinctFrom<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition isNotDistinctFrom(Select<? extends Record1<T>> arg2) {
        return new IsNotDistinctFrom<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition isNotDistinctFrom(Field<T> arg2) {
        return new IsNotDistinctFrom<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition isNotNull() {
        return new IsNotNull(this);
    }

    @Override
    public final Condition le(T arg2) {
        return new Le<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition le(Select<? extends Record1<T>> arg2) {
        return new Le<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition le(Field<T> arg2) {
        return new Le<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition lessOrEqual(T arg2) {
        return le(arg2);
    }

    @Override
    public final Condition lessOrEqual(Select<? extends Record1<T>> arg2) {
        return le(arg2);
    }

    @Override
    public final Condition lessOrEqual(Field<T> arg2) {
        return le(arg2);
    }

    @Override
    public final Condition lessThan(T arg2) {
        return lt(arg2);
    }

    @Override
    public final Condition lessThan(Select<? extends Record1<T>> arg2) {
        return lt(arg2);
    }

    @Override
    public final Condition lessThan(Field<T> arg2) {
        return lt(arg2);
    }

    @Override
    public final LikeEscapeStep like(String pattern) {
        return new Like(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep like(Field<String> pattern) {
        return new Like(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final LikeEscapeStep likeIgnoreCase(String pattern) {
        return new LikeIgnoreCase(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep likeIgnoreCase(Field<String> pattern) {
        return new LikeIgnoreCase(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final Condition lt(T arg2) {
        return new Lt<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition lt(Select<? extends Record1<T>> arg2) {
        return new Lt<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition lt(Field<T> arg2) {
        return new Lt<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition ne(T arg2) {
        return new Ne<>(this, Tools.field(arg2, this));
    }

    @Override
    public final Condition ne(Select<? extends Record1<T>> arg2) {
        return new Ne<>(this, DSL.field(arg2));
    }

    @Override
    public final Condition ne(Field<T> arg2) {
        return new Ne<>(this, nullSafe(arg2, getDataType()));
    }

    @Override
    public final Condition notEqual(T arg2) {
        return ne(arg2);
    }

    @Override
    public final Condition notEqual(Select<? extends Record1<T>> arg2) {
        return ne(arg2);
    }

    @Override
    public final Condition notEqual(Field<T> arg2) {
        return ne(arg2);
    }

    @Override
    public final Condition notIn(Select<? extends Record1<T>> arg2) {
        return new NotIn<>(this, arg2);
    }

    @Override
    public final LikeEscapeStep notLike(String pattern) {
        return new NotLike(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep notLike(Field<String> pattern) {
        return new NotLike(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final LikeEscapeStep notLikeIgnoreCase(String pattern) {
        return new NotLikeIgnoreCase(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep notLikeIgnoreCase(Field<String> pattern) {
        return new NotLikeIgnoreCase(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final LikeEscapeStep notSimilarTo(String pattern) {
        return new NotSimilarTo(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep notSimilarTo(Field<String> pattern) {
        return new NotSimilarTo(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final LikeEscapeStep similarTo(String pattern) {
        return new SimilarTo(this, Tools.field(pattern));
    }

    @Override
    public final LikeEscapeStep similarTo(Field<String> pattern) {
        return new SimilarTo(this, nullSafe(pattern, getDataType()));
    }

    // -------------------------------------------------------------------------
    // XML predicates
    // -------------------------------------------------------------------------

    @Override
    public final Condition isDocument() {
        return new IsDocument(this);
    }

    @Override
    public final Condition isNotDocument() {
        return new IsNotDocument(this);
    }

    // -------------------------------------------------------------------------
    // JSON predicates
    // -------------------------------------------------------------------------

    @Override
    public final Condition isJson() {
        return new IsJson(this);
    }

    @Override
    public final Condition isNotJson() {
        return new IsNotJson(this);
    }

    // -------------------------------------------------------------------------
    // Numeric functions
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitAnd(T arg2) {
        return DSL.bitAnd((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitAnd(Field<T> arg2) {
        return DSL.bitAnd((Field) this, (Field<Number>) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitNand(T arg2) {
        return DSL.bitNand((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitNand(Field<T> arg2) {
        return DSL.bitNand((Field) this, (Field<Number>) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitNor(T arg2) {
        return DSL.bitNor((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitNor(Field<T> arg2) {
        return DSL.bitNor((Field) this, (Field<Number>) arg2);
    }

    @Override
    public final Field<T> bitNot() {
        return DSL.bitNot((Field) this);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitOr(T arg2) {
        return DSL.bitOr((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitOr(Field<T> arg2) {
        return DSL.bitOr((Field) this, (Field<Number>) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitXNor(T arg2) {
        return DSL.bitXNor((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitXNor(Field<T> arg2) {
        return DSL.bitXNor((Field) this, (Field<Number>) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitXor(T arg2) {
        return DSL.bitXor((Field) this, (Number) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> bitXor(Field<T> arg2) {
        return DSL.bitXor((Field) this, (Field<Number>) arg2);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> mod(Number divisor) {
        return new Mod(this, Tools.field(divisor));
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> mod(Field<? extends Number> divisor) {
        return new Mod(this, nullSafe(divisor, getDataType()));
    }

    @Override
    public final Field<T> modulo(Number divisor) {
        return mod(divisor);
    }

    @Override
    public final Field<T> modulo(Field<? extends Number> divisor) {
        return mod(divisor);
    }

    @Override
    public final Field<T> rem(Number divisor) {
        return mod(divisor);
    }

    @Override
    public final Field<T> rem(Field<? extends Number> divisor) {
        return mod(divisor);
    }

    @Override
    public final Field<BigDecimal> power(Number exponent) {
        return DSL.power((Field) this, exponent);
    }

    @Override
    public final Field<BigDecimal> power(Field<? extends Number> exponent) {
        return DSL.power((Field) this, exponent);
    }

    @Override
    public final Field<BigDecimal> pow(Number exponent) {
        return power(exponent);
    }

    @Override
    public final Field<BigDecimal> pow(Field<? extends Number> exponent) {
        return power(exponent);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> shl(Number count) {
        return DSL.shl((Field) this, count);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> shl(Field<? extends Number> count) {
        return DSL.shl((Field) this, count);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> shr(Number count) {
        return DSL.shr((Field) this, count);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Field<T> shr(Field<? extends Number> count) {
        return DSL.shr((Field) this, count);
    }

    // -------------------------------------------------------------------------
    // String functions
    // -------------------------------------------------------------------------

    @Override
    public final Condition contains(T content) {
        return new Contains<>(this, Tools.field(content, this));
    }

    @Override
    public final Condition contains(Field<T> content) {
        return new Contains<>(this, nullSafe(content, getDataType()));
    }

    @Override
    public final Condition containsIgnoreCase(T content) {
        return new ContainsIgnoreCase<>(this, Tools.field(content, this));
    }

    @Override
    public final Condition containsIgnoreCase(Field<T> content) {
        return new ContainsIgnoreCase<>(this, nullSafe(content, getDataType()));
    }

    @Override
    public final Condition endsWith(T suffix) {
        return new EndsWith<>(this, Tools.field(suffix, this));
    }

    @Override
    public final Condition endsWith(Field<T> suffix) {
        return new EndsWith<>(this, nullSafe(suffix, getDataType()));
    }

    @Override
    public final Condition endsWithIgnoreCase(T suffix) {
        return new EndsWithIgnoreCase<>(this, Tools.field(suffix, this));
    }

    @Override
    public final Condition endsWithIgnoreCase(Field<T> suffix) {
        return new EndsWithIgnoreCase<>(this, nullSafe(suffix, getDataType()));
    }

    @Override
    public final Condition startsWith(T prefix) {
        return new StartsWith<>(this, Tools.field(prefix, this));
    }

    @Override
    public final Condition startsWith(Field<T> prefix) {
        return new StartsWith<>(this, nullSafe(prefix, getDataType()));
    }

    @Override
    public final Condition startsWithIgnoreCase(T prefix) {
        return new StartsWithIgnoreCase<>(this, Tools.field(prefix, this));
    }

    @Override
    public final Condition startsWithIgnoreCase(Field<T> prefix) {
        return new StartsWithIgnoreCase<>(this, nullSafe(prefix, getDataType()));
    }

















    // ------------------------------------------------------------------------
    // XXX: Arithmetic operations
    // ------------------------------------------------------------------------

    @Override
    public final Field<T> neg() {
        return new Neg<>(this, false);
    }

    @Override
    public final Field<T> unaryMinus() {
        return neg();
    }

    @Override
    public final Field<T> unaryPlus() {
        return this;
    }

    @Override
    public final Field<T> add(Number value) {
        return add(Tools.field(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> add(Field<?> value) {
        Field<?> rhs;

        if (getDataType().isDateTime() && ((rhs = nullSafe(value)).getDataType().isNumeric() || rhs.getDataType().isInterval()))
            return new Expression<>(ADD, false, this, rhs);
        else
            return new Add<T>(this, (Field<T>) nullSafe(value, getDataType()));
    }

    @Override
    public final Field<T> sub(Number value) {
        return sub(Tools.field(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> sub(Field<?> value) {
        Field<?> rhs;

        if (getDataType().isDateTime() && ((rhs = nullSafe(value)).getDataType().isNumeric() || rhs.getDataType().isInterval()))
            return new Expression<>(SUBTRACT, false, this, rhs);
        else
            return new Sub<T>(this, (Field<T>) nullSafe(value, getDataType()));
    }

    @Override
    public final Field<T> mul(Number value) {
        return mul(Tools.field(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> mul(Field<? extends Number> value) {
        return new Mul<T>(this, (Field<T>) (getDataType().isTemporal() || nullSafe(value).getDataType().isTemporal()
            ? nullSafe(value)
            : nullSafe(value, getDataType())
        ));
    }

    @Override
    public final Field<T> div(Number value) {
        return div(Tools.field(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return new Div<T>(this, (Field<T>) (getDataType().isTemporal() || nullSafe(value).getDataType().isTemporal()
            ? nullSafe(value)
            : nullSafe(value, getDataType())
        ));
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
    public final Field<T> times(Number value) {
        return mul(value);
    }

    @Override
    public final Field<T> times(Field<? extends Number> value) {
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

    // ------------------------------------------------------------------------
    // XXX: Conditions created from this field
    // ------------------------------------------------------------------------

    /**
     * [#11200] Nest these constants to prevent initialisation deadlocks.
     */
    private static class BooleanValues {
        static final List<Field<String>> TRUE_VALUES  = Tools.map(Convert.TRUE_VALUES, v -> DSL.inline(v));
        static final List<Field<String>> FALSE_VALUES = Tools.map(Convert.FALSE_VALUES, v -> DSL.inline(v));
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public final Condition isTrue() {
        Class<?> type = getType();

        if (type == String.class)
            return ((Field<String>) this).in(BooleanValues.TRUE_VALUES);
        else if (Number.class.isAssignableFrom(type))
            return ((Field<Number>) this).equal(inline((Number) getDataType().convert(1)));
        else if (Boolean.class.isAssignableFrom(type))
            return ((Field<Boolean>) this).equal(inline(true, (DataType<Boolean>) getDataType()));
        else
            return castIfNeeded(this, String.class).in(BooleanValues.TRUE_VALUES);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public final Condition isFalse() {
        Class<?> type = getType();

        if (type == String.class)
            return ((Field<String>) this).in(BooleanValues.FALSE_VALUES);
        else if (Number.class.isAssignableFrom(type))
            return ((Field<Number>) this).equal(inline((Number) getDataType().convert(0)));
        else if (Boolean.class.isAssignableFrom(type))
            return ((Field<Boolean>) this).equal(inline(false, (DataType<Boolean>) getDataType()));
        else
            return castIfNeeded(this, String.class).in(BooleanValues.FALSE_VALUES);
    }

    @Override
    public final Condition similarTo(String value, char escape) {
        return similarTo(Tools.field(value), escape);
    }

    @Override
    public final Condition similarTo(Field<String> field, char escape) {
        return similarTo(field).escape(escape);
    }

    @Override
    public final Condition notSimilarTo(String value, char escape) {
        return notSimilarTo(Tools.field(value), escape);
    }

    @Override
    public final Condition notSimilarTo(Field<String> field, char escape) {
        return notSimilarTo(field).escape(escape);
    }

    @Override
    public final Condition like(String value, char escape) {
        return like(value).escape(escape);
    }

    @Override
    public final Condition like(Field<String> field, char escape) {
        return like(field).escape(escape);
    }

    @Override
    public final LikeEscapeStep like(QuantifiedSelect<Record1<String>> query) {
        return new QuantifiedComparisonCondition(query, this, LIKE);
    }

    @Override
    public final Condition likeIgnoreCase(String value, char escape) {
        return likeIgnoreCase(Tools.field(value), escape);
    }

    @Override
    public final Condition likeIgnoreCase(Field<String> field, char escape) {
        return likeIgnoreCase(field).escape(escape);
    }

    @Override
    public final Condition likeRegex(String pattern) {
        return likeRegex(Tools.field(pattern));
    }

    @Override
    public final Condition likeRegex(Field<String> pattern) {
        return new RegexpLike(this, nullSafe(pattern, getDataType()));
    }

    @Override
    public final Condition notLike(String value, char escape) {
        return notLike(Tools.field(value), escape);
    }

    @Override
    public final Condition notLike(Field<String> field, char escape) {
        return notLike(field).escape(escape);
    }

    @Override
    public final LikeEscapeStep notLike(QuantifiedSelect<Record1<String>> query) {
        return new QuantifiedComparisonCondition(query, this, NOT_LIKE);
    }

    @Override
    public final Condition notLikeIgnoreCase(String value, char escape) {
        return notLikeIgnoreCase(Tools.field(value), escape);
    }

    @Override
    public final Condition notLikeIgnoreCase(Field<String> field, char escape) {
        return notLikeIgnoreCase(field).escape(escape);
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
    public final Condition notContains(T value) {
        return contains(value).not();
    }

    @Override
    public final Condition notContains(Field<T> value) {
        return contains(value).not();
    }

    @Override
    public final Condition notContainsIgnoreCase(T value) {
        return containsIgnoreCase(value).not();
    }

    @Override
    public final Condition notContainsIgnoreCase(Field<T> value) {
        return containsIgnoreCase(value).not();
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

        return new InList<>(this, Tools.fields(values, this));
    }

    @Override
    public final Condition in(Field<?>... values) {
        return new InList<>(this, nullSafeList(values, getDataType()));
    }

    @Override
    public final Condition in(Collection<?> values) {
        return in(map(values, (Object v) -> Tools.field(v, this), Field[]::new));
    }

    @Override
    public final Condition in(Result<? extends Record1<T>> result) {
        return in(result.getValues(0, getType()));
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

        return new NotInList<>(this, Tools.fields(values, this));
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return new NotInList<>(this, nullSafeList(values, getDataType()));
    }

    @Override
    public final Condition notIn(Collection<?> values) {
        return notIn(map(values, (Object v) -> Tools.field(v, this), Field[]::new));
    }

    @Override
    public final Condition notIn(Result<? extends Record1<T>> result) {
        return notIn(result.getValues(0, getType()));
    }

    @Override
    public final Condition between(T minValue, T maxValue) {
        return between(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition between(Field<T> minValue, Field<T> maxValue) {
        return between(nullSafe(minValue, getDataType())).and(nullSafe(maxValue, getDataType()));
    }

    @Override
    public final Condition betweenSymmetric(T minValue, T maxValue) {
        return betweenSymmetric(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition betweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return betweenSymmetric(nullSafe(minValue, getDataType())).and(nullSafe(maxValue, getDataType()));
    }

    @Override
    public final Condition notBetween(T minValue, T maxValue) {
        return notBetween(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition notBetween(Field<T> minValue, Field<T> maxValue) {
        return notBetween(nullSafe(minValue, getDataType())).and(nullSafe(maxValue, getDataType()));
    }

    @Override
    public final Condition notBetweenSymmetric(T minValue, T maxValue) {
        return notBetweenSymmetric(Tools.field(minValue, this), Tools.field(maxValue, this));
    }

    @Override
    public final Condition notBetweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return notBetweenSymmetric(nullSafe(minValue, getDataType())).and(nullSafe(maxValue, getDataType()));
    }

    @Override
    public final BetweenAndStep<T> between(T minValue) {
        return between(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> between(Field<T> minValue) {
        return new BetweenCondition<>(this, nullSafe(minValue, getDataType()), false, false);
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(T minValue) {
        return betweenSymmetric(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(Field<T> minValue) {
        return new BetweenCondition<>(this, nullSafe(minValue, getDataType()), false, true);
    }

    @Override
    public final BetweenAndStep<T> notBetween(T minValue) {
        return notBetween(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> notBetween(Field<T> minValue) {
        return new BetweenCondition<>(this, nullSafe(minValue, getDataType()), true, false);
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(T minValue) {
        return notBetweenSymmetric(Tools.field(minValue, this));
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(Field<T> minValue) {
        return new BetweenCondition<>(this, nullSafe(minValue, getDataType()), true, true);
    }

    @Override
    public final Condition eq(QuantifiedSelect<? extends Record1<T>> query) {
        return equal(query);
    }

    @Override
    public final Condition ne(QuantifiedSelect<? extends Record1<T>> query) {
        return notEqual(query);
    }

    @Override
    public final Condition lt(QuantifiedSelect<? extends Record1<T>> query) {
        return lessThan(query);
    }

    @Override
    public final Condition le(QuantifiedSelect<? extends Record1<T>> query) {
        return lessOrEqual(query);
    }

    @Override
    public final Condition gt(QuantifiedSelect<? extends Record1<T>> query) {
        return greaterThan(query);
    }

    @Override
    public final Condition ge(QuantifiedSelect<? extends Record1<T>> query) {
        return greaterOrEqual(query);
    }

    @Override
    public final Condition equalIgnoreCase(String value) {
        return equalIgnoreCase(Tools.field(value));
    }

    @Override
    public final Condition equalIgnoreCase(Field<String> value) {
        return DSL.lower(castIfNeeded(this, String.class)).equal(DSL.lower(value));
    }

    @Override
    public final Condition equal(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(EQUALS, query);
    }

    @Override
    public final Condition notEqualIgnoreCase(String value) {
        return notEqualIgnoreCase(Tools.field(value));
    }

    @Override
    public final Condition notEqualIgnoreCase(Field<String> value) {
        return DSL.lower(castIfNeeded(this, String.class)).notEqual(DSL.lower(value));
    }

    @Override
    public final Condition notEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(NOT_EQUALS, query);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(LESS, query);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(LESS_OR_EQUAL, query);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(GREATER, query);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return compare(GREATER_OR_EQUAL, query);
    }

    @Override
    public final Condition compare(Comparator comparator, T value) {
        return compare(comparator, Tools.field(value, this));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final Condition compare(Comparator comparator, Field<T> field) {
        switch (comparator) {
            case EQUALS:
                return new Eq<>(this, nullSafe(field, getDataType()));
            case GREATER:
                return new Gt<>(this, nullSafe(field, getDataType()));
            case GREATER_OR_EQUAL:
                return new Ge<>(this, nullSafe(field, getDataType()));
            case LESS:
                return new Lt<>(this, nullSafe(field, getDataType()));
            case LESS_OR_EQUAL:
                return new Le<>(this, nullSafe(field, getDataType()));
            case NOT_EQUALS:
                return new Ne<>(this, nullSafe(field, getDataType()));

            case LIKE:
                return new Like(this, (Field) nullSafe(field, getDataType()));
            case LIKE_IGNORE_CASE:
                return new LikeIgnoreCase(this, (Field) nullSafe(field, getDataType()));
            case SIMILAR_TO:
                return new SimilarTo(this, (Field) nullSafe(field, getDataType()));
            case NOT_LIKE:
                return new NotLike(this, (Field) nullSafe(field, getDataType()));
            case NOT_LIKE_IGNORE_CASE:
                return new NotLikeIgnoreCase(this, (Field) nullSafe(field, getDataType()));
            case NOT_SIMILAR_TO:
                return new NotSimilarTo(this, (Field) nullSafe(field, getDataType()));

            case IS_DISTINCT_FROM:
                return new IsDistinctFrom<>(this, nullSafe(field, getDataType()));
            case IS_NOT_DISTINCT_FROM:
                return new IsNotDistinctFrom<>(this, nullSafe(field, getDataType()));

            case IN:
                if (field instanceof ScalarSubquery)
                    return new In<>(this, (Select<? extends Record1<T>>) ((ScalarSubquery<?>) field).query);

                break;

            case NOT_IN:
                if (field instanceof ScalarSubquery)
                    return new NotIn<>(this, (Select<? extends Record1<T>>) ((ScalarSubquery<?>) field).query);

                break;
        }

        throw new IllegalArgumentException("Comparator not supported: " + comparator);
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record1<T>> query) {
        return compare(comparator, new ScalarSubquery<>(query, getDataType()));
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
        if (getDataType().isNumeric())
            return (Field<Z>) this;
        else
            return (Field<Z>) cast(BigDecimal.class);
    }

    @SuppressWarnings("unchecked")
    private final Field<String> varchar() {
        if (getDataType().isString())
            return (Field<String>) this;
        else
            return cast(String.class);
    }

    @SuppressWarnings("unchecked")
    private final <Z extends java.util.Date> Field<Z> date() {
        if (getDataType().isTemporal())
            return (Field<Z>) this;
        else
            return (Field<Z>) cast(Timestamp.class);
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
    public final Field<String> collate(String collation) {
        return collate(DSL.collation(collation));
    }

    @Override
    public final Field<String> collate(Name collation) {
        return collate(DSL.collation(collation));
    }

    @Override
    public final Field<String> collate(Collation collation) {
        return new Collated(this, collation);
    }

    @Override
    @Deprecated
    public final Field<String> concat(Field<?>... fields) {
        return DSL.concat(Tools.combine(this, fields));
    }

    @Override
    @Deprecated
    public final Field<String> concat(String... values) {
        return DSL.concat(Tools.combine(this, Tools.fieldsArray(values)));
    }

    @Override
    public final Field<String> concat(char... values) {
        return concat(new String(values));
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
        return DSL.greatest(this, Tools.fieldsArray(others));
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
        return DSL.least(this, Tools.fieldsArray(others));
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
        return DSL.decode(this, Tools.field(search, this), Tools.field(result));
    }

    @Override
    @Deprecated
    public final <Z> Field<Z> decode(T search, Z result, Object... more) {
        Field<Z> r = Tools.field(result);
        DataType<?>[] types = new DataType[more.length];

        for (int i = 0; i < types.length - 1; i = i + 2) {
            types[i]     = getDataType();
            types[i + 1] = r.getDataType();
        }

        if (types.length % 2 == 1) {
            types[types.length - 1] = r.getDataType();
        }

        return DSL.decode(this, Tools.field(search, this), r, Tools.fieldsArray(more, types));
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
        return DSL.coalesce(this, Tools.combine(Tools.field(option, this), Tools.fields(options, this).toArray(EMPTY_FIELD)));
    }

    @Override
    @Deprecated
    public final Field<T> coalesce(Field<T> option, Field<?>... options) {
        return DSL.coalesce(this, Tools.combine(option, options));
    }
}
