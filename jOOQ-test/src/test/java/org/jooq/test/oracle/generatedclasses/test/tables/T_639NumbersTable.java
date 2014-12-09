/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_639NumbersTable extends org.jooq.impl.TableImpl<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord> {

	private static final long serialVersionUID = 1293541364;

	/**
	 * The reference instance of <code>TEST.T_639_NUMBERS_TABLE</code>
	 */
	public static final org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable T_639_NUMBERS_TABLE = new org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord> getRecordType() {
		return org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord.class;
	}

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.BYTE</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Byte> BYTE = createField("BYTE", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.SHORT</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Short> SHORT = createField("SHORT", org.jooq.impl.SQLDataType.SMALLINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.INTEGER</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Integer> INTEGER = createField("INTEGER", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.LONG</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Long> LONG = createField("LONG", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.BYTE_DECIMAL</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Byte> BYTE_DECIMAL = createField("BYTE_DECIMAL", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.SHORT_DECIMAL</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Short> SHORT_DECIMAL = createField("SHORT_DECIMAL", org.jooq.impl.SQLDataType.SMALLINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.INTEGER_DECIMAL</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Integer> INTEGER_DECIMAL = createField("INTEGER_DECIMAL", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.LONG_DECIMAL</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.lang.Long> LONG_DECIMAL = createField("LONG_DECIMAL", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.BIG_INTEGER</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.math.BigInteger> BIG_INTEGER = createField("BIG_INTEGER", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(22), this, "");

	/**
	 * The column <code>TEST.T_639_NUMBERS_TABLE.BIG_DECIMAL</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord, java.math.BigDecimal> BIG_DECIMAL = createField("BIG_DECIMAL", org.jooq.impl.SQLDataType.NUMERIC.precision(22, 5), this, "");

	/**
	 * Create a <code>TEST.T_639_NUMBERS_TABLE</code> table reference
	 */
	public T_639NumbersTable() {
		this("T_639_NUMBERS_TABLE", null);
	}

	/**
	 * Create an aliased <code>TEST.T_639_NUMBERS_TABLE</code> table reference
	 */
	public T_639NumbersTable(java.lang.String alias) {
		this(alias, org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable.T_639_NUMBERS_TABLE);
	}

	private T_639NumbersTable(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord> aliased) {
		this(alias, aliased, null);
	}

	private T_639NumbersTable(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle.generatedclasses.test.Test.TEST, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord> getPrimaryKey() {
		return org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_639_NUMBERS_TABLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_639NumbersTableRecord>>asList(org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_639_NUMBERS_TABLE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable as(java.lang.String alias) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable rename(java.lang.String name) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_639NumbersTable(name, null);
	}
}
