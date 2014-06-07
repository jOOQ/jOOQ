/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class V_TRANSACTIONS_BY_TIME extends org.jooq.impl.TableImpl<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME> implements java.lang.Cloneable {

	private static final long serialVersionUID = 452335600;

	/**
	 * The singleton instance of <code>V_TRANSACTIONS_BY_TIME</code>
	 */
	public static final org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME V_TRANSACTIONS_BY_TIME = new org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME> getRecordType() {
		return org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME.class;
	}

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.lang.Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.ACCOUNT_ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.lang.Long> ACCOUNT_ID = createField("ACCOUNT_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.VALUE_DATE</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.sql.Date> VALUE_DATE = createField("VALUE_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.AMOUNT</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.math.BigDecimal> AMOUNT = createField("AMOUNT", org.jooq.impl.SQLDataType.NUMERIC.precision(10, 2).nullable(false), this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.CREDIT</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.math.BigDecimal> CREDIT = createField("CREDIT", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.DEBIT</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.math.BigDecimal> DEBIT = createField("DEBIT", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.CURRENT_BALANCE</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.math.BigDecimal> CURRENT_BALANCE = createField("CURRENT_BALANCE", org.jooq.impl.SQLDataType.NUMERIC.precision(10, 2).nullable(false), this, "");

	/**
	 * The column <code>V_TRANSACTIONS_BY_TIME.TRANSACTION_NUMBER</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME, java.math.BigDecimal> TRANSACTION_NUMBER = createField("TRANSACTION_NUMBER", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * Create a <code>V_TRANSACTIONS_BY_TIME</code> table reference
	 */
	public V_TRANSACTIONS_BY_TIME() {
		this("V_TRANSACTIONS_BY_TIME", null);
	}

	/**
	 * Create an aliased <code>V_TRANSACTIONS_BY_TIME</code> table reference
	 */
	public V_TRANSACTIONS_BY_TIME(java.lang.String alias) {
		this(alias, org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME.V_TRANSACTIONS_BY_TIME);
	}

	private V_TRANSACTIONS_BY_TIME(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME> aliased) {
		this(alias, aliased, null);
	}

	private V_TRANSACTIONS_BY_TIME(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.V_TRANSACTIONS_BY_TIME> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME as(java.lang.String alias) {
		return new org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME rename(java.lang.String name) {
		return new org.jooq.test.oracle3.generatedclasses.tables.V_TRANSACTIONS_BY_TIME(name, null);
	}
}
