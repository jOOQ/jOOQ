
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SyntheticIdentityGenerationMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="SyntheticIdentityGenerationMode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BY DEFAULT"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SyntheticIdentityGenerationMode")
@XmlEnum
public enum SyntheticIdentityGenerationMode {


    /**
     * <code>BY DEFAULT</code> generation mode.
     * 
     */
    @XmlEnumValue("BY DEFAULT")
    BY_DEFAULT("BY DEFAULT"),

    /**
     * <code>ALWAYS</code> generation mode.
     * 
     */
    ALWAYS("ALWAYS");
    private final String value;

    SyntheticIdentityGenerationMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SyntheticIdentityGenerationMode fromValue(String v) {
        for (SyntheticIdentityGenerationMode c: SyntheticIdentityGenerationMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @Override
    public String toString() {
        switch (this) {
            case BY_DEFAULT:
                return "BY DEFAULT";
            default:
                return this.name();
        }
    }

}
