
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderOptionalKeyword.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RenderOptionalKeyword"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *     &lt;enumeration value="ON"/&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderOptionalKeyword")
@XmlEnum
public enum RenderOptionalKeyword {

    OFF,
    ON,
    DEFAULT;

    public String value() {
        return name();
    }

    public static RenderOptionalKeyword fromValue(String v) {
        return valueOf(v);
    }

}
