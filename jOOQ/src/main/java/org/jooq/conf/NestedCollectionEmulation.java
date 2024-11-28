
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NestedCollectionEmulation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NestedCollectionEmulation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NATIVE"/&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="XML"/&gt;
 *     &lt;enumeration value="JSON"/&gt;
 *     &lt;enumeration value="JSONB"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NestedCollectionEmulation")
@XmlEnum
public enum NestedCollectionEmulation {


    /**
     * Generate native LIST, SET, MULTISET syntax irrespective of support
     * 
     */
    NATIVE,

    /**
     * Generate native LIST, SET, MULTISET syntax if supported, or the most optimal emulation, otherwise
     * 
     */
    DEFAULT,

    /**
     * Emulate LIST, SET, MULTISET syntax using XML
     * 
     */
    XML,

    /**
     * Emulate LIST, SET, MULTISET syntax using JSON
     * 
     */
    JSON,

    /**
     * Emulate LIST, SET, MULTISET syntax using JSONB
     * 
     */
    JSONB;

    public String value() {
        return name();
    }

    public static NestedCollectionEmulation fromValue(String v) {
        return valueOf(v);
    }

}
