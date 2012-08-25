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

import java.sql.ResultSet;
import java.sql.Statement;

import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataChangedException;
import org.jooq.impl.Factory;

/**
 * A common interface for records that can be stored back to the database again.
 * <p>
 * Any {@link Record} can be {@link Updatable}, if
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
 * can also be instanciated without any underlying {@link Configuration}, in
 * case of which they have to be attached first, in order to be refreshed,
 * stored, or deleted.
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface UpdatableRecord<R extends UpdatableRecord<R>> extends Updatable<R>, TableRecord<R> {

    /**
     * The table from which this record was read
     */
    @Override
    UpdatableTable<R> getTable();

    /**
     * Store this record back to the database.
     * <p>
     * Depending on the state of the primary key's or main unique key's value,
     * an <code>INSERT</code> or an <code>UPDATE</code> statement is executed.
     * <p>
     * <h3>Statement type</h3>
     * <p>
     * <ul>
     * <li>If this record was created by client code, an <code>INSERT</code>
     * statement is executed</li>
     * <li>If this record was loaded by jOOQ, but the primary key value was
     * changed, an <code>INSERT</code> statement is executed. jOOQ expects that
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
     * <h3>Automatic value generation</h3>
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
     * record returns fields for either
     * {@link UpdatableTable#getRecordVersion()} or
     * {@link UpdatableTable#getRecordTimestamp()}, then these values are set
     * onto the <code>INSERT</code> or <code>UPDATE</code> statement being
     * executed. On execution success, the generated values are set to this
     * record. Use the code-generation configuration to specify naming patterns
     * for auto-generated "version" and "timestamp" columns.
     * <p>
     * Should you want to circumvent jOOQ-generated updates to these columns,
     * you can render an <code>INSERT</code> or <code>UPDATE</code> statement
     * manually using the various {@link Factory#insertInto(Table)},
     * {@link Factory#update(Table)} methods.</li>
     * </ul>
     * <h3>Optimistic locking</h3>
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
     * {@link UpdatableTable#getRecordVersion()} or
     * {@link UpdatableTable#getRecordTimestamp()}, then these values are
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
     * checking of database record state and the actual <code>UPDATE</code></li>
     * </ul>
     * <p>
     * See {@link LockProvider#setForUpdate(boolean)} for more details</li>
     * </ul>
     * <h3>Statement examples</h3>
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
     * This is in fact the same as calling
     * <code>store(getTable().getMainKey().getFieldsArray())</code>
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     */
    int store() throws DataAccessException, DataChangedException;

    /**
     * Deletes this record from the database, based on the value of the primary
     * key or main unique key.
     * <p>
     * <h3>Optimistic locking</h3>
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
     * {@link UpdatableTable#getRecordVersion()} or
     * {@link UpdatableTable#getRecordTimestamp()}, then these values are
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
     * See {@link LockProvider#setForUpdate(boolean)} for more details</li>
     * </ul>
     * <h3>Statement examples</h3>
     * <p>
     * The executed statement is <code><pre>
     * DELETE FROM [table]
     * WHERE [key fields = key values]
     * AND [version/timestamp fields = version/timestamp values]</pre></code>
     * <p>
     * This is in fact the same as calling
     * <code>delete(getTable().getMainKey().getFieldsArray())</code>
     *
     * @return <code>1</code> if the record was deleted from the database.
     *         <code>0</code> if deletion was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     */
    int delete() throws DataAccessException, DataChangedException;

    /**
     * Refresh this record from the database, based on the value of the primary
     * key or main unique key.
     * <p>
     * This is in fact the same as calling
     * <code>refresh(getTable().getMainKey().getFieldsArray())</code>
     * <p>
     * The executed statement is <code><pre>
     * SELECT * FROM [table]
     * WHERE [main key fields = main key values]</pre></code>
     *
     * @throws DataAccessException This exception is thrown if
     *             <ul>
     *             <li>something went wrong executing the query</li> <li>the
     *             record does not exist anymore in the database</li>
     *             </ul>
     */
    void refresh() throws DataAccessException;

    /**
     * Duplicate this record (in memory) and reset all fields from the primary
     * key or main unique key, such that a subsequent call to {@link #store()}
     * will result in an <code>INSERT</code> statement.
     *
     * @return A new record, distinct from <code>this</code> record.
     */
    R copy();
}
