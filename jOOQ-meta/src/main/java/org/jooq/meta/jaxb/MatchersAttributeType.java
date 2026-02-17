
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
 * Declarative naming strategy configuration for UDT attribute names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersAttributeType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersAttributeType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32012L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule attributeIdentifier;
    protected MatcherRule attributeMember;
    protected MatcherRule attributeSetter;
    protected MatcherRule attributeGetter;
    @XmlElement(defaultValue = "false")
    protected Boolean udtMemberOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordSetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordGetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordMemberOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordTypeSetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordTypeGetterOverride = false;
    @XmlElement(defaultValue = "false")
    protected Boolean recordTypeMemberOverride = false;
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
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public MatcherRule getAttributeIdentifier() {
        return attributeIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public void setAttributeIdentifier(MatcherRule value) {
        this.attributeIdentifier = value;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeMember() {
        return attributeMember;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeMember(MatcherRule value) {
        this.attributeMember = value;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeSetter() {
        return attributeSetter;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeSetter(MatcherRule value) {
        this.attributeSetter = value;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatcherRule getAttributeGetter() {
        return attributeGetter;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public void setAttributeGetter(MatcherRule value) {
        this.attributeGetter = value;
    }

    /**
     * Whether an "override" modifier in generated udt members (table fields) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUdtMemberOverride() {
        return udtMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated udt members (table fields) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUdtMemberOverride(Boolean value) {
        this.udtMemberOverride = value;
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
     * Whether an "override" modifier in generated record type setters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordTypeSetterOverride() {
        return recordTypeSetterOverride;
    }

    /**
     * Whether an "override" modifier in generated record type setters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordTypeSetterOverride(Boolean value) {
        this.recordTypeSetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated record type getters should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordTypeGetterOverride() {
        return recordTypeGetterOverride;
    }

    /**
     * Whether an "override" modifier in generated record type getters should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordTypeGetterOverride(Boolean value) {
        this.recordTypeGetterOverride = value;
    }

    /**
     * Whether an "override" modifier in generated record type members (properties) should be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordTypeMemberOverride() {
        return recordTypeMemberOverride;
    }

    /**
     * Whether an "override" modifier in generated record type members (properties) should be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordTypeMemberOverride(Boolean value) {
        this.recordTypeMemberOverride = value;
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
     * This field matcher applies to all unqualified or qualified UDT attribute names matched by this expression. If left empty, this matcher applies to all fields.
     * 
     */
    public MatchersAttributeType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTField} identifier.
     * 
     */
    public MatchersAttributeType withAttributeIdentifier(MatcherRule value) {
        setAttributeIdentifier(value);
        return this;
    }

    /**
     * This rule influences the naming of generated members (e.g. in POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeMember(MatcherRule value) {
        setAttributeMember(value);
        return this;
    }

    /**
     * This rule influences the naming of generated setters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeSetter(MatcherRule value) {
        setAttributeSetter(value);
        return this;
    }

    /**
     * This rule influences the naming of generated getters (e.g. in {@link org.jooq.UDTRecord} and/or POJOs) corresponding to this {@link org.jooq.UDTField}
     * 
     */
    public MatchersAttributeType withAttributeGetter(MatcherRule value) {
        setAttributeGetter(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated udt members (table fields) should be generated.
     * 
     */
    public MatchersAttributeType withUdtMemberOverride(Boolean value) {
        setUdtMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record setters should be generated.
     * 
     */
    public MatchersAttributeType withRecordSetterOverride(Boolean value) {
        setRecordSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record getters should be generated.
     * 
     */
    public MatchersAttributeType withRecordGetterOverride(Boolean value) {
        setRecordGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record members (properties) should be generated.
     * 
     */
    public MatchersAttributeType withRecordMemberOverride(Boolean value) {
        setRecordMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record type setters should be generated.
     * 
     */
    public MatchersAttributeType withRecordTypeSetterOverride(Boolean value) {
        setRecordTypeSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record type getters should be generated.
     * 
     */
    public MatchersAttributeType withRecordTypeGetterOverride(Boolean value) {
        setRecordTypeGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated record type members (properties) should be generated.
     * 
     */
    public MatchersAttributeType withRecordTypeMemberOverride(Boolean value) {
        setRecordTypeMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface setters should be generated.
     * 
     */
    public MatchersAttributeType withInterfaceSetterOverride(Boolean value) {
        setInterfaceSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface getters should be generated.
     * 
     */
    public MatchersAttributeType withInterfaceGetterOverride(Boolean value) {
        setInterfaceGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated interface members (properties) should be generated.
     * 
     */
    public MatchersAttributeType withInterfaceMemberOverride(Boolean value) {
        setInterfaceMemberOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO setters should be generated.
     * 
     */
    public MatchersAttributeType withPojoSetterOverride(Boolean value) {
        setPojoSetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO getters should be generated.
     * 
     */
    public MatchersAttributeType withPojoGetterOverride(Boolean value) {
        setPojoGetterOverride(value);
        return this;
    }

    /**
     * Whether an "override" modifier in generated POJO members (properties) should be generated.
     * 
     */
    public MatchersAttributeType withPojoMemberOverride(Boolean value) {
        setPojoMemberOverride(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("attributeIdentifier", attributeIdentifier);
        builder.append("attributeMember", attributeMember);
        builder.append("attributeSetter", attributeSetter);
        builder.append("attributeGetter", attributeGetter);
        builder.append("udtMemberOverride", udtMemberOverride);
        builder.append("recordSetterOverride", recordSetterOverride);
        builder.append("recordGetterOverride", recordGetterOverride);
        builder.append("recordMemberOverride", recordMemberOverride);
        builder.append("recordTypeSetterOverride", recordTypeSetterOverride);
        builder.append("recordTypeGetterOverride", recordTypeGetterOverride);
        builder.append("recordTypeMemberOverride", recordTypeMemberOverride);
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
        MatchersAttributeType other = ((MatchersAttributeType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (attributeIdentifier == null) {
            if (other.attributeIdentifier!= null) {
                return false;
            }
        } else {
            if (!attributeIdentifier.equals(other.attributeIdentifier)) {
                return false;
            }
        }
        if (attributeMember == null) {
            if (other.attributeMember!= null) {
                return false;
            }
        } else {
            if (!attributeMember.equals(other.attributeMember)) {
                return false;
            }
        }
        if (attributeSetter == null) {
            if (other.attributeSetter!= null) {
                return false;
            }
        } else {
            if (!attributeSetter.equals(other.attributeSetter)) {
                return false;
            }
        }
        if (attributeGetter == null) {
            if (other.attributeGetter!= null) {
                return false;
            }
        } else {
            if (!attributeGetter.equals(other.attributeGetter)) {
                return false;
            }
        }
        if (udtMemberOverride == null) {
            if (other.udtMemberOverride!= null) {
                return false;
            }
        } else {
            if (!udtMemberOverride.equals(other.udtMemberOverride)) {
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
        if (recordTypeSetterOverride == null) {
            if (other.recordTypeSetterOverride!= null) {
                return false;
            }
        } else {
            if (!recordTypeSetterOverride.equals(other.recordTypeSetterOverride)) {
                return false;
            }
        }
        if (recordTypeGetterOverride == null) {
            if (other.recordTypeGetterOverride!= null) {
                return false;
            }
        } else {
            if (!recordTypeGetterOverride.equals(other.recordTypeGetterOverride)) {
                return false;
            }
        }
        if (recordTypeMemberOverride == null) {
            if (other.recordTypeMemberOverride!= null) {
                return false;
            }
        } else {
            if (!recordTypeMemberOverride.equals(other.recordTypeMemberOverride)) {
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
        result = ((prime*result)+((attributeIdentifier == null)? 0 :attributeIdentifier.hashCode()));
        result = ((prime*result)+((attributeMember == null)? 0 :attributeMember.hashCode()));
        result = ((prime*result)+((attributeSetter == null)? 0 :attributeSetter.hashCode()));
        result = ((prime*result)+((attributeGetter == null)? 0 :attributeGetter.hashCode()));
        result = ((prime*result)+((udtMemberOverride == null)? 0 :udtMemberOverride.hashCode()));
        result = ((prime*result)+((recordSetterOverride == null)? 0 :recordSetterOverride.hashCode()));
        result = ((prime*result)+((recordGetterOverride == null)? 0 :recordGetterOverride.hashCode()));
        result = ((prime*result)+((recordMemberOverride == null)? 0 :recordMemberOverride.hashCode()));
        result = ((prime*result)+((recordTypeSetterOverride == null)? 0 :recordTypeSetterOverride.hashCode()));
        result = ((prime*result)+((recordTypeGetterOverride == null)? 0 :recordTypeGetterOverride.hashCode()));
        result = ((prime*result)+((recordTypeMemberOverride == null)? 0 :recordTypeMemberOverride.hashCode()));
        result = ((prime*result)+((interfaceSetterOverride == null)? 0 :interfaceSetterOverride.hashCode()));
        result = ((prime*result)+((interfaceGetterOverride == null)? 0 :interfaceGetterOverride.hashCode()));
        result = ((prime*result)+((interfaceMemberOverride == null)? 0 :interfaceMemberOverride.hashCode()));
        result = ((prime*result)+((pojoSetterOverride == null)? 0 :pojoSetterOverride.hashCode()));
        result = ((prime*result)+((pojoGetterOverride == null)? 0 :pojoGetterOverride.hashCode()));
        result = ((prime*result)+((pojoMemberOverride == null)? 0 :pojoMemberOverride.hashCode()));
        return result;
    }

}
