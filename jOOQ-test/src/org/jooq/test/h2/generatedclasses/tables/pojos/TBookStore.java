package org.jooq.test.h2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 *
 * A book store
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBookStore implements org.jooq.test.h2.generatedclasses.tables.interfaces.ITBookStore {

	private static final long serialVersionUID = -319948492;

	private java.lang.String name;

	@Override
	public java.lang.String getName() {
		return this.name;
	}

	@Override
	public void setName(java.lang.String name) {
		this.name = name;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(org.jooq.test.h2.generatedclasses.tables.interfaces.ITBookStore from) {
		setName(from.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends org.jooq.test.h2.generatedclasses.tables.interfaces.ITBookStore> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
