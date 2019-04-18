
package org.jooq.meta.jaxb;

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
 *         &lt;element name="logging" type="{http://www.jooq.org/xsd/jooq-codegen-3.12.0.xsd}Logging" minOccurs="0"/&gt;
 *         &lt;element name="onError" type="{http://www.jooq.org/xsd/jooq-codegen-3.12.0.xsd}OnError" minOccurs="0"/&gt;
 *         &lt;element name="jdbc" type="{http://www.jooq.org/xsd/jooq-codegen-3.12.0.xsd}Jdbc" minOccurs="0"/&gt;
 *         &lt;element name="generator" type="{http://www.jooq.org/xsd/jooq-codegen-3.12.0.xsd}Generator"/&gt;
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

    private final static long serialVersionUID = 31200L;
    @XmlSchemaType(name = "string")
    protected Logging logging;
    @XmlElement(defaultValue = "FAIL")
    @XmlSchemaType(name = "string")
    protected OnError onError = OnError.FAIL;
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
     * The action to be taken by the generator as the consequence of an encountered exception. Defaults to FAIL.
     *
     * @return
     *     possible object is
     *     {@link OnError }
     *
     */
    public OnError getOnError() {
        return onError;
    }

    /**
     * Sets the value of the onError property.
     *
     * @param value
     *     allowed object is
     *     {@link OnError }
     *
     */
    public void setOnError(OnError value) {
        this.onError = value;
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

    public Configuration withOnError(OnError value) {
        setOnError(value);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (logging!= null) {
            sb.append("<logging>");
            sb.append(logging);
            sb.append("</logging>");
        }
        if (onError!= null) {
            sb.append("<onError>");
            sb.append(onError);
            sb.append("</onError>");
        }
        if (jdbc!= null) {
            sb.append("<jdbc>");
            sb.append(jdbc);
            sb.append("</jdbc>");
        }
        if (generator!= null) {
            sb.append("<generator>");
            sb.append(generator);
            sb.append("</generator>");
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
        Configuration other = ((Configuration) that);
        if (logging == null) {
            if (other.logging!= null) {
                return false;
            }
        } else {
            if (!logging.equals(other.logging)) {
                return false;
            }
        }
        if (onError == null) {
            if (other.onError!= null) {
                return false;
            }
        } else {
            if (!onError.equals(other.onError)) {
                return false;
            }
        }
        if (jdbc == null) {
            if (other.jdbc!= null) {
                return false;
            }
        } else {
            if (!jdbc.equals(other.jdbc)) {
                return false;
            }
        }
        if (generator == null) {
            if (other.generator!= null) {
                return false;
            }
        } else {
            if (!generator.equals(other.generator)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((logging == null)? 0 :logging.hashCode()));
        result = ((prime*result)+((onError == null)? 0 :onError.hashCode()));
        result = ((prime*result)+((jdbc == null)? 0 :jdbc.hashCode()));
        result = ((prime*result)+((generator == null)? 0 :generator.hashCode()));
        return result;
    }

}
