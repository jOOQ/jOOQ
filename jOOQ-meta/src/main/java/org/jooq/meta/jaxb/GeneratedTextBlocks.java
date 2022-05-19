
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneratedTextBlocks.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="GeneratedTextBlocks"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DETECT_FROM_JDK"/&gt;
 *     &lt;enumeration value="ON"/&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "GeneratedTextBlocks")
@XmlEnum
public enum GeneratedTextBlocks {

    DETECT_FROM_JDK,
    ON,
    OFF;

    public String value() {
        return name();
    }

    public static GeneratedTextBlocks fromValue(String v) {
        return valueOf(v);
    }

}
