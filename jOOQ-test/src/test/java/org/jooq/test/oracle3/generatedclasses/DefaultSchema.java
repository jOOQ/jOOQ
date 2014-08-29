/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends org.jooq.impl.SchemaImpl implements java.lang.Cloneable {

	private static final long serialVersionUID = 1092285693;

	/**
	 * The singleton instance of <code></code>
	 */
	public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

	/**
	 * No further instances allowed
	 */
	private DefaultSchema() {
		super("");
	}

	@Override
	public final java.util.List<org.jooq.Sequence<?>> getSequences() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final java.util.List<org.jooq.Sequence<?>> getSequences0() {
		return java.util.Arrays.<org.jooq.Sequence<?>>asList(
			org.jooq.test.oracle3.generatedclasses.Sequences.S_AUTHOR_ID,
			org.jooq.test.oracle3.generatedclasses.Sequences.S_961_BIG_INTEGER,
			org.jooq.test.oracle3.generatedclasses.Sequences.S_961_BYTE,
			org.jooq.test.oracle3.generatedclasses.Sequences.S_961_INT,
			org.jooq.test.oracle3.generatedclasses.Sequences.S_961_LONG,
			org.jooq.test.oracle3.generatedclasses.Sequences.S_961_SHORT);
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			org.jooq.test.oracle3.generatedclasses.tables.M_LIBRARY.M_LIBRARY,
			org.jooq.test.oracle3.generatedclasses.tables.T_ARRAYS.T_ARRAYS,
			org.jooq.test.oracle3.generatedclasses.tables.T_AUTHOR.T_AUTHOR,
			org.jooq.test.oracle3.generatedclasses.tables.T_BOOK.T_BOOK,
			org.jooq.test.oracle3.generatedclasses.tables.T_BOOK_STORE.T_BOOK_STORE,
			org.jooq.test.oracle3.generatedclasses.tables.T_BOOK_TO_BOOK_STORE.T_BOOK_TO_BOOK_STORE,
			org.jooq.test.oracle3.generatedclasses.tables.T_BOOLEANS.T_BOOLEANS,
			org.jooq.test.oracle3.generatedclasses.tables.T_DATES.T_DATES,
			org.jooq.test.oracle3.generatedclasses.tables.T_DIRECTORY.T_DIRECTORY,
			org.jooq.test.oracle3.generatedclasses.tables.T_EXOTIC_TYPES.T_EXOTIC_TYPES,
			org.jooq.test.oracle3.generatedclasses.tables.T_LANGUAGE.T_LANGUAGE,
			org.jooq.test.oracle3.generatedclasses.tables.T_TEMP.T_TEMP,
			org.jooq.test.oracle3.generatedclasses.tables.T_TRIGGERS.T_TRIGGERS,
			org.jooq.test.oracle3.generatedclasses.tables.T_UNSIGNED.T_UNSIGNED,
			org.jooq.test.oracle3.generatedclasses.tables.T_2845_CASE_sensitivity.T_2845_CASE_sensitivity,
			org.jooq.test.oracle3.generatedclasses.tables.T_639_NUMBERS_TABLE.T_639_NUMBERS_TABLE,
			org.jooq.test.oracle3.generatedclasses.tables.T_725_LOB_TEST.T_725_LOB_TEST,
			org.jooq.test.oracle3.generatedclasses.tables.T_785.T_785,
			org.jooq.test.oracle3.generatedclasses.tables.V_AUTHOR.V_AUTHOR,
			org.jooq.test.oracle3.generatedclasses.tables.V_BOOK.V_BOOK,
			org.jooq.test.oracle3.generatedclasses.tables.V_INCOMPLETE.V_INCOMPLETE,
			org.jooq.test.oracle3.generatedclasses.tables.V_LIBRARY.V_LIBRARY,
			org.jooq.test.oracle3.generatedclasses.tables.X_TEST_CASE_2025.X_TEST_CASE_2025,
			org.jooq.test.oracle3.generatedclasses.tables.X_TEST_CASE_64_69.X_TEST_CASE_64_69,
			org.jooq.test.oracle3.generatedclasses.tables.X_TEST_CASE_71.X_TEST_CASE_71,
			org.jooq.test.oracle3.generatedclasses.tables.X_TEST_CASE_85.X_TEST_CASE_85,
			org.jooq.test.oracle3.generatedclasses.tables.X_UNUSED.X_UNUSED);
	}

	@Override
	public final java.util.List<org.jooq.UDT<?>> getUDTs() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getUDTs0());
		return result;
	}

	private final java.util.List<org.jooq.UDT<?>> getUDTs0() {
		return java.util.Arrays.<org.jooq.UDT<?>>asList(
			org.jooq.test.oracle3.generatedclasses.udt.O_INVALID_TYPE.O_INVALID_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_ADDRESS_TYPE.U_ADDRESS_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_AUTHOR_TYPE.U_AUTHOR_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_BOOK_TYPE.U_BOOK_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_INVALID_TABLE.U_INVALID_TABLE,
			org.jooq.test.oracle3.generatedclasses.udt.U_INVALID_TYPE.U_INVALID_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_NESTED_1.U_NESTED_1,
			org.jooq.test.oracle3.generatedclasses.udt.U_NESTED_3.U_NESTED_3,
			org.jooq.test.oracle3.generatedclasses.udt.U_STREET_TYPE.U_STREET_TYPE,
			org.jooq.test.oracle3.generatedclasses.udt.U_3005.U_3005);
	}
}
