
package org.jooq.conf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateUnchangedRecords.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UpdateUnchangedRecords"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NEVER"/&gt;
 *     &lt;enumeration value="SET_PRIMARY_KEY_TO_ITSELF"/&gt;
 *     &lt;enumeration value="SET_NON_PRIMARY_KEY_TO_THEMSELVES"/&gt;
 *     &lt;enumeration value="SET_NON_PRIMARY_KEY_TO_RECORD_VALUES"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UpdateUnchangedRecords")
@XmlEnum
public enum UpdateUnchangedRecords {


    /**
     * Never update unchanged records
     * 
     */
    NEVER,

    /**
     * Update unchanged records by setting the primary key column to itself, e.g. SET id = id
     * 
     */
    SET_PRIMARY_KEY_TO_ITSELF,

    /**
     * Update unchanged records by setting non-primary key columns to themselves, e.g. SET a = a, b = b
     * 
     */
    SET_NON_PRIMARY_KEY_TO_THEMSELVES,

    /**
     * Update unchanged records by setting record values to the values from the record, e.g. SET a = :a, b = :b. This is the same as calling record.changed(true) prior to updating.
     * 
     */
    SET_NON_PRIMARY_KEY_TO_RECORD_VALUES;

    public String value() {
        return name();
    }

    public static UpdateUnchangedRecords fromValue(String v) {
        return valueOf(v);
    }

}
