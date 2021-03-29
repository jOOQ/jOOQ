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
 *
 *
 *
 */
package org.jooq.tools.jdbc;

import static java.util.Collections.emptyMap;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * A batching connection.
 * <p>
 * This connection buffers consecutive identical prepared statements for
 * <code>DELETE</code>, <code>INSERT</code>, <code>MERGE</code>,
 * <code>UPDATE</code> statements, instead of allowing for them to be executed
 * directly.
 * <p>
 * Calls to {@link PreparedStatement#executeUpdate()} or
 * {@link PreparedStatement#execute()} are replaced by
 * {@link PreparedStatement#addBatch()}. The update count of such operations is
 * always zero, and cannot be retrieved later on. When any of the following
 * events happen, the batch is executed using {@link Statement#executeBatch()}:
 * <ul>
 * <li>The {@link BatchedConnection#close} method is called (the call is not
 * delegated to the wrapped connection).</li>
 * <li>A {@link Connection#prepareStatement(String)} call is made with a
 * different SQL string.</li>
 * <li>Any other type of statement is created or other API is called, such as
 * {@link #commit()}.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class BatchedConnection extends DefaultConnection {

    final int                batchSize;
    String                   lastSQL;
    BatchedPreparedStatement lastStatement;

    public BatchedConnection(Connection delegate) {
        this(delegate, Integer.MAX_VALUE);
    }

    public BatchedConnection(Connection delegate, int batchSize) {
        super(delegate);

        this.batchSize = batchSize;
    }

    // -------------------------------------------------------------------------
    // XXX: Wrappers
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return BatchedConnection.class == iface ? (T) this : super.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return BatchedConnection.class == iface || super.isWrapperFor(iface);
    }

    // -------------------------------------------------------------------------
    // XXX: Utilities
    // -------------------------------------------------------------------------

    void executeLastBatch(String sql) throws SQLException {
        if (!sql.equals(lastSQL))
            executeLastBatch();
    }

    void executeLastBatch() throws SQLException {
        if (lastStatement != null) {
            if (lastStatement.batches > 0)
                lastStatement.executeBatch();

            safeClose(lastStatement);
        }

        clearLastBatch();
    }

    void clearLastBatch() {
        lastStatement = null;
        lastSQL = null;
    }

    void setBatch(BatchedPreparedStatement s) throws SQLException {
        if (lastStatement == s)
            return;

        if (lastStatement != null)
            executeLastBatch();

        lastStatement = s;
        lastSQL = s.sql;
    }

    // -------------------------------------------------------------------------
    // XXX: Creating non-batchable statements
    // -------------------------------------------------------------------------

    @Override
    public Statement createStatement() throws SQLException {
        executeLastBatch();
        return super.createStatement();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        executeLastBatch();
        return super.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        executeLastBatch();
        return super.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        executeLastBatch();
        return super.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        executeLastBatch();
        return super.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        executeLastBatch();
        return super.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        executeLastBatch();
        return super.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        executeLastBatch();
        return super.prepareStatement(sql, columnNames);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        executeLastBatch();
        return super.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        executeLastBatch();
        return super.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        executeLastBatch();
        return super.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    // -------------------------------------------------------------------------
    // XXX: Creating non-batchable statements
    // -------------------------------------------------------------------------

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        executeLastBatch(sql);
        return lastStatement != null ? lastStatement : prepareStatement0(sql);
    }

    // TODO: Can we implement this in a more sophisticated way without invoking the costly parser?
    static final Pattern P_DML = Pattern.compile("\\s*(?i:delete|insert|merge|update).*");

    private PreparedStatement prepareStatement0(String sql) throws SQLException {
        PreparedStatement result = super.prepareStatement(sql);

        if (P_DML.matcher(sql).matches()) {
            lastSQL = sql;
            return lastStatement = new BatchedPreparedStatement(sql, this, result);
        }
        else
            return result;
    }

    // -------------------------------------------------------------------------
    // XXX: Ignored operations
    // -------------------------------------------------------------------------

    @Override
    public void commit() throws SQLException {
        executeLastBatch();
        super.commit();
    }

    @Override
    public void rollback() throws SQLException {
        executeLastBatch();
        super.rollback();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        executeLastBatch();
        super.rollback(savepoint);
    }

    @Override
    public void close() throws SQLException {
        executeLastBatch();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        executeLastBatch();
        return super.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        executeLastBatch();
        return super.setSavepoint(name);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        executeLastBatch();
        super.releaseSavepoint(savepoint);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        executeLastBatch();
        super.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        executeLastBatch();
        return super.getAutoCommit();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        executeLastBatch();
        super.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        executeLastBatch();
        return super.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        executeLastBatch();
        super.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        executeLastBatch();
        return super.getCatalog();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        executeLastBatch();
        return super.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        executeLastBatch();
        super.clearWarnings();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        executeLastBatch();
        super.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        executeLastBatch();
        return super.getTransactionIsolation();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        executeLastBatch();
        super.setTypeMap(map);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        executeLastBatch();
        return super.getTypeMap();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        executeLastBatch();
        super.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        executeLastBatch();
        return super.getHoldability();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        executeLastBatch();
        return super.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            executeLastBatch();
        }
        catch (SQLException e) {
            throw new SQLClientInfoException(emptyMap(), e);
        }

        super.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            executeLastBatch();
        }
        catch (SQLException e) {
            throw new SQLClientInfoException(emptyMap(), e);
        }

        super.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        executeLastBatch();
        return super.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        executeLastBatch();
        return super.getClientInfo();
    }
}
