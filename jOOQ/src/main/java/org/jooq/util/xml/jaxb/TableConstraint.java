







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
 * <p>Java class for TableConstraint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TableConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="constraint_type" type="{http://www.jooq.org/xsd/jooq-meta-3.11.0.xsd}TableConstraintType"/&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

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
     * Gets the value of the constraintType property.
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
     * Sets the value of the constraintType property.
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
     * Gets the value of the tableCatalog property.
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
     * Sets the value of the tableCatalog property.
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
     * Gets the value of the tableSchema property.
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
     * Sets the value of the tableSchema property.
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
     * Gets the value of the tableName property.
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
     * Sets the value of the tableName property.
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
     * Gets the value of the comment property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setComment(String value) {
        this.comment = value;
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

    public TableConstraint withComment(String value) {
        setComment(value);
        return this;
    }

}
