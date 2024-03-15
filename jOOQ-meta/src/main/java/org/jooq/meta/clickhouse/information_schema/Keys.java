/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.clickhouse.information_schema;


import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.meta.clickhouse.information_schema.tables.Columns;
import org.jooq.meta.clickhouse.information_schema.tables.KeyColumnUsage;
import org.jooq.meta.clickhouse.information_schema.tables.Schemata;
import org.jooq.meta.clickhouse.information_schema.tables.Tables;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * information_schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<Record> SYNTHETIC_PK_SCHEMATA = Internal.createUniqueKey(Schemata.SCHEMATA, DSL.name("SYNTHETIC_PK_schemata"), new TableField[] { Schemata.SCHEMATA.CATALOG_NAME, Schemata.SCHEMATA.SCHEMA_NAME }, true);
    public static final UniqueKey<Record> SYNTHETIC_PK_TABLES = Internal.createUniqueKey(Tables.TABLES, DSL.name("SYNTHETIC_PK_tables"), new TableField[] { Tables.TABLES.TABLE_CATALOG, Tables.TABLES.TABLE_SCHEMA, Tables.TABLES.TABLE_NAME }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<Record, Record> SYNTHETIC_FK_COLUMNS__SYNTHETIC_PK_SCHEMATA = Internal.createForeignKey(Columns.COLUMNS, DSL.name("SYNTHETIC_FK_columns__SYNTHETIC_PK_schemata"), new TableField[] { Columns.COLUMNS.TABLE_CATALOG, Columns.COLUMNS.TABLE_SCHEMA }, Keys.SYNTHETIC_PK_SCHEMATA, new TableField[] { Schemata.SCHEMATA.CATALOG_NAME, Schemata.SCHEMATA.SCHEMA_NAME }, true);
    public static final ForeignKey<Record, Record> SYNTHETIC_FK_COLUMNS__SYNTHETIC_PK_TABLES = Internal.createForeignKey(Columns.COLUMNS, DSL.name("SYNTHETIC_FK_columns__SYNTHETIC_PK_tables"), new TableField[] { Columns.COLUMNS.TABLE_CATALOG, Columns.COLUMNS.TABLE_SCHEMA, Columns.COLUMNS.TABLE_NAME }, Keys.SYNTHETIC_PK_TABLES, new TableField[] { Tables.TABLES.TABLE_CATALOG, Tables.TABLES.TABLE_SCHEMA, Tables.TABLES.TABLE_NAME }, true);
    public static final ForeignKey<Record, Record> SYNTHETIC_FK_KEY_COLUMN_USAGE__SYNTHETIC_PK_SCHEMATA = Internal.createForeignKey(KeyColumnUsage.KEY_COLUMN_USAGE, DSL.name("SYNTHETIC_FK_key_column_usage__SYNTHETIC_PK_schemata"), new TableField[] { KeyColumnUsage.KEY_COLUMN_USAGE.CONSTRAINT_CATALOG, KeyColumnUsage.KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA }, Keys.SYNTHETIC_PK_SCHEMATA, new TableField[] { Schemata.SCHEMATA.CATALOG_NAME, Schemata.SCHEMATA.SCHEMA_NAME }, true);
    public static final ForeignKey<Record, Record> SYNTHETIC_FK_TABLES__SYNTHETIC_PK_SCHEMATA = Internal.createForeignKey(Tables.TABLES, DSL.name("SYNTHETIC_FK_tables__SYNTHETIC_PK_schemata"), new TableField[] { Tables.TABLES.TABLE_CATALOG, Tables.TABLES.TABLE_SCHEMA }, Keys.SYNTHETIC_PK_SCHEMATA, new TableField[] { Schemata.SCHEMATA.CATALOG_NAME, Schemata.SCHEMATA.SCHEMA_NAME }, true);
}