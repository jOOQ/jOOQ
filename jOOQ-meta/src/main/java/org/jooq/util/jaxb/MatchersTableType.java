







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für MatchersTableType complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="MatchersTableType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tableClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="tableIdentifier" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="tableImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="recordClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="recordImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="interfaceClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="interfaceImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="daoClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="daoImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pojoClass" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatcherRule" minOccurs="0"/&gt;
 *         &lt;element name="pojoExtends" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pojoImplements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersTableType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersTableType implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule tableClass;
    protected MatcherRule tableIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableImplements;
    protected MatcherRule recordClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordImplements;
    protected MatcherRule interfaceClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String interfaceImplements;
    protected MatcherRule daoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String daoImplements;
    protected MatcherRule pojoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoImplements;

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
     * Ruft den Wert der tableClass-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getTableClass() {
        return tableClass;
    }

    /**
     * Legt den Wert der tableClass-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setTableClass(MatcherRule value) {
        this.tableClass = value;
    }

    /**
     * Ruft den Wert der tableIdentifier-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getTableIdentifier() {
        return tableIdentifier;
    }

    /**
     * Legt den Wert der tableIdentifier-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setTableIdentifier(MatcherRule value) {
        this.tableIdentifier = value;
    }

    /**
     * Ruft den Wert der tableImplements-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTableImplements() {
        return tableImplements;
    }

    /**
     * Legt den Wert der tableImplements-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTableImplements(String value) {
        this.tableImplements = value;
    }

    /**
     * Ruft den Wert der recordClass-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getRecordClass() {
        return recordClass;
    }

    /**
     * Legt den Wert der recordClass-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setRecordClass(MatcherRule value) {
        this.recordClass = value;
    }

    /**
     * Ruft den Wert der recordImplements-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRecordImplements() {
        return recordImplements;
    }

    /**
     * Legt den Wert der recordImplements-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRecordImplements(String value) {
        this.recordImplements = value;
    }

    /**
     * Ruft den Wert der interfaceClass-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * Legt den Wert der interfaceClass-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setInterfaceClass(MatcherRule value) {
        this.interfaceClass = value;
    }

    /**
     * Ruft den Wert der interfaceImplements-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInterfaceImplements() {
        return interfaceImplements;
    }

    /**
     * Legt den Wert der interfaceImplements-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInterfaceImplements(String value) {
        this.interfaceImplements = value;
    }

    /**
     * Ruft den Wert der daoClass-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getDaoClass() {
        return daoClass;
    }

    /**
     * Legt den Wert der daoClass-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setDaoClass(MatcherRule value) {
        this.daoClass = value;
    }

    /**
     * Ruft den Wert der daoImplements-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDaoImplements() {
        return daoImplements;
    }

    /**
     * Legt den Wert der daoImplements-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDaoImplements(String value) {
        this.daoImplements = value;
    }

    /**
     * Ruft den Wert der pojoClass-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getPojoClass() {
        return pojoClass;
    }

    /**
     * Legt den Wert der pojoClass-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setPojoClass(MatcherRule value) {
        this.pojoClass = value;
    }

    /**
     * Ruft den Wert der pojoExtends-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPojoExtends() {
        return pojoExtends;
    }

    /**
     * Legt den Wert der pojoExtends-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPojoExtends(String value) {
        this.pojoExtends = value;
    }

    /**
     * Ruft den Wert der pojoImplements-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPojoImplements() {
        return pojoImplements;
    }

    /**
     * Legt den Wert der pojoImplements-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPojoImplements(String value) {
        this.pojoImplements = value;
    }

    public MatchersTableType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersTableType withTableClass(MatcherRule value) {
        setTableClass(value);
        return this;
    }

    public MatchersTableType withTableIdentifier(MatcherRule value) {
        setTableIdentifier(value);
        return this;
    }

    public MatchersTableType withTableImplements(String value) {
        setTableImplements(value);
        return this;
    }

    public MatchersTableType withRecordClass(MatcherRule value) {
        setRecordClass(value);
        return this;
    }

    public MatchersTableType withRecordImplements(String value) {
        setRecordImplements(value);
        return this;
    }

    public MatchersTableType withInterfaceClass(MatcherRule value) {
        setInterfaceClass(value);
        return this;
    }

    public MatchersTableType withInterfaceImplements(String value) {
        setInterfaceImplements(value);
        return this;
    }

    public MatchersTableType withDaoClass(MatcherRule value) {
        setDaoClass(value);
        return this;
    }

    public MatchersTableType withDaoImplements(String value) {
        setDaoImplements(value);
        return this;
    }

    public MatchersTableType withPojoClass(MatcherRule value) {
        setPojoClass(value);
        return this;
    }

    public MatchersTableType withPojoExtends(String value) {
        setPojoExtends(value);
        return this;
    }

    public MatchersTableType withPojoImplements(String value) {
        setPojoImplements(value);
        return this;
    }

}
