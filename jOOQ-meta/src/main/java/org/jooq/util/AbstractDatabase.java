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

package org.jooq.util;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.SQLDialect;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.MasterDataTable;
import org.jooq.util.jaxb.Schema;

/**
 * A base implementation for all types of databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDatabase implements Database {

    private static final JooqLogger log = JooqLogger.getLogger(AbstractDatabase.class);

    // -------------------------------------------------------------------------
    // Configuration elements
    // -------------------------------------------------------------------------

    private Connection                      connection;
    private String[]                        excludes;
    private String[]                        includes;
    private boolean                         supportsUnsignedTypes;
    private boolean                         dateAsTimestamp;
    private List<Schema>                    configuredSchemata;
    private List<MasterDataTable>           configuredMasterDataTables;
    private List<CustomType>                configuredCustomTypes;
    private List<EnumType>                  configuredEnumTypes;
    private List<ForcedType>                configuredForcedTypes;

    // -------------------------------------------------------------------------
    // Loaded definitions
    // -------------------------------------------------------------------------

    private List<SchemaDefinition>          schemata;
    private List<SequenceDefinition>        sequences;
    private List<TableDefinition>           tables;
    private List<MasterDataTableDefinition> masterDataTables;
    private List<EnumDefinition>            enums;
    private List<UDTDefinition>             udts;
    private List<ArrayDefinition>           arrays;
    private List<RoutineDefinition>         routines;
    private List<PackageDefinition>         packages;
    private Relations                       relations;



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
    public final List<SchemaDefinition> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<SchemaDefinition>();

            for (String name : getInputSchemata()) {
                schemata.add(new SchemaDefinition(this, name, null));
            }
        }

        return schemata;
    }

    @Override
    public final SchemaDefinition getSchema(String inputName) {
        for (SchemaDefinition schema : getSchemata()) {
            if (schema.getName().equals(inputName)) {
                return schema;
            }
        }

        return null;
    }

    @Override
    public final List<String> getInputSchemata() {
        List<String> result = new ArrayList<String>();

        for (Schema schema : configuredSchemata) {
            result.add(schema.getInputSchema());
        }

        return result;
    }

    @Override
    @Deprecated
    public String getOutputSchema(String inputSchema) {
        for (Schema schema : configuredSchemata) {
            if (inputSchema.equals(schema.getInputSchema())) {
                return schema.getOutputSchema();
            }
        }

        return inputSchema;
    }

    @Override
    public final void setConfiguredSchemata(List<Schema> schemata) {
        this.configuredSchemata = schemata;
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
    public final void setConfiguredMasterDataTables(List<MasterDataTable> configuredMasterDataTables) {
        this.configuredMasterDataTables = configuredMasterDataTables;
    }

    @Override
    public final List<MasterDataTable> getConfiguredMasterDataTables() {
        return configuredMasterDataTables;
    }

    @Override
    public final void setConfiguredEnumTypes(List<EnumType> configuredEnumTypes) {
        this.configuredEnumTypes = configuredEnumTypes;
    }

    @Override
    public final List<EnumType> getConfiguredEnumTypes() {
        return configuredEnumTypes;
    }

    @Override
    public final void setConfiguredCustomTypes(List<CustomType> configuredCustomTypes) {
        this.configuredCustomTypes = configuredCustomTypes;
    }

    @Override
    public final List<CustomType> getConfiguredCustomTypes() {
        return configuredCustomTypes;
    }

    @Override
    public final CustomType getConfiguredCustomType(String name) {
        for (CustomType type : configuredCustomTypes) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        return null;
    }

    @Override
    public final void setConfiguredForcedTypes(List<ForcedType> configuredForcedTypes) {
        this.configuredForcedTypes = configuredForcedTypes;
    }

    @Override
    public final List<ForcedType> getConfiguredForcedTypes() {
        return configuredForcedTypes;
    }

    @Override
    public final void setSupportsUnsignedTypes(boolean supportsUnsignedTypes) {
        this.supportsUnsignedTypes = supportsUnsignedTypes;
    }

    @Override
    public final boolean supportsUnsignedTypes() {
        return supportsUnsignedTypes;
    }

    @Override
    public final void setDateAsTimestamp(boolean dateAsTimestamp) {
        this.dateAsTimestamp = dateAsTimestamp;
    }

    @Override
    public final boolean dateAsTimestamp() {
        return dateAsTimestamp;
    }

    @Override
    public final List<SequenceDefinition> getSequences(SchemaDefinition schema) {
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

        return filterSchema(sequences, schema);
    }

    @Override
    public final List<TableDefinition> getTables(SchemaDefinition schema) {
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

        return filterSchema(tables, schema);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name) {
        return getTable(schema, name, false);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase) {
        TableDefinition result = null;

        result = getDefinition(getTables(schema), name, ignoreCase);
        if (result == null) {
            result = getDefinition(getMasterDataTables(schema), name, ignoreCase);
        }

        return result;
    }

    @Override
    public final List<MasterDataTableDefinition> getMasterDataTables(SchemaDefinition schema) {
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

        return filterSchema(masterDataTables, schema);
    }

    @Override
    public final MasterDataTableDefinition getMasterDataTable(SchemaDefinition schema, String name) {
        return getMasterDataTable(schema, name, false);
    }

    @Override
    public final MasterDataTableDefinition getMasterDataTable(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getMasterDataTables(schema), name, ignoreCase);
    }

    @Override
    public final List<EnumDefinition> getEnums(SchemaDefinition schema) {
        if (enums == null) {
            enums = new ArrayList<EnumDefinition>();

            try {
                List<EnumDefinition> e = getEnums0();

                enums = filterExcludeInclude(e);
                enums.addAll(getConfiguredEnums());

                log.info("Enums fetched", fetchedSize(e, enums));
            } catch (Exception e) {
                log.error("Error while fetching enums", e);
            }
        }

        return enums;
    }

    private final List<EnumDefinition> getConfiguredEnums() {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        for (EnumType enumType : configuredEnumTypes) {
            String name = enumType.getName();
            DefaultEnumDefinition e = new DefaultEnumDefinition(getSchemata().get(0), name, null, true);

            String literals = enumType.getLiterals();

            try {
                CSVReader reader = new CSVReader(new StringReader(literals));
                e.addLiterals(reader.readNext());
            } catch (IOException ignore) {}

            result.add(e);
        }

        return result;
    }

    @Override
    public final ForcedType getConfiguredForcedType(Definition definition) {
        for (ForcedType forcedType : getConfiguredForcedTypes()) {
            if (definition.getQualifiedName().matches(forcedType.getExpressions())) {
                return forcedType;
            }
        }

        return null;
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, String name) {
        return getEnum(schema, name, false);
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getEnums(schema), name, ignoreCase);
    }

    @Override
    public final List<ArrayDefinition> getArrays(SchemaDefinition schema) {
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

        return filterSchema(arrays, schema);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, String name) {
        return getArray(schema, name, false);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getArrays(schema), name, ignoreCase);
    }

    @Override
    public final List<UDTDefinition> getUDTs(SchemaDefinition schema) {
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

        return filterSchema(udts, schema);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, String name) {
        return getUDT(schema, name, false);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getUDTs(schema), name, ignoreCase);
    }

    @Override
    public final Relations getRelations() {
        if (relations == null) {
            try {
                relations = getRelations0();
            } catch (Exception e) {
                log.error("Error while fetching relations", e);
                relations = new DefaultRelations();
            }
        }

        return relations;
    }

    @Override
    public final List<RoutineDefinition> getRoutines(SchemaDefinition schema) {
        if (routines == null) {
            routines = new ArrayList<RoutineDefinition>();

            try {
                List<RoutineDefinition> r = getRoutines0();

                routines = filterExcludeInclude(r);
                log.info("Routines fetched", fetchedSize(r, routines));
            } catch (Exception e) {
                log.error("Error while fetching functions", e);
            }
        }

        return filterSchema(routines, schema);
    }

    @Override
    public final List<PackageDefinition> getPackages(SchemaDefinition schema) {
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

        return filterSchema(packages, schema);
    }

    static final <D extends Definition> D getDefinition(List<D> definitions, String name, boolean ignoreCase) {
        for (D definition : definitions) {
            if ((ignoreCase && definition.getName().equalsIgnoreCase(name)) ||
                (!ignoreCase && definition.getName().equals(name))) {

                return definition;
            }
        }

        return null;
    }

    private final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema) {
        if (schema == null) {
            return definitions;
        }
        else {
            List<T> result = new ArrayList<T>();

            for (T definition : definitions) {
                if (definition.getSchema().equals(schema)) {
                    result.add(definition);
                }
            }

            return result;
        }
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
            for (MasterDataTable table : configuredMasterDataTables) {
                if (definition.getName().equals(table.getName())) {

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
        DefaultRelations result = new DefaultRelations();

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
     * {@link #getTables(SchemaDefinition)}
     */
    protected abstract List<SequenceDefinition> getSequences0() throws SQLException;

    /**
     * Retrieve ALL tables from the database. This will be filtered in
     * {@link #getTables(SchemaDefinition)}
     */
    protected abstract List<TableDefinition> getTables0() throws SQLException;

    /**
     * Retrieve ALL stored routines (functions and procedures) from the
     * database. This will be filtered in {@link #getRoutines(SchemaDefinition)}
     */
    protected abstract List<RoutineDefinition> getRoutines0() throws SQLException;

    /**
     * Retrieve ALL packages from the database. This will be filtered in
     * {@link #getPackages(SchemaDefinition)}
     */
    protected abstract List<PackageDefinition> getPackages0() throws SQLException;

    /**
     * Retrieve ALL enum UDTs from the database. This will be filtered in
     * {@link #getEnums(SchemaDefinition)}
     */
    protected abstract List<EnumDefinition> getEnums0() throws SQLException;

    /**
     * Retrieve ALL UDTs from the database. This will be filtered in
     * {@link #getEnums(SchemaDefinition)}
     */
    protected abstract List<UDTDefinition> getUDTs0() throws SQLException;

    /**
     * Retrieve ALL ARRAYs from the database. This will be filtered in
     * {@link #getArrays(SchemaDefinition)}
     */
    protected abstract List<ArrayDefinition> getArrays0() throws SQLException;

    /**
     * Get the data type considering a known max value
     */
    protected final DataTypeDefinition getDataTypeForMAX_VAL(SchemaDefinition schema, BigInteger value) {
        DataTypeDefinition type;

        if (BigInteger.valueOf(Byte.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 2, 0);
        }
        else if (BigInteger.valueOf(Short.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 4, 0);
        }
        else if (BigInteger.valueOf(Integer.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 9, 0);
        }
        else if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 18, 0);
        }
        else {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 38, 0);
        }

        return type;
    }
}
