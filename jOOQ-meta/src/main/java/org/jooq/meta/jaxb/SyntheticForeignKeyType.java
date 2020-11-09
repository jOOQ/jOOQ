
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.jooq.org/xsd/jooq-codegen-3.15.0.xsd}SyntheticKeyFieldsType"/&gt;
 *         &lt;element name="referencedTable" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="referencedFields" type="{http://www.jooq.org/xsd/jooq-codegen-3.15.0.xsd}SyntheticKeyFieldsType" minOccurs="0"/&gt;
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

    private final static long serialVersionUID = 31500L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencedTable;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencedKey;
    @XmlElementWrapper(name = "fields", required = true)
    @XmlElement(name = "field")
    protected List<String> fields;
    @XmlElementWrapper(name = "referencedFields")
    @XmlElement(name = "field")
    protected List<String> referencedFields;

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
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic foreign key.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
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

    public List<String> getFields() {
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getReferencedFields() {
        if (referencedFields == null) {
            referencedFields = new ArrayList<String>();
        }
        return referencedFields;
    }

    public void setReferencedFields(List<String> referencedFields) {
        this.referencedFields = referencedFields;
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
    public SyntheticForeignKeyType withTables(String value) {
        setTables(value);
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
     * A regular expression matching a key that is referenced by this synthetic foreign key.
     * 
     */
    public SyntheticForeignKeyType withReferencedKey(String value) {
        setReferencedKey(value);
        return this;
    }

    public SyntheticForeignKeyType withFields(String... values) {
        if (values!= null) {
            for (String value: values) {
                getFields().add(value);
            }
        }
        return this;
    }

    public SyntheticForeignKeyType withFields(Collection<String> values) {
        if (values!= null) {
            getFields().addAll(values);
        }
        return this;
    }

    public SyntheticForeignKeyType withFields(List<String> fields) {
        setFields(fields);
        return this;
    }

    public SyntheticForeignKeyType withReferencedFields(String... values) {
        if (values!= null) {
            for (String value: values) {
                getReferencedFields().add(value);
            }
        }
        return this;
    }

    public SyntheticForeignKeyType withReferencedFields(Collection<String> values) {
        if (values!= null) {
            getReferencedFields().addAll(values);
        }
        return this;
    }

    public SyntheticForeignKeyType withReferencedFields(List<String> referencedFields) {
        setReferencedFields(referencedFields);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("tables", tables);
        builder.append("referencedTable", referencedTable);
        builder.append("referencedKey", referencedKey);
        builder.append("fields", "field", fields);
        builder.append("referencedFields", "field", referencedFields);
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
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
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
        if (referencedKey == null) {
            if (other.referencedKey!= null) {
                return false;
            }
        } else {
            if (!referencedKey.equals(other.referencedKey)) {
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
        if (referencedFields == null) {
            if (other.referencedFields!= null) {
                return false;
            }
        } else {
            if (!referencedFields.equals(other.referencedFields)) {
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
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((referencedTable == null)? 0 :referencedTable.hashCode()));
        result = ((prime*result)+((referencedKey == null)? 0 :referencedKey.hashCode()));
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        result = ((prime*result)+((referencedFields == null)? 0 :referencedFields.hashCode()));
        return result;
    }

}
