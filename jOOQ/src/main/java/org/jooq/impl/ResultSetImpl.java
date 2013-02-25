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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
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
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.tools.jdbc.JDBC41ResultSet;
import org.jooq.tools.unsigned.UNumber;

/**
 * @author Lukas Eder
 */
class ResultSetImpl extends JDBC41ResultSet implements ResultSet, Serializable {

    /**
     * Generated UID
     */
    private static final long        serialVersionUID = -2292216936424437750L;

    private Result<? extends Record> result;
    private transient int            index;
    private transient boolean        wasNull;

    ResultSetImpl(Result<? extends Record> result) {
        this.result = result;
    }

    // -------------------------------------------------------------------------
    // XXX: Unsupported implementations
    // -------------------------------------------------------------------------

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public final boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    // -------------------------------------------------------------------------
    // XXX: ResultSet operations
    // -------------------------------------------------------------------------

    private final void checkNotClosed() throws SQLException {
        if (result == null) {
            throw new SQLException("ResultSet is already closed");
        }
    }

    private final void checkInRange() throws SQLException {
        checkNotClosed();

        if (index <= 0 || index > result.size()) {
            throw new SQLException("ResultSet index is at an illegal position : " + index);
        }
    }

    private final void checkField(String columnLabel) throws SQLException {
        if (result.getField(columnLabel) == null) {
            throw new SQLException("Unknown column label : " + columnLabel);
        }
    }

    private final void checkField(int columnIndex) throws SQLException {
        if (result.getField(columnIndex - 1) == null) {
            throw new SQLException("Unknown column index : " + columnIndex);
        }
    }

    @Override
    public final boolean next() throws SQLException {
        return relative(1);
    }

    @Override
    public final boolean previous() throws SQLException {
        return relative(-1);
    }

