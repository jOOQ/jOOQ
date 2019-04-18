
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseUnsupportedSyntax.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
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

    FAIL,
    IGNORE;

    public String value() {
        return name();
    }

    public static ParseUnsupportedSyntax fromValue(String v) {
        return valueOf(v);
    }

}
