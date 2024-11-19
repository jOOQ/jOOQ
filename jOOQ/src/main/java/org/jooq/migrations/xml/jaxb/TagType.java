
package org.jooq.migrations.xml.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for TagType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TagType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TagType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class TagType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32000L;
    @XmlElement(required = true)
    protected String id;
    protected String message;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public TagType withId(String value) {
        setId(value);
        return this;
    }

    public TagType withMessage(String value) {
        setMessage(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("id", id);
        builder.append("message", message);
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
        TagType other = ((TagType) that);
        if (id == null) {
            if (other.id!= null) {
                return false;
            }
        } else {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        if (message == null) {
            if (other.message!= null) {
                return false;
            }
        } else {
            if (!message.equals(other.message)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((id == null)? 0 :id.hashCode()));
        result = ((prime*result)+((message == null)? 0 :message.hashCode()));
        return result;
    }

}
