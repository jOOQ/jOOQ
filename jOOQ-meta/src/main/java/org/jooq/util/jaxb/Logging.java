







package org.jooq.util.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Logging.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="Logging"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TRACE"/&gt;
 *     &lt;enumeration value="DEBUG"/&gt;
 *     &lt;enumeration value="INFO"/&gt;
 *     &lt;enumeration value="WARN"/&gt;
 *     &lt;enumeration value="ERROR"/&gt;
 *     &lt;enumeration value="FATAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "Logging")
@XmlEnum
public enum Logging {

    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL;

    public String value() {
        return name();
    }

    public static Logging fromValue(String v) {
        return valueOf(v);
    }

}
