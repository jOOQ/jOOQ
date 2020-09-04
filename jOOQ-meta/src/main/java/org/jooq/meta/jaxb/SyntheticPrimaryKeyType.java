
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
 * <p>Java class for SyntheticPrimaryKeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticPrimaryKeyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyTables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyFields" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticPrimaryKeyType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticPrimaryKeyType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyTables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyFields;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String key;

    /**
     * The optional primary key name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The optional primary key name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic primary key.
     * 
     */
    public String getKeyTables() {
        return keyTables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic primary key.
     * 
     */
    public void setKeyTables(String value) {
        this.keyTables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this new synthetic primary key.
     * 
     */
    public String getKeyFields() {
        return keyFields;
    }

    /**
     * A regular expression matching all fields on which to apply this new synthetic primary key.
     * 
     */
    public void setKeyFields(String value) {
        this.keyFields = value;
    }

    /**
     * A regular expression matching all unique keys and unique indexes which should be treated as primary key.
     * 
     */
    public String getKey() {
        return key;
    }

    /**
     * A regular expression matching all unique keys and unique indexes which should be treated as primary key.
     * 
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * The optional primary key name.
     * 
     */
    public SyntheticPrimaryKeyType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic primary key.
     * 
     */
    public SyntheticPrimaryKeyType withKeyTables(String value) {
        setKeyTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this new synthetic primary key.
     * 
     */
    public SyntheticPrimaryKeyType withKeyFields(String value) {
        setKeyFields(value);
        return this;
    }

    /**
     * A regular expression matching all unique keys and unique indexes which should be treated as primary key.
     * 
     */
    public SyntheticPrimaryKeyType withKey(String value) {
        setKey(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("keyTables", keyTables);
        builder.append("keyFields", keyFields);
        builder.append("key", key);
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
        SyntheticPrimaryKeyType other = ((SyntheticPrimaryKeyType) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
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
        if (key == null) {
            if (other.key!= null) {
                return false;
            }
        } else {
            if (!key.equals(other.key)) {
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
        result = ((prime*result)+((keyTables == null)? 0 :keyTables.hashCode()));
        result = ((prime*result)+((keyFields == null)? 0 :keyFields.hashCode()));
        result = ((prime*result)+((key == null)? 0 :key.hashCode()));
        return result;
    }

}
