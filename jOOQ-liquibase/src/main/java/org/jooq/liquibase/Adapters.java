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
package org.jooq.liquibase;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.FirebirdDatabase;
import liquibase.database.core.H2Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.core.SQLiteDatabase;
import liquibase.database.core.SybaseASADatabase;
import liquibase.database.core.SybaseDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.database.structure.View;

import org.jooq.SQLDialect;

/**
 * A set of adapters to wrap Liquibase types in jOOQ types.
 * <p>
 * This class provides a set of adapter methods that take Liquibase type
 * arguments wrapping them in the most appropriate jOOQ type.
 *
 * @author Lukas Eder
 */
public class Adapters {

    /**
     * Extract the jOOQ {@link SQLDialect} from a Liquibase column.
     */
    public static SQLDialect dialect(Column column) {
        View view = column.getView();

        if (view != null) {
            return dialect(view);
        }

        return dialect(column.getTable());
    }

    /**
     * Extract the jOOQ {@link SQLDialect} from a Liquibase view.
     */
    public static SQLDialect dialect(View view) {
        return dialect(view.getDatabase());
    }

    /**
     * Extract the jOOQ {@link SQLDialect} from a Liquibase table.
     */
    public static SQLDialect dialect(Table table) {
        return dialect(table.getDatabase());
    }

    /**
     * Extract the jOOQ {@link SQLDialect} from a Liquibase.
     */
    @SuppressWarnings("deprecation")
    public static SQLDialect dialect(Database database) {
        if (database instanceof DB2Database)
            return SQLDialect.DB2;
        if (database instanceof DerbyDatabase)
            return SQLDialect.DERBY;
        if (database instanceof FirebirdDatabase)
            return SQLDialect.FIREBIRD;
        if (database instanceof H2Database)
            return SQLDialect.H2;
        if (database instanceof HsqlDatabase)
            return SQLDialect.HSQLDB;
        // if (database instanceof InformixDatabase) return SQLDialect.INFORMIX;
        // if (database instanceof MaxDBDatabase) return SQLDialect.MAXDB;
        if (database instanceof MSSQLDatabase)
            return SQLDialect.SQLSERVER;
        if (database instanceof MySQLDatabase)
            return SQLDialect.MYSQL;
        if (database instanceof OracleDatabase)
            return SQLDialect.ORACLE;
        if (database instanceof PostgresDatabase)
            return SQLDialect.POSTGRES;
        if (database instanceof SQLiteDatabase)
            return SQLDialect.SQLITE;
        if (database instanceof SybaseASADatabase)
            return SQLDialect.ASE;
        if (database instanceof SybaseDatabase)
            return SQLDialect.SYBASE;

        return SQLDialect.SQL99;
    }

    /**
     * Extract the jOOQ {@link Table} from a Liquibase table.
     */
    public static org.jooq.Table<?> table(Table table) {
        return new LiquibaseTable(table);
    }

    /**
     * Extract the jOOQ {@link Table} from a Liquibase view.
     */
    public static org.jooq.Table<?> table(View view) {
        return new LiquibaseTable(view);
    }
}
