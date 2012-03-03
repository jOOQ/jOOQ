/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables;

/**
 * This class is generated by jOOQ.
 */
public class ScrapReason extends org.jooq.impl.UpdatableTableImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason> {

	private static final long serialVersionUID = 1027305550;

	/**
	 * The singleton instance of Production.ScrapReason
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.production.tables.ScrapReason ScrapReason = new org.jooq.examples.sqlserver.adventureworks.production.tables.ScrapReason();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason, java.lang.Short> ScrapReasonID = createField("ScrapReasonID", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason, java.lang.String> Name = createField("Name", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private ScrapReason() {
		super("ScrapReason", org.jooq.examples.sqlserver.adventureworks.production.Production.Production);
	}

	/**
	 * No further instances allowed
	 */
	private ScrapReason(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.production.Production.Production, org.jooq.examples.sqlserver.adventureworks.production.tables.ScrapReason.ScrapReason);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason, java.lang.Short> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.IDENTITY_ScrapReason;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason> getMainKey() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_ScrapReason_ScrapReasonID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ScrapReason>>asList(org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_ScrapReason_ScrapReasonID);
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.production.tables.ScrapReason as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.production.tables.ScrapReason(alias);
	}
}
