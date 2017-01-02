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
package org.jooq.tools.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Add JDBC 4.1 API compliance to a JDBC 4.0 {@link Statement}.
 * <p>
 * Extend this type if you want to compile {@link Statement},
 * {@link PreparedStatement}, and {@link CallableStatement} implementations on
 * both JDBC 4.0 (JDK 6) and 4.1 (JDK 7).
 *
 * @author Lukas Eder
 */
public abstract class JDBC41Statement {

    // ------------------------------------------------------------------------
    // Methods from JDBC 4.1 java.sql.Statement
    // ------------------------------------------------------------------------

    // JDBC 4.1 compliance: @Override
    @SuppressWarnings("unused")
    public final void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // JDBC 4.1 compliance: @Override
    @SuppressWarnings("unused")
    public final boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // ------------------------------------------------------------------------
    // Methods from JDBC 4.1 java.sql.PreparedCall
    // ------------------------------------------------------------------------

    // JDBC 4.1 compliance: @Override
    @SuppressWarnings("unused")
    public final <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // JDBC 4.1 compliance: @Override
    @SuppressWarnings("unused")
    public final <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
