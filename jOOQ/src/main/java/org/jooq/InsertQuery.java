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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_5;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

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
     * Whether a <code>ON CONFLICT</code> clause should be added to
     * this <code>INSERT</code> statement.
     * <p>
     * When setting this flag to <code>true</code>, be sure to also add values
     * "for update" using the {@link #addValueForUpdate(Field, Field)} methods.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ POSTGRES_9_5 })
    void onConflict(Field<?>... fields);

    /**
     * Whether a <code>ON CONFLICT</code> clause should be added to
     * this <code>INSERT</code> statement.
     * <p>
     * When setting this flag to <code>true</code>, be sure to also add values
     * "for update" using the {@link #addValueForUpdate(Field, Field)} methods.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ POSTGRES_9_5 })
    void onConflict(Collection<? extends Field<?>> fields);

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
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
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
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T> void addValueForUpdate(Field<T> field, T value);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
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
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    void addValuesForUpdate(Map<? extends Field<?>, ?> map);

    /**
     * Adds new conditions to the query, connecting them to existing conditions
     * with {@link Operator#AND}.
     * <p>
     * This is for use with {@link SQLDialect#POSTGRES}'s
     * {@link #onConflict(Field...)} clause.
     *
     * @param conditions The condition
     */
    @Support({ POSTGRES_9_5 })
    void addConditions(Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with {@link Operator#AND}.
     * <p>
     * This is for use with {@link SQLDialect#POSTGRES}'s
     * {@link #onConflict(Field...)} clause.
     *
     * @param conditions The condition
     */
    @Support({ POSTGRES_9_5 })
    void addConditions(Collection<? extends Condition> conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator.
     * <p>
     * This is for use with {@link SQLDialect#POSTGRES}'s
     * {@link #onConflict(Field...)} clause.
     *
     * @param conditions The condition
     */
    @Support({ POSTGRES_9_5 })
    void addConditions(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator.
     * <p>
     * This is for use with {@link SQLDialect#POSTGRES}'s
     * {@link #onConflict(Field...)} clause.
     *
     * @param conditions The condition
     */
    @Support({ POSTGRES_9_5 })
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

    /**
     * Set an empty record with the <code>DEFAULT VALUES</code> clause.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
