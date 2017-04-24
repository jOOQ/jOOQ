







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für MatchersSchemaType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der schemaClass-Eigenschaft ab.
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
     * Legt den Wert der schemaClass-Eigenschaft fest.
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
     * Ruft den Wert der schemaIdentifier-Eigenschaft ab.
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
     * Legt den Wert der schemaIdentifier-Eigenschaft fest.
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
     * Ruft den Wert der schemaImplements-Eigenschaft ab.
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
     * Legt den Wert der schemaImplements-Eigenschaft fest.
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
