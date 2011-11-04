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
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        return null;
    }

    @Override
    public int executeUpdate(String sql) {
        return 0;
    }

    @Override
    public void close() {}

    @Override
    public int getMaxFieldSize() {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) {}

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public void setMaxRows(int max) {}

    @Override
    public void setEscapeProcessing(boolean enable) {}

    @Override
    public int getQueryTimeout() {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) {}

    @Override
    public void cancel() {}

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {}

    @Override
    public void setCursorName(String name) {}

    @Override
    public boolean execute(String sql) {
        return false;
    }

    @Override
    public ResultSet getResultSet() {
        return null;
    }

    @Override
    public int getUpdateCount() {
        return 0;
    }

    @Override
    public boolean getMoreResults() {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) {}

    @Override
    public int getFetchDirection() {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) {}

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() {
        return 0;
    }

    @Override
    public int getResultSetType() {
        return 0;
    }

    @Override
    public void addBatch(String sql) {}

    @Override
    public void clearBatch() {}

    @Override
    public int[] executeBatch() {
        return null;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) {
        return false;
    }

    @Override
    public int getResultSetHoldability() {
        return 0;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) {}

    @Override
    public boolean isPoolable() {
        return false;
    }

    @Override
    public ResultSet executeQuery() {
        return null;
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        result.add(null);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {
        result.add(x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        result.add(x);
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        result.add(x);
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        result.add(x);
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        result.add(x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        result.add(x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        result.add(x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        result.add(x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        result.add(x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        result.add(x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        result.add(x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        result.add(x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        result.add(x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        result.add(x);
    }

    @Override
    public void clearParameters() {}

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        result.add(x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        result.add(x);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void addBatch() {}

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int length) {
        result.add(x);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) {
        result.add(x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) {
        result.add(x);
    }

    @Override
    public ResultSetMetaData getMetaData() {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        result.add(x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        result.add(x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        result.add(x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {
        result.add(null);
    }

    @Override
    public void setURL(int parameterIndex, URL x) {
        result.add(x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {
        result.add(x);
    }

    @Override
    public void setNString(int parameterIndex, String x) {
        result.add(x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long length) {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long length) {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long length) {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long length) {
        result.add(x);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) {
        result.add(x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {
        result.add(x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long length) {
        result.add(x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {
        result.add(x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {
        result.add(x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) {
        result.add(x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) {
        result.add(x);
    }

    @Override
    public void setClob(int parameterIndex, Reader x) {
        result.add(x);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) {
        result.add(x);
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) {
        result.add(x);
    }
}
