







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für MatcherRule complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="MatcherRule"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="transform" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherTransformType" minOccurs="0"/&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatcherRule", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatcherRule implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlSchemaType(name = "string")
    protected MatcherTransformType transform;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;

    /**
     * Ruft den Wert der transform-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherTransformType }
     *
     */
    public MatcherTransformType getTransform() {
        return transform;
    }

    /**
     * Legt den Wert der transform-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherTransformType }
     *
     */
    public void setTransform(MatcherTransformType value) {
        this.transform = value;
    }

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

    public MatcherRule withTransform(MatcherTransformType value) {
        setTransform(value);
        return this;
    }

    public MatcherRule withExpression(String value) {
        setExpression(value);
        return this;
    }

}
