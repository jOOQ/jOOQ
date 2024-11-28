
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderNameStyle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderNameStyle"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="QUOTED"/&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderNameStyle")
@XmlEnum
@Deprecated
public enum RenderNameStyle {


    /**
     * Render object names quoted, as defined in the database. Use this
     *            to stay on the safe side with case-sensitivity and special
     *            characters. For instance:
     *            Oracle    : "SYS"."ALL_TAB_COLS"
     *            MySQL     : `information_schema`.`TABLES`
     *            SQL Server: [INFORMATION_SCHEMA].[TABLES]
     * 
     */
    QUOTED,

    /**
     * Render object names, as defined in the database. For instance:
     *            Oracle    : SYS.ALL_TAB_COLS
     *            MySQL     : information_schema.TABLES
     *            SQL Server: INFORMATION_SCHEMA.TABLES
     * 
     */
    AS_IS,

    /**
     * Force rendering object names in lower case. For instance:
     *            Oracle    : sys.all_tab_cols
     *            MySQL     : information_schema.tables
     *            SQL Server: information_schema.tables
     * 
     */
    LOWER,

    /**
     * Force rendering object names in upper case. For instance:
     *            Oracle    : SYS.ALL_TAB_COLS
     *            MySQL     : INFORMATION_SCHEMA.TABLES
     *            SQL Server: INFORMATION_SCHEMA.TABLES
     * 
     */
    UPPER;

    public String value() {
        return name();
    }

    public static RenderNameStyle fromValue(String v) {
        return valueOf(v);
    }

}
