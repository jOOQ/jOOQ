
package org.jooq.meta.jaxb;

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
 * <p>Java class for SyntheticDefaultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticDefaultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ignoreUnused" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticDefaultType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticDefaultType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32001L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fields;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreUnused = false;

    /**
     * A regular expression matching all tables on which to apply this synthetic default.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic default.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic default.
     * 
     */
    public String getFields() {
        return fields;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic default.
     * 
     */
    public void setFields(String value) {
        this.fields = value;
    }

    /**
     * The default expression to apply to the field. This is expected to be a valid SQL expression, e.g. <code>'some string'</code>, not <code>some string</code>
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * The default expression to apply to the field. This is expected to be a valid SQL expression, e.g. <code>'some string'</code>, not <code>some string</code>
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIgnoreUnused() {
        return ignoreUnused;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreUnused(Boolean value) {
        this.ignoreUnused = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic default.
     * 
     */
    public SyntheticDefaultType withTables(String value) {
        setTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic default.
     * 
     */
    public SyntheticDefaultType withFields(String value) {
        setFields(value);
        return this;
    }

    /**
     * The default expression to apply to the field. This is expected to be a valid SQL expression, e.g. <code>'some string'</code>, not <code>some string</code>
     * 
     */
    public SyntheticDefaultType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     */
    public SyntheticDefaultType withIgnoreUnused(Boolean value) {
        setIgnoreUnused(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("tables", tables);
        builder.append("fields", fields);
        builder.append("expression", expression);
        builder.append("ignoreUnused", ignoreUnused);
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
        SyntheticDefaultType other = ((SyntheticDefaultType) that);
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
                return false;
            }
        }
        if (fields == null) {
            if (other.fields!= null) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
                return false;
            }
        }
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (ignoreUnused == null) {
            if (other.ignoreUnused!= null) {
                return false;
            }
        } else {
            if (!ignoreUnused.equals(other.ignoreUnused)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((ignoreUnused == null)? 0 :ignoreUnused.hashCode()));
        return result;
    }

}
