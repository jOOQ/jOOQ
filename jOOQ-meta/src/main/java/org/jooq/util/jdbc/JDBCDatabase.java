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

package org.jooq.util.jdbc;

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
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;

/**
 * The JDBC database
 *
 * @author Lukas Eder
 */
public class JDBCDatabase extends AbstractDatabase {

    private List<Schema> schemas;

    @Override
    protected DSLContext create0() {
        Connection connection = getConnection();
        return DSL.using(connection, dialect(connection));
    }

    /**
     * Method merged from jOOQ 3.1's {@link JDBCUtils}
     */
    @SuppressWarnings("deprecation")
    static final SQLDialect dialect(Connection connection) {
        SQLDialect result = SQLDialect.SQL99;

        try {
            String url = connection.getMetaData().getURL();
            result = dialect(url);
        }
        catch (SQLException ignore) {}

        if (result == SQLDialect.SQL99) {
            // If the dialect cannot be guessed from the URL, take some other
            // measures, e.g. by querying DatabaseMetaData.getDatabaseProductName()
        }

        return result;
    }

    /**
     * Method merged from jOOQ 3.1's {@link JDBCUtils}
     */
    @SuppressWarnings("deprecation")
    static final SQLDialect dialect(String url) {

        // The below list might not be accurate or complete. Feel free to
        // contribute fixes related to new / different JDBC driver configuraitons
        if (url.startsWith("jdbc:jtds:sybase:")) {
            return ASE;
        }
        else if (url.startsWith("jdbc:cubrid:")) {
            return CUBRID;
        }
        else if (url.startsWith("jdbc:db2:")) {
            return DB2;
        }
        else if (url.startsWith("jdbc:derby:")) {
            return DERBY;
        }
        else if (url.startsWith("jdbc:firebirdsql:")) {
            return FIREBIRD;
        }
        else if (url.startsWith("jdbc:h2:")) {
            return H2;
        }
        else if (url.startsWith("jdbc:hsqldb:")) {
            return HSQLDB;
        }
        else if (url.startsWith("jdbc:ingres:")) {
            return INGRES;
        }
        else if (url.startsWith("jdbc:mysql:")
              || url.startsWith("jdbc:google:")) {
            return MYSQL;
        }
        else if (url.startsWith("jdbc:oracle:")
              || url.startsWith("jdbc:oracle:oci")) {
            return ORACLE;
        }
        else if (url.startsWith("jdbc:postgresql:")) {
            return POSTGRES;
        }
        else if (url.startsWith("jdbc:sqlite:")) {
            return SQLITE;
        }
        else if (url.startsWith("jdbc:sqlserver:")
              || url.startsWith("jdbc:jtds:sqlserver:")
              || url.startsWith("jdbc:microsoft:sqlserver:")
              || url.contains(":mssql")) {
            return SQLSERVER;
        }
        else if (url.startsWith("jdbc:sybase:")) {
            return SYBASE;
        }

        return SQLDialect.SQL99;
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            result.add(new SchemaDefinition(this, schema.getName(), ""));
        }

        return result;
    }

    private List<Schema> getSchemasFromMeta() {
        if (schemas == null) {
            schemas = create().meta().getSchemas();
        }

        return schemas;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            for (Sequence<?> sequence : schema.getSequences()) {
                SchemaDefinition sd = getSchema(schema.getName());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    sd,
                    sequence.getDataType().getTypeName(), 0, 0, 0);

                result.add(new DefaultSequenceDefinition(
                    sd, sequence.getName(), type));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            SchemaDefinition sd = getSchema(schema.getName());

            if (sd != null) {
                for (Table<?> table : schema.getTables()) {
                    result.add(new JDBCTableDefinition(sd, table));
                }
            }
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
