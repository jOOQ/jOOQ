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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * A proxy for a JDBC {@link PreparedStatement} that simulates the API of a
 * prepared statement, when in fact executing an ad-hoc {@link Statement}
 *
 * @author Lukas Eder
 */
class PreparedStatementProxy implements PreparedStatement {

    private final Connection connection;
    private final Statement  delegate;
    private final MethodType methodType;
    private final String     sql;
    private int              autoGeneratedKeys;
    private int[]            columnIndexes;
    private String[]         columnNames;

    // ------------------------------------------------------------------------
    // XXX: Creation of PreparedStatements
    // ------------------------------------------------------------------------

    private PreparedStatementProxy(Connection connection, String sql, MethodType type, Statement statement) {
        this.connection = connection;
        this.methodType = type;
        this.sql = sql;
        this.delegate = statement;
    }

    private PreparedStatementProxy(Connection connection, String sql, MethodType type) throws SQLException {
        this(connection, sql, type, connection.createStatement());
    }

    PreparedStatementProxy(Connection connection) throws SQLException {
        this(connection, null, MethodType.BATCH);
    }

    PreparedStatementProxy(Connection connection, String sql) throws SQLException {
        this(connection, sql, MethodType.SQL);
    }

    PreparedStatementProxy(Connection connection, String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        this(connection, sql, MethodType.SQL_RST_RSC, connection.createStatement(resultSetType, resultSetConcurrency));
    }

    PreparedStatementProxy(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this(connection, sql, MethodType.SQL_RST_RSC_RSH, connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    PreparedStatementProxy(Connection connection, String sql, int autoGeneratedKeys) throws SQLException {
        this(connection, sql, MethodType.SQL_AGK);

        this.autoGeneratedKeys = autoGeneratedKeys;
    }

    PreparedStatementProxy(Connection connection, String sql, int[] columnIndexes) throws SQLException {
        this(connection, sql, MethodType.SQL_CI);

        this.columnIndexes = columnIndexes;
    }

    PreparedStatementProxy(Connection connection, String sql, String[] columnNames) throws SQLException {
        this(connection, sql, MethodType.SQL_CN);

        this.columnNames = columnNames;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // ------------------------------------------------------------------------
    // XXX: Utilities
    // ------------------------------------------------------------------------

    /**
     * A descriptor for the various methods that can create a prespared
     * statement
     */
    private static enum MethodType {

        /**
         * Corresponds to {@link Connection#prepareStatement(String)}
         */
        SQL,

        /**
         * Corresponds to {@link Connection#prepareStatement(String, int, int)
         */
        SQL_RST_RSC,

        /**
         * Corresponds to
         * {@link Connection#prepareStatement(String, int, int, int)
         */
        SQL_RST_RSC_RSH,

        /**
         * Corresponds to
         * {@link Connection#prepareStatement(String, int)
         */
        SQL_AGK,

        /**
         * Corresponds to
         * {@link Connection#prepareStatement(String, int[])
         */
        SQL_CI,

        /**
         * Corresponds to
         * {@link Connection#prepareStatement(String, String[])
         */
        SQL_CN,

        /**
         * Corresponds to {@link Connection#createStatement()} and
         * {@link Statement#executeBatch()}
         */
        BATCH
    }

    // ------------------------------------------------------------------------
    // XXX: Irrelevant methods from java.sql.Statement
    // ------------------------------------------------------------------------

    @Override
    public final void close() throws SQLException {
        delegate.close();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public final void cancel() throws SQLException {
        delegate.cancel();
    }

    @Override
    public final int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }

    @Override
    public final void setMaxFieldSize(int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }

    @Override
    public final int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }

    @Override
    public final void setMaxRows(int max) throws SQLException {
        delegate.setMaxRows(max);
    }

    @Override
    public final void setEscapeProcessing(boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }

    @Override
    public final int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }

    @Override
    public final void setQueryTimeout(int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
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
    public final void setCursorName(String name) throws SQLException {
        delegate.setCursorName(name);
    }

    @Override
    public final ResultSet getResultSet() throws SQLException {
        return delegate.getResultSet();
    }

    @Override
    public final int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }

    @Override
    public final boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public final void setPoolable(boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }

    @Override
    public final boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
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
    public final int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }

