
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneratedSerialVersionUID.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="GeneratedSerialVersionUID"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HASH"/&gt;
 *     &lt;enumeration value="CONSTANT"/&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "GeneratedSerialVersionUID")
@XmlEnum
public enum GeneratedSerialVersionUID {


    /**
     * The generated serialVersionUID should be based on a hash code taken from the generated content.
     * 
     */
    HASH,

    /**
     * The generated serialVersionUID should be constant.
     * 
     */
    CONSTANT,

    /**
     * No serialVersionUID should be generated.
     * 
     */
    OFF;

    public String value() {
        return name();
    }

    public static GeneratedSerialVersionUID fromValue(String v) {
        return valueOf(v);
    }

}
