/**
 * This class is generated by jOOQ
 */
package org.jooq.test.sqlserver.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "f_tables5", schema = "dbo")
public class FTables5Record extends org.jooq.impl.TableRecordImpl<org.jooq.test.sqlserver.generatedclasses.tables.records.FTables5Record> implements org.jooq.Record2<java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -872631063;

	/**
	 * Setter for <code>dbo.f_tables5.v</code>.
	 */
	public void setV(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>dbo.f_tables5.v</code>.
	 */
	@javax.persistence.Column(name = "v", precision = 10)
	public java.lang.Integer getV() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>dbo.f_tables5.s</code>.
	 */
	public void setS(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>dbo.f_tables5.s</code>.
	 */
	@javax.persistence.Column(name = "s", precision = 10)
	public java.lang.Integer getS() {
		return (java.lang.Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.sqlserver.generatedclasses.tables.FTables5.F_TABLES5.V;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.jooq.test.sqlserver.generatedclasses.tables.FTables5.F_TABLES5.S;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getV();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FTables5Record value1(java.lang.Integer value) {
		setV(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FTables5Record value2(java.lang.Integer value) {
		setS(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FTables5Record values(java.lang.Integer value1, java.lang.Integer value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached FTables5Record
	 */
	public FTables5Record() {
		super(org.jooq.test.sqlserver.generatedclasses.tables.FTables5.F_TABLES5);
	}

	/**
	 * Create a detached, initialised FTables5Record
	 */
	public FTables5Record(java.lang.Integer v, java.lang.Integer s) {
		super(org.jooq.test.sqlserver.generatedclasses.tables.FTables5.F_TABLES5);

		setValue(0, v);
		setValue(1, s);
	}
}
