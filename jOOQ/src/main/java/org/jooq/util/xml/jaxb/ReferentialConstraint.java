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
 * <p>Java-Klasse für ReferentialConstraint complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ReferentialConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="unique_constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unique_constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unique_constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferentialConstraint", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ReferentialConstraint implements Serializable
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
    @XmlElement(name = "unique_constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintCatalog;
    @XmlElement(name = "unique_constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintSchema;
    @XmlElement(name = "unique_constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintName;

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
     * Ruft den Wert der uniqueConstraintCatalog-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUniqueConstraintCatalog() {
        return uniqueConstraintCatalog;
    }

    /**
     * Legt den Wert der uniqueConstraintCatalog-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUniqueConstraintCatalog(String value) {
        this.uniqueConstraintCatalog = value;
    }

    /**
     * Ruft den Wert der uniqueConstraintSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUniqueConstraintSchema() {
        return uniqueConstraintSchema;
    }

    /**
     * Legt den Wert der uniqueConstraintSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUniqueConstraintSchema(String value) {
        this.uniqueConstraintSchema = value;
    }

    /**
     * Ruft den Wert der uniqueConstraintName-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUniqueConstraintName() {
        return uniqueConstraintName;
    }

    /**
     * Legt den Wert der uniqueConstraintName-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUniqueConstraintName(String value) {
        this.uniqueConstraintName = value;
    }

    public ReferentialConstraint withConstraintCatalog(String value) {
        setConstraintCatalog(value);
        return this;
    }

    public ReferentialConstraint withConstraintSchema(String value) {
        setConstraintSchema(value);
        return this;
    }

    public ReferentialConstraint withConstraintName(String value) {
        setConstraintName(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintCatalog(String value) {
        setUniqueConstraintCatalog(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintSchema(String value) {
        setUniqueConstraintSchema(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintName(String value) {
        setUniqueConstraintName(value);
        return this;
    }

}
