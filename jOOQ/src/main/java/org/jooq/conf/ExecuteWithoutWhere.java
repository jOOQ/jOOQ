
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExecuteWithoutWhere.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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


    /**
     * UPDATE and DELETE statements are allowed to lack a WHERE clause
     * 
     */
    IGNORE,

    /**
     * UPDATE and DELETE statements are allowed to lack a WHERE clause, but this is logged as DEBUG level
     * 
     */
    LOG_DEBUG,

    /**
     * UPDATE and DELETE statements are allowed to lack a WHERE clause, but this is logged as INFO level
     * 
     */
    LOG_INFO,

    /**
     * UPDATE and DELETE statements are allowed to lack a WHERE clause, but this is logged as WARN level
     * 
     */
    LOG_WARN,

    /**
     * UPDATE and DELETE statements are not allowed to lack a WHERE clause
     * 
     */
    THROW;

    public String value() {
        return name();
    }

    public static ExecuteWithoutWhere fromValue(String v) {
        return valueOf(v);
    }

}
