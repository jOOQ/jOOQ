







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Routine complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Routine"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="specific_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_package" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routine_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routine_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routine_package" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routine_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="routine_type" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}RoutineType"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Routine", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Routine implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(name = "specific_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificCatalog;
    @XmlElement(name = "specific_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificSchema;
    @XmlElement(name = "specific_package")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificPackage;
    @XmlElement(name = "specific_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificName;
    @XmlElement(name = "routine_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineCatalog;
    @XmlElement(name = "routine_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineSchema;
    @XmlElement(name = "routine_package")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routinePackage;
    @XmlElement(name = "routine_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineName;
    @XmlElement(name = "routine_type", required = true)
    @XmlSchemaType(name = "string")
    protected RoutineType routineType;
    @XmlElement(name = "data_type")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;

    /**
     * Ruft den Wert der specificCatalog-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificCatalog() {
        return specificCatalog;
    }

    /**
     * Legt den Wert der specificCatalog-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificCatalog(String value) {
        this.specificCatalog = value;
    }

    /**
     * Ruft den Wert der specificSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificSchema() {
        return specificSchema;
    }

    /**
     * Legt den Wert der specificSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificSchema(String value) {
        this.specificSchema = value;
    }

    /**
     * Ruft den Wert der specificPackage-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificPackage() {
        return specificPackage;
    }

    /**
     * Legt den Wert der specificPackage-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificPackage(String value) {
        this.specificPackage = value;
    }

    /**
     * Ruft den Wert der specificName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Legt den Wert der specificName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificName(String value) {
        this.specificName = value;
    }

    /**
     * Ruft den Wert der routineCatalog-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoutineCatalog() {
        return routineCatalog;
    }

    /**
     * Legt den Wert der routineCatalog-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoutineCatalog(String value) {
        this.routineCatalog = value;
    }

    /**
     * Ruft den Wert der routineSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoutineSchema() {
        return routineSchema;
    }

    /**
     * Legt den Wert der routineSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoutineSchema(String value) {
        this.routineSchema = value;
    }

    /**
     * Ruft den Wert der routinePackage-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoutinePackage() {
        return routinePackage;
    }

    /**
     * Legt den Wert der routinePackage-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoutinePackage(String value) {
        this.routinePackage = value;
    }

    /**
     * Ruft den Wert der routineName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoutineName() {
        return routineName;
    }

    /**
     * Legt den Wert der routineName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoutineName(String value) {
        this.routineName = value;
    }

    /**
     * Ruft den Wert der routineType-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link RoutineType }
     *
     */
    public RoutineType getRoutineType() {
        return routineType;
    }

    /**
     * Legt den Wert der routineType-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link RoutineType }
     *
     */
    public void setRoutineType(RoutineType value) {
        this.routineType = value;
    }

    /**
     * Ruft den Wert der dataType-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Legt den Wert der dataType-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Ruft den Wert der characterMaximumLength-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    /**
     * Legt den Wert der characterMaximumLength-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCharacterMaximumLength(Integer value) {
        this.characterMaximumLength = value;
    }

    /**
     * Ruft den Wert der numericPrecision-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    /**
     * Legt den Wert der numericPrecision-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumericPrecision(Integer value) {
        this.numericPrecision = value;
    }

    /**
     * Ruft den Wert der numericScale-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumericScale() {
        return numericScale;
    }

    /**
     * Legt den Wert der numericScale-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumericScale(Integer value) {
        this.numericScale = value;
    }

    public Routine withSpecificCatalog(String value) {
        setSpecificCatalog(value);
        return this;
    }

    public Routine withSpecificSchema(String value) {
        setSpecificSchema(value);
        return this;
    }

    public Routine withSpecificPackage(String value) {
        setSpecificPackage(value);
        return this;
    }

    public Routine withSpecificName(String value) {
        setSpecificName(value);
        return this;
    }

    public Routine withRoutineCatalog(String value) {
        setRoutineCatalog(value);
        return this;
    }

    public Routine withRoutineSchema(String value) {
        setRoutineSchema(value);
        return this;
    }

    public Routine withRoutinePackage(String value) {
        setRoutinePackage(value);
        return this;
    }

    public Routine withRoutineName(String value) {
        setRoutineName(value);
        return this;
    }

    public Routine withRoutineType(RoutineType value) {
        setRoutineType(value);
        return this;
    }

    public Routine withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Routine withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Routine withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Routine withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

}
