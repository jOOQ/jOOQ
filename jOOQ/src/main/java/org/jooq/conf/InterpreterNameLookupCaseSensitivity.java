
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterpreterNameLookupCaseSensitivity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="InterpreterNameLookupCaseSensitivity"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="WHEN_QUOTED"/&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InterpreterNameLookupCaseSensitivity")
@XmlEnum
public enum InterpreterNameLookupCaseSensitivity {


    /**
     * The default value depending on Settings.interpreterDialect
     * 
     */
    DEFAULT,

    /**
     * Identifiers are always case sensitive
     * 
     */
    ALWAYS,

    /**
     * Only quoted identifiers are case sensitive
     * 
     */
    WHEN_QUOTED,

    /**
     * Identifiers are never case sensitive
     * 
     */
    NEVER;

    public String value() {
        return name();
    }

    public static InterpreterNameLookupCaseSensitivity fromValue(String v) {
        return valueOf(v);
    }

}
