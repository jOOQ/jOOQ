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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FutureResult;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.Table;

abstract class AbstractDelegatingSelect<R extends Record> extends AbstractQueryPart
    implements Select<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3382400928803573548L;

    protected final Select<R> query;

    AbstractDelegatingSelect(Select<R> query) {
        this.query = query;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(query);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return query.getRecordType();
    }

    @Override
    public final List<Field<?>> getSelect() {
        return query.getSelect();
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(query);
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.bind(query);
    }

    @Override
    public final Result<R> getResult() {
        return query.getResult();
    }

    @Override
    public final Result<R> fetch() throws SQLException {
        return query.fetch();
    }

    @Override
    public final Cursor<R> fetchLazy() throws SQLException {
        return query.fetchLazy();
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) throws SQLException {
        return query.fetch(field);
    }

    @Override
    public final List<?> fetch(int fieldIndex) throws SQLException {
        return query.fetch(fieldIndex);
    }

    @Override
    public final <T> List<T> fetch(int fieldIndex, Class<? extends T> type) throws SQLException {
        return query.fetch(fieldIndex, type);
    }

    @Override
    public final List<?> fetch(String fieldName) throws SQLException {
        return query.fetch(fieldName);
    }

    @Override
    public final <T> List<T> fetch(String fieldName, Class<? extends T> type) throws SQLException {
        return query.fetch(fieldName, type);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) throws SQLException {
        return query.fetchOne(field);
    }

    @Override
    public final Object fetchOne(int fieldIndex) throws SQLException {
        return query.fetchOne(fieldIndex);
    }

    @Override
    public final <T> T fetchOne(int fieldIndex, Class<? extends T> type) throws SQLException {
        return query.fetchOne(fieldIndex, type);
    }

    @Override
    public final Object fetchOne(String fieldName) throws SQLException {
        return query.fetchOne(fieldName);
    }

    @Override
    public final <T> T fetchOne(String fieldName, Class<? extends T> type) throws SQLException {
        return query.fetchOne(fieldName, type);
    }

    @Override
    public final R fetchOne() throws SQLException {
        return query.fetchOne();
    }

    @Override
    public final R fetchAny() throws SQLException {
        return query.fetchAny();
    }

    @Override
    public final <K> Map<K, R> fetchMap(Field<K> key) throws SQLException {
        return query.fetchMap(key);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) throws SQLException {
        return query.fetchMap(key, value);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() throws SQLException {
        return query.fetchMaps();
    }

    @Override
    public final Map<String, Object> fetchOneMap() throws SQLException {
        return query.fetchOneMap();
    }

    @Override
    public final Object[][] fetchArrays() throws SQLException {
        return query.fetchArrays();
    }

    @Override
    public final Object[] fetchArray(int fieldIndex) throws SQLException {
        return query.fetchArray(fieldIndex);
    }

    @Override
    public final <T> T[] fetchArray(int fieldIndex, Class<? extends T> type) throws SQLException {
        return query.fetchArray(fieldIndex, type);
    }

    @Override
    public final Object[] fetchArray(String fieldName) throws SQLException {
        return query.fetchArray(fieldName);
    }

    @Override
    public final <T> T[] fetchArray(String fieldName, Class<? extends T> type) throws SQLException {
        return query.fetchArray(fieldName, type);
    }

    @Override
    public final <T> T[] fetchArray(Field<T> field) throws SQLException {
        return query.fetchArray(field);
    }

    @Override
    public final Object[] fetchOneArray() throws SQLException {
        return query.fetchOneArray();
    }

    @Override
    public final <T> List<T> fetchInto(Class<? extends T> type) throws SQLException {
        return query.fetchInto(type);
    }

    @Override
    public final <H extends RecordHandler<R>> H fetchInto(H handler) throws SQLException {
        return query.fetchInto(handler);
    }

    @Override
    public final FutureResult<R> fetchLater() throws SQLException {
        return query.fetchLater();
    }

    @Override
    public final FutureResult<R> fetchLater(ExecutorService executor) throws SQLException {
        return query.fetchLater(executor);
    }

    @Override
    public final int execute() throws SQLException {
        return query.execute();
    }

    @Override
    public final Table<R> asTable() {
        return query.asTable();
    }

    @Override
    public final Table<R> asTable(String alias) {
        return query.asTable(alias);
    }

    @Override
    public final <T> Field<T> asField() {
        return query.asField();
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return query.asField(alias);
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return query.asTable().getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return query.asTable().getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return query.asTable().getField(index);
    }

    @Override
    public final List<Field<?>> getFields() {
        return query.asTable().getFields();
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return query.asTable().getIndex(field);
    }
}
