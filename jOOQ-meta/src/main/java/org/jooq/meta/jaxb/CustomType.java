
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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

    private final static long serialVersionUID = 31200L;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String type;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String converter;
    protected Boolean enumConverter;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String binding;

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
    public Boolean isEnumConverter() {
        return enumConverter;
    }

    /**
     * Sets the value of the enumConverter property.
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
    public CustomType withConverter(String value) {
        setConverter(value);
        return this;
    }

    public CustomType withEnumConverter(Boolean value) {
        setEnumConverter(value);
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

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("type", type);
        builder.append("converter", converter);
        builder.append("enumConverter", enumConverter);
        builder.append("binding", binding);
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
        if (converter == null) {
            if (other.converter!= null) {
                return false;
            }
        } else {
            if (!converter.equals(other.converter)) {
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
        if (binding == null) {
            if (other.binding!= null) {
                return false;
            }
        } else {
            if (!binding.equals(other.binding)) {
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
        result = ((prime*result)+((converter == null)? 0 :converter.hashCode()));
        result = ((prime*result)+((enumConverter == null)? 0 :enumConverter.hashCode()));
        result = ((prime*result)+((binding == null)? 0 :binding.hashCode()));
        return result;
    }

}
