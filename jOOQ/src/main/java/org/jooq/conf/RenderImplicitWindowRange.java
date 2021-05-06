
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderImplicitWindowRange.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderImplicitWindowRange"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *     &lt;enumeration value="ROWS_UNBOUNDED_PRECEDING"/&gt;
 *     &lt;enumeration value="ROWS_ALL"/&gt;
 *     &lt;enumeration value="RANGE_UNBOUNDED_PRECEDING"/&gt;
 *     &lt;enumeration value="RANGE_ALL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderImplicitWindowRange")
@XmlEnum
public enum RenderImplicitWindowRange {

    OFF,
    ROWS_UNBOUNDED_PRECEDING,
    ROWS_ALL,
    RANGE_UNBOUNDED_PRECEDING,
    RANGE_ALL;

    public String value() {
        return name();
    }

    public static RenderImplicitWindowRange fromValue(String v) {
        return valueOf(v);
    }

}
