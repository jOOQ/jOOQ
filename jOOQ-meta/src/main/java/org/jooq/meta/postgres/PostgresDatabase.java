/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.meta.postgres;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.Records.intoList;
import static org.jooq.Records.mapping;
import static org.jooq.Rows.toRowArray;
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.array;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.power;
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.DECIMAL_INTEGER;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.postgres.information_schema.Tables.ATTRIBUTES;
import static org.jooq.meta.postgres.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.meta.postgres.information_schema.Tables.COLUMNS;
import static org.jooq.meta.postgres.information_schema.Tables.DOMAINS;
import static org.jooq.meta.postgres.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.meta.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.meta.postgres.information_schema.Tables.ROUTINES;
import static org.jooq.meta.postgres.information_schema.Tables.SEQUENCES;
import static org.jooq.meta.postgres.information_schema.Tables.TABLES;
import static org.jooq.meta.postgres.information_schema.Tables.VIEWS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_CLASS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_CONSTRAINT;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_DEPEND;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_DESCRIPTION;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_ENUM;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_INDEX;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_INHERITS;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_PROC;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_SEQUENCE;
import static org.jooq.meta.postgres.pg_catalog.Tables.PG_TYPE;
import static org.jooq.util.postgres.PostgresDSL.array;
import static org.jooq.util.postgres.PostgresDSL.arrayAppend;
import static org.jooq.util.postgres.PostgresDSL.oid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Records;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Rows;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions.TableType;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultDomainDefinition;
import org.jooq.meta.DefaultEnumDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.ResultQueryDatabase;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.hsqldb.HSQLDBDatabase;
import org.jooq.meta.postgres.information_schema.tables.CheckConstraints;
import org.jooq.meta.postgres.information_schema.tables.KeyColumnUsage;
import org.jooq.meta.postgres.information_schema.tables.Routines;
import org.jooq.meta.postgres.pg_catalog.Tables;
import org.jooq.meta.postgres.pg_catalog.tables.PgClass;
import org.jooq.meta.postgres.pg_catalog.tables.PgConstraint;
import org.jooq.meta.postgres.pg_catalog.tables.PgIndex;
import org.jooq.meta.postgres.pg_catalog.tables.PgInherits;
import org.jooq.meta.postgres.pg_catalog.tables.PgNamespace;
import org.jooq.meta.postgres.pg_catalog.tables.PgType;
import org.jooq.tools.JooqLogger;

/**
 * Postgres uses the ANSI default INFORMATION_SCHEMA, but unfortunately ships
 * with a non-capitalised version of it: <code>information_schema</code>. Hence
 * the {@link HSQLDBDatabase} is not used here.
 *
 * @author Lukas Eder
 */
