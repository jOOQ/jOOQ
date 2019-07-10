
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for ElementType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="object_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="object_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="object_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="object_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="udt_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElementType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ElementType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(name = "object_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectCatalog;
    @XmlElement(name = "object_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectSchema;
    @XmlElement(name = "object_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectName;
    @XmlElement(name = "object_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectType;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "udt_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtCatalog;
    @XmlElement(name = "udt_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtSchema;
    @XmlElement(name = "udt_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtName;

    /**
     * Gets the value of the objectCatalog property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getObjectCatalog() {
        return objectCatalog;
    }

    /**
     * Sets the value of the objectCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setObjectCatalog(String value) {
        this.objectCatalog = value;
    }

    /**
     * Gets the value of the objectSchema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getObjectSchema() {
        return objectSchema;
    }

    /**
     * Sets the value of the objectSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setObjectSchema(String value) {
        this.objectSchema = value;
    }

    /**
     * Gets the value of the objectName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the value of the objectName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setObjectName(String value) {
        this.objectName = value;
    }

    /**
     * Gets the value of the objectType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setObjectType(String value) {
        this.objectType = value;
    }

    /**
     * Gets the value of the dataType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the characterMaximumLength property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    /**
     * Sets the value of the characterMaximumLength property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCharacterMaximumLength(Integer value) {
        this.characterMaximumLength = value;
    }

    /**
     * Gets the value of the numericPrecision property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    /**
     * Sets the value of the numericPrecision property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumericPrecision(Integer value) {
        this.numericPrecision = value;
    }

    /**
     * Gets the value of the numericScale property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNumericScale() {
        return numericScale;
    }

    /**
     * Sets the value of the numericScale property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNumericScale(Integer value) {
        this.numericScale = value;
    }

    /**
     * Gets the value of the udtCatalog property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUdtCatalog() {
        return udtCatalog;
    }

    /**
     * Sets the value of the udtCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUdtCatalog(String value) {
        this.udtCatalog = value;
    }

    /**
     * Gets the value of the udtSchema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUdtSchema() {
        return udtSchema;
    }

    /**
     * Sets the value of the udtSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUdtSchema(String value) {
        this.udtSchema = value;
    }

    /**
     * Gets the value of the udtName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUdtName() {
        return udtName;
    }

    /**
     * Sets the value of the udtName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUdtName(String value) {
        this.udtName = value;
    }

    public ElementType withObjectCatalog(String value) {
        setObjectCatalog(value);
        return this;
    }

    public ElementType withObjectSchema(String value) {
        setObjectSchema(value);
        return this;
    }

    public ElementType withObjectName(String value) {
        setObjectName(value);
        return this;
    }

    public ElementType withObjectType(String value) {
        setObjectType(value);
        return this;
    }

    public ElementType withDataType(String value) {
        setDataType(value);
        return this;
    }

    public ElementType withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public ElementType withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public ElementType withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public ElementType withUdtCatalog(String value) {
        setUdtCatalog(value);
        return this;
    }

    public ElementType withUdtSchema(String value) {
        setUdtSchema(value);
        return this;
    }

    public ElementType withUdtName(String value) {
        setUdtName(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("object_catalog", objectCatalog);
        builder.append("object_schema", objectSchema);
        builder.append("object_name", objectName);
        builder.append("object_type", objectType);
        builder.append("data_type", dataType);
        builder.append("character_maximum_length", characterMaximumLength);
        builder.append("numeric_precision", numericPrecision);
        builder.append("numeric_scale", numericScale);
        builder.append("udt_catalog", udtCatalog);
        builder.append("udt_schema", udtSchema);
        builder.append("udt_name", udtName);
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
        ElementType other = ((ElementType) that);
        if (objectCatalog == null) {
            if (other.objectCatalog!= null) {
                return false;
            }
        } else {
            if (!objectCatalog.equals(other.objectCatalog)) {
                return false;
            }
        }
        if (objectSchema == null) {
            if (other.objectSchema!= null) {
                return false;
            }
        } else {
            if (!objectSchema.equals(other.objectSchema)) {
                return false;
            }
        }
        if (objectName == null) {
            if (other.objectName!= null) {
                return false;
            }
        } else {
            if (!objectName.equals(other.objectName)) {
                return false;
            }
        }
        if (objectType == null) {
            if (other.objectType!= null) {
                return false;
            }
        } else {
            if (!objectType.equals(other.objectType)) {
                return false;
            }
        }
        if (dataType == null) {
            if (other.dataType!= null) {
                return false;
            }
        } else {
            if (!dataType.equals(other.dataType)) {
                return false;
            }
        }
        if (characterMaximumLength == null) {
            if (other.characterMaximumLength!= null) {
                return false;
            }
        } else {
            if (!characterMaximumLength.equals(other.characterMaximumLength)) {
                return false;
            }
        }
        if (numericPrecision == null) {
            if (other.numericPrecision!= null) {
                return false;
            }
        } else {
            if (!numericPrecision.equals(other.numericPrecision)) {
                return false;
            }
        }
        if (numericScale == null) {
            if (other.numericScale!= null) {
                return false;
            }
        } else {
            if (!numericScale.equals(other.numericScale)) {
                return false;
            }
        }
        if (udtCatalog == null) {
            if (other.udtCatalog!= null) {
                return false;
            }
        } else {
            if (!udtCatalog.equals(other.udtCatalog)) {
                return false;
            }
        }
        if (udtSchema == null) {
            if (other.udtSchema!= null) {
                return false;
            }
        } else {
            if (!udtSchema.equals(other.udtSchema)) {
                return false;
            }
        }
        if (udtName == null) {
            if (other.udtName!= null) {
                return false;
            }
        } else {
            if (!udtName.equals(other.udtName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((objectCatalog == null)? 0 :objectCatalog.hashCode()));
        result = ((prime*result)+((objectSchema == null)? 0 :objectSchema.hashCode()));
        result = ((prime*result)+((objectName == null)? 0 :objectName.hashCode()));
        result = ((prime*result)+((objectType == null)? 0 :objectType.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((udtCatalog == null)? 0 :udtCatalog.hashCode()));
        result = ((prime*result)+((udtSchema == null)? 0 :udtSchema.hashCode()));
        result = ((prime*result)+((udtName == null)? 0 :udtName.hashCode()));
        return result;
    }

}
