
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for Attribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Attribute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="udt_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="attribute_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ordinal_position" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="attribute_default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="attribute_udt_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="attribute_udt_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="attribute_udt_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attribute", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Attribute implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32100L;
    @XmlElement(name = "udt_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtCatalog;
    @XmlElement(name = "udt_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtSchema;
    @XmlElement(name = "udt_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtName;
    @XmlElement(name = "attribute_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String attributeName;
    @XmlElement(name = "ordinal_position")
    protected int ordinalPosition;
    @XmlElement(name = "attribute_default")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String attributeDefault;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "attribute_udt_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String attributeUdtCatalog;
    @XmlElement(name = "attribute_udt_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String attributeUdtSchema;
    @XmlElement(name = "attribute_udt_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String attributeUdtName;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    public String getUdtCatalog() {
        return udtCatalog;
    }

    public void setUdtCatalog(String value) {
        this.udtCatalog = value;
    }

    public String getUdtSchema() {
        return udtSchema;
    }

    public void setUdtSchema(String value) {
        this.udtSchema = value;
    }

    public String getUdtName() {
        return udtName;
    }

    public void setUdtName(String value) {
        this.udtName = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String value) {
        this.attributeName = value;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int value) {
        this.ordinalPosition = value;
    }

    public String getAttributeDefault() {
        return attributeDefault;
    }

    public void setAttributeDefault(String value) {
        this.attributeDefault = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String value) {
        this.dataType = value;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer value) {
        this.characterMaximumLength = value;
    }

    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(Integer value) {
        this.numericPrecision = value;
    }

    public Integer getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(Integer value) {
        this.numericScale = value;
    }

    public String getAttributeUdtCatalog() {
        return attributeUdtCatalog;
    }

    public void setAttributeUdtCatalog(String value) {
        this.attributeUdtCatalog = value;
    }

    public String getAttributeUdtSchema() {
        return attributeUdtSchema;
    }

    public void setAttributeUdtSchema(String value) {
        this.attributeUdtSchema = value;
    }

    public String getAttributeUdtName() {
        return attributeUdtName;
    }

    public void setAttributeUdtName(String value) {
        this.attributeUdtName = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public Attribute withUdtCatalog(String value) {
        setUdtCatalog(value);
        return this;
    }

    public Attribute withUdtSchema(String value) {
        setUdtSchema(value);
        return this;
    }

    public Attribute withUdtName(String value) {
        setUdtName(value);
        return this;
    }

    public Attribute withAttributeName(String value) {
        setAttributeName(value);
        return this;
    }

    public Attribute withOrdinalPosition(int value) {
        setOrdinalPosition(value);
        return this;
    }

    public Attribute withAttributeDefault(String value) {
        setAttributeDefault(value);
        return this;
    }

    public Attribute withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Attribute withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Attribute withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Attribute withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Attribute withAttributeUdtCatalog(String value) {
        setAttributeUdtCatalog(value);
        return this;
    }

    public Attribute withAttributeUdtSchema(String value) {
        setAttributeUdtSchema(value);
        return this;
    }

    public Attribute withAttributeUdtName(String value) {
        setAttributeUdtName(value);
        return this;
    }

    public Attribute withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("udt_catalog", udtCatalog);
        builder.append("udt_schema", udtSchema);
        builder.append("udt_name", udtName);
        builder.append("attribute_name", attributeName);
        builder.append("ordinal_position", ordinalPosition);
        builder.append("attribute_default", attributeDefault);
        builder.append("data_type", dataType);
        builder.append("character_maximum_length", characterMaximumLength);
        builder.append("numeric_precision", numericPrecision);
        builder.append("numeric_scale", numericScale);
        builder.append("attribute_udt_catalog", attributeUdtCatalog);
        builder.append("attribute_udt_schema", attributeUdtSchema);
        builder.append("attribute_udt_name", attributeUdtName);
        builder.append("comment", comment);
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
        Attribute other = ((Attribute) that);
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
        if (attributeName == null) {
            if (other.attributeName!= null) {
                return false;
            }
        } else {
            if (!attributeName.equals(other.attributeName)) {
                return false;
            }
        }
        if (ordinalPosition!= other.ordinalPosition) {
            return false;
        }
        if (attributeDefault == null) {
            if (other.attributeDefault!= null) {
                return false;
            }
        } else {
            if (!attributeDefault.equals(other.attributeDefault)) {
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
        if (attributeUdtCatalog == null) {
            if (other.attributeUdtCatalog!= null) {
                return false;
            }
        } else {
            if (!attributeUdtCatalog.equals(other.attributeUdtCatalog)) {
                return false;
            }
        }
        if (attributeUdtSchema == null) {
            if (other.attributeUdtSchema!= null) {
                return false;
            }
        } else {
            if (!attributeUdtSchema.equals(other.attributeUdtSchema)) {
                return false;
            }
        }
        if (attributeUdtName == null) {
            if (other.attributeUdtName!= null) {
                return false;
            }
        } else {
            if (!attributeUdtName.equals(other.attributeUdtName)) {
                return false;
            }
        }
        if (comment == null) {
            if (other.comment!= null) {
                return false;
            }
        } else {
            if (!comment.equals(other.comment)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((udtCatalog == null)? 0 :udtCatalog.hashCode()));
        result = ((prime*result)+((udtSchema == null)? 0 :udtSchema.hashCode()));
        result = ((prime*result)+((udtName == null)? 0 :udtName.hashCode()));
        result = ((prime*result)+((attributeName == null)? 0 :attributeName.hashCode()));
        result = ((prime*result)+ ordinalPosition);
        result = ((prime*result)+((attributeDefault == null)? 0 :attributeDefault.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((attributeUdtCatalog == null)? 0 :attributeUdtCatalog.hashCode()));
        result = ((prime*result)+((attributeUdtSchema == null)? 0 :attributeUdtSchema.hashCode()));
        result = ((prime*result)+((attributeUdtName == null)? 0 :attributeUdtName.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
