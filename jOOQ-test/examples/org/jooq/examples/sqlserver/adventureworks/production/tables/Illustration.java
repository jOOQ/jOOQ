/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables;

/**
 * This class is generated by jOOQ.
 */
public class Illustration extends org.jooq.impl.UpdatableTableImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration> {

	private static final long serialVersionUID = -489323956;

	/**
	 * The singleton instance of Production.Illustration
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration Illustration = new org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration, java.lang.Integer> IllustrationID = createField("IllustrationID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 * 
	 * The SQL type of this item (xml, ) could not be mapped.<br/>
	 * Deserialising this field might not work!
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration, java.lang.Object> Diagram = createField("Diagram", org.jooq.util.sqlserver.SQLServerDataType.getDefaultDataType("xml"), this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private Illustration() {
		super("Illustration", org.jooq.examples.sqlserver.adventureworks.production.Production.Production);
	}

	/**
	 * No further instances allowed
	 */
	private Illustration(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.production.Production.Production, org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration.Illustration);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration, java.lang.Integer> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.IDENTITY_Illustration;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration> getMainKey() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_Illustration_IllustrationID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.Illustration>>asList(org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_Illustration_IllustrationID);
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration(alias);
	}
}
