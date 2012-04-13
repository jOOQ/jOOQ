/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "Culture", schema = "Production")
public class Culture implements java.io.Serializable {

	private static final long serialVersionUID = -2037209663;


	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 6)
	private java.lang.String   CultureID;

	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max = 50)
	private java.lang.String   Name;

	@javax.validation.constraints.NotNull
	private java.sql.Timestamp ModifiedDate;

	@javax.persistence.Id
	@javax.persistence.Column(name = "CultureID", unique = true, nullable = false, length = 6)
	public java.lang.String getCultureID() {
		return this.CultureID;
	}

	public void setCultureID(java.lang.String CultureID) {
		this.CultureID = CultureID;
	}

	@javax.persistence.Column(name = "Name", nullable = false, length = 50)
	public java.lang.String getName() {
		return this.Name;
	}

	public void setName(java.lang.String Name) {
		this.Name = Name;
	}

	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return this.ModifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
}
