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
import java.util.Map;

/**
 * A query storing objects to the database. This is either an insert or an
 * update query.
 *
 * @param <R> The record type of the table being modified
 * @author Lukas Eder
 */
public interface StoreQuery<R extends Record> extends Query {

    /**
     * Add values to the store statement
     *
     * @param record The record holding values that are stored by the query
     */
    @Support
    void setRecord(R record);

    /**
     * Add a value to the store statement
     *
     * @param field The field
     * @param value The value
     */
    @Support
    <T> void addValue(Field<T> field, T value);

    /**
     * Add a value to the store statement
     *
     * @param field The field
     * @param value The value. If value is <code>null</code>, this results in
     *            calling {@link #addValue(Field, Object)} with null as a value.
     */
    @Support
    <T> void addValue(Field<T> field, Field<T> value);

    /**
     * Add multiple values to the store statement.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support
    void addValues(Map<? extends Field<?>, ?> map);

    /**
     * Configure the <code>INSERT</code> or <code>UPDATE</code> statement to return all fields in
     * <code>R</code>.
     *
     * @see #getReturnedRecords()
     */
    @Support
    void setReturning();

    /**
     * Configure the <code>INSERT</code> or <code>UPDATE</code> statement to return the generated
     * identity value.
     *
     * @param identity The table's identity
     * @see #getReturnedRecords()
     */
    @Support
    void setReturning(Identity<R, ?> identity);

    /**
     * Configure the <code>INSERT</code> or <code>UPDATE</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturnedRecords()
     */
    @Support
    void setReturning(Field<?>... fields);

    /**
     * Configure the <code>INSERT</code> or <code>UPDATE</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturnedRecords()
     */
    @Support
    void setReturning(Collection<? extends Field<?>> fields);

    /**
     * The record holding returned values as specified by any of the
     * {@link #setReturning()} methods.
     * <p>
     * If the insert statement returns several records, this is the same as
     * calling <code>getReturnedRecords().get(0)</code>
     * <p>
     * This implemented differently for every dialect:
     * <ul>
     * <li>Firebird and Postgres have native support for
     * <code>INSERT .. RETURNING</code> and <code>UPDATE .. RETURNING</code>
     * clauses</li>
     * <li>HSQLDB, Oracle, and DB2 JDBC drivers allow for retrieving any table
     * column as "generated key" in one statement</li>
     * <li>Derby, H2, Ingres, MySQL, SQL Server only allow for retrieving
     * IDENTITY column values as "generated key". If other fields are requested,
     * a second statement is issued. Client code must assure transactional
     * integrity between the two statements.</li>
     * <li>Sybase and SQLite allow for retrieving IDENTITY values as
     * <code>@@identity</code> or <code>last_inserted_rowid()</code> values.
     * Those values are fetched in a separate <code>SELECT</code> statement. If
     * other fields are requested, a second statement is issued. Client code
     * must assure transactional integrity between the two statements.</li>
     * </ul>
     *
     * @return The returned value as specified by any of the
     *         {@link #setReturning()} methods. This may return
     *         <code>null</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.
     * @see #getReturnedRecords()
     */
    @Support
    R getReturnedRecord();

    /**
     * The records holding returned values as specified by any of the
     * {@link #setReturning()} methods.
     * <p>
     * This implemented differently for every dialect:
     * <ul>
     * <li>Firebird and Postgres have native support for
     * <code>INSERT .. RETURNING</code> and <code>UPDATE .. RETURNING</code>
     * clauses</li>
     * <li>HSQLDB, Oracle, and DB2 JDBC drivers allow for retrieving any table
     * column as "generated key" in one statement</li>
     * <li>Derby, H2, Ingres, MySQL, SQL Server only allow for retrieving
     * IDENTITY column values as "generated key". If other fields are requested,
     * a second statement is issued. Client code must assure transactional
     * integrity between the two statements.</li>
     * <li>Sybase and SQLite allow for retrieving IDENTITY values as
     * <code>@@identity</code> or <code>last_inserted_rowid()</code> values.
     * Those values are fetched in a separate <code>SELECT</code> statement. If
     * other fields are requested, a second statement is issued. Client code
     * must assure transactional integrity between the two statements.</li>
     * </ul>
     *
     * @return The returned values as specified by any of the
     *         {@link #setReturning()} methods. Note:
     *         <ul>
     *         <li>Not all databases / JDBC drivers support returning several
     *         values on multi-row inserts!</li><li>This may return an empty
     *         <code>Result</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.</li>
     *         </ul>
     */
    @Support
    Result<R> getReturnedRecords();

}
