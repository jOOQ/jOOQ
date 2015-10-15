/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.public_.tables.interfaces;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITPerformanceJooq extends Serializable {

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.ID</code>.
	 */
	public ITPerformanceJooq setId(Integer value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.ID</code>.
	 */
	public Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_INT</code>.
	 */
	public ITPerformanceJooq setValueInt(Integer value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_INT</code>.
	 */
	public Integer getValueInt();

	/**
	 * Setter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_STRING</code>.
	 */
	public ITPerformanceJooq setValueString(String value);

	/**
	 * Getter for <code>PUBLIC.T_PERFORMANCE_JOOQ.VALUE_STRING</code>.
	 */
	public String getValueString();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITPerformanceJooq
	 */
	public void from(org.jooq.test.h2.generatedclasses.public_.tables.interfaces.ITPerformanceJooq from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITPerformanceJooq
	 */
	public <E extends org.jooq.test.h2.generatedclasses.public_.tables.interfaces.ITPerformanceJooq> E into(E into);
}
