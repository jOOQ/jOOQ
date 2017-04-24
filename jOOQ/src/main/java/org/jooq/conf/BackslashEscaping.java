







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BackslashEscaping.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="BackslashEscaping"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="ON"/&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "BackslashEscaping")
@XmlEnum
public enum BackslashEscaping {

    DEFAULT,
    ON,
    OFF;

    public String value() {
        return name();
    }

    public static BackslashEscaping fromValue(String v) {
        return valueOf(v);
    }

}
