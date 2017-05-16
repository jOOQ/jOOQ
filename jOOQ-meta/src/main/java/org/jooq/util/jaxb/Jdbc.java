







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
 * <p>Java class for Jdbc complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Jdbc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="driver" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="properties" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Properties" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Jdbc", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Jdbc implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String driver;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String url;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schema;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String user;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String username;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String password;
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<Property> properties;

    /**
     * Gets the value of the driver property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets the value of the driver property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDriver(String value) {
        this.driver = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the schema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * Gets the value of the user property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the username property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPassword(String value) {
        this.password = value;
    }

    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public Jdbc withDriver(String value) {
        setDriver(value);
        return this;
    }

    public Jdbc withUrl(String value) {
        setUrl(value);
        return this;
    }

    public Jdbc withSchema(String value) {
        setSchema(value);
        return this;
    }

    public Jdbc withUser(String value) {
        setUser(value);
        return this;
    }

    public Jdbc withUsername(String value) {
        setUsername(value);
        return this;
    }

    public Jdbc withPassword(String value) {
        setPassword(value);
        return this;
    }

    public Jdbc withProperties(Property... values) {
        if (values!= null) {
            for (Property value: values) {
                getProperties().add(value);
            }
        }
        return this;
    }

    public Jdbc withProperties(Collection<Property> values) {
        if (values!= null) {
            getProperties().addAll(values);
        }
        return this;
    }

    public Jdbc withProperties(List<Property> properties) {
        setProperties(properties);
        return this;
    }

}
