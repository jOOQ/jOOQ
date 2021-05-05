
package org.jooq.migrations.xml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChangeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ChangeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ADD"/&gt;
 *     &lt;enumeration value="MODIFY"/&gt;
 *     &lt;enumeration value="DELETE"/&gt;
 *     &lt;enumeration value="RENAME"/&gt;
 *     &lt;enumeration value="COPY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ChangeType")
@XmlEnum
public enum ChangeType {

    ADD,
    MODIFY,
    DELETE,
    RENAME,
    COPY;

    public String value() {
        return name();
    }

    public static ChangeType fromValue(String v) {
        return valueOf(v);
    }

}
