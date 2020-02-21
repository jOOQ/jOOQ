
package org.jooq.conf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExecuteWithoutWhere.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExecuteWithoutWhere"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IGNORE"/&gt;
 *     &lt;enumeration value="LOG_DEBUG"/&gt;
 *     &lt;enumeration value="LOG_INFO"/&gt;
 *     &lt;enumeration value="LOG_WARN"/&gt;
 *     &lt;enumeration value="THROW"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ExecuteWithoutWhere")
@XmlEnum
public enum ExecuteWithoutWhere {

    IGNORE,
    LOG_DEBUG,
    LOG_INFO,
    LOG_WARN,
    THROW;

    public String value() {
        return name();
    }

    public static ExecuteWithoutWhere fromValue(String v) {
        return valueOf(v);
    }

}
