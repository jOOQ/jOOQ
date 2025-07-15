
package org.jooq.util.xml.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserDefinedTypeCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UserDefinedTypeCategory"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="STRUCTURED"/&gt;
 *     &lt;enumeration value="DISTINCT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UserDefinedTypeCategory")
@XmlEnum
public enum UserDefinedTypeCategory {

    STRUCTURED,
    DISTINCT;

    public String value() {
        return name();
    }

    public static UserDefinedTypeCategory fromValue(String v) {
        return valueOf(v);
    }

}
