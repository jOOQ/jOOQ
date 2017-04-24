//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:35:14 AM CEST
//


package org.jooq.conf;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für MappedTable complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="MappedTable"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="input" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="inputExpression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="output" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedTable", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MappedTable
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31000L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    @XmlElement(required = true)
    protected String output;

    /**
     * Ruft den Wert der input-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInput() {
        return input;
    }

    /**
     * Legt den Wert der input-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInput(String value) {
        this.input = value;
    }

    /**
     * Ruft den Wert der inputExpression-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Pattern getInputExpression() {
        return inputExpression;
    }

    /**
     * Legt den Wert der inputExpression-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputExpression(Pattern value) {
        this.inputExpression = value;
    }

    /**
     * Ruft den Wert der output-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutput() {
        return output;
    }

    /**
     * Legt den Wert der output-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutput(String value) {
        this.output = value;
    }

    public MappedTable withInput(String value) {
        setInput(value);
        return this;
    }

    public MappedTable withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    public MappedTable withOutput(String value) {
        setOutput(value);
        return this;
    }

}
