
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for SyntheticUniqueKeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticUniqueKeyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.jooq.org/xsd/jooq-codegen-3.20.1.xsd}SyntheticKeyFieldsType"/&gt;
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
@XmlType(name = "SyntheticUniqueKeyType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticUniqueKeyType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32001L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreUnused = false;
    @XmlElementWrapper(name = "fields", required = true)
    @XmlElement(name = "field")
    protected List<String> fields;

    /**
     * The optional unique key name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The optional unique key name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic unique key.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic unique key.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
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

    public List<String> getFields() {
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * The optional unique key name.
     * 
     */
    public SyntheticUniqueKeyType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic unique key.
     * 
     */
    public SyntheticUniqueKeyType withTables(String value) {
        setTables(value);
        return this;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     */
    public SyntheticUniqueKeyType withIgnoreUnused(Boolean value) {
        setIgnoreUnused(value);
        return this;
    }

    public SyntheticUniqueKeyType withFields(String... values) {
        if (values!= null) {
            for (String value: values) {
                getFields().add(value);
            }
        }
        return this;
    }

    public SyntheticUniqueKeyType withFields(Collection<String> values) {
        if (values!= null) {
            getFields().addAll(values);
        }
        return this;
    }

    public SyntheticUniqueKeyType withFields(List<String> fields) {
        setFields(fields);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("tables", tables);
        builder.append("ignoreUnused", ignoreUnused);
        builder.append("fields", "field", fields);
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
        SyntheticUniqueKeyType other = ((SyntheticUniqueKeyType) that);
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
        if (ignoreUnused == null) {
            if (other.ignoreUnused!= null) {
                return false;
            }
        } else {
            if (!ignoreUnused.equals(other.ignoreUnused)) {
                return false;
            }
        }
        if ((fields == null)||fields.isEmpty()) {
            if ((other.fields!= null)&&(!other.fields.isEmpty())) {
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
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((ignoreUnused == null)? 0 :ignoreUnused.hashCode()));
        result = ((prime*result)+(((fields == null)||fields.isEmpty())? 0 :fields.hashCode()));
        return result;
    }

}
