







package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Sequence complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Sequence"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="sequence_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sequence_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sequence_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
@XmlType(name = "Sequence", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Sequence implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(name = "sequence_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sequenceCatalog;
    @XmlElement(name = "sequence_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sequenceSchema;
    @XmlElement(name = "sequence_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sequenceName;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    /**
     * Gets the value of the sequenceCatalog property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSequenceCatalog() {
        return sequenceCatalog;
    }

    /**
     * Sets the value of the sequenceCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSequenceCatalog(String value) {
        this.sequenceCatalog = value;
    }

    /**
     * Gets the value of the sequenceSchema property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSequenceSchema() {
        return sequenceSchema;
    }

    /**
     * Sets the value of the sequenceSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSequenceSchema(String value) {
        this.sequenceSchema = value;
    }

    /**
     * Gets the value of the sequenceName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSequenceName() {
        return sequenceName;
    }

    /**
     * Sets the value of the sequenceName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSequenceName(String value) {
        this.sequenceName = value;
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

    public Sequence withSequenceCatalog(String value) {
        setSequenceCatalog(value);
        return this;
    }

    public Sequence withSequenceSchema(String value) {
        setSequenceSchema(value);
        return this;
    }

    public Sequence withSequenceName(String value) {
        setSequenceName(value);
        return this;
    }

    public Sequence withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Sequence withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Sequence withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Sequence withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Sequence withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (sequenceCatalog!= null) {
            sb.append("<sequenceCatalog>");
            sb.append(sequenceCatalog);
            sb.append("</sequenceCatalog>");
        }
        if (sequenceSchema!= null) {
            sb.append("<sequenceSchema>");
            sb.append(sequenceSchema);
            sb.append("</sequenceSchema>");
        }
        if (sequenceName!= null) {
            sb.append("<sequenceName>");
            sb.append(sequenceName);
            sb.append("</sequenceName>");
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
        Sequence other = ((Sequence) that);
        if (sequenceCatalog == null) {
            if (other.sequenceCatalog!= null) {
                return false;
            }
        } else {
            if (!sequenceCatalog.equals(other.sequenceCatalog)) {
                return false;
            }
        }
        if (sequenceSchema == null) {
            if (other.sequenceSchema!= null) {
                return false;
            }
        } else {
            if (!sequenceSchema.equals(other.sequenceSchema)) {
                return false;
            }
        }
        if (sequenceName == null) {
            if (other.sequenceName!= null) {
                return false;
            }
        } else {
            if (!sequenceName.equals(other.sequenceName)) {
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
        result = ((prime*result)+((sequenceCatalog == null)? 0 :sequenceCatalog.hashCode()));
        result = ((prime*result)+((sequenceSchema == null)? 0 :sequenceSchema.hashCode()));
        result = ((prime*result)+((sequenceName == null)? 0 :sequenceName.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
