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

/**
 * Dialects and dialect families as supported by jOOQ.
 * <p>
 * jOOQ supports a variety of dialects, which are grouped into dialect families.
 * For instance, the SQL Server dialect family {@link #SQLSERVER} is specialised
 * by its dialects
 * <ul>
 * <li> {@link #SQLSERVER2008}</li>
 * <li> {@link #SQLSERVER2012}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public enum SQLDialect {

    /**
     * The standard SQL dialect family.
     *
     * @deprecated - Do not reference this pseudo-dialect. It is only used for
     *             unit testing
     */
    @Deprecated
    SQL99(null),

    /**
     * The Sybase Adaptive Server SQL dialect family.
     */
    ASE("ASE"),

    /**
     * The CUBRID SQL dialect family.
     */
    CUBRID("CUBRID"),

    /**
     * The IBM DB2 SQL dialect family.
     */
    DB2("DB2"),

    /**
     * The Apache Derby SQL dialect family.
     */
    DERBY("Derby"),

    /**
     * The Firebird SQL dialect family.
     */
    FIREBIRD("Firebird"),

    /**
     * The H2 SQL dialect family.
     */
    H2("H2"),

    /**
     * The Hypersonic SQL dialect family.
     */
    HSQLDB("HSQLDB"),

    /**
     * The Ingres dialect family.
     */
    INGRES("Ingres"),

    /**
     * The MariaDB dialect family.
     */
    MARIADB("MariaDB"),

    /**
     * The MySQL dialect family.
     */
    MYSQL("MySQL"),

    /**
     * The Oracle dialect family.
     */
    ORACLE("Oracle"),

    /**
     * The PostgreSQL dialect family.
     */
    POSTGRES("Postgres"),

    /**
     * The SQLite dialect family.
     */
    SQLITE("SQLite"),

    /**
     * The SQL Server dialect family.
     */
    SQLSERVER("SQLServer"),

    /**
     * The SQL Server 2008 dialect.
     */
    SQLSERVER2008("SQLServer", SQLSERVER),

    /**
     * The SQL Server 2012 dialect.
     */
    SQLSERVER2012("SQLServer", SQLSERVER),

    /**
     * The Sybase SQL Anywhere dialect family.
     */
    SYBASE("Sybase");

    private final String     name;
    private final SQLDialect family;

    private SQLDialect(String name) {
        this(name, null);
    }

    private SQLDialect(String name, SQLDialect family) {
        this.name = name;
        this.family = family;
    }

    /**
     * The dialect family.
     * <p>
     * This returns the dialect itself, if it has no "parent family". E.g.
     * <code><pre>
     * SQLSERVER == SQLSERVER2012.family();
     * SQLSERVER == SQLSERVER2008.family();
     * SQLSERVER == SQLSERVER.family();
     * </pre></code>
     */
    public final SQLDialect family() {
        return family == null ? this : family;
    }

    /**
     * The name of this dialect as it appears in related class names.
     */
    public final String getName() {
        return name;
    }

    /**
     * The name of this dialect as it appears in related package names.
     */
    public final String getNameLC() {
        return name.toLowerCase();
    }

    /**
     * The name of this dialect as it appears in related enum values.
     */
    public final String getNameUC() {
        return name.toUpperCase();
    }
}
