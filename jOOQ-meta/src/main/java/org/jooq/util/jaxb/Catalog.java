







package org.jooq.util.jaxb;

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
 * <p>Java class for Catalog complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Catalog"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="inputCatalog" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="outputCatalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputCatalogToDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="schemata" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Schemata" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
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
     * Gets the value of the inputCatalog property.
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
     * Gets the value of the outputCatalog property.
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
     * Gets the value of the outputCatalogToDefault property.
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

}
