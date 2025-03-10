
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatementType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="StatementType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="STATIC_STATEMENT"/&gt;
 *     &lt;enumeration value="PREPARED_STATEMENT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StatementType")
@XmlEnum
public enum StatementType {


    /**
     * Execute statements with inlined bind values, avoiding JDBC's PreparedStatements
     * 
     */
    STATIC_STATEMENT,

    /**
     * Execute statements with bind values, using JDBC's PreparedStatements
     * 
     */
    PREPARED_STATEMENT;

    public String value() {
        return name();
    }

    public static StatementType fromValue(String v) {
        return valueOf(v);
    }

}
