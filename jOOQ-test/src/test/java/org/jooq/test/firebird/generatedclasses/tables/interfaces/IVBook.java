/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables.interfaces;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "V_BOOK")
public interface IVBook extends Serializable {

	/**
	 * Setter for <code>V_BOOK.ID</code>.
	 */
	public void setId(Integer value);

	/**
	 * Getter for <code>V_BOOK.ID</code>.
	 */
	@Column(name = "ID")
	public Integer getId();

	/**
	 * Setter for <code>V_BOOK.AUTHOR_ID</code>.
	 */
	public void setAuthorId(Integer value);

	/**
	 * Getter for <code>V_BOOK.AUTHOR_ID</code>.
	 */
	@Column(name = "AUTHOR_ID")
	public Integer getAuthorId();

	/**
	 * Setter for <code>V_BOOK.CO_AUTHOR_ID</code>.
	 */
	public void setCoAuthorId(Integer value);

	/**
	 * Getter for <code>V_BOOK.CO_AUTHOR_ID</code>.
	 */
	@Column(name = "CO_AUTHOR_ID")
	public Integer getCoAuthorId();

	/**
	 * Setter for <code>V_BOOK.DETAILS_ID</code>.
	 */
	public void setDetailsId(Integer value);

	/**
	 * Getter for <code>V_BOOK.DETAILS_ID</code>.
	 */
	@Column(name = "DETAILS_ID")
	public Integer getDetailsId();

	/**
	 * Setter for <code>V_BOOK.TITLE</code>.
	 */
	public void setTitle(String value);

	/**
	 * Getter for <code>V_BOOK.TITLE</code>.
	 */
	@Column(name = "TITLE", length = 400)
	@Size(max = 400)
	public String getTitle();

	/**
	 * Setter for <code>V_BOOK.PUBLISHED_IN</code>.
	 */
	public void setPublishedIn(Integer value);

	/**
	 * Getter for <code>V_BOOK.PUBLISHED_IN</code>.
	 */
	@Column(name = "PUBLISHED_IN")
	public Integer getPublishedIn();

	/**
	 * Setter for <code>V_BOOK.LANGUAGE_ID</code>.
	 */
	public void setLanguageId(Integer value);

	/**
	 * Getter for <code>V_BOOK.LANGUAGE_ID</code>.
	 */
	@Column(name = "LANGUAGE_ID")
	public Integer getLanguageId();

	/**
	 * Setter for <code>V_BOOK.CONTENT_TEXT</code>.
	 */
	public void setContentText(String value);

	/**
	 * Getter for <code>V_BOOK.CONTENT_TEXT</code>.
	 */
	@Column(name = "CONTENT_TEXT")
	public String getContentText();

	/**
	 * Setter for <code>V_BOOK.CONTENT_PDF</code>.
	 */
	public void setContentPdf(byte[] value);

	/**
	 * Getter for <code>V_BOOK.CONTENT_PDF</code>.
	 */
	@Column(name = "CONTENT_PDF")
	public byte[] getContentPdf();

	/**
	 * Setter for <code>V_BOOK.REC_VERSION</code>.
	 */
	public void setRecVersion(Integer value);

	/**
	 * Getter for <code>V_BOOK.REC_VERSION</code>.
	 */
	@Column(name = "REC_VERSION")
	public Integer getRecVersion();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IVBook
	 */
	public void from(org.jooq.test.firebird.generatedclasses.tables.interfaces.IVBook from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IVBook
	 */
	public <E extends org.jooq.test.firebird.generatedclasses.tables.interfaces.IVBook> E into(E into);
}
