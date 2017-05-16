







package org.jooq.util.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegexFlag.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
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

    UNIX_LINES,
    CASE_INSENSITIVE,
    COMMENTS,
    MULTILINE,
    LITERAL,
    DOTALL,
    UNICODE_CASE,
    CANON_EQ,
    UNICODE_CHARACTER_CLASS;

    public String value() {
        return name();
    }

    public static RegexFlag fromValue(String v) {
        return valueOf(v);
    }

}
