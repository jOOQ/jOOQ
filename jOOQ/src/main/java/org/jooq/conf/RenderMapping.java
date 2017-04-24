







package org.jooq.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RenderMapping complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="RenderMapping"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="defaultSchema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schemata" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}MappedSchemata" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RenderMapping", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class RenderMapping
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31000L;
    protected String defaultSchema;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<MappedSchema> schemata;

    /**
     * Ruft den Wert der defaultSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultSchema() {
        return defaultSchema;
    }

    /**
     * Legt den Wert der defaultSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultSchema(String value) {
        this.defaultSchema = value;
    }

    public List<MappedSchema> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<MappedSchema>();
        }
        return schemata;
    }

    public void setSchemata(List<MappedSchema> schemata) {
        this.schemata = schemata;
    }

    public RenderMapping withDefaultSchema(String value) {
        setDefaultSchema(value);
        return this;
    }

    public RenderMapping withSchemata(MappedSchema... values) {
        if (values!= null) {
            for (MappedSchema value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public RenderMapping withSchemata(Collection<MappedSchema> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public RenderMapping withSchemata(List<MappedSchema> schemata) {
        setSchemata(schemata);
        return this;
    }

}
