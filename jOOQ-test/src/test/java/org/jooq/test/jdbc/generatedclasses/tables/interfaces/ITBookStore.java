/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITBookStore extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_BOOK_STORE.NAME</code>.
	 */
	public void setName(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK_STORE.NAME</code>.
	 */
	public java.lang.String getName();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITBookStore
	 */
	public void from(org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITBookStore from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITBookStore
	 */
	public <E extends org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITBookStore> E into(E into);
}
