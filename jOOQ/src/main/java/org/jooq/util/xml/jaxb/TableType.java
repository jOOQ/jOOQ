
package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TableType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BASE TABLE"/&gt;
 *     &lt;enumeration value="VIEW"/&gt;
 *     &lt;enumeration value="GLOBAL TEMPORARY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "TableType")
@XmlEnum
public enum TableType {

    @XmlEnumValue("BASE TABLE")
    BASE_TABLE("BASE TABLE"),
    VIEW("VIEW"),
    @XmlEnumValue("GLOBAL TEMPORARY")
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY");
    private final String value;

    TableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableType fromValue(String v) {
        for (TableType c: TableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @Override
    public String toString() {
        switch (this) {
            case BASE_TABLE:
                return "BASE TABLE";
            case GLOBAL_TEMPORARY:
                return "GLOBAL TEMPORARY";
            default:
                return this.name();
        }
    }

}
