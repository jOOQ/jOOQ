
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BackslashEscaping.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="BackslashEscaping"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="ON"/&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BackslashEscaping")
@XmlEnum
public enum BackslashEscaping {


    /**
     * Use the database's most sensible default value for ON (MySQL, MariaDB) / OFF (all other databases)
     * 
     */
    DEFAULT,

    /**
     * Always escape backslashes.
     * 
     */
    ON,

    /**
     * Never escape backslashes.
     * 
     */
    OFF;

    public String value() {
        return name();
    }

    public static BackslashEscaping fromValue(String v) {
        return valueOf(v);
    }

}
