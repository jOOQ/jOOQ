
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
 * <p>Java class for ReferentialConstraint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReferentialConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="unique_constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unique_constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="unique_constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferentialConstraint", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ReferentialConstraint implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31300L;
    @XmlElement(name = "constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintCatalog;
    @XmlElement(name = "constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintSchema;
    @XmlElement(name = "constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintName;
    @XmlElement(name = "unique_constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintCatalog;
    @XmlElement(name = "unique_constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintSchema;
    @XmlElement(name = "unique_constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String uniqueConstraintName;

    public String getConstraintCatalog() {
        return constraintCatalog;
    }

    public void setConstraintCatalog(String value) {
        this.constraintCatalog = value;
    }

    public String getConstraintSchema() {
        return constraintSchema;
    }

    public void setConstraintSchema(String value) {
        this.constraintSchema = value;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String value) {
        this.constraintName = value;
    }

    public String getUniqueConstraintCatalog() {
        return uniqueConstraintCatalog;
    }

    public void setUniqueConstraintCatalog(String value) {
        this.uniqueConstraintCatalog = value;
    }

    public String getUniqueConstraintSchema() {
        return uniqueConstraintSchema;
    }

    public void setUniqueConstraintSchema(String value) {
        this.uniqueConstraintSchema = value;
    }

    public String getUniqueConstraintName() {
        return uniqueConstraintName;
    }

    public void setUniqueConstraintName(String value) {
        this.uniqueConstraintName = value;
    }

    public ReferentialConstraint withConstraintCatalog(String value) {
        setConstraintCatalog(value);
        return this;
    }

    public ReferentialConstraint withConstraintSchema(String value) {
        setConstraintSchema(value);
        return this;
    }

    public ReferentialConstraint withConstraintName(String value) {
        setConstraintName(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintCatalog(String value) {
        setUniqueConstraintCatalog(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintSchema(String value) {
        setUniqueConstraintSchema(value);
        return this;
    }

    public ReferentialConstraint withUniqueConstraintName(String value) {
        setUniqueConstraintName(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("constraint_catalog", constraintCatalog);
        builder.append("constraint_schema", constraintSchema);
        builder.append("constraint_name", constraintName);
        builder.append("unique_constraint_catalog", uniqueConstraintCatalog);
        builder.append("unique_constraint_schema", uniqueConstraintSchema);
        builder.append("unique_constraint_name", uniqueConstraintName);
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
        ReferentialConstraint other = ((ReferentialConstraint) that);
        if (constraintCatalog == null) {
            if (other.constraintCatalog!= null) {
                return false;
            }
        } else {
            if (!constraintCatalog.equals(other.constraintCatalog)) {
                return false;
            }
        }
        if (constraintSchema == null) {
            if (other.constraintSchema!= null) {
                return false;
            }
        } else {
            if (!constraintSchema.equals(other.constraintSchema)) {
                return false;
            }
        }
        if (constraintName == null) {
            if (other.constraintName!= null) {
                return false;
            }
        } else {
            if (!constraintName.equals(other.constraintName)) {
                return false;
            }
        }
        if (uniqueConstraintCatalog == null) {
            if (other.uniqueConstraintCatalog!= null) {
                return false;
            }
        } else {
            if (!uniqueConstraintCatalog.equals(other.uniqueConstraintCatalog)) {
                return false;
            }
        }
        if (uniqueConstraintSchema == null) {
            if (other.uniqueConstraintSchema!= null) {
                return false;
            }
        } else {
            if (!uniqueConstraintSchema.equals(other.uniqueConstraintSchema)) {
                return false;
            }
        }
        if (uniqueConstraintName == null) {
            if (other.uniqueConstraintName!= null) {
                return false;
            }
        } else {
            if (!uniqueConstraintName.equals(other.uniqueConstraintName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((constraintCatalog == null)? 0 :constraintCatalog.hashCode()));
        result = ((prime*result)+((constraintSchema == null)? 0 :constraintSchema.hashCode()));
        result = ((prime*result)+((constraintName == null)? 0 :constraintName.hashCode()));
        result = ((prime*result)+((uniqueConstraintCatalog == null)? 0 :uniqueConstraintCatalog.hashCode()));
        result = ((prime*result)+((uniqueConstraintSchema == null)? 0 :uniqueConstraintSchema.hashCode()));
        result = ((prime*result)+((uniqueConstraintName == null)? 0 :uniqueConstraintName.hashCode()));
        return result;
    }

}
