
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
 * Configuration of an input schema and its mappings.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SchemaMappingType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SchemaMappingType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(required = true, defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String inputSchema = "";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String outputSchema;
    @XmlElement(defaultValue = "false")
    protected Boolean outputSchemaToDefault = false;

    /**
     * See also {@link Database#getInputSchema()}
     * 
     */
    public String getInputSchema() {
        return inputSchema;
    }

    /**
     * See also {@link Database#getInputSchema()}
     * 
     */
    public void setInputSchema(String value) {
        this.inputSchema = value;
    }

    /**
     * See also {@link Database#getOutputSchema()}
     * 
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * See also {@link Database#getOutputSchema()}
     * 
     */
    public void setOutputSchema(String value) {
        this.outputSchema = value;
    }

    /**
     * See also {@link Database#isOutputSchemaToDefault()}
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutputSchemaToDefault() {
        return outputSchemaToDefault;
    }

    /**
     * Sets the value of the outputSchemaToDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutputSchemaToDefault(Boolean value) {
        this.outputSchemaToDefault = value;
    }

    /**
     * See also {@link Database#getInputSchema()}
     * 
     */
    public SchemaMappingType withInputSchema(String value) {
        setInputSchema(value);
        return this;
    }

    /**
     * See also {@link Database#getOutputSchema()}
     * 
     */
    public SchemaMappingType withOutputSchema(String value) {
        setOutputSchema(value);
        return this;
    }

    public SchemaMappingType withOutputSchemaToDefault(Boolean value) {
        setOutputSchemaToDefault(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("inputSchema", inputSchema);
        builder.append("outputSchema", outputSchema);
        builder.append("outputSchemaToDefault", outputSchemaToDefault);
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
        SchemaMappingType other = ((SchemaMappingType) that);
        if (inputSchema == null) {
            if (other.inputSchema!= null) {
                return false;
            }
        } else {
            if (!inputSchema.equals(other.inputSchema)) {
                return false;
            }
        }
        if (outputSchema == null) {
            if (other.outputSchema!= null) {
                return false;
            }
        } else {
            if (!outputSchema.equals(other.outputSchema)) {
                return false;
            }
        }
        if (outputSchemaToDefault == null) {
            if (other.outputSchemaToDefault!= null) {
                return false;
            }
        } else {
            if (!outputSchemaToDefault.equals(other.outputSchemaToDefault)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((inputSchema == null)? 0 :inputSchema.hashCode()));
        result = ((prime*result)+((outputSchema == null)? 0 :outputSchema.hashCode()));
        result = ((prime*result)+((outputSchemaToDefault == null)? 0 :outputSchemaToDefault.hashCode()));
        return result;
    }

}
