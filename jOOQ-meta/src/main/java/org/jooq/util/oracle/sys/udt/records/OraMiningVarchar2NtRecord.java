/**
 * This class is generated by jOOQ
 */
package org.jooq.util.oracle.sys.udt.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OraMiningVarchar2NtRecord extends org.jooq.impl.ArrayRecordImpl<java.lang.String> {

	private static final long serialVersionUID = 1775755208;

	/**
	 * Create a new <code>SYS.ORA_MINING_VARCHAR2_NT</code> record
	 */
	public OraMiningVarchar2NtRecord() {
		super(org.jooq.util.oracle.sys.Sys.SYS, "ORA_MINING_VARCHAR2_NT", org.jooq.impl.SQLDataType.VARCHAR.length(4000));
	}

	/**
	 * Create a new <code>SYS.ORA_MINING_VARCHAR2_NT</code> record
	 */
	public OraMiningVarchar2NtRecord(java.lang.String... array) {
		this();
		set(array);
	}

	/**
	 * Create a new <code>SYS.ORA_MINING_VARCHAR2_NT</code> record
	 */
	public OraMiningVarchar2NtRecord(java.util.Collection<? extends java.lang.String> collection) {
		this();
		set(collection);
	}
}
