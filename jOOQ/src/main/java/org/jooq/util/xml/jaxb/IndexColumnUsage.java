







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for IndexColumnUsage complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IndexColumnUsage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="index_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="index_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="index_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="column_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ordinal_position" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="is_descending" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndexColumnUsage", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class IndexColumnUsage implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(name = "index_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String indexCatalog;
    @XmlElement(name = "index_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String indexSchema;
    @XmlElement(name = "index_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String indexName;
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
    @XmlElement(name = "ordinal_position")
    protected int ordinalPosition;
    @XmlElement(name = "is_descending")
    protected Boolean isDescending;

    /**
     * Gets the value of the indexCatalog property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndexCatalog() {
        return indexCatalog;
    }

    /**
     * Sets the value of the indexCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndexCatalog(String value) {
        this.indexCatalog = value;
    }

    /**
     * Gets the value of the indexSchema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndexSchema() {
        return indexSchema;
    }

    /**
     * Sets the value of the indexSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndexSchema(String value) {
        this.indexSchema = value;
    }

    /**
     * Gets the value of the indexName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * Sets the value of the indexName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndexName(String value) {
        this.indexName = value;
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
     * Gets the value of the columnName property.
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
     * Sets the value of the columnName property.
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
     * Gets the value of the ordinalPosition property.
     *
     */
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of the ordinalPosition property.
     *
     */
    public void setOrdinalPosition(int value) {
        this.ordinalPosition = value;
    }

    /**
     * Gets the value of the isDescending property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsDescending() {
        return isDescending;
    }

    /**
     * Sets the value of the isDescending property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsDescending(Boolean value) {
        this.isDescending = value;
    }

    public IndexColumnUsage withIndexCatalog(String value) {
        setIndexCatalog(value);
        return this;
    }

    public IndexColumnUsage withIndexSchema(String value) {
        setIndexSchema(value);
        return this;
    }

    public IndexColumnUsage withIndexName(String value) {
        setIndexName(value);
        return this;
    }

    public IndexColumnUsage withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public IndexColumnUsage withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public IndexColumnUsage withTableName(String value) {
        setTableName(value);
        return this;
    }

    public IndexColumnUsage withColumnName(String value) {
        setColumnName(value);
        return this;
    }

    public IndexColumnUsage withOrdinalPosition(int value) {
        setOrdinalPosition(value);
        return this;
    }

    public IndexColumnUsage withIsDescending(Boolean value) {
        setIsDescending(value);
        return this;
    }

}
