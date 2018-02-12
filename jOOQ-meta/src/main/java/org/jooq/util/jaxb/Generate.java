







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Options strictly related to generated code.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Generate", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Generate implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(defaultValue = "true")
    protected Boolean indexes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean relations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsToOne = true;
    @XmlElement(defaultValue = "true")
    protected Boolean deprecated = true;
    @XmlElement(defaultValue = "true")
    protected Boolean deprecationOnUnknownTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean instanceFields = true;
    @XmlElement(defaultValue = "true")
    protected Boolean generatedAnnotation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean routines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sequences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean udts = true;
    @XmlElement(defaultValue = "true")
    protected Boolean queues = true;
    @XmlElement(defaultValue = "true")
    protected Boolean links = true;
    @XmlElement(defaultValue = "true")
    protected Boolean keys = true;
    @XmlElement(defaultValue = "true")
    protected Boolean tables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean records = true;
    @XmlElement(defaultValue = "true")
    protected Boolean recordsImplementingRecordN = true;
    @XmlElement(defaultValue = "false")
    protected Boolean pojos = false;
    @XmlElement(defaultValue = "false")
    protected Boolean pojosEqualsAndHashCode = false;
    @XmlElement(defaultValue = "true")
    protected Boolean pojosToString = true;
    @XmlElement(defaultValue = "false")
    protected Boolean immutablePojos = false;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaces = false;
    @XmlElement(defaultValue = "false")
    protected Boolean immutableInterfaces = false;
    @XmlElement(defaultValue = "false")
    protected Boolean daos = false;
    @XmlElement(defaultValue = "false")
    protected Boolean jpaAnnotations = false;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String jpaVersion;
    @XmlElement(defaultValue = "false")
    protected Boolean validationAnnotations = false;
    @XmlElement(defaultValue = "false")
    protected Boolean springAnnotations = false;
    @XmlElement(defaultValue = "true")
    protected Boolean globalObjectReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalCatalogReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalSchemaReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalTableReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalSequenceReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalUDTReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalRoutineReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalQueueReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalLinkReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalKeyReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean javadoc = true;
    @XmlElement(defaultValue = "true")
    protected Boolean comments = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnCatalogs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnSchemas = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnTables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnColumns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnUDTs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnAttributes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnPackages = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnRoutines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnParameters = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnSequences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnLinks = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnQueues = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnKeys = true;
    @XmlElement(defaultValue = "false")
    protected Boolean fluentSetters = false;
    @XmlElement(defaultValue = "false")
    protected Boolean javaBeansGettersAndSetters = false;
    @XmlElement(defaultValue = "true")
    protected Boolean varargSetters = true;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fullyQualifiedTypes = "";
    @XmlElement(defaultValue = "false")
    protected Boolean emptyCatalogs = false;
    @XmlElement(defaultValue = "false")
    protected Boolean emptySchemas = false;
    @XmlElement(defaultValue = "false")
    protected Boolean javaTimeTypes = false;
    @XmlElement(defaultValue = "false")
    protected Boolean primaryKeyTypes = false;

    /**
     * Generate index information.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIndexes() {
        return indexes;
    }

    /**
     * Sets the value of the indexes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIndexes(Boolean value) {
        this.indexes = value;
    }

    /**
     * Primary key / foreign key relations should be generated and used.
     * This is a prerequisite for various advanced features
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRelations() {
        return relations;
    }

    /**
     * Sets the value of the relations property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRelations(Boolean value) {
        this.relations = value;
    }

    /**
     * Generate implicit join path constructors on generated tables for outgoing foreign key relationships (to-one relationships)
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isImplicitJoinPathsToOne() {
        return implicitJoinPathsToOne;
    }

    /**
     * Sets the value of the implicitJoinPathsToOne property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setImplicitJoinPathsToOne(Boolean value) {
        this.implicitJoinPathsToOne = value;
    }

    /**
     * Generate deprecated code for backwards compatibility
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Sets the value of the deprecated property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDeprecated(Boolean value) {
        this.deprecated = value;
    }

    /**
     * Generate deprecation annotations on references to unknown data types.
     * This helps identifying columns, attributes, and parameters, which may not be usable through
     * jOOQ API, without adding custom data type bindings to them.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDeprecationOnUnknownTypes() {
        return deprecationOnUnknownTypes;
    }

    /**
     * Sets the value of the deprecationOnUnknownTypes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDeprecationOnUnknownTypes(Boolean value) {
        this.deprecationOnUnknownTypes = value;
    }

    /**
     * @deprecated
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInstanceFields() {
        return instanceFields;
    }

    /**
     * Sets the value of the instanceFields property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInstanceFields(Boolean value) {
        this.instanceFields = value;
    }

    /**
     * Generate the {@link javax.annotation.Generated} annotation to indicate
     * jOOQ version used for source code
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGeneratedAnnotation() {
        return generatedAnnotation;
    }

    /**
     * Sets the value of the generatedAnnotation property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGeneratedAnnotation(Boolean value) {
        this.generatedAnnotation = value;
    }

    /**
     * Generate Routine classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRoutines() {
        return routines;
    }

    /**
     * Sets the value of the routines property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRoutines(Boolean value) {
        this.routines = value;
    }

    /**
     * Generate Sequence classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSequences() {
        return sequences;
    }

    /**
     * Sets the value of the sequences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSequences(Boolean value) {
        this.sequences = value;
    }

    /**
     * Generate UDT classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isUdts() {
        return udts;
    }

    /**
     * Sets the value of the udts property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setUdts(Boolean value) {
        this.udts = value;
    }

    /**
     * Generate Queue classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isQueues() {
        return queues;
    }

    /**
     * Sets the value of the queues property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setQueues(Boolean value) {
        this.queues = value;
    }

    /**
     * Generate database Link classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isLinks() {
        return links;
    }

    /**
     * Sets the value of the links property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setLinks(Boolean value) {
        this.links = value;
    }

    /**
     * Generate Key classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isKeys() {
        return keys;
    }

    /**
     * Sets the value of the keys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setKeys(Boolean value) {
        this.keys = value;
    }

    /**
     * Generate Table classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isTables() {
        return tables;
    }

    /**
     * Sets the value of the tables property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setTables(Boolean value) {
        this.tables = value;
    }

    /**
     * Generate TableRecord classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRecords() {
        return records;
    }

    /**
     * Sets the value of the records property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRecords(Boolean value) {
        this.records = value;
    }

    /**
     * Generate TableRecord classes that implement Record[N] super types
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRecordsImplementingRecordN() {
        return recordsImplementingRecordN;
    }

    /**
     * Sets the value of the recordsImplementingRecordN property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRecordsImplementingRecordN(Boolean value) {
        this.recordsImplementingRecordN = value;
    }

    /**
     * Generate POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isPojos() {
        return pojos;
    }

    /**
     * Sets the value of the pojos property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setPojos(Boolean value) {
        this.pojos = value;
    }

    /**
     * Generate basic equals() and hashCode() methods in POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isPojosEqualsAndHashCode() {
        return pojosEqualsAndHashCode;
    }

    /**
     * Sets the value of the pojosEqualsAndHashCode property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setPojosEqualsAndHashCode(Boolean value) {
        this.pojosEqualsAndHashCode = value;
    }

    /**
     * Generate basic toString() methods in POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isPojosToString() {
        return pojosToString;
    }

    /**
     * Sets the value of the pojosToString property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setPojosToString(Boolean value) {
        this.pojosToString = value;
    }

    /**
     * Generate immutable POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isImmutablePojos() {
        return immutablePojos;
    }

    /**
     * Sets the value of the immutablePojos property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setImmutablePojos(Boolean value) {
        this.immutablePojos = value;
    }

    /**
     * Generated interfaces to be implemented by records and/or POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInterfaces() {
        return interfaces;
    }

    /**
     * Sets the value of the interfaces property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInterfaces(Boolean value) {
        this.interfaces = value;
    }

    /**
     * Generate immutable interfaces.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isImmutableInterfaces() {
        return immutableInterfaces;
    }

    /**
     * Sets the value of the immutableInterfaces property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setImmutableInterfaces(Boolean value) {
        this.immutableInterfaces = value;
    }

    /**
     * Generate DAOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDaos() {
        return daos;
    }

    /**
     * Sets the value of the daos property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDaos(Boolean value) {
        this.daos = value;
    }

    /**
     * Annotate POJOs and Records with JPA annotations.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isJpaAnnotations() {
        return jpaAnnotations;
    }

    /**
     * Sets the value of the jpaAnnotations property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setJpaAnnotations(Boolean value) {
        this.jpaAnnotations = value;
    }

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getJpaVersion() {
        return jpaVersion;
    }

    /**
     * Sets the value of the jpaVersion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setJpaVersion(String value) {
        this.jpaVersion = value;
    }

    /**
     * Annotate POJOs and Records with JSR-303 validation annotations
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isValidationAnnotations() {
        return validationAnnotations;
    }

    /**
     * Sets the value of the validationAnnotations property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setValidationAnnotations(Boolean value) {
        this.validationAnnotations = value;
    }

    /**
     * Annotate DAOs with useful spring annotations such as @Repository or @Autowired.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSpringAnnotations() {
        return springAnnotations;
    }

    /**
     * Sets the value of the springAnnotations property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSpringAnnotations(Boolean value) {
        this.springAnnotations = value;
    }

    /**
     * Turn off generation of all global object references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalObjectReferences() {
        return globalObjectReferences;
    }

    /**
     * Sets the value of the globalObjectReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalObjectReferences(Boolean value) {
        this.globalObjectReferences = value;
    }

    /**
     * Turn off generation of global catalog references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalCatalogReferences() {
        return globalCatalogReferences;
    }

    /**
     * Sets the value of the globalCatalogReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalCatalogReferences(Boolean value) {
        this.globalCatalogReferences = value;
    }

    /**
     * Turn off generation of global schema references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalSchemaReferences() {
        return globalSchemaReferences;
    }

    /**
     * Sets the value of the globalSchemaReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalSchemaReferences(Boolean value) {
        this.globalSchemaReferences = value;
    }

    /**
     * Turn off generation of global table references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalTableReferences() {
        return globalTableReferences;
    }

    /**
     * Sets the value of the globalTableReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalTableReferences(Boolean value) {
        this.globalTableReferences = value;
    }

    /**
     * Turn off generation of global sequence references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalSequenceReferences() {
        return globalSequenceReferences;
    }

    /**
     * Sets the value of the globalSequenceReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalSequenceReferences(Boolean value) {
        this.globalSequenceReferences = value;
    }

    /**
     * Turn off generation of global UDT references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalUDTReferences() {
        return globalUDTReferences;
    }

    /**
     * Sets the value of the globalUDTReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalUDTReferences(Boolean value) {
        this.globalUDTReferences = value;
    }

    /**
     * Turn off generation of global routine references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalRoutineReferences() {
        return globalRoutineReferences;
    }

    /**
     * Sets the value of the globalRoutineReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalRoutineReferences(Boolean value) {
        this.globalRoutineReferences = value;
    }

    /**
     * Turn off generation of global queue references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalQueueReferences() {
        return globalQueueReferences;
    }

    /**
     * Sets the value of the globalQueueReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalQueueReferences(Boolean value) {
        this.globalQueueReferences = value;
    }

    /**
     * Turn off generation of global database link references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalLinkReferences() {
        return globalLinkReferences;
    }

    /**
     * Sets the value of the globalLinkReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalLinkReferences(Boolean value) {
        this.globalLinkReferences = value;
    }

    /**
     * Turn off generation of global key references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalKeyReferences() {
        return globalKeyReferences;
    }

    /**
     * Sets the value of the globalKeyReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalKeyReferences(Boolean value) {
        this.globalKeyReferences = value;
    }

    /**
     * Turn off generation of Javadoc on all objects.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isJavadoc() {
        return javadoc;
    }

    /**
     * Sets the value of the javadoc property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setJavadoc(Boolean value) {
        this.javadoc = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all objects.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setComments(Boolean value) {
        this.comments = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all catalogs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnCatalogs() {
        return commentsOnCatalogs;
    }

    /**
     * Sets the value of the commentsOnCatalogs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnCatalogs(Boolean value) {
        this.commentsOnCatalogs = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all schemas.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnSchemas() {
        return commentsOnSchemas;
    }

    /**
     * Sets the value of the commentsOnSchemas property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnSchemas(Boolean value) {
        this.commentsOnSchemas = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all tables.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnTables() {
        return commentsOnTables;
    }

    /**
     * Sets the value of the commentsOnTables property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnTables(Boolean value) {
        this.commentsOnTables = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all columns.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnColumns() {
        return commentsOnColumns;
    }

    /**
     * Sets the value of the commentsOnColumns property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnColumns(Boolean value) {
        this.commentsOnColumns = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all UDTs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnUDTs() {
        return commentsOnUDTs;
    }

    /**
     * Sets the value of the commentsOnUDTs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnUDTs(Boolean value) {
        this.commentsOnUDTs = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all attributes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnAttributes() {
        return commentsOnAttributes;
    }

    /**
     * Sets the value of the commentsOnAttributes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnAttributes(Boolean value) {
        this.commentsOnAttributes = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all packages.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnPackages() {
        return commentsOnPackages;
    }

    /**
     * Sets the value of the commentsOnPackages property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnPackages(Boolean value) {
        this.commentsOnPackages = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all routines.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnRoutines() {
        return commentsOnRoutines;
    }

    /**
     * Sets the value of the commentsOnRoutines property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnRoutines(Boolean value) {
        this.commentsOnRoutines = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all parameters.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnParameters() {
        return commentsOnParameters;
    }

    /**
     * Sets the value of the commentsOnParameters property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnParameters(Boolean value) {
        this.commentsOnParameters = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all sequences.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnSequences() {
        return commentsOnSequences;
    }

    /**
     * Sets the value of the commentsOnSequences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnSequences(Boolean value) {
        this.commentsOnSequences = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all links.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnLinks() {
        return commentsOnLinks;
    }

    /**
     * Sets the value of the commentsOnLinks property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnLinks(Boolean value) {
        this.commentsOnLinks = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all queues.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnQueues() {
        return commentsOnQueues;
    }

    /**
     * Sets the value of the commentsOnQueues property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnQueues(Boolean value) {
        this.commentsOnQueues = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all keys.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCommentsOnKeys() {
        return commentsOnKeys;
    }

    /**
     * Sets the value of the commentsOnKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCommentsOnKeys(Boolean value) {
        this.commentsOnKeys = value;
    }

    /**
     * Generate fluent setters in records, POJOs, interfaces.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFluentSetters() {
        return fluentSetters;
    }

    /**
     * Sets the value of the fluentSetters property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setFluentSetters(Boolean value) {
        this.fluentSetters = value;
    }

    /**
     * Modify DefaultGeneratorStrategy behaviour to generate getters and setters in JavaBeans style in records, POJOs, interfaces.
     * <p>
     * If this flag is set to false, then:
     * <p>
     * <ul>
     * <li>Column name   : X_INDEX</li>
     * <li>Attribute name: xIndex</li>
     * <li>Getter name   : getXIndex()</li>
     * <li>Setter name   : setXIndex()</li>
     * </ul>
     * <p>
     * If this flag is set to true, then:
     * <ul>
     * <li>Getter name   : getxIndex()</li>
     * <li>Setter name   : setxIndex()</li>
     * </ul>
     * <p>
     * Custom GeneratorStrategy implementations are unaffected
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isJavaBeansGettersAndSetters() {
        return javaBeansGettersAndSetters;
    }

    /**
     * Sets the value of the javaBeansGettersAndSetters property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setJavaBeansGettersAndSetters(Boolean value) {
        this.javaBeansGettersAndSetters = value;
    }

    /**
     * Generate varargs setters for array types for convenience.
     * <p>
     * This may lead to compilation warnings in current Java versions.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isVarargSetters() {
        return varargSetters;
    }

    /**
     * Sets the value of the varargSetters property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setVarargSetters(Boolean value) {
        this.varargSetters = value;
    }

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFullyQualifiedTypes() {
        return fullyQualifiedTypes;
    }

    /**
     * Sets the value of the fullyQualifiedTypes property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFullyQualifiedTypes(String value) {
        this.fullyQualifiedTypes = value;
    }

    /**
     * Whether empty catalogs (e.g. empty because of <excludes/> configurations) should still be generated.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isEmptyCatalogs() {
        return emptyCatalogs;
    }

    /**
     * Sets the value of the emptyCatalogs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEmptyCatalogs(Boolean value) {
        this.emptyCatalogs = value;
    }

    /**
     * Whether empty schemas (e.g. empty because of <excludes/> configurations) should still be generated.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isEmptySchemas() {
        return emptySchemas;
    }

    /**
     * Sets the value of the emptySchemas property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEmptySchemas(Boolean value) {
        this.emptySchemas = value;
    }

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isJavaTimeTypes() {
        return javaTimeTypes;
    }

    /**
     * Sets the value of the javaTimeTypes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setJavaTimeTypes(Boolean value) {
        this.javaTimeTypes = value;
    }

    /**
     * Whether wrapper types should be generated for primary key columns, and for their referencing foreign keys.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isPrimaryKeyTypes() {
        return primaryKeyTypes;
    }

    /**
     * Sets the value of the primaryKeyTypes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setPrimaryKeyTypes(Boolean value) {
        this.primaryKeyTypes = value;
    }

    public Generate withIndexes(Boolean value) {
        setIndexes(value);
        return this;
    }

    public Generate withRelations(Boolean value) {
        setRelations(value);
        return this;
    }

    public Generate withImplicitJoinPathsToOne(Boolean value) {
        setImplicitJoinPathsToOne(value);
        return this;
    }

    public Generate withDeprecated(Boolean value) {
        setDeprecated(value);
        return this;
    }

    public Generate withDeprecationOnUnknownTypes(Boolean value) {
        setDeprecationOnUnknownTypes(value);
        return this;
    }

    public Generate withInstanceFields(Boolean value) {
        setInstanceFields(value);
        return this;
    }

    public Generate withGeneratedAnnotation(Boolean value) {
        setGeneratedAnnotation(value);
        return this;
    }

    public Generate withRoutines(Boolean value) {
        setRoutines(value);
        return this;
    }

    public Generate withSequences(Boolean value) {
        setSequences(value);
        return this;
    }

    public Generate withUdts(Boolean value) {
        setUdts(value);
        return this;
    }

    public Generate withQueues(Boolean value) {
        setQueues(value);
        return this;
    }

    public Generate withLinks(Boolean value) {
        setLinks(value);
        return this;
    }

    public Generate withKeys(Boolean value) {
        setKeys(value);
        return this;
    }

    public Generate withTables(Boolean value) {
        setTables(value);
        return this;
    }

    public Generate withRecords(Boolean value) {
        setRecords(value);
        return this;
    }

    public Generate withRecordsImplementingRecordN(Boolean value) {
        setRecordsImplementingRecordN(value);
        return this;
    }

    public Generate withPojos(Boolean value) {
        setPojos(value);
        return this;
    }

    public Generate withPojosEqualsAndHashCode(Boolean value) {
        setPojosEqualsAndHashCode(value);
        return this;
    }

    public Generate withPojosToString(Boolean value) {
        setPojosToString(value);
        return this;
    }

    public Generate withImmutablePojos(Boolean value) {
        setImmutablePojos(value);
        return this;
    }

    public Generate withInterfaces(Boolean value) {
        setInterfaces(value);
        return this;
    }

    public Generate withImmutableInterfaces(Boolean value) {
        setImmutableInterfaces(value);
        return this;
    }

    public Generate withDaos(Boolean value) {
        setDaos(value);
        return this;
    }

    public Generate withJpaAnnotations(Boolean value) {
        setJpaAnnotations(value);
        return this;
    }

    public Generate withJpaVersion(String value) {
        setJpaVersion(value);
        return this;
    }

    public Generate withValidationAnnotations(Boolean value) {
        setValidationAnnotations(value);
        return this;
    }

    public Generate withSpringAnnotations(Boolean value) {
        setSpringAnnotations(value);
        return this;
    }

    public Generate withGlobalObjectReferences(Boolean value) {
        setGlobalObjectReferences(value);
        return this;
    }

    public Generate withGlobalCatalogReferences(Boolean value) {
        setGlobalCatalogReferences(value);
        return this;
    }

    public Generate withGlobalSchemaReferences(Boolean value) {
        setGlobalSchemaReferences(value);
        return this;
    }

    public Generate withGlobalTableReferences(Boolean value) {
        setGlobalTableReferences(value);
        return this;
    }

    public Generate withGlobalSequenceReferences(Boolean value) {
        setGlobalSequenceReferences(value);
        return this;
    }

    public Generate withGlobalUDTReferences(Boolean value) {
        setGlobalUDTReferences(value);
        return this;
    }

    public Generate withGlobalRoutineReferences(Boolean value) {
        setGlobalRoutineReferences(value);
        return this;
    }

    public Generate withGlobalQueueReferences(Boolean value) {
        setGlobalQueueReferences(value);
        return this;
    }

    public Generate withGlobalLinkReferences(Boolean value) {
        setGlobalLinkReferences(value);
        return this;
    }

    public Generate withGlobalKeyReferences(Boolean value) {
        setGlobalKeyReferences(value);
        return this;
    }

    public Generate withJavadoc(Boolean value) {
        setJavadoc(value);
        return this;
    }

    public Generate withComments(Boolean value) {
        setComments(value);
        return this;
    }

    public Generate withCommentsOnCatalogs(Boolean value) {
        setCommentsOnCatalogs(value);
        return this;
    }

    public Generate withCommentsOnSchemas(Boolean value) {
        setCommentsOnSchemas(value);
        return this;
    }

    public Generate withCommentsOnTables(Boolean value) {
        setCommentsOnTables(value);
        return this;
    }

    public Generate withCommentsOnColumns(Boolean value) {
        setCommentsOnColumns(value);
        return this;
    }

    public Generate withCommentsOnUDTs(Boolean value) {
        setCommentsOnUDTs(value);
        return this;
    }

    public Generate withCommentsOnAttributes(Boolean value) {
        setCommentsOnAttributes(value);
        return this;
    }

    public Generate withCommentsOnPackages(Boolean value) {
        setCommentsOnPackages(value);
        return this;
    }

    public Generate withCommentsOnRoutines(Boolean value) {
        setCommentsOnRoutines(value);
        return this;
    }

    public Generate withCommentsOnParameters(Boolean value) {
        setCommentsOnParameters(value);
        return this;
    }

    public Generate withCommentsOnSequences(Boolean value) {
        setCommentsOnSequences(value);
        return this;
    }

    public Generate withCommentsOnLinks(Boolean value) {
        setCommentsOnLinks(value);
        return this;
    }

    public Generate withCommentsOnQueues(Boolean value) {
        setCommentsOnQueues(value);
        return this;
    }

    public Generate withCommentsOnKeys(Boolean value) {
        setCommentsOnKeys(value);
        return this;
    }

    public Generate withFluentSetters(Boolean value) {
        setFluentSetters(value);
        return this;
    }

    public Generate withJavaBeansGettersAndSetters(Boolean value) {
        setJavaBeansGettersAndSetters(value);
        return this;
    }

    public Generate withVarargSetters(Boolean value) {
        setVarargSetters(value);
        return this;
    }

    public Generate withFullyQualifiedTypes(String value) {
        setFullyQualifiedTypes(value);
        return this;
    }

    public Generate withEmptyCatalogs(Boolean value) {
        setEmptyCatalogs(value);
        return this;
    }

    public Generate withEmptySchemas(Boolean value) {
        setEmptySchemas(value);
        return this;
    }

    public Generate withJavaTimeTypes(Boolean value) {
        setJavaTimeTypes(value);
        return this;
    }

    public Generate withPrimaryKeyTypes(Boolean value) {
        setPrimaryKeyTypes(value);
        return this;
    }

}
