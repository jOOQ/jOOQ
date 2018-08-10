







package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


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
public class SchemaMappingType implements Serializable
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
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInputSchema() {
        return inputSchema;
    }

    /**
     * Sets the value of the inputSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputSchema(String value) {
        this.inputSchema = value;
    }

    /**
     * See also {@link Database#getOutputSchema()}
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * Sets the value of the outputSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
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

    public SchemaMappingType withInputSchema(String value) {
        setInputSchema(value);
        return this;
    }

    public SchemaMappingType withOutputSchema(String value) {
        setOutputSchema(value);
        return this;
    }

    public SchemaMappingType withOutputSchemaToDefault(Boolean value) {
        setOutputSchemaToDefault(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (inputSchema!= null) {
            sb.append("<inputSchema>");
            sb.append(inputSchema);
            sb.append("</inputSchema>");
        }
        if (outputSchema!= null) {
            sb.append("<outputSchema>");
            sb.append(outputSchema);
            sb.append("</outputSchema>");
        }
        if (outputSchemaToDefault!= null) {
            sb.append("<outputSchemaToDefault>");
            sb.append(outputSchemaToDefault);
            sb.append("</outputSchemaToDefault>");
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
