
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
 * <p>Java class for SyntheticDaoMethodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticDaoMethodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="returnType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "SyntheticDaoMethodType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticDaoMethodType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31800L;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    @XmlElement(defaultValue = "void")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String returnType = "void";
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sql;

    /**
     * The defining name of the DAO method.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The defining name of the DAO method.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The defining comment on the DAO method.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The defining comment on the DAO method.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * The return type of the DAO method, defaulting to <code>void</code>.
     * 
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * The return type of the DAO method, defaulting to <code>void</code>.
     * 
     */
    public void setReturnType(String value) {
        this.returnType = value;
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
     * The defining name of the DAO method.
     * 
     */
    public SyntheticDaoMethodType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The defining comment on the DAO method.
     * 
     */
    public SyntheticDaoMethodType withComment(String value) {
        setComment(value);
        return this;
    }

    /**
     * The return type of the DAO method, defaulting to <code>void</code>.
     * 
     */
    public SyntheticDaoMethodType withReturnType(String value) {
        setReturnType(value);
        return this;
    }

    /**
     * The defining SQL query.
     * 
     */
    public SyntheticDaoMethodType withSql(String value) {
        setSql(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("comment", comment);
        builder.append("returnType", returnType);
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
        SyntheticDaoMethodType other = ((SyntheticDaoMethodType) that);
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
        if (returnType == null) {
            if (other.returnType!= null) {
                return false;
            }
        } else {
            if (!returnType.equals(other.returnType)) {
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
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        result = ((prime*result)+((returnType == null)? 0 :returnType.hashCode()));
        result = ((prime*result)+((sql == null)? 0 :sql.hashCode()));
        return result;
    }

}
