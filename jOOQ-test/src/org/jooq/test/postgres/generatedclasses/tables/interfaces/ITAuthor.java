/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITAuthor extends java.io.Serializable {

	/**
	 * Getter for <code>public.t_author.id</code>.
	 */
	public java.lang.Integer getId();

	/**
	 * Getter for <code>public.t_author.first_name</code>.
	 */
	public java.lang.String getFirstName();

	/**
	 * Getter for <code>public.t_author.last_name</code>.
	 */
	public java.lang.String getLastName();

	/**
	 * Getter for <code>public.t_author.date_of_birth</code>.
	 */
	public java.sql.Date getDateOfBirth();

	/**
	 * Getter for <code>public.t_author.year_of_birth</code>.
	 */
	public java.lang.Integer getYearOfBirth();

	/**
	 * Getter for <code>public.t_author.address</code>.
	 */
	public org.jooq.test.postgres.generatedclasses.udt.interfaces.IUAddressType getAddress();
}
