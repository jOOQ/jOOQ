
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderTable.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderTable"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="WHEN_MULTIPLE_TABLES"/&gt;
 *     &lt;enumeration value="WHEN_AMBIGUOUS_COLUMNS"/&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderTable")
@XmlEnum
public enum RenderTable {

    ALWAYS,
    WHEN_MULTIPLE_TABLES,
    WHEN_AMBIGUOUS_COLUMNS,
    NEVER;

    public String value() {
        return name();
    }

    public static RenderTable fromValue(String v) {
        return valueOf(v);
    }

}
