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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
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
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

/**
 * A <code>SELECT</code> statement (model API).
 * <p>
 * This type is the model API representation of a {@link Select} statement,
 * which can be mutated after creation. The advantage of this API compared to
 * the DSL API is a more simple approach to writing dynamic SQL.
 * <p>
 * Instances can be created using {@link DSLContext#selectQuery()} and overloads.
 *
 * @author Lukas Eder
 */
public interface SelectQuery<R extends Record> extends Select<R>, ConditionProvider {

    /**
     * Add a list of select fields.
     *
     * @param fields
     */
    @Support
    void addSelect(SelectFieldOrAsterisk... fields);

    /**
     * Add a list of select fields.
     *
     * @param fields
     */
    @Support
    void addSelect(Collection<? extends SelectFieldOrAsterisk> fields);

    /**
     * Add "distinct" keyword to the select clause.
     */
    @Support
    void setDistinct(boolean distinct);

    /**
     * Add a PostgreSQL-specific <code>DISTINCT ON (fields…)</code> clause.
     * <p>
     * This also sets the <code>distinct</code> flag to <code>true</code>
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addDistinctOn(SelectFieldOrAsterisk... fields);

    /**
     * Add a PostgreSQL-specific <code>DISTINCT ON (fields…)</code> clause.
     * <p>
     * This also sets the <code>distinct</code> flag to <code>true</code>
     */
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addDistinctOn(Collection<? extends SelectFieldOrAsterisk> fields);

    /**
     * Add a T-SQL style <code>INTO</code> clause to the <code>SELECT</code>
     * statement to create a new table from a <code>SELECT</code> statement.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    void setInto(Table<?> table);














    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support
    void addFrom(TableLike<?> from);

    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support
    void addFrom(TableLike<?>... from);

    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support
    void addFrom(Collection<? extends TableLike<?>> from);

    /**
     * Joins the existing table product to a new table using a condition,
     * connecting them with each other with {@link Operator#AND}.
     *
     * @param table The joined table
     * @param condition The joining condition
     */
    @Support
    void addJoin(TableLike<?> table, Condition condition);

    /**
     * Joins the existing table product to a new table using a condition,
     * connecting them with each other with {@link Operator#AND}.
     *
     * @param table The joined table
     * @param conditions The joining conditions
     */
    @Support
    void addJoin(TableLike<?> table, Condition... conditions);

    /**
     * Joins the existing table product to a new table using a condition,
     * connecting them with each other with {@link Operator#AND}.
     *
     * @param table The joined table
     * @param type The type of join
     * @param condition The joining condition
     */
    @Support
    void addJoin(TableLike<?> table, JoinType type, Condition condition);

    /**
     * Joins the existing table product to a new table using a condition,
     * connecting them with each other with {@link Operator#AND}.
     *
     * @param table The joined table
     * @param type The type of join
     * @param conditions The joining conditions
     */
    @Support
    void addJoin(TableLike<?> table, JoinType type, Condition... conditions);





































    /**
     * Joins the existing table product to a new table with a <code>USING</code>
     * clause.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @param table The joined table
     * @param fields The fields for the <code>USING</code> clause
     */
    @Support
    void addJoinUsing(TableLike<?> table, Collection<? extends Field<?>> fields);

    /**
     * Joins the existing table product to a new table with a <code>USING</code>
     * clause.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     *
     * @param table The joined table
     * @param type The type of join
     * @param fields The fields for the <code>USING</code> clause
     */
    @Support
    void addJoinUsing(TableLike<?> table, JoinType type, Collection<? extends Field<?>> fields);

    /**
     * Joins the existing table product to a new table using a foreign key.
     *
     * @param table The joined table
     * @param type The type of join
     * @see TableOnStep#onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ. <em>Please note that if you evolve your
     *             schema, a previously non-ambiguous <code>ON KEY</code> clause
     *             can suddenly become ambiguous on an existing query, so use
     *             this clause with care.</em>
     */
    @Support
    void addJoinOnKey(TableLike<?> table, JoinType type) throws DataAccessException;

