
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Declarative naming strategy configuration for field names.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersFieldType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersFieldType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule fieldIdentifier;
    protected MatcherRule fieldMember;
    protected MatcherRule fieldSetter;
    protected MatcherRule fieldGetter;

    /**
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     *
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     *
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     *
     */
    public MatcherRule getFieldIdentifier() {
        return fieldIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     *
     */
    public void setFieldIdentifier(MatcherRule value) {
        this.fieldIdentifier = value;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatcherRule getFieldMember() {
        return fieldMember;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public void setFieldMember(MatcherRule value) {
        this.fieldMember = value;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatcherRule getFieldSetter() {
        return fieldSetter;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public void setFieldSetter(MatcherRule value) {
        this.fieldSetter = value;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatcherRule getFieldGetter() {
        return fieldGetter;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public void setFieldGetter(MatcherRule value) {
        this.fieldGetter = value;
    }

    /**
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     *
     */
    public MatchersFieldType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     *
     */
    public MatchersFieldType withFieldIdentifier(MatcherRule value) {
        setFieldIdentifier(value);
        return this;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatchersFieldType withFieldMember(MatcherRule value) {
        setFieldMember(value);
        return this;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatchersFieldType withFieldSetter(MatcherRule value) {
        setFieldSetter(value);
        return this;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     */
    public MatchersFieldType withFieldGetter(MatcherRule value) {
        setFieldGetter(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("fieldIdentifier", fieldIdentifier);
        builder.append("fieldMember", fieldMember);
        builder.append("fieldSetter", fieldSetter);
        builder.append("fieldGetter", fieldGetter);
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
        MatchersFieldType other = ((MatchersFieldType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (fieldIdentifier == null) {
            if (other.fieldIdentifier!= null) {
                return false;
            }
        } else {
            if (!fieldIdentifier.equals(other.fieldIdentifier)) {
                return false;
            }
        }
        if (fieldMember == null) {
            if (other.fieldMember!= null) {
                return false;
            }
        } else {
            if (!fieldMember.equals(other.fieldMember)) {
                return false;
            }
        }
        if (fieldSetter == null) {
            if (other.fieldSetter!= null) {
                return false;
            }
        } else {
            if (!fieldSetter.equals(other.fieldSetter)) {
                return false;
            }
        }
        if (fieldGetter == null) {
            if (other.fieldGetter!= null) {
                return false;
            }
        } else {
            if (!fieldGetter.equals(other.fieldGetter)) {
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
        result = ((prime*result)+((fieldIdentifier == null)? 0 :fieldIdentifier.hashCode()));
        result = ((prime*result)+((fieldMember == null)? 0 :fieldMember.hashCode()));
        result = ((prime*result)+((fieldSetter == null)? 0 :fieldSetter.hashCode()));
        result = ((prime*result)+((fieldGetter == null)? 0 :fieldGetter.hashCode()));
        return result;
    }

}
