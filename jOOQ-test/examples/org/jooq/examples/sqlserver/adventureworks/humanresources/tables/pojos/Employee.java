/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.humanresources.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "Employee", schema = "HumanResources")
public class Employee implements java.io.Serializable {

	private static final long serialVersionUID = -248178844;


	@javax.validation.constraints.NotNull
	private java.lang.Integer  EmployeeID;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 15)
	private java.lang.String   NationalIDNumber;

	@javax.validation.constraints.NotNull
	private java.lang.Integer  ContactID;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 256)
	private java.lang.String   LoginID;
	private java.lang.Integer  ManagerID;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 50)
	private java.lang.String   Title;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp BirthDate;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 1)
	private java.lang.String   MaritalStatus;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 1)
	private java.lang.String   Gender;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp HireDate;

	@javax.validation.constraints.NotNull
	private java.lang.Boolean  SalariedFlag;

	@javax.validation.constraints.NotNull
	private java.lang.Short    VacationHours;

	@javax.validation.constraints.NotNull
	private java.lang.Short    SickLeaveHours;

	@javax.validation.constraints.NotNull
	private java.lang.Boolean  CurrentFlag;

	@javax.validation.constraints.NotNull
	private java.lang.String   rowguid;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp ModifiedDate;

	@javax.persistence.Id
	@javax.persistence.Column(name = "EmployeeID", unique = true, nullable = false, precision = 10)
	public java.lang.Integer getEmployeeID() {
		return this.EmployeeID;
	}

	public void setEmployeeID(java.lang.Integer EmployeeID) {
		this.EmployeeID = EmployeeID;
	}

	@javax.persistence.Column(name = "NationalIDNumber", nullable = false, length = 15)
	public java.lang.String getNationalIDNumber() {
		return this.NationalIDNumber;
	}

	public void setNationalIDNumber(java.lang.String NationalIDNumber) {
		this.NationalIDNumber = NationalIDNumber;
	}

	@javax.persistence.Column(name = "ContactID", nullable = false, precision = 10)
	public java.lang.Integer getContactID() {
		return this.ContactID;
	}

	public void setContactID(java.lang.Integer ContactID) {
		this.ContactID = ContactID;
	}

	@javax.persistence.Column(name = "LoginID", nullable = false, length = 256)
	public java.lang.String getLoginID() {
		return this.LoginID;
	}

	public void setLoginID(java.lang.String LoginID) {
		this.LoginID = LoginID;
	}

	@javax.persistence.Column(name = "ManagerID", precision = 10)
	public java.lang.Integer getManagerID() {
		return this.ManagerID;
	}

	public void setManagerID(java.lang.Integer ManagerID) {
		this.ManagerID = ManagerID;
	}

	@javax.persistence.Column(name = "Title", nullable = false, length = 50)
	public java.lang.String getTitle() {
		return this.Title;
	}

	public void setTitle(java.lang.String Title) {
		this.Title = Title;
	}

	@javax.persistence.Column(name = "BirthDate", nullable = false)
	public java.sql.Timestamp getBirthDate() {
		return this.BirthDate;
	}

	public void setBirthDate(java.sql.Timestamp BirthDate) {
		this.BirthDate = BirthDate;
	}

	@javax.persistence.Column(name = "MaritalStatus", nullable = false, length = 1)
	public java.lang.String getMaritalStatus() {
		return this.MaritalStatus;
	}

	public void setMaritalStatus(java.lang.String MaritalStatus) {
		this.MaritalStatus = MaritalStatus;
	}

	@javax.persistence.Column(name = "Gender", nullable = false, length = 1)
	public java.lang.String getGender() {
		return this.Gender;
	}

	public void setGender(java.lang.String Gender) {
		this.Gender = Gender;
	}

	@javax.persistence.Column(name = "HireDate", nullable = false)
	public java.sql.Timestamp getHireDate() {
		return this.HireDate;
	}

	public void setHireDate(java.sql.Timestamp HireDate) {
		this.HireDate = HireDate;
	}

	@javax.persistence.Column(name = "SalariedFlag", nullable = false)
	public java.lang.Boolean getSalariedFlag() {
		return this.SalariedFlag;
	}

	public void setSalariedFlag(java.lang.Boolean SalariedFlag) {
		this.SalariedFlag = SalariedFlag;
	}

	@javax.persistence.Column(name = "VacationHours", nullable = false, precision = 5)
	public java.lang.Short getVacationHours() {
		return this.VacationHours;
	}

	public void setVacationHours(java.lang.Short VacationHours) {
		this.VacationHours = VacationHours;
	}

	@javax.persistence.Column(name = "SickLeaveHours", nullable = false, precision = 5)
	public java.lang.Short getSickLeaveHours() {
		return this.SickLeaveHours;
	}

	public void setSickLeaveHours(java.lang.Short SickLeaveHours) {
		this.SickLeaveHours = SickLeaveHours;
	}

	@javax.persistence.Column(name = "CurrentFlag", nullable = false)
	public java.lang.Boolean getCurrentFlag() {
		return this.CurrentFlag;
	}

	public void setCurrentFlag(java.lang.Boolean CurrentFlag) {
		this.CurrentFlag = CurrentFlag;
	}

	@javax.persistence.Column(name = "rowguid", nullable = false)
	public java.lang.String getrowguid() {
		return this.rowguid;
	}

	public void setrowguid(java.lang.String rowguid) {
		this.rowguid = rowguid;
	}

	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return this.ModifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
}
