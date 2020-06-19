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
package org.jooq.impl;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateSubclass;

/**
 * An exception that arises while parsing SQL through
 * {@link DSLContext#parser()}.
 *
 * @author Lukas Eder
 */
public final class ParserException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -724913199583039157L;
    private final String      sql;
    private int               position;
    private int               line;
    private int               column;

    public ParserException(String sql) {
        this(sql, null);
    }

    public ParserException(String sql, String message) {
        this(sql, message, null);
    }

    public ParserException(String sql, String message, SQLStateSubclass state) {
        this(sql, message, state, null);
    }

    public ParserException(String sql, String message, SQLStateSubclass state, Throwable cause) {
        super(
            (state == null ? "" : state + ": ")
          + (message == null ? "" : message + ": ")
          + sql, cause
        );

        this.sql = sql;
    }

    /**
     * The zero-based position within the SQL string at which an exception was thrown, if applicable.
     */
    public final int position() {
        return position;
    }

    /**
     * Set the {@link #position()}.
     */
    public final ParserException position(int p) {
        this.position = p;
        return this;
    }

    /**
     * The one-based line number within the SQL string at which an exception was thrown, if applicable.
     */
    public final int line() {
        return line;
    }

    /**
     * Set the {@link #line()}.
     */
    public final ParserException line(int l) {
        this.line = l;
        return this;
    }

    /**
     * The one-based column number within the SQL string at which an exception was thrown, if applicable.
     */
    public final int column() {
        return column;
    }

    /**
     * Set the {@link #column()}.
     */
    public final ParserException column(int c) {
        this.column = c;
        return this;
    }

    /**
     * The SQL string that caused the exception.
     */
    public final String sql() {
        return sql;
    }
}