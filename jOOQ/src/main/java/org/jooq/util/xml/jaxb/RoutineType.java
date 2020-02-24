
package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutineType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoutineType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FUNCTION"/&gt;
 *     &lt;enumeration value="PROCEDURE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RoutineType")
@XmlEnum
public enum RoutineType {

    FUNCTION,
    PROCEDURE;

    public String value() {
        return name();
    }

    public static RoutineType fromValue(String v) {
        return valueOf(v);
    }

}
