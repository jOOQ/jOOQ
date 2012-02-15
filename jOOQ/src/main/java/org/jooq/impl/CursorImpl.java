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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.tools.JooqLogger;

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

    private transient CursorResultSet   rs;
    private transient Iterator<R>       iterator;

    @SuppressWarnings("unchecked")
    CursorImpl(Configuration configuration, FieldProvider fields, ResultSet rs) {
        this(configuration, fields, rs, null, (Class<? extends R>) RecordImpl.class);
    }

    CursorImpl(Configuration configuration, FieldProvider fields, ResultSet rs, Statement stmt, Class<? extends R> type) {
        this.configuration = configuration;
        this.fields = fields;
        this.type = type;
        this.rs = new CursorResultSet(stmt, rs);
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
    public final boolean hasNext() {
        return iterator().hasNext();
    }

    @Override
    public final Result<R> fetch() {
        return fetch(Integer.MAX_VALUE);
    }

    @Override
    public final R fetchOne() {
        return iterator().next();
    }

    @Override
    public final Result<R> fetch(int number) {
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
    public final <H extends RecordHandler<R>> H fetchOneInto(H handler) {
        handler.next(fetchOne());
        return handler;
    }

    @Override
    public final <H extends RecordHandler<R>> H fetchInto(H handler) {
        while (hasNext()) {
            fetchOneInto(handler);
        }

        return handler;
    }

    @Override
    public final <E> E fetchOneInto(Class<? extends E> clazz) {
        return fetchOne().into(clazz);
    }

    @Override
    public final <E> List<E> fetchInto(Class<? extends E> clazz) {
        return fetch().into(clazz);
    }

    @Override
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        return fetchOne().into(table);
    }

    @Override
    public final <Z extends Record> List<Z> fetchInto(Table<Z> table) {
        return fetch().into(table);
    }

    @Override
    public final void close() {
        Util.safeClose(rs);
        rs = null;
        isClosed = true;
    }

    @Override
    public final boolean isClosed() {
        return isClosed;
    }

    @Override
    public final ResultSet resultSet() {
        return rs;
    }

    /**
     * A wrapper for the underlying JDBC {@link ResultSet} and {@link Statement}
     */
    private final class CursorResultSet implements ResultSet {

        private Statement stmt;
        private ResultSet delegate;

        CursorResultSet(Statement stmt, ResultSet delegate) {
            this.stmt = stmt;
            this.delegate = delegate;
        }

        @Override
        public final <T> T unwrap(Class<T> iface) throws SQLException {
            return delegate.unwrap(iface);
        }

        @Override
        public final boolean isWrapperFor(Class<?> iface) throws SQLException {
            return delegate.isWrapperFor(iface);
        }

        @Override
        public final boolean next() throws SQLException {
            return delegate.next();
        }

        @Override
        public final void close() throws SQLException {
            delegate.close();
            stmt.close();
        }

        @Override
        public final boolean wasNull() throws SQLException {
            return delegate.wasNull();
        }

        @Override
        public final String getString(int columnIndex) throws SQLException {
            return delegate.getString(columnIndex);
        }

        @Override
        public final boolean getBoolean(int columnIndex) throws SQLException {
            return delegate.getBoolean(columnIndex);
        }

        @Override
        public final byte getByte(int columnIndex) throws SQLException {
            return delegate.getByte(columnIndex);
        }

        @Override
        public final short getShort(int columnIndex) throws SQLException {
            return delegate.getShort(columnIndex);
        }

        @Override
        public final int getInt(int columnIndex) throws SQLException {
            return delegate.getInt(columnIndex);
        }

        @Override
        public final long getLong(int columnIndex) throws SQLException {
            return delegate.getLong(columnIndex);
        }

        @Override
        public final float getFloat(int columnIndex) throws SQLException {
            return delegate.getFloat(columnIndex);
        }

        @Override
        public final double getDouble(int columnIndex) throws SQLException {
            return delegate.getDouble(columnIndex);
        }

        @Override
        @Deprecated
        public final BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
            return delegate.getBigDecimal(columnIndex, scale);
        }

        @Override
        public final byte[] getBytes(int columnIndex) throws SQLException {
            return delegate.getBytes(columnIndex);
        }

        @Override
        public final Date getDate(int columnIndex) throws SQLException {
            return delegate.getDate(columnIndex);
        }

        @Override
        public final Time getTime(int columnIndex) throws SQLException {
            return delegate.getTime(columnIndex);
        }

        @Override
        public final Timestamp getTimestamp(int columnIndex) throws SQLException {
            return delegate.getTimestamp(columnIndex);
        }

        @Override
        public final InputStream getAsciiStream(int columnIndex) throws SQLException {
            return delegate.getAsciiStream(columnIndex);
        }

        @Override
        @Deprecated
        public final InputStream getUnicodeStream(int columnIndex) throws SQLException {
            return delegate.getUnicodeStream(columnIndex);
        }

        @Override
        public final InputStream getBinaryStream(int columnIndex) throws SQLException {
            return delegate.getBinaryStream(columnIndex);
        }

        @Override
        public final String getString(String columnLabel) throws SQLException {
            return delegate.getString(columnLabel);
        }

        @Override
        public final boolean getBoolean(String columnLabel) throws SQLException {
            return delegate.getBoolean(columnLabel);
        }

        @Override
        public final byte getByte(String columnLabel) throws SQLException {
            return delegate.getByte(columnLabel);
        }

        @Override
        public final short getShort(String columnLabel) throws SQLException {
            return delegate.getShort(columnLabel);
        }

        @Override
        public final int getInt(String columnLabel) throws SQLException {
            return delegate.getInt(columnLabel);
        }

        @Override
        public final long getLong(String columnLabel) throws SQLException {
            return delegate.getLong(columnLabel);
        }

        @Override
        public final float getFloat(String columnLabel) throws SQLException {
            return delegate.getFloat(columnLabel);
        }

        @Override
        public final double getDouble(String columnLabel) throws SQLException {
            return delegate.getDouble(columnLabel);
        }

        @Override
        @Deprecated
        public final BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
            return delegate.getBigDecimal(columnLabel, scale);
        }

        @Override
        public final byte[] getBytes(String columnLabel) throws SQLException {
            return delegate.getBytes(columnLabel);
        }

        @Override
        public final Date getDate(String columnLabel) throws SQLException {
            return delegate.getDate(columnLabel);
        }

        @Override
        public final Time getTime(String columnLabel) throws SQLException {
            return delegate.getTime(columnLabel);
        }

        @Override
        public final Timestamp getTimestamp(String columnLabel) throws SQLException {
            return delegate.getTimestamp(columnLabel);
        }

        @Override
        public final InputStream getAsciiStream(String columnLabel) throws SQLException {
            return delegate.getAsciiStream(columnLabel);
        }

        @Override
        @Deprecated
        public final InputStream getUnicodeStream(String columnLabel) throws SQLException {
            return delegate.getUnicodeStream(columnLabel);
        }

        @Override
        public final InputStream getBinaryStream(String columnLabel) throws SQLException {
            return delegate.getBinaryStream(columnLabel);
        }

        @Override
        public final SQLWarning getWarnings() throws SQLException {
            return delegate.getWarnings();
        }

        @Override
        public final void clearWarnings() throws SQLException {
            delegate.clearWarnings();
        }

        @Override
        public final String getCursorName() throws SQLException {
            return delegate.getCursorName();
        }

        @Override
        public final ResultSetMetaData getMetaData() throws SQLException {
            return delegate.getMetaData();
        }

        @Override
        public final Object getObject(int columnIndex) throws SQLException {
            return delegate.getObject(columnIndex);
        }

        @Override
        public final Object getObject(String columnLabel) throws SQLException {
            return delegate.getObject(columnLabel);
        }

        @Override
        public final int findColumn(String columnLabel) throws SQLException {
            return delegate.findColumn(columnLabel);
        }

        @Override
        public final Reader getCharacterStream(int columnIndex) throws SQLException {
            return delegate.getCharacterStream(columnIndex);
        }

        @Override
        public final Reader getCharacterStream(String columnLabel) throws SQLException {
            return delegate.getCharacterStream(columnLabel);
        }

        @Override
        public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            return delegate.getBigDecimal(columnIndex);
        }

        @Override
        public final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
            return delegate.getBigDecimal(columnLabel);
        }

        @Override
        public final boolean isBeforeFirst() throws SQLException {
            return delegate.isBeforeFirst();
        }

        @Override
        public final boolean isAfterLast() throws SQLException {
            return delegate.isAfterLast();
        }

        @Override
        public final boolean isFirst() throws SQLException {
            return delegate.isFirst();
        }

        @Override
        public final boolean isLast() throws SQLException {
            return delegate.isLast();
        }

        @Override
        public final void beforeFirst() throws SQLException {
            delegate.beforeFirst();
        }

        @Override
        public final void afterLast() throws SQLException {
            delegate.afterLast();
        }

        @Override
        public final boolean first() throws SQLException {
            return delegate.first();
        }

        @Override
        public final boolean last() throws SQLException {
            return delegate.last();
        }

        @Override
        public final int getRow() throws SQLException {
            return delegate.getRow();
        }

        @Override
        public final boolean absolute(int row) throws SQLException {
            return delegate.absolute(row);
        }

        @Override
        public final boolean relative(int rows) throws SQLException {
            return delegate.relative(rows);
        }

        @Override
        public final boolean previous() throws SQLException {
            return delegate.previous();
        }

        @Override
        public final void setFetchDirection(int direction) throws SQLException {
            delegate.setFetchDirection(direction);
        }

        @Override
        public final int getFetchDirection() throws SQLException {
            return delegate.getFetchDirection();
        }

        @Override
        public final void setFetchSize(int rows) throws SQLException {
            delegate.setFetchSize(rows);
        }

        @Override
        public final int getFetchSize() throws SQLException {
            return delegate.getFetchSize();
        }

        @Override
        public final int getType() throws SQLException {
            return delegate.getType();
        }

        @Override
        public final int getConcurrency() throws SQLException {
            return delegate.getConcurrency();
        }

        @Override
        public final boolean rowUpdated() throws SQLException {
            return delegate.rowUpdated();
        }

        @Override
        public final boolean rowInserted() throws SQLException {
            return delegate.rowInserted();
        }

        @Override
        public final boolean rowDeleted() throws SQLException {
            return delegate.rowDeleted();
        }

        @Override
        public final void updateNull(int columnIndex) throws SQLException {
            delegate.updateNull(columnIndex);
        }

        @Override
        public final void updateBoolean(int columnIndex, boolean x) throws SQLException {
            delegate.updateBoolean(columnIndex, x);
        }

        @Override
        public final void updateByte(int columnIndex, byte x) throws SQLException {
            delegate.updateByte(columnIndex, x);
        }

        @Override
        public final void updateShort(int columnIndex, short x) throws SQLException {
            delegate.updateShort(columnIndex, x);
        }

        @Override
        public final void updateInt(int columnIndex, int x) throws SQLException {
            delegate.updateInt(columnIndex, x);
        }

        @Override
        public final void updateLong(int columnIndex, long x) throws SQLException {
            delegate.updateLong(columnIndex, x);
        }

        @Override
        public final void updateFloat(int columnIndex, float x) throws SQLException {
            delegate.updateFloat(columnIndex, x);
        }

        @Override
        public final void updateDouble(int columnIndex, double x) throws SQLException {
            delegate.updateDouble(columnIndex, x);
        }

        @Override
        public final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            delegate.updateBigDecimal(columnIndex, x);
        }

        @Override
        public final void updateString(int columnIndex, String x) throws SQLException {
            delegate.updateString(columnIndex, x);
        }

        @Override
        public final void updateBytes(int columnIndex, byte[] x) throws SQLException {
            delegate.updateBytes(columnIndex, x);
        }

        @Override
        public final void updateDate(int columnIndex, Date x) throws SQLException {
            delegate.updateDate(columnIndex, x);
        }

        @Override
        public final void updateTime(int columnIndex, Time x) throws SQLException {
            delegate.updateTime(columnIndex, x);
        }

        @Override
        public final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            delegate.updateTimestamp(columnIndex, x);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            delegate.updateAsciiStream(columnIndex, x, length);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            delegate.updateBinaryStream(columnIndex, x, length);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            delegate.updateCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
            delegate.updateObject(columnIndex, x, scaleOrLength);
        }

        @Override
        public final void updateObject(int columnIndex, Object x) throws SQLException {
            delegate.updateObject(columnIndex, x);
        }

        @Override
        public final void updateNull(String columnLabel) throws SQLException {
            delegate.updateNull(columnLabel);
        }

        @Override
        public final void updateBoolean(String columnLabel, boolean x) throws SQLException {
            delegate.updateBoolean(columnLabel, x);
        }

        @Override
        public final void updateByte(String columnLabel, byte x) throws SQLException {
            delegate.updateByte(columnLabel, x);
        }

        @Override
        public final void updateShort(String columnLabel, short x) throws SQLException {
            delegate.updateShort(columnLabel, x);
        }

        @Override
        public final void updateInt(String columnLabel, int x) throws SQLException {
            delegate.updateInt(columnLabel, x);
        }

        @Override
        public final void updateLong(String columnLabel, long x) throws SQLException {
            delegate.updateLong(columnLabel, x);
        }

        @Override
        public final void updateFloat(String columnLabel, float x) throws SQLException {
            delegate.updateFloat(columnLabel, x);
        }

        @Override
        public final void updateDouble(String columnLabel, double x) throws SQLException {
            delegate.updateDouble(columnLabel, x);
        }

        @Override
        public final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
            delegate.updateBigDecimal(columnLabel, x);
        }

        @Override
        public final void updateString(String columnLabel, String x) throws SQLException {
            delegate.updateString(columnLabel, x);
        }

        @Override
        public final void updateBytes(String columnLabel, byte[] x) throws SQLException {
            delegate.updateBytes(columnLabel, x);
        }

        @Override
        public final void updateDate(String columnLabel, Date x) throws SQLException {
            delegate.updateDate(columnLabel, x);
        }

        @Override
        public final void updateTime(String columnLabel, Time x) throws SQLException {
            delegate.updateTime(columnLabel, x);
        }

        @Override
        public final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
            delegate.updateTimestamp(columnLabel, x);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
            delegate.updateAsciiStream(columnLabel, x, length);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
            delegate.updateBinaryStream(columnLabel, x, length);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
            delegate.updateCharacterStream(columnLabel, reader, length);
        }

        @Override
        public final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
            delegate.updateObject(columnLabel, x, scaleOrLength);
        }

        @Override
        public final void updateObject(String columnLabel, Object x) throws SQLException {
            delegate.updateObject(columnLabel, x);
        }

        @Override
        public final void insertRow() throws SQLException {
            delegate.insertRow();
        }

        @Override
        public final void updateRow() throws SQLException {
            delegate.updateRow();
        }

        @Override
        public final void deleteRow() throws SQLException {
            delegate.deleteRow();
        }

        @Override
        public final void refreshRow() throws SQLException {
            delegate.refreshRow();
        }

        @Override
        public final void cancelRowUpdates() throws SQLException {
            delegate.cancelRowUpdates();
        }

        @Override
        public final void moveToInsertRow() throws SQLException {
            delegate.moveToInsertRow();
        }

        @Override
        public final void moveToCurrentRow() throws SQLException {
            delegate.moveToCurrentRow();
        }

        @Override
        public final Statement getStatement() throws SQLException {
            return delegate.getStatement();
        }

        @Override
        public final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
            return delegate.getObject(columnIndex, map);
        }

        @Override
        public final Ref getRef(int columnIndex) throws SQLException {
            return delegate.getRef(columnIndex);
        }

        @Override
        public final Blob getBlob(int columnIndex) throws SQLException {
            return delegate.getBlob(columnIndex);
        }

        @Override
        public final Clob getClob(int columnIndex) throws SQLException {
            return delegate.getClob(columnIndex);
        }

        @Override
        public final Array getArray(int columnIndex) throws SQLException {
            return delegate.getArray(columnIndex);
        }

        @Override
        public final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
            return delegate.getObject(columnLabel, map);
        }

        @Override
        public final Ref getRef(String columnLabel) throws SQLException {
            return delegate.getRef(columnLabel);
        }

        @Override
        public final Blob getBlob(String columnLabel) throws SQLException {
            return delegate.getBlob(columnLabel);
        }

        @Override
        public final Clob getClob(String columnLabel) throws SQLException {
            return delegate.getClob(columnLabel);
        }

        @Override
        public final Array getArray(String columnLabel) throws SQLException {
            return delegate.getArray(columnLabel);
        }

        @Override
        public final Date getDate(int columnIndex, Calendar cal) throws SQLException {
            return delegate.getDate(columnIndex, cal);
        }

        @Override
        public final Date getDate(String columnLabel, Calendar cal) throws SQLException {
            return delegate.getDate(columnLabel, cal);
        }

        @Override
        public final Time getTime(int columnIndex, Calendar cal) throws SQLException {
            return delegate.getTime(columnIndex, cal);
        }

        @Override
        public final Time getTime(String columnLabel, Calendar cal) throws SQLException {
            return delegate.getTime(columnLabel, cal);
        }

        @Override
        public final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            return delegate.getTimestamp(columnIndex, cal);
        }

        @Override
        public final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
            return delegate.getTimestamp(columnLabel, cal);
        }

        @Override
        public final URL getURL(int columnIndex) throws SQLException {
            return delegate.getURL(columnIndex);
        }

        @Override
        public final URL getURL(String columnLabel) throws SQLException {
            return delegate.getURL(columnLabel);
        }

        @Override
        public final void updateRef(int columnIndex, Ref x) throws SQLException {
            delegate.updateRef(columnIndex, x);
        }

        @Override
        public final void updateRef(String columnLabel, Ref x) throws SQLException {
            delegate.updateRef(columnLabel, x);
        }

        @Override
        public final void updateBlob(int columnIndex, Blob x) throws SQLException {
            delegate.updateBlob(columnIndex, x);
        }

        @Override
        public final void updateBlob(String columnLabel, Blob x) throws SQLException {
            delegate.updateBlob(columnLabel, x);
        }

        @Override
        public final void updateClob(int columnIndex, Clob x) throws SQLException {
            delegate.updateClob(columnIndex, x);
        }

        @Override
        public final void updateClob(String columnLabel, Clob x) throws SQLException {
            delegate.updateClob(columnLabel, x);
        }

        @Override
        public final void updateArray(int columnIndex, Array x) throws SQLException {
            delegate.updateArray(columnIndex, x);
        }

        @Override
        public final void updateArray(String columnLabel, Array x) throws SQLException {
            delegate.updateArray(columnLabel, x);
        }

        @Override
        public final RowId getRowId(int columnIndex) throws SQLException {
            return delegate.getRowId(columnIndex);
        }

        @Override
        public final RowId getRowId(String columnLabel) throws SQLException {
            return delegate.getRowId(columnLabel);
        }

        @Override
        public final void updateRowId(int columnIndex, RowId x) throws SQLException {
            delegate.updateRowId(columnIndex, x);
        }

        @Override
        public final void updateRowId(String columnLabel, RowId x) throws SQLException {
            delegate.updateRowId(columnLabel, x);
        }

        @Override
        public final int getHoldability() throws SQLException {
            return delegate.getHoldability();
        }

        @Override
        public final boolean isClosed() throws SQLException {
            return delegate.isClosed();
        }

        @Override
        public final void updateNString(int columnIndex, String nString) throws SQLException {
            delegate.updateNString(columnIndex, nString);
        }

        @Override
        public final void updateNString(String columnLabel, String nString) throws SQLException {
            delegate.updateNString(columnLabel, nString);
        }

        @Override
        public final void updateNClob(int columnIndex, NClob nClob) throws SQLException {
            delegate.updateNClob(columnIndex, nClob);
        }

        @Override
        public final void updateNClob(String columnLabel, NClob nClob) throws SQLException {
            delegate.updateNClob(columnLabel, nClob);
        }

        @Override
        public final NClob getNClob(int columnIndex) throws SQLException {
            return delegate.getNClob(columnIndex);
        }

        @Override
        public final NClob getNClob(String columnLabel) throws SQLException {
            return delegate.getNClob(columnLabel);
        }

        @Override
        public final SQLXML getSQLXML(int columnIndex) throws SQLException {
            return delegate.getSQLXML(columnIndex);
        }

        @Override
        public final SQLXML getSQLXML(String columnLabel) throws SQLException {
            return delegate.getSQLXML(columnLabel);
        }

        @Override
        public final void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
            delegate.updateSQLXML(columnIndex, xmlObject);
        }

        @Override
        public final void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
            delegate.updateSQLXML(columnLabel, xmlObject);
        }

        @Override
        public final String getNString(int columnIndex) throws SQLException {
            return delegate.getNString(columnIndex);
        }

        @Override
        public final String getNString(String columnLabel) throws SQLException {
            return delegate.getNString(columnLabel);
        }

        @Override
        public final Reader getNCharacterStream(int columnIndex) throws SQLException {
            return delegate.getNCharacterStream(columnIndex);
        }

        @Override
        public final Reader getNCharacterStream(String columnLabel) throws SQLException {
            return delegate.getNCharacterStream(columnLabel);
        }

        @Override
        public final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            delegate.updateNCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            delegate.updateNCharacterStream(columnLabel, reader, length);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
            delegate.updateAsciiStream(columnIndex, x, length);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
            delegate.updateBinaryStream(columnIndex, x, length);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            delegate.updateCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
            delegate.updateAsciiStream(columnLabel, x, length);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
            delegate.updateBinaryStream(columnLabel, x, length);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            delegate.updateCharacterStream(columnLabel, reader, length);
        }

        @Override
        public final void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
            delegate.updateBlob(columnIndex, inputStream, length);
        }

        @Override
        public final void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
            delegate.updateBlob(columnLabel, inputStream, length);
        }

        @Override
        public final void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
            delegate.updateClob(columnIndex, reader, length);
        }

        @Override
        public final void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
            delegate.updateClob(columnLabel, reader, length);
        }

        @Override
        public final void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
            delegate.updateNClob(columnIndex, reader, length);
        }

        @Override
        public final void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
            delegate.updateNClob(columnLabel, reader, length);
        }

        @Override
        public final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
            delegate.updateNCharacterStream(columnIndex, x);
        }

        @Override
        public final void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
            delegate.updateNCharacterStream(columnLabel, reader);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
            delegate.updateAsciiStream(columnIndex, x);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
            delegate.updateBinaryStream(columnIndex, x);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
            delegate.updateCharacterStream(columnIndex, x);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
            delegate.updateAsciiStream(columnLabel, x);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
            delegate.updateBinaryStream(columnLabel, x);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
            delegate.updateCharacterStream(columnLabel, reader);
        }

        @Override
        public final void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
            delegate.updateBlob(columnIndex, inputStream);
        }

        @Override
        public final void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
            delegate.updateBlob(columnLabel, inputStream);
        }

        @Override
        public final void updateClob(int columnIndex, Reader reader) throws SQLException {
            delegate.updateClob(columnIndex, reader);
        }

        @Override
        public final void updateClob(String columnLabel, Reader reader) throws SQLException {
            delegate.updateClob(columnLabel, reader);
        }

        @Override
        public final void updateNClob(int columnIndex, Reader reader) throws SQLException {
            delegate.updateNClob(columnIndex, reader);
        }

        @Override
        public final void updateNClob(String columnLabel, Reader reader) throws SQLException {
            delegate.updateNClob(columnLabel, reader);
        }
    }

    /**
     * An iterator for records fetched by this cursor
     */
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
                next = fetch();
                hasNext = (next != null);
            }

            return hasNext;
        }

        @Override
        public final R next() {
            if (hasNext == null) {
                return fetch();
            }

            R result = next;
            hasNext = null;
            next = null;
            return result;
        }

        private final R fetch() {
            R record = null;

            try {
                if (!isClosed && rs.next()) {
                    record = Util.newRecord(type, fields, configuration);
                    final List<Field<?>> fieldList = fields.getFields();
                    final int size = fieldList.size();

                    for (int i = 0; i < size; i++) {
                        setValue((AbstractRecord) record, fieldList.get(i), i, rs);
                    }

                    if (log.isTraceEnabled()) {
                        log.trace("Fetching record", record);
                    }
                }
            }
            catch (SQLException e) {
                throw Util.translate("Cursor.fetch", null, e);
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
