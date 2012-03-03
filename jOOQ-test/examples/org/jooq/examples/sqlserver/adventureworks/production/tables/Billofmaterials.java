/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables;

/**
 * This class is generated by jOOQ.
 */
public class BillOfMaterials extends org.jooq.impl.UpdatableTableImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials> {

	private static final long serialVersionUID = -1174468949;

	/**
	 * The singleton instance of Production.BillOfMaterials
	 */
	public static final org.jooq.examples.sqlserver.adventureworks.production.tables.BillOfMaterials BillOfMaterials = new org.jooq.examples.sqlserver.adventureworks.production.tables.BillOfMaterials();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials> __RECORD_TYPE = org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.Integer> BillOfMaterialsID = createField("BillOfMaterialsID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * CONSTRAINT FK_BillOfMaterials_Product_ProductAssemblyID
	 * FOREIGN KEY (ProductAssemblyID)
	 * REFERENCES Production.Product (ProductID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.Integer> ProductAssemblyID = createField("ProductAssemblyID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * CONSTRAINT FK_BillOfMaterials_Product_ComponentID
	 * FOREIGN KEY (ComponentID)
	 * REFERENCES Production.Product (ProductID)
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.Integer> ComponentID = createField("ComponentID", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.sql.Timestamp> StartDate = createField("StartDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.sql.Timestamp> EndDate = createField("EndDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * An uncommented item
	 * <p>
	 * <code><pre>
	 * CONSTRAINT FK_BillOfMaterials_UnitMeasure_UnitMeasureCode
	 * FOREIGN KEY (UnitMeasureCode)
	 * REFERENCES Production.UnitMeasure (UnitMeasureCode)
	 * </pre></code>
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.String> UnitMeasureCode = createField("UnitMeasureCode", org.jooq.impl.SQLDataType.NCHAR, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.Short> BOMLevel = createField("BOMLevel", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.math.BigDecimal> PerAssemblyQty = createField("PerAssemblyQty", org.jooq.impl.SQLDataType.NUMERIC, this);

	/**
	 * An uncommented item
	 */
	public final org.jooq.TableField<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.sql.Timestamp> ModifiedDate = createField("ModifiedDate", org.jooq.impl.SQLDataType.TIMESTAMP, this);

	/**
	 * No further instances allowed
	 */
	private BillOfMaterials() {
		super("BillOfMaterials", org.jooq.examples.sqlserver.adventureworks.production.Production.Production);
	}

	/**
	 * No further instances allowed
	 */
	private BillOfMaterials(java.lang.String alias) {
		super(alias, org.jooq.examples.sqlserver.adventureworks.production.Production.Production, org.jooq.examples.sqlserver.adventureworks.production.tables.BillOfMaterials.BillOfMaterials);
	}

	@Override
	public org.jooq.Identity<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, java.lang.Integer> getIdentity() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.IDENTITY_BillOfMaterials;
	}

	@Override
	public org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials> getMainKey() {
		return org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_BillOfMaterials_BillOfMaterialsID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials>>asList(org.jooq.examples.sqlserver.adventureworks.production.Keys.PK_BillOfMaterials_BillOfMaterialsID);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.jooq.examples.sqlserver.adventureworks.production.tables.records.BillOfMaterials, ?>>asList(org.jooq.examples.sqlserver.adventureworks.production.Keys.FK_BillOfMaterials_Product_ProductAssemblyID, org.jooq.examples.sqlserver.adventureworks.production.Keys.FK_BillOfMaterials_Product_ComponentID, org.jooq.examples.sqlserver.adventureworks.production.Keys.FK_BillOfMaterials_UnitMeasure_UnitMeasureCode);
	}

	@Override
	public org.jooq.examples.sqlserver.adventureworks.production.tables.BillOfMaterials as(java.lang.String alias) {
		return new org.jooq.examples.sqlserver.adventureworks.production.tables.BillOfMaterials(alias);
	}
}
