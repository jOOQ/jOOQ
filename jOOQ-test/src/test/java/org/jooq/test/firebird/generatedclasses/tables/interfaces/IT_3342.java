/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables.interfaces;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "T_3342")
public interface IT_3342 extends Serializable {

	/**
	 * Setter for <code>T_3342.B</code>.
	 */
	public void setB(String value);

	/**
	 * Getter for <code>T_3342.B</code>.
	 */
	@Column(name = "B")
	public String getB();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IT_3342
	 */
	public void from(org.jooq.test.firebird.generatedclasses.tables.interfaces.IT_3342 from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IT_3342
	 */
	public <E extends org.jooq.test.firebird.generatedclasses.tables.interfaces.IT_3342> E into(E into);
}
