







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for MatchersSchemaType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MatchersSchemaType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="schemaClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="schemaIdentifier" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="schemaImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersSchemaType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersSchemaType implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule schemaClass;
    protected MatcherRule schemaIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaImplements;

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
     * Gets the value of the schemaClass property.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSchemaClass() {
        return schemaClass;
    }

    /**
     * Sets the value of the schemaClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSchemaClass(MatcherRule value) {
        this.schemaClass = value;
    }

    /**
     * Gets the value of the schemaIdentifier property.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSchemaIdentifier() {
        return schemaIdentifier;
    }

    /**
     * Sets the value of the schemaIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSchemaIdentifier(MatcherRule value) {
        this.schemaIdentifier = value;
    }

    /**
     * Gets the value of the schemaImplements property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchemaImplements() {
        return schemaImplements;
    }

    /**
     * Sets the value of the schemaImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchemaImplements(String value) {
        this.schemaImplements = value;
    }

    public MatchersSchemaType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersSchemaType withSchemaClass(MatcherRule value) {
        setSchemaClass(value);
        return this;
    }

    public MatchersSchemaType withSchemaIdentifier(MatcherRule value) {
        setSchemaIdentifier(value);
        return this;
    }

    public MatchersSchemaType withSchemaImplements(String value) {
        setSchemaImplements(value);
        return this;
    }

}
