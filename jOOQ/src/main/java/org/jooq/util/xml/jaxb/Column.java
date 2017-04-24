//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:36:23 AM CEST
//


package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Column complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Column"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="column_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="ordinal_position" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="identity_generation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="is_nullable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="column_default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Column", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Column implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(name = "table_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableCatalog;
    @XmlElement(name = "table_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableSchema;
    @XmlElement(name = "table_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableName;
    @XmlElement(name = "column_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String columnName;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "ordinal_position")
    protected Integer ordinalPosition;
    @XmlElement(name = "identity_generation")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String identityGeneration;
    @XmlElement(name = "is_nullable")
    protected Boolean isNullable;
    @XmlElement(name = "column_default")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String columnDefault;

    /**
     * Ruft den Wert der tableCatalog-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTableCatalog() {
        return tableCatalog;
    }

    /**
     * Legt den Wert der tableCatalog-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTableCatalog(String value) {
        this.tableCatalog = value;
    }

    /**
     * Ruft den Wert der tableSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTableSchema() {
        return tableSchema;
    }

    /**
     * Legt den Wert der tableSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTableSchema(String value) {
        this.tableSchema = value;
    }

    /**
     * Ruft den Wert der tableName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Legt den Wert der tableName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTableName(String value) {
        this.tableName = value;
    }

    /**
     * Ruft den Wert der columnName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Legt den Wert der columnName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setColumnName(String value) {
        this.columnName = value;
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

    /**
     * Ruft den Wert der ordinalPosition-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Legt den Wert der ordinalPosition-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setOrdinalPosition(Integer value) {
        this.ordinalPosition = value;
    }

    /**
     * Ruft den Wert der identityGeneration-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdentityGeneration() {
        return identityGeneration;
    }

    /**
     * Legt den Wert der identityGeneration-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdentityGeneration(String value) {
        this.identityGeneration = value;
    }

    /**
     * Ruft den Wert der isNullable-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsNullable() {
        return isNullable;
    }

    /**
     * Legt den Wert der isNullable-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsNullable(Boolean value) {
        this.isNullable = value;
    }

    /**
     * Ruft den Wert der columnDefault-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getColumnDefault() {
        return columnDefault;
    }

    /**
     * Legt den Wert der columnDefault-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setColumnDefault(String value) {
        this.columnDefault = value;
    }

    public Column withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public Column withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public Column withTableName(String value) {
        setTableName(value);
        return this;
    }

    public Column withColumnName(String value) {
        setColumnName(value);
        return this;
    }

    public Column withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Column withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Column withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Column withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Column withOrdinalPosition(Integer value) {
        setOrdinalPosition(value);
        return this;
    }

    public Column withIdentityGeneration(String value) {
        setIdentityGeneration(value);
        return this;
    }

    public Column withIsNullable(Boolean value) {
        setIsNullable(value);
        return this;
    }

    public Column withColumnDefault(String value) {
        setColumnDefault(value);
        return this;
    }

}
