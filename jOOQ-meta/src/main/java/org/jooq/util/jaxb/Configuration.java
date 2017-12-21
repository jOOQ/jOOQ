







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="logging" type="{http://www.jooq.org/xsd/jooq-codegen-3.11.0.xsd}Logging" minOccurs="0"/&gt;
 *         &lt;element name="jdbc" type="{http://www.jooq.org/xsd/jooq-codegen-3.11.0.xsd}Jdbc" minOccurs="0"/&gt;
 *         &lt;element name="generator" type="{http://www.jooq.org/xsd/jooq-codegen-3.11.0.xsd}Generator"/&gt;
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

    private final static long serialVersionUID = 31100L;
    @XmlSchemaType(name = "string")
    protected Logging logging;
    protected Jdbc jdbc;
    @XmlElement(required = true)
    protected Generator generator;

    /**
     * The logging configuration element specifies the code generation logging threshold.
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
     * Sets the value of the logging property.
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
     * The JDBC configuration element contains information about how to set up the database connection used for source code generation.
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
     * Sets the value of the jdbc property.
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
     * The GENERATOR configuration element contains information about source code generation itself.
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
     * Sets the value of the generator property.
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