    /**
     * Joins the existing table product to a new table using a foreign key.
     *
     * @param table The joined table
     * @param type The type of join
     * @param keyFields The foreign key fields
     * @see TableOnStep#onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ. <em>Please note that if you evolve your
     *             schema, a previously non-ambiguous <code>ON KEY</code> clause
     *             can suddenly become ambiguous on an existing query, so use
     *             this clause with care.</em>
     */
    @Support
    void addJoinOnKey(TableLike<?> table, JoinType type, TableField<?, ?>... keyFields) throws DataAccessException;

    /**
     * Joins the existing table product to a new table using a foreign key.
     *
     * @param table The joined table
     * @param type The type of join
     * @param key The foreign key
     * @see TableOnStep#onKey(ForeignKey)
     */
    @Support
    void addJoinOnKey(TableLike<?> table, JoinType type, ForeignKey<?, ?> key);

    /**
     * Adds grouping fields.
     * <p>
     * Calling this with an empty argument list will result in an empty
     * <code>GROUP BY ()</code> clause being rendered.
     *
     * @param fields The grouping fields
     */
    @Support
    void addGroupBy(GroupField... fields);

    /**
     * Adds grouping fields.
     * <p>
     * Calling this with an empty argument list will result in an empty
     * <code>GROUP BY ()</code> clause being rendered.
     *
     * @param fields The grouping fields
     */
    @Support
    void addGroupBy(Collection<? extends GroupField> fields);

    /**
     * Specifies the <code>GROUP BY DISTINCT</code> clause.
     * <p>
     * This is mostly useful when combined with
     * {@link DSL#groupingSets(Field[]...)} to remove duplicate grouping set
     * results prior to aggregation and projection.
     */
    @Support({ POSTGRES })
    void setGroupByDistinct(boolean groupByDistinct);

    /**
     * Adds a new condition to the having clause of the query, connecting it
     * with each other with {@link Operator#AND}.
     *
     * @param condition The condition
     */
    @Support
    void addHaving(Condition condition);

    /**
     * Adds new conditions to the having clause of the query, connecting them
     * with each other with {@link Operator#AND}.
     *
     * @param conditions The condition
     */
    @Support
    void addHaving(Condition... conditions);

    /**
     * Adds new conditions to the having clause of the query, connecting them
     * with each other with {@link Operator#AND}.
     *
     * @param conditions The condition
     */
    @Support
    void addHaving(Collection<? extends Condition> conditions);

    /**
     * Adds a new condition to the having clause of query, connecting it with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param condition The condition
     */
    @Support
    void addHaving(Operator operator, Condition condition);

    /**
     * Adds new conditions to the having clause of query, connecting them with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support
    void addHaving(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the having clause of query, connecting them with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support
    void addHaving(Operator operator, Collection<? extends Condition> conditions);

    /**
     * Adds new window definitions to the window clause of the query.
     *
     * @param definitions The definitions
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addWindow(WindowDefinition... definitions);

    /**
     * Adds new window definitions to the window clause of the query.
     *
     * @param definitions The definitions
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addWindow(Collection<? extends WindowDefinition> definitions);

    /**
     * Adds a new condition to the qualify clause of the query, connecting it
     * with each other with {@link Operator#AND}.
     *
     * @param condition The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Condition condition);

    /**
     * Adds new conditions to the qualify clause of the query, connecting them
     * with each other with {@link Operator#AND}.
     *
     * @param conditions The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Condition... conditions);

    /**
     * Adds new conditions to the qualify clause of the query, connecting them
     * with each other with {@link Operator#AND}.
     *
     * @param conditions The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Collection<? extends Condition> conditions);

    /**
     * Adds a new condition to the qualify clause of query, connecting it with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param condition The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Operator operator, Condition condition);

    /**
     * Adds new conditions to the qualify clause of query, connecting them with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the qualify clause of query, connecting them with
     * each other with <code>operator</code>.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addQualify(Operator operator, Collection<? extends Condition> conditions);

    /**
     * Add an Oracle-style hint to the select clause.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .hint("/*+ALL_ROWS&#42;/")
     *       .from(table1)
     *       .execute();
     * </code></pre>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for MySQL's
     * <code>SQL_CALC_FOUND_ROWS</code> option: <pre><code>
     * create.select(field1, field2)
     *       .hint("SQL_CALC_FOUND_ROWS")
     *       .from(table1)
     *       .fetch();
     * </code></pre>
     * <p>
     * The outcome of such a query is this: <pre><code>
     * SELECT [hint] field1, field2 FROM table1
     * </code></pre>
     * <p>
     * For SQL Server style table hints, see {@link Table#with(String)}
     *
     * @see Table#with(String)
     */
    @Support
    void addHint(String hint);

