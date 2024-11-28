
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


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


    /**
     * Do not produce any nullability clause for Nullability.DEFAULT, and thus produce the dialect specific default behaviors
     * 
     */
    IMPLICIT_DEFAULT,

    /**
     * Produce implicit nullability for Nullability.DEFAULT if NULL is the default for a given dialect, or explicit nullability otherwise (e.g. in both Sybase)
     * 
     */
    IMPLICIT_NULL,

    /**
     * Produce explicit nullability for Nullability.DEFAULT, irrespective of the context (e.g. if the column is a primary key)
     * 
     */
    EXPLICIT_NULL;

    public String value() {
        return name();
    }

    public static RenderDefaultNullability fromValue(String v) {
        return valueOf(v);
    }

}
