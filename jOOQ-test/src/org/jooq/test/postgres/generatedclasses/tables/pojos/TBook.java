/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "t_book", schema = "public")
public class TBook implements java.io.Serializable {

	private static final long serialVersionUID = -1087405330;

	private final java.lang.Integer                                         id;
	private final java.lang.Integer                                         authorId;
	private final java.lang.Integer                                         coAuthorId;
	private final java.lang.Integer                                         detailsId;
	private final java.lang.String                                          title;
	private final java.lang.Integer                                         publishedIn;
	private final org.jooq.test.postgres.generatedclasses.enums.TLanguage   languageId;
	private final java.lang.String                                          contentText;
	private final byte[]                                                    contentPdf;
	private final org.jooq.test.postgres.generatedclasses.enums.UBookStatus status;

	public TBook(
		java.lang.Integer                                         id,
		java.lang.Integer                                         authorId,
		java.lang.Integer                                         coAuthorId,
		java.lang.Integer                                         detailsId,
		java.lang.String                                          title,
		java.lang.Integer                                         publishedIn,
		org.jooq.test.postgres.generatedclasses.enums.TLanguage   languageId,
		java.lang.String                                          contentText,
		byte[]                                                    contentPdf,
		org.jooq.test.postgres.generatedclasses.enums.UBookStatus status
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
		this.status = status;
	}

	@javax.persistence.Id
	@javax.persistence.Column(name = "id", unique = true, nullable = false, precision = 32)
	public java.lang.Integer getId() {
		return this.id;
	}

	@javax.persistence.Column(name = "author_id", nullable = false, precision = 32)
	public java.lang.Integer getAuthorId() {
		return this.authorId;
	}

	@javax.persistence.Column(name = "co_author_id", precision = 32)
	public java.lang.Integer getCoAuthorId() {
		return this.coAuthorId;
	}

	@javax.persistence.Column(name = "details_id", precision = 32)
	public java.lang.Integer getDetailsId() {
		return this.detailsId;
	}

	@javax.persistence.Column(name = "title", nullable = false, length = 400)
	public java.lang.String getTitle() {
		return this.title;
	}

	@javax.persistence.Column(name = "published_in", nullable = false, precision = 32)
	public java.lang.Integer getPublishedIn() {
		return this.publishedIn;
	}

	@javax.persistence.Column(name = "language_id", nullable = false, precision = 32)
	public org.jooq.test.postgres.generatedclasses.enums.TLanguage getLanguageId() {
		return this.languageId;
	}

	@javax.persistence.Column(name = "content_text")
	public java.lang.String getContentText() {
		return this.contentText;
	}

	@javax.persistence.Column(name = "content_pdf")
	public byte[] getContentPdf() {
		return this.contentPdf;
	}

	@javax.persistence.Column(name = "status")
	public org.jooq.test.postgres.generatedclasses.enums.UBookStatus getStatus() {
		return this.status;
	}
}
