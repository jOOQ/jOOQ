
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for SyntheticViewType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticViewType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sql" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticViewType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticViewType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalog;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schema;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sql;

    /**
     * The defining catalog of the view.
     * 
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * The defining catalog of the view.
     * 
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The defining schema of the view.
     * 
     */
    public String getSchema() {
        return schema;
    }

    /**
     * The defining schema of the view.
     * 
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The defining name of the view.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The defining name of the view.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The defining comment on the view.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The defining comment on the view.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * The defining SQL query.
     * 
     */
    public String getSql() {
        return sql;
    }

    /**
     * The defining SQL query.
     * 
     */
    public void setSql(String value) {
        this.sql = value;
    }

    /**
     * The defining catalog of the view.
     * 
     */
    public SyntheticViewType withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    /**
     * The defining schema of the view.
     * 
     */
    public SyntheticViewType withSchema(String value) {
        setSchema(value);
        return this;
    }

    /**
     * The defining name of the view.
     * 
     */
    public SyntheticViewType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The defining comment on the view.
     * 
     */
    public SyntheticViewType withComment(String value) {
        setComment(value);
        return this;
    }

    /**
     * The defining SQL query.
     * 
     */
    public SyntheticViewType withSql(String value) {
        setSql(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog", catalog);
        builder.append("schema", schema);
        builder.append("name", name);
        builder.append("comment", comment);
        builder.append("sql", sql);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
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
        SyntheticViewType other = ((SyntheticViewType) that);
        if (catalog == null) {
            if (other.catalog!= null) {
                return false;
            }
        } else {
            if (!catalog.equals(other.catalog)) {
                return false;
            }
        }
        if (schema == null) {
            if (other.schema!= null) {
                return false;
            }
        } else {
            if (!schema.equals(other.schema)) {
                return false;
            }
        }
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
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
        if (sql == null) {
            if (other.sql!= null) {
                return false;
            }
        } else {
            if (!sql.equals(other.sql)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((catalog == null)? 0 :catalog.hashCode()));
        result = ((prime*result)+((schema == null)? 0 :schema.hashCode()));
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        result = ((prime*result)+((sql == null)? 0 :sql.hashCode()));
        return result;
    }

}
