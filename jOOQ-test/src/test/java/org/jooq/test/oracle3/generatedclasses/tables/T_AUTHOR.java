/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 *
 * An entity holding authors of books
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_AUTHOR extends org.jooq.impl.TableImpl<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR> implements java.lang.Cloneable {

	private static final long serialVersionUID = 51478976;

	/**
	 * The singleton instance of <code>T_AUTHOR</code>
	 */
	public static final org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR T_AUTHOR = new org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR> getRecordType() {
		return org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR.class;
	}

	/**
	 * The column <code>T_AUTHOR.ID</code>. The author ID
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "The author ID");

	/**
	 * The column <code>T_AUTHOR.FIRST_NAME</code>. The author's first name
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, java.lang.String> FIRST_NAME = createField("FIRST_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "The author's first name");

	/**
	 * The column <code>T_AUTHOR.LAST_NAME</code>. The author's last name
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, java.lang.String> LAST_NAME = createField("LAST_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "The author's last name");

	/**
	 * The column <code>T_AUTHOR.DATE_OF_BIRTH</code>. The author's date of birth
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, java.sql.Date> DATE_OF_BIRTH = createField("DATE_OF_BIRTH", org.jooq.impl.SQLDataType.DATE, this, "The author's date of birth");

	/**
	 * The column <code>T_AUTHOR.YEAR_OF_BIRTH</code>. The author's year of birth
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, java.lang.Integer> YEAR_OF_BIRTH = createField("YEAR_OF_BIRTH", org.jooq.impl.SQLDataType.INTEGER, this, "The author's year of birth");

	/**
	 * The column <code>T_AUTHOR.ADDRESS</code>. The author's address
	 */
	public final org.jooq.TableField<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR, org.jooq.test.oracle3.generatedclasses.udt.records.U_ADDRESS_TYPE> ADDRESS = createField("ADDRESS", org.jooq.test.oracle3.generatedclasses.udt.U_ADDRESS_TYPE.U_ADDRESS_TYPE.getDataType(), this, "The author's address");

	/**
	 * Create a <code>T_AUTHOR</code> table reference
	 */
	public T_AUTHOR() {
		this("T_AUTHOR", null);
	}

	/**
	 * Create an aliased <code>T_AUTHOR</code> table reference
	 */
	public T_AUTHOR(java.lang.String alias) {
		this(alias, org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR);
	}

	private T_AUTHOR(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR> aliased) {
		this(alias, aliased, null);
	}

	private T_AUTHOR(java.lang.String alias, org.jooq.Table<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "An entity holding authors of books");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR> getPrimaryKey() {
		return org.jooq.test.oracle3.generatedclasses.Keys.PK_T_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.oracle3.generatedclasses.tables.records.T_AUTHOR>>asList(org.jooq.test.oracle3.generatedclasses.Keys.PK_T_AUTHOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR as(java.lang.String alias) {
		return new org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR rename(java.lang.String name) {
		return new org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR(name, null);
	}
}
