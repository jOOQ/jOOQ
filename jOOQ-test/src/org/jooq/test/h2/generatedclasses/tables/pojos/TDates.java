/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TDates implements org.jooq.test.h2.generatedclasses.tables.interfaces.ITDates {

	private static final long serialVersionUID = -632478581;

	private java.lang.Integer  id;
	private java.sql.Date      d;
	private java.sql.Time      t;
	private java.sql.Timestamp ts;
	private java.lang.Integer  dInt;
	private java.lang.Long     tsBigint;

	@Override
	public java.lang.Integer getId() {
		return this.id;
	}

	@Override
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@Override
	public java.sql.Date getD() {
		return this.d;
	}

	@Override
	public void setD(java.sql.Date d) {
		this.d = d;
	}

	@Override
	public java.sql.Time getT() {
		return this.t;
	}

	@Override
	public void setT(java.sql.Time t) {
		this.t = t;
	}

	@Override
	public java.sql.Timestamp getTs() {
		return this.ts;
	}

	@Override
	public void setTs(java.sql.Timestamp ts) {
		this.ts = ts;
	}

	@Override
	public java.lang.Integer getDInt() {
		return this.dInt;
	}

	@Override
	public void setDInt(java.lang.Integer dInt) {
		this.dInt = dInt;
	}

	@Override
	public java.lang.Long getTsBigint() {
		return this.tsBigint;
	}

	@Override
	public void setTsBigint(java.lang.Long tsBigint) {
		this.tsBigint = tsBigint;
	}
}
