/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq;

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.ORACLE11G;
import static org.jooq.SQLDialect.ORACLE12C;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * A table to be used in queries
 *
 * @param <R> The record type associated with this table
 * @author Lukas Eder
 */
@State(
    name = "Table",
    aliases = {
        "AliasedTable",
        "JoinedTable",
        "PivotTable",
        "DividedTable",
        "UnnestedTable"
    },
    terminal = true
)
public interface Table<R extends Record> extends TableLike<R> {

    /**
     * Get the table schema.
     */
    Schema getSchema();

    /**
     * The name of this table.
     */
    String getName();

    /**
     * The record type produced by this table.
     */
    RecordType<R> recordType();

    /**
     * The record type produced by this table.
     */
    Class<? extends R> getRecordType();

    /**
     * Retrieve the table's <code>IDENTITY</code> information, if available.
     * <p>
     * With SQL:2003, the concept of <code>IDENTITY</code> columns was
     * introduced in most RDBMS. These are special kinds of columns that have
     * auto-increment functionality when <code>INSERT</code> statements are
     * performed.
     * <p>
     * An <code>IDENTITY</code> column is usually part of the
     * <code>PRIMARY KEY</code> or of a <code>UNIQUE KEY</code> in the table,
     * although in some RDBMS, this is not required. There can only be at most
     * one <code>IDENTITY</code> column.
     * <p>
     * Note: Unfortunately, this is not supported in the Oracle dialect, where
     * identities simulated by triggers cannot be formally detected.
     *
     * @return The table's <code>IDENTITY</code> information, or
     *         <code>null</code>, if no such information is available.
     */
    Identity<R, ? extends Number> getIdentity();
    /**
     * Retrieve the table's primary key
     *
     * @return The primary key. This is never <code>null</code> for an updatable
     *         table.
     */
    UniqueKey<R> getPrimaryKey();

    /**
     * A "version" field holding record version information used for optimistic
     * locking
     * <p>
     * jOOQ supports optimistic locking in {@link UpdatableRecord#store()} and
     * {@link UpdatableRecord#delete()} if
     * {@link Settings#isExecuteWithOptimisticLocking()} is enabled. Optimistic
     * locking is performed in a single <code>UPDATE</code> or
     * <code>DELETE</code> statement if tables provide a "version" or
     * "timestamp" field, or in two steps using an additional
     * <code>SELECT .. FOR UPDATE</code> statement otherwise.
     * <p>
     * This method is overridden in generated subclasses if their corresponding
     * tables have been configured accordingly. A table may have both a
     * "version" and a "timestamp" field.
     *
     * @return The "version" field, or <code>null</code>, if this table has no
     *         "version" field.
     * @see #getRecordTimestamp()
     * @see UpdatableRecord#store()
     * @see UpdatableRecord#delete()
     * @see Settings#isExecuteWithOptimisticLocking()
     */
    TableField<R, ? extends Number> getRecordVersion();

    /**
     * A "timestamp" field holding record timestamp information used for
     * optimistic locking
     * <p>
     * jOOQ supports optimistic locking in {@link UpdatableRecord#store()} and
     * {@link UpdatableRecord#delete()} if
     * {@link Settings#isExecuteWithOptimisticLocking()} is enabled. Optimistic
     * locking is performed in a single <code>UPDATE</code> or
     * <code>DELETE</code> statement if tables provide a "version" or
     * "timestamp" field, or in two steps using an additional
     * <code>SELECT .. FOR UPDATE</code> statement otherwise.
     * <p>
     * This method is overridden in generated subclasses if their corresponding
     * tables have been configured accordingly. A table may have both a
     * "version" and a "timestamp" field.
     *
     * @return The "timestamp" field, or <code>null</code>, if this table has no
     *         "timestamp" field.
     * @see #getRecordVersion()
     * @see UpdatableRecord#store()
     * @see UpdatableRecord#delete()
     * @see Settings#isExecuteWithOptimisticLocking()
     */
    TableField<R, ? extends java.util.Date> getRecordTimestamp();

