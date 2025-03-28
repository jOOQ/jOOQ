
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransformUnneededArithmeticExpressions.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TransformUnneededArithmeticExpressions"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="INTERNAL"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TransformUnneededArithmeticExpressions")
@XmlEnum
public enum TransformUnneededArithmeticExpressions {


    /**
     * Never transform unneeded arithmetic expressions
     * 
     */
    NEVER,

    /**
     * Transform arithmetic expressions arising from jOOQ's internals
     * 
     */
    INTERNAL,

    /**
     * Transform all arithmetic expressions
     * 
     */
    ALWAYS;

    public String value() {
        return name();
    }

    public static TransformUnneededArithmeticExpressions fromValue(String v) {
        return valueOf(v);
    }

}
