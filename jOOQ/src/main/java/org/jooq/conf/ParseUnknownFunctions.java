
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseUnknownFunctions.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ParseUnknownFunctions"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FAIL"/&gt;
 *     &lt;enumeration value="IGNORE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParseUnknownFunctions")
@XmlEnum
public enum ParseUnknownFunctions {


    /**
     * Functions have to be known by the parser, or by the catalog.
     * 
     */
    FAIL,

    /**
     * Unknown functions (parser or catalog) will be passed on as plain SQL.
     * 
     */
    IGNORE;

    public String value() {
        return name();
    }

    public static ParseUnknownFunctions fromValue(String v) {
        return valueOf(v);
    }

}
