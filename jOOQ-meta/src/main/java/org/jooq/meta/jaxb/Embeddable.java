
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
 * An embeddable type declaration
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Embeddable", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Embeddable implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencingName;
    @XmlElement(defaultValue = "false")
    protected Boolean replacesFields = false;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    protected List<EmbeddableField> fields;

    /**
     * The defining name of the embeddable type.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The defining name of the embeddable type.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public String getReferencingName() {
        return referencingName;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public void setReferencingName(String value) {
        this.referencingName = value;
    }

    /**
     * Specify that the embeddable field replaces its underlying fields in code generation output, and when working with asterisks.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReplacesFields() {
        return replacesFields;
    }

    /**
     * Sets the value of the replacesFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReplacesFields(Boolean value) {
        this.replacesFields = value;
    }

    public List<EmbeddableField> getFields() {
        if (fields == null) {
            fields = new ArrayList<EmbeddableField>();
        }
        return fields;
    }

    public void setFields(List<EmbeddableField> fields) {
        this.fields = fields;
    }

    /**
     * The defining name of the embeddable type.
     * 
     */
    public Embeddable withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public Embeddable withReferencingName(String value) {
        setReferencingName(value);
        return this;
    }

    public Embeddable withReplacesFields(Boolean value) {
        setReplacesFields(value);
        return this;
    }

    public Embeddable withFields(EmbeddableField... values) {
        if (values!= null) {
            for (EmbeddableField value: values) {
                getFields().add(value);
            }
        }
        return this;
    }

    public Embeddable withFields(Collection<EmbeddableField> values) {
        if (values!= null) {
            getFields().addAll(values);
        }
        return this;
    }

    public Embeddable withFields(List<EmbeddableField> fields) {
        setFields(fields);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("referencingName", referencingName);
        builder.append("replacesFields", replacesFields);
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
        Embeddable other = ((Embeddable) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (referencingName == null) {
            if (other.referencingName!= null) {
                return false;
            }
        } else {
            if (!referencingName.equals(other.referencingName)) {
                return false;
            }
        }
        if (replacesFields == null) {
            if (other.replacesFields!= null) {
                return false;
            }
        } else {
            if (!replacesFields.equals(other.replacesFields)) {
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
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((referencingName == null)? 0 :referencingName.hashCode()));
        result = ((prime*result)+((replacesFields == null)? 0 :replacesFields.hashCode()));
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        return result;
    }

}
