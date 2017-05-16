







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for MatchersSequenceType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MatchersSequenceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sequenceIdentifier" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersSequenceType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersSequenceType implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule sequenceIdentifier;

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
     * Gets the value of the sequenceIdentifier property.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSequenceIdentifier() {
        return sequenceIdentifier;
    }

    /**
     * Sets the value of the sequenceIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSequenceIdentifier(MatcherRule value) {
        this.sequenceIdentifier = value;
    }

    public MatchersSequenceType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersSequenceType withSequenceIdentifier(MatcherRule value) {
        setSequenceIdentifier(value);
        return this;
    }

}
