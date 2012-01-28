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

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.FutureResult;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.InvalidResultException;
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

    private transient boolean       lazy;
    private transient int           size;
    private transient boolean       many;
    private transient Cursor<R>     cursor;
    private Result<R>               result;
    private List<Result<Record>>    results;

    AbstractResultQuery(Configuration configuration) {
        super(configuration);
    }

    /**
     * Get a list of fields provided a result set.
     */
    protected abstract List<Field<?>> getFields(ResultSetMetaData rs) throws SQLException;

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

    @Override
    protected final PreparedStatement prepare(Configuration configuration, String sql) throws SQLException {
        PreparedStatement statement = super.prepare(configuration, sql);

        if (size > 0) {
            if (log.isDebugEnabled())
                log.debug("Setting fetch size", size);

            statement.setFetchSize(size);
        }

        return statement;
    }

    @Override
    protected final int execute(Configuration configuration, PreparedStatement statement) throws SQLException {
        Connection connection = configuration.getConnection();
        boolean autoCommit = false;

        // [#706] Postgres requires two separate queries running in the same
        // transaction to be executed when fetching refcursor types
        if (configuration.getDialect() == SQLDialect.POSTGRES && isSelectingRefCursor()) {
            autoCommit = connection.getAutoCommit();

            if (autoCommit) {
                if (log.isDebugEnabled())
                    log.debug("Unsetting auto-commit", false);

                connection.setAutoCommit(false);
            }
        }

        try {
            ResultSet rs = statement.executeQuery();

            // Fetch a single result set
            if (!many) {
                FieldList fields = new FieldList(getFields(rs.getMetaData()));
                cursor = new CursorImpl<R>(configuration, fields, rs, statement, getRecordType());

                if (!lazy) {
                    result = cursor.fetch();
                    cursor = null;
                }
            }

            // Fetch several result sets
            else {
                results = new ArrayList<Result<Record>>();

                for (;;) {
                    FieldProvider fields = new MetaDataFieldProvider(configuration, rs.getMetaData());
                    Cursor<Record> c = new CursorImpl<Record>(configuration, fields, rs);
                    results.add(c.fetch());

                    if (statement.getMoreResults()) {
                        rs = statement.getResultSet();
                    }
                    else {
                        break;
                    }
                }

                statement.getMoreResults(Statement.CLOSE_ALL_RESULTS);
                statement.close();
            }
        }
        finally {
            if (autoCommit) {
                if (log.isDebugEnabled())
                    log.debug("Resetting auto-commit", autoCommit);

                connection.setAutoCommit(autoCommit);
            }
        }

        return result != null ? result.size() : 0;
    }

    @Override
    protected final boolean keepStatementOpen() {
        return lazy;
    }

    /**
     * Subclasses should indicate whether a Postgres refcursor is being selected
     */
    abstract boolean isSelectingRefCursor();

    @Override
    public final Result<R> fetch() {
        execute();
        return result;
    }

    @Override
    public final Cursor<R> fetchLazy() {
        return fetchLazy(0);
    }

    @Override
    public final Cursor<R> fetchLazy(int fetchSize) throws DataAccessException {
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
        execute();
        many = false;

        return results;
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return fetch().getValues(field);
    }

    @Override
    public final <T> List<T> fetch(Field<?> field, Class<? extends T> type) throws DataAccessException {
        return fetch().getValues(field, type);
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
    public final List<?> fetch(String fieldName) {
        return fetch().getValues(fieldName);
    }

    @Override
    public final <T> List<T> fetch(String fieldName, Class<? extends T> type) {
        return fetch().getValues(fieldName, type);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        R record = fetchOne();
        return record == null ? null : record.getValue(field);
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
    public final Object fetchOne(String fieldName) {
        R record = fetchOne();
        return record == null ? null : record.getValue(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) {
        return Convert.convert(fetchOne(fieldName), type);
    }

    @Override
    public final R fetchOne() {
        Result<R> r = fetch();

        if (r.size() == 1) {
            return r.get(0);
        }
        else if (r.size() > 1) {
            throw new InvalidResultException("Query returned more than one result");
        }

        return null;
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
        Map<K, R> map = new LinkedHashMap<K, R>();

        for (R record : fetch()) {
            if (map.put(record.getValue(key), record) != null) {
                throw new InvalidResultException("Key " + key + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (Map.Entry<K, R> entry : fetchMap(key).entrySet()) {
            map.put(entry.getKey(), entry.getValue().getValue(value));
        }

        return map;
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (R record : fetch()) {
            list.add(convertToMap(record));
        }

        return list;
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        return convertToMap(fetchOne());
    }

    private final Map<String, Object> convertToMap(R record) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        for (Field<?> field : record.getFields()) {
            if (map.put(field.getName(), record.getValue(field)) != null) {
                throw new InvalidResultException("Field " + field.getName() + " is not unique in Record for " + this);
            }
        }

        return map;
    }

    @Override
    public final Object[][] fetchArrays() {
        return fetch().intoArray();
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) {
        return fetch(fieldIndex).toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] fetchArray(int fieldIndex, Class<? extends T> type) {
        return (T[]) Convert.convertArray(fetchArray(fieldIndex), type);
    }

    @Override
    public final Object[] fetchArray(String fieldName) {
        return fetch(fieldName).toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] fetchArray(String fieldName, Class<? extends T> type) {
        return (T[]) Convert.convertArray(fetchArray(fieldName), type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] fetchArray(Field<T> field) {
        return fetch(field).toArray((T[]) Array.newInstance(field.getType(), 0));
    }

    @Override
    public final Object[] fetchOneArray() {
        return fetchOne().intoArray();
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) {
        return fetch().into(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) throws DataAccessException {
        return fetch().into(table);
    }

    @Override
    public final <H extends RecordHandler<R>> H fetchInto(H handler) {
        return fetch().into(handler);
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
