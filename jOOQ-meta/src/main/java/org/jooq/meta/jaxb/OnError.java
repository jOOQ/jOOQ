
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OnError.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="OnError"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FAIL"/&gt;
 *     &lt;enumeration value="LOG"/&gt;
 *     &lt;enumeration value="SILENT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OnError")
@XmlEnum
public enum OnError {


    /**
     * On error, the code generation should fail.
     * 
     */
    FAIL,

    /**
     * On error, the code generation should log the error, and continue.
     * 
     */
    LOG,

    /**
     * Suppress all errors.
     * 
     */
    SILENT;

    public String value() {
        return name();
    }

    public static OnError fromValue(String v) {
        return valueOf(v);
    }

}
