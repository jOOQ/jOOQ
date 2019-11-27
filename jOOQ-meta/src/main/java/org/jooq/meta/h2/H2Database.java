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
package org.jooq.meta.h2;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.meta.h2.information_schema.tables.Columns.COLUMNS;
import static org.jooq.meta.h2.information_schema.tables.Constraints.CONSTRAINTS;
import static org.jooq.meta.h2.information_schema.tables.CrossReferences.CROSS_REFERENCES;
import static org.jooq.meta.h2.information_schema.tables.FunctionAliases.FUNCTION_ALIASES;
import static org.jooq.meta.h2.information_schema.tables.Indexes.INDEXES;
import static org.jooq.meta.h2.information_schema.tables.Schemata.SCHEMATA;
import static org.jooq.meta.h2.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.meta.h2.information_schema.tables.Tables.TABLES;
import static org.jooq.meta.h2.information_schema.tables.TypeInfo.TYPE_INFO;

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
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultEnumDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
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
import org.jooq.meta.h2.information_schema.tables.Columns;
import org.jooq.meta.h2.information_schema.tables.Constraints;
import org.jooq.meta.h2.information_schema.tables.CrossReferences;
import org.jooq.meta.h2.information_schema.tables.Domains;
import org.jooq.meta.h2.information_schema.tables.FunctionAliases;
import org.jooq.meta.h2.information_schema.tables.Indexes;
import org.jooq.meta.h2.information_schema.tables.Schemata;
import org.jooq.meta.h2.information_schema.tables.Sequences;
import org.jooq.meta.h2.information_schema.tables.Tables;
import org.jooq.meta.h2.information_schema.tables.TypeInfo;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.h2.H2DataType;

/**
 * H2 implementation of {@link AbstractDatabase}
 *
 * @author Espen Stromsnes
 */
public class H2Database extends AbstractDatabase {

