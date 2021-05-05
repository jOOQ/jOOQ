
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Transformation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Transformation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="WHEN_NEEDED"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Transformation")
@XmlEnum
public enum Transformation {

    NEVER,
    WHEN_NEEDED,
    ALWAYS;

    public String value() {
        return name();
    }

    public static Transformation fromValue(String v) {
        return valueOf(v);
    }

}
