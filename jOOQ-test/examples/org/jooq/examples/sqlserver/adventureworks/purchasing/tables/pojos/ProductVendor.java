/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.purchasing.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "ProductVendor", schema = "Purchasing", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"ProductID", "VendorID"})
})
public class ProductVendor implements java.io.Serializable {

	private static final long serialVersionUID = 239163359;


	@javax.validation.constraints.NotNull
	private java.lang.Integer    ProductID;

	@javax.validation.constraints.NotNull
	private java.lang.Integer    VendorID;

	@javax.validation.constraints.NotNull
	private java.lang.Integer    AverageLeadTime;

	@javax.validation.constraints.NotNull
	private java.math.BigDecimal StandardPrice;
	private java.math.BigDecimal LastReceiptCost;
	private java.sql.Timestamp   LastReceiptDate;

	@javax.validation.constraints.NotNull
	private java.lang.Integer    MinOrderQty;

	@javax.validation.constraints.NotNull
	private java.lang.Integer    MaxOrderQty;
	private java.lang.Integer    OnOrderQty;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 3)
	private java.lang.String     UnitMeasureCode;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp   ModifiedDate;

	@javax.persistence.Column(name = "ProductID", nullable = false, precision = 10)
	public java.lang.Integer getProductID() {
		return this.ProductID;
	}

	public void setProductID(java.lang.Integer ProductID) {
		this.ProductID = ProductID;
	}

	@javax.persistence.Column(name = "VendorID", nullable = false, precision = 10)
	public java.lang.Integer getVendorID() {
		return this.VendorID;
	}

	public void setVendorID(java.lang.Integer VendorID) {
		this.VendorID = VendorID;
	}

	@javax.persistence.Column(name = "AverageLeadTime", nullable = false, precision = 10)
	public java.lang.Integer getAverageLeadTime() {
		return this.AverageLeadTime;
	}

	public void setAverageLeadTime(java.lang.Integer AverageLeadTime) {
		this.AverageLeadTime = AverageLeadTime;
	}

	@javax.persistence.Column(name = "StandardPrice", nullable = false, precision = 19, scale = 4)
	public java.math.BigDecimal getStandardPrice() {
		return this.StandardPrice;
	}

	public void setStandardPrice(java.math.BigDecimal StandardPrice) {
		this.StandardPrice = StandardPrice;
	}

	@javax.persistence.Column(name = "LastReceiptCost", precision = 19, scale = 4)
	public java.math.BigDecimal getLastReceiptCost() {
		return this.LastReceiptCost;
	}

	public void setLastReceiptCost(java.math.BigDecimal LastReceiptCost) {
		this.LastReceiptCost = LastReceiptCost;
	}

	@javax.persistence.Column(name = "LastReceiptDate")
	public java.sql.Timestamp getLastReceiptDate() {
		return this.LastReceiptDate;
	}

	public void setLastReceiptDate(java.sql.Timestamp LastReceiptDate) {
		this.LastReceiptDate = LastReceiptDate;
	}

	@javax.persistence.Column(name = "MinOrderQty", nullable = false, precision = 10)
	public java.lang.Integer getMinOrderQty() {
		return this.MinOrderQty;
	}

	public void setMinOrderQty(java.lang.Integer MinOrderQty) {
		this.MinOrderQty = MinOrderQty;
	}

	@javax.persistence.Column(name = "MaxOrderQty", nullable = false, precision = 10)
	public java.lang.Integer getMaxOrderQty() {
		return this.MaxOrderQty;
	}

	public void setMaxOrderQty(java.lang.Integer MaxOrderQty) {
		this.MaxOrderQty = MaxOrderQty;
	}

	@javax.persistence.Column(name = "OnOrderQty", precision = 10)
	public java.lang.Integer getOnOrderQty() {
		return this.OnOrderQty;
	}

	public void setOnOrderQty(java.lang.Integer OnOrderQty) {
		this.OnOrderQty = OnOrderQty;
	}

	@javax.persistence.Column(name = "UnitMeasureCode", nullable = false, length = 3)
	public java.lang.String getUnitMeasureCode() {
		return this.UnitMeasureCode;
	}

	public void setUnitMeasureCode(java.lang.String UnitMeasureCode) {
		this.UnitMeasureCode = UnitMeasureCode;
	}

	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return this.ModifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
}
