/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.RegexFlag;
import org.jooq.util.jaxb.Schema;

/**
 * A general database model.
 *
 * @author Lukas Eder
 */
public interface Database {

    /**
     * The schemata generated from this database.
     */
    List<SchemaDefinition> getSchemata();

    /**
     * Get a schema defined in this database by name.
     */
    SchemaDefinition getSchema(String name);

    /**
     * Retrieve the schema's primary key / foreign key relations.
     */
    Relations getRelations();

    /**
     * The sequences contained in this database.
     */
    List<SequenceDefinition> getSequences(SchemaDefinition schema);

    /**
     * The identities contained in this database.
     */
    List<IdentityDefinition> getIdentities(SchemaDefinition schema);

    /**
     * The unique keys contained in this database.
     */
    List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema);

    /**
     * The foreign keys contained in this database.
     */
    List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema);

    /**
     * The check constraints contained in this database.
     */
    List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema);

    /**
     * The tables contained in this database.
     */
    List<TableDefinition> getTables(SchemaDefinition schema);

    /**
     * Get a table in this database by name.
     */
    TableDefinition getTable(SchemaDefinition schema, String name);

    /**
     * Get a table in this database by name.
     */
    TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The enum UDTs defined in this database.
     */
    List<EnumDefinition> getEnums(SchemaDefinition schema);

    /**
     * Get an enum UDT defined in this database by name.
     */
    EnumDefinition getEnum(SchemaDefinition schema, String name);

    /**
     * Get an enum UDT defined in this database by name.
     */
    EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The domain UDTs defined in this database.
     */
    List<DomainDefinition> getDomains(SchemaDefinition schema);

    /**
     * Get an domain UDT defined in this database by name.
     */
    DomainDefinition getDomain(SchemaDefinition schema, String name);

    /**
     * Get an domain UDT defined in this database by name.
     */
    DomainDefinition getDomain(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The UDTs defined in this database.
     */
    List<UDTDefinition> getUDTs(SchemaDefinition schema);

    /**
     * Get a UDT defined in this database by name.
     */
    UDTDefinition getUDT(SchemaDefinition schema, String name);

    /**
     * Get a UDT defined in this database by name.
     */
    UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The Arrays defined in this database.
     */
    List<ArrayDefinition> getArrays(SchemaDefinition schema);

    /**
     * Get a ARRAY defined in this database by name.
     */
    ArrayDefinition getArray(SchemaDefinition schema, String name);

    /**
     * Get a ARRAY defined in this database by name.
     */
    ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The stored routines (procedures and functions) contained in this
     * database.
     */
    List<RoutineDefinition> getRoutines(SchemaDefinition schema);

    /**
     * The packages contained in this database.
     */
    List<PackageDefinition> getPackages(SchemaDefinition schema);

    /**
     * Initialise a connection to this database.
     */
    void setConnection(Connection connection);

    /**
     * The database connection.
     */
    Connection getConnection();

    /**
     * The input schemata are the schemata that jooq-meta is reading data from.
     */
    List<String> getInputSchemata();

    /**
     * The output schema is the schema used by jooq-codegen in class names.
     *
     * @deprecated - 2.0.5 - This will be implemented in each
     *             {@link Definition#getOutputName()}
     */
    @Deprecated
    String getOutputSchema(String inputSchema);

    /**
     * The input and output schemata.
     */
    void setConfiguredSchemata(List<Schema> schemata);

    /**
     * Database objects matching any of these regular expressions will not be
     * generated.
     */
    void setExcludes(String[] excludes);

    /**
     * Database objects matching any of these regular expressions will not be
     * generated.
     */
    String[] getExcludes();

    /**
     * Only database objects matching any of these regular expressions will be
     * generated.
     */
    void setIncludes(String[] includes);

    /**
     * Only database objects matching any of these regular expressions will be
     * generated.
     */
    String[] getIncludes();

    /**
     * Indicate whether include / exclude regular expression shall also match
     * database columns.
     */
    void setIncludeExcludeColumns(boolean includeExcludeColumns);

    /**
     * Indicate whether include / exclude regular expression shall also match
     * database columns.
     */
    boolean getIncludeExcludeColumns();

    /**
     * [#3488] Add an additional filter to the database that is applied in
     * addition to include / exclude.
     */
    void addFilter(Filter filter);

    /**
     * [#3488] The filters that are applied in addition to include / exclude.
     */
    List<Filter> getFilters();

    /**
     * Filter a list of definitions according to the exclude / include / and filter settings of this database.
     */
    <D extends Definition> List<D> filterExcludeInclude(List<D> definitions);

    /**
     * The regular expression flags that should be applied when using regular expressions.
     */
    void setRegexFlags(List<RegexFlag> regexFlags);

    /**
     * The regular expression flags that should be applied when using regular expressions.
     */
    List<RegexFlag> getRegexFlags();

    /**
     * Table columns matching these regular expressions will be considered as
     * record version fields in generated code.
     */
    void setRecordVersionFields(String[] recordVersionFields);

    /**
     * Table columns matching these regular expressions will be considered as
     * record version fields in generated code.
     */
    String[] getRecordVersionFields();

    /**
     * Table columns matching these regular expressions will be considered as
     * record timestamp fields in generated code.
     */
    void setRecordTimestampFields(String[] recordTimestampFields);

    /**
     * Table columns matching these regular expressions will be considered as
     * record timestamp fields in generated code.
     */
    String[] getRecordTimestampFields();

    /**
     * Columns matching these regular expressions will be considered as members
     * of synthetic primary keys in generated code.
     */
    void setSyntheticPrimaryKeys(String[] primaryKeys);

    /**
     * Columns matching these regular expressions will be considered as members
     * of synthetic primary keys in generated code.
     */
    String[] getSyntheticPrimaryKeys();

    /**
     * Unique keys matching these regular expressions will be considered as
     * primary keys in generated code.
     */
    void setOverridePrimaryKeys(String[] primaryKeys);

    /**
     * Unique keys matching these regular expressions will be considered as
     * primary keys in generated code.
     */
    String[] getOverridePrimaryKeys();

    /**
     * Database objects matching any of these field names will be generated as
     * custom types.
     */
    void setConfiguredCustomTypes(List<CustomType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * custom types.
     */
    List<CustomType> getConfiguredCustomTypes();

    /**
     * Get a specific configured custom type by its name.
     */
    CustomType getConfiguredCustomType(String name);

    /**
     * Database objects matching any of these field names will be generated as
     * enum types.
     */
    void setConfiguredEnumTypes(List<EnumType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * enum types.
     */
    List<EnumType> getConfiguredEnumTypes();

    /**
     * Database objects matching any of these field names will be generated as
     * forced types.
     */
    void setConfiguredForcedTypes(List<ForcedType> types);

    SchemaVersionProvider getSchemaVersionProvider();
    void setSchemaVersionProvider(SchemaVersionProvider provider);

    /**
     * Database objects matching any of these field names will be generated as
     * forced types.
     */
    List<ForcedType> getConfiguredForcedTypes();

    /**
     * Get the configured forced type object for any given {@link Definition},
     * or <code>null</code> if no {@link ForcedType} matches the definition.
     */
    ForcedType getConfiguredForcedType(Definition definition);

    /**
     * Get the configured forced type object for any given {@link Definition},
     * or <code>null</code> if no {@link ForcedType} matches the definition.
     */
    ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType);

    /**
     * Get the dialect for this database.
     */
    SQLDialect getDialect();

    /**
     * Create the factory for this database.
     */
    DSLContext create();

    /**
     * Check whether a type is an array type.
     */
    boolean isArrayType(String dataType);

    /**
     * Whether this database supports unsigned types.
     */
    void setSupportsUnsignedTypes(boolean supportsUnsignedTypes);

    /**
     * Whether this database supports unsigned types.
     */
    boolean supportsUnsignedTypes();

    /**
     * Whether this database should ignore procedure return values.
     */
    void setIgnoreProcedureReturnValues(boolean ignoreProcedureReturnValues);

    /**
     * Whether this database should ignore procedure return values.
     */
    boolean ignoreProcedureReturnValues();

    /**
     * Whether DATE columns should be treated as TIMESTAMP columns.
     */
    void setDateAsTimestamp(boolean dateAsTimestamp);

    /**
     * Whether DATE columns should be treated as TIMESTAMP columns.
     */
    boolean dateAsTimestamp();

    /**
     * [#3559] Whether relations (i.e. constraints) should be included in this database.
     */
    void setIncludeRelations(boolean includeRelations);

    /**
     * [#3559] Whether relations (i.e. constraints) should be included in this database.
     */
    boolean includeRelations();

    /**
     * Check for the existence of a table in the dictionary views.
     */
    boolean exists(Table<?> table);

    /**
     * Check for the existence of several tables in the dictionary views.
     */
    boolean existAll(Table<?>... tables);

    /**
     * Database properties.
     */
    void setProperties(Properties properties);

    /**
     * A filter type that can be used with {@link Database#addFilter(Filter)}
     */
    public interface Filter {

        /**
         * Whether to include an object in this database.
         */
        boolean exclude(Definition definition);
    }
}
