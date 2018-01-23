







package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
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

    INDEXED,
    FORCE_INDEXED,
    NAMED,
    NAMED_OR_INLINED,
    INLINED;

    public String value() {
        return name();
    }

    public static ParamType fromValue(String v) {
        return valueOf(v);
    }

}
