
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
 * Declarative naming strategy configuration for enum names.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersEnumType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersEnumType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule enumClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String enumImplements;

    /**
     * This enum matcher applies to all unqualified or qualified enum names matched by this expression. If left empty, this matcher applies to all enums.
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
     * This rule influences the naming of the generated {@link org.jooq.EnumType} object.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getEnumClass() {
        return enumClass;
    }

    /**
     * Sets the value of the enumClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setEnumClass(MatcherRule value) {
        this.enumClass = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EnumType} should implement.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEnumImplements() {
        return enumImplements;
    }

    /**
     * Sets the value of the enumImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEnumImplements(String value) {
        this.enumImplements = value;
    }

    /**
     * Sets the value of the expression property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public MatchersEnumType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * Sets the value of the enumClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public MatchersEnumType withEnumClass(MatcherRule value) {
        setEnumClass(value);
        return this;
    }

    /**
     * Sets the value of the enumImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public MatchersEnumType withEnumImplements(String value) {
        setEnumImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("enumClass", enumClass);
        builder.append("enumImplements", enumImplements);
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
        MatchersEnumType other = ((MatchersEnumType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (enumClass == null) {
            if (other.enumClass!= null) {
                return false;
            }
        } else {
            if (!enumClass.equals(other.enumClass)) {
                return false;
            }
        }
        if (enumImplements == null) {
            if (other.enumImplements!= null) {
                return false;
            }
        } else {
            if (!enumImplements.equals(other.enumImplements)) {
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
        result = ((prime*result)+((enumClass == null)? 0 :enumClass.hashCode()));
        result = ((prime*result)+((enumImplements == null)? 0 :enumImplements.hashCode()));
        return result;
    }

}
