
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseNameCase.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ParseNameCase"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="LOWER_IF_UNQUOTED"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *     &lt;enumeration value="UPPER_IF_UNQUOTED"/&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParseNameCase")
@XmlEnum
public enum ParseNameCase {


    /**
     * Parse object names, as defined in the database. For instance: schema.TABLE
     * 
     */
    AS_IS,

    /**
     * Force parsing object names in lower case. For instance: schema."table"
     * 
     */
    LOWER,

    /**
     * Force parsing object names in lower case, if unquoted. For instance schema."TABLE"
     * 
     */
    LOWER_IF_UNQUOTED,

    /**
     * Force parsing object names in upper case. For instance: SCHEMA."TABLE"
     * 
     */
    UPPER,

    /**
     * Force parsing object names in upper case, if unquoted. For instance SCHEMA."table"
     * 
     */
    UPPER_IF_UNQUOTED,

    /**
     * Apply the parse dialect specific default behaviour
     * 
     */
    DEFAULT;

    public String value() {
        return name();
    }

    public static ParseNameCase fromValue(String v) {
        return valueOf(v);
    }

}
