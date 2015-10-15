/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.public_.tables.pojos;


import org.jooq.test.h2.generatedclasses.public_.tables.interfaces.ITPerformanceJdbc;


/**
 * This is a POJO for table T_PERFORMANCE_JDBC.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPerformanceJdbc implements ITPerformanceJdbc {

	private static final long serialVersionUID = -697512140;

	private Integer id;
	private Integer valueInt;
	private String  valueString;

	public TPerformanceJdbc() {}

	public TPerformanceJdbc(TPerformanceJdbc value) {
		this.id = value.id;
		this.valueInt = value.valueInt;
		this.valueString = value.valueString;
	}

	public TPerformanceJdbc(
		Integer id,
		Integer valueInt,
		String  valueString
	) {
		this.id = id;
		this.valueInt = valueInt;
		this.valueString = valueString;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public TPerformanceJdbc setId(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public Integer getValueInt() {
		return this.valueInt;
	}

	@Override
	public TPerformanceJdbc setValueInt(Integer valueInt) {
		this.valueInt = valueInt;
		return this;
	}

	@Override
	public String getValueString() {
		return this.valueString;
	}

	@Override
	public TPerformanceJdbc setValueString(String valueString) {
		this.valueString = valueString;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TPerformanceJdbc (");

		sb.append(id);
		sb.append(", ").append(valueInt);
		sb.append(", ").append(valueString);

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
	public void from(ITPerformanceJdbc from) {
		setId(from.getId());
		setValueInt(from.getValueInt());
		setValueString(from.getValueString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITPerformanceJdbc> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
