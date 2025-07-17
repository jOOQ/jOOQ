
package org.jooq.util.xml.jaxb;

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
 * <p>Java class for DirectSupertype complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectSupertype"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="udt_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="supertype_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supertype_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supertype_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectSupertype", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class DirectSupertype implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32100L;
    @XmlElement(name = "udt_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtCatalog;
    @XmlElement(name = "udt_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtSchema;
    @XmlElement(name = "udt_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtName;
    @XmlElement(name = "supertype_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String supertypeCatalog;
    @XmlElement(name = "supertype_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String supertypeSchema;
    @XmlElement(name = "supertype_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String supertypeName;

    public String getUdtCatalog() {
        return udtCatalog;
    }

    public void setUdtCatalog(String value) {
        this.udtCatalog = value;
    }

    public String getUdtSchema() {
        return udtSchema;
    }

    public void setUdtSchema(String value) {
        this.udtSchema = value;
    }

    public String getUdtName() {
        return udtName;
    }

    public void setUdtName(String value) {
        this.udtName = value;
    }

    public String getSupertypeCatalog() {
        return supertypeCatalog;
    }

    public void setSupertypeCatalog(String value) {
        this.supertypeCatalog = value;
    }

    public String getSupertypeSchema() {
        return supertypeSchema;
    }

    public void setSupertypeSchema(String value) {
        this.supertypeSchema = value;
    }

    public String getSupertypeName() {
        return supertypeName;
    }

    public void setSupertypeName(String value) {
        this.supertypeName = value;
    }

    public DirectSupertype withUdtCatalog(String value) {
        setUdtCatalog(value);
        return this;
    }

    public DirectSupertype withUdtSchema(String value) {
        setUdtSchema(value);
        return this;
    }

    public DirectSupertype withUdtName(String value) {
        setUdtName(value);
        return this;
    }

    public DirectSupertype withSupertypeCatalog(String value) {
        setSupertypeCatalog(value);
        return this;
    }

    public DirectSupertype withSupertypeSchema(String value) {
        setSupertypeSchema(value);
        return this;
    }

    public DirectSupertype withSupertypeName(String value) {
        setSupertypeName(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("udt_catalog", udtCatalog);
        builder.append("udt_schema", udtSchema);
        builder.append("udt_name", udtName);
        builder.append("supertype_catalog", supertypeCatalog);
        builder.append("supertype_schema", supertypeSchema);
        builder.append("supertype_name", supertypeName);
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
        DirectSupertype other = ((DirectSupertype) that);
        if (udtCatalog == null) {
            if (other.udtCatalog!= null) {
                return false;
            }
        } else {
            if (!udtCatalog.equals(other.udtCatalog)) {
                return false;
            }
        }
        if (udtSchema == null) {
            if (other.udtSchema!= null) {
                return false;
            }
        } else {
            if (!udtSchema.equals(other.udtSchema)) {
                return false;
            }
        }
        if (udtName == null) {
            if (other.udtName!= null) {
                return false;
            }
        } else {
            if (!udtName.equals(other.udtName)) {
                return false;
            }
        }
        if (supertypeCatalog == null) {
            if (other.supertypeCatalog!= null) {
                return false;
            }
        } else {
            if (!supertypeCatalog.equals(other.supertypeCatalog)) {
                return false;
            }
        }
        if (supertypeSchema == null) {
            if (other.supertypeSchema!= null) {
                return false;
            }
        } else {
            if (!supertypeSchema.equals(other.supertypeSchema)) {
                return false;
            }
        }
        if (supertypeName == null) {
            if (other.supertypeName!= null) {
                return false;
            }
        } else {
            if (!supertypeName.equals(other.supertypeName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((udtCatalog == null)? 0 :udtCatalog.hashCode()));
        result = ((prime*result)+((udtSchema == null)? 0 :udtSchema.hashCode()));
        result = ((prime*result)+((udtName == null)? 0 :udtName.hashCode()));
        result = ((prime*result)+((supertypeCatalog == null)? 0 :supertypeCatalog.hashCode()));
        result = ((prime*result)+((supertypeSchema == null)? 0 :supertypeSchema.hashCode()));
        result = ((prime*result)+((supertypeName == null)? 0 :supertypeName.hashCode()));
        return result;
    }

}
