







package org.jooq.util.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MatcherTransformType.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="MatcherTransformType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *     &lt;enumeration value="CAMEL"/&gt;
 *     &lt;enumeration value="PASCAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "MatcherTransformType")
@XmlEnum
public enum MatcherTransformType {

    AS_IS,
    LOWER,
    UPPER,
    CAMEL,
    PASCAL;

    public String value() {
        return name();
    }

    public static MatcherTransformType fromValue(String v) {
        return valueOf(v);
    }

}
