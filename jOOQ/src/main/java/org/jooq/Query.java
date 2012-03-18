/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.Factory;

/**
 * Any query
 *
 * @author Lukas Eder
 */
public interface Query extends QueryPart {

    /**
     * Execute the query, if it has been created with a properly configured
     * factory
     *
     * @return A result value, depending on the concrete implementation of
     *         {@link Query}:
     *         <ul>
     *         <li> {@link Delete} : the number of deleted records</li>
     *         <li> {@link Insert} : the number of inserted records</li>
     *         <li> {@link Merge} : the result may have no meaning</li>
     *         <li> {@link Select} : the number of resulting records</li>
     *         <li> {@link Truncate} : the result may have no meaning</li>
     *         <li> {@link Update} : the number of updated records</li>
     *         </ul>
     * @throws DataAccessException If anything goes wrong in the database
     */
    int execute() throws DataAccessException;

    /**
     * Retrieve the SQL code rendered by this Query
     * <p>
     * This method can be expected to work correctly for any SQL dialect, as a
     * query is usually "attached" when created from a {@link Factory}.
     * <p>
     * Use this method, when you want to use jOOQ for object oriented query
     * creation, but execute the query with some other technology, such as
     * <ul>
     * <li>JDBC</li>
     * <li>Spring Templates</li>
     * <li>JPA native queries</li>
     * <li>etc...</li>
     * </ul>
     * <p>
     * Note, this is the same as calling {@link #getSQL(boolean)}. The boolean
     * parameter will depend on your {@link Factory}'s {@link Settings}:
     * <table border="1">
     * <tr>
     * <th><code>StatementType</code></th>
     * <th>boolean parameter</th>
     * <th>effect</th>
     * </tr>
     * <tr>
     * <td> {@link StatementType#PREPARED_STATEMENT}</td>
     * <td><code>false</code> (default)</td>
     * <td>This will render bind variables to be used with a JDBC
     * {@link PreparedStatement}. You can extract bind values from this
     * <code>Query</code> using {@link #getBindValues()}</td>
     * </tr>
     * <tr>
     * <td> {@link StatementType#STATIC_STATEMENT}</td>
     * <td><code>true</code></td>
     * <td>This will inline all bind variables in a statement to be used with a
     * JDBC {@link Statement}</td>
     * </tr>
     * </table>
     *
     * @see #getSQL(boolean)
     */
    String getSQL();

    /**
     * Retrieve the SQL code rendered by this Query
     * <p>
     * See {@link #getSQL()} for more details
     *
     * @param inline Whether to inline bind variables. This overrides values in
     *            {@link Settings#getStatementType()}
     * @return The generated SQL
     */
    String getSQL(boolean inline);

    /**
     * Retrieve the bind values that will be bound by this Query. This
     * <code>List</code> cannot be modified. To modify bind values, use
     * {@link #getParams()} instead.
     */
    List<Object> getBindValues();

    /**
     * Get a <code>Map</code> of named parameters. The <code>Map</code> itself
     * cannot be modified, but the {@link Param} elements allow for modifying
     * bind values on an existing {@link Query}.
     * <p>
     * Bind values created with {@link Factory#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see Factory#param(String, Object)
     */
    Map<String, Param<?>> getParams();

    /**
     * Get a named parameter from the {@link Query}, provided its name.
     * <p>
     * Bind values created with {@link Factory#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see Factory#param(String, Object)
     */
    Param<?> getParam(String name);

    /**
     * Bind a new value to a named parameter
     *
     * @param param The named parameter name. If this is a number, then this is
     *            the same as calling {@link #bind(int, Object)}
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter name or index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    Query bind(String param, Object value) throws IllegalArgumentException, DataTypeException;

    /**
     * Bind a new value to an indexed parameter
     *
     * @param index The parameter index, starting with 1
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    Query bind(int index, Object value) throws IllegalArgumentException, DataTypeException;

}
