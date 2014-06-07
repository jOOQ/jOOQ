/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITBook extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_BOOK.ID</code>.
	 */
	public void setId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.ID</code>.
	 */
	public java.lang.Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.AUTHOR_ID</code>.
	 */
	public void setAuthorId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.AUTHOR_ID</code>.
	 */
	public java.lang.Integer getAuthorId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>.
	 */
	public void setCoAuthorId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>.
	 */
	public java.lang.Integer getCoAuthorId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.DETAILS_ID</code>.
	 */
	public void setDetailsId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.DETAILS_ID</code>.
	 */
	public java.lang.Integer getDetailsId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.TITLE</code>.
	 */
	public void setTitle(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.TITLE</code>.
	 */
	public java.lang.String getTitle();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.PUBLISHED_IN</code>.
	 */
	public void setPublishedIn(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.PUBLISHED_IN</code>.
	 */
	public java.lang.Integer getPublishedIn();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.LANGUAGE_ID</code>.
	 */
	public void setLanguageId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.LANGUAGE_ID</code>.
	 */
	public java.lang.Integer getLanguageId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CONTENT_TEXT</code>.
	 */
	public void setContentText(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CONTENT_TEXT</code>.
	 */
	public java.lang.String getContentText();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CONTENT_PDF</code>.
	 */
	public void setContentPdf(byte[] value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CONTENT_PDF</code>.
	 */
	public byte[] getContentPdf();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.REC_VERSION</code>.
	 */
	public void setRecVersion(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.REC_VERSION</code>.
	 */
	public java.lang.Integer getRecVersion();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>.
	 */
	public void setRecTimestamp(java.sql.Timestamp value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>.
	 */
	public java.sql.Timestamp getRecTimestamp();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITBook
	 */
	public void from(org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITBook from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITBook
	 */
	public <E extends org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITBook> E into(E into);
}
