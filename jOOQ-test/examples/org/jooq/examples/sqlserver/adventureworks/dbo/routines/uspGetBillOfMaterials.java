/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.dbo.routines;

/**
 * This class is generated by jOOQ.
 */
public class uspGetBillOfMaterials extends org.jooq.impl.AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = 2139682679;


	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.lang.Integer> StartProductID = createParameter("StartProductID", org.jooq.impl.SQLDataType.INTEGER);

	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.sql.Timestamp> CheckDate = createParameter("CheckDate", org.jooq.impl.SQLDataType.TIMESTAMP);

	/**
	 * Create a new routine call instance
	 */
	public uspGetBillOfMaterials() {
		super("uspGetBillOfMaterials", org.jooq.examples.sqlserver.adventureworks.dbo.dbo.dbo);

		addInParameter(StartProductID);
		addInParameter(CheckDate);
	}

	/**
	 * Set the <code>StartProductID</code> parameter to the routine
	 */
	public void setStartProductID(java.lang.Integer value) {
		setValue(StartProductID, value);
	}

	/**
	 * Set the <code>CheckDate</code> parameter to the routine
	 */
	public void setCheckDate(java.sql.Timestamp value) {
		setValue(CheckDate, value);
	}
}
