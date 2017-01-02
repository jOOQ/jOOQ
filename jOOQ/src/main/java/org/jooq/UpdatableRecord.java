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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataChangedException;
import org.jooq.exception.NoDataFoundException;
import org.jooq.exception.TooManyRowsException;

/**
 * A common interface for records that can be stored back to the database again.
 * <p>
 * Any {@link Record} can be updatable, if
 * <p>
 * <ol>
 * <li>it represents a record from a table or view - a {@link TableRecord}</li>
 * <li>its underlying table or view has a "main unique key", i.e. a primary key
 * or at least one unique key</li>
 * </ol>
 * <p>
 * The "main unique key" is used by jOOQ to perform the various operations that
 * can be performed on an <code>UpdatableRecord</code>:
 * <p>
 * <ul>
 * <li> {@link #delete()} : Deleting the record</li>
 * <li> {@link #refresh()} : Refreshing the records attributes (or loading it for
 * the first time)</li>
 * <li> {@link #store()} : Storing the record to the database. This executes
 * either an <code>INSERT</code> or an <code>UPDATE</code> statement</li>
 * </ul>
 * <p>
 * <code>UpdatableRecords</code> are {@link Attachable}, which means that they
 * hold an underlying {@link Configuration} that they can be detached from. They
 * can also be instantiated without any underlying {@link Configuration}, in
 * case of which they have to be attached first, in order to be refreshed,
 * stored, or deleted.
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface UpdatableRecord<R extends UpdatableRecord<R>> extends TableRecord<R> {

    /**
     * A Record copy holding values for the {@link Table#getPrimaryKey()}.
     * <p>
     * The returned record consists exactly of those fields as returned by the
     * table's primary key: {@link UniqueKey#getFields()}.
     * <p>
     * Generated subtypes may covariantly override this method to add more
     * record type information. For instance, they may return {@link Record1},
     * {@link Record2}, ...
     */
    Record key();

    /**
     * Store this record back to the database.
     * <p>
     * Depending on the state of the primary key's value, an {@link #insert()}
     * or an {@link #update()} statement is executed.
     * <p>
     * <h5>Statement type</h5>
     * <p>
     * <ul>
     * <li>If this record was created by client code, an <code>INSERT</code>
     * statement is executed</li>
     * <li>If this record was loaded by jOOQ and the primary key value was
     * changed, an <code>INSERT</code> statement is executed (unless
     * {@link Settings#isUpdatablePrimaryKeys()} is set). jOOQ expects that
     * primary key values will never change due to the principle of
     * normalisation in RDBMS. So if client code changes primary key values,
     * this is interpreted by jOOQ as client code wanting to duplicate this
     * record.</li>
     * <li>If this record was loaded by jOOQ, and the primary key value was not
     * changed, an <code>UPDATE</code> statement is executed.</li>
     * </ul>
     * <p>
     * In either statement type, only those fields are inserted/updated, which
     * had been explicitly set by client code, in order to allow for
     * <code>DEFAULT</code> values to be applied by the underlying RDBMS. If no
     * fields were modified, neither an <code>UPDATE</code> nor an
     * <code>INSERT</code> will be executed.
     * <p>
     * <h5>Automatic value generation</h5>
     * <p>
     * Use {@link #insert()} or {@link #update()} to explicitly force either
     * statement type.
     * <p>
     * <ul>
     * <li><strong>IDENTITY columns</strong>
     * <p>
     * If there is an <code>IDENTITY</code> column defined on the record's
     * underlying table (see {@link Table#getIdentity()}), then the
     * auto-generated <code>IDENTITY</code> value is refreshed automatically on
     * <code>INSERT</code>'s. Refreshing is done using
     * {@link Statement#getGeneratedKeys()}, where this is supported by the JDBC
     * driver. See also {@link InsertQuery#getReturnedRecord()} for more details
     * </li>
     * <li><strong>VERSION and TIMESTAMP columns</strong>
     * <p>
     * jOOQ can auto-generate "version" and "timestamp" values that can be used
     * for optimistic locking. If this is an {@link UpdatableRecord} and if this
     * record returns fields for either {@link Table#getRecordVersion()} or
     * {@link Table#getRecordTimestamp()}, then these values are set onto the
     * <code>INSERT</code> or <code>UPDATE</code> statement being executed. On
     * execution success, the generated values are set to this record. Use the
     * code-generation configuration to specify naming patterns for
     * auto-generated "version" and "timestamp" columns.
     * <p>
     * Should you want to circumvent jOOQ-generated updates to these columns,
     * you can render an <code>INSERT</code> or <code>UPDATE</code> statement
     * manually using the various {@link DSLContext#insertInto(Table)},
     * {@link DSLContext#update(Table)} methods.</li>
     * </ul>
     * <p>
     * <h5>Optimistic locking</h5>
     * <p>
     * If an <code>UPDATE</code> statement is executed and
     * {@link Settings#isExecuteWithOptimisticLocking()} is set to
     * <code>true</code>, then this record will first be compared with the
     * latest state in the database. There are two modes of operation for
     * optimistic locking:
     * <ul>
     * <li><strong>With VERSION and/or TIMESTAMP columns configured</strong>
     * <p>
     * This is the preferred way of using optimistic locking in jOOQ. If this is
     * an {@link UpdatableRecord} and if this record returns fields for either
     * {@link Table#getRecordVersion()} or {@link Table#getRecordTimestamp()},
     * then these values are compared to the corresponding value in the database
     * in the <code>WHERE</code> clause of the executed <code>DELETE</code>
     * statement.</li>
     * <li><strong>Without any specific column configurations</strong>
     * <p>
     * In order to compare this record with the latest state, the database
     * record will be locked pessimistically using a
     * <code>SELECT .. FOR UPDATE</code> statement. Not all databases support
     * the <code>FOR UPDATE</code> clause natively. Namely, the following
     * databases will show slightly different behaviour:
     * <ul>
     * <li> {@link SQLDialect#CUBRID} and {@link SQLDialect#SQLSERVER}: jOOQ will
     * try to lock the database record using JDBC's
     * {@link ResultSet#TYPE_SCROLL_SENSITIVE} and
     * {@link ResultSet#CONCUR_UPDATABLE}.</li>
     * <li> {@link SQLDialect#SQLITE}: No pessimistic locking is possible. Client
     * code must assure that no race-conditions can occur between jOOQ's
     * checking of database record state and the actual <code>UPDATE</code></li>
     * </ul>
     * <p>
     * See {@link SelectQuery#setForUpdate(boolean)} for more details</li>
     * </ul>
     * <p>
     * <h5>Statement examples</h5>
     * <p>
     * Possible statements are
     * <ul>
     * <li>
     * <code><pre>
     * INSERT INTO [table] ([modified fields, including keys])
     * VALUES ([modified values, including keys])</pre></code></li>
     * <li>
     * <code><pre>
     * UPDATE [table]
     * SET [modified fields = modified values, excluding keys]
     * WHERE [key fields = key values]
     * AND [version/timestamp fields = version/timestamp values]</pre></code></li>
     * </ul>
     * <p>
     * <h3>Statement execution enforcement</h3>
     * <p>
     * If you want to enforce statement execution, regardless if the values in
     * this record were changed, you can explicitly set the changed flags for
     * all values with {@link #changed(boolean)} or for single values with
     * {@link #changed(Field, boolean)}, prior to storing.
     * <p>
     * This is the same as calling <code>record.store(record.fields())</code>
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #insert()
     * @see #update()
     */
    int store() throws DataAccessException, DataChangedException;

    /**
     * Store parts of this record to the database.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #store()
     * @see #insert(Field...)
     * @see #update(Field...)
     */
    int store(Field<?>... fields) throws DataAccessException, DataChangedException;

    /**
     * Store parts of this record to the database.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #store()
     * @see #insert(Field...)
     * @see #update(Field...)
     */
    int store(Collection<? extends Field<?>> fields) throws DataAccessException, DataChangedException;

    /**
     * Store this record back to the database using an <code>INSERT</code>
     * statement.
     * <p>
     * This is the same as {@link #store()}, except that an <code>INSERT</code>
     * statement (or no statement) will always be executed.
     * <p>
     * If you want to enforce statement execution, regardless if the values in
     * this record were changed, you can explicitly set the changed flags for
     * all values with {@link #changed(boolean)} or for single values with
     * {@link #changed(Field, boolean)}, prior to insertion.
     * <p>
     * This is the same as calling <code>record.insert(record.fields())</code>
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @see #store()
     */
    @Override
    int insert() throws DataAccessException;

    /**
     * Store parts of this record to the database using an <code>INSERT</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert()
     */
    @Override
    int insert(Field<?>... fields) throws DataAccessException;

    /**
     * Store parts of this record to the database using an <code>INSERT</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert()
     */
    @Override
    int insert(Collection<? extends Field<?>> fields) throws DataAccessException;

    /**
     * Store this record back to the database using an <code>UPDATE</code>
     * statement.
     * <p>
     * This is the same as {@link #store()}, except that an <code>UPDATE</code>
     * statement (or no statement) will always be executed.
     * <p>
     * If you want to enforce statement execution, regardless if the values in
     * this record were changed, you can explicitly set the changed flags for
     * all values with {@link #changed(boolean)} or for single values with
     * {@link #changed(Field, boolean)}, prior to updating.
     * <p>
     * This is the same as calling <code>record.update(record.fields())</code>
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #store()
     */
    int update() throws DataAccessException, DataChangedException;

    /**
     * Store parts of this record to the database using an <code>UPDATE</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #update()
     */
    int update(Field<?>... fields) throws DataAccessException, DataChangedException;

    /**
     * Store parts of this record to the database using an <code>UPDATE</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @see #update()
     */
    int update(Collection<? extends Field<?>> fields) throws DataAccessException, DataChangedException;

    /**
     * Deletes this record from the database, based on the value of the primary
     * key or main unique key.
     * <p>
     * <h5>Optimistic locking</h5>
     * <p>
     * If a <code>DELETE</code> statement is executed and
     * {@link Settings#isExecuteWithOptimisticLocking()} is set to
     * <code>true</code>, then this record will first be compared with the
     * latest state in the database. There are two modes of operation for
     * optimistic locking:
     * <ul>
     * <li><strong>With VERSION and/or TIMESTAMP columns configured</strong>
     * <p>
     * This is the preferred way of using optimistic locking in jOOQ. If this is
     * an {@link UpdatableRecord} and if this record returns fields for either
     * {@link Table#getRecordVersion()} or
     * {@link Table#getRecordTimestamp()}, then these values are
     * compared to the corresponding value in the database in the
     * <code>WHERE</code> clause of the executed <code>DELETE</code> statement.</li>
     * <li><strong>Without any specific column configurations</strong>
     * <p>
     * In order to compare this record with the latest state, the database
     * record will be locked pessimistically using a
     * <code>SELECT .. FOR UPDATE</code> statement. Not all databases support
     * the <code>FOR UPDATE</code> clause natively. Namely, the following
     * databases will show slightly different behaviour:
     * <ul>
     * <li> {@link SQLDialect#CUBRID} and {@link SQLDialect#SQLSERVER}: jOOQ will
     * try to lock the database record using JDBC's
     * {@link ResultSet#TYPE_SCROLL_SENSITIVE} and
     * {@link ResultSet#CONCUR_UPDATABLE}.</li>
     * <li> {@link SQLDialect#SQLITE}: No pessimistic locking is possible. Client
     * code must assure that no race-conditions can occur between jOOQ's
     * checking of database record state and the actual <code>DELETE</code></li>
     * </ul>
     * <p>
     * See {@link SelectQuery#setForUpdate(boolean)} for more details</li>
     * </ul>
     * <h5>Statement examples</h5>
     * <p>
     * The executed statement is <code><pre>
     * DELETE FROM [table]
     * WHERE [key fields = key values]
     * AND [version/timestamp fields = version/timestamp values]</pre></code>
     * <p>
     * This is in fact the same as calling
     * <code>delete(getTable().getPrimaryKey().getFieldsArray())</code>
     *
     * @return <code>1</code> if the record was deleted from the database.
     *         <code>0</code> if deletion was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     */
    int delete() throws DataAccessException, DataChangedException;

    /**
     * Refresh this record from the database.
     * <p>
     * A successful refresh results in the following:
     * <ul>
     * <li>{@link #valuesRow()} will have been restored to the respective values
     * from the database</li>
     * <li>{@link #original()} will match this record</li>
     * <li>{@link #changed()} will be <code>false</code></li>
     * </ul>
     * <p>
     * Refreshing can trigger any of the following actions:
     * <ul>
     * <li>Executing a new <code>SELECT</code> statement, if this is an
     * {@link UpdatableRecord}.</li>
     * <li>Failing, otherwise</li>
     * </ul>
     * <p>
     * This is the same as calling <code>record.refresh(record.fields())</code>
     *
     * @throws DataAccessException This exception is thrown if something went
     *             wrong executing the refresh <code>SELECT</code> statement
     * @throws NoDataFoundException If the record does not exist anymore in the
     *             database
     */
    void refresh() throws DataAccessException;

    /**
     * Refresh parts of this record from the database.
     * <p>
     * A successful refresh results in the following:
     * <ul>
     * <li>{@link #valuesRow()} will have been restored to the respective values
     * from the database</li>
     * <li>{@link #original()} will match this record</li>
     * <li>{@link #changed()} will be <code>false</code></li>
     * </ul>
     * <p>
     * Refreshing can trigger any of the following actions:
     * <ul>
     * <li>Executing a new <code>SELECT</code> statement, if this is an
     * {@link UpdatableRecord}.</li>
     * <li>Failing, otherwise</li>
     * </ul>
     * <p>
     * This is the same as calling <code>record.refresh(record.fields())</code>
     *
     * @throws DataAccessException This exception is thrown if something went
     *             wrong executing the refresh <code>SELECT</code> statement
     * @throws NoDataFoundException If the record does not exist anymore in the
     *             database
     */
    void refresh(Field<?>... fields) throws DataAccessException, NoDataFoundException;

    /**
     * Refresh parts of this record from the database.
     * <p>
     * A successful refresh results in the following:
     * <ul>
     * <li>{@link #valuesRow()} will have been restored to the respective values
     * from the database</li>
     * <li>{@link #original()} will match this record</li>
     * <li>{@link #changed()} will be <code>false</code></li>
     * </ul>
     * <p>
     * Refreshing can trigger any of the following actions:
     * <ul>
     * <li>Executing a new <code>SELECT</code> statement, if this is an
     * {@link UpdatableRecord}.</li>
     * <li>Failing, otherwise</li>
     * </ul>
     * <p>
     * This is the same as calling <code>record.refresh(record.fields())</code>
     *
     * @throws DataAccessException This exception is thrown if something went
     *             wrong executing the refresh <code>SELECT</code> statement
     * @throws NoDataFoundException If the record does not exist anymore in the
     *             database
     */
    void refresh(Collection<? extends Field<?>> fields) throws DataAccessException, NoDataFoundException;

    /**
     * Duplicate this record (in memory) and reset all fields from the primary
     * key or main unique key, such that a subsequent call to {@link #store()}
     * will result in an <code>INSERT</code> statement.
     *
     * @return A new record, distinct from <code>this</code> record.
     */
    R copy();

    /**
     * Fetch a child record of this record, given a foreign key.
     * <p>
     * This returns a child record referencing this record through a given
     * foreign key. If no child record was found, this returns <code>null</code>
     *
     * @throws DataAccessException if something went wrong executing the query
     * @throws TooManyRowsException if the query returned more than one record
     * @see ForeignKey#fetchChildren(java.util.Collection)
     * @see ForeignKey#fetchChildren(Record)
     * @see ForeignKey#fetchChildren(Record...)
     */
    <O extends TableRecord<O>> O fetchChild(ForeignKey<O, R> key) throws TooManyRowsException, DataAccessException;

    /**
     * Fetch child records of this record, given a foreign key.
     * <p>
     * This returns childs record referencing this record through a given
     * foreign key.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see ForeignKey#fetchChildren(java.util.Collection)
     * @see ForeignKey#fetchChildren(Record)
     * @see ForeignKey#fetchChildren(Record...)
     */
    <O extends TableRecord<O>> Result<O> fetchChildren(ForeignKey<O, R> key) throws DataAccessException;
}
