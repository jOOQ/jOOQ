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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES_9_5;
// ...
// ...

import java.util.Collection;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .values(value3, value4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertOnDuplicateStep<R extends Record> extends InsertReturningStep<R> {

    /**
     * Add an <code>ON CONFLICT</code> clause to this insert query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep<R> onConflict(Field<?>... keys);

    /**
     * Add an <code>ON CONFLICT</code> clause to this insert query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep<R> onConflict(Collection<? extends Field<?>> keys);

    /**
     * Add an <code>ON CONFLICT DO NOTHING</code> clause to this insert query.
     */
    @Support({ POSTGRES_9_5 })
    InsertFinalStep<R> onConflictDoNothing();

    /**
     * Add an <code>ON DUPLICATE KEY UPDATE</code> clause to this insert query.
     * <p>
     * This will try to <code>INSERT</code> a record. If there is a primary key
     * or unique key in this <code>INSERT</code> statement's affected table that
     * matches the value being inserted, then the <code>UPDATE</code> clause is
     * executed instead.
     * <p>
     * MySQL and CUBRID natively implements this type of clause. jOOQ can
     * emulate this clause using a <code>MERGE</code> statement on some other
     * databases. The conditions for a RDBMS to emulate this clause are:
     * <ul>
     * <li>The <code>INSERT</code> statement's table is a
     * {@link Table} with a {@link Table#getPrimaryKey()}</li>
     * <li>The RDBMS supports the <code>MERGE</code> clause (see
     * {@link DSLContext#mergeInto(Table)}).</li>
     * </ul>
     * <p>
     * These are the dialects that fulfill the above requirements:
     */
    @Support({ CUBRID, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    InsertOnDuplicateSetStep<R> onDuplicateKeyUpdate();

    /**
     * Add an <code>ON DUPLICATE KEY IGNORE</code> clause to this insert query.
     * <p>
     * This will try to <code>INSERT</code> a record. If there is a primary key
     * or unique key in this <code>INSERT</code> statement's affected table that
     * matches the value being inserted, then the <code>INSERT</code> statement
     * is ignored.
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
     */
    @Support
    InsertFinalStep<R> onDuplicateKeyIgnore();
}
