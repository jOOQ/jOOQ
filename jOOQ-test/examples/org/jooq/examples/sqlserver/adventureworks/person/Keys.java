/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.person;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the Person schema
 */
@SuppressWarnings({"unchecked"})
public class Keys extends org.jooq.impl.AbstractKeys {

	// IDENTITY definitions
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.Address, java.lang.Integer> IDENTITY_Address = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address, org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address.AddressID);
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType, java.lang.Integer> IDENTITY_AddressType = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType.AddressType, org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType.AddressType.AddressTypeID);
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.Contact, java.lang.Integer> IDENTITY_Contact = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.Contact.Contact, org.jooq.examples.sqlserver.adventureworks.person.tables.Contact.Contact.ContactID);
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.ContactType, java.lang.Integer> IDENTITY_ContactType = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.ContactType.ContactType, org.jooq.examples.sqlserver.adventureworks.person.tables.ContactType.ContactType.ContactTypeID);
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.StateProvince, java.lang.Integer> IDENTITY_StateProvince = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince.StateProvinceID);
	public static final org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.person.tables.records.vAdditionalContactInfo, java.lang.Integer> IDENTITY_vAdditionalContactInfo = createIdentity(org.jooq.examples.sqlserver.adventureworks.person.tables.vAdditionalContactInfo.vAdditionalContactInfo, org.jooq.examples.sqlserver.adventureworks.person.tables.vAdditionalContactInfo.vAdditionalContactInfo.ContactID);

	// UNIQUE and PRIMARY KEY definitions
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.Address> PK_Address_AddressID = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address, org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address.AddressID);
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.AddressType> PK_AddressType_AddressTypeID = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType.AddressType, org.jooq.examples.sqlserver.adventureworks.person.tables.AddressType.AddressType.AddressTypeID);
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.Contact> PK_Contact_ContactID = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.Contact.Contact, org.jooq.examples.sqlserver.adventureworks.person.tables.Contact.Contact.ContactID);
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.ContactType> PK_ContactType_ContactTypeID = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.ContactType.ContactType, org.jooq.examples.sqlserver.adventureworks.person.tables.ContactType.ContactType.ContactTypeID);
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.CountryRegion> PK_CountryRegion_CountryRegionCode = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.CountryRegion.CountryRegion, org.jooq.examples.sqlserver.adventureworks.person.tables.CountryRegion.CountryRegion.CountryRegionCode);
	public static final org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.StateProvince> PK_StateProvince_StateProvinceID = createUniqueKey(org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince.StateProvinceID);

	// FOREIGN KEY definitions
	public static final org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.Address, org.jooq.examples.sqlserver.adventureworks.person.tables.records.StateProvince> FK_Address_StateProvince_StateProvinceID = createForeignKey(PK_StateProvince_StateProvinceID, org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address, org.jooq.examples.sqlserver.adventureworks.person.tables.Address.Address.StateProvinceID);
	public static final org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.StateProvince, org.jooq.examples.sqlserver.adventureworks.person.tables.records.CountryRegion> FK_StateProvince_CountryRegion_CountryRegionCode = createForeignKey(PK_CountryRegion_CountryRegionCode, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince.CountryRegionCode);
	public static final org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.person.tables.records.StateProvince, org.jooq.examples.sqlserver.adventureworks.sales.tables.records.SalesTerritory> FK_StateProvince_SalesTerritory_TerritoryID = createForeignKey(org.jooq.examples.sqlserver.adventureworks.sales.Keys.PK_SalesTerritory_TerritoryID, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince, org.jooq.examples.sqlserver.adventureworks.person.tables.StateProvince.StateProvince.TerritoryID);

	/**
	 * No instances
	 */
	private Keys() {}
}
