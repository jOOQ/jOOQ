
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecordDirtyTracking.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RecordDirtyTracking"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TOUCHED"/&gt;
 *     &lt;enumeration value="MODIFIED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RecordDirtyTracking")
@XmlEnum
public enum RecordDirtyTracking {


    /**
     * Dirty tracking is based on Record.touched() semantics
     * 
     */
    TOUCHED,

    /**
     * Dirty tracking is based on Record.modified() semantics
     * 
     */
    MODIFIED;

    public String value() {
        return name();
    }

    public static RecordDirtyTracking fromValue(String v) {
        return valueOf(v);
    }

}
