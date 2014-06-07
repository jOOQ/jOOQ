/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 *
 * An unused table in the same schema. 
 * 
 * "Its comments contain special characters"
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "X_UNUSED", uniqueConstraints = {
	@javax.persistence.UniqueConstraint(columnNames = {"ID", "NAME"})
})
public class X_UNUSED extends org.jooq.impl.UpdatableRecordImpl<org.jooq.test.oracle3.generatedclasses.tables.records.X_UNUSED> implements java.lang.Cloneable, org.jooq.Record16<java.lang.Integer, java.lang.String, java.math.BigInteger, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.math.BigDecimal, java.lang.Integer, java.lang.String> {

	private static final long serialVersionUID = -1066112569;

	/**
	 * Setter for <code>X_UNUSED.ID</code>. An unused column of an unused table in the same schema. 

"Its comments contain special characters"
	 */
	public void setID(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>X_UNUSED.ID</code>. An unused column of an unused table in the same schema. 

"Its comments contain special characters"
	 */
	@javax.persistence.Column(name = "ID", unique = true, nullable = false, precision = 7)
	public java.lang.Integer getID() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>X_UNUSED.NAME</code>.
	 */
	public void setNAME(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>X_UNUSED.NAME</code>.
	 */
	@javax.persistence.Column(name = "NAME", nullable = false, length = 10)
	public java.lang.String getNAME() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>X_UNUSED.BIG_INTEGER</code>.
	 */
	public void setBIG_INTEGER(java.math.BigInteger value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>X_UNUSED.BIG_INTEGER</code>.
	 */
	@javax.persistence.Column(name = "BIG_INTEGER", precision = 38)
	public java.math.BigInteger getBIG_INTEGER() {
		return (java.math.BigInteger) getValue(2);
	}

	/**
	 * Setter for <code>X_UNUSED.ID_REF</code>.
	 */
	public void setID_REF(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>X_UNUSED.ID_REF</code>.
	 */
	@javax.persistence.Column(name = "ID_REF", precision = 7)
	public java.lang.Integer getID_REF() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>X_UNUSED.CLASS</code>.
	 */
	public void setCLASS(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>X_UNUSED.CLASS</code>.
	 */
	@javax.persistence.Column(name = "CLASS", precision = 7)
	public java.lang.Integer getCLASS() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>X_UNUSED.FIELDS</code>.
	 */
	public void setFIELDS(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>X_UNUSED.FIELDS</code>.
	 */
	@javax.persistence.Column(name = "FIELDS", precision = 7)
	public java.lang.Integer getFIELDS() {
		return (java.lang.Integer) getValue(5);
	}

	/**
	 * Setter for <code>X_UNUSED.CONFIGURATION</code>.
	 */
	public void setCONFIGURATION(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>X_UNUSED.CONFIGURATION</code>.
	 */
	@javax.persistence.Column(name = "CONFIGURATION", precision = 7)
	public java.lang.Integer getCONFIGURATION() {
		return (java.lang.Integer) getValue(6);
	}

	/**
	 * Setter for <code>X_UNUSED.U_D_T</code>.
	 */
	public void setU_D_T(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>X_UNUSED.U_D_T</code>.
	 */
	@javax.persistence.Column(name = "U_D_T", precision = 7)
	public java.lang.Integer getU_D_T() {
		return (java.lang.Integer) getValue(7);
	}

	/**
	 * Setter for <code>X_UNUSED.META_DATA</code>.
	 */
	public void setMETA_DATA(java.lang.Integer value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>X_UNUSED.META_DATA</code>.
	 */
	@javax.persistence.Column(name = "META_DATA", precision = 7)
	public java.lang.Integer getMETA_DATA() {
		return (java.lang.Integer) getValue(8);
	}

	/**
	 * Setter for <code>X_UNUSED.TYPE0</code>.
	 */
	public void setTYPE0(java.lang.Integer value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>X_UNUSED.TYPE0</code>.
	 */
	@javax.persistence.Column(name = "TYPE0", precision = 7)
	public java.lang.Integer getTYPE0() {
		return (java.lang.Integer) getValue(9);
	}

	/**
	 * Setter for <code>X_UNUSED.PRIMARY_KEY</code>.
	 */
	public void setPRIMARY_KEY(java.lang.Integer value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>X_UNUSED.PRIMARY_KEY</code>.
	 */
	@javax.persistence.Column(name = "PRIMARY_KEY", precision = 7)
	public java.lang.Integer getPRIMARY_KEY() {
		return (java.lang.Integer) getValue(10);
	}

	/**
	 * Setter for <code>X_UNUSED.PRIMARYKEY</code>.
	 */
	public void setPRIMARYKEY(java.lang.Integer value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>X_UNUSED.PRIMARYKEY</code>.
	 */
	@javax.persistence.Column(name = "PRIMARYKEY", precision = 7)
	public java.lang.Integer getPRIMARYKEY() {
		return (java.lang.Integer) getValue(11);
	}

	/**
	 * Setter for <code>X_UNUSED.NAME_REF</code>.
	 */
	public void setNAME_REF(java.lang.String value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>X_UNUSED.NAME_REF</code>.
	 */
	@javax.persistence.Column(name = "NAME_REF", length = 10)
	public java.lang.String getNAME_REF() {
		return (java.lang.String) getValue(12);
	}

	/**
	 * Setter for <code>X_UNUSED.FIELD 737</code>.
	 */
	public void setFIELD_737(java.math.BigDecimal value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>X_UNUSED.FIELD 737</code>.
	 */
	@javax.persistence.Column(name = "FIELD 737", precision = 25, scale = 2)
	public java.math.BigDecimal getFIELD_737() {
		return (java.math.BigDecimal) getValue(13);
	}

	/**
	 * Setter for <code>X_UNUSED.MS_UNUSED_ID_REF</code>.
	 */
	public void setMS_UNUSED_ID_REF(java.lang.Integer value) {
		setValue(14, value);
	}

	/**
	 * Getter for <code>X_UNUSED.MS_UNUSED_ID_REF</code>.
	 */
	@javax.persistence.Column(name = "MS_UNUSED_ID_REF", precision = 7)
	public java.lang.Integer getMS_UNUSED_ID_REF() {
		return (java.lang.Integer) getValue(14);
	}

	/**
	 * Setter for <code>X_UNUSED.MS_UNUSED_NAME_REF</code>.
	 */
	public void setMS_UNUSED_NAME_REF(java.lang.String value) {
		setValue(15, value);
	}

	/**
	 * Getter for <code>X_UNUSED.MS_UNUSED_NAME_REF</code>.
	 */
	@javax.persistence.Column(name = "MS_UNUSED_NAME_REF", length = 10)
	public java.lang.String getMS_UNUSED_NAME_REF() {
		return (java.lang.String) getValue(15);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Integer, java.lang.String> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record16 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row16<java.lang.Integer, java.lang.String, java.math.BigInteger, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.math.BigDecimal, java.lang.Integer, java.lang.String> fieldsRow() {
		return (org.jooq.Row16) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row16<java.lang.Integer, java.lang.String, java.math.BigInteger, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.math.BigDecimal, java.lang.Integer, java.lang.String> valuesRow() {
		return (org.jooq.Row16) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigInteger> field3() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.BIG_INTEGER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.ID_REF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.CLASS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.FIELDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.CONFIGURATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.U_D_T;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field9() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.META_DATA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field10() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.TYPE0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field11() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.PRIMARY_KEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field12() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.PRIMARYKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field13() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.NAME_REF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field14() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.FIELD_737;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field15() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.MS_UNUSED_ID_REF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field16() {
		return org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED.MS_UNUSED_NAME_REF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getNAME();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigInteger value3() {
		return getBIG_INTEGER();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getID_REF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getCLASS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getFIELDS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getCONFIGURATION();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getU_D_T();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value9() {
		return getMETA_DATA();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value10() {
		return getTYPE0();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value11() {
		return getPRIMARY_KEY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value12() {
		return getPRIMARYKEY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value13() {
		return getNAME_REF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value14() {
		return getFIELD_737();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value15() {
		return getMS_UNUSED_ID_REF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value16() {
		return getMS_UNUSED_NAME_REF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value1(java.lang.Integer value) {
		setID(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value2(java.lang.String value) {
		setNAME(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value3(java.math.BigInteger value) {
		setBIG_INTEGER(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value4(java.lang.Integer value) {
		setID_REF(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value5(java.lang.Integer value) {
		setCLASS(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value6(java.lang.Integer value) {
		setFIELDS(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value7(java.lang.Integer value) {
		setCONFIGURATION(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value8(java.lang.Integer value) {
		setU_D_T(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value9(java.lang.Integer value) {
		setMETA_DATA(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value10(java.lang.Integer value) {
		setTYPE0(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value11(java.lang.Integer value) {
		setPRIMARY_KEY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value12(java.lang.Integer value) {
		setPRIMARYKEY(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value13(java.lang.String value) {
		setNAME_REF(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value14(java.math.BigDecimal value) {
		setFIELD_737(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value15(java.lang.Integer value) {
		setMS_UNUSED_ID_REF(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED value16(java.lang.String value) {
		setMS_UNUSED_NAME_REF(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X_UNUSED values(java.lang.Integer value1, java.lang.String value2, java.math.BigInteger value3, java.lang.Integer value4, java.lang.Integer value5, java.lang.Integer value6, java.lang.Integer value7, java.lang.Integer value8, java.lang.Integer value9, java.lang.Integer value10, java.lang.Integer value11, java.lang.Integer value12, java.lang.String value13, java.math.BigDecimal value14, java.lang.Integer value15, java.lang.String value16) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached X_UNUSED
	 */
	public X_UNUSED() {
		super(org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED);
	}

	/**
	 * Create a detached, initialised X_UNUSED
	 */
	public X_UNUSED(java.lang.Integer ID, java.lang.String NAME, java.math.BigInteger BIG_INTEGER, java.lang.Integer ID_REF, java.lang.Integer CLASS, java.lang.Integer FIELDS, java.lang.Integer CONFIGURATION, java.lang.Integer U_D_T, java.lang.Integer META_DATA, java.lang.Integer TYPE0, java.lang.Integer PRIMARY_KEY, java.lang.Integer PRIMARYKEY, java.lang.String NAME_REF, java.math.BigDecimal FIELD_737, java.lang.Integer MS_UNUSED_ID_REF, java.lang.String MS_UNUSED_NAME_REF) {
		super(org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED);

		setValue(0, ID);
		setValue(1, NAME);
		setValue(2, BIG_INTEGER);
		setValue(3, ID_REF);
		setValue(4, CLASS);
		setValue(5, FIELDS);
		setValue(6, CONFIGURATION);
		setValue(7, U_D_T);
		setValue(8, META_DATA);
		setValue(9, TYPE0);
		setValue(10, PRIMARY_KEY);
		setValue(11, PRIMARYKEY);
		setValue(12, NAME_REF);
		setValue(13, FIELD_737);
		setValue(14, MS_UNUSED_ID_REF);
		setValue(15, MS_UNUSED_NAME_REF);
	}
}
