/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.routines;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class P_ENHANCE_ADDRESS3 extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.lang.Cloneable {

	private static final long serialVersionUID = -1607117668;

	/**
	 * The parameter <code>P_ENHANCE_ADDRESS3.ADDRESS</code>.
	 */
	public static final org.jooq.Parameter<org.jooq.test.oracle3.generatedclasses.udt.records.U_ADDRESS_TYPE> ADDRESS = createParameter("ADDRESS", org.jooq.test.oracle3.generatedclasses.udt.U_ADDRESS_TYPE.U_ADDRESS_TYPE.getDataType());

	/**
	 * Create a new routine call instance
	 */
	public P_ENHANCE_ADDRESS3() {
		super("P_ENHANCE_ADDRESS3", org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA);

		addInOutParameter(ADDRESS);
	}

	/**
	 * Set the <code>ADDRESS</code> parameter IN value to the routine
	 */
	public void setADDRESS(org.jooq.test.oracle3.generatedclasses.udt.records.U_ADDRESS_TYPE value) {
		setValue(org.jooq.test.oracle3.generatedclasses.routines.P_ENHANCE_ADDRESS3.ADDRESS, value);
	}

	/**
	 * Get the <code>ADDRESS</code> parameter OUT value from the routine
	 */
	public org.jooq.test.oracle3.generatedclasses.udt.records.U_ADDRESS_TYPE getADDRESS() {
		return getValue(ADDRESS);
	}
}
