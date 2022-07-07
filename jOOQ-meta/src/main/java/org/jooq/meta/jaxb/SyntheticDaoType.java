
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for SyntheticDaoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticDaoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="methods" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.0.xsd}SyntheticDaoMethodsType" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticDaoType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticDaoType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31800L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalog;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schema;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    @XmlElementWrapper(name = "methods")
    @XmlElement(name = "method")
    protected List<SyntheticDaoMethodType> methods;

    /**
     * The defining catalog of the DAO.
     * 
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * The defining catalog of the DAO.
     * 
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The defining schema of the DAO.
     * 
     */
    public String getSchema() {
        return schema;
    }

    /**
     * The defining schema of the DAO.
     * 
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The defining name of the DAO.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The defining name of the DAO.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The defining comment on the DAO.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The defining comment on the DAO.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    public List<SyntheticDaoMethodType> getMethods() {
        if (methods == null) {
            methods = new ArrayList<SyntheticDaoMethodType>();
        }
        return methods;
    }

    public void setMethods(List<SyntheticDaoMethodType> methods) {
        this.methods = methods;
    }

    /**
     * The defining catalog of the DAO.
     * 
     */
    public SyntheticDaoType withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    /**
     * The defining schema of the DAO.
     * 
     */
    public SyntheticDaoType withSchema(String value) {
        setSchema(value);
        return this;
    }

    /**
     * The defining name of the DAO.
     * 
     */
    public SyntheticDaoType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The defining comment on the DAO.
     * 
     */
    public SyntheticDaoType withComment(String value) {
        setComment(value);
        return this;
    }

    public SyntheticDaoType withMethods(SyntheticDaoMethodType... values) {
        if (values!= null) {
            for (SyntheticDaoMethodType value: values) {
                getMethods().add(value);
            }
        }
        return this;
    }

    public SyntheticDaoType withMethods(Collection<SyntheticDaoMethodType> values) {
        if (values!= null) {
            getMethods().addAll(values);
        }
        return this;
    }

    public SyntheticDaoType withMethods(List<SyntheticDaoMethodType> methods) {
        setMethods(methods);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog", catalog);
        builder.append("schema", schema);
        builder.append("name", name);
        builder.append("comment", comment);
        builder.append("methods", "method", methods);
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
        SyntheticDaoType other = ((SyntheticDaoType) that);
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
        if (methods == null) {
            if (other.methods!= null) {
                return false;
            }
        } else {
            if (!methods.equals(other.methods)) {
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
        result = ((prime*result)+((methods == null)? 0 :methods.hashCode()));
        return result;
    }

}
