/**
 * This class is generated by jOOQ
 */
package org.jooq.test.mysql.generatedclasses.routines;

/**
 * This class is generated by jOOQ.
 *
 * 1 constant value
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.0.0"},
                            comments = "This class is generated by jOOQ")
public class FOne extends org.jooq.impl.AbstractRoutine<java.lang.Integer> {

	private static final long serialVersionUID = 225071809;


	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.lang.Integer> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.INTEGER);

	/**
	 * Create a new routine call instance
	 */
	public FOne() {
		super(org.jooq.SQLDialect.MYSQL, "f_one", org.jooq.test.mysql.generatedclasses.Test.TEST, org.jooq.impl.SQLDataType.INTEGER);

		setReturnParameter(RETURN_VALUE);
	}
}
