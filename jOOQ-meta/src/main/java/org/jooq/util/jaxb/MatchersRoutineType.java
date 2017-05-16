







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for MatchersRoutineType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MatchersRoutineType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routineClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="routineMethod" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="routineImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule routineClass;
    protected MatcherRule routineMethod;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String routineImplements;

    /**
     * Gets the value of the expression property.
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
     * Gets the value of the routineClass property.
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
     * Gets the value of the routineMethod property.
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
     * Gets the value of the routineImplements property.
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
