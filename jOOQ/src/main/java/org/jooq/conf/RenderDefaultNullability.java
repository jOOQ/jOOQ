
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RenderDefaultNullability.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RenderDefaultNullability"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IMPLICIT_DEFAULT"/&gt;
 *     &lt;enumeration value="IMPLICIT_NULL"/&gt;
 *     &lt;enumeration value="EXPLICIT_NULL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RenderDefaultNullability")
@XmlEnum
public enum RenderDefaultNullability {

    IMPLICIT_DEFAULT,
    IMPLICIT_NULL,
    EXPLICIT_NULL;

    public String value() {
        return name();
    }

    public static RenderDefaultNullability fromValue(String v) {
        return valueOf(v);
    }

}
