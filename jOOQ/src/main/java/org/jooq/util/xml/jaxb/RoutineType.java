
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutineType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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


    /**
     * The routine is a FUNCTION.
     * 
     */
    FUNCTION,

    /**
     * The routine is a PROCEDURE.
     * 
     */
    PROCEDURE;

    public String value() {
        return name();
    }

    public static RoutineType fromValue(String v) {
        return valueOf(v);
    }

}
