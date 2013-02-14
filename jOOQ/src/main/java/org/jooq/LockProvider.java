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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

/**
 * A query part (mostly a {@link Select} statement) providing the possibility of
 * locking tables, rows using a <code>FOR UPDATE</code> clause
 *
 * @author Lukas Eder
 * @deprecated - 2.6.0 [#1881] - This type will be removed from the public API,
 *             soon. Its methods will be pushed down into extending interfaces.
 *             Do not reference this type directly.
 */
@Deprecated
public interface LockProvider {

    /**
     * Sets the "FOR UPDATE" flag onto the query
     * <p>
     * <h5>Native implementation</h5> This has been observed to be supported by
     * any of these dialects:
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
     * <h5>Simulation</h5> These dialects can simulate the
     * <code>FOR UPDATE</code> clause using a cursor. The cursor is handled by
     * the JDBC driver, at {@link PreparedStatement} construction time, when
     * calling {@link Connection#prepareStatement(String, int, int)} with
     * {@link ResultSet#CONCUR_UPDATABLE}. jOOQ handles simulation of a
     * <code>FOR UPDATE</code> clause using <code>CONCUR_UPDATABLE</code> for
     * these dialects:
     * <ul>
     * <li> {@link SQLDialect#CUBRID}</li>
     * <li> {@link SQLDialect#SQLSERVER}</li>
     * </ul>
     * <p>
     * Note: This simulation may not be efficient for large result sets!
     * <h5>Not supported</h5> These dialects are known not to support the
     * <code>FOR UPDATE</code> clause in regular SQL:
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
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
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
    @Support(ORACLE)
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
    @Support({ MYSQL, POSTGRES })
    void setForShare(boolean forShare);
}
