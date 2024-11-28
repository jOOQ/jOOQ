
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ParamType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INDEXED"/&gt;
 *     &lt;enumeration value="FORCE_INDEXED"/&gt;
 *     &lt;enumeration value="NAMED"/&gt;
 *     &lt;enumeration value="NAMED_OR_INLINED"/&gt;
 *     &lt;enumeration value="INLINED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ParamType")
@XmlEnum
public enum ParamType {


    /**
     * Execute statements with indexed parameters, the way JDBC expects them.
     * 
     */
    INDEXED,

    /**
     * Execute statements with indexed parameters, forcing explicit inlined and named parameters to be indexed as well.
     * 
     */
    FORCE_INDEXED,

    /**
     * Execute statements with named parameters.
     * 
     */
    NAMED,

    /**
     * Execute statements with named parameters, if a name is given, or inlined parameters otherwise.
     * 
     */
    NAMED_OR_INLINED,

    /**
     * Execute statements with inlined parameters.
     * 
     */
    INLINED;

    public String value() {
        return name();
    }

    public static ParamType fromValue(String v) {
        return valueOf(v);
    }

}
