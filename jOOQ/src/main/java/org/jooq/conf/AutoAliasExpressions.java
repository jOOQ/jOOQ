
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AutoAliasExpressions.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AutoAliasExpressions"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="UNNAMED"/&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AutoAliasExpressions")
@XmlEnum
public enum AutoAliasExpressions {


    /**
     * Never auto-alias expressions.
     * 
     */
    NEVER,

    /**
     * Auto-alias only unnamed expressions.
     * 
     */
    UNNAMED,

    /**
     * Always auto-alias expressions.
     * 
     */
    ALWAYS;

    public String value() {
        return name();
    }

    public static AutoAliasExpressions fromValue(String v) {
        return valueOf(v);
    }

}
