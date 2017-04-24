







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="logging" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Logging" minOccurs="0"/&gt;
 *         &lt;element name="jdbc" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Jdbc" minOccurs="0"/&gt;
 *         &lt;element name="generator" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Generator"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "configuration")
@SuppressWarnings({
    "all"
})
public class Configuration implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlSchemaType(name = "string")
    protected Logging logging;
    protected Jdbc jdbc;
    @XmlElement(required = true)
    protected Generator generator;

    /**
     * Ruft den Wert der logging-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Logging }
     *
     */
    public Logging getLogging() {
        return logging;
    }

    /**
     * Legt den Wert der logging-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Logging }
     *
     */
    public void setLogging(Logging value) {
        this.logging = value;
    }

    /**
     * Ruft den Wert der jdbc-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Jdbc }
     *
     */
    public Jdbc getJdbc() {
        return jdbc;
    }

    /**
     * Legt den Wert der jdbc-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Jdbc }
     *
     */
    public void setJdbc(Jdbc value) {
        this.jdbc = value;
    }

    /**
     * Ruft den Wert der generator-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Generator }
     *
     */
    public Generator getGenerator() {
        return generator;
    }

    /**
     * Legt den Wert der generator-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Generator }
     *
     */
    public void setGenerator(Generator value) {
        this.generator = value;
    }

    public Configuration withLogging(Logging value) {
        setLogging(value);
        return this;
    }

    public Configuration withJdbc(Jdbc value) {
        setJdbc(value);
        return this;
    }

    public Configuration withGenerator(Generator value) {
        setGenerator(value);
        return this;
    }

}
