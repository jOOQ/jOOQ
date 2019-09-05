
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
public class MatchersRoutineType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule routineClass;
    protected MatcherRule routineMethod;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineImplements;

    /**
     * This routine matcher applies to all unqualified or qualified routine names matched by this expression. If left empty, this matcher applies to all routines.
     *
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This routine matcher applies to all unqualified or qualified routine names matched by this expression. If left empty, this matcher applies to all routines.
     *
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Routine} object.
     *
     */
    public MatcherRule getRoutineClass() {
        return routineClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Routine} object.
     *
     */
    public void setRoutineClass(MatcherRule value) {
        this.routineClass = value;
    }

    /**
     * This rule influences the naming of generated convenience methods used to call the {@link org.jooq.Routine}.
     *
     */
    public MatcherRule getRoutineMethod() {
        return routineMethod;
    }

    /**
     * This rule influences the naming of generated convenience methods used to call the {@link org.jooq.Routine}.
     *
     */
    public void setRoutineMethod(MatcherRule value) {
        this.routineMethod = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Routine} should implement.
     *
     */
    public String getRoutineImplements() {
        return routineImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Routine} should implement.
     *
     */
    public void setRoutineImplements(String value) {
        this.routineImplements = value;
    }

    /**
     * This routine matcher applies to all unqualified or qualified routine names matched by this expression. If left empty, this matcher applies to all routines.
     *
     */
    public MatchersRoutineType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Routine} object.
     *
     */
    public MatchersRoutineType withRoutineClass(MatcherRule value) {
        setRoutineClass(value);
        return this;
    }

    /**
     * This rule influences the naming of generated convenience methods used to call the {@link org.jooq.Routine}.
     *
     */
    public MatchersRoutineType withRoutineMethod(MatcherRule value) {
        setRoutineMethod(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Routine} should implement.
     *
     */
    public MatchersRoutineType withRoutineImplements(String value) {
        setRoutineImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("routineClass", routineClass);
        builder.append("routineMethod", routineMethod);
        builder.append("routineImplements", routineImplements);
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
