/**
 * This class is generated by jOOQ
 */
package org.jooq.test.mysql2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "t_dates", schema = "test2")
public class TDates implements java.io.Serializable {

	private static final long serialVersionUID = 1249290027;

	private java.lang.Integer  id;
	private java.sql.Date      d;
	private java.sql.Time      t;
	private java.sql.Timestamp ts;
	private java.lang.Integer  dInt;
	private java.lang.Long     tsBigint;
	private java.sql.Date      y2;
	private java.sql.Date      y4;

	@javax.persistence.Id
	@javax.persistence.Column(name = "id", unique = true, nullable = false, precision = 10)
	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "d")
	public java.sql.Date getD() {
		return this.d;
	}

	public void setD(java.sql.Date d) {
		this.d = d;
	}

	@javax.persistence.Column(name = "t")
	public java.sql.Time getT() {
		return this.t;
	}

	public void setT(java.sql.Time t) {
		this.t = t;
	}

	@javax.persistence.Column(name = "ts")
	public java.sql.Timestamp getTs() {
		return this.ts;
	}

	public void setTs(java.sql.Timestamp ts) {
		this.ts = ts;
	}

	@javax.persistence.Column(name = "d_int", precision = 10)
	public java.lang.Integer getDInt() {
		return this.dInt;
	}

	public void setDInt(java.lang.Integer dInt) {
		this.dInt = dInt;
	}

	@javax.persistence.Column(name = "ts_bigint", precision = 19)
	public java.lang.Long getTsBigint() {
		return this.tsBigint;
	}

	public void setTsBigint(java.lang.Long tsBigint) {
		this.tsBigint = tsBigint;
	}

	@javax.persistence.Column(name = "y2")
	public java.sql.Date getY2() {
		return this.y2;
	}

	public void setY2(java.sql.Date y2) {
		this.y2 = y2;
	}

	@javax.persistence.Column(name = "y4")
	public java.sql.Date getY4() {
		return this.y4;
	}

	public void setY4(java.sql.Date y4) {
		this.y4 = y4;
	}
}
