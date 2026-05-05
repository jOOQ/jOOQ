
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Declarative naming strategy configuration for embeddable names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersEmbeddableType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersEmbeddableType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32200L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule recordClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordImplements;
    protected MatcherRule interfaceClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String interfaceImplements;
    protected MatcherRule pojoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoImplements;
    @XmlElement(defaultValue = "false")
    protected Boolean tableMemberOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordSetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordGetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordMemberOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaceSetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaceGetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaceMemberOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean pojoSetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean pojoGetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean pojoMemberOverride = false;

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public MatcherRule getRecordClass() {
        return recordClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public void setRecordClass(MatcherRule value) {
        this.recordClass = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.EmbeddableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.EmbeddableRecord} does, so to minimise
     * unexpected behaviour, custom embeddable record super classes should extend {@link org.jooq.impl.EmbeddableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getRecordExtends() {
        return recordExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.EmbeddableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.EmbeddableRecord} does, so to minimise
     * unexpected behaviour, custom embeddable record super classes should extend {@link org.jooq.impl.EmbeddableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setRecordExtends(String value) {
        this.recordExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public String getRecordImplements() {
        return recordImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public void setRecordImplements(String value) {
        this.recordImplements = value;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public MatcherRule getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public void setInterfaceClass(MatcherRule value) {
        this.interfaceClass = value;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public String getInterfaceImplements() {
        return interfaceImplements;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public void setInterfaceImplements(String value) {
        this.interfaceImplements = value;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatcherRule getPojoClass() {
        return pojoClass;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public void setPojoClass(MatcherRule value) {
        this.pojoClass = value;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public String getPojoExtends() {
        return pojoExtends;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public void setPojoExtends(String value) {
        this.pojoExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public String getPojoImplements() {
        return pojoImplements;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public void setPojoImplements(String value) {
        this.pojoImplements = value;
    }

    /**
     * Whether an "override" modifier in generated table members (table fields) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTableMemberOverride() {
        return tableMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated table members (table fields) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTableMemberOverride(Boolean value) {
        this.tableMemberOverride = value;
    }

    /**
     * Whether an "override" modifier in generated record setters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordSetterOverride() {
        return recordSetterOverride;
    }

    /**
     * Whether an "override" modifier in generated record setters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordSetterOverride(Boolean value) {
        this.recordSetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated record getters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordGetterOverride() {
        return recordGetterOverride;
    }

    /**
     * Whether an "override" modifier in generated record getters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordGetterOverride(Boolean value) {
        this.recordGetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated record members (properties) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordMemberOverride() {
        return recordMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated record members (properties) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordMemberOverride(Boolean value) {
        this.recordMemberOverride = value;
    }

    /**
     * Whether an "override" modifier in generated interface setters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterfaceSetterOverride() {
        return interfaceSetterOverride;
    }

    /**
     * Whether an "override" modifier in generated interface setters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterfaceSetterOverride(Boolean value) {
        this.interfaceSetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated interface getters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterfaceGetterOverride() {
        return interfaceGetterOverride;
    }

    /**
     * Whether an "override" modifier in generated interface getters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterfaceGetterOverride(Boolean value) {
        this.interfaceGetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated interface members (properties) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterfaceMemberOverride() {
        return interfaceMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated interface members (properties) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterfaceMemberOverride(Boolean value) {
        this.interfaceMemberOverride = value;
    }

    /**
     * Whether an "override" modifier in generated POJO setters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojoSetterOverride() {
        return pojoSetterOverride;
    }

    /**
     * Whether an "override" modifier in generated POJO setters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojoSetterOverride(Boolean value) {
        this.pojoSetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated POJO getters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojoGetterOverride() {
        return pojoGetterOverride;
    }

    /**
     * Whether an "override" modifier in generated POJO getters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojoGetterOverride(Boolean value) {
        this.pojoGetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated POJO members (properties) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojoMemberOverride() {
        return pojoMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated POJO members (properties) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojoMemberOverride(Boolean value) {
        this.pojoMemberOverride = value;
    }

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public MatchersEmbeddableType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public MatchersEmbeddableType withRecordClass(MatcherRule value) {
        setRecordClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.EmbeddableRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.EmbeddableRecord} does, so to minimise
     * unexpected behaviour, custom embeddable record super classes should extend {@link org.jooq.impl.EmbeddableRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersEmbeddableType withRecordExtends(String value) {
        setRecordExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public MatchersEmbeddableType withRecordImplements(String value) {
        setRecordImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public MatchersEmbeddableType withInterfaceClass(MatcherRule value) {
        setInterfaceClass(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public MatchersEmbeddableType withInterfaceImplements(String value) {
        setInterfaceImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatchersEmbeddableType withPojoClass(MatcherRule value) {
        setPojoClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public MatchersEmbeddableType withPojoExtends(String value) {
        setPojoExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public MatchersEmbeddableType withPojoImplements(String value) {
        setPojoImplements(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated table members (table fields) should be generated.
     * 
     */
    public MatchersEmbeddableType withTableMemberOverride(Boolean value) {
        setTableMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record setters should be generated.
     * 
     */
    public MatchersEmbeddableType withRecordSetterOverride(Boolean value) {
        setRecordSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record getters should be generated.
     * 
     */
    public MatchersEmbeddableType withRecordGetterOverride(Boolean value) {
        setRecordGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record members (properties) should be generated.
     * 
     */
    public MatchersEmbeddableType withRecordMemberOverride(Boolean value) {
        setRecordMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface setters should be generated.
     * 
     */
    public MatchersEmbeddableType withInterfaceSetterOverride(Boolean value) {
        setInterfaceSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface getters should be generated.
     * 
     */
    public MatchersEmbeddableType withInterfaceGetterOverride(Boolean value) {
        setInterfaceGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface members (properties) should be generated.
     * 
     */
    public MatchersEmbeddableType withInterfaceMemberOverride(Boolean value) {
        setInterfaceMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO setters should be generated.
     * 
     */
    public MatchersEmbeddableType withPojoSetterOverride(Boolean value) {
        setPojoSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO getters should be generated.
     * 
     */
    public MatchersEmbeddableType withPojoGetterOverride(Boolean value) {
        setPojoGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO members (properties) should be generated.
     * 
     */
    public MatchersEmbeddableType withPojoMemberOverride(Boolean value) {
        setPojoMemberOverride(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("recordClass", recordClass);
        builder.append("recordExtends", recordExtends);
        builder.append("recordImplements", recordImplements);
        builder.append("interfaceClass", interfaceClass);
        builder.append("interfaceImplements", interfaceImplements);
        builder.append("pojoClass", pojoClass);
        builder.append("pojoExtends", pojoExtends);
        builder.append("pojoImplements", pojoImplements);
        builder.append("tableMemberOverride", tableMemberOverride);
        builder.append("recordSetterOverride", recordSetterOverride);
        builder.append("recordGetterOverride", recordGetterOverride);
        builder.append("recordMemberOverride", recordMemberOverride);
        builder.append("interfaceSetterOverride", interfaceSetterOverride);
        builder.append("interfaceGetterOverride", interfaceGetterOverride);
        builder.append("interfaceMemberOverride", interfaceMemberOverride);
        builder.append("pojoSetterOverride", pojoSetterOverride);
        builder.append("pojoGetterOverride", pojoGetterOverride);
        builder.append("pojoMemberOverride", pojoMemberOverride);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
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
        MatchersEmbeddableType other = ((MatchersEmbeddableType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
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
        if (recordExtends == null) {
            if (other.recordExtends!= null) {
                return false;
            }
        } else {
            if (!recordExtends.equals(other.recordExtends)) {
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
        if (tableMemberOverride == null) {
            if (other.tableMemberOverride!= null) {
                return false;
            }
        } else {
            if (!tableMemberOverride.equals(other.tableMemberOverride)) {
                return false;
            }
        }
        if (recordSetterOverride == null) {
            if (other.recordSetterOverride!= null) {
                return false;
            }
        } else {
            if (!recordSetterOverride.equals(other.recordSetterOverride)) {
                return false;
            }
        }
        if (recordGetterOverride == null) {
            if (other.recordGetterOverride!= null) {
                return false;
            }
        } else {
            if (!recordGetterOverride.equals(other.recordGetterOverride)) {
                return false;
            }
        }
        if (recordMemberOverride == null) {
            if (other.recordMemberOverride!= null) {
                return false;
            }
        } else {
            if (!recordMemberOverride.equals(other.recordMemberOverride)) {
                return false;
            }
        }
        if (interfaceSetterOverride == null) {
            if (other.interfaceSetterOverride!= null) {
                return false;
            }
        } else {
            if (!interfaceSetterOverride.equals(other.interfaceSetterOverride)) {
                return false;
            }
        }
        if (interfaceGetterOverride == null) {
            if (other.interfaceGetterOverride!= null) {
                return false;
            }
        } else {
            if (!interfaceGetterOverride.equals(other.interfaceGetterOverride)) {
                return false;
            }
        }
        if (interfaceMemberOverride == null) {
            if (other.interfaceMemberOverride!= null) {
                return false;
            }
        } else {
            if (!interfaceMemberOverride.equals(other.interfaceMemberOverride)) {
                return false;
            }
        }
        if (pojoSetterOverride == null) {
            if (other.pojoSetterOverride!= null) {
                return false;
            }
        } else {
            if (!pojoSetterOverride.equals(other.pojoSetterOverride)) {
                return false;
            }
        }
        if (pojoGetterOverride == null) {
            if (other.pojoGetterOverride!= null) {
                return false;
            }
        } else {
            if (!pojoGetterOverride.equals(other.pojoGetterOverride)) {
                return false;
            }
        }
        if (pojoMemberOverride == null) {
            if (other.pojoMemberOverride!= null) {
                return false;
            }
        } else {
            if (!pojoMemberOverride.equals(other.pojoMemberOverride)) {
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
        result = ((prime*result)+((recordClass == null)? 0 :recordClass.hashCode()));
        result = ((prime*result)+((recordExtends == null)? 0 :recordExtends.hashCode()));
        result = ((prime*result)+((recordImplements == null)? 0 :recordImplements.hashCode()));
        result = ((prime*result)+((interfaceClass == null)? 0 :interfaceClass.hashCode()));
        result = ((prime*result)+((interfaceImplements == null)? 0 :interfaceImplements.hashCode()));
        result = ((prime*result)+((pojoClass == null)? 0 :pojoClass.hashCode()));
        result = ((prime*result)+((pojoExtends == null)? 0 :pojoExtends.hashCode()));
        result = ((prime*result)+((pojoImplements == null)? 0 :pojoImplements.hashCode()));
        result = ((prime*result)+((tableMemberOverride == null)? 0 :tableMemberOverride.hashCode()));
        result = ((prime*result)+((recordSetterOverride == null)? 0 :recordSetterOverride.hashCode()));
        result = ((prime*result)+((recordGetterOverride == null)? 0 :recordGetterOverride.hashCode()));
        result = ((prime*result)+((recordMemberOverride == null)? 0 :recordMemberOverride.hashCode()));
        result = ((prime*result)+((interfaceSetterOverride == null)? 0 :interfaceSetterOverride.hashCode()));
        result = ((prime*result)+((interfaceGetterOverride == null)? 0 :interfaceGetterOverride.hashCode()));
        result = ((prime*result)+((interfaceMemberOverride == null)? 0 :interfaceMemberOverride.hashCode()));
        result = ((prime*result)+((pojoSetterOverride == null)? 0 :pojoSetterOverride.hashCode()));
        result = ((prime*result)+((pojoGetterOverride == null)? 0 :pojoGetterOverride.hashCode()));
        result = ((prime*result)+((pojoMemberOverride == null)? 0 :pojoMemberOverride.hashCode()));
        return result;
    }

}
