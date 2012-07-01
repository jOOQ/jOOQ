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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.jdbc.JDBC41Connection;

/**
 * A {@link DataSource}-enabled connection.
 * <p>
 * This is a wrapper for both a {@link DataSource} and/or a {@link Connection}.
 * This wrapper abstracts closing a JDBC connection when it is obtained from a
 * data source by closing it when the {@link Statement},
 * {@link PreparedStatement}, or {@link CallableStatement} is closed.
 *
 * @author Lukas Eder
 */
class DataSourceConnection extends JDBC41Connection implements Connection {

    private final DataSource datasource;
    private final Settings   settings;

    private Connection       connection;

    DataSourceConnection(DataSource datasource, Connection connection, Settings settings) {
        this.datasource = datasource;
        this.connection = connection;
        this.settings = settings;
    }

    final Connection getDelegate() {
        if (connection == null) {
            try {
                connection = new ConnectionProxy(datasource.getConnection(), settings);
            }
            catch (SQLException e) {
                throw new DataAccessException("Error when fetching Connection from DataSource", e);
            }
        }

        return connection;
    }

    final DataSource getDataSource() {
        return datasource;
    }

    // ------------------------------------------------------------------------
    // XXX Closing the connection
    // ------------------------------------------------------------------------

    @Override
    public final void close() throws SQLException {
        getDelegate().close();

        if (datasource != null) {
            connection = null;
        }
    }

    // ------------------------------------------------------------------------
    // XXX Creation of Statements
    // ------------------------------------------------------------------------

    @Override
    public final Statement createStatement() throws SQLException {
        return new DataSourceStatement(this, getDelegate().createStatement());
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DataSourceStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency));
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return new DataSourceStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    // ------------------------------------------------------------------------
    // XXX Creation of PreparedStatements
    // ------------------------------------------------------------------------

    @Override
    public final PreparedStatement prepareStatement(String sql) throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql, autoGeneratedKeys));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql, columnIndexes));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new DataSourcePreparedStatement(this, getDelegate().prepareStatement(sql, columnNames));
    }

    // ------------------------------------------------------------------------
    // XXX Creation of CallableStatements
    // ------------------------------------------------------------------------

    @Override
    public final CallableStatement prepareCall(String sql) throws SQLException {
        return new DataSourcePreparedCall(this, getDelegate().prepareCall(sql));
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DataSourcePreparedCall(this, getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency));
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        return new DataSourcePreparedCall(this, getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    // ------------------------------------------------------------------------
    // XXX Other methods
    // ------------------------------------------------------------------------

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        return getDelegate().unwrap(iface);
    }

    @Override
    public final boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getDelegate().isWrapperFor(iface);
    }

    @Override
    public final String nativeSQL(String sql) throws SQLException {
        return getDelegate().nativeSQL(sql);
    }

    @Override
    public final void setAutoCommit(boolean autoCommit) throws SQLException {
        getDelegate().setAutoCommit(autoCommit);
    }

    @Override
    public final boolean getAutoCommit() throws SQLException {
        return getDelegate().getAutoCommit();
    }

    @Override
    public final void commit() throws SQLException {
        getDelegate().commit();
    }

    @Override
    public final void rollback() throws SQLException {
        getDelegate().rollback();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return getDelegate().isClosed();
    }

    @Override
    public final DatabaseMetaData getMetaData() throws SQLException {
        return getDelegate().getMetaData();
    }

    @Override
    public final void setReadOnly(boolean readOnly) throws SQLException {
        getDelegate().setReadOnly(readOnly);
    }

    @Override
    public final boolean isReadOnly() throws SQLException {
        return getDelegate().isReadOnly();
    }

    @Override
    public final void setCatalog(String catalog) throws SQLException {
        getDelegate().setCatalog(catalog);
    }

    @Override
    public final String getCatalog() throws SQLException {
        return getDelegate().getCatalog();
    }

    @Override
    public final void setTransactionIsolation(int level) throws SQLException {
        getDelegate().setTransactionIsolation(level);
    }

    @Override
    public final int getTransactionIsolation() throws SQLException {
        return getDelegate().getTransactionIsolation();
    }

    @Override
    public final SQLWarning getWarnings() throws SQLException {
        return getDelegate().getWarnings();
    }

    @Override
    public final void clearWarnings() throws SQLException {
        getDelegate().clearWarnings();
    }

    @Override
    public final Map<String, Class<?>> getTypeMap() throws SQLException {
        return getDelegate().getTypeMap();
    }

    @Override
    public final void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getDelegate().setTypeMap(map);
    }

    @Override
    public final void setHoldability(int holdability) throws SQLException {
        getDelegate().setHoldability(holdability);
    }

    @Override
    public final int getHoldability() throws SQLException {
        return getDelegate().getHoldability();
    }

    @Override
    public final Savepoint setSavepoint() throws SQLException {
        return getDelegate().setSavepoint();
    }

    @Override
    public final Savepoint setSavepoint(String name) throws SQLException {
        return getDelegate().setSavepoint(name);
    }

    @Override
    public final void rollback(Savepoint savepoint) throws SQLException {
        getDelegate().rollback(savepoint);
    }

    @Override
    public final void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getDelegate().releaseSavepoint(savepoint);
    }

    @Override
    public final Clob createClob() throws SQLException {
        return getDelegate().createClob();
    }

    @Override
    public final Blob createBlob() throws SQLException {
        return getDelegate().createBlob();
    }

    @Override
    public final NClob createNClob() throws SQLException {
        return getDelegate().createNClob();
    }

    @Override
    public final SQLXML createSQLXML() throws SQLException {
        return getDelegate().createSQLXML();
    }

    @Override
    public final boolean isValid(int timeout) throws SQLException {
        return getDelegate().isValid(timeout);
    }

    @Override
    public final void setClientInfo(String name, String value) throws SQLClientInfoException {
        getDelegate().setClientInfo(name, value);
    }

    @Override
    public final void setClientInfo(Properties properties) throws SQLClientInfoException {
        getDelegate().setClientInfo(properties);
    }

    @Override
    public final String getClientInfo(String name) throws SQLException {
        return getDelegate().getClientInfo(name);
    }

    @Override
    public final Properties getClientInfo() throws SQLException {
        return getDelegate().getClientInfo();
    }

    @Override
    public final Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getDelegate().createArrayOf(typeName, elements);
    }

    @Override
    public final Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getDelegate().createStruct(typeName, attributes);
    }
}
