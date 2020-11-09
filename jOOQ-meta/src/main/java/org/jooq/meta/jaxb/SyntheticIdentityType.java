
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
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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

    private final static long serialVersionUID = 31500L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fields;

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public String getFields() {
        return fields;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public void setFields(String value) {
        this.fields = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic identity.
     * 
     */
    public SyntheticIdentityType withTables(String value) {
        setTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic identity.
     * 
     */
    public SyntheticIdentityType withFields(String value) {
        setFields(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("tables", tables);
        builder.append("fields", fields);
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
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
                return false;
            }
        }
        if (fields == null) {
            if (other.fields!= null) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
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
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        return result;
    }

}
