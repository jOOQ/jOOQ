







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Generate complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Generate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="relations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="deprecated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="instanceFields" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="generatedAnnotation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="routines" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="sequences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="udts" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="queues" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="links" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="records" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="pojos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="pojosEqualsAndHashCode" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="pojosToString" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="immutablePojos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="interfaces" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="immutableInterfaces" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="daos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="jpaAnnotations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="validationAnnotations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="springAnnotations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalObjectReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalCatalogReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalSchemaReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalTableReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalSequenceReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalUDTReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalRoutineReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalQueueReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="globalLinkReferences" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="fluentSetters" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="javaBeansGettersAndSetters" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="varargSetters" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="fullyQualifiedTypes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="emptyCatalogs" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="emptySchemas" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="javaTimeTypes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
    @XmlElement(defaultValue = "true")
    protected Boolean relations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean deprecated = true;
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
    protected Boolean tables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean records = true;
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

    /**
     * Gets the value of the relations property.
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
     * Gets the value of the deprecated property.
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
     * Gets the value of the instanceFields property.
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
     * Gets the value of the generatedAnnotation property.
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
     * Gets the value of the routines property.
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
     * Gets the value of the sequences property.
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
     * Gets the value of the udts property.
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
     * Gets the value of the queues property.
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
     * Gets the value of the links property.
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
     * Gets the value of the tables property.
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
     * Gets the value of the records property.
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
     * Gets the value of the pojos property.
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
     * Gets the value of the pojosEqualsAndHashCode property.
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
     * Gets the value of the pojosToString property.
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
     * Gets the value of the immutablePojos property.
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
     * Gets the value of the interfaces property.
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
     * Gets the value of the immutableInterfaces property.
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
     * Gets the value of the daos property.
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
     * Gets the value of the jpaAnnotations property.
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
     * Gets the value of the validationAnnotations property.
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
     * Gets the value of the springAnnotations property.
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
     * Gets the value of the globalObjectReferences property.
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
     * Gets the value of the globalCatalogReferences property.
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
     * Gets the value of the globalSchemaReferences property.
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
     * Gets the value of the globalTableReferences property.
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
     * Gets the value of the globalSequenceReferences property.
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
     * Gets the value of the globalUDTReferences property.
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
     * Gets the value of the globalRoutineReferences property.
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
     * Gets the value of the globalQueueReferences property.
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
     * Gets the value of the globalLinkReferences property.
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
     * Gets the value of the fluentSetters property.
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
     * Gets the value of the javaBeansGettersAndSetters property.
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
     * Gets the value of the varargSetters property.
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
     * Gets the value of the fullyQualifiedTypes property.
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
     * Gets the value of the emptyCatalogs property.
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
     * Gets the value of the emptySchemas property.
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
     * Gets the value of the javaTimeTypes property.
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

    public Generate withRelations(Boolean value) {
        setRelations(value);
        return this;
    }

    public Generate withDeprecated(Boolean value) {
        setDeprecated(value);
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

    public Generate withTables(Boolean value) {
        setTables(value);
        return this;
    }

    public Generate withRecords(Boolean value) {
        setRecords(value);
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

}
