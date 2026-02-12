
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdentityGeneration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="IdentityGeneration"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="BY DEFAULT"/&gt;
 *     &lt;enumeration value="YES"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "IdentityGeneration")
@XmlEnum
public enum IdentityGeneration {

    ALWAYS("ALWAYS"),
    @XmlEnumValue("BY DEFAULT")
    BY_DEFAULT("BY DEFAULT"),

    /**
     * @deprecated - historic alternative for <code>BY DEFAULT</code>, do not reuse.
     * 
     */
    YES("YES");
    private final String value;

    IdentityGeneration(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IdentityGeneration fromValue(String v) {
        for (IdentityGeneration c: IdentityGeneration.values()) {
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
