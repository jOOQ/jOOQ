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
 */

package org.jooq.util.mysql;

import static org.jooq.impl.DSL.inline;
import static org.jooq.util.mysql.information_schema.Tables.COLUMNS;
import static org.jooq.util.mysql.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.mysql.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.mysql.information_schema.Tables.SCHEMATA;
import static org.jooq.util.mysql.information_schema.Tables.STATISTICS;
import static org.jooq.util.mysql.information_schema.Tables.TABLES;
import static org.jooq.util.mysql.mysql.tables.Proc.DB;
import static org.jooq.util.mysql.mysql.tables.Proc.PROC;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;
import org.jooq.util.mysql.information_schema.tables.KeyColumnUsage;
import org.jooq.util.mysql.information_schema.tables.ReferentialConstraints;
import org.jooq.util.mysql.information_schema.tables.Schemata;
import org.jooq.util.mysql.information_schema.tables.Statistics;
import org.jooq.util.mysql.information_schema.tables.Tables;
import org.jooq.util.mysql.mysql.enums.ProcType;
import org.jooq.util.mysql.mysql.tables.Proc;

/**
 * @author Lukas Eder
 */
public class MySQLDatabase extends AbstractDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(MySQLDatabase.class);

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(true)) {
            SchemaDefinition schema = getSchema(record.get(Statistics.TABLE_SCHEMA));
            String constraintName = record.get(Statistics.INDEX_NAME);
            String tableName = record.get(Statistics.TABLE_NAME);
            String columnName = record.get(Statistics.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
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

            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private String getKeyName(String tableName, String keyName) {
        return "KEY_" + tableName + "_" + keyName;
    }

    private Result<Record4<String, String, String, String>> fetchKeys(boolean primary) {

        // [#3560] It has been shown that querying the STATISTICS table is much faster on
        // very large databases than going through TABLE_CONSTRAINTS and KEY_COLUMN_USAGE
        return create().select(
                           Statistics.TABLE_SCHEMA,
                           Statistics.TABLE_NAME,
                           Statistics.COLUMN_NAME,
                           Statistics.INDEX_NAME)
                       .from(STATISTICS)
                       .where(Statistics.TABLE_SCHEMA.in(getInputSchemata()))
                       .and(primary
                           ? Statistics.INDEX_NAME.eq(inline("PRIMARY"))
                           : Statistics.INDEX_NAME.ne(inline("PRIMARY")).and(Statistics.NON_UNIQUE.eq(inline(0L))))
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
                .where(ReferentialConstraints.CONSTRAINT_SCHEMA.in(getInputSchemata()))
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
            String referencedKey = record.get(ReferentialConstraints.UNIQUE_CONSTRAINT_NAME);
            String referencedTableName = record.get(ReferentialConstraints.REFERENCED_TABLE_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);

            if (foreignKeyTable != null) {
                ColumnDefinition column = foreignKeyTable.getColumn(foreignKeyColumn);

                String key = getKeyName(referencedTableName, referencedKey);
                relations.addForeignKey(foreignKey, key, column, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<CatalogDefinition>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

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
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME,
                Tables.TABLE_COMMENT)
            .from(TABLES)
            .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Tables.TABLE_SCHEMA));
            String name = record.get(Tables.TABLE_NAME);
            String comment = record.get(Tables.TABLE_COMMENT);

            MySQLTableDefinition table = new MySQLTableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

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
                Columns.TABLE_SCHEMA.in(getInputSchemata())))
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
        List<DomainDefinition> result = new ArrayList<DomainDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        try {
            create(true).fetchCount(PROC);
        }
        catch (DataAccessException e) {
            log.warn("Table unavailable", "The `mysql`.`proc` table is unavailable. Stored procedures cannot be loaded. Check if you have sufficient grants");
            return result;
        }

        Result<Record6<String, String, String, byte[], byte[], ProcType>> records =
        create().select(
                    Proc.DB,
                    Proc.NAME,
                    Proc.COMMENT,
                    Proc.PARAM_LIST,
                    Proc.RETURNS,
                    Proc.TYPE)
                .from(PROC)
                .where(DB.in(getInputSchemata()))
                .orderBy(
                    DB,
                    Proc.NAME)
                .fetch();

        Map<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> groups =
            records.intoGroups(new Field[] { Proc.DB, Proc.NAME });

        // [#1908] This indirection is necessary as MySQL allows for overloading
        // procedures and functions with the same signature.
        for (Entry<Record, Result<Record6<String, String, String, byte[], byte[], ProcType>>> entry : groups.entrySet()) {
            Result<?> overloads = entry.getValue();

            for (int i = 0; i < overloads.size(); i++) {
                Record record = overloads.get(i);

                SchemaDefinition schema = getSchema(record.get(DB));
                String name = record.get(Proc.NAME);
                String comment = record.get(Proc.COMMENT);
                String params = new String(record.get(Proc.PARAM_LIST));
                String returns = new String(record.get(Proc.RETURNS));
                ProcType type = record.get(Proc.TYPE);

                if (overloads.size() > 1) {
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, "_" + type.name()));
                }
                else {
                    result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns, type, null));
                }
            }
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }
}
