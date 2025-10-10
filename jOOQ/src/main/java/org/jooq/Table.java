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
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.TableOptions.TableType;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import org.jooq.impl.QOM.JoinHint;
import org.jooq.impl.QOM.TableAlias;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A table.
 * <p>
 * Like {@link Field}, a {@link Table} is a basic building block of any
 * {@link Query}, as they all operate on at least one table. There are many
 * different types of tables, including:
 * <p>
 * <ul>
 * <li>Generated table or view references</li>
 * <li>Plain SQL tables created with {@link DSL#table(String)}</li>
 * <li>Table references created with {@link DSL#table(Name)}</li>
 * <li>Derived tables created with {@link DSL#table(Select)}</li>
 * <li>Join expressions created e.g. with {@link Table#join(TableLike)}</li>
 * <li>Common table expressions ({@link CommonTableExpression})</li>
 * <li>Unnested arrays referenced through {@link DSL#unnest(Field)} and
 * overloads</li>
 * <li>Table valued functions as provided by the code generator</li>
 * <li>Etc.</li>
 * </ul>
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR) // Table reference
 *    .fetch();
 * </code></pre>
 * <p>
 * Instances can be created using {@link DSL#table(Name)} and overloads.
 * <p>
 * <h3>Using table references as field expressions</h3>
 * <p>
 * Table references can be used like {@link Field} in queries. This includes:
 * <ul>
 * <li>A {@link GroupField} is an expression that is used in a {@link Select}
 * query's <code>GROUP BY</code> clause.</li>
 * <li>A {@link SelectField} is an expression that is used in a {@link Select}
 * query's <code>SELECT</code> clause, or in a DML query's
 * <code>RETURNING</code> clause, such as <code>INSERT … RETURNING</code>.</li>
 * </ul>
 * <p>
 * Other types of {@link Table} cannot be used this way, even if the type system
 * cannot prevent this.
 *
 * @param <R> The record type associated with this table
 * @author Lukas Eder
 */
public interface Table<R extends Record>
extends
    TableLike<R>,
    RecordQualifier<R>,
    GroupField,
    SelectField<R>
{

    /**
     * Get the table type.
     */
    @NotNull
    TableType getTableType();

    /**
     * Get the table options.
     */
    @NotNull
    TableOptions getOptions();

    /**
     * The record type produced by this table.
     */
    @NotNull
    RecordType<R> recordType();

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
    @Nullable
    Identity<R, ?> getIdentity();

    /**
     * Retrieve the table's primary key
     *
     * @return The primary key. This is never <code>null</code> for an updatable
     *         table.
     */
    @Nullable
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
     * <code>SELECT … FOR UPDATE</code> statement otherwise.
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
    @Nullable
    TableField<R, ?> getRecordVersion();

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
     * <code>SELECT … FOR UPDATE</code> statement otherwise.
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
    @Nullable
    TableField<R, ?> getRecordTimestamp();

    /**
     * Retrieve all of the table's indexes.
     *
     * @return All indexes. This is never <code>null</code>.
     */
    @NotNull
    List<Index> getIndexes();

    /**
     * Retrieve all of the table's primary and unique keys.
     *
     * @return All keys. This is never <code>null</code>. This is never empty
     *         for a {@link Table} with a {@link Table#getPrimaryKey()}.
     */
    @NotNull
    List<UniqueKey<R>> getKeys();

    /**
     * Retrieve all of the table's unique keys.
     *
     * @return All keys. This is never <code>null</code>.
     */
    @NotNull
    List<UniqueKey<R>> getUniqueKeys();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of a specific table, referencing
     * a this table.
     * <p>
     * This will recurse into joined tables.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return Some other table's <code>FOREIGN KEY</code>'s towards an this
     *         table. This is never <code>null</code>.
     */
    @NotNull
    <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other);

    /**
     * Get the list of <code>FOREIGN KEY</code>'s of this table
     *
     * @return This table's <code>FOREIGN KEY</code>'s. This is never
     *         <code>null</code>.
     */
    @NotNull
    List<ForeignKey<R, ?>> getReferences();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of this table, referencing a
     * specific table.
     * <p>
     * This will recurse into joined tables.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return This table's <code>FOREIGN KEY</code>'s towards an other table.
     *         This is never <code>null</code>.
     */
    @NotNull
    <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other);

    /**
     * Get a list of <code>CHECK</code> constraints of this table.
     */
    @NotNull
    List<Check<R>> getChecks();












































    // -------------------------------------------------------------------------
    // XXX: Expressions based on this table
    // -------------------------------------------------------------------------

    /**
     * Create a qualified asterisk expression from this table
     * (<code>table.*</code>) for use with <code>SELECT</code>.
     * <p>
     * When using an asterisk, jOOQ will let the database server define the
     * order of columns, as well as which columns are included in the result
     * set. If using jOOQ with generated code, this may conflict with the column
     * set and its ordering as defined at code generation time, meaning columns
     * may be in a different order, there may be fewer or more columns than
     * expected. It is usually better to list columns explicitly.
     *
     * @see DSL#asterisk()
     */
    @NotNull
    @Support
    QualifiedAsterisk asterisk();

    // -------------------------------------------------------------------------
    // XXX: Aliasing clauses
    // -------------------------------------------------------------------------

    /**
     * Create an alias for this table.
     * <p>
     * This method works both to alias the table as well as alias the table in
     * its {@link SelectField} form via the {@link SelectField#as(String)}
     * override. In order to alias only the projected table expression, use
     * {@link DSL#field(SelectField)} to wrap this table into a {@link Field}
     * first.
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderQuotedNames()}. By default, table aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     *
     * @param alias The alias name
     * @return The table alias
     */
    @Override
    @NotNull
    @Support
    Table<R> as(String alias);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent: <pre><code>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </code></pre>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent: <pre><code>
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
     * </code></pre>
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table and columns depends
     * on {@link Settings#getRenderQuotedNames()}. By default, table aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     *
     * @param alias The alias name
     * @param fieldAliases The field aliases. Excess aliases are ignored,
     *            missing aliases will be substituted by this table's field
     *            names.
     * @return The table alias
     */
    @NotNull
    @Support
    Table<R> as(String alias, String... fieldAliases);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent: <pre><code>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </code></pre>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent: <pre><code>
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
     * </code></pre>
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table and columns depends
     * on {@link Settings#getRenderQuotedNames()}. By default, table aliases are
     * quoted, and thus case-sensitive in many SQL dialects!
     *
     * @param alias The alias name
     * @param fieldAliases The field aliases. Excess aliases are ignored,
     *            missing aliases will be substituted by this table's field
     *            names.
     * @return The table alias
     */
    @NotNull
    @Support
    Table<R> as(String alias, Collection<? extends String> fieldAliases);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", f -&gt;"prefix_" + f.getName());
     * </code></pre>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(String alias, Function<? super Field<?>, ? extends String> aliasFunction);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(String, String...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", (f, i) -&gt;"column" + i);
     * </code></pre>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction);

    /**
     * Create an alias for this table.
     * <p>
     * This method works both to alias the table as well as alias the table in
     * its {@link SelectField} form via the {@link SelectField#as(String)}
     * override. In order to alias only the projected table expression, use
     * {@link DSL#field(SelectField)} to wrap this table into a {@link Field}
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderQuotedNames()} and the {@link Name}. By default,
     * table aliases are quoted, and thus case-sensitive in many SQL dialects -
     * use {@link DSL#unquotedName(String...)} for case-insensitive aliases.
     *
     * @param alias The alias name. If {@link Name#getName()} is qualified, then
     *            the {@link Name#last()} part will be used.
     * @return The table alias
     */
    @Override
    @NotNull
    @Support
    Table<R> as(Name alias);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent:
     *
     * <pre>
     * <code>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </code>
     * </pre>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent:
     *
     * <pre>
     * <code>
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
     * </code>
     * </pre>
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderQuotedNames()} and the {@link Name}. By default,
     * table aliases are quoted, and thus case-sensitive in many SQL dialects -
     * use {@link DSL#unquotedName(String...)} for case-insensitive aliases.
     *
     * @param alias The alias name. If {@link Name#getName()} is qualified, then
     *            the {@link Name#last()} part will be used.
     * @param fieldAliases The field aliases. If {@link Name#getName()} is
     *            qualified, then the {@link Name#last()} part will be used.
     *            Excess aliases are ignored, missing aliases will be
     *            substituted by this table's field names.
     * @return The table alias
     */
    @NotNull
    @Support
    Table<R> as(Name alias, Name... fieldAliases);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * <h5>Derived column lists for table references</h5>
     * <p>
     * Note, not all databases support derived column lists for their table
     * aliases. On the other hand, some databases do support derived column
     * lists, but only for derived tables. jOOQ will try to turn table
     * references into derived tables to make this syntax work. In other words,
     * the following statements are equivalent:
     *
     * <pre>
     * <code>
     * -- Using derived column lists to rename columns (e.g. Postgres)
     * SELECT t.a, t.b
     * FROM my_table t(a, b)
     *
     * -- Nesting table references within derived tables (e.g. SQL Server)
     * SELECT t.a, t.b
     * FROM (
     *   SELECT * FROM my_table
     * ) t(a, b)
     * </code>
     * </pre>
     * <p>
     * <h5>Derived column lists for derived tables</h5>
     * <p>
     * Other databases may not support derived column lists at all, but they do
     * support common table expressions. The following statements are
     * equivalent:
     *
     * <pre>
     * <code>
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
     * </code>
     * </pre>
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     * <p>
     * Note that the case-sensitivity of the returned table depends on
     * {@link Settings#getRenderQuotedNames()} and the {@link Name}. By default,
     * table aliases are quoted, and thus case-sensitive in many SQL dialects -
     * use {@link DSL#unquotedName(String...)} for case-insensitive aliases.
     *
     * @param alias The alias name. If {@link Name#getName()} is qualified, then
     *            the {@link Name#last()} part will be used.
     * @param fieldAliases The field aliases. If {@link Name#getName()} is
     *            qualified, then the {@link Name#last()} part will be used.
     *            Excess aliases are ignored, missing aliases will be
     *            substituted by this table's field names.
     * @return The table alias
     */
    @NotNull
    @Support
    Table<R> as(Name alias, Collection<? extends Name> fieldAliases);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(Name, Name...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", f -&gt;"prefix_" + f.getName());
     * </code></pre>
     *
     * @param alias The alias name. If {@link Name#getName()} is qualified, then
     *            the {@link Name#last()} part will be used.
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(Name alias, Function<? super Field<?>, ? extends Name> aliasFunction);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(Name, Name...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", (f, i) -&gt;"column" + i);
     * </code></pre>
     *
     * @param alias The alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(Name alias, BiFunction<? super Field<?>, ? super Integer, ? extends Name> aliasFunction);

    /**
     * Create an alias for this table based on another table's name.
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     *
     * @param otherTable The other table whose name this table is aliased with.
     * @return The table alias.
     */
    @NotNull
    @Support
    Table<R> as(Table<?> otherTable);

    /**
     * Create an alias for this table based on another table's name.
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     *
     * @param otherTable The other table whose name this table is aliased with.
     * @param otherFields The other fields whose field name this table's fields
     *            are aliased with.
     * @return The table alias.
     */
    @NotNull
    @Support
    Table<R> as(Table<?> otherTable, Field<?>... otherFields);

    /**
     * Create an alias for this table based on another table's name.
     * <p>
     * A table alias renders itself differently, depending on
     * {@link Context#declareTables()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The table alias renders its aliased expression
     * (<code>this</code>) along with the <code>AS alias</code> clause. This
     * typically happens in <code>FROM</code> and <code>INTO</code>
     * clauses.</li>
     * <li>Reference: The table alias renders its alias identifier. This happens
     * everywhere else.</li>
     * </ul>
     * <p>
     * <strong>There is no rendering mode that reproduces the aliased expression
     * as there is no way to formally decide when that mode would be more
     * appropriate than the referencing of the alias!</strong> If the aliased
     * expression is the preferred output, it can be extracted from the
     * {@link QOM} API via {@link TableAlias#$aliased()}.
     *
     * @param otherTable The other table whose name this table is aliased with.
     * @param otherFields The other fields whose field name this table's fields
     *            are aliased with.
     * @return The table alias.
     */
    @NotNull
    @Support
    Table<R> as(Table<?> otherTable, Collection<? extends Field<?>> otherFields);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(Table, Field...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as(MY_OTHER_TABLE, f -&gt;MY_OTHER_TABLE.field(f));
     * </code></pre>
     *
     * @param otherTable The other table whose name is used as alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(Table<?> otherTable, Function<? super Field<?>, ? extends Field<?>> aliasFunction);

    /**
     * Create an alias for this table and its fields.
     * <p>
     * This works like {@link #as(Table, Field...)}, except that field aliases
     * are provided by a function. This is useful, for instance, to prefix all
     * columns with a common prefix:
     * <p>
     * <pre><code>
     * MY_TABLE.as("t1", (f, i) -&gt;"column" + i);
     * </code></pre>
     *
     * @param otherTable The other table whose name is used as alias name
     * @param aliasFunction The function providing field aliases.
     * @return The table alias
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> as(Table<?> otherTable, BiFunction<? super Field<?>, ? super Integer, ? extends Field<?>> aliasFunction);

    // -------------------------------------------------------------------------
    // XXX: WHERE clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Add a <code>WHERE</code> clause to the table, connecting them with each
     * other with {@link Operator#AND}.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> where(Condition condition);

    /**
     * Add a <code>WHERE</code> clause to the table, connecting them with each
     * other with {@link Operator#AND}.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> where(Condition... conditions);

    /**
     * Add a <code>WHERE</code> clause to the table, connecting them with each
     * other with {@link Operator#AND}.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> where(Collection<? extends Condition> conditions);

    /**
     * Add a <code>WHERE</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> where(Field<Boolean> field);

    /**
     * Add a <code>WHERE</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    Table<R> where(SQL sql);

    /**
     * Add a <code>WHERE</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    Table<R> where(String sql);

    /**
     * Add a <code>WHERE</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    Table<R> where(String sql, Object... bindings);

    /**
     * Add a <code>WHERE</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    Table<R> where(String sql, QueryPart... parts);

    /**
     * Add a <code>WHERE EXISTS</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> whereExists(Select<?> select);

    /**
     * Add a <code>WHERE NOT EXISTS</code> clause to the table.
     * <p>
     * The resulting table acts like a derived table that projects all of this
     * table's columns and filters by the argument {@link Condition}. If
     * syntactically reasonable, the derived table may be inlined to the query
     * that selects from the resulting table.
     */
    @NotNull
    @Support
    Table<R> whereNotExists(Select<?> select);

    // -------------------------------------------------------------------------
    // XXX: JOIN clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Join a table to this table using a {@link JoinType}.
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link TableOnStep#on(Condition)} or
     * {@link TableOnStep#using(Field...)} clause is required. If it is required
     * but omitted, a {@link DSL#trueCondition()}, i.e. <code>1 = 1</code>
     * condition will be rendered
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> join(TableLike<?> table, JoinType type);

    /**
     * Join a table to this table using a {@link JoinType} and {@link JoinHint}.
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link TableOnStep#on(Condition)} or {@link TableOnStep#using(Field...)}
     * clause is required. If it is required but omitted, a
     * {@link DSL#trueCondition()}, i.e. <code>1 = 1</code> condition will be
     * rendered.
     * <p>
     * {@link JoinHint} are a commercial only feature and are ignored in the
     * jOOQ Open Source Edition.
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> join(TableLike<?> table, JoinType type, JoinHint hint);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #innerJoin(TableLike)}.
     *
     * @see #innerJoin(TableLike)
     */
    @NotNull
    @Support
    TableOnStep<Record> join(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a path to this table.
     * <p>
     * A synonym for {@link #innerJoin(Path)}.
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> join(Path<?> path);












































































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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    @PlainSQL
    TableOnStep<Record> join(Name name);

    /**
     * <code>INNER JOIN</code> a table to this table.
     */
    @NotNull
    @Support
    TableOnStep<Record> innerJoin(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a path to this table.
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> innerJoin(Path<?> path);



























































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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    @PlainSQL
    TableOnStep<Record> innerJoin(String sql, QueryPart... parts);

    /**
     * <code>INNER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support
    TableOnStep<Record> innerJoin(Name name);





















    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(TableLike)}.
     *
     * @see #leftOuterJoin(TableLike)
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> leftJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a path to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(Path)}.
     *
     * @see #leftOuterJoin(Path)
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> leftJoin(Path<?> path);



















































































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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    TablePartitionByStep<Record> leftJoin(Name name);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a path to this table.
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> leftOuterJoin(Path<?> path);



























































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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> leftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see SQL
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> leftOuterJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(TableLike)}.
     *
     * @see #rightOuterJoin(TableLike)
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> rightJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a path to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(Path)}.
     *
     * @see #rightOuterJoin(Path)
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> rightJoin(Path<?> path);



















































































    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(SQL sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, Object...)}.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, QueryPart...)}.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see #rightOuterJoin(Name)
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> rightJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> rightOuterJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a path to this table.
     */
    @NotNull
    @Support
    TableOptionalOnStep<Record> rightOuterJoin(Path<?> path);



























































    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(SQL sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support
    @PlainSQL
    TablePartitionByStep<Record> rightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support
    TablePartitionByStep<Record> rightOuterJoin(Name name);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(TableLike)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TablePartitionByStep<Record> fullJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a path to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(Path)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TableOptionalOnStep<Record> fullJoin(Path<?> path);







































































    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(SQL)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullJoin(SQL sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(Name)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TablePartitionByStep<Record> fullJoin(Name name);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TablePartitionByStep<Record> fullOuterJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a path to this table.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TableOptionalOnStep<Record> fullOuterJoin(Path<?> path);



























































    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullOuterJoin(SQL sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullOuterJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullOuterJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    TablePartitionByStep<Record> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    TablePartitionByStep<Record> fullOuterJoin(Name name);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Table<Record> crossJoin(TableLike<?> table);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> crossJoin(SQL sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> crossJoin(String sql);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> crossJoin(String sql, Object... bindings);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> crossJoin(String sql, QueryPart... parts);

    /**
     * <code>CROSS JOIN</code> a table to this table.
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Table<Record> crossJoin(Name name);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    @PlainSQL
    Table<Record> naturalJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    @Support
    @PlainSQL
    Table<Record> naturalLeftOuterJoin(Name name);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Table<Record> naturalRightOuterJoin(Name name);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Table<Record> naturalFullOuterJoin(TableLike<?> table);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> naturalFullOuterJoin(SQL sql);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> naturalFullOuterJoin(String sql);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> naturalFullOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
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
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> naturalFullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>NATURAL FULL OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ CLICKHOUSE, FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Table<Record> naturalFullOuterJoin(Name name);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /**
     * <code>CROSS APPLY</code> a table to this table.
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> crossApply(String sql, QueryPart... parts);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    Table<Record> crossApply(Name name);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
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
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    Table<Record> outerApply(String sql, QueryPart... parts);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    Table<Record> outerApply(Name name);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    TableOnStep<Record> straightJoin(TableLike<?> table);

    /**
     * <code>STRAIGHT_JOIN</code> a path to this table.
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    TableOptionalOnStep<Record> straightJoin(Path<?> path);

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
    @NotNull
    @Support({ MARIADB, MYSQL })
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
    @NotNull
    @Support({ MARIADB, MYSQL })
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
    @NotNull
    @Support({ MARIADB, MYSQL })
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
    @NotNull
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(String sql, QueryPart... parts);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    TableOnStep<Record> straightJoin(Name name);



    // -------------------------------------------------------------------------
    // Generic predicates
    // -------------------------------------------------------------------------

    /**
     * The <code>EQ</code> operator.
     */
    @NotNull
    @Support
    Condition eq(Table<R> arg2);

    /**
     * The <code>EQUAL</code> operator, an alias for the <code>EQ</code> operator.
     */
    @NotNull
    @Support
    Condition equal(Table<R> arg2);

    /**
     * The <code>NE</code> operator.
     */
    @NotNull
    @Support
    Condition ne(Table<R> arg2);

    /**
     * The <code>NOT_EQUAL</code> operator, an alias for the <code>NE</code> operator.
     */
    @NotNull
    @Support
    Condition notEqual(Table<R> arg2);

    // -------------------------------------------------------------------------
    // Table functions
    // -------------------------------------------------------------------------

    /**
     * The <code>ROWID</code> operator.
     * <p>
     * Get a <code>table.rowid</code> reference from this table.
     * <p>
     * A rowid value describes the physical location of a row on the disk, which
     * can be used as a replacement for a primary key in some situations -
     * especially within a query, e.g. to self-join a table:
     * <p>
     * <pre><code>
     * -- Emulating this MySQL statement...
     * DELETE FROM x ORDER BY x.y LIMIT 1
     *
     * -- ... in other databases
     * DELETE FROM x
     * WHERE x.rowid IN (
     *   SELECT x.rowid FROM x ORDER BY x.a LIMIT 1
     * )
     * </code></pre>
     * <p>
     * It is <em>not</em> recommended to use <code>rowid</code> values in client
     * applications as actual row identifiers as the database system may move a
     * row to a different physical location at any time, thus changing the rowid
     * value. In general, use primary keys, instead.
     */
    @NotNull
    @Support({ H2, POSTGRES, SQLITE })
    Field<RowId> rowid();

    /**
     * The <code>TABLESAMPLE</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the default sample
     * method.
     *
     * @param size is wrapped as {@link DSL#val(Object)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesample(Number size);

    /**
     * The <code>TABLESAMPLE</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the default sample
     * method.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesample(Field<? extends Number> size);

    /**
     * The <code>TABLESAMPLE_BERNOULLI</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the <code>BERNOULLI</code>
     * sample method.
     *
     * @param size is wrapped as {@link DSL#val(Object)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesampleBernoulli(Number size);

    /**
     * The <code>TABLESAMPLE_BERNOULLI</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the <code>BERNOULLI</code>
     * sample method.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesampleBernoulli(Field<? extends Number> size);

    /**
     * The <code>TABLESAMPLE_SYSTEM</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the <code>SYSTEM</code>
     * sample method.
     *
     * @param size is wrapped as {@link DSL#val(Object)}.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesampleSystem(Number size);

    /**
     * The <code>TABLESAMPLE_SYSTEM</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the <code>SYSTEM</code>
     * sample method.
     */
    @NotNull
    @Support({ CLICKHOUSE, DUCKDB, POSTGRES, TRINO })
    TableSampleRowsStep<R> tablesampleSystem(Field<? extends Number> size);



    /**
     * {@inheritDoc}
     * <p>
     * <strong>Watch out! This is {@link Object#equals(Object)}, not a jOOQ DSL
     * feature!</strong>
     */
    @Override
    boolean equals(Object other);

    // -------------------------------------------------------------------------
    // XXX: Exotic and vendor-specific clauses on tables
    // -------------------------------------------------------------------------

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ H2, MARIADB, MYSQL })
    Table<R> useIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> useIndexForGroupBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> ignoreIndexForGroupBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndex("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndex(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForJoin("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForJoin(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForOrderBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForOrderBy(String... indexes);

    /**
     * Specify a MySQL style table hint for query optimisation.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * create.select()
     *       .from(BOOK.as("b").useIndexForGroupBy("MY_INDEX")
     *       .fetch();
     * </code></pre>
     *
     * @see <a
     *      href="http://dev.mysql.com/doc/refman/5.7/en/index-hints.html">http://dev.mysql.com/doc/refman/5.7/en/index-hints.html</a>
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    Table<R> forceIndexForGroupBy(String... indexes);














































































































































































































    /**
     * Add the <code>WITH ORDINALITY</code> clause.
     * <p>
     * This clause can be emulated using derived tables and calculations of
     * {@link DSL#rowNumber()} or {@link DSL#rownum()}, where supported. The
     * ordering stability of such a derived table is at the mercy of the
     * optimiser implementation, and may break "unexpectedly," derived table
     * ordering isn't required to be stable in most RDBMS. So, unless the
     * ordinality can be assigned without any ambiguity (e.g. through native
     * support or because the emulation is entirely implemented in jOOQ, client
     * side), it is better not to rely on deterministic ordinalities, other than
     * the fact that all numbers from <code>1</code> to <code>N</code> will be
     * assigned uniquely.
     */
    @NotNull
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    Table<Record> withOrdinality();

    /**
     * Create a new <code>TABLE</code> reference from this table, applying
     * relational division.
     * <p>
     * Relational division is the inverse of a cross join operation. The
     * following is an approximate definition of a relational division:
     * <pre><code>
     * Assume the following cross join / cartesian product
     * C = A × B
     *
     * Then it can be said that
     * A = C ÷ B
     * B = C ÷ A
     * </code></pre>
     * <p>
     * With jOOQ, you can simplify using relational divisions by using the
     * following syntax: <pre><code>
     * C.divideBy(B).on(C.ID.equal(B.C_ID)).returning(C.TEXT)
     * </code></pre>
     * <p>
     * The above roughly translates to <pre><code>
     * SELECT DISTINCT C.TEXT FROM C "c1"
     * WHERE NOT EXISTS (
     *   SELECT 1 FROM B
     *   WHERE NOT EXISTS (
     *     SELECT 1 FROM C "c2"
     *     WHERE "c2".TEXT = "c1".TEXT
     *     AND "c2".ID = B.C_ID
     *   )
     * )
     * </code></pre>
     * <p>
     * Or in plain text: Find those TEXT values in C whose ID's correspond to
     * all ID's in B. Note that from the above SQL statement, it is immediately
     * clear that proper indexing is of the essence. Be sure to have indexes on
     * all columns referenced from the <code>on(…)</code> and
     * <code>returning(…)</code> clauses.
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
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    DivideByOnStep divideBy(Table<?> divisor);

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
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
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     */
    @NotNull
    @Support
    TableOnStep<R> leftSemiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
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
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     */
    @NotNull
    @Support
    TableOptionalOnStep<R> leftSemiJoin(Path<?> path);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
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
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left anti join, the right hand side of the
     * left anti join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     */
    @NotNull
    @Support
    TableOnStep<R> leftAntiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
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
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left anti join, the right hand side of the
     * left anti join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     */
    @NotNull
    @Support
    TableOptionalOnStep<R> leftAntiJoin(Path<?> path);




















































































































    // ------------------------------------------------------------------------
    // [#5518] Record method inversions, e.g. for use as method references
    // ------------------------------------------------------------------------

    /**
     * The inverse operation of {@link Record#into(Table)}.
     * <p>
     * This method can be used in its method reference form conveniently on a
     * generated table, for instance, when mapping records in a stream:
     * <pre><code>
     * DSL.using(configuration)
     *    .fetch("select * from t")
     *    .stream()
     *    .map(MY_TABLE::into)
     *    .forEach(System.out::println);
     * </code></pre>
     */
    @NotNull
    R from(Record record);
}
