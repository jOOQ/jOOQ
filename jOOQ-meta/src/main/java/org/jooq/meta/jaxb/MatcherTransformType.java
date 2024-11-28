
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatcherTransformType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MatcherTransformType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AS_IS"/&gt;
 *     &lt;enumeration value="LOWER"/&gt;
 *     &lt;enumeration value="LOWER_FIRST_LETTER"/&gt;
 *     &lt;enumeration value="UPPER"/&gt;
 *     &lt;enumeration value="UPPER_FIRST_LETTER"/&gt;
 *     &lt;enumeration value="CAMEL"/&gt;
 *     &lt;enumeration value="PASCAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MatcherTransformType")
@XmlEnum
public enum MatcherTransformType {


    /**
     * A {@link MatcherRule} should not transform identifiers, but output them "as is" (as the input is).
     * 
     */
    AS_IS,

    /**
     * A {@link MatcherRule} should transform identifiers to lower case.
     * 
     */
    LOWER,

    /**
     * A {@link MatcherRule} should transform the first letters of identifiers to lower case.
     * 
     */
    LOWER_FIRST_LETTER,

    /**
     * A {@link MatcherRule} should transform identifiers to UPPER case.
     * 
     */
    UPPER,

    /**
     * A {@link MatcherRule} should transform the first letters of identifiers to UPPER case.
     * 
     */
    UPPER_FIRST_LETTER,

    /**
     * A {@link MatcherRule} should transform identifiers to camelCase.
     * 
     */
    CAMEL,

    /**
     * A {@link MatcherRule} should transform identifiers to PascalCase.
     * 
     */
    PASCAL;

    public String value() {
        return name();
    }

    public static MatcherTransformType fromValue(String v) {
        return valueOf(v);
    }

}
