/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_2845CaseSensitivity extends org.jooq.impl.TableImpl<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord> {

	private static final long serialVersionUID = -2125725889;

	/**
	 * The reference instance of <code>TEST.T_2845_CASE_sensitivity</code>
	 */
	public static final org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity T_2845_CASE_SENSITIVITY = new org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord> getRecordType() {
		return org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord.class;
	}

	/**
	 * The column <code>TEST.T_2845_CASE_sensitivity.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord, java.math.BigDecimal> ID = createField("ID", org.jooq.impl.SQLDataType.NUMERIC.nullable(false), this, "");

	/**
	 * The column <code>TEST.T_2845_CASE_sensitivity.INSENSITIVE</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord, java.math.BigDecimal> INSENSITIVE = createField("INSENSITIVE", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * The column <code>TEST.T_2845_CASE_sensitivity.UPPER</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord, java.math.BigDecimal> UPPER = createField("UPPER", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * The column <code>TEST.T_2845_CASE_sensitivity.lower</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord, java.math.BigDecimal> LOWER = createField("lower", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * The column <code>TEST.T_2845_CASE_sensitivity.Mixed</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord, java.math.BigDecimal> MIXED = createField("Mixed", org.jooq.impl.SQLDataType.NUMERIC, this, "");

	/**
	 * Create a <code>TEST.T_2845_CASE_sensitivity</code> table reference
	 */
	public T_2845CaseSensitivity() {
		this("T_2845_CASE_sensitivity", null);
	}

	/**
	 * Create an aliased <code>TEST.T_2845_CASE_sensitivity</code> table reference
	 */
	public T_2845CaseSensitivity(java.lang.String alias) {
		this(alias, org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity.T_2845_CASE_SENSITIVITY);
	}

	private T_2845CaseSensitivity(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord> aliased) {
		this(alias, aliased, null);
	}

	private T_2845CaseSensitivity(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle.generatedclasses.test.Test.TEST, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord> getPrimaryKey() {
		return org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_2845_CASE_SENSITIVITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2845CaseSensitivityRecord>>asList(org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_2845_CASE_SENSITIVITY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity as(java.lang.String alias) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity rename(java.lang.String name) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_2845CaseSensitivity(name, null);
	}
}
