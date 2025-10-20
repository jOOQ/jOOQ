
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
 * <p>Java class for EmbeddableField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EmbeddableField"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmbeddableField", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class EmbeddableField implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31835L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;

    /**
     * A name for the field in case the regex does not produce unique names for all matches.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * A name for the field in case the regex does not produce unique names for all matches.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regex matching all column names that are part of the embeddable type. The regex must match only one column per table.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * A regex matching all column names that are part of the embeddable type. The regex must match only one column per table.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * A name for the field in case the regex does not produce unique names for all matches.
     * 
     */
    public EmbeddableField withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regex matching all column names that are part of the embeddable type. The regex must match only one column per table.
     * 
     */
    public EmbeddableField withExpression(String value) {
        setExpression(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("expression", expression);
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
        EmbeddableField other = ((EmbeddableField) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        return result;
    }

}
