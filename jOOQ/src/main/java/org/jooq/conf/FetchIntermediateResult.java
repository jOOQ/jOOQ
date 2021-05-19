
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FetchIntermediateResult.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FetchIntermediateResult"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="WHEN_EXECUTE_LISTENERS_PRESENT"/&gt;
 *     &lt;enumeration value="WHEN_RESULT_REQUESTED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FetchIntermediateResult")
@XmlEnum
public enum FetchIntermediateResult {

    ALWAYS,
    WHEN_EXECUTE_LISTENERS_PRESENT,
    WHEN_RESULT_REQUESTED;

    public String value() {
        return name();
    }

    public static FetchIntermediateResult fromValue(String v) {
        return valueOf(v);
    }

}
