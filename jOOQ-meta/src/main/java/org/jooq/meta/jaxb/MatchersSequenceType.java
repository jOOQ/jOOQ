







package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Declarative naming strategy configuration for sequence names.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersSequenceType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersSequenceType implements Serializable
{

    private final static long serialVersionUID = 31200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule sequenceIdentifier;

    /**
     * This sequence matcher applies to all unqualified or qualified sequence names matched by this expression. If left empty, this matcher applies to all sequences.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Sequence} identifier.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSequenceIdentifier() {
        return sequenceIdentifier;
    }

    /**
     * Sets the value of the sequenceIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSequenceIdentifier(MatcherRule value) {
        this.sequenceIdentifier = value;
    }

    public MatchersSequenceType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersSequenceType withSequenceIdentifier(MatcherRule value) {
        setSequenceIdentifier(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((expression!= null)&&(!"".equals(expression))) {
            sb.append("<expression>");
            sb.append(expression);
            sb.append("</expression>");
        }
        if (sequenceIdentifier!= null) {
            sb.append("<sequenceIdentifier>");
            sb.append(sequenceIdentifier);
            sb.append("</sequenceIdentifier>");
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
        MatchersSequenceType other = ((MatchersSequenceType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (sequenceIdentifier == null) {
            if (other.sequenceIdentifier!= null) {
                return false;
            }
        } else {
            if (!sequenceIdentifier.equals(other.sequenceIdentifier)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((sequenceIdentifier == null)? 0 :sequenceIdentifier.hashCode()));
        return result;
    }

}
