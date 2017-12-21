







package org.jooq.util.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Configuration of the database meta data source.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Database", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Database implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlList
    @XmlElement(defaultValue = "COMMENTS CASE_INSENSITIVE")
    @XmlSchemaType(name = "anySimpleType")
    protected List<RegexFlag> regexFlags;
    @XmlElement(defaultValue = ".*")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String includes = ".*";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String excludes = "";
    @XmlElement(defaultValue = "false")
    protected Boolean includeExcludeColumns = false;
    @XmlElement(defaultValue = "true")
    protected Boolean includeTables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeRoutines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includePackages = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeUDTs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeSequences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeIndexes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includePrimaryKeys = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeUniqueKeys = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeForeignKeys = true;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordVersionFields = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordTimestampFields = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String syntheticIdentities = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String syntheticPrimaryKeys = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String overridePrimaryKeys = "";
    @XmlElement(defaultValue = "false")
    protected Boolean dateAsTimestamp = false;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreProcedureReturnValues = false;
    @XmlElement(defaultValue = "true")
    protected Boolean unsignedTypes = true;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String inputCatalog = "";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String outputCatalog;
    @XmlElement(defaultValue = "false")
    protected Boolean outputCatalogToDefault = false;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String inputSchema = "";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String outputSchema;
    @XmlElement(defaultValue = "false")
    protected Boolean outputSchemaToDefault = false;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaVersionProvider = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogVersionProvider = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String orderProvider = "";
    protected Boolean tableValuedFunctions;
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<Property> properties;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<Catalog> catalogs;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<Schema> schemata;
    @XmlElementWrapper(name = "customTypes")
    @XmlElement(name = "customType")
    protected List<CustomType> customTypes;
    @XmlElementWrapper(name = "enumTypes")
    @XmlElement(name = "enumType")
    protected List<EnumType> enumTypes;
    @XmlElementWrapper(name = "forcedTypes")
    @XmlElement(name = "forcedType")
    protected List<ForcedType> forcedTypes;

    /**
     * The database dialect from jooq-meta.
     * Available dialects are named <code>org.util.[database].[database]Database</code>.
     * <p>
     * Natively supported values are:
     * <ul>
     * <li>{@link org.jooq.util.ase.ASEDatabase}</li>
     * <li>{@link org.jooq.util.cubrid.CUBRIDDatabase}</li>
     * <li>{@link org.jooq.util.db2.DB2Database}</li>
     * <li>{@link org.jooq.util.derby.DerbyDatabase}</li>
     * <li>{@link org.jooq.util.firebird.FirebirdDatabase}</li>
     * <li>{@link org.jooq.util.h2.H2Database}</li>
     * <li>{@link org.jooq.util.hana.HanaDatabase}</li>
     * <li>{@link org.jooq.util.hsqldb.HSQLDBDatabase}</li>
     * <li>{@link org.jooq.util.informix.InformixDatabase}</li>
     * <li>{@link org.jooq.util.ingres.IngresDatabase}</li>
     * <li>{@link org.jooq.util.mariadb.MariaDBDatabase}</li>
     * <li>{@link org.jooq.util.mysql.MySQLDatabase}</li>
     * <li>{@link org.jooq.util.oracle.OracleDatabase}</li>
     * <li>{@link org.jooq.util.postgres.PostgresDatabase}</li>
     * <li>{@link org.jooq.util.redshift.RedshiftDatabase}</li>
     * <li>{@link org.jooq.util.sqlite.SQLiteDatabase}</li>
     * <li>{@link org.jooq.util.sqlserver.SQLServerDatabase}</li>
     * <li>{@link org.jooq.util.sybase.SybaseDatabase}</li>
     * <li>{@link org.jooq.util.vertica.VerticaDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer generic JDBC DatabaseMetaData (e.g. for MS Access).
     * <ul>
     * <li>{@link org.jooq.util.jdbc.JDBCDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer standard jOOQ-meta XML formats.
     * <ul>
     * <li>{@link org.jooq.util.xml.XMLDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer JPA annotated entities
     * <ul>
     * <li>{@link org.jooq.util.jpa.JPADatabase}</li>
     * </ul>
     * <p>
     * You can also provide your own org.jooq.util.Database implementation
     * here, if your database is currently not supported
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The flags that will be applied to all regular expressions from this configuration by default.
     * <p>
     * The default value is "COMMENTS CASE_INSENSITIVE"Gets the value of the regexFlags property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regexFlags property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegexFlags().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegexFlag }
     *
     *
     */
    public List<RegexFlag> getRegexFlags() {
        if (regexFlags == null) {
            regexFlags = new ArrayList<RegexFlag>();
        }
        return this.regexFlags;
    }

    /**
     * All elements that are generated from your schema.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIncludes() {
        return includes;
    }

    /**
     * Sets the value of the includes property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIncludes(String value) {
        this.includes = value;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Excludes match before includes, i.e. excludes have a higher priority.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExcludes() {
        return excludes;
    }

    /**
     * Sets the value of the excludes property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExcludes(String value) {
        this.excludes = value;
    }

    /**
     * This flag indicates whether include / exclude patterns should also match columns within tables.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeExcludeColumns() {
        return includeExcludeColumns;
    }

    /**
     * Sets the value of the includeExcludeColumns property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeExcludeColumns(Boolean value) {
        this.includeExcludeColumns = value;
    }

    /**
     * This flag indicates whether tables should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeTables() {
        return includeTables;
    }

    /**
     * Sets the value of the includeTables property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeTables(Boolean value) {
        this.includeTables = value;
    }

    /**
     * This flag indicates whether routines should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeRoutines() {
        return includeRoutines;
    }

    /**
     * Sets the value of the includeRoutines property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeRoutines(Boolean value) {
        this.includeRoutines = value;
    }

    /**
     * This flag indicates whether packages should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludePackages() {
        return includePackages;
    }

    /**
     * Sets the value of the includePackages property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludePackages(Boolean value) {
        this.includePackages = value;
    }

    /**
     * This flag indicates whether udts should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeUDTs() {
        return includeUDTs;
    }

    /**
     * Sets the value of the includeUDTs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeUDTs(Boolean value) {
        this.includeUDTs = value;
    }

    /**
     * This flag indicates whether sequences should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeSequences() {
        return includeSequences;
    }

    /**
     * Sets the value of the includeSequences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeSequences(Boolean value) {
        this.includeSequences = value;
    }

    /**
     * This flag indicates whether indexes should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeIndexes() {
        return includeIndexes;
    }

    /**
     * Sets the value of the includeIndexes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeIndexes(Boolean value) {
        this.includeIndexes = value;
    }

    /**
     * This flag indicates whether primary keys should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludePrimaryKeys() {
        return includePrimaryKeys;
    }

    /**
     * Sets the value of the includePrimaryKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludePrimaryKeys(Boolean value) {
        this.includePrimaryKeys = value;
    }

    /**
     * This flag indicates whether unique keys should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeUniqueKeys() {
        return includeUniqueKeys;
    }

    /**
     * Sets the value of the includeUniqueKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeUniqueKeys(Boolean value) {
        this.includeUniqueKeys = value;
    }

    /**
     * This flag indicates whether foreign keys should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeForeignKeys() {
        return includeForeignKeys;
    }

    /**
     * Sets the value of the includeForeignKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeForeignKeys(Boolean value) {
        this.includeForeignKeys = value;
    }

    /**
     * All table and view columns that are used as "version" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord.store()} and {@link org.jooq.UpdatableRecord.delete()} for details about optimistic locking.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRecordVersionFields() {
        return recordVersionFields;
    }

    /**
     * Sets the value of the recordVersionFields property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRecordVersionFields(String value) {
        this.recordVersionFields = value;
    }

    /**
     * All table and view columns that are used as "timestamp" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord.store()} and {@link org.jooq.UpdatableRecord.delete()} for details about optimistic locking.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRecordTimestampFields() {
        return recordTimestampFields;
    }

    /**
     * Sets the value of the recordTimestampFields property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRecordTimestampFields(String value) {
        this.recordTimestampFields = value;
    }

    /**
     * A regular expression matching all columns that represent identities.
     * <p>
     * To be used if columns are not detected as automatically as identities.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSyntheticIdentities() {
        return syntheticIdentities;
    }

    /**
     * Sets the value of the syntheticIdentities property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSyntheticIdentities(String value) {
        this.syntheticIdentities = value;
    }

    /**
     * A regular expression matching all columns that participate in "synthetic" primary keys,
     * which should be placed on generated {@link org.jooq.UpdatableRecord}
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord.store()}</li>
     * <li>{@link org.jooq.UpdatableRecord.update()}</li>
     * <li>{@link org.jooq.UpdatableRecord.delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord.refresh()}</li>
     * </ul>
     * <p>
     * Synthetic primary keys will override existing primary keys.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSyntheticPrimaryKeys() {
        return syntheticPrimaryKeys;
    }

    /**
     * Sets the value of the syntheticPrimaryKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSyntheticPrimaryKeys(String value) {
        this.syntheticPrimaryKeys = value;
    }

    /**
     * All (UNIQUE) key names that should be used instead of primary keys on
     * generated {@link org.jooq.UpdatableRecord}.
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord.store()}</li>
     * <li>{@link org.jooq.UpdatableRecord.update()}</li>
     * <li>{@link org.jooq.UpdatableRecord.delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord.refresh()}</li>
     * </ul>
     * <p>
     * If several keys match, a warning is emitted and the first one encountered will be used.
     * <p>
     * This flag will also replace synthetic primary keys, if it matches.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOverridePrimaryKeys() {
        return overridePrimaryKeys;
    }

    /**
     * Sets the value of the overridePrimaryKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOverridePrimaryKeys(String value) {
        this.overridePrimaryKeys = value;
    }

    /**
     * Generate {@link java.sql.Timestamp} fields for DATE columns. This is particularly useful for Oracle databases
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDateAsTimestamp() {
        return dateAsTimestamp;
    }

    /**
     * Sets the value of the dateAsTimestamp property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDateAsTimestamp(Boolean value) {
        this.dateAsTimestamp = value;
    }

    /**
     * Ignore procedure return values in Transact-SQL generated code.
     * <p>
     * In jOOQ 3.6.0, #4106 was implemented to support Transact-SQL's
     * optional return values from stored procedures. This turns all procedures
     * into Routine<Integer> (instead of Routine<Void>). For backwards-
     * compatibility reasons, users can suppress this change in jOOQ 3.x
     * <p>
     * @deprecated This feature is deprecated as of jOOQ 3.6.0 and will be removed again in
     * jOOQ 4.0.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIgnoreProcedureReturnValues() {
        return ignoreProcedureReturnValues;
    }

    /**
     * Sets the value of the ignoreProcedureReturnValues property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIgnoreProcedureReturnValues(Boolean value) {
        this.ignoreProcedureReturnValues = value;
    }

    /**
     * Generate jOOU data types for your unsigned data types, which are not natively supported in Java
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isUnsignedTypes() {
        return unsignedTypes;
    }

    /**
     * Sets the value of the unsignedTypes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setUnsignedTypes(Boolean value) {
        this.unsignedTypes = value;
    }

    /**
     * The catalog that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getCatalogs()} configuration element.
     * If left empty (and without any {@link #getCatalogs()} configuration  element), jOOQ will generate all available catalogs.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInputCatalog() {
        return inputCatalog;
    }

    /**
     * Sets the value of the inputCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputCatalog(String value) {
        this.inputCatalog = value;
    }

    /**
     * The catalog that is used in generated source code.
     * <p>
     * Use this to override your local development
     * catalog name for source code generation. If not specified, this
     * will be the same as {@link #getInputCatalog()}
     * <p>
     * This will be ignored if {@link #isOutputCatalogToDefault()} is set to true
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutputCatalog() {
        return outputCatalog;
    }

    /**
     * Sets the value of the outputCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutputCatalog(String value) {
        this.outputCatalog = value;
    }

    /**
     * A flag to indicate that the outputCatalog should be the "default" catalog,
     * which generates catalog-less, unqualified tables, procedures, etc.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOutputCatalogToDefault() {
        return outputCatalogToDefault;
    }

    /**
     * Sets the value of the outputCatalogToDefault property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOutputCatalogToDefault(Boolean value) {
        this.outputCatalogToDefault = value;
    }

    /**
     * The schema that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getSchemata()} configuration element.
     * If left empty (and without any {@link #getSchemata()} configuration element), jOOQ will generate all available schemata.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInputSchema() {
        return inputSchema;
    }

    /**
     * Sets the value of the inputSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputSchema(String value) {
        this.inputSchema = value;
    }

    /**
     * The schema that is used in generated source code.
     * <p>
     * Use this to override your local development
     * schema name for source code generation. If not specified, this
     * will be the same as {@link #getInputSchema()}.
     *
     * This will be ignored if {@link #isOutputSchemaToDefault()} is set to true
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * Sets the value of the outputSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutputSchema(String value) {
        this.outputSchema = value;
    }

    /**
     * A flag to indicate that the outputSchema should be the "default" schema,
     * which generates schema-less, unqualified tables, procedures, etc.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOutputSchemaToDefault() {
        return outputSchemaToDefault;
    }

    /**
     * Sets the value of the outputSchemaToDefault property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOutputSchemaToDefault(Boolean value) {
        this.outputSchemaToDefault = value;
    }

    /**
     * A custom version number that, if available, will be used to assess whether the
     * {@link #getInputSchema()} will need to be regenerated.
     * <p>
     * There are three operation modes for this element:
     * <ul>
     * <li>The value is a class that can be found on the classpath and that implements
     *   {@link org.jooq.util.SchemaVersionProvider}. Such classes must provide a default constructor</li>
     * <li>The value is a SELECT statement that returns one record with one column. The
     *   SELECT statement may contain a named variable called :schema_name</li>
     * <li>The value is a constant, such as a Maven property</li>
     * </ul>
     * <p>
     * Schema versions will be generated into the {@link javax.annotation.Generated} annotation on
     * generated artefacts.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchemaVersionProvider() {
        return schemaVersionProvider;
    }

    /**
     * Sets the value of the schemaVersionProvider property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchemaVersionProvider(String value) {
        this.schemaVersionProvider = value;
    }

    /**
     * A custom version number that, if available, will be used to assess whether the
     * {@link #getInputCatalog()} from a given catalog will need to be regenerated.
     * <p>
     * There are three operation modes for this element:
     * <ul>
     * <li>The value is a class that can be found on the classpath and that implements
     *   {@link org.jooq.util.CatalogVersionProvider}. Such classes must provide a default constructor</li>
     * <li>The value is a SELECT statement that returns one record with one column. The
     *   SELECT statement may contain a named variable called :catalog_name</li>
     * <li>The value is a constant, such as a Maven property</li>
     * <p>
     * Catalog versions will be generated into the {@link javax.annotation.Generated} annotation on
     * generated artefacts.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCatalogVersionProvider() {
        return catalogVersionProvider;
    }

    /**
     * Sets the value of the catalogVersionProvider property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCatalogVersionProvider(String value) {
        this.catalogVersionProvider = value;
    }

    /**
     * A custom {@link java.util.Comparator} that can compare two {@link org.jooq.util.Definition} objects to determine their order.
     * <p>
     * This comparator can be used to influence the order of any object that is produced by jOOQ meta, and thus, indirectly, the order of declared objects in generated code.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrderProvider() {
        return orderProvider;
    }

    /**
     * Sets the value of the orderProvider property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOrderProvider(String value) {
        this.orderProvider = value;
    }

    /**
     * Whether table valued functions should be reported as tables.
     * <p>
     * If this is deactivated, such functions are not generated as tables, but
     * as ordinary routines. This is particularly useful for backwards-
     * compatibility between jOOQ 3.8 and previous versions, when using TABLE
     * and VARRAY types in Oracle.
     * <p>
     * While this flag defaults to true for most databases, it defaults to false
     * for Oracle.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isTableValuedFunctions() {
        return tableValuedFunctions;
    }

    /**
     * Sets the value of the tableValuedFunctions property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setTableValuedFunctions(Boolean value) {
        this.tableValuedFunctions = value;
    }

    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Catalog> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<Catalog>();
        }
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public List<Schema> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<Schema>();
        }
        return schemata;
    }

    public void setSchemata(List<Schema> schemata) {
        this.schemata = schemata;
    }

    public List<CustomType> getCustomTypes() {
        if (customTypes == null) {
            customTypes = new ArrayList<CustomType>();
        }
        return customTypes;
    }

    public void setCustomTypes(List<CustomType> customTypes) {
        this.customTypes = customTypes;
    }

    public List<EnumType> getEnumTypes() {
        if (enumTypes == null) {
            enumTypes = new ArrayList<EnumType>();
        }
        return enumTypes;
    }

    public void setEnumTypes(List<EnumType> enumTypes) {
        this.enumTypes = enumTypes;
    }

    public List<ForcedType> getForcedTypes() {
        if (forcedTypes == null) {
            forcedTypes = new ArrayList<ForcedType>();
        }
        return forcedTypes;
    }

    public void setForcedTypes(List<ForcedType> forcedTypes) {
        this.forcedTypes = forcedTypes;
    }

    public Database withName(String value) {
        setName(value);
        return this;
    }

    public Database withRegexFlags(RegexFlag... values) {
        if (values!= null) {
            for (RegexFlag value: values) {
                getRegexFlags().add(value);
            }
        }
        return this;
    }

    public Database withRegexFlags(Collection<RegexFlag> values) {
        if (values!= null) {
            getRegexFlags().addAll(values);
        }
        return this;
    }

    public Database withIncludes(String value) {
        setIncludes(value);
        return this;
    }

    public Database withExcludes(String value) {
        setExcludes(value);
        return this;
    }

    public Database withIncludeExcludeColumns(Boolean value) {
        setIncludeExcludeColumns(value);
        return this;
    }

    public Database withIncludeTables(Boolean value) {
        setIncludeTables(value);
        return this;
    }

    public Database withIncludeRoutines(Boolean value) {
        setIncludeRoutines(value);
        return this;
    }

    public Database withIncludePackages(Boolean value) {
        setIncludePackages(value);
        return this;
    }

    public Database withIncludeUDTs(Boolean value) {
        setIncludeUDTs(value);
        return this;
    }

    public Database withIncludeSequences(Boolean value) {
        setIncludeSequences(value);
        return this;
    }

    public Database withIncludeIndexes(Boolean value) {
        setIncludeIndexes(value);
        return this;
    }

    public Database withIncludePrimaryKeys(Boolean value) {
        setIncludePrimaryKeys(value);
        return this;
    }

    public Database withIncludeUniqueKeys(Boolean value) {
        setIncludeUniqueKeys(value);
        return this;
    }

    public Database withIncludeForeignKeys(Boolean value) {
        setIncludeForeignKeys(value);
        return this;
    }

    public Database withRecordVersionFields(String value) {
        setRecordVersionFields(value);
        return this;
    }

    public Database withRecordTimestampFields(String value) {
        setRecordTimestampFields(value);
        return this;
    }

    public Database withSyntheticIdentities(String value) {
        setSyntheticIdentities(value);
        return this;
    }

    public Database withSyntheticPrimaryKeys(String value) {
        setSyntheticPrimaryKeys(value);
        return this;
    }

    public Database withOverridePrimaryKeys(String value) {
        setOverridePrimaryKeys(value);
        return this;
    }

    public Database withDateAsTimestamp(Boolean value) {
        setDateAsTimestamp(value);
        return this;
    }

    public Database withIgnoreProcedureReturnValues(Boolean value) {
        setIgnoreProcedureReturnValues(value);
        return this;
    }

    public Database withUnsignedTypes(Boolean value) {
        setUnsignedTypes(value);
        return this;
    }

    public Database withInputCatalog(String value) {
        setInputCatalog(value);
        return this;
    }

    public Database withOutputCatalog(String value) {
        setOutputCatalog(value);
        return this;
    }

    public Database withOutputCatalogToDefault(Boolean value) {
        setOutputCatalogToDefault(value);
        return this;
    }

    public Database withInputSchema(String value) {
        setInputSchema(value);
        return this;
    }

    public Database withOutputSchema(String value) {
        setOutputSchema(value);
        return this;
    }

    public Database withOutputSchemaToDefault(Boolean value) {
        setOutputSchemaToDefault(value);
        return this;
    }

    public Database withSchemaVersionProvider(String value) {
        setSchemaVersionProvider(value);
        return this;
    }

    public Database withCatalogVersionProvider(String value) {
        setCatalogVersionProvider(value);
        return this;
    }

    public Database withOrderProvider(String value) {
        setOrderProvider(value);
        return this;
    }

    public Database withTableValuedFunctions(Boolean value) {
        setTableValuedFunctions(value);
        return this;
    }

    public Database withProperties(Property... values) {
        if (values!= null) {
            for (Property value: values) {
                getProperties().add(value);
            }
        }
        return this;
    }

    public Database withProperties(Collection<Property> values) {
        if (values!= null) {
            getProperties().addAll(values);
        }
        return this;
    }

    public Database withProperties(List<Property> properties) {
        setProperties(properties);
        return this;
    }

    public Database withCatalogs(Catalog... values) {
        if (values!= null) {
            for (Catalog value: values) {
                getCatalogs().add(value);
            }
        }
        return this;
    }

    public Database withCatalogs(Collection<Catalog> values) {
        if (values!= null) {
            getCatalogs().addAll(values);
        }
        return this;
    }

    public Database withCatalogs(List<Catalog> catalogs) {
        setCatalogs(catalogs);
        return this;
    }

    public Database withSchemata(Schema... values) {
        if (values!= null) {
            for (Schema value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public Database withSchemata(Collection<Schema> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public Database withSchemata(List<Schema> schemata) {
        setSchemata(schemata);
        return this;
    }

    public Database withCustomTypes(CustomType... values) {
        if (values!= null) {
            for (CustomType value: values) {
                getCustomTypes().add(value);
            }
        }
        return this;
    }

    public Database withCustomTypes(Collection<CustomType> values) {
        if (values!= null) {
            getCustomTypes().addAll(values);
        }
        return this;
    }

    public Database withCustomTypes(List<CustomType> customTypes) {
        setCustomTypes(customTypes);
        return this;
    }

    public Database withEnumTypes(EnumType... values) {
        if (values!= null) {
            for (EnumType value: values) {
                getEnumTypes().add(value);
            }
        }
        return this;
    }

    public Database withEnumTypes(Collection<EnumType> values) {
        if (values!= null) {
            getEnumTypes().addAll(values);
        }
        return this;
    }

    public Database withEnumTypes(List<EnumType> enumTypes) {
        setEnumTypes(enumTypes);
        return this;
    }

    public Database withForcedTypes(ForcedType... values) {
        if (values!= null) {
            for (ForcedType value: values) {
                getForcedTypes().add(value);
            }
        }
        return this;
    }

    public Database withForcedTypes(Collection<ForcedType> values) {
        if (values!= null) {
            getForcedTypes().addAll(values);
        }
        return this;
    }

    public Database withForcedTypes(List<ForcedType> forcedTypes) {
        setForcedTypes(forcedTypes);
        return this;
    }

}
