
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderImplicitJoinType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderImplicitJoinType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="INNER_JOIN"/&gt;
 *     &lt;enumeration value="LEFT_JOIN"/&gt;
 *     &lt;enumeration value="SCALAR_SUBQUERY"/&gt;
 *     &lt;enumeration value="THROW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderImplicitJoinType")
@XmlEnum
public enum RenderImplicitJoinType {


    /**
     * The DEFAULT behaviour, depending on the type of path
     * 
     */
    DEFAULT,

    /**
     * Always render INNER JOIN
     * 
     */
    INNER_JOIN,

    /**
     * Always render LEFT JOIN
     * 
     */
    LEFT_JOIN,

    /**
     * Always render a scalar subquery
     * 
     */
    SCALAR_SUBQUERY,

    /**
     * Always throw an exception (effectively disallowing the type of implicit joins)
     * 
     */
    THROW;

    public String value() {
        return name();
    }

    public static RenderImplicitJoinType fromValue(String v) {
        return valueOf(v);
    }

}
