
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JSONConverterImplementation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="JSONConverterImplementation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="JACKSON_2"/&gt;
 *     &lt;enumeration value="JACKSON_3"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "JSONConverterImplementation")
@XmlEnum
public enum JSONConverterImplementation {


    /**
     * Whatever is found on the classpath will be preferred, with a preference to Jackson 3 support over Jackson 2 support.
     * 
     */
    DEFAULT,

    /**
     * Jackson 2 support.
     * 
     */
    JACKSON_2,

    /**
     * Jackson 3 support.
     * 
     */
    JACKSON_3;

    public String value() {
        return name();
    }

    public static JSONConverterImplementation fromValue(String v) {
        return valueOf(v);
    }

}
