/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.derby.sys.tables;


import java.util.Arrays;
import java.util.List;

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
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.meta.derby.sys.Keys;
import org.jooq.meta.derby.sys.Sys;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Syssequences extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>SYS.SYSSEQUENCES</code>
     */
    public static final Syssequences SYSSEQUENCES = new Syssequences();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>SYS.SYSSEQUENCES.SEQUENCEID</code>.
     */
    public final TableField<Record, String> SEQUENCEID = createField(DSL.name("SEQUENCEID"), SQLDataType.CHAR(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.SEQUENCENAME</code>.
     */
    public final TableField<Record, String> SEQUENCENAME = createField(DSL.name("SEQUENCENAME"), SQLDataType.VARCHAR(128).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.SCHEMAID</code>.
     */
    public final TableField<Record, String> SCHEMAID = createField(DSL.name("SCHEMAID"), SQLDataType.CHAR(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.SEQUENCEDATATYPE</code>.
     */
    public final TableField<Record, String> SEQUENCEDATATYPE = createField(DSL.name("SEQUENCEDATATYPE"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.CURRENTVALUE</code>.
     */
    public final TableField<Record, Long> CURRENTVALUE = createField(DSL.name("CURRENTVALUE"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.STARTVALUE</code>.
     */
    public final TableField<Record, Long> STARTVALUE = createField(DSL.name("STARTVALUE"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.MINIMUMVALUE</code>.
     */
    public final TableField<Record, Long> MINIMUMVALUE = createField(DSL.name("MINIMUMVALUE"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.MAXIMUMVALUE</code>.
     */
    public final TableField<Record, Long> MAXIMUMVALUE = createField(DSL.name("MAXIMUMVALUE"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.INCREMENT</code>.
     */
    public final TableField<Record, Long> INCREMENT = createField(DSL.name("INCREMENT"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSEQUENCES.CYCLEOPTION</code>.
     */
    public final TableField<Record, String> CYCLEOPTION = createField(DSL.name("CYCLEOPTION"), SQLDataType.CHAR(1).nullable(false), this, "");

    private Syssequences(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Syssequences(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>SYS.SYSSEQUENCES</code> table reference
     */
    public Syssequences(String alias) {
        this(DSL.name(alias), SYSSEQUENCES);
    }

    /**
     * Create an aliased <code>SYS.SYSSEQUENCES</code> table reference
     */
    public Syssequences(Name alias) {
        this(alias, SYSSEQUENCES);
    }

    /**
     * Create a <code>SYS.SYSSEQUENCES</code> table reference
     */
    public Syssequences() {
        this(DSL.name("SYSSEQUENCES"), null);
    }

    public <O extends Record> Syssequences(Table<O> path, ForeignKey<O, Record> childPath, InverseForeignKey<O, Record> parentPath) {
        super(path, childPath, parentPath, SYSSEQUENCES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Sys.SYS;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.SYNTHETIC_FK_SYSSEQUENCES__SYNTHETIC_PK_SYSSCHEMAS);
    }

    private transient Sysschemas _sysschemas;

    /**
     * Get the implicit join path to the <code>SYS.SYSSCHEMAS</code> table.
     */
    public Sysschemas sysschemas() {
        if (_sysschemas == null)
            _sysschemas = new Sysschemas(this, Keys.SYNTHETIC_FK_SYSSEQUENCES__SYNTHETIC_PK_SYSSCHEMAS, null);

        return _sysschemas;
    }

    @Override
    public Syssequences as(String alias) {
        return new Syssequences(DSL.name(alias), this);
    }

    @Override
    public Syssequences as(Name alias) {
        return new Syssequences(alias, this);
    }

    @Override
    public Syssequences as(Table<?> alias) {
        return new Syssequences(alias.getQualifiedName(), this);
    }
}
