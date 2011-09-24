/**
 * This class is generated by jOOQ
 */
package org.jooq.util.maven.example.functions;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class FArrays2 extends org.jooq.impl.StoredFunctionImpl<java.lang.Long[]> {

	private static final long serialVersionUID = 2127746386;


	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<java.lang.Long[]> IN_ARRAY = new org.jooq.impl.ParameterImpl<java.lang.Long[]>("in_array", org.jooq.impl.SQLDataType.BIGINT.getArrayDataType());

	/**
	 * Create a new function call instance
	 */
	public FArrays2() {
		super(org.jooq.SQLDialect.POSTGRES, "f_arrays", org.jooq.util.maven.example.Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT.getArrayDataType());

		addInParameter(IN_ARRAY);
		setOverloaded(true);
	}

	/**
	 * Set the <code>in_array</code> parameter to the function
	 */
	public void setInArray(java.lang.Long[] value) {
		setValue(IN_ARRAY, value);
	}

	/**
	 * Set the <code>in_array</code> parameter to the function
	 * <p>
	 * Use this method only, if the function is called as a {@link org.jooq.Field} in a {@link org.jooq.Select} statement!
	 */
	public void setInArray(org.jooq.Field<java.lang.Long[]> field) {
		setField(IN_ARRAY, field);
	}
}
