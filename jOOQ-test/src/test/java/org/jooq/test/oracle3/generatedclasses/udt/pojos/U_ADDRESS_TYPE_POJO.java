/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.udt.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class U_ADDRESS_TYPE_POJO extends java.lang.ThreadDeath implements java.lang.Cloneable, java.io.Serializable {

	private static final long serialVersionUID = -165111121;

	private org.jooq.test.oracle3.generatedclasses.udt.pojos.U_STREET_TYPE_POJO STREET;
	private java.lang.String                                                    ZIP;
	private java.lang.String                                                    CITY;
	private java.lang.String                                                    COUNTRY;
	private java.sql.Date                                                       SINCE;
	private java.lang.Integer                                                   CODE;
	private byte[]                                                              F_1323;
	private java.lang.String                                                    F_1326;

	public U_ADDRESS_TYPE_POJO() {}

	public U_ADDRESS_TYPE_POJO(
		org.jooq.test.oracle3.generatedclasses.udt.pojos.U_STREET_TYPE_POJO STREET,
		java.lang.String                                                    ZIP,
		java.lang.String                                                    CITY,
		java.lang.String                                                    COUNTRY,
		java.sql.Date                                                       SINCE,
		java.lang.Integer                                                   CODE,
		byte[]                                                              F_1323,
		java.lang.String                                                    F_1326
	) {
		this.STREET = STREET;
		this.ZIP = ZIP;
		this.CITY = CITY;
		this.COUNTRY = COUNTRY;
		this.SINCE = SINCE;
		this.CODE = CODE;
		this.F_1323 = F_1323;
		this.F_1326 = F_1326;
	}

	public org.jooq.test.oracle3.generatedclasses.udt.pojos.U_STREET_TYPE_POJO getSTREET() {
		return this.STREET;
	}

	public void setSTREET(org.jooq.test.oracle3.generatedclasses.udt.pojos.U_STREET_TYPE_POJO STREET) {
		this.STREET = STREET;
	}

	public java.lang.String getZIP() {
		return this.ZIP;
	}

	public void setZIP(java.lang.String ZIP) {
		this.ZIP = ZIP;
	}

	public java.lang.String getCITY() {
		return this.CITY;
	}

	public void setCITY(java.lang.String CITY) {
		this.CITY = CITY;
	}

	public java.lang.String getCOUNTRY() {
		return this.COUNTRY;
	}

	public void setCOUNTRY(java.lang.String COUNTRY) {
		this.COUNTRY = COUNTRY;
	}

	public java.sql.Date getSINCE() {
		return this.SINCE;
	}

	public void setSINCE(java.sql.Date SINCE) {
		this.SINCE = SINCE;
	}

	public java.lang.Integer getCODE() {
		return this.CODE;
	}

	public void setCODE(java.lang.Integer CODE) {
		this.CODE = CODE;
	}

	public byte[] getF_1323() {
		return this.F_1323;
	}

	public void setF_1323(byte[] F_1323) {
		this.F_1323 = F_1323;
	}

	public java.lang.String getF_1326() {
		return this.F_1326;
	}

	public void setF_1326(java.lang.String F_1326) {
		this.F_1326 = F_1326;
	}
}
