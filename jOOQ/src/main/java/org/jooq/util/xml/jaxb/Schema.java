







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Schema complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Schema"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalog_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schema_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Schema", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Schema implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(name = "catalog_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogName;
    @XmlElement(name = "schema_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaName;

    /**
     * Gets the value of the catalogName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * Sets the value of the catalogName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCatalogName(String value) {
        this.catalogName = value;
    }

    /**
     * Gets the value of the schemaName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets the value of the schemaName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchemaName(String value) {
        this.schemaName = value;
    }

    public Schema withCatalogName(String value) {
        setCatalogName(value);
        return this;
    }

    public Schema withSchemaName(String value) {
        setSchemaName(value);
        return this;
    }

}
