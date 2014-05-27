/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_785 extends org.jooq.impl.TableImpl<org.jooq.test.oracle3.generatedclasses.tables.records.T_785> implements java.lang.Cloneable {

	private static final long serialVersionUID = 157822898;

	/**
	 * The singleton instance of <code>T_785</code>
	 */
	public static final org.jooq.test.oracle3.generatedclasses.tables.T_785 T_785 = new org.jooq.test.oracle3.generatedclasses.tables.T_785();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle3.generatedclasses.tables.records.T_785> getRecordType() {
		return org.jooq.test.oracle3.generatedclasses.tables.records.T_785.class;
	}

	/**
	 * The column <code>T_785.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_785, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>T_785.NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_785, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "");

	/**
	 * The column <code>T_785.VALUE</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_785, java.lang.String> VALUE = createField("VALUE", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "");

	/**
	 * Create a <code>T_785</code> table reference
	 */
	public T_785() {
		this("T_785", null);
	}

	/**
	 * Create an aliased <code>T_785</code> table reference
	 */
	public T_785(java.lang.String alias) {
		this(alias, org.jooq.test.oracle3.generatedclasses.tables.T_785.T_785);
	}

	private T_785(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.T_785> aliased) {
		this(alias, aliased, null);
	}

	private T_785(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.T_785> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.tables.T_785 as(java.lang.String alias) {
		return new org.jooq.test.oracle3.generatedclasses.tables.T_785(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle3.generatedclasses.tables.T_785 rename(java.lang.String name) {
		return new org.jooq.test.oracle3.generatedclasses.tables.T_785(name, null);
	}
}
