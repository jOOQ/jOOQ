/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import generated.tables.Language;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LanguageRecord extends UpdatableRecordImpl<LanguageRecord> implements Record3<Integer, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.language.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.language.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.language.cd</code>.
     */
    public void setCd(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.language.cd</code>.
     */
    public String getCd() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.language.description</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.language.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Language.LANGUAGE.ID;
    }

    @Override
    public Field<String> field2() {
        return Language.LANGUAGE.CD;
    }

    @Override
    public Field<String> field3() {
        return Language.LANGUAGE.DESCRIPTION;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getCd();
    }

    @Override
    public String component3() {
        return getDescription();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getCd();
    }

    @Override
    public String value3() {
        return getDescription();
    }

    @Override
    public LanguageRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public LanguageRecord value2(String value) {
        setCd(value);
        return this;
    }

    @Override
    public LanguageRecord value3(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public LanguageRecord values(Integer value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LanguageRecord
     */
    public LanguageRecord() {
        super(Language.LANGUAGE);
    }

    /**
     * Create a detached, initialised LanguageRecord
     */
    public LanguageRecord(Integer id, String cd, String description) {
        super(Language.LANGUAGE);

        setId(id);
        setCd(cd);
        setDescription(description);
    }
}
