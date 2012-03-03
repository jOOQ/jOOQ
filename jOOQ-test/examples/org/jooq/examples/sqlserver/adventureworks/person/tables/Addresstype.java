/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.person.tables;

/**
 * This class is generated by jOOQ.
 */
public class AddressType extends org.jooq.impl.UpdatableTableImpl<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType> {

	private static final long serialVersionUID = 1295094076;

	/**
	 * The singleton instance of Person.AddressType
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType AddressType = new org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.lang.Integer> AddressTypeID = createField("AddressTypeID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.lang.String> Name = createField("Name", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.lang.String> rowguid = createField("rowguid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private AddressType() {
		super("AddressType", org.jooq.examples.sqlserver.adventureworks.person.Person.Person);
	}

	/**
	 * No further instances allowed
	 */
	private AddressType(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.person.Person.Person, org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType.AddressType);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.lang.Integer> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.person.Keys.IDENTITY_AddressType;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType> getMainKey() {
		return org.jooq.examples.sqlserver.adventureworks.person.Keys.PK_AddressType_AddressTypeID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType>>asList(org.jooq.examples.sqlserver.adventureworks.person.Keys.PK_AddressType_AddressTypeID);
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType(alias);
	}
}
