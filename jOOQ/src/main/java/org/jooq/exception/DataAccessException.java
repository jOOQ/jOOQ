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
package org.jooq.exception;

import java.sql.SQLException;

import io.r2dbc.spi.R2dbcException;

/**
 * The <code>DataAccessException</code> is a generic {@link RuntimeException}
 * indicating that something went wrong while executing a SQL statement from
 * jOOQ. The idea behind this unchecked exception is borrowed from Spring's
 * JDBC's DataAccessException
 *
 * @author Sergey Epik - Merged into jOOQ from Spring JDBC Support
 * @author Lukas Eder
 */
public class DataAccessException extends RuntimeException {

    /**
     * Constructor for DataAccessException.
     *
     * @param message the detail message
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructor for DataAccessException.
     *
     * @param message the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Retrieve the {@link SQLException#getSQLState()} or
     * {@link R2dbcException#getSqlState()} from {@link #getCause()}, if this
     * <code>DataAccessException</code> was caused by a {@link SQLException} or
     * {@link R2dbcException}.
     */
    public String sqlState() {
        SQLException s = getCause(SQLException.class);
        if (s != null)
            return s.getSQLState();

        R2dbcException r = getCause(R2dbcException.class);
        if (r != null)
            return r.getSqlState();

        return "00000";
    }

    /**
     * Decode the {@link SQLException#getSQLState()} or
     * {@link R2dbcException#getSqlState()} from {@link #getCause()} into
     * {@link SQLStateClass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException} or {@link R2dbcException}.
     */
    public SQLStateClass sqlStateClass() {
        SQLException s = getCause(SQLException.class);
        if (s != null)
            if (s.getSQLState() != null)
                return SQLStateClass.fromCode(s.getSQLState());
            else if (s.getSQLState() == null && "org.sqlite.SQLiteException".equals(s.getClass().getName()))
                return SQLStateClass.fromSQLiteVendorCode(s.getErrorCode());

        R2dbcException r = getCause(R2dbcException.class);
        if (r != null)
            if (r.getSqlState() != null)
                return SQLStateClass.fromCode(r.getSqlState());

        return SQLStateClass.NONE;
    }

    /**
     * Decode the {@link SQLException#getSQLState()} or
     * {@link R2dbcException#getSqlState()} from {@link #getCause()} into
     * {@link SQLStateSubclass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException} or {@link R2dbcException}.
     */
    public SQLStateSubclass sqlStateSubclass() {
        return SQLStateSubclass.fromCode(sqlState());
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    /**
     * Find a root cause of a given type, or <code>null</code> if no root cause
     * of that type was found.
     */
    public <T extends Throwable> T getCause(Class<? extends T> type) {
        return ExceptionTools.getCause(this, type);
    }
}
