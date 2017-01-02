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

import java.util.Collection;

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
 * <li>Firebird and Postgres have native support for
 * <code>INSERT .. RETURNING</code> clauses</li>
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
 * fields are requested, a second statement is issued. Client code must assure
 * transactional integrity between the two statements.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface InsertReturningStep<R extends Record> extends InsertFinalStep<R> {

    /**
     * Configure the <code>INSERT</code> statement to return all fields in
     * <code>R</code>.
     *
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning();

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning(Field<?>... fields);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning(Collection<? extends Field<?>> fields);
}
