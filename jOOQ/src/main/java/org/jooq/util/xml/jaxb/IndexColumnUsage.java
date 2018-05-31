







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

    private final static long serialVersionUID = 31100L;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (indexCatalog!= null) {
            sb.append("<indexCatalog>");
            sb.append(indexCatalog);
            sb.append("</indexCatalog>");
        }
        if (indexSchema!= null) {
            sb.append("<indexSchema>");
            sb.append(indexSchema);
            sb.append("</indexSchema>");
        }
        if (indexName!= null) {
            sb.append("<indexName>");
            sb.append(indexName);
            sb.append("</indexName>");
        }
        if (tableCatalog!= null) {
            sb.append("<tableCatalog>");
            sb.append(tableCatalog);
            sb.append("</tableCatalog>");
        }
        if (tableSchema!= null) {
            sb.append("<tableSchema>");
            sb.append(tableSchema);
            sb.append("</tableSchema>");
        }
        if (tableName!= null) {
            sb.append("<tableName>");
            sb.append(tableName);
            sb.append("</tableName>");
        }
        if (columnName!= null) {
            sb.append("<columnName>");
            sb.append(columnName);
            sb.append("</columnName>");
        }
        sb.append("<ordinalPosition>");
        sb.append(ordinalPosition);
        sb.append("</ordinalPosition>");
        if (isDescending!= null) {
            sb.append("<isDescending>");
            sb.append(isDescending);
            sb.append("</isDescending>");
        }
        return sb.toString();
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
        IndexColumnUsage other = ((IndexColumnUsage) that);
        if (indexCatalog == null) {
            if (other.indexCatalog!= null) {
                return false;
            }
        } else {
            if (!indexCatalog.equals(other.indexCatalog)) {
                return false;
            }
        }
        if (indexSchema == null) {
            if (other.indexSchema!= null) {
                return false;
            }
        } else {
            if (!indexSchema.equals(other.indexSchema)) {
                return false;
            }
        }
        if (indexName == null) {
            if (other.indexName!= null) {
                return false;
            }
        } else {
            if (!indexName.equals(other.indexName)) {
                return false;
            }
        }
        if (tableCatalog == null) {
            if (other.tableCatalog!= null) {
                return false;
            }
        } else {
            if (!tableCatalog.equals(other.tableCatalog)) {
                return false;
            }
        }
        if (tableSchema == null) {
            if (other.tableSchema!= null) {
                return false;
            }
        } else {
            if (!tableSchema.equals(other.tableSchema)) {
                return false;
            }
        }
        if (tableName == null) {
            if (other.tableName!= null) {
                return false;
            }
        } else {
            if (!tableName.equals(other.tableName)) {
                return false;
            }
        }
        if (columnName == null) {
            if (other.columnName!= null) {
                return false;
            }
        } else {
            if (!columnName.equals(other.columnName)) {
                return false;
            }
        }
        if (ordinalPosition!= other.ordinalPosition) {
            return false;
        }
        if (isDescending == null) {
            if (other.isDescending!= null) {
                return false;
            }
        } else {
            if (!isDescending.equals(other.isDescending)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((indexCatalog == null)? 0 :indexCatalog.hashCode()));
        result = ((prime*result)+((indexSchema == null)? 0 :indexSchema.hashCode()));
        result = ((prime*result)+((indexName == null)? 0 :indexName.hashCode()));
        result = ((prime*result)+((tableCatalog == null)? 0 :tableCatalog.hashCode()));
        result = ((prime*result)+((tableSchema == null)? 0 :tableSchema.hashCode()));
        result = ((prime*result)+((tableName == null)? 0 :tableName.hashCode()));
        result = ((prime*result)+((columnName == null)? 0 :columnName.hashCode()));
        result = ((prime*result)+ ordinalPosition);
        result = ((prime*result)+((isDescending == null)? 0 :isDescending.hashCode()));
        return result;
    }

}