    /**
     * Retrieve all of the table's unique keys.
     *
     * @return All keys. This is never <code>null</code>. This is never empty
     *         for a {@link Table} with a {@link Table#getPrimaryKey()}. This
     *         method returns an unmodifiable list.
     */
    List<UniqueKey<R>> getKeys();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of a specific table, referencing
     * a this table.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return Some other table's <code>FOREIGN KEY</code>'s towards an this
     *         table. This is never <code>null</code>. This method returns an
     *         unmodifiable list.
     */
    <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other);

    /**
     * Get the list of <code>FOREIGN KEY</code>'s of this table
     *
     * @return This table's <code>FOREIGN KEY</code>'s. This is never
     *         <code>null</code>.
     */
    List<ForeignKey<R, ?>> getReferences();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of this table, referencing a
     * specific table.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return This table's <code>FOREIGN KEY</code>'s towards an other table.
     *         This is never <code>null</code>.
     */
    <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other);

    // -------------------------------------------------------------------------
    // XXX: Aliasing clauses
    // -------------------------------------------------------------------------

    /**
     * Create an alias for this table.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     *
     * @param alias The alias name
     * @return The table alias
     */
    @Support
    @Transition(
        name = "AS",
        to = "AliasedTable",
        args = "String"
    )
    Table<R> as(String alias);

    /**
     * Create an alias for this table and its fields
     * <p>
     * Note that the case-sensitivity of the returned table and columns depends
     * on {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent: <code><pre>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </pre></code>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent: <code><pre>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT 1, 2
     * ) AS t(a, b)
     *
     * -- Using UNION ALL to produce column names (e.g. MySQL)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT null a, null b FROM DUAL WHERE 1 = 0
     *   UNION ALL
     *   SELECT 1, 2 FROM DUAL
     * ) t
     * </pre></code>
     *
     * @param alias The alias name
     * @param fieldAliases The field aliases. Excess aliases are ignored,
     *            missing aliases will be substituted by this table's field
     *            names.
     * @return The table alias
     */
    @Support
    @Transition(
        name = "AS",
        to = "AliasedTable",
        args = {
            "String",
            "String+"
        }
    )
    Table<R> as(String alias, String... fieldAliases);

    // -------------------------------------------------------------------------
    // XXX: JOIN clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Join a table to this table using a {@link JoinType}
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link TableOnStep#on(Condition...)} or
     * {@link TableOnStep#using(Field...)} clause is required. If it is required
     * but omitted, a {@link DSL#trueCondition()}, i.e. <code>1 = 1</code>
     * condition will be rendered
     */
    @Support
    TableOptionalOnStep join(TableLike<?> table, JoinType type);

    /**
     * <code>INNER JOIN</code> a table to this table.
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql, Object... bindings);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "JOIN",
        args = "Table"
    )
    TableOnStep join(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "LEFT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep leftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "RIGHT OUTER JOIN",
        args = "Table"
    )
    TablePartitionByStep rightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ DB2, FIREBIRD, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ DB2, FIREBIRD, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ DB2, FIREBIRD, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ DB2, FIREBIRD, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "FULL OUTER JOIN",
        args = "Table"
    )
    TableOnStep fullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     */
    @Support
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(TableLike<?> table);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql, Object... bindings);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "CROSS JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> crossJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(TableLike<?> table);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "NATURAL JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    @Transition(
        name = "NATURAL LEFT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalLeftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "NATURAL RIGHT OUTER JOIN",
        args = "Table",
        to = "JoinedTable"
    )
    Table<Record> naturalRightOuterJoin(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX: Exotic and vendor-specific clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Specify a SQL Server style table hint for query optimisation
     * <p>
     * This makes sense only on an actual database table or view, not on other
     * composed table sources.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").with("READUNCOMMITTED")
     *       .fetch();
     * </pre></code>
     * <p>
     * For {@link SQLDialect#ORACLE} style hints, see
     * {@link SelectFromStep#hint(String)} and {@link SelectQuery#addHint(String)}
     *
     * @see <a
     *      href="http://msdn.microsoft.com/en-us/library/ms187373.aspx">http://msdn.microsoft.com/en-us/library/ms187373.aspx</a>
     * @see SelectFromStep#hint(String)
     * @see SelectQuery#addHint(String)
     */
    @Support({ SQLSERVER, SYBASE })
    @Transition(
        name = "WITH",
        args = "String"
    )
    Table<R> with(String hint);

    /**
     * Create a new <code>TABLE</code> reference from this table, pivoting it
     * into another form
     * <p>
     * This has been observed to work with
     * <ul>
     * <li> {@link SQLDialect#ORACLE11G} upwards</li>
     * <li> {@link SQLDialect#SQLSERVER} (not yet officially supported)</li>
     * <li>Other dialects by using some means of simulation (not yet officially
     * supported)</li>
     * </ul>
     *
     * @param aggregateFunctions The aggregate functions used for pivoting.
     * @return A DSL object to create the <code>PIVOT</code> expression
     */
    @Support({ ORACLE11G, ORACLE12C })
    @Transition(
        name = "PIVOT",
        args = "Field+"
    )
    PivotForStep pivot(Field<?>... aggregateFunctions);

    /**
     * Create a new <code>TABLE</code> reference from this table, pivoting it
     * into another form
     * <p>
     * For more details, see {@link #pivot(Field...)}
     *
     * @param aggregateFunctions The aggregate functions used for pivoting.
     * @return A DSL object to create the <code>PIVOT</code> expression
     * @see #pivot(Field...)
     */
    @Support({ ORACLE11G, ORACLE12C })
    @Transition(
        name = "PIVOT",
        args = "Field+"
    )
    PivotForStep pivot(Collection<? extends Field<?>> aggregateFunctions);

    /**
     * Create a new <code>TABLE</code> reference from this table, applying
     * relational division.
     * <p>
     * Relational division is the inverse of a cross join operation. The
     * following is an approximate definition of a relational division:
     * <code><pre>
     * Assume the following cross join / cartesian product
     * C = A × B
     *
     * Then it can be said that
     * A = C ÷ B
     * B = C ÷ A
     * </pre></code>
     * <p>
     * With jOOQ, you can simplify using relational divisions by using the
     * following syntax: <code><pre>
     * C.divideBy(B).on(C.ID.equal(B.C_ID)).returning(C.TEXT)
     * </pre></code>
     * <p>
     * The above roughly translates to <code><pre>
     * SELECT DISTINCT C.TEXT FROM C "c1"
     * WHERE NOT EXISTS (
     *   SELECT 1 FROM B
     *   WHERE NOT EXISTS (
     *     SELECT 1 FROM C "c2"
     *     WHERE "c2".TEXT = "c1".TEXT
     *     AND "c2".ID = B.C_ID
     *   )
     * )
     * </pre></code>
     * <p>
     * Or in plain text: Find those TEXT values in C whose ID's correspond to
     * all ID's in B. Note that from the above SQL statement, it is immediately
     * clear that proper indexing is of the essence. Be sure to have indexes on
     * all columns referenced from the <code>on(...)</code> and
     * <code>returning(...)</code> clauses.
     * <p>
     * For more information about relational division and some nice, real-life
     * examples, see
     * <ul>
     * <li><a
     * href="http://en.wikipedia.org/wiki/Relational_algebra#Division">http
     * ://en.wikipedia.org/wiki/Relational_algebra#Division</a></li>
     * <li><a href=
     * "http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/"
     * >http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-
     * sql-of-relational-division/</a></li>
     * </ul>
     * <p>
     * This has been observed to work with all dialects
     */
    @Support
    @Transition(
        name = "DIVIDE BY",
        args = "Table"
    )
    DivideByOnStep divideBy(Table<?> divisor);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN SCN",
        args = "Field"
    )
    VersionsBetweenAndStep<R, Number> versionsBetweenScn(Number scn);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN SCN",
        args = "Field"
    )
    VersionsBetweenAndStep<R, Number> versionsBetweenScn(Field<? extends Number> scn);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN SCN MINVALUE"
    )
    VersionsBetweenAndStep<R, Number> versionsBetweenScnMinvalue();

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN TIMESTAMP",
        args = "Field"
    )
    VersionsBetweenAndStep<R, Timestamp> versionsBetweenTimestamp(Timestamp timestamp);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN TIMESTAMP",
        args = "Field"
    )
    VersionsBetweenAndStep<R, Timestamp> versionsBetweenTimestamp(Field<Timestamp> timestamp);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback versions query clause from
     * this table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "VERSIONS BETWEEN TIMESTAMP MINVALUE"
    )
    VersionsBetweenAndStep<R, Timestamp> versionsBetweenTimestampMinvalue();

    /**
     * Create an {@link SQLDialect#ORACLE} flashback query clause from this
     * table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "AS OF SCN",
        args = "Field"
    )
    Table<R> asOfScn(Number scn);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback query clause from this
     * table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "AS OF SCN",
        args = "Field"
    )
    Table<R> asOfScn(Field<? extends Number> scn);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback query clause from this
     * table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "AS OF TIMESTAMP",
        args = "Field"
    )
    Table<R> asOfTimestamp(Timestamp timestamp);

    /**
     * Create an {@link SQLDialect#ORACLE} flashback query clause from this
     * table.
     */
    @Support({ ORACLE })
    @Transition(
        name = "AS OF TIMESTAMP",
        args = "Field"
    )
    Table<R> asOfTimestamp(Field<Timestamp> timestamp);
}
