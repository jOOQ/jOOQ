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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für TableConstraint complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="TableConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="constraint_type" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}TableConstraintType"/&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TableConstraint", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class TableConstraint implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(name = "constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintCatalog;
    @XmlElement(name = "constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintSchema;
    @XmlElement(name = "constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintName;
    @XmlElement(name = "constraint_type", required = true)
    @XmlSchemaType(name = "string")
    protected TableConstraintType constraintType;
    @XmlElement(name = "table_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableCatalog;
    @XmlElement(name = "table_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableSchema;
    @XmlElement(name = "table_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableName;

    /**
     * Ruft den Wert der constraintCatalog-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConstraintCatalog() {
        return constraintCatalog;
    }

    /**
     * Legt den Wert der constraintCatalog-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConstraintCatalog(String value) {
        this.constraintCatalog = value;
    }

    /**
     * Ruft den Wert der constraintSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConstraintSchema() {
        return constraintSchema;
    }

    /**
     * Legt den Wert der constraintSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConstraintSchema(String value) {
        this.constraintSchema = value;
    }

    /**
     * Ruft den Wert der constraintName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConstraintName() {
        return constraintName;
    }

    /**
     * Legt den Wert der constraintName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConstraintName(String value) {
        this.constraintName = value;
    }

    /**
     * Ruft den Wert der constraintType-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link TableConstraintType }
     *
     */
    public TableConstraintType getConstraintType() {
        return constraintType;
    }

    /**
     * Legt den Wert der constraintType-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link TableConstraintType }
     *
     */
    public void setConstraintType(TableConstraintType value) {
        this.constraintType = value;
    }

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

    public TableConstraint withConstraintCatalog(String value) {
        setConstraintCatalog(value);
        return this;
    }

    public TableConstraint withConstraintSchema(String value) {
        setConstraintSchema(value);
        return this;
    }

    public TableConstraint withConstraintName(String value) {
        setConstraintName(value);
        return this;
    }

    public TableConstraint withConstraintType(TableConstraintType value) {
        setConstraintType(value);
        return this;
    }

    public TableConstraint withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public TableConstraint withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public TableConstraint withTableName(String value) {
        setTableName(value);
        return this;
    }

}
