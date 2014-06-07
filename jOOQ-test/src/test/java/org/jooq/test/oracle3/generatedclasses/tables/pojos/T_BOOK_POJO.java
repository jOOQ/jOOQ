/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 *
 * An entity holding books
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "T_BOOK")
public class T_BOOK_POJO extends java.lang.Object implements java.io.Serializable {

	private static final long serialVersionUID = -1264214964;

	private java.lang.Integer    ID;
	private java.lang.Integer    AUTHOR_ID;
	private java.lang.Integer    CO_AUTHOR_ID;
	private java.lang.Integer    DETAILS_ID;
	private java.lang.String     TITLE;
	private java.lang.Integer    PUBLISHED_IN;
	private java.lang.Integer    LANGUAGE_ID;
	private java.lang.String     CONTENT_TEXT;
	private byte[]               CONTENT_PDF;
	private java.math.BigDecimal REC_VERSION;
	private java.sql.Timestamp   REC_TIMESTAMP;

	public T_BOOK_POJO() {}

	public T_BOOK_POJO(
		java.lang.Integer    ID,
		java.lang.Integer    AUTHOR_ID,
		java.lang.Integer    CO_AUTHOR_ID,
		java.lang.Integer    DETAILS_ID,
		java.lang.String     TITLE,
		java.lang.Integer    PUBLISHED_IN,
		java.lang.Integer    LANGUAGE_ID,
		java.lang.String     CONTENT_TEXT,
		byte[]               CONTENT_PDF,
		java.math.BigDecimal REC_VERSION,
		java.sql.Timestamp   REC_TIMESTAMP
	) {
		this.ID = ID;
		this.AUTHOR_ID = AUTHOR_ID;
		this.CO_AUTHOR_ID = CO_AUTHOR_ID;
		this.DETAILS_ID = DETAILS_ID;
		this.TITLE = TITLE;
		this.PUBLISHED_IN = PUBLISHED_IN;
		this.LANGUAGE_ID = LANGUAGE_ID;
		this.CONTENT_TEXT = CONTENT_TEXT;
		this.CONTENT_PDF = CONTENT_PDF;
		this.REC_VERSION = REC_VERSION;
		this.REC_TIMESTAMP = REC_TIMESTAMP;
	}

	@javax.persistence.Id
	@javax.persistence.Column(name = "ID", unique = true, nullable = false, precision = 7)
	public java.lang.Integer getID() {
		return this.ID;
	}

	public void setID(java.lang.Integer ID) {
		this.ID = ID;
	}

	@javax.persistence.Column(name = "AUTHOR_ID", nullable = false, precision = 7)
	public java.lang.Integer getAUTHOR_ID() {
		return this.AUTHOR_ID;
	}

	public void setAUTHOR_ID(java.lang.Integer AUTHOR_ID) {
		this.AUTHOR_ID = AUTHOR_ID;
	}

	@javax.persistence.Column(name = "CO_AUTHOR_ID", precision = 7)
	public java.lang.Integer getCO_AUTHOR_ID() {
		return this.CO_AUTHOR_ID;
	}

	public void setCO_AUTHOR_ID(java.lang.Integer CO_AUTHOR_ID) {
		this.CO_AUTHOR_ID = CO_AUTHOR_ID;
	}

	@javax.persistence.Column(name = "DETAILS_ID", precision = 7)
	public java.lang.Integer getDETAILS_ID() {
		return this.DETAILS_ID;
	}

	public void setDETAILS_ID(java.lang.Integer DETAILS_ID) {
		this.DETAILS_ID = DETAILS_ID;
	}

	@javax.persistence.Column(name = "TITLE", nullable = false, length = 400)
	public java.lang.String getTITLE() {
		return this.TITLE;
	}

	public void setTITLE(java.lang.String TITLE) {
		this.TITLE = TITLE;
	}

	@javax.persistence.Column(name = "PUBLISHED_IN", nullable = false, precision = 7)
	public java.lang.Integer getPUBLISHED_IN() {
		return this.PUBLISHED_IN;
	}

	public void setPUBLISHED_IN(java.lang.Integer PUBLISHED_IN) {
		this.PUBLISHED_IN = PUBLISHED_IN;
	}

	@javax.persistence.Column(name = "LANGUAGE_ID", nullable = false, precision = 7)
	public java.lang.Integer getLANGUAGE_ID() {
		return this.LANGUAGE_ID;
	}

	public void setLANGUAGE_ID(java.lang.Integer LANGUAGE_ID) {
		this.LANGUAGE_ID = LANGUAGE_ID;
	}

	@javax.persistence.Column(name = "CONTENT_TEXT")
	public java.lang.String getCONTENT_TEXT() {
		return this.CONTENT_TEXT;
	}

	public void setCONTENT_TEXT(java.lang.String CONTENT_TEXT) {
		this.CONTENT_TEXT = CONTENT_TEXT;
	}

	@javax.persistence.Column(name = "CONTENT_PDF")
	public byte[] getCONTENT_PDF() {
		return this.CONTENT_PDF;
	}

	public void setCONTENT_PDF(byte[] CONTENT_PDF) {
		this.CONTENT_PDF = CONTENT_PDF;
	}

	@javax.persistence.Column(name = "REC_VERSION", length = 22)
	public java.math.BigDecimal getREC_VERSION() {
		return this.REC_VERSION;
	}

	public void setREC_VERSION(java.math.BigDecimal REC_VERSION) {
		this.REC_VERSION = REC_VERSION;
	}

	@javax.persistence.Column(name = "REC_TIMESTAMP", length = 11)
	public java.sql.Timestamp getREC_TIMESTAMP() {
		return this.REC_TIMESTAMP;
	}

	public void setREC_TIMESTAMP(java.sql.Timestamp REC_TIMESTAMP) {
		this.REC_TIMESTAMP = REC_TIMESTAMP;
	}
}
