
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
 * Declarative naming strategy configuration for index names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersIndexType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersIndexType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31908L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule keyIdentifier;

    /**
     * This table matcher applies to all unqualified or qualified index names matched by this expression. If left empty, this matcher applies to all indexes.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified index names matched by this expression. If left empty, this matcher applies to all indexes.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated key literal in the Indexes class.
     * 
     */
    public MatcherRule getKeyIdentifier() {
        return keyIdentifier;
    }

    /**
     * This rule influences the naming of the generated key literal in the Indexes class.
     * 
     */
    public void setKeyIdentifier(MatcherRule value) {
        this.keyIdentifier = value;
    }

    /**
     * This table matcher applies to all unqualified or qualified index names matched by this expression. If left empty, this matcher applies to all indexes.
     * 
     */
    public MatchersIndexType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated key literal in the Indexes class.
     * 
     */
    public MatchersIndexType withKeyIdentifier(MatcherRule value) {
        setKeyIdentifier(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("keyIdentifier", keyIdentifier);
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
        MatchersIndexType other = ((MatchersIndexType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (keyIdentifier == null) {
            if (other.keyIdentifier!= null) {
                return false;
            }
        } else {
            if (!keyIdentifier.equals(other.keyIdentifier)) {
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
        result = ((prime*result)+((keyIdentifier == null)? 0 :keyIdentifier.hashCode()));
        return result;
    }

}
