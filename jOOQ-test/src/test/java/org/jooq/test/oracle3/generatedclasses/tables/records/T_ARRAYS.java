/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "T_ARRAYS")
public class T_ARRAYS extends org.jooq.impl.UpdatableRecordImpl<org.jooq.test.oracle3.generatedclasses.tables.records.T_ARRAYS> implements java.lang.Cloneable, org.jooq.Record5<java.lang.Integer, org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY> {

	private static final long serialVersionUID = 45967441;

	/**
	 * Setter for <code>T_ARRAYS.ID</code>.
	 */
	public void setID(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>T_ARRAYS.ID</code>.
	 */
	@javax.persistence.Id
	@javax.persistence.Column(name = "ID", unique = true, nullable = false, precision = 7)
	public java.lang.Integer getID() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>T_ARRAYS.STRING_ARRAY</code>.
	 */
	public void setSTRING_ARRAY(org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>T_ARRAYS.STRING_ARRAY</code>.
	 */
	@javax.persistence.Column(name = "STRING_ARRAY", length = 101)
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY getSTRING_ARRAY() {
		return (org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY) getValue(1);
	}

	/**
	 * Setter for <code>T_ARRAYS.NUMBER_ARRAY</code>.
	 */
	public void setNUMBER_ARRAY(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>T_ARRAYS.NUMBER_ARRAY</code>.
	 */
	@javax.persistence.Column(name = "NUMBER_ARRAY", length = 109)
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY getNUMBER_ARRAY() {
		return (org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY) getValue(2);
	}

	/**
	 * Setter for <code>T_ARRAYS.NUMBER_LONG_ARRAY</code>.
	 */
	public void setNUMBER_LONG_ARRAY(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>T_ARRAYS.NUMBER_LONG_ARRAY</code>.
	 */
	@javax.persistence.Column(name = "NUMBER_LONG_ARRAY", length = 109)
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY getNUMBER_LONG_ARRAY() {
		return (org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY) getValue(3);
	}

	/**
	 * Setter for <code>T_ARRAYS.DATE_ARRAY</code>.
	 */
	public void setDATE_ARRAY(org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>T_ARRAYS.DATE_ARRAY</code>.
	 */
	@javax.persistence.Column(name = "DATE_ARRAY", length = 49)
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY getDATE_ARRAY() {
		return (org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY> fieldsRow() {
		return (org.jooq.Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY> valuesRow() {
		return (org.jooq.Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY> field2() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS.STRING_ARRAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY> field3() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS.NUMBER_ARRAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY> field4() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS.NUMBER_LONG_ARRAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY> field5() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS.DATE_ARRAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY value2() {
		return getSTRING_ARRAY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY value3() {
		return getNUMBER_ARRAY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY value4() {
		return getNUMBER_LONG_ARRAY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY value5() {
		return getDATE_ARRAY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS value1(java.lang.Integer value) {
		setID(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS value2(org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY value) {
		setSTRING_ARRAY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS value3(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY value) {
		setNUMBER_ARRAY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS value4(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY value) {
		setNUMBER_LONG_ARRAY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS value5(org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY value) {
		setDATE_ARRAY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_ARRAYS values(java.lang.Integer value1, org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY value2, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY value3, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY value4, org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY value5) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached T_ARRAYS
	 */
	public T_ARRAYS() {
		super(org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS);
	}

	/**
	 * Create a detached, initialised T_ARRAYS
	 */
	public T_ARRAYS(java.lang.Integer ID, org.jooq.test.oracle3.generatedclasses.udt.records.U_STRING_ARRAY STRING_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY NUMBER_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_LONG_ARRAY NUMBER_LONG_ARRAY, org.jooq.test.oracle3.generatedclasses.udt.records.U_DATE_ARRAY DATE_ARRAY) {
		super(org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS);

		setValue(0, ID);
		setValue(1, STRING_ARRAY);
		setValue(2, NUMBER_ARRAY);
		setValue(3, NUMBER_LONG_ARRAY);
		setValue(4, DATE_ARRAY);
	}
}
