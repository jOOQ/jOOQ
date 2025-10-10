/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.r2dbc.spi.R2dbcException;

/**
 * The <code>DataAccessException</code> is a generic {@link RuntimeException}
 * indicating that something went wrong while executing a SQL statement from
 * jOOQ.
 * <p>
 * Unlike JDBC, jOOQ throws {@link RuntimeException}, knowing that
 * <ul>
 * <li>most {@link SQLException} types are not recoverable.</li>
 * <li>even when they are (e.g.
 * {@link SQLIntegrityConstraintViolationException}), they won't appear with
 * most statements.</li>
 * </ul>
 * <p>
 * Apart from jOOQ's own {@link DataAccessException} subtypes, which are thrown
 * by jOOQ's internals, most {@link SQLException} types (or
 * {@link R2dbcException} types) are translated and wrapped by:
 * <ul>
 * <li>{@link DataException} when jOOQ detects
 * {@link SQLStateClass#C22_DATA_EXCEPTION}.</li>
 * <li>{@link IntegrityConstraintViolationException} when jOOQ detects
 * {@link SQLStateClass#C23_INTEGRITY_CONSTRAINT_VIOLATION}.</li>
 * <li>{@link DataAccessException} otherwise.</li>
 * </ul>
 *
 * @author Sergey Epik - Merged into jOOQ from Spring JDBC Support
 * @author Lukas Eder
 */
public class DataAccessException extends RuntimeException {

    SQLStateClass    sqlStateClass;
    SQLStateSubclass sqlStateSubclass;

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
    @NotNull
    public String sqlState() {
        SQLException s = getCause(SQLException.class);
        if (s != null)
            return defaultIfNull(s.getSQLState(), "00000");

        R2dbcException r = getCause(R2dbcException.class);
        if (r != null)
            return defaultIfNull(r.getSqlState(), "00000");

        return "00000";
    }

    /**
     * Decode the {@link SQLException#getSQLState()} or
     * {@link R2dbcException#getSqlState()} from {@link #getCause()} into
     * {@link SQLStateClass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException} or {@link R2dbcException}.
     */
    @NotNull
    public SQLStateClass sqlStateClass() {
        if (sqlStateClass != null)
            return sqlStateClass;

        SQLException s = getCause(SQLException.class);
        if (s != null)
            return sqlStateClass(s);

        R2dbcException r = getCause(R2dbcException.class);
        if (r != null)
            return sqlStateClass(r);

        return SQLStateClass.NONE;
    }

    /**
     * Set the {@link SQLStateClass}.
     */
    public DataAccessException sqlStateClass(SQLStateClass c) {
        this.sqlStateClass = c;
        return this;
    }

    /**
     * Decode the {@link SQLException#getSQLState()} into {@link SQLStateClass}.
     */
    @NotNull
    public static SQLStateClass sqlStateClass(SQLException e) {





































        if (e.getSQLState() != null)
            return SQLStateClass.fromCode(e.getSQLState());
        else if (causePrefix(e, "org.sqlite"))
            return SQLStateClass.fromSQLiteVendorCode(e.getErrorCode());
        else if (causePrefix(e, "io.trino"))
            return SQLStateClass.fromTrinoVendorCode(e.getErrorCode());
        else
            return SQLStateClass.NONE;
    }

    private static final boolean causePrefix(SQLException e, String prefix) {
        return e.getClass().getName().startsWith(prefix)
            || e.getCause() != null && e.getCause().getClass().getName().startsWith(prefix);
    }

    /**
     * Decode the {@link R2dbcException#getSqlState()} into
     * {@link SQLStateClass}.
     */
    @NotNull
    public static SQLStateClass sqlStateClass(R2dbcException e) {
        if (e.getSqlState() != null)
            return SQLStateClass.fromCode(e.getSqlState());
        else
            return SQLStateClass.NONE;
    }

    /**
     * Set the {@link SQLStateSubclass}.
     */
    public DataAccessException sqlStateSubclass(SQLStateSubclass c) {
        this.sqlStateSubclass = c;
        return this;
    }

    /**
     * Decode the {@link SQLException#getSQLState()} or
     * {@link R2dbcException#getSqlState()} from {@link #getCause()} into
     * {@link SQLStateSubclass}, if this <code>DataAccessException</code> was
     * caused by a {@link SQLException} or {@link R2dbcException}.
     */
    @NotNull
    public SQLStateSubclass sqlStateSubclass() {
        if (sqlStateSubclass != null)
            return sqlStateSubclass;
        else
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
    @Nullable
    public <T extends Throwable> T getCause(Class<? extends T> type) {
        return ExceptionTools.getCause(this, type);
    }
}
