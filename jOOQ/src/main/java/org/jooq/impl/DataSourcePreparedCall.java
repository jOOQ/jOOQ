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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import javax.sql.DataSource;

/**
 * A {@link DataSource}-enabled statement.
 *
 * @author Lukas Eder
 * @see DataSourceConnection
 */
class DataSourcePreparedCall extends DataSourcePreparedStatement implements CallableStatement {

    DataSourcePreparedCall(DataSourceConnection connection, CallableStatement statement) {
        super(connection, statement);
    }

    /**
     * Subclasses may override this method
     */
    @Override
    CallableStatement getDelegate() {
        return (CallableStatement) super.getDelegate();
    }

    // ------------------------------------------------------------------------
    // XXX Other methods
    // ------------------------------------------------------------------------

    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        getDelegate().registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        getDelegate().registerOutParameter(parameterIndex, sqlType, scale);
    }

    @Override
    public final boolean wasNull() throws SQLException {
        return getDelegate().wasNull();
    }

    @Override
    public final String getString(int parameterIndex) throws SQLException {
        return getDelegate().getString(parameterIndex);
    }

    @Override
    public final boolean getBoolean(int parameterIndex) throws SQLException {
        return getDelegate().getBoolean(parameterIndex);
    }

    @Override
    public final byte getByte(int parameterIndex) throws SQLException {
        return getDelegate().getByte(parameterIndex);
    }

    @Override
    public final short getShort(int parameterIndex) throws SQLException {
        return getDelegate().getShort(parameterIndex);
    }

    @Override
    public final int getInt(int parameterIndex) throws SQLException {
        return getDelegate().getInt(parameterIndex);
    }

    @Override
    public final long getLong(int parameterIndex) throws SQLException {
        return getDelegate().getLong(parameterIndex);
    }

    @Override
    public final float getFloat(int parameterIndex) throws SQLException {
        return getDelegate().getFloat(parameterIndex);
    }

    @Override
    public final double getDouble(int parameterIndex) throws SQLException {
        return getDelegate().getDouble(parameterIndex);
    }

    @Override
    @Deprecated
    public final BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return getDelegate().getBigDecimal(parameterIndex, scale);
    }

    @Override
    public final byte[] getBytes(int parameterIndex) throws SQLException {
        return getDelegate().getBytes(parameterIndex);
    }

    @Override
    public final Date getDate(int parameterIndex) throws SQLException {
        return getDelegate().getDate(parameterIndex);
    }

    @Override
    public final Time getTime(int parameterIndex) throws SQLException {
        return getDelegate().getTime(parameterIndex);
    }

    @Override
    public final Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return getDelegate().getTimestamp(parameterIndex);
    }

    @Override
    public final Object getObject(int parameterIndex) throws SQLException {
        return getDelegate().getObject(parameterIndex);
    }

    @Override
    public final BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return getDelegate().getBigDecimal(parameterIndex);
    }

    @Override
    public final Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return getDelegate().getObject(parameterIndex, map);
    }

    @Override
    public final Ref getRef(int parameterIndex) throws SQLException {
        return getDelegate().getRef(parameterIndex);
    }

    @Override
    public final Blob getBlob(int parameterIndex) throws SQLException {
        return getDelegate().getBlob(parameterIndex);
    }

    @Override
    public final Clob getClob(int parameterIndex) throws SQLException {
        return getDelegate().getClob(parameterIndex);
    }

    @Override
    public final Array getArray(int parameterIndex) throws SQLException {
        return getDelegate().getArray(parameterIndex);
    }

    @Override
    public final Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegate().getDate(parameterIndex, cal);
    }

    @Override
    public final Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegate().getTime(parameterIndex, cal);
    }

    @Override
    public final Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegate().getTimestamp(parameterIndex, cal);
    }

    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        getDelegate().registerOutParameter(parameterIndex, sqlType, typeName);
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        getDelegate().registerOutParameter(parameterName, sqlType);
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        getDelegate().registerOutParameter(parameterName, sqlType, scale);
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        getDelegate().registerOutParameter(parameterName, sqlType, typeName);
    }

    @Override
    public final URL getURL(int parameterIndex) throws SQLException {
        return getDelegate().getURL(parameterIndex);
    }

    @Override
    public final void setURL(String parameterName, URL val) throws SQLException {
        getDelegate().setURL(parameterName, val);
    }

    @Override
    public final void setNull(String parameterName, int sqlType) throws SQLException {
        getDelegate().setNull(parameterName, sqlType);
    }

    @Override
    public final void setBoolean(String parameterName, boolean x) throws SQLException {
        getDelegate().setBoolean(parameterName, x);
    }

    @Override
    public final void setByte(String parameterName, byte x) throws SQLException {
        getDelegate().setByte(parameterName, x);
    }

    @Override
    public final void setShort(String parameterName, short x) throws SQLException {
        getDelegate().setShort(parameterName, x);
    }

    @Override
    public final void setInt(String parameterName, int x) throws SQLException {
        getDelegate().setInt(parameterName, x);
    }

    @Override
    public final void setLong(String parameterName, long x) throws SQLException {
        getDelegate().setLong(parameterName, x);
    }

    @Override
    public final void setFloat(String parameterName, float x) throws SQLException {
        getDelegate().setFloat(parameterName, x);
    }

    @Override
    public final void setDouble(String parameterName, double x) throws SQLException {
        getDelegate().setDouble(parameterName, x);
    }

    @Override
    public final void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        getDelegate().setBigDecimal(parameterName, x);
    }

    @Override
    public final void setString(String parameterName, String x) throws SQLException {
        getDelegate().setString(parameterName, x);
    }

    @Override
    public final void setBytes(String parameterName, byte[] x) throws SQLException {
        getDelegate().setBytes(parameterName, x);
    }

    @Override
    public final void setDate(String parameterName, Date x) throws SQLException {
        getDelegate().setDate(parameterName, x);
    }

    @Override
    public final void setTime(String parameterName, Time x) throws SQLException {
        getDelegate().setTime(parameterName, x);
    }

    @Override
    public final void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        getDelegate().setTimestamp(parameterName, x);
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        getDelegate().setAsciiStream(parameterName, x, length);
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        getDelegate().setBinaryStream(parameterName, x, length);
    }

    @Override
    public final void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        getDelegate().setObject(parameterName, x, targetSqlType, scale);
    }

    @Override
    public final void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        getDelegate().setObject(parameterName, x, targetSqlType);
    }

    @Override
    public final void setObject(String parameterName, Object x) throws SQLException {
        getDelegate().setObject(parameterName, x);
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        getDelegate().setCharacterStream(parameterName, reader, length);
    }

    @Override
    public final void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        getDelegate().setDate(parameterName, x, cal);
    }

    @Override
    public final void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        getDelegate().setTime(parameterName, x, cal);
    }

    @Override
    public final void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        getDelegate().setTimestamp(parameterName, x, cal);
    }

    @Override
    public final void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        getDelegate().setNull(parameterName, sqlType, typeName);
    }

    @Override
    public final String getString(String parameterName) throws SQLException {
        return getDelegate().getString(parameterName);
    }

    @Override
    public final boolean getBoolean(String parameterName) throws SQLException {
        return getDelegate().getBoolean(parameterName);
    }

    @Override
    public final byte getByte(String parameterName) throws SQLException {
        return getDelegate().getByte(parameterName);
    }

    @Override
    public final short getShort(String parameterName) throws SQLException {
        return getDelegate().getShort(parameterName);
    }

    @Override
    public final int getInt(String parameterName) throws SQLException {
        return getDelegate().getInt(parameterName);
    }

    @Override
    public final long getLong(String parameterName) throws SQLException {
        return getDelegate().getLong(parameterName);
    }

    @Override
    public final float getFloat(String parameterName) throws SQLException {
        return getDelegate().getFloat(parameterName);
    }

    @Override
    public final double getDouble(String parameterName) throws SQLException {
        return getDelegate().getDouble(parameterName);
    }

    @Override
    public final byte[] getBytes(String parameterName) throws SQLException {
        return getDelegate().getBytes(parameterName);
    }

    @Override
    public final Date getDate(String parameterName) throws SQLException {
        return getDelegate().getDate(parameterName);
    }

    @Override
    public final Time getTime(String parameterName) throws SQLException {
        return getDelegate().getTime(parameterName);
    }

    @Override
    public final Timestamp getTimestamp(String parameterName) throws SQLException {
        return getDelegate().getTimestamp(parameterName);
    }

    @Override
    public final Object getObject(String parameterName) throws SQLException {
        return getDelegate().getObject(parameterName);
    }

    @Override
    public final BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return getDelegate().getBigDecimal(parameterName);
    }

    @Override
    public final Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return getDelegate().getObject(parameterName, map);
    }

    @Override
    public final Ref getRef(String parameterName) throws SQLException {
        return getDelegate().getRef(parameterName);
    }

    @Override
    public final Blob getBlob(String parameterName) throws SQLException {
        return getDelegate().getBlob(parameterName);
    }

    @Override
    public final Clob getClob(String parameterName) throws SQLException {
        return getDelegate().getClob(parameterName);
    }

    @Override
    public final Array getArray(String parameterName) throws SQLException {
        return getDelegate().getArray(parameterName);
    }

    @Override
    public final Date getDate(String parameterName, Calendar cal) throws SQLException {
        return getDelegate().getDate(parameterName, cal);
    }

    @Override
    public final Time getTime(String parameterName, Calendar cal) throws SQLException {
        return getDelegate().getTime(parameterName, cal);
    }

    @Override
    public final Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return getDelegate().getTimestamp(parameterName, cal);
    }

    @Override
    public final URL getURL(String parameterName) throws SQLException {
        return getDelegate().getURL(parameterName);
    }

    @Override
    public final RowId getRowId(int parameterIndex) throws SQLException {
        return getDelegate().getRowId(parameterIndex);
    }

    @Override
    public final RowId getRowId(String parameterName) throws SQLException {
        return getDelegate().getRowId(parameterName);
    }

    @Override
    public final void setRowId(String parameterName, RowId x) throws SQLException {
        getDelegate().setRowId(parameterName, x);
    }

    @Override
    public final void setNString(String parameterName, String value) throws SQLException {
        getDelegate().setNString(parameterName, value);
    }

    @Override
    public final void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        getDelegate().setNCharacterStream(parameterName, value, length);
    }

    @Override
    public final void setNClob(String parameterName, NClob value) throws SQLException {
        getDelegate().setNClob(parameterName, value);
    }

    @Override
    public final void setClob(String parameterName, Reader reader, long length) throws SQLException {
        getDelegate().setClob(parameterName, reader, length);
    }

    @Override
    public final void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        getDelegate().setBlob(parameterName, inputStream, length);
    }

    @Override
    public final void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        getDelegate().setNClob(parameterName, reader, length);
    }

    @Override
    public final NClob getNClob(int parameterIndex) throws SQLException {
        return getDelegate().getNClob(parameterIndex);
    }

    @Override
    public final NClob getNClob(String parameterName) throws SQLException {
        return getDelegate().getNClob(parameterName);
    }

    @Override
    public final void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        getDelegate().setSQLXML(parameterName, xmlObject);
    }

    @Override
    public final SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return getDelegate().getSQLXML(parameterIndex);
    }

    @Override
    public final SQLXML getSQLXML(String parameterName) throws SQLException {
        return getDelegate().getSQLXML(parameterName);
    }

    @Override
    public final String getNString(int parameterIndex) throws SQLException {
        return getDelegate().getNString(parameterIndex);
    }

    @Override
    public final String getNString(String parameterName) throws SQLException {
        return getDelegate().getNString(parameterName);
    }

    @Override
    public final Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return getDelegate().getNCharacterStream(parameterIndex);
    }

    @Override
    public final Reader getNCharacterStream(String parameterName) throws SQLException {
        return getDelegate().getNCharacterStream(parameterName);
    }

    @Override
    public final Reader getCharacterStream(int parameterIndex) throws SQLException {
        return getDelegate().getCharacterStream(parameterIndex);
    }

    @Override
    public final Reader getCharacterStream(String parameterName) throws SQLException {
        return getDelegate().getCharacterStream(parameterName);
    }

    @Override
    public final void setBlob(String parameterName, Blob x) throws SQLException {
        getDelegate().setBlob(parameterName, x);
    }

    @Override
    public final void setClob(String parameterName, Clob x) throws SQLException {
        getDelegate().setClob(parameterName, x);
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        getDelegate().setAsciiStream(parameterName, x, length);
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        getDelegate().setBinaryStream(parameterName, x, length);
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        getDelegate().setCharacterStream(parameterName, reader, length);
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        getDelegate().setAsciiStream(parameterName, x);
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        getDelegate().setBinaryStream(parameterName, x);
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        getDelegate().setCharacterStream(parameterName, reader);
    }

    @Override
    public final void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        getDelegate().setNCharacterStream(parameterName, value);
    }

    @Override
    public final void setClob(String parameterName, Reader reader) throws SQLException {
        getDelegate().setClob(parameterName, reader);
    }

    @Override
    public final void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        getDelegate().setBlob(parameterName, inputStream);
    }

    @Override
    public final void setNClob(String parameterName, Reader reader) throws SQLException {
        getDelegate().setNClob(parameterName, reader);
    }
}
