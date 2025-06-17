
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
 * <p>Java class for Synonym complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Synonym"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="synonym_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="synonym_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="synonym_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="object_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="object_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="object_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="is_public" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "Synonym", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Synonym implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32100L;
    @XmlElement(name = "synonym_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String synonymCatalog;
    @XmlElement(name = "synonym_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String synonymSchema;
    @XmlElement(name = "synonym_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String synonymName;
    @XmlElement(name = "object_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectCatalog;
    @XmlElement(name = "object_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectSchema;
    @XmlElement(name = "object_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String objectName;
    @XmlElement(name = "is_public")
    protected Boolean isPublic;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    public String getSynonymCatalog() {
        return synonymCatalog;
    }

    public void setSynonymCatalog(String value) {
        this.synonymCatalog = value;
    }

    public String getSynonymSchema() {
        return synonymSchema;
    }

    public void setSynonymSchema(String value) {
        this.synonymSchema = value;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String value) {
        this.synonymName = value;
    }

    public String getObjectCatalog() {
        return objectCatalog;
    }

    public void setObjectCatalog(String value) {
        this.objectCatalog = value;
    }

    public String getObjectSchema() {
        return objectSchema;
    }

    public void setObjectSchema(String value) {
        this.objectSchema = value;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String value) {
        this.objectName = value;
    }

    /**
     * Gets the value of the isPublic property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPublic() {
        return isPublic;
    }

    /**
     * Sets the value of the isPublic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPublic(Boolean value) {
        this.isPublic = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public Synonym withSynonymCatalog(String value) {
        setSynonymCatalog(value);
        return this;
    }

    public Synonym withSynonymSchema(String value) {
        setSynonymSchema(value);
        return this;
    }

    public Synonym withSynonymName(String value) {
        setSynonymName(value);
        return this;
    }

    public Synonym withObjectCatalog(String value) {
        setObjectCatalog(value);
        return this;
    }

    public Synonym withObjectSchema(String value) {
        setObjectSchema(value);
        return this;
    }

    public Synonym withObjectName(String value) {
        setObjectName(value);
        return this;
    }

    public Synonym withIsPublic(Boolean value) {
        setIsPublic(value);
        return this;
    }

    public Synonym withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("synonym_catalog", synonymCatalog);
        builder.append("synonym_schema", synonymSchema);
        builder.append("synonym_name", synonymName);
        builder.append("object_catalog", objectCatalog);
        builder.append("object_schema", objectSchema);
        builder.append("object_name", objectName);
        builder.append("is_public", isPublic);
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
        Synonym other = ((Synonym) that);
        if (synonymCatalog == null) {
            if (other.synonymCatalog!= null) {
                return false;
            }
        } else {
            if (!synonymCatalog.equals(other.synonymCatalog)) {
                return false;
            }
        }
        if (synonymSchema == null) {
            if (other.synonymSchema!= null) {
                return false;
            }
        } else {
            if (!synonymSchema.equals(other.synonymSchema)) {
                return false;
            }
        }
        if (synonymName == null) {
            if (other.synonymName!= null) {
                return false;
            }
        } else {
            if (!synonymName.equals(other.synonymName)) {
                return false;
            }
        }
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
        if (isPublic == null) {
            if (other.isPublic!= null) {
                return false;
            }
        } else {
            if (!isPublic.equals(other.isPublic)) {
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
        result = ((prime*result)+((synonymCatalog == null)? 0 :synonymCatalog.hashCode()));
        result = ((prime*result)+((synonymSchema == null)? 0 :synonymSchema.hashCode()));
        result = ((prime*result)+((synonymName == null)? 0 :synonymName.hashCode()));
        result = ((prime*result)+((objectCatalog == null)? 0 :objectCatalog.hashCode()));
        result = ((prime*result)+((objectSchema == null)? 0 :objectSchema.hashCode()));
        result = ((prime*result)+((objectName == null)? 0 :objectName.hashCode()));
        result = ((prime*result)+((isPublic == null)? 0 :isPublic.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
