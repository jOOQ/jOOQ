/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq;

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_5;
import static org.jooq.SQLDialect.SQLITE;
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
    @Support({ CUBRID, DB2, HSQLDB, INFORMIX, MARIADB, MYSQL, ORACLE, POSTGRES_9_5, SQLSERVER, SYBASE })
    void onDuplicateKeyUpdate(boolean flag);

    /**
     * Whether an <code>ON DUPLICATE KEY IGNORE</code> clause should be added to
     * this <code>INSERT</code> statement.
     * <p>
     * This clause is not actually supported in this form by any database, but
     * can be emulated as such:
     * <table border="1">
     * <tr>
     * <th>Dialect</th>
     * <th>Emulation</th>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#MYSQL} and {@link SQLDialect#MARIADB}</td>
     * <td><code><pre>INSERT IGNORE INTO ..</pre></code></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#POSTGRES_9_5}</td>
     * <td><code><pre>INSERT INTO .. ON CONFLICT DO NOTHING</pre></code></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#CUBRID}</td>
     * <td>
     * <code><pre>INSERT INTO .. ON DUPLICATE KEY UPDATE [any-field] = [any-field]</pre></code>
     * </td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#DB2}<br/>
     * {@link SQLDialect#HSQLDB}<br/>
     * {@link SQLDialect#ORACLE}<br/>
     * {@link SQLDialect#SQLSERVER}<br/>
     * {@link SQLDialect#SYBASE}</td>
     * <td><code><pre>MERGE INTO [dst]
     * USING ([values])
     * ON [dst.key] = [values.key]
     * WHEN NOT MATCHED THEN INSERT ..</pre></code></td>
     * </tr>
     * <tr>
     * <td>All the others</td>
     * <td><code><pre>INSERT INTO [dst] ( ... )
     * SELECT [values]
     * WHERE NOT EXISTS (
     *   SELECT 1
     *   FROM [dst]
     *   WHERE [dst.key] = [values.key]
     * )</pre></code></td>
     * </tr>
     * </table>
     * <p>
     * The <code>ON DUPLICATE KEY UPDATE</code> flag is mutually exclusive with
     * the <code>ON DUPLICATE KEY IGNORE</code> flag (see
     * {@link #onDuplicateKeyIgnore(boolean)}. Setting one will unset the other
     */
    @Support
    void onDuplicateKeyIgnore(boolean flag);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ CUBRID, DB2, HSQLDB, INFORMIX, MARIADB, MYSQL, ORACLE, POSTGRES_9_5, SQLSERVER, SYBASE })
    <T> void addValueForUpdate(Field<T> field, T value);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ CUBRID, DB2, HSQLDB, INFORMIX, MARIADB, MYSQL, ORACLE, POSTGRES_9_5, SQLSERVER, SYBASE })
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
    @Support({ CUBRID, DB2, HSQLDB, INFORMIX, MARIADB, MYSQL, ORACLE, POSTGRES_9_5, SQLSERVER, SYBASE })
    void addValuesForUpdate(Map<? extends Field<?>, ?> map);

    /**
     * Set an empty record with the <code>DEFAULT VALUES</code> clause.
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void setDefaultValues();

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement.
     */
    @Support
    void setSelect(Field<?>[] fields, Select<?> select);

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
    void setReturning(Identity<R, ?> identity);

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
