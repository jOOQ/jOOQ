







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


/**
 * Configuration of an input catalog and its mappings.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Catalog", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Catalog implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(required = true, defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String inputCatalog = "";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String outputCatalog;
    @XmlElement(defaultValue = "false")
    protected Boolean outputCatalogToDefault = false;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<Schema> schemata;

    /**
     * See also {@link Database#getInputCatalog()}
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInputCatalog() {
        return inputCatalog;
    }

    /**
     * Sets the value of the inputCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputCatalog(String value) {
        this.inputCatalog = value;
    }

    /**
     * See also {@link Database#getOutputCatalog()}
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutputCatalog() {
        return outputCatalog;
    }

    /**
     * Sets the value of the outputCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutputCatalog(String value) {
        this.outputCatalog = value;
    }

    /**
     * See also {@link Database#isOutputCatalogToDefault()}
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOutputCatalogToDefault() {
        return outputCatalogToDefault;
    }

    /**
     * Sets the value of the outputCatalogToDefault property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOutputCatalogToDefault(Boolean value) {
        this.outputCatalogToDefault = value;
    }

    public List<Schema> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<Schema>();
        }
        return schemata;
    }

    public void setSchemata(List<Schema> schemata) {
        this.schemata = schemata;
    }

    public Catalog withInputCatalog(String value) {
        setInputCatalog(value);
        return this;
    }

    public Catalog withOutputCatalog(String value) {
        setOutputCatalog(value);
        return this;
    }

    public Catalog withOutputCatalogToDefault(Boolean value) {
        setOutputCatalogToDefault(value);
        return this;
    }

    public Catalog withSchemata(Schema... values) {
        if (values!= null) {
            for (Schema value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public Catalog withSchemata(Collection<Schema> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public Catalog withSchemata(List<Schema> schemata) {
        setSchemata(schemata);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (inputCatalog!= null) {
            sb.append("<inputCatalog>");
            sb.append(inputCatalog);
            sb.append("</inputCatalog>");
        }
        if (outputCatalog!= null) {
            sb.append("<outputCatalog>");
            sb.append(outputCatalog);
            sb.append("</outputCatalog>");
        }
        if (outputCatalogToDefault!= null) {
            sb.append("<outputCatalogToDefault>");
            sb.append(outputCatalogToDefault);
            sb.append("</outputCatalogToDefault>");
        }
        if (schemata!= null) {
            sb.append("<schemata>");
            sb.append(schemata);
            sb.append("</schemata>");
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
        Catalog other = ((Catalog) that);
        if (inputCatalog == null) {
            if (other.inputCatalog!= null) {
                return false;
            }
        } else {
            if (!inputCatalog.equals(other.inputCatalog)) {
                return false;
            }
        }
        if (outputCatalog == null) {
            if (other.outputCatalog!= null) {
                return false;
            }
        } else {
            if (!outputCatalog.equals(other.outputCatalog)) {
                return false;
            }
        }
        if (outputCatalogToDefault == null) {
            if (other.outputCatalogToDefault!= null) {
                return false;
            }
        } else {
            if (!outputCatalogToDefault.equals(other.outputCatalogToDefault)) {
                return false;
            }
        }
        if (schemata == null) {
            if (other.schemata!= null) {
                return false;
            }
        } else {
            if (!schemata.equals(other.schemata)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((inputCatalog == null)? 0 :inputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalog == null)? 0 :outputCatalog.hashCode()));
        result = ((prime*result)+((outputCatalogToDefault == null)? 0 :outputCatalogToDefault.hashCode()));
        result = ((prime*result)+((schemata == null)? 0 :schemata.hashCode()));
        return result;
    }

}
