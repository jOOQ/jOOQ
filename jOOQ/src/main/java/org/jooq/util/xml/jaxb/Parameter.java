







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Parameter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Parameter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="specific_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_package" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="specific_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ordinal_position" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="parameter_mode" type="{http://www.jooq.org/xsd/jooq-meta-3.11.0.xsd}ParameterMode"/&gt;
 *         &lt;element name="parameter_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="parameter_default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "Parameter", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Parameter implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(name = "specific_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificCatalog;
    @XmlElement(name = "specific_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificSchema;
    @XmlElement(name = "specific_package")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificPackage;
    @XmlElement(name = "specific_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String specificName;
    @XmlElement(name = "ordinal_position")
    protected int ordinalPosition;
    @XmlElement(name = "parameter_mode", required = true)
    @XmlSchemaType(name = "string")
    protected ParameterMode parameterMode;
    @XmlElement(name = "parameter_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String parameterName;
    @XmlElement(name = "data_type")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "parameter_default")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String parameterDefault;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    /**
     * Gets the value of the specificCatalog property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificCatalog() {
        return specificCatalog;
    }

    /**
     * Sets the value of the specificCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificCatalog(String value) {
        this.specificCatalog = value;
    }

    /**
     * Gets the value of the specificSchema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificSchema() {
        return specificSchema;
    }

    /**
     * Sets the value of the specificSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificSchema(String value) {
        this.specificSchema = value;
    }

    /**
     * Gets the value of the specificPackage property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificPackage() {
        return specificPackage;
    }

    /**
     * Sets the value of the specificPackage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificPackage(String value) {
        this.specificPackage = value;
    }

    /**
     * Gets the value of the specificName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSpecificName() {
        return specificName;
    }

    /**
     * Sets the value of the specificName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSpecificName(String value) {
        this.specificName = value;
    }

    /**
     * Gets the value of the ordinalPosition property.
     *
     */
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of the ordinalPosition property.
     *
     */
    public void setOrdinalPosition(int value) {
        this.ordinalPosition = value;
    }

    /**
     * Gets the value of the parameterMode property.
     *
     * @return
     *     possible object is
     *     {@link ParameterMode }
     *
     */
    public ParameterMode getParameterMode() {
        return parameterMode;
    }

    /**
     * Sets the value of the parameterMode property.
     *
     * @param value
     *     allowed object is
     *     {@link ParameterMode }
     *
     */
    public void setParameterMode(ParameterMode value) {
        this.parameterMode = value;
    }

    /**
     * Gets the value of the parameterName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Sets the value of the parameterName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParameterName(String value) {
        this.parameterName = value;
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
     * Gets the value of the parameterDefault property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParameterDefault() {
        return parameterDefault;
    }

    /**
     * Sets the value of the parameterDefault property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParameterDefault(String value) {
        this.parameterDefault = value;
    }

    /**
     * Gets the value of the comment property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setComment(String value) {
        this.comment = value;
    }

    public Parameter withSpecificCatalog(String value) {
        setSpecificCatalog(value);
        return this;
    }

    public Parameter withSpecificSchema(String value) {
        setSpecificSchema(value);
        return this;
    }

    public Parameter withSpecificPackage(String value) {
        setSpecificPackage(value);
        return this;
    }

    public Parameter withSpecificName(String value) {
        setSpecificName(value);
        return this;
    }

    public Parameter withOrdinalPosition(int value) {
        setOrdinalPosition(value);
        return this;
    }

    public Parameter withParameterMode(ParameterMode value) {
        setParameterMode(value);
        return this;
    }

    public Parameter withParameterName(String value) {
        setParameterName(value);
        return this;
    }

    public Parameter withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Parameter withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Parameter withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Parameter withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Parameter withParameterDefault(String value) {
        setParameterDefault(value);
        return this;
    }

    public Parameter withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (specificCatalog!= null) {
            sb.append("<specificCatalog>");
            sb.append(specificCatalog);
            sb.append("</specificCatalog>");
        }
        if (specificSchema!= null) {
            sb.append("<specificSchema>");
            sb.append(specificSchema);
            sb.append("</specificSchema>");
        }
        if (specificPackage!= null) {
            sb.append("<specificPackage>");
            sb.append(specificPackage);
            sb.append("</specificPackage>");
        }
        if (specificName!= null) {
            sb.append("<specificName>");
            sb.append(specificName);
            sb.append("</specificName>");
        }
        sb.append("<ordinalPosition>");
        sb.append(ordinalPosition);
        sb.append("</ordinalPosition>");
        if (parameterMode!= null) {
            sb.append("<parameterMode>");
            sb.append(parameterMode);
            sb.append("</parameterMode>");
        }
        if (parameterName!= null) {
            sb.append("<parameterName>");
            sb.append(parameterName);
            sb.append("</parameterName>");
        }
        if (dataType!= null) {
            sb.append("<dataType>");
            sb.append(dataType);
            sb.append("</dataType>");
        }
        if (characterMaximumLength!= null) {
            sb.append("<characterMaximumLength>");
            sb.append(characterMaximumLength);
            sb.append("</characterMaximumLength>");
        }
        if (numericPrecision!= null) {
            sb.append("<numericPrecision>");
            sb.append(numericPrecision);
            sb.append("</numericPrecision>");
        }
        if (numericScale!= null) {
            sb.append("<numericScale>");
            sb.append(numericScale);
            sb.append("</numericScale>");
        }
        if (parameterDefault!= null) {
            sb.append("<parameterDefault>");
            sb.append(parameterDefault);
            sb.append("</parameterDefault>");
        }
        if (comment!= null) {
            sb.append("<comment>");
            sb.append(comment);
            sb.append("</comment>");
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
        Parameter other = ((Parameter) that);
        if (specificCatalog == null) {
            if (other.specificCatalog!= null) {
                return false;
            }
        } else {
            if (!specificCatalog.equals(other.specificCatalog)) {
                return false;
            }
        }
        if (specificSchema == null) {
            if (other.specificSchema!= null) {
                return false;
            }
        } else {
            if (!specificSchema.equals(other.specificSchema)) {
                return false;
            }
        }
        if (specificPackage == null) {
            if (other.specificPackage!= null) {
                return false;
            }
        } else {
            if (!specificPackage.equals(other.specificPackage)) {
                return false;
            }
        }
        if (specificName == null) {
            if (other.specificName!= null) {
                return false;
            }
        } else {
            if (!specificName.equals(other.specificName)) {
                return false;
            }
        }
        if (ordinalPosition!= other.ordinalPosition) {
            return false;
        }
        if (parameterMode == null) {
            if (other.parameterMode!= null) {
                return false;
            }
        } else {
            if (!parameterMode.equals(other.parameterMode)) {
                return false;
            }
        }
        if (parameterName == null) {
            if (other.parameterName!= null) {
                return false;
            }
        } else {
            if (!parameterName.equals(other.parameterName)) {
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
        if (parameterDefault == null) {
            if (other.parameterDefault!= null) {
                return false;
            }
        } else {
            if (!parameterDefault.equals(other.parameterDefault)) {
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
        result = ((prime*result)+((specificCatalog == null)? 0 :specificCatalog.hashCode()));
        result = ((prime*result)+((specificSchema == null)? 0 :specificSchema.hashCode()));
        result = ((prime*result)+((specificPackage == null)? 0 :specificPackage.hashCode()));
        result = ((prime*result)+((specificName == null)? 0 :specificName.hashCode()));
        result = ((prime*result)+ ordinalPosition);
        result = ((prime*result)+((parameterMode == null)? 0 :parameterMode.hashCode()));
        result = ((prime*result)+((parameterName == null)? 0 :parameterName.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((parameterDefault == null)? 0 :parameterDefault.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
