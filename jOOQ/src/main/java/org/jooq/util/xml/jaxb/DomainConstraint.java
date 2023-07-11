
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
 * <p>Java class for DomainConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="domain_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainConstraint", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class DomainConstraint implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    @XmlElement(name = "constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintCatalog;
    @XmlElement(name = "constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintSchema;
    @XmlElement(name = "constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintName;
    @XmlElement(name = "domain_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainCatalog;
    @XmlElement(name = "domain_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainSchema;
    @XmlElement(name = "domain_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainName;

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

    public String getDomainCatalog() {
        return domainCatalog;
    }

    public void setDomainCatalog(String value) {
        this.domainCatalog = value;
    }

    public String getDomainSchema() {
        return domainSchema;
    }

    public void setDomainSchema(String value) {
        this.domainSchema = value;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String value) {
        this.domainName = value;
    }

    public DomainConstraint withConstraintCatalog(String value) {
        setConstraintCatalog(value);
        return this;
    }

    public DomainConstraint withConstraintSchema(String value) {
        setConstraintSchema(value);
        return this;
    }

    public DomainConstraint withConstraintName(String value) {
        setConstraintName(value);
        return this;
    }

    public DomainConstraint withDomainCatalog(String value) {
        setDomainCatalog(value);
        return this;
    }

    public DomainConstraint withDomainSchema(String value) {
        setDomainSchema(value);
        return this;
    }

    public DomainConstraint withDomainName(String value) {
        setDomainName(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("constraint_catalog", constraintCatalog);
        builder.append("constraint_schema", constraintSchema);
        builder.append("constraint_name", constraintName);
        builder.append("domain_catalog", domainCatalog);
        builder.append("domain_schema", domainSchema);
        builder.append("domain_name", domainName);
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
        DomainConstraint other = ((DomainConstraint) that);
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
        if (domainCatalog == null) {
            if (other.domainCatalog!= null) {
                return false;
            }
        } else {
            if (!domainCatalog.equals(other.domainCatalog)) {
                return false;
            }
        }
        if (domainSchema == null) {
            if (other.domainSchema!= null) {
                return false;
            }
        } else {
            if (!domainSchema.equals(other.domainSchema)) {
                return false;
            }
        }
        if (domainName == null) {
            if (other.domainName!= null) {
                return false;
            }
        } else {
            if (!domainName.equals(other.domainName)) {
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
        result = ((prime*result)+((domainCatalog == null)? 0 :domainCatalog.hashCode()));
        result = ((prime*result)+((domainSchema == null)? 0 :domainSchema.hashCode()));
        result = ((prime*result)+((domainName == null)? 0 :domainName.hashCode()));
        return result;
    }

}
