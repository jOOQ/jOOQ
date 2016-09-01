/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */

package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_3;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * A table to be used in queries
 *
 * @param <R> The record type associated with this table
 * @author Lukas Eder
 */
public interface Table<R extends Record> extends TableLike<R> {

    /**
     * Get the table catalog.
     */
    Catalog getCatalog();

    /**
     * Get the table schema.
     */
    Schema getSchema();

    /**
     * The name of this table.
     */
    String getName();

    /**
     * The comment given to the table.
     * <p>
     * If this <code>Table</code> is a generated table from your database, it
     * may provide its DDL comment through this method. All other table
     * expressions return the empty string <code>""</code> here, never
     * <code>null</code>.
     */
    String getComment();

    /**
     * The record type produced by this table.
     */
    RecordType<R> recordType();

    /**
     * The record type produced by this table.
     */
    Class<? extends R> getRecordType();

    /**
     * The table's record type as a UDT data type, in case the underlying
     * database supports table records as UDT records.
     */
    DataType<R> getDataType();

    /**
     * Create a new {@link Record} of this table's type.
     *
     * @see DSLContext#newRecord(Table)
     */
    R newRecord();

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
     * identities emulated by triggers cannot be formally detected.
     *
     * @return The table's <code>IDENTITY</code> information, or
     *         <code>null</code>, if no such information is available.
     */
    Identity<R, ?> getIdentity();
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
    Table<R> as(String alias);

    /**
     * Create an alias for this table and its fields.
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
    Table<R> as(String alias, String... fieldAliases);


    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <code><pre>
     * MY_TABLE.as("t1", f -> "prefix_" + f.getName());
     * </pre></code>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     */
    @Support
    Table<R> as(String alias, Function<? super Field<?>, ? extends String> aliasFunction);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <code><pre>
     * MY_TABLE.as("t1", (f, i) -> "column" + i);
     * </pre></code>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     */
    @Support
    Table<R> as(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction);


    /**
     * Create an alias for this table based on another table's name.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     *
     * @param otherTable The other table whose name this table is aliased with.
     * @return The table alias.
     */
    @Support
    Table<R> as(Table<?> otherTable);

