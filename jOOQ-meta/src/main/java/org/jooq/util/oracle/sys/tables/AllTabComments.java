/**
 * This class is generated by jOOQ
 */
package org.jooq.util.oracle.sys.tables;

/**
 * This class is generated by jOOQ.
 *
 * Comments on tables and views accessible to the user
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AllTabComments extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 1279621736;

	/**
	 * The singleton instance of <code>SYS.ALL_TAB_COMMENTS</code>
	 */
	public static final org.jooq.util.oracle.sys.tables.AllTabComments ALL_TAB_COMMENTS = new org.jooq.util.oracle.sys.tables.AllTabComments();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.OWNER</code>. Owner of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> OWNER = createField("OWNER", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), this, "Owner of the object");

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.TABLE_NAME</code>. Name of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_NAME = createField("TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), this, "Name of the object");

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.TABLE_TYPE</code>. Type of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_TYPE = createField("TABLE_TYPE", org.jooq.impl.SQLDataType.VARCHAR.length(11), this, "Type of the object");

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.COMMENTS</code>. Comment on the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> COMMENTS = createField("COMMENTS", org.jooq.impl.SQLDataType.VARCHAR.length(4000), this, "Comment on the object");

	/**
	 * Create a <code>SYS.ALL_TAB_COMMENTS</code> table reference
	 */
	public AllTabComments() {
		this("ALL_TAB_COMMENTS", null);
	}

	/**
	 * Create an aliased <code>SYS.ALL_TAB_COMMENTS</code> table reference
	 */
	public AllTabComments(java.lang.String alias) {
		this(alias, org.jooq.util.oracle.sys.tables.AllTabComments.ALL_TAB_COMMENTS);
	}

	private AllTabComments(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private AllTabComments(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.oracle.sys.Sys.SYS, aliased, parameters, "Comments on tables and views accessible to the user");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.oracle.sys.tables.AllTabComments as(java.lang.String alias) {
		return new org.jooq.util.oracle.sys.tables.AllTabComments(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.util.oracle.sys.tables.AllTabComments rename(java.lang.String name) {
		return new org.jooq.util.oracle.sys.tables.AllTabComments(name, null);
	}
}
