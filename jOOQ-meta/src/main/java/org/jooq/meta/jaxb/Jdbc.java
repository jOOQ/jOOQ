
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
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
public class Jdbc implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
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
    protected Boolean autoCommit;
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<Property> properties;

    /**
     * The JDBC driver class.
     *
     */
    public String getDriver() {
        return driver;
    }

    /**
     * The JDBC driver class.
     *
     */
    public void setDriver(String value) {
        this.driver = value;
    }

    /**
     * The JDBC connection URL.
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * The JDBC connection URL.
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * @deprecated Use database schema configuration elements instead.
     *
     */
    @Deprecated
    public String getSchema() {
        return schema;
    }

    /**
     * @deprecated Use database schema configuration elements instead.
     *
     */
    @Deprecated
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The JDBC connection user. Be sure this user has all required GRANTs to the dictionary views/tables to generate the desired artefacts
     *
     */
    public String getUser() {
        return user;
    }

    /**
     * The JDBC connection user. Be sure this user has all required GRANTs to the dictionary views/tables to generate the desired artefacts
     *
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Just a synonym for "user" to be compatible with other Maven plugins.
     *
     */
    public String getUsername() {
        return username;
    }

    /**
     * Just a synonym for "user" to be compatible with other Maven plugins.
     *
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * The JDBC connection password.
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * The JDBC connection password.
     *
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * The value of the JDBC autocommit flag. The flag is not set by default, i.e. it keeps the default provided to jOOQ.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isAutoCommit() {
        return autoCommit;
    }

    /**
     * Sets the value of the autoCommit property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setAutoCommit(Boolean value) {
        this.autoCommit = value;
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

    /**
     * The JDBC driver class.
     *
     */
    public Jdbc withDriver(String value) {
        setDriver(value);
        return this;
    }

    /**
     * The JDBC connection URL.
     *
     */
    public Jdbc withUrl(String value) {
        setUrl(value);
        return this;
    }

    /**
     * @deprecated Use database schema configuration elements instead.
     *
     */
    @Deprecated
    public Jdbc withSchema(String value) {
        setSchema(value);
        return this;
    }

    /**
     * The JDBC connection user. Be sure this user has all required GRANTs to the dictionary views/tables to generate the desired artefacts
     *
     */
    public Jdbc withUser(String value) {
        setUser(value);
        return this;
    }

    /**
     * Just a synonym for "user" to be compatible with other Maven plugins.
     *
     */
    public Jdbc withUsername(String value) {
        setUsername(value);
        return this;
    }

    /**
     * The JDBC connection password.
     *
     */
    public Jdbc withPassword(String value) {
        setPassword(value);
        return this;
    }

    public Jdbc withAutoCommit(Boolean value) {
        setAutoCommit(value);
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
    public final void appendTo(XMLBuilder builder) {
        builder.append("driver", driver);
        builder.append("url", url);
        builder.append("schema", schema);
        builder.append("user", user);
        builder.append("username", username);
        builder.append("password", password);
        builder.append("autoCommit", autoCommit);
        builder.append("properties", "property", properties);
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
        if (autoCommit == null) {
            if (other.autoCommit!= null) {
                return false;
            }
        } else {
            if (!autoCommit.equals(other.autoCommit)) {
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
        result = ((prime*result)+((autoCommit == null)? 0 :autoCommit.hashCode()));
        result = ((prime*result)+((properties == null)? 0 :properties.hashCode()));
        return result;
    }

}
