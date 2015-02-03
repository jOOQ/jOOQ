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
import org.jooq.test.sqlite.generatedclasses.tables.records.XTestCase_71Record;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class XTestCase_71 extends TableImpl<XTestCase_71Record> {

	private static final long serialVersionUID = 1297255773;

	/**
	 * The reference instance of <code>x_test_case_71</code>
	 */
	public static final XTestCase_71 X_TEST_CASE_71 = new XTestCase_71();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<XTestCase_71Record> getRecordType() {
		return XTestCase_71Record.class;
	}

	/**
	 * The column <code>x_test_case_71.ID</code>.
	 */
	public static final TableField<XTestCase_71Record, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), X_TEST_CASE_71, "");

	/**
	 * The column <code>x_test_case_71.TEST_CASE_64_69_ID</code>.
	 */
	public static final TableField<XTestCase_71Record, Short> TEST_CASE_64_69_ID = createField("TEST_CASE_64_69_ID", org.jooq.impl.SQLDataType.SMALLINT, X_TEST_CASE_71, "");

	/**
	 * No further instances allowed
	 */
	private XTestCase_71() {
		this("x_test_case_71", null);
	}

	private XTestCase_71(String alias, Table<XTestCase_71Record> aliased) {
		this(alias, aliased, null);
	}

	private XTestCase_71(String alias, Table<XTestCase_71Record> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<XTestCase_71Record> getPrimaryKey() {
		return Keys.PK_X_TEST_CASE_71;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<XTestCase_71Record>> getKeys() {
		return Arrays.<UniqueKey<XTestCase_71Record>>asList(Keys.PK_X_TEST_CASE_71);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<XTestCase_71Record, ?>> getReferences() {
		return Arrays.<ForeignKey<XTestCase_71Record, ?>>asList(Keys.FK_X_TEST_CASE_71_X_TEST_CASE_64_69_1);
	}
}
