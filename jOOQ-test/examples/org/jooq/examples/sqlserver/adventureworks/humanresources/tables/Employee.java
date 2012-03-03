/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.humanresources.tables;

/**
 * This class is generated by jOOQ.
 */
public class Employee extends org.jooq.impl.UpdatableTableImpl<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee> {

	private static final long serialVersionUID = -782548277;

	/**
	 * The singleton instance of HumanResources.Employee
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.humanresources.tables.Employee Employee = new org.jooq.examples.sqlserver.adventureworks.humanresources.tables.Employee();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Integer> EmployeeID = createField("EmployeeID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> NationalIDNumber = createField("NationalIDNumber", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * CONSTRAINT FK_Employee_Contact_ContactID
	 * FOREIGN KEY (ContactID)
	 * REFERENCES Person.Contact (ContactID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Integer> ContactID = createField("ContactID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> LoginID = createField("LoginID", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * CONSTRAINT FK_Employee_Employee_ManagerID
	 * FOREIGN KEY (ManagerID)
	 * REFERENCES HumanResources.Employee (EmployeeID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Integer> ManagerID = createField("ManagerID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> Title = createField("Title", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.sql.Timestamp> BirthDate = createField("BirthDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> MaritalStatus = createField("MaritalStatus", org.jooq.impl.SQLDataType.NCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> Gender = createField("Gender", org.jooq.impl.SQLDataType.NCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.sql.Timestamp> HireDate = createField("HireDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Boolean> SalariedFlag = createField("SalariedFlag", org.jooq.impl.SQLDataType.BIT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Short> VacationHours = createField("VacationHours", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Short> SickLeaveHours = createField("SickLeaveHours", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Boolean> CurrentFlag = createField("CurrentFlag", org.jooq.impl.SQLDataType.BIT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.String> rowguid = createField("rowguid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private Employee() {
		super("Employee", org.jooq.examples.sqlserver.adventureworks.humanresources.HumanResources.HumanResources);
	}

	/**
	 * No further instances allowed
	 */
	private Employee(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.humanresources.HumanResources.HumanResources, org.jooq.examples.sqlserver.adventureworks.humanresources.tables.Employee.Employee);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, java.lang.Integer> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.humanresources.Keys.IDENTITY_Employee;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee> getMainKey() {
		return org.jooq.examples.sqlserver.adventureworks.humanresources.Keys.PK_Employee_EmployeeID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee>>asList(org.jooq.examples.sqlserver.adventureworks.humanresources.Keys.PK_Employee_EmployeeID);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.humanresources.tables.records.Employee, ?>>asList(org.jooq.examples.sqlserver.adventureworks.humanresources.Keys.FK_Employee_Contact_ContactID, org.jooq.examples.sqlserver.adventureworks.humanresources.Keys.FK_Employee_Employee_ManagerID);
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.humanresources.tables.Employee as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.humanresources.tables.Employee(alias);
	}
}
