
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


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
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderImplicitJoinType")
@XmlEnum
public enum RenderImplicitJoinType {

    DEFAULT,
    INNER_JOIN,
    LEFT_JOIN;

    public String value() {
        return name();
    }

    public static RenderImplicitJoinType fromValue(String v) {
        return valueOf(v);
    }

}
