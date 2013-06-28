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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;
import java.util.Map;

/**
 * A query for data insertion
 *
 * @param <R> The record type of the table being inserted into
 * @author Lukas Eder
 */
public interface InsertQuery<R extends Record> extends StoreQuery<R>, Insert<R> {

    /**
     * Adds a new Record to the insert statement for multi-record inserts
     * <p>
     * Calling this method will cause subsequent calls to
     * {@link #addValue(Field, Object)} (and similar) to fill the next record.
     * <p>
     * If this call is not followed by {@link #addValue(Field, Object)} calls,
     * then this call has no effect.
     * <p>
     * If this call is done on a fresh insert statement (without any values
     * yet), then this call has no effect either.
     */
    @Support
    void newRecord();

    /**
     * Short for calling <code>
     * newRecord();
     * setRecord(record);
     * </code>
     *
     * @param record The record to add to this insert statement.
     */
    @Support
    void addRecord(R record);

    /**
     * Whether a <code>ON DUPLICATE KEY UPDATE</code> clause should be added to
     * this <code>INSERT</code> statement.
     * <p>
     * When setting this flag to <code>true</code>, be sure to also add values
     * "for update" using the {@link #addValueForUpdate(Field, Field)} methods.
     * <p>
     * The <code>ON DUPLICATE KEY UPDATE</code> flag is mutually exclusive with
     * the <code>ON DUPLICATE KEY IGNORE</code> flag (see
     * {@link #onDuplicateKeyIgnore(boolean)}. Setting one will unset the other
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ CUBRID, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    void onDuplicateKeyUpdate(boolean flag);

    /**
     * Whether an <code>ON DUPLICATE KEY IGNORE</code> clause should be added to
     * this <code>INSERT</code> statement.
     * <p>
     * This clause is not actually supported in this form by any database, but
     * can be simulated as such:
     * <table border="1">
     * <tr>
     * <th>Dialect</th>
     * <th>Simulation</th>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#MYSQL}</td>
     * <td> <code><pre>INSERT IGNORE INTO ..</pre></code></td>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#CUBRID}</td>
     * <td>
     * <code><pre>INSERT INTO .. ON DUPLICATE KEY UPDATE [any-field] = [any-field]</pre></code>
     * </td>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#DB2}<br/>
     * {@link SQLDialect#HSQLDB}<br/>
     * {@link SQLDialect#ORACLE}<br/>
     * {@link SQLDialect#SQLSERVER}<br/>
     * {@link SQLDialect#SYBASE}</td>
     * <td><code><pre>MERGE INTO [dst]
     * USING ([values]) src
     * ON [dst.key] = [src.key]
     * WHEN NOT MATCHED THEN INSERT ..</pre></code></td>
     * </tr>
     * </table>
     * <p>
     * The <code>ON DUPLICATE KEY UPDATE</code> flag is mutually exclusive with
     * the <code>ON DUPLICATE KEY IGNORE</code> flag (see
     * {@link #onDuplicateKeyIgnore(boolean)}. Setting one will unset the other
     */
    @Support({ CUBRID, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    void onDuplicateKeyIgnore(boolean flag);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    <T> void addValueForUpdate(Field<T> field, T value);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    <T> void addValueForUpdate(Field<T> field, Field<T> value);

    /**
     * Add multiple values to the <code>ON DUPLICATE KEY UPDATE</code> clause of
     * this <code>INSERT</code> statement, where this is supported.
     * <p>
     * Please assure that key/value pairs have matching <code>&lt;T&gt;</code>
     * types. Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    void addValuesForUpdate(Map<? extends Field<?>, ?> map);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    void setReturning();

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    void setReturning(Identity<R, ? extends Number> identity);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    void setReturning(Field<?>... fields);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    void setReturning(Collection<? extends Field<?>> fields);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    R getReturnedRecord();

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>INSERT</code> statements for all SQL dialects
     */
    @Override
    @Support
    Result<R> getReturnedRecords();

}
