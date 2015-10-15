/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.public_.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.test.h2.generatedclasses.public_.tables.TPerformanceJdbc;
import org.jooq.test.h2.generatedclasses.public_.tables.interfaces.ITPerformanceJdbc;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPerformanceJdbcRecord extends UpdatableRecordImpl<TPerformanceJdbcRecord> implements Record3<Integer, Integer, String>, ITPerformanceJdbc {

	private static final long serialVersionUID = 159098319;

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.ID</code>.
	 */
	@Override
	public TPerformanceJdbcRecord setId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.ID</code>.
	 */
	@Override
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_INT</code>.
	 */
	@Override
	public TPerformanceJdbcRecord setValueInt(Integer value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_INT</code>.
	 */
	@Override
	public Integer getValueInt() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_STRING</code>.
	 */
	@Override
	public TPerformanceJdbcRecord setValueString(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_STRING</code>.
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
		return TPerformanceJdbc.T_PERFORMANCE_JDBC.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return TPerformanceJdbc.T_PERFORMANCE_JDBC.VALUE_INT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return TPerformanceJdbc.T_PERFORMANCE_JDBC.VALUE_STRING;
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
	public TPerformanceJdbcRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJdbcRecord value2(Integer value) {
		setValueInt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJdbcRecord value3(String value) {
		setValueString(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TPerformanceJdbcRecord values(Integer value1, Integer value2, String value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITPerformanceJdbc from) {
		setId(from.getId());
		setValueInt(from.getValueInt());
		setValueString(from.getValueString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITPerformanceJdbc> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TPerformanceJdbcRecord
	 */
	public TPerformanceJdbcRecord() {
		super(TPerformanceJdbc.T_PERFORMANCE_JDBC);
	}

	/**
	 * Create a detached, initialised TPerformanceJdbcRecord
	 */
	public TPerformanceJdbcRecord(Integer id, Integer valueInt, String valueString) {
		super(TPerformanceJdbc.T_PERFORMANCE_JDBC);

		setValue(0, id);
		setValue(1, valueInt);
		setValue(2, valueString);
	}
}
