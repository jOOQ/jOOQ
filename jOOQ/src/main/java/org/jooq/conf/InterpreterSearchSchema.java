
package org.jooq.conf;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * A schema that is on the search path.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterpreterSearchSchema", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class InterpreterSearchSchema
    extends SettingsBase
    implements Serializable, Cloneable, XMLAppendable
{

    private final static long serialVersionUID = 32003L;
    protected String catalog;
    @XmlElement(required = true)
    protected String schema;

    /**
     * The catalog qualifier of the schema, if applicable.
     * 
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * The catalog qualifier of the schema, if applicable.
     * 
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The schema qualifier whose elements can be found from the search path.
     * 
     */
    public String getSchema() {
        return schema;
    }

    /**
     * The schema qualifier whose elements can be found from the search path.
     * 
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The catalog qualifier of the schema, if applicable.
     * 
     */
    public InterpreterSearchSchema withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    /**
     * The schema qualifier whose elements can be found from the search path.
     * 
     */
    public InterpreterSearchSchema withSchema(String value) {
        setSchema(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog", catalog);
        builder.append("schema", schema);
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
        InterpreterSearchSchema other = ((InterpreterSearchSchema) that);
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((catalog == null)? 0 :catalog.hashCode()));
        result = ((prime*result)+((schema == null)? 0 :schema.hashCode()));
        return result;
    }

}
