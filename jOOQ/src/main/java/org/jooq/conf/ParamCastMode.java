
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamCastMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ParamCastMode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALWAYS"/&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParamCastMode")
@XmlEnum
public enum ParamCastMode {


    /**
     * Bind values are always cast to their respective type.
     * 
     */
    ALWAYS,

    /**
     * Bind values are cast to their respective type when needed.
     * 
     *            Some databases are not able to delay bind value type inference until the execution of a statement.
     *            They will either reject the value of unknown type, or assume a possibly inappropriate type. In these
     *            cases, jOOQ will generate an explicit cast(? as datatype) expression around the bind value to help
     *            the query parser do its job. The exact behaviour of this mode is undefined and subject to change.
     * 
     */
    DEFAULT,

    /**
     * Bind values are never cast to their respective type.
     * 
     */
    NEVER;

    public String value() {
        return name();
    }

    public static ParamCastMode fromValue(String v) {
        return valueOf(v);
    }

}
