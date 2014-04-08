/**
 * This class is generated by jOOQ
 */
package org.jooq.test.sqlserver.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_3085Record extends org.jooq.impl.TableRecordImpl<org.jooq.test.sqlserver.generatedclasses.tables.records.T_3085Record> implements org.jooq.Record2<java.lang.Integer, java.sql.Timestamp> {

	private static final long serialVersionUID = -1145631659;

	/**
	 * Setter for <code>dbo.t_3085.c1</code>.
	 */
	public void setC1(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>dbo.t_3085.c1</code>.
	 */
	public java.lang.Integer getC1() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>dbo.t_3085.c2</code>.
	 */
	public void setC2(java.sql.Timestamp value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>dbo.t_3085.c2</code>.
	 */
	public java.sql.Timestamp getC2() {
		return (java.sql.Timestamp) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.sql.Timestamp> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.sql.Timestamp> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.sqlserver.generatedclasses.tables.T_3085.T_3085.C1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field2() {
		return org.jooq.test.sqlserver.generatedclasses.tables.T_3085.T_3085.C2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getC1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value2() {
		return getC2();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_3085Record value1(java.lang.Integer value) {
		setC1(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_3085Record value2(java.sql.Timestamp value) {
		setC2(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_3085Record values(java.lang.Integer value1, java.sql.Timestamp value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached T_3085Record
	 */
	public T_3085Record() {
		super(org.jooq.test.sqlserver.generatedclasses.tables.T_3085.T_3085);
	}

	/**
	 * Create a detached, initialised T_3085Record
	 */
	public T_3085Record(java.lang.Integer c1, java.sql.Timestamp c2) {
		super(org.jooq.test.sqlserver.generatedclasses.tables.T_3085.T_3085);

		setValue(0, c1);
		setValue(1, c2);
	}
}
