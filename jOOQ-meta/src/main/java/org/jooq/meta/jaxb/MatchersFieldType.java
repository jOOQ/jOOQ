
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
 * Declarative naming strategy configuration for field names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersFieldType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersFieldType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32012L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule fieldIdentifier;
    protected MatcherRule fieldMember;
    protected MatcherRule fieldSetter;
    protected MatcherRule fieldGetter;
    protected MatcherRule daoMember;
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
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     * 
     */
    public MatcherRule getFieldIdentifier() {
        return fieldIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     * 
     */
    public void setFieldIdentifier(MatcherRule value) {
        this.fieldIdentifier = value;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatcherRule getFieldMember() {
        return fieldMember;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public void setFieldMember(MatcherRule value) {
        this.fieldMember = value;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatcherRule getFieldSetter() {
        return fieldSetter;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public void setFieldSetter(MatcherRule value) {
        this.fieldSetter = value;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatcherRule getFieldGetter() {
        return fieldGetter;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public void setFieldGetter(MatcherRule value) {
        this.fieldGetter = value;
    }

    /**
     * This rule influences the naming of generated members and member suffixes in DAO methods corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatcherRule getDaoMember() {
        return daoMember;
    }

    /**
     * This rule influences the naming of generated members and member suffixes in DAO methods corresponding to this {@link org.jooq.Field}
     * 
     */
    public void setDaoMember(MatcherRule value) {
        this.daoMember = value;
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
     * This field matcher applies to all unqualified or qualified field names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public MatchersFieldType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Field} identifier.
     * 
     */
    public MatchersFieldType withFieldIdentifier(MatcherRule value) {
        setFieldIdentifier(value);
        return this;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatchersFieldType withFieldMember(MatcherRule value) {
        setFieldMember(value);
        return this;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatchersFieldType withFieldSetter(MatcherRule value) {
        setFieldSetter(value);
        return this;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.TableRecord} and/or POJOs) corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatchersFieldType withFieldGetter(MatcherRule value) {
        setFieldGetter(value);
        return this;
    }

    /**
     * This rule influences the naming of generated members and member suffixes in DAO methods corresponding to this {@link org.jooq.Field}
     * 
     */
    public MatchersFieldType withDaoMember(MatcherRule value) {
        setDaoMember(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated table members (table fields) should be generated.
     * 
     */
    public MatchersFieldType withTableMemberOverride(Boolean value) {
        setTableMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record setters should be generated.
     * 
     */
    public MatchersFieldType withRecordSetterOverride(Boolean value) {
        setRecordSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record getters should be generated.
     * 
     */
    public MatchersFieldType withRecordGetterOverride(Boolean value) {
        setRecordGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record members (properties) should be generated.
     * 
     */
    public MatchersFieldType withRecordMemberOverride(Boolean value) {
        setRecordMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface setters should be generated.
     * 
     */
    public MatchersFieldType withInterfaceSetterOverride(Boolean value) {
        setInterfaceSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface getters should be generated.
     * 
     */
    public MatchersFieldType withInterfaceGetterOverride(Boolean value) {
        setInterfaceGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface members (properties) should be generated.
     * 
     */
    public MatchersFieldType withInterfaceMemberOverride(Boolean value) {
        setInterfaceMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO setters should be generated.
     * 
     */
    public MatchersFieldType withPojoSetterOverride(Boolean value) {
        setPojoSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO getters should be generated.
     * 
     */
    public MatchersFieldType withPojoGetterOverride(Boolean value) {
        setPojoGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO members (properties) should be generated.
     * 
     */
    public MatchersFieldType withPojoMemberOverride(Boolean value) {
        setPojoMemberOverride(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("fieldIdentifier", fieldIdentifier);
        builder.append("fieldMember", fieldMember);
        builder.append("fieldSetter", fieldSetter);
        builder.append("fieldGetter", fieldGetter);
        builder.append("daoMember", daoMember);
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
        MatchersFieldType other = ((MatchersFieldType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (fieldIdentifier == null) {
            if (other.fieldIdentifier!= null) {
                return false;
            }
        } else {
            if (!fieldIdentifier.equals(other.fieldIdentifier)) {
                return false;
            }
        }
        if (fieldMember == null) {
            if (other.fieldMember!= null) {
                return false;
            }
        } else {
            if (!fieldMember.equals(other.fieldMember)) {
                return false;
            }
        }
        if (fieldSetter == null) {
            if (other.fieldSetter!= null) {
                return false;
            }
        } else {
            if (!fieldSetter.equals(other.fieldSetter)) {
                return false;
            }
        }
        if (fieldGetter == null) {
            if (other.fieldGetter!= null) {
                return false;
            }
        } else {
            if (!fieldGetter.equals(other.fieldGetter)) {
                return false;
            }
        }
        if (daoMember == null) {
            if (other.daoMember!= null) {
                return false;
            }
        } else {
            if (!daoMember.equals(other.daoMember)) {
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
        result = ((prime*result)+((fieldIdentifier == null)? 0 :fieldIdentifier.hashCode()));
        result = ((prime*result)+((fieldMember == null)? 0 :fieldMember.hashCode()));
        result = ((prime*result)+((fieldSetter == null)? 0 :fieldSetter.hashCode()));
        result = ((prime*result)+((fieldGetter == null)? 0 :fieldGetter.hashCode()));
        result = ((prime*result)+((daoMember == null)? 0 :daoMember.hashCode()));
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
