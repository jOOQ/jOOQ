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
