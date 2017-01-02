/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.util;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.util.jaxb.Catalog;
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
     * The catalogs generated from this database.
     */
    List<CatalogDefinition> getCatalogs();

    /**
     * Get a catalog defined in this database by name.
     */
    CatalogDefinition getCatalog(String name);

    /**
     * The schemata generated from this database.
     */
    List<SchemaDefinition> getSchemata();

    /**
     * The schemata generated from this database and a given catalog.
     */
    List<SchemaDefinition> getSchemata(CatalogDefinition catalog);

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
     * Get a table in this database by name.
     */
    TableDefinition getTable(SchemaDefinition schema, Name name);

    /**
     * Get a table in this database by name.
     */
    TableDefinition getTable(SchemaDefinition schema, Name name, boolean ignoreCase);

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
     * Get an enum UDT defined in this database by name.
     */
    EnumDefinition getEnum(SchemaDefinition schema, Name name);

    /**
     * Get an enum UDT defined in this database by name.
     */
    EnumDefinition getEnum(SchemaDefinition schema, Name name, boolean ignoreCase);

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
     * Get an domain UDT defined in this database by name.
     */
    DomainDefinition getDomain(SchemaDefinition schema, Name name);

    /**
     * Get an domain UDT defined in this database by name.
     */
    DomainDefinition getDomain(SchemaDefinition schema, Name name, boolean ignoreCase);

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
     * Get a UDT defined in this database by name.
     */
    UDTDefinition getUDT(SchemaDefinition schema, Name name);

    /**
     * Get a UDT defined in this database by name.
     */
    UDTDefinition getUDT(SchemaDefinition schema, Name name, boolean ignoreCase);

    /**
     * The UDTs defined in this database.
     */
    List<UDTDefinition> getUDTs(PackageDefinition pkg);

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
     * Get a ARRAY defined in this database by name.
     */
    ArrayDefinition getArray(SchemaDefinition schema, Name name);

    /**
     * Get a ARRAY defined in this database by name.
     */
    ArrayDefinition getArray(SchemaDefinition schema, Name name, boolean ignoreCase);

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
     * Get a package defined in this database by name.
     */
    PackageDefinition getPackage(SchemaDefinition schema, String inputName);

    /**
     * Initialise a connection to this database.
     */
    void setConnection(Connection connection);

    /**
     * The database connection.
     */
    Connection getConnection();

    /**
     * The input catalogs are the catalogs that jooq-meta is reading data from.
     */
    List<String> getInputCatalogs();

    /**
     * The input schemata are the schemata from all catalogs that jooq-meta is
     * reading data from.
     * <p>
     * This will combine the schemata from all catalogs in a single list. If
     * you're working with a multi-catalog environment, you may want to call
     * {@link #getInputSchemata(String)} instead to disambiguate schema names
     * (e.g. in SQL Server, there are multiple "dbo" schemas).
     */
    List<String> getInputSchemata();

    /**
     * The input schemata are the schemata from a given catalog that jooq-meta is reading data from.
     */
    List<String> getInputSchemata(CatalogDefinition catalog);

    /**
     * The input schemata are the schemata from a given catalog that jooq-meta is reading data from.
     */
    List<String> getInputSchemata(String catalog);

    /**
     * The output catalog is the catalog used by jooq-codegen in class names.
     *
     * @deprecated - 2.0.5 - This will be implemented in each
     *             {@link Definition#getOutputName()}
     */
    @Deprecated
    String getOutputCatalog(String inputCatalog);

    /**
     * The output schema is the schema used by jooq-codegen in class names.
     *
     * @deprecated - 2.0.5 - This will be implemented in each
     *             {@link Definition#getOutputName()}
     */
    @Deprecated
    String getOutputSchema(String inputSchema);

    /**
     * The output schema is the schema used by jooq-codegen in class names.
     *
     * @deprecated - 2.0.5 - This will be implemented in each
     *             {@link Definition#getOutputName()}
     */
    @Deprecated
    String getOutputSchema(String inputCatalog, String inputSchema);

    /**
     * The input and output catalogs.
     */
    void setConfiguredCatalogs(List<Catalog> catalogs);

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
     * whether foreign key relationships should be included.
     */
    void setIncludeForeignKeys(boolean includeForeignKeys);

    /**
     * whether foreign key relationships should be included.
     */
    boolean getIncludeForeignKeys();

    /**
     * whether unique keys should be included.
     */
    void setIncludeUniqueKeys(boolean includeUniqueKeys);

    /**
     * whether unique keys should be included.
     */
    boolean getIncludeUniqueKeys();

    /**
     * whether primary keys should be included.
     */
    void setIncludePrimaryKeys(boolean includePrimaryKeys);

    /**
     * whether primary keys should be included.
     */
    boolean getIncludePrimaryKeys();

    /**
     * whether sequences should be included.
     */
    void setIncludeSequences(boolean includeSequences);

    /**
     * whether sequences should be included.
     */
    boolean getIncludeSequences();

    /**
     * whether user defined types should be included.
     */
    void setIncludeUDTs(boolean includeUDTs);

    /**
     * whether user defined types should be included.
     */
    boolean getIncludeUDTs();

    /**
     * whether packages should be included.
     */
    void setIncludePackages(boolean includePackages);

    /**
     * whether packages should be included.
     */
    boolean getIncludePackages();

    /**
     * whether routines should be included.
     */
    void setIncludeRoutines(boolean includeRoutines);

    /**
     * whether routines should be included.
     */
    boolean getIncludeRoutines();

    /**
     * Whether tables (and views) should be included.
     */
    void setIncludeTables(boolean includeTables);

    /**
     * Whether tables (and views) should be included.
     */
    boolean getIncludeTables();

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
     * Retrieve all included objects.
     */
    List<Definition> getIncluded();

    /**
     * Retrieve all excluded objects.
     */
    List<Definition> getExcluded();

    /**
     * Retrieve all objects.
     */
    List<Definition> getAll();

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
     * Columns matching these regular expressions will be considered as identity
     * columns in generated code.
     */
    void setSyntheticIdentities(String[] syntheticIdentities);

    /**
     * Columns matching these regular expressions will be considered as identity
     * columns in generated code.
     */
    String[] getSyntheticIdentities();

    /**
     * Database objects matching any of these field names will be generated as
     * custom types.
     *
     * @deprecated - 3.10.0 - [#5750] - Use {@link #getConfiguredForcedTypes()} only.
     */
    @Deprecated
    void setConfiguredCustomTypes(List<CustomType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * custom types.
     *
     * @deprecated - 3.10.0 - [#5750] - Use {@link #getConfiguredForcedTypes()} only.
     */
    @Deprecated
    List<CustomType> getConfiguredCustomTypes();

    /**
     * Get a specific configured custom type by its name.
     *
     * @deprecated - 3.10.0 - [#5750] - Use {@link #getConfiguredForcedTypes()} only.
     */
    @Deprecated
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

    /**
     * The database's schema version provider.
     */
    SchemaVersionProvider getSchemaVersionProvider();

    /**
     * The database's schema version provider.
     */
    void setSchemaVersionProvider(SchemaVersionProvider provider);

    /**
     * The database's catalog version provider.
     */
    CatalogVersionProvider getCatalogVersionProvider();

    /**
     * The database's catalog version provider.
     */
    void setCatalogVersionProvider(CatalogVersionProvider provider);

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
     * [#4838] Whether table-valued functions should be reported as tables.
     */
    void setTableValuedFunctions(boolean tableValuedFunctions);

    /**
     * [#4838] Whether table-valued functions should be reported as tables.
     */
    boolean tableValuedFunctions();

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
