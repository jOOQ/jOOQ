/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.udt;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class U_3005 extends org.jooq.impl.UDTImpl<org.jooq.test.oracle.generatedclasses.test.udt.records.U_3005Record> {

	private static final long serialVersionUID = 1939392719;

	/**
	 * The reference instance of <code>TEST.U_3005</code>
	 */
	public static final org.jooq.test.oracle.generatedclasses.test.udt.U_3005 U_3005 = new org.jooq.test.oracle.generatedclasses.test.udt.U_3005();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.oracle.generatedclasses.test.udt.records.U_3005Record> getRecordType() {
		return org.jooq.test.oracle.generatedclasses.test.udt.records.U_3005Record.class;
	}

	/**
	 * The attribute <code>TEST.U_3005.ID</code>.
	 */
	public static final org.jooq.UDTField<org.jooq.test.oracle.generatedclasses.test.udt.records.U_3005Record, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, U_3005, "");

	/**
	 * No further instances allowed
	 */
	private U_3005() {
		super("U_3005", org.jooq.test.oracle.generatedclasses.test.Test.TEST);

		// Initialise data type
		getDataType();
	}
}
