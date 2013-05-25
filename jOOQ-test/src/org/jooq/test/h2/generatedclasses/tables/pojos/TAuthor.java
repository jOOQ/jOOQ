/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 *
 * An entity holding authors of books
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TAuthor implements org.jooq.test.h2.generatedclasses.tables.interfaces.ITAuthor {

	private static final long serialVersionUID = 1523594314;

	private java.lang.Integer id;
	private java.lang.String  firstName;
	private java.lang.String  lastName;
	private java.sql.Date     dateOfBirth;
	private java.lang.Integer yearOfBirth;
	private java.lang.String  address;

	@Override
	public java.lang.Integer getId() {
		return this.id;
	}

	@Override
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@Override
	public java.lang.String getFirstName() {
		return this.firstName;
	}

	@Override
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	@Override
	public java.lang.String getLastName() {
		return this.lastName;
	}

	@Override
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	@Override
	public java.sql.Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	@Override
	public void setDateOfBirth(java.sql.Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public java.lang.Integer getYearOfBirth() {
		return this.yearOfBirth;
	}

	@Override
	public void setYearOfBirth(java.lang.Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	@Override
	public java.lang.String getAddress() {
		return this.address;
	}

	@Override
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
}
