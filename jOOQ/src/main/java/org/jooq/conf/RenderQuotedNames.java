
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderQuotedNames.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderQuotedNames"&gt;
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
@XmlType(name = "RenderQuotedNames")
@XmlEnum
public enum RenderQuotedNames {


    /**
     * Always render object names quoted, as defined in the database.
     *            Use this to stay on the safe side with case-sensitivity, special
     *            characters, and keywords. For instance:
     *            Oracle    : "SYS"."ALL_TAB_COLS"
     *            MySQL     : `information_schema`.`TABLES`
     *            SQL Server: [INFORMATION_SCHEMA].[TABLES]
     * 
     */
    ALWAYS,

    /**
     * Render object names quoted if they are constructed using DSL.quotedName() or DSL.name()
     * 
     */
    EXPLICIT_DEFAULT_QUOTED,

    /**
     * Render object names quoted if they are constructed using DSL.quotedName()
     * 
     */
    EXPLICIT_DEFAULT_UNQUOTED,

    /**
     * Never quote names
     * 
     */
    NEVER;

    public String value() {
        return name();
    }

    public static RenderQuotedNames fromValue(String v) {
        return valueOf(v);
    }

}