    private static final long DEFAULT_SEQUENCE_MAXVALUE = Long.MAX_VALUE;

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.H2);
    }

    @Override
    protected boolean exists0(TableField<?, ?> field) {
        return exists1(field, COLUMNS, Columns.TABLE_SCHEMA, Columns.TABLE_NAME, Columns.COLUMN_NAME);
    }

    @Override
    protected boolean exists0(Table<?> table) {
        return exists1(table, TABLES, Tables.TABLE_SCHEMA, Tables.TABLE_NAME);
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        // Same implementation as in HSQLDBDatabase and MySQLDatabase
        Map<Record, Result<Record>> indexes = create()
            .select(
                Indexes.TABLE_SCHEMA,
                Indexes.TABLE_NAME,
                Indexes.INDEX_NAME,
                Indexes.NON_UNIQUE,
                Indexes.COLUMN_NAME,
                Indexes.ORDINAL_POSITION,
                Indexes.ASC_OR_DESC)
            .from(INDEXES)
            .where(Indexes.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                Indexes.TABLE_SCHEMA,
                Indexes.TABLE_NAME,
                Indexes.INDEX_NAME,
                Indexes.ORDINAL_POSITION)
            .fetchGroups(
                new Field[] {
                    Indexes.TABLE_SCHEMA,
                    Indexes.TABLE_NAME,
                    Indexes.INDEX_NAME,
                    Indexes.NON_UNIQUE
                },
                new Field[] {
                    Indexes.COLUMN_NAME,
                    Indexes.ORDINAL_POSITION
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();

            final SchemaDefinition tableSchema = getSchema(index.get(Indexes.TABLE_SCHEMA));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(Indexes.INDEX_NAME);
            final String tableName = index.get(Indexes.TABLE_NAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = !index.get(Indexes.NON_UNIQUE, boolean.class);

            // [#6310] [#6620] Function-based indexes are not yet supported
            for (Record column : columns)
                if (table.getColumn(column.get(Indexes.COLUMN_NAME)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(Indexes.COLUMN_NAME)),
                            SortOrder.ASC,
                            column.get(Indexes.ORDINAL_POSITION, int.class)
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

        // Workaround for https://github.com/h2database/h2database/issues/1000
        for (Record record : fetchKeys("PRIMARY KEY", "PRIMARY_KEY")) {
            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                String tableName = record.get(Constraints.TABLE_NAME);
                String primaryKey = record.get(Constraints.CONSTRAINT_NAME);
                String columnName = record.get(Indexes.COLUMN_NAME);

                TableDefinition table = getTable(schema, tableName);
                if (table != null)
                    relations.addPrimaryKey(primaryKey, table, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                String tableName = record.get(Constraints.TABLE_NAME);
                String primaryKey = record.get(Constraints.CONSTRAINT_NAME);
                String columnName = record.get(Indexes.COLUMN_NAME);

                TableDefinition table = getTable(schema, tableName);
                if (table != null)
                    relations.addUniqueKey(primaryKey, table, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String... constraintTypes) {
        return create().select(
                    Constraints.TABLE_SCHEMA,
                    Constraints.TABLE_NAME,
                    Constraints.CONSTRAINT_NAME,
                    Indexes.COLUMN_NAME)
                .from(CONSTRAINTS)
                .join(INDEXES)
                .on(Constraints.TABLE_SCHEMA.eq(Indexes.TABLE_SCHEMA))
                .and(Constraints.TABLE_NAME.eq(Indexes.TABLE_NAME))
                .and(Constraints.UNIQUE_INDEX_NAME.eq(Indexes.INDEX_NAME))
                .where(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
                .and(Constraints.CONSTRAINT_TYPE.in(constraintTypes))
                .orderBy(
                    Constraints.TABLE_SCHEMA,
                    Constraints.CONSTRAINT_NAME,
                    Indexes.ORDINAL_POSITION)
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    CrossReferences.FK_NAME,
                    CrossReferences.FKTABLE_NAME,
                    CrossReferences.FKTABLE_SCHEMA,
                    CrossReferences.FKCOLUMN_NAME,
                    Constraints.CONSTRAINT_NAME,
                    Constraints.TABLE_NAME,
                    Constraints.CONSTRAINT_SCHEMA)
                .from(CROSS_REFERENCES)
                .join(CONSTRAINTS)
                .on(CrossReferences.PK_NAME.equal(Constraints.UNIQUE_INDEX_NAME))
                .and(CrossReferences.PKTABLE_NAME.equal(Constraints.TABLE_NAME))
                .and(CrossReferences.PKTABLE_SCHEMA.equal(Constraints.TABLE_SCHEMA))
                .where(CrossReferences.FKTABLE_SCHEMA.in(getInputSchemata()))

                // Workaround for https://github.com/h2database/h2database/issues/1000
                .and(Constraints.CONSTRAINT_TYPE.in("PRIMARY KEY", "PRIMARY_KEY", "UNIQUE"))
                .orderBy(
                    CrossReferences.FKTABLE_SCHEMA.asc(),
                    CrossReferences.FK_NAME.asc(),
                    CrossReferences.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.get(CrossReferences.FKTABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(Constraints.CONSTRAINT_SCHEMA));

            if (foreignKeySchema != null && uniqueKeySchema != null) {
                String foreignKey = record.get(CrossReferences.FK_NAME);
                String foreignKeyTableName = record.get(CrossReferences.FKTABLE_NAME);
                String foreignKeyColumn = record.get(CrossReferences.FKCOLUMN_NAME);
                String uniqueKey = record.get(Constraints.CONSTRAINT_NAME);
                String uniqueKeyTableName = record.get(Constraints.TABLE_NAME);

                TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
                TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

                if (foreignKeyTable != null && uniqueKeyTable != null)
                    relations.addForeignKey(
                        foreignKey,
                        foreignKeyTable,
                        foreignKeyTable.getColumn(foreignKeyColumn),
                        uniqueKey,
                        uniqueKeyTable
                    );
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {

        // TODO: Should we really UNION INFORMATION_SCHEMA.COLUMNS.CHECK_CONSTRAINT?
        for (Record record : create()
            .select(
                Constraints.TABLE_SCHEMA,
                Constraints.TABLE_NAME,
                Constraints.CONSTRAINT_NAME,
                Constraints.CHECK_EXPRESSION
             )
            .from(CONSTRAINTS)
            .where(Constraints.CONSTRAINT_TYPE.eq("CHECK"))
            .and(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
            .union(
            select(
                Columns.TABLE_SCHEMA,
                Columns.TABLE_NAME,
                Columns.CHECK_CONSTRAINT,
                Columns.CHECK_CONSTRAINT
            )
            .from(COLUMNS)
            .where(Columns.CHECK_CONSTRAINT.nvl("").ne(""))
            .and(Columns.TABLE_SCHEMA.in(getInputSchemata())))
            .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                TableDefinition table = getTable(schema, record.get(Constraints.TABLE_NAME));

                if (table != null) {
                    relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                        schema,
                        table,
                        record.get(Constraints.CONSTRAINT_NAME),
                        record.get(Constraints.CHECK_EXPRESSION)
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

        for (Record record : create().select(
                    Schemata.SCHEMA_NAME,
                    Schemata.REMARKS)
                .from(SCHEMATA)
                .fetch()) {

            result.add(new SchemaDefinition(this,
                record.get(Schemata.SCHEMA_NAME),
                record.get(Schemata.REMARKS)));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME,
                    Sequences.INCREMENT,
                    Sequences.MIN_VALUE,
                    nullif(Sequences.MAX_VALUE, inline(DEFAULT_SEQUENCE_MAXVALUE)).as(Sequences.MAX_VALUE),
                    Sequences.IS_CYCLE,
                    Sequences.CACHE
                    )
                .from(SEQUENCES)
                .where(Sequences.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .and(Sequences.SEQUENCE_NAME.upper().notLike("SYSTEM!_SEQUENCE!_%", '!'))
                .orderBy(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Sequences.SEQUENCE_SCHEMA));

            if (schema != null) {
                String name = record.get(Sequences.SEQUENCE_NAME);

                DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    H2DataType.BIGINT.getTypeName()
                );

                result.add(new DefaultSequenceDefinition(
                    schema,
                    name,
                    type,
                    null,
                    null, // H2 doesn't support Postgres-style START WITH
                    record.get(Sequences.INCREMENT),
                    record.get(Sequences.MIN_VALUE),
                    record.get(Sequences.MAX_VALUE),
                    record.get(Sequences.IS_CYCLE),
                    record.get(Sequences.CACHE)));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                    Tables.TABLE_SCHEMA,
                    Tables.TABLE_NAME,
                    Tables.REMARKS)
                .from(TABLES)
                .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Tables.TABLE_SCHEMA,
                    Tables.TABLE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Tables.TABLE_SCHEMA));

            if (schema != null) {
                String name = record.get(Tables.TABLE_NAME);
                String comment = record.get(Tables.REMARKS);

                H2TableDefinition table = new H2TableDefinition(schema, name, comment);
                result.add(table);
            }
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<>();

        Field<Boolean> overloaded = field(select(field(DSL.exists(
            select(one())
            .from(FUNCTION_ALIASES.as("a"))
            .where(field(name("a", FunctionAliases.ALIAS_SCHEMA.getName())).eq(FunctionAliases.ALIAS_SCHEMA))
            .and(field(name("a", FunctionAliases.ALIAS_NAME.getName())).eq(FunctionAliases.ALIAS_NAME))
            .and(field(name("a", FunctionAliases.COLUMN_COUNT.getName())).ne(FunctionAliases.COLUMN_COUNT))
        )))).as("overloaded");

        for (Record record : create()
                .select(
                    FunctionAliases.ALIAS_SCHEMA,
                    FunctionAliases.ALIAS_NAME,
                    FunctionAliases.REMARKS,
                    FunctionAliases.DATA_TYPE,
                    FunctionAliases.RETURNS_RESULT,
                    FunctionAliases.COLUMN_COUNT,
                    overloaded,
                    TypeInfo.TYPE_NAME,
                    TypeInfo.PRECISION,
                    TypeInfo.MAXIMUM_SCALE)
                .from(FUNCTION_ALIASES)
                .leftOuterJoin(TYPE_INFO)
                .on(FunctionAliases.DATA_TYPE.equal(TypeInfo.DATA_TYPE))
                // [#1256] TODO implement this correctly. Not sure if POS = 0
                // is the right predicate to rule out duplicate entries in TYPE_INFO
                .and(TypeInfo.POS.equal(0))
                .where(FunctionAliases.ALIAS_SCHEMA.in(getInputSchemata()))
                .and(FunctionAliases.RETURNS_RESULT.in((short) 1, (short) 2))
                .orderBy(FunctionAliases.ALIAS_NAME).fetch()) {

            SchemaDefinition schema = getSchema(record.get(FunctionAliases.ALIAS_SCHEMA));

            if (schema != null) {
                String name = record.get(FunctionAliases.ALIAS_NAME);
                String comment = record.get(FunctionAliases.REMARKS);
                String typeName = record.get(TypeInfo.TYPE_NAME);
                Integer precision = record.get(TypeInfo.PRECISION);
                Short scale = record.get(TypeInfo.MAXIMUM_SCALE);
                String overload = record.get(overloaded)
                    ? record.get(FunctionAliases.COLUMN_COUNT, String.class)
                    : null;

                result.add(new H2RoutineDefinition(schema, name, comment, typeName, precision, scale, overload));
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
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<>();

        if (!is1_4_197())
            return result;

        getInlineEnums(result);
        getDomainEnums(result);

        return result;
    }

    private void getInlineEnums(List<EnumDefinition> result) {

        enumLoop:
        for (Record record : create()
                .select(
                    Columns.TABLE_SCHEMA,
                    Columns.TABLE_NAME,
                    Columns.COLUMN_NAME,
                    Columns.COLUMN_TYPE)
                .from(COLUMNS)
                .where(
                    Columns.COLUMN_TYPE.like("ENUM(%)%").and(
                    Columns.TABLE_SCHEMA.in(getInputSchemata())))
                .orderBy(
                    Columns.TABLE_SCHEMA.asc(),
                    Columns.TABLE_NAME.asc(),
                    Columns.COLUMN_NAME.asc())) {

            SchemaDefinition schema = getSchema(record.get(Columns.TABLE_SCHEMA));
            if (schema == null)
                continue enumLoop;

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
                        DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, "");

                        CSVReader reader = new CSVReader(
                            new StringReader(columnType.replaceAll("(^enum\\()|(\\).*$)", ""))
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
        }
    }

    private void getDomainEnums(List<EnumDefinition> result) {

        enumLoop:
        for (Record record : create()
                .select(
                    Domains.DOMAIN_SCHEMA,
                    Domains.DOMAIN_NAME,
                    Domains.SQL)
                .from(Domains.DOMAINS)
                .where(Domains.TYPE_NAME.eq(inline("ENUM")))
                .and(Domains.DOMAIN_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Domains.DOMAIN_SCHEMA,
                    Domains.DOMAIN_NAME)) {

            SchemaDefinition schema = getSchema(record.get(Domains.DOMAIN_SCHEMA));
            if (schema == null)
                continue enumLoop;

            String name = record.get(Domains.DOMAIN_NAME);
            String sql = record.get(Domains.SQL);

            DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, "");

            CSVReader reader = new CSVReader(
                new StringReader(sql.replaceAll("(?i:(^.*as enum\\()|(\\).*$))", ""))
               ,','  // Separator
               ,'\'' // Quote character
               ,true // Strict quotes
            );

            for (String string : reader.next())
                definition.addLiteral(string);

            result.add(definition);
        }
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

    private static Boolean is1_4_197;
    private static Boolean is1_4_198;

    boolean is1_4_197() {

        // [#5874] The COLUMNS.COLUMN_TYPE column was introduced in H2 1.4.197
        if (is1_4_197 == null)
            is1_4_197 = exists(Columns.COLUMN_TYPE);

        return is1_4_197;
    }

    boolean is1_4_198() {

        // [#5874] The COLUMNS.IS_VISIBLE column was introduced in H2 1.4.198
        if (is1_4_198 == null)
            is1_4_198 = exists(Columns.IS_VISIBLE);

        return is1_4_198;
    }
}
