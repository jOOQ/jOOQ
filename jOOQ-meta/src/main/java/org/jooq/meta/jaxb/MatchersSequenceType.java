
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
public class MatchersSequenceType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31928L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule sequenceIdentifier;

    /**
     * This sequence matcher applies to all unqualified or qualified sequence names matched by this expression. If left empty, this matcher applies to all sequences.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This sequence matcher applies to all unqualified or qualified sequence names matched by this expression. If left empty, this matcher applies to all sequences.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Sequence} identifier.
     * 
     */
    public MatcherRule getSequenceIdentifier() {
        return sequenceIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Sequence} identifier.
     * 
     */
    public void setSequenceIdentifier(MatcherRule value) {
        this.sequenceIdentifier = value;
    }

    /**
     * This sequence matcher applies to all unqualified or qualified sequence names matched by this expression. If left empty, this matcher applies to all sequences.
     * 
     */
    public MatchersSequenceType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Sequence} identifier.
     * 
     */
    public MatchersSequenceType withSequenceIdentifier(MatcherRule value) {
        setSequenceIdentifier(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("sequenceIdentifier", sequenceIdentifier);
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
