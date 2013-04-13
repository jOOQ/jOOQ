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

import org.jooq.exception.DataAccessException;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * TableRecord<?> record =
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .returning(field1)
 *       .fetchOne();
 * </pre></code>
 * <p>
 * This implemented differently for every dialect:
 * <ul>
 * <li>Firebird and Postgres have native support for
 * <code>INSERT .. RETURNING</code> clauses</li>
 * <li>HSQLDB, Oracle, and DB2 JDBC drivers allow for retrieving any table
 * column as "generated key" in one statement</li>
 * <li>Derby, H2, Ingres, MySQL, SQL Server only allow for retrieving IDENTITY
 * column values as "generated key". If other fields are requested, a second
 * statement is issued. Client code must assure transactional integrity between
 * the two statements.</li>
 * <li>Sybase and SQLite allow for retrieving IDENTITY values as
 * <code>@@identity</code> or <code>last_inserted_rowid()</code> values. Those
 * values are fetched in a separate <code>SELECT</code> statement. If other
 * fields are requested, another statement is issued. Client code must assure
 * transactional integrity between these statements.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface InsertResultStep<R extends Record> extends Insert<R> {

    /**
     * The result holding returned values as specified by the
     * {@link InsertReturningStep}
     * <p>
     * This currently only works well for DB2, HSQLDB, MySQL, and Postgres
     *
     * @return The returned values as specified by the
     *         {@link InsertReturningStep}. Note:
     *         <ul>
     *         <li>Not all databases / JDBC drivers support returning several
     *         values on multi-row inserts!</li><li>This may return an empty
     *         <code>Result</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.</li>
     *         </ul>
     * @throws DataAccessException if something went wrong executing the query
     * @see InsertQuery#getReturnedRecords()
     */
    @Support
    Result<R> fetch() throws DataAccessException;

    /**
     * The record holding returned values as specified by the
     * {@link InsertReturningStep}
     *
     * @return The returned value as specified by the
     *         {@link InsertReturningStep}. This may return <code>null</code> in
     *         case jOOQ could not retrieve any generated keys from the JDBC
     *         driver.
     * @throws DataAccessException if something went wrong executing the query
     * @see InsertQuery#getReturnedRecord()
     */
    @Support
    R fetchOne() throws DataAccessException;
}
