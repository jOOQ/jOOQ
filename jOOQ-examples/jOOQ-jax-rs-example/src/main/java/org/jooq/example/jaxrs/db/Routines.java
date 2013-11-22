/**
 * This class is generated by jOOQ
 */
package org.jooq.example.jaxrs.db;

/**
 * This class is generated by jOOQ.
 *
 * Convenience access to all stored procedures and functions in license_server
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.1.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

	/**
	 * Call <code>license_server.generate_key</code>
	 */
	public static java.lang.String generateKey(org.jooq.Configuration configuration, java.sql.Timestamp licenseDate, java.lang.String email) {
		org.jooq.example.jaxrs.db.routines.GenerateKey f = new org.jooq.example.jaxrs.db.routines.GenerateKey();
		f.setLicenseDate(licenseDate);
		f.setEmail(email);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>license_server.generate_key</code> as a field
	 */
	public static org.jooq.Field<java.lang.String> generateKey(java.sql.Timestamp licenseDate, java.lang.String email) {
		org.jooq.example.jaxrs.db.routines.GenerateKey f = new org.jooq.example.jaxrs.db.routines.GenerateKey();
		f.setLicenseDate(licenseDate);
		f.setEmail(email);

		return f.asField();
	}

	/**
	 * Get <code>license_server.generate_key</code> as a field
	 */
	public static org.jooq.Field<java.lang.String> generateKey(org.jooq.Field<java.sql.Timestamp> licenseDate, org.jooq.Field<java.lang.String> email) {
		org.jooq.example.jaxrs.db.routines.GenerateKey f = new org.jooq.example.jaxrs.db.routines.GenerateKey();
		f.setLicenseDate(licenseDate);
		f.setEmail(email);

		return f.asField();
	}
}
