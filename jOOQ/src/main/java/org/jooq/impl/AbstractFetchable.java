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

import static org.jooq.impl.Tools.blocking;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.Fetchable;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.R2DBC.BlockingRecordSubscription;
import org.jooq.impl.R2DBC.QuerySubscription;
import org.jooq.impl.R2DBC.ResultSubscriber;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.ConnectionFactory;

abstract class AbstractFetchable<R extends Record> extends AbstractQueryPart implements Fetchable<R>, Attachable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3663286102135167198L;

    @Override
    public Configuration configuration() {
        return super.configuration();
    }

    @Override
    public /* non-final */ Result<R> fetch() throws DataAccessException {
        throw new DataAccessException("Attempt to call fetch() on " + getClass());
    }

    @Override
    public /* non-final */ Cursor<R> fetchLazy() throws DataAccessException {
        return new ResultAsCursor<R>(fetch());
    }

    @Override
    public /* non-final */ Results fetchMany() throws DataAccessException {
        throw new DataAccessException("Attempt to call fetchMany() on " + getClass());
    }

    /**
     * Get a list of fields provided a result set.
     */
    abstract Field<?>[] getFields(ResultSetMetaData rs) throws SQLException;
    abstract Class<? extends R> getRecordType();

    /* non-final */ Cursor<R> fetchLazyNonAutoClosing() {
        return fetchLazy();
    }

    @Override
    public final ResultSet fetchResultSet() {
        return fetchLazy().resultSet();
    }

    @Override
    public final Iterator<R> iterator() {
        return fetch().iterator();
    }

    @Override
    public final CompletionStage<Result<R>> fetchAsync() {
        return fetchAsync(Tools.configuration(this).executorProvider().provide());
    }

    @Override
    public final CompletionStage<Result<R>> fetchAsync(Executor executor) {
        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(blocking(this::fetch), executor), () -> executor);
    }

    @Override
    public final Stream<R> fetchStream() {
        return Stream.of(1).flatMap(i -> fetchLazy().stream());
    }

    @Override
    public final <E> Stream<E> fetchStreamInto(Class<? extends E> type) {
        return fetchStream().map(r -> r.into(type));
    }

    @Override
    public final <Z extends Record> Stream<Z> fetchStreamInto(Table<Z> table) {
        return fetchStream().map(r -> r.into(table));
    }

    @Override
    public final Stream<R> stream() {
        return fetchStream();
    }

    @Override
    public final <X, A> X collect(Collector<? super R, A, X> collector) {
        try (Cursor<R> c = fetchLazyNonAutoClosing()) {
            return c.collect(collector);
        }
    }

    @Override
    public final Publisher<R> publisher() {
        ConnectionFactory cf = configuration().connectionFactory();

        if (!(cf instanceof NoConnectionFactory))
            return subscriber -> subscriber.onSubscribe(new QuerySubscription<>(this, subscriber, ResultSubscriber::new));
        else
            return subscriber -> subscriber.onSubscribe(new BlockingRecordSubscription<>(this, subscriber));
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return fetch().getValues(field);
    }

    @Override
    public final <U> List<U> fetch(Field<?> field, Class<? extends U> type) {
        return fetch().getValues(field, type);
    }

    @Override
    public final <T, U> List<U> fetch(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetch().getValues(field, converter);
    }

    @Override
    public final List<?> fetch(int fieldIndex) {
        return fetch().getValues(fieldIndex);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Class<? extends U> type) {
        return fetch().getValues(fieldIndex, type);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Converter<?, ? extends U> converter) {
        return fetch().getValues(fieldIndex, converter);
    }

    @Override
    public final List<?> fetch(String fieldName) {
        return fetch().getValues(fieldName);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Class<? extends U> type) {
        return fetch().getValues(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Converter<?, ? extends U> converter) {
        return fetch().getValues(fieldName, converter);
    }

    @Override
    public final List<?> fetch(Name fieldName) {
        return fetch().getValues(fieldName);
    }

    @Override
    public final <U> List<U> fetch(Name fieldName, Class<? extends U> type) {
        return fetch().getValues(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(Name fieldName, Converter<?, ? extends U> converter) {
        return fetch().getValues(fieldName, converter);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        R record = fetchOne();
        return record == null ? null : record.get(field);
    }

    @Override
    public final <U> U fetchOne(Field<?> field, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(field, type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(field, converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex, type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex, converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    public final Object fetchOne(Name fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <U> U fetchOne(Name fieldName, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(Name fieldName, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    public final R fetchOne() {
        return Tools.fetchOne(fetchLazyNonAutoClosing(), hasLimit1());
    }

    @Override
    public final <E> E fetchOne(RecordMapper<? super R, E> mapper) {
        R record = fetchOne();
        return record == null ? null : mapper.map(record);
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        R record = fetchOne();
        return record == null ? null : record.intoMap();
    }

    @Override
    public final Object[] fetchOneArray() {
        R record = fetchOne();
        return record == null ? null : record.intoArray();
    }

    @Override
    public final <E> E fetchOneInto(Class<? extends E> type) {
        R record = fetchOne();
        return record == null ? null : record.into(type);
    }

    @Override
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        R record = fetchOne();
        return record == null ? null : record.into(table);
    }

    @Override
    public final <T> T fetchSingle(Field<T> field) {
        return fetchSingle().get(field);
    }

    @Override
    public final <U> U fetchSingle(Field<?> field, Class<? extends U> type) {
        return fetchSingle().get(field, type);
    }

    @Override
    public final <T, U> U fetchSingle(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetchSingle().get(field, converter);
    }

    @Override
    public final Object fetchSingle(int fieldIndex) {
        return fetchSingle().get(fieldIndex);
    }

    @Override
    public final <U> U fetchSingle(int fieldIndex, Class<? extends U> type) {
        return fetchSingle().get(fieldIndex, type);
    }

    @Override
    public final <U> U fetchSingle(int fieldIndex, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldIndex, converter);
    }

    @Override
    public final Object fetchSingle(String fieldName) {
        return fetchSingle().get(fieldName);
    }

    @Override
    public final <U> U fetchSingle(String fieldName, Class<? extends U> type) {
        return fetchSingle().get(fieldName, type);
    }

    @Override
    public final <U> U fetchSingle(String fieldName, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldName, converter);
    }

    @Override
    public final Object fetchSingle(Name fieldName) {
        return fetchSingle().get(fieldName);
    }

    @Override
    public final <U> U fetchSingle(Name fieldName, Class<? extends U> type) {
        return fetchSingle().get(fieldName, type);
    }

    @Override
    public final <U> U fetchSingle(Name fieldName, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldName, converter);
    }

    @Override
    public final R fetchSingle() {
        return Tools.fetchSingle(fetchLazyNonAutoClosing(), hasLimit1());
    }

    @Override
    public final <E> E fetchSingle(RecordMapper<? super R, E> mapper) {
        return mapper.map(fetchSingle());
    }

    @Override
    public final Map<String, Object> fetchSingleMap() {
        return fetchSingle().intoMap();
    }

    @Override
    public final Object[] fetchSingleArray() {
        return fetchSingle().intoArray();
    }

    @Override
    public final <E> E fetchSingleInto(Class<? extends E> type) {
        return fetchSingle().into(type);
    }

    @Override
    public final <Z extends Record> Z fetchSingleInto(Table<Z> table) {
        return fetchSingle().into(table);
    }

    @Override
    public final <T> Optional<T> fetchOptional(Field<T> field) {
        return Optional.ofNullable(fetchOne(field));
    }

    @Override
    public final <U> Optional<U> fetchOptional(Field<?> field, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(field, type));
    }

    @Override
    public final <T, U> Optional<U> fetchOptional(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(field, converter));
    }

    @Override
    public final Optional<?> fetchOptional(int fieldIndex) {
        return Optional.ofNullable(fetchOne(fieldIndex));
    }

    @Override
    public final <U> Optional<U> fetchOptional(int fieldIndex, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldIndex, type));
    }

    @Override
    public final <U> Optional<U> fetchOptional(int fieldIndex, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldIndex, converter));
    }

    @Override
    public final Optional<?> fetchOptional(String fieldName) {
        return Optional.ofNullable(fetchOne(fieldName));
    }

    @Override
    public final <U> Optional<U> fetchOptional(String fieldName, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldName, type));
    }

    @Override
    public final <U> Optional<U> fetchOptional(String fieldName, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldName, converter));
    }

    @Override
    public final Optional<?> fetchOptional(Name fieldName) {
        return Optional.ofNullable(fetchOne(fieldName));
    }

    @Override
    public final <U> Optional<U> fetchOptional(Name fieldName, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldName, type));
    }

    @Override
    public final <U> Optional<U> fetchOptional(Name fieldName, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldName, converter));
    }

    @Override
    public final Optional<R> fetchOptional() {
        return Optional.ofNullable(fetchOne());
    }

    @Override
    public final <E> Optional<E> fetchOptional(RecordMapper<? super R, E> mapper) {
        return Optional.ofNullable(fetchOne(mapper));
    }

    @Override
    public final Optional<Map<String, Object>> fetchOptionalMap() {
        return Optional.ofNullable(fetchOneMap());
    }

    @Override
    public final Optional<Object[]> fetchOptionalArray() {
        return Optional.ofNullable(fetchOneArray());
    }

    @Override
    public final <E> Optional<E> fetchOptionalInto(Class<? extends E> type) {
        return Optional.ofNullable(fetchOneInto(type));
    }

    @Override
    public final <Z extends Record> Optional<Z> fetchOptionalInto(Table<Z> table) {
        return Optional.ofNullable(fetchOneInto(table));
    }

    @Override
    public final <T> T fetchAny(Field<T> field) {
        R record = fetchAny();
        return record == null ? null : record.get(field);
    }

    @Override
    public final <U> U fetchAny(Field<?> field, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(field, type);
    }

    @Override
    public final <T, U> U fetchAny(Field<T> field, Converter<? super T, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(field, converter);
    }

    @Override
    public final Object fetchAny(int fieldIndex) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex, type);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex, converter);
    }

    @Override
    public final Object fetchAny(String fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    public final Object fetchAny(Name fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <U> U fetchAny(Name fieldName, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    public final <U> U fetchAny(Name fieldName, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    public final R fetchAny() {
        try (Cursor<R> c = fetchLazyNonAutoClosing()) {
            return c.fetchNext();
        }
    }

    @Override
    public final <E> E fetchAny(RecordMapper<? super R, E> mapper) {
        R record = fetchAny();
        return record == null ? null : mapper.map(record);
    }

    @Override
    public final Map<String, Object> fetchAnyMap() {
        R record = fetchAny();
        return record == null ? null : record.intoMap();
    }

    @Override
    public final Object[] fetchAnyArray() {
        R record = fetchAny();
        return record == null ? null : record.intoArray();
    }

    @Override
    public final <E> E fetchAnyInto(Class<? extends E> type) {
        R record = fetchAny();
        return record == null ? null : record.into(type);
    }

    @Override
    public final <Z extends Record> Z fetchAnyInto(Table<Z> table) {
        R record = fetchAny();
        return record == null ? null : record.into(table);
    }

    @Override
    public final <K> Map<K, R> fetchMap(Field<K> key) {
        return fetch().intoMap(key);
    }

    @Override
    public final Map<?, R> fetchMap(int keyFieldIndex) {
        return fetch().intoMap(keyFieldIndex);
    }

    @Override
    public final Map<?, R> fetchMap(String keyFieldName) {
        return fetch().intoMap(keyFieldName);
    }

    @Override
    public final Map<?, R> fetchMap(Name keyFieldName) {
        return fetch().intoMap(keyFieldName);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return fetch().intoMap(key, value);
    }

    @Override
    public final Map<?, ?> fetchMap(int keyFieldIndex, int valueFieldIndex) {
        return fetch().intoMap(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, ?> fetchMap(String keyFieldName, String valueFieldName) {
        return fetch().intoMap(keyFieldName, valueFieldName);
    }

    @Override
    public final Map<?, ?> fetchMap(Name keyFieldName, Name valueFieldName) {
        return fetch().intoMap(keyFieldName, valueFieldName);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return fetch().intoMap(key, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(int keyFieldIndex, Class<? extends E> type) {
        return fetch().intoMap(keyFieldIndex, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(String keyFieldName, Class<? extends E> type) {
        return fetch().intoMap(keyFieldName, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(Name keyFieldName, Class<? extends E> type) {
        return fetch().intoMap(keyFieldName, type);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(key, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldName, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldName, mapper);
    }

    @Override
    public final Map<Record, R> fetchMap(Field<?>[] keys) {
        return fetch().intoMap(keys);
    }

    @Override
    public final Map<Record, R> fetchMap(int[] keyFieldIndexes) {
        return fetch().intoMap(keyFieldIndexes);
    }

    @Override
    public final Map<Record, R> fetchMap(String[] keyFieldNames) {
        return fetch().intoMap(keyFieldNames);
    }

    @Override
    public final Map<Record, R> fetchMap(Name[] keyFieldNames) {
        return fetch().intoMap(keyFieldNames);
    }

    @Override
    public final Map<Record, Record> fetchMap(Field<?>[] keys, Field<?>[] values) {
        return fetch().intoMap(keys, values);
    }

    @Override
    public final Map<Record, Record> fetchMap(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return fetch().intoMap(keyFieldIndexes, valueFieldIndexes);
    }

    @Override
    public final Map<Record, Record> fetchMap(String[] keyFieldNames, String[] valueFieldNames) {
        return fetch().intoMap(keyFieldNames, valueFieldNames);
    }

    @Override
    public final Map<Record, Record> fetchMap(Name[] keyFieldNames, Name[] valueFieldNames) {
        return fetch().intoMap(keyFieldNames, valueFieldNames);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return fetch().intoMap(keys, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, Class<? extends E> type) {
        return fetch().intoMap(keyFieldIndexes, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, Class<? extends E> type) {
        return fetch().intoMap(keyFieldNames, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, Class<? extends E> type) {
        return fetch().intoMap(keyFieldNames, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keys, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldIndexes, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(keyFieldNames, mapper);
    }

    @Override
    public final <K> Map<K, R> fetchMap(Class<? extends K> keyType) {
        return fetch().intoMap(keyType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, Class<? extends V> valueType) {
        return fetch().intoMap(keyType, valueType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return fetch().intoMap(keyType, valueMapper);
    }

    @Override
    public final <K> Map<K, R> fetchMap(RecordMapper<? super R, K> keyMapper) {
        return fetch().intoMap(keyMapper);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return fetch().intoMap(keyMapper, valueType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return fetch().intoMap(keyMapper, valueMapper);
    }

    @Override
    public final <S extends Record> Map<S, R> fetchMap(Table<S> table) {
        return fetch().intoMap(table);
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, T> fetchMap(Table<S> keyTable, Table<T> valueTable) {
        return fetch().intoMap(keyTable, valueTable);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, Class<? extends E> type) {
        return fetch().intoMap(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        return fetch().intoMap(table, mapper);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        return fetch().intoMaps();
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return fetch().intoGroups(key);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(int keyFieldIndex) {
        return fetch().intoGroups(keyFieldIndex);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(String keyFieldName) {
        return fetch().intoGroups(keyFieldName);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(Name keyFieldName) {
        return fetch().intoGroups(keyFieldName);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return fetch().intoGroups(key, value);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(int keyFieldIndex, int valueFieldIndex) {
        return fetch().intoGroups(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(String keyFieldName, String valueFieldName) {
        return fetch().intoGroups(keyFieldName, valueFieldName);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(Name keyFieldName, Name valueFieldName) {
        return fetch().intoGroups(keyFieldName, valueFieldName);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return fetch().intoGroups(key, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldIndex, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(String keyFieldName, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldName, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(Name keyFieldName, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldName, type);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(key, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldName, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldName, mapper);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return fetch().intoGroups(keys);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(int[] keyFieldIndexes) {
        return fetch().intoGroups(keyFieldIndexes);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(String[] keyFieldNames) {
        return fetch().intoGroups(keyFieldNames);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Name[] keyFieldNames) {
        return fetch().intoGroups(keyFieldNames);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(Field<?>[] keys, Field<?>[] values) {
        return fetch().intoGroups(keys, values);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return fetch().intoGroups(keyFieldIndexes, valueFieldIndexes);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(String[] keyFieldNames, String[] valueFieldNames) {
        return fetch().intoGroups(keyFieldNames, valueFieldNames);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(Name[] keyFieldNames, Name[] valueFieldNames) {
        return fetch().intoGroups(keyFieldNames, valueFieldNames);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return fetch().intoGroups(keys, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldIndexes, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldNames, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, Class<? extends E> type) {
        return fetch().intoGroups(keyFieldNames, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldIndexes, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(keys, mapper);
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Class<? extends K> keyType) {
        return fetch().intoGroups(keyType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, Class<? extends V> valueType) {
        return fetch().intoGroups(keyType, valueType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return fetch().intoGroups(keyType, valueMapper);
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(RecordMapper<? super R, K> keyMapper) {
        return fetch().intoGroups(keyMapper);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return fetch().intoGroups(keyMapper, valueType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return fetch().intoGroups(keyMapper, valueMapper);
    }

    @Override
    public final <S extends Record> Map<S, Result<R>> fetchGroups(Table<S> table) {
        return fetch().intoGroups(table);
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, Result<T>> fetchGroups(Table<S> keyTable, Table<T> valueTable) {
        return fetch().intoGroups(keyTable, valueTable);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, Class<? extends E> type) {
        return fetch().intoGroups(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        return fetch().intoGroups(table, mapper);
    }

    @Override
    public final Object[][] fetchArrays() {
        return fetch().intoArrays();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final R[] fetchArray() {
        Result<R> r = fetch();

        if (r.isNotEmpty())
            return r.toArray((R[]) Array.newInstance(r.get(0).getClass(), r.size()));

        Class<? extends R> recordType;

        // TODO [#3185] Pull up getRecordType()
        if (this instanceof AbstractResultQuery)
            recordType = ((AbstractResultQuery<R>) this).getRecordType();
        else if (this instanceof SelectImpl)
            recordType = ((SelectImpl) this).getRecordType();
        else
            throw new DataAccessException("Attempt to call fetchArray() on " + getClass());

        return r.toArray((R[]) Array.newInstance(recordType, r.size()));
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) {
        return fetch().intoArray(fieldIndex);
    }

    @Override
    public final <U> U[] fetchArray(int fieldIndex, Class<? extends U> type) {
        return fetch().intoArray(fieldIndex, type);
    }

    @Override
    public final <U> U[] fetchArray(int fieldIndex, Converter<?, ? extends U> converter) {
        return fetch().intoArray(fieldIndex, converter);
    }

    @Override
    public final Object[] fetchArray(String fieldName) {
        return fetch().intoArray(fieldName);
    }

    @Override
    public final <U> U[] fetchArray(String fieldName, Class<? extends U> type) {
        return fetch().intoArray(fieldName, type);
    }

    @Override
    public final <U> U[] fetchArray(String fieldName, Converter<?, ? extends U> converter) {
        return fetch().intoArray(fieldName, converter);
    }

    @Override
    public final Object[] fetchArray(Name fieldName) {
        return fetch().intoArray(fieldName);
    }

    @Override
    public final <U> U[] fetchArray(Name fieldName, Class<? extends U> type) {
        return fetch().intoArray(fieldName, type);
    }

    @Override
    public final <U> U[] fetchArray(Name fieldName, Converter<?, ? extends U> converter) {
        return fetch().intoArray(fieldName, converter);
    }

    @Override
    public final <T> T[] fetchArray(Field<T> field) {
        return fetch().intoArray(field);
    }

    @Override
    public final <U> U[] fetchArray(Field<?> field, Class<? extends U> type) {
        return fetch().intoArray(field, type);
    }

    @Override
    public final <T, U> U[] fetchArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetch().intoArray(field, converter);
    }

    @Override
    public final <E> Set<E> fetchSet(RecordMapper<? super R, E> mapper) {
        return fetch().intoSet(mapper);
    }

    @Override
    public final Set<?> fetchSet(int fieldIndex) {
        return fetch().intoSet(fieldIndex);
    }

    @Override
    public final <U> Set<U> fetchSet(int fieldIndex, Class<? extends U> type) {
        return fetch().intoSet(fieldIndex, type);
    }

    @Override
    public final <U> Set<U> fetchSet(int fieldIndex, Converter<?, ? extends U> converter) {
        return fetch().intoSet(fieldIndex, converter);
    }

    @Override
    public final Set<?> fetchSet(String fieldName) {
        return fetch().intoSet(fieldName);
    }

    @Override
    public final <U> Set<U> fetchSet(String fieldName, Class<? extends U> type) {
        return fetch().intoSet(fieldName, type);
    }

    @Override
    public final <U> Set<U> fetchSet(String fieldName, Converter<?, ? extends U> converter) {
        return fetch().intoSet(fieldName, converter);
    }

    @Override
    public final Set<?> fetchSet(Name fieldName) {
        return fetch().intoSet(fieldName);
    }

    @Override
    public final <U> Set<U> fetchSet(Name fieldName, Class<? extends U> type) {
        return fetch().intoSet(fieldName, type);
    }

    @Override
    public final <U> Set<U> fetchSet(Name fieldName, Converter<?, ? extends U> converter) {
        return fetch().intoSet(fieldName, converter);
    }

    @Override
    public final <T> Set<T> fetchSet(Field<T> field) {
        return fetch().intoSet(field);
    }

    @Override
    public final <U> Set<U> fetchSet(Field<?> field, Class<? extends U> type) {
        return fetch().intoSet(field, type);
    }

    @Override
    public final <T, U> Set<U> fetchSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetch().intoSet(field, converter);
    }
    @Override
    public final <U> List<U> fetchInto(Class<? extends U> type) {
        return fetch().into(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return fetch().into(table);
    }

    @Override
    public final <H extends RecordHandler<? super R>> H fetchInto(H handler) {
        return fetch().into(handler);
    }

    @Override
    public final <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return fetch().map(mapper);
    }

    private final boolean hasLimit1() {
        if (this instanceof Select) {
            SelectQueryImpl<?> s = Tools.selectQueryImpl((Select<?>) this);

            if (s != null) {
                Limit l = s.getLimit();
                return !l.withTies() && !l.percent() && l.limitOne();
            }
        }

        return false;
    }
}
