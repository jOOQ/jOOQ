/**
 * This class is generated by jOOQ
 */
package org.jooq.util.maven.example.procedures;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class PEnhanceAddress2 extends org.jooq.util.postgres.PostgresSingleUDTOutParameterProcedure {

	private static final long serialVersionUID = -609306050;


	/**
	 * An uncommented item
	 */
	public static final org.jooq.Parameter<org.jooq.util.maven.example.udt.records.UAddressTypeRecord> ADDRESS = new org.jooq.impl.ParameterImpl<org.jooq.util.maven.example.udt.records.UAddressTypeRecord>("address", org.jooq.util.maven.example.udt.UAddressType.U_ADDRESS_TYPE.getDataType());

	/**
	 * Create a new procedure call instance
	 */
	public PEnhanceAddress2() {
		super(org.jooq.SQLDialect.POSTGRES, "p_enhance_address2", org.jooq.util.maven.example.Public.PUBLIC);

		addOutParameter(ADDRESS);
	}

	public org.jooq.util.maven.example.udt.records.UAddressTypeRecord getAddress() {
		return getValue(ADDRESS);
	}
}
