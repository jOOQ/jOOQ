
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ForeignKeyRule.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ForeignKeyRule"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CASCADE"/&gt;
 *     &lt;enumeration value="SET NULL"/&gt;
 *     &lt;enumeration value="SET DEFAULT"/&gt;
 *     &lt;enumeration value="RESTRICT"/&gt;
 *     &lt;enumeration value="NO ACTION"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ForeignKeyRule")
@XmlEnum
public enum ForeignKeyRule {

    CASCADE("CASCADE"),
    @XmlEnumValue("SET NULL")
    SET_NULL("SET NULL"),
    @XmlEnumValue("SET DEFAULT")
    SET_DEFAULT("SET DEFAULT"),
    RESTRICT("RESTRICT"),
    @XmlEnumValue("NO ACTION")
    NO_ACTION("NO ACTION");
    private final String value;

    ForeignKeyRule(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ForeignKeyRule fromValue(String v) {
        for (ForeignKeyRule c: ForeignKeyRule.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @Override
    public String toString() {
        switch (this) {
            case SET_NULL:
                return "SET NULL";
            case SET_DEFAULT:
                return "SET DEFAULT";
            case NO_ACTION:
                return "NO ACTION";
            default:
                return this.name();
        }
    }

}
