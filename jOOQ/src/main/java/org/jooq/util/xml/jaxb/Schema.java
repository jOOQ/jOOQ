
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
 * <p>Java class for Schema complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Schema"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schema_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "Schema", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Schema implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(name = "catalog_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogName;
    @XmlElement(name = "schema_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaName;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    /**
     * Gets the value of the catalogName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * Sets the value of the catalogName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCatalogName(String value) {
        this.catalogName = value;
    }

    /**
     * Gets the value of the schemaName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets the value of the schemaName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchemaName(String value) {
        this.schemaName = value;
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

    public Schema withCatalogName(String value) {
        setCatalogName(value);
        return this;
    }

    public Schema withSchemaName(String value) {
        setSchemaName(value);
        return this;
    }

    public Schema withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog_name", catalogName);
        builder.append("schema_name", schemaName);
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
        Schema other = ((Schema) that);
        if (catalogName == null) {
            if (other.catalogName!= null) {
                return false;
            }
        } else {
            if (!catalogName.equals(other.catalogName)) {
                return false;
            }
        }
        if (schemaName == null) {
            if (other.schemaName!= null) {
                return false;
            }
        } else {
            if (!schemaName.equals(other.schemaName)) {
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
        result = ((prime*result)+((schemaName == null)? 0 :schemaName.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
