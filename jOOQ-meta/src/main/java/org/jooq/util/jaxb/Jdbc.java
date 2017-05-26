







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
 * JDBC connection configuration.
 *
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
     * The JDBC driver class.
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
     * The JDBC connection URL.
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
     * @deprecated Use database schema configuration elements instead.
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
     * The JDBC connection user. Be sure this user has all required GRANTs to the dictionary views/tables to generate the desired artefacts
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
     * Just a synonym for "user" to be compatible with other Maven plugins.
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
     * The JDBC connection password.
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
