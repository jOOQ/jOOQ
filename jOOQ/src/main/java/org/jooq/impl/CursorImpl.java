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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.Result;

/**
 * @author Lukas Eder
 */
class CursorImpl<R extends Record> implements Cursor<R> {

    /**
     * Generated UID
     */
    private static final JooqLogger     log              = JooqLogger.getLogger(CursorImpl.class);

    private final FieldProvider         fields;
    private final Configuration         configuration;
    private final Class<? extends R>    type;
    private boolean                     isClosed;

    private transient PreparedStatement stmt;
    private transient ResultSet         rs;
    private transient Iterator<R>       iterator;

    @SuppressWarnings({ "unchecked", "deprecation" })
    CursorImpl(Configuration configuration, FieldProvider fields, ResultSet rs) {
        this(configuration, fields, rs, null, (Class<? extends R>) RecordImpl.class);
    }

    CursorImpl(Configuration configuration, FieldProvider fields, ResultSet rs, PreparedStatement stmt, Class<? extends R> type) {
        this.configuration = configuration;
        this.fields = fields;
        this.type = type;
        this.stmt = stmt;
        this.rs = rs;
    }

    @Override
    public final List<Field<?>> getFields() {
        return fields.getFields();
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return fields.getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return fields.getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return fields.getField(index);
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return fields.getIndex(field);
    }

    @Override
    public final Iterator<R> iterator() {
        if (iterator == null) {
            iterator = new CursorIterator();
        }

        return iterator;
    }

    @Override
    public final boolean hasNext() throws SQLException {
        return iterator().hasNext();
    }

    @Override
    public final Result<R> fetch() throws SQLException {
        return fetch(Integer.MAX_VALUE);
    }

    @Override
    public final R fetchOne() throws SQLException {
        return iterator().next();
    }

    @Override
    public final Result<R> fetch(int number) throws SQLException {
        return fetchResult(number);
    }

    @Override
    public final Result<R> fetchResult() throws SQLException {
        return fetchResult(Integer.MAX_VALUE);
    }

    @Override
    public final Result<R> fetchResult(int number) throws SQLException {
        ResultImpl<R> result = new ResultImpl<R>(configuration, fields);
        R record = null;

        for (int i = 0; i < number && ((record = fetchOne()) != null); i++) {
            result.addRecord(record);
        }

        if (log.isDebugEnabled()) {
            String comment = "Fetched result";

            for (String line : result.format(5).split("\n")) {
                log.debug(comment, line);
                comment = "";
            }
        }

        return result;
    }

    @Override
    public final RecordHandler<R> fetchInto(RecordHandler<R> handler) throws SQLException {
        R record = null;

        while ((record = fetchOne()) != null) {
            handler.next(record);
        }

        return handler;
    }

    @Override
    public final <E> List<E> fetchInto(Class<? extends E> clazz) throws SQLException {
        return fetch().into(clazz);
    }

    @Override
    public final void close() throws SQLException {
        JooqUtil.safeClose(rs, stmt);
        rs = null;
        stmt = null;
        isClosed = true;
    }

    @Override
    public final boolean isClosed() {
        return isClosed;
    }

    private final class CursorIterator implements Iterator<R> {

        /**
         * The (potentially) pre-fetched next record
         */
        private R next;

        /**
         * Whether the underlying {@link ResultSet} has a next record. This boolean has three states:
         * <ul>
         * <li>null: it's not known whether there is a next record</li>
         * <li>true: there is a next record, and it has been pre-fetched</li>
         * <li>false: there aren't any next records</li>
         * </ul>
         */
        private Boolean hasNext;

        @Override
        public final boolean hasNext() {
            if (hasNext == null) {
                try {
                    next = fetch();
                    hasNext = (next != null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return hasNext;
        }

        @Override
        public final R next() {
            if (hasNext == null) {
                try {
                    return fetch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            R result = next;
            hasNext = null;
            next = null;
            return result;
        }

        private final R fetch() throws SQLException {
            R record = null;

            if (!isClosed && rs.next()) {
                record = JooqUtil.newRecord(type, fields, configuration);
                final List<Field<?>> fieldList = fields.getFields();
                final int size = fieldList.size();

                for (int i = 0; i < size; i++) {
                    setValue((AbstractRecord) record, fieldList.get(i), i, rs);
                }

                if (log.isTraceEnabled()) {
                    log.trace("Fetching record", record);
                }
            }

            // Conveniently close cursors and underlying objects after the last
            // Record was fetched
            if (record == null) {
                CursorImpl.this.close();
            }

            return record;
        }

        /**
         * Utility method to prevent unnecessary unchecked conversions
         */
        private final <T> void setValue(AbstractRecord record, Field<T> field, int index, ResultSet rs) throws SQLException {
            T value = FieldTypeHelper.getFromResultSet(configuration, rs, field, index + 1);
            record.setValue(field, new Value<T>(value));
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
