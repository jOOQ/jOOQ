
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
 * <p>Java class for Domain complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Domain"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="domain_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="domain_default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Domain", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Domain implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    @XmlElement(name = "domain_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainCatalog;
    @XmlElement(name = "domain_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainSchema;
    @XmlElement(name = "domain_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainName;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "domain_default")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainDefault;

    public String getDomainCatalog() {
        return domainCatalog;
    }

    public void setDomainCatalog(String value) {
        this.domainCatalog = value;
    }

    public String getDomainSchema() {
        return domainSchema;
    }

    public void setDomainSchema(String value) {
        this.domainSchema = value;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String value) {
        this.domainName = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String value) {
        this.dataType = value;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer value) {
        this.characterMaximumLength = value;
    }

    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(Integer value) {
        this.numericPrecision = value;
    }

    public Integer getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(Integer value) {
        this.numericScale = value;
    }

    public String getDomainDefault() {
        return domainDefault;
    }

    public void setDomainDefault(String value) {
        this.domainDefault = value;
    }

    public Domain withDomainCatalog(String value) {
        setDomainCatalog(value);
        return this;
    }

    public Domain withDomainSchema(String value) {
        setDomainSchema(value);
        return this;
    }

    public Domain withDomainName(String value) {
        setDomainName(value);
        return this;
    }

    public Domain withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Domain withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Domain withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Domain withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Domain withDomainDefault(String value) {
        setDomainDefault(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("domain_catalog", domainCatalog);
        builder.append("domain_schema", domainSchema);
        builder.append("domain_name", domainName);
        builder.append("data_type", dataType);
        builder.append("character_maximum_length", characterMaximumLength);
        builder.append("numeric_precision", numericPrecision);
        builder.append("numeric_scale", numericScale);
        builder.append("domain_default", domainDefault);
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
        Domain other = ((Domain) that);
        if (domainCatalog == null) {
            if (other.domainCatalog!= null) {
                return false;
            }
        } else {
            if (!domainCatalog.equals(other.domainCatalog)) {
                return false;
            }
        }
        if (domainSchema == null) {
            if (other.domainSchema!= null) {
                return false;
            }
        } else {
            if (!domainSchema.equals(other.domainSchema)) {
                return false;
            }
        }
        if (domainName == null) {
            if (other.domainName!= null) {
                return false;
            }
        } else {
            if (!domainName.equals(other.domainName)) {
                return false;
            }
        }
        if (dataType == null) {
            if (other.dataType!= null) {
                return false;
            }
        } else {
            if (!dataType.equals(other.dataType)) {
                return false;
            }
        }
        if (characterMaximumLength == null) {
            if (other.characterMaximumLength!= null) {
                return false;
            }
        } else {
            if (!characterMaximumLength.equals(other.characterMaximumLength)) {
                return false;
            }
        }
        if (numericPrecision == null) {
            if (other.numericPrecision!= null) {
                return false;
            }
        } else {
            if (!numericPrecision.equals(other.numericPrecision)) {
                return false;
            }
        }
        if (numericScale == null) {
            if (other.numericScale!= null) {
                return false;
            }
        } else {
            if (!numericScale.equals(other.numericScale)) {
                return false;
            }
        }
        if (domainDefault == null) {
            if (other.domainDefault!= null) {
                return false;
            }
        } else {
            if (!domainDefault.equals(other.domainDefault)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((domainCatalog == null)? 0 :domainCatalog.hashCode()));
        result = ((prime*result)+((domainSchema == null)? 0 :domainSchema.hashCode()));
        result = ((prime*result)+((domainName == null)? 0 :domainName.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((domainDefault == null)? 0 :domainDefault.hashCode()));
        return result;
    }

}
