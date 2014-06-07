/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.routines;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class P_ARRAYS1 extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.lang.Cloneable {

	private static final long serialVersionUID = 1484194964;

	/**
	 * The parameter <code>P_ARRAYS1.IN_ARRAY</code>.
	 */
	public static final org.jooq.Parameter<org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY> IN_ARRAY = createParameter("IN_ARRAY", org.jooq.impl.SQLDataType.INTEGER.asArrayDataType(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY.class));

	/**
	 * The parameter <code>P_ARRAYS1.OUT_ARRAY</code>.
	 */
	public static final org.jooq.Parameter<org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY> OUT_ARRAY = createParameter("OUT_ARRAY", org.jooq.impl.SQLDataType.INTEGER.asArrayDataType(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY.class));

	/**
	 * Create a new routine call instance
	 */
	public P_ARRAYS1() {
		super("P_ARRAYS1", org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA);

		addInParameter(IN_ARRAY);
		addOutParameter(OUT_ARRAY);
	}

	/**
	 * Set the <code>IN_ARRAY</code> parameter IN value to the routine
	 */
	public void setIN_ARRAY(org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY value) {
		setValue(org.jooq.test.oracle3.generatedclasses.routines.P_ARRAYS1.IN_ARRAY, value);
	}

	/**
	 * Get the <code>OUT_ARRAY</code> parameter OUT value from the routine
	 */
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_NUMBER_ARRAY getOUT_ARRAY() {
		return getValue(OUT_ARRAY);
	}
}
