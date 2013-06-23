/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.mysql.sakila.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SakilaPaymentRecord extends org.jooq.impl.UpdatableRecordImpl<org.jooq.examples.mysql.sakila.tables.records.SakilaPaymentRecord> implements org.jooq.Record7<java.lang.Short, java.lang.Short, java.lang.Byte, java.lang.Integer, java.math.BigDecimal, java.sql.Timestamp, java.sql.Timestamp> {

	private static final long serialVersionUID = 927986124;

	/**
	 * Setter for <code>sakila.payment.payment_id</code>. 
	 */
	public void setPaymentId(java.lang.Short value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>sakila.payment.payment_id</code>. 
	 */
	public java.lang.Short getPaymentId() {
		return (java.lang.Short) getValue(0);
	}

	/**
	 * Setter for <code>sakila.payment.customer_id</code>. 
	 */
	public void setCustomerId(java.lang.Short value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>sakila.payment.customer_id</code>. 
	 */
	public java.lang.Short getCustomerId() {
		return (java.lang.Short) getValue(1);
	}

	/**
	 * Setter for <code>sakila.payment.staff_id</code>. 
	 */
	public void setStaffId(java.lang.Byte value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>sakila.payment.staff_id</code>. 
	 */
	public java.lang.Byte getStaffId() {
		return (java.lang.Byte) getValue(2);
	}

	/**
	 * Setter for <code>sakila.payment.rental_id</code>. 
	 */
	public void setRentalId(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>sakila.payment.rental_id</code>. 
	 */
	public java.lang.Integer getRentalId() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>sakila.payment.amount</code>. 
	 */
	public void setAmount(java.math.BigDecimal value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>sakila.payment.amount</code>. 
	 */
	public java.math.BigDecimal getAmount() {
		return (java.math.BigDecimal) getValue(4);
	}

	/**
	 * Setter for <code>sakila.payment.payment_date</code>. 
	 */
	public void setPaymentDate(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>sakila.payment.payment_date</code>. 
	 */
	public java.sql.Timestamp getPaymentDate() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>sakila.payment.last_update</code>. 
	 */
	public void setLastUpdate(java.sql.Timestamp value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>sakila.payment.last_update</code>. 
	 */
	public java.sql.Timestamp getLastUpdate() {
		return (java.sql.Timestamp) getValue(6);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Short> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record7 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Short, java.lang.Short, java.lang.Byte, java.lang.Integer, java.math.BigDecimal, java.sql.Timestamp, java.sql.Timestamp> fieldsRow() {
		return (org.jooq.Row7) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Short, java.lang.Short, java.lang.Byte, java.lang.Integer, java.math.BigDecimal, java.sql.Timestamp, java.sql.Timestamp> valuesRow() {
		return (org.jooq.Row7) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Short> field1() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.PAYMENT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Short> field2() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.CUSTOMER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Byte> field3() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.STAFF_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.RENTAL_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field5() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.AMOUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.PAYMENT_DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field7() {
		return org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT.LAST_UPDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Short value1() {
		return getPaymentId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Short value2() {
		return getCustomerId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Byte value3() {
		return getStaffId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getRentalId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value5() {
		return getAmount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getPaymentDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value7() {
		return getLastUpdate();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached SakilaPaymentRecord
	 */
	public SakilaPaymentRecord() {
		super(org.jooq.examples.mysql.sakila.tables.SakilaPayment.PAYMENT);
	}
}
