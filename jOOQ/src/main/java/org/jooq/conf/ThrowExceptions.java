
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ThrowExceptions.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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


    /**
     * Collect all exceptions from a batch and throw them all together
     * 
     */
    THROW_ALL,

    /**
     * Throw only the first exception from a batch
     * 
     */
    THROW_FIRST,

    /**
     * Throw no exceptions, but collect them in ResultOrRows
     * 
     */
    THROW_NONE;

    public String value() {
        return name();
    }

    public static ThrowExceptions fromValue(String v) {
        return valueOf(v);
    }

}
