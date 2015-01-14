/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables.records;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "T_937")
public class T_937Record extends UpdatableRecordImpl<T_937Record> implements Record1<Integer>, org.jooq.test.firebird.generatedclasses.tables.interfaces.IT_937 {

	private static final long serialVersionUID = -632174660;

	/**
	 * Setter for <code>T_937.T_937</code>.
	 */
	@Override
	public void setT_937(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>T_937.T_937</code>.
	 */
	@Id
	@Column(name = "T_937", unique = true, nullable = false)
	@NotNull
	@Override
	public Integer getT_937() {
		return (Integer) getValue(0);
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
	// Record1 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row1<Integer> fieldsRow() {
		return (Row1) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row1<Integer> valuesRow() {
		return (Row1) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return org.jooq.test.firebird.generatedclasses.tables.T_937.T_937.T_937_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getT_937();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_937Record value1(Integer value) {
		setT_937(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_937Record values(Integer value1) {
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(org.jooq.test.firebird.generatedclasses.tables.interfaces.IT_937 from) {
		setT_937(from.getT_937());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends org.jooq.test.firebird.generatedclasses.tables.interfaces.IT_937> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached T_937Record
	 */
	public T_937Record() {
		super(org.jooq.test.firebird.generatedclasses.tables.T_937.T_937);
	}

	/**
	 * Create a detached, initialised T_937Record
	 */
	public T_937Record(Integer t_937) {
		super(org.jooq.test.firebird.generatedclasses.tables.T_937.T_937);

		setValue(0, t_937);
	}
}
