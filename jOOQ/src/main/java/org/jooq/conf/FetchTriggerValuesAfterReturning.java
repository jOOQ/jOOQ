
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


    /**
     * Never fetch trigger values after returning.
     * 
     */
    NEVER,

    /**
     * Fetch trigger values only when triggers are known to be present.
     *            Trigger meta data is only available in jOOQ's commercial editions
     * 
     */
    WHEN_NEEDED,

    /**
     * Always fetch trigger values.
     * 
     */
    ALWAYS;

    public String value() {
        return name();
    }

    public static FetchTriggerValuesAfterReturning fromValue(String v) {
        return valueOf(v);
    }

}
