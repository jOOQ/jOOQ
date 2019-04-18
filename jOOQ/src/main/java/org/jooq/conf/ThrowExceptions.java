
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ThrowExceptions.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ThrowExceptions"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="THROW_ALL"/&gt;
 *     &lt;enumeration value="THROW_FIRST"/&gt;
 *     &lt;enumeration value="THROW_NONE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ThrowExceptions")
@XmlEnum
public enum ThrowExceptions {

    THROW_ALL,
    THROW_FIRST,
    THROW_NONE;

    public String value() {
        return name();
    }

    public static ThrowExceptions fromValue(String v) {
        return valueOf(v);
    }

}
