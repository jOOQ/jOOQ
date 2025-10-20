
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for MatcherRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatcherRule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="transform" type="{http://www.jooq.org/xsd/jooq-codegen-3.19.28.xsd}MatcherTransformType" minOccurs="0"/&gt;
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
@XmlType(name = "MatcherRule", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatcherRule implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31928L;
    @XmlSchemaType(name = "string")
    protected MatcherTransformType transform;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;

    /**
     * A pre-defined transformation type that transforms this rule's output into a specific format.
     * 
     */
    public MatcherTransformType getTransform() {
        return transform;
    }

    /**
     * A pre-defined transformation type that transforms this rule's output into a specific format.
     * 
     */
    public void setTransform(MatcherTransformType value) {
        this.transform = value;
    }

    /**
     * A replacement expression that transforms the matched expression in a new value.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * A replacement expression that transforms the matched expression in a new value.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * A pre-defined transformation type that transforms this rule's output into a specific format.
     * 
     */
    public MatcherRule withTransform(MatcherTransformType value) {
        setTransform(value);
        return this;
    }

    /**
     * A replacement expression that transforms the matched expression in a new value.
     * 
     */
    public MatcherRule withExpression(String value) {
        setExpression(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("transform", transform);
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
        MatcherRule other = ((MatcherRule) that);
        if (transform == null) {
            if (other.transform!= null) {
                return false;
            }
        } else {
            if (!transform.equals(other.transform)) {
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
        result = ((prime*result)+((transform == null)? 0 :transform.hashCode()));
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        return result;
    }

}
