/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;

/**
 * A query that can be ordered and limited
 *
 * @author Lukas Eder
 */
public interface OrderProvider {

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
     *
     * @param fieldIndexes The ordering fields
     */
    @Support
    void addOrderBy(int... fieldIndexes);

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
     * Limit the results of this select
     *
     * @param offset The lowest offset starting at 0
     * @param numberOfRows The number of rows to return
     */
    @Support({DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE})
    void addLimit(int offset, int numberOfRows);

}
