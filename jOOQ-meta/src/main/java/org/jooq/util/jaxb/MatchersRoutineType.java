







package org.jooq.util.jaxb;

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

}
