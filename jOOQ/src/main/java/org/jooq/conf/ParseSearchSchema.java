
package org.jooq.conf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A schema that is on the search path.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParseSearchSchema", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ParseSearchSchema
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31200L;
    protected String catalog;
    @XmlElement(required = true)
    protected String schema;

    /**
     * The catalog qualifier of the schema, if applicable.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * Sets the value of the catalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The schema qualifier whose elements can be found from the search path.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    public ParseSearchSchema withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    public ParseSearchSchema withSchema(String value) {
        setSchema(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((catalog!= null)&&(!"".equals(catalog))) {
            sb.append("<catalog>");
            sb.append(catalog);
            sb.append("</catalog>");
        }
        if ((schema!= null)&&(!"".equals(schema))) {
            sb.append("<schema>");
            sb.append(schema);
            sb.append("</schema>");
        }
        return sb.toString();
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
        ParseSearchSchema other = ((ParseSearchSchema) that);
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
