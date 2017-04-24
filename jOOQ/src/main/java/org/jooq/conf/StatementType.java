







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für StatementType.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="StatementType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="STATIC_STATEMENT"/&gt;
 *     &lt;enumeration value="PREPARED_STATEMENT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "StatementType")
@XmlEnum
public enum StatementType {

    STATIC_STATEMENT,
    PREPARED_STATEMENT;

    public String value() {
        return name();
    }

    public static StatementType fromValue(String v) {
        return valueOf(v);
    }

}
