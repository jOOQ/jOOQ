/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.purchasing.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "VendorAddress", schema = "Purchasing", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"VendorID", "AddressID"})
})
public class VendorAddress implements java.io.Serializable {

	private static final long serialVersionUID = 1875259502;

	private java.lang.Integer  VendorID;
	private java.lang.Integer  AddressID;
	private java.lang.Integer  AddressTypeID;
	private java.sql.Timestamp ModifiedDate;

	@javax.persistence.Column(name = "VendorID", nullable = false)
	public java.lang.Integer getVendorID() {
		return this.VendorID;
	}

	public void setVendorID(java.lang.Integer VendorID) {
		this.VendorID = VendorID;
	}

	@javax.persistence.Column(name = "AddressID", nullable = false)
	public java.lang.Integer getAddressID() {
		return this.AddressID;
	}

	public void setAddressID(java.lang.Integer AddressID) {
		this.AddressID = AddressID;
	}

	@javax.persistence.Column(name = "AddressTypeID", nullable = false)
	public java.lang.Integer getAddressTypeID() {
		return this.AddressTypeID;
	}

	public void setAddressTypeID(java.lang.Integer AddressTypeID) {
		this.AddressTypeID = AddressTypeID;
	}

	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return this.ModifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
}
