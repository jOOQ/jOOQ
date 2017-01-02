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
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

/**
 * A mock connection.
 * <p>
 * Mock connections can be used to supply jOOQ with unit test data, avoiding the
 * round-trip of using an actual in-memory test database, such as Derby, H2 or
 * HSQLDB. A usage example:
 * <p>
 * <code><pre>
 * MockDataProvider provider = new MockDataProvider() {
 *     public MockResult[] execute(MockExecuteContext context) throws SQLException {
 *         Result&lt;MyTableRecord> result = executor.newResult(MY_TABLE);
 *         result.add(executor.newRecord(MY_TABLE));
 *
 *         return new MockResult[] {
 *             new MockResult(1, result)
 *         };
 *     }
 * };
 * Connection connection = new MockConnection(provider);
 * DSLContext create = DSL.using(connection, dialect);
 * assertEquals(1, create.selectOne().fetch().size());
 * </pre></code>
 * <p>
 * While this <code>MockConnection</code> can be used independently of jOOQ, it
 * has been optimised for usage with jOOQ. JDBC features that are not used by
 * jOOQ (e.g. procedure bind value access by parameter name) are not supported
 * in this mock framework
 *
 * @author Lukas Eder
 */
public class MockConnection extends JDBC41Connection implements Connection {

    private final MockDataProvider data;
    private boolean                isClosed;

    public MockConnection(MockDataProvider data) {
        this.data = data;
    }

    // -------------------------------------------------------------------------
    // XXX: Utilities
    // -------------------------------------------------------------------------

    private void checkNotClosed() throws SQLException {
        if (isClosed) {
            throw new SQLException("Connection is already closed");
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Creating statements
    // -------------------------------------------------------------------------

    @Override
    public Statement createStatement() throws SQLException {
        return createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return createStatement(resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data);
        result.resultSetType = resultSetType;
        result.resultSetConcurrency = resultSetConcurrency;
        result.resultSetHoldability = resultSetHoldability;
        return result;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareStatement(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data, sql);
        result.resultSetType = resultSetType;
        result.resultSetConcurrency = resultSetConcurrency;
        result.resultSetHoldability = resultSetHoldability;
        return result;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data, sql);
        result.autoGeneratedKeys = autoGeneratedKeys;
        return result;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data, sql);
        result.autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;
        result.columnIndexes = columnIndexes;
        return result;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data, sql);
        result.autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;
        result.columnNames = columnNames;
        return result;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareCall(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        checkNotClosed();

        MockStatement result = new MockStatement(this, data, sql);
        result.resultSetType = resultSetType;
        result.resultSetConcurrency = resultSetConcurrency;
        result.resultSetHoldability = resultSetHoldability;
        return result;
    }

    // -------------------------------------------------------------------------
    // XXX: Ignored operations
    // -------------------------------------------------------------------------

    @Override
    public void commit() throws SQLException {
        checkNotClosed();
    }

    @Override
    public void rollback() throws SQLException {
        checkNotClosed();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        checkNotClosed();
    }

    @Override
    public void close() throws SQLException {
        isClosed = true;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        checkNotClosed();
        return new MockSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        checkNotClosed();
        return new MockSavepoint(name);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkNotClosed();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return isClosed;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkNotClosed();
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        checkNotClosed();
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        checkNotClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        checkNotClosed();
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        checkNotClosed();}

    @Override
    public String getCatalog() throws SQLException {
        checkNotClosed();
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkNotClosed();
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkNotClosed();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        checkNotClosed();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        checkNotClosed();
        return 0;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkNotClosed();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkNotClosed();
        return null;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        checkNotClosed();
    }

    @Override
    public int getHoldability() throws SQLException {
        checkNotClosed();
        return 0;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        checkNotClosed();
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        checkNotClosed();
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        checkNotClosed();
        return null;
    }

    // -------------------------------------------------------------------------
    // XXX: Unsupported operations
    // -------------------------------------------------------------------------

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported Operation");
    }
}
