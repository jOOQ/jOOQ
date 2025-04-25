/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq;

// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <pre><code>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .values(value3, value4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </code></pre>
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface InsertOnDuplicateStep<R extends Record> extends InsertReturningStep<R> {

    /**
     * Add a <code>ON CONFLICT ON CONSTRAINT</code> clause to this INSERT statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnConflictDoUpdateStep<R> onConflictOnConstraint(Constraint constraint);

    /**
     * Add a <code>ON CONFLICT ON CONSTRAINT</code> clause to this INSERT statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnConflictDoUpdateStep<R> onConflictOnConstraint(Name constraint);

    /**
     * Add a <code>ON CONFLICT ON CONSTRAINT</code> clause to this INSERT statement.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnConflictDoUpdateStep<R> onConflictOnConstraint(UniqueKey<R> constraint);

    /**
     * Add an <code>ON CONFLICT</code> clause to this INSERT statement.
     * <p>
     * Only {@link SQLDialect#POSTGRES} and {@link SQLDialect#SQLITE} have
     * native support for this clause. The other dialects can emulate it using
     * <code>MERGE</code>, if table meta data is available.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnConflictWhereIndexPredicateStep<R> onConflict(Field<?>... keys);

    /**
     * Add an <code>ON CONFLICT</code> clause to this INSERT statement.
     * <p>
     * Only {@link SQLDialect#POSTGRES} and {@link SQLDialect#SQLITE} have
     * native support for this clause. The other dialects can emulate it using
     * <code>MERGE</code>, if table meta data is available.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnConflictWhereIndexPredicateStep<R> onConflict(Collection<? extends Field<?>> keys);

    /**
     * Add an <code>ON CONFLICT DO NOTHING</code> clause to this INSERT statement.
     * <p>
     * Only {@link SQLDialect#POSTGRES} and {@link SQLDialect#SQLITE} have
     * native support for this clause. The other dialects can emulate it using
     * <code>MERGE</code>, if table meta data is available.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertReturningStep<R> onConflictDoNothing();

    /**
     * Add an <code>ON DUPLICATE KEY UPDATE</code> clause to this INSERT statement.
     * <p>
     * This will try to <code>INSERT</code> a record. If there is a primary key
     * or unique key in this <code>INSERT</code> statement's affected table that
     * matches the value being inserted, then the <code>UPDATE</code> clause is
     * executed instead.
     * <p>
     * MySQL natively implements this type of clause. jOOQ can
     * emulate this clause using a <code>MERGE</code> statement on some other
     * databases. The conditions for a RDBMS to emulate this clause are:
     * <ul>
     * <li>The <code>INSERT</code> statement's table is a
     * {@link Table} with a {@link Table#getPrimaryKey()}</li>
     * <li>The RDBMS supports the <code>MERGE</code> clause (see
     * {@link DSLContext#mergeInto(Table)}).</li>
     * </ul>
     * <p>
     * H2 supports this clause in MySQL mode.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    InsertOnDuplicateSetStep<R> onDuplicateKeyUpdate();

    /**
     * Add an <code>ON DUPLICATE KEY IGNORE</code> clause to this INSERT statement.
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
     * <td><pre><code>INSERT IGNORE INTO …</code></pre></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#POSTGRES_9_5} and {@link SQLDialect#SQLITE}</td>
     * <td><pre><code>INSERT INTO … ON CONFLICT DO NOTHING</code></pre></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#DB2}<br>
     * {@link SQLDialect#HSQLDB}<br>
     * {@link SQLDialect#ORACLE}<br>
     * {@link SQLDialect#SQLSERVER}<br>
     * {@link SQLDialect#SYBASE}</td>
     * <td><pre><code>MERGE INTO [dst]
     * USING ([values])
     * ON [dst.key] = [values.key]
     * WHEN NOT MATCHED THEN INSERT ..</code></pre></td>
     * </tr>
     * <tr>
     * <td>All the others</td>
     * <td><pre><code>INSERT INTO [dst] ( ... )
     * SELECT [values]
     * WHERE NOT EXISTS (
     *   SELECT 1
     *   FROM [dst]
     *   WHERE [dst.key] = [values.key]
     * )</code></pre></td>
     * </tr>
     * </table>
     */
    @NotNull @CheckReturnValue
    @Support
    InsertReturningStep<R> onDuplicateKeyIgnore();
}
