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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

package org.jooq.meta.duckdb;

import static java.util.stream.Collectors.toList;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_CONSTRAINTS;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_DATABASES;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_SCHEMAS;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_TABLES;
import static org.jooq.meta.duckdb.system.main.Tables.DUCKDB_VIEWS;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
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

/**
 * The DuckDB database
 *
 * @author Lukas Eder
 */
public class DuckDBDatabase extends AbstractDatabase implements ResultQueryDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.DUCKDB);
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : primaryKeys(getInputSchemata())) {
            CatalogDefinition catalog = getCatalog(record.get(DUCKDB_CONSTRAINTS.DATABASE_NAME));

            if (catalog != null) {
                SchemaDefinition schema = catalog.getSchema(record.get(DUCKDB_CONSTRAINTS.SCHEMA_NAME));
                String key = record.get(DUCKDB_CONSTRAINTS.CONSTRAINT_TEXT);
                String tableName = record.get(DUCKDB_CONSTRAINTS.TABLE_NAME);
                String columnName = record.get(DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_NAMES, String.class);

                TableDefinition table = getTable(schema, tableName);
                if (table != null)
                    relations.addPrimaryKey(key, table, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : uniqueKeys(getInputSchemata())) {
            CatalogDefinition catalog = getCatalog(record.get(DUCKDB_CONSTRAINTS.DATABASE_NAME));

            if (catalog != null) {
                SchemaDefinition schema = catalog.getSchema(record.get(DUCKDB_CONSTRAINTS.SCHEMA_NAME));
                String key = record.get(DUCKDB_CONSTRAINTS.CONSTRAINT_TEXT);
                String tableName = record.get(DUCKDB_CONSTRAINTS.TABLE_NAME);
                String columnName = record.get(DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_NAMES, String.class);

                TableDefinition table = getTable(schema, tableName);
                if (table != null)
                    relations.addUniqueKey(key, table, table.getColumn(columnName));
            }
        }
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return keys(schemas, "PRIMARY KEY");
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return keys(schemas, "UNIQUE");
    }

    private ResultQuery<Record6<String, String, String, String, String, Integer>> keys(List<String> schemas, String constraintType) {
        return create()
            .select(
                DUCKDB_CONSTRAINTS.DATABASE_NAME,
                DUCKDB_CONSTRAINTS.SCHEMA_NAME,
                DUCKDB_CONSTRAINTS.TABLE_NAME,
                DUCKDB_CONSTRAINTS.DATABASE_NAME
                    .concat(inline("__"))
                    .concat(DUCKDB_CONSTRAINTS.SCHEMA_NAME)
                    .concat(inline("__"))
                    .concat(DUCKDB_CONSTRAINTS.TABLE_NAME)
                    .concat(inline("__"))
                    .concat(DUCKDB_CONSTRAINTS.CONSTRAINT_TEXT).as(DUCKDB_CONSTRAINTS.CONSTRAINT_TEXT),
                field("unnest({0})", VARCHAR, DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_NAMES).as(DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_NAMES),
                field("unnest({0})", INTEGER, DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_INDEXES).as(DUCKDB_CONSTRAINTS.CONSTRAINT_COLUMN_INDEXES)
            )
            .from("{0}()", DUCKDB_CONSTRAINTS)
            .where(DUCKDB_CONSTRAINTS.CONSTRAINT_TYPE.eq(inline(constraintType)))

            // TODO: Query (catalog, schema), instead
            .and(DUCKDB_CONSTRAINTS.SCHEMA_NAME.in(schemas));
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        return
        create().select(DUCKDB_DATABASES.DATABASE_NAME)
                .from("{0}()", DUCKDB_DATABASES)
                .fetch(mapping(c -> new CatalogDefinition(this, c, "")));
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        return
        create().select(DUCKDB_SCHEMAS.DATABASE_NAME, DUCKDB_SCHEMAS.SCHEMA_NAME)
                .from("{0}()", DUCKDB_SCHEMAS)
                .where(DUCKDB_SCHEMAS.DATABASE_NAME.in(getInputCatalogs()))
                .fetch(mapping((c, s) -> new SchemaDefinition(this, s, "", getCatalog(c))));
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                DUCKDB_VIEWS.DATABASE_NAME,
                DUCKDB_VIEWS.SCHEMA_NAME,
                DUCKDB_VIEWS.VIEW_NAME,
                DUCKDB_VIEWS.SQL)
            .from("{0}()", DUCKDB_VIEWS)
            .where(DUCKDB_VIEWS.SCHEMA_NAME.in(schemas));
    }

    @Override
    public ResultQuery<Record5<String, String, String, String, String>> comments(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return create()
            .resultQuery(
                """
                select
                  database_name,
                  schema_name,
                  sequence_name,
                  'bigint' as data_type,
                  0 as precision,
                  0 as scale,
                  start_value,
                  increment_by,
                  min_value,
                  max_value,
                  cycle,
                  0 as cache
                from duckdb_sequences()
                where sequence_name in ({0})
                """,
                DSL.list(schemas.stream().map(DSL::val).collect(toList()))
            )
            .coerce(
                field("database_name", VARCHAR),
                field("schema_name", VARCHAR),
                field("sequence_name", VARCHAR),
                field("data_type", VARCHAR),
                field("precision", INTEGER),
                field("scale", INTEGER),
                field("start_value", BIGINT),
                field("increment_by", BIGINT),
                field("min_value", NUMERIC),
                field("max_value", NUMERIC),
                field("cycle", BOOLEAN),
                field("cache", BIGINT)
            );
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : sequences(getInputSchemata())) {
            CatalogDefinition catalog = getCatalog(record.get("database_name", String.class));

            if (catalog != null) {
                SchemaDefinition schema = getSchema(record.get("schema_name", String.class));

                if (schema != null) {
                    DataTypeDefinition type = new DefaultDataTypeDefinition(
                        this,
                        schema,
                        "BIGINT"
                    );

                    result.add(new DefaultSequenceDefinition(
                        schema, record.get("sequence_name", String.class), type));
                }
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create()
            .select(
                DUCKDB_TABLES.DATABASE_NAME,
                DUCKDB_TABLES.SCHEMA_NAME,
                DUCKDB_TABLES.TABLE_NAME,
                inline(TableType.TABLE.name()).as("table_type")
            )
            .from("{0}()", DUCKDB_TABLES)
            .where(row(DUCKDB_TABLES.DATABASE_NAME, DUCKDB_TABLES.SCHEMA_NAME).in(
                getInputCatalogsAndSchemata().stream().map(e -> row(e.getKey(), e.getValue())).collect(toList())
            ))
            .unionAll(
                select(
                    DUCKDB_VIEWS.DATABASE_NAME,
                    DUCKDB_VIEWS.SCHEMA_NAME,
                    DUCKDB_VIEWS.VIEW_NAME,
                    inline(TableType.VIEW.name()).as("table_type")
                )
                .from("{0}()", DUCKDB_VIEWS)
                .where(row(DUCKDB_VIEWS.DATABASE_NAME, DUCKDB_VIEWS.SCHEMA_NAME).in(
                    getInputCatalogsAndSchemata().stream().map(e -> row(e.getKey(), e.getValue())).collect(toList())
                ))
            )
            .orderBy(1, 2, 3)
        ) {
            CatalogDefinition catalog = getCatalog(record.get(DUCKDB_TABLES.DATABASE_NAME));
            SchemaDefinition schema = catalog.getSchema(record.get(DUCKDB_TABLES.SCHEMA_NAME));
            String name = record.get(DUCKDB_TABLES.TABLE_NAME);
            TableType tableType = record.get("table_type", TableType.class);
            result.add(new DuckDBTableDefinition(schema, name, "", tableType, null));
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
}
