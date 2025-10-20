
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
 * Declarative naming strategy configuration for UDT attribute names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersAttributeType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersAttributeType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31928L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule attributeIdentifier;
    protected MatcherRule attributeMember;
    protected MatcherRule attributeSetter;
    protected MatcherRule attributeGetter;

    /**
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public MatcherRule getAttributeIdentifier() {
        return attributeIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public void setAttributeIdentifier(MatcherRule value) {
        this.attributeIdentifier = value;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeMember() {
        return attributeMember;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeMember(MatcherRule value) {
        this.attributeMember = value;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeSetter() {
        return attributeSetter;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeSetter(MatcherRule value) {
        this.attributeSetter = value;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeGetter() {
        return attributeGetter;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeGetter(MatcherRule value) {
        this.attributeGetter = value;
    }

    /**
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public MatchersAttributeType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public MatchersAttributeType withAttributeIdentifier(MatcherRule value) {
        setAttributeIdentifier(value);
        return this;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeMember(MatcherRule value) {
        setAttributeMember(value);
        return this;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeSetter(MatcherRule value) {
        setAttributeSetter(value);
        return this;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeGetter(MatcherRule value) {
        setAttributeGetter(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("attributeIdentifier", attributeIdentifier);
        builder.append("attributeMember", attributeMember);
        builder.append("attributeSetter", attributeSetter);
        builder.append("attributeGetter", attributeGetter);
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
        MatchersAttributeType other = ((MatchersAttributeType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (attributeIdentifier == null) {
            if (other.attributeIdentifier!= null) {
                return false;
            }
        } else {
            if (!attributeIdentifier.equals(other.attributeIdentifier)) {
                return false;
            }
        }
        if (attributeMember == null) {
            if (other.attributeMember!= null) {
                return false;
            }
        } else {
            if (!attributeMember.equals(other.attributeMember)) {
                return false;
            }
        }
        if (attributeSetter == null) {
            if (other.attributeSetter!= null) {
                return false;
            }
        } else {
            if (!attributeSetter.equals(other.attributeSetter)) {
                return false;
            }
        }
        if (attributeGetter == null) {
            if (other.attributeGetter!= null) {
                return false;
            }
        } else {
            if (!attributeGetter.equals(other.attributeGetter)) {
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
        result = ((prime*result)+((attributeIdentifier == null)? 0 :attributeIdentifier.hashCode()));
        result = ((prime*result)+((attributeMember == null)? 0 :attributeMember.hashCode()));
        result = ((prime*result)+((attributeSetter == null)? 0 :attributeSetter.hashCode()));
        result = ((prime*result)+((attributeGetter == null)? 0 :attributeGetter.hashCode()));
        return result;
    }

}
