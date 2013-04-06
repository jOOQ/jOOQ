/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings("all")
@javax.persistence.Entity
@javax.persistence.Table(name = "WorkOrder", schema = "Production")
public class WorkOrder extends org.jooq.impl.UpdatableRecordImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.WorkOrder> implements org.jooq.Record10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Short, java.sql.Timestamp, java.sql.Timestamp, java.sql.Timestamp, java.lang.Short, java.sql.Timestamp> {

	private static final long serialVersionUID = -443489619;

	/**
	 * Setter for <code>Production.WorkOrder.WorkOrderID</code>. 
	 */
	public void setWorkOrderID(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.WorkOrderID, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.WorkOrderID</code>. 
	 */
	@javax.persistence.Id
	@javax.persistence.Column(name = "WorkOrderID", unique = true, nullable = false, precision = 10)
	public java.lang.Integer getWorkOrderID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.WorkOrderID);
	}

	/**
	 * Setter for <code>Production.WorkOrder.ProductID</code>. 
	 */
	public void setProductID(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ProductID, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.ProductID</code>. 
	 */
	@javax.persistence.Column(name = "ProductID", nullable = false, precision = 10)
	public java.lang.Integer getProductID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ProductID);
	}

	/**
	 * Setter for <code>Production.WorkOrder.OrderQty</code>. 
	 */
	public void setOrderQty(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.OrderQty, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.OrderQty</code>. 
	 */
	@javax.persistence.Column(name = "OrderQty", nullable = false, precision = 10)
	public java.lang.Integer getOrderQty() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.OrderQty);
	}

	/**
	 * Setter for <code>Production.WorkOrder.StockedQty</code>. 
	 */
	public void setStockedQty(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StockedQty, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.StockedQty</code>. 
	 */
	@javax.persistence.Column(name = "StockedQty", nullable = false, precision = 10)
	public java.lang.Integer getStockedQty() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StockedQty);
	}

	/**
	 * Setter for <code>Production.WorkOrder.ScrappedQty</code>. 
	 */
	public void setScrappedQty(java.lang.Short value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrappedQty, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.ScrappedQty</code>. 
	 */
	@javax.persistence.Column(name = "ScrappedQty", nullable = false, precision = 5)
	public java.lang.Short getScrappedQty() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrappedQty);
	}

	/**
	 * Setter for <code>Production.WorkOrder.StartDate</code>. 
	 */
	public void setStartDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StartDate, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.StartDate</code>. 
	 */
	@javax.persistence.Column(name = "StartDate", nullable = false)
	public java.sql.Timestamp getStartDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StartDate);
	}

	/**
	 * Setter for <code>Production.WorkOrder.EndDate</code>. 
	 */
	public void setEndDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.EndDate, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.EndDate</code>. 
	 */
	@javax.persistence.Column(name = "EndDate")
	public java.sql.Timestamp getEndDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.EndDate);
	}

	/**
	 * Setter for <code>Production.WorkOrder.DueDate</code>. 
	 */
	public void setDueDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.DueDate, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.DueDate</code>. 
	 */
	@javax.persistence.Column(name = "DueDate", nullable = false)
	public java.sql.Timestamp getDueDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.DueDate);
	}

	/**
	 * Setter for <code>Production.WorkOrder.ScrapReasonID</code>. 
	 */
	public void setScrapReasonID(java.lang.Short value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrapReasonID, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.ScrapReasonID</code>. 
	 */
	@javax.persistence.Column(name = "ScrapReasonID", precision = 5)
	public java.lang.Short getScrapReasonID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrapReasonID);
	}

	/**
	 * Setter for <code>Production.WorkOrder.ModifiedDate</code>. 
	 */
	public void setModifiedDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ModifiedDate, value);
	}

	/**
	 * Getter for <code>Production.WorkOrder.ModifiedDate</code>. 
	 */
	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ModifiedDate);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record10 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Short, java.sql.Timestamp, java.sql.Timestamp, java.sql.Timestamp, java.lang.Short, java.sql.Timestamp> fieldsRow() {
		return org.jooq.impl.DSL.row(field1(), field2(), field3(), field4(), field5(), field6(), field7(), field8(), field9(), field10());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Short, java.sql.Timestamp, java.sql.Timestamp, java.sql.Timestamp, java.lang.Short, java.sql.Timestamp> valuesRow() {
		return org.jooq.impl.DSL.row(value1(), value2(), value3(), value4(), value5(), value6(), value7(), value8(), value9(), value10());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.WorkOrderID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ProductID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.OrderQty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StockedQty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Short> field5() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrappedQty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.StartDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field7() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.EndDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field8() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.DueDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Short> field9() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ScrapReasonID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field10() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder.ModifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getWorkOrderID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getProductID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getOrderQty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getStockedQty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Short value5() {
		return getScrappedQty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getStartDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value7() {
		return getEndDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value8() {
		return getDueDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Short value9() {
		return getScrapReasonID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value10() {
		return getModifiedDate();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached WorkOrder
	 */
	public WorkOrder() {
		super(org.jooq.examples.sqlserver.adventureworks.production.tables.WorkOrder.WorkOrder);
	}
}
