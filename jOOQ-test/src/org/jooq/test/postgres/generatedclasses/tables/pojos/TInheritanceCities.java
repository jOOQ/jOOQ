/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TInheritanceCities implements org.jooq.test.postgres.generatedclasses.tables.interfaces.ITInheritanceCities {

	private static final long serialVersionUID = -1174461345;

	private final java.lang.String  name;
	private final java.lang.Integer population;
	private final java.lang.Integer altitude;

	public TInheritanceCities(
		java.lang.String  name,
		java.lang.Integer population,
		java.lang.Integer altitude
	) {
		this.name = name;
		this.population = population;
		this.altitude = altitude;
	}

	@Override
	public java.lang.String getName() {
		return this.name;
	}

	@Override
	public java.lang.Integer getPopulation() {
		return this.population;
	}

	@Override
	public java.lang.Integer getAltitude() {
		return this.altitude;
	}
}
