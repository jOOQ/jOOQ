/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import org.jooq.test.h2.generatedclasses.tables.interfaces.IT_2327UkOnly;


/**
 * This is a POJO for table T_2327_UK_ONLY.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_2327UkOnly implements IT_2327UkOnly {

	private static final long serialVersionUID = -728832744;

	private Integer id;

	public T_2327UkOnly() {}

	public T_2327UkOnly(T_2327UkOnly value) {
		this.id = value.id;
	}

	public T_2327UkOnly(
		Integer id
	) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public T_2327UkOnly setId(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("T_2327UkOnly (");

		sb.append(id);

		sb.append(")");
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IT_2327UkOnly from) {
		setId(from.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IT_2327UkOnly> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
