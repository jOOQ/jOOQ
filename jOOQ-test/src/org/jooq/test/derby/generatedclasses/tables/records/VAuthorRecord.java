/**
 * This class is generated by jOOQ
 */
package org.jooq.test.derby.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VAuthorRecord extends org.jooq.impl.TableRecordImpl<org.jooq.test.derby.generatedclasses.tables.records.VAuthorRecord> implements org.jooq.Record6<java.lang.Integer, java.lang.String, java.lang.String, java.sql.Date, java.lang.Integer, java.lang.String> {

	private static final long serialVersionUID = -419310941;

	/**
	 * Setter for <code>TEST.V_AUTHOR.ID</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.ID</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>TEST.V_AUTHOR.FIRST_NAME</code>.
	 */
	public void setFirstName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.FIRST_NAME</code>.
	 */
	public java.lang.String getFirstName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>TEST.V_AUTHOR.LAST_NAME</code>.
	 */
	public void setLastName(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.LAST_NAME</code>.
	 */
	public java.lang.String getLastName() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>TEST.V_AUTHOR.DATE_OF_BIRTH</code>.
	 */
	public void setDateOfBirth(java.sql.Date value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.DATE_OF_BIRTH</code>.
	 */
	public java.sql.Date getDateOfBirth() {
		return (java.sql.Date) getValue(3);
	}

	/**
	 * Setter for <code>TEST.V_AUTHOR.YEAR_OF_BIRTH</code>.
	 */
	public void setYearOfBirth(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.YEAR_OF_BIRTH</code>.
	 */
	public java.lang.Integer getYearOfBirth() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>TEST.V_AUTHOR.ADDRESS</code>.
	 */
	public void setAddress(java.lang.String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>TEST.V_AUTHOR.ADDRESS</code>.
	 */
	public java.lang.String getAddress() {
		return (java.lang.String) getValue(5);
	}

	// -------------------------------------------------------------------------
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.sql.Date, java.lang.Integer, java.lang.String> fieldsRow() {
		return (org.jooq.Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.sql.Date, java.lang.Integer, java.lang.String> valuesRow() {
		return (org.jooq.Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.FIRST_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.LAST_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Date> field4() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.DATE_OF_BIRTH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.YEAR_OF_BIRTH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field6() {
		return org.jooq.test.derby.generatedclasses.tables.VAuthor.ADDRESS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getFirstName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getLastName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Date value4() {
		return getDateOfBirth();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getYearOfBirth();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value6() {
		return getAddress();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value2(java.lang.String value) {
		setFirstName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value3(java.lang.String value) {
		setLastName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value4(java.sql.Date value) {
		setDateOfBirth(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value5(java.lang.Integer value) {
		setYearOfBirth(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord value6(java.lang.String value) {
		setAddress(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VAuthorRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.sql.Date value4, java.lang.Integer value5, java.lang.String value6) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached VAuthorRecord
	 */
	public VAuthorRecord() {
		super(org.jooq.test.derby.generatedclasses.tables.VAuthor.V_AUTHOR);
	}

	/**
	 * Create a detached, initialised VAuthorRecord
	 */
	public VAuthorRecord(java.lang.Integer id, java.lang.String firstName, java.lang.String lastName, java.sql.Date dateOfBirth, java.lang.Integer yearOfBirth, java.lang.String address) {
		super(org.jooq.test.derby.generatedclasses.tables.VAuthor.V_AUTHOR);

		setValue(0, id);
		setValue(1, firstName);
		setValue(2, lastName);
		setValue(3, dateOfBirth);
		setValue(4, yearOfBirth);
		setValue(5, address);
	}
}