    @Override
    public final boolean absolute(int row) throws SQLException {
        if (result.size() > 0) {
            if (row > 0) {
                if (row <= result.size()) {
                    index = row;
                    return true;
                }
                else {
                    afterLast();
                    return false;
                }
            }
            else if (row == 0) {
                beforeFirst();
                return false;
            }
            else {
                if (-row <= result.size()) {
                    index = result.size() + 1 + row;
                    return true;
                }
                else {
                    beforeFirst();
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    @Override
    public final boolean relative(int rows) throws SQLException {
        checkNotClosed();

        index += rows;
        try {
            return (index > 0 && index <= result.size());
        }

        // Be sure we don't go out of bounds
        finally {
            index = Math.max(index, 0);
            index = Math.min(index, result.size() + 1);
        }
    }

    @Override
    public final int getRow() throws SQLException {
        return (index > result.size()) ? 0 : index;
    }

    @Override
    public final void beforeFirst() throws SQLException {
        checkNotClosed();
        index = 0;
    }

    @Override
    public final void afterLast() throws SQLException {
        checkNotClosed();
        index = result.size() + 1;
    }

    @Override
    public final boolean first() throws SQLException {
        return absolute(1);
    }

    @Override
    public final boolean last() throws SQLException {
        checkNotClosed();
        return absolute(result.size());
    }

    @Override
    public final boolean isFirst() throws SQLException {
        checkNotClosed();

        return (result.size() > 0 && index == 1);
    }

    @Override
    public final boolean isBeforeFirst() throws SQLException {
        checkNotClosed();

        return (result.size() > 0 && index == 0);
    }

    @Override
    public final boolean isLast() throws SQLException {
        checkNotClosed();

        return (result.size() > 0 && index == result.size());
    }

    @Override
    public final boolean isAfterLast() throws SQLException {
        checkNotClosed();

        return (result.size() > 0 && index > result.size());
    }

    @Override
    public final void close() throws SQLException {
        checkNotClosed();

        result = null;
        index = 0;
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return result == null;
    }

    @Override
    public final SQLWarning getWarnings() throws SQLException {
        // Warnings are not supported
        return null;
    }

    @Override
    public final void clearWarnings() throws SQLException {
        // Warnings are not supported
    }

    @Override
    public final String getCursorName() throws SQLException {
        throw new SQLFeatureNotSupportedException("jOOQ ResultSets don't have a cursor name");
    }

    @Override
    public final int findColumn(String columnLabel) throws SQLException {
        checkNotClosed();

        Field<?> field = result.getField(columnLabel);
        if (field == null) {
            throw new SQLException("No such column : " + columnLabel);
        }

        return result.getFields().indexOf(field) + 1;
    }

    @Override
    public final void setFetchDirection(int direction) throws SQLException {

        // Fetch direction is not supported
        if (direction != ResultSet.FETCH_FORWARD) {
            throw new SQLException("Fetch direction can only be FETCH_FORWARD");
        }
    }

    @Override
    public final int getFetchDirection() throws SQLException {

        // Fetch direction is not supported
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public final void setFetchSize(int rows) throws SQLException {
        // Fetch size is not supported
    }

    @Override
    public final int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public final int getType() throws SQLException {
        return ResultSet.TYPE_SCROLL_INSENSITIVE;
    }

    @Override
    public final int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public final int getHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    // -------------------------------------------------------------------------
    // XXX: Getters
    // -------------------------------------------------------------------------

    @Override
    public final boolean wasNull() throws SQLException {
        checkNotClosed();

        return wasNull;
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        return new MetaData();
    }

    @Override
    public final Statement getStatement() throws SQLException {
        return null;
    }

    private final <T> T getValue(String columnLabel, Class<T> type) throws SQLException {
        checkInRange();
        checkField(columnLabel);

        T value = result.get(index - 1).getValue(columnLabel, type);
        wasNull = (value == null);
        return value;
    }

    private final <T> T getValue(int columnIndex, Class<T> type) throws SQLException {
        checkInRange();
        checkField(columnIndex);

        T value = result.get(index - 1).getValue(columnIndex - 1, type);
        wasNull = (value == null);
        return value;
    }

    @Override
    public final String getString(int columnIndex) throws SQLException {
        return getValue(columnIndex, String.class);
    }

    @Override
    public final String getString(String columnLabel) throws SQLException {
        return getValue(columnLabel, String.class);
    }

    @Override
    public final String getNString(int columnIndex) throws SQLException {
        return getString(columnIndex);
    }

    @Override
    public final String getNString(String columnLabel) throws SQLException {
        return getString(columnLabel);
    }

    @Override
    public final boolean getBoolean(int columnIndex) throws SQLException {
        Boolean value = getValue(columnIndex, Boolean.class);
        return wasNull ? false : value;
    }

    @Override
    public final boolean getBoolean(String columnLabel) throws SQLException {
        Boolean value = getValue(columnLabel, Boolean.class);
        return wasNull ? false : value;
    }

    @Override
    public final byte getByte(int columnIndex) throws SQLException {
        Byte value = getValue(columnIndex, Byte.class);
        return wasNull ? (byte) 0 : value;
    }

    @Override
    public final byte getByte(String columnLabel) throws SQLException {
        Byte value = getValue(columnLabel, Byte.class);
        return wasNull ? (byte) 0 : value;
    }

    @Override
    public final short getShort(int columnIndex) throws SQLException {
        Short value = getValue(columnIndex, Short.class);
        return wasNull ? (short) 0 : value;
    }

    @Override
    public final short getShort(String columnLabel) throws SQLException {
        Short value = getValue(columnLabel, Short.class);
        return wasNull ? (short) 0 : value;
    }

    @Override
    public final int getInt(int columnIndex) throws SQLException {
        Integer value = getValue(columnIndex, Integer.class);
        return wasNull ? 0 : value;
    }

    @Override
    public final int getInt(String columnLabel) throws SQLException {
        Integer value = getValue(columnLabel, Integer.class);
        return wasNull ? 0 : value;
    }

    @Override
    public final long getLong(int columnIndex) throws SQLException {
        Long value = getValue(columnIndex, Long.class);
        return wasNull ? 0L : value;
    }

    @Override
    public final long getLong(String columnLabel) throws SQLException {
        Long value = getValue(columnLabel, Long.class);
        return wasNull ? 0L : value;
    }

    @Override
    public final float getFloat(int columnIndex) throws SQLException {
        Float value = getValue(columnIndex, Float.class);
        return wasNull ? 0.0f : value;
    }

    @Override
    public final float getFloat(String columnLabel) throws SQLException {
        Float value = getValue(columnLabel, Float.class);
        return wasNull ? 0.0f : value;
    }

    @Override
    public final double getDouble(int columnIndex) throws SQLException {
        Double value = getValue(columnIndex, Double.class);
        return wasNull ? 0.0 : value;
    }

    @Override
    public final double getDouble(String columnLabel) throws SQLException {
        Double value = getValue(columnLabel, Double.class);
        return wasNull ? 0.0 : value;
    }

    @Override
    public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getValue(columnIndex, BigDecimal.class);
    }

    @Override
    @Deprecated
    public final BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return getValue(columnIndex, BigDecimal.class);
    }

    @Override
    public final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getValue(columnLabel, BigDecimal.class);
    }

    @Override
    @Deprecated
    public final BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return getValue(columnLabel, BigDecimal.class);
    }

    @Override
    public final byte[] getBytes(int columnIndex) throws SQLException {
        return getValue(columnIndex, byte[].class);
    }

    @Override
    public final byte[] getBytes(String columnLabel) throws SQLException {
        return getValue(columnLabel, byte[].class);
    }

    @Override
    public final Date getDate(int columnIndex) throws SQLException {
        return getValue(columnIndex, Date.class);
    }

    @Override
    public final Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getValue(columnIndex, Date.class);
    }

