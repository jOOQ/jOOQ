







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for MatchersTableType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the tableClass property.
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
     * Sets the value of the tableClass property.
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
     * Gets the value of the tableIdentifier property.
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
     * Sets the value of the tableIdentifier property.
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
     * Gets the value of the tableImplements property.
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
     * Sets the value of the tableImplements property.
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
     * Gets the value of the recordClass property.
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
     * Sets the value of the recordClass property.
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
     * Gets the value of the recordImplements property.
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
     * Sets the value of the recordImplements property.
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
     * Gets the value of the interfaceClass property.
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
     * Sets the value of the interfaceClass property.
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
     * Gets the value of the interfaceImplements property.
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
     * Sets the value of the interfaceImplements property.
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
     * Gets the value of the daoClass property.
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
     * Sets the value of the daoClass property.
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
     * Gets the value of the daoImplements property.
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
     * Sets the value of the daoImplements property.
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
     * Gets the value of the pojoClass property.
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
     * Sets the value of the pojoClass property.
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
     * Gets the value of the pojoExtends property.
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
     * Sets the value of the pojoExtends property.
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
     * Gets the value of the pojoImplements property.
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
     * Sets the value of the pojoImplements property.
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
