
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
 * <p>Java class for SyntheticSynonymType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticSynonymType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="table" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "SyntheticSynonymType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticSynonymType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalog;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schema;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String table;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreUnused = false;

    /**
     * The defining catalog of the synonym.
     * 
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * The defining catalog of the synonym.
     * 
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The defining schema of the synonym.
     * 
     */
    public String getSchema() {
        return schema;
    }

    /**
     * The defining schema of the synonym.
     * 
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The synonym name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The synonym name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching a table to which to apply this synthetic synonym.
     * 
     */
    public String getTable() {
        return table;
    }

    /**
     * A regular expression matching a table to which to apply this synthetic synonym.
     * 
     */
    public void setTable(String value) {
        this.table = value;
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
     * The defining catalog of the synonym.
     * 
     */
    public SyntheticSynonymType withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    /**
     * The defining schema of the synonym.
     * 
     */
    public SyntheticSynonymType withSchema(String value) {
        setSchema(value);
        return this;
    }

    /**
     * The synonym name.
     * 
     */
    public SyntheticSynonymType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching a table to which to apply this synthetic synonym.
     * 
     */
    public SyntheticSynonymType withTable(String value) {
        setTable(value);
        return this;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     */
    public SyntheticSynonymType withIgnoreUnused(Boolean value) {
        setIgnoreUnused(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog", catalog);
        builder.append("schema", schema);
        builder.append("name", name);
        builder.append("table", table);
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
        SyntheticSynonymType other = ((SyntheticSynonymType) that);
        if (catalog == null) {
            if (other.catalog!= null) {
                return false;
            }
        } else {
            if (!catalog.equals(other.catalog)) {
                return false;
            }
        }
        if (schema == null) {
            if (other.schema!= null) {
                return false;
            }
        } else {
            if (!schema.equals(other.schema)) {
                return false;
            }
        }
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (table == null) {
            if (other.table!= null) {
                return false;
            }
        } else {
            if (!table.equals(other.table)) {
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
        result = ((prime*result)+((catalog == null)? 0 :catalog.hashCode()));
        result = ((prime*result)+((schema == null)? 0 :schema.hashCode()));
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((table == null)? 0 :table.hashCode()));
        result = ((prime*result)+((ignoreUnused == null)? 0 :ignoreUnused.hashCode()));
        return result;
    }

}
