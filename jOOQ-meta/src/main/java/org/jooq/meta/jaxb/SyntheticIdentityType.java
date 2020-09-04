
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
 * <p>Java class for SyntheticIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticIdentityType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="keyTables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyFields" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticIdentityType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticIdentityType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyTables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyFields;

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public String getKeyTables() {
        return keyTables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public void setKeyTables(String value) {
        this.keyTables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public String getKeyFields() {
        return keyFields;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public void setKeyFields(String value) {
        this.keyFields = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public SyntheticIdentityType withKeyTables(String value) {
        setKeyTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public SyntheticIdentityType withKeyFields(String value) {
        setKeyFields(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("keyTables", keyTables);
        builder.append("keyFields", keyFields);
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
        SyntheticIdentityType other = ((SyntheticIdentityType) that);
        if (keyTables == null) {
            if (other.keyTables!= null) {
                return false;
            }
        } else {
            if (!keyTables.equals(other.keyTables)) {
                return false;
            }
        }
        if (keyFields == null) {
            if (other.keyFields!= null) {
                return false;
            }
        } else {
            if (!keyFields.equals(other.keyFields)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((keyTables == null)? 0 :keyTables.hashCode()));
        result = ((prime*result)+((keyFields == null)? 0 :keyFields.hashCode()));
        return result;
    }

}
