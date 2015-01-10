/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.test.h2.generatedclasses.tables.TPerformanceJooq;
import org.jooq.test.h2.generatedclasses.tables.interfaces.ITPerformanceJooq;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPerformanceJooqRecord extends UpdatableRecordImpl<TPerformanceJooqRecord> implements Record3<Integer, Integer, String>, ITPerformanceJooq {

	private static final long serialVersionUID = -1768642390;

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.ID</code>.
	 */
	@Override
	public TPerformanceJooqRecord setId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.ID</code>.
	 */
	@Override
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_INT</code>.
	 */
	@Override
	public TPerformanceJooqRecord setValueInt(Integer value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_INT</code>.
	 */
	@Override
	public Integer getValueInt() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_STRING</code>.
	 */
	@Override
	public TPerformanceJooqRecord setValueString(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_STRING</code>.
	 */
	@Override
	public String getValueString() {
		return (String) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, String> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, String> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return TPerformanceJooq.T_PERFORMANCE_JOOQ.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return TPerformanceJooq.T_PERFORMANCE_JOOQ.VALUE_INT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return TPerformanceJooq.T_PERFORMANCE_JOOQ.VALUE_STRING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getValueInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getValueString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJooqRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJooqRecord value2(Integer value) {
		setValueInt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJooqRecord value3(String value) {
		setValueString(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJooqRecord values(Integer value1, Integer value2, String value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITPerformanceJooq from) {
		setId(from.getId());
		setValueInt(from.getValueInt());
		setValueString(from.getValueString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITPerformanceJooq> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TPerformanceJooqRecord
	 */
	public TPerformanceJooqRecord() {
		super(TPerformanceJooq.T_PERFORMANCE_JOOQ);
	}

	/**
	 * Create a detached, initialised TPerformanceJooqRecord
	 */
	public TPerformanceJooqRecord(Integer id, Integer valueInt, String valueString) {
		super(TPerformanceJooq.T_PERFORMANCE_JOOQ);

		setValue(0, id);
		setValue(1, valueInt);
		setValue(2, valueString);
	}
}
