/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TArraysRecord extends org.jooq.impl.UpdatableRecordImpl<org.jooq.test.h2.generatedclasses.tables.records.TArraysRecord> implements org.jooq.test.h2.generatedclasses.tables.interfaces.ITArrays {

	private static final long serialVersionUID = 1563134407;

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	@Override
	public void setId(java.lang.Integer value) {
		setValue(org.jooq.test.h2.generatedclasses.tables.TArrays.ID, value);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.ID</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	@Override
	public java.lang.Integer getId() {
		return getValue(org.jooq.test.h2.generatedclasses.tables.TArrays.ID);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.STRING_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public void setStringArray(java.lang.Object[] value) {
		setValue(org.jooq.test.h2.generatedclasses.tables.TArrays.STRING_ARRAY, value);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.STRING_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public java.lang.Object[] getStringArray() {
		return getValue(org.jooq.test.h2.generatedclasses.tables.TArrays.STRING_ARRAY);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.NUMBER_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public void setNumberArray(java.lang.Object[] value) {
		setValue(org.jooq.test.h2.generatedclasses.tables.TArrays.NUMBER_ARRAY, value);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.NUMBER_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public java.lang.Object[] getNumberArray() {
		return getValue(org.jooq.test.h2.generatedclasses.tables.TArrays.NUMBER_ARRAY);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.DATE_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public void setDateArray(java.lang.Object[] value) {
		setValue(org.jooq.test.h2.generatedclasses.tables.TArrays.DATE_ARRAY, value);
	}

	/**
	 * The table column <code>PUBLIC.T_ARRAYS.DATE_ARRAY</code>
	 * <p>
	 * The SQL type of this item (ARRAY) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	@Override
	public java.lang.Object[] getDateArray() {
		return getValue(org.jooq.test.h2.generatedclasses.tables.TArrays.DATE_ARRAY);
	}

	/**
	 * Create a detached TArraysRecord
	 */
	public TArraysRecord() {
		super(org.jooq.test.h2.generatedclasses.tables.TArrays.T_ARRAYS);
	}
}