    /**
     * Add a SQL Server-style query hint to the select clause.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .option("OPTION (OPTIMIZE FOR UNKNOWN)")
     *       .execute();
     * </code></pre>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for DB2's isolation clause: <pre><code>
     * create.select(field1, field2)
     *       .from(table1)
     *       .option("WITH RR USE AND KEEP EXCLUSIVE LOCKS")
     *       .execute();
     * </code></pre>
     * <p>
     * The outcome of such a query is this: <pre><code>
     * SELECT field1, field2 FROM table1 [option]
     * </code></pre>
     * <p>
     * For SQL Server style table hints, see {@link Table#with(String)}
     *
     * @see Table#with(String)
     */
    @Support
    void addOption(String option);































    // ------------------------------------------------------------------------
    // Methods from ConditionProvider, OrderProvider, LockProvider
    // ------------------------------------------------------------------------

    @Override
    @Support
    void addConditions(Condition condition);

    @Override
    @Support
    void addConditions(Condition... conditions);

    @Override
    @Support
    void addConditions(Collection<? extends Condition> conditions);

    @Override
    @Support
    void addConditions(Operator operator, Condition condition);

    @Override
    @Support
    void addConditions(Operator operator, Condition... conditions);

    @Override
    @Support
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

    /**
     * Adds ordering fields.
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(OrderField<?>... fields);

    /**
     * Adds ordering fields.
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(Collection<? extends OrderField<?>> fields);

    /**
     * Adds ordering fields.
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>addOrderBy(DSL.val(1).desc())</code> or
     * <code>addOrderBy(DSL.literal(1).desc())</code> to apply descending
     * ordering
     *
     * @param fieldIndexes The ordering fields
     */
    @Support
    void addOrderBy(int... fieldIndexes);




















    /**
     * Adds seeking fields.
     *
     * @param fields The seeking fields
     */
    @Support
    void addSeekAfter(Field<?>... fields);

    /**
     * Adds seeking fields.
     *
     * @param fields The seeking fields
     */
    @Support
    void addSeekAfter(Collection<? extends Field<?>> fields);

    /**
     * Adds seeking fields.
     *
     * @param fields The seeking fields
     * @deprecated - [#7461] - SEEK BEFORE is not implemented correctly
     */
    @Deprecated
    @Support
    void addSeekBefore(Field<?>... fields);

    /**
     * Adds seeking fields.
     *
     * @param fields The seeking fields
     * @deprecated - [#7461] - SEEK BEFORE is not implemented correctly
     */
    @Deprecated
    @Support
    void addSeekBefore(Collection<? extends Field<?>> fields);

