







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Strategy complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Strategy"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="matchers" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}Matchers" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Strategy", propOrder = {
    "name",
    "matchers"
})
@SuppressWarnings({
    "all"
})
public class Strategy implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(defaultValue = "org.jooq.util.DefaultGeneratorStrategy")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name = "org.jooq.util.DefaultGeneratorStrategy";
    protected Matchers matchers;

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
     * Ruft den Wert der matchers-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link Matchers }
     *
     */
    public Matchers getMatchers() {
        return matchers;
    }

    /**
     * Legt den Wert der matchers-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link Matchers }
     *
     */
    public void setMatchers(Matchers value) {
        this.matchers = value;
    }

    public Strategy withName(String value) {
        setName(value);
        return this;
    }

    public Strategy withMatchers(Matchers value) {
        setMatchers(value);
        return this;
    }

}
