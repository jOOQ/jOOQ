/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.tools.jdbc;

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

/**
 * A default JDBC Connection implementation delegating all JDBC 4.0 calls to an
 * internal delegate.
 *
 * @author Lukas Eder
 */
public class DefaultConnection extends JDBC41Connection implements Connection {

    private final Connection delegate;

    public DefaultConnection(Connection delegate) {
        this.delegate = delegate;
    }

    public Connection getDelegate() {
        return delegate;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getDelegate().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getDelegate().isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return getDelegate().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getDelegate().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return getDelegate().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getDelegate().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        getDelegate().setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return getDelegate().getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        getDelegate().commit();
    }

    @Override
    public void rollback() throws SQLException {
        getDelegate().rollback();
    }

    @Override
    public void close() throws SQLException {
        getDelegate().close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return getDelegate().isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getDelegate().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        getDelegate().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return getDelegate().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        getDelegate().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return getDelegate().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        getDelegate().setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return getDelegate().getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getDelegate().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        getDelegate().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getDelegate().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getDelegate().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getDelegate().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        getDelegate().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getDelegate().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return getDelegate().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return getDelegate().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        getDelegate().rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getDelegate().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return getDelegate().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        return getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return getDelegate().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return getDelegate().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return getDelegate().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return getDelegate().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getDelegate().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getDelegate().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getDelegate().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getDelegate().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        getDelegate().setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        getDelegate().setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getDelegate().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getDelegate().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getDelegate().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getDelegate().createStruct(typeName, attributes);
    }
}
