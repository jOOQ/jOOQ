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

import static org.jooq.conf.SettingsTools.executePreparedStatements;

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
import java.util.concurrent.Executor;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;

/**
 * A proxy for a JDBC {@link Connection} that handles creation of prepared
 * statements according to the settings' {@link StatementType}
 *
 * @author Lukas Eder
 */
class ConnectionProxy implements Connection {

    private final Connection delegate;
    private final Settings settings;

    ConnectionProxy(Connection delegate, Settings settings) {
        this.delegate = delegate;
        this.settings = settings;
    }

    final Connection getDelegate() {
        return delegate;
    }

    // ------------------------------------------------------------------------
    // XXX Creation of PreparedStatements
    // ------------------------------------------------------------------------

    @Override
    public final PreparedStatement prepareStatement(String sql) throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql);
        }
        else {
            return new PreparedStatementProxy(delegate, sql);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        else {
            return new PreparedStatementProxy(delegate, sql, resultSetType, resultSetConcurrency);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        else {
            return new PreparedStatementProxy(delegate, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql, autoGeneratedKeys);
        }
        else {
            return new PreparedStatementProxy(delegate, sql, autoGeneratedKeys);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql, columnIndexes);
        }
        else {
            return new PreparedStatementProxy(delegate, sql, columnIndexes);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (executePreparedStatements(settings)) {
            return delegate.prepareStatement(sql, columnNames);
        }
        else {
            return new PreparedStatementProxy(delegate, sql, columnNames);
        }
    }

    // ------------------------------------------------------------------------
    // XXX Other methods
    // ------------------------------------------------------------------------

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public final boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public final Statement createStatement() throws SQLException {
        return delegate.createStatement();
    }

    @Override
    public final CallableStatement prepareCall(String sql) throws SQLException {
        return delegate.prepareCall(sql);
    }

    @Override
    public final String nativeSQL(String sql) throws SQLException {
        return delegate.nativeSQL(sql);
    }

    @Override
    public final void setAutoCommit(boolean autoCommit) throws SQLException {
        delegate.setAutoCommit(autoCommit);
    }

    @Override
    public final boolean getAutoCommit() throws SQLException {
        return delegate.getAutoCommit();
    }

    @Override
    public final void commit() throws SQLException {
        delegate.commit();
    }

    @Override
    public final void rollback() throws SQLException {
        delegate.rollback();
    }

    @Override
    public final void close() throws SQLException {
        delegate.close();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public final DatabaseMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public final void setReadOnly(boolean readOnly) throws SQLException {
        delegate.setReadOnly(readOnly);
    }

    @Override
    public final boolean isReadOnly() throws SQLException {
        return delegate.isReadOnly();
    }

    @Override
    public final void setCatalog(String catalog) throws SQLException {
        delegate.setCatalog(catalog);
    }

    @Override
    public final String getCatalog() throws SQLException {
        return delegate.getCatalog();
    }

    @Override
    public final void setTransactionIsolation(int level) throws SQLException {
        delegate.setTransactionIsolation(level);
    }

    @Override
    public final int getTransactionIsolation() throws SQLException {
        return delegate.getTransactionIsolation();
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
    public final Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public final Map<String, Class<?>> getTypeMap() throws SQLException {
        return delegate.getTypeMap();
    }

    @Override
    public final void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        delegate.setTypeMap(map);
    }

    @Override
    public final void setHoldability(int holdability) throws SQLException {
        delegate.setHoldability(holdability);
    }

    @Override
    public final int getHoldability() throws SQLException {
        return delegate.getHoldability();
    }

    @Override
    public final Savepoint setSavepoint() throws SQLException {
        return delegate.setSavepoint();
    }

    @Override
    public final Savepoint setSavepoint(String name) throws SQLException {
        return delegate.setSavepoint(name);
    }

    @Override
    public final void rollback(Savepoint savepoint) throws SQLException {
        delegate.rollback(savepoint);
    }

    @Override
    public final void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public final Clob createClob() throws SQLException {
        return delegate.createClob();
    }

    @Override
    public final Blob createBlob() throws SQLException {
        return delegate.createBlob();
    }

    @Override
    public final NClob createNClob() throws SQLException {
        return delegate.createNClob();
    }

    @Override
    public final SQLXML createSQLXML() throws SQLException {
        return delegate.createSQLXML();
    }

    @Override
    public final boolean isValid(int timeout) throws SQLException {
        return delegate.isValid(timeout);
    }

    @Override
    public final void setClientInfo(String name, String value) throws SQLClientInfoException {
        delegate.setClientInfo(name, value);
    }

    @Override
    public final void setClientInfo(Properties properties) throws SQLClientInfoException {
        delegate.setClientInfo(properties);
    }

    @Override
    public final String getClientInfo(String name) throws SQLException {
        return delegate.getClientInfo(name);
    }

    @Override
    public final Properties getClientInfo() throws SQLException {
        return delegate.getClientInfo();
    }

    @Override
    public final Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return delegate.createArrayOf(typeName, elements);
    }

    @Override
    public final Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return delegate.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
