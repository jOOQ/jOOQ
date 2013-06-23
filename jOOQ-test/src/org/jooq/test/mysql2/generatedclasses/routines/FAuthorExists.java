/**
 * This class is generated by jOOQ
 */
package org.jooq.test.mysql2.generatedclasses.routines;

/**
 * This class is generated by jOOQ.
 *
 * Check existence of an author
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FAuthorExists extends org.jooq.impl.AbstractRoutine<java.lang.Integer> {

	private static final long serialVersionUID = 27146167;

	/**
	 * The parameter <code>test2.f_author_exists.RETURN_VALUE</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.Integer> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.INTEGER);

	/**
	 * The parameter <code>test2.f_author_exists.author_name</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.String> AUTHOR_NAME = createParameter("author_name", org.jooq.impl.SQLDataType.VARCHAR.length(50));

	/**
	 * Create a new routine call instance
	 */
	public FAuthorExists() {
		super("f_author_exists", org.jooq.test.mysql2.generatedclasses.Test2.TEST2, org.jooq.impl.SQLDataType.INTEGER);

		setReturnParameter(RETURN_VALUE);
		addInParameter(AUTHOR_NAME);
	}

	/**
	 * Set the <code>author_name</code> parameter IN value to the routine
	 */
	public void setAuthorName(java.lang.String value) {
		setValue(org.jooq.test.mysql2.generatedclasses.routines.FAuthorExists.AUTHOR_NAME, value);
	}

	/**
	 * Set the <code>author_name</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public void setAuthorName(org.jooq.Field<java.lang.String> field) {
		setField(AUTHOR_NAME, field);
	}
}
