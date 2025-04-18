
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


    /**
     * The DELETE or UPDATE rule is for changes to the referenced table to CASCADE to the referencing table.
     * 
     */
    CASCADE("CASCADE"),

    /**
     * The DELETE or UPDATE rule is for changes to the referenced table to result in SET NULL actions in the referencing table.
     * 
     */
    @XmlEnumValue("SET NULL")
    SET_NULL("SET NULL"),

    /**
     * The DELETE or UPDATE rule is for changes to the referenced table to result in SET DELETE actions in the referencing table.
     * 
     */
    @XmlEnumValue("SET DEFAULT")
    SET_DEFAULT("SET DEFAULT"),

    /**
     * The DELETE or UPDATE rule is to RESTRICT changes to the referenced table.
     * 
     */
    RESTRICT("RESTRICT"),

    /**
     * The DELETE or UPDATE rule is to take NO ACTION on changes to the referenced table. This is similar to {@link #RESTRICT}, but allows for checks to be deferred.
     * 
     */
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
