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

import java.util.List;

import org.jooq.exception.DataAccessException;

/**
 * A routine is a callable object in your RDBMS.
 * <p>
 * Callable objects are mainly stored procedures and stored functions. The
 * distinction between those two object types is very subtle and not well
 * defined across various RDBMS. In general, this can be said:
 * <p>
 * Procedures:
 * <p>
 * <ul>
 * <li>Are called as callable statements</li>
 * <li>Have no return value</li>
 * <li>Support OUT parameters</li>
 * </ul>
 * Functions
 * <p>
 * <ul>
 * <li>Can be used in SQL statements</li>
 * <li>Have a return value</li>
 * <li>Don't support OUT parameters</li>
 * </ul>
 * But there are exceptions to these rules:
 * <p>
 * <ul>
 * <li>DB2, H2, and HSQLDB don't allow for JDBC escape syntax when calling
 * functions. Functions must be used in a SELECT statement</li>
 * <li>H2 only knows functions (without OUT parameters)</li>
 * <li>Oracle functions may have OUT parameters</li>
 * <li>Oracle knows functions that mustn't be used in SQL statements</li>
 * <li>Oracle parameters can have default values (to support this, jOOQ renders
 * PL/SQL instead of the JDBC escape syntax)</li>
 * <li>Postgres only knows functions (with all features combined)</li>
 * <li>The Sybase JDBC driver doesn't handle null values correctly when using
 * the JDBC escape syntax on functions</li>
 * <li>etc...</li>
 * </ul>
 * <p>
 * Hence, with #852, jOOQ 1.6.8, the distinction between procedures and
 * functions becomes obsolete. All stored routines are simply referred to as
 * "Routine".
 *
 * @author Lukas Eder
 */
public interface Routine<T> extends QueryPart {

    /**
     * Get the routine schema
     */
    Schema getSchema();

    /**
     * The name of this routine
     */
    String getName();

    /**
     * The container package of this stored procedure or function.
     * <p>
     * This is only supported in the {@link SQLDialect#ORACLE} dialect.
     *
     * @return The container package of this object, or <code>null</code> if
     *         there is no such container.
     */
    Package getPackage();

    /**
     * A list of OUT parameters passed to the stored procedure as argument. This
     * list contains all parameters that are either OUT or INOUT in their
     * respective order of appearance in {@link #getParameters()}.
     *
     * @return The list of out parameters
     * @see #getParameters()
     */
    List<Parameter<?>> getOutParameters();

    /**
     * A list of IN parameters passed to the stored procedure as argument. This
     * list contains all parameters that are either IN or INOUT in their
     * respective order of appearance in {@link #getParameters()}.
     *
     * @return The list of in parameters
     * @see #getParameters()
     */
    List<Parameter<?>> getInParameters();

    /**
     * @return The routine's return value (if it is a function)
     */
    T getReturnValue();

    /**
     * @return A list of parameters passed to the stored object as argument
     */
    List<Parameter<?>> getParameters();

    /**
     * Execute the stored object using a {@link Configuration} object
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    int execute(Configuration configuration) throws DataAccessException;

    /**
     * Execute the stored object on an underlying connection
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    int execute() throws DataAccessException;
}
