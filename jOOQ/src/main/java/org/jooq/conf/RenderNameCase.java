
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderNameCase.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RenderNameCase"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="LOWER_IF_UNQUOTED"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *     &lt;enumeration value="UPPER_IF_UNQUOTED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "RenderNameCase")
@XmlEnum
public enum RenderNameCase {

    AS_IS,
    LOWER,
    LOWER_IF_UNQUOTED,
    UPPER,
    UPPER_IF_UNQUOTED;

    public String value() {
        return name();
    }

    public static RenderNameCase fromValue(String v) {
        return valueOf(v);
    }

}
