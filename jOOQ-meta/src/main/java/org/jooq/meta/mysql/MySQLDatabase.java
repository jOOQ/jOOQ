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

package org.jooq.meta.mysql;

import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.when;
import static org.jooq.meta.mysql.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.COLUMNS;
import static org.jooq.meta.mysql.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.meta.mysql.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.ROUTINES;
import static org.jooq.meta.mysql.information_schema.Tables.SCHEMATA;
import static org.jooq.meta.mysql.information_schema.Tables.STATISTICS;
import static org.jooq.meta.mysql.information_schema.Tables.TABLES;
import static org.jooq.meta.mysql.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.meta.mysql.information_schema.Tables.VIEWS;
import static org.jooq.meta.mysql.mysql.Tables.PROC;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultEnumDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.mariadb.MariaDBDatabase;
import org.jooq.meta.mysql.information_schema.tables.CheckConstraints;
import org.jooq.meta.mysql.information_schema.tables.Columns;
import org.jooq.meta.mysql.information_schema.tables.KeyColumnUsage;
import org.jooq.meta.mysql.information_schema.tables.ReferentialConstraints;
import org.jooq.meta.mysql.information_schema.tables.Routines;
import org.jooq.meta.mysql.information_schema.tables.Schemata;
import org.jooq.meta.mysql.information_schema.tables.Statistics;
import org.jooq.meta.mysql.information_schema.tables.TableConstraints;
import org.jooq.meta.mysql.information_schema.tables.Tables;
import org.jooq.meta.mysql.information_schema.tables.Views;
import org.jooq.meta.mysql.mysql.enums.ProcType;
import org.jooq.meta.mysql.mysql.tables.Proc;
import org.jooq.tools.csv.CSVReader;

/**
 * @author Lukas Eder
 */
public class MySQLDatabase extends AbstractDatabase {

    private static Boolean is8;
    private static Boolean is8_0_16;

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        // Workaround for MemSQL bug
        // https://www.memsql.com/forum/t/wrong-query-result-on-information-schema-query/1423/2
        Table<?> from = getIncludeSystemIndexes()
            ? STATISTICS
            : STATISTICS.leftJoin(TABLE_CONSTRAINTS)
                .on(Statistics.INDEX_SCHEMA.eq(TableConstraints.CONSTRAINT_SCHEMA))
                .and(Statistics.TABLE_NAME.eq(TableConstraints.TABLE_NAME))
                .and(Statistics.INDEX_NAME.eq(TableConstraints.CONSTRAINT_NAME));

