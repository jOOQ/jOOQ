
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


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
public class MatchersFieldType implements Serializable
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
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getFieldIdentifier() {
        return fieldIdentifier;
    }

    /**
     * Sets the value of the fieldIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setFieldIdentifier(MatcherRule value) {
        this.fieldIdentifier = value;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getFieldMember() {
        return fieldMember;
    }

    /**
     * Sets the value of the fieldMember property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setFieldMember(MatcherRule value) {
        this.fieldMember = value;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getFieldSetter() {
        return fieldSetter;
    }

    /**
     * Sets the value of the fieldSetter property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setFieldSetter(MatcherRule value) {
        this.fieldSetter = value;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Sets the value of the fieldGetter property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setFieldGetter(MatcherRule value) {
        this.fieldGetter = value;
    }

    public MatchersFieldType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersFieldType withFieldIdentifier(MatcherRule value) {
        setFieldIdentifier(value);
        return this;
    }

    public MatchersFieldType withFieldMember(MatcherRule value) {
        setFieldMember(value);
        return this;
    }

    public MatchersFieldType withFieldSetter(MatcherRule value) {
        setFieldSetter(value);
        return this;
    }

    public MatchersFieldType withFieldGetter(MatcherRule value) {
        setFieldGetter(value);
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
        if (fieldIdentifier!= null) {
            sb.append("<fieldIdentifier>");
            sb.append(fieldIdentifier);
            sb.append("</fieldIdentifier>");
        }
        if (fieldMember!= null) {
            sb.append("<fieldMember>");
            sb.append(fieldMember);
            sb.append("</fieldMember>");
        }
        if (fieldSetter!= null) {
            sb.append("<fieldSetter>");
            sb.append(fieldSetter);
            sb.append("</fieldSetter>");
        }
        if (fieldGetter!= null) {
            sb.append("<fieldGetter>");
            sb.append(fieldGetter);
            sb.append("</fieldGetter>");
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
