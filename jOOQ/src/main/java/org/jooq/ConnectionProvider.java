/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
