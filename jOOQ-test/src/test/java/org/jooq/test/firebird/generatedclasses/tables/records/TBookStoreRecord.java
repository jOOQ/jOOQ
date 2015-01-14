/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables.records;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.test.firebird.generatedclasses.tables.TBookStore;
import org.jooq.test.firebird.generatedclasses.tables.interfaces.ITBookStore;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "T_BOOK_STORE")
public class TBookStoreRecord extends UpdatableRecordImpl<TBookStoreRecord> implements Record1<String>, ITBookStore {

	private static final long serialVersionUID = -683558459;

	/**
	 * Setter for <code>T_BOOK_STORE.NAME</code>.
	 */
	@Override
	public void setName(String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>T_BOOK_STORE.NAME</code>.
	 */
	@Id
	@Column(name = "NAME", unique = true, nullable = false, length = 400)
	@NotNull
	@Size(max = 400)
	@Override
	public String getName() {
		return (String) getValue(0);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<String> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record1 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row1<String> fieldsRow() {
		return (Row1) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row1<String> valuesRow() {
		return (Row1) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field1() {
		return TBookStore.T_BOOK_STORE.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value1() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TBookStoreRecord value1(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TBookStoreRecord values(String value1) {
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITBookStore from) {
		setName(from.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITBookStore> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TBookStoreRecord
	 */
	public TBookStoreRecord() {
		super(TBookStore.T_BOOK_STORE);
	}

	/**
	 * Create a detached, initialised TBookStoreRecord
	 */
	public TBookStoreRecord(String name) {
		super(TBookStore.T_BOOK_STORE);

		setValue(0, name);
	}
}
