
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterpreterWithMetaLookups.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="InterpreterWithMetaLookups"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IGNORE_ON_FAILURE"/&gt;
 *     &lt;enumeration value="THROW_ON_FAILURE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InterpreterWithMetaLookups")
@XmlEnum
public enum InterpreterWithMetaLookups {


    /**
     * Meta lookups are active in interpreter, but don't throw exceptions on failure
     * 
     */
    IGNORE_ON_FAILURE,

    /**
     * Meta lookups are active in interpreter and throw exceptions on failure
     * 
     */
    THROW_ON_FAILURE;

    public String value() {
        return name();
    }

    public static InterpreterWithMetaLookups fromValue(String v) {
        return valueOf(v);
    }

}
