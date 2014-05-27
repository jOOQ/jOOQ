/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 *
 * A book store
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "T_BOOK_STORE")
public class T_BOOK_STORE extends org.jooq.impl.UpdatableRecordImpl<org.jooq.test.oracle3.generatedclasses.tables.records.T_BOOK_STORE> implements java.lang.Cloneable, org.jooq.Record1<java.lang.String> {

	private static final long serialVersionUID = -551651381;

	/**
	 * Setter for <code>T_BOOK_STORE.NAME</code>. The books store name
	 */
	public void setNAME(java.lang.String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>T_BOOK_STORE.NAME</code>. The books store name
	 */
	@javax.persistence.Id
	@javax.persistence.Column(name = "NAME", unique = true, nullable = false, length = 400)
	public java.lang.String getNAME() {
		return (java.lang.String) getValue(0);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.String> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record1 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row1<java.lang.String> fieldsRow() {
		return (org.jooq.Row1) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row1<java.lang.String> valuesRow() {
		return (org.jooq.Row1) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return org.jooq.test.oracle3.generatedclasses.tables.T_BOOK_STORE.T_BOOK_STORE.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value1() {
		return getNAME();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_BOOK_STORE value1(java.lang.String value) {
		setNAME(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T_BOOK_STORE values(java.lang.String value1) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached T_BOOK_STORE
	 */
	public T_BOOK_STORE() {
		super(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK_STORE.T_BOOK_STORE);
	}

	/**
	 * Create a detached, initialised T_BOOK_STORE
	 */
	public T_BOOK_STORE(java.lang.String NAME) {
		super(org.jooq.test.oracle3.generatedclasses.tables.T_BOOK_STORE.T_BOOK_STORE);

		setValue(0, NAME);
	}
}
