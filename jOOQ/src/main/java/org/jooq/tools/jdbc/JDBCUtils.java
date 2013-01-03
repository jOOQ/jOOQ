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
package org.jooq.tools.jdbc;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.Statement;

/**
 * JDBC-related utility methods
 *
 * @author Lukas Eder
 */
public class JDBCUtils {

    /**
     * Safely close a statement
     * <p>
     * This method will silently ignore if <code>statement</code> is
     * <code>null</code>, or if {@link Statement#close()} throws an exception.
     */
    public static final void safeClose(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely close a result set
     * <p>
     * This method will silently ignore if <code>resultSet</code> is
     * <code>null</code>, or if {@link ResultSet#close()} throws an exception.
     */
    public static final void safeClose(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely close a result set and / or a statement
     * <p>
     * This method will silently ignore if <code>resultSet</code> or
     * <code>statement</code> is <code>null</code>, or if
     * {@link ResultSet#close()} or {@link Statement#close()} throws an
     * exception.
     */
    public static final void safeClose(ResultSet resultSet, PreparedStatement statement) {
        safeClose(resultSet);
        safeClose(statement);
    }

    /**
     * Safely free a blob
     * <p>
     * This method will silently ignore if <code>blob</code> is
     * <code>null</code>, or if {@link Blob#free()} throws an exception.
     */
    public static final void safeFree(Blob blob) {
        if (blob != null) {
            try {
                blob.free();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely free a clob
     * <p>
     * This method will silently ignore if <code>clob</code> is
     * <code>null</code>, or if {@link Clob#free()} throws an exception.
     */
    public static final void safeFree(Clob clob) {
        if (clob != null) {
            try {
                clob.free();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Convenient way to check if a JDBC-originated record was null
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param stream The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link SQLInput#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(SQLInput stream, T value) throws SQLException {
        return stream.wasNull() ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was null
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param rs The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link ResultSet#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(ResultSet rs, T value) throws SQLException {
        return rs.wasNull() ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was null
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param statement The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link CallableStatement#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(CallableStatement statement, T value) throws SQLException {
        return statement.wasNull() ? null : value;
    }

    /**
     * No instances
     */
    private JDBCUtils() {}
}
