
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LengthUnit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LengthUnit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CHARACTERS"/&gt;
 *     &lt;enumeration value="OCTETS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LengthUnit")
@XmlEnum
public enum LengthUnit {

    CHARACTERS,
    OCTETS;

    public String value() {
        return name();
    }

    public static LengthUnit fromValue(String v) {
        return valueOf(v);
    }

}
