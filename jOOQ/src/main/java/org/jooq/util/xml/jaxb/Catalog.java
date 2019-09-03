
package org.jooq.util.xml.jaxb;

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
 * <p>Java class for Catalog complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Catalog"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "Catalog", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Catalog implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(name = "catalog_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogName;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String value) {
        this.catalogName = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public Catalog withCatalogName(String value) {
        setCatalogName(value);
        return this;
    }

    public Catalog withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog_name", catalogName);
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
        Catalog other = ((Catalog) that);
        if (catalogName == null) {
            if (other.catalogName!= null) {
                return false;
            }
        } else {
            if (!catalogName.equals(other.catalogName)) {
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
        result = ((prime*result)+((catalogName == null)? 0 :catalogName.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
