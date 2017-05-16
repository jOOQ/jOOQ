







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for MatchersFieldType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MatchersFieldType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fieldIdentifier" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="fieldMember" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="fieldSetter" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="fieldGetter" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule fieldIdentifier;
    protected MatcherRule fieldMember;
    protected MatcherRule fieldSetter;
    protected MatcherRule fieldGetter;

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
     * Gets the value of the fieldIdentifier property.
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
     * Gets the value of the fieldMember property.
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
     * Gets the value of the fieldSetter property.
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
     * Gets the value of the fieldGetter property.
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

}
