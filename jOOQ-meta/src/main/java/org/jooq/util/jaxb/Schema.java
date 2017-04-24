







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Schema complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Schema"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="inputSchema" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="outputSchema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outputSchemaToDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    @XmlElement(required = true, defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String inputSchema = "";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String outputSchema;
    @XmlElement(defaultValue = "false")
    protected Boolean outputSchemaToDefault = false;

    /**
     * Ruft den Wert der inputSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInputSchema() {
        return inputSchema;
    }

    /**
     * Legt den Wert der inputSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputSchema(String value) {
        this.inputSchema = value;
    }

    /**
     * Ruft den Wert der outputSchema-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * Legt den Wert der outputSchema-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutputSchema(String value) {
        this.outputSchema = value;
    }

    /**
     * Ruft den Wert der outputSchemaToDefault-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOutputSchemaToDefault() {
        return outputSchemaToDefault;
    }

    /**
     * Legt den Wert der outputSchemaToDefault-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOutputSchemaToDefault(Boolean value) {
        this.outputSchemaToDefault = value;
    }

    public Schema withInputSchema(String value) {
        setInputSchema(value);
        return this;
    }

    public Schema withOutputSchema(String value) {
        setOutputSchema(value);
        return this;
    }

    public Schema withOutputSchemaToDefault(Boolean value) {
        setOutputSchemaToDefault(value);
        return this;
    }

}
