/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import org.jooq.test.h2.generatedclasses.tables.interfaces.IXTestCase_85;


/**
 * This is a POJO for table X_TEST_CASE_85.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class XTestCase_85 implements IXTestCase_85 {

	private static final long serialVersionUID = 292075442;

	private Integer id;
	private Integer xUnusedId;
	private String  xUnusedName;

	public XTestCase_85() {}

	public XTestCase_85(XTestCase_85 value) {
		this.id = value.id;
		this.xUnusedId = value.xUnusedId;
		this.xUnusedName = value.xUnusedName;
	}

	public XTestCase_85(
		Integer id,
		Integer xUnusedId,
		String  xUnusedName
	) {
		this.id = id;
		this.xUnusedId = xUnusedId;
		this.xUnusedName = xUnusedName;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public XTestCase_85 setId(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public Integer getXUnusedId() {
		return this.xUnusedId;
	}

	@Override
	public XTestCase_85 setXUnusedId(Integer xUnusedId) {
		this.xUnusedId = xUnusedId;
		return this;
	}

	@Override
	public String getXUnusedName() {
		return this.xUnusedName;
	}

	@Override
	public XTestCase_85 setXUnusedName(String xUnusedName) {
		this.xUnusedName = xUnusedName;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("XTestCase_85 (");

		sb.append(id);
		sb.append(", ").append(xUnusedId);
		sb.append(", ").append(xUnusedName);

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
	public void from(IXTestCase_85 from) {
		setId(from.getId());
		setXUnusedId(from.getXUnusedId());
		setXUnusedName(from.getXUnusedName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IXTestCase_85> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
