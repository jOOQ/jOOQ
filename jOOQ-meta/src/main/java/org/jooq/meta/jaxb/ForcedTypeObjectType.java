







package org.jooq.meta.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ForcedTypeObjectType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ForcedTypeObjectType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALL"/&gt;
 *     &lt;enumeration value="ATTRIBUTE"/&gt;
 *     &lt;enumeration value="COLUMN"/&gt;
 *     &lt;enumeration value="ELEMENT"/&gt;
 *     &lt;enumeration value="PARAMETER"/&gt;
 *     &lt;enumeration value="SEQUENCE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ForcedTypeObjectType")
@XmlEnum
public enum ForcedTypeObjectType {

    ALL,
    ATTRIBUTE,
    COLUMN,
    ELEMENT,
    PARAMETER,
    SEQUENCE;

    public String value() {
        return name();
    }

    public static ForcedTypeObjectType fromValue(String v) {
        return valueOf(v);
    }

}
