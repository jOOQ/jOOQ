
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WriteIfReadonly.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="WriteIfReadonly"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="WRITE"/&gt;
 *     &lt;enumeration value="IGNORE"/&gt;
 *     &lt;enumeration value="THROW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "WriteIfReadonly")
@XmlEnum
public enum WriteIfReadonly {

    WRITE,
    IGNORE,
    THROW;

    public String value() {
        return name();
    }

    public static WriteIfReadonly fromValue(String v) {
        return valueOf(v);
    }

}
