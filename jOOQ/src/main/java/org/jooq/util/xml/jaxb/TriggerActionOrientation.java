
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TriggerActionOrientation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TriggerActionOrientation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ROW"/&gt;
 *     &lt;enumeration value="STATEMENT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TriggerActionOrientation")
@XmlEnum
public enum TriggerActionOrientation {

    ROW,
    STATEMENT;

    public String value() {
        return name();
    }

    public static TriggerActionOrientation fromValue(String v) {
        return valueOf(v);
    }

}
