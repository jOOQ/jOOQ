
package org.jooq.meta.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VisibilityModifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="VisibilityModifier"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="NONE"/&gt;
 *     &lt;enumeration value="PUBLIC"/&gt;
 *     &lt;enumeration value="INTERNAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VisibilityModifier")
@XmlEnum
public enum VisibilityModifier {

    DEFAULT,
    NONE,
    PUBLIC,
    INTERNAL;

    public String value() {
        return name();
    }

    public static VisibilityModifier fromValue(String v) {
        return valueOf(v);
    }

}
