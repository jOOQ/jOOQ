/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the <code>PUBLIC</code> 
 * schema
 */
@java.lang.SuppressWarnings("all")
public final class ForeignKeys {

	public static final class TBook extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.TBookRecord, org.jooq.test.h2.generatedclasses.tables.records.TAuthorRecord> FK_T_BOOK_AUTHOR_ID = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.TAuthor.PK_T_AUTHOR, org.jooq.test.h2.generatedclasses.tables.TBook.T_BOOK, org.jooq.test.h2.generatedclasses.tables.TBook.AUTHOR_ID);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.TBookRecord, org.jooq.test.h2.generatedclasses.tables.records.TAuthorRecord> FK_T_BOOK_CO_AUTHOR_ID = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.TAuthor.PK_T_AUTHOR, org.jooq.test.h2.generatedclasses.tables.TBook.T_BOOK, org.jooq.test.h2.generatedclasses.tables.TBook.CO_AUTHOR_ID);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.TBookRecord, org.jooq.test.h2.generatedclasses.tables.records.TLanguageRecord> FK_T_BOOK_LANGUAGE_ID = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.TLanguage.PK_T_LANGUAGE, org.jooq.test.h2.generatedclasses.tables.TBook.T_BOOK, org.jooq.test.h2.generatedclasses.tables.TBook.LANGUAGE_ID);
	}

	public static final class TBookToBookStore extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.TBookToBookStoreRecord, org.jooq.test.h2.generatedclasses.tables.records.TBookStoreRecord> FK_B2BS_BS_NAME = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.TBookStore.UK_T_BOOK_STORE_NAME, org.jooq.test.h2.generatedclasses.tables.TBookToBookStore.T_BOOK_TO_BOOK_STORE, org.jooq.test.h2.generatedclasses.tables.TBookToBookStore.BOOK_STORE_NAME);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.TBookToBookStoreRecord, org.jooq.test.h2.generatedclasses.tables.records.TBookRecord> FK_B2BS_B_ID = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.TBook.PK_T_BOOK, org.jooq.test.h2.generatedclasses.tables.TBookToBookStore.T_BOOK_TO_BOOK_STORE, org.jooq.test.h2.generatedclasses.tables.TBookToBookStore.BOOK_ID);
	}

	public static final class XUnused extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord, org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord> FK_X_UNUSED_SELF = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XUnused.PK_X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XUnused.X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XUnused.ID_REF, org.jooq.test.h2.generatedclasses.tables.XUnused.ID_REF, org.jooq.test.h2.generatedclasses.tables.XUnused.NAME_REF, org.jooq.test.h2.generatedclasses.tables.XUnused.NAME_REF);
	}

	public static final class XTestCase_64_69 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_64_69Record, org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord> FK_X_TEST_CASE_64_69A = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XUnused.PK_X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.X_TEST_CASE_64_69, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.UNUSED_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.UNUSED_ID);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_64_69Record, org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord> FK_X_TEST_CASE_64_69B = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XUnused.PK_X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.X_TEST_CASE_64_69, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.UNUSED_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_64_69.UNUSED_ID);
	}

	public static final class XTestCase_71 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_71Record, org.jooq.test.h2.generatedclasses.tables.records.XTestCase_64_69Record> FK_X_TEST_CASE_71 = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XTestCase_64_69.PK_X_TEST_CASE_64_69, org.jooq.test.h2.generatedclasses.tables.XTestCase_71.X_TEST_CASE_71, org.jooq.test.h2.generatedclasses.tables.XTestCase_71.TEST_CASE_64_69_ID);
	}

	public static final class XTestCase_85 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_85Record, org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord> FK_X_TEST_CASE_85 = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XUnused.PK_X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XTestCase_85.X_TEST_CASE_85, org.jooq.test.h2.generatedclasses.tables.XTestCase_85.X_UNUSED_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_85.X_UNUSED_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_85.X_UNUSED_NAME, org.jooq.test.h2.generatedclasses.tables.XTestCase_85.X_UNUSED_NAME);
	}

	public static final class XTestCase_2025 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_2025Record, org.jooq.test.h2.generatedclasses.tables.records.XTestCase_85Record> FK_X_TEST_CASE_2025_1 = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XTestCase_85.PK_X_TEST_CASE_85, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.X_TEST_CASE_2025, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_ID);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_2025Record, org.jooq.test.h2.generatedclasses.tables.records.XTestCase_71Record> FK_X_TEST_CASE_2025_2 = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XTestCase_71.PK_X_TEST_CASE_71, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.X_TEST_CASE_2025, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_ID);
		public static final org.jooq.ForeignKey<org.jooq.test.h2.generatedclasses.tables.records.XTestCase_2025Record, org.jooq.test.h2.generatedclasses.tables.records.XUnusedRecord> FK_X_TEST_CASE_2025_3 = createForeignKey(org.jooq.test.h2.generatedclasses.UniqueKeys.XUnused.PK_X_UNUSED, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.X_TEST_CASE_2025, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_ID, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_NAME, org.jooq.test.h2.generatedclasses.tables.XTestCase_2025.REF_NAME);
	}

	/**
	 * No further instances allowed
	 */
	private ForeignKeys() {}
}
