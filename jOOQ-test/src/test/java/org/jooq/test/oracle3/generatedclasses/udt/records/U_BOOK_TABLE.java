/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.udt.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class U_BOOK_TABLE extends org.jooq.impl.ArrayRecordImpl<org.jooq.test.oracle3.generatedclasses.udt.records.U_BOOK_TYPE> implements java.lang.Cloneable {

	private static final long serialVersionUID = 524568170;

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_BOOK_TABLE()} constructor instead
	 */
	@java.lang.Deprecated
	public U_BOOK_TABLE(org.jooq.Configuration configuration) {
		super(org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, "U_BOOK_TABLE", org.jooq.test.oracle3.generatedclasses.udt.U_BOOK_TYPE.U_BOOK_TYPE.getDataType(), configuration);
	}

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_BOOK_TABLE()} constructor instead
	 */
	@java.lang.Deprecated
	public U_BOOK_TABLE(org.jooq.Configuration configuration, org.jooq.test.oracle3.generatedclasses.udt.records.U_BOOK_TYPE... array) {
		this(configuration);
		set(array);
	}

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_BOOK_TABLE()} constructor instead
	 */
	@java.lang.Deprecated
	public U_BOOK_TABLE(org.jooq.Configuration configuration, java.util.List<? extends org.jooq.test.oracle3.generatedclasses.udt.records.U_BOOK_TYPE> list) {
		this(configuration);
		setList(list);
	}

	/**
	 * Create a new <code>U_BOOK_TABLE</code> record
	 */
	public U_BOOK_TABLE() {
		super(org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, "U_BOOK_TABLE", org.jooq.test.oracle3.generatedclasses.udt.U_BOOK_TYPE.U_BOOK_TYPE.getDataType());
	}

	/**
	 * Create a new <code>U_BOOK_TABLE</code> record
	 */
	public U_BOOK_TABLE(org.jooq.test.oracle3.generatedclasses.udt.records.U_BOOK_TYPE... array) {
		this();
		set(array);
	}

	/**
	 * Create a new <code>U_BOOK_TABLE</code> record
	 */
	public U_BOOK_TABLE(java.util.Collection<? extends org.jooq.test.oracle3.generatedclasses.udt.records.U_BOOK_TYPE> collection) {
		this();
		set(collection);
	}
}
