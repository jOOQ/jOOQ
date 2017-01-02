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

import java.util.Optional;

import org.jooq.exception.DataAccessException;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * TableRecord&lt;?> record =
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .returning(field1)
 *       .fetchOne();
 * </pre></code>
 * <p>
 * This implemented differently for every dialect:
 * <ul>
 * <li>DB2 allows to execute
 * <code>SELECT .. FROM FINAL TABLE (INSERT ...)</code></li>
 * <li>HSQLDB, and Oracle JDBC drivers allow for retrieving any table column as
 * "generated key" in one statement</li>
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
     * {@link InsertReturningStep}.
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
     * {@link InsertReturningStep}.
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


    /**
     * The record holding returned values as specified by the
     * {@link InsertReturningStep}.
     *
     * @return The returned value as specified by the
     *         {@link InsertReturningStep}
     * @throws DataAccessException if something went wrong executing the query
     * @see InsertQuery#getReturnedRecord()
     */
    @Support
    Optional<R> fetchOptional() throws DataAccessException;

}
