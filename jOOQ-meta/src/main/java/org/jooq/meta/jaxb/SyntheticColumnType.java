
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for SyntheticColumnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticColumnType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "SyntheticColumnType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticColumnType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31800L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String type;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    /**
     * A regular expression matching all tables on which to apply this synthetic readonly column.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic readonly column.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * The column name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The column name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The column type.
     * 
     */
    public String getType() {
        return type;
    }

    /**
     * The column type.
     * 
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * The column comment.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The column comment.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic readonly column.
     * 
     */
    public SyntheticColumnType withTables(String value) {
        setTables(value);
        return this;
    }

    /**
     * The column name.
     * 
     */
    public SyntheticColumnType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The column type.
     * 
     */
    public SyntheticColumnType withType(String value) {
        setType(value);
        return this;
    }

    /**
     * The column comment.
     * 
     */
    public SyntheticColumnType withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("tables", tables);
        builder.append("name", name);
        builder.append("type", type);
        builder.append("comment", comment);
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
        SyntheticColumnType other = ((SyntheticColumnType) that);
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
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
        if (type == null) {
            if (other.type!= null) {
                return false;
            }
        } else {
            if (!type.equals(other.type)) {
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
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((type == null)? 0 :type.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
