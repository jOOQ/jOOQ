
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * @deprecated Use ForcedType only
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomType", propOrder = {

})
@Deprecated
@SuppressWarnings({
    "all"
})
public class CustomType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31902L;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String type;
    @XmlSchemaType(name = "string")
    protected VisibilityModifier visibilityModifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String generator;
    protected Boolean auditInsertTimestamp;
    protected Boolean auditInsertUser;
    protected Boolean auditUpdateTimestamp;
    protected Boolean auditUpdateUser;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String converter;
    protected Boolean genericConverter;
    protected Boolean autoConverter;
    protected Boolean enumConverter;
    protected Boolean xmlConverter;
    protected Boolean jsonConverter;
    protected LambdaConverter lambdaConverter;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String binding;
    protected Boolean genericBinding;

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public String getName() {
        return name;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setName(String value) {
        this.name = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public String getType() {
        return type;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setType(String value) {
        this.type = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setVisibilityModifier(VisibilityModifier value) {
        this.visibilityModifier = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public String getGenerator() {
        return generator;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setGenerator(String value) {
        this.generator = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isAuditInsertTimestamp() {
        return auditInsertTimestamp;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setAuditInsertTimestamp(Boolean value) {
        this.auditInsertTimestamp = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isAuditInsertUser() {
        return auditInsertUser;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setAuditInsertUser(Boolean value) {
        this.auditInsertUser = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isAuditUpdateTimestamp() {
        return auditUpdateTimestamp;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setAuditUpdateTimestamp(Boolean value) {
        this.auditUpdateTimestamp = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isAuditUpdateUser() {
        return auditUpdateUser;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setAuditUpdateUser(Boolean value) {
        this.auditUpdateUser = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public String getConverter() {
        return converter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setConverter(String value) {
        this.converter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isGenericConverter() {
        return genericConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setGenericConverter(Boolean value) {
        this.genericConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isAutoConverter() {
        return autoConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setAutoConverter(Boolean value) {
        this.autoConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isEnumConverter() {
        return enumConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setEnumConverter(Boolean value) {
        this.enumConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isXmlConverter() {
        return xmlConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setXmlConverter(Boolean value) {
        this.xmlConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isJsonConverter() {
        return jsonConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setJsonConverter(Boolean value) {
        this.jsonConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public LambdaConverter getLambdaConverter() {
        return lambdaConverter;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setLambdaConverter(LambdaConverter value) {
        this.lambdaConverter = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public String getBinding() {
        return binding;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public void setBinding(String value) {
        this.binding = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isGenericBinding() {
        return genericBinding;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setGenericBinding(Boolean value) {
        this.genericBinding = value;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withType(String value) {
        setType(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withVisibilityModifier(VisibilityModifier value) {
        setVisibilityModifier(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withGenerator(String value) {
        setGenerator(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withAuditInsertTimestamp(Boolean value) {
        setAuditInsertTimestamp(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withAuditInsertUser(Boolean value) {
        setAuditInsertUser(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withAuditUpdateTimestamp(Boolean value) {
        setAuditUpdateTimestamp(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withAuditUpdateUser(Boolean value) {
        setAuditUpdateUser(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withConverter(String value) {
        setConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withGenericConverter(Boolean value) {
        setGenericConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withAutoConverter(Boolean value) {
        setAutoConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withEnumConverter(Boolean value) {
        setEnumConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withXmlConverter(Boolean value) {
        setXmlConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withJsonConverter(Boolean value) {
        setJsonConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withLambdaConverter(LambdaConverter value) {
        setLambdaConverter(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    @Deprecated
    public CustomType withBinding(String value) {
        setBinding(value);
        return this;
    }

    /**
     * @deprecated Use ForcedType only
     * 
     */
    public CustomType withGenericBinding(Boolean value) {
        setGenericBinding(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("type", type);
        builder.append("visibilityModifier", visibilityModifier);
        builder.append("generator", generator);
        builder.append("auditInsertTimestamp", auditInsertTimestamp);
        builder.append("auditInsertUser", auditInsertUser);
        builder.append("auditUpdateTimestamp", auditUpdateTimestamp);
        builder.append("auditUpdateUser", auditUpdateUser);
        builder.append("converter", converter);
        builder.append("genericConverter", genericConverter);
        builder.append("autoConverter", autoConverter);
        builder.append("enumConverter", enumConverter);
        builder.append("xmlConverter", xmlConverter);
        builder.append("jsonConverter", jsonConverter);
        builder.append("lambdaConverter", lambdaConverter);
        builder.append("binding", binding);
        builder.append("genericBinding", genericBinding);
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
        CustomType other = ((CustomType) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (type == null) {
            if (other.type!= null) {
                return false;
            }
        } else {
            if (!type.equals(other.type)) {
                return false;
            }
        }
        if (visibilityModifier == null) {
            if (other.visibilityModifier!= null) {
                return false;
            }
        } else {
            if (!visibilityModifier.equals(other.visibilityModifier)) {
                return false;
            }
        }
        if (generator == null) {
            if (other.generator!= null) {
                return false;
            }
        } else {
            if (!generator.equals(other.generator)) {
                return false;
            }
        }
        if (auditInsertTimestamp == null) {
            if (other.auditInsertTimestamp!= null) {
                return false;
            }
        } else {
            if (!auditInsertTimestamp.equals(other.auditInsertTimestamp)) {
                return false;
            }
        }
        if (auditInsertUser == null) {
            if (other.auditInsertUser!= null) {
                return false;
            }
        } else {
            if (!auditInsertUser.equals(other.auditInsertUser)) {
                return false;
            }
        }
        if (auditUpdateTimestamp == null) {
            if (other.auditUpdateTimestamp!= null) {
                return false;
            }
        } else {
            if (!auditUpdateTimestamp.equals(other.auditUpdateTimestamp)) {
                return false;
            }
        }
        if (auditUpdateUser == null) {
            if (other.auditUpdateUser!= null) {
                return false;
            }
        } else {
            if (!auditUpdateUser.equals(other.auditUpdateUser)) {
                return false;
            }
        }
        if (converter == null) {
            if (other.converter!= null) {
                return false;
            }
        } else {
            if (!converter.equals(other.converter)) {
                return false;
            }
        }
        if (genericConverter == null) {
            if (other.genericConverter!= null) {
                return false;
            }
        } else {
            if (!genericConverter.equals(other.genericConverter)) {
                return false;
            }
        }
        if (autoConverter == null) {
            if (other.autoConverter!= null) {
                return false;
            }
        } else {
            if (!autoConverter.equals(other.autoConverter)) {
                return false;
            }
        }
        if (enumConverter == null) {
            if (other.enumConverter!= null) {
                return false;
            }
        } else {
            if (!enumConverter.equals(other.enumConverter)) {
                return false;
            }
        }
        if (xmlConverter == null) {
            if (other.xmlConverter!= null) {
                return false;
            }
        } else {
            if (!xmlConverter.equals(other.xmlConverter)) {
                return false;
            }
        }
        if (jsonConverter == null) {
            if (other.jsonConverter!= null) {
                return false;
            }
        } else {
            if (!jsonConverter.equals(other.jsonConverter)) {
                return false;
            }
        }
        if (lambdaConverter == null) {
            if (other.lambdaConverter!= null) {
                return false;
            }
        } else {
            if (!lambdaConverter.equals(other.lambdaConverter)) {
                return false;
            }
        }
        if (binding == null) {
            if (other.binding!= null) {
                return false;
            }
        } else {
            if (!binding.equals(other.binding)) {
                return false;
            }
        }
        if (genericBinding == null) {
            if (other.genericBinding!= null) {
                return false;
            }
        } else {
            if (!genericBinding.equals(other.genericBinding)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((type == null)? 0 :type.hashCode()));
        result = ((prime*result)+((visibilityModifier == null)? 0 :visibilityModifier.hashCode()));
        result = ((prime*result)+((generator == null)? 0 :generator.hashCode()));
        result = ((prime*result)+((auditInsertTimestamp == null)? 0 :auditInsertTimestamp.hashCode()));
        result = ((prime*result)+((auditInsertUser == null)? 0 :auditInsertUser.hashCode()));
        result = ((prime*result)+((auditUpdateTimestamp == null)? 0 :auditUpdateTimestamp.hashCode()));
        result = ((prime*result)+((auditUpdateUser == null)? 0 :auditUpdateUser.hashCode()));
        result = ((prime*result)+((converter == null)? 0 :converter.hashCode()));
        result = ((prime*result)+((genericConverter == null)? 0 :genericConverter.hashCode()));
        result = ((prime*result)+((autoConverter == null)? 0 :autoConverter.hashCode()));
        result = ((prime*result)+((enumConverter == null)? 0 :enumConverter.hashCode()));
        result = ((prime*result)+((xmlConverter == null)? 0 :xmlConverter.hashCode()));
        result = ((prime*result)+((jsonConverter == null)? 0 :jsonConverter.hashCode()));
        result = ((prime*result)+((lambdaConverter == null)? 0 :lambdaConverter.hashCode()));
        result = ((prime*result)+((binding == null)? 0 :binding.hashCode()));
        result = ((prime*result)+((genericBinding == null)? 0 :genericBinding.hashCode()));
        return result;
    }

}
