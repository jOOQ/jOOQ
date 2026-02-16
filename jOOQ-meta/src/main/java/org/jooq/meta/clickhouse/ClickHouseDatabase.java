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

package org.jooq.meta.clickhouse;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.clickhouse.information_schema.Tables.COLUMNS;
import static org.jooq.meta.clickhouse.information_schema.Tables.SCHEMATA;
import static org.jooq.meta.clickhouse.information_schema.Tables.TABLES;
import static org.jooq.meta.clickhouse.system.System.SYSTEM;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record14;
import org.jooq.Record15;
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
import org.jooq.conf.RenderOptionalKeyword;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
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
import org.jooq.meta.XMLSchemaCollectionDefinition;
import org.jooq.meta.clickhouse.system.tables.DataSkippingIndices;
import org.jooq.meta.clickhouse.system.tables.Tables;

/**
 * @author Lukas Eder
 */
public class ClickHouseDatabase extends AbstractDatabase implements ResultQueryDatabase {

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        DataSkippingIndices i = SYSTEM.DATA_SKIPPING_INDICES;
        Field<String> columnName = field(name("column_name"), VARCHAR);
        Field<Integer> columnIndex = field(name("column_index"), INTEGER);


        Map<Record, Result<Record>> indexes = create()
            .select(
                i.DATABASE,
                i.TABLE,
                i.NAME,
                trim(field("c.1", VARCHAR)).as("column_name"),
                field("c.2", INTEGER).as("column_index"))
            .from("{0} array join arrayZip(splitByChar(',', expr), arrayEnumerate(splitByChar(',', expr))) as c",
                i)
            .where(i.DATABASE.in(getInputSchemata()))
            .fetchGroups(
                new Field[] {
                    i.DATABASE,
                    i.TABLE,
                    i.NAME
                },
                new Field[] {
                    columnName,
                    columnIndex
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();

            final SchemaDefinition tableSchema = getSchema(index.get(i.DATABASE));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(i.NAME);
            final String tableName = index.get(i.TABLE);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            // [#6310] [#6620] Function-based indexes are not yet supported
            // [#16237]        Alternatively, the column could be hidden or excluded
            for (Record column : columns)
                if (table.getColumn(column.get(columnName)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, false) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(columnName)),
                            SortOrder.ASC,
                            column.get(columnIndex)
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
        primaryKeys(getInputSchemata()).forEach(record -> {
            SchemaDefinition schema = getSchema(record.value2());

            if (schema != null) {
                String tableName = record.value3();
                String key = record.value4();
                String columnName = record.value5();

                TableDefinition table = getTable(schema, tableName);
                if (table != null)
                    relations.addPrimaryKey(key, table, table.getColumn(columnName));
            }
        });
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        Tables t = SYSTEM.TABLES;

        return
        create().select(
                    t.DATABASE.as("catalog"),
                    t.DATABASE,
                    t.NAME,
                    inline("KEY_").concat(t.NAME).concat(inline("_PRIMARY")).as("constraint_name"),
                    trim(field("c.1", VARCHAR)).as("column_name"),
                    field("c.2", INTEGER).as("column_index"))
                .from("{0} array join arrayZip(splitByChar(',', primary_key), arrayEnumerate(splitByChar(',', primary_key))) as c",
                    t)
                .where(t.PRIMARY_KEY.ne(""))
                .and(t.DATABASE.in(schemas));
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return null;
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    public ResultQuery<Record5<String, String, String, String, String>> checks(List<String> schemas) {
        return null;
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
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
                SYSTEM.TABLES.DATABASE.as("catalog"),
                SYSTEM.TABLES.DATABASE,
                SYSTEM.TABLES.TABLE,
                SYSTEM.TABLES.CREATE_TABLE_QUERY)
            .from(SYSTEM.TABLES)
            .where(SYSTEM.TABLES.DATABASE.in(schemas))
            .and(SYSTEM.TABLES.ENGINE.eq(inline("View")));
    }

    @Override
    public ResultQuery<Record5<String, String, String, String, String>> comments(List<String> schemas) {
        Table<?> c =
            select(
                SYSTEM.TABLES.DATABASE.as("catalog"),
                SYSTEM.TABLES.DATABASE,
                SYSTEM.TABLES.NAME.as(SYSTEM.COLUMNS.TABLE),
                inline(null, VARCHAR).as(SYSTEM.COLUMNS.NAME),
                SYSTEM.TABLES.COMMENT.as(SYSTEM.COLUMNS.COMMENT))
            .from(SYSTEM.TABLES)
            .where(SYSTEM.TABLES.COMMENT.ne(inline("")))
            .unionAll(
                select(
                    SYSTEM.COLUMNS.DATABASE.as("catalog"),
                    SYSTEM.COLUMNS.DATABASE,
                    SYSTEM.COLUMNS.TABLE,
                    SYSTEM.COLUMNS.NAME,
                    SYSTEM.COLUMNS.COMMENT)
                .from(SYSTEM.COLUMNS)
                .where(SYSTEM.COLUMNS.COMMENT.ne(inline(""))))
            .asTable("c");

        return create()
            .select(
                c.field(SYSTEM.TABLES.DATABASE.as("catalog")),
                c.field(SYSTEM.TABLES.DATABASE),
                c.field(SYSTEM.COLUMNS.TABLE),
                c.field(SYSTEM.COLUMNS.NAME),
                c.field(SYSTEM.COLUMNS.COMMENT))
            .from(c)
            .where(c.field(SYSTEM.TABLES.DATABASE).in(schemas))
            .orderBy(1, 2, 3, 4);
    }

    @Override
    public ResultQuery<Record15<String, String, String, String, String, Integer, String, Integer, Integer, Boolean, String, String, String, String, String>> attributes(List<String> schemas) {
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
        return null;
    }

    static record TableRecord(String schema, String table, TableType type, String comment) {}

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (TableRecord r : create()
            .select(
                TABLES.TABLE_SCHEMA,
                TABLES.TABLE_NAME,
                when(TABLES.TABLE_TYPE.in(inline("VIEW"), inline("SYSTEM VIEW")), inline(TableType.VIEW.name()))
                   .else_(inline(TableType.TABLE.name())).convertFrom(TableType::valueOf).as("table_type"),
                SYSTEM.TABLES.COMMENT)
            .from(TABLES)
            .join(SYSTEM.TABLES)
                .on(TABLES.TABLE_SCHEMA.eq(SYSTEM.TABLES.DATABASE))
                .and(TABLES.TABLE_NAME.eq(SYSTEM.TABLES.NAME))
            .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                TABLES.TABLE_SCHEMA,
                TABLES.TABLE_NAME)
            .fetch(mapping(TableRecord::new))
        ) {
            SchemaDefinition schema = getSchema(r.schema);

            if (schema != null)
                result.add(new ClickHouseTableDefinition(schema, r.table, r.comment, r.type, null));
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<>();
        return result;
    }















    @Override
    public ResultQuery<Record5<String, String, String, String, String>> identities(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, String>> generators(List<String> schemas) {
        return null;
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
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL
            .using(getConnection(), SQLDialect.CLICKHOUSE)
            .configuration()
            .deriveSettings(s -> s.withRenderOptionalAsKeywordForFieldAliases(RenderOptionalKeyword.ON))
            .dsl();
    }

    @Override
    protected boolean exists0(TableField<?, ?> field) {
        return exists1(field, COLUMNS, COLUMNS.TABLE_SCHEMA, COLUMNS.TABLE_NAME, COLUMNS.COLUMN_NAME);
    }

    @Override
    protected boolean exists0(Table<?> table) {
        return exists1(table, TABLES, TABLES.TABLE_SCHEMA, TABLES.TABLE_NAME);
    }
}
