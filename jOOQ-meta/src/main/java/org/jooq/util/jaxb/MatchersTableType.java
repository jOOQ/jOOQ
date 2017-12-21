







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Declarative naming strategy configuration for table names.
 *
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

    private final static long serialVersionUID = 31100L;
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
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
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
     * This rule influences the naming of the generated {@link org.jooq.Table} object.
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
     * This rule influences the naming of the generated {@link org.jooq.Table} identifier.
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
     * This string provides additional interfaces that a generated {@link org.jooq.Table} should implement.
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
     * This rule influences the naming of the generated {@link org.jooq.TableRecord} object.
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
     * This string provides additional interfaces that a generated {@link org.jooq.TableRecord} should implement.
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
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.TableRecord} and/or the POJO.
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
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.TableRecord} and/or POJO) should implement.
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
     * This rule influences the naming of the generated {@link org.jooq.DAO} object.
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
     * This string provides additional interfaces that a generated {@link org.jooq.DAO} should implement.
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
     * This rule influences the naming of the generated POJOs object.
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
     * This string provides a super class that a generated POJO should extend.
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
     * This string provides additional interfaces that a generated POJO should implement.
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
