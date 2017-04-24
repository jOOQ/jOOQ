//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:36:23 AM CEST
//


package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für TableConstraintType.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="TableConstraintType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PRIMARY KEY"/&gt;
 *     &lt;enumeration value="UNIQUE"/&gt;
 *     &lt;enumeration value="CHECK"/&gt;
 *     &lt;enumeration value="FOREIGN KEY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "TableConstraintType")
@XmlEnum
public enum TableConstraintType {

    @XmlEnumValue("PRIMARY KEY")
    PRIMARY_KEY("PRIMARY KEY"),
    UNIQUE("UNIQUE"),
    CHECK("CHECK"),
    @XmlEnumValue("FOREIGN KEY")
    FOREIGN_KEY("FOREIGN KEY");
    private final String value;

    TableConstraintType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TableConstraintType fromValue(String v) {
        for (TableConstraintType c: TableConstraintType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
