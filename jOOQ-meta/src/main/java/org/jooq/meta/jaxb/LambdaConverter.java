
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
 * A converter taking two lambda definitions.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LambdaConverter", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class LambdaConverter implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String from;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String to;
    @XmlElement(defaultValue = "true")
    protected Boolean nullable = true;

    /**
     * The implementation of {@link org.jooq.Converter#from(Object)}.
     * 
     */
    public String getFrom() {
        return from;
    }

    /**
     * The implementation of {@link org.jooq.Converter#from(Object)}.
     * 
     */
    public void setFrom(String value) {
        this.from = value;
    }

    /**
     * The implementation of {@link org.jooq.Converter#to(Object)}.
     * 
     */
    public String getTo() {
        return to;
    }

    /**
     * The implementation of {@link org.jooq.Converter#to(Object)}.
     * 
     */
    public void setTo(String value) {
        this.to = value;
    }

    /**
     * Whether to use {@link org.jooq.Converter#ofNullable(Class, Class, java.util.function.Function, java.util.function.Function)} or {@link org.jooq.Converter#of(Class, Class, java.util.function.Function, java.util.function.Function)}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNullable() {
        return nullable;
    }

    /**
     * Sets the value of the nullable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNullable(Boolean value) {
        this.nullable = value;
    }

    /**
     * The implementation of {@link org.jooq.Converter#from(Object)}.
     * 
     */
    public LambdaConverter withFrom(String value) {
        setFrom(value);
        return this;
    }

    /**
     * The implementation of {@link org.jooq.Converter#to(Object)}.
     * 
     */
    public LambdaConverter withTo(String value) {
        setTo(value);
        return this;
    }

    public LambdaConverter withNullable(Boolean value) {
        setNullable(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("from", from);
        builder.append("to", to);
        builder.append("nullable", nullable);
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
        LambdaConverter other = ((LambdaConverter) that);
        if (from == null) {
            if (other.from!= null) {
                return false;
            }
        } else {
            if (!from.equals(other.from)) {
                return false;
            }
        }
        if (to == null) {
            if (other.to!= null) {
                return false;
            }
        } else {
            if (!to.equals(other.to)) {
                return false;
            }
        }
        if (nullable == null) {
            if (other.nullable!= null) {
                return false;
            }
        } else {
            if (!nullable.equals(other.nullable)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((from == null)? 0 :from.hashCode()));
        result = ((prime*result)+((to == null)? 0 :to.hashCode()));
        result = ((prime*result)+((nullable == null)? 0 :nullable.hashCode()));
        return result;
    }

}
