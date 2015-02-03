/**
 * This class is generated by jOOQ
 */
package org.jooq.test.sqlite.generatedclasses.tables;


import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.jooq.test.sqlite.generatedclasses.DefaultSchema;
import org.jooq.test.sqlite.generatedclasses.tables.records.VLibraryRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VLibrary extends TableImpl<VLibraryRecord> {

	private static final long serialVersionUID = -564683547;

	/**
	 * The reference instance of <code>V_LIBRARY</code>
	 */
	public static final VLibrary V_LIBRARY = new VLibrary();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<VLibraryRecord> getRecordType() {
		return VLibraryRecord.class;
	}

	/**
	 * The column <code>V_LIBRARY.AUTHOR</code>.
	 */
	public static final TableField<VLibraryRecord, String> AUTHOR = createField("AUTHOR", org.jooq.impl.SQLDataType.VARCHAR.length(50), V_LIBRARY, "");

	/**
	 * The column <code>V_LIBRARY.TITLE</code>.
	 */
	public static final TableField<VLibraryRecord, String> TITLE = createField("TITLE", org.jooq.impl.SQLDataType.VARCHAR.length(400), V_LIBRARY, "");

	/**
	 * No further instances allowed
	 */
	private VLibrary() {
		this("V_LIBRARY", null);
	}

	private VLibrary(String alias, Table<VLibraryRecord> aliased) {
		this(alias, aliased, null);
	}

	private VLibrary(String alias, Table<VLibraryRecord> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}
}
