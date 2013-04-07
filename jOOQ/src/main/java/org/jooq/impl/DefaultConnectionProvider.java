/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;

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

    private static final JooqLogger log = JooqLogger.getLogger(DSL.class);
    private Connection              connection;

    public DefaultConnectionProvider(Connection connection) {
        this.connection = connection;
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
