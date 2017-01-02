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

// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.POSTGRES;

import java.util.Optional;

import org.jooq.exception.DataAccessException;

/**
 * This type is used for the {@link Update}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * TableRecord&lt;?> record =
 * create.update(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .returning(field1)
 *       .fetchOne();
 * </pre></code>
 * <p>
 * This implemented differently for every dialect:
 * <ul>
 * <li>Firebird and Postgres have native support for
 * <code>UPDATE .. RETURNING</code> clauses</li>
 * <li>DB2 allows to execute
 * <code>SELECT .. FROM FINAL TABLE (UPDATE ...)</code></li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface UpdateResultStep<R extends Record> extends Update<R> {

    /**
     * The result holding returned values as specified by the
     * {@link UpdateReturningStep}.
     *
     * @return The returned values as specified by the
     *         {@link UpdateReturningStep}. Note:
     *         <ul>
     *         <li>Not all databases / JDBC drivers support returning several
     *         values on multi-row inserts!</li><li>This may return an empty
     *         <code>Result</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.</li>
     *         </ul>
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdateQuery#getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    Result<R> fetch() throws DataAccessException;

    /**
     * The record holding returned values as specified by the
     * {@link UpdateReturningStep}.
     *
     * @return The returned value as specified by the
     *         {@link UpdateReturningStep}. This may return <code>null</code> in
     *         case jOOQ could not retrieve any generated keys from the JDBC
     *         driver.
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdateQuery#getReturnedRecord()
     */
    @Support({ FIREBIRD, POSTGRES })
    R fetchOne() throws DataAccessException;


    /**
     * The record holding returned values as specified by the
     * {@link UpdateReturningStep}.
     *
     * @return The returned value as specified by the
     *         {@link UpdateReturningStep}.
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdateQuery#getReturnedRecord()
     */
    @Support({ FIREBIRD, POSTGRES })
    Optional<R> fetchOptional() throws DataAccessException;

}
