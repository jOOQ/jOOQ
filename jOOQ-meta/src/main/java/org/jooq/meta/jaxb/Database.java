







package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
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

    private final static long serialVersionUID = 31200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlList
    @XmlElement(defaultValue = "COMMENTS CASE_INSENSITIVE")
    protected List<RegexFlag> regexFlags;
    @XmlElement(defaultValue = "true")
    protected Boolean regexMatchesPartialQualification = true;
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
    protected Boolean includeEmbeddables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeRoutines = true;
    @XmlElement(defaultValue = "false")
    protected Boolean includeTriggerRoutines = false;
    @XmlElement(defaultValue = "true")
    protected Boolean includePackages = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includePackageRoutines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includePackageUDTs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includePackageConstants = true;
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
    @XmlElement(defaultValue = "true")
    protected Boolean includeInvisibleColumns = true;
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
    @XmlElement(defaultValue = "true")
    protected Boolean forceIntegerTypesOnZeroScaleDecimals = true;
    protected Boolean tableValuedFunctions;
    @XmlElement(defaultValue = "5")
    protected Integer logSlowQueriesAfterSeconds = 5;
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<Property> properties;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<CatalogMappingType> catalogs;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<SchemaMappingType> schemata;
    @XmlElementWrapper(name = "embeddables")
    @XmlElement(name = "embeddable")
    protected List<Embeddable> embeddables;
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
     * <li>{@link org.jooq.meta.ase.ASEDatabase}</li>
     * <li>{@link org.jooq.meta.cubrid.CUBRIDDatabase}</li>
     * <li>{@link org.jooq.meta.db2.DB2Database}</li>
     * <li>{@link org.jooq.meta.derby.DerbyDatabase}</li>
     * <li>{@link org.jooq.meta.firebird.FirebirdDatabase}</li>
     * <li>{@link org.jooq.meta.h2.H2Database}</li>
     * <li>{@link org.jooq.meta.hana.HanaDatabase}</li>
     * <li>{@link org.jooq.meta.hsqldb.HSQLDBDatabase}</li>
     * <li>{@link org.jooq.meta.informix.InformixDatabase}</li>
     * <li>{@link org.jooq.meta.ingres.IngresDatabase}</li>
     * <li>{@link org.jooq.meta.mariadb.MariaDBDatabase}</li>
     * <li>{@link org.jooq.meta.mysql.MySQLDatabase}</li>
     * <li>{@link org.jooq.meta.oracle.OracleDatabase}</li>
     * <li>{@link org.jooq.meta.postgres.PostgresDatabase}</li>
     * <li>{@link org.jooq.meta.redshift.RedshiftDatabase}</li>
     * <li>{@link org.jooq.meta.sqlite.SQLiteDatabase}</li>
     * <li>{@link org.jooq.meta.sqlserver.SQLServerDatabase}</li>
     * <li>{@link org.jooq.meta.sybase.SybaseDatabase}</li>
     * <li>{@link org.jooq.meta.vertica.VerticaDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer generic JDBC DatabaseMetaData (e.g. for MS Access).
     * <ul>
     * <li>{@link org.jooq.meta.jdbc.JDBCDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer standard jOOQ-meta XML formats.
     * <ul>
     * <li>{@link org.jooq.meta.xml.XMLDatabase}</li>
     * </ul>
     * <p>
     * This value can be used to reverse-engineer JPA annotated entities
     * <ul>
     * <li>{@link org.jooq.meta.extensions.jpa.JPADatabase}</li>
     * </ul>
     * <p>
     * You can also provide your own org.jooq.meta.Database implementation
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
     * Whether regular expressions that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRegexMatchesPartialQualification() {
        return regexMatchesPartialQualification;
    }

    /**
     * Sets the value of the regexMatchesPartialQualification property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRegexMatchesPartialQualification(Boolean value) {
        this.regexMatchesPartialQualification = value;
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
     * This flag indicates whether embeddable types should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeEmbeddables() {
        return includeEmbeddables;
    }

    /**
     * Sets the value of the includeEmbeddables property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeEmbeddables(Boolean value) {
        this.includeEmbeddables = value;
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
     * This flag indicates whether trigger implementation routines should be included in output produced by this database (e.g. in PostgreSQL)
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeTriggerRoutines() {
        return includeTriggerRoutines;
    }

    /**
     * Sets the value of the includeTriggerRoutines property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeTriggerRoutines(Boolean value) {
        this.includeTriggerRoutines = value;
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
     * This flag indicates whether routines contained in packages should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludePackageRoutines() {
        return includePackageRoutines;
    }

    /**
     * Sets the value of the includePackageRoutines property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludePackageRoutines(Boolean value) {
        this.includePackageRoutines = value;
    }

    /**
     * This flag indicates whether UDTs contained in packages should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludePackageUDTs() {
        return includePackageUDTs;
    }

    /**
     * Sets the value of the includePackageUDTs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludePackageUDTs(Boolean value) {
        this.includePackageUDTs = value;
    }

    /**
     * This flag indicates whether constants contained in packages should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludePackageConstants() {
        return includePackageConstants;
    }

    /**
     * Sets the value of the includePackageConstants property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludePackageConstants(Boolean value) {
        this.includePackageConstants = value;
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
     * This flag indicates whether invisible columns should be included in output produced by this database
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIncludeInvisibleColumns() {
        return includeInvisibleColumns;
    }

    /**
     * Sets the value of the includeInvisibleColumns property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeInvisibleColumns(Boolean value) {
        this.includeInvisibleColumns = value;
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
     *   {@link org.jooq.meta.SchemaVersionProvider}. Such classes must provide a default constructor</li>
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
     *   {@link org.jooq.meta.CatalogVersionProvider}. Such classes must provide a default constructor</li>
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
     * A custom {@link java.util.Comparator} that can compare two {@link org.jooq.meta.Definition} objects to determine their order.
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
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). This allows for turning off this feature. In case of conflict between this rule and actual {@link #getForcedTypes()}, the latter will win.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isForceIntegerTypesOnZeroScaleDecimals() {
        return forceIntegerTypesOnZeroScaleDecimals;
    }

    /**
     * Sets the value of the forceIntegerTypesOnZeroScaleDecimals property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setForceIntegerTypesOnZeroScaleDecimals(Boolean value) {
        this.forceIntegerTypesOnZeroScaleDecimals = value;
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

    /**
     * The number of seconds that are considered "slow" before a query is logged to indicate a bug, 0 for not logging.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getLogSlowQueriesAfterSeconds() {
        return logSlowQueriesAfterSeconds;
    }

    /**
     * Sets the value of the logSlowQueriesAfterSeconds property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setLogSlowQueriesAfterSeconds(Integer value) {
        this.logSlowQueriesAfterSeconds = value;
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

    public List<CatalogMappingType> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<CatalogMappingType>();
        }
        return catalogs;
    }

    public void setCatalogs(List<CatalogMappingType> catalogs) {
        this.catalogs = catalogs;
    }

    public List<SchemaMappingType> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<SchemaMappingType>();
        }
        return schemata;
    }

    public void setSchemata(List<SchemaMappingType> schemata) {
        this.schemata = schemata;
    }

    public List<Embeddable> getEmbeddables() {
        if (embeddables == null) {
            embeddables = new ArrayList<Embeddable>();
        }
        return embeddables;
    }

    public void setEmbeddables(List<Embeddable> embeddables) {
        this.embeddables = embeddables;
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

    public Database withRegexMatchesPartialQualification(Boolean value) {
        setRegexMatchesPartialQualification(value);
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

    public Database withIncludeEmbeddables(Boolean value) {
        setIncludeEmbeddables(value);
        return this;
    }

    public Database withIncludeRoutines(Boolean value) {
        setIncludeRoutines(value);
        return this;
    }

    public Database withIncludeTriggerRoutines(Boolean value) {
        setIncludeTriggerRoutines(value);
        return this;
    }

    public Database withIncludePackages(Boolean value) {
        setIncludePackages(value);
        return this;
    }

    public Database withIncludePackageRoutines(Boolean value) {
        setIncludePackageRoutines(value);
        return this;
    }

    public Database withIncludePackageUDTs(Boolean value) {
        setIncludePackageUDTs(value);
        return this;
    }

    public Database withIncludePackageConstants(Boolean value) {
        setIncludePackageConstants(value);
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

    public Database withIncludeInvisibleColumns(Boolean value) {
        setIncludeInvisibleColumns(value);
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

    public Database withForceIntegerTypesOnZeroScaleDecimals(Boolean value) {
        setForceIntegerTypesOnZeroScaleDecimals(value);
        return this;
    }

    public Database withTableValuedFunctions(Boolean value) {
        setTableValuedFunctions(value);
        return this;
    }

    public Database withLogSlowQueriesAfterSeconds(Integer value) {
        setLogSlowQueriesAfterSeconds(value);
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

    public Database withCatalogs(CatalogMappingType... values) {
        if (values!= null) {
            for (CatalogMappingType value: values) {
                getCatalogs().add(value);
            }
        }
        return this;
    }

    public Database withCatalogs(Collection<CatalogMappingType> values) {
        if (values!= null) {
            getCatalogs().addAll(values);
        }
        return this;
    }

    public Database withCatalogs(List<CatalogMappingType> catalogs) {
        setCatalogs(catalogs);
        return this;
    }

    public Database withSchemata(SchemaMappingType... values) {
        if (values!= null) {
            for (SchemaMappingType value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public Database withSchemata(Collection<SchemaMappingType> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public Database withSchemata(List<SchemaMappingType> schemata) {
        setSchemata(schemata);
        return this;
    }

    public Database withEmbeddables(Embeddable... values) {
        if (values!= null) {
            for (Embeddable value: values) {
                getEmbeddables().add(value);
            }
        }
        return this;
    }

    public Database withEmbeddables(Collection<Embeddable> values) {
        if (values!= null) {
            getEmbeddables().addAll(values);
        }
        return this;
    }

    public Database withEmbeddables(List<Embeddable> embeddables) {
        setEmbeddables(embeddables);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name!= null) {
            sb.append("<name>");
            sb.append(name);
            sb.append("</name>");
        }
        if (regexFlags!= null) {
            sb.append("<regexFlags>");
            for (int i = 0; (i<regexFlags.size()); i ++) {
                sb.append("<regexFlags>");
                sb.append(regexFlags.get(i));
                sb.append("</regexFlags>");
            }
            sb.append("</regexFlags>");
        }
        if (regexMatchesPartialQualification!= null) {
            sb.append("<regexMatchesPartialQualification>");
            sb.append(regexMatchesPartialQualification);
            sb.append("</regexMatchesPartialQualification>");
        }
        if (includes!= null) {
            sb.append("<includes>");
            sb.append(includes);
            sb.append("</includes>");
        }
        if (excludes!= null) {
            sb.append("<excludes>");
            sb.append(excludes);
            sb.append("</excludes>");
        }
        if (includeExcludeColumns!= null) {
            sb.append("<includeExcludeColumns>");
            sb.append(includeExcludeColumns);
            sb.append("</includeExcludeColumns>");
        }
        if (includeTables!= null) {
            sb.append("<includeTables>");
            sb.append(includeTables);
            sb.append("</includeTables>");
        }
        if (includeEmbeddables!= null) {
            sb.append("<includeEmbeddables>");
            sb.append(includeEmbeddables);
            sb.append("</includeEmbeddables>");
        }
        if (includeRoutines!= null) {
            sb.append("<includeRoutines>");
            sb.append(includeRoutines);
            sb.append("</includeRoutines>");
        }
        if (includeTriggerRoutines!= null) {
            sb.append("<includeTriggerRoutines>");
            sb.append(includeTriggerRoutines);
            sb.append("</includeTriggerRoutines>");
        }
        if (includePackages!= null) {
            sb.append("<includePackages>");
            sb.append(includePackages);
            sb.append("</includePackages>");
        }
        if (includePackageRoutines!= null) {
            sb.append("<includePackageRoutines>");
            sb.append(includePackageRoutines);
            sb.append("</includePackageRoutines>");
        }
        if (includePackageUDTs!= null) {
            sb.append("<includePackageUDTs>");
            sb.append(includePackageUDTs);
            sb.append("</includePackageUDTs>");
        }
        if (includePackageConstants!= null) {
            sb.append("<includePackageConstants>");
            sb.append(includePackageConstants);
            sb.append("</includePackageConstants>");
        }
        if (includeUDTs!= null) {
            sb.append("<includeUDTs>");
            sb.append(includeUDTs);
            sb.append("</includeUDTs>");
        }
        if (includeSequences!= null) {
            sb.append("<includeSequences>");
            sb.append(includeSequences);
            sb.append("</includeSequences>");
        }
        if (includeIndexes!= null) {
            sb.append("<includeIndexes>");
            sb.append(includeIndexes);
            sb.append("</includeIndexes>");
        }
        if (includePrimaryKeys!= null) {
            sb.append("<includePrimaryKeys>");
            sb.append(includePrimaryKeys);
            sb.append("</includePrimaryKeys>");
        }
        if (includeUniqueKeys!= null) {
            sb.append("<includeUniqueKeys>");
            sb.append(includeUniqueKeys);
            sb.append("</includeUniqueKeys>");
        }
        if (includeForeignKeys!= null) {
            sb.append("<includeForeignKeys>");
            sb.append(includeForeignKeys);
            sb.append("</includeForeignKeys>");
        }
        if (includeInvisibleColumns!= null) {
            sb.append("<includeInvisibleColumns>");
            sb.append(includeInvisibleColumns);
            sb.append("</includeInvisibleColumns>");
        }
        if (recordVersionFields!= null) {
            sb.append("<recordVersionFields>");
            sb.append(recordVersionFields);
            sb.append("</recordVersionFields>");
        }
        if (recordTimestampFields!= null) {
            sb.append("<recordTimestampFields>");
            sb.append(recordTimestampFields);
            sb.append("</recordTimestampFields>");
        }
        if (syntheticIdentities!= null) {
            sb.append("<syntheticIdentities>");
            sb.append(syntheticIdentities);
            sb.append("</syntheticIdentities>");
        }
        if (syntheticPrimaryKeys!= null) {
            sb.append("<syntheticPrimaryKeys>");
            sb.append(syntheticPrimaryKeys);
            sb.append("</syntheticPrimaryKeys>");
        }
        if (overridePrimaryKeys!= null) {
            sb.append("<overridePrimaryKeys>");
            sb.append(overridePrimaryKeys);
            sb.append("</overridePrimaryKeys>");
        }
        if (dateAsTimestamp!= null) {
            sb.append("<dateAsTimestamp>");
            sb.append(dateAsTimestamp);
            sb.append("</dateAsTimestamp>");
        }
        if (ignoreProcedureReturnValues!= null) {
            sb.append("<ignoreProcedureReturnValues>");
            sb.append(ignoreProcedureReturnValues);
            sb.append("</ignoreProcedureReturnValues>");
        }
        if (unsignedTypes!= null) {
            sb.append("<unsignedTypes>");
            sb.append(unsignedTypes);
            sb.append("</unsignedTypes>");
        }
        if (inputCatalog!= null) {
            sb.append("<inputCatalog>");
            sb.append(inputCatalog);
            sb.append("</inputCatalog>");
        }
        if (outputCatalog!= null) {
            sb.append("<outputCatalog>");
            sb.append(outputCatalog);
            sb.append("</outputCatalog>");
        }
        if (outputCatalogToDefault!= null) {
            sb.append("<outputCatalogToDefault>");
            sb.append(outputCatalogToDefault);
            sb.append("</outputCatalogToDefault>");
        }
        if (inputSchema!= null) {
            sb.append("<inputSchema>");
            sb.append(inputSchema);
            sb.append("</inputSchema>");
        }
        if (outputSchema!= null) {
            sb.append("<outputSchema>");
            sb.append(outputSchema);
            sb.append("</outputSchema>");
        }
        if (outputSchemaToDefault!= null) {
            sb.append("<outputSchemaToDefault>");
            sb.append(outputSchemaToDefault);
            sb.append("</outputSchemaToDefault>");
        }
        if (schemaVersionProvider!= null) {
            sb.append("<schemaVersionProvider>");
            sb.append(schemaVersionProvider);
            sb.append("</schemaVersionProvider>");
        }
        if (catalogVersionProvider!= null) {
            sb.append("<catalogVersionProvider>");
            sb.append(catalogVersionProvider);
            sb.append("</catalogVersionProvider>");
        }
        if (orderProvider!= null) {
            sb.append("<orderProvider>");
            sb.append(orderProvider);
            sb.append("</orderProvider>");
        }
        if (forceIntegerTypesOnZeroScaleDecimals!= null) {
            sb.append("<forceIntegerTypesOnZeroScaleDecimals>");
            sb.append(forceIntegerTypesOnZeroScaleDecimals);
            sb.append("</forceIntegerTypesOnZeroScaleDecimals>");
        }
        if (tableValuedFunctions!= null) {
            sb.append("<tableValuedFunctions>");
            sb.append(tableValuedFunctions);
            sb.append("</tableValuedFunctions>");
        }
        if (logSlowQueriesAfterSeconds!= null) {
            sb.append("<logSlowQueriesAfterSeconds>");
            sb.append(logSlowQueriesAfterSeconds);
            sb.append("</logSlowQueriesAfterSeconds>");
        }
        if (properties!= null) {
            sb.append("<properties>");
            for (int i = 0; (i<properties.size()); i ++) {
                sb.append("<property>");
                sb.append(properties.get(i));
                sb.append("</property>");
            }
            sb.append("</properties>");
        }
        if (catalogs!= null) {
            sb.append("<catalogs>");
            for (int i = 0; (i<catalogs.size()); i ++) {
                sb.append("<catalog>");
                sb.append(catalogs.get(i));
                sb.append("</catalog>");
            }
            sb.append("</catalogs>");
        }
        if (schemata!= null) {
            sb.append("<schemata>");
            for (int i = 0; (i<schemata.size()); i ++) {
                sb.append("<schema>");
                sb.append(schemata.get(i));
                sb.append("</schema>");
            }
            sb.append("</schemata>");
        }
        if (embeddables!= null) {
            sb.append("<embeddables>");
            for (int i = 0; (i<embeddables.size()); i ++) {
                sb.append("<embeddable>");
                sb.append(embeddables.get(i));
                sb.append("</embeddable>");
            }
            sb.append("</embeddables>");
        }
        if (customTypes!= null) {
            sb.append("<customTypes>");
            for (int i = 0; (i<customTypes.size()); i ++) {
                sb.append("<customType>");
                sb.append(customTypes.get(i));
                sb.append("</customType>");
            }
            sb.append("</customTypes>");
        }
        if (enumTypes!= null) {
            sb.append("<enumTypes>");
            for (int i = 0; (i<enumTypes.size()); i ++) {
                sb.append("<enumType>");
                sb.append(enumTypes.get(i));
                sb.append("</enumType>");
            }
            sb.append("</enumTypes>");
        }
        if (forcedTypes!= null) {
            sb.append("<forcedTypes>");
            for (int i = 0; (i<forcedTypes.size()); i ++) {
                sb.append("<forcedType>");
                sb.append(forcedTypes.get(i));
                sb.append("</forcedType>");
            }
            sb.append("</forcedTypes>");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        Database other = ((Database) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (regexFlags == null) {
            if (other.regexFlags!= null) {
                return false;
            }
        } else {
            if (!regexFlags.equals(other.regexFlags)) {
                return false;
            }
        }
        if (regexMatchesPartialQualification == null) {
            if (other.regexMatchesPartialQualification!= null) {
                return false;
            }
        } else {
            if (!regexMatchesPartialQualification.equals(other.regexMatchesPartialQualification)) {
                return false;
            }
        }
        if (includes == null) {
            if (other.includes!= null) {
                return false;
            }
        } else {
            if (!includes.equals(other.includes)) {
                return false;
            }
        }
        if (excludes == null) {
            if (other.excludes!= null) {
                return false;
            }
        } else {
            if (!excludes.equals(other.excludes)) {
                return false;
            }
        }
        if (includeExcludeColumns == null) {
            if (other.includeExcludeColumns!= null) {
                return false;
            }
        } else {
            if (!includeExcludeColumns.equals(other.includeExcludeColumns)) {
                return false;
            }
        }
        if (includeTables == null) {
            if (other.includeTables!= null) {
                return false;
            }
        } else {
            if (!includeTables.equals(other.includeTables)) {
                return false;
            }
        }
        if (includeEmbeddables == null) {
            if (other.includeEmbeddables!= null) {
                return false;
            }
        } else {
            if (!includeEmbeddables.equals(other.includeEmbeddables)) {
                return false;
            }
        }
        if (includeRoutines == null) {
            if (other.includeRoutines!= null) {
                return false;
            }
        } else {
            if (!includeRoutines.equals(other.includeRoutines)) {
                return false;
            }
        }
        if (includeTriggerRoutines == null) {
            if (other.includeTriggerRoutines!= null) {
                return false;
            }
        } else {
            if (!includeTriggerRoutines.equals(other.includeTriggerRoutines)) {
                return false;
            }
        }
        if (includePackages == null) {
            if (other.includePackages!= null) {
                return false;
            }
        } else {
            if (!includePackages.equals(other.includePackages)) {
                return false;
            }
        }
        if (includePackageRoutines == null) {
            if (other.includePackageRoutines!= null) {
                return false;
            }
        } else {
            if (!includePackageRoutines.equals(other.includePackageRoutines)) {
                return false;
            }
        }
        if (includePackageUDTs == null) {
            if (other.includePackageUDTs!= null) {
                return false;
            }
        } else {
            if (!includePackageUDTs.equals(other.includePackageUDTs)) {
                return false;
            }
        }
        if (includePackageConstants == null) {
            if (other.includePackageConstants!= null) {
                return false;
            }
        } else {
            if (!includePackageConstants.equals(other.includePackageConstants)) {
                return false;
            }
        }
        if (includeUDTs == null) {
            if (other.includeUDTs!= null) {
                return false;
            }
        } else {
            if (!includeUDTs.equals(other.includeUDTs)) {
                return false;
            }
        }
        if (includeSequences == null) {
            if (other.includeSequences!= null) {
                return false;
            }
        } else {
            if (!includeSequences.equals(other.includeSequences)) {
                return false;
            }
        }
        if (includeIndexes == null) {
            if (other.includeIndexes!= null) {
                return false;
            }
        } else {
            if (!includeIndexes.equals(other.includeIndexes)) {
                return false;
            }
        }
        if (includePrimaryKeys == null) {
            if (other.includePrimaryKeys!= null) {
                return false;
            }
        } else {
            if (!includePrimaryKeys.equals(other.includePrimaryKeys)) {
                return false;
            }
        }
        if (includeUniqueKeys == null) {
            if (other.includeUniqueKeys!= null) {
                return false;
            }
        } else {
            if (!includeUniqueKeys.equals(other.includeUniqueKeys)) {
                return false;
            }
        }
        if (includeForeignKeys == null) {
            if (other.includeForeignKeys!= null) {
                return false;
            }
        } else {
            if (!includeForeignKeys.equals(other.includeForeignKeys)) {
                return false;
            }
        }
        if (includeInvisibleColumns == null) {
            if (other.includeInvisibleColumns!= null) {
                return false;
            }
        } else {
            if (!includeInvisibleColumns.equals(other.includeInvisibleColumns)) {
                return false;
            }
        }
        if (recordVersionFields == null) {
            if (other.recordVersionFields!= null) {
                return false;
            }
        } else {
            if (!recordVersionFields.equals(other.recordVersionFields)) {
                return false;
            }
        }
        if (recordTimestampFields == null) {
            if (other.recordTimestampFields!= null) {
                return false;
            }
        } else {
            if (!recordTimestampFields.equals(other.recordTimestampFields)) {
                return false;
            }
        }
        if (syntheticIdentities == null) {
            if (other.syntheticIdentities!= null) {
                return false;
            }
        } else {
            if (!syntheticIdentities.equals(other.syntheticIdentities)) {
                return false;
            }
        }
        if (syntheticPrimaryKeys == null) {
            if (other.syntheticPrimaryKeys!= null) {
                return false;
            }
        } else {
            if (!syntheticPrimaryKeys.equals(other.syntheticPrimaryKeys)) {
                return false;
            }
        }
        if (overridePrimaryKeys == null) {
            if (other.overridePrimaryKeys!= null) {
                return false;
            }
        } else {
            if (!overridePrimaryKeys.equals(other.overridePrimaryKeys)) {
                return false;
            }
        }
        if (dateAsTimestamp == null) {
            if (other.dateAsTimestamp!= null) {
                return false;
            }
        } else {
            if (!dateAsTimestamp.equals(other.dateAsTimestamp)) {
                return false;
            }
        }
        if (ignoreProcedureReturnValues == null) {
            if (other.ignoreProcedureReturnValues!= null) {
                return false;
            }
        } else {
            if (!ignoreProcedureReturnValues.equals(other.ignoreProcedureReturnValues)) {
                return false;
            }
        }
        if (unsignedTypes == null) {
            if (other.unsignedTypes!= null) {
                return false;
            }
        } else {
            if (!unsignedTypes.equals(other.unsignedTypes)) {
                return false;
            }
        }
        if (inputCatalog == null) {
            if (other.inputCatalog!= null) {
                return false;
            }
        } else {
            if (!inputCatalog.equals(other.inputCatalog)) {
                return false;
            }
        }
        if (outputCatalog == null) {
            if (other.outputCatalog!= null) {
                return false;
            }
        } else {
            if (!outputCatalog.equals(other.outputCatalog)) {
                return false;
            }
        }
        if (outputCatalogToDefault == null) {
            if (other.outputCatalogToDefault!= null) {
                return false;
            }
        } else {
            if (!outputCatalogToDefault.equals(other.outputCatalogToDefault)) {
                return false;
            }
        }
        if (inputSchema == null) {
            if (other.inputSchema!= null) {
                return false;
            }
        } else {
            if (!inputSchema.equals(other.inputSchema)) {
                return false;
            }
        }
        if (outputSchema == null) {
            if (other.outputSchema!= null) {
                return false;
            }
        } else {
            if (!outputSchema.equals(other.outputSchema)) {
                return false;
            }
        }
        if (outputSchemaToDefault == null) {
            if (other.outputSchemaToDefault!= null) {
                return false;
            }
        } else {
            if (!outputSchemaToDefault.equals(other.outputSchemaToDefault)) {
                return false;
            }
        }
        if (schemaVersionProvider == null) {
            if (other.schemaVersionProvider!= null) {
                return false;
            }
        } else {
            if (!schemaVersionProvider.equals(other.schemaVersionProvider)) {
                return false;
            }
        }
        if (catalogVersionProvider == null) {
            if (other.catalogVersionProvider!= null) {
                return false;
            }
        } else {
            if (!catalogVersionProvider.equals(other.catalogVersionProvider)) {
                return false;
            }
        }
        if (orderProvider == null) {
            if (other.orderProvider!= null) {
                return false;
            }
        } else {
            if (!orderProvider.equals(other.orderProvider)) {
                return false;
            }
        }
        if (forceIntegerTypesOnZeroScaleDecimals == null) {
            if (other.forceIntegerTypesOnZeroScaleDecimals!= null) {
                return false;
            }
        } else {
            if (!forceIntegerTypesOnZeroScaleDecimals.equals(other.forceIntegerTypesOnZeroScaleDecimals)) {
                return false;
            }
        }
        if (tableValuedFunctions == null) {
            if (other.tableValuedFunctions!= null) {
                return false;
            }
        } else {
            if (!tableValuedFunctions.equals(other.tableValuedFunctions)) {
                return false;
            }
        }
        if (logSlowQueriesAfterSeconds == null) {
            if (other.logSlowQueriesAfterSeconds!= null) {
                return false;
            }
        } else {
            if (!logSlowQueriesAfterSeconds.equals(other.logSlowQueriesAfterSeconds)) {
                return false;
            }
        }
        if (properties == null) {
            if (other.properties!= null) {
                return false;
            }
        } else {
            if (!properties.equals(other.properties)) {
                return false;
            }
        }
        if (catalogs == null) {
            if (other.catalogs!= null) {
                return false;
            }
        } else {
            if (!catalogs.equals(other.catalogs)) {
                return false;
            }
        }
        if (schemata == null) {
            if (other.schemata!= null) {
                return false;
            }
        } else {
            if (!schemata.equals(other.schemata)) {
                return false;
            }
        }
        if (embeddables == null) {
            if (other.embeddables!= null) {
                return false;
            }
        } else {
            if (!embeddables.equals(other.embeddables)) {
                return false;
            }
        }
        if (customTypes == null) {
            if (other.customTypes!= null) {
                return false;
            }
        } else {
            if (!customTypes.equals(other.customTypes)) {
                return false;
            }
        }
        if (enumTypes == null) {
            if (other.enumTypes!= null) {
                return false;
            }
        } else {
            if (!enumTypes.equals(other.enumTypes)) {
                return false;
            }
        }
        if (forcedTypes == null) {
            if (other.forcedTypes!= null) {
                return false;
            }
        } else {
            if (!forcedTypes.equals(other.forcedTypes)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((regexFlags == null)? 0 :regexFlags.hashCode()));
        result = ((prime*result)+((regexMatchesPartialQualification == null)? 0 :regexMatchesPartialQualification.hashCode()));
        result = ((prime*result)+((includes == null)? 0 :includes.hashCode()));
        result = ((prime*result)+((excludes == null)? 0 :excludes.hashCode()));
        result = ((prime*result)+((includeExcludeColumns == null)? 0 :includeExcludeColumns.hashCode()));
        result = ((prime*result)+((includeTables == null)? 0 :includeTables.hashCode()));
        result = ((prime*result)+((includeEmbeddables == null)? 0 :includeEmbeddables.hashCode()));
        result = ((prime*result)+((includeRoutines == null)? 0 :includeRoutines.hashCode()));
        result = ((prime*result)+((includeTriggerRoutines == null)? 0 :includeTriggerRoutines.hashCode()));
        result = ((prime*result)+((includePackages == null)? 0 :includePackages.hashCode()));
        result = ((prime*result)+((includePackageRoutines == null)? 0 :includePackageRoutines.hashCode()));
        result = ((prime*result)+((includePackageUDTs == null)? 0 :includePackageUDTs.hashCode()));
        result = ((prime*result)+((includePackageConstants == null)? 0 :includePackageConstants.hashCode()));
        result = ((prime*result)+((includeUDTs == null)? 0 :includeUDTs.hashCode()));
        result = ((prime*result)+((includeSequences == null)? 0 :includeSequences.hashCode()));
        result = ((prime*result)+((includeIndexes == null)? 0 :includeIndexes.hashCode()));
        result = ((prime*result)+((includePrimaryKeys == null)? 0 :includePrimaryKeys.hashCode()));
        result = ((prime*result)+((includeUniqueKeys == null)? 0 :includeUniqueKeys.hashCode()));
        result = ((prime*result)+((includeForeignKeys == null)? 0 :includeForeignKeys.hashCode()));
        result = ((prime*result)+((includeInvisibleColumns == null)? 0 :includeInvisibleColumns.hashCode()));
        result = ((prime*result)+((recordVersionFields == null)? 0 :recordVersionFields.hashCode()));
        result = ((prime*result)+((recordTimestampFields == null)? 0 :recordTimestampFields.hashCode()));
        result = ((prime*result)+((syntheticIdentities == null)? 0 :syntheticIdentities.hashCode()));
        result = ((prime*result)+((syntheticPrimaryKeys == null)? 0 :syntheticPrimaryKeys.hashCode()));
        result = ((prime*result)+((overridePrimaryKeys == null)? 0 :overridePrimaryKeys.hashCode()));
        result = ((prime*result)+((dateAsTimestamp == null)? 0 :dateAsTimestamp.hashCode()));
        result = ((prime*result)+((ignoreProcedureReturnValues == null)? 0 :ignoreProcedureReturnValues.hashCode()));
        result = ((prime*result)+((unsignedTypes == null)? 0 :unsignedTypes.hashCode()));
        result = ((prime*result)+((inputCatalog == null)? 0 :inputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalog == null)? 0 :outputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalogToDefault == null)? 0 :outputCatalogToDefault.hashCode()));
        result = ((prime*result)+((inputSchema == null)? 0 :inputSchema.hashCode()));
        result = ((prime*result)+((outputSchema == null)? 0 :outputSchema.hashCode()));
        result = ((prime*result)+((outputSchemaToDefault == null)? 0 :outputSchemaToDefault.hashCode()));
        result = ((prime*result)+((schemaVersionProvider == null)? 0 :schemaVersionProvider.hashCode()));
        result = ((prime*result)+((catalogVersionProvider == null)? 0 :catalogVersionProvider.hashCode()));
        result = ((prime*result)+((orderProvider == null)? 0 :orderProvider.hashCode()));
        result = ((prime*result)+((forceIntegerTypesOnZeroScaleDecimals == null)? 0 :forceIntegerTypesOnZeroScaleDecimals.hashCode()));
        result = ((prime*result)+((tableValuedFunctions == null)? 0 :tableValuedFunctions.hashCode()));
        result = ((prime*result)+((logSlowQueriesAfterSeconds == null)? 0 :logSlowQueriesAfterSeconds.hashCode()));
        result = ((prime*result)+((properties == null)? 0 :properties.hashCode()));
        result = ((prime*result)+((catalogs == null)? 0 :catalogs.hashCode()));
        result = ((prime*result)+((schemata == null)? 0 :schemata.hashCode()));
        result = ((prime*result)+((embeddables == null)? 0 :embeddables.hashCode()));
        result = ((prime*result)+((customTypes == null)? 0 :customTypes.hashCode()));
        result = ((prime*result)+((enumTypes == null)? 0 :enumTypes.hashCode()));
        result = ((prime*result)+((forcedTypes == null)? 0 :forcedTypes.hashCode()));
        return result;
    }

}
