
package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterMode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IN"/&gt;
 *     &lt;enumeration value="INOUT"/&gt;
 *     &lt;enumeration value="OUT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParameterMode")
@XmlEnum
public enum ParameterMode {

    IN,
    INOUT,
    OUT;

    public String value() {
        return name();
    }

    public static ParameterMode fromValue(String v) {
        return valueOf(v);
    }

}
