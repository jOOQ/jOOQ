/**
 * This class is generated by jOOQ
 */
package org.jooq.test.sqlite.generatedclasses.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.jooq.test.sqlite.generatedclasses.DefaultSchema;
import org.jooq.test.sqlite.generatedclasses.Keys;
import org.jooq.test.sqlite.generatedclasses.tables.records.XTestCase_64_69Record;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class XTestCase_64_69 extends TableImpl<XTestCase_64_69Record> {

	private static final long serialVersionUID = 577471243;

	/**
	 * The reference instance of <code>x_test_case_64_69</code>
	 */
	public static final XTestCase_64_69 X_TEST_CASE_64_69 = new XTestCase_64_69();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<XTestCase_64_69Record> getRecordType() {
		return XTestCase_64_69Record.class;
	}

	/**
	 * The column <code>x_test_case_64_69.ID</code>.
	 */
	public static final TableField<XTestCase_64_69Record, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), X_TEST_CASE_64_69, "");

	/**
	 * The column <code>x_test_case_64_69.UNUSED_ID</code>.
	 */
	public static final TableField<XTestCase_64_69Record, Integer> UNUSED_ID = createField("UNUSED_ID", org.jooq.impl.SQLDataType.INTEGER, X_TEST_CASE_64_69, "");

	/**
	 * No further instances allowed
	 */
	private XTestCase_64_69() {
		this("x_test_case_64_69", null);
	}

	private XTestCase_64_69(String alias, Table<XTestCase_64_69Record> aliased) {
		this(alias, aliased, null);
	}

	private XTestCase_64_69(String alias, Table<XTestCase_64_69Record> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<XTestCase_64_69Record> getPrimaryKey() {
		return Keys.PK_X_TEST_CASE_64_69;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<XTestCase_64_69Record>> getKeys() {
		return Arrays.<UniqueKey<XTestCase_64_69Record>>asList(Keys.PK_X_TEST_CASE_64_69);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<XTestCase_64_69Record, ?>> getReferences() {
		return Arrays.<ForeignKey<XTestCase_64_69Record, ?>>asList(Keys.FK_X_TEST_CASE_64_69_X_UNUSED_1, Keys.FK_X_TEST_CASE_64_69_X_UNUSED_2);
	}
}
