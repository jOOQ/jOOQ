/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

package org.jooq.meta.mysql;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.Records.mapping;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.length;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.regexpReplaceAll;
import static org.jooq.impl.DSL.regexpReplaceFirst;
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.CHAR;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.mysql.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.COLUMNS;
import static org.jooq.meta.mysql.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.meta.mysql.information_schema.Tables.PARAMETERS;
import static org.jooq.meta.mysql.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.ROUTINES;
import static org.jooq.meta.mysql.information_schema.Tables.SCHEMATA;
import static org.jooq.meta.mysql.information_schema.Tables.STATISTICS;
import static org.jooq.meta.mysql.information_schema.Tables.TABLES;
import static org.jooq.meta.mysql.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.TRIGGERS;
import static org.jooq.meta.mysql.information_schema.Tables.VIEWS;
import static org.jooq.meta.mysql.mysql.Tables.PROC;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record14;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions.TableType;
// ...
// ...
// ...
import org.jooq.impl.DSL;
import org.jooq.impl.QOM.ForeignKeyRule;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultEnumDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
// ...
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
// ...
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.XMLSchemaCollectionDefinition;
import org.jooq.meta.mariadb.MariaDBDatabase;
import org.jooq.meta.mysql.information_schema.tables.Columns;
import org.jooq.meta.mysql.information_schema.tables.Triggers;
import org.jooq.meta.mysql.mysql.enums.ProcType;
import org.jooq.tools.csv.CSVReader;

import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
public class MySQLDatabase extends AbstractDatabase implements ResultQueryDatabase {

