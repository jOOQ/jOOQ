
package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObjectType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObjectType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DOMAIN"/&gt;
 *     &lt;enumeration value="ROUTINE"/&gt;
 *     &lt;enumeration value="TABLE"/&gt;
 *     &lt;enumeration value="USER-DEFINED TYPE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ObjectType")
@XmlEnum
public enum ObjectType {

    DOMAIN("DOMAIN"),
    ROUTINE("ROUTINE"),
    TABLE("TABLE"),
    @XmlEnumValue("USER-DEFINED TYPE")
    USER_DEFINED_TYPE("USER-DEFINED TYPE");
    private final String value;

    ObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ObjectType fromValue(String v) {
        for (ObjectType c: ObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
