/**
 * This class is generated by jOOQ
 */
package org.jooq.util.hsqldb.information_schema.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.0.0"},
                            comments = "This class is generated by jOOQ")
public class Columns extends org.jooq.impl.TableImpl<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord> {

	private static final long serialVersionUID = -954327901;

	/**
	 * The singleton instance of COLUMNS
	 */
	public static final org.jooq.util.hsqldb.information_schema.tables.Columns COLUMNS = new org.jooq.util.hsqldb.information_schema.tables.Columns();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord> __RECORD_TYPE = org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> TABLE_CATALOG = createField("TABLE_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> TABLE_SCHEMA = createField("TABLE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> TABLE_NAME = createField("TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> COLUMN_NAME = createField("COLUMN_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> ORDINAL_POSITION = createField("ORDINAL_POSITION", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> COLUMN_DEFAULT = createField("COLUMN_DEFAULT", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IS_NULLABLE = createField("IS_NULLABLE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DATA_TYPE = createField("DATA_TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> CHARACTER_MAXIMUM_LENGTH = createField("CHARACTER_MAXIMUM_LENGTH", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> CHARACTER_OCTET_LENGTH = createField("CHARACTER_OCTET_LENGTH", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> NUMERIC_PRECISION = createField("NUMERIC_PRECISION", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> NUMERIC_PRECISION_RADIX = createField("NUMERIC_PRECISION_RADIX", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> NUMERIC_SCALE = createField("NUMERIC_SCALE", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> DATETIME_PRECISION = createField("DATETIME_PRECISION", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> INTERVAL_TYPE = createField("INTERVAL_TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> INTERVAL_PRECISION = createField("INTERVAL_PRECISION", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> CHARACTER_SET_CATALOG = createField("CHARACTER_SET_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> CHARACTER_SET_SCHEMA = createField("CHARACTER_SET_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> CHARACTER_SET_NAME = createField("CHARACTER_SET_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> COLLATION_CATALOG = createField("COLLATION_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> COLLATION_SCHEMA = createField("COLLATION_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> COLLATION_NAME = createField("COLLATION_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DOMAIN_CATALOG = createField("DOMAIN_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DOMAIN_SCHEMA = createField("DOMAIN_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DOMAIN_NAME = createField("DOMAIN_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> UDT_CATALOG = createField("UDT_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> UDT_SCHEMA = createField("UDT_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> UDT_NAME = createField("UDT_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> SCOPE_CATALOG = createField("SCOPE_CATALOG", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> SCOPE_SCHEMA = createField("SCOPE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> SCOPE_NAME = createField("SCOPE_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> MAXIMUM_CARDINALITY = createField("MAXIMUM_CARDINALITY", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DTD_IDENTIFIER = createField("DTD_IDENTIFIER", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IS_SELF_REFERENCING = createField("IS_SELF_REFERENCING", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IS_IDENTITY = createField("IS_IDENTITY", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_GENERATION = createField("IDENTITY_GENERATION", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_START = createField("IDENTITY_START", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_INCREMENT = createField("IDENTITY_INCREMENT", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_MAXIMUM = createField("IDENTITY_MAXIMUM", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_MINIMUM = createField("IDENTITY_MINIMUM", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IDENTITY_CYCLE = createField("IDENTITY_CYCLE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IS_GENERATED = createField("IS_GENERATED", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> GENERATION_EXPRESSION = createField("GENERATION_EXPRESSION", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> IS_UPDATABLE = createField("IS_UPDATABLE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.String> DECLARED_DATA_TYPE = createField("DECLARED_DATA_TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> DECLARED_NUMERIC_PRECISION = createField("DECLARED_NUMERIC_PRECISION", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.util.hsqldb.information_schema.tables.records.ColumnsRecord, java.lang.Long> DECLARED_NUMERIC_SCALE = createField("DECLARED_NUMERIC_SCALE", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * No further instances allowed
	 */
	private Columns() {
		super("COLUMNS", org.jooq.util.hsqldb.information_schema.InformationSchema.INFORMATION_SCHEMA);
	}

	/**
	 * No further instances allowed
	 */
	private Columns(java.lang.String alias) {
		super(alias, org.jooq.util.hsqldb.information_schema.InformationSchema.INFORMATION_SCHEMA, org.jooq.util.hsqldb.information_schema.tables.Columns.COLUMNS);
	}

	@Override
	public org.jooq.util.hsqldb.information_schema.tables.Columns as(java.lang.String alias) {
		return new org.jooq.util.hsqldb.information_schema.tables.Columns(alias);
	}
}