    @Override
    public final Date getDate(String columnLabel) throws SQLException {
        return getValue(columnLabel, Date.class);
    }

    @Override
    public final Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return getValue(columnLabel, Date.class);
    }

    @Override
    public final Time getTime(int columnIndex) throws SQLException {
        return getValue(columnIndex, Time.class);
    }

    @Override
    public final Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getValue(columnIndex, Time.class);
    }

    @Override
    public final Time getTime(String columnLabel) throws SQLException {
        return getValue(columnLabel, Time.class);
    }

    @Override
    public final Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return getValue(columnLabel, Time.class);
    }

    @Override
    public final Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getValue(columnIndex, Timestamp.class);
    }

    @Override
    public final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getValue(columnIndex, Timestamp.class);
    }

    @Override
    public final Timestamp getTimestamp(String columnLabel) throws SQLException {
        return getValue(columnLabel, Timestamp.class);
    }

    @Override
    public final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return getValue(columnLabel, Timestamp.class);
    }

    @Override
    public final InputStream getAsciiStream(int columnIndex) throws SQLException {
        byte[] bytes = getBytes(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public final InputStream getAsciiStream(String columnLabel) throws SQLException {
        byte[] bytes = getBytes(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    @Deprecated
    public final InputStream getUnicodeStream(int columnIndex) throws SQLException {
        String string = getString(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(string.getBytes());
    }

    @Override
    @Deprecated
    public final InputStream getUnicodeStream(String columnLabel) throws SQLException {
        String string = getString(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(string.getBytes());
    }

    @Override
    public final Reader getCharacterStream(int columnIndex) throws SQLException {
        String string = getString(columnIndex);
        return wasNull ? null : new StringReader(string);
    }

    @Override
    public final Reader getCharacterStream(String columnLabel) throws SQLException {
        String string = getString(columnLabel);
        return wasNull ? null : new StringReader(string);
    }

    @Override
    public final Reader getNCharacterStream(int columnIndex) throws SQLException {
        return getCharacterStream(columnIndex);
    }

    @Override
    public final Reader getNCharacterStream(String columnLabel) throws SQLException {
        return getCharacterStream(columnLabel);
    }

    @Override
    public final InputStream getBinaryStream(int columnIndex) throws SQLException {
        byte[] bytes = getBytes(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public final InputStream getBinaryStream(String columnLabel) throws SQLException {
        byte[] bytes = getBytes(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public final Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final Ref getRef(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public final Blob getBlob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getBytes() instead");
    }

    @Override
    public final Blob getBlob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getBytes() instead");
    }

    @Override
    public final Clob getClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public final Clob getClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public final NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public final NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public final Array getArray(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getObject() instead");
    }

    @Override
    public final Array getArray(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getObject() instead");
    }

    @Override
    public final URL getURL(int columnIndex) throws SQLException {
        return getValue(columnIndex, URL.class);
    }

    @Override
    public final URL getURL(String columnLabel) throws SQLException {
        return getValue(columnLabel, URL.class);
    }

    @Override
    public final Object getObject(int columnIndex) throws SQLException {
        return getValue(columnIndex, Object.class);
    }

    @Override
    public final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return getValue(columnIndex, Object.class);
    }

    @Override
    public final Object getObject(String columnLabel) throws SQLException {
        return getValue(columnLabel, Object.class);
    }

    @Override
    public final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getValue(columnLabel, Object.class);
    }

    // -------------------------------------------------------------------------
    // XXX: Setters and row update methods
    // -------------------------------------------------------------------------

    @Override
    public final boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public final boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public final boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public final void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBlob(int columnIndex, InputStream inputStream) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateBlob(String columnLabel, InputStream inputStream) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateClob(int columnIndex, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateClob(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNClob(int columnIndex, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public final void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public final void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    private class MetaData implements ResultSetMetaData, Serializable {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -6859273409631070434L;

        @Override
        public final <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public final boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public final int getColumnCount() throws SQLException {
            checkNotClosed();

            return result.getFields().size();
        }

        @Override
        public final boolean isAutoIncrement(int column) throws SQLException {
            checkNotClosed();

            return false;
        }

        @Override
        public final boolean isCaseSensitive(int column) throws SQLException {
            checkNotClosed();

            return true;
        }

        @Override
        public final boolean isSearchable(int column) throws SQLException {
            checkNotClosed();

            return true;
        }

        @Override
        public final boolean isCurrency(int column) throws SQLException {
            checkNotClosed();

            return false;
        }

        @Override
        public final int isNullable(int column) throws SQLException {
            checkNotClosed();

            // TODO: Check generated JSR-303 or JPA annotations for nullability
            return ResultSetMetaData.columnNullableUnknown;
        }

        @Override
        public final boolean isSigned(int column) throws SQLException {
            checkNotClosed();

            Field<?> field = result.getField(column - 1);
            Class<?> type = field.getType();

            return Number.class.isAssignableFrom(type) && !UNumber.class.isAssignableFrom(type);
        }

        @Override
        public final int getColumnDisplaySize(int column) throws SQLException {
            return 0;
        }

        @Override
        public final String getColumnLabel(int column) throws SQLException {
            return getColumnName(column);
        }

        @Override
        public final String getColumnName(int column) throws SQLException {
            checkNotClosed();

            return result.getField(column - 1).getName();
        }

        @Override
        public final String getSchemaName(int column) throws SQLException {
            checkNotClosed();

            Field<?> field = result.getField(column - 1);
            if (field instanceof TableField) {
                Table<?> table = ((TableField<?, ?>) field).getTable();

                if (table != null) {
                    Schema schema = table.getSchema();

                    if (schema != null) {
                        Configuration configuration = ((AttachableInternal) result).getConfiguration();
                        Schema mapped = null;

                        if (configuration != null) {
                            mapped = Utils.getMappedSchema(configuration, schema);
                        }

                        if (mapped != null) {
                            return mapped.getName();
                        }
                        else {
                            return schema.getName();
                        }
                    }
                }
            }

            // By default, no schema is available
            return "";
        }

        @Override
        public final int getPrecision(int column) throws SQLException {
            checkNotClosed();

            // TODO: Check generated JSR-303 or JPA annotations for precision
            return 0;
        }

        @Override
        public final int getScale(int column) throws SQLException {
            checkNotClosed();

            // TODO: Check generated JSR-303 or JPA annotations for scale
            return 0;
        }

        @Override
        public final String getTableName(int column) throws SQLException {
            checkNotClosed();

            Field<?> field = result.getField(column - 1);
            if (field instanceof TableField) {
                Table<?> table = ((TableField<?, ?>) field).getTable();

                if (table != null) {
                    return table.getName();
                }
            }

            // By default, no table is available
            return "";
        }

        @Override
        public final String getCatalogName(int column) throws SQLException {
            checkNotClosed();

            // jOOQ doesn't support catalogs yet
            return "";
        }

        @Override
        public final int getColumnType(int column) throws SQLException {
            checkNotClosed();

            return result.getField(column - 1).getDataType().getSQLType();
        }

        @Override
        public final String getColumnTypeName(int column) throws SQLException {
            checkNotClosed();

            return result.getField(column - 1).getDataType().getTypeName();
        }

        @Override
        public final boolean isReadOnly(int column) throws SQLException {
            checkNotClosed();

            return true;
        }

        @Override
        public final boolean isWritable(int column) throws SQLException {
            checkNotClosed();

            return false;
        }

        @Override
        public final boolean isDefinitelyWritable(int column) throws SQLException {
            checkNotClosed();

            return false;
        }

        @Override
        public final String getColumnClassName(int column) throws SQLException {
            checkNotClosed();

            return result.getField(column - 1).getType().getName();
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return result == null ? "null" : result.toString();
    }
}