    /**
     * Add a 0-based <code>OFFSET</code> clause to the query.
     * <p>
     * Offsets are 0-based as they describe the number of rows to <em>skip</em>.
     * <p>
     * If there is no <code>LIMIT … OFFSET</code> or <code>TOP</code> clause in
     * your RDBMS, or if your RDBMS does not natively support offsets, this is
     * emulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addOffset(Number offset);

    /**
     * Add a 0-based <code>OFFSET</code> clause to the query.
     * <p>
     * Offsets are 0-based as they describe the number of rows to <em>skip</em>.
     * <p>
     * If there is no <code>LIMIT … OFFSET</code> or <code>TOP</code> clause in
     * your RDBMS, or if your RDBMS does not natively support offsets, this is
     * emulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    void addOffset(Field<? extends Number> offset);

    /**
     * Limit the results of this select.
     * <p>
     * This is the same as calling {@link #addLimit(Number, Number)} with offset
     * = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Number numberOfRows);

    /**
     * Limit the results of this select.
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, this may be emulated with a
     * <code>ROW_NUMBER()</code> window function and nested <code>SELECT</code>
     * statements.
     * <p>
     * This is the same as calling {@link #addLimit(Number, Number)} with offset
     * = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Field<? extends Number> numberOfRows);

    /**
     * Limit the results of this select.
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or if your RDBMS does not natively support offsets, this is
     * emulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Number offset, Number numberOfRows);

    /**
     * Limit the results of this select.
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be emulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Field<? extends Number> offset, Number numberOfRows);

    /**
     * Limit the results of this select.
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be emulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Number offset, Field<? extends Number> numberOfRows);

    /**
     * Limit the results of this select.
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be emulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(Field<? extends Number> offset, Field<? extends Number> numberOfRows);

    /**
     * Add the <code>PERCENT</code> clause to a <code>LIMIT</code> clause.
     */
    @Support({ H2 })
    void setLimitPercent(boolean percent);

    /**
     * Add the <code>WITH TIES</code> clause to a <code>LIMIT</code> clause.
     */
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    void setWithTies(boolean withTies);

