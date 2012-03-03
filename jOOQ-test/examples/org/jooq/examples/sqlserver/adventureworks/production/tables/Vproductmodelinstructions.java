/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables;

/**
 * This class is generated by jOOQ.
 */
public class vProductModelInstructions extends org.jooq.impl.TableImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions> {

	private static final long serialVersionUID = 1867175673;

	/**
	 * The singleton instance of Production.vProductModelInstructions
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions vProductModelInstructions = new org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.Integer> ProductModelID = createField("ProductModelID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.String> Name = createField("Name", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.String> Instructions = createField("Instructions", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.Integer> LocationID = createField("LocationID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.math.BigDecimal> SetupHours = createField("SetupHours", org.jooq.impl.SQLDataType.NUMERIC, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.math.BigDecimal> MachineHours = createField("MachineHours", org.jooq.impl.SQLDataType.NUMERIC, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.math.BigDecimal> LaborHours = createField("LaborHours", org.jooq.impl.SQLDataType.NUMERIC, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.Integer> LotSize = createField("LotSize", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.String> Step = createField("Step", org.jooq.impl.SQLDataType.NVARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.String> rowguid = createField("rowguid", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private vProductModelInstructions() {
		super("vProductModelInstructions", org.jooq.examples.sqlserver.adventureworks.production.Production.Production);
	}

	/**
	 * No further instances allowed
	 */
	private vProductModelInstructions(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.production.Production.Production, org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions, java.lang.Integer> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.IDENTITY_vProductModelInstructions;
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions(alias);
	}
}