    /**
     * Create an alias for this table based on another table's name.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderNameStyle()}. By default, table aliases are
     * quoted, and thus case-sensitive!
     *
     * @param otherTable The other table whose name this table is aliased with.
     * @param otherFields The other fields whose field name this table's fields
     *            are aliased with.
     * @return The table alias.
     */
    @Support
    Table<R> as(Table<?> otherTable, Field<?>... otherFields);


    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <code><pre>
     * MY_TABLE.as(MY_OTHER_TABLE, f -> MY_OTHER_TABLE.field(f));
     * </pre></code>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     */
    @Support
    Table<R> as(Table<?> otherTable, Function<? super Field<?>, ? extends Field<?>> aliasFunction);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <code><pre>
     * MY_TABLE.as("t1", (f, i) -> "column" + i);
     * </pre></code>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     */
    @Support
    Table<R> as(Table<?> otherTable, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> aliasFunction);


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
    TableOptionalOnStep<Record> join(TableLike<?> table, JoinType type);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(TableLike)}.
     *
     * @see #innerJoin(TableLike)
     */
    @Support
    TableOnStep<Record> join(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see #innerJoin(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> join(SQL sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see #innerJoin(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> join(String sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see #innerJoin(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> join(String sql, Object... bindings);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see #innerJoin(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> join(String sql, QueryPart... parts);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see #innerJoin(Name)
     */
    @Support
    @PlainSQL
    TableOnStep<Record> join(Name name);

    /**
     * <code>INNER JOIN</code> a table to this table.
     */
    @Support
    TableOnStep<Record> innerJoin(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> innerJoin(SQL sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> innerJoin(String sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> innerJoin(String sql, Object... bindings);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> innerJoin(String sql, QueryPart... parts);

    /**
     * <code>INNER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @Support
    TableOnStep<Record> innerJoin(Name name);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(TableLike)}.
     *
     * @see #leftOuterJoin(TableLike)
     */
    @Support
    TablePartitionByStep<Record> leftJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see #leftOuterJoin(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftJoin(SQL sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see #leftOuterJoin(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see #leftOuterJoin(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see #leftOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftJoin(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see #leftOuterJoin(Name)
     */
    @Support
    TablePartitionByStep<Record> leftJoin(Name name);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @Support
    TablePartitionByStep<Record> leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftOuterJoin(SQL sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftOuterJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftOuterJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see SQL
     */
    @Support
    TablePartitionByStep<Record> leftOuterJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(TableLike)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     *
     * @see #rightOuterJoin(TableLike)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TablePartitionByStep<Record> rightJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see #rightOuterJoin(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(SQL sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see #rightOuterJoin(String)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, Object...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see #rightOuterJoin(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, QueryPart...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see #rightOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(Name)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     * @see #rightOuterJoin(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TablePartitionByStep<Record> rightJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TablePartitionByStep<Record> rightOuterJoin(TableLike<?> table);

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
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(SQL sql);

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
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql);

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
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql, Object... bindings);

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
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TablePartitionByStep<Record> rightOuterJoin(Name name);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullOuterJoin(TableLike<?> table);

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
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(SQL sql);

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
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql);

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
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql, Object... bindings);

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
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullOuterJoin(Name name);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    Table<Record> crossJoin(TableLike<?> table);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
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
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    Table<Record> crossJoin(SQL sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
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
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    Table<Record> crossJoin(String sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
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
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    Table<Record> crossJoin(String sql, Object... bindings);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
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
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    Table<Record> crossJoin(String sql, QueryPart... parts);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     *
     * @see DSL#table(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    Table<Record> crossJoin(Name name);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @Support
    Table<Record> naturalJoin(TableLike<?> table);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalJoin(SQL sql);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalJoin(String sql);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @see DSL#table(Name)
     */
    @Support
    Table<Record> naturalJoin(Name name);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @Support
    Table<Record> naturalLeftOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(SQL sql);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(String sql);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @see DSL#table(Name)
     */
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(Name name);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Table<Record> naturalRightOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    Table<Record> naturalRightOuterJoin(SQL sql);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    Table<Record> naturalRightOuterJoin(String sql);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    Table<Record> naturalRightOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    Table<Record> naturalRightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @see DSL#table(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    Table<Record> naturalRightOuterJoin(Name name);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /**
     * <code>CROSS APPLY</code> a table to this table.
     */
    @Support({ POSTGRES_9_3 })
    Table<Record> crossApply(TableLike<?> table);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> crossApply(SQL sql);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> crossApply(String sql);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> crossApply(String sql, Object... bindings);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> crossApply(String sql, QueryPart... parts);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @Support({ POSTGRES_9_3 })
    Table<Record> crossApply(Name name);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     */
    @Support({ POSTGRES_9_3 })
    Table<Record> outerApply(TableLike<?> table);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> outerApply(SQL sql);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> outerApply(String sql);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> outerApply(String sql, Object... bindings);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    Table<Record> outerApply(String sql, QueryPart... parts);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @Support({ POSTGRES_9_3 })
    Table<Record> outerApply(Name name);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     */
    @Support({ MYSQL })
    TableOnStep<Record> straightJoin(TableLike<?> table);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(SQL sql);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(String sql);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(String sql, Object... bindings);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(String sql, QueryPart... parts);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @Support({ MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(Name name);

    // -------------------------------------------------------------------------
    // XXX: Convenience methods and synthetic methods
    // -------------------------------------------------------------------------

    /**
     * Create a predicate comparing records from self-joined tables.
     * <p>
     * This is a convenience method for self-joins, comparing complete records
     * between tables.
     * <p>
     * For example: <code><pre>
     * MyTable a = MY_TABLE.as("a");
     * MyTable b = MY_TABLE.as("b");
     *
     * DSL.using(configuration)
     *    .select()
     *    .from(a)
     *    .join(b).on(a.eq(b));
     * </pre></code>
     *
     * @see #equal(Table)
     */
    @Support
    Condition eq(Table<R> table);

    /**
     * Create a predicate comparing records from self-joined tables.
     * <p>
     * This is a convenience method for self-joins, comparing complete records
     * between tables.
     * <p>
     * For example:
     * <code><pre>
     * MyTable a = MY_TABLE.as("a");
     * MyTable b = MY_TABLE.as("b");
     *
     * DSL.using(configuration)
     *    .select()
     *    .from(a)
     *    .join(b).on(a.equal(b));
     * </pre></code>
     */
    @Support
    Condition equal(Table<R> table);

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Watch out! This is {@link Object#equals(Object)}, not a jOOQ DSL
     * feature!</strong>
     */
    @Override
    boolean equals(Object other);

    /**
     * Create a predicate comparing records from self-non-equi-joined tables.
     * This is a convenience method for self-joins, comparing complete records
     * between tables.
     * <p>
     * For example:
     * <code><pre>
     * MyTable a = MY_TABLE.as("a");
     * MyTable b = MY_TABLE.as("b");
     *
     * DSL.using(configuration)
     *    .select()
     *    .from(a)
     *    .join(b).on(a.ne(b));
     * </pre></code>
     *
     * @see #notEqual(Table)
     */
    @Support
    Condition ne(Table<R> table);

    /**
     * Create a predicate comparing records from self-non-equi-joined tables.
     * <p>
     * This is a convenience method for self-joins, comparing complete records
     * between tables.
     * <p>
     * For example:
     * <code><pre>
     * MyTable a = MY_TABLE.as("a");
     * MyTable b = MY_TABLE.as("b");
     *
     * DSL.using(configuration)
     *    .select()
     *    .from(a)
     *    .join(b).on(a.notEqual(b));
     * </pre></code>
     */
    @Support
    Condition notEqual(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX: Exotic and vendor-specific clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> useIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForGroupBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForGroupBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </pre></code>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForGroupBy(String... indexes);



























































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
    DivideByOnStep divideBy(Table<?> divisor);

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <code><pre>
     * -- Using LEFT SEMI JOIN
     * FROM A
     *     LEFT SEMI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE EXISTS
     * FROM A
     * WHERE EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </pre></code>
     */
    @Support
    TableOnStep<R> leftSemiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <code><pre>
     * -- Using LEFT ANTI JOIN
     * FROM A
     *     LEFT ANTI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE NOT EXISTS
     * FROM A
     * WHERE NOT EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </pre></code>
     */
    @Support
    TableOnStep<R> leftAntiJoin(TableLike<?> table);









































































    // ------------------------------------------------------------------------
    // [#5518] Record method inversions, e.g. for use as method references
    // ------------------------------------------------------------------------

    /**
     * The inverse operation of {@link Record#into(Table)}.
     * <p>
     * This method can be used in its method reference form conveniently on a
     * generated table, for instance, when mapping records in a stream:
     * <code><pre>
     * DSL.using(configuration)
     *    .fetch("select * from t")
     *    .stream()
     *    .map(MY_TABLE::into)
     *    .forEach(System.out::println);
     * </pre></code>
     */
    R from(Record record);
}
