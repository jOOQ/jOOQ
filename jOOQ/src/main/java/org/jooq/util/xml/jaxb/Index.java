
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Index complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Index"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="index_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="index_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="index_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="is_unique" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "Index", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Index implements Serializable
{

    private final static long serialVersionUID = 31200L;
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
    @XmlElement(name = "is_unique")
    protected Boolean isUnique;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

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
     * Gets the value of the isUnique property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isIsUnique() {
        return isUnique;
    }

    /**
     * Sets the value of the isUnique property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIsUnique(Boolean value) {
        this.isUnique = value;
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

    public Index withIndexCatalog(String value) {
        setIndexCatalog(value);
        return this;
    }

    public Index withIndexSchema(String value) {
        setIndexSchema(value);
        return this;
    }

    public Index withIndexName(String value) {
        setIndexName(value);
        return this;
    }

    public Index withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public Index withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public Index withTableName(String value) {
        setTableName(value);
        return this;
    }

    public Index withIsUnique(Boolean value) {
        setIsUnique(value);
        return this;
    }

    public Index withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((indexCatalog!= null)&&(!"".equals(indexCatalog))) {
            sb.append("<index_catalog>");
            sb.append(indexCatalog);
            sb.append("</index_catalog>");
        }
        if ((indexSchema!= null)&&(!"".equals(indexSchema))) {
            sb.append("<index_schema>");
            sb.append(indexSchema);
            sb.append("</index_schema>");
        }
        sb.append("<index_name>");
        sb.append(((indexName == null)?"":indexName));
        sb.append("</index_name>");
        if ((tableCatalog!= null)&&(!"".equals(tableCatalog))) {
            sb.append("<table_catalog>");
            sb.append(tableCatalog);
            sb.append("</table_catalog>");
        }
        if ((tableSchema!= null)&&(!"".equals(tableSchema))) {
            sb.append("<table_schema>");
            sb.append(tableSchema);
            sb.append("</table_schema>");
        }
        sb.append("<table_name>");
        sb.append(((tableName == null)?"":tableName));
        sb.append("</table_name>");
        if (isUnique!= null) {
            sb.append("<is_unique>");
            sb.append(isUnique);
            sb.append("</is_unique>");
        }
        if ((comment!= null)&&(!"".equals(comment))) {
            sb.append("<comment>");
            sb.append(comment);
            sb.append("</comment>");
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
        Index other = ((Index) that);
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
        if (isUnique == null) {
            if (other.isUnique!= null) {
                return false;
            }
        } else {
            if (!isUnique.equals(other.isUnique)) {
                return false;
            }
        }
        if (comment == null) {
            if (other.comment!= null) {
                return false;
            }
        } else {
            if (!comment.equals(other.comment)) {
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
        result = ((prime*result)+((isUnique == null)? 0 :isUnique.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
