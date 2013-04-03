/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.mysql.sakila.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class Payment extends org.jooq.impl.TableImpl<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord> {

	private static final long serialVersionUID = -783712535;

	/**
	 * The singleton instance of <code>sakila.payment</code>
	 */
	public static final org.jooq.examples.mysql.sakila.tables.Payment PAYMENT = new org.jooq.examples.mysql.sakila.tables.Payment();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord> getRecordType() {
		return org.jooq.examples.mysql.sakila.tables.records.PaymentRecord.class;
	}

	/**
	 * The column <code>sakila.payment.payment_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.lang.Short> PAYMENT_ID = createField("payment_id", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * The column <code>sakila.payment.customer_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.lang.Short> CUSTOMER_ID = createField("customer_id", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * The column <code>sakila.payment.staff_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.lang.Byte> STAFF_ID = createField("staff_id", org.jooq.impl.SQLDataType.TINYINT, this);

	/**
	 * The column <code>sakila.payment.rental_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.lang.Integer> RENTAL_ID = createField("rental_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>sakila.payment.amount</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.math.BigDecimal> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.DECIMAL.precision(5, 2), this);

	/**
	 * The column <code>sakila.payment.payment_date</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.sql.Timestamp> PAYMENT_DATE = createField("payment_date", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * The column <code>sakila.payment.last_update</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.sql.Timestamp> LAST_UPDATE = createField("last_update", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * Create a <code>sakila.payment</code> table reference
	 */
	public Payment() {
		super("payment", org.jooq.examples.mysql.sakila.Sakila.SAKILA);
	}

	/**
	 * Create an aliased <code>sakila.payment</code> table reference
	 */
	public Payment(java.lang.String alias) {
		super(alias, org.jooq.examples.mysql.sakila.Sakila.SAKILA, org.jooq.examples.mysql.sakila.tables.Payment.PAYMENT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, java.lang.Short> getIdentity() {
		return org.jooq.examples.mysql.sakila.Keys.IDENTITY_PAYMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord> getPrimaryKey() {
		return org.jooq.examples.mysql.sakila.Keys.KEY_PAYMENT_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord>>asList(org.jooq.examples.mysql.sakila.Keys.KEY_PAYMENT_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.jooq.examples.mysql.sakila.tables.records.PaymentRecord, ?>>asList(org.jooq.examples.mysql.sakila.Keys.FK_PAYMENT_CUSTOMER, org.jooq.examples.mysql.sakila.Keys.FK_PAYMENT_STAFF, org.jooq.examples.mysql.sakila.Keys.FK_PAYMENT_RENTAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.examples.mysql.sakila.tables.Payment as(java.lang.String alias) {
		return new org.jooq.examples.mysql.sakila.tables.Payment(alias);
	}
}
