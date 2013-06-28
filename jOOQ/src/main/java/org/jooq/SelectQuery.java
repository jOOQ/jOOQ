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
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

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
     * Add a list of select fields
     *
     * @param fields
     */
    @Support
    void addSelect(Field<?>... fields);

    /**
     * Add a list of select fields
     *
     * @param fields
     */
    @Support
    void addSelect(Collection<? extends Field<?>> fields);

    /**
     * Add "distinct" keyword to the select clause
     */
    @Support
    void setDistinct(boolean distinct);

    /**
     * Add tables to the table product
     *
     * @param from The added tables
     */
    @Support
    void addFrom(TableLike<?>... from);

    /**
     * Add tables to the table product
     *
     * @param from The added tables
     */
    @Support
    void addFrom(Collection<? extends TableLike<?>> from);

    /**
     * Joins the existing table product to a new table using a condition
     *
     * @param table The joined table
     * @param conditions The joining conditions
     */
    @Support
    void addJoin(TableLike<?> table, Condition... conditions);

    /**
     * Joins the existing table product to a new table using a condition
     *
     * @param table The joined table
     * @param type The type of join
     * @param conditions The joining conditions
     */
    @Support
    void addJoin(TableLike<?> table, JoinType type, Condition... conditions);

    /**
     * Joins the existing table product to a new table using a condition
     * <p>
     * This adds a <code>PARTITION BY</code> clause to the right hand side of a
     * <code>OUTER JOIN</code> expression.
     *
     * @param table The joined table
     * @param type The type of join
     * @param conditions The joining conditions
     * @param partitionBy The <code>PARTITION BY</code> expression
     * @see TablePartitionByStep
     */
    @Support(ORACLE)
    void addJoin(TableLike<?> table, JoinType type, Condition[] conditions, Field<?>[] partitionBy);

    /**
     * Joins the existing table product to a new table with a <code>USING</code>
     * clause
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     *
     * @param table The joined table
     * @param fields The fields for the <code>USING</code> clause
     */
    @Support
    void addJoinUsing(TableLike<?> table, Collection<? extends Field<?>> fields);

    /**
     * Joins the existing table product to a new table with a <code>USING</code>
     * clause
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     *
     * @param table The joined table
     * @param type The type of join
     * @param fields The fields for the <code>USING</code> clause
     */
    @Support
    void addJoinUsing(TableLike<?> table, JoinType type, Collection<? extends Field<?>> fields);

    /**
     * Joins the existing table product to a new table using a foreign key
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
     * Joins the existing table product to a new table using a foreign key
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
     * Joins the existing table product to a new table using a foreign key
     *
     * @param table The joined table
     * @param type The type of join
     * @param key The foreign key
     * @see TableOnStep#onKey(ForeignKey)
     */
    @Support
    void addJoinOnKey(TableLike<?> table, JoinType type, ForeignKey<?, ?> key);

    /**
     * Adds grouping fields
     * <p>
     * Calling this with an empty argument list will result in an empty
     * <code>GROUP BY ()</code> clause being rendered.
     *
     * @param fields The grouping fields
     */
    @Support
    void addGroupBy(GroupField... fields);

    /**
     * Adds grouping fields
     * <p>
     * Calling this with an empty argument list will result in an empty
     * <code>GROUP BY ()</code> clause being rendered.
     *
     * @param fields The grouping fields
     */
    @Support
    void addGroupBy(Collection<? extends GroupField> fields);

    /**
     * Adds new conditions to the having clause of the query, connecting it to
     * existing conditions with the and operator.
     *
     * @param conditions The condition
     */
    @Support
    void addHaving(Condition... conditions);

    /**
     * Adds new conditions to the having clause of the query, connecting it to
     * existing conditions with the and operator.
     *
     * @param conditions The condition
     */
    @Support
    void addHaving(Collection<Condition> conditions);

    /**
     * Adds new conditions to the having clause of query, connecting them to
     * existing conditions with the provided operator
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support
    void addHaving(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the having clause of query, connecting them to
     * existing conditions with the provided operator
     *
     * @param operator The operator to use to add the conditions to the existing
     *            conditions
     * @param conditions The condition
     */
    @Support
    void addHaving(Operator operator, Collection<Condition> conditions);

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
     * Add an Oracle-specific <code>CONNECT BY</code> clause to the query
     */
    @Support({ CUBRID, ORACLE })
    void addConnectBy(Condition condition);

    /**
     * Add an Oracle-specific <code>CONNECT BY NOCYCLE</code> clause to the
     * query
     */
    @Support({ CUBRID, ORACLE })
    void addConnectByNoCycle(Condition condition);

    /**
     * Add an Oracle-specific <code>START WITH</code> clause to the query's
     * <code>CONNECT BY</code> clause
     */
    @Support({ CUBRID, ORACLE })
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
    void addConditions(Collection<Condition> conditions);

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
    void addConditions(Operator operator, Collection<Condition> conditions);

    /**
     * Adds ordering fields, ordering by the default sort order
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(Field<?>... fields);

    /**
     * Adds ordering fields
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(SortField<?>... fields);

    /**
     * Adds ordering fields
     *
     * @param fields The ordering fields
     */
    @Support
    void addOrderBy(Collection<SortField<?>> fields);

    /**
     * Adds ordering fields
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
    @Support({ CUBRID, ORACLE })
    void setOrderBySiblings(boolean orderBySiblings);

    /**
     * Limit the results of this select
     * <p>
     * This is the same as calling {@link #addLimit(int, int)} with offset = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support
    void addLimit(int numberOfRows);

    /**
     * Limit the results of this select using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, this may be simulated with a
     * <code>ROW_NUMBER()</code> window function and nested <code>SELECT</code>
     * statements.
     * <p>
     * This is the same as calling {@link #addLimit(int, int)} with offset = 0
     *
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void addLimit(Param<Integer> numberOfRows);

    /**
     * Limit the results of this select
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or if your RDBMS does not natively support offsets, this is
     * simulated with a <code>ROW_NUMBER()</code> window function and nested
     * <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void addLimit(int offset, int numberOfRows);

    /**
     * Limit the results of this select
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void addLimit(Param<Integer> offset, int numberOfRows);

    /**
     * Limit the results of this select using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void addLimit(int offset, Param<Integer> numberOfRows);

    /**
     * Limit the results of this select using named parameters
     * <p>
     * Note that some dialects do not support bind values at all in
     * <code>LIMIT</code> or <code>TOP</code> clauses!
     * <p>
     * If there is no <code>LIMIT</code> or <code>TOP</code> clause in your
     * RDBMS, or the <code>LIMIT</code> or <code>TOP</code> clause does not
     * support bind values, or if your RDBMS does not natively support offsets,
     * this may be simulated with a <code>ROW_NUMBER()</code> window function
     * and nested <code>SELECT</code> statements.
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE })
    void addLimit(Param<Integer> offset, Param<Integer> numberOfRows);

    /**
     * Sets the "FOR UPDATE" flag onto the query
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
     * <h5>Simulation</h5>
     * <p>
     * These dialects can simulate the <code>FOR UPDATE</code> clause using a
     * cursor. The cursor is handled by the JDBC driver, at
     * {@link PreparedStatement} construction time, when calling
     * {@link Connection#prepareStatement(String, int, int)} with
     * {@link ResultSet#CONCUR_UPDATABLE}. jOOQ handles simulation of a
     * <code>FOR UPDATE</code> clause using <code>CONCUR_UPDATABLE</code> for
     * these dialects:
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#SQLSERVER}</li>
     * </ul>
     * <p>
     * Note: This simulation may not be efficient for large result sets!
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
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
    @Support({ DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, ORACLE, SYBASE })
    void setForUpdateOf(Field<?>... fields);

    /**
     * Some RDBMS allow for specifying the fields that should be locked by the
     * <code>FOR UPDATE</code> clause, instead of the full row.
     * <p>
     *
     * @see #setForUpdateOf(Field...)
     */
    @Support({ DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, ORACLE, SYBASE })
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
     * jOOQ simulates this by locking all known fields of [<code>tables</code>]
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
    @Support({ DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, POSTGRES, ORACLE, SYBASE })
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
     */
    @Support(ORACLE)
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
     */
    @Support({ ORACLE, POSTGRES })
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
    @Support(ORACLE)
    void setForUpdateSkipLocked();

    /**
     * Sets the "FOR SHARE" flag onto the query
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
