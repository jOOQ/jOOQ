/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.exception;

import java.sql.SQLException;

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
     * Generated UID
     */
    private static final long serialVersionUID = 491834858363345767L;

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
     * Retrieve the {@link SQLException#getSQLState()} from {@link #getCause()},
     * if this <code>DataAccessException</code> was caused by a
     * {@link SQLException}.
     */
    public String sqlState() {
        Throwable t = getCause();
        if (t instanceof SQLException)
            return ((SQLException) t).getSQLState();

        return "00000";
    }

    /**
     * Decode the {@link SQLException#getSQLState()} from {@link #getCause()}
     * into {@link SQLStateClass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException}.
     */
    public SQLStateClass sqlStateClass() {
        Throwable t = getCause();
        if (t instanceof SQLException)
            return SQLStateClass.fromCode(((SQLException) t).getSQLState());

        return SQLStateClass.NONE;
    }

    /**
     * Decode the {@link SQLException#getSQLState()} from {@link #getCause()}
     * into {@link SQLStateSubclass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException}.
     */
    public SQLStateSubclass sqlStateSubclass() {
        Throwable t= getCause();
        if (t instanceof SQLException)
            return SQLStateSubclass.fromCode(((SQLException) t).getSQLState());

        return SQLStateSubclass.NONE;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }


}
