/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.packages.library;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.0.0"},
                            comments = "This class is generated by jOOQ")
public class PkgFAuthorExists2 extends org.jooq.impl.AbstractRoutine<java.math.BigDecimal> {

	private static final long serialVersionUID = -1385513895;


	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.math.BigDecimal> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.NUMERIC);

	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.lang.String> AUTHOR_NAME = createParameter("AUTHOR_NAME", org.jooq.impl.SQLDataType.VARCHAR);

	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.math.BigDecimal> UNUSED = createParameter("UNUSED", org.jooq.impl.SQLDataType.NUMERIC);

	/**
	 * Create a new routine call instance
	 */
	public PkgFAuthorExists2() {
		super(org.jooq.SQLDialect.ORACLE, "PKG_F_AUTHOR_EXISTS", org.jooq.test.oracle.generatedclasses.Test.TEST, org.jooq.test.oracle.generatedclasses.packages.Library.LIBRARY, org.jooq.impl.SQLDataType.NUMERIC);

		setReturnParameter(RETURN_VALUE);
		addInParameter(AUTHOR_NAME);
		addInParameter(UNUSED);
		setOverloaded(true);
	}

	/**
	 * Set the <code>AUTHOR_NAME</code> parameter to the routine
	 */
	public void setAuthorName(java.lang.String value) {
		setValue(AUTHOR_NAME, value);
	}

	/**
	 * Set the <code>AUTHOR_NAME</code> parameter to the function
	 * <p>
	 * Use this method only, if the function is called as a {@link org.jooq.Field} in a {@link org.jooq.Select} statement!
	 */
	public void setAuthorName(org.jooq.Field<java.lang.String> field) {
		setField(AUTHOR_NAME, field);
	}

	/**
	 * Set the <code>UNUSED</code> parameter to the routine
	 */
	public void setUnused(java.lang.Number value) {
		setNumber(UNUSED, value);
	}

	/**
	 * Set the <code>UNUSED</code> parameter to the function
	 * <p>
	 * Use this method only, if the function is called as a {@link org.jooq.Field} in a {@link org.jooq.Select} statement!
	 */
	public void setUnused(org.jooq.Field<? extends java.lang.Number> field) {
		setNumber(UNUSED, field);
	}
}
