
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MigrationDefaultContentType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MigrationDefaultContentType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INCREMENT"/&gt;
 *     &lt;enumeration value="SCRIPT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MigrationDefaultContentType")
@XmlEnum
public enum MigrationDefaultContentType {

    INCREMENT,
    SCRIPT;

    public String value() {
        return name();
    }

    public static MigrationDefaultContentType fromValue(String v) {
        return valueOf(v);
    }

}
