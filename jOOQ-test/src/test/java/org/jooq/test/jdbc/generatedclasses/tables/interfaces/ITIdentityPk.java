/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITIdentityPk extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_IDENTITY_PK.ID</code>.
	 */
	public void setId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_IDENTITY_PK.ID</code>.
	 */
	public java.lang.Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_IDENTITY_PK.VAL</code>.
	 */
	public void setVal(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_IDENTITY_PK.VAL</code>.
	 */
	public java.lang.Integer getVal();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITIdentityPk
	 */
	public void from(org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITIdentityPk from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITIdentityPk
	 */
	public <E extends org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITIdentityPk> E into(E into);
}
