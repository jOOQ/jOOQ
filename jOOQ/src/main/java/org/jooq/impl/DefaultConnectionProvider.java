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
package org.jooq.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A default implementation for {@link ConnectionProvider}.
 * <p>
 * This implementation just wraps a JDBC {@link Connection} and provides jOOQ
 * with the same connection for every query. jOOQ will not call any
 * transaction-related methods on the supplied connection. Instead, jOOQ
 * provides you with convenient access to those methods, wrapping any checked
 * {@link SQLException} into an unchecked {@link DataAccessException}
 *
 * @author Aaron Digulla
 * @author Lukas Eder
 */
public class DefaultConnectionProvider implements ConnectionProvider {

    private static final JooqLogger log = JooqLogger.getLogger(DefaultConnectionProvider.class);
    Connection                      connection;
    final boolean                   finalize;

    public DefaultConnectionProvider(Connection connection) {
        this(connection, false);
    }

    DefaultConnectionProvider(Connection connection, boolean finalize) {
        this.connection = connection;
        this.finalize = finalize;
    }

    // -------------------------------------------------------------------------
    // XXX: ConnectionProvider API
    // -------------------------------------------------------------------------

    @Override
    public final Connection acquire() {
        return connection;
    }

    @Override
    public final void release(Connection released) {}

    @Override
    protected void finalize() throws Throwable {
        if (finalize)
            JDBCUtils.safeClose(connection);

        super.finalize();
    }

    // -------------------------------------------------------------------------
    // XXX: Original DSLContext/Factory API (JDBC utility methods)
    // -------------------------------------------------------------------------

    public final void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Convenience method to access {@link Connection#commit()}.
     */
    public final void commit() throws DataAccessException {
        try {
            log.debug("commit");
            connection.commit();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot commit transaction", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#rollback()}.
     */
    public final void rollback() throws DataAccessException {
        try {
            log.debug("rollback");
            connection.rollback();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot rollback transaction", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#rollback(Savepoint)}.
     */
    public final void rollback(Savepoint savepoint) throws DataAccessException {
        try {
            log.debug("rollback to savepoint");
            connection.rollback(savepoint);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot rollback transaction", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#setSavepoint()}.
     */
    public final Savepoint setSavepoint() throws DataAccessException {
        try {
            log.debug("set savepoint");
            return connection.setSavepoint();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set savepoint", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#setSavepoint(String)}.
     */
    public final Savepoint setSavepoint(String name) throws DataAccessException {
        try {
            log.debug("set savepoint", name);
            return connection.setSavepoint(name);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set savepoint", e);
        }
    }

    /**
     * Convenience method to access
     * {@link Connection#releaseSavepoint(Savepoint)}.
     */
    public final void releaseSavepoint(Savepoint savepoint) throws DataAccessException {
        try {
            log.debug("release savepoint");
            connection.releaseSavepoint(savepoint);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot release savepoint", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#setReadOnly(boolean)}.
     */
    public final void setReadOnly(boolean readOnly) throws DataAccessException {
        try {
            log.debug("setting read only", readOnly);
            connection.setReadOnly(readOnly);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set readOnly", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#isReadOnly()}.
     */
    public final boolean isReadOnly() throws DataAccessException {
        try {
            return connection.isReadOnly();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get readOnly", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#setAutoCommit(boolean)}.
     */
    public final void setAutoCommit(boolean autoCommit) throws DataAccessException {
        try {
            log.debug("setting auto commit", autoCommit);
            connection.setAutoCommit(autoCommit);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set autoCommit", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#getAutoCommit()}.
     */
    public final boolean getAutoCommit() throws DataAccessException {
        try {
            return connection.getAutoCommit();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get autoCommit", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#setHoldability(int)}.
     */
    public final void setHoldability(int holdability) throws DataAccessException {
        try {
            log.debug("setting holdability", holdability);
            connection.setHoldability(holdability);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set holdability", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#getHoldability()}.
     */
    public final int getHoldability() throws DataAccessException {
        try {
            return connection.getHoldability();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get holdability", e);
        }
    }

    /**
     * Convenience method to access
     * {@link Connection#setTransactionIsolation(int)}.
     */
    public final void setTransactionIsolation(int level) throws DataAccessException {
        try {
            log.debug("setting tx isolation", level);
            connection.setTransactionIsolation(level);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set transactionIsolation", e);
        }
    }

    /**
     * Convenience method to access {@link Connection#getTransactionIsolation()}.
     */
    public final int getTransactionIsolation() throws DataAccessException {
        try {
            return connection.getTransactionIsolation();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get transactionIsolation", e);
        }
    }
}
