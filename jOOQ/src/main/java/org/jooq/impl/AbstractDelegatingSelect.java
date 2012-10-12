/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FutureResult;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;

/**
 * This class serves as a base class for <code>SelectImpl</code> and
 * <code>SimpleSelectImpl</code>, the two classes that implement the
 * <code>SELECT</code> DSL API. It delegates all calls of the {@link Select} API
 * to an underlying {@link Select} (e.g. a non-DSL {@link SelectQuery})
 *
 * @author Lukas Eder
 */
abstract class AbstractDelegatingSelect<R extends Record>
    extends AbstractDelegatingQueryPart<Select<R>>
    implements Select<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3382400928803573548L;

    AbstractDelegatingSelect(Select<R> query) {
        super(query);
    }

    @Override
    public final ResultQuery<R> bind(String param, Object value) {
        return getDelegate().bind(param, value);
    }

    @Override
    public final ResultQuery<R> bind(int index, Object value) {
        return getDelegate().bind(index, value);
    }

    @Override
    public final ResultQuery<R> queryTimeout(int timeout) {
        return getDelegate().queryTimeout(timeout);
    }

    @Override
    public final ResultQuery<R> maxRows(int rows) {
        return getDelegate().maxRows(rows);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return getDelegate().getRecordType();
    }

    @Override
    public final List<Field<?>> getSelect() {
        return getDelegate().getSelect();
    }

    @Override
    public final Result<R> getResult() {
        return getDelegate().getResult();
    }

    @Override
    public final Result<R> fetch() {
        return getDelegate().fetch();
    }

    @Override
    public final ResultSet fetchResultSet() {
        return getDelegate().fetchResultSet();
    }

    @Override
    public final Cursor<R> fetchLazy() {
        return getDelegate().fetchLazy();
    }

    @Override
    public final Cursor<R> fetchLazy(int fetchSize) {
        return getDelegate().fetchLazy(fetchSize);
    }

    @Override
    public final List<Result<Record>> fetchMany() {
        return getDelegate().fetchMany();
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return getDelegate().fetch(field);
    }

    @Override
    public final <T> List<T> fetch(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetch(field, type);
    }

    @Override
    public final <T, U> List<U> fetch(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetch(field, converter);
    }

    @Override
    public final List<?> fetch(int fieldIndex) {
        return getDelegate().fetch(fieldIndex);
    }

    @Override
    public final <T> List<T> fetch(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetch(fieldIndex, type);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetch(fieldIndex, converter);
    }

    @Override
    public final List<?> fetch(String fieldName) {
        return getDelegate().fetch(fieldName);
    }

    @Override
    public final <T> List<T> fetch(String fieldName, Class<? extends T> type) {
        return getDelegate().fetch(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetch(fieldName, converter);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        return getDelegate().fetchOne(field);
    }

    @Override
    public final <T> T fetchOne(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchOne(field, type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchOne(field, converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        return getDelegate().fetchOne(fieldIndex);
    }

    @Override
    public final <T> T fetchOne(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchOne(fieldIndex, type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchOne(fieldIndex, converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        return getDelegate().fetchOne(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchOne(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchOne(fieldName, converter);
    }

    @Override
    public final R fetchOne() {
        return getDelegate().fetchOne();
    }

    @Override
    public final R fetchAny() {
        return getDelegate().fetchAny();
    }

    @Override
    public final <K> Map<K, R> fetchMap(Field<K> key) {
        return getDelegate().fetchMap(key);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return getDelegate().fetchMap(key, value);
    }

    @Override
    public final Map<Record, R> fetchMap(Field<?>[] keys) {
        return getDelegate().fetchMap(keys);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchMap(keys, type);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchMap(key, type);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        return getDelegate().fetchMaps();
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        return getDelegate().fetchOneMap();
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return getDelegate().fetchGroups(key);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return getDelegate().fetchGroups(key, value);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return getDelegate().fetchGroups(keys);
    }

    @Override
    public final <E> Map<List<?>, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchGroups(keys, type);
    }

    @Override
    public final Object[][] fetchArrays() {
        return getDelegate().fetchArrays();
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) {
        return getDelegate().fetchArray(fieldIndex);
    }

    @Override
    public final <T> T[] fetchArray(int fieldIndex, Class<? extends T> type) {
        return getDelegate().fetchArray(fieldIndex, type);
    }

    @Override
    public final <U> U[] fetchArray(int fieldIndex, Converter<?, U> converter) {
        return getDelegate().fetchArray(fieldIndex, converter);
    }

    @Override
    public final Object[] fetchArray(String fieldName) {
        return getDelegate().fetchArray(fieldName);
    }

    @Override
    public final <T> T[] fetchArray(String fieldName, Class<? extends T> type) {
        return getDelegate().fetchArray(fieldName, type);
    }

    @Override
    public final <U> U[] fetchArray(String fieldName, Converter<?, U> converter) {
        return getDelegate().fetchArray(fieldName, converter);
    }

    @Override
    public final <T> T[] fetchArray(Field<T> field) {
        return getDelegate().fetchArray(field);
    }

    @Override
    public final <T> T[] fetchArray(Field<?> field, Class<? extends T> type) {
        return getDelegate().fetchArray(field, type);
    }

    @Override
    public final <T, U> U[] fetchArray(Field<T> field, Converter<? super T, U> converter) {
        return getDelegate().fetchArray(field, converter);
    }

    @Override
    public final Object[] fetchOneArray() {
        return getDelegate().fetchOneArray();
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) {
        return getDelegate().fetchInto(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return getDelegate().fetchInto(table);
    }

    @Override
    public final <H extends RecordHandler<R>> H fetchInto(H handler) {
        return getDelegate().fetchInto(handler);
    }

    @Override
    public final <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetch(mapper);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchGroups(key, type);
    }

    @Override
    public final FutureResult<R> fetchLater() {
        return getDelegate().fetchLater();
    }

    @Override
    public final FutureResult<R> fetchLater(ExecutorService executor) {
        return getDelegate().fetchLater(executor);
    }

    @Override
    public final int execute() {
        return getDelegate().execute();
    }

    @Override
    public final boolean isExecutable() {
        return getDelegate().isExecutable();
    }

    @Override
    public final Table<R> asTable() {
        return getDelegate().asTable();
    }

    @Override
    public final Table<R> asTable(String alias) {
        return getDelegate().asTable(alias);
    }

    @Override
    public final <T> Field<T> asField() {
        return getDelegate().asField();
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return getDelegate().asField(alias);
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return getDelegate().asTable().getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return getDelegate().asTable().getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return getDelegate().asTable().getField(index);
    }

    @Override
    public final List<Field<?>> getFields() {
        return getDelegate().asTable().getFields();
    }

    @Override
    public final int getIndex(Field<?> field) {
        return getDelegate().asTable().getIndex(field);
    }
}
