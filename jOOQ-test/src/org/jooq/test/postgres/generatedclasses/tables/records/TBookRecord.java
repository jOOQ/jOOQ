/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings("all")
public class TBookRecord extends org.jooq.impl.UpdatableRecordImpl<org.jooq.test.postgres.generatedclasses.tables.records.TBookRecord> implements org.jooq.Record10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, byte[], org.jooq.test.postgres.generatedclasses.enums.UBookStatus> {

	private static final long serialVersionUID = 811756690;

	/**
	 * Setter for <code>public.t_book.id</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.ID, value);
	}

	/**
	 * Getter for <code>public.t_book.id</code>. 
	 */
	public java.lang.Integer getId() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.ID);
	}

	/**
	 * Setter for <code>public.t_book.author_id</code>. 
	 */
	public void setAuthorId(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.AUTHOR_ID, value);
	}

	/**
	 * Getter for <code>public.t_book.author_id</code>. 
	 */
	public java.lang.Integer getAuthorId() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.AUTHOR_ID);
	}

	/**
	 * Setter for <code>public.t_book.co_author_id</code>. 
	 */
	public void setCoAuthorId(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CO_AUTHOR_ID, value);
	}

	/**
	 * Getter for <code>public.t_book.co_author_id</code>. 
	 */
	public java.lang.Integer getCoAuthorId() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CO_AUTHOR_ID);
	}

	/**
	 * Setter for <code>public.t_book.details_id</code>. 
	 */
	public void setDetailsId(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.DETAILS_ID, value);
	}

	/**
	 * Getter for <code>public.t_book.details_id</code>. 
	 */
	public java.lang.Integer getDetailsId() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.DETAILS_ID);
	}

	/**
	 * Setter for <code>public.t_book.title</code>. 
	 */
	public void setTitle(java.lang.String value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.TITLE, value);
	}

	/**
	 * Getter for <code>public.t_book.title</code>. 
	 */
	public java.lang.String getTitle() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.TITLE);
	}

	/**
	 * Setter for <code>public.t_book.published_in</code>. 
	 */
	public void setPublishedIn(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.PUBLISHED_IN, value);
	}

	/**
	 * Getter for <code>public.t_book.published_in</code>. 
	 */
	public java.lang.Integer getPublishedIn() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.PUBLISHED_IN);
	}

	/**
	 * Setter for <code>public.t_book.language_id</code>. 
	 */
	public void setLanguageId(java.lang.Integer value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.LANGUAGE_ID, value);
	}

	/**
	 * Getter for <code>public.t_book.language_id</code>. 
	 */
	public java.lang.Integer getLanguageId() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.LANGUAGE_ID);
	}

	/**
	 * Setter for <code>public.t_book.content_text</code>. 
	 */
	public void setContentText(java.lang.String value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_TEXT, value);
	}

	/**
	 * Getter for <code>public.t_book.content_text</code>. 
	 */
	public java.lang.String getContentText() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_TEXT);
	}

	/**
	 * Setter for <code>public.t_book.content_pdf</code>. 
	 */
	public void setContentPdf(byte[] value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_PDF, value);
	}

	/**
	 * Getter for <code>public.t_book.content_pdf</code>. 
	 */
	public byte[] getContentPdf() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_PDF);
	}

	/**
	 * Setter for <code>public.t_book.status</code>. 
	 */
	public void setStatus(org.jooq.test.postgres.generatedclasses.enums.UBookStatus value) {
		setValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.STATUS, value);
	}

	/**
	 * Getter for <code>public.t_book.status</code>. 
	 */
	public org.jooq.test.postgres.generatedclasses.enums.UBookStatus getStatus() {
		return getValue(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.STATUS);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record10 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, byte[], org.jooq.test.postgres.generatedclasses.enums.UBookStatus> fieldsRow() {
		return org.jooq.impl.Factory.row(field1(), field2(), field3(), field4(), field5(), field6(), field7(), field8(), field9(), field10());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, byte[], org.jooq.test.postgres.generatedclasses.enums.UBookStatus> valuesRow() {
		return org.jooq.impl.Factory.row(value1(), value2(), value3(), value4(), value5(), value6(), value7(), value8(), value9(), value10());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.AUTHOR_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CO_AUTHOR_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.DETAILS_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.PUBLISHED_IN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.LANGUAGE_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_TEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<byte[]> field9() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.CONTENT_PDF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<org.jooq.test.postgres.generatedclasses.enums.UBookStatus> field10() {
		return org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK.STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getAuthorId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getCoAuthorId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getDetailsId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getPublishedIn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getLanguageId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getContentText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] value9() {
		return getContentPdf();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.postgres.generatedclasses.enums.UBookStatus value10() {
		return getStatus();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TBookRecord
	 */
	public TBookRecord() {
		super(org.jooq.test.postgres.generatedclasses.tables.TBook.T_BOOK);
	}
}
