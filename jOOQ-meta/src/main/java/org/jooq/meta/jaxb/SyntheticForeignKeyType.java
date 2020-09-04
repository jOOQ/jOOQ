
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
 * <p>Java class for SyntheticForeignKeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticForeignKeyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyTables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyFields" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="referencedTable" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="referencedFields" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="referencedKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticForeignKeyType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticForeignKeyType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyTables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String keyFields;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencedTable;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencedFields;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencedKey;

    /**
     * The optional foreign key name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The optional foreign key name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic foreign key.
     * 
     */
    public String getKeyTables() {
        return keyTables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic foreign key.
     * 
     */
    public void setKeyTables(String value) {
        this.keyTables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic foreign key.
     * 
     */
    public String getKeyFields() {
        return keyFields;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic foreign key.
     * 
     */
    public void setKeyFields(String value) {
        this.keyFields = value;
    }

    /**
     * A regular expression matching a table that is referenced by this synthetic foreign key.
     * 
     */
    public String getReferencedTable() {
        return referencedTable;
    }

    /**
     * A regular expression matching a table that is referenced by this synthetic foreign key.
     * 
     */
    public void setReferencedTable(String value) {
        this.referencedTable = value;
    }

    /**
     * A regular expression matching fields that are referenced by this synthetic foreign key.
     * 
     */
    public String getReferencedFields() {
        return referencedFields;
    }

    /**
     * A regular expression matching fields that are referenced by this synthetic foreign key.
     * 
     */
    public void setReferencedFields(String value) {
        this.referencedFields = value;
    }

    /**
     * A regular expression matching a key that is referenced by this synthetic foreign key.
     * 
     */
    public String getReferencedKey() {
        return referencedKey;
    }

    /**
     * A regular expression matching a key that is referenced by this synthetic foreign key.
     * 
     */
    public void setReferencedKey(String value) {
        this.referencedKey = value;
    }

    /**
     * The optional foreign key name.
     * 
     */
    public SyntheticForeignKeyType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withKeyTables(String value) {
        setKeyTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withKeyFields(String value) {
        setKeyFields(value);
        return this;
    }

    /**
     * A regular expression matching a table that is referenced by this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withReferencedTable(String value) {
        setReferencedTable(value);
        return this;
    }

    /**
     * A regular expression matching fields that are referenced by this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withReferencedFields(String value) {
        setReferencedFields(value);
        return this;
    }

    /**
     * A regular expression matching a key that is referenced by this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withReferencedKey(String value) {
        setReferencedKey(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("keyTables", keyTables);
        builder.append("keyFields", keyFields);
        builder.append("referencedTable", referencedTable);
        builder.append("referencedFields", referencedFields);
        builder.append("referencedKey", referencedKey);
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
        SyntheticForeignKeyType other = ((SyntheticForeignKeyType) that);
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
        if (referencedTable == null) {
            if (other.referencedTable!= null) {
                return false;
            }
        } else {
            if (!referencedTable.equals(other.referencedTable)) {
                return false;
            }
        }
        if (referencedFields == null) {
            if (other.referencedFields!= null) {
                return false;
            }
        } else {
            if (!referencedFields.equals(other.referencedFields)) {
                return false;
            }
        }
        if (referencedKey == null) {
            if (other.referencedKey!= null) {
                return false;
            }
        } else {
            if (!referencedKey.equals(other.referencedKey)) {
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
        result = ((prime*result)+((referencedTable == null)? 0 :referencedTable.hashCode()));
        result = ((prime*result)+((referencedFields == null)? 0 :referencedFields.hashCode()));
        result = ((prime*result)+((referencedKey == null)? 0 :referencedKey.hashCode()));
        return result;
    }

}
