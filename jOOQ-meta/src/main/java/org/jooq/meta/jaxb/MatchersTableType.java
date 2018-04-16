







package org.jooq.meta.jaxb;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (expression!= null) {
            sb.append("<expression>");
            sb.append(expression);
            sb.append("</expression>");
        }
        if (tableClass!= null) {
            sb.append("<tableClass>");
            sb.append(tableClass);
            sb.append("</tableClass>");
        }
        if (tableIdentifier!= null) {
            sb.append("<tableIdentifier>");
            sb.append(tableIdentifier);
            sb.append("</tableIdentifier>");
        }
        if (tableImplements!= null) {
            sb.append("<tableImplements>");
            sb.append(tableImplements);
            sb.append("</tableImplements>");
        }
        if (recordClass!= null) {
            sb.append("<recordClass>");
            sb.append(recordClass);
            sb.append("</recordClass>");
        }
        if (recordImplements!= null) {
            sb.append("<recordImplements>");
            sb.append(recordImplements);
            sb.append("</recordImplements>");
        }
        if (interfaceClass!= null) {
            sb.append("<interfaceClass>");
            sb.append(interfaceClass);
            sb.append("</interfaceClass>");
        }
        if (interfaceImplements!= null) {
            sb.append("<interfaceImplements>");
            sb.append(interfaceImplements);
            sb.append("</interfaceImplements>");
        }
        if (daoClass!= null) {
            sb.append("<daoClass>");
            sb.append(daoClass);
            sb.append("</daoClass>");
        }
        if (daoImplements!= null) {
            sb.append("<daoImplements>");
            sb.append(daoImplements);
            sb.append("</daoImplements>");
        }
        if (pojoClass!= null) {
            sb.append("<pojoClass>");
            sb.append(pojoClass);
            sb.append("</pojoClass>");
        }
        if (pojoExtends!= null) {
            sb.append("<pojoExtends>");
            sb.append(pojoExtends);
            sb.append("</pojoExtends>");
        }
        if (pojoImplements!= null) {
            sb.append("<pojoImplements>");
            sb.append(pojoImplements);
            sb.append("</pojoImplements>");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        MatchersTableType other = ((MatchersTableType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (tableClass == null) {
            if (other.tableClass!= null) {
                return false;
            }
        } else {
            if (!tableClass.equals(other.tableClass)) {
                return false;
            }
        }
        if (tableIdentifier == null) {
            if (other.tableIdentifier!= null) {
                return false;
            }
        } else {
            if (!tableIdentifier.equals(other.tableIdentifier)) {
                return false;
            }
        }
        if (tableImplements == null) {
            if (other.tableImplements!= null) {
                return false;
            }
        } else {
            if (!tableImplements.equals(other.tableImplements)) {
                return false;
            }
        }
        if (recordClass == null) {
            if (other.recordClass!= null) {
                return false;
            }
        } else {
            if (!recordClass.equals(other.recordClass)) {
                return false;
            }
        }
        if (recordImplements == null) {
            if (other.recordImplements!= null) {
                return false;
            }
        } else {
            if (!recordImplements.equals(other.recordImplements)) {
                return false;
            }
        }
        if (interfaceClass == null) {
            if (other.interfaceClass!= null) {
                return false;
            }
        } else {
            if (!interfaceClass.equals(other.interfaceClass)) {
                return false;
            }
        }
        if (interfaceImplements == null) {
            if (other.interfaceImplements!= null) {
                return false;
            }
        } else {
            if (!interfaceImplements.equals(other.interfaceImplements)) {
                return false;
            }
        }
        if (daoClass == null) {
            if (other.daoClass!= null) {
                return false;
            }
        } else {
            if (!daoClass.equals(other.daoClass)) {
                return false;
            }
        }
        if (daoImplements == null) {
            if (other.daoImplements!= null) {
                return false;
            }
        } else {
            if (!daoImplements.equals(other.daoImplements)) {
                return false;
            }
        }
        if (pojoClass == null) {
            if (other.pojoClass!= null) {
                return false;
            }
        } else {
            if (!pojoClass.equals(other.pojoClass)) {
                return false;
            }
        }
        if (pojoExtends == null) {
            if (other.pojoExtends!= null) {
                return false;
            }
        } else {
            if (!pojoExtends.equals(other.pojoExtends)) {
                return false;
            }
        }
        if (pojoImplements == null) {
            if (other.pojoImplements!= null) {
                return false;
            }
        } else {
            if (!pojoImplements.equals(other.pojoImplements)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((tableClass == null)? 0 :tableClass.hashCode()));
        result = ((prime*result)+((tableIdentifier == null)? 0 :tableIdentifier.hashCode()));
        result = ((prime*result)+((tableImplements == null)? 0 :tableImplements.hashCode()));
        result = ((prime*result)+((recordClass == null)? 0 :recordClass.hashCode()));
        result = ((prime*result)+((recordImplements == null)? 0 :recordImplements.hashCode()));
        result = ((prime*result)+((interfaceClass == null)? 0 :interfaceClass.hashCode()));
        result = ((prime*result)+((interfaceImplements == null)? 0 :interfaceImplements.hashCode()));
        result = ((prime*result)+((daoClass == null)? 0 :daoClass.hashCode()));
        result = ((prime*result)+((daoImplements == null)? 0 :daoImplements.hashCode()));
        result = ((prime*result)+((pojoClass == null)? 0 :pojoClass.hashCode()));
        result = ((prime*result)+((pojoExtends == null)? 0 :pojoExtends.hashCode()));
        result = ((prime*result)+((pojoImplements == null)? 0 :pojoImplements.hashCode()));
        return result;
    }

}
