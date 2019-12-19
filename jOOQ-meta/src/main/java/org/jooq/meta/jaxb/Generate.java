
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
public class Generate implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(defaultValue = "true")
    protected Boolean indexes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean relations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sequenceFlags = true;
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
    @XmlElement(defaultValue = "DETECT_FROM_JDK")
    @XmlSchemaType(name = "string")
    protected GeneratedAnnotationType generatedAnnotationType = GeneratedAnnotationType.DETECT_FROM_JDK;
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
    protected Boolean embeddables = true;
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
    @XmlElement(defaultValue = "true")
    protected Boolean serializablePojos = true;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaces = false;
    @XmlElement(defaultValue = "false")
    protected Boolean immutableInterfaces = false;
    @XmlElement(defaultValue = "true")
    protected Boolean serializableInterfaces = true;
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
    protected Boolean globalIndexReferences = true;
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
    @XmlElement(defaultValue = "false")
    protected Boolean varargSetters = false;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fullyQualifiedTypes = "";
    @XmlElement(defaultValue = "false")
    protected Boolean emptyCatalogs = false;
    @XmlElement(defaultValue = "false")
    protected Boolean emptySchemas = false;
    @XmlElement(defaultValue = "true")
    protected Boolean javaTimeTypes = true;
    @XmlElement(defaultValue = "false")
    protected Boolean primaryKeyTypes = false;
    @XmlElement(defaultValue = "\\n")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String newline = "\\n";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String indentation;

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
     * Sequence flags should be generated and used.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSequenceFlags() {
        return sequenceFlags;
    }

    /**
     * Sets the value of the sequenceFlags property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSequenceFlags(Boolean value) {
        this.sequenceFlags = value;
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
    @Deprecated
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
    @Deprecated
    public void setInstanceFields(Boolean value) {
        this.instanceFields = value;
    }

    /**
     * Generate the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation to indicate
     * jOOQ version used for source code.
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
     * Generate the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation to indicate
     * jOOQ version used for source code.
     *
     */
    public GeneratedAnnotationType getGeneratedAnnotationType() {
        return generatedAnnotationType;
    }

    /**
     * Generate the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation to indicate
     * jOOQ version used for source code.
     *
     */
    public void setGeneratedAnnotationType(GeneratedAnnotationType value) {
        this.generatedAnnotationType = value;
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
     * Generate embeddable classes.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isEmbeddables() {
        return embeddables;
    }

    /**
     * Sets the value of the embeddables property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEmbeddables(Boolean value) {
        this.embeddables = value;
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
     * Generate serializable POJOs.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSerializablePojos() {
        return serializablePojos;
    }

    /**
     * Sets the value of the serializablePojos property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSerializablePojos(Boolean value) {
        this.serializablePojos = value;
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
     * Generate serializable interfaces.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSerializableInterfaces() {
        return serializableInterfaces;
    }

    /**
     * Sets the value of the serializableInterfaces property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSerializableInterfaces(Boolean value) {
        this.serializableInterfaces = value;
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
     */
    public String getJpaVersion() {
        return jpaVersion;
    }

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
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
     * Turn off generation of global index references.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isGlobalIndexReferences() {
        return globalIndexReferences;
    }

    /**
     * Sets the value of the globalIndexReferences property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setGlobalIndexReferences(Boolean value) {
        this.globalIndexReferences = value;
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
     */
    public String getFullyQualifiedTypes() {
        return fullyQualifiedTypes;
    }

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
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

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     *
     */
    public String getNewline() {
        return newline;
    }

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     *
     */
    public void setNewline(String value) {
        this.newline = value;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     *
     */
    public String getIndentation() {
        return indentation;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     *
     */
    public void setIndentation(String value) {
        this.indentation = value;
    }

    public Generate withIndexes(Boolean value) {
        setIndexes(value);
        return this;
    }

    public Generate withRelations(Boolean value) {
        setRelations(value);
        return this;
    }

    public Generate withSequenceFlags(Boolean value) {
        setSequenceFlags(value);
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

    /**
     * Generate the {@link javax.annotation.Generated} or {@link javax.annotation.processing.Generated} annotation to indicate
     * jOOQ version used for source code.
     *
     */
    public Generate withGeneratedAnnotationType(GeneratedAnnotationType value) {
        setGeneratedAnnotationType(value);
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

    public Generate withEmbeddables(Boolean value) {
        setEmbeddables(value);
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

    public Generate withSerializablePojos(Boolean value) {
        setSerializablePojos(value);
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

    public Generate withSerializableInterfaces(Boolean value) {
        setSerializableInterfaces(value);
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

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
     *
     */
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

    public Generate withGlobalIndexReferences(Boolean value) {
        setGlobalIndexReferences(value);
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

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
     *
     */
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

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     *
     */
    public Generate withNewline(String value) {
        setNewline(value);
        return this;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     *
     */
    public Generate withIndentation(String value) {
        setIndentation(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("indexes", indexes);
        builder.append("relations", relations);
        builder.append("sequenceFlags", sequenceFlags);
        builder.append("implicitJoinPathsToOne", implicitJoinPathsToOne);
        builder.append("deprecated", deprecated);
        builder.append("deprecationOnUnknownTypes", deprecationOnUnknownTypes);
        builder.append("instanceFields", instanceFields);
        builder.append("generatedAnnotation", generatedAnnotation);
        builder.append("generatedAnnotationType", generatedAnnotationType);
        builder.append("routines", routines);
        builder.append("sequences", sequences);
        builder.append("udts", udts);
        builder.append("queues", queues);
        builder.append("links", links);
        builder.append("keys", keys);
        builder.append("tables", tables);
        builder.append("embeddables", embeddables);
        builder.append("records", records);
        builder.append("recordsImplementingRecordN", recordsImplementingRecordN);
        builder.append("pojos", pojos);
        builder.append("pojosEqualsAndHashCode", pojosEqualsAndHashCode);
        builder.append("pojosToString", pojosToString);
        builder.append("immutablePojos", immutablePojos);
        builder.append("serializablePojos", serializablePojos);
        builder.append("interfaces", interfaces);
        builder.append("immutableInterfaces", immutableInterfaces);
        builder.append("serializableInterfaces", serializableInterfaces);
        builder.append("daos", daos);
        builder.append("jpaAnnotations", jpaAnnotations);
        builder.append("jpaVersion", jpaVersion);
        builder.append("validationAnnotations", validationAnnotations);
        builder.append("springAnnotations", springAnnotations);
        builder.append("globalObjectReferences", globalObjectReferences);
        builder.append("globalCatalogReferences", globalCatalogReferences);
        builder.append("globalSchemaReferences", globalSchemaReferences);
        builder.append("globalTableReferences", globalTableReferences);
        builder.append("globalSequenceReferences", globalSequenceReferences);
        builder.append("globalUDTReferences", globalUDTReferences);
        builder.append("globalRoutineReferences", globalRoutineReferences);
        builder.append("globalQueueReferences", globalQueueReferences);
        builder.append("globalLinkReferences", globalLinkReferences);
        builder.append("globalKeyReferences", globalKeyReferences);
        builder.append("globalIndexReferences", globalIndexReferences);
        builder.append("javadoc", javadoc);
        builder.append("comments", comments);
        builder.append("commentsOnCatalogs", commentsOnCatalogs);
        builder.append("commentsOnSchemas", commentsOnSchemas);
        builder.append("commentsOnTables", commentsOnTables);
        builder.append("commentsOnColumns", commentsOnColumns);
        builder.append("commentsOnUDTs", commentsOnUDTs);
        builder.append("commentsOnAttributes", commentsOnAttributes);
        builder.append("commentsOnPackages", commentsOnPackages);
        builder.append("commentsOnRoutines", commentsOnRoutines);
        builder.append("commentsOnParameters", commentsOnParameters);
        builder.append("commentsOnSequences", commentsOnSequences);
        builder.append("commentsOnLinks", commentsOnLinks);
        builder.append("commentsOnQueues", commentsOnQueues);
        builder.append("commentsOnKeys", commentsOnKeys);
        builder.append("fluentSetters", fluentSetters);
        builder.append("javaBeansGettersAndSetters", javaBeansGettersAndSetters);
        builder.append("varargSetters", varargSetters);
        builder.append("fullyQualifiedTypes", fullyQualifiedTypes);
        builder.append("emptyCatalogs", emptyCatalogs);
        builder.append("emptySchemas", emptySchemas);
        builder.append("javaTimeTypes", javaTimeTypes);
        builder.append("primaryKeyTypes", primaryKeyTypes);
        builder.append("newline", newline);
        builder.append("indentation", indentation);
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
        Generate other = ((Generate) that);
        if (indexes == null) {
            if (other.indexes!= null) {
                return false;
            }
        } else {
            if (!indexes.equals(other.indexes)) {
                return false;
            }
        }
        if (relations == null) {
            if (other.relations!= null) {
                return false;
            }
        } else {
            if (!relations.equals(other.relations)) {
                return false;
            }
        }
        if (sequenceFlags == null) {
            if (other.sequenceFlags!= null) {
                return false;
            }
        } else {
            if (!sequenceFlags.equals(other.sequenceFlags)) {
                return false;
            }
        }
        if (implicitJoinPathsToOne == null) {
            if (other.implicitJoinPathsToOne!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsToOne.equals(other.implicitJoinPathsToOne)) {
                return false;
            }
        }
        if (deprecated == null) {
            if (other.deprecated!= null) {
                return false;
            }
        } else {
            if (!deprecated.equals(other.deprecated)) {
                return false;
            }
        }
        if (deprecationOnUnknownTypes == null) {
            if (other.deprecationOnUnknownTypes!= null) {
                return false;
            }
        } else {
            if (!deprecationOnUnknownTypes.equals(other.deprecationOnUnknownTypes)) {
                return false;
            }
        }
        if (instanceFields == null) {
            if (other.instanceFields!= null) {
                return false;
            }
        } else {
            if (!instanceFields.equals(other.instanceFields)) {
                return false;
            }
        }
        if (generatedAnnotation == null) {
            if (other.generatedAnnotation!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotation.equals(other.generatedAnnotation)) {
                return false;
            }
        }
        if (generatedAnnotationType == null) {
            if (other.generatedAnnotationType!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotationType.equals(other.generatedAnnotationType)) {
                return false;
            }
        }
        if (routines == null) {
            if (other.routines!= null) {
                return false;
            }
        } else {
            if (!routines.equals(other.routines)) {
                return false;
            }
        }
        if (sequences == null) {
            if (other.sequences!= null) {
                return false;
            }
        } else {
            if (!sequences.equals(other.sequences)) {
                return false;
            }
        }
        if (udts == null) {
            if (other.udts!= null) {
                return false;
            }
        } else {
            if (!udts.equals(other.udts)) {
                return false;
            }
        }
        if (queues == null) {
            if (other.queues!= null) {
                return false;
            }
        } else {
            if (!queues.equals(other.queues)) {
                return false;
            }
        }
        if (links == null) {
            if (other.links!= null) {
                return false;
            }
        } else {
            if (!links.equals(other.links)) {
                return false;
            }
        }
        if (keys == null) {
            if (other.keys!= null) {
                return false;
            }
        } else {
            if (!keys.equals(other.keys)) {
                return false;
            }
        }
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
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
        if (records == null) {
            if (other.records!= null) {
                return false;
            }
        } else {
            if (!records.equals(other.records)) {
                return false;
            }
        }
        if (recordsImplementingRecordN == null) {
            if (other.recordsImplementingRecordN!= null) {
                return false;
            }
        } else {
            if (!recordsImplementingRecordN.equals(other.recordsImplementingRecordN)) {
                return false;
            }
        }
        if (pojos == null) {
            if (other.pojos!= null) {
                return false;
            }
        } else {
            if (!pojos.equals(other.pojos)) {
                return false;
            }
        }
        if (pojosEqualsAndHashCode == null) {
            if (other.pojosEqualsAndHashCode!= null) {
                return false;
            }
        } else {
            if (!pojosEqualsAndHashCode.equals(other.pojosEqualsAndHashCode)) {
                return false;
            }
        }
        if (pojosToString == null) {
            if (other.pojosToString!= null) {
                return false;
            }
        } else {
            if (!pojosToString.equals(other.pojosToString)) {
                return false;
            }
        }
        if (immutablePojos == null) {
            if (other.immutablePojos!= null) {
                return false;
            }
        } else {
            if (!immutablePojos.equals(other.immutablePojos)) {
                return false;
            }
        }
        if (serializablePojos == null) {
            if (other.serializablePojos!= null) {
                return false;
            }
        } else {
            if (!serializablePojos.equals(other.serializablePojos)) {
                return false;
            }
        }
        if (interfaces == null) {
            if (other.interfaces!= null) {
                return false;
            }
        } else {
            if (!interfaces.equals(other.interfaces)) {
                return false;
            }
        }
        if (immutableInterfaces == null) {
            if (other.immutableInterfaces!= null) {
                return false;
            }
        } else {
            if (!immutableInterfaces.equals(other.immutableInterfaces)) {
                return false;
            }
        }
        if (serializableInterfaces == null) {
            if (other.serializableInterfaces!= null) {
                return false;
            }
        } else {
            if (!serializableInterfaces.equals(other.serializableInterfaces)) {
                return false;
            }
        }
        if (daos == null) {
            if (other.daos!= null) {
                return false;
            }
        } else {
            if (!daos.equals(other.daos)) {
                return false;
            }
        }
        if (jpaAnnotations == null) {
            if (other.jpaAnnotations!= null) {
                return false;
            }
        } else {
            if (!jpaAnnotations.equals(other.jpaAnnotations)) {
                return false;
            }
        }
        if (jpaVersion == null) {
            if (other.jpaVersion!= null) {
                return false;
            }
        } else {
            if (!jpaVersion.equals(other.jpaVersion)) {
                return false;
            }
        }
        if (validationAnnotations == null) {
            if (other.validationAnnotations!= null) {
                return false;
            }
        } else {
            if (!validationAnnotations.equals(other.validationAnnotations)) {
                return false;
            }
        }
        if (springAnnotations == null) {
            if (other.springAnnotations!= null) {
                return false;
            }
        } else {
            if (!springAnnotations.equals(other.springAnnotations)) {
                return false;
            }
        }
        if (globalObjectReferences == null) {
            if (other.globalObjectReferences!= null) {
                return false;
            }
        } else {
            if (!globalObjectReferences.equals(other.globalObjectReferences)) {
                return false;
            }
        }
        if (globalCatalogReferences == null) {
            if (other.globalCatalogReferences!= null) {
                return false;
            }
        } else {
            if (!globalCatalogReferences.equals(other.globalCatalogReferences)) {
                return false;
            }
        }
        if (globalSchemaReferences == null) {
            if (other.globalSchemaReferences!= null) {
                return false;
            }
        } else {
            if (!globalSchemaReferences.equals(other.globalSchemaReferences)) {
                return false;
            }
        }
        if (globalTableReferences == null) {
            if (other.globalTableReferences!= null) {
                return false;
            }
        } else {
            if (!globalTableReferences.equals(other.globalTableReferences)) {
                return false;
            }
        }
        if (globalSequenceReferences == null) {
            if (other.globalSequenceReferences!= null) {
                return false;
            }
        } else {
            if (!globalSequenceReferences.equals(other.globalSequenceReferences)) {
                return false;
            }
        }
        if (globalUDTReferences == null) {
            if (other.globalUDTReferences!= null) {
                return false;
            }
        } else {
            if (!globalUDTReferences.equals(other.globalUDTReferences)) {
                return false;
            }
        }
        if (globalRoutineReferences == null) {
            if (other.globalRoutineReferences!= null) {
                return false;
            }
        } else {
            if (!globalRoutineReferences.equals(other.globalRoutineReferences)) {
                return false;
            }
        }
        if (globalQueueReferences == null) {
            if (other.globalQueueReferences!= null) {
                return false;
            }
        } else {
            if (!globalQueueReferences.equals(other.globalQueueReferences)) {
                return false;
            }
        }
        if (globalLinkReferences == null) {
            if (other.globalLinkReferences!= null) {
                return false;
            }
        } else {
            if (!globalLinkReferences.equals(other.globalLinkReferences)) {
                return false;
            }
        }
        if (globalKeyReferences == null) {
            if (other.globalKeyReferences!= null) {
                return false;
            }
        } else {
            if (!globalKeyReferences.equals(other.globalKeyReferences)) {
                return false;
            }
        }
        if (globalIndexReferences == null) {
            if (other.globalIndexReferences!= null) {
                return false;
            }
        } else {
            if (!globalIndexReferences.equals(other.globalIndexReferences)) {
                return false;
            }
        }
        if (javadoc == null) {
            if (other.javadoc!= null) {
                return false;
            }
        } else {
            if (!javadoc.equals(other.javadoc)) {
                return false;
            }
        }
        if (comments == null) {
            if (other.comments!= null) {
                return false;
            }
        } else {
            if (!comments.equals(other.comments)) {
                return false;
            }
        }
        if (commentsOnCatalogs == null) {
            if (other.commentsOnCatalogs!= null) {
                return false;
            }
        } else {
            if (!commentsOnCatalogs.equals(other.commentsOnCatalogs)) {
                return false;
            }
        }
        if (commentsOnSchemas == null) {
            if (other.commentsOnSchemas!= null) {
                return false;
            }
        } else {
            if (!commentsOnSchemas.equals(other.commentsOnSchemas)) {
                return false;
            }
        }
        if (commentsOnTables == null) {
            if (other.commentsOnTables!= null) {
                return false;
            }
        } else {
            if (!commentsOnTables.equals(other.commentsOnTables)) {
                return false;
            }
        }
        if (commentsOnColumns == null) {
            if (other.commentsOnColumns!= null) {
                return false;
            }
        } else {
            if (!commentsOnColumns.equals(other.commentsOnColumns)) {
                return false;
            }
        }
        if (commentsOnUDTs == null) {
            if (other.commentsOnUDTs!= null) {
                return false;
            }
        } else {
            if (!commentsOnUDTs.equals(other.commentsOnUDTs)) {
                return false;
            }
        }
        if (commentsOnAttributes == null) {
            if (other.commentsOnAttributes!= null) {
                return false;
            }
        } else {
            if (!commentsOnAttributes.equals(other.commentsOnAttributes)) {
                return false;
            }
        }
        if (commentsOnPackages == null) {
            if (other.commentsOnPackages!= null) {
                return false;
            }
        } else {
            if (!commentsOnPackages.equals(other.commentsOnPackages)) {
                return false;
            }
        }
        if (commentsOnRoutines == null) {
            if (other.commentsOnRoutines!= null) {
                return false;
            }
        } else {
            if (!commentsOnRoutines.equals(other.commentsOnRoutines)) {
                return false;
            }
        }
        if (commentsOnParameters == null) {
            if (other.commentsOnParameters!= null) {
                return false;
            }
        } else {
            if (!commentsOnParameters.equals(other.commentsOnParameters)) {
                return false;
            }
        }
        if (commentsOnSequences == null) {
            if (other.commentsOnSequences!= null) {
                return false;
            }
        } else {
            if (!commentsOnSequences.equals(other.commentsOnSequences)) {
                return false;
            }
        }
        if (commentsOnLinks == null) {
            if (other.commentsOnLinks!= null) {
                return false;
            }
        } else {
            if (!commentsOnLinks.equals(other.commentsOnLinks)) {
                return false;
            }
        }
        if (commentsOnQueues == null) {
            if (other.commentsOnQueues!= null) {
                return false;
            }
        } else {
            if (!commentsOnQueues.equals(other.commentsOnQueues)) {
                return false;
            }
        }
        if (commentsOnKeys == null) {
            if (other.commentsOnKeys!= null) {
                return false;
            }
        } else {
            if (!commentsOnKeys.equals(other.commentsOnKeys)) {
                return false;
            }
        }
        if (fluentSetters == null) {
            if (other.fluentSetters!= null) {
                return false;
            }
        } else {
            if (!fluentSetters.equals(other.fluentSetters)) {
                return false;
            }
        }
        if (javaBeansGettersAndSetters == null) {
            if (other.javaBeansGettersAndSetters!= null) {
                return false;
            }
        } else {
            if (!javaBeansGettersAndSetters.equals(other.javaBeansGettersAndSetters)) {
                return false;
            }
        }
        if (varargSetters == null) {
            if (other.varargSetters!= null) {
                return false;
            }
        } else {
            if (!varargSetters.equals(other.varargSetters)) {
                return false;
            }
        }
        if (fullyQualifiedTypes == null) {
            if (other.fullyQualifiedTypes!= null) {
                return false;
            }
        } else {
            if (!fullyQualifiedTypes.equals(other.fullyQualifiedTypes)) {
                return false;
            }
        }
        if (emptyCatalogs == null) {
            if (other.emptyCatalogs!= null) {
                return false;
            }
        } else {
            if (!emptyCatalogs.equals(other.emptyCatalogs)) {
                return false;
            }
        }
        if (emptySchemas == null) {
            if (other.emptySchemas!= null) {
                return false;
            }
        } else {
            if (!emptySchemas.equals(other.emptySchemas)) {
                return false;
            }
        }
        if (javaTimeTypes == null) {
            if (other.javaTimeTypes!= null) {
                return false;
            }
        } else {
            if (!javaTimeTypes.equals(other.javaTimeTypes)) {
                return false;
            }
        }
        if (primaryKeyTypes == null) {
            if (other.primaryKeyTypes!= null) {
                return false;
            }
        } else {
            if (!primaryKeyTypes.equals(other.primaryKeyTypes)) {
                return false;
            }
        }
        if (newline == null) {
            if (other.newline!= null) {
                return false;
            }
        } else {
            if (!newline.equals(other.newline)) {
                return false;
            }
        }
        if (indentation == null) {
            if (other.indentation!= null) {
                return false;
            }
        } else {
            if (!indentation.equals(other.indentation)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((indexes == null)? 0 :indexes.hashCode()));
        result = ((prime*result)+((relations == null)? 0 :relations.hashCode()));
        result = ((prime*result)+((sequenceFlags == null)? 0 :sequenceFlags.hashCode()));
        result = ((prime*result)+((implicitJoinPathsToOne == null)? 0 :implicitJoinPathsToOne.hashCode()));
        result = ((prime*result)+((deprecated == null)? 0 :deprecated.hashCode()));
        result = ((prime*result)+((deprecationOnUnknownTypes == null)? 0 :deprecationOnUnknownTypes.hashCode()));
        result = ((prime*result)+((instanceFields == null)? 0 :instanceFields.hashCode()));
        result = ((prime*result)+((generatedAnnotation == null)? 0 :generatedAnnotation.hashCode()));
        result = ((prime*result)+((generatedAnnotationType == null)? 0 :generatedAnnotationType.hashCode()));
        result = ((prime*result)+((routines == null)? 0 :routines.hashCode()));
        result = ((prime*result)+((sequences == null)? 0 :sequences.hashCode()));
        result = ((prime*result)+((udts == null)? 0 :udts.hashCode()));
        result = ((prime*result)+((queues == null)? 0 :queues.hashCode()));
        result = ((prime*result)+((links == null)? 0 :links.hashCode()));
        result = ((prime*result)+((keys == null)? 0 :keys.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((embeddables == null)? 0 :embeddables.hashCode()));
        result = ((prime*result)+((records == null)? 0 :records.hashCode()));
        result = ((prime*result)+((recordsImplementingRecordN == null)? 0 :recordsImplementingRecordN.hashCode()));
        result = ((prime*result)+((pojos == null)? 0 :pojos.hashCode()));
        result = ((prime*result)+((pojosEqualsAndHashCode == null)? 0 :pojosEqualsAndHashCode.hashCode()));
        result = ((prime*result)+((pojosToString == null)? 0 :pojosToString.hashCode()));
        result = ((prime*result)+((immutablePojos == null)? 0 :immutablePojos.hashCode()));
        result = ((prime*result)+((serializablePojos == null)? 0 :serializablePojos.hashCode()));
        result = ((prime*result)+((interfaces == null)? 0 :interfaces.hashCode()));
        result = ((prime*result)+((immutableInterfaces == null)? 0 :immutableInterfaces.hashCode()));
        result = ((prime*result)+((serializableInterfaces == null)? 0 :serializableInterfaces.hashCode()));
        result = ((prime*result)+((daos == null)? 0 :daos.hashCode()));
        result = ((prime*result)+((jpaAnnotations == null)? 0 :jpaAnnotations.hashCode()));
        result = ((prime*result)+((jpaVersion == null)? 0 :jpaVersion.hashCode()));
        result = ((prime*result)+((validationAnnotations == null)? 0 :validationAnnotations.hashCode()));
        result = ((prime*result)+((springAnnotations == null)? 0 :springAnnotations.hashCode()));
        result = ((prime*result)+((globalObjectReferences == null)? 0 :globalObjectReferences.hashCode()));
        result = ((prime*result)+((globalCatalogReferences == null)? 0 :globalCatalogReferences.hashCode()));
        result = ((prime*result)+((globalSchemaReferences == null)? 0 :globalSchemaReferences.hashCode()));
        result = ((prime*result)+((globalTableReferences == null)? 0 :globalTableReferences.hashCode()));
        result = ((prime*result)+((globalSequenceReferences == null)? 0 :globalSequenceReferences.hashCode()));
        result = ((prime*result)+((globalUDTReferences == null)? 0 :globalUDTReferences.hashCode()));
        result = ((prime*result)+((globalRoutineReferences == null)? 0 :globalRoutineReferences.hashCode()));
        result = ((prime*result)+((globalQueueReferences == null)? 0 :globalQueueReferences.hashCode()));
        result = ((prime*result)+((globalLinkReferences == null)? 0 :globalLinkReferences.hashCode()));
        result = ((prime*result)+((globalKeyReferences == null)? 0 :globalKeyReferences.hashCode()));
        result = ((prime*result)+((globalIndexReferences == null)? 0 :globalIndexReferences.hashCode()));
        result = ((prime*result)+((javadoc == null)? 0 :javadoc.hashCode()));
        result = ((prime*result)+((comments == null)? 0 :comments.hashCode()));
        result = ((prime*result)+((commentsOnCatalogs == null)? 0 :commentsOnCatalogs.hashCode()));
        result = ((prime*result)+((commentsOnSchemas == null)? 0 :commentsOnSchemas.hashCode()));
        result = ((prime*result)+((commentsOnTables == null)? 0 :commentsOnTables.hashCode()));
        result = ((prime*result)+((commentsOnColumns == null)? 0 :commentsOnColumns.hashCode()));
        result = ((prime*result)+((commentsOnUDTs == null)? 0 :commentsOnUDTs.hashCode()));
        result = ((prime*result)+((commentsOnAttributes == null)? 0 :commentsOnAttributes.hashCode()));
        result = ((prime*result)+((commentsOnPackages == null)? 0 :commentsOnPackages.hashCode()));
        result = ((prime*result)+((commentsOnRoutines == null)? 0 :commentsOnRoutines.hashCode()));
        result = ((prime*result)+((commentsOnParameters == null)? 0 :commentsOnParameters.hashCode()));
        result = ((prime*result)+((commentsOnSequences == null)? 0 :commentsOnSequences.hashCode()));
        result = ((prime*result)+((commentsOnLinks == null)? 0 :commentsOnLinks.hashCode()));
        result = ((prime*result)+((commentsOnQueues == null)? 0 :commentsOnQueues.hashCode()));
        result = ((prime*result)+((commentsOnKeys == null)? 0 :commentsOnKeys.hashCode()));
        result = ((prime*result)+((fluentSetters == null)? 0 :fluentSetters.hashCode()));
        result = ((prime*result)+((javaBeansGettersAndSetters == null)? 0 :javaBeansGettersAndSetters.hashCode()));
        result = ((prime*result)+((varargSetters == null)? 0 :varargSetters.hashCode()));
        result = ((prime*result)+((fullyQualifiedTypes == null)? 0 :fullyQualifiedTypes.hashCode()));
        result = ((prime*result)+((emptyCatalogs == null)? 0 :emptyCatalogs.hashCode()));
        result = ((prime*result)+((emptySchemas == null)? 0 :emptySchemas.hashCode()));
        result = ((prime*result)+((javaTimeTypes == null)? 0 :javaTimeTypes.hashCode()));
        result = ((prime*result)+((primaryKeyTypes == null)? 0 :primaryKeyTypes.hashCode()));
        result = ((prime*result)+((newline == null)? 0 :newline.hashCode()));
        result = ((prime*result)+((indentation == null)? 0 :indentation.hashCode()));
        return result;
    }

}
