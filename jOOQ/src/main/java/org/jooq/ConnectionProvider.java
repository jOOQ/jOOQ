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

import org.jooq.exception.DataAccessException;

/**
 * A connection lifecycle handler API.
 * <p>
 * The <code>ConnectionProvider</code> allows for abstracting the handling of
 * custom <code>Connection</code> lifecycles outside of jOOQ, injecting
 * behaviour into jOOQ's internals. jOOQ will try to acquire a new JDBC
 * {@link Connection} from the connection provider as early as needed, and will
 * release it as early as possible.
 * <p>
 * {@link TransactionProvider} implementations may choose to influence
 * {@link ConnectionProvider} behaviour, e.g. by acquiring connections upon
 * {@link TransactionProvider#begin(TransactionContext)} and by releasing
 * connections only upon {@link TransactionProvider#commit(TransactionContext)},
 * or {@link TransactionProvider#rollback(TransactionContext)}.
 *
 * @author Aaron Digulla
 * @author Lukas Eder
 */
public interface ConnectionProvider {

    /**
     * Acquire a connection from the connection lifecycle handler.
     * <p>
     * This method is called by jOOQ exactly once per execution lifecycle, i.e.
     * per {@link ExecuteContext}. Implementations may freely chose, whether
     * subsequent calls to this method:
     * <ul>
     * <li>return the same connection instance</li>
     * <li>return the same connection instance for the same thread</li>
     * <li>return the same connection instance for the same transaction (e.g. a
     * <code>javax.transaction.UserTransaction</code>)</li>
     * <li>return a fresh connection instance every time</li>
     * </ul>
     * <p>
     * jOOQ will guarantee that every acquired connection is released through
     * {@link #release(Connection)} exactly once.
     *
     * @return A connection for the current <code>ExecuteContext</code>.
     * @throws DataAccessException If anything went wrong while acquiring a
     *             connection
     */
    Connection acquire() throws DataAccessException;

    /**
     * Release a connection to the connection lifecycle handler.
     * <p>
     * jOOQ will guarantee that every acquired connection is released exactly
     * once.
     *
     * @param connection A connection that was previously obtained from
     *            {@link #acquire()}. This is never <code>null</code>.
     * @throws DataAccessException If anything went wrong while releasing a
     *             connection
     */
    void release(Connection connection) throws DataAccessException;
}
