
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseUnsupportedSyntax.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ParseUnsupportedSyntax"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FAIL"/&gt;
 *     &lt;enumeration value="IGNORE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParseUnsupportedSyntax")
@XmlEnum
public enum ParseUnsupportedSyntax {


    /**
     * Fail on syntax that is supported (known) by the parser, but not the jOOQ API
     * 
     */
    FAIL,

    /**
     * Ignore syntax that is supported (known) by the parser, but not the jOOQ API
     * 
     */
    IGNORE;

    public String value() {
        return name();
    }

    public static ParseUnsupportedSyntax fromValue(String v) {
        return valueOf(v);
    }

}
