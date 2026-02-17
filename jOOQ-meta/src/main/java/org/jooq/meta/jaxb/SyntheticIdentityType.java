
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
 *         &lt;element name="ignoreUnused" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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

    private final static long serialVersionUID = 32012L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fields;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreUnused = false;

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
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIgnoreUnused() {
        return ignoreUnused;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreUnused(Boolean value) {
        this.ignoreUnused = value;
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

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     */
    public SyntheticIdentityType withIgnoreUnused(Boolean value) {
        setIgnoreUnused(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("tables", tables);
        builder.append("fields", fields);
        builder.append("ignoreUnused", ignoreUnused);
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
        if (ignoreUnused == null) {
            if (other.ignoreUnused!= null) {
                return false;
            }
        } else {
            if (!ignoreUnused.equals(other.ignoreUnused)) {
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
        result = ((prime*result)+((ignoreUnused == null)? 0 :ignoreUnused.hashCode()));
        return result;
    }

}
