







package org.jooq.meta.jaxb;

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

    private final static long serialVersionUID = 31100L;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (driver!= null) {
            sb.append("<driver>");
            sb.append(driver);
            sb.append("</driver>");
        }
        if (url!= null) {
            sb.append("<url>");
            sb.append(url);
            sb.append("</url>");
        }
        if (schema!= null) {
            sb.append("<schema>");
            sb.append(schema);
            sb.append("</schema>");
        }
        if (user!= null) {
            sb.append("<user>");
            sb.append(user);
            sb.append("</user>");
        }
        if (username!= null) {
            sb.append("<username>");
            sb.append(username);
            sb.append("</username>");
        }
        if (password!= null) {
            sb.append("<password>");
            sb.append(password);
            sb.append("</password>");
        }
        if (properties!= null) {
            sb.append("<properties>");
            sb.append(properties);
            sb.append("</properties>");
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
        Jdbc other = ((Jdbc) that);
        if (driver == null) {
            if (other.driver!= null) {
                return false;
            }
        } else {
            if (!driver.equals(other.driver)) {
                return false;
            }
        }
        if (url == null) {
            if (other.url!= null) {
                return false;
            }
        } else {
            if (!url.equals(other.url)) {
                return false;
            }
        }
        if (schema == null) {
            if (other.schema!= null) {
                return false;
            }
        } else {
            if (!schema.equals(other.schema)) {
                return false;
            }
        }
        if (user == null) {
            if (other.user!= null) {
                return false;
            }
        } else {
            if (!user.equals(other.user)) {
                return false;
            }
        }
        if (username == null) {
            if (other.username!= null) {
                return false;
            }
        } else {
            if (!username.equals(other.username)) {
                return false;
            }
        }
        if (password == null) {
            if (other.password!= null) {
                return false;
            }
        } else {
            if (!password.equals(other.password)) {
                return false;
            }
        }
        if (properties == null) {
            if (other.properties!= null) {
                return false;
            }
        } else {
            if (!properties.equals(other.properties)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((driver == null)? 0 :driver.hashCode()));
        result = ((prime*result)+((url == null)? 0 :url.hashCode()));
        result = ((prime*result)+((schema == null)? 0 :schema.hashCode()));
        result = ((prime*result)+((user == null)? 0 :user.hashCode()));
        result = ((prime*result)+((username == null)? 0 :username.hashCode()));
        result = ((prime*result)+((password == null)? 0 :password.hashCode()));
        result = ((prime*result)+((properties == null)? 0 :properties.hashCode()));
        return result;
    }

}
