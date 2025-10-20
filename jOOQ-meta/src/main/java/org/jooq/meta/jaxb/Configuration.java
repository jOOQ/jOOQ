
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
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
 *         &lt;element name="logging" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.35.xsd}Logging" minOccurs="0"/&gt;
 *         &lt;element name="onError" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.35.xsd}OnError" minOccurs="0"/&gt;
 *         &lt;element name="onUnused" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.35.xsd}OnError" minOccurs="0"/&gt;
 *         &lt;element name="jdbc" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.35.xsd}Jdbc" minOccurs="0"/&gt;
 *         &lt;element name="generator" type="{http://www.jooq.org/xsd/jooq-codegen-3.18.35.xsd}Generator"/&gt;
 *         &lt;element name="basedir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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

    private final static long serialVersionUID = 31835L;
    @XmlSchemaType(name = "string")
    protected Logging logging;
    @XmlElement(defaultValue = "FAIL")
    @XmlSchemaType(name = "string")
    protected OnError onError = OnError.FAIL;
    @XmlElement(defaultValue = "LOG")
    @XmlSchemaType(name = "string")
    protected OnError onUnused = OnError.LOG;
    protected Jdbc jdbc;
    @XmlElement(required = true)
    protected Generator generator;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String basedir;

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
     * The action to be taken by the generator as the consequence of unused objects being encountered. Defaults to LOG.
     * 
     */
    public OnError getOnUnused() {
        return onUnused;
    }

    /**
     * The action to be taken by the generator as the consequence of unused objects being encountered. Defaults to LOG.
     * 
     */
    public void setOnUnused(OnError value) {
        this.onUnused = value;
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
     * The base directory that should be used instead of the JVM's working directory, to resolve all relative paths.
     * 
     */
    public String getBasedir() {
        return basedir;
    }

    /**
     * The base directory that should be used instead of the JVM's working directory, to resolve all relative paths.
     * 
     */
    public void setBasedir(String value) {
        this.basedir = value;
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
     * The action to be taken by the generator as the consequence of unused objects being encountered. Defaults to LOG.
     * 
     */
    public Configuration withOnUnused(OnError value) {
        setOnUnused(value);
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

    /**
     * The base directory that should be used instead of the JVM's working directory, to resolve all relative paths.
     * 
     */
    public Configuration withBasedir(String value) {
        setBasedir(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("logging", logging);
        builder.append("onError", onError);
        builder.append("onUnused", onUnused);
        builder.append("jdbc", jdbc);
        builder.append("generator", generator);
        builder.append("basedir", basedir);
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
        if (onUnused == null) {
            if (other.onUnused!= null) {
                return false;
            }
        } else {
            if (!onUnused.equals(other.onUnused)) {
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
        if (basedir == null) {
            if (other.basedir!= null) {
                return false;
            }
        } else {
            if (!basedir.equals(other.basedir)) {
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
        result = ((prime*result)+((onUnused == null)? 0 :onUnused.hashCode()));
        result = ((prime*result)+((jdbc == null)? 0 :jdbc.hashCode()));
        result = ((prime*result)+((generator == null)? 0 :generator.hashCode()));
        result = ((prime*result)+((basedir == null)? 0 :basedir.hashCode()));
        return result;
    }

}
