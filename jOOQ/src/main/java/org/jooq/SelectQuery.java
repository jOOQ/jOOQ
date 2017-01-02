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
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.H2;
// ...
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
// ...

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import org.jooq.exception.DataAccessException;

/**
 * A query for data selection
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public interface SelectQuery<R extends Record> extends Select<R>, ConditionProvider {

    /**
     * Add a list of select fields.
     *
     * @param fields
     */
    @Support
    void addSelect(SelectField<?>... fields);

    /**
     * Add a list of select fields.
     *
     * @param fields
     */
    @Support
    void addSelect(Collection<? extends SelectField<?>> fields);

    /**
     * Add "distinct" keyword to the select clause.
     */
    @Support
    void setDistinct(boolean distinct);

    /**
     * Add a PostgreSQL-specific <code>DISTINCT ON (fields...)</code> clause.
     * <p>
     * This also sets the <code>distinct</code> flag to <code>true</code>
     */
    @Support({ POSTGRES })
    void addDistinctOn(SelectField<?>... fields);

    /**
     * Add a PostgreSQL-specific <code>DISTINCT ON (fields...)</code> clause.
     * <p>
     * This also sets the <code>distinct</code> flag to <code>true</code>
     */
    @Support({ POSTGRES })
    void addDistinctOn(Collection<? extends SelectField<?>> fields);

    /**
     * Add <code>INTO</code> clause to the <code>SELECT</code> statement.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
     *             known to jOOQ
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
     *             known to jOOQ
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
     * Adds new conditions to the having clause of query, connecting them with
     * each other with {@link Operator#AND}.
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support
    void addHaving(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the having clause of query, connecting them with
     * each other with {@link Operator#AND}.
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
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    void addWindow(WindowDefinition... definitions);

    /**
     * Adds new window definitions to the window clause of the query.
     *
     * @param definitions The definitions
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    void addWindow(Collection<? extends WindowDefinition> definitions);

    /**
     * Add an Oracle-style hint to the select clause.
     * <p>
     * Example: <code><pre>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .hint("/*+ALL_ROWS&#42;/")
     *       .from(table1)
     *       .execute();
     * </pre></code>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for MySQL's
     * <code>SQL_CALC_FOUND_ROWS</code> option: <code><pre>
     * create.select(field1, field2)
     *       .hint("SQL_CALC_FOUND_ROWS")
     *       .from(table1)
     *       .fetch();
     * </pre></code>
     * <p>
     * The outcome of such a query is this: <code><pre>
     * SELECT [hint] field1, field2 FROM table1
     * </pre></code>
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
     * Example: <code><pre>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .option("OPTION (OPTIMIZE FOR UNKNOWN)")
     *       .execute();
     * </pre></code>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for DB2's isolation clause: <code><pre>
     * create.select(field1, field2)
     *       .from(table1)
     *       .option("WITH RR USE AND KEEP EXCLUSIVE LOCKS")
     *       .execute();
     * </pre></code>
     * <p>
     * The outcome of such a query is this: <code><pre>
     * SELECT field1, field2 FROM table1 [option]
     * </pre></code>
     * <p>
     * For SQL Server style table hints, see {@link Table#with(String)}
     *
     * @see Table#with(String)
     */
    @Support
    void addOption(String option);

    /**
     * Add an Oracle-specific <code>CONNECT BY</code> clause to the query.
     */
    @Support({ CUBRID })
    void addConnectBy(Condition condition);

    /**
     * Add an Oracle-specific <code>CONNECT BY NOCYCLE</code> clause to the
     * query.
     */
    @Support({ CUBRID })
    void addConnectByNoCycle(Condition condition);

    /**
     * Add an Oracle-specific <code>START WITH</code> clause to the query's
     * <code>CONNECT BY</code> clause.
     */
    @Support({ CUBRID })
    void setConnectByStartWith(Condition condition);

    // ------------------------------------------------------------------------
    // Methods from ConditionProvider, OrderProvider, LockProvider
    // ------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Collection<? extends Condition> conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

    /**
     * Adds ordering fields, ordering by the default sort order.
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(Field<?>... fields);

    /**
     * Adds ordering fields.
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(SortField<?>... fields);

    /**
     * Adds ordering fields.
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(Collection<? extends SortField<?>> fields);

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
     * Indicate whether the <code>SIBLINGS</code> keyword should be used in an
     * <code>ORDER BY</code> clause to form an <code>ORDER SIBLINGS BY</code>
     * clause.
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     *
     * @param orderBySiblings
     */
    @Support({ CUBRID })
    void setOrderBySiblings(boolean orderBySiblings);

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
     */
    @Support
    void addSeekBefore(Field<?>... fields);

    /**
     * Adds seeking fields.
     *
     * @param fields The seeking fields
     */
    @Support
    void addSeekBefore(Collection<? extends Field<?>> fields);

    /**
     * Add an <code>OFFSET</code> clause to the query.
     * <p>
     * If there is no <code>LIMIT .. OFFSET</code> or <code>TOP</code> clause in
     * your RDBMS, or if your RDBMS does not natively support offsets, this is
     * emulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addOffset(int offset);

    /**
     * Add an <code>OFFSET</code> clause to the query using a named parameter.
     * <p>
     * If there is no <code>LIMIT .. OFFSET</code> or <code>TOP</code> clause in
     * your RDBMS, or if your RDBMS does not natively support offsets, this is
     * emulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addOffset(Param<Integer> offset);

    /**
     * Limit the results of this select.
     * <p>
     * This is the same as calling {@link #addLimit(int, int)} with offset = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(int numberOfRows);

    /**
     * Limit the results of this select using named parameters.
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
     * This is the same as calling {@link #addLimit(int, int)} with offset = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addLimit(Param<Integer> numberOfRows);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addLimit(int offset, int numberOfRows);

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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addLimit(Param<Integer> offset, int numberOfRows);

    /**
     * Limit the results of this select using named parameters.
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addLimit(int offset, Param<Integer> numberOfRows);

    /**
     * Limit the results of this select using named parameters.
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    void addLimit(Param<Integer> offset, Param<Integer> numberOfRows);

    /**
     * Sets the "FOR UPDATE" flag onto the query.
     * <p>
     * <h5>Native implementation</h5>
     * <p>
     * This has been observed to be supported by any of these dialects:
     * <ul>
     * <li><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=/com.ibm.db2.luw.sql.ref.doc/doc/r0000879.html"
     * >DB2 FOR UPDATE and similar clauses</a></li>
     * <li><a
     * href="http://db.apache.org/derby/docs/10.7/ref/rrefsqlj31783.html">
     * Derby's FOR UPDATE clause</a></li>
     * <li><a href="http://www.h2database.com/html/grammar.html#select">H2's FOR
     * UPDATE clause</a></li>
     * <li><a
     * href="http://www.hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N11DA9"
     * >HSQLDB's FOR UPDATE clause</a></li>
     * <li><a
     * href="http://dev.mysql.com/doc/refman/5.5/en/innodb-locking-reads.html"
     * >MySQL's InnoDB locking reads</a></li>
     * <li><a
     * href="http://www.techonthenet.com/oracle/cursors/for_update.php">Oracle's
     * PL/SQL FOR UPDATE clause</a></li>
     * <li><a href=
     * "http://www.postgresql.org/docs/9.0/static/sql-select.html#SQL-FOR-UPDATE-SHARE"
     * >Postgres FOR UPDATE / FOR SHARE</a></li>
     * </ul>
     * <p>
     * <h5>emulation</h5>
     * <p>
     * These dialects can emulate the <code>FOR UPDATE</code> clause using a
     * cursor. The cursor is handled by the JDBC driver, at
     * {@link PreparedStatement} construction time, when calling
     * {@link Connection#prepareStatement(String, int, int)} with
     * {@link ResultSet#CONCUR_UPDATABLE}. jOOQ handles emulation of a
     * <code>FOR UPDATE</code> clause using <code>CONCUR_UPDATABLE</code> for
     * these dialects:
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#SQLSERVER}</li>
     * </ul>
     * <p>
     * Note: This emulation may not be efficient for large result sets!
     * <p>
     * <h5>Not supported</h5>
     * <p>
     * These dialects are known not to support the <code>FOR UPDATE</code>
     * clause in regular SQL:
     * <ul>
     * <li> {@link SQLDialect#SQLITE}</li>
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    void setForUpdate(boolean forUpdate);

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
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB })
    void setForUpdateOf(Field<?>... fields);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR UPDATE</code> clause, instead of the full row.
     * <p>
     *
     * @see #setForUpdateOf(Field...)
     */
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
     */
    @Support({ DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    void setForUpdateOf(Table<?>... tables);






















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
     */
    @Support({ POSTGRES })
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
     */
    @Support({POSTGRES_9_5})
    void setForUpdateSkipLocked();

    /**
     * Sets the "FOR SHARE" flag onto the query.
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
    @Support({ MARIADB, MYSQL, POSTGRES })
    void setForShare(boolean forShare);















}
