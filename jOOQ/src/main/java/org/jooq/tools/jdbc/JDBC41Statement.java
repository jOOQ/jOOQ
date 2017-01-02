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
