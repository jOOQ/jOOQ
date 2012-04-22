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

import org.jooq.impl.Factory;
import org.jooq.util.ase.ASEFactory;
import org.jooq.util.cubrid.CUBRIDFactory;
import org.jooq.util.db2.DB2Factory;
import org.jooq.util.derby.DerbyFactory;
import org.jooq.util.h2.H2Factory;
import org.jooq.util.hsqldb.HSQLDBFactory;
import org.jooq.util.ingres.IngresFactory;
import org.jooq.util.mysql.MySQLFactory;
import org.jooq.util.oracle.OracleFactory;
import org.jooq.util.postgres.PostgresFactory;
import org.jooq.util.sqlite.SQLiteFactory;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.jooq.util.sybase.SybaseFactory;

/**
 * This enumeration lists all supported dialects.
 *
 * @author Lukas Eder
 */
public enum SQLDialect {
    /**
     * The standard SQL dialect.
     *
     * @deprecated - Do not reference this pseudo-dialect. It is only used for
     *             unit testing
     */
    @Deprecated
    SQL99(null, null),

    /**
     * The Sybase Adaptive Server SQL dialect
     */
    ASE("ASE", ASEFactory.class),

    /**
     * The CUBRID SQL dialect
     */
    CUBRID("CUBRID", CUBRIDFactory.class),

    /**
     * The IBM DB2 SQL dialect
     */
    DB2("DB2", DB2Factory.class),

    /**
     * The Apache Derby SQL dialect
     */
    DERBY("Derby", DerbyFactory.class),

    /**
     * The H2 SQL dialect
     */
    H2("H2", H2Factory.class),

    /**
     * The Hypersonic SQL dialect
     */
    HSQLDB("HSQLDB", HSQLDBFactory.class),

    /**
     * The Ingres dialect
     */
    INGRES("Ingres", IngresFactory.class),

    /**
     * The MySQL dialect
     */
    MYSQL("MySQL", MySQLFactory.class),

    /**
     * The Oracle dialect
     */
    ORACLE("Oracle", OracleFactory.class),

    /**
     * The PostGres dialect
     */
    POSTGRES("Postgres", PostgresFactory.class),

    /**
     * The SQLite dialect
     */
    SQLITE("SQLite", SQLiteFactory.class),

    /**
     * The SQL Server dialect
     */
    SQLSERVER("SQLServer", SQLServerFactory.class),

    /**
     * The Sybase dialect
     */
    SYBASE("Sybase", SybaseFactory.class);

    private final String                   name;
    private final Class<? extends Factory> factory;

    private SQLDialect(String name, Class<? extends Factory> factory) {
        this.name = name;
        this.factory = factory;
    }

    public final String getName() {
        return name;
    }

    public final Class<? extends Factory> getFactory() {
        return factory;
    }
}
