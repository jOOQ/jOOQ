
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateUnchangedRecords.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UpdateUnchangedRecords"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="SET_PRIMARY_KEY_TO_ITSELF"/&gt;
 *     &lt;enumeration value="SET_NON_PRIMARY_KEY_TO_THEMSELVES"/&gt;
 *     &lt;enumeration value="SET_NON_PRIMARY_KEY_TO_RECORD_VALUES"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UpdateUnchangedRecords")
@XmlEnum
public enum UpdateUnchangedRecords {

    NEVER,
    SET_PRIMARY_KEY_TO_ITSELF,
    SET_NON_PRIMARY_KEY_TO_THEMSELVES,
    SET_NON_PRIMARY_KEY_TO_RECORD_VALUES;

    public String value() {
        return name();
    }

    public static UpdateUnchangedRecords fromValue(String v) {
        return valueOf(v);
    }

}
