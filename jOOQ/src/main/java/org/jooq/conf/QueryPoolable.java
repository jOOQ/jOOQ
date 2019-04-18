
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QueryPoolable.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="QueryPoolable"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TRUE"/&gt;
 *     &lt;enumeration value="FALSE"/&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "QueryPoolable")
@XmlEnum
public enum QueryPoolable {

    TRUE,
    FALSE,
    DEFAULT;

    public String value() {
        return name();
    }

    public static QueryPoolable fromValue(String v) {
        return valueOf(v);
    }

}
