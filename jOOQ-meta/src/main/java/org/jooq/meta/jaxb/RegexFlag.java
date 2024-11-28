
package org.jooq.meta.jaxb;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegexFlag.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RegexFlag"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UNIX_LINES"/&gt;
 *     &lt;enumeration value="CASE_INSENSITIVE"/&gt;
 *     &lt;enumeration value="COMMENTS"/&gt;
 *     &lt;enumeration value="MULTILINE"/&gt;
 *     &lt;enumeration value="LITERAL"/&gt;
 *     &lt;enumeration value="DOTALL"/&gt;
 *     &lt;enumeration value="UNICODE_CASE"/&gt;
 *     &lt;enumeration value="CANON_EQ"/&gt;
 *     &lt;enumeration value="UNICODE_CHARACTER_CLASS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RegexFlag")
@XmlEnum
public enum RegexFlag {


    /**
     * Apply the {@link java.util.regex.Pattern#UNIX_LINES} flag to regular expressions from the code generation configuration.
     * 
     */
    UNIX_LINES,

    /**
     * Apply the {@link java.util.regex.Pattern#CASE_INSENSITIVE} flag to regular expressions from the code generation configuration.
     * 
     */
    CASE_INSENSITIVE,

    /**
     * Apply the {@link java.util.regex.Pattern#COMMENTS} flag to regular expressions from the code generation configuration.
     * 
     */
    COMMENTS,

    /**
     * Apply the {@link java.util.regex.Pattern#MULTILINE} flag to regular expressions from the code generation configuration.
     * 
     */
    MULTILINE,

    /**
     * Apply the {@link java.util.regex.Pattern#LITERAL} flag to regular expressions from the code generation configuration.
     * 
     */
    LITERAL,

    /**
     * Apply the {@link java.util.regex.Pattern#DOTALL} flag to regular expressions from the code generation configuration.
     * 
     */
    DOTALL,

    /**
     * Apply the {@link java.util.regex.Pattern#UNICODE_CASE} flag to regular expressions from the code generation configuration.
     * 
     */
    UNICODE_CASE,

    /**
     * Apply the {@link java.util.regex.Pattern#CANON_EQ} flag to regular expressions from the code generation configuration.
     * 
     */
    CANON_EQ,

    /**
     * Apply the {@link java.util.regex.Pattern#UNICODE_CHARACTER_CLASS} flag to regular expressions from the code generation configuration.
     * 
     */
    UNICODE_CHARACTER_CLASS;

    public String value() {
        return name();
    }

    public static RegexFlag fromValue(String v) {
        return valueOf(v);
    }

}
