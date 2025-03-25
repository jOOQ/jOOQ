
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlList;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
public class Database implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32001L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String java;
    @XmlList
    @XmlElement(defaultValue = "COMMENTS CASE_INSENSITIVE")
    protected List<RegexFlag> regexFlags;
    @XmlElement(defaultValue = "true")
    protected Boolean regexMatchesPartialQualification = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sqlMatchesPartialQualification = true;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String includes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String includeSql;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String excludes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String excludeSql;
    @XmlElement(defaultValue = "false")
    protected Boolean includeExcludeColumns = false;
    @XmlElement(defaultValue = "false")
    protected Boolean includeExcludePackageRoutines = false;
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
    protected Boolean includeXMLSchemaCollections = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeUDTs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeDomains = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeTriggers = true;
    @XmlElement(defaultValue = "true")
    protected Boolean includeSynonyms = true;
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
    protected Boolean includeCheckConstraints = true;
    @XmlElement(defaultValue = "false")
    protected Boolean includeSystemTables = false;
    @XmlElement(defaultValue = "false")
    protected Boolean includeSystemIndexes = false;
    @XmlElement(defaultValue = "false")
    protected Boolean includeSystemCheckConstraints = false;
    @XmlElement(defaultValue = "false")
    protected Boolean includeSystemSequences = false;
    @XmlElement(defaultValue = "false")
    protected Boolean includeSystemUDTs = false;
    @XmlElement(defaultValue = "true")
    protected Boolean includeInvisibleColumns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean invisibleColumnsAsHidden = true;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordVersionFields = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordTimestampFields = "";
    protected SyntheticObjectsType syntheticObjects;
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
    @XmlElement(defaultValue = "true")
    protected Boolean integerDisplayWidths = true;
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
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String embeddablePrimaryKeys;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String embeddableUniqueKeys;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String embeddableDomains;
    @XmlElement(defaultValue = "false")
    protected Boolean readonlyIdentities = false;
    @XmlElement(defaultValue = "true")
    protected Boolean readonlyComputedColumns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean readonlyNonUpdatableColumns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean forcedTypesForBuiltinDataTypeExtensions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean forcedTypesForXMLSchemaCollections = true;
    @XmlElement(defaultValue = "true")
    protected Boolean forceIntegerTypesOnZeroScaleDecimals = true;
    protected Boolean tableValuedFunctions;
    @XmlElement(defaultValue = "false")
    protected Boolean oracleUseDBAViews = false;
    @XmlElement(defaultValue = "5")
    protected Integer logSlowQueriesAfterSeconds = 5;
    @XmlElement(defaultValue = "5")
    protected Integer logSlowResultsAfterSeconds = 5;
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<Property> properties;
    @XmlElementWrapper(name = "comments")
    @XmlElement(name = "comment")
    protected List<CommentType> comments;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<CatalogMappingType> catalogs;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<SchemaMappingType> schemata;
    @XmlElementWrapper(name = "embeddables")
    @XmlElement(name = "embeddable")
    protected List<EmbeddableDefinitionType> embeddables;
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
     * <li>{@link org.jooq.meta.cockroachdb.CockroachDBDatabase}</li>
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
     * <li>{@link org.jooq.meta.yugabytedb.YugabyteDBDatabase}</li>
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
     */
    public String getName() {
        return name;
    }

    /**
     * The database dialect from jooq-meta.
     * Available dialects are named <code>org.util.[database].[database]Database</code>.
     * <p>
     * Natively supported values are:
     * <ul>
     * <li>{@link org.jooq.meta.ase.ASEDatabase}</li>
     * <li>{@link org.jooq.meta.cockroachdb.CockroachDBDatabase}</li>
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
     * <li>{@link org.jooq.meta.yugabytedb.YugabyteDBDatabase}</li>
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
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A self-contained, inline implementation of {@link org.jooq.meta.Database} that will be compiled and class-loaded on the fly by the code generator.
     * 
     */
    public String getJava() {
        return java;
    }

    /**
     * A self-contained, inline implementation of {@link org.jooq.meta.Database} that will be compiled and class-loaded on the fly by the code generator.
     * 
     */
    public void setJava(String value) {
        this.java = value;
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
     * Whether regular expressions that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
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
     * Whether SQL queries that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSqlMatchesPartialQualification() {
        return sqlMatchesPartialQualification;
    }

    /**
     * Whether SQL queries that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSqlMatchesPartialQualification(Boolean value) {
        this.sqlMatchesPartialQualification = value;
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
     */
    public String getIncludes() {
        return includes;
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
     */
    public void setIncludes(String value) {
        this.includes = value;
    }

    /**
     * All elements that are generated from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by includes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public String getIncludeSql() {
        return includeSql;
    }

    /**
     * All elements that are generated from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by includes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public void setIncludeSql(String value) {
        this.includeSql = value;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public String getExcludes() {
        return excludes;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public void setExcludes(String value) {
        this.excludes = value;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by excludes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public String getExcludeSql() {
        return excludeSql;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by excludes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public void setExcludeSql(String value) {
        this.excludeSql = value;
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
     * This flag indicates whether include / exclude patterns should also match columns within tables.
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
     * This flag indicates whether include / exclude patterns should also match routines within packages.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeExcludePackageRoutines() {
        return includeExcludePackageRoutines;
    }

    /**
     * This flag indicates whether include / exclude patterns should also match routines within packages.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeExcludePackageRoutines(Boolean value) {
        this.includeExcludePackageRoutines = value;
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
     * This flag indicates whether tables should be included in output produced by this database
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
     * This flag indicates whether embeddable types should be included in output produced by this database
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
     * This flag indicates whether routines should be included in output produced by this database
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
     * This flag indicates whether trigger implementation routines should be included in output produced by this database (e.g. in PostgreSQL)
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
     * This flag indicates whether packages should be included in output produced by this database
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
     * This flag indicates whether routines contained in packages should be included in output produced by this database
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
     * This flag indicates whether UDTs contained in packages should be included in output produced by this database
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
     * This flag indicates whether constants contained in packages should be included in output produced by this database
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
     * This flag indicates whether XML schema collections should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeXMLSchemaCollections() {
        return includeXMLSchemaCollections;
    }

    /**
     * This flag indicates whether XML schema collections should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeXMLSchemaCollections(Boolean value) {
        this.includeXMLSchemaCollections = value;
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
     * This flag indicates whether udts should be included in output produced by this database
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
     * This flag indicates whether domains should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeDomains() {
        return includeDomains;
    }

    /**
     * This flag indicates whether domains should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeDomains(Boolean value) {
        this.includeDomains = value;
    }

    /**
     * This flag indicates whether triggers should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeTriggers() {
        return includeTriggers;
    }

    /**
     * This flag indicates whether triggers should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeTriggers(Boolean value) {
        this.includeTriggers = value;
    }

    /**
     * This flag indicates whether synonyms should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSynonyms() {
        return includeSynonyms;
    }

    /**
     * This flag indicates whether synonyms should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSynonyms(Boolean value) {
        this.includeSynonyms = value;
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
     * This flag indicates whether sequences should be included in output produced by this database
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
     * This flag indicates whether indexes should be included in output produced by this database
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
     * This flag indicates whether primary keys should be included in output produced by this database
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
     * This flag indicates whether unique keys should be included in output produced by this database
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
     * This flag indicates whether foreign keys should be included in output produced by this database
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
     * This flag indicates whether check constraints should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeCheckConstraints() {
        return includeCheckConstraints;
    }

    /**
     * This flag indicates whether check constraints should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeCheckConstraints(Boolean value) {
        this.includeCheckConstraints = value;
    }

    /**
     * This flag indicates whether system tables should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSystemTables() {
        return includeSystemTables;
    }

    /**
     * This flag indicates whether system tables should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSystemTables(Boolean value) {
        this.includeSystemTables = value;
    }

    /**
     * This flag indicates whether system generated indexes should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSystemIndexes() {
        return includeSystemIndexes;
    }

    /**
     * This flag indicates whether system generated indexes should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSystemIndexes(Boolean value) {
        this.includeSystemIndexes = value;
    }

    /**
     * This flag indicates whether system generated check constraints should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSystemCheckConstraints() {
        return includeSystemCheckConstraints;
    }

    /**
     * This flag indicates whether system generated check constraints should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSystemCheckConstraints(Boolean value) {
        this.includeSystemCheckConstraints = value;
    }

    /**
     * This flag indicates whether system generated sequences should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSystemSequences() {
        return includeSystemSequences;
    }

    /**
     * This flag indicates whether system generated sequences should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSystemSequences(Boolean value) {
        this.includeSystemSequences = value;
    }

    /**
     * This flag indicates whether system generated UDTs should be included in output produced by this database
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSystemUDTs() {
        return includeSystemUDTs;
    }

    /**
     * This flag indicates whether system generated UDTs should be included in output produced by this database
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSystemUDTs(Boolean value) {
        this.includeSystemUDTs = value;
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
     * This flag indicates whether invisible columns should be included in output produced by this database
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
     * This flag indicates whether invisible columns should marked as {@link org.jooq.DataType#hidden()} in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInvisibleColumnsAsHidden() {
        return invisibleColumnsAsHidden;
    }

    /**
     * This flag indicates whether invisible columns should marked as {@link org.jooq.DataType#hidden()} in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInvisibleColumnsAsHidden(Boolean value) {
        this.invisibleColumnsAsHidden = value;
    }

    /**
     * All table and view columns that are used as "version" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public String getRecordVersionFields() {
        return recordVersionFields;
    }

    /**
     * All table and view columns that are used as "version" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public void setRecordVersionFields(String value) {
        this.recordVersionFields = value;
    }

    /**
     * All table and view columns that are used as "timestamp" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public String getRecordTimestampFields() {
        return recordTimestampFields;
    }

    /**
     * All table and view columns that are used as "timestamp" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public void setRecordTimestampFields(String value) {
        this.recordTimestampFields = value;
    }

    /**
     * The synthetic objects configuration.
     * 
     */
    public SyntheticObjectsType getSyntheticObjects() {
        return syntheticObjects;
    }

    /**
     * The synthetic objects configuration.
     * 
     */
    public void setSyntheticObjects(SyntheticObjectsType value) {
        this.syntheticObjects = value;
    }

    /**
     * A regular expression matching all columns that represent identities.
     * <p>
     * To be used if columns are not detected as automatically as identities.
     * 
     */
    public String getSyntheticIdentities() {
        return syntheticIdentities;
    }

    /**
     * A regular expression matching all columns that represent identities.
     * <p>
     * To be used if columns are not detected as automatically as identities.
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
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * Synthetic primary keys will override existing primary keys.
     * 
     */
    public String getSyntheticPrimaryKeys() {
        return syntheticPrimaryKeys;
    }

    /**
     * A regular expression matching all columns that participate in "synthetic" primary keys,
     * which should be placed on generated {@link org.jooq.UpdatableRecord}
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * Synthetic primary keys will override existing primary keys.
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
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * If several keys match, a warning is emitted and the first one encountered will be used.
     * <p>
     * This flag will also replace synthetic primary keys, if it matches.
     * 
     */
    public String getOverridePrimaryKeys() {
        return overridePrimaryKeys;
    }

    /**
     * All (UNIQUE) key names that should be used instead of primary keys on
     * generated {@link org.jooq.UpdatableRecord}.
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * If several keys match, a warning is emitted and the first one encountered will be used.
     * <p>
     * This flag will also replace synthetic primary keys, if it matches.
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
     * Generate {@link java.sql.Timestamp} fields for DATE columns. This is particularly useful for Oracle databases
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
    @Deprecated
    public Boolean isIgnoreProcedureReturnValues() {
        return ignoreProcedureReturnValues;
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
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
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
     * Generate jOOU data types for your unsigned data types, which are not natively supported in Java
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
     * Include display width in type declaration. In some RDBMS (e.g. MariaDB, MySQL), fixed width integer types are optionally accompanied by a display width. This is sometimes abused to model BOOLEAN types via TINYINT(1). This flag allows for including that display width in the type declaration exposed by jOOQ-meta code, as if it were a numeric precision.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIntegerDisplayWidths() {
        return integerDisplayWidths;
    }

    /**
     * Include display width in type declaration. In some RDBMS (e.g. MariaDB, MySQL), fixed width integer types are optionally accompanied by a display width. This is sometimes abused to model BOOLEAN types via TINYINT(1). This flag allows for including that display width in the type declaration exposed by jOOQ-meta code, as if it were a numeric precision.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIntegerDisplayWidths(Boolean value) {
        this.integerDisplayWidths = value;
    }

    /**
     * The catalog that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getCatalogs()} configuration element.
     * If left empty (and without any {@link #getCatalogs()} configuration  element), jOOQ will generate all available catalogs.
     * 
     */
    public String getInputCatalog() {
        return inputCatalog;
    }

    /**
     * The catalog that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getCatalogs()} configuration element.
     * If left empty (and without any {@link #getCatalogs()} configuration  element), jOOQ will generate all available catalogs.
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
     */
    public String getOutputCatalog() {
        return outputCatalog;
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
     * A flag to indicate that the outputCatalog should be the "default" catalog,
     * which generates catalog-less, unqualified tables, procedures, etc.
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
     */
    public String getInputSchema() {
        return inputSchema;
    }

    /**
     * The schema that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getSchemata()} configuration element.
     * If left empty (and without any {@link #getSchemata()} configuration element), jOOQ will generate all available schemata.
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
     */
    public String getOutputSchema() {
        return outputSchema;
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
     * A flag to indicate that the outputSchema should be the "default" schema,
     * which generates schema-less, unqualified tables, procedures, etc.
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
     * Schema versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
     * 
     */
    public String getSchemaVersionProvider() {
        return schemaVersionProvider;
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
     * Schema versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
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
     * Catalog versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
     * 
     */
    public String getCatalogVersionProvider() {
        return catalogVersionProvider;
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
     * Catalog versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
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
     */
    public String getOrderProvider() {
        return orderProvider;
    }

    /**
     * A custom {@link java.util.Comparator} that can compare two {@link org.jooq.meta.Definition} objects to determine their order.
     * <p>
     * This comparator can be used to influence the order of any object that is produced by jOOQ meta, and thus, indirectly, the order of declared objects in generated code.
     * 
     */
    public void setOrderProvider(String value) {
        this.orderProvider = value;
    }

    /**
     * A regular expression matching all primary key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public String getEmbeddablePrimaryKeys() {
        return embeddablePrimaryKeys;
    }

    /**
     * A regular expression matching all primary key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setEmbeddablePrimaryKeys(String value) {
        this.embeddablePrimaryKeys = value;
    }

    /**
     * A regular expression matching all unique key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public String getEmbeddableUniqueKeys() {
        return embeddableUniqueKeys;
    }

    /**
     * A regular expression matching all unique key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setEmbeddableUniqueKeys(String value) {
        this.embeddableUniqueKeys = value;
    }

    /**
     * A regular expression matching all domain type declarations for which wrapper types should be generated.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public String getEmbeddableDomains() {
        return embeddableDomains;
    }

    /**
     * A regular expression matching all domain type declarations for which wrapper types should be generated.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setEmbeddableDomains(String value) {
        this.embeddableDomains = value;
    }

    /**
     * Whether identity columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReadonlyIdentities() {
        return readonlyIdentities;
    }

    /**
     * Whether identity columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadonlyIdentities(Boolean value) {
        this.readonlyIdentities = value;
    }

    /**
     * Whether computed columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReadonlyComputedColumns() {
        return readonlyComputedColumns;
    }

    /**
     * Whether computed columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadonlyComputedColumns(Boolean value) {
        this.readonlyComputedColumns = value;
    }

    /**
     * Whether columns that are known not to be updatable (e.g. in views) should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReadonlyNonUpdatableColumns() {
        return readonlyNonUpdatableColumns;
    }

    /**
     * Whether columns that are known not to be updatable (e.g. in views) should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadonlyNonUpdatableColumns(Boolean value) {
        this.readonlyNonUpdatableColumns = value;
    }

    /**
     * Enable some default forced type configurations for built in data type extensions, such as the ones from the jooq-postgres-extensions module.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForcedTypesForBuiltinDataTypeExtensions() {
        return forcedTypesForBuiltinDataTypeExtensions;
    }

    /**
     * Enable some default forced type configurations for built in data type extensions, such as the ones from the jooq-postgres-extensions module.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForcedTypesForBuiltinDataTypeExtensions(Boolean value) {
        this.forcedTypesForBuiltinDataTypeExtensions = value;
    }

    /**
     * Enable some default forced type configurations for XML schema collections, mapping them to JAXB annotated types using the {@link org.jooq.impl.XMLtoJAXBConverter}
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForcedTypesForXMLSchemaCollections() {
        return forcedTypesForXMLSchemaCollections;
    }

    /**
     * Enable some default forced type configurations for XML schema collections, mapping them to JAXB annotated types using the {@link org.jooq.impl.XMLtoJAXBConverter}
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForcedTypesForXMLSchemaCollections(Boolean value) {
        this.forcedTypesForXMLSchemaCollections = value;
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
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). This allows for turning off this feature. In case of conflict between this rule and actual {@link #getForcedTypes()}, the latter will win.
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
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTableValuedFunctions(Boolean value) {
        this.tableValuedFunctions = value;
    }

    /**
     * Specify whether to use the Oracle DBA_XYZ views instead of the ALL_XYZ views.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOracleUseDBAViews() {
        return oracleUseDBAViews;
    }

    /**
     * Specify whether to use the Oracle DBA_XYZ views instead of the ALL_XYZ views.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOracleUseDBAViews(Boolean value) {
        this.oracleUseDBAViews = value;
    }

    /**
     * The number of seconds that are considered "slow" before a query is logged to indicate a bug, 0 for not logging.
     * 
     */
    public Integer getLogSlowQueriesAfterSeconds() {
        return logSlowQueriesAfterSeconds;
    }

    /**
     * The number of seconds that are considered "slow" before a query is logged to indicate a bug, 0 for not logging.
     * 
     */
    public void setLogSlowQueriesAfterSeconds(Integer value) {
        this.logSlowQueriesAfterSeconds = value;
    }

    /**
     * The number of seconds that are considered "slow" before a result set is logged to indicate a bug, 0 for not logging.
     * 
     */
    public Integer getLogSlowResultsAfterSeconds() {
        return logSlowResultsAfterSeconds;
    }

    /**
     * The number of seconds that are considered "slow" before a result set is logged to indicate a bug, 0 for not logging.
     * 
     */
    public void setLogSlowResultsAfterSeconds(Integer value) {
        this.logSlowResultsAfterSeconds = value;
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

    public List<CommentType> getComments() {
        if (comments == null) {
            comments = new ArrayList<CommentType>();
        }
        return comments;
    }

    public void setComments(List<CommentType> comments) {
        this.comments = comments;
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

    public List<EmbeddableDefinitionType> getEmbeddables() {
        if (embeddables == null) {
            embeddables = new ArrayList<EmbeddableDefinitionType>();
        }
        return embeddables;
    }

    public void setEmbeddables(List<EmbeddableDefinitionType> embeddables) {
        this.embeddables = embeddables;
    }

    @Deprecated
    public List<CustomType> getCustomTypes() {
        if (customTypes == null) {
            customTypes = new ArrayList<CustomType>();
        }
        return customTypes;
    }

    @Deprecated
    public void setCustomTypes(List<CustomType> customTypes) {
        this.customTypes = customTypes;
    }

    @Deprecated
    public List<EnumType> getEnumTypes() {
        if (enumTypes == null) {
            enumTypes = new ArrayList<EnumType>();
        }
        return enumTypes;
    }

    @Deprecated
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

    /**
     * The database dialect from jooq-meta.
     * Available dialects are named <code>org.util.[database].[database]Database</code>.
     * <p>
     * Natively supported values are:
     * <ul>
     * <li>{@link org.jooq.meta.ase.ASEDatabase}</li>
     * <li>{@link org.jooq.meta.cockroachdb.CockroachDBDatabase}</li>
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
     * <li>{@link org.jooq.meta.yugabytedb.YugabyteDBDatabase}</li>
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
     */
    public Database withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A self-contained, inline implementation of {@link org.jooq.meta.Database} that will be compiled and class-loaded on the fly by the code generator.
     * 
     */
    public Database withJava(String value) {
        setJava(value);
        return this;
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
    public Database withRegexFlags(RegexFlag... values) {
        if (values!= null) {
            for (RegexFlag value: values) {
                getRegexFlags().add(value);
            }
        }
        return this;
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
    public Database withRegexFlags(Collection<RegexFlag> values) {
        if (values!= null) {
            getRegexFlags().addAll(values);
        }
        return this;
    }

    /**
     * Whether regular expressions that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
     * 
     */
    public Database withRegexMatchesPartialQualification(Boolean value) {
        setRegexMatchesPartialQualification(value);
        return this;
    }

    /**
     * Whether SQL queries that match qualified object names also match partial qualifications (e.g. `table\.column` matches `schema.table.column`) or only full and/or no qualifications (e.g. `schema\.table\.column` and `column` match `schema.table.column`)
     * 
     */
    public Database withSqlMatchesPartialQualification(Boolean value) {
        setSqlMatchesPartialQualification(value);
        return this;
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
     */
    public Database withIncludes(String value) {
        setIncludes(value);
        return this;
    }

    /**
     * All elements that are generated from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by includes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public Database withIncludeSql(String value) {
        setIncludeSql(value);
        return this;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public Database withExcludes(String value) {
        setExcludes(value);
        return this;
    }

    /**
     * All elements that are excluded from your schema.
     * <p>
     * This is a query that produces Java regular expressions, which are appended to the ones produced by excludes.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * <p>
     * Excludes match before includes, i.e. excludes have a higher priority.
     * 
     */
    public Database withExcludeSql(String value) {
        setExcludeSql(value);
        return this;
    }

    /**
     * This flag indicates whether include / exclude patterns should also match columns within tables.
     * 
     */
    public Database withIncludeExcludeColumns(Boolean value) {
        setIncludeExcludeColumns(value);
        return this;
    }

    /**
     * This flag indicates whether include / exclude patterns should also match routines within packages.
     * 
     */
    public Database withIncludeExcludePackageRoutines(Boolean value) {
        setIncludeExcludePackageRoutines(value);
        return this;
    }

    /**
     * This flag indicates whether tables should be included in output produced by this database
     * 
     */
    public Database withIncludeTables(Boolean value) {
        setIncludeTables(value);
        return this;
    }

    /**
     * This flag indicates whether embeddable types should be included in output produced by this database
     * 
     */
    public Database withIncludeEmbeddables(Boolean value) {
        setIncludeEmbeddables(value);
        return this;
    }

    /**
     * This flag indicates whether routines should be included in output produced by this database
     * 
     */
    public Database withIncludeRoutines(Boolean value) {
        setIncludeRoutines(value);
        return this;
    }

    /**
     * This flag indicates whether trigger implementation routines should be included in output produced by this database (e.g. in PostgreSQL)
     * 
     */
    public Database withIncludeTriggerRoutines(Boolean value) {
        setIncludeTriggerRoutines(value);
        return this;
    }

    /**
     * This flag indicates whether packages should be included in output produced by this database
     * 
     */
    public Database withIncludePackages(Boolean value) {
        setIncludePackages(value);
        return this;
    }

    /**
     * This flag indicates whether routines contained in packages should be included in output produced by this database
     * 
     */
    public Database withIncludePackageRoutines(Boolean value) {
        setIncludePackageRoutines(value);
        return this;
    }

    /**
     * This flag indicates whether UDTs contained in packages should be included in output produced by this database
     * 
     */
    public Database withIncludePackageUDTs(Boolean value) {
        setIncludePackageUDTs(value);
        return this;
    }

    /**
     * This flag indicates whether constants contained in packages should be included in output produced by this database
     * 
     */
    public Database withIncludePackageConstants(Boolean value) {
        setIncludePackageConstants(value);
        return this;
    }

    /**
     * This flag indicates whether XML schema collections should be included in output produced by this database
     * 
     */
    public Database withIncludeXMLSchemaCollections(Boolean value) {
        setIncludeXMLSchemaCollections(value);
        return this;
    }

    /**
     * This flag indicates whether udts should be included in output produced by this database
     * 
     */
    public Database withIncludeUDTs(Boolean value) {
        setIncludeUDTs(value);
        return this;
    }

    /**
     * This flag indicates whether domains should be included in output produced by this database
     * 
     */
    public Database withIncludeDomains(Boolean value) {
        setIncludeDomains(value);
        return this;
    }

    /**
     * This flag indicates whether triggers should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withIncludeTriggers(Boolean value) {
        setIncludeTriggers(value);
        return this;
    }

    /**
     * This flag indicates whether synonyms should be included in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withIncludeSynonyms(Boolean value) {
        setIncludeSynonyms(value);
        return this;
    }

    /**
     * This flag indicates whether sequences should be included in output produced by this database
     * 
     */
    public Database withIncludeSequences(Boolean value) {
        setIncludeSequences(value);
        return this;
    }

    /**
     * This flag indicates whether indexes should be included in output produced by this database
     * 
     */
    public Database withIncludeIndexes(Boolean value) {
        setIncludeIndexes(value);
        return this;
    }

    /**
     * This flag indicates whether primary keys should be included in output produced by this database
     * 
     */
    public Database withIncludePrimaryKeys(Boolean value) {
        setIncludePrimaryKeys(value);
        return this;
    }

    /**
     * This flag indicates whether unique keys should be included in output produced by this database
     * 
     */
    public Database withIncludeUniqueKeys(Boolean value) {
        setIncludeUniqueKeys(value);
        return this;
    }

    /**
     * This flag indicates whether foreign keys should be included in output produced by this database
     * 
     */
    public Database withIncludeForeignKeys(Boolean value) {
        setIncludeForeignKeys(value);
        return this;
    }

    /**
     * This flag indicates whether check constraints should be included in output produced by this database
     * 
     */
    public Database withIncludeCheckConstraints(Boolean value) {
        setIncludeCheckConstraints(value);
        return this;
    }

    /**
     * This flag indicates whether system tables should be included in output produced by this database
     * 
     */
    public Database withIncludeSystemTables(Boolean value) {
        setIncludeSystemTables(value);
        return this;
    }

    /**
     * This flag indicates whether system generated indexes should be included in output produced by this database
     * 
     */
    public Database withIncludeSystemIndexes(Boolean value) {
        setIncludeSystemIndexes(value);
        return this;
    }

    /**
     * This flag indicates whether system generated check constraints should be included in output produced by this database
     * 
     */
    public Database withIncludeSystemCheckConstraints(Boolean value) {
        setIncludeSystemCheckConstraints(value);
        return this;
    }

    /**
     * This flag indicates whether system generated sequences should be included in output produced by this database
     * 
     */
    public Database withIncludeSystemSequences(Boolean value) {
        setIncludeSystemSequences(value);
        return this;
    }

    /**
     * This flag indicates whether system generated UDTs should be included in output produced by this database
     * 
     */
    public Database withIncludeSystemUDTs(Boolean value) {
        setIncludeSystemUDTs(value);
        return this;
    }

    /**
     * This flag indicates whether invisible columns should be included in output produced by this database
     * 
     */
    public Database withIncludeInvisibleColumns(Boolean value) {
        setIncludeInvisibleColumns(value);
        return this;
    }

    /**
     * This flag indicates whether invisible columns should marked as {@link org.jooq.DataType#hidden()} in output produced by this database.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withInvisibleColumnsAsHidden(Boolean value) {
        setInvisibleColumnsAsHidden(value);
        return this;
    }

    /**
     * All table and view columns that are used as "version" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public Database withRecordVersionFields(String value) {
        setRecordVersionFields(value);
        return this;
    }

    /**
     * All table and view columns that are used as "timestamp" fields for optimistic locking.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * See {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#delete()} for details about optimistic locking.
     * 
     */
    public Database withRecordTimestampFields(String value) {
        setRecordTimestampFields(value);
        return this;
    }

    /**
     * The synthetic objects configuration.
     * 
     */
    public Database withSyntheticObjects(SyntheticObjectsType value) {
        setSyntheticObjects(value);
        return this;
    }

    /**
     * A regular expression matching all columns that represent identities.
     * <p>
     * To be used if columns are not detected as automatically as identities.
     * 
     */
    public Database withSyntheticIdentities(String value) {
        setSyntheticIdentities(value);
        return this;
    }

    /**
     * A regular expression matching all columns that participate in "synthetic" primary keys,
     * which should be placed on generated {@link org.jooq.UpdatableRecord}
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * Synthetic primary keys will override existing primary keys.
     * 
     */
    public Database withSyntheticPrimaryKeys(String value) {
        setSyntheticPrimaryKeys(value);
        return this;
    }

    /**
     * All (UNIQUE) key names that should be used instead of primary keys on
     * generated {@link org.jooq.UpdatableRecord}.
     * <p>
     * To be used with:
     * <ul>
     * <li>{@link org.jooq.UpdatableRecord#store()}</li>
     * <li>{@link org.jooq.UpdatableRecord#update()}</li>
     * <li>{@link org.jooq.UpdatableRecord#delete()}</li>
     * <li>{@link org.jooq.UpdatableRecord#refresh()}</li>
     * </ul>
     * <p>
     * If several keys match, a warning is emitted and the first one encountered will be used.
     * <p>
     * This flag will also replace synthetic primary keys, if it matches.
     * 
     */
    public Database withOverridePrimaryKeys(String value) {
        setOverridePrimaryKeys(value);
        return this;
    }

    /**
     * Generate {@link java.sql.Timestamp} fields for DATE columns. This is particularly useful for Oracle databases
     * 
     */
    public Database withDateAsTimestamp(Boolean value) {
        setDateAsTimestamp(value);
        return this;
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
     */
    public Database withIgnoreProcedureReturnValues(Boolean value) {
        setIgnoreProcedureReturnValues(value);
        return this;
    }

    /**
     * Generate jOOU data types for your unsigned data types, which are not natively supported in Java
     * 
     */
    public Database withUnsignedTypes(Boolean value) {
        setUnsignedTypes(value);
        return this;
    }

    /**
     * Include display width in type declaration. In some RDBMS (e.g. MariaDB, MySQL), fixed width integer types are optionally accompanied by a display width. This is sometimes abused to model BOOLEAN types via TINYINT(1). This flag allows for including that display width in the type declaration exposed by jOOQ-meta code, as if it were a numeric precision.
     * 
     */
    public Database withIntegerDisplayWidths(Boolean value) {
        setIntegerDisplayWidths(value);
        return this;
    }

    /**
     * The catalog that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getCatalogs()} configuration element.
     * If left empty (and without any {@link #getCatalogs()} configuration  element), jOOQ will generate all available catalogs.
     * 
     */
    public Database withInputCatalog(String value) {
        setInputCatalog(value);
        return this;
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
     */
    public Database withOutputCatalog(String value) {
        setOutputCatalog(value);
        return this;
    }

    /**
     * A flag to indicate that the outputCatalog should be the "default" catalog,
     * which generates catalog-less, unqualified tables, procedures, etc.
     * 
     */
    public Database withOutputCatalogToDefault(Boolean value) {
        setOutputCatalogToDefault(value);
        return this;
    }

    /**
     * The schema that is used locally as a source for meta information.
     * <p>
     * This cannot be combined with the {@link #getSchemata()} configuration element.
     * If left empty (and without any {@link #getSchemata()} configuration element), jOOQ will generate all available schemata.
     * 
     */
    public Database withInputSchema(String value) {
        setInputSchema(value);
        return this;
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
     */
    public Database withOutputSchema(String value) {
        setOutputSchema(value);
        return this;
    }

    /**
     * A flag to indicate that the outputSchema should be the "default" schema,
     * which generates schema-less, unqualified tables, procedures, etc.
     * 
     */
    public Database withOutputSchemaToDefault(Boolean value) {
        setOutputSchemaToDefault(value);
        return this;
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
     * Schema versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
     * 
     */
    public Database withSchemaVersionProvider(String value) {
        setSchemaVersionProvider(value);
        return this;
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
     * Catalog versions will be generated into the {@link javax.annotation.processing.Generated} annotation on
     * generated artefacts.
     * 
     */
    public Database withCatalogVersionProvider(String value) {
        setCatalogVersionProvider(value);
        return this;
    }

    /**
     * A custom {@link java.util.Comparator} that can compare two {@link org.jooq.meta.Definition} objects to determine their order.
     * <p>
     * This comparator can be used to influence the order of any object that is produced by jOOQ meta, and thus, indirectly, the order of declared objects in generated code.
     * 
     */
    public Database withOrderProvider(String value) {
        setOrderProvider(value);
        return this;
    }

    /**
     * A regular expression matching all primary key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withEmbeddablePrimaryKeys(String value) {
        setEmbeddablePrimaryKeys(value);
        return this;
    }

    /**
     * A regular expression matching all unique key declarations for which wrapper types should be generated, and for their referencing foreign keys.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withEmbeddableUniqueKeys(String value) {
        setEmbeddableUniqueKeys(value);
        return this;
    }

    /**
     * A regular expression matching all domain type declarations for which wrapper types should be generated.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withEmbeddableDomains(String value) {
        setEmbeddableDomains(value);
        return this;
    }

    /**
     * Whether identity columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withReadonlyIdentities(Boolean value) {
        setReadonlyIdentities(value);
        return this;
    }

    /**
     * Whether computed columns should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withReadonlyComputedColumns(Boolean value) {
        setReadonlyComputedColumns(value);
        return this;
    }

    /**
     * Whether columns that are known not to be updatable (e.g. in views) should expose {@link org.jooq.DataType#readonly()} behaviour.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withReadonlyNonUpdatableColumns(Boolean value) {
        setReadonlyNonUpdatableColumns(value);
        return this;
    }

    /**
     * Enable some default forced type configurations for built in data type extensions, such as the ones from the jooq-postgres-extensions module.
     * 
     */
    public Database withForcedTypesForBuiltinDataTypeExtensions(Boolean value) {
        setForcedTypesForBuiltinDataTypeExtensions(value);
        return this;
    }

    /**
     * Enable some default forced type configurations for XML schema collections, mapping them to JAXB annotated types using the {@link org.jooq.impl.XMLtoJAXBConverter}
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Database withForcedTypesForXMLSchemaCollections(Boolean value) {
        setForcedTypesForXMLSchemaCollections(value);
        return this;
    }

    /**
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). This allows for turning off this feature. In case of conflict between this rule and actual {@link #getForcedTypes()}, the latter will win.
     * 
     */
    public Database withForceIntegerTypesOnZeroScaleDecimals(Boolean value) {
        setForceIntegerTypesOnZeroScaleDecimals(value);
        return this;
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
     */
    public Database withTableValuedFunctions(Boolean value) {
        setTableValuedFunctions(value);
        return this;
    }

    /**
     * Specify whether to use the Oracle DBA_XYZ views instead of the ALL_XYZ views.
     * 
     */
    public Database withOracleUseDBAViews(Boolean value) {
        setOracleUseDBAViews(value);
        return this;
    }

    /**
     * The number of seconds that are considered "slow" before a query is logged to indicate a bug, 0 for not logging.
     * 
     */
    public Database withLogSlowQueriesAfterSeconds(Integer value) {
        setLogSlowQueriesAfterSeconds(value);
        return this;
    }

    /**
     * The number of seconds that are considered "slow" before a result set is logged to indicate a bug, 0 for not logging.
     * 
     */
    public Database withLogSlowResultsAfterSeconds(Integer value) {
        setLogSlowResultsAfterSeconds(value);
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

    public Database withComments(CommentType... values) {
        if (values!= null) {
            for (CommentType value: values) {
                getComments().add(value);
            }
        }
        return this;
    }

    public Database withComments(Collection<CommentType> values) {
        if (values!= null) {
            getComments().addAll(values);
        }
        return this;
    }

    public Database withComments(List<CommentType> comments) {
        setComments(comments);
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

    public Database withEmbeddables(EmbeddableDefinitionType... values) {
        if (values!= null) {
            for (EmbeddableDefinitionType value: values) {
                getEmbeddables().add(value);
            }
        }
        return this;
    }

    public Database withEmbeddables(Collection<EmbeddableDefinitionType> values) {
        if (values!= null) {
            getEmbeddables().addAll(values);
        }
        return this;
    }

    public Database withEmbeddables(List<EmbeddableDefinitionType> embeddables) {
        setEmbeddables(embeddables);
        return this;
    }

    @Deprecated
    public Database withCustomTypes(CustomType... values) {
        if (values!= null) {
            for (CustomType value: values) {
                getCustomTypes().add(value);
            }
        }
        return this;
    }

    @Deprecated
    public Database withCustomTypes(Collection<CustomType> values) {
        if (values!= null) {
            getCustomTypes().addAll(values);
        }
        return this;
    }

    @Deprecated
    public Database withCustomTypes(List<CustomType> customTypes) {
        setCustomTypes(customTypes);
        return this;
    }

    @Deprecated
    public Database withEnumTypes(EnumType... values) {
        if (values!= null) {
            for (EnumType value: values) {
                getEnumTypes().add(value);
            }
        }
        return this;
    }

    @Deprecated
    public Database withEnumTypes(Collection<EnumType> values) {
        if (values!= null) {
            getEnumTypes().addAll(values);
        }
        return this;
    }

    @Deprecated
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
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("java", java);
        builder.append("regexFlags", "regexFlags", regexFlags);
        builder.append("regexMatchesPartialQualification", regexMatchesPartialQualification);
        builder.append("sqlMatchesPartialQualification", sqlMatchesPartialQualification);
        builder.append("includes", includes);
        builder.append("includeSql", includeSql);
        builder.append("excludes", excludes);
        builder.append("excludeSql", excludeSql);
        builder.append("includeExcludeColumns", includeExcludeColumns);
        builder.append("includeExcludePackageRoutines", includeExcludePackageRoutines);
        builder.append("includeTables", includeTables);
        builder.append("includeEmbeddables", includeEmbeddables);
        builder.append("includeRoutines", includeRoutines);
        builder.append("includeTriggerRoutines", includeTriggerRoutines);
        builder.append("includePackages", includePackages);
        builder.append("includePackageRoutines", includePackageRoutines);
        builder.append("includePackageUDTs", includePackageUDTs);
        builder.append("includePackageConstants", includePackageConstants);
        builder.append("includeXMLSchemaCollections", includeXMLSchemaCollections);
        builder.append("includeUDTs", includeUDTs);
        builder.append("includeDomains", includeDomains);
        builder.append("includeTriggers", includeTriggers);
        builder.append("includeSynonyms", includeSynonyms);
        builder.append("includeSequences", includeSequences);
        builder.append("includeIndexes", includeIndexes);
        builder.append("includePrimaryKeys", includePrimaryKeys);
        builder.append("includeUniqueKeys", includeUniqueKeys);
        builder.append("includeForeignKeys", includeForeignKeys);
        builder.append("includeCheckConstraints", includeCheckConstraints);
        builder.append("includeSystemTables", includeSystemTables);
        builder.append("includeSystemIndexes", includeSystemIndexes);
        builder.append("includeSystemCheckConstraints", includeSystemCheckConstraints);
        builder.append("includeSystemSequences", includeSystemSequences);
        builder.append("includeSystemUDTs", includeSystemUDTs);
        builder.append("includeInvisibleColumns", includeInvisibleColumns);
        builder.append("invisibleColumnsAsHidden", invisibleColumnsAsHidden);
        builder.append("recordVersionFields", recordVersionFields);
        builder.append("recordTimestampFields", recordTimestampFields);
        builder.append("syntheticObjects", syntheticObjects);
        builder.append("syntheticIdentities", syntheticIdentities);
        builder.append("syntheticPrimaryKeys", syntheticPrimaryKeys);
        builder.append("overridePrimaryKeys", overridePrimaryKeys);
        builder.append("dateAsTimestamp", dateAsTimestamp);
        builder.append("ignoreProcedureReturnValues", ignoreProcedureReturnValues);
        builder.append("unsignedTypes", unsignedTypes);
        builder.append("integerDisplayWidths", integerDisplayWidths);
        builder.append("inputCatalog", inputCatalog);
        builder.append("outputCatalog", outputCatalog);
        builder.append("outputCatalogToDefault", outputCatalogToDefault);
        builder.append("inputSchema", inputSchema);
        builder.append("outputSchema", outputSchema);
        builder.append("outputSchemaToDefault", outputSchemaToDefault);
        builder.append("schemaVersionProvider", schemaVersionProvider);
        builder.append("catalogVersionProvider", catalogVersionProvider);
        builder.append("orderProvider", orderProvider);
        builder.append("embeddablePrimaryKeys", embeddablePrimaryKeys);
        builder.append("embeddableUniqueKeys", embeddableUniqueKeys);
        builder.append("embeddableDomains", embeddableDomains);
        builder.append("readonlyIdentities", readonlyIdentities);
        builder.append("readonlyComputedColumns", readonlyComputedColumns);
        builder.append("readonlyNonUpdatableColumns", readonlyNonUpdatableColumns);
        builder.append("forcedTypesForBuiltinDataTypeExtensions", forcedTypesForBuiltinDataTypeExtensions);
        builder.append("forcedTypesForXMLSchemaCollections", forcedTypesForXMLSchemaCollections);
        builder.append("forceIntegerTypesOnZeroScaleDecimals", forceIntegerTypesOnZeroScaleDecimals);
        builder.append("tableValuedFunctions", tableValuedFunctions);
        builder.append("oracleUseDBAViews", oracleUseDBAViews);
        builder.append("logSlowQueriesAfterSeconds", logSlowQueriesAfterSeconds);
        builder.append("logSlowResultsAfterSeconds", logSlowResultsAfterSeconds);
        builder.append("properties", "property", properties);
        builder.append("comments", "comment", comments);
        builder.append("catalogs", "catalog", catalogs);
        builder.append("schemata", "schema", schemata);
        builder.append("embeddables", "embeddable", embeddables);
        builder.append("customTypes", "customType", customTypes);
        builder.append("enumTypes", "enumType", enumTypes);
        builder.append("forcedTypes", "forcedType", forcedTypes);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
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
        if (java == null) {
            if (other.java!= null) {
                return false;
            }
        } else {
            if (!java.equals(other.java)) {
                return false;
            }
        }
        if ((regexFlags == null)||regexFlags.isEmpty()) {
            if ((other.regexFlags!= null)&&(!other.regexFlags.isEmpty())) {
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
        if (sqlMatchesPartialQualification == null) {
            if (other.sqlMatchesPartialQualification!= null) {
                return false;
            }
        } else {
            if (!sqlMatchesPartialQualification.equals(other.sqlMatchesPartialQualification)) {
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
        if (includeSql == null) {
            if (other.includeSql!= null) {
                return false;
            }
        } else {
            if (!includeSql.equals(other.includeSql)) {
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
        if (excludeSql == null) {
            if (other.excludeSql!= null) {
                return false;
            }
        } else {
            if (!excludeSql.equals(other.excludeSql)) {
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
        if (includeExcludePackageRoutines == null) {
            if (other.includeExcludePackageRoutines!= null) {
                return false;
            }
        } else {
            if (!includeExcludePackageRoutines.equals(other.includeExcludePackageRoutines)) {
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
        if (includeXMLSchemaCollections == null) {
            if (other.includeXMLSchemaCollections!= null) {
                return false;
            }
        } else {
            if (!includeXMLSchemaCollections.equals(other.includeXMLSchemaCollections)) {
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
        if (includeDomains == null) {
            if (other.includeDomains!= null) {
                return false;
            }
        } else {
            if (!includeDomains.equals(other.includeDomains)) {
                return false;
            }
        }
        if (includeTriggers == null) {
            if (other.includeTriggers!= null) {
                return false;
            }
        } else {
            if (!includeTriggers.equals(other.includeTriggers)) {
                return false;
            }
        }
        if (includeSynonyms == null) {
            if (other.includeSynonyms!= null) {
                return false;
            }
        } else {
            if (!includeSynonyms.equals(other.includeSynonyms)) {
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
        if (includeCheckConstraints == null) {
            if (other.includeCheckConstraints!= null) {
                return false;
            }
        } else {
            if (!includeCheckConstraints.equals(other.includeCheckConstraints)) {
                return false;
            }
        }
        if (includeSystemTables == null) {
            if (other.includeSystemTables!= null) {
                return false;
            }
        } else {
            if (!includeSystemTables.equals(other.includeSystemTables)) {
                return false;
            }
        }
        if (includeSystemIndexes == null) {
            if (other.includeSystemIndexes!= null) {
                return false;
            }
        } else {
            if (!includeSystemIndexes.equals(other.includeSystemIndexes)) {
                return false;
            }
        }
        if (includeSystemCheckConstraints == null) {
            if (other.includeSystemCheckConstraints!= null) {
                return false;
            }
        } else {
            if (!includeSystemCheckConstraints.equals(other.includeSystemCheckConstraints)) {
                return false;
            }
        }
        if (includeSystemSequences == null) {
            if (other.includeSystemSequences!= null) {
                return false;
            }
        } else {
            if (!includeSystemSequences.equals(other.includeSystemSequences)) {
                return false;
            }
        }
        if (includeSystemUDTs == null) {
            if (other.includeSystemUDTs!= null) {
                return false;
            }
        } else {
            if (!includeSystemUDTs.equals(other.includeSystemUDTs)) {
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
        if (invisibleColumnsAsHidden == null) {
            if (other.invisibleColumnsAsHidden!= null) {
                return false;
            }
        } else {
            if (!invisibleColumnsAsHidden.equals(other.invisibleColumnsAsHidden)) {
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
        if (syntheticObjects == null) {
            if (other.syntheticObjects!= null) {
                return false;
            }
        } else {
            if (!syntheticObjects.equals(other.syntheticObjects)) {
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
        if (integerDisplayWidths == null) {
            if (other.integerDisplayWidths!= null) {
                return false;
            }
        } else {
            if (!integerDisplayWidths.equals(other.integerDisplayWidths)) {
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
        if (embeddablePrimaryKeys == null) {
            if (other.embeddablePrimaryKeys!= null) {
                return false;
            }
        } else {
            if (!embeddablePrimaryKeys.equals(other.embeddablePrimaryKeys)) {
                return false;
            }
        }
        if (embeddableUniqueKeys == null) {
            if (other.embeddableUniqueKeys!= null) {
                return false;
            }
        } else {
            if (!embeddableUniqueKeys.equals(other.embeddableUniqueKeys)) {
                return false;
            }
        }
        if (embeddableDomains == null) {
            if (other.embeddableDomains!= null) {
                return false;
            }
        } else {
            if (!embeddableDomains.equals(other.embeddableDomains)) {
                return false;
            }
        }
        if (readonlyIdentities == null) {
            if (other.readonlyIdentities!= null) {
                return false;
            }
        } else {
            if (!readonlyIdentities.equals(other.readonlyIdentities)) {
                return false;
            }
        }
        if (readonlyComputedColumns == null) {
            if (other.readonlyComputedColumns!= null) {
                return false;
            }
        } else {
            if (!readonlyComputedColumns.equals(other.readonlyComputedColumns)) {
                return false;
            }
        }
        if (readonlyNonUpdatableColumns == null) {
            if (other.readonlyNonUpdatableColumns!= null) {
                return false;
            }
        } else {
            if (!readonlyNonUpdatableColumns.equals(other.readonlyNonUpdatableColumns)) {
                return false;
            }
        }
        if (forcedTypesForBuiltinDataTypeExtensions == null) {
            if (other.forcedTypesForBuiltinDataTypeExtensions!= null) {
                return false;
            }
        } else {
            if (!forcedTypesForBuiltinDataTypeExtensions.equals(other.forcedTypesForBuiltinDataTypeExtensions)) {
                return false;
            }
        }
        if (forcedTypesForXMLSchemaCollections == null) {
            if (other.forcedTypesForXMLSchemaCollections!= null) {
                return false;
            }
        } else {
            if (!forcedTypesForXMLSchemaCollections.equals(other.forcedTypesForXMLSchemaCollections)) {
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
        if (oracleUseDBAViews == null) {
            if (other.oracleUseDBAViews!= null) {
                return false;
            }
        } else {
            if (!oracleUseDBAViews.equals(other.oracleUseDBAViews)) {
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
        if (logSlowResultsAfterSeconds == null) {
            if (other.logSlowResultsAfterSeconds!= null) {
                return false;
            }
        } else {
            if (!logSlowResultsAfterSeconds.equals(other.logSlowResultsAfterSeconds)) {
                return false;
            }
        }
        if ((properties == null)||properties.isEmpty()) {
            if ((other.properties!= null)&&(!other.properties.isEmpty())) {
                return false;
            }
        } else {
            if (!properties.equals(other.properties)) {
                return false;
            }
        }
        if ((comments == null)||comments.isEmpty()) {
            if ((other.comments!= null)&&(!other.comments.isEmpty())) {
                return false;
            }
        } else {
            if (!comments.equals(other.comments)) {
                return false;
            }
        }
        if ((catalogs == null)||catalogs.isEmpty()) {
            if ((other.catalogs!= null)&&(!other.catalogs.isEmpty())) {
                return false;
            }
        } else {
            if (!catalogs.equals(other.catalogs)) {
                return false;
            }
        }
        if ((schemata == null)||schemata.isEmpty()) {
            if ((other.schemata!= null)&&(!other.schemata.isEmpty())) {
                return false;
            }
        } else {
            if (!schemata.equals(other.schemata)) {
                return false;
            }
        }
        if ((embeddables == null)||embeddables.isEmpty()) {
            if ((other.embeddables!= null)&&(!other.embeddables.isEmpty())) {
                return false;
            }
        } else {
            if (!embeddables.equals(other.embeddables)) {
                return false;
            }
        }
        if ((customTypes == null)||customTypes.isEmpty()) {
            if ((other.customTypes!= null)&&(!other.customTypes.isEmpty())) {
                return false;
            }
        } else {
            if (!customTypes.equals(other.customTypes)) {
                return false;
            }
        }
        if ((enumTypes == null)||enumTypes.isEmpty()) {
            if ((other.enumTypes!= null)&&(!other.enumTypes.isEmpty())) {
                return false;
            }
        } else {
            if (!enumTypes.equals(other.enumTypes)) {
                return false;
            }
        }
        if ((forcedTypes == null)||forcedTypes.isEmpty()) {
            if ((other.forcedTypes!= null)&&(!other.forcedTypes.isEmpty())) {
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
        result = ((prime*result)+((java == null)? 0 :java.hashCode()));
        result = ((prime*result)+(((regexFlags == null)||regexFlags.isEmpty())? 0 :regexFlags.hashCode()));
        result = ((prime*result)+((regexMatchesPartialQualification == null)? 0 :regexMatchesPartialQualification.hashCode()));
        result = ((prime*result)+((sqlMatchesPartialQualification == null)? 0 :sqlMatchesPartialQualification.hashCode()));
        result = ((prime*result)+((includes == null)? 0 :includes.hashCode()));
        result = ((prime*result)+((includeSql == null)? 0 :includeSql.hashCode()));
        result = ((prime*result)+((excludes == null)? 0 :excludes.hashCode()));
        result = ((prime*result)+((excludeSql == null)? 0 :excludeSql.hashCode()));
        result = ((prime*result)+((includeExcludeColumns == null)? 0 :includeExcludeColumns.hashCode()));
        result = ((prime*result)+((includeExcludePackageRoutines == null)? 0 :includeExcludePackageRoutines.hashCode()));
        result = ((prime*result)+((includeTables == null)? 0 :includeTables.hashCode()));
        result = ((prime*result)+((includeEmbeddables == null)? 0 :includeEmbeddables.hashCode()));
        result = ((prime*result)+((includeRoutines == null)? 0 :includeRoutines.hashCode()));
        result = ((prime*result)+((includeTriggerRoutines == null)? 0 :includeTriggerRoutines.hashCode()));
        result = ((prime*result)+((includePackages == null)? 0 :includePackages.hashCode()));
        result = ((prime*result)+((includePackageRoutines == null)? 0 :includePackageRoutines.hashCode()));
        result = ((prime*result)+((includePackageUDTs == null)? 0 :includePackageUDTs.hashCode()));
        result = ((prime*result)+((includePackageConstants == null)? 0 :includePackageConstants.hashCode()));
        result = ((prime*result)+((includeXMLSchemaCollections == null)? 0 :includeXMLSchemaCollections.hashCode()));
        result = ((prime*result)+((includeUDTs == null)? 0 :includeUDTs.hashCode()));
        result = ((prime*result)+((includeDomains == null)? 0 :includeDomains.hashCode()));
        result = ((prime*result)+((includeTriggers == null)? 0 :includeTriggers.hashCode()));
        result = ((prime*result)+((includeSynonyms == null)? 0 :includeSynonyms.hashCode()));
        result = ((prime*result)+((includeSequences == null)? 0 :includeSequences.hashCode()));
        result = ((prime*result)+((includeIndexes == null)? 0 :includeIndexes.hashCode()));
        result = ((prime*result)+((includePrimaryKeys == null)? 0 :includePrimaryKeys.hashCode()));
        result = ((prime*result)+((includeUniqueKeys == null)? 0 :includeUniqueKeys.hashCode()));
        result = ((prime*result)+((includeForeignKeys == null)? 0 :includeForeignKeys.hashCode()));
        result = ((prime*result)+((includeCheckConstraints == null)? 0 :includeCheckConstraints.hashCode()));
        result = ((prime*result)+((includeSystemTables == null)? 0 :includeSystemTables.hashCode()));
        result = ((prime*result)+((includeSystemIndexes == null)? 0 :includeSystemIndexes.hashCode()));
        result = ((prime*result)+((includeSystemCheckConstraints == null)? 0 :includeSystemCheckConstraints.hashCode()));
        result = ((prime*result)+((includeSystemSequences == null)? 0 :includeSystemSequences.hashCode()));
        result = ((prime*result)+((includeSystemUDTs == null)? 0 :includeSystemUDTs.hashCode()));
        result = ((prime*result)+((includeInvisibleColumns == null)? 0 :includeInvisibleColumns.hashCode()));
        result = ((prime*result)+((invisibleColumnsAsHidden == null)? 0 :invisibleColumnsAsHidden.hashCode()));
        result = ((prime*result)+((recordVersionFields == null)? 0 :recordVersionFields.hashCode()));
        result = ((prime*result)+((recordTimestampFields == null)? 0 :recordTimestampFields.hashCode()));
        result = ((prime*result)+((syntheticObjects == null)? 0 :syntheticObjects.hashCode()));
        result = ((prime*result)+((syntheticIdentities == null)? 0 :syntheticIdentities.hashCode()));
        result = ((prime*result)+((syntheticPrimaryKeys == null)? 0 :syntheticPrimaryKeys.hashCode()));
        result = ((prime*result)+((overridePrimaryKeys == null)? 0 :overridePrimaryKeys.hashCode()));
        result = ((prime*result)+((dateAsTimestamp == null)? 0 :dateAsTimestamp.hashCode()));
        result = ((prime*result)+((ignoreProcedureReturnValues == null)? 0 :ignoreProcedureReturnValues.hashCode()));
        result = ((prime*result)+((unsignedTypes == null)? 0 :unsignedTypes.hashCode()));
        result = ((prime*result)+((integerDisplayWidths == null)? 0 :integerDisplayWidths.hashCode()));
        result = ((prime*result)+((inputCatalog == null)? 0 :inputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalog == null)? 0 :outputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalogToDefault == null)? 0 :outputCatalogToDefault.hashCode()));
        result = ((prime*result)+((inputSchema == null)? 0 :inputSchema.hashCode()));
        result = ((prime*result)+((outputSchema == null)? 0 :outputSchema.hashCode()));
        result = ((prime*result)+((outputSchemaToDefault == null)? 0 :outputSchemaToDefault.hashCode()));
        result = ((prime*result)+((schemaVersionProvider == null)? 0 :schemaVersionProvider.hashCode()));
        result = ((prime*result)+((catalogVersionProvider == null)? 0 :catalogVersionProvider.hashCode()));
        result = ((prime*result)+((orderProvider == null)? 0 :orderProvider.hashCode()));
        result = ((prime*result)+((embeddablePrimaryKeys == null)? 0 :embeddablePrimaryKeys.hashCode()));
        result = ((prime*result)+((embeddableUniqueKeys == null)? 0 :embeddableUniqueKeys.hashCode()));
        result = ((prime*result)+((embeddableDomains == null)? 0 :embeddableDomains.hashCode()));
        result = ((prime*result)+((readonlyIdentities == null)? 0 :readonlyIdentities.hashCode()));
        result = ((prime*result)+((readonlyComputedColumns == null)? 0 :readonlyComputedColumns.hashCode()));
        result = ((prime*result)+((readonlyNonUpdatableColumns == null)? 0 :readonlyNonUpdatableColumns.hashCode()));
        result = ((prime*result)+((forcedTypesForBuiltinDataTypeExtensions == null)? 0 :forcedTypesForBuiltinDataTypeExtensions.hashCode()));
        result = ((prime*result)+((forcedTypesForXMLSchemaCollections == null)? 0 :forcedTypesForXMLSchemaCollections.hashCode()));
        result = ((prime*result)+((forceIntegerTypesOnZeroScaleDecimals == null)? 0 :forceIntegerTypesOnZeroScaleDecimals.hashCode()));
        result = ((prime*result)+((tableValuedFunctions == null)? 0 :tableValuedFunctions.hashCode()));
        result = ((prime*result)+((oracleUseDBAViews == null)? 0 :oracleUseDBAViews.hashCode()));
        result = ((prime*result)+((logSlowQueriesAfterSeconds == null)? 0 :logSlowQueriesAfterSeconds.hashCode()));
        result = ((prime*result)+((logSlowResultsAfterSeconds == null)? 0 :logSlowResultsAfterSeconds.hashCode()));
        result = ((prime*result)+(((properties == null)||properties.isEmpty())? 0 :properties.hashCode()));
        result = ((prime*result)+(((comments == null)||comments.isEmpty())? 0 :comments.hashCode()));
        result = ((prime*result)+(((catalogs == null)||catalogs.isEmpty())? 0 :catalogs.hashCode()));
        result = ((prime*result)+(((schemata == null)||schemata.isEmpty())? 0 :schemata.hashCode()));
        result = ((prime*result)+(((embeddables == null)||embeddables.isEmpty())? 0 :embeddables.hashCode()));
        result = ((prime*result)+(((customTypes == null)||customTypes.isEmpty())? 0 :customTypes.hashCode()));
        result = ((prime*result)+(((enumTypes == null)||enumTypes.isEmpty())? 0 :enumTypes.hashCode()));
        result = ((prime*result)+(((forcedTypes == null)||forcedTypes.isEmpty())? 0 :forcedTypes.hashCode()));
        return result;
    }

}
