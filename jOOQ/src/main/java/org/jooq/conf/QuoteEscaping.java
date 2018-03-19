







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuoteEscaping.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="QuoteEscaping">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DEFAULT"/>
 *     &lt;enumeration value="QUOTE"/>
 *     &lt;enumeration value="ESCAPE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
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
