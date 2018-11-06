







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderQuotedNames.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RenderQuotedNames"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "RenderQuotedNames")
@XmlEnum
public enum RenderQuotedNames {

    ALWAYS,
    NEVER;

    public String value() {
        return name();
    }

    public static RenderQuotedNames fromValue(String v) {
        return valueOf(v);
    }

}
