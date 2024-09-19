/*
 * This file is generated by jOOQ.
 */
package org.jooq.meta.clickhouse.information_schema;


import org.jooq.meta.clickhouse.information_schema.tables.Columns;
import org.jooq.meta.clickhouse.information_schema.tables.KeyColumnUsage;
import org.jooq.meta.clickhouse.information_schema.tables.Schemata;


/**
 * Convenience access to all tables in information_schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Tables {

    /**
     * The table <code>information_schema.columns</code>.
     */
    public static final Columns COLUMNS = Columns.COLUMNS;

    /**
     * The table <code>information_schema.key_column_usage</code>.
     */
    public static final KeyColumnUsage KEY_COLUMN_USAGE = KeyColumnUsage.KEY_COLUMN_USAGE;

    /**
     * The table <code>information_schema.schemata</code>.
     */
    public static final Schemata SCHEMATA = Schemata.SCHEMATA;

    /**
     * The table <code>information_schema.tables</code>.
     */
    public static final org.jooq.meta.clickhouse.information_schema.tables.Tables TABLES = org.jooq.meta.clickhouse.information_schema.tables.Tables.TABLES;
}