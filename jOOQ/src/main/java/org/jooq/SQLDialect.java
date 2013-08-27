/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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

//  /**
//   * The MS Access SQL dialect family. ACCESS support will be added in jOOQ 3.3
//   */
//  ACCESS("Access"),

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
     * The Oracle 10g dialect.
     */
    ORACLE10G("Oracle", ORACLE),

    /**
     * The Oracle 11g dialect.
     */
    ORACLE11G("Oracle", ORACLE),

    /**
     * The Oracle 12c dialect.
     */
    ORACLE12C("Oracle", ORACLE),

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
        return name == null ? null : name.toLowerCase();
    }

    /**
     * The name of this dialect as it appears in related enum values.
     */
    public final String getNameUC() {
        return name == null ? null : name.toUpperCase();
    }
}
