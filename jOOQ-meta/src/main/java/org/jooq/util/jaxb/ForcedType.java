







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für ForcedType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ForcedType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="converter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *           &lt;element name="enumConverter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="binding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="expressions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="types" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForcedType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ForcedType implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userType;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String converter;
    protected Boolean enumConverter;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String binding;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expressions;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String types;

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der userType-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Legt den Wert der userType-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserType(String value) {
        this.userType = value;
    }

    /**
     * Ruft den Wert der converter-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConverter() {
        return converter;
    }

    /**
     * Legt den Wert der converter-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConverter(String value) {
        this.converter = value;
    }

    /**
     * Ruft den Wert der enumConverter-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isEnumConverter() {
        return enumConverter;
    }

    /**
     * Legt den Wert der enumConverter-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEnumConverter(Boolean value) {
        this.enumConverter = value;
    }

    /**
     * Ruft den Wert der binding-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Legt den Wert der binding-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBinding(String value) {
        this.binding = value;
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

    /**
     * Ruft den Wert der expressions-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExpressions() {
        return expressions;
    }

    /**
     * Legt den Wert der expressions-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExpressions(String value) {
        this.expressions = value;
    }

    /**
     * Ruft den Wert der types-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTypes() {
        return types;
    }

    /**
     * Legt den Wert der types-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTypes(String value) {
        this.types = value;
    }

    public ForcedType withName(String value) {
        setName(value);
        return this;
    }

    public ForcedType withUserType(String value) {
        setUserType(value);
        return this;
    }

    public ForcedType withConverter(String value) {
        setConverter(value);
        return this;
    }

    public ForcedType withEnumConverter(Boolean value) {
        setEnumConverter(value);
        return this;
    }

    public ForcedType withBinding(String value) {
        setBinding(value);
        return this;
    }

    public ForcedType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public ForcedType withExpressions(String value) {
        setExpressions(value);
        return this;
    }

    public ForcedType withTypes(String value) {
        setTypes(value);
        return this;
    }

}
