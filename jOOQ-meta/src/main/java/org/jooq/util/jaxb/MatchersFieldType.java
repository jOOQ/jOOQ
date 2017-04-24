







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für MatchersFieldType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der expression-Eigenschaft ab.
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
     * Legt den Wert der expression-Eigenschaft fest.
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
     * Ruft den Wert der fieldIdentifier-Eigenschaft ab.
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
     * Legt den Wert der fieldIdentifier-Eigenschaft fest.
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
     * Ruft den Wert der fieldMember-Eigenschaft ab.
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
     * Legt den Wert der fieldMember-Eigenschaft fest.
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
     * Ruft den Wert der fieldSetter-Eigenschaft ab.
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
     * Legt den Wert der fieldSetter-Eigenschaft fest.
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
     * Ruft den Wert der fieldGetter-Eigenschaft ab.
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
     * Legt den Wert der fieldGetter-Eigenschaft fest.
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
