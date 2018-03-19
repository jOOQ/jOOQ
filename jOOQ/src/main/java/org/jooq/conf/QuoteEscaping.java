







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuoteEscaping.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="QuoteEscaping"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="QUOTE"/&gt;
 *     &lt;enumeration value="BACKSLASH"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "QuoteEscaping")
@XmlEnum
public enum QuoteEscaping {

    DEFAULT,
    QUOTE,
    BACKSLASH;

    public String value() {
        return name();
    }

    public static QuoteEscaping fromValue(String v) {
        return valueOf(v);
    }

}
