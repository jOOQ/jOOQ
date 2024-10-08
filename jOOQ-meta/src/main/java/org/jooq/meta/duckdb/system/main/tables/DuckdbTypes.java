/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.duckdb.system.main.tables;


import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.meta.duckdb.system.main.Keys;
import org.jooq.meta.duckdb.system.main.Main;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DuckdbTypes extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>system.main.duckdb_types</code>
     */
    public static final DuckdbTypes DUCKDB_TYPES = new DuckdbTypes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>system.main.duckdb_types.database_name</code>.
     */
    public final TableField<Record, String> DATABASE_NAME = createField(DSL.name("database_name"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>system.main.duckdb_types.database_oid</code>.
     */
    public final TableField<Record, Long> DATABASE_OID = createField(DSL.name("database_oid"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>system.main.duckdb_types.schema_name</code>.
     */
    public final TableField<Record, String> SCHEMA_NAME = createField(DSL.name("schema_name"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>system.main.duckdb_types.schema_oid</code>.
     */
    public final TableField<Record, Long> SCHEMA_OID = createField(DSL.name("schema_oid"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>system.main.duckdb_types.type_oid</code>.
     */
    public final TableField<Record, Long> TYPE_OID = createField(DSL.name("type_oid"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>system.main.duckdb_types.type_name</code>.
     */
    public final TableField<Record, String> TYPE_NAME = createField(DSL.name("type_name"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>system.main.duckdb_types.type_size</code>.
     */
    public final TableField<Record, Long> TYPE_SIZE = createField(DSL.name("type_size"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>system.main.duckdb_types.logical_type</code>.
     */
    public final TableField<Record, String> LOGICAL_TYPE = createField(DSL.name("logical_type"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>system.main.duckdb_types.type_category</code>.
     */
    public final TableField<Record, String> TYPE_CATEGORY = createField(DSL.name("type_category"), SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>system.main.duckdb_types.comment</code>.
     */
    public final TableField<Record, String> COMMENT = createField(DSL.name("comment"), SQLDataType.VARCHAR, this, "");

    /**
     * @deprecated Unknown data type. If this is a qualified, user-defined type,
     * it may have been excluded from code generation. If this is a built-in
     * type, you can define an explicit {@link org.jooq.Binding} to specify how
     * this type should be handled. Deprecation can be turned off using
     * {@literal <deprecationOnUnknownTypes/>} in your code generator
     * configuration.
     */
    @Deprecated
    public final TableField<Record, Object> TAGS = createField(DSL.name("tags"), DefaultDataType.getDefaultDataType("MAP(VARCHAR, VARCHAR)"), this, "");

    /**
     * The column <code>system.main.duckdb_types.internal</code>.
     */
    public final TableField<Record, Boolean> INTERNAL = createField(DSL.name("internal"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>system.main.duckdb_types.labels</code>.
     */
    public final TableField<Record, String[]> LABELS = createField(DSL.name("labels"), SQLDataType.VARCHAR.array(), this, "");

    private DuckdbTypes(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private DuckdbTypes(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view(), where);
    }

    /**
     * Create an aliased <code>system.main.duckdb_types</code> table reference
     */
    public DuckdbTypes(String alias) {
        this(DSL.name(alias), DUCKDB_TYPES);
    }

    /**
     * Create an aliased <code>system.main.duckdb_types</code> table reference
     */
    public DuckdbTypes(Name alias) {
        this(alias, DUCKDB_TYPES);
    }

    /**
     * Create a <code>system.main.duckdb_types</code> table reference
     */
    public DuckdbTypes() {
        this(DSL.name("duckdb_types"), null);
    }

    public <O extends Record> DuckdbTypes(Table<O> path, ForeignKey<O, Record> childPath, InverseForeignKey<O, Record> parentPath) {
        super(path, childPath, parentPath, DUCKDB_TYPES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Main.MAIN;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.SYNTHETIC_PK_DUCKDB_TYPES;
    }

    private transient DuckdbColumns _duckdbColumns;

    /**
     * Get the implicit to-many join path to the
     * <code>system.main.duckdb_columns</code> table
     */
    public DuckdbColumns duckdbColumns() {
        if (_duckdbColumns == null)
            _duckdbColumns = new DuckdbColumns(this, null, Keys.SYNTHETIC_FK_DUCKDB_COLUMNS__SYNTHETIC_PK_DUCKDB_TYPES.getInverseKey());

        return _duckdbColumns;
    }

    @Override
    public DuckdbTypes as(String alias) {
        return new DuckdbTypes(DSL.name(alias), this);
    }

    @Override
    public DuckdbTypes as(Name alias) {
        return new DuckdbTypes(alias, this);
    }

    @Override
    public DuckdbTypes as(Table<?> alias) {
        return new DuckdbTypes(alias.getQualifiedName(), this);
    }
}
