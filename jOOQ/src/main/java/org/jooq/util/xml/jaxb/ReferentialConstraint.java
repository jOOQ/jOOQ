







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for ReferentialConstraint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
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

    private final static long serialVersionUID = 31100L;
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
     * Gets the value of the constraintCatalog property.
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
     * Sets the value of the constraintCatalog property.
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
     * Gets the value of the constraintSchema property.
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
     * Sets the value of the constraintSchema property.
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
     * Gets the value of the constraintName property.
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
     * Sets the value of the constraintName property.
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
     * Gets the value of the uniqueConstraintCatalog property.
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
     * Sets the value of the uniqueConstraintCatalog property.
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
     * Gets the value of the uniqueConstraintSchema property.
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
     * Sets the value of the uniqueConstraintSchema property.
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
     * Gets the value of the uniqueConstraintName property.
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
     * Sets the value of the uniqueConstraintName property.
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
