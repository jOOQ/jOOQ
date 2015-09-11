/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;


import org.jooq.test.h2.generatedclasses.tables.interfaces.IVLibrary;


/**
 * This is a POJO for table V_LIBRARY.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VLibrary implements IVLibrary {

	private static final long serialVersionUID = -1921844575;

	private String author;
	private String title;

	public VLibrary() {}

	public VLibrary(VLibrary value) {
		this.author = value.author;
		this.title = value.title;
	}

	public VLibrary(
		String author,
		String title
	) {
		this.author = author;
		this.title = title;
	}

	@Override
	public String getAuthor() {
		return this.author;
	}

	@Override
	public VLibrary setAuthor(String author) {
		this.author = author;
		return this;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public VLibrary setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("VLibrary (");

		sb.append(author);
		sb.append(", ").append(title);

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
	public void from(IVLibrary from) {
		setAuthor(from.getAuthor());
		setTitle(from.getTitle());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IVLibrary> E into(E into) {
		into.from(this);
		return into;
	}

	// Here, a toString() method could be generated
}