        // Same implementation as in H2Database and HSQLDBDatabase
        Map<Record, Result<Record>> indexes = create()
            // [#2059] In MemSQL primary key indexes are typically duplicated
            // (once with INDEX_TYPE = 'SHARD' and once with INDEX_TYPE = 'BTREE)
            .selectDistinct(
                Statistics.TABLE_SCHEMA,
                Statistics.TABLE_NAME,
                Statistics.INDEX_NAME,
                Statistics.NON_UNIQUE,
                Statistics.COLUMN_NAME,
                Statistics.SEQ_IN_INDEX)
            .from(from)
            // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
            .where(Statistics.TABLE_SCHEMA.in(getInputSchemata()).or(
                  getInputSchemata().size() == 1
                ? Statistics.TABLE_SCHEMA.in(getInputSchemata())
                : falseCondition()))
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : TableConstraints.CONSTRAINT_NAME.isNull()
            )
            .orderBy(
                Statistics.TABLE_SCHEMA,
                Statistics.TABLE_NAME,
                Statistics.INDEX_NAME,
                Statistics.SEQ_IN_INDEX)
            .fetchGroups(
                new Field[] {
                    Statistics.TABLE_SCHEMA,
                    Statistics.TABLE_NAME,
                    Statistics.INDEX_NAME,
                    Statistics.NON_UNIQUE
                },
                new Field[] {
                    Statistics.COLUMN_NAME,
                    Statistics.SEQ_IN_INDEX
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();

            final SchemaDefinition tableSchema = getSchema(index.get(Statistics.TABLE_SCHEMA));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(Statistics.INDEX_NAME);
            final String tableName = index.get(Statistics.TABLE_NAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = !index.get(Statistics.NON_UNIQUE, boolean.class);

            // [#6310] [#6620] Function-based indexes are not yet supported
            for (Record column : columns)
                if (table.getColumn(column.get(Statistics.COLUMN_NAME)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(Statistics.COLUMN_NAME)),
                            SortOrder.ASC,
                            column.get(Statistics.SEQ_IN_INDEX, int.class)
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
        for (Record record : fetchKeys(true)) {
            SchemaDefinition schema = getSchema(record.get(Statistics.TABLE_SCHEMA));
            String constraintName = record.get(Statistics.INDEX_NAME);
            String tableName = record.get(Statistics.TABLE_NAME);
            String columnName = record.get(Statistics.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null)
                relations.addPrimaryKey(key, table, table.getColumn(columnName));
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(false)) {
            SchemaDefinition schema = getSchema(record.get(Statistics.TABLE_SCHEMA));
            String constraintName = record.get(Statistics.INDEX_NAME);
            String tableName = record.get(Statistics.TABLE_NAME);
            String columnName = record.get(Statistics.COLUMN_NAME);

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
            is8 = !exists(PROC);

        return is8;
    }

    protected boolean is8_0_16() {

        // [#7639] The information_schema.check_constraints table was added in MySQL 8.0.16 only
        if (is8_0_16 == null)
            is8_0_16 = exists(CHECK_CONSTRAINTS);

        return is8_0_16;
    }

    private Result<?> fetchKeys(boolean primary) {

        // [#3560] It has been shown that querying the STATISTICS table is much faster on
        // very large databases than going through TABLE_CONSTRAINTS and KEY_COLUMN_USAGE
        // [#2059] In MemSQL primary key indexes are typically duplicated
        // (once with INDEX_TYPE = 'SHARD' and once with INDEX_TYPE = 'BTREE)
        return create().selectDistinct(
                           Statistics.TABLE_SCHEMA,
                           Statistics.TABLE_NAME,
                           Statistics.COLUMN_NAME,
                           Statistics.INDEX_NAME,
                           Statistics.SEQ_IN_INDEX)
                       .from(STATISTICS)
                       // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
                       .where(Statistics.TABLE_SCHEMA.in(getInputSchemata()).or(
                             getInputSchemata().size() == 1
                           ? Statistics.TABLE_SCHEMA.in(getInputSchemata())
                           : falseCondition()))
                       .and(primary
                           ? Statistics.INDEX_NAME.eq(inline("PRIMARY"))
                           : Statistics.INDEX_NAME.ne(inline("PRIMARY")).and(Statistics.NON_UNIQUE.eq(inline(0))))
                       .orderBy(
                           Statistics.TABLE_SCHEMA,
                           Statistics.TABLE_NAME,
                           Statistics.INDEX_NAME,
                           Statistics.SEQ_IN_INDEX)
                       .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ReferentialConstraints.CONSTRAINT_SCHEMA,
                    ReferentialConstraints.CONSTRAINT_NAME,
                    ReferentialConstraints.TABLE_NAME,
                    ReferentialConstraints.REFERENCED_TABLE_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA,
                    KeyColumnUsage.COLUMN_NAME)
                .from(REFERENTIAL_CONSTRAINTS)
                .join(KEY_COLUMN_USAGE)
                .on(ReferentialConstraints.CONSTRAINT_SCHEMA.equal(KeyColumnUsage.CONSTRAINT_SCHEMA))
                .and(ReferentialConstraints.CONSTRAINT_NAME.equal(KeyColumnUsage.CONSTRAINT_NAME))
                .and(ReferentialConstraints.TABLE_NAME.equal(KeyColumnUsage.TABLE_NAME))
                // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
                .where(ReferentialConstraints.CONSTRAINT_SCHEMA.in(getInputSchemata()).or(
                      getInputSchemata().size() == 1
                    ? ReferentialConstraints.CONSTRAINT_SCHEMA.in(getInputSchemata())
                    : falseCondition()))
                .orderBy(
                    KeyColumnUsage.CONSTRAINT_SCHEMA.asc(),
                    KeyColumnUsage.CONSTRAINT_NAME.asc(),
                    KeyColumnUsage.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.get(ReferentialConstraints.CONSTRAINT_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.get(ReferentialConstraints.CONSTRAINT_NAME);
            String foreignKeyColumn = record.get(KeyColumnUsage.COLUMN_NAME);
            String foreignKeyTableName = record.get(ReferentialConstraints.TABLE_NAME);
            String uniqueKey = record.get(ReferentialConstraints.UNIQUE_CONSTRAINT_NAME);
            String uniqueKeyTableName = record.get(ReferentialConstraints.REFERENCED_TABLE_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
            TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

            if (foreignKeyTable != null)
                relations.addForeignKey(
                    foreignKey,
                    foreignKeyTable,
                    foreignKeyTable.getColumn(foreignKeyColumn),
                    getKeyName(uniqueKeyTableName, uniqueKey),
                    uniqueKeyTable
                );
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        if (is8_0_16()) {
            for (Record record : create()
                    .select(
                        TableConstraints.TABLE_SCHEMA,
                        TableConstraints.TABLE_NAME,
                        CheckConstraints.CONSTRAINT_NAME,
                        CheckConstraints.CHECK_CLAUSE,

                        // We need this additional, useless projection. See:
                        // https://jira.mariadb.org/browse/MDEV-21201
                        TableConstraints.CONSTRAINT_CATALOG,
                        TableConstraints.CONSTRAINT_SCHEMA
                     )
                    .from(TABLE_CONSTRAINTS)
                    .join(CHECK_CONSTRAINTS)
                    .using(this instanceof MariaDBDatabase
                        ? new Field[] {
                            TableConstraints.CONSTRAINT_CATALOG,
                            TableConstraints.CONSTRAINT_SCHEMA,
                            // MariaDB has this column, but not MySQL
                            TableConstraints.TABLE_NAME,
                            TableConstraints.CONSTRAINT_NAME
                        }
                        : new Field[] {
                            TableConstraints.CONSTRAINT_CATALOG,
                            TableConstraints.CONSTRAINT_SCHEMA,
                            TableConstraints.CONSTRAINT_NAME
                        }
                    )
                    .where(TableConstraints.TABLE_SCHEMA.in(getInputSchemata()))
                    .orderBy(
                        TableConstraints.TABLE_SCHEMA,
                        TableConstraints.TABLE_NAME,
                        TableConstraints.CONSTRAINT_NAME)) {

                SchemaDefinition schema = getSchema(record.get(TableConstraints.TABLE_SCHEMA));
                TableDefinition table = getTable(schema, record.get(TableConstraints.TABLE_NAME));

                if (table != null) {
                    relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                        schema,
                        table,
                        record.get(CheckConstraints.CONSTRAINT_NAME),
                        record.get(CheckConstraints.CHECK_CLAUSE)
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
        List<SchemaDefinition> result = new ArrayList<>();

        for (String name : create()
                .select(Schemata.SCHEMA_NAME)
                .from(SCHEMATA)
                .fetch(Schemata.SCHEMA_NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME,
                Tables.TABLE_COMMENT,
                when(Tables.TABLE_TYPE.eq(inline("VIEW")), inline(TableType.VIEW.name()))
                    .else_(inline(TableType.TABLE.name())).as("table_type"),
                when(Views.VIEW_DEFINITION.lower().like(inline("create%")), Views.VIEW_DEFINITION)
                    .else_(inline("create view `").concat(Tables.TABLE_NAME).concat("` as ").concat(Views.VIEW_DEFINITION)).as(Views.VIEW_DEFINITION))
            .from(TABLES)
            .leftJoin(VIEWS)
                .on(Tables.TABLE_SCHEMA.eq(Views.TABLE_SCHEMA))
                .and(Tables.TABLE_NAME.eq(Views.TABLE_NAME))

            // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
            .where(Tables.TABLE_SCHEMA.in(getInputSchemata()).or(
                  getInputSchemata().size() == 1
                ? Tables.TABLE_SCHEMA.in(getInputSchemata())
                : falseCondition()))

            // [#9291] MariaDB treats sequences as tables
            .and(Tables.TABLE_TYPE.ne(inline("SEQUENCE")))
            .orderBy(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME)) {

            SchemaDefinition schema = getSchema(record.get(Tables.TABLE_SCHEMA));
            String name = record.get(Tables.TABLE_NAME);
            String comment = record.get(Tables.TABLE_COMMENT);
            TableType tableType = record.get("table_type", TableType.class);
            String source = record.get(Views.VIEW_DEFINITION);

            MySQLTableDefinition table = new MySQLTableDefinition(schema, name, comment, tableType, source);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();

        Result<Record5<String, String, String, String, String>> records = create()
            .select(
                Columns.TABLE_SCHEMA,
                Columns.COLUMN_COMMENT,
                Columns.TABLE_NAME,
                Columns.COLUMN_NAME,
                Columns.COLUMN_TYPE)
            .from(COLUMNS)
            .where(
                Columns.COLUMN_TYPE.like("enum(%)").and(
                // [#5213] Duplicate schema value to work around MySQL issue https://bugs.mysql.com/bug.php?id=86022
                Columns.TABLE_SCHEMA.in(getInputSchemata()).or(
                      getInputSchemata().size() == 1
                    ? Columns.TABLE_SCHEMA.in(getInputSchemata())
                    : falseCondition())))
            .orderBy(
                Columns.TABLE_SCHEMA.asc(),
                Columns.TABLE_NAME.asc(),
                Columns.COLUMN_NAME.asc())
            .fetch();

        for (Record record : records) {
            SchemaDefinition schema = getSchema(record.get(Columns.TABLE_SCHEMA));

            String comment = record.get(Columns.COLUMN_COMMENT);
            String table = record.get(Columns.TABLE_NAME);
            String column = record.get(Columns.COLUMN_NAME);
            String name = table + "_" + column;
            String columnType = record.get(Columns.COLUMN_TYPE);

            // [#1237] Don't generate enum classes for columns in MySQL tables
            // that are excluded from code generation
            TableDefinition tableDefinition = getTable(schema, table);
            if (tableDefinition != null) {
                ColumnDefinition columnDefinition = tableDefinition.getColumn(column);

                if (columnDefinition != null) {

                    // [#1137] Avoid generating enum classes for enum types that
                    // are explicitly forced to another type
                    if (getConfiguredForcedType(columnDefinition, columnDefinition.getType()) == null) {
                        DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, comment);

                        CSVReader reader = new CSVReader(
                            new StringReader(columnType.replaceAll("(^enum\\()|(\\)$)", ""))
                           ,','  // Separator
                           ,'\'' // Quote character
                           ,true // Strict quotes
                        );

                        for (String string : reader.next()) {
                            definition.addLiteral(string);
                        }

                        result.add(definition);
                    }
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
                    Routines.ROUTINE_SCHEMA,
                    Routines.ROUTINE_NAME,
                    Routines.ROUTINE_COMMENT,
                    inline(new byte[0]).as(Proc.PARAM_LIST),
                    inline(new byte[0]).as(Proc.RETURNS),
                    Routines.ROUTINE_TYPE.coerce(Proc.TYPE).as(Routines.ROUTINE_TYPE))
                .from(ROUTINES)
                .where(Routines.ROUTINE_SCHEMA.in(getInputSchemata()))
                .orderBy(1, 2, 6)
                .fetch()

            : create().select(
                    Proc.DB.as(Routines.ROUTINE_SCHEMA),
                    Proc.NAME.as(Routines.ROUTINE_NAME),
                    Proc.COMMENT.as(Routines.ROUTINE_COMMENT),
                    Proc.PARAM_LIST,
                    Proc.RETURNS,
                    Proc.TYPE.as(Routines.ROUTINE_TYPE))
                .from(PROC)
                .where(Proc.DB.in(getInputSchemata()))
                .orderBy(1, 2, 6)
                .fetch();

        Map<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> groups =
            records.intoGroups(new Field[] { Routines.ROUTINE_SCHEMA, Routines.ROUTINE_NAME });

        // [#1908] This indirection is necessary as MySQL allows for overloading
        // procedures and functions with the same signature.
        for (Entry<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> entry : groups.entrySet()) {
            Result<?> overloads = entry.getValue();

            for (int i = 0; i < overloads.size(); i++) {
                Record record = overloads.get(i);

                SchemaDefinition schema = getSchema(record.get(Routines.ROUTINE_SCHEMA));
                String name = record.get(Routines.ROUTINE_NAME);
                String comment = record.get(Routines.ROUTINE_COMMENT);
                String params = is8() ? "" : new String(record.get(Proc.PARAM_LIST));
                String returns = is8() ? "" : new String(record.get(Proc.RETURNS));
                ProcType type = record.get(Routines.ROUTINE_TYPE.coerce(Proc.TYPE).as(Routines.ROUTINE_TYPE));

                if (overloads.size() > 1)
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, "_" + type.name()));
                else
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, null));
            }
        }

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
        return exists1(field, Columns.COLUMNS, Columns.TABLE_SCHEMA, Columns.TABLE_NAME, Columns.COLUMN_NAME);
    }

    @Override
    protected boolean exists0(Table<?> table) {
        return exists1(table, Tables.TABLES, Tables.TABLE_SCHEMA, Tables.TABLE_NAME);
    }
}
