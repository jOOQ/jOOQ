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
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;

/**
 * A {@link DataSource}-enabled statement.
 *
 * @author Lukas Eder
 * @see DataSourceConnection
 */
class DataSourcePreparedStatement extends DataSourceStatement implements PreparedStatement {

    DataSourcePreparedStatement(DataSourceConnection connection, PreparedStatement statement) {
        super(connection, statement);
    }

    /**
     * Subclasses may override this method
     */
    @Override
    PreparedStatement getDelegate() {
        return (PreparedStatement) super.getDelegate();
    }

    // ------------------------------------------------------------------------
    // XXX Executing the statement
    // ------------------------------------------------------------------------

    @Override
    public final boolean execute() throws SQLException {
        return getDelegate().execute();
    }

    @Override
    public final ResultSet executeQuery() throws SQLException {
        return getDelegate().executeQuery();
    }

    @Override
    public final int executeUpdate() throws SQLException {
        return getDelegate().executeUpdate();
    }

    @Override
    public final void setArray(int parameterIndex, Array x) throws SQLException {
        getDelegate().setArray(parameterIndex, x);
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        getDelegate().setAsciiStream(parameterIndex, x);
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getDelegate().setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        getDelegate().setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public final void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        getDelegate().setBigDecimal(parameterIndex, x);
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        getDelegate().setBinaryStream(parameterIndex, x);
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getDelegate().setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        getDelegate().setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public final void setBlob(int parameterIndex, Blob x) throws SQLException {
        getDelegate().setBlob(parameterIndex, x);
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        getDelegate().setBlob(parameterIndex, inputStream);
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        getDelegate().setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public final void setBoolean(int parameterIndex, boolean x) throws SQLException {
        getDelegate().setBoolean(parameterIndex, x);
    }

    @Override
    public final void setByte(int parameterIndex, byte x) throws SQLException {
        getDelegate().setByte(parameterIndex, x);
    }

    @Override
    public final void setBytes(int parameterIndex, byte[] x) throws SQLException {
        getDelegate().setBytes(parameterIndex, x);
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        getDelegate().setCharacterStream(parameterIndex, reader);
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        getDelegate().setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        getDelegate().setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public final void setClob(int parameterIndex, Clob x) throws SQLException {
        getDelegate().setClob(parameterIndex, x);
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader) throws SQLException {
        getDelegate().setClob(parameterIndex, reader);
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        getDelegate().setClob(parameterIndex, reader, length);
    }

    @Override
    public final void setDate(int parameterIndex, Date x) throws SQLException {
        getDelegate().setDate(parameterIndex, x);
    }

    @Override
    public final void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        getDelegate().setDate(parameterIndex, x, cal);
    }

    @Override
    public final void setDouble(int parameterIndex, double x) throws SQLException {
        getDelegate().setDouble(parameterIndex, x);
    }

    @Override
    public final void setFloat(int parameterIndex, float x) throws SQLException {
        getDelegate().setFloat(parameterIndex, x);
    }

    @Override
    public final void setInt(int parameterIndex, int x) throws SQLException {
        getDelegate().setInt(parameterIndex, x);
    }

    @Override
    public final void setLong(int parameterIndex, long x) throws SQLException {
        getDelegate().setLong(parameterIndex, x);
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        getDelegate().setNCharacterStream(parameterIndex, value);
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        getDelegate().setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public final void setNClob(int parameterIndex, NClob value) throws SQLException {
        getDelegate().setNClob(parameterIndex, value);
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader) throws SQLException {
        getDelegate().setNClob(parameterIndex, reader);
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        getDelegate().setNClob(parameterIndex, reader, length);
    }

    @Override
    public final void setNString(int parameterIndex, String value) throws SQLException {
        getDelegate().setNString(parameterIndex, value);
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType) throws SQLException {
        getDelegate().setNull(parameterIndex, sqlType);
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        getDelegate().setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public final void setObject(int parameterIndex, Object x) throws SQLException {
        getDelegate().setObject(parameterIndex, x);
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        getDelegate().setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        getDelegate().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public final void setRef(int parameterIndex, Ref x) throws SQLException {
        getDelegate().setRef(parameterIndex, x);
    }

    @Override
    public final void setRowId(int parameterIndex, RowId x) throws SQLException {
        getDelegate().setRowId(parameterIndex, x);
    }

    @Override
    public final void setShort(int parameterIndex, short x) throws SQLException {
        getDelegate().setShort(parameterIndex, x);
    }

    @Override
    public final void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        getDelegate().setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public final void setString(int parameterIndex, String x) throws SQLException {
        getDelegate().setString(parameterIndex, x);
    }

    @Override
    public final void setTime(int parameterIndex, Time x) throws SQLException {
        getDelegate().setTime(parameterIndex, x);
    }

    @Override
    public final void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        getDelegate().setTime(parameterIndex, x, cal);
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        getDelegate().setTimestamp(parameterIndex, x);
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        getDelegate().setTimestamp(parameterIndex, x, cal);
    }

    @Override
    @Deprecated
    public final void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getDelegate().setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public final void setURL(int parameterIndex, URL x) throws SQLException {
        getDelegate().setURL(parameterIndex, x);
    }

    @Override
    public final void clearParameters() throws SQLException {
        getDelegate().clearParameters();
    }

    // ------------------------------------------------------------------------
    // XXX Other methods
    // ------------------------------------------------------------------------

    @Override
    public final void addBatch() throws SQLException {
        getDelegate().addBatch();
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        return getDelegate().getMetaData();
    }

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        return getDelegate().getParameterMetaData();
    }
}
