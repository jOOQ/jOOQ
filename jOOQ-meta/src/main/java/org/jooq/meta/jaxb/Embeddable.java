
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
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    protected List<EmbeddableField> fields;

    /**
     * The name of the embeddable type
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The name of the embeddable type
     * 
     */
    public void setName(String value) {
        this.name = value;
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
     * The name of the embeddable type
     * 
     */
    public Embeddable withName(String value) {
        setName(value);
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
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        return result;
    }

}
