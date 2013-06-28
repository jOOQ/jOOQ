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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

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
public interface InsertOnDuplicateStep<R extends Record> extends InsertFinalStep<R>, InsertReturningStep<R> {

    /**
     * Add an <code>ON DUPLICATE KEY UPDATE</code> clause to this insert query.
     * <p>
     * This will try to <code>INSERT</code> a record. If there is a primary key
     * or unique key in this <code>INSERT</code> statement's affected table that
     * matches the value being inserted, then the <code>UPDATE</code> clause is
     * executed instead.
     * <p>
     * MySQL and CUBRID natively implements this type of clause. jOOQ can
     * simulate this clause using a <code>MERGE</code> statement on some other
     * databases. The conditions for a RDBMS to simulate this clause are:
     * <ul>
     * <li>The <code>INSERT</code> statement's table is a
     * {@link Table} with a {@link Table#getPrimaryKey()}</li>
     * <li>The RDBMS supports the <code>MERGE</code> clause (see
     * {@link DSLContext#mergeInto(Table)}).</li>
     * </ul>
     * <p>
     * These are the dialects that fulfill the above requirements:
     */
    @Support({ CUBRID, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
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
     * can be simulated as such:
     * <table border="1">
     * <tr>
     * <th>Dialect</th>
     * <th>Simulation</th>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#MARIADB}</td>
     * <td> <code><pre>INSERT IGNORE INTO ..</pre></code></td>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#MYSQL}</td>
     * <td> <code><pre>INSERT IGNORE INTO ..</pre></code></td>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#CUBRID}</td>
     * <td>
     * <code><pre>INSERT INTO .. ON DUPLICATE KEY UPDATE [any-field] = [any-field]</pre></code>
     * </td>
     * </tr>
     * <tr>
     * <td> {@link SQLDialect#DB2}<br/>
     * {@link SQLDialect#HSQLDB}<br/>
     * {@link SQLDialect#ORACLE}<br/>
     * {@link SQLDialect#SQLSERVER}<br/>
     * {@link SQLDialect#SYBASE}</td>
     * <td><code><pre>MERGE INTO [dst]
     * USING ([values]) src
     * ON [dst.key] = [src.key]
     * WHEN NOT MATCHED THEN INSERT ..</pre></code></td>
     * </tr>
     * </table>
     */
    @Support({ CUBRID, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, SQLSERVER, SYBASE })
    InsertFinalStep<R> onDuplicateKeyIgnore();
}
