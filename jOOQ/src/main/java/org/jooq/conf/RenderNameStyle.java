







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RenderNameStyle.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="RenderNameStyle"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="QUOTED"/&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "RenderNameStyle")
@XmlEnum
public enum RenderNameStyle {

    QUOTED,
    AS_IS,
    LOWER,
    UPPER;

    public String value() {
        return name();
    }

    public static RenderNameStyle fromValue(String v) {
        return valueOf(v);
    }

}