    private Boolean is5_5;
    private Boolean is5_7;
    private Boolean is8;
    private Boolean is8_0_16;

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        // Same implementation as in H2Database and HSQLDBDatabase
        Map<Record, Result<Record>> indexes = create()
            // [#2059] In MemSQL primary key indexes are typically duplicated
            // (once with INDEX_TYPE = 'SHARD' and once with INDEX_TYPE = 'BTREE)
            .selectDistinct(
                STATISTICS.TABLE_SCHEMA,
                STATISTICS.TABLE_NAME,
                STATISTICS.INDEX_NAME,
                STATISTICS.NON_UNIQUE,
                STATISTICS.COLUMN_NAME,
                STATISTICS.SEQ_IN_INDEX)
            .from(STATISTICS)
            .where(STATISTICS.TABLE_SCHEMA.in(workaroundFor5213(getInputSchemata())))
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : row(STATISTICS.INDEX_SCHEMA, STATISTICS.TABLE_NAME, STATISTICS.INDEX_NAME).notIn(
                    select(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA, TABLE_CONSTRAINTS.TABLE_NAME, TABLE_CONSTRAINTS.CONSTRAINT_NAME)
                    .from(TABLE_CONSTRAINTS)
                  )
            )
            .orderBy(
                STATISTICS.TABLE_SCHEMA,
                STATISTICS.TABLE_NAME,
                STATISTICS.INDEX_NAME,
                STATISTICS.SEQ_IN_INDEX)
            .fetchGroups(
                new Field[] {
                    STATISTICS.TABLE_SCHEMA,
                    STATISTICS.TABLE_NAME,
                    STATISTICS.INDEX_NAME,
                    STATISTICS.NON_UNIQUE
                },
                new Field[] {
                    STATISTICS.COLUMN_NAME,
                    STATISTICS.SEQ_IN_INDEX
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();

            final SchemaDefinition tableSchema = getSchema(index.get(STATISTICS.TABLE_SCHEMA));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(STATISTICS.INDEX_NAME);
            final String tableName = index.get(STATISTICS.TABLE_NAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = !index.get(STATISTICS.NON_UNIQUE, boolean.class);

            // [#6310] [#6620] Function-based indexes are not yet supported
            // [#16237]        Alternatively, the column could be hidden or excluded
            for (Record column : columns)
                if (table.getColumn(column.get(STATISTICS.COLUMN_NAME)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(STATISTICS.COLUMN_NAME)),
                            SortOrder.ASC,
                            column.get(STATISTICS.SEQ_IN_INDEX, int.class)
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

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : primaryKeys(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(STATISTICS.TABLE_SCHEMA));
            String constraintName = record.get(STATISTICS.INDEX_NAME);
            String tableName = record.get(STATISTICS.TABLE_NAME);
            String columnName = record.get(STATISTICS.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null)
                relations.addPrimaryKey(key, table, table.getColumn(columnName));
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : uniqueKeys(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(STATISTICS.TABLE_SCHEMA));
            String constraintName = record.get(STATISTICS.INDEX_NAME);
            String tableName = record.get(STATISTICS.TABLE_NAME);
            String columnName = record.get(STATISTICS.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null)
                relations.addUniqueKey(key, table, table.getColumn(columnName));
        }
    }

    private String getKeyName(String tableName, String keyName) {
        return "KEY_" + tableName + "_" + keyName;
    }

    protected boolean is8() {

        // [#6602] The mysql.proc table got removed in MySQL 8.0
        if (is8 == null)
            is8 = configuredDialectIsNotFamilyAndSupports(asList(MYSQL), () -> !exists(PROC));

        return is8;
    }

    protected boolean is8_0_16() {

        // [#7639] The information_schema.check_constraints table was added in MySQL 8.0.16 only
        if (is8_0_16 == null)
            is8_0_16 = configuredDialectIsNotFamilyAndSupports(asList(MYSQL), () -> exists(CHECK_CONSTRAINTS))
                    || configuredDialectIsNotFamilyAndSupports(asList(MARIADB), () -> exists(CHECK_CONSTRAINTS));

        return is8_0_16;
    }

    protected boolean is5_7() {

        // [#14598] The information_schema.columns.GENERATION_EXPRESSION column was added in MySQL 5.7 only
        if (is5_7 == null)
            is5_7 = configuredDialectIsNotFamilyAndSupports(asList(MYSQL), () -> exists(COLUMNS.GENERATION_EXPRESSION))
                 || configuredDialectIsNotFamilyAndSupports(asList(MARIADB), () -> exists(COLUMNS.GENERATION_EXPRESSION));

        return is5_7;
    }

    protected boolean is5_5() {

        // Check if this is a MySQL 5.5 or later database
        if (is5_5 == null) {
            try {
                create().selectOne().from(PARAMETERS).limit(1).fetchOne();
                is5_5 = true;
            }
            catch (Exception e) {
                is5_5 = false;
            }
        }

        return is5_5;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return keys(schemas, true);
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return keys(schemas, false);
    }

    private ResultQuery<Record6<String, String, String, String, String, Integer>> keys(List<String> inputSchemata, boolean primary) {

        // [#3560] It has been shown that querying the STATISTICS table is much faster on
        // very large databases than going through TABLE_CONSTRAINTS and KEY_COLUMN_USAGE
        // [#2059] In MemSQL primary key indexes are typically duplicated
        // (once with INDEX_TYPE = 'SHARD' and once with INDEX_TYPE = 'BTREE)
        return create().selectDistinct(

                           // Don't use the actual catalog value, which is meaningless.
                           // Besides, MetaImpl will rely on the TABLE_SCHEMA acting as the catalog.
                           inline(null, STATISTICS.TABLE_CATALOG).as(STATISTICS.TABLE_CATALOG),
                           STATISTICS.TABLE_SCHEMA,
                           STATISTICS.TABLE_NAME,
                           STATISTICS.INDEX_NAME,
                           STATISTICS.COLUMN_NAME,
                           STATISTICS.SEQ_IN_INDEX.coerce(INTEGER))
                       .from(STATISTICS)
                       .where(STATISTICS.TABLE_SCHEMA.in(workaroundFor5213(inputSchemata)))
                       .and(primary
                           ? STATISTICS.INDEX_NAME.eq(inline("PRIMARY"))
                           : STATISTICS.INDEX_NAME.ne(inline("PRIMARY")).and(STATISTICS.NON_UNIQUE.eq(inline(0))))
                       .orderBy(
                           STATISTICS.TABLE_SCHEMA,
                           STATISTICS.TABLE_NAME,
                           STATISTICS.INDEX_NAME,
                           STATISTICS.SEQ_IN_INDEX);
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create()
                .select(
                    REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA,
                    REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME,
                    REFERENTIAL_CONSTRAINTS.TABLE_NAME,
                    REFERENTIAL_CONSTRAINTS.REFERENCED_TABLE_NAME,
                    REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME,
                    REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA,
                    replace(REFERENTIAL_CONSTRAINTS.DELETE_RULE, inline(" "), inline("_")).as(REFERENTIAL_CONSTRAINTS.DELETE_RULE),
                    replace(REFERENTIAL_CONSTRAINTS.UPDATE_RULE, inline(" "), inline("_")).as(REFERENTIAL_CONSTRAINTS.UPDATE_RULE),
                    KEY_COLUMN_USAGE.COLUMN_NAME)
                .from(REFERENTIAL_CONSTRAINTS)
                .join(KEY_COLUMN_USAGE)
                .on(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA.equal(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA))
                .and(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME.equal(KEY_COLUMN_USAGE.CONSTRAINT_NAME))
                .and(REFERENTIAL_CONSTRAINTS.TABLE_NAME.equal(KEY_COLUMN_USAGE.TABLE_NAME))
                .where(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA.in(workaroundFor5213(getInputSchemata())))
                .orderBy(
                    KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA.asc(),
                    KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                    KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
        ) {
            SchemaDefinition foreignKeySchema = getSchema(record.get(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.get(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME);
            String foreignKeyColumn = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);
            String foreignKeyTableName = record.get(REFERENTIAL_CONSTRAINTS.TABLE_NAME);
            String uniqueKey = record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME);
            String uniqueKeyTableName = record.get(REFERENTIAL_CONSTRAINTS.REFERENCED_TABLE_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
            TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

            ForeignKeyRule deleteRule = record.get(REFERENTIAL_CONSTRAINTS.DELETE_RULE, ForeignKeyRule.class);
            ForeignKeyRule updateRule = record.get(REFERENTIAL_CONSTRAINTS.UPDATE_RULE, ForeignKeyRule.class);

            if (foreignKeyTable != null)
                relations.addForeignKey(
                    foreignKey,
                    foreignKeyTable,
                    foreignKeyTable.getColumn(foreignKeyColumn),
                    getKeyName(uniqueKeyTableName, uniqueKey),
                    uniqueKeyTable,
                    true,
                    deleteRule,
                    updateRule
                );
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        if (is8_0_16()) {
            for (Record record : create()
                    .select(
                        TABLE_CONSTRAINTS.TABLE_SCHEMA,
                        TABLE_CONSTRAINTS.TABLE_NAME,
                        CHECK_CONSTRAINTS.CONSTRAINT_NAME,
                        CHECK_CONSTRAINTS.CHECK_CLAUSE,

                        // We need this additional, useless projection. See:
                        // https://jira.mariadb.org/browse/MDEV-21201
                        TABLE_CONSTRAINTS.CONSTRAINT_CATALOG,
                        TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA
                     )
                    .from(TABLE_CONSTRAINTS)
                    .join(CHECK_CONSTRAINTS)
                    .using(this instanceof MariaDBDatabase
                        ? new Field[] {
                            TABLE_CONSTRAINTS.CONSTRAINT_CATALOG,
                            TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA,
                            // MariaDB has this column, but not MySQL
                            TABLE_CONSTRAINTS.TABLE_NAME,
                            TABLE_CONSTRAINTS.CONSTRAINT_NAME
                        }
                        : new Field[] {
                            TABLE_CONSTRAINTS.CONSTRAINT_CATALOG,
                            TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA,
                            TABLE_CONSTRAINTS.CONSTRAINT_NAME
                        }
                    )
                    .where(TABLE_CONSTRAINTS.TABLE_SCHEMA.in(getInputSchemata()))
                    .orderBy(
                        TABLE_CONSTRAINTS.TABLE_SCHEMA,
                        TABLE_CONSTRAINTS.TABLE_NAME,
                        TABLE_CONSTRAINTS.CONSTRAINT_NAME)) {

                SchemaDefinition schema = getSchema(record.get(TABLE_CONSTRAINTS.TABLE_SCHEMA));
                TableDefinition table = getTable(schema, record.get(TABLE_CONSTRAINTS.TABLE_NAME));

                if (table != null) {
                    relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                        schema,
                        table,
                        record.get(CHECK_CONSTRAINTS.CONSTRAINT_NAME),
                        record.get(CHECK_CONSTRAINTS.CHECK_CLAUSE)
                    ));
                }
            }
        }
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        return
        create().select(SCHEMATA.SCHEMA_NAME)
                .from(SCHEMATA)
                .collect(mapping(r -> new SchemaDefinition(this, r.value1(), ""), toList()));
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                VIEWS.TABLE_CATALOG,
                VIEWS.TABLE_SCHEMA,
                VIEWS.TABLE_NAME,
                when(lower(VIEWS.VIEW_DEFINITION).like(inline("create%")), VIEWS.VIEW_DEFINITION)
                .else_(prependCreateView(VIEWS.TABLE_NAME, VIEWS.VIEW_DEFINITION, '`')).as(VIEWS.VIEW_DEFINITION))
            .from(VIEWS)
            .where(VIEWS.TABLE_SCHEMA.in(schemas))
            .orderBy(
                VIEWS.TABLE_SCHEMA,
                VIEWS.TABLE_NAME)
        ;
    }

    @Override
    public ResultQuery<Record5<String, String, String, String, String>> comments(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return null;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> enums(List<String> schemas) {

        // Recursive query that works with MySQL 8+ only:
        // https://stackoverflow.com/a/77057135/521799

        Columns c = COLUMNS;

        Field<String> e = field(name("e"), VARCHAR);
        Field<String> l = field(name("l"), VARCHAR);
        Field<Integer> p = field(name("p"), INTEGER);

        CommonTableExpression<?> te = name("e").as(
            select(
                c.TABLE_SCHEMA,
                c.TABLE_NAME,
                c.COLUMN_NAME,
                regexpReplaceAll(c.COLUMN_TYPE, inline("enum\\((.*)\\)"), inline("$1")).as(e))
            .from(c)
            .where(c.DATA_TYPE.eq(inline("enum")))
        );

        CommonTableExpression<?> tl = name("l").as(
            select(
                te.field(c.TABLE_SCHEMA),
                te.field(c.TABLE_NAME),
                te.field(c.COLUMN_NAME),
                e, cast(inline(""), CHAR(32767)).as(l),
                inline(0).as(p))
            .from(te)
            .unionAll(
                select(
                    te.field(c.TABLE_SCHEMA),
                    te.field(c.TABLE_NAME),
                    te.field(c.COLUMN_NAME),
                    regexpReplaceFirst(e, inline("'.*?'(?:,|$)(.*)"), inline("$1")),
                    replace(
                        regexpReplaceFirst(e, inline("'(.*?)'(?:,|$).*"), inline("$1")),
                        inline("''"), inline("'")
                    ),
                    p.plus(inline(1)))
                .from(table(name("l")).as(te))
                .where(length(e).gt(inline(0)))
            )
        );

        return create()
            .withRecursive(te, tl)
            .select(
                tl.field(c.TABLE_SCHEMA),
                tl.field(c.TABLE_NAME),
                tl.field(c.COLUMN_NAME),
                inline(null, VARCHAR).as(c.DATA_TYPE),
                tl.field(l),
                tl.field(p))
            .from(tl)
            .where(p.gt(inline(0)))
            .and(tl.field(c.TABLE_SCHEMA).in(schemas))
            .orderBy(
                tl.field(c.TABLE_SCHEMA),
                tl.field(c.TABLE_NAME),
                tl.field(c.COLUMN_NAME),
                tl.field(p));
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                TABLES.TABLE_SCHEMA,
                TABLES.TABLE_NAME,

                // [#17344] MySQL's INFORMATION_SCHEMA.TABLES.TABLE_COMMENT just adds a dummy 'VIEW' REMARK to all views, which we should ignore
                when(TABLES.TABLE_TYPE.ne(inline("VIEW")), TABLES.TABLE_COMMENT).as(TABLES.TABLE_COMMENT),
                when(TABLES.TABLE_TYPE.eq(inline("VIEW")), inline(TableType.VIEW.name()))
                    .else_(inline(TableType.TABLE.name())).as("table_type"))
            .from(TABLES)
            .where(TABLES.TABLE_SCHEMA.in(workaroundFor5213(getInputSchemata())))

            // [#9291] MariaDB treats sequences as tables
            .and(TABLES.TABLE_TYPE.ne(inline("SEQUENCE")))
            .orderBy(
                TABLES.TABLE_SCHEMA,
                TABLES.TABLE_NAME)) {

            SchemaDefinition schema = getSchema(record.get(TABLES.TABLE_SCHEMA));
            String name = record.get(TABLES.TABLE_NAME);
            String comment = record.get(TABLES.TABLE_COMMENT);
            TableType tableType = record.get("table_type", TableType.class);

            MySQLTableDefinition table = new MySQLTableDefinition(schema, name, comment, tableType, null);
            result.add(table);
        }

        return result;
    }

    static record ColumnRecord (String schema, String table, String column, String type, String comment) {}

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();

        for (ColumnRecord r : create()
                .select(
                    COLUMNS.TABLE_SCHEMA,
                    COLUMNS.TABLE_NAME,
                    COLUMNS.COLUMN_NAME,
                    COLUMNS.COLUMN_TYPE,
                    COLUMNS.COLUMN_COMMENT)
                .from(COLUMNS)
                .where(
                    COLUMNS.COLUMN_TYPE.like("enum(%)").and(
                    COLUMNS.TABLE_SCHEMA.in(workaroundFor5213(getInputSchemata()))))
                .orderBy(
                    COLUMNS.TABLE_SCHEMA.asc(),
                    COLUMNS.TABLE_NAME.asc(),
                    COLUMNS.COLUMN_NAME.asc())
                .fetch(mapping(ColumnRecord::new))
        ) {
            SchemaDefinition schema = getSchema(r.schema);

            String name = r.table + "_" + r.column;

            // [#1237] Don't generate enum classes for columns in MySQL tables
            // that are excluded from code generation
            TableDefinition tableDefinition = getTable(schema, r.table);
            if (tableDefinition != null) {
                ColumnDefinition columnDefinition = tableDefinition.getColumn(r.column);

                if (columnDefinition != null) {
                    DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, r.comment, true);

                    CSVReader reader = new CSVReader(
                        new StringReader(r.type.replaceAll("(^enum\\()|(\\)$)", ""))
                       ,','  // Separator
                       ,'\'' // Quote character
                       ,true // Strict quotes
                    );

                    for (String string : reader.next())
                        definition.addLiteral(string);

                    result.add(definition);
                }
            }
        }

        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<>();
        return result;
    }















































    @Override
    public ResultQuery<Record6<String, String, String, String, String, String>> generators(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).as("table_catalog"),
                COLUMNS.TABLE_SCHEMA,
                COLUMNS.TABLE_NAME,
                COLUMNS.COLUMN_NAME,
                generationExpression(COLUMNS.GENERATION_EXPRESSION),
                when(COLUMNS.EXTRA.in(inline("VIRTUAL"), inline("VIRTUAL GENERATED")), inline(GenerationOption.VIRTUAL.name()))
                .when(COLUMNS.EXTRA.in(inline("PERSISTENT"), inline("STORED GENERATED")), inline(GenerationOption.STORED.name()))
                .as("generationOption"))
            .from(COLUMNS)
            .where(COLUMNS.TABLE_SCHEMA.in(schemas))
            .and(COLUMNS.EXTRA.in(
                inline("VIRTUAL"),
                inline("VIRTUAL GENERATED"),
                inline("PERSISTENT"),
                inline("STORED GENERATED")))
            .orderBy(COLUMNS.ORDINAL_POSITION);
    }

    @Override
    protected List<XMLSchemaCollectionDefinition> getXMLSchemaCollections0() throws SQLException {
        List<XMLSchemaCollectionDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<>();
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

        Result<Record6<String, String, String, byte[], byte[], ProcType>> records = is8()

            ? create().select(
                    ROUTINES.ROUTINE_SCHEMA,
                    ROUTINES.ROUTINE_NAME,
                    ROUTINES.ROUTINE_COMMENT,
                    inline(new byte[0]).as(PROC.PARAM_LIST),
                    inline(new byte[0]).as(PROC.RETURNS),
                    ROUTINES.ROUTINE_TYPE.coerce(PROC.TYPE).as(ROUTINES.ROUTINE_TYPE))
                .from(ROUTINES)
                .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))

                // [#9309] [#15319] Until we support MariaDB packages, we must exclude them here, explicitly
                .and(ROUTINES.ROUTINE_TYPE.in(ProcType.FUNCTION.name(), ProcType.PROCEDURE.name()))
                .orderBy(1, 2, 6)
                .fetch()

            : create().select(
                    PROC.DB.as(ROUTINES.ROUTINE_SCHEMA),
                    PROC.NAME.as(ROUTINES.ROUTINE_NAME),
                    PROC.COMMENT.as(ROUTINES.ROUTINE_COMMENT),
                    PROC.PARAM_LIST,
                    PROC.RETURNS,
                    PROC.TYPE.as(ROUTINES.ROUTINE_TYPE))
                .from(PROC)
                .where(PROC.DB.in(getInputSchemata()))

                // [#9309] [#15319] Until we support MariaDB packages, we must exclude them here, explicitly
                .and(PROC.TYPE.in(ProcType.FUNCTION, ProcType.PROCEDURE))
                .orderBy(1, 2, 6)
                .fetch();

        Map<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> groups =
            records.intoGroups(new Field[] { ROUTINES.ROUTINE_SCHEMA, ROUTINES.ROUTINE_NAME });

        // [#1908] This indirection is necessary as MySQL allows for overloading
        // procedures and functions with the same signature.
        groups.forEach((k, overloads) -> {
            overloads.forEach(record -> {
                SchemaDefinition schema = getSchema(record.get(ROUTINES.ROUTINE_SCHEMA));
                String name = record.get(ROUTINES.ROUTINE_NAME);
                String comment = record.get(ROUTINES.ROUTINE_COMMENT);
                String params = is8() ? "" : new String(record.get(PROC.PARAM_LIST));
                String returns = is8() ? "" : new String(record.get(PROC.RETURNS));
                ProcType type = record.get(ROUTINES.ROUTINE_TYPE.coerce(PROC.TYPE).as(ROUTINES.ROUTINE_TYPE));

                if (overloads.size() > 1)
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, "_" + type.name()));
                else
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, null));
            });
        });

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }

    @Override
    protected boolean exists0(TableField<?, ?> field) {
        return exists1(field, COLUMNS, COLUMNS.TABLE_SCHEMA, COLUMNS.TABLE_NAME, COLUMNS.COLUMN_NAME);
    }

    @Override
    protected boolean exists0(Table<?> table) {
        return exists1(table, TABLES, TABLES.TABLE_SCHEMA, TABLES.TABLE_NAME);
    }

    private List<Field<String>> workaroundFor5213(List<String> inputSchemata) {
        // [#5213] Add a dummy schema to single element lists to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022

        List<Field<String>> schemas = new ArrayList<>();

        for (String schema : inputSchemata)
            schemas.add(DSL.val(schema));

        // Random UUID generated by fair dice roll
        if (schemas.size() == 1)
            schemas.add(DSL.inline("ee7f6174-34f2-484b-8d81-20a4d9fc866d"));

        return schemas;
    }

    protected Field<String> generationExpression(Field<String> generationExpression) {
        if (is5_7())
            return generationExpression;
        else
            return inline(null, VARCHAR);
    }
}
