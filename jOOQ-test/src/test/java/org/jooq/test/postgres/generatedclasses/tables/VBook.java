/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VBook extends org.jooq.impl.TableImpl<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord> {

	private static final long serialVersionUID = -607744706;

	/**
	 * The reference instance of <code>public.v_book</code>
	 */
	public static final org.jooq.test.postgres.generatedclasses.tables.VBook V_BOOK = new org.jooq.test.postgres.generatedclasses.tables.VBook();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord> getRecordType() {
		return org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord.class;
	}

	/**
	 * The column <code>public.v_book.id</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.author_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> AUTHOR_ID = createField("author_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.co_author_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> CO_AUTHOR_ID = createField("co_author_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.details_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> DETAILS_ID = createField("details_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.title</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(400), this, "");

	/**
	 * The column <code>public.v_book.published_in</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> PUBLISHED_IN = createField("published_in", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.language_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.Integer> LANGUAGE_ID = createField("language_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>public.v_book.content_text</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, java.lang.String> CONTENT_TEXT = createField("content_text", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.v_book.content_pdf</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, byte[]> CONTENT_PDF = createField("content_pdf", org.jooq.impl.SQLDataType.BLOB, this, "");

	/**
	 * The column <code>public.v_book.status</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord, org.jooq.test.postgres.generatedclasses.enums.UBookStatus> STATUS = createField("status", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(org.jooq.test.postgres.generatedclasses.enums.UBookStatus.class), this, "");

	/**
	 * Create a <code>public.v_book</code> table reference
	 */
	public VBook() {
		this("v_book", null);
	}

	/**
	 * Create an aliased <code>public.v_book</code> table reference
	 */
	public VBook(java.lang.String alias) {
		this(alias, org.jooq.test.postgres.generatedclasses.tables.VBook.V_BOOK);
	}

	private VBook(java.lang.String alias, org.jooq.Table<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord> aliased) {
		this(alias, aliased, null);
	}

	private VBook(java.lang.String alias, org.jooq.Table<org.jooq.test.postgres.generatedclasses.tables.records.VBookRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.postgres.generatedclasses.Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.postgres.generatedclasses.tables.VBook as(java.lang.String alias) {
		return new org.jooq.test.postgres.generatedclasses.tables.VBook(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.postgres.generatedclasses.tables.VBook rename(java.lang.String name) {
		return new org.jooq.test.postgres.generatedclasses.tables.VBook(name, null);
	}
}
