
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Redact.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Redact"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALL_FORMATS"/&gt;
 *     &lt;enumeration value="TEXT_ONLY"/&gt;
 *     &lt;enumeration value="NONE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Redact")
@XmlEnum
public enum Redact {


    /**
     * Redact formatted content for all output formats, including CSV, JSON, XML, and all text formats.
     * 
     */
    ALL_FORMATS,

    /**
     * Redact formatted content for text formats, including text, HTML, chart formats.
     * 
     */
    TEXT_ONLY,

    /**
     * Redact no content, turning the feature off.
     * 
     */
    NONE;

    public String value() {
        return name();
    }

    public static Redact fromValue(String v) {
        return valueOf(v);
    }

}
