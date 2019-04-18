
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseWithMetaLookups.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParseWithMetaLookups"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OFF"/&gt;
 *     &lt;enumeration value="IGNORE_ON_FAILURE"/&gt;
 *     &lt;enumeration value="THROW_ON_FAILURE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ParseWithMetaLookups")
@XmlEnum
public enum ParseWithMetaLookups {

    OFF,
    IGNORE_ON_FAILURE,
    THROW_ON_FAILURE;

    public String value() {
        return name();
    }

    public static ParseWithMetaLookups fromValue(String v) {
        return valueOf(v);
    }

}
