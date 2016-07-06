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

import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.impl.Tools.blocking;
import static org.jooq.impl.Tools.consumeResultSets;
import static org.jooq.impl.Tools.DataKey.DATA_LOCK_ROWS_FOR_UPDATE;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.Table;
import org.jooq.conf.SettingsTools;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;

/**
 * A query that returns a {@link Result}
 *
 * @author Lukas Eder
 */
abstract class AbstractResultQuery<R extends Record> extends AbstractQuery implements ResultQuery<R> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -5588344253566055707L;
    private static final JooqLogger log              = JooqLogger.getLogger(AbstractResultQuery.class);

    private int                     maxRows;
    private int                     fetchSize;
    private int                     resultSetConcurrency;
    private int                     resultSetType;
    private int                     resultSetHoldability;
    private transient boolean       lazy;
    private transient boolean       many;
    private transient Cursor<R>     cursor;
    private Result<R>               result;
    private ResultsImpl             results;

    // Some temp variables for String interning
    private final Intern            intern = new Intern();

    AbstractResultQuery(Configuration configuration) {
        super(configuration);
    }

    /**
     * Get a list of fields provided a result set.
     */
    protected abstract Field<?>[] getFields(ResultSetMetaData rs) throws SQLException;

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(String param, Object value) {
        return (ResultQuery<R>) super.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(int index, Object value) {
        return (ResultQuery<R>) super.bind(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> queryTimeout(int timeout) {
        return (ResultQuery<R>) super.queryTimeout(timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> keepStatement(boolean k) {
        return (ResultQuery<R>) super.keepStatement(k);
    }

    @Override
    public final ResultQuery<R> maxRows(int rows) {
        this.maxRows = rows;
        return this;
    }

    @Override
    public final ResultQuery<R> fetchSize(int rows) {
        this.fetchSize = rows;
        return this;
    }

    @Override
    public final ResultQuery<R> resultSetConcurrency(int concurrency) {
        this.resultSetConcurrency = concurrency;
        return this;
    }

    @Override
    public final ResultQuery<R> resultSetType(int type) {
        this.resultSetType = type;
        return this;
    }

    @Override
    public final ResultQuery<R> resultSetHoldability(int holdability) {
        this.resultSetHoldability = holdability;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(Field<?>... fields) {
        intern.internFields = fields;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(int... fieldIndexes) {
        intern.internIndexes = fieldIndexes;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(String... fieldNameStrings) {
        intern.internNameStrings = fieldNameStrings;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(Name... fieldNames) {
        intern.internNames = fieldNames;
        return this;
    }

    @Override
    protected final void prepare(ExecuteContext ctx) throws SQLException {

        // [#1846] [#2265] [#2299] Users may explicitly specify how ResultSets
        // created by jOOQ behave. This will override any other default behaviour
        if (resultSetConcurrency != 0 || resultSetType != 0 || resultSetHoldability != 0) {
            int type = resultSetType != 0 ? resultSetType : ResultSet.TYPE_FORWARD_ONLY;
            int concurrency = resultSetConcurrency != 0 ? resultSetConcurrency : ResultSet.CONCUR_READ_ONLY;

            // Sybase doesn't support holdability. Avoid setting it!
            if (resultSetHoldability == 0) {
                ctx.statement(ctx.connection().prepareStatement(ctx.sql(), type, concurrency));
            }
            else {
                ctx.statement(ctx.connection().prepareStatement(ctx.sql(), type, concurrency, resultSetHoldability));
            }
        }

        // [#1296] These dialects do not implement FOR UPDATE. But the same
        // effect can be achieved using ResultSet.CONCUR_UPDATABLE
        else if (isForUpdate() && asList(CUBRID).contains(ctx.configuration().dialect().family())) {
            ctx.data(DATA_LOCK_ROWS_FOR_UPDATE, true);
            ctx.statement(ctx.connection().prepareStatement(ctx.sql(), TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE));
        }

        // Regular behaviour
        else {
            ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
        }

        // [#1263] [#4753] Allow for negative fetch sizes to support some non-standard
        // MySQL feature, where Integer.MIN_VALUE is used
        int f = SettingsTools.getFetchSize(fetchSize, ctx.settings());
        if (f != 0) {
            if (log.isDebugEnabled())
                log.debug("Setting fetch size", f);

            ctx.statement().setFetchSize(f);
        }

        // [#1854] [#4753] Set the max number of rows for this result query
        int m = SettingsTools.getMaxRows(maxRows, ctx.settings());
        if (m != 0) {
            ctx.statement().setMaxRows(m);
        }
    }

    @Override
    protected final int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        listener.executeStart(ctx);

        // [#4511] [#4753] PostgreSQL doesn't like fetchSize with autoCommit == true
        int f = SettingsTools.getFetchSize(fetchSize, ctx.settings());
        if (ctx.family() == POSTGRES && f != 0 && ctx.connection().getAutoCommit())
            log.info("Fetch Size", "A fetch size of " + f + " was set on a auto-commit PostgreSQL connection, which is not recommended. See http://jdbc.postgresql.org/documentation/head/query.html#query-with-cursor");










                         if (ctx.statement().execute()) {
            ctx.resultSet(ctx.statement().getResultSet());
        }

        listener.executeEnd(ctx);

        // Fetch a single result set
        if (!many) {
            if (ctx.resultSet() != null) {
                Field<?>[] fields = getFields(ctx.resultSet().getMetaData());
                cursor = new CursorImpl<R>(ctx, listener, fields, intern.internIndexes(fields), keepStatement(), keepResultSet(), getRecordType(), SettingsTools.getMaxRows(maxRows, ctx.settings()));

                if (!lazy) {
                    result = cursor.fetch();
                    cursor = null;
                }
            }
            else {
                result = new ResultImpl<R>(ctx.configuration());
            }
        }

        // Fetch several result sets
        else {
            results = new ResultsImpl(ctx.configuration());
            consumeResultSets(ctx, listener, results, intern);
        }

        return result != null ? result.size() : 0;
    }

    @Override
    protected final boolean keepResultSet() {
        return lazy;
    }

    /**
     * Subclasses should indicate whether they want an updatable {@link ResultSet}
     */
    abstract boolean isForUpdate();

    @Override
    public final Result<R> fetch() {
        execute();
        return result;
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
        return fetchLazy().stream();
    }

    @Override
    public final Stream<R> stream() {
        return fetchLazy().stream();
    }



    @Override
    public final Cursor<R> fetchLazy() {
        return fetchLazy(fetchSize);
    }

    @Override
    @Deprecated
    public final Cursor<R> fetchLazy(int size) {
        final int previousFetchSize = fetchSize;

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        lazy = true;
        fetchSize = size;

        try {
            execute();
        }
        finally {
            lazy = false;
            fetchSize = previousFetchSize;
        }

        return cursor;
    }

    @Override
    public final Results fetchMany() {

        // [#3515] TODO: Avoid modifying a Query's per-execution state
        many = true;

        try {
            execute();
        }
        finally {
            many = false;
        }

        return results;
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return fetch().getValues(field);
    }

    @Override
    public final <T> List<T> fetch(Field<?> field, Class<? extends T> type) {
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
    public final <T> List<T> fetch(int fieldIndex, Class<? extends T> type) {
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
    public final <T> List<T> fetch(String fieldName, Class<? extends T> type) {
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
    public final <T> List<T> fetch(Name fieldName, Class<? extends T> type) {
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
    public final <T> T fetchOne(Field<?> field, Class<? extends T> type) {
        return Convert.convert(fetchOne(field), type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Convert.convert(fetchOne(field), converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    public final <T> T fetchOne(int fieldIndex, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldIndex), type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchOne(fieldIndex), converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldName), type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchOne(fieldName), converter);
    }

    @Override
    public final Object fetchOne(Name fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <T> T fetchOne(Name fieldName, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldName), type);
    }

    @Override
    public final <U> U fetchOne(Name fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchOne(fieldName), converter);
    }

    @Override
    public final R fetchOne() {
        return Tools.fetchOne(fetchLazy());
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
    public final <T> Optional<T> fetchOptional(Field<T> field) {
        return Optional.ofNullable(fetchOne(field));
    }

    @Override
    public final <T> Optional<T> fetchOptional(Field<?> field, Class<? extends T> type) {
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
    public final <T> Optional<T> fetchOptional(int fieldIndex, Class<? extends T> type) {
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
    public final <T> Optional<T> fetchOptional(String fieldName, Class<? extends T> type) {
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
    public final <T> Optional<T> fetchOptional(Name fieldName, Class<? extends T> type) {
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
    public final <T> T fetchAny(Field<?> field, Class<? extends T> type) {
        return Convert.convert(fetchAny(field), type);
    }

    @Override
    public final <T, U> U fetchAny(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Convert.convert(fetchAny(field), converter);
    }

    @Override
    public final Object fetchAny(int fieldIndex) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    public final <T> T fetchAny(int fieldIndex, Class<? extends T> type) {
        return Convert.convert(fetchAny(fieldIndex), type);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchAny(fieldIndex), converter);
    }

    @Override
    public final Object fetchAny(String fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <T> T fetchAny(String fieldName, Class<? extends T> type) {
        return Convert.convert(fetchAny(fieldName), type);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchAny(fieldName), converter);
    }

    @Override
    public final Object fetchAny(Name fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    public final <T> T fetchAny(Name fieldName, Class<? extends T> type) {
        return Convert.convert(fetchAny(fieldName), type);
    }

    @Override
    public final <U> U fetchAny(Name fieldName, Converter<?, ? extends U> converter) {
        return Convert.convert(fetchAny(fieldName), converter);
    }

    @Override
    public final R fetchAny() {
        Cursor<R> c = fetchLazy();

        try {
            return c.fetchOne();
        }
        finally {
            c.close();
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

    @SuppressWarnings("unchecked")
    @Override
    public final R[] fetchArray() {
        Result<R> r = fetch();
        return r.toArray((R[]) Array.newInstance(getRecordType(), r.size()));
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) {
        return fetch().intoArray(fieldIndex);
    }

    @Override
    public final <T> T[] fetchArray(int fieldIndex, Class<? extends T> type) {
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
    public final <T> T[] fetchArray(String fieldName, Class<? extends T> type) {
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
    public final <T> T[] fetchArray(Name fieldName, Class<? extends T> type) {
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
    public final <T> T[] fetchArray(Field<?> field, Class<? extends T> type) {
        return fetch().intoArray(field, type);
    }

    @Override
    public final <T, U> U[] fetchArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetch().intoArray(field, converter);
    }

    @Override
    public final Set<?> fetchSet(int fieldIndex) {
        return fetch().intoSet(fieldIndex);
    }

    @Override
    public final <T> Set<T> fetchSet(int fieldIndex, Class<? extends T> type) {
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
    public final <T> Set<T> fetchSet(String fieldName, Class<? extends T> type) {
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
    public final <T> Set<T> fetchSet(Name fieldName, Class<? extends T> type) {
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
    public final <T> Set<T> fetchSet(Field<?> field, Class<? extends T> type) {
        return fetch().intoSet(field, type);
    }

    @Override
    public final <T, U> Set<U> fetchSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetch().intoSet(field, converter);
    }

    /**
     * Subclasses may override this method
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Class<? extends R> getRecordType() {
        return null;
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) {
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

    @Override
    @Deprecated
    public final org.jooq.FutureResult<R> fetchLater() {
        ExecutorService executor = newSingleThreadExecutor();
        Future<Result<R>> future = executor.submit(new ResultQueryCallable());
        return new FutureResultImpl<R>(future, executor);
    }

    @Override
    @Deprecated
    public final org.jooq.FutureResult<R> fetchLater(ExecutorService executor) {
        Future<Result<R>> future = executor.submit(new ResultQueryCallable());
        return new FutureResultImpl<R>(future);
    }

    @Override
    public final Result<R> getResult() {
        return result;
    }

    /**
     * A wrapper for the {@link ResultQuery#fetch()} method
     */
    private final class ResultQueryCallable implements Callable<Result<R>> {

        @Override
        public final Result<R> call() throws Exception {
            return fetch();
        }
    }
}
