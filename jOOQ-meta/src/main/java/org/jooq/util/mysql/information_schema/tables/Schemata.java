/**
 * This class is generated by jOOQ
 */
package org.jooq.util.mysql.information_schema.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.3" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Schemata extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -724128531;

	/**
	 * The singleton instance of <code>information_schema.SCHEMATA</code>
	 */
	public static final org.jooq.util.mysql.information_schema.tables.Schemata SCHEMATA = new org.jooq.util.mysql.information_schema.tables.Schemata();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>information_schema.SCHEMATA.CATALOG_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CATALOG_NAME = createField("CATALOG_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(512).nullable(false).defaulted(true), SCHEMATA, "");

	/**
	 * The column <code>information_schema.SCHEMATA.SCHEMA_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SCHEMA_NAME = createField("SCHEMA_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), SCHEMATA, "");

	/**
	 * The column <code>information_schema.SCHEMATA.DEFAULT_CHARACTER_SET_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFAULT_CHARACTER_SET_NAME = createField("DEFAULT_CHARACTER_SET_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(32).nullable(false).defaulted(true), SCHEMATA, "");

	/**
	 * The column <code>information_schema.SCHEMATA.DEFAULT_COLLATION_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFAULT_COLLATION_NAME = createField("DEFAULT_COLLATION_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(32).nullable(false).defaulted(true), SCHEMATA, "");

	/**
	 * The column <code>information_schema.SCHEMATA.SQL_PATH</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SQL_PATH = createField("SQL_PATH", org.jooq.impl.SQLDataType.VARCHAR.length(512), SCHEMATA, "");

	/**
	 * No further instances allowed
	 */
	private Schemata() {
		this("SCHEMATA", null);
	}

	private Schemata(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private Schemata(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.mysql.information_schema.InformationSchema.INFORMATION_SCHEMA, aliased, parameters, "");
	}
}
