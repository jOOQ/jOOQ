
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


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


    /**
     * Implicit RANGE clause will not be generated explicitly. The RDBMS's implicit behaviour is used
     * 
     */
    OFF,

    /**
     * Implicit RANGE clause is generated as ROWS UNBOUNDED PRECEDING
     * 
     */
    ROWS_UNBOUNDED_PRECEDING,

    /**
     * Implicit RANGE clause is generated as ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
     * 
     */
    ROWS_ALL,

    /**
     * Implicit RANGE clause is generated as RANGE UNBOUNDED PRECEDING
     * 
     */
    RANGE_UNBOUNDED_PRECEDING,

    /**
     * Implicit RANGE clause is generated as RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
     * 
     */
    RANGE_ALL;

    public String value() {
        return name();
    }

    public static RenderImplicitWindowRange fromValue(String v) {
        return valueOf(v);
    }

}
