/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.udt.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class U_NUMBER_LONG_ARRAY extends org.jooq.impl.ArrayRecordImpl<java.lang.Long> implements java.lang.Cloneable {

	private static final long serialVersionUID = 2069713154;

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_NUMBER_LONG_ARRAY()} constructor instead
	 */
	@java.lang.Deprecated
	public U_NUMBER_LONG_ARRAY(org.jooq.Configuration configuration) {
		super(org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, "U_NUMBER_LONG_ARRAY", org.jooq.impl.SQLDataType.BIGINT, configuration);
	}

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_NUMBER_LONG_ARRAY()} constructor instead
	 */
	@java.lang.Deprecated
	public U_NUMBER_LONG_ARRAY(org.jooq.Configuration configuration, java.lang.Long... array) {
		this(configuration);
		set(array);
	}

	/**
	 * @deprecated - 3.4.0 - [#3126] - Use the {@link #U_NUMBER_LONG_ARRAY()} constructor instead
	 */
	@java.lang.Deprecated
	public U_NUMBER_LONG_ARRAY(org.jooq.Configuration configuration, java.util.List<? extends java.lang.Long> list) {
		this(configuration);
		setList(list);
	}

	/**
	 * Create a new <code>U_NUMBER_LONG_ARRAY</code> record
	 */
	public U_NUMBER_LONG_ARRAY() {
		super(org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, "U_NUMBER_LONG_ARRAY", org.jooq.impl.SQLDataType.BIGINT);
	}

	/**
	 * Create a new <code>U_NUMBER_LONG_ARRAY</code> record
	 */
	public U_NUMBER_LONG_ARRAY(java.lang.Long... array) {
		this();
		set(array);
	}

	/**
	 * Create a new <code>U_NUMBER_LONG_ARRAY</code> record
	 */
	public U_NUMBER_LONG_ARRAY(java.util.Collection<? extends java.lang.Long> collection) {
		this();
		set(collection);
	}
}
