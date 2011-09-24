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
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jooq.Query;

/**
 * A stub prepared statement that acts as a collector of bind values, in order
 * to retrieve the bound values in correct order for
 * {@link Query#getBindValues()}
 * 
 * @author Lukas Eder
 */
class BindValueCollector implements PreparedStatement {

    final List<Object> result = new ArrayList<Object>();

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return 0;
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {}

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {}

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {}

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {}

    @Override
    public void cancel() throws SQLException {}

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {}

    @Override
    public void setCursorName(String name) throws SQLException {}

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {}

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {}

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {}

    @Override
    public void clearBatch() throws SQLException {}

    @Override
    public int[] executeBatch() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {}

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate() throws SQLException {
        return 0;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        result.add(null);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        result.add(x);
    }

    @Override
    public void clearParameters() throws SQLException {}

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        result.add(x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        result.add(x);
    }

    @Override
    public boolean execute() throws SQLException {
        return false;
    }

    @Override
    public void addBatch() throws SQLException {}

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        result.add(x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        result.add(x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        result.add(x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        result.add(null);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        result.add(x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNString(int parameterIndex, String x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) throws SQLException {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) throws SQLException {
        result.add(x);
    }
}
