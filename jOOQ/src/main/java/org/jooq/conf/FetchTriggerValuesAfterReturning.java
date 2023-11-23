
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FetchTriggerValuesAfterReturning.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FetchTriggerValuesAfterReturning"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="WHEN_NEEDED"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FetchTriggerValuesAfterReturning")
@XmlEnum
public enum FetchTriggerValuesAfterReturning {

    NEVER,
    WHEN_NEEDED,
    ALWAYS;

    public String value() {
        return name();
    }

    public static FetchTriggerValuesAfterReturning fromValue(String v) {
        return valueOf(v);
    }

}