    @Override
    public final int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }

    @Override
    public final int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public final boolean getMoreResults(int current) throws SQLException {
        return delegate.getMoreResults();
    }

    @Override
    public final ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }

    // ------------------------------------------------------------------------
    // XXX: Execute methods from java.sql.PreparedStatement
    // ------------------------------------------------------------------------

    @Override
    public final ResultSet executeQuery() throws SQLException {
        return delegate.executeQuery(sql);
    }

    @Override
    public final int executeUpdate() throws SQLException {
        switch (methodType) {
            case SQL_AGK:
                return delegate.executeUpdate(sql, autoGeneratedKeys);
            case SQL_CI:
                return delegate.executeUpdate(sql, columnIndexes);
            case SQL_CN:
                return delegate.executeUpdate(sql, columnNames);

            case SQL:
            case SQL_RST_RSC:
            case SQL_RST_RSC_RSH:
            default:
                return delegate.executeUpdate(sql);
        }
    }

    @Override
    public final boolean execute() throws SQLException {
        switch (methodType) {
            case SQL_AGK:
                return delegate.execute(sql, autoGeneratedKeys);
            case SQL_CI:
                return delegate.execute(sql, columnIndexes);
            case SQL_CN:
                return delegate.execute(sql, columnNames);

            case SQL:
            case SQL_RST_RSC:
            case SQL_RST_RSC_RSH:
            default:
                return delegate.execute(sql);
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Supported and unsupported batch methods
    // ------------------------------------------------------------------------

    @Override
    public final void addBatch() throws SQLException {
        throw new UnsupportedOperationException("Cannot batch execute statements on PreparedStatementProxy");
    }

    @Override
    public final void clearBatch() throws SQLException {
        delegate.clearBatch();
    }

    @Override
    public final int[] executeBatch() throws SQLException {
        return delegate.executeBatch();
    }

    @Override
    public final void addBatch(String query) throws SQLException {
        delegate.addBatch(query);
    }

    // ------------------------------------------------------------------------
    // XXX: Unsupported execute methods from java.sql.Statement
    // ------------------------------------------------------------------------

    @Override
    public final boolean execute(String query) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final ResultSet executeQuery(String query) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final int executeUpdate(String query) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final int executeUpdate(String query, int pAutoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final int executeUpdate(String query, int[] pColumnIndexes) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final int executeUpdate(String query, String[] pColumnNames) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final boolean execute(String query, int pAutoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final boolean execute(String query, int[] pColumnIndexes) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    @Override
    public final boolean execute(String query, String[] pColumnNames) throws SQLException {
        throw new UnsupportedOperationException("Cannot use java.sql.Statement.executeXXX() methods on PreparedStatementProxy");
    }

    // ------------------------------------------------------------------------
    // XXX: Unsupported bind variable methods from java.sql.PreparedStatement
    // ------------------------------------------------------------------------

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException("Cannot fetch ResultSetMetaData early on PreparedStatementProxy");
    }

    @Override
    public final void clearParameters() throws SQLException {
        throw new UnsupportedOperationException("Cannot operate on bind values on a PreparedStatementProxy");
    }

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        throw new UnsupportedOperationException("Cannot operate on bind values on a PreparedStatementProxy");
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBoolean(int parameterIndex, boolean x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setByte(int parameterIndex, byte x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setShort(int parameterIndex, short x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setInt(int parameterIndex, int x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setLong(int parameterIndex, long x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setFloat(int parameterIndex, float x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setDouble(int parameterIndex, double x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setString(int parameterIndex, String x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBytes(int parameterIndex, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setDate(int parameterIndex, Date x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setTime(int parameterIndex, Time x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setObject(int parameterIndex, Object x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setClob(int parameterIndex, Clob x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setArray(int parameterIndex, Array x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setURL(int parameterIndex, URL x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNString(int parameterIndex, String value) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Cannot set a bind value on a PreparedStatementProxy");
    }
}
