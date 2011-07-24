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

package org.jooq.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jooq.SQLDialect;
import org.jooq.impl.JooqLogger;

/**
 * A base implementation for all types of databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDatabase implements Database {

    private static final JooqLogger log = JooqLogger.getLogger(AbstractDatabase.class);

    private Connection                      connection;
    private String                          schema;
    private String[]                        excludes;
    private String[]                        includes;
    private String[]                        masterDataTableNames;
    private Properties                      properties;

    private List<SequenceDefinition>        sequences;
    private List<TableDefinition>           tables;
    private List<MasterDataTableDefinition> masterDataTables;
    private List<EnumDefinition>            enums;
    private List<UDTDefinition>             udts;
    private List<ArrayDefinition>           arrays;
    private List<ProcedureDefinition>       procedures;
    private List<FunctionDefinition>        functions;
    private List<PackageDefinition>         packages;
    private Relations                       relations;



    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    @Override
    public final SQLDialect getDialect() {
        return create().getDialect();
    }

    @Override
    public final void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public final Connection getConnection() {
        return connection;
    }

    @Override
    public final void setSchemaName(String schema) {
        this.schema = schema;
    }

    @Override
    public final String getSchemaName() {
        return schema;
    }

    @Override
    public final SchemaDefinition getSchema() {
        return new SchemaDefinition(this, getSchemaName(), null);
    }

    @Override
    public final void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    @Override
    public final String[] getExcludes() {
        return excludes;
    }

    @Override
    public final void setIncludes(String[] includes) {
        this.includes = includes;
    }

    @Override
    public final String[] getIncludes() {
        return includes;
    }

    @Override
    public final void setMasterDataTableNames(String[] masterDataTableNames) {
        this.masterDataTableNames = masterDataTableNames;
    }

    @Override
    public final String[] getMasterDataTableNames() {
        return masterDataTableNames;
    }

    @Override
    public List<SequenceDefinition> getSequences() {
        if (sequences == null) {
            sequences = new ArrayList<SequenceDefinition>();

            try {
                List<SequenceDefinition> s = getSequences0();

                sequences = filterExcludeInclude(s);
                log.info("Sequences fetched", fetchedSize(s, sequences));
            } catch (Exception e) {
                log.error("Error while fetching sequences", e);
            }
        }

        return sequences;
    }

    @Override
    public final List<TableDefinition> getTables() {
        if (tables == null) {
            tables = new ArrayList<TableDefinition>();

            try {
                List<TableDefinition> t = filterMasterDataTables(getTables0(), false);

                tables = filterExcludeInclude(t);
                log.info("Tables fetched", fetchedSize(t, tables));
            } catch (Exception e) {
                log.error("Error while fetching tables", e);
            }
        }

        return tables;
    }

    @Override
    public final TableDefinition getTable(String name) {
        for (TableDefinition table : getTables()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }

        for (TableDefinition table : getMasterDataTables()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }

        return null;
    }

    @Override
    public final List<MasterDataTableDefinition> getMasterDataTables() {
        if (masterDataTables == null) {
            masterDataTables = new ArrayList<MasterDataTableDefinition>();

            try {
                List<MasterDataTableDefinition> t = filterMasterDataTables(getTables0(), true);
                masterDataTables = filterExcludeInclude(t);

                log.info("Masterdata tables fetched", fetchedSize(t, masterDataTables));
            } catch (Exception e) {
                log.error("Exception while fetching master data tables", e);
            }
        }

        return masterDataTables;
    }

    @Override
    public final MasterDataTableDefinition getMasterDataTable(String name) {
        for (MasterDataTableDefinition table : getMasterDataTables()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }

        return null;
    }

    @Override
    public final List<EnumDefinition> getEnums() {
        if (enums == null) {
            enums = new ArrayList<EnumDefinition>();

            try {
                List<EnumDefinition> e = getEnums0();

                enums = filterExcludeInclude(e);
                log.info("Enums fetched", fetchedSize(e, enums));
            } catch (Exception e) {
                log.error("Error while fetching enums", e);
            }
        }

        return enums;
    }

    @Override
    public final EnumDefinition getEnum(String name) {
        for (EnumDefinition e : getEnums()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }

        return null;
    }

    @Override
    public final List<ArrayDefinition> getArrays() {
        if (arrays == null) {
            arrays = new ArrayList<ArrayDefinition>();

            try {
                List<ArrayDefinition> a = getArrays0();

                arrays = filterExcludeInclude(a);
                log.info("ARRAYs fetched", fetchedSize(a, arrays));
            } catch (Exception e) {
                log.error("Error while fetching ARRAYS", e);
            }
        }

        return arrays;
    }

    @Override
    public final ArrayDefinition getArray(String name) {
        for (ArrayDefinition e : getArrays()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }

        return null;
    }

    @Override
    public final List<UDTDefinition> getUDTs() {
        if (udts == null) {
            udts = new ArrayList<UDTDefinition>();

            try {
                List<UDTDefinition> u = getUDTs0();

                udts = filterExcludeInclude(u);
                log.info("UDTs fetched", fetchedSize(u, udts));
            } catch (Exception e) {
                log.error("Error while fetching udts", e);
            }
        }

        return udts;
    }

    @Override
    public final UDTDefinition getUDT(String name) {
        for (UDTDefinition e : getUDTs()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }

        return null;
    }

    @Override
    public Relations getRelations() {
        if (relations == null) {
            try {
                relations = getRelations0();
            } catch (Exception e) {
                log.error("Error while fetching relations", e);
                relations = new DefaultRelations(this);
            }
        }

        return relations;
    }

    @Override
    public final List<ProcedureDefinition> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<ProcedureDefinition>();

            try {
                List<ProcedureDefinition> p = getProcedures0();

                procedures = filterExcludeInclude(p);
                log.info("Procedures fetched", fetchedSize(p, procedures));
            } catch (Exception e) {
                log.error("Error while fetching procedures", e);
            }
        }

        return procedures;
    }

    @Override
    public final List<FunctionDefinition> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<FunctionDefinition>();

            try {
                List<FunctionDefinition> f = getFunctions0();

                functions = filterExcludeInclude(f);
                log.info("Functions fetched", fetchedSize(f, functions));
            } catch (Exception e) {
                log.error("Error while fetching functions", e);
            }
        }

        return functions;
    }

    @Override
    public final List<PackageDefinition> getPackages() {
        if (packages == null) {
            packages = new ArrayList<PackageDefinition>();

            try {
                List<PackageDefinition> p = getPackages0();

                packages = filterExcludeInclude(p);
                log.info("Packages fetched", fetchedSize(p, packages));
            } catch (Exception e) {
                log.error("Error while fetching packages", e);
            }
        }

        return packages;
    }

    private final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions) {
        List<T> result = new ArrayList<T>();

        definitionsLoop: for (T definition : definitions) {
            for (String exclude : excludes) {
                if (exclude != null && definition.getName().matches(exclude.trim())) {
                    continue definitionsLoop;
                }
            }

            for (String include : includes) {
                if (include != null && definition.getName().matches(include.trim())) {
                    result.add(definition);
                    continue definitionsLoop;
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private final <T extends TableDefinition> List<T> filterMasterDataTables(List<TableDefinition> list, boolean include) {
        List<T> result = new ArrayList<T>();

        definitionLoop: for (TableDefinition definition : list) {
            for (String table : masterDataTableNames) {
                if (definition.getName().matches(table)) {

                    // If we have a match, then add the table only if master
                    // data tables are included in the result
                    if (include) {
                        result.add((T) new DefaultMasterDataTableDefinition(definition));
                    }

                    continue definitionLoop;
                }

            }

            // If we don't have any match, then add the table only if
            // master data tables are excluded in the result
            if (!include) {
                result.add((T) definition);
            }
        }

        return result;
    }

    /**
     * Retrieve ALL relations from the database.
     */
    protected final Relations getRelations0() throws SQLException {
        DefaultRelations result = new DefaultRelations(this);

        loadPrimaryKeys(result);
        loadUniqueKeys(result);
        loadForeignKeys(result);

        return result;
    }

    @Override
    public final boolean isArrayType(String dataType) {
        switch (getDialect()) {
            case POSTGRES:
            case H2:
                return "ARRAY".equals(dataType);
            case HSQLDB:
                return dataType.endsWith("ARRAY");
        }

        return false;
    }

    private final String fetchedSize(List<?> fetched, List<?> included) {
        return fetched.size() + " (" + included.size() + " included, " + (fetched.size() - included.size()) + " excluded)";
    }

    /**
     * Retrieve primary keys and store them to relations
     */
    protected abstract void loadPrimaryKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve non-primary unique keys and store them to relations
     */
    protected abstract void loadUniqueKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve foreign keys and store them to relations. Unique keys are
     * already loaded.
     */
    protected abstract void loadForeignKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve ALL sequences from the database. This will be filtered in
     * {@link #getTables()}
     */
    protected abstract List<SequenceDefinition> getSequences0() throws SQLException;

    /**
     * Retrieve ALL tables from the database. This will be filtered in
     * {@link #getTables()}
     */
    protected abstract List<TableDefinition> getTables0() throws SQLException;

    /**
     * Retrieve ALL stored procedures from the database. This will be filtered
     * in {@link #getProcedures()}
     */
    protected abstract List<ProcedureDefinition> getProcedures0() throws SQLException;

    /**
     * Retrieve ALL stored functions from the database. This will be filtered in
     * {@link #getFunctions()}
     */
    protected abstract List<FunctionDefinition> getFunctions0() throws SQLException;

    /**
     * Retrieve ALL packages from the database. This will be filtered in
     * {@link #getPackages()}
     */
    protected abstract List<PackageDefinition> getPackages0() throws SQLException;

    /**
     * Retrieve ALL enum UDTs from the database. This will be filtered in
     * {@link #getEnums()}
     */
    protected abstract List<EnumDefinition> getEnums0() throws SQLException;

    /**
     * Retrieve ALL UDTs from the database. This will be filtered in
     * {@link #getEnums()}
     */
    protected abstract List<UDTDefinition> getUDTs0() throws SQLException;

    /**
     * Retrieve ALL ARRAYs from the database. This will be filtered in
     * {@link #getArrays()}
     */
    protected abstract List<ArrayDefinition> getArrays0() throws SQLException;
}
