/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import java.util.Arrays;

import org.jooq.test.h2.generatedclasses.tables.interfaces.ITArrays;


/**
 * This is a POJO for table T_ARRAYS.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TArrays implements ITArrays {

	private static final long serialVersionUID = 1615511176;

	private Integer  id;
	private Object[] stringArray;
	private Object[] numberArray;
	private Object[] dateArray;

	public TArrays() {}

	public TArrays(TArrays value) {
		this.id = value.id;
		this.stringArray = value.stringArray;
		this.numberArray = value.numberArray;
		this.dateArray = value.dateArray;
	}

	public TArrays(
		Integer  id,
		Object[] stringArray,
		Object[] numberArray,
		Object[] dateArray
	) {
		this.id = id;
		this.stringArray = stringArray;
		this.numberArray = numberArray;
		this.dateArray = dateArray;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public TArrays setId(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public Object[] getStringArray() {
		return this.stringArray;
	}

	@Override
	public TArrays setStringArray(Object[] stringArray) {
		this.stringArray = stringArray;
		return this;
	}

	@Override
	public Object[] getNumberArray() {
		return this.numberArray;
	}

	@Override
	public TArrays setNumberArray(Object[] numberArray) {
		this.numberArray = numberArray;
		return this;
	}

	@Override
	public Object[] getDateArray() {
		return this.dateArray;
	}

	@Override
	public TArrays setDateArray(Object[] dateArray) {
		this.dateArray = dateArray;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TArrays (");

		sb.append(id);
		sb.append(", ").append(Arrays.toString(stringArray));
		sb.append(", ").append(Arrays.toString(numberArray));
		sb.append(", ").append(Arrays.toString(dateArray));

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
	public void from(ITArrays from) {
		setId(from.getId());
		setStringArray(from.getStringArray());
		setNumberArray(from.getNumberArray());
		setDateArray(from.getDateArray());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITArrays> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
