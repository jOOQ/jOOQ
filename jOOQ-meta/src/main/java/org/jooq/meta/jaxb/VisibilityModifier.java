
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VisibilityModifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="VisibilityModifier"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DEFAULT"/&gt;
 *     &lt;enumeration value="NONE"/&gt;
 *     &lt;enumeration value="PUBLIC"/&gt;
 *     &lt;enumeration value="INTERNAL"/&gt;
 *     &lt;enumeration value="PRIVATE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VisibilityModifier")
@XmlEnum
public enum VisibilityModifier {


    /**
     * The generated visibility modifier is the default for the target language to produce <code>public</code> visibility (explicit <code>public</code> in Java, nothing in Kotlin, Scala).
     * 
     */
    DEFAULT,

    /**
     * No visibility modifier is generated.
     * 
     */
    NONE,

    /**
     * An explicit <code>public</code> visibility modifier is generated, where supported (no modifier is generated in Scala).
     * 
     */
    PUBLIC,

    /**
     * An explicit <code>internal</code> visibility modifier is generated, where supported (or <code>public</code>, otherwise).
     * 
     */
    INTERNAL,

    /**
     * A <code>private</code> visibility modifier is generated. This is useful only for {@link ForcedType}, not as a global configuration.
     * 
     */
    PRIVATE;

    public String value() {
        return name();
    }

    public static VisibilityModifier fromValue(String v) {
        return valueOf(v);
    }

}
