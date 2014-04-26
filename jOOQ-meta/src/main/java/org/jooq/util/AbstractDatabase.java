/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util;

import static org.jooq.impl.DSL.falseCondition;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.Schema;
// ...

/**
 * A base implementation for all types of databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDatabase implements Database {

    private static final JooqLogger                                          log = JooqLogger.getLogger(AbstractDatabase.class);

    // -------------------------------------------------------------------------
    // Configuration elements
    // -------------------------------------------------------------------------

    private SQLDialect                                                       dialect;
    private Connection                                                       connection;
    private DSLContext                                                       create;
    private String[]                                                         excludes;
    private String[]                                                         includes;
    private boolean                                                          includeExcludeColumns;
    private String[]                                                         recordVersionFields;
    private String[]                                                         recordTimestampFields;
    private String[]                                                         overridePrimaryKeys;
    private boolean                                                          supportsUnsignedTypes;
    private boolean                                                          dateAsTimestamp;
    private List<Schema>                                                     configuredSchemata;
    private List<CustomType>                                                 configuredCustomTypes;
    private List<EnumType>                                                   configuredEnumTypes;
    private List<ForcedType>                                                 configuredForcedTypes;

    // -------------------------------------------------------------------------
    // Loaded definitions
    // -------------------------------------------------------------------------

    private List<String>                                                     inputSchemata;
    private List<SchemaDefinition>                                           schemata;
    private List<SequenceDefinition>                                         sequences;
    private List<IdentityDefinition>                                         identities;
    private List<UniqueKeyDefinition>                                        uniqueKeys;
    private List<ForeignKeyDefinition>                                       foreignKeys;
    private List<CheckConstraintDefinition>                                  checkConstraints;
    private List<TableDefinition>                                            tables;
    private List<EnumDefinition>                                             enums;
    private List<UDTDefinition>                                              udts;
    private List<ArrayDefinition>                                            arrays;
    private List<RoutineDefinition>                                          routines;
    private List<PackageDefinition>                                          packages;
    private Relations                                                        relations;

    private transient Map<SchemaDefinition, List<SequenceDefinition>>        sequencesBySchema;
    private transient Map<SchemaDefinition, List<IdentityDefinition>>        identitiesBySchema;
    private transient Map<SchemaDefinition, List<UniqueKeyDefinition>>       uniqueKeysBySchema;
    private transient Map<SchemaDefinition, List<ForeignKeyDefinition>>      foreignKeysBySchema;
    private transient Map<SchemaDefinition, List<CheckConstraintDefinition>> checkConstraintsBySchema;
    private transient Map<SchemaDefinition, List<TableDefinition>>           tablesBySchema;
    @SuppressWarnings("unused")
    private transient Map<SchemaDefinition, List<EnumDefinition>>            enumsBySchema;
    private transient Map<SchemaDefinition, List<UDTDefinition>>             udtsBySchema;
    private transient Map<SchemaDefinition, List<ArrayDefinition>>           arraysBySchema;
    private transient Map<SchemaDefinition, List<RoutineDefinition>>         routinesBySchema;
    private transient Map<SchemaDefinition, List<PackageDefinition>>         packagesBySchema;

    // Other caches
    private final Map<Table<?>, Boolean>                                     exists;

    protected AbstractDatabase() {
        exists = new HashMap<Table<?>, Boolean>();
    }

    @Override
    public final SQLDialect getDialect() {
        if (dialect == null) {
            dialect = create().configuration().dialect();
        }

        return dialect;
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
    public final DSLContext create() {
        if (create == null) {
            create = create0();
        }

        return create;
    }

    @Override
    public final boolean exists(Table<?> table) {
        Boolean result = exists.get(table);

        if (result == null) {
            try {
                create().selectOne().from(table).where(falseCondition()).fetch();
                result = true;
            }
            catch (DataAccessException e) {
                result = false;
            }

            exists.put(table, result);
        }

        return result;
    }

    @Override
    public final List<SchemaDefinition> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<SchemaDefinition>();

            try {
                schemata = getSchemata0();
            }
            catch (SQLException e) {
                log.error("Could not load schemata", e);
            }

            Iterator<SchemaDefinition> it = schemata.iterator();
            while (it.hasNext()) {
                if (!getInputSchemata().contains(it.next().getName())) {
                    it.remove();
                }
            }

            if (schemata.isEmpty()) {
                log.warn(
                    "No schemata were loaded",
                    "Please check your connection settings, and whether your database (and your database version!) is really supported by jOOQ. Also, check the case-sensitivity in your configured <inputSchema/> elements : " + inputSchemata);
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
        if (inputSchemata == null) {
            inputSchemata = new ArrayList<String>();

            // [#1312] Allow for ommitting inputSchema configuration. Generate
            // All schemata instead
            if (configuredSchemata.size() == 1 && StringUtils.isBlank(configuredSchemata.get(0).getInputSchema())) {
                try {
                    for (SchemaDefinition schema : getSchemata0()) {
                        inputSchemata.add(schema.getName());
                    }
                }
                catch (SQLException e) {
                    log.error("Could not load schemata", e);
                }
            }
            else {
                for (Schema schema : configuredSchemata) {
                    /* [pro] xx

                    xx xxxxxxx xxxxxx xxx xxxxxxxxxxxxxxxx xxxxxx xxxxxx
                    xx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxx x
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    x
                    xxxx
                    xx [/pro] */
                    {
                        inputSchemata.add(schema.getInputSchema());
                    }
                }
            }
        }

        return inputSchemata;
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
    public final void setIncludeExcludeColumns(boolean includeExcludeColumns) {
        this.includeExcludeColumns = includeExcludeColumns;
    }

    @Override
    public final boolean getIncludeExcludeColumns() {
        return includeExcludeColumns;
    }

    @Override
    public void setRecordVersionFields(String[] recordVersionFields) {
        this.recordVersionFields = recordVersionFields;
    }

    @Override
    public String[] getRecordVersionFields() {
        return recordVersionFields;
    }

    @Override
    public void setRecordTimestampFields(String[] recordTimestampFields) {
        this.recordTimestampFields = recordTimestampFields;
    }

    @Override
    public String[] getRecordTimestampFields() {
        return recordTimestampFields;
    }

    @Override
    public void setOverridePrimaryKeys(String[] overridePrimaryKeys) {
        this.overridePrimaryKeys = overridePrimaryKeys;
    }

    @Override
    public String[] getOverridePrimaryKeys() {
        return overridePrimaryKeys;
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

        if (sequencesBySchema == null) {
            sequencesBySchema = new LinkedHashMap<SchemaDefinition, List<SequenceDefinition>>();
        }

        return filterSchema(sequences, schema, sequencesBySchema);
    }

    @Override
    public final List<IdentityDefinition> getIdentities(SchemaDefinition schema) {
        if (identities == null) {
            identities = new ArrayList<IdentityDefinition>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    IdentityDefinition identity = table.getIdentity();

                    if (identity != null) {
                        identities.add(identity);
                    }
                }
            }
        }

        if (identitiesBySchema == null) {
            identitiesBySchema = new LinkedHashMap<SchemaDefinition, List<IdentityDefinition>>();
        }

        return filterSchema(identities, schema, identitiesBySchema);
    }



    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        if (uniqueKeys == null) {
            uniqueKeys = new ArrayList<UniqueKeyDefinition>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    for (UniqueKeyDefinition uniqueKey : table.getUniqueKeys()) {
                        uniqueKeys.add(uniqueKey);
                    }
                }
            }
        }

        if (uniqueKeysBySchema == null) {
            uniqueKeysBySchema = new LinkedHashMap<SchemaDefinition, List<UniqueKeyDefinition>>();
        }

        return filterSchema(uniqueKeys, schema, uniqueKeysBySchema);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema) {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<ForeignKeyDefinition>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    for (ForeignKeyDefinition foreignKey : table.getForeignKeys()) {
                        foreignKeys.add(foreignKey);
                    }
                }
            }
        }

        if (foreignKeysBySchema == null) {
            foreignKeysBySchema = new LinkedHashMap<SchemaDefinition, List<ForeignKeyDefinition>>();
        }

        return filterSchema(foreignKeys, schema, foreignKeysBySchema);
    }

    @Override
    public final List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema) {
        if (checkConstraints == null) {
            checkConstraints = new ArrayList<CheckConstraintDefinition>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    for (CheckConstraintDefinition checkConstraint : table.getCheckConstraints()) {
                        checkConstraints.add(checkConstraint);
                    }
                }
            }
        }

        if (checkConstraintsBySchema == null) {
            checkConstraintsBySchema = new LinkedHashMap<SchemaDefinition, List<CheckConstraintDefinition>>();
        }

        return filterSchema(checkConstraints, schema, checkConstraintsBySchema);
    }

    @Override
    public final List<TableDefinition> getTables(SchemaDefinition schema) {
        if (tables == null) {
            tables = new ArrayList<TableDefinition>();

            try {
                List<TableDefinition> t = getTables0();

                tables = filterExcludeInclude(t);
                log.info("Tables fetched", fetchedSize(t, tables));
            } catch (Exception e) {
                log.error("Error while fetching tables", e);
            }
        }

        if (tablesBySchema == null) {
            tablesBySchema = new LinkedHashMap<SchemaDefinition, List<TableDefinition>>();
        }

        return filterSchema(tables, schema, tablesBySchema);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name) {
        return getTable(schema, name, false);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getTables(schema), name, ignoreCase);
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
                @SuppressWarnings("resource")
                CSVReader reader = new CSVReader(new StringReader(literals));
                e.addLiterals(reader.readNext());
            } catch (IOException ignore) {}

            result.add(e);
        }

        return result;
    }

    @Override
    public final ForcedType getConfiguredForcedType(Definition definition) {
        return getConfiguredForcedType(definition, null);
    }

    @Override
    public final ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType) {
        for (ForcedType forcedType : getConfiguredForcedTypes()) {
            String expression = forcedType.getExpression();

            if (forcedType.getExpressions() != null) {
                expression = forcedType.getExpressions();
                log.warn("DEPRECATED", "The <expressions/> element in <forcedType/> is deprecated. Use <expression/> instead");
            }

            String types = forcedType.getTypes();
            boolean match = true;

            if (expression != null && !definition.getQualifiedName().matches(expression)) {
                match = false;
            }

            if (types != null && definedType != null && !definedType.getType().matches(types)) {
                match = false;
            }

            if (match) {
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

        if (arraysBySchema == null) {
            arraysBySchema = new LinkedHashMap<SchemaDefinition, List<ArrayDefinition>>();
        }

        return filterSchema(arrays, schema, arraysBySchema);
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

        if (udtsBySchema == null) {
            udtsBySchema = new LinkedHashMap<SchemaDefinition, List<UDTDefinition>>();
        }

        return filterSchema(udts, schema, udtsBySchema);
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

        if (routinesBySchema == null) {
            routinesBySchema = new LinkedHashMap<SchemaDefinition, List<RoutineDefinition>>();
        }

        return filterSchema(routines, schema, routinesBySchema);
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

        if (packagesBySchema == null) {
            packagesBySchema = new LinkedHashMap<SchemaDefinition, List<PackageDefinition>>();
        }

        return filterSchema(packages, schema, packagesBySchema);
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

    private final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema, Map<SchemaDefinition, List<T>> cache) {
        List<T> result = cache.get(schema);

        if (result == null) {
            result = filterSchema(definitions, schema);
            cache.put(schema, result);
        }

        return result;
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
        return filterExcludeInclude(definitions, excludes, includes);
    }

    static final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions, String[] excludes, String[] includes) {
        List<T> result = new ArrayList<T>();

        definitionsLoop: for (T definition : definitions) {
            if (excludes != null) {
                for (String exclude : excludes) {
                    Pattern p = Pattern.compile(exclude, Pattern.COMMENTS);

                    if (exclude != null &&
                            (p.matcher(definition.getName()).matches() ||
                             p.matcher(definition.getQualifiedName()).matches())) {

                        continue definitionsLoop;
                    }
                }
            }

            if (includes != null) {
                for (String include : includes) {
                    Pattern p = Pattern.compile(include, Pattern.COMMENTS);

                    if (include != null &&
                            (p.matcher(definition.getName()).matches() ||
                             p.matcher(definition.getQualifiedName()).matches())) {

                        result.add(definition);
                        continue definitionsLoop;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Retrieve ALL relations from the database.
     */
    protected final Relations getRelations0() {
        DefaultRelations result = new DefaultRelations();

        try {
            loadPrimaryKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching primary keys", e);
        }

        try {
            loadUniqueKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching unique keys", e);
        }

        try {
            loadForeignKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching foreign keys", e);
        }

        try {
            loadCheckConstraints(result);
        }
        catch (Exception e) {
            log.error("Error while fetching check constraints", e);
        }

        try {
            overridePrimaryKeys(result);
        }
        catch (Exception e) {
            log.error("Error while overriding primary keys", e);
        }

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

    static final String fetchedSize(List<?> fetched, List<?> included) {
        return fetched.size() + " (" + included.size() + " included, " + (fetched.size() - included.size()) + " excluded)";
    }

    private final void overridePrimaryKeys(DefaultRelations r) {
        List<UniqueKeyDefinition> allKeys = r.getUniqueKeys();
        List<UniqueKeyDefinition> filteredKeys = filterExcludeInclude(allKeys, null, overridePrimaryKeys);

        log.info("Overriding primary keys", fetchedSize(allKeys, filteredKeys));

        for (UniqueKeyDefinition key : filteredKeys) {
            r.overridePrimaryKey(key);
        }
    }

    /**
     * Create a new Factory
     */
    protected abstract DSLContext create0();

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
     * Retrieve <code>CHECK</code> constraints and store them to relations.
     */
    protected abstract void loadCheckConstraints(DefaultRelations r) throws SQLException;

    /**
     * Retrieve ALL schemata from the database. This will be filtered in
     * {@link #getSchemata()}
     */
    protected abstract List<SchemaDefinition> getSchemata0() throws SQLException;

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
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 2, 0, false, false);
        }
        else if (BigInteger.valueOf(Short.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 4, 0, false, false);
        }
        else if (BigInteger.valueOf(Integer.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 9, 0, false, false);
        }
        else if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 18, 0, false, false);
        }
        else {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 38, 0, false, false);
        }

        return type;
    }
}
