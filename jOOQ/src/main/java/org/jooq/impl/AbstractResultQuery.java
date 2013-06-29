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

import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.Utils.DATA_LOCK_ROWS_FOR_UPDATE;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.FutureResult;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.exception.DataTypeException;
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
    private int                     resultSetConcurrency;
    private int                     resultSetType;
    private int                     resultSetHoldability;
    private transient boolean       lazy;
    private transient int           size;
    private transient boolean       many;
    private transient Cursor<R>     cursor;
    private Result<R>               result;
    private List<Result<Record>>    results;

    // Some temp variables for String interning
    private int[]                   internIndexes;
    private Field<?>[]              internFields;
    private String[]                internNames;

    AbstractResultQuery(Configuration configuration) {
        super(configuration);
    }

    /**
     * Get a list of fields provided a result set.
     */
    protected abstract Field<?>[] getFields(ResultSetMetaData rs) throws SQLException;

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(String param, Object value) throws IllegalArgumentException, DataTypeException {
        return (ResultQuery<R>) super.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResultQuery<R> bind(int index, Object value) throws IllegalArgumentException, DataTypeException {
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
        this.internFields = fields;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(int... fieldIndexes) {
        this.internIndexes = fieldIndexes;
        return this;
    }

    @Override
    public final ResultQuery<R> intern(String... fieldNames) {
        this.internNames = fieldNames;
        return this;
    }

    private final int[] internIndexes(Field<?>[] fields) {
        if (internIndexes != null) {
            return internIndexes;
        }
        else if (internFields != null) {
            return new Fields<Record>(fields).indexesOf(internFields);
        }
        else if (internNames != null) {
            return new Fields<Record>(fields).indexesOf(internNames);
        }

        return null;
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
        else if (isForUpdate() && asList(CUBRID, SQLSERVER).contains(ctx.configuration().dialect().family())) {
            ctx.data(DATA_LOCK_ROWS_FOR_UPDATE, true);
            ctx.statement(ctx.connection().prepareStatement(ctx.sql(), TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE));
        }

        // Regular behaviour
        else {
            ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
        }

        // [#1263] Allow for negative fetch sizes to support some non-standard
        // MySQL feature, where Integer.MIN_VALUE is used
        if (size != 0) {
            if (log.isDebugEnabled())
                log.debug("Setting fetch size", size);

            ctx.statement().setFetchSize(size);
        }

        // [#1854] Set the max number of rows for this result query
        if (maxRows != 0) {
            ctx.statement().setMaxRows(maxRows);
        }
    }

    @Override
    protected final int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        listener.executeStart(ctx);

        // JTDS doesn't seem to implement PreparedStatement.execute()
        // correctly, at least not for sp_help
        if (ctx.configuration().dialect() == ASE) {
            ctx.resultSet(ctx.statement().executeQuery());
        }

        // [#1232] Avoid executeQuery() in order to handle queries that may
        // not return a ResultSet, e.g. SQLite's pragma foreign_key_list(table)
        else if (ctx.statement().execute()) {
            ctx.resultSet(ctx.statement().getResultSet());
        }

        listener.executeEnd(ctx);

        // Fetch a single result set
        if (!many) {
            if (ctx.resultSet() != null) {
                Field<?>[] fields = getFields(ctx.resultSet().getMetaData());
                cursor = new CursorImpl<R>(ctx, listener, fields, internIndexes(fields), keepStatement(), keepResultSet(), getRecordType());

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
            results = new ArrayList<Result<Record>>();
            boolean anyResults = false;

            while (ctx.resultSet() != null) {
                anyResults = true;

                Field<?>[] fields = new MetaDataFieldProvider(ctx.configuration(), ctx.resultSet().getMetaData()).getFields();
                Cursor<Record> c = new CursorImpl<Record>(ctx, listener, fields, internIndexes(fields), true, false);
                results.add(c.fetch());

                if (ctx.statement().getMoreResults()) {
                    ctx.resultSet(ctx.statement().getResultSet());
                }
                else {
                    ctx.resultSet(null);
                }
            }

            // Call this only when there was at least one ResultSet.
            // Otherwise, this call is not supported by ojdbc...
            if (anyResults) {
                ctx.statement().getMoreResults(Statement.CLOSE_ALL_RESULTS);
            }
        }

        return result != null ? result.size() : 0;
    }

    @Override
    protected final boolean keepResultSet() {
        return lazy;
    }

    /**
     * Subclasses should indicate whether a Postgres refcursor is being selected
     */
    abstract boolean isSelectingRefCursor();

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
    public final Cursor<R> fetchLazy() {
        return fetchLazy(0);
    }

    @Override
    public final Cursor<R> fetchLazy(int fetchSize) {
        lazy = true;
        size = fetchSize;

        try {
            execute();
        }
        finally {
            lazy = false;
            size = 0;
        }

        return cursor;
    }

    @Override
    public final List<Result<Record>> fetchMany() {
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
    public final <T, U> List<U> fetch(Field<T> field, Converter<? super T, U> converter) {
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
    public final <U> List<U> fetch(int fieldIndex, Converter<?, U> converter) {
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
    public final <U> List<U> fetch(String fieldName, Converter<?, U> converter) {
        return fetch().getValues(fieldName, converter);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        R record = fetchOne();
        return record == null ? null : record.getValue(field);
    }

    @Override
    public final <T> T fetchOne(Field<?> field, Class<? extends T> type) {
        return Convert.convert(fetchOne(field), type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, U> converter) {
        return Convert.convert(fetchOne(field), converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        R record = fetchOne();
        return record == null ? null : record.getValue(fieldIndex);
    }

    @Override
    public final <T> T fetchOne(int fieldIndex, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldIndex), type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, U> converter) {
        return Convert.convert(fetchOne(fieldIndex), converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        R record = fetchOne();
        return record == null ? null : record.getValue(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldName), type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, U> converter) {
        return Convert.convert(fetchOne(fieldName), converter);
    }

    @Override
    public final R fetchOne() {
        return Utils.fetchOne(fetchLazy());
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
    public final <K> Map<K, R> fetchMap(Field<K> key) {
        return fetch().intoMap(key);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return fetch().intoMap(key, value);
    }

    @Override
    public final Map<Record, R> fetchMap(Field<?>[] keys) {
        return fetch().intoMap(keys);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return fetch().intoMap(keys, type);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return fetch().intoMap(key, type);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        return fetch().intoMaps();
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        R record = fetchOne();
        return record == null ? null : record.intoMap();
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return fetch().intoGroups(key);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return fetch().intoGroups(key, value);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return fetch().intoGroups(keys);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return fetch().intoGroups(keys, type);
    }

    @Override
    public final Object[][] fetchArrays() {
        return fetch().intoArray();
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
    public final <U> U[] fetchArray(int fieldIndex, Converter<?, U> converter) {
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
    public final <U> U[] fetchArray(String fieldName, Converter<?, U> converter) {
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
    public final <T, U> U[] fetchArray(Field<T> field, Converter<? super T, U> converter) {
        return fetch().intoArray(field, converter);
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
    public final Object[] fetchOneArray() {
        R record = fetchOne();
        return record == null ? null : record.intoArray();
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) {
        return fetch().into(type);
    }

    @Override
    public final <E> E fetchOneInto(Class<? extends E> type) {
        R record = fetchOne();
        return record == null ? null : record.into(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return fetch().into(table);
    }

    @Override
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        R record = fetchOne();
        return record == null ? null : record.into(table);
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
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return fetch().intoGroups(key, type);
    }

    @Override
    public final FutureResult<R> fetchLater() {
        ExecutorService executor = newSingleThreadExecutor();
        Future<Result<R>> future = executor.submit(new ResultQueryCallable());
        return new FutureResultImpl<R>(future, executor);
    }

    @Override
    public final FutureResult<R> fetchLater(ExecutorService executor) {
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
