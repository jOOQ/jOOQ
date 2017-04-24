







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Generate complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der relations-Eigenschaft ab.
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
     * Legt den Wert der relations-Eigenschaft fest.
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
     * Ruft den Wert der deprecated-Eigenschaft ab.
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
     * Legt den Wert der deprecated-Eigenschaft fest.
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
     * Ruft den Wert der instanceFields-Eigenschaft ab.
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
     * Legt den Wert der instanceFields-Eigenschaft fest.
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
     * Ruft den Wert der generatedAnnotation-Eigenschaft ab.
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
     * Legt den Wert der generatedAnnotation-Eigenschaft fest.
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
     * Ruft den Wert der routines-Eigenschaft ab.
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
     * Legt den Wert der routines-Eigenschaft fest.
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
     * Ruft den Wert der sequences-Eigenschaft ab.
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
     * Legt den Wert der sequences-Eigenschaft fest.
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
     * Ruft den Wert der udts-Eigenschaft ab.
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
     * Legt den Wert der udts-Eigenschaft fest.
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
     * Ruft den Wert der queues-Eigenschaft ab.
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
     * Legt den Wert der queues-Eigenschaft fest.
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
     * Ruft den Wert der links-Eigenschaft ab.
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
     * Legt den Wert der links-Eigenschaft fest.
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
     * Ruft den Wert der tables-Eigenschaft ab.
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
     * Legt den Wert der tables-Eigenschaft fest.
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
     * Ruft den Wert der records-Eigenschaft ab.
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
     * Legt den Wert der records-Eigenschaft fest.
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
     * Ruft den Wert der pojos-Eigenschaft ab.
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
     * Legt den Wert der pojos-Eigenschaft fest.
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
     * Ruft den Wert der pojosEqualsAndHashCode-Eigenschaft ab.
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
     * Legt den Wert der pojosEqualsAndHashCode-Eigenschaft fest.
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
     * Ruft den Wert der pojosToString-Eigenschaft ab.
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
     * Legt den Wert der pojosToString-Eigenschaft fest.
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
     * Ruft den Wert der immutablePojos-Eigenschaft ab.
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
     * Legt den Wert der immutablePojos-Eigenschaft fest.
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
     * Ruft den Wert der interfaces-Eigenschaft ab.
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
     * Legt den Wert der interfaces-Eigenschaft fest.
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
     * Ruft den Wert der immutableInterfaces-Eigenschaft ab.
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
     * Legt den Wert der immutableInterfaces-Eigenschaft fest.
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
     * Ruft den Wert der daos-Eigenschaft ab.
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
     * Legt den Wert der daos-Eigenschaft fest.
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
     * Ruft den Wert der jpaAnnotations-Eigenschaft ab.
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
     * Legt den Wert der jpaAnnotations-Eigenschaft fest.
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
     * Ruft den Wert der validationAnnotations-Eigenschaft ab.
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
     * Legt den Wert der validationAnnotations-Eigenschaft fest.
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
     * Ruft den Wert der springAnnotations-Eigenschaft ab.
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
     * Legt den Wert der springAnnotations-Eigenschaft fest.
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
     * Ruft den Wert der globalObjectReferences-Eigenschaft ab.
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
     * Legt den Wert der globalObjectReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalCatalogReferences-Eigenschaft ab.
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
     * Legt den Wert der globalCatalogReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalSchemaReferences-Eigenschaft ab.
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
     * Legt den Wert der globalSchemaReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalTableReferences-Eigenschaft ab.
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
     * Legt den Wert der globalTableReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalSequenceReferences-Eigenschaft ab.
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
     * Legt den Wert der globalSequenceReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalUDTReferences-Eigenschaft ab.
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
     * Legt den Wert der globalUDTReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalRoutineReferences-Eigenschaft ab.
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
     * Legt den Wert der globalRoutineReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalQueueReferences-Eigenschaft ab.
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
     * Legt den Wert der globalQueueReferences-Eigenschaft fest.
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
     * Ruft den Wert der globalLinkReferences-Eigenschaft ab.
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
     * Legt den Wert der globalLinkReferences-Eigenschaft fest.
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
     * Ruft den Wert der fluentSetters-Eigenschaft ab.
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
     * Legt den Wert der fluentSetters-Eigenschaft fest.
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
     * Ruft den Wert der javaBeansGettersAndSetters-Eigenschaft ab.
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
     * Legt den Wert der javaBeansGettersAndSetters-Eigenschaft fest.
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
     * Ruft den Wert der varargSetters-Eigenschaft ab.
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
     * Legt den Wert der varargSetters-Eigenschaft fest.
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
     * Ruft den Wert der fullyQualifiedTypes-Eigenschaft ab.
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
     * Legt den Wert der fullyQualifiedTypes-Eigenschaft fest.
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
     * Ruft den Wert der emptyCatalogs-Eigenschaft ab.
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
     * Legt den Wert der emptyCatalogs-Eigenschaft fest.
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
     * Ruft den Wert der emptySchemas-Eigenschaft ab.
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
     * Legt den Wert der emptySchemas-Eigenschaft fest.
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
     * Ruft den Wert der javaTimeTypes-Eigenschaft ab.
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
     * Legt den Wert der javaTimeTypes-Eigenschaft fest.
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
