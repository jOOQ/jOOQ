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
package org.jooq;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.exception.DataAccessException;

/**
 * An operation that can run code and statements against a provided JDBC
 * {@link Connection}.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface ConnectionCallable<T> {

    /**
     * Run statements.
     * <p>
     * Implementations may freely use the argument {@link Connection} to run
     * statements against the database. Implementations MUST manage the
     * lifecycle of any resources created from this <code>connection</code>,
     * such as {@link Statement} or {@link ResultSet}. Implementations MUST NOT
     * manage the lifecycle of the {@link Connection}, which is managed by the
     * {@link ConnectionProvider} that provided the <code>connection</code> to
     * this {@link ConnectionCallable}.
     *
     * @param connection The connection.
     * @return The outcome of the callable.
     * @throws Exception Any exception, including {@link SQLException}, that
     *             will be propagated as an unchecked
     *             {@link DataAccessException}.
     */
    T run(Connection connection) throws Exception;
}
