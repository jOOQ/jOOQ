/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import org.jooq.test.h2.generatedclasses.tables.interfaces.IT_3571;


/**
 * This is a POJO for table T_3571.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_3571 implements IT_3571 {

	private static final long serialVersionUID = 1425926013;

	private org.jooq.test.all.converters.T_3571 e1;
	private org.jooq.test.all.converters.T_3571 e2;
	private org.jooq.test.all.converters.T_3571 e3;
	private org.jooq.test.all.converters.T_3571 e4;

	public T_3571() {}

	public T_3571(T_3571 value) {
		this.e1 = value.e1;
		this.e2 = value.e2;
		this.e3 = value.e3;
		this.e4 = value.e4;
	}

	public T_3571(
		org.jooq.test.all.converters.T_3571 e1,
		org.jooq.test.all.converters.T_3571 e2,
		org.jooq.test.all.converters.T_3571 e3,
		org.jooq.test.all.converters.T_3571 e4
	) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}

	@Override
	public org.jooq.test.all.converters.T_3571 getE1() {
		return this.e1;
	}

	@Override
	public T_3571 setE1(org.jooq.test.all.converters.T_3571 e1) {
		this.e1 = e1;
		return this;
	}

	@Override
	public org.jooq.test.all.converters.T_3571 getE2() {
		return this.e2;
	}

	@Override
	public T_3571 setE2(org.jooq.test.all.converters.T_3571 e2) {
		this.e2 = e2;
		return this;
	}

	@Override
	public org.jooq.test.all.converters.T_3571 getE3() {
		return this.e3;
	}

	@Override
	public T_3571 setE3(org.jooq.test.all.converters.T_3571 e3) {
		this.e3 = e3;
		return this;
	}

	@Override
	public org.jooq.test.all.converters.T_3571 getE4() {
		return this.e4;
	}

	@Override
	public T_3571 setE4(org.jooq.test.all.converters.T_3571 e4) {
		this.e4 = e4;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("T_3571 (");

		sb.append(e1);
		sb.append(", ").append(e2);
		sb.append(", ").append(e3);
		sb.append(", ").append(e4);

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
	public void from(IT_3571 from) {
		setE1(from.getE1());
		setE2(from.getE2());
		setE3(from.getE3());
		setE4(from.getE4());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IT_3571> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
