
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
public class Sequence implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
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

    public String getSequenceCatalog() {
        return sequenceCatalog;
    }

    public void setSequenceCatalog(String value) {
        this.sequenceCatalog = value;
    }

    public String getSequenceSchema() {
        return sequenceSchema;
    }

    public void setSequenceSchema(String value) {
        this.sequenceSchema = value;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String value) {
        this.sequenceName = value;
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

    public String getComment() {
        return comment;
    }

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
    public final void appendTo(XMLBuilder builder) {
        builder.append("sequence_catalog", sequenceCatalog);
        builder.append("sequence_schema", sequenceSchema);
        builder.append("sequence_name", sequenceName);
        builder.append("data_type", dataType);
        builder.append("character_maximum_length", characterMaximumLength);
        builder.append("numeric_precision", numericPrecision);
        builder.append("numeric_scale", numericScale);
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
