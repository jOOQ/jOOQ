/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.pojos;

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
public class X_UNUSED_POJO extends java.lang.ThreadDeath implements java.lang.Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 2015765409;

	private java.lang.Integer    ID;
	private java.lang.String     NAME;
	private java.math.BigInteger BIG_INTEGER;
	private java.lang.Integer    ID_REF;
	private java.lang.Integer    CLASS;
	private java.lang.Integer    FIELDS;
	private java.lang.Integer    CONFIGURATION;
	private java.lang.Integer    U_D_T;
	private java.lang.Integer    META_DATA;
	private java.lang.Integer    TYPE0;
	private java.lang.Integer    PRIMARY_KEY;
	private java.lang.Integer    PRIMARYKEY;
	private java.lang.String     NAME_REF;
	private java.math.BigDecimal FIELD_737;
	private java.lang.Integer    MS_UNUSED_ID_REF;
	private java.lang.String     MS_UNUSED_NAME_REF;

	public X_UNUSED_POJO() {}

	public X_UNUSED_POJO(
		java.lang.Integer    ID,
		java.lang.String     NAME,
		java.math.BigInteger BIG_INTEGER,
		java.lang.Integer    ID_REF,
		java.lang.Integer    CLASS,
		java.lang.Integer    FIELDS,
		java.lang.Integer    CONFIGURATION,
		java.lang.Integer    U_D_T,
		java.lang.Integer    META_DATA,
		java.lang.Integer    TYPE0,
		java.lang.Integer    PRIMARY_KEY,
		java.lang.Integer    PRIMARYKEY,
		java.lang.String     NAME_REF,
		java.math.BigDecimal FIELD_737,
		java.lang.Integer    MS_UNUSED_ID_REF,
		java.lang.String     MS_UNUSED_NAME_REF
	) {
		this.ID = ID;
		this.NAME = NAME;
		this.BIG_INTEGER = BIG_INTEGER;
		this.ID_REF = ID_REF;
		this.CLASS = CLASS;
		this.FIELDS = FIELDS;
		this.CONFIGURATION = CONFIGURATION;
		this.U_D_T = U_D_T;
		this.META_DATA = META_DATA;
		this.TYPE0 = TYPE0;
		this.PRIMARY_KEY = PRIMARY_KEY;
		this.PRIMARYKEY = PRIMARYKEY;
		this.NAME_REF = NAME_REF;
		this.FIELD_737 = FIELD_737;
		this.MS_UNUSED_ID_REF = MS_UNUSED_ID_REF;
		this.MS_UNUSED_NAME_REF = MS_UNUSED_NAME_REF;
	}

	@javax.persistence.Column(name = "ID", unique = true, nullable = false, precision = 7)
	public java.lang.Integer getID() {
		return this.ID;
	}

	public void setID(java.lang.Integer ID) {
		this.ID = ID;
	}

	@javax.persistence.Column(name = "NAME", nullable = false, length = 10)
	public java.lang.String getNAME() {
		return this.NAME;
	}

	public void setNAME(java.lang.String NAME) {
		this.NAME = NAME;
	}

	@javax.persistence.Column(name = "BIG_INTEGER", precision = 38)
	public java.math.BigInteger getBIG_INTEGER() {
		return this.BIG_INTEGER;
	}

	public void setBIG_INTEGER(java.math.BigInteger BIG_INTEGER) {
		this.BIG_INTEGER = BIG_INTEGER;
	}

	@javax.persistence.Column(name = "ID_REF", precision = 7)
	public java.lang.Integer getID_REF() {
		return this.ID_REF;
	}

	public void setID_REF(java.lang.Integer ID_REF) {
		this.ID_REF = ID_REF;
	}

	@javax.persistence.Column(name = "CLASS", precision = 7)
	public java.lang.Integer getCLASS() {
		return this.CLASS;
	}

	public void setCLASS(java.lang.Integer CLASS) {
		this.CLASS = CLASS;
	}

	@javax.persistence.Column(name = "FIELDS", precision = 7)
	public java.lang.Integer getFIELDS() {
		return this.FIELDS;
	}

	public void setFIELDS(java.lang.Integer FIELDS) {
		this.FIELDS = FIELDS;
	}

	@javax.persistence.Column(name = "CONFIGURATION", precision = 7)
	public java.lang.Integer getCONFIGURATION() {
		return this.CONFIGURATION;
	}

	public void setCONFIGURATION(java.lang.Integer CONFIGURATION) {
		this.CONFIGURATION = CONFIGURATION;
	}

	@javax.persistence.Column(name = "U_D_T", precision = 7)
	public java.lang.Integer getU_D_T() {
		return this.U_D_T;
	}

	public void setU_D_T(java.lang.Integer U_D_T) {
		this.U_D_T = U_D_T;
	}

	@javax.persistence.Column(name = "META_DATA", precision = 7)
	public java.lang.Integer getMETA_DATA() {
		return this.META_DATA;
	}

	public void setMETA_DATA(java.lang.Integer META_DATA) {
		this.META_DATA = META_DATA;
	}

	@javax.persistence.Column(name = "TYPE0", precision = 7)
	public java.lang.Integer getTYPE0() {
		return this.TYPE0;
	}

	public void setTYPE0(java.lang.Integer TYPE0) {
		this.TYPE0 = TYPE0;
	}

	@javax.persistence.Column(name = "PRIMARY_KEY", precision = 7)
	public java.lang.Integer getPRIMARY_KEY() {
		return this.PRIMARY_KEY;
	}

	public void setPRIMARY_KEY(java.lang.Integer PRIMARY_KEY) {
		this.PRIMARY_KEY = PRIMARY_KEY;
	}

	@javax.persistence.Column(name = "PRIMARYKEY", precision = 7)
	public java.lang.Integer getPRIMARYKEY() {
		return this.PRIMARYKEY;
	}

	public void setPRIMARYKEY(java.lang.Integer PRIMARYKEY) {
		this.PRIMARYKEY = PRIMARYKEY;
	}

	@javax.persistence.Column(name = "NAME_REF", length = 10)
	public java.lang.String getNAME_REF() {
		return this.NAME_REF;
	}

	public void setNAME_REF(java.lang.String NAME_REF) {
		this.NAME_REF = NAME_REF;
	}

	@javax.persistence.Column(name = "FIELD 737", precision = 25, scale = 2)
	public java.math.BigDecimal getFIELD_737() {
		return this.FIELD_737;
	}

	public void setFIELD_737(java.math.BigDecimal FIELD_737) {
		this.FIELD_737 = FIELD_737;
	}

	@javax.persistence.Column(name = "MS_UNUSED_ID_REF", precision = 7)
	public java.lang.Integer getMS_UNUSED_ID_REF() {
		return this.MS_UNUSED_ID_REF;
	}

	public void setMS_UNUSED_ID_REF(java.lang.Integer MS_UNUSED_ID_REF) {
		this.MS_UNUSED_ID_REF = MS_UNUSED_ID_REF;
	}

	@javax.persistence.Column(name = "MS_UNUSED_NAME_REF", length = 10)
	public java.lang.String getMS_UNUSED_NAME_REF() {
		return this.MS_UNUSED_NAME_REF;
	}

	public void setMS_UNUSED_NAME_REF(java.lang.String MS_UNUSED_NAME_REF) {
		this.MS_UNUSED_NAME_REF = MS_UNUSED_NAME_REF;
	}
}
