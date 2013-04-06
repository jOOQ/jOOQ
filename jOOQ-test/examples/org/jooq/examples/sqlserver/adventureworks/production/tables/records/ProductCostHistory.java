/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings("all")
@javax.persistence.Entity
@javax.persistence.Table(name = "ProductCostHistory", schema = "Production", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"ProductID", "StartDate"})
})
public class ProductCostHistory extends org.jooq.impl.UpdatableRecordImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ProductCostHistory> implements org.jooq.Record5<java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.math.BigDecimal, java.sql.Timestamp> {

	private static final long serialVersionUID = -435513731;

	/**
	 * Setter for <code>Production.ProductCostHistory.ProductID</code>. 
	 */
	public void setProductID(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ProductID, value);
	}

	/**
	 * Getter for <code>Production.ProductCostHistory.ProductID</code>. 
	 */
	@javax.persistence.Column(name = "ProductID", nullable = false, precision = 10)
	public java.lang.Integer getProductID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ProductID);
	}

	/**
	 * Setter for <code>Production.ProductCostHistory.StartDate</code>. 
	 */
	public void setStartDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StartDate, value);
	}

	/**
	 * Getter for <code>Production.ProductCostHistory.StartDate</code>. 
	 */
	@javax.persistence.Column(name = "StartDate", nullable = false)
	public java.sql.Timestamp getStartDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StartDate);
	}

	/**
	 * Setter for <code>Production.ProductCostHistory.EndDate</code>. 
	 */
	public void setEndDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.EndDate, value);
	}

	/**
	 * Getter for <code>Production.ProductCostHistory.EndDate</code>. 
	 */
	@javax.persistence.Column(name = "EndDate")
	public java.sql.Timestamp getEndDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.EndDate);
	}

	/**
	 * Setter for <code>Production.ProductCostHistory.StandardCost</code>. 
	 */
	public void setStandardCost(java.math.BigDecimal value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StandardCost, value);
	}

	/**
	 * Getter for <code>Production.ProductCostHistory.StandardCost</code>. 
	 */
	@javax.persistence.Column(name = "StandardCost", nullable = false, precision = 19, scale = 4)
	public java.math.BigDecimal getStandardCost() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StandardCost);
	}

	/**
	 * Setter for <code>Production.ProductCostHistory.ModifiedDate</code>. 
	 */
	public void setModifiedDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ModifiedDate, value);
	}

	/**
	 * Getter for <code>Production.ProductCostHistory.ModifiedDate</code>. 
	 */
	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ModifiedDate);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Integer, java.sql.Timestamp> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.math.BigDecimal, java.sql.Timestamp> fieldsRow() {
		return org.jooq.impl.DSL.row(field1(), field2(), field3(), field4(), field5());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.math.BigDecimal, java.sql.Timestamp> valuesRow() {
		return org.jooq.impl.DSL.row(value1(), value2(), value3(), value4(), value5());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ProductID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field2() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StartDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field3() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.EndDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field4() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.StandardCost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field5() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory.ModifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getProductID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value2() {
		return getStartDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value3() {
		return getEndDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value4() {
		return getStandardCost();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value5() {
		return getModifiedDate();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ProductCostHistory
	 */
	public ProductCostHistory() {
		super(org.jooq.examples.sqlserver.adventureworks.production.tables.ProductCostHistory.ProductCostHistory);
	}
}
