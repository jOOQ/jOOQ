/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "T_785")
public class T_785_POJO extends java.lang.ThreadDeath implements java.lang.Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 541030264;

	private java.lang.Integer ID;
	private java.lang.String  NAME;
	private java.lang.String  VALUE;

	public T_785_POJO() {}

	public T_785_POJO(
		java.lang.Integer ID,
		java.lang.String  NAME,
		java.lang.String  VALUE
	) {
		this.ID = ID;
		this.NAME = NAME;
		this.VALUE = VALUE;
	}

	@javax.persistence.Column(name = "ID", precision = 7)
	public java.lang.Integer getID() {
		return this.ID;
	}

	public void setID(java.lang.Integer ID) {
		this.ID = ID;
	}

	@javax.persistence.Column(name = "NAME", length = 50)
	public java.lang.String getNAME() {
		return this.NAME;
	}

	public void setNAME(java.lang.String NAME) {
		this.NAME = NAME;
	}

	@javax.persistence.Column(name = "VALUE", length = 50)
	public java.lang.String getVALUE() {
		return this.VALUE;
	}

	public void setVALUE(java.lang.String VALUE) {
		this.VALUE = VALUE;
	}
}
