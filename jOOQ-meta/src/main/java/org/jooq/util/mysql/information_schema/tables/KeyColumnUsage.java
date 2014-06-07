/**
 * This class is generated by jOOQ
 */
package org.jooq.util.mysql.information_schema.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class KeyColumnUsage extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -1303145965;

	/**
	 * The singleton instance of <code>information_schema.KEY_COLUMN_USAGE</code>
	 */
	public static final org.jooq.util.mysql.information_schema.tables.KeyColumnUsage KEY_COLUMN_USAGE = new org.jooq.util.mysql.information_schema.tables.KeyColumnUsage();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.CONSTRAINT_CATALOG</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_CATALOG = createField("CONSTRAINT_CATALOG", org.jooq.impl.SQLDataType.VARCHAR.length(512).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_SCHEMA = createField("CONSTRAINT_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.CONSTRAINT_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_NAME = createField("CONSTRAINT_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.TABLE_CATALOG</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_CATALOG = createField("TABLE_CATALOG", org.jooq.impl.SQLDataType.VARCHAR.length(512).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.TABLE_SCHEMA</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_SCHEMA = createField("TABLE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.TABLE_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_NAME = createField("TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.COLUMN_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> COLUMN_NAME = createField("COLUMN_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.ORDINAL_POSITION</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Long> ORDINAL_POSITION = createField("ORDINAL_POSITION", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.POSITION_IN_UNIQUE_CONSTRAINT</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Long> POSITION_IN_UNIQUE_CONSTRAINT = createField("POSITION_IN_UNIQUE_CONSTRAINT", org.jooq.impl.SQLDataType.BIGINT, KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.REFERENCED_TABLE_SCHEMA</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> REFERENCED_TABLE_SCHEMA = createField("REFERENCED_TABLE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR.length(64), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.REFERENCED_TABLE_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> REFERENCED_TABLE_NAME = createField("REFERENCED_TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64), KEY_COLUMN_USAGE, "");

	/**
	 * The column <code>information_schema.KEY_COLUMN_USAGE.REFERENCED_COLUMN_NAME</code>.
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> REFERENCED_COLUMN_NAME = createField("REFERENCED_COLUMN_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(64), KEY_COLUMN_USAGE, "");

	/**
	 * No further instances allowed
	 */
	private KeyColumnUsage() {
		this("KEY_COLUMN_USAGE", null);
	}

	private KeyColumnUsage(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private KeyColumnUsage(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.mysql.information_schema.InformationSchema.INFORMATION_SCHEMA, aliased, parameters, "");
	}
}
