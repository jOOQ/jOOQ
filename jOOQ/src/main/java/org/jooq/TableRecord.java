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
 * A record originating from a single table
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface TableRecord<R extends TableRecord<R>> extends Record {

    /**
     * The table from which this record was read
     */
    Table<R> getTable();

    /**
     * Store this record back to the database.
     * <p>
     * Depending on the state of the provided keys' value, an
     * <code>INSERT</code> or an <code>UPDATE</code> statement is executed.
     * <p>
     * <h5>Statement type</h5>
     * <p>
     * <ul>
     * <li>If this record was created by client code, an <code>INSERT</code>
     * statement is executed</li>
     * <li>If this record was loaded by jOOQ, but the provided keys' value was
     * changed, an <code>INSERT</code> statement is executed. jOOQ expects that
     * primary key values will never change due to the principle of
     * normalisation in RDBMS. So if client code changes primary key values,
     * this is interpreted by jOOQ as client code wanting to duplicate this
     * record.</li>
     * <li>If this record was loaded by jOOQ, and the provided keys' value was
     * not changed, an <code>UPDATE</code> statement is executed.</li>
     * </ul>
     * <p>
     * In either statement type, only those fields are inserted/updated, which
     * had been explicitly set by client code, in order to allow for
     * <code>DEFAULT</code> values to be applied by the underlying RDBMS. If no
     * fields were modified, neither an <code>UPDATE</code> nor an
     * <code>INSERT</code> will be executed.
     * <h5>Automatic value generation</h5>
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
     *
     * @param keys The key fields used for deciding whether to execute an
     *            <code>INSERT</code> or <code>UPDATE</code> statement. If an
     *            <code>UPDATE</code> statement is executed, they are also the
     *            key fields for the <code>UPDATE</code> statement's
     *            <code>WHERE</code> clause.
     * @return The number of stored records.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @deprecated - 2.5.0 [#1736] - These methods will be made part of jOOQ's
     *             internal API soon. Do not reuse these methods.
     */
    @Deprecated
    int storeUsing(TableField<R, ?>... keys) throws DataAccessException, DataChangedException;

    /**
     * Deletes this record from the database, based on the value of the provided
     * keys.
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
     * <h5>Statement examples</h5>
     * <p>
     * The executed statement is <code><pre>
     * DELETE FROM [table]
     * WHERE [key fields = key values]
     * AND [version/timestamp fields = version/timestamp values]</pre></code>
     *
     * @param keys The key fields for the <code>DELETE</code> statement's
     *            <code>WHERE</code> clause.
     * @return The number of deleted records.
     * @throws DataAccessException if something went wrong executing the query
     * @throws DataChangedException If optimistic locking is enabled and the
     *             record has already been changed/deleted in the database
     * @deprecated - 2.5.0 [#1736] - These methods will be made part of jOOQ's
     *             internal API soon. Do not reuse these methods.
     */
    @Deprecated
    int deleteUsing(TableField<R, ?>... keys) throws DataAccessException, DataChangedException;

    /**
     * Refresh this record from the database, based on the value of the provided
     * keys.
     * <p>
     * The executed statement is <code><pre>
     * SELECT * FROM [table]
     * WHERE [key fields = key values]</pre></code>
     *
     * @param keys The key fields for the <code>SELECT</code> statement's
     *            <code>WHERE</code> clause.
     * @throws DataAccessException This exception is thrown if
     *             <ul>
     *             <li>something went wrong executing the query</li> <li>the
     *             record does not exist anymore in the database</li> <li>the
     *             provided keys return several records.</li>
     *             </ul>
     * @deprecated - 2.5.0 [#1736] - These methods will be made part of jOOQ's
     *             internal API soon. Do not reuse these methods.
     */
    @Deprecated
    void refreshUsing(TableField<R, ?>... keys) throws DataAccessException;

    /**
     * {@inheritDoc}
     */
    @Override
    R original();
}
