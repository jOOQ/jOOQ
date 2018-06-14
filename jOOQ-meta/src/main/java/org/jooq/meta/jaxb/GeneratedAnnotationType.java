







package org.jooq.meta.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneratedAnnotationType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GeneratedAnnotationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DETECT_FROM_JDK"/&gt;
 *     &lt;enumeration value="JAVAX_ANNOTATION_GENERATED"/&gt;
 *     &lt;enumeration value="JAVAX_ANNOTATION_PROCESSING_GENERATED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "GeneratedAnnotationType")
@XmlEnum
public enum GeneratedAnnotationType {

    DETECT_FROM_JDK,
    JAVAX_ANNOTATION_GENERATED,
    JAVAX_ANNOTATION_PROCESSING_GENERATED;

    public String value() {
        return name();
    }

    public static GeneratedAnnotationType fromValue(String v) {
        return valueOf(v);
    }

}
