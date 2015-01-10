/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.interfaces;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITPerformanceJdbc extends Serializable {

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.ID</code>.
	 */
	public ITPerformanceJdbc setId(Integer value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.ID</code>.
	 */
	public Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_INT</code>.
	 */
	public ITPerformanceJdbc setValueInt(Integer value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_INT</code>.
	 */
	public Integer getValueInt();

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_STRING</code>.
	 */
	public ITPerformanceJdbc setValueString(String value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JDBC.VALUE_STRING</code>.
	 */
	public String getValueString();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITPerformanceJdbc
	 */
	public void from(org.jooq.test.h2.generatedclasses.tables.interfaces.ITPerformanceJdbc from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITPerformanceJdbc
	 */
	public <E extends org.jooq.test.h2.generatedclasses.tables.interfaces.ITPerformanceJdbc> E into(E into);
}