public class PostgresDatabase extends AbstractDatabase implements ResultQueryDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(PostgresDatabase.class);

    private Boolean                 is84;
    private Boolean                 is94;
    private Boolean                 is10;
    private Boolean                 is11;
    private Boolean                 is12;
    private Boolean                 canUseRoutines;
    private Boolean                 canCastToEnumType;
    private Boolean                 canCombineArrays;
    private Boolean                 canUseTupleInPredicates;

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        PgIndex i = PG_INDEX.as("i");
        PgClass trel = PG_CLASS.as("trel");
        PgConstraint c = PG_CONSTRAINT.as("c");

        indexLoop:
        for (Record6<String, String, String, Boolean, String[], Integer[]> record : create()
                .select(
                    trel.pgNamespace().NSPNAME,
                    trel.RELNAME,
                    i.indexClass().RELNAME,
                    i.INDISUNIQUE,
                    array(
                        select(field("pg_get_indexdef({0}, k + 1, true)", String.class, i.INDEXRELID))
                        .from("generate_subscripts({0}, 1) as k", i.INDKEY)
                        .orderBy(field("k"))
                    ).as("columns"),
                    field("{0}::int[]", Integer[].class, i.INDOPTION).as("asc_or_desc")
                )
                .from(i)
                .join(trel).on(oid(trel).eq(i.INDRELID))
                .where(trel.pgNamespace().NSPNAME.in(getInputSchemata()))
                .and(getIncludeSystemIndexes()
                    ? noCondition()
                    : row(trel.pgNamespace().NSPNAME, i.indexClass().RELNAME).notIn(
                        select(c.pgNamespace().NSPNAME, c.CONNAME).from(c)
                      ))
                .orderBy(1, 2, 3)) {

            final SchemaDefinition tableSchema = getSchema(record.get(trel.pgNamespace().NSPNAME));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = record.get(i.indexClass().RELNAME);
            final String tableName = record.get(trel.RELNAME);
            final String[] columns = record.value5();
            final Integer[] options = record.value6();
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = record.get(i.INDISUNIQUE);

            for (int k = 0; k < columns.length; k++)

                // [#6310] [#6620] Function-based indexes are not yet supported
                // [#11047]        Even without supporting function-based indexes, we might have to parse
                //                 the column expression, because it might be quoted
                if (table.getColumn(columns[k]) == null && table.getColumn(columns[k] = tryParseColumnName(columns[k])) == null)
                    continue indexLoop;

                // [#10466] Neither are INCLUDE columns, which are reported as
                //          columns without options
                else if (k >= options.length)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (int ordinal = 0; ordinal < columns.length; ordinal++) {
                        ColumnDefinition column = table.getColumn(columns[ordinal]);

                        // [#6307] Some background info on this bitwise operation here:
                        // https://stackoverflow.com/a/18128104/521799
                        SortOrder order = (options[ordinal] & 1) == 1 ? SortOrder.DESC : SortOrder.ASC;

                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            column,
                            order,
                            ordinal + 1
                        ));
                    }
                }

                @Override
                protected List<IndexColumnDefinition> getIndexColumns0() {
                    return indexColumns;
                }
            });
        }

        return result;
    }

    private String tryParseColumnName(String string) {
        try {
            return create()
                .configuration()
                .deriveSettings(s -> s.withParseUnknownFunctions(ParseUnknownFunctions.IGNORE))
                .dsl()
                .parser().parseField(string).getName();
        }
        catch (ParserException e) {
            log.info("Parse error", "Error when parsing column name : " + string, e);
            return string;
        }
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : primaryKeys(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.get(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.get(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                relations.addPrimaryKey(key, table, table.getColumn(columnName));
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : uniqueKeys(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.get(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.get(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                relations.addUniqueKey(key, table, table.getColumn(columnName));
        }
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return keys(schemas, inline("p"));
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return keys(schemas, inline("u"));
    }

    private ResultQuery<Record6<String, String, String, String, String, Integer>> keys(List<String> schemas, Field<String> constraintType) {
        KeyColumnUsage k = KEY_COLUMN_USAGE.as("k");
        PgConstraint c = PG_CONSTRAINT.as("c");

        return create()
            .select(
                k.TABLE_CATALOG,
                k.TABLE_SCHEMA,
                k.TABLE_NAME,
                k.CONSTRAINT_NAME,
                k.COLUMN_NAME,
                k.ORDINAL_POSITION)
            .from(c)
            .join(k)
                .on(k.TABLE_SCHEMA.eq(c.pgClass().pgNamespace().NSPNAME))
                .and(k.TABLE_NAME.eq(c.pgClass().RELNAME))
                .and(k.CONSTRAINT_SCHEMA.eq(c.pgNamespace().NSPNAME))
                .and(k.CONSTRAINT_NAME.eq(c.CONNAME))
            .where(c.CONTYPE.eq(constraintType))
            .and(c.pgNamespace().NSPNAME.in(schemas))
            .orderBy(
                k.TABLE_SCHEMA.asc(),
                k.TABLE_NAME.asc(),
                k.CONSTRAINT_NAME.asc(),
                k.ORDINAL_POSITION.asc());
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {

        // [#3520] PostgreSQL INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS contains incomplete information about foreign keys
        // The (CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME) tuple is non-unique, in case two tables share the
        // same CONSTRAINT_NAME.
        // The JDBC driver implements this correctly through the pg_catalog, although the sorting and column name casing is wrong, too.
        Result<Record> result = create()
            .fetch(getConnection().getMetaData().getExportedKeys(null, null, null))
            .sortAsc("key_seq")
            .sortAsc("fk_name")
            .sortAsc("fktable_name")
            .sortAsc("fktable_schem");

        resultLoop:
        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.get("fktable_schem", String.class));
            SchemaDefinition uniqueKeySchema = getSchema(record.get("pktable_schem", String.class));

            if (foreignKeySchema == null || uniqueKeySchema == null)
                continue resultLoop;

            String foreignKey = record.get("fk_name", String.class);
            String foreignKeyTableName = record.get("fktable_name", String.class);
            String foreignKeyColumn = record.get("fkcolumn_name", String.class);
            String uniqueKey = record.get("pk_name", String.class);
            String uniqueKeyTableName = record.get("pktable_name", String.class);
            String uniqueKeyColumn = record.get("pkcolumn_name", String.class);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
            TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

            if (foreignKeyTable != null && uniqueKeyTable != null)
                relations.addForeignKey(
                    foreignKey,
                    foreignKeyTable,
                    foreignKeyTable.getColumn(foreignKeyColumn),
                    uniqueKey,
                    uniqueKeyTable,
                    uniqueKeyTable.getColumn(uniqueKeyColumn),
                    true
                );
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        CheckConstraints cc = CHECK_CONSTRAINTS.as("cc");

        // [#10940] [#10992] Workaround for issue caused by re-using implicit join paths
        PgConstraint pc = PG_CONSTRAINT.as("pc");

        for (Record record : create()
            .select(
                pc.pgClass().pgNamespace().NSPNAME,
                pc.pgClass().RELNAME,
                pc.CONNAME.as(cc.CONSTRAINT_NAME),
                replace(field("pg_get_constraintdef({0}.oid)", VARCHAR, pc), inline("CHECK "), inline("")).as(cc.CHECK_CLAUSE))
            .from(pc)
            .where(pc.pgClass().pgNamespace().NSPNAME.in(getInputSchemata()))
            .and(pc.CONTYPE.eq(inline("c")))
            .unionAll(
                getIncludeSystemCheckConstraints()
              ? select(
                    pc.pgClass().pgNamespace().NSPNAME,
                    pc.pgClass().RELNAME,
                    cc.CONSTRAINT_NAME,
                    cc.CHECK_CLAUSE
                )
                .from(pc)
                .join(cc)
                .on(pc.CONNAME.eq(cc.CONSTRAINT_NAME))
                .and(pc.pgNamespace().NSPNAME.eq(cc.CONSTRAINT_NAME))
                .where(pc.pgNamespace().NSPNAME.in(getInputSchemata()))
                .and(row(pc.pgClass().pgNamespace().NSPNAME, pc.pgClass().RELNAME, cc.CONSTRAINT_NAME).notIn(
                    select(
                        pc.pgClass().pgNamespace().NSPNAME,
                        pc.pgClass().RELNAME,
                        pc.CONNAME)
                    .from(pc)
                    .where(pc.CONTYPE.eq(inline("c")))
                ))
              : select(inline(""), inline(""), inline(""), inline("")).where(falseCondition()))
            .orderBy(1, 2, 3)
        ) {
            SchemaDefinition schema = getSchema(record.get(pc.pgClass().pgNamespace().NSPNAME));
            TableDefinition table = getTable(schema, record.get(pc.pgClass().RELNAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(cc.CONSTRAINT_NAME),
                    record.get(cc.CHECK_CLAUSE)
                ));
            }
        }
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();
        Map<Name, PostgresTableDefinition> map = new HashMap<>();

        Select<Record6<String, String, String, String, String, String>> empty =
            select(inline(""), inline(""), inline(""), inline(""), inline(""), inline(""))
            .where(falseCondition());

        for (Record record : create()
                .select()
                .from(
                     select(
                        TABLES.TABLE_SCHEMA,
                        TABLES.TABLE_NAME,
                        TABLES.TABLE_NAME.as("specific_name"),
                        PG_DESCRIPTION.DESCRIPTION,
                        when(TABLES.TABLE_TYPE.eq(inline("VIEW")), inline(TableType.VIEW.name()))
                            .else_(inline(TableType.TABLE.name())).as("table_type"),
                        VIEWS.VIEW_DEFINITION)
                    .from(TABLES)
                    .join(PG_NAMESPACE)
                        .on(TABLES.TABLE_SCHEMA.eq(PG_NAMESPACE.NSPNAME))
                    .join(PG_CLASS)
                        .on(PG_CLASS.RELNAME.eq(TABLES.TABLE_NAME))
                        .and(PG_CLASS.RELNAMESPACE.eq(oid(PG_NAMESPACE)))
                    .leftJoin(PG_DESCRIPTION)
                        .on(PG_DESCRIPTION.OBJOID.eq(oid(PG_CLASS)))
                        .and(PG_DESCRIPTION.OBJSUBID.eq(0))
                    .leftJoin(VIEWS)
                        .on(TABLES.TABLE_SCHEMA.eq(VIEWS.TABLE_SCHEMA))
                        .and(TABLES.TABLE_NAME.eq(VIEWS.TABLE_NAME))
                    .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))

                    // To stay on the safe side, if the INFORMATION_SCHEMA ever
                    // includes materialised views, let's exclude them from here
                    .and(canUseTupleInPredicates()
                        ? row(TABLES.TABLE_SCHEMA, TABLES.TABLE_NAME).notIn(
                            select(
                                PG_NAMESPACE.NSPNAME,
                                PG_CLASS.RELNAME)
                            .from(PG_CLASS)
                            .join(PG_NAMESPACE)
                                .on(PG_CLASS.RELNAMESPACE.eq(oid(PG_NAMESPACE)))
                            .where(PG_CLASS.RELKIND.eq(inline("m"))))
                        : noCondition()
                    )

                // [#3254] Materialised views are reported only in PG_CLASS, not
                //         in INFORMATION_SCHEMA.TABLES
                // [#8478] CockroachDB cannot compare "sql_identifier" types (varchar)
                //         from information_schema with "name" types from pg_catalog
                .unionAll(
                    select(
                        field("{0}::varchar", PG_NAMESPACE.NSPNAME.getDataType(), PG_NAMESPACE.NSPNAME),
                        field("{0}::varchar", PG_CLASS.RELNAME.getDataType(), PG_CLASS.RELNAME),
                        field("{0}::varchar", PG_CLASS.RELNAME.getDataType(), PG_CLASS.RELNAME),
                        PG_DESCRIPTION.DESCRIPTION,
                        inline(TableType.MATERIALIZED_VIEW.name()).as("table_type"),
                        inline(""))
                    .from(PG_CLASS)
                    .join(PG_NAMESPACE)
                        .on(PG_CLASS.RELNAMESPACE.eq(oid(PG_NAMESPACE)))
                    .leftOuterJoin(PG_DESCRIPTION)
                        .on(PG_DESCRIPTION.OBJOID.eq(oid(PG_CLASS)))
                        .and(PG_DESCRIPTION.OBJSUBID.eq(0))
                    .where(PG_NAMESPACE.NSPNAME.in(getInputSchemata()))
                    .and(PG_CLASS.RELKIND.eq(inline("m"))))

                // [#3375] [#3376] Include table-valued functions in the set of tables
                .unionAll(
                    tableValuedFunctions()

                    ?   select(
                            ROUTINES.ROUTINE_SCHEMA,
                            ROUTINES.ROUTINE_NAME,
                            ROUTINES.SPECIFIC_NAME,
                            inline(""),
                            inline(TableType.FUNCTION.name()).as("table_type"),
                            inline(""))
                        .from(ROUTINES)
                        .join(PG_NAMESPACE).on(ROUTINES.SPECIFIC_SCHEMA.eq(PG_NAMESPACE.NSPNAME))
                        .join(PG_PROC).on(PG_PROC.PRONAMESPACE.eq(oid(PG_NAMESPACE)))
                                      .and(PG_PROC.PRONAME.concat("_").concat(oid(PG_PROC)).eq(ROUTINES.SPECIFIC_NAME))
                        .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
                        .and(PG_PROC.PRORETSET)

                    :   empty)
                .asTable("tables"))
                .orderBy(1, 2)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(TABLES.TABLE_SCHEMA));
            String name = record.get(TABLES.TABLE_NAME);
            String comment = record.get(PG_DESCRIPTION.DESCRIPTION, String.class);
            TableType tableType = record.get("table_type", TableType.class);
            String source = record.get(VIEWS.VIEW_DEFINITION);

            if (source != null && !source.toLowerCase().startsWith("create"))
                source = "create view \"" + name + "\" as " + source;

            switch (tableType) {
                case FUNCTION:
                    result.add(new PostgresTableValuedFunction(schema, name, record.get(ROUTINES.SPECIFIC_NAME), comment));
                    break;
                case MATERIALIZED_VIEW:
                    result.add(new PostgresMaterializedViewDefinition(schema, name, comment));
                    break;
                default:
                    PostgresTableDefinition t = new PostgresTableDefinition(schema, name, comment, tableType, source);
                    result.add(t);
                    map.put(name(schema.getName(), name), t);
                    break;
            }
        }

        PgClass ct = PG_CLASS.as("ct");
        PgNamespace cn = PG_NAMESPACE.as("cn");
        PgInherits i = PG_INHERITS.as("i");
        PgClass pt = PG_CLASS.as("pt");
        PgNamespace pn = PG_NAMESPACE.as("pn");

        // [#2916] If window functions are not supported (prior to PostgreSQL 8.4), then
        // don't execute the following query:
        if (is84()) {
            for (Record5<String, String, String, String, Integer> inheritance : create()
                    .select(
                        cn.NSPNAME,
                        ct.RELNAME,
                        pn.NSPNAME,
                        pt.RELNAME,
                        max(i.INHSEQNO).over().partitionBy(i.INHRELID).as("m")
                    )
                    .from(ct)
                    .join(cn).on(ct.RELNAMESPACE.eq(oid(cn)))
                    .join(i).on(i.INHRELID.eq(oid(ct)))
                    .join(pt).on(i.INHPARENT.eq(oid(pt)))
                    .join(pn).on(pt.RELNAMESPACE.eq(oid(pn)))
                    .where(cn.NSPNAME.in(getInputSchemata()))
                    .and(pn.NSPNAME.in(getInputSchemata()))
                    .fetch()) {

                Name child = name(inheritance.value1(), inheritance.value2());
                Name parent = name(inheritance.value3(), inheritance.value4());

                if (inheritance.value5() > 1) {
                    log.info("Multiple inheritance",
                        "Multiple inheritance is not supported by jOOQ: " +
                        child +
                        " inherits from " +
                        parent);
                }
                else {
                    PostgresTableDefinition childTable = map.get(child);
                    PostgresTableDefinition parentTable = map.get(parent);

                    if (childTable != null && parentTable != null) {
                        childTable.setParentTable(parentTable);
                        parentTable.getChildTables().add(childTable);
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {

        // [#1409] Shouldn't select from INFORMATION_SCHEMA.SCHEMATA, as that
        // would only return schemata of which CURRENT_USER is the owner
        return
        create().select(PG_NAMESPACE.NSPNAME)
                .from(PG_NAMESPACE)
                .orderBy(PG_NAMESPACE.NSPNAME)
                .collect(mapping(r -> new SchemaDefinition(this, r.value1(), ""), toList()));
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        CommonTableExpression<Record1<String>> s = name("schemas").fields("schema").as(selectFrom(values(schemas.stream().collect(toRowArray(DSL::val)))));

        return create()
            .with(s)
            .select(
                inline(null, VARCHAR).as("catalog"),
                SEQUENCES.SEQUENCE_SCHEMA,
                SEQUENCES.SEQUENCE_NAME,
                SEQUENCES.DATA_TYPE,
                SEQUENCES.NUMERIC_PRECISION,
                SEQUENCES.NUMERIC_SCALE,
                nullif(SEQUENCES.START_VALUE.cast(BIGINT), inline(1L)).as(SEQUENCES.START_VALUE),
                nullif(SEQUENCES.INCREMENT.cast(BIGINT), inline(1L)).as(SEQUENCES.INCREMENT),
                nullif(SEQUENCES.MINIMUM_VALUE.cast(BIGINT), inline(1L)).coerce(NUMERIC).as(SEQUENCES.MINIMUM_VALUE),
                nullif(SEQUENCES.MAXIMUM_VALUE.cast(DECIMAL_INTEGER),
                    power(cast(inline(2L), DECIMAL_INTEGER), cast(SEQUENCES.NUMERIC_PRECISION.minus(inline(1L)), DECIMAL_INTEGER)).minus(inline(1L))).coerce(NUMERIC).as(SEQUENCES.MAXIMUM_VALUE),
                SEQUENCES.CYCLE_OPTION.cast(BOOLEAN).as(SEQUENCES.CYCLE_OPTION),
                inline(null, BIGINT).as("cache"))
            .from(SEQUENCES)
            .where(SEQUENCES.SEQUENCE_SCHEMA.in(selectFrom(s)))
            .and(!getIncludeSystemSequences()
                ? row(SEQUENCES.SEQUENCE_SCHEMA, SEQUENCES.SEQUENCE_NAME).notIn(
                    select(COLUMNS.TABLE_SCHEMA, COLUMNS.TABLE_NAME.concat(inline("_")).concat(COLUMNS.COLUMN_NAME).concat(inline("_seq")))
                    .from(COLUMNS)
                    .where(COLUMNS.COLUMN_DEFAULT.eq(
                        inline("nextval('").concat(COLUMNS.TABLE_NAME.concat(inline("_")).concat(COLUMNS.COLUMN_NAME)).concat(inline("_seq'::regclass)"))
                    ))
                    .or(COLUMNS.COLUMN_DEFAULT.eq(
                        inline("nextval('").concat(COLUMNS.TABLE_SCHEMA.concat(inline(".")).concat(COLUMNS.TABLE_NAME).concat(inline("_")).concat(COLUMNS.COLUMN_NAME)).concat(inline("_seq'::regclass)"))
                    ))
                  )
                : noCondition())
            .unionAll(is10() && getIncludeSystemSequences()
            ?   select(
                    inline(null, VARCHAR),
                    PG_SEQUENCE.pgClass().pgNamespace().NSPNAME,
                    PG_SEQUENCE.pgClass().RELNAME,
                    PG_SEQUENCE.pgType().TYPNAME,

                    // See https://github.com/postgres/postgres/blob/master/src/backend/catalog/information_schema.sql
                    field("information_schema._pg_numeric_precision({0}, {1})", INTEGER, PG_SEQUENCE.pgType().TYPBASETYPE, PG_SEQUENCE.pgType().TYPTYPMOD),
                    inline(0),
                    PG_SEQUENCE.SEQSTART,
                    PG_SEQUENCE.SEQINCREMENT,
                    PG_SEQUENCE.SEQMIN.coerce(NUMERIC),
                    PG_SEQUENCE.SEQMAX.coerce(NUMERIC),
                    PG_SEQUENCE.SEQCYCLE,
                    inline(null, BIGINT).as("cache"))
                .from(PG_SEQUENCE)
                .where(PG_SEQUENCE.pgClass().pgNamespace().NSPNAME.in(selectFrom(s)))
                .and(PG_SEQUENCE.pgClass().OID.in(
                    select(PG_DEPEND.OBJID)
                    .from(PG_DEPEND)
                    .where(PG_DEPEND.DEPTYPE.eq(inline("i")))
                    .and(PG_DEPEND.CLASSID.eq(field("'pg_class'::regclass", PG_DEPEND.CLASSID.getDataType())))
                ))
                // AND NOT EXISTS (SELECT 1 FROM pg_depend WHERE classid = 'pg_class'::regclass AND objid = c.oid AND deptype = 'i')
            :   select(inline(""), inline(""), inline(""), inline(""), inline(0), inline(0), inline(0L), inline(0L), inline(BigDecimal.ZERO), inline(BigDecimal.ZERO), inline(false), inline(0L))
                .where(falseCondition()))
            .orderBy(2, 3);
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : sequences(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(SEQUENCES.SEQUENCE_SCHEMA));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema,
                record.get(SEQUENCES.DATA_TYPE),
                0,
                record.get(SEQUENCES.NUMERIC_PRECISION),
                record.get(SEQUENCES.NUMERIC_SCALE),
                false,
                (String) null
            );

            result.add(new DefaultSequenceDefinition(
                schema,
                record.get(SEQUENCES.SEQUENCE_NAME),
                type,
                null,
                record.get(SEQUENCES.START_VALUE, BigInteger.class),
                record.get(SEQUENCES.INCREMENT, BigInteger.class),
                record.get(SEQUENCES.MINIMUM_VALUE, BigInteger.class),
                record.get(SEQUENCES.MAXIMUM_VALUE, BigInteger.class),
                record.get(SEQUENCES.CYCLE_OPTION, Boolean.class),
                null // [#9442] The CACHE flag is not available from SEQUENCES
            ));
        }

        return result;
    }

    static final /* record */ class Identifier { private final String schema; private final String name; public Identifier(String schema, String name) { this.schema = schema; this.name = name; } public String schema() { return schema; } public String name() { return name; } @Override public boolean equals(Object o) { if (!(o instanceof Identifier)) return false; Identifier other = (Identifier) o; if (!java.util.Objects.equals(this.schema, other.schema)) return false; if (!java.util.Objects.equals(this.name, other.name)) return false; return true; } @Override public int hashCode() { return java.util.Objects.hash(this.schema, this.name); } @Override public String toString() { return new StringBuilder("Identifier[").append("schema=").append(this.schema).append(", name=").append(this.name).append("]").toString(); } }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();

        // [#2736] This table is unavailable in Amazon Redshift
        if (exists(PG_ENUM)) {

            // [#2707] Fetch all enum type names first, in order to be able to
            // perform enumlabel::[typname] casts in the subsequent query for
            // cross-version compatible enum literal ordering
            for (Identifier type : create()
                    .select(
                        PG_TYPE.pgNamespace().NSPNAME,
                        PG_TYPE.TYPNAME)
                    .from(PG_TYPE)
                    .where(PG_TYPE.pgNamespace().NSPNAME.in(getInputSchemata()))
                    .and(oid(PG_TYPE).in(select(PG_ENUM.ENUMTYPID).from(PG_ENUM)))
                    .orderBy(
                        PG_TYPE.pgNamespace().NSPNAME,
                        PG_TYPE.TYPNAME)
                    .fetch(mapping(Identifier::new))) {
                DefaultEnumDefinition definition = null;

                for (String label : enumLabels(type.schema, type.name)) {
                    SchemaDefinition schema = getSchema(type.schema);
                    String typeName = String.valueOf(type.name);

                    if (definition == null || !definition.getName().equals(typeName)) {
                        definition = new DefaultEnumDefinition(schema, typeName, null);
                        result.add(definition);
                    }

                    definition.addLiteral(label);
                }
            }
        }

        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<>();

        if (existAll(PG_CONSTRAINT, PG_TYPE)) {
            PgNamespace n = PG_NAMESPACE.as("n");
            PgConstraint c = PG_CONSTRAINT.as("c");
            PgType d = PG_TYPE.as("d");
            PgType b = PG_TYPE.as("b");

            Field<String[]> src = field(name("domains", "src"), String[].class);
            Field<String> constraintDef = field("pg_get_constraintdef({0})", VARCHAR, oid(c));

            for (Record record : create()
                    .withRecursive("domains",
                        "domain_id",
                        "base_id",
                        "typbasetype",
                        "src"
                    )
                    .as(
                         select(
                             oid(d),
                             oid(d),
                             d.TYPBASETYPE,
                             when(oid(c).isNotNull(), array(constraintDef))
                         )
                        .from(d)
                        .join(n)
                            .on(oid(n).eq(d.TYPNAMESPACE))
                        .leftJoin(c)
                            .on(oid(d).eq(c.CONTYPID))
                        .where(d.TYPTYPE.eq("d"))
                        .and(n.NSPNAME.in(getInputSchemata()))
                    .unionAll(
                         select(
                             field(name("domains", "domain_id"), Long.class),
                             oid(d),
                             d.TYPBASETYPE,
                             decode()
                                 .when(c.CONBIN.isNull(), src)
                                 .otherwise(arrayAppend(src, constraintDef))
                         )
                        .from(name("domains"))
                        .join(d)
                            .on(field(name("domains", d.TYPBASETYPE.getName())).eq(oid(d)))
                        .leftJoin(c)
                            .on(oid(d).eq(c.CONTYPID))
                    ))
                    .select(
                        n.NSPNAME,
                        d.TYPNAME,
                        d.TYPNOTNULL,
                        d.TYPDEFAULT,
                        b.TYPNAME,

                        // See https://github.com/postgres/postgres/blob/master/src/backend/catalog/information_schema.sql
                        field("information_schema._pg_char_max_length({0}, {1})", INTEGER, d.TYPBASETYPE, d.TYPTYPMOD).as(DOMAINS.CHARACTER_MAXIMUM_LENGTH),
                        field("information_schema._pg_numeric_precision({0}, {1})", INTEGER, d.TYPBASETYPE, d.TYPTYPMOD).as(DOMAINS.NUMERIC_PRECISION),
                        field("information_schema._pg_numeric_scale({0}, {1})", INTEGER, d.TYPBASETYPE, d.TYPTYPMOD).as(DOMAINS.NUMERIC_SCALE),
                        src)
                    .from(d)
                    .join(name("domains"))
                        .on(field(name("domains", "typbasetype")).eq(0))
                        .and(field(name("domains", "domain_id")).eq(oid(d)))
                    .join(b)
                        .on(field(name("domains", "base_id")).eq(oid(b)))
                    .join(n)
                        .on(oid(n).eq(d.TYPNAMESPACE))
                    .where(d.TYPTYPE.eq("d"))
                    .and(n.NSPNAME.in(getInputSchemata()))
                    .orderBy(n.NSPNAME, d.TYPNAME)) {

                SchemaDefinition schema = getSchema(record.get(n.NSPNAME));

                DataTypeDefinition baseType = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    record.get(b.TYPNAME),
                    record.get(DOMAINS.CHARACTER_MAXIMUM_LENGTH),
                    record.get(DOMAINS.NUMERIC_PRECISION),
                    record.get(DOMAINS.NUMERIC_SCALE),
                   !record.get(d.TYPNOTNULL, boolean.class),
                    record.get(d.TYPDEFAULT),
                    name(
                        record.get(n.NSPNAME),
                        record.get(b.TYPNAME)
                    )
                );

                DefaultDomainDefinition domain = new DefaultDomainDefinition(
                    schema,
                    record.get(d.TYPNAME),
                    baseType
                );

                domain.addCheckClause(record.get(src));
                result.add(domain);
            }
        }

        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<>();

        // [#2736] This table is unavailable in Amazon Redshift
        if (exists(ATTRIBUTES)) {
            for (Identifier udt : create()
                    .selectDistinct(
                        ATTRIBUTES.UDT_SCHEMA,
                        ATTRIBUTES.UDT_NAME)
                    .from(ATTRIBUTES)
                    .where(ATTRIBUTES.UDT_SCHEMA.in(getInputSchemata()))
                    .orderBy(
                        ATTRIBUTES.UDT_SCHEMA,
                        ATTRIBUTES.UDT_NAME)
                    .fetch(mapping(Identifier::new))) {

                SchemaDefinition schema = getSchema(udt.schema);

                if (schema != null)
                    result.add(new PostgresUDTDefinition(schema, udt.name, null));
            }
        }

        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<>();

        if (!canUseRoutines())
            return result;

        Routines r1 = ROUTINES.as("r1");

        // [#7785] The pg_proc.proisagg column has been replaced incompatibly in PostgreSQL 11
        Field<Boolean> isAgg = (is11()
            ? field(PG_PROC.PROKIND.eq(inline("a")))
            : field("{0}.proisagg", SQLDataType.BOOLEAN, PG_PROC)
        ).as("is_agg");

        return
        create().select(
                r1.ROUTINE_SCHEMA,
                r1.ROUTINE_NAME,
                r1.SPECIFIC_NAME,

                // Ignore the data type when there is at least one out parameter
                canCombineArrays()
                    ? when(condition("{0} && ARRAY['o','b']::\"char\"[]", PG_PROC.PROARGMODES), inline("void"))
                     .otherwise(r1.DATA_TYPE).as("data_type")
                    : r1.DATA_TYPE.as("data_type"),

                r1.CHARACTER_MAXIMUM_LENGTH,

                // [#12048] TODO: Maintain whether we know the precision or not
                when(r1.NUMERIC_PRECISION.isNull().and(r1.DATA_TYPE.in(
                    inline("time"),
                    inline("timetz"),
                    inline("time without time zone"),
                    inline("time with time zone"),
                    inline("timestamp"),
                    inline("timestamptz"),
                    inline("timestamp without time zone"),
                    inline("timestamp with time zone"))), inline(6))
                .else_(r1.NUMERIC_PRECISION).as(r1.NUMERIC_PRECISION),
                r1.NUMERIC_SCALE,
                r1.TYPE_UDT_SCHEMA,
                r1.TYPE_UDT_NAME,

                // Calculate overload index if applicable
                when(
                    count().over(partitionBy(r1.ROUTINE_SCHEMA, r1.ROUTINE_NAME)).gt(one()),
                    rowNumber().over(partitionBy(r1.ROUTINE_SCHEMA, r1.ROUTINE_NAME).orderBy(

                        // [#9754] To stabilise overload calculation, we use the type signature
                        // replace(field("pg_get_function_arguments({0})", VARCHAR, oid(PG_PROC)), inline('"'), inline("")),
                        r1.SPECIFIC_NAME
                    ))
                ).as("overload"),

                isAgg)
            .from(r1)

            // [#3375] Exclude table-valued functions as they're already generated as tables
            .join(PG_NAMESPACE).on(PG_NAMESPACE.NSPNAME.eq(r1.SPECIFIC_SCHEMA))
            .join(PG_PROC).on(PG_PROC.PRONAMESPACE.eq(oid(PG_NAMESPACE)))
                          .and(is12()
                              ? condition("nameconcatoid({0}, {1}) = {2}", PG_PROC.PRONAME, oid(PG_PROC), r1.SPECIFIC_NAME)
                              : PG_PROC.PRONAME.concat("_").concat(oid(PG_PROC)).eq(r1.SPECIFIC_NAME))
            .where(r1.ROUTINE_SCHEMA.in(getInputSchemata()))
            .and(tableValuedFunctions()
                    ? condition(not(PG_PROC.PRORETSET))
                    : noCondition())
            .and(!getIncludeTriggerRoutines()
                    ? r1.DATA_TYPE.ne(inline("trigger"))
                    : noCondition())
            .orderBy(
                r1.ROUTINE_SCHEMA.asc(),
                r1.ROUTINE_NAME.asc(),
                field(name("overload")).asc())
            .collect(mapping(r -> new PostgresRoutineDefinition(this, r), Collectors.<RoutineDefinition>toList()));
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.POSTGRES);
    }

    boolean is84() {
        if (is84 == null) {
            is84 = configuredDialectIsNotFamilyAndSupports(asList(POSTGRES), () -> {

                // [#2916] Window functions were introduced with PostgreSQL 9.0
                try {
                    create(true)
                        .select(count().over())
                        .fetch();

                    return true;
                }
                catch (DataAccessException e) {
                    return false;
                }
            });
        }

        return is84;
    }

    boolean is94() {

        // [#4254] INFORMATION_SCHEMA.PARAMETERS.PARAMETER_DEFAULT was added
        // in PostgreSQL 9.4 only
        if (is94 == null)
            is94 = configuredDialectIsNotFamilyAndSupports(asList(POSTGRES), () -> exists(PARAMETERS.PARAMETER_DEFAULT));

        return is94;
    }

    boolean is10() {

        // [#7785] pg_sequence was added in PostgreSQL 10 only
        if (is10 == null)
            is10 = configuredDialectIsNotFamilyAndSupports(asList(POSTGRES), () -> exists(PG_SEQUENCE.SEQRELID));

        return is10;
    }

    boolean is11() {

        // [#7785] pg_proc.prokind was added in PostgreSQL 11 only, and
        //         pg_proc.proisagg was removed, incompatibly
        if (is11 == null)
            is11 = configuredDialectIsNotFamilyAndSupports(asList(POSTGRES), () -> exists(PG_PROC.PROKIND));

        return is11;
    }

    boolean is12() {

        // [#11325] nameconcatoid was added in PostgreSQL 12 only
        if (is12 == null)
            is12 = configuredDialectIsNotFamilyAndSupports(asList(POSTGRES), () -> exists(table(select(field("nameconcatoid({0}, {1})", PG_PROC.PRONAME, oid(PG_PROC))).from(PG_PROC))));

        return is12;
    }

    @Override
    protected boolean exists0(TableField<?, ?> field) {
        return exists1(field, COLUMNS, COLUMNS.TABLE_SCHEMA, COLUMNS.TABLE_NAME, COLUMNS.COLUMN_NAME);
    }

    @Override
    protected boolean exists0(Table<?> table) {
        return exists1(table, TABLES, TABLES.TABLE_SCHEMA, TABLES.TABLE_NAME);
    }






































    boolean canCombineArrays() {
        if (canCombineArrays == null) {

            // [#7270] The ARRAY && ARRAY operator is not implemented in all PostgreSQL
            //         style databases, e.g. CockroachDB
            try {
                create(true).select(field("array[1, 2] && array[2, 3]")).fetch();
                canCombineArrays = true;
            }
            catch (DataAccessException e) {
                canCombineArrays = false;
            }
        }

        return canCombineArrays;
    }

    boolean canUseTupleInPredicates() {
        if (canUseTupleInPredicates == null) {

            // [#7270] The tuple in predicate is not implemented in all PostgreSQL
            //         style databases, e.g. CockroachDB
            // [#8072] Some database versions might support in, but not not in
            try {
                create(true).select(field("(1, 2) in (select 1, 2)")).fetch();
                create(true).select(field("(1, 2) not in (select 1, 2)")).fetch();
                canUseTupleInPredicates = true;
            }
            catch (DataAccessException e) {
                canUseTupleInPredicates = false;
            }
        }

        return canUseTupleInPredicates;
    }

    boolean canUseRoutines() {

        // [#7892] The information_schema.routines table is not available in all PostgreSQL
        //         style databases, e.g. CockroachDB
        if (canUseRoutines == null)
            canUseRoutines = exists(ROUTINES);

        return canUseRoutines;
    }

    private List<String> enumLabels(String nspname, String typname) {

        // [#10821] Avoid the cast if pg_enum.enumsortorder is available (PG 9.1+)
        Field<?> orderBy =
            exists(PG_ENUM.ENUMSORTORDER)
          ? PG_ENUM.ENUMSORTORDER

        // [#9511] [#9917] Workaround for regression introduced by avoiding quoting names everywhere
          : field("{0}::{1}", PG_ENUM.ENUMLABEL, sql(name(nspname, typname) + ""));

        if (canCastToEnumType == null) {

            // [#2917] Older versions of PostgreSQL don't support the above cast
            try {
                canCastToEnumType = true;
                return enumLabels(nspname, typname, orderBy);
            }
            catch (DataAccessException e) {
                canCastToEnumType = false;
            }
        }

        return canCastToEnumType ? enumLabels(nspname, typname, orderBy) : enumLabels(nspname, typname, PG_ENUM.ENUMLABEL);
    }

    private List<String> enumLabels(String nspname, String typname, Field<?> orderBy) {
        return
        create().select(PG_ENUM.ENUMLABEL)
                .from(PG_ENUM)
                .where(PG_ENUM.pgType().pgNamespace().NSPNAME.eq(nspname))
                .and(PG_ENUM.pgType().TYPNAME.eq(typname))
                .orderBy(orderBy)
                .collect(intoList());
    }
}
