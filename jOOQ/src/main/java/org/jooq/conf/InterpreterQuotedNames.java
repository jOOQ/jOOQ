
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterpreterQuotedNames.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="InterpreterQuotedNames"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="EXPLICIT_DEFAULT_QUOTED"/&gt;
 *     &lt;enumeration value="EXPLICIT_DEFAULT_UNQUOTED"/&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InterpreterQuotedNames")
@XmlEnum
public enum InterpreterQuotedNames {


    /**
     * Always interpret object names as quoted, as defined in the database.
     *            Use this to stay on the safe side with case-sensitivity, special
     *            characters, and keywords. For instance:
     *            Oracle    : "SYS"."ALL_TAB_COLS"
     *            MySQL     : `information_schema`.`TABLES`
     *            SQL Server: [INFORMATION_SCHEMA].[TABLES]
     * 
     */
    ALWAYS,

    /**
     * Interpret object names as quoted if they are constructed using DSL.quotedName() or DSL.name()
     * 
     */
    EXPLICIT_DEFAULT_QUOTED,

    /**
     * Interpret object names as quoted if they are constructed using DSL.quotedName()
     * 
     */
    EXPLICIT_DEFAULT_UNQUOTED,

    /**
     * Never interpret object names as quoted.
     * 
     */
    NEVER;

    public String value() {
        return name();
    }

    public static InterpreterQuotedNames fromValue(String v) {
        return valueOf(v);
    }

}
