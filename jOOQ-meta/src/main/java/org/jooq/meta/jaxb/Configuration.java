
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
 *         &lt;element name="logging" type="{http://www.jooq.org/xsd/jooq-codegen-3.13.0.xsd}Logging" minOccurs="0"/&gt;
 *         &lt;element name="onError" type="{http://www.jooq.org/xsd/jooq-codegen-3.13.0.xsd}OnError" minOccurs="0"/&gt;
 *         &lt;element name="jdbc" type="{http://www.jooq.org/xsd/jooq-codegen-3.13.0.xsd}Jdbc" minOccurs="0"/&gt;
 *         &lt;element name="generator" type="{http://www.jooq.org/xsd/jooq-codegen-3.13.0.xsd}Generator"/&gt;
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
public class Configuration implements Serializable, XMLAppendable
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
     */
    public Logging getLogging() {
        return logging;
    }

    /**
     * The logging configuration element specifies the code generation logging threshold.
     *
     */
    public void setLogging(Logging value) {
        this.logging = value;
    }

    /**
     * The action to be taken by the generator as the consequence of an encountered exception. Defaults to FAIL.
     *
     */
    public OnError getOnError() {
        return onError;
    }

    /**
     * The action to be taken by the generator as the consequence of an encountered exception. Defaults to FAIL.
     *
     */
    public void setOnError(OnError value) {
        this.onError = value;
    }

    /**
     * The JDBC configuration element contains information about how to set up the database connection used for source code generation.
     *
     */
    public Jdbc getJdbc() {
        return jdbc;
    }

    /**
     * The JDBC configuration element contains information about how to set up the database connection used for source code generation.
     *
     */
    public void setJdbc(Jdbc value) {
        this.jdbc = value;
    }

    /**
     * The GENERATOR configuration element contains information about source code generation itself.
     *
     */
    public Generator getGenerator() {
        return generator;
    }

    /**
     * The GENERATOR configuration element contains information about source code generation itself.
     *
     */
    public void setGenerator(Generator value) {
        this.generator = value;
    }

    /**
     * The logging configuration element specifies the code generation logging threshold.
     *
     */
    public Configuration withLogging(Logging value) {
        setLogging(value);
        return this;
    }

    /**
     * The action to be taken by the generator as the consequence of an encountered exception. Defaults to FAIL.
     *
     */
    public Configuration withOnError(OnError value) {
        setOnError(value);
        return this;
    }

    /**
     * The JDBC configuration element contains information about how to set up the database connection used for source code generation.
     *
     */
    public Configuration withJdbc(Jdbc value) {
        setJdbc(value);
        return this;
    }

    /**
     * The GENERATOR configuration element contains information about source code generation itself.
     *
     */
    public Configuration withGenerator(Generator value) {
        setGenerator(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("logging", logging);
        builder.append("onError", onError);
        builder.append("jdbc", jdbc);
        builder.append("generator", generator);
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