    /**
     * Sets the "FOR UPDATE" lock mode onto the query.
     * <p>
     * <h5>Native implementation</h5>
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=/com.ibm.db2.luw.sql.ref.doc/doc/r0000879.html"
     * >DB2 FOR UPDATE and similar clauses</a></li>
     * <li><a href=
     * "http://db.apache.org/derby/docs/10.7/ref/rrefsqlj31783.html"> Derby's
     * FOR UPDATE clause</a></li>
     * <li><a href="https://www.h2database.com/html/grammar.html#select">H2's FOR
     * UPDATE clause</a></li>
     * <li><a href=
     * "http://www.hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N11DA9"
     * >HSQLDB's FOR UPDATE clause</a></li>
     * <li><a href=
     * "http://dev.mysql.com/doc/refman/5.5/en/innodb-locking-reads.html"
     * >MySQL's InnoDB locking reads</a></li>
     * <li><a href=
     * "http://www.techonthenet.com/oracle/cursors/for_update.php">Oracle's
     * PL/SQL FOR UPDATE clause</a></li>
     * <li><a href=
     * "http://www.postgresql.org/docs/9.0/static/sql-select.html#SQL-FOR-UPDATE-SHARE"
     * >Postgres FOR UPDATE / FOR SHARE</a></li>
     * </ul>
     * <p>
     * <h5>Emulation</h5>
     * <p>
     * {@link SQLDialect#SQLSERVER}: jOOQ will try to lock the database record
     * using <code>WITH (ROWLOCK, UPDLOCK)</code> hints.
     * <h5>Not supported</h5>
     * <p>
     * These dialects are known not to support the <code>FOR UPDATE</code>
     * clause in regular SQL:
     * <ul>
     * <li>{@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * If your dialect does not support this clause, jOOQ will still render it,
     * if you apply it to your query. This might then cause syntax errors
     * reported either by your database or your JDBC driver.
     * <p>
     * You shouldn't combine this with {@link #setForShare(boolean)}
     *
     * @param forUpdate The flag's value
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForUpdate(boolean forUpdate);

    /**
     * Sets the "FOR NO KEY UPDATE" lock mode onto the query.
     */
    @Support({ POSTGRES, YUGABYTEDB })
    void setForNoKeyUpdate(boolean forNoKeyUpdate);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR UPDATE</code> clause, instead of the full row.
     * <p>
     * This automatically sets the {@link #setForUpdate(boolean)} flag, and
     * unsets the {@link #setForShare(boolean)} flag, if it was previously set.
     * <p>
     * This has been observed to be natively supported by any of these dialects:
     * <ul>
     * <li>DB2</li>
     * <li>Derby</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>Oracle</li>
     * <li>Sybase</li>
     * </ul>
     * <p>
     * Note, that {@link SQLDialect#DB2} has some stricter requirements
     * regarding the updatability of fields. Refer to the DB2 documentation for
     * further details
     *
     * @param fields The fields that should be locked
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeOf(Field...)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ DERBY, FIREBIRD, H2, HSQLDB })
    void setForUpdateOf(Field<?>... fields);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR UPDATE</code> clause, instead of the full row.
     * <p>
     *
     * @see #setForUpdateOf(Field...)
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeOf(Collection)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ DERBY, FIREBIRD, H2, HSQLDB })
    void setForUpdateOf(Collection<? extends Field<?>> fields);

    /**
     * Some RDBMS allow for specifying the tables that should be locked by the
     * <code>FOR UPDATE</code> clause, instead of the full row.
     * <p>
     * This automatically sets the {@link #setForUpdate(boolean)} flag, and
     * unsets the {@link #setForShare(boolean)} flag, if it was previously set.
     * <p>
     * This has been observed to be natively supported by any of these dialects:
     * <ul>
     * <li>Postgres</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Sybase</li>
     * </ul>
     * <p>
     * jOOQ emulates this by locking all known fields of [<code>tables</code>]
     * for any of these dialects:
     * <ul>
     * <li>DB2</li>
     * <li>Derby</li>
     * <li>Ingres</li>
     * <li>Oracle</li>
     * </ul>
     *
     * @param tables The tables that should be locked
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeOf(Table...)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForUpdateOf(Table<?>... tables);

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR UPDATE</code> clause. In this case, the session will wait for
     * some <code>seconds</code>, before aborting the lock acquirement if the
     * lock is not available.
     * <p>
     * This automatically sets the {@link #setForUpdate(boolean)} flag, and
     * unsets the {@link #setForShare(boolean)} flag, if it was previously set.
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li>Oracle</li>
     * </ul>
     *
     * @param seconds The number of seconds to wait for a lock
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeWait(int)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ MARIADB })
    void setForUpdateWait(int seconds);

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR UPDATE</code> clause. In this case, the session will not wait
     * before aborting the lock acquirement if the lock is not available.
     * <p>
     * This automatically sets the {@link #setForUpdate(boolean)} flag, and
     * unsets the {@link #setForShare(boolean)} flag, if it was previously set.
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li>Oracle</li>
     * </ul>
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeNoWait()}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForUpdateNoWait();

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR UPDATE</code> clause. In this case, the session will skip all
     * locked rows from the select statement, whose lock is not available.
     * <p>
     * This automatically sets the {@link #setForUpdate(boolean)} flag, and
     * unsets the {@link #setForShare(boolean)} flag, if it was previously set.
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li>Oracle</li>
     * </ul>
     *
     * @deprecated [#5218] - 3.14.0 - Use {@link #setForLockModeSkipLocked()}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @Support({ MYSQL, POSTGRES, YUGABYTEDB })
    void setForUpdateSkipLocked();

    /**
     * Sets the "FOR SHARE" lock mode onto the query.
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li><a
     * href="http://dev.mysql.com/doc/refman/5.5/en/innodb-locking-reads.html"
     * >MySQL's InnoDB locking reads</a></li>
     * <li><a href=
     * "http://www.postgresql.org/docs/9.0/static/sql-select.html#SQL-FOR-UPDATE-SHARE"
     * >Postgres FOR UPDATE / FOR SHARE</a></li>
     * </ul>
     * <p>
     * If your dialect does not support this clause, jOOQ will still render it,
     * if you apply it to your query. This might then cause syntax errors
     * reported either by your database or your JDBC driver.
     * <p>
     * You shouldn't combine this with {@link #setForUpdate(boolean)}
     *
     * @param forShare The flag's value
     */
    @Support({ MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForShare(boolean forShare);

    /**
     * Sets the "FOR KEY SHARE" lock mode onto the query.
     */
    @Support({ POSTGRES, YUGABYTEDB })
    void setForKeyShare(boolean forKeyShare);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR &lt;lock_mode&gt;</code> clause, instead of the full row.
     * <p>
     * In case no lock mode has been set yet, it will implicitly be set to
     * <code>UPDATE</code> (i.e. {@link #setForUpdate(boolean)}).
     * <p>
     * Depending on the dialect and lock mode this flag may or may not be
     * supported.
     * <p>
     * Note, that {@link SQLDialect#DB2} has some stricter requirements
     * regarding the updatability of fields. Refer to the DB2 documentation for
     * further details
     *
     * @param fields The fields that should be locked
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB })
    void setForLockModeOf(Field<?>... fields);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR &lt;lock_mode&gt;</code> clause, instead of the full row.
     * <p>
     *
     * @see #setForLockModeOf(Field...)
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB })
    void setForLockModeOf(Collection<? extends Field<?>> fields);

    /**
     * Some RDBMS allow for specifying the tables that should be locked by the
     * <code>FOR &lt;lock_mode&gt;</code> clause, instead of the full row.
     * <p>
     * In case no lock mode has been set yet, it will implicitly be set to
     * <code>UPDATE</code> (i.e. {@link #setForUpdate(boolean)}).
     * <p>
     * Depending on the dialect and lock mode this flag may or may not be
     * supported.
     *
     * @param tables The tables that should be locked
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForLockModeOf(Table<?>... tables);

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR &lt;lock_mode&gt;</code> clause. In this case, the session will
     * wait for some <code>seconds</code>, before aborting the lock acquirement
     * if the lock is not available.
     * <p>
     * In case no lock mode has been set yet, it will implicitly be set to
     * <code>UPDATE</code> (i.e. {@link #setForUpdate(boolean)}).
     * <p>
     * Depending on the dialect and lock mode this flag may or may not be
     * supported.
     *
     * @param seconds The number of seconds to wait for a lock
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES })
    void setForLockModeWait(int seconds);

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR &lt;lock_mode&gt;</code> clause. In this case, the session will
     * not wait before aborting the lock acquirement if the lock is not
     * available.
     * <p>
     * In case no lock mode has been set yet, it will implicitly be set to
     * <code>UPDATE</code> (i.e. {@link #setForUpdate(boolean)}).
     * <p>
     * Depending on the dialect and lock mode this flag may or may not be
     * supported.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForLockModeNoWait();

    /**
     * Some RDBMS allow for specifying the locking mode for the applied
     * <code>FOR &lt;lock_mode&gt;</code> clause. In this case, the session will
     * skip all locked rows from the select statement, whose lock is not
     * available.
     * <p>
     * In case no lock mode has been set yet, it will implicitly be set to
     * <code>UPDATE</code> (i.e. {@link #setForUpdate(boolean)}).
     * <p>
     * Depending on the dialect and lock mode this flag may or may not be
     * supported.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    void setForLockModeSkipLocked();


















































































































































    /**
     * Add a <code>WITH CHECK OPTION</code> clause to the end of the subquery.
     */
    @Support({ FIREBIRD, MARIADB, MYSQL, POSTGRES })
    void setWithCheckOption();

    /**
     * Add a <code>WITH READ ONLY</code> clause to the end of the subquery.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    void setWithReadOnly();

}
