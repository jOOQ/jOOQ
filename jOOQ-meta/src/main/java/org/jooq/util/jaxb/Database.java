







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
 * <p>Java-Klasse für Database complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Database"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="properties" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Properties" minOccurs="0"/&gt;
 *         &lt;element name="regexFlags" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}RegexFlags" minOccurs="0"/&gt;
 *         &lt;element name="includes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="excludes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="includeExcludeColumns" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeTables" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeRoutines" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includePackages" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeUDTs" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeSequences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includePrimaryKeys" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeUniqueKeys" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="includeForeignKeys" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="recordVersionFields" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="recordTimestampFields" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="syntheticIdentities" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="syntheticPrimaryKeys" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="overridePrimaryKeys" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateAsTimestamp" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="ignoreProcedureReturnValues" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="unsignedTypes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="inputCatalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputCatalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputCatalogToDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="inputSchema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputSchema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputSchemaToDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="catalogs" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Catalogs" minOccurs="0"/&gt;
 *         &lt;element name="schemata" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Schemata" minOccurs="0"/&gt;
 *         &lt;element name="schemaVersionProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="catalogVersionProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="customTypes" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}CustomTypes" minOccurs="0"/&gt;
 *         &lt;element name="enumTypes" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}EnumTypes" minOccurs="0"/&gt;
 *         &lt;element name="forcedTypes" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}ForcedTypes" minOccurs="0"/&gt;
 *         &lt;element name="tableValuedFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
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
     * Ruft den Wert der name-Eigenschaft ab.
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
     * Legt den Wert der name-Eigenschaft fest.
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
     * Gets the value of the regexFlags property.
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
     * Ruft den Wert der includes-Eigenschaft ab.
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
     * Legt den Wert der includes-Eigenschaft fest.
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
     * Ruft den Wert der excludes-Eigenschaft ab.
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
     * Legt den Wert der excludes-Eigenschaft fest.
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
     * Ruft den Wert der includeExcludeColumns-Eigenschaft ab.
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
     * Legt den Wert der includeExcludeColumns-Eigenschaft fest.
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
     * Ruft den Wert der includeTables-Eigenschaft ab.
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
     * Legt den Wert der includeTables-Eigenschaft fest.
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
     * Ruft den Wert der includeRoutines-Eigenschaft ab.
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
     * Legt den Wert der includeRoutines-Eigenschaft fest.
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
     * Ruft den Wert der includePackages-Eigenschaft ab.
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
     * Legt den Wert der includePackages-Eigenschaft fest.
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
     * Ruft den Wert der includeUDTs-Eigenschaft ab.
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
     * Legt den Wert der includeUDTs-Eigenschaft fest.
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
     * Ruft den Wert der includeSequences-Eigenschaft ab.
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
     * Legt den Wert der includeSequences-Eigenschaft fest.
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
     * Ruft den Wert der includePrimaryKeys-Eigenschaft ab.
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
     * Legt den Wert der includePrimaryKeys-Eigenschaft fest.
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
     * Ruft den Wert der includeUniqueKeys-Eigenschaft ab.
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
     * Legt den Wert der includeUniqueKeys-Eigenschaft fest.
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
     * Ruft den Wert der includeForeignKeys-Eigenschaft ab.
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
     * Legt den Wert der includeForeignKeys-Eigenschaft fest.
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
     * Ruft den Wert der recordVersionFields-Eigenschaft ab.
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
     * Legt den Wert der recordVersionFields-Eigenschaft fest.
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
     * Ruft den Wert der recordTimestampFields-Eigenschaft ab.
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
     * Legt den Wert der recordTimestampFields-Eigenschaft fest.
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
     * Ruft den Wert der syntheticIdentities-Eigenschaft ab.
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
     * Legt den Wert der syntheticIdentities-Eigenschaft fest.
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
     * Ruft den Wert der syntheticPrimaryKeys-Eigenschaft ab.
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
     * Legt den Wert der syntheticPrimaryKeys-Eigenschaft fest.
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
     * Ruft den Wert der overridePrimaryKeys-Eigenschaft ab.
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
     * Legt den Wert der overridePrimaryKeys-Eigenschaft fest.
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
     * Ruft den Wert der dateAsTimestamp-Eigenschaft ab.
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
     * Legt den Wert der dateAsTimestamp-Eigenschaft fest.
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
     * Ruft den Wert der ignoreProcedureReturnValues-Eigenschaft ab.
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
     * Legt den Wert der ignoreProcedureReturnValues-Eigenschaft fest.
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
     * Ruft den Wert der unsignedTypes-Eigenschaft ab.
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
     * Legt den Wert der unsignedTypes-Eigenschaft fest.
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
     * Ruft den Wert der inputCatalog-Eigenschaft ab.
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
     * Legt den Wert der inputCatalog-Eigenschaft fest.
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
     * Ruft den Wert der outputCatalog-Eigenschaft ab.
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
     * Legt den Wert der outputCatalog-Eigenschaft fest.
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
     * Ruft den Wert der outputCatalogToDefault-Eigenschaft ab.
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
     * Legt den Wert der outputCatalogToDefault-Eigenschaft fest.
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
     * Ruft den Wert der inputSchema-Eigenschaft ab.
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
     * Legt den Wert der inputSchema-Eigenschaft fest.
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
     * Ruft den Wert der outputSchema-Eigenschaft ab.
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
     * Legt den Wert der outputSchema-Eigenschaft fest.
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
     * Ruft den Wert der outputSchemaToDefault-Eigenschaft ab.
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
     * Legt den Wert der outputSchemaToDefault-Eigenschaft fest.
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
     * Ruft den Wert der schemaVersionProvider-Eigenschaft ab.
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
     * Legt den Wert der schemaVersionProvider-Eigenschaft fest.
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
     * Ruft den Wert der catalogVersionProvider-Eigenschaft ab.
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
     * Legt den Wert der catalogVersionProvider-Eigenschaft fest.
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
     * Ruft den Wert der tableValuedFunctions-Eigenschaft ab.
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
     * Legt den Wert der tableValuedFunctions-Eigenschaft fest.
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
