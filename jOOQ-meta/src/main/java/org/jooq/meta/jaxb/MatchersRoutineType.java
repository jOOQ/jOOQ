







package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Declarative naming strategy configuration for routine names.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersRoutineType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersRoutineType implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule routineClass;
    protected MatcherRule routineMethod;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineImplements;

    /**
     * This routine matcher applies to all unqualified or qualified routine names matched by this expression. If left empty, this matcher applies to all routines.
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
     * This rule influences the naming of the generated {@link org.jooq.Routine} object.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getRoutineClass() {
        return routineClass;
    }

    /**
     * Sets the value of the routineClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setRoutineClass(MatcherRule value) {
        this.routineClass = value;
    }

    /**
     * This rule influences the naming of generated convenience methods used to call the {@link org.jooq.Routine}.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getRoutineMethod() {
        return routineMethod;
    }

    /**
     * Sets the value of the routineMethod property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setRoutineMethod(MatcherRule value) {
        this.routineMethod = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Routine} should implement.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoutineImplements() {
        return routineImplements;
    }

    /**
     * Sets the value of the routineImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoutineImplements(String value) {
        this.routineImplements = value;
    }

    public MatchersRoutineType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersRoutineType withRoutineClass(MatcherRule value) {
        setRoutineClass(value);
        return this;
    }

    public MatchersRoutineType withRoutineMethod(MatcherRule value) {
        setRoutineMethod(value);
        return this;
    }

    public MatchersRoutineType withRoutineImplements(String value) {
        setRoutineImplements(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (expression!= null) {
            sb.append("<expression>");
            sb.append(expression);
            sb.append("</expression>");
        }
        if (routineClass!= null) {
            sb.append("<routineClass>");
            sb.append(routineClass);
            sb.append("</routineClass>");
        }
        if (routineMethod!= null) {
            sb.append("<routineMethod>");
            sb.append(routineMethod);
            sb.append("</routineMethod>");
        }
        if (routineImplements!= null) {
            sb.append("<routineImplements>");
            sb.append(routineImplements);
            sb.append("</routineImplements>");
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
        MatchersRoutineType other = ((MatchersRoutineType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (routineClass == null) {
            if (other.routineClass!= null) {
                return false;
            }
        } else {
            if (!routineClass.equals(other.routineClass)) {
                return false;
            }
        }
        if (routineMethod == null) {
            if (other.routineMethod!= null) {
                return false;
            }
        } else {
            if (!routineMethod.equals(other.routineMethod)) {
                return false;
            }
        }
        if (routineImplements == null) {
            if (other.routineImplements!= null) {
                return false;
            }
        } else {
            if (!routineImplements.equals(other.routineImplements)) {
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
        result = ((prime*result)+((routineClass == null)? 0 :routineClass.hashCode()));
        result = ((prime*result)+((routineMethod == null)? 0 :routineMethod.hashCode()));
        result = ((prime*result)+((routineImplements == null)? 0 :routineImplements.hashCode()));
        return result;
    }

}
