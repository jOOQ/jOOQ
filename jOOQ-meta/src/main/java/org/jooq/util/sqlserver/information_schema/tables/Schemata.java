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
public class Schemata extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -48975589;

	/**
	 * The singleton instance of <code>INFORMATION_SCHEMA.SCHEMATA</code>
	 */
	public static final org.jooq.util.sqlserver.information_schema.tables.Schemata SCHEMATA = new org.jooq.util.sqlserver.information_schema.tables.Schemata();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.CATALOG_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CATALOG_NAME = createField("CATALOG_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.SCHEMA_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SCHEMA_NAME = createField("SCHEMA_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.SCHEMA_OWNER</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SCHEMA_OWNER = createField("SCHEMA_OWNER", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.DEFAULT_CHARACTER_SET_CATALOG</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFAULT_CHARACTER_SET_CATALOG = createField("DEFAULT_CHARACTER_SET_CATALOG", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.DEFAULT_CHARACTER_SET_SCHEMA</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFAULT_CHARACTER_SET_SCHEMA = createField("DEFAULT_CHARACTER_SET_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.DEFAULT_CHARACTER_SET_NAME</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFAULT_CHARACTER_SET_NAME = createField("DEFAULT_CHARACTER_SET_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(128), this, "");

	/**
	 * The column <code>INFORMATION_SCHEMA.SCHEMATA.SQL_PATH</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SQL_PATH = createField("SQL_PATH", org.jooq.impl.SQLDataType.VARCHAR.length(65536), this, "");

	/**
	 * Create a <code>INFORMATION_SCHEMA.SCHEMATA</code> table reference
	 */
	public Schemata() {
		this("SCHEMATA", null);
	}

	/**
	 * Create an aliased <code>INFORMATION_SCHEMA.SCHEMATA</code> table reference
	 */
	public Schemata(java.lang.String alias) {
		this(alias, org.jooq.util.sqlserver.information_schema.tables.Schemata.SCHEMATA);
	}

	private Schemata(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private Schemata(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.sqlserver.information_schema.InformationSchema.INFORMATION_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.sqlserver.information_schema.tables.Schemata as(java.lang.String alias) {
		return new org.jooq.util.sqlserver.information_schema.tables.Schemata(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.util.sqlserver.information_schema.tables.Schemata rename(java.lang.String name) {
		return new org.jooq.util.sqlserver.information_schema.tables.Schemata(name, null);
	}
}
