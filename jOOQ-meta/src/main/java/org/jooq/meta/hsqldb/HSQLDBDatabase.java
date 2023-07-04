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

package org.jooq.meta.hsqldb;

import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.hsqldb.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.meta.hsqldb.information_schema.Tables.COLUMNS;
import static org.jooq.meta.hsqldb.information_schema.Tables.DOMAIN_CONSTRAINTS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.meta.hsqldb.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.meta.hsqldb.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.meta.hsqldb.information_schema.Tables.ROUTINES;
import static org.jooq.meta.hsqldb.information_schema.Tables.SCHEMATA;
import static org.jooq.meta.hsqldb.information_schema.Tables.SEQUENCES;
import static org.jooq.meta.hsqldb.information_schema.Tables.SYSTEM_INDEXINFO;
import static org.jooq.meta.hsqldb.information_schema.Tables.SYSTEM_TABLES;
import static org.jooq.meta.hsqldb.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.meta.hsqldb.information_schema.Tables.VIEWS;
import static org.jooq.meta.hsqldb.information_schema.Tables.TRIGGERS;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
// ...
// ...
// ...
import org.jooq.TableOptions.TableType;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultDomainDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
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
import org.jooq.meta.hsqldb.information_schema.tables.CheckConstraints;
import org.jooq.meta.hsqldb.information_schema.tables.Columns;
import org.jooq.meta.hsqldb.information_schema.tables.DomainConstraints;
import org.jooq.meta.hsqldb.information_schema.tables.KeyColumnUsage;
import org.jooq.meta.hsqldb.information_schema.tables.Triggers;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * The HSQLDB database
 *
 * @author Lukas Eder
 */
