/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.h2.information_schema.tables;


import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.meta.h2.information_schema.InformationSchema;


/**
 * This class is generated by jOOQ.
 *
 * @deprecated - [#17388] [#17389] - 3.20.0 - Use the classes from the
 *             <code>information_schema_2</code> package, instead.
 */
@Deprecated
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes extends TableImpl<Record> {

    private static final long serialVersionUID = 1219205902;

    /**
     * The reference instance of <code>INFORMATION_SCHEMA.INDEXES</code>
     */
    public static final Indexes INDEXES = new Indexes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.TABLE_CATALOG</code>.
     */
    public final TableField<Record, String> TABLE_CATALOG = createField(DSL.name("TABLE_CATALOG"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.TABLE_SCHEMA</code>.
     */
    public final TableField<Record, String> TABLE_SCHEMA = createField(DSL.name("TABLE_SCHEMA"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.TABLE_NAME</code>.
     */
    public final TableField<Record, String> TABLE_NAME = createField(DSL.name("TABLE_NAME"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.NON_UNIQUE</code>.
     */
    public final TableField<Record, Boolean> NON_UNIQUE = createField(DSL.name("NON_UNIQUE"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.INDEX_NAME</code>.
     */
    public final TableField<Record, String> INDEX_NAME = createField(DSL.name("INDEX_NAME"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.ORDINAL_POSITION</code>.
     */
    public final TableField<Record, Short> ORDINAL_POSITION = createField(DSL.name("ORDINAL_POSITION"), SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.COLUMN_NAME</code>.
     */
    public final TableField<Record, String> COLUMN_NAME = createField(DSL.name("COLUMN_NAME"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.CARDINALITY</code>.
     */
    public final TableField<Record, Integer> CARDINALITY = createField(DSL.name("CARDINALITY"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.PRIMARY_KEY</code>.
     */
    public final TableField<Record, Boolean> PRIMARY_KEY = createField(DSL.name("PRIMARY_KEY"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.INDEX_TYPE_NAME</code>.
     */
    public final TableField<Record, String> INDEX_TYPE_NAME = createField(DSL.name("INDEX_TYPE_NAME"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.IS_GENERATED</code>.
     */
    public final TableField<Record, Boolean> IS_GENERATED = createField(DSL.name("IS_GENERATED"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.INDEX_TYPE</code>.
     */
    public final TableField<Record, Short> INDEX_TYPE = createField(DSL.name("INDEX_TYPE"), SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.ASC_OR_DESC</code>.
     */
    public final TableField<Record, String> ASC_OR_DESC = createField(DSL.name("ASC_OR_DESC"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.PAGES</code>.
     */
    public final TableField<Record, Integer> PAGES = createField(DSL.name("PAGES"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.FILTER_CONDITION</code>.
     */
    public final TableField<Record, String> FILTER_CONDITION = createField(DSL.name("FILTER_CONDITION"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.REMARKS</code>.
     */
    public final TableField<Record, String> REMARKS = createField(DSL.name("REMARKS"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.SQL</code>.
     */
    public final TableField<Record, String> SQL = createField(DSL.name("SQL"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.ID</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.SORT_TYPE</code>.
     */
    public final TableField<Record, Integer> SORT_TYPE = createField(DSL.name("SORT_TYPE"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.CONSTRAINT_NAME</code>.
     */
    public final TableField<Record, String> CONSTRAINT_NAME = createField(DSL.name("CONSTRAINT_NAME"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.INDEX_CLASS</code>.
     */
    public final TableField<Record, String> INDEX_CLASS = createField(DSL.name("INDEX_CLASS"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.INDEXES.AFFINITY</code>.
     */
    public final TableField<Record, Boolean> AFFINITY = createField(DSL.name("AFFINITY"), SQLDataType.BOOLEAN, this, "");

    private Indexes(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Indexes(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>INFORMATION_SCHEMA.INDEXES</code> table reference
     */
    public Indexes(String alias) {
        this(DSL.name(alias), INDEXES);
    }

    /**
     * Create an aliased <code>INFORMATION_SCHEMA.INDEXES</code> table reference
     */
    public Indexes(Name alias) {
        this(alias, INDEXES);
    }

    /**
     * Create a <code>INFORMATION_SCHEMA.INDEXES</code> table reference
     */
    public Indexes() {
        this(DSL.name("INDEXES"), null);
    }

    public <O extends Record> Indexes(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, INDEXES);
    }

    @Override
    public Schema getSchema() {
        return InformationSchema.INFORMATION_SCHEMA;
    }

    @Override
    public Indexes as(String alias) {
        return new Indexes(DSL.name(alias), this);
    }

    @Override
    public Indexes as(Name alias) {
        return new Indexes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Indexes rename(String name) {
        return new Indexes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Indexes rename(Name name) {
        return new Indexes(name, null);
    }
}
