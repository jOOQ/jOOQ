/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_2155 extends org.jooq.impl.TableImpl<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record> {

	private static final long serialVersionUID = 2139773623;

	/**
	 * The reference instance of <code>TEST.T_2155</code>
	 */
	public static final org.jooq.test.oracle.generatedclasses.test.tables.T_2155 T_2155 = new org.jooq.test.oracle.generatedclasses.test.tables.T_2155();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record> getRecordType() {
		return org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record.class;
	}

	/**
	 * The column <code>TEST.T_2155.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>TEST.T_2155.D1</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record, java.time.LocalDateTime> D1 = createField("D1", org.jooq.impl.SQLDataType.DATE, this, "", new org.jooq.test.all.converters.LocalDateTimeConverter());

	/**
	 * The column <code>TEST.T_2155.D2</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record, org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ObjectRecord> D2 = createField("D2", org.jooq.test.oracle.generatedclasses.test.udt.U_2155Object.U_2155_OBJECT.getDataType(), this, "");

	/**
	 * The column <code>TEST.T_2155.D3</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record, org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ArrayRecord> D3 = createField("D3", org.jooq.impl.SQLDataType.DATE.asArrayDataType(org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ArrayRecord.class), this, "");

	/**
	 * Create a <code>TEST.T_2155</code> table reference
	 */
	public T_2155() {
		this("T_2155", null);
	}

	/**
	 * Create an aliased <code>TEST.T_2155</code> table reference
	 */
	public T_2155(java.lang.String alias) {
		this(alias, org.jooq.test.oracle.generatedclasses.test.tables.T_2155.T_2155);
	}

	private T_2155(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record> aliased) {
		this(alias, aliased, null);
	}

	private T_2155(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle.generatedclasses.test.Test.TEST, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record> getPrimaryKey() {
		return org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_2155;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.oracle.generatedclasses.test.tables.records.T_2155Record>>asList(org.jooq.test.oracle.generatedclasses.test.Keys.PK_T_2155);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle.generatedclasses.test.tables.T_2155 as(java.lang.String alias) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_2155(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle.generatedclasses.test.tables.T_2155 rename(java.lang.String name) {
		return new org.jooq.test.oracle.generatedclasses.test.tables.T_2155(name, null);
	}
}