public class HSQLDBDatabase extends AbstractDatabase implements ResultQueryDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.HSQLDB);
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        // Same implementation as in H2Database and MySQLDatabase
        Map<Record, Result<Record>> indexes = create()
            .select(
                SYSTEM_INDEXINFO.TABLE_SCHEM,
                SYSTEM_INDEXINFO.TABLE_NAME,
                SYSTEM_INDEXINFO.INDEX_NAME,
                SYSTEM_INDEXINFO.NON_UNIQUE,
                SYSTEM_INDEXINFO.COLUMN_NAME,
                SYSTEM_INDEXINFO.ORDINAL_POSITION,
                SYSTEM_INDEXINFO.ASC_OR_DESC)
            .from(SYSTEM_INDEXINFO)
            .where(SYSTEM_INDEXINFO.TABLE_SCHEM.in(getInputSchemata()))
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : SYSTEM_INDEXINFO.INDEX_NAME.notLike("SYS!_IDX!_%", '!'))
            .orderBy(
                SYSTEM_INDEXINFO.TABLE_SCHEM,
                SYSTEM_INDEXINFO.TABLE_NAME,
                SYSTEM_INDEXINFO.INDEX_NAME,
                SYSTEM_INDEXINFO.ORDINAL_POSITION)
            .fetchGroups(
                new Field[] {
                    SYSTEM_INDEXINFO.TABLE_SCHEM,
                    SYSTEM_INDEXINFO.TABLE_NAME,
                    SYSTEM_INDEXINFO.INDEX_NAME,
                    SYSTEM_INDEXINFO.NON_UNIQUE
                },
                new Field[] {
                    SYSTEM_INDEXINFO.COLUMN_NAME,
                    SYSTEM_INDEXINFO.ORDINAL_POSITION,
                    SYSTEM_INDEXINFO.ASC_OR_DESC
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> cols = entry.getValue();

            final SchemaDefinition tableSchema = getSchema(index.get(SYSTEM_INDEXINFO.TABLE_SCHEM));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(SYSTEM_INDEXINFO.INDEX_NAME);
            final String tableName = index.get(SYSTEM_INDEXINFO.TABLE_NAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = !index.get(SYSTEM_INDEXINFO.NON_UNIQUE, boolean.class);

            // [#6310] [#6620] Function-based indexes are not yet supported
            for (Record column : cols)
                if (table.getColumn(column.get(SYSTEM_INDEXINFO.COLUMN_NAME)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : cols) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(SYSTEM_INDEXINFO.COLUMN_NAME)),
                            "D".equals(column.get(SYSTEM_INDEXINFO.ASC_OR_DESC)) ? SortOrder.DESC : SortOrder.ASC,
                            column.get(SYSTEM_INDEXINFO.ORDINAL_POSITION, int.class)
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
        return keys(schemas, "PRIMARY KEY");
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return keys(schemas, "UNIQUE");
    }

    private ResultQuery<Record6<String, String, String, String, String, Integer>> keys(List<String> schemas, String constraintType) {
        return create()
            .select(
                KEY_COLUMN_USAGE.TABLE_CATALOG,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME,
                KEY_COLUMN_USAGE.ORDINAL_POSITION.coerce(INTEGER))
            .from(KEY_COLUMN_USAGE)
            .where(KEY_COLUMN_USAGE.tableConstraints().CONSTRAINT_TYPE.eq(inline(constraintType)))
            .and(KEY_COLUMN_USAGE.tableConstraints().TABLE_SCHEMA.in(schemas))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc());
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        KeyColumnUsage fkKcu = KEY_COLUMN_USAGE.as("fk_kcu");
        KeyColumnUsage pkKcu = KEY_COLUMN_USAGE.as("pk_kcu");

        Result<?> result = create()
            .select(
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME,
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA,
                TABLE_CONSTRAINTS.TABLE_NAME,
                fkKcu.CONSTRAINT_NAME,
                fkKcu.TABLE_SCHEMA,
                fkKcu.TABLE_NAME,
                fkKcu.COLUMN_NAME,
                pkKcu.COLUMN_NAME
            )
            .from(REFERENTIAL_CONSTRAINTS)
            .join(fkKcu)
                .on(fkKcu.CONSTRAINT_SCHEMA.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA))
                .and(fkKcu.CONSTRAINT_NAME.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME))
            .join(TABLE_CONSTRAINTS)
                .on(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA.eq(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA))
                .and(TABLE_CONSTRAINTS.CONSTRAINT_NAME.eq(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME))
            .join(pkKcu)
                .on(pkKcu.CONSTRAINT_SCHEMA.eq(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA))
                .and(pkKcu.CONSTRAINT_NAME.eq(TABLE_CONSTRAINTS.CONSTRAINT_NAME))
                .and(pkKcu.ORDINAL_POSITION.eq(fkKcu.POSITION_IN_UNIQUE_CONSTRAINT))
            .where(fkKcu.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                fkKcu.TABLE_SCHEMA.asc(),
                fkKcu.TABLE_NAME.asc(),
                fkKcu.CONSTRAINT_NAME.asc(),
                fkKcu.ORDINAL_POSITION.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.get(fkKcu.TABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.get(fkKcu.CONSTRAINT_NAME);
            String foreignKeyTableName = record.get(fkKcu.TABLE_NAME);
            String foreignKeyColumn = record.get(fkKcu.COLUMN_NAME);
            String uniqueKey = record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME);
            String uniqueKeyTableName = record.get(TABLE_CONSTRAINTS.TABLE_NAME);
            String uniqueKeyColumn = record.get(pkKcu.COLUMN_NAME);

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
        Columns c = COLUMNS.as("c");

        // [#2808] [#3019] Workaround for bad handling of JOIN .. USING
        Field<String> constraintName = field(name(cc.CONSTRAINT_NAME.getName()), String.class);

        for (Record record : create()
                .select(
                    cc.tableConstraints().TABLE_SCHEMA,
                    cc.tableConstraints().TABLE_NAME,
                    constraintName,
                    cc.CHECK_CLAUSE
                 )
                .from(cc)
                .where(cc.tableConstraints().TABLE_SCHEMA.in(getInputSchemata()))
                .and(getIncludeSystemCheckConstraints()
                    ? noCondition()
                    : cc.tableConstraints().CONSTRAINT_NAME.notLike("SYS!_CT!_%", '!')
                        .or(cc.CHECK_CLAUSE.notIn(

                            // TODO: Should we ever quote these?
                            select(c.TABLE_SCHEMA.concat(inline('.'))
                                    .concat(c.TABLE_NAME).concat(inline('.'))
                                    .concat(c.COLUMN_NAME).concat(inline(" IS NOT NULL")))
                            .from(c)
                            .where(c.TABLE_SCHEMA.eq(cc.tableConstraints().TABLE_SCHEMA))
                            .and(c.TABLE_NAME.eq(cc.tableConstraints().TABLE_NAME))
                        )))
        ) {

            SchemaDefinition schema = getSchema(record.get(cc.tableConstraints().TABLE_SCHEMA));
            TableDefinition table = getTable(schema, record.get(cc.tableConstraints().TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(constraintName),
                    record.get(cc.CHECK_CLAUSE)
                ));
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
                .fetch(mapping(s -> new SchemaDefinition(this, s, "")));
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                VIEWS.TABLE_CATALOG,
                VIEWS.TABLE_SCHEMA,
                VIEWS.TABLE_NAME,
                VIEWS.VIEW_DEFINITION)
            .from(VIEWS)
            .where(VIEWS.TABLE_SCHEMA.in(schemas))
            .orderBy(
                VIEWS.TABLE_SCHEMA,
                VIEWS.TABLE_NAME)
        ;
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).as("catalog"),
                SEQUENCES.SEQUENCE_SCHEMA,
                SEQUENCES.SEQUENCE_NAME,
                SEQUENCES.DATA_TYPE,
                SEQUENCES.NUMERIC_PRECISION.coerce(INTEGER),
                SEQUENCES.NUMERIC_SCALE.coerce(INTEGER),
                SEQUENCES.START_WITH.coerce(BIGINT),
                SEQUENCES.INCREMENT.coerce(BIGINT),
                SEQUENCES.MINIMUM_VALUE.coerce(NUMERIC),
                SEQUENCES.MAXIMUM_VALUE.coerce(NUMERIC),
                decode(SEQUENCES.CYCLE_OPTION, inline("YES"), inline(true), inline(false)).as(SEQUENCES.CYCLE_OPTION),
                inline(null, BIGINT).as("cache"))
            .from(SEQUENCES)
            .where(SEQUENCES.SEQUENCE_SCHEMA.in(schemas))
            .orderBy(
                SEQUENCES.SEQUENCE_SCHEMA,
                SEQUENCES.SEQUENCE_NAME);
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : sequences(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(SEQUENCES.SEQUENCE_SCHEMA));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get(SEQUENCES.DATA_TYPE)
            );

            result.add(new DefaultSequenceDefinition(
                schema, record.get(SEQUENCES.SEQUENCE_NAME), type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create()
                .select(
                    SYSTEM_TABLES.TABLE_SCHEM,
                    SYSTEM_TABLES.TABLE_NAME,
                    inline("").as(ROUTINES.SPECIFIC_NAME),
                    SYSTEM_TABLES.REMARKS,
                    when(SYSTEM_TABLES.TABLE_TYPE.eq(inline("VIEW")), inline(TableType.VIEW.name()))
                        .else_(inline(TableType.TABLE.name())).trim().as("table_type"),
                    when(VIEWS.VIEW_DEFINITION.lower().like(inline("create%")), VIEWS.VIEW_DEFINITION)
                        .else_(inline("create view \"").concat(SYSTEM_TABLES.TABLE_NAME).concat("\" as ").concat(VIEWS.VIEW_DEFINITION)).as(VIEWS.VIEW_DEFINITION)
                )
                .from(SYSTEM_TABLES)
                .leftJoin(VIEWS)
                    .on(SYSTEM_TABLES.TABLE_SCHEM.eq(VIEWS.TABLE_SCHEMA))
                    .and(SYSTEM_TABLES.TABLE_NAME.eq(VIEWS.TABLE_NAME))
                .where(SYSTEM_TABLES.TABLE_SCHEM.in(getInputSchemata()))
                .unionAll(tableValuedFunctions()
                    ? select(
                        ROUTINES.ROUTINE_SCHEMA,
                        ROUTINES.ROUTINE_NAME,
                        ROUTINES.SPECIFIC_NAME,
                        inline(""),
                        inline(TableType.FUNCTION.name()),
                        ROUTINES.ROUTINE_DEFINITION)
                      .from(ROUTINES)
                      .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
                      .and(ROUTINES.ROUTINE_TYPE.eq(inline("FUNCTION")))
                      .and(ROUTINES.DATA_TYPE.startsWith(inline("ROW(")))
                    : select(inline(""), inline(""), inline(""), inline(""), inline(TableType.FUNCTION.name()), inline(""))
                      .where(falseCondition())
                )
                .orderBy(
                    SYSTEM_TABLES.TABLE_SCHEM,
                    SYSTEM_TABLES.TABLE_NAME).fetch()) {

            SchemaDefinition schema = getSchema(record.get(SYSTEM_TABLES.TABLE_SCHEM));
            String name = record.get(SYSTEM_TABLES.TABLE_NAME);
            String specificName = record.get(ROUTINES.SPECIFIC_NAME);
            String comment = record.get(SYSTEM_TABLES.REMARKS);
            TableType tableType = record.get("table_type", TableType.class);
            String source = record.get(VIEWS.VIEW_DEFINITION);


            if (tableType == TableType.FUNCTION)
                result.add(new HSQLDBTableValuedFunction(schema, name, specificName, comment, source));
            else
                result.add(new HSQLDBTableDefinition(schema, name, comment, tableType, source));
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

        DomainConstraints dc = DOMAIN_CONSTRAINTS.as("dc");

        for (Record record : create()
            .select(
                dc.domains().DOMAIN_SCHEMA,
                dc.domains().DOMAIN_NAME,
                dc.domains().DATA_TYPE,
                dc.domains().CHARACTER_MAXIMUM_LENGTH,
                dc.domains().NUMERIC_PRECISION,
                dc.domains().NUMERIC_SCALE,
                dc.domains().DOMAIN_DEFAULT,
                dc.checkConstraints().CHECK_CLAUSE)
            .from(dc)
            .where(dc.domains().DOMAIN_SCHEMA.in(getInputSchemata()))
            .orderBy(dc.domains().DOMAIN_SCHEMA, dc.domains().DOMAIN_NAME)
        ) {
            SchemaDefinition schema = getSchema(record.get(dc.domains().DOMAIN_SCHEMA));

            DataTypeDefinition baseType = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get(dc.domains().DATA_TYPE),
                record.get(dc.domains().CHARACTER_MAXIMUM_LENGTH),
                record.get(dc.domains().NUMERIC_PRECISION),
                record.get(dc.domains().NUMERIC_SCALE),
                true,
                record.get(dc.domains().DOMAIN_DEFAULT)
            );

            DefaultDomainDefinition domain = new DefaultDomainDefinition(
                schema,
                record.get(dc.domains().DOMAIN_NAME),
                baseType
            );

            if (!StringUtils.isBlank(record.get(dc.checkConstraints().CHECK_CLAUSE)))
                domain.addCheckClause(record.get(dc.checkConstraints().CHECK_CLAUSE));

            result.add(domain);
        }

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

        for (Record record : create()
                .select(
                    ROUTINES.ROUTINE_SCHEMA,
                    ROUTINES.ROUTINE_NAME,
                    ROUTINES.SPECIFIC_NAME,
                    nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER, ROUTINES.DATA_TYPE).as("datatype"),
                    ROUTINES.NUMERIC_PRECISION,
                    ROUTINES.NUMERIC_SCALE,
                    ROUTINES.ROUTINE_DEFINITION.likeRegex(".*(?i:(\\w+\\s+)+aggregate\\s+function).*").as("aggregate"))
                .from(ROUTINES)
                .leftOuterJoin(ELEMENT_TYPES)
                .on(ROUTINES.ROUTINE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
                .and(ROUTINES.ROUTINE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
                .and(ROUTINES.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
                .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
                .and(tableValuedFunctions()
                    ? ROUTINES.DATA_TYPE.isNull().or(ROUTINES.DATA_TYPE.notLike(inline("ROW(%")))
                    : noCondition())
                .orderBy(
                    ROUTINES.ROUTINE_SCHEMA,
                    ROUTINES.ROUTINE_NAME)
                .fetch()) {

            String datatype = record.get("datatype", String.class);

            // [#3285] We currently do not recognise HSQLDB table-valued functions as such.
            if (datatype != null && datatype.toUpperCase().startsWith("ROW")) {
                JooqLogger.getLogger(getClass()).info("A row : " +datatype);
                datatype = "ROW";
            }

            result.add(new HSQLDBRoutineDefinition(
                getSchema(record.get(ROUTINES.ROUTINE_SCHEMA)),
                record.get(ROUTINES.ROUTINE_NAME),
                record.get(ROUTINES.SPECIFIC_NAME),
                datatype,
                record.get(ROUTINES.NUMERIC_PRECISION),
                record.get(ROUTINES.NUMERIC_SCALE),
                record.get("aggregate", boolean.class)));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }
}
