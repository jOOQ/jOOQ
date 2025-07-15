
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for UserDefinedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserDefinedType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="user_defined_type_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="user_defined_type_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="user_defined_type_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="user_defined_type_category" type="{http://www.jooq.org/xsd/jooq-meta-3.21.0.xsd}UserDefinedTypeCategory" minOccurs="0"/&gt;
 *         &lt;element name="is_instantiable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="is_final" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserDefinedType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class UserDefinedType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32100L;
    @XmlElement(name = "user_defined_type_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userDefinedTypeCatalog;
    @XmlElement(name = "user_defined_type_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userDefinedTypeSchema;
    @XmlElement(name = "user_defined_type_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userDefinedTypeName;
    @XmlElement(name = "user_defined_type_category")
    @XmlSchemaType(name = "string")
    protected UserDefinedTypeCategory userDefinedTypeCategory;
    @XmlElement(name = "is_instantiable")
    protected Boolean isInstantiable;
    @XmlElement(name = "is_final")
    protected Boolean isFinal;

    public String getUserDefinedTypeCatalog() {
        return userDefinedTypeCatalog;
    }

    public void setUserDefinedTypeCatalog(String value) {
        this.userDefinedTypeCatalog = value;
    }

    public String getUserDefinedTypeSchema() {
        return userDefinedTypeSchema;
    }

    public void setUserDefinedTypeSchema(String value) {
        this.userDefinedTypeSchema = value;
    }

    public String getUserDefinedTypeName() {
        return userDefinedTypeName;
    }

    public void setUserDefinedTypeName(String value) {
        this.userDefinedTypeName = value;
    }

    public UserDefinedTypeCategory getUserDefinedTypeCategory() {
        return userDefinedTypeCategory;
    }

    public void setUserDefinedTypeCategory(UserDefinedTypeCategory value) {
        this.userDefinedTypeCategory = value;
    }

    /**
     * Gets the value of the isInstantiable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInstantiable() {
        return isInstantiable;
    }

    /**
     * Sets the value of the isInstantiable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInstantiable(Boolean value) {
        this.isInstantiable = value;
    }

    /**
     * Gets the value of the isFinal property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFinal() {
        return isFinal;
    }

    /**
     * Sets the value of the isFinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFinal(Boolean value) {
        this.isFinal = value;
    }

    public UserDefinedType withUserDefinedTypeCatalog(String value) {
        setUserDefinedTypeCatalog(value);
        return this;
    }

    public UserDefinedType withUserDefinedTypeSchema(String value) {
        setUserDefinedTypeSchema(value);
        return this;
    }

    public UserDefinedType withUserDefinedTypeName(String value) {
        setUserDefinedTypeName(value);
        return this;
    }

    public UserDefinedType withUserDefinedTypeCategory(UserDefinedTypeCategory value) {
        setUserDefinedTypeCategory(value);
        return this;
    }

    public UserDefinedType withIsInstantiable(Boolean value) {
        setIsInstantiable(value);
        return this;
    }

    public UserDefinedType withIsFinal(Boolean value) {
        setIsFinal(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("user_defined_type_catalog", userDefinedTypeCatalog);
        builder.append("user_defined_type_schema", userDefinedTypeSchema);
        builder.append("user_defined_type_name", userDefinedTypeName);
        builder.append("user_defined_type_category", userDefinedTypeCategory);
        builder.append("is_instantiable", isInstantiable);
        builder.append("is_final", isFinal);
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
        UserDefinedType other = ((UserDefinedType) that);
        if (userDefinedTypeCatalog == null) {
            if (other.userDefinedTypeCatalog!= null) {
                return false;
            }
        } else {
            if (!userDefinedTypeCatalog.equals(other.userDefinedTypeCatalog)) {
                return false;
            }
        }
        if (userDefinedTypeSchema == null) {
            if (other.userDefinedTypeSchema!= null) {
                return false;
            }
        } else {
            if (!userDefinedTypeSchema.equals(other.userDefinedTypeSchema)) {
                return false;
            }
        }
        if (userDefinedTypeName == null) {
            if (other.userDefinedTypeName!= null) {
                return false;
            }
        } else {
            if (!userDefinedTypeName.equals(other.userDefinedTypeName)) {
                return false;
            }
        }
        if (userDefinedTypeCategory == null) {
            if (other.userDefinedTypeCategory!= null) {
                return false;
            }
        } else {
            if (!userDefinedTypeCategory.equals(other.userDefinedTypeCategory)) {
                return false;
            }
        }
        if (isInstantiable == null) {
            if (other.isInstantiable!= null) {
                return false;
            }
        } else {
            if (!isInstantiable.equals(other.isInstantiable)) {
                return false;
            }
        }
        if (isFinal == null) {
            if (other.isFinal!= null) {
                return false;
            }
        } else {
            if (!isFinal.equals(other.isFinal)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((userDefinedTypeCatalog == null)? 0 :userDefinedTypeCatalog.hashCode()));
        result = ((prime*result)+((userDefinedTypeSchema == null)? 0 :userDefinedTypeSchema.hashCode()));
        result = ((prime*result)+((userDefinedTypeName == null)? 0 :userDefinedTypeName.hashCode()));
        result = ((prime*result)+((userDefinedTypeCategory == null)? 0 :userDefinedTypeCategory.hashCode()));
        result = ((prime*result)+((isInstantiable == null)? 0 :isInstantiable.hashCode()));
        result = ((prime*result)+((isFinal == null)? 0 :isFinal.hashCode()));
        return result;
    }

}
