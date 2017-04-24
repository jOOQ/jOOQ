//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:35:14 AM CEST
//


package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ParamType.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="ParamType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INDEXED"/&gt;
 *     &lt;enumeration value="NAMED"/&gt;
 *     &lt;enumeration value="NAMED_OR_INLINED"/&gt;
 *     &lt;enumeration value="INLINED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ParamType")
@XmlEnum
public enum ParamType {

    INDEXED,
    NAMED,
    NAMED_OR_INLINED,
    INLINED;

    public String value() {
        return name();
    }

    public static ParamType fromValue(String v) {
        return valueOf(v);
    }

}
