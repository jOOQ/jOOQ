/**
 * This class is generated by jOOQ
 */
package org.jooq.util.sqlserver.information_schema.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 463230913;

	/**
	 * The singleton instance of <code>INFORMATION_SCHEMA.TABLES</code>
	 */
	public static final org.jooq.util.sqlserver.information_schema.tables.Tables TABLES = new org.jooq.util.sqlserver.information_schema.tables.Tables();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.TABLE_CATALOG</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_CATALOG = createField("TABLE_CATALOG", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_SCHEMA = createField("TABLE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.TABLE_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_NAME = createField("TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.TABLE_TYPE</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_TYPE = createField("TABLE_TYPE", org.jooq.impl.SQLDataType.VARCHAR.length(65536), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.SELF_REFERENCING_COLUMN_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SELF_REFERENCING_COLUMN_NAME = createField("SELF_REFERENCING_COLUMN_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.REFERENCE_GENERATION</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> REFERENCE_GENERATION = createField("REFERENCE_GENERATION", org.jooq.impl.SQLDataType.VARCHAR.length(65536), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.USER_DEFINED_TYPE_CATALOG</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> USER_DEFINED_TYPE_CATALOG = createField("USER_DEFINED_TYPE_CATALOG", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.USER_DEFINED_TYPE_SCHEMA</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> USER_DEFINED_TYPE_SCHEMA = createField("USER_DEFINED_TYPE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.USER_DEFINED_TYPE_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> USER_DEFINED_TYPE_NAME = createField("USER_DEFINED_TYPE_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.IS_INSERTABLE_INTO</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> IS_INSERTABLE_INTO = createField("IS_INSERTABLE_INTO", org.jooq.impl.SQLDataType.VARCHAR.length(3), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.IS_TYPED</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> IS_TYPED = createField("IS_TYPED", org.jooq.impl.SQLDataType.VARCHAR.length(3), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.TABLES.COMMIT_ACTION</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> COMMIT_ACTION = createField("COMMIT_ACTION", org.jooq.impl.SQLDataType.VARCHAR.length(65536), this, "");

	/**
	 * Create a <code>INFORMATION_SCHEMA.TABLES</code> table reference
	 */
	public Tables() {
		this("TABLES", null);
	}

	/**
	 * Create an aliased <code>INFORMATION_SCHEMA.TABLES</code> table reference
	 */
	public Tables(java.lang.String alias) {
		this(alias, org.jooq.util.sqlserver.information_schema.tables.Tables.TABLES);
	}

	private Tables(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private Tables(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.sqlserver.information_schema.InformationSchema.INFORMATION_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.sqlserver.information_schema.tables.Tables as(java.lang.String alias) {
		return new org.jooq.util.sqlserver.information_schema.tables.Tables(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.util.sqlserver.information_schema.tables.Tables rename(java.lang.String name) {
		return new org.jooq.util.sqlserver.information_schema.tables.Tables(name, null);
	}
}
