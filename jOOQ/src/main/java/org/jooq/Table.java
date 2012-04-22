/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;
import java.util.List;

import org.jooq.impl.Factory;

/**
 * A table to be used in queries
 *
 * @param <R> The record type associated with this table
 * @author Lukas Eder
 */
public interface Table<R extends Record> extends Type<R>, AliasProvider<Table<R>>, TableLike<R> {

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

    /**
     * Create a new <code>TABLE</code> reference from this table, pivoting it
     * into another form
     * <p>
     * This has been observed to work with
     * <ul>
     * <li> {@link SQLDialect#ORACLE}</li>
     * <li> {@link SQLDialect#SQLSERVER} (not yet officially supported)</li>
     * <li>Other dialects by using some means of simulation (not yet officially
     * supported)</li>
     * </ul>
     *
     * @param aggregateFunctions The aggregate functions used for pivoting.
     * @return A DSL object to create the <code>PIVOT</code> expression
     */
    @Support({ ORACLE })
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
    @Support({ ORACLE })
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
     * href="http://en.wikipedia.org/wiki/Relational_algebra#Division">http://en.wikipedia.org/wiki/Relational_algebra#Division</a></li>
     * <li><a href=
     * "http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/"
     * >http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/</a></li>
     * </ul>
     * <p>
     * This has been observed to work with all dialects
     */
    @Support
    DivideByOnStep divideBy(Table<?> divisor);

    /**
     * <code>INNER JOIN</code> a table to this table.
     */
    @Support
    TableOnStep join(TableLike<?> table);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#table(String)
     */
    @Support
    TableOnStep join(String sql);

    /**
     * <code>INNER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#table(String, Object...)
     */
    @Support
    TableOnStep join(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @Support
    TableOnStep leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#table(String)
     */
    @Support
    TableOnStep leftOuterJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#table(String, Object...)
     */
    @Support
    TableOnStep leftOuterJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    TableOnStep rightOuterJoin(TableLike<?> table);

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
     * @see Factory#table(String)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    TableOnStep rightOuterJoin(String sql);

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
     * @see Factory#table(String, Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    TableOnStep rightOuterJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ DB2, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
     * @see Factory#table(String)
     */
    @Support({ DB2, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
     * @see Factory#table(String, Object...)
     */
    @Support({ DB2, HSQLDB, INGRES, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    TableOnStep fullOuterJoin(String sql, Object... bindings);

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
     * @see Factory#table(String)
     */
    @Support
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
     * @see Factory#table(String, Object...)
     */
    @Support
    Table<Record> crossJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
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
     * @see Factory#table(String)
     */
    @Support
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
     * @see Factory#table(String, Object...)
     */
    @Support
    Table<Record> naturalJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
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
     * @see Factory#table(String)
     */
    @Support
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
     * @see Factory#table(String, Object...)
     */
    @Support
    Table<Record> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
     * @see Factory#table(String)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
     * @see Factory#table(String, Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Table<Record> naturalRightOuterJoin(String sql, Object... bindings);
}
