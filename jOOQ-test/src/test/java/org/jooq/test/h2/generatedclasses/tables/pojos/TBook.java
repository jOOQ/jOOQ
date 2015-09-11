/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import java.sql.Timestamp;

import org.jooq.test.h2.generatedclasses.tables.interfaces.ITBook;


/**
 * This is a POJO for table T_BOOK.
 * <p>
 * An entity holding books
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBook implements ITBook {

	private static final long serialVersionUID = -819363860;

	private Integer   id;
	private Integer   authorId;
	private Integer   coAuthorId;
	private Integer   detailsId;
	private String    title;
	private Integer   publishedIn;
	private Integer   languageId;
	private String    contentText;
	private byte[]    contentPdf;
	private Integer   recVersion;
	private Timestamp recTimestamp;

	public TBook() {}

	public TBook(TBook value) {
		this.id = value.id;
		this.authorId = value.authorId;
		this.coAuthorId = value.coAuthorId;
		this.detailsId = value.detailsId;
		this.title = value.title;
		this.publishedIn = value.publishedIn;
		this.languageId = value.languageId;
		this.contentText = value.contentText;
		this.contentPdf = value.contentPdf;
		this.recVersion = value.recVersion;
		this.recTimestamp = value.recTimestamp;
	}

	public TBook(
		Integer   id,
		Integer   authorId,
		Integer   coAuthorId,
		Integer   detailsId,
		String    title,
		Integer   publishedIn,
		Integer   languageId,
		String    contentText,
		byte[]    contentPdf,
		Integer   recVersion,
		Timestamp recTimestamp
	) {
		this.id = id;
		this.authorId = authorId;
		this.coAuthorId = coAuthorId;
		this.detailsId = detailsId;
		this.title = title;
		this.publishedIn = publishedIn;
		this.languageId = languageId;
		this.contentText = contentText;
		this.contentPdf = contentPdf;
		this.recVersion = recVersion;
		this.recTimestamp = recTimestamp;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public TBook setId(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public Integer getAuthorId() {
		return this.authorId;
	}

	@Override
	public TBook setAuthorId(Integer authorId) {
		this.authorId = authorId;
		return this;
	}

	@Override
	public Integer getCoAuthorId() {
		return this.coAuthorId;
	}

	@Override
	public TBook setCoAuthorId(Integer coAuthorId) {
		this.coAuthorId = coAuthorId;
		return this;
	}

	@Override
	public Integer getDetailsId() {
		return this.detailsId;
	}

	@Override
	public TBook setDetailsId(Integer detailsId) {
		this.detailsId = detailsId;
		return this;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public TBook setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public Integer getPublishedIn() {
		return this.publishedIn;
	}

	@Override
	public TBook setPublishedIn(Integer publishedIn) {
		this.publishedIn = publishedIn;
		return this;
	}

	@Override
	public Integer getLanguageId() {
		return this.languageId;
	}

	@Override
	public TBook setLanguageId(Integer languageId) {
		this.languageId = languageId;
		return this;
	}

	@Override
	public String getContentText() {
		return this.contentText;
	}

	@Override
	public TBook setContentText(String contentText) {
		this.contentText = contentText;
		return this;
	}

	@Override
	public byte[] getContentPdf() {
		return this.contentPdf;
	}

	@Override
	public TBook setContentPdf(byte[] contentPdf) {
		this.contentPdf = contentPdf;
		return this;
	}

	@Override
	public Integer getRecVersion() {
		return this.recVersion;
	}

	@Override
	public TBook setRecVersion(Integer recVersion) {
		this.recVersion = recVersion;
		return this;
	}

	@Override
	public Timestamp getRecTimestamp() {
		return this.recTimestamp;
	}

	@Override
	public TBook setRecTimestamp(Timestamp recTimestamp) {
		this.recTimestamp = recTimestamp;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TBook (");

		sb.append(id);
		sb.append(", ").append(authorId);
		sb.append(", ").append(coAuthorId);
		sb.append(", ").append(detailsId);
		sb.append(", ").append(title);
		sb.append(", ").append(publishedIn);
		sb.append(", ").append(languageId);
		sb.append(", ").append(contentText);
		sb.append(", ").append("[binary...]");
		sb.append(", ").append(recVersion);
		sb.append(", ").append(recTimestamp);

		sb.append(")");
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITBook from) {
		setId(from.getId());
		setAuthorId(from.getAuthorId());
		setCoAuthorId(from.getCoAuthorId());
		setDetailsId(from.getDetailsId());
		setTitle(from.getTitle());
		setPublishedIn(from.getPublishedIn());
		setLanguageId(from.getLanguageId());
		setContentText(from.getContentText());
		setContentPdf(from.getContentPdf());
		setRecVersion(from.getRecVersion());
		setRecTimestamp(from.getRecTimestamp());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITBook> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
