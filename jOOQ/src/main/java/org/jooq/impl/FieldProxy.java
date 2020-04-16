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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.jooq.BetweenAndStep;
import org.jooq.BindContext;
import org.jooq.Binding;
import org.jooq.Clause;
import org.jooq.Collation;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
import org.jooq.Name;
// ...
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.exception.DataAccessException;

/**
 * A {@link Field} that acts as another field, allowing for the proxied field to
 * be replaced.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
final class FieldProxy<T> implements Field<T>, QueryPartInternal {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8311876498583467760L;

    AbstractField<T>          delegate;
    char[]                    sql;
    int                       position;

    FieldProxy(AbstractField<T> delegate, char[] sql, int position) {
        this.delegate = delegate;
        this.sql = sql;
        this.position = position;
    }

    @Override
    public final Converter<?, T> getConverter() {
        return delegate.getConverter();
    }

    @Override
    public final String getName() {
        return delegate.getName();
    }

    @Override
    public final Binding<?, T> getBinding() {
        return delegate.getBinding();
    }

    @Override
    public final Name getQualifiedName() {
        return delegate.getQualifiedName();
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate.toSQL(context);
    }

    @Override
    public final Class<T> getType() {
        return delegate.getType();
    }

    @Override
    public final DataType<T> getDataType() {
        return delegate.getDataType();
    }

    @Override
    public final Name getUnqualifiedName() {
        return delegate.getUnqualifiedName();
    }

    @Override
    public final DataType<T> getDataType(Configuration configuration) {
        return delegate.getDataType(configuration);
    }

    @Override
    public final String getComment() {
        return delegate.getComment();
    }

    @Override
    public final int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        delegate.bind(context);
    }

    @Override
    public final boolean equals(Object that) {
        return delegate.equals(that);
    }

    @Override
    public final boolean declaresFields() {
        return delegate.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return delegate.declaresTables();
    }

    @Override
    public final boolean declaresWindows() {
        return delegate.declaresWindows();
    }

    @Override
    public final boolean declaresCTE() {
        return delegate.declaresCTE();
    }

    @Override
    public final boolean generatesCast() {
        return delegate.generatesCast();
    }

    @Override
    public final void accept(Context<?> ctx) {
        delegate.accept(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate.clauses(ctx);
    }

    @Override
    public final Field<T> field(Record record) {
        return delegate.field(record);
    }

    @Override
    public final String toString() {
        return delegate.toString();
    }

    @Override
    public final T get(Record record) {
        return delegate.get(record);
    }

    @Override
    public final T getValue(Record record) {
        return delegate.getValue(record);
    }

    @Override
    public final T original(Record record) {
        return delegate.original(record);
    }

    @Override
    public final boolean changed(Record record) {
        return delegate.changed(record);
    }

    @Override
    public final void reset(Record record) {
        delegate.reset(record);
    }

    @Override
    public final Record1<T> from(Record record) {
        return delegate.from(record);
    }

    @Override
    public final Field<T> as(String alias) {
        return delegate.as(alias);
    }

    @Override
    public final Field<T> as(Name alias) {
        return delegate.as(alias);
    }

    @Override
    public final Field<T> as(Field<?> otherField) {
        return delegate.as(otherField);
    }

    @Override
    public final Field<T> as(Function<? super Field<T>, ? extends String> aliasFunction) {
        return delegate.as(aliasFunction);
    }

    @Override
    public final <Z> Field<Z> cast(Field<Z> field) {
        return delegate.cast(field);
    }

    @Override
    public final <Z> Field<Z> cast(DataType<Z> type) {
        return delegate.cast(type);
    }

    @Override
    public final <Z> Field<Z> cast(Class<Z> type) {
        return delegate.cast(type);
    }

    @Override
    public final <Z> Field<Z> coerce(Field<Z> field) {
        return delegate.coerce(field);
    }

    @Override
    public final <Z> Field<Z> coerce(DataType<Z> type) {
        return delegate.coerce(type);
    }

    @Override
    public final <Z> Field<Z> coerce(Class<Z> type) {
        return delegate.coerce(type);
    }

    @Override
    public final SortField<T> asc() {
        return delegate.asc();
    }

    @Override
    public final SortField<T> desc() {
        return delegate.desc();
    }

    @Override
    public final SortField<T> sortDefault() {
        return delegate.sortDefault();
    }

    @Override
    public final SortField<T> sort(SortOrder order) {
        return delegate.sort(order);
    }

    @Override
    public final SortField<Integer> sortAsc(Collection<T> sortList) {
        return delegate.sortAsc(sortList);
    }

    @Override
    public final SortField<Integer> sortAsc(T... sortList) {
        return delegate.sortAsc(sortList);
    }

    @Override
    public final SortField<Integer> sortDesc(Collection<T> sortList) {
        return delegate.sortDesc(sortList);
    }

    @Override
    public final SortField<Integer> sortDesc(T... sortList) {
        return delegate.sortDesc(sortList);
    }

    @Override
    public final <Z> SortField<Z> sort(Map<T, Z> sortMap) {
        return delegate.sort(sortMap);
    }

    @Override
    public final Field<T> neg() {
        return delegate.neg();
    }

    @Override
    public final Field<T> unaryMinus() {
        return delegate.unaryMinus();
    }

    @Override
    public final Field<T> unaryPlus() {
        return delegate.unaryPlus();
    }

    @Override
    public final Field<T> add(Number value) {
        return delegate.add(value);
    }

    @Override
    public final Field<T> add(Field<?> value) {
        return delegate.add(value);
    }

    @Override
    public final Field<T> sub(Number value) {
        return delegate.sub(value);
    }

    @Override
    public final Field<T> sub(Field<?> value) {
        return delegate.sub(value);
    }

    @Override
    public final Field<T> mul(Number value) {
        return delegate.mul(value);
    }

    @Override
    public final Field<T> mul(Field<? extends Number> value) {
        return delegate.mul(value);
    }

    @Override
    public final Field<T> div(Number value) {
        return delegate.div(value);
    }

    @Override
    public final Field<T> div(Field<? extends Number> value) {
        return delegate.div(value);
    }

    @Override
    public final Field<T> mod(Number value) {
        return delegate.mod(value);
    }

    @Override
    public final Field<T> mod(Field<? extends Number> value) {
        return delegate.mod(value);
    }

    @Override
    public final Field<T> plus(Number value) {
        return delegate.plus(value);
    }

    @Override
    public final Field<T> plus(Field<?> value) {
        return delegate.plus(value);
    }

    @Override
    public final Field<T> subtract(Number value) {
        return delegate.subtract(value);
    }

    @Override
    public final Field<T> subtract(Field<?> value) {
        return delegate.subtract(value);
    }

    @Override
    public final Field<T> minus(Number value) {
        return delegate.minus(value);
    }

    @Override
    public final Field<T> minus(Field<?> value) {
        return delegate.minus(value);
    }

    @Override
    public final Field<T> multiply(Number value) {
        return delegate.multiply(value);
    }

    @Override
    public final Field<T> multiply(Field<? extends Number> value) {
        return delegate.multiply(value);
    }

    @Override
    public final Field<T> times(Number value) {
        return delegate.times(value);
    }

    @Override
    public final Field<T> times(Field<? extends Number> value) {
        return delegate.times(value);
    }

    @Override
    public final Field<T> divide(Number value) {
        return delegate.divide(value);
    }

    @Override
    public final Field<T> divide(Field<? extends Number> value) {
        return delegate.divide(value);
    }

    @Override
    public final Field<T> modulo(Number value) {
        return delegate.modulo(value);
    }

    @Override
    public final Field<T> modulo(Field<? extends Number> value) {
        return delegate.modulo(value);
    }

    @Override
    public final Field<T> rem(Number value) {
        return delegate.rem(value);
    }

    @Override
    public final Field<T> rem(Field<? extends Number> value) {
        return delegate.rem(value);
    }

    @Override
    public final Field<T> bitNot() {
        return delegate.bitNot();
    }

    @Override
    public final Field<T> bitAnd(T value) {
        return delegate.bitAnd(value);
    }

    @Override
    public final Field<T> bitAnd(Field<T> value) {
        return delegate.bitAnd(value);
    }

    @Override
    public final Field<T> bitNand(T value) {
        return delegate.bitNand(value);
    }

    @Override
    public final Field<T> bitNand(Field<T> value) {
        return delegate.bitNand(value);
    }

    @Override
    public final Field<T> bitOr(T value) {
        return delegate.bitOr(value);
    }

    @Override
    public final Field<T> bitOr(Field<T> value) {
        return delegate.bitOr(value);
    }

    @Override
    public final Field<T> bitNor(T value) {
        return delegate.bitNor(value);
    }

    @Override
    public final Field<T> bitNor(Field<T> value) {
        return delegate.bitNor(value);
    }

    @Override
    public final Field<T> bitXor(T value) {
        return delegate.bitXor(value);
    }

    @Override
    public final Field<T> bitXor(Field<T> value) {
        return delegate.bitXor(value);
    }

    @Override
    public final Field<T> bitXNor(T value) {
        return delegate.bitXNor(value);
    }

    @Override
    public final Field<T> bitXNor(Field<T> value) {
        return delegate.bitXNor(value);
    }

    @Override
    public final Field<T> shl(Number value) {
        return delegate.shl(value);
    }

    @Override
    public final Field<T> shl(Field<? extends Number> value) {
        return delegate.shl(value);
    }

    @Override
    public final Field<T> shr(Number value) {
        return delegate.shr(value);
    }

    @Override
    public final Field<T> shr(Field<? extends Number> value) {
        return delegate.shr(value);
    }

    @Override
    public final Condition isJson() {
        return delegate.isJson();
    }

    @Override
    public final Condition isNotJson() {
        return delegate.isNotJson();
    }

    @Override
    public final Condition isNull() {
        return delegate.isNull();
    }

    @Override
    public final Condition isNotNull() {
        return delegate.isNotNull();
    }

    @Override
    public final Condition isDistinctFrom(T value) {
        return delegate.isDistinctFrom(value);
    }

    @Override
    public final Condition isDistinctFrom(Field<T> field) {
        return delegate.isDistinctFrom(field);
    }

    @Override
    public final Condition isNotDistinctFrom(T value) {
        return delegate.isNotDistinctFrom(value);
    }

    @Override
    public final Condition isNotDistinctFrom(Field<T> field) {
        return delegate.isNotDistinctFrom(field);
    }

    @Override
    public final Condition isTrue() {
        return delegate.isTrue();
    }

    @Override
    public final Condition isFalse() {
        return delegate.isFalse();
    }

    @Override
    public final LikeEscapeStep similarTo(String value) {
        return delegate.similarTo(value);
    }

    @Override
    public final Condition similarTo(String value, char escape) {
        return delegate.similarTo(value, escape);
    }

    @Override
    public final LikeEscapeStep similarTo(Field<String> field) {
        return delegate.similarTo(field);
    }

    @Override
    public final Condition similarTo(Field<String> field, char escape) {
        return delegate.similarTo(field, escape);
    }

    @Override
    public final LikeEscapeStep notSimilarTo(String value) {
        return delegate.notSimilarTo(value);
    }

    @Override
    public final Condition notSimilarTo(String value, char escape) {
        return delegate.notSimilarTo(value, escape);
    }

    @Override
    public final LikeEscapeStep notSimilarTo(Field<String> field) {
        return delegate.notSimilarTo(field);
    }

    @Override
    public final Condition notSimilarTo(Field<String> field, char escape) {
        return delegate.notSimilarTo(field, escape);
    }

    @Override
    public final LikeEscapeStep like(String value) {
        return delegate.like(value);
    }

    @Override
    public final Condition like(String value, char escape) {
        return delegate.like(value, escape);
    }

    @Override
    public final LikeEscapeStep like(Field<String> field) {
        return delegate.like(field);
    }

    @Override
    public final Condition like(Field<String> field, char escape) {
        return delegate.like(field, escape);
    }

    @Override
    public final LikeEscapeStep like(QuantifiedSelect<Record1<String>> query) {
        return delegate.like(query);
    }

    @Override
    public final LikeEscapeStep likeIgnoreCase(String value) {
        return delegate.likeIgnoreCase(value);
    }

    @Override
    public final Condition likeIgnoreCase(String value, char escape) {
        return delegate.likeIgnoreCase(value, escape);
    }

    @Override
    public final LikeEscapeStep likeIgnoreCase(Field<String> field) {
        return delegate.likeIgnoreCase(field);
    }

    @Override
    public final Condition likeIgnoreCase(Field<String> field, char escape) {
        return delegate.likeIgnoreCase(field, escape);
    }

    @Override
    public final Condition likeRegex(String pattern) {
        return delegate.likeRegex(pattern);
    }

    @Override
    public final Condition likeRegex(Field<String> pattern) {
        return delegate.likeRegex(pattern);
    }

    @Override
    public final LikeEscapeStep notLike(String value) {
        return delegate.notLike(value);
    }

    @Override
    public final Condition notLike(String value, char escape) {
        return delegate.notLike(value, escape);
    }

    @Override
    public final LikeEscapeStep notLike(Field<String> field) {
        return delegate.notLike(field);
    }

    @Override
    public final Condition notLike(Field<String> field, char escape) {
        return delegate.notLike(field, escape);
    }

    @Override
    public final LikeEscapeStep notLike(QuantifiedSelect<Record1<String>> query) {
        return delegate.notLike(query);
    }

    @Override
    public final LikeEscapeStep notLikeIgnoreCase(String value) {
        return delegate.notLikeIgnoreCase(value);
    }

    @Override
    public final Condition notLikeIgnoreCase(String value, char escape) {
        return delegate.notLikeIgnoreCase(value, escape);
    }

    @Override
    public final LikeEscapeStep notLikeIgnoreCase(Field<String> field) {
        return delegate.notLikeIgnoreCase(field);
    }

    @Override
    public final Condition notLikeIgnoreCase(Field<String> field, char escape) {
        return delegate.notLikeIgnoreCase(field, escape);
    }

    @Override
    public final Condition notLikeRegex(String pattern) {
        return delegate.notLikeRegex(pattern);
    }

    @Override
    public final Condition notLikeRegex(Field<String> pattern) {
        return delegate.notLikeRegex(pattern);
    }

    @Override
    public final Condition contains(T value) {
        return delegate.contains(value);
    }

    @Override
    public final Condition contains(Field<T> value) {
        return delegate.contains(value);
    }

    @Override
    public final Condition notContains(T value) {
        return delegate.notContains(value);
    }

    @Override
    public final Condition notContains(Field<T> value) {
        return delegate.notContains(value);
    }

    @Override
    public final Condition containsIgnoreCase(T value) {
        return delegate.containsIgnoreCase(value);
    }

    @Override
    public final Condition containsIgnoreCase(Field<T> value) {
        return delegate.containsIgnoreCase(value);
    }

    @Override
    public final Condition notContainsIgnoreCase(T value) {
        return delegate.notContainsIgnoreCase(value);
    }

    @Override
    public final Condition notContainsIgnoreCase(Field<T> value) {
        return delegate.notContainsIgnoreCase(value);
    }

    @Override
    public final Condition startsWith(T value) {
        return delegate.startsWith(value);
    }

    @Override
    public final Condition startsWith(Field<T> value) {
        return delegate.startsWith(value);
    }

    @Override
    public final Condition startsWithIgnoreCase(T value) {
        return delegate.startsWithIgnoreCase(value);
    }

    @Override
    public final Condition startsWithIgnoreCase(Field<T> value) {
        return delegate.startsWithIgnoreCase(value);
    }

    @Override
    public final Condition endsWith(T value) {
        return delegate.endsWith(value);
    }

    @Override
    public final Condition endsWith(Field<T> value) {
        return delegate.endsWith(value);
    }

    @Override
    public final Condition endsWithIgnoreCase(T value) {
        return delegate.endsWithIgnoreCase(value);
    }

    @Override
    public final Condition endsWithIgnoreCase(Field<T> value) {
        return delegate.endsWithIgnoreCase(value);
    }

    @Override
    public final Condition in(T... values) {
        return delegate.in(values);
    }

    @Override
    public final Condition in(Field<?>... values) {
        return delegate.in(values);
    }

    @Override
    public final Condition in(Collection<?> values) {
        return delegate.in(values);
    }

    @Override
    public final Condition in(Result<? extends Record1<T>> result) {
        return delegate.in(result);
    }

    @Override
    public final Condition in(Select<? extends Record1<T>> query) {
        return delegate.in(query);
    }

    @Override
    public final Condition notIn(T... values) {
        return delegate.notIn(values);
    }

    @Override
    public final Condition notIn(Field<?>... values) {
        return delegate.notIn(values);
    }

    @Override
    public final Condition notIn(Collection<?> values) {
        return delegate.notIn(values);
    }

    @Override
    public final Condition notIn(Result<? extends Record1<T>> result) {
        return delegate.notIn(result);
    }

    @Override
    public final Condition notIn(Select<? extends Record1<T>> query) {
        return delegate.notIn(query);
    }

    @Override
    public final Condition between(T minValue, T maxValue) {
        return delegate.between(minValue, maxValue);
    }

    @Override
    public final Condition between(Field<T> minValue, Field<T> maxValue) {
        return delegate.between(minValue, maxValue);
    }

    @Override
    public final Condition betweenSymmetric(T minValue, T maxValue) {
        return delegate.betweenSymmetric(minValue, maxValue);
    }

    @Override
    public final Condition betweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return delegate.betweenSymmetric(minValue, maxValue);
    }

    @Override
    public final Condition notBetween(T minValue, T maxValue) {
        return delegate.notBetween(minValue, maxValue);
    }

    @Override
    public final Condition notBetween(Field<T> minValue, Field<T> maxValue) {
        return delegate.notBetween(minValue, maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(T minValue, T maxValue) {
        return delegate.notBetweenSymmetric(minValue, maxValue);
    }

    @Override
    public final Condition notBetweenSymmetric(Field<T> minValue, Field<T> maxValue) {
        return delegate.notBetweenSymmetric(minValue, maxValue);
    }

    @Override
    public final BetweenAndStep<T> between(T minValue) {
        return delegate.between(minValue);
    }

    @Override
    public final BetweenAndStep<T> between(Field<T> minValue) {
        return delegate.between(minValue);
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(T minValue) {
        return delegate.betweenSymmetric(minValue);
    }

    @Override
    public final BetweenAndStep<T> betweenSymmetric(Field<T> minValue) {
        return delegate.betweenSymmetric(minValue);
    }

    @Override
    public final BetweenAndStep<T> notBetween(T minValue) {
        return delegate.notBetween(minValue);
    }

    @Override
    public final BetweenAndStep<T> notBetween(Field<T> minValue) {
        return delegate.notBetween(minValue);
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(T minValue) {
        return delegate.notBetweenSymmetric(minValue);
    }

    @Override
    public final BetweenAndStep<T> notBetweenSymmetric(Field<T> minValue) {
        return delegate.notBetweenSymmetric(minValue);
    }

    @Override
    public final Condition eq(T value) {
        return delegate.eq(value);
    }

    @Override
    public final Condition eq(Field<T> field) {
        return delegate.eq(field);
    }

    @Override
    public final Condition eq(Select<? extends Record1<T>> query) {
        return delegate.eq(query);
    }

    @Override
    public final Condition eq(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.eq(query);
    }

    @Override
    public final Condition ne(T value) {
        return delegate.ne(value);
    }

    @Override
    public final Condition ne(Field<T> field) {
        return delegate.ne(field);
    }

    @Override
    public final Condition ne(Select<? extends Record1<T>> query) {
        return delegate.ne(query);
    }

    @Override
    public final Condition ne(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.ne(query);
    }

    @Override
    public final Condition lt(T value) {
        return delegate.lt(value);
    }

    @Override
    public final Condition lt(Field<T> field) {
        return delegate.lt(field);
    }

    @Override
    public final Condition lt(Select<? extends Record1<T>> query) {
        return delegate.lt(query);
    }

    @Override
    public final Condition lt(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.lt(query);
    }

    @Override
    public final Condition le(T value) {
        return delegate.le(value);
    }

    @Override
    public final Condition le(Field<T> field) {
        return delegate.le(field);
    }

    @Override
    public final Condition le(Select<? extends Record1<T>> query) {
        return delegate.le(query);
    }

    @Override
    public final Condition le(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.le(query);
    }

    @Override
    public final Condition gt(T value) {
        return delegate.gt(value);
    }

    @Override
    public final Condition gt(Field<T> field) {
        return delegate.gt(field);
    }

    @Override
    public final Condition gt(Select<? extends Record1<T>> query) {
        return delegate.gt(query);
    }

    @Override
    public final Condition gt(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.gt(query);
    }

    @Override
    public final Condition ge(T value) {
        return delegate.ge(value);
    }

    @Override
    public final Condition ge(Field<T> field) {
        return delegate.ge(field);
    }

    @Override
    public final Condition ge(Select<? extends Record1<T>> query) {
        return delegate.ge(query);
    }

    @Override
    public final Condition ge(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.ge(query);
    }

    @Override
    public final Condition equal(T value) {
        return delegate.equal(value);
    }

    @Override
    public final Condition equal(Field<T> field) {
        return delegate.equal(field);
    }

    @Override
    public final Condition equalIgnoreCase(String value) {
        return delegate.equalIgnoreCase(value);
    }

    @Override
    public final Condition equalIgnoreCase(Field<String> value) {
        return delegate.equalIgnoreCase(value);
    }

    @Override
    public final Condition equal(Select<? extends Record1<T>> query) {
        return delegate.equal(query);
    }

    @Override
    public final Condition equal(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.equal(query);
    }

    @Override
    public final Condition notEqual(T value) {
        return delegate.notEqual(value);
    }

    @Override
    public final Condition notEqual(Field<T> field) {
        return delegate.notEqual(field);
    }

    @Override
    public final Condition notEqualIgnoreCase(String value) {
        return delegate.notEqualIgnoreCase(value);
    }

    @Override
    public final Condition notEqualIgnoreCase(Field<String> value) {
        return delegate.notEqualIgnoreCase(value);
    }

    @Override
    public final Condition notEqual(Select<? extends Record1<T>> query) {
        return delegate.notEqual(query);
    }

    @Override
    public final Condition notEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.notEqual(query);
    }

    @Override
    public final Condition lessThan(T value) {
        return delegate.lessThan(value);
    }

    @Override
    public final Condition lessThan(Field<T> field) {
        return delegate.lessThan(field);
    }

    @Override
    public final Condition lessThan(Select<? extends Record1<T>> query) {
        return delegate.lessThan(query);
    }

    @Override
    public final Condition lessThan(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.lessThan(query);
    }

    @Override
    public final Condition lessOrEqual(T value) {
        return delegate.lessOrEqual(value);
    }

    @Override
    public final Condition lessOrEqual(Field<T> field) {
        return delegate.lessOrEqual(field);
    }

    @Override
    public final Condition lessOrEqual(Select<? extends Record1<T>> query) {
        return delegate.lessOrEqual(query);
    }

    @Override
    public final Condition lessOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.lessOrEqual(query);
    }

    @Override
    public final Condition greaterThan(T value) {
        return delegate.greaterThan(value);
    }

    @Override
    public final Condition greaterThan(Field<T> field) {
        return delegate.greaterThan(field);
    }

    @Override
    public final Condition greaterThan(Select<? extends Record1<T>> query) {
        return delegate.greaterThan(query);
    }

    @Override
    public final Condition greaterThan(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.greaterThan(query);
    }

    @Override
    public final Condition greaterOrEqual(T value) {
        return delegate.greaterOrEqual(value);
    }

    @Override
    public final Condition greaterOrEqual(Field<T> field) {
        return delegate.greaterOrEqual(field);
    }

    @Override
    public final Condition greaterOrEqual(Select<? extends Record1<T>> query) {
        return delegate.greaterOrEqual(query);
    }

    @Override
    public final Condition greaterOrEqual(QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.greaterOrEqual(query);
    }

    @Override
    public final Condition compare(Comparator comparator, T value) {
        return delegate.compare(comparator, value);
    }

    @Override
    public final Condition compare(Comparator comparator, Field<T> field) {
        return delegate.compare(comparator, field);
    }

    @Override
    public final Condition compare(Comparator comparator, Select<? extends Record1<T>> query) {
        return delegate.compare(comparator, query);
    }

    @Override
    public final Condition compare(Comparator comparator, QuantifiedSelect<? extends Record1<T>> query) {
        return delegate.compare(comparator, query);
    }











    @Override
    public final Field<Integer> sign() {
        return delegate.sign();
    }

    @Override
    public final Field<T> abs() {
        return delegate.abs();
    }

    @Override
    public final Field<T> round() {
        return delegate.round();
    }

    @Override
    public final Field<T> round(int decimals) {
        return delegate.round(decimals);
    }

    @Override
    public final Field<T> floor() {
        return delegate.floor();
    }

    @Override
    public final Field<T> ceil() {
        return delegate.ceil();
    }

    @Override
    public final Field<BigDecimal> sqrt() {
        return delegate.sqrt();
    }

    @Override
    public final Field<BigDecimal> exp() {
        return delegate.exp();
    }

    @Override
    public final Field<BigDecimal> ln() {
        return delegate.ln();
    }

    @Override
    public final Field<BigDecimal> log(int base) {
        return delegate.log(base);
    }

    @Override
    public final Field<BigDecimal> pow(Number exponent) {
        return delegate.pow(exponent);
    }

    @Override
    public final Field<BigDecimal> power(Number exponent) {
        return delegate.power(exponent);
    }

    @Override
    public final Field<BigDecimal> pow(Field<? extends Number> exponent) {
        return delegate.pow(exponent);
    }

    @Override
    public final Field<BigDecimal> power(Field<? extends Number> exponent) {
        return delegate.power(exponent);
    }

    @Override
    public final Field<BigDecimal> acos() {
        return delegate.acos();
    }

    @Override
    public final Field<BigDecimal> asin() {
        return delegate.asin();
    }

    @Override
    public final Field<BigDecimal> atan() {
        return delegate.atan();
    }

    @Override
    public final Field<BigDecimal> atan2(Number y) {
        return delegate.atan2(y);
    }

    @Override
    public final Field<BigDecimal> atan2(Field<? extends Number> y) {
        return delegate.atan2(y);
    }

    @Override
    public final Field<BigDecimal> cos() {
        return delegate.cos();
    }

    @Override
    public final Field<BigDecimal> sin() {
        return delegate.sin();
    }

    @Override
    public final Field<BigDecimal> tan() {
        return delegate.tan();
    }

    @Override
    public final Field<BigDecimal> cot() {
        return delegate.cot();
    }

    @Override
    public final Field<BigDecimal> sinh() {
        return delegate.sinh();
    }

    @Override
    public final Field<BigDecimal> cosh() {
        return delegate.cosh();
    }

    @Override
    public final Field<BigDecimal> tanh() {
        return delegate.tanh();
    }

    @Override
    public final Field<BigDecimal> coth() {
        return delegate.coth();
    }

    @Override
    public final Field<BigDecimal> deg() {
        return delegate.deg();
    }

    @Override
    public final Field<BigDecimal> rad() {
        return delegate.rad();
    }

    @Override
    public final Field<Integer> count() {
        return delegate.count();
    }

    @Override
    public final Field<Integer> countDistinct() {
        return delegate.countDistinct();
    }

    @Override
    public final Field<T> max() {
        return delegate.max();
    }

    @Override
    public final Field<T> min() {
        return delegate.min();
    }

    @Override
    public final Field<BigDecimal> sum() {
        return delegate.sum();
    }

    @Override
    public final Field<BigDecimal> avg() {
        return delegate.avg();
    }

    @Override
    public final Field<BigDecimal> median() {
        return delegate.median();
    }

    @Override
    public final Field<BigDecimal> stddevPop() {
        return delegate.stddevPop();
    }

    @Override
    public final Field<BigDecimal> stddevSamp() {
        return delegate.stddevSamp();
    }

    @Override
    public final Field<BigDecimal> varPop() {
        return delegate.varPop();
    }

    @Override
    public final Field<BigDecimal> varSamp() {
        return delegate.varSamp();
    }

    @Override
    public final WindowPartitionByStep<Integer> countOver() {
        return delegate.countOver();
    }

    @Override
    public final WindowPartitionByStep<T> maxOver() {
        return delegate.maxOver();
    }

    @Override
    public final WindowPartitionByStep<T> minOver() {
        return delegate.minOver();
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> sumOver() {
        return delegate.sumOver();
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> avgOver() {
        return delegate.avgOver();
    }

    @Override
    public final WindowIgnoreNullsStep<T> firstValue() {
        return delegate.firstValue();
    }

    @Override
    public final WindowIgnoreNullsStep<T> lastValue() {
        return delegate.lastValue();
    }

    @Override
    public final WindowIgnoreNullsStep<T> lead() {
        return delegate.lead();
    }

    @Override
    public final WindowIgnoreNullsStep<T> lead(int offset) {
        return delegate.lead(offset);
    }

    @Override
    public final WindowIgnoreNullsStep<T> lead(int offset, T defaultValue) {
        return delegate.lead(offset, defaultValue);
    }

    @Override
    public final WindowIgnoreNullsStep<T> lead(int offset, Field<T> defaultValue) {
        return delegate.lead(offset, defaultValue);
    }

    @Override
    public final WindowIgnoreNullsStep<T> lag() {
        return delegate.lag();
    }

    @Override
    public final WindowIgnoreNullsStep<T> lag(int offset) {
        return delegate.lag(offset);
    }

    @Override
    public final WindowIgnoreNullsStep<T> lag(int offset, T defaultValue) {
        return delegate.lag(offset, defaultValue);
    }

    @Override
    public final WindowIgnoreNullsStep<T> lag(int offset, Field<T> defaultValue) {
        return delegate.lag(offset, defaultValue);
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> stddevPopOver() {
        return delegate.stddevPopOver();
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> stddevSampOver() {
        return delegate.stddevSampOver();
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> varPopOver() {
        return delegate.varPopOver();
    }

    @Override
    public final WindowPartitionByStep<BigDecimal> varSampOver() {
        return delegate.varSampOver();
    }

    @Override
    public final Field<String> upper() {
        return delegate.upper();
    }

    @Override
    public final Field<String> lower() {
        return delegate.lower();
    }

    @Override
    public final Field<String> trim() {
        return delegate.trim();
    }

    @Override
    public final Field<String> rtrim() {
        return delegate.rtrim();
    }

    @Override
    public final Field<String> ltrim() {
        return delegate.ltrim();
    }

    @Override
    public final Field<String> rpad(Field<? extends Number> length) {
        return delegate.rpad(length);
    }

    @Override
    public final Field<String> rpad(int length) {
        return delegate.rpad(length);
    }

    @Override
    public final Field<String> rpad(Field<? extends Number> length, Field<String> character) {
        return delegate.rpad(length, character);
    }

    @Override
    public final Field<String> rpad(int length, char character) {
        return delegate.rpad(length, character);
    }

    @Override
    public final Field<String> lpad(Field<? extends Number> length) {
        return delegate.lpad(length);
    }

    @Override
    public final Field<String> lpad(int length) {
        return delegate.lpad(length);
    }

    @Override
    public final Field<String> lpad(Field<? extends Number> length, Field<String> character) {
        return delegate.lpad(length, character);
    }

    @Override
    public final Field<String> lpad(int length, char character) {
        return delegate.lpad(length, character);
    }

    @Override
    public final Field<String> repeat(Number count) {
        return delegate.repeat(count);
    }

    @Override
    public final Field<String> repeat(Field<? extends Number> count) {
        return delegate.repeat(count);
    }

    @Override
    public final Field<String> replace(Field<String> search) {
        return delegate.replace(search);
    }

    @Override
    public final Field<String> replace(String search) {
        return delegate.replace(search);
    }

    @Override
    public final Field<String> replace(Field<String> search, Field<String> replace) {
        return delegate.replace(search, replace);
    }

    @Override
    public final Field<String> replace(String search, String replace) {
        return delegate.replace(search, replace);
    }

    @Override
    public final Field<Integer> position(String search) {
        return delegate.position(search);
    }

    @Override
    public final Field<Integer> position(Field<String> search) {
        return delegate.position(search);
    }

    @Override
    public final Field<Integer> ascii() {
        return delegate.ascii();
    }

    @Override
    public final Field<String> collate(String collation) {
        return delegate.collate(collation);
    }

    @Override
    public final Field<String> collate(Name collation) {
        return delegate.collate(collation);
    }

    @Override
    public final Field<String> collate(Collation collation) {
        return delegate.collate(collation);
    }

    @Override
    public final Field<String> concat(Field<?>... fields) {
        return delegate.concat(fields);
    }

    @Override
    public final Field<String> concat(String... values) {
        return delegate.concat(values);
    }

    @Override
    public final Field<String> concat(char... values) {
        return delegate.concat(values);
    }

    @Override
    public final Field<String> substring(int startingPosition) {
        return delegate.substring(startingPosition);
    }

    @Override
    public final Field<String> substring(Field<? extends Number> startingPosition) {
        return delegate.substring(startingPosition);
    }

    @Override
    public final Field<String> substring(int startingPosition, int length) {
        return delegate.substring(startingPosition, length);
    }

    @Override
    public final Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return delegate.substring(startingPosition, length);
    }

    @Override
    public final Field<Integer> length() {
        return delegate.length();
    }

    @Override
    public final Field<Integer> charLength() {
        return delegate.charLength();
    }

    @Override
    public final Field<Integer> bitLength() {
        return delegate.bitLength();
    }

    @Override
    public final Field<Integer> octetLength() {
        return delegate.octetLength();
    }

    @Override
    public final Field<Integer> extract(DatePart datePart) {
        return delegate.extract(datePart);
    }

    @Override
    public final Field<T> greatest(T... others) {
        return delegate.greatest(others);
    }

    @Override
    public final Field<T> greatest(Field<?>... others) {
        return delegate.greatest(others);
    }

    @Override
    public final Field<T> least(T... others) {
        return delegate.least(others);
    }

    @Override
    public final Field<T> least(Field<?>... others) {
        return delegate.least(others);
    }

    @Override
    public final Field<T> nvl(T defaultValue) {
        return delegate.nvl(defaultValue);
    }

    @Override
    public final Field<T> nvl(Field<T> defaultValue) {
        return delegate.nvl(defaultValue);
    }

    @Override
    public final <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull) {
        return delegate.nvl2(valueIfNotNull, valueIfNull);
    }

    @Override
    public final <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        return delegate.nvl2(valueIfNotNull, valueIfNull);
    }

    @Override
    public final Field<T> nullif(T other) {
        return delegate.nullif(other);
    }

    @Override
    public final Field<T> nullif(Field<T> other) {
        return delegate.nullif(other);
    }

    @Override
    public final <Z> Field<Z> decode(T search, Z result) {
        return delegate.decode(search, result);
    }

    @Override
    public final <Z> Field<Z> decode(T search, Z result, Object... more) {
        return delegate.decode(search, result, more);
    }

    @Override
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result) {
        return delegate.decode(search, result);
    }

    @Override
    public final <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more) {
        return delegate.decode(search, result, more);
    }

    @Override
    public final Field<T> coalesce(T option, T... options) {
        return delegate.coalesce(option, options);
    }

    @Override
    public final Field<T> coalesce(Field<T> option, Field<?>... options) {
        return delegate.coalesce(option, options);
    }
}