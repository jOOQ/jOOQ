
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Warning.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Warning"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IGNORE"/&gt;
 *     &lt;enumeration value="LOG_DEBUG"/&gt;
 *     &lt;enumeration value="LOG_INFO"/&gt;
 *     &lt;enumeration value="LOG_WARN"/&gt;
 *     &lt;enumeration value="THROW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Warning")
@XmlEnum
public enum Warning {


    /**
     * Ignore a warning
     * 
     */
    IGNORE,

    /**
     * Log a warning at DEBUG level
     * 
     */
    LOG_DEBUG,

    /**
     * Log a warning at INFO level
     * 
     */
    LOG_INFO,

    /**
     * Log a warning at WARN level
     * 
     */
    LOG_WARN,

    /**
     * Throw an exception
     * 
     */
    THROW;

    public String value() {
        return name();
    }

    public static Warning fromValue(String v) {
        return valueOf(v);
    }

}
