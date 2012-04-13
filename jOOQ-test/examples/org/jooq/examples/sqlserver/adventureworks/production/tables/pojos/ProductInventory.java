/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "ProductInventory", schema = "Production", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"ProductID", "LocationID"})
})
public class ProductInventory implements java.io.Serializable {

	private static final long serialVersionUID = 1935041794;


	@javax.validation.constraints.NotNull
	private java.lang.Integer  ProductID;

	@javax.validation.constraints.NotNull
	private java.lang.Short    LocationID;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 10)
	private java.lang.String   Shelf;

	@javax.validation.constraints.NotNull
	private java.lang.Byte     Bin;

	@javax.validation.constraints.NotNull
	private java.lang.Short    Quantity;

	@javax.validation.constraints.NotNull
	private java.lang.String   rowguid;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp ModifiedDate;

	@javax.persistence.Column(name = "ProductID", nullable = false, precision = 10)
	public java.lang.Integer getProductID() {
		return this.ProductID;
	}

	public void setProductID(java.lang.Integer ProductID) {
		this.ProductID = ProductID;
	}

	@javax.persistence.Column(name = "LocationID", nullable = false, precision = 5)
	public java.lang.Short getLocationID() {
		return this.LocationID;
	}

	public void setLocationID(java.lang.Short LocationID) {
		this.LocationID = LocationID;
	}

	@javax.persistence.Column(name = "Shelf", nullable = false, length = 10)
	public java.lang.String getShelf() {
		return this.Shelf;
	}

	public void setShelf(java.lang.String Shelf) {
		this.Shelf = Shelf;
	}

	@javax.persistence.Column(name = "Bin", nullable = false, precision = 3)
	public java.lang.Byte getBin() {
		return this.Bin;
	}

	public void setBin(java.lang.Byte Bin) {
		this.Bin = Bin;
	}

	@javax.persistence.Column(name = "Quantity", nullable = false, precision = 5)
	public java.lang.Short getQuantity() {
		return this.Quantity;
	}

	public void setQuantity(java.lang.Short Quantity) {
		this.Quantity = Quantity;
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
