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
package org.jooq.meta.firebird;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.bitGet;
import static org.jooq.impl.DSL.bitOr;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.SMALLINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.firebird.rdb.Tables.RDB$CHECK_CONSTRAINTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FIELDS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FUNCTIONS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$FUNCTION_ARGUMENTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$GENERATORS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$INDEX_SEGMENTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$INDICES;
import static org.jooq.meta.firebird.rdb.Tables.RDB$PROCEDURES;
import static org.jooq.meta.firebird.rdb.Tables.RDB$REF_CONSTRAINTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$RELATIONS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$RELATION_CONSTRAINTS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$RELATION_FIELDS;
import static org.jooq.meta.firebird.rdb.Tables.RDB$TRIGGERS;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.jooq.TableOptions.TableType;
// ...
// ...
import org.jooq.impl.DSL;
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
import org.jooq.meta.firebird.rdb.tables.Rdb$checkConstraints;
import org.jooq.meta.firebird.rdb.tables.Rdb$fields;
import org.jooq.meta.firebird.rdb.tables.Rdb$functionArguments;
import org.jooq.meta.firebird.rdb.tables.Rdb$functions;
import org.jooq.meta.firebird.rdb.tables.Rdb$indexSegments;
import org.jooq.meta.firebird.rdb.tables.Rdb$indices;
import org.jooq.meta.firebird.rdb.tables.Rdb$procedures;
import org.jooq.meta.firebird.rdb.tables.Rdb$refConstraints;
import org.jooq.meta.firebird.rdb.tables.Rdb$relationConstraints;
import org.jooq.meta.firebird.rdb.tables.Rdb$triggers;
import org.jooq.meta.jaxb.SchemaMappingType;
import org.jooq.tools.StringUtils;
import org.jooq.util.firebird.FirebirdDataType;

/**
 * @author Sugiharto Lim - Initial contribution
 */
public class FirebirdDatabase extends AbstractDatabase implements ResultQueryDatabase {

    private Boolean is30;

    public FirebirdDatabase() {

        // Firebird doesn't know schemata
        SchemaMappingType schema = new SchemaMappingType();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<SchemaMappingType> schemata = new ArrayList<>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) throws SQLException {
        for (Record record : primaryKeys(Collections.<String>emptyList())) {
            String tableName = record.get(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME);
            String fieldName = record.get(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME);
            String key = record.get(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME);

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null)
                r.addPrimaryKey(key, td, td.getColumn(fieldName));
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
        for (Record record : uniqueKeys(Collections.<String>emptyList())) {
            String tableName = record.get(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME);
            String fieldName = record.get(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME);
            String key = record.get(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME);

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null)
                r.addUniqueKey(key, td, td.getColumn(fieldName));
        }
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return keys("PRIMARY KEY");
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return keys("UNIQUE");
    }

    private ResultQuery<Record6<String, String, String, String, String, Integer>> keys(String constraintType) {
        return create()
            .select(
                inline(null, VARCHAR).as("catalog"),
                inline(null, VARCHAR).as("schema"),
                trim(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME).as(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME),
                trim(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME).as(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME),
                trim(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME).as(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME),
                RDB$INDEX_SEGMENTS.RDB$FIELD_POSITION.coerce(INTEGER))
            .from(RDB$RELATION_CONSTRAINTS)
            .join(RDB$INDEX_SEGMENTS)
            .on(RDB$INDEX_SEGMENTS.RDB$INDEX_NAME.eq(RDB$RELATION_CONSTRAINTS.RDB$INDEX_NAME))
            .where(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE.eq(inline(constraintType)))
            .orderBy(
                RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.asc(),
                RDB$INDEX_SEGMENTS.RDB$FIELD_POSITION.asc());
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Rdb$relationConstraints pk = RDB$RELATION_CONSTRAINTS.as("pk");
        Rdb$relationConstraints fk = RDB$RELATION_CONSTRAINTS.as("fk");
        Rdb$refConstraints rc = RDB$REF_CONSTRAINTS.as("rc");
        Rdb$indexSegments isp = RDB$INDEX_SEGMENTS.as("isp");
        Rdb$indexSegments isf = RDB$INDEX_SEGMENTS.as("isf");

        for (Record record : create()
                .selectDistinct(
                    trim(fk.RDB$CONSTRAINT_NAME).as("fk"),
                    trim(fk.RDB$RELATION_NAME).as("fkTable"),
                    trim(isf.RDB$FIELD_NAME).as("fkField"),
                    trim(pk.RDB$CONSTRAINT_NAME).as("pk"),
                    trim(pk.RDB$RELATION_NAME).as("pkTable"))
                .from(fk)
                .join(rc).on(fk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONSTRAINT_NAME))
                .join(pk).on(pk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONST_NAME_UQ))
                .join(isp).on(isp.RDB$INDEX_NAME.eq(pk.RDB$INDEX_NAME))
                .join(isf).on(isf.RDB$INDEX_NAME.eq(fk.RDB$INDEX_NAME))
                .where(isp.RDB$FIELD_POSITION.eq(isf.RDB$FIELD_POSITION))
                .orderBy(
                    fk.RDB$CONSTRAINT_NAME.asc(),
                    isf.RDB$FIELD_POSITION.asc())
                .fetch()) {

            String pkName = record.get("pk", String.class);
            String pkTable = record.get("pkTable", String.class);

            String fkName = record.get("fk", String.class);
            String fkTable = record.get("fkTable", String.class);
            String fkField = record.get("fkField", String.class);

            TableDefinition foreignKeyTable = getTable(getSchemata().get(0), fkTable, true);
            TableDefinition primaryKeyTable = getTable(getSchemata().get(0), pkTable, true);

            if (primaryKeyTable != null && foreignKeyTable != null)
                relations.addForeignKey(
                    fkName,
                    foreignKeyTable,
                    foreignKeyTable.getColumn(fkField),
                    pkName,
                    primaryKeyTable
                );
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        Rdb$relationConstraints r = RDB$RELATION_CONSTRAINTS.as("r");
        Rdb$checkConstraints c = RDB$CHECK_CONSTRAINTS.as("c");
        Rdb$triggers t = RDB$TRIGGERS.as("t");

        // [#7639] RDB$TRIGGERS is not in 3NF. The RDB$TRIGGER_SOURCE is repeated
        //         for RDB$TRIGGER_TYPE 1 (before insert) and 3 (before update)
        for (Record record : create()
            .select(
                trim(r.RDB$RELATION_NAME).as(r.RDB$RELATION_NAME),
                trim(r.RDB$CONSTRAINT_NAME).as(r.RDB$CONSTRAINT_NAME),
                max(trim(t.RDB$TRIGGER_SOURCE)).as(t.RDB$TRIGGER_SOURCE)
            )
            .from(r)
            .join(c).on(r.RDB$CONSTRAINT_NAME.eq(c.RDB$CONSTRAINT_NAME))
            .join(t).on(c.RDB$TRIGGER_NAME.eq(t.RDB$TRIGGER_NAME))
            .where(r.RDB$CONSTRAINT_TYPE.eq(inline("CHECK")))
            .groupBy(
                r.RDB$RELATION_NAME,
                r.RDB$CONSTRAINT_NAME
            )
            .orderBy(
                r.RDB$RELATION_NAME,
                r.RDB$CONSTRAINT_NAME
            )
        ) {
            SchemaDefinition schema = getSchemata().get(0);
            TableDefinition table = getTable(schema, record.get(r.RDB$RELATION_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(r.RDB$CONSTRAINT_NAME),
                    record.get(t.RDB$TRIGGER_SOURCE)
                ));
            }
        }
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        final List<IndexDefinition> result = new ArrayList<>();

        final Rdb$relationConstraints c = RDB$RELATION_CONSTRAINTS.as("c");
        final Rdb$indices i = RDB$INDICES.as("i");
        final Rdb$indexSegments s = RDB$INDEX_SEGMENTS.as("s");

        Map<Record, Result<Record>> indexes = create()
            .select(
                trim(s.rdb$indices().RDB$RELATION_NAME).as(i.RDB$RELATION_NAME),
                trim(s.rdb$indices().RDB$INDEX_NAME).as(i.RDB$INDEX_NAME),
                s.rdb$indices().RDB$UNIQUE_FLAG,
                trim(s.RDB$FIELD_NAME).as(s.RDB$FIELD_NAME),
                s.RDB$FIELD_POSITION)
            .from(s)
            .where(s.rdb$indices().RDB$INDEX_NAME.notIn(select(c.RDB$CONSTRAINT_NAME).from(c)))
            .orderBy(
                s.rdb$indices().RDB$RELATION_NAME,
                s.rdb$indices().RDB$INDEX_NAME,
                s.RDB$FIELD_POSITION)
            .fetchGroups(
                new Field[] {
                    i.RDB$RELATION_NAME,
                    i.RDB$INDEX_NAME,
                    i.RDB$UNIQUE_FLAG
                },
                new Field[] {
                    s.RDB$FIELD_NAME,
                    s.RDB$FIELD_POSITION
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();
            final SchemaDefinition schema = getSchemata().get(0);

            final String indexName = index.get(i.RDB$INDEX_NAME);
            final String tableName = index.get(i.RDB$RELATION_NAME);
            final TableDefinition table = getTable(schema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = index.get(i.RDB$UNIQUE_FLAG, boolean.class);

            // [#6310] [#6620] Function-based indexes are not yet supported
            // [#16237]        Alternatively, the column could be hidden or excluded
            for (Record column : columns)
                if (table.getColumn(column.get(s.RDB$FIELD_NAME)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(schema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(s.RDB$FIELD_NAME)),
                            SortOrder.ASC,
                            column.get(s.RDB$FIELD_POSITION, int.class)
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
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<>();
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).as("catalog"),
                inline(null, VARCHAR).as("schema"),
                trim(RDB$RELATIONS.RDB$RELATION_NAME),
                when(lower(RDB$RELATIONS.RDB$VIEW_SOURCE).like(inline("create%")), trim(RDB$RELATIONS.RDB$VIEW_SOURCE))
                .else_(inline("create view \"").concat(trim(RDB$RELATIONS.RDB$RELATION_NAME)).concat(inline("\" as ")).concat(RDB$RELATIONS.RDB$VIEW_SOURCE)).as("view_source"))
            .from(RDB$RELATIONS)
            .orderBy(trim(RDB$RELATIONS.RDB$RELATION_NAME));
    }

    @Override
    public ResultQuery<Record5<String, String, String, String, String>> comments(List<String> schemas) {
        Table<?> c =
            select(
                inline(null, VARCHAR).as("catalog"),
                inline(null, VARCHAR).as("schema"),
                trim(RDB$RELATIONS.RDB$RELATION_NAME).as(RDB$RELATIONS.RDB$RELATION_NAME),
                inline(null, VARCHAR).as(RDB$RELATION_FIELDS.RDB$FIELD_NAME),
                trim(RDB$RELATIONS.RDB$DESCRIPTION).as(RDB$RELATIONS.RDB$DESCRIPTION))
            .from(RDB$RELATIONS)
            .where(RDB$RELATIONS.RDB$DESCRIPTION.isNotNull())
            .unionAll(
                select(
                    inline(null, VARCHAR),
                    inline(null, VARCHAR),
                    RDB$RELATION_FIELDS.RDB$RELATION_NAME,
                    RDB$RELATION_FIELDS.RDB$FIELD_NAME,
                    RDB$RELATION_FIELDS.RDB$DESCRIPTION)
                .from(RDB$RELATION_FIELDS)
                .where(RDB$RELATION_FIELDS.RDB$DESCRIPTION.isNotNull()))
            .asTable("c");

        return create()
            .select(
                c.field("catalog", VARCHAR),
                c.field("schema", VARCHAR),
                c.field(RDB$RELATIONS.RDB$RELATION_NAME),
                c.field(RDB$RELATION_FIELDS.RDB$FIELD_NAME),
                c.field(RDB$RELATIONS.RDB$DESCRIPTION))
            .from(c)
            .orderBy(1, 2, 3);
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).as("catalog"),
                inline(null, VARCHAR).as("schema"),
                trim(RDB$GENERATORS.RDB$GENERATOR_NAME).as(RDB$GENERATORS.RDB$GENERATOR_NAME),
                inline("BIGINT").as("type_name"),
                inline(null, INTEGER).as("numeric_precision"),
                inline(null, INTEGER).as("numeric_scale"),
                (is30()
                    ? nullif(RDB$GENERATORS.RDB$INITIAL_VALUE, inline(0L))
                    : inline(0L))
                .as(RDB$GENERATORS.RDB$INITIAL_VALUE),
                (is30()
                    ? nullif(RDB$GENERATORS.RDB$GENERATOR_INCREMENT, inline(1))
                    : inline(1)).coerce(BIGINT).as(RDB$GENERATORS.RDB$GENERATOR_INCREMENT),
                inline(null, NUMERIC).as("min_value"),
                inline(null, NUMERIC).as("max_value"),
                inline(null, BOOLEAN).as("cycle"),
                inline(null, BIGINT).as("cache")
            )
            .from(RDB$GENERATORS)
            .where(getIncludeSystemSequences() || !is30()
                ? noCondition()
                : RDB$GENERATORS.RDB$GENERATOR_NAME.notIn(
                    select(RDB$RELATION_FIELDS.RDB$GENERATOR_NAME)
                    .from(RDB$RELATION_FIELDS)
                    .where(RDB$RELATION_FIELDS.RDB$GENERATOR_NAME.isNotNull())
                    .and(RDB$RELATION_FIELDS.RDB$IDENTITY_TYPE.eq(inline((short) 1)))
                )
            )
            .orderBy(RDB$GENERATORS.RDB$GENERATOR_NAME);
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : sequences(getInputSchemata())) {
            SchemaDefinition schema = getSchemata().get(0);
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema, FirebirdDataType.BIGINT.getTypeName()
            );

            result.add(new DefaultSequenceDefinition(
                schema,
                record.get(RDB$GENERATORS.RDB$GENERATOR_NAME),
                type,
                null,
                record.get(RDB$GENERATORS.RDB$INITIAL_VALUE),
                record.get(RDB$GENERATORS.RDB$GENERATOR_INCREMENT),
                null,
                null,
                false,
                null
            ));
        }

        return result;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> enums(List<String> schemas) {
        return null;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record4<String, String, String, String> record : create()
                .select(
                    trim(RDB$RELATIONS.RDB$RELATION_NAME),
                    trim(RDB$RELATIONS.RDB$DESCRIPTION),
                    trim(when(RDB$RELATIONS.RDB$RELATION_TYPE.eq(inline((short) 1)), inline(TableType.VIEW.name()))
                        .else_(inline(TableType.TABLE.name()))).as("table_type"),
                    when(lower(RDB$RELATIONS.RDB$VIEW_SOURCE).like(inline("create%")), trim(RDB$RELATIONS.RDB$VIEW_SOURCE))
                        .else_(inline("create view \"").concat(trim(RDB$RELATIONS.RDB$RELATION_NAME)).concat("\" as ").concat(RDB$RELATIONS.RDB$VIEW_SOURCE)).as("view_source"))
                .from(RDB$RELATIONS)
                .unionAll(
                     select(
                         trim(RDB$PROCEDURES.RDB$PROCEDURE_NAME),
                         inline(""),
                         trim(inline(TableType.FUNCTION.name())),
                         inline(""))
                    .from(RDB$PROCEDURES)

                    // "selectable" procedures
                    .where(RDB$PROCEDURES.RDB$PROCEDURE_TYPE.eq((short) 1))
                    .and(tableValuedFunctions()
                        ? noCondition()
                        : falseCondition())
                )
                .orderBy(1)) {

            TableType tableType = record.get("table_type", TableType.class);

            if (TableType.FUNCTION == tableType)
                result.add(new FirebirdTableValuedFunction(getSchemata().get(0), record.value1(), ""));
            else
                result.add(new FirebirdTableDefinition(getSchemata().get(0), record.value1(), record.value2(), tableType, record.value4()));
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        Rdb$procedures p = RDB$PROCEDURES.as("p");
        Rdb$functions fu = RDB$FUNCTIONS.as("fu");
        Rdb$functionArguments fa = RDB$FUNCTION_ARGUMENTS.as("fa");
        Rdb$fields fi = RDB$FIELDS.as("fi");

        return
        create().select(
                    trim(p.RDB$PROCEDURE_NAME),
                    inline(null, VARCHAR).as("t"),
                    inline(null, SMALLINT).as("p"),
                    inline(null, SMALLINT).as("s"))
                .from(p)

                // "executable" procedures
                .where(p.RDB$PROCEDURE_TYPE.eq((short) 2))
                .union(is30()
                    ? select(
                        trim(fu.RDB$FUNCTION_NAME),
                        FIELD_TYPE(fi).as("t"),
                        coalesce(CHARACTER_LENGTH(fi), fi.RDB$FIELD_PRECISION).as("p"),
                        FIELD_SCALE(fi).as("s"))
                      .from(fu)

                      // [#11784] Procedures and functions live in different
                      // namespaces in Firebird. For now, such "overloads" are
                      // not yet supported.
                      .leftAntiJoin(p)
                          .on(fu.RDB$FUNCTION_NAME.eq(p.RDB$PROCEDURE_NAME))
                      .join(fa)
                          .on(fu.RDB$FUNCTION_NAME.eq(fa.RDB$FUNCTION_NAME))
                      .leftOuterJoin(fi)
                          .on(fa.RDB$FIELD_SOURCE.eq(fi.RDB$FIELD_NAME))
                      .where(fa.RDB$ARGUMENT_POSITION.eq(inline((short) 0)))
                    : select(inline(""), inline(""), inline((short) 0), inline((short) 0)).where(falseCondition()))
                .orderBy(1)
                .collect(mapping(
                    r -> new FirebirdRoutineDefinition(
                        getSchemata().get(0),
                        r.get(0, String.class),
                        r.get(1, String.class),
                        r.get(2, Integer.class),
                        r.get(3, Integer.class)
                    ),
                    Collectors.<RoutineDefinition>toList()
                ));
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
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

        Rdb$fields f = RDB$FIELDS;

        for (Record record : create()
            .select(
                trim(f.RDB$FIELD_NAME).as(f.RDB$FIELD_NAME),

                CHARACTER_LENGTH(f).as("CHAR_LEN"),
                f.RDB$FIELD_PRECISION,
                FIELD_SCALE(f).as("FIELD_SCALE"),
                FIELD_TYPE(f).as("FIELD_TYPE"),
                bitOr(nvl(f.RDB$NULL_FLAG, (short) 0), nvl(f.RDB$NULL_FLAG, (short) 0)).as(f.RDB$NULL_FLAG),
                trim(f.RDB$VALIDATION_SOURCE).as(f.RDB$VALIDATION_SOURCE),
                trim(f.RDB$DEFAULT_SOURCE).as(f.RDB$DEFAULT_SOURCE))
            .from(f)
            .where(f.RDB$FIELD_NAME.notLike(any("RDB$%", "SEC$%", "MON$%")))
            .orderBy(f.RDB$FIELD_NAME)
        ) {
            SchemaDefinition schema = getSchemata().get(0);

            DataTypeDefinition baseType = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get("FIELD_TYPE", String.class),
                record.get("CHAR_LEN", short.class),
                record.get(f.RDB$FIELD_PRECISION),
                record.get("FIELD_SCALE", Integer.class),
                record.get(f.RDB$NULL_FLAG) == 0,
                record.get(f.RDB$DEFAULT_SOURCE) == null ? null : record.get(f.RDB$DEFAULT_SOURCE).replaceAll("(?i:default )", "")
            );

            DefaultDomainDefinition domain = new DefaultDomainDefinition(
                schema,
                record.get(f.RDB$FIELD_NAME),
                baseType
            );

            if (!StringUtils.isBlank(record.get(f.RDB$VALIDATION_SOURCE)))
                domain.addCheckClause(record.get(f.RDB$VALIDATION_SOURCE).replaceAll("(?i:check )", ""));

            result.add(domain);
        }

        return result;
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
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.FIREBIRD);
    }

    static Field<String> FIELD_TYPE(Rdb$fields f) {
        return decode().value(f.RDB$FIELD_TYPE)
                .when((short) 7, decode()
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                     .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                    .otherwise("SMALLINT"))
                .when((short) 8, decode()
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                     .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                    .otherwise("INTEGER"))
                .when((short) 9, "QUAD")
                .when((short) 10, "FLOAT")
                .when((short) 11, "D_FLOAT")
                .when((short) 12, "DATE")
                .when((short) 13, "TIME")
                .when((short) 14, "CHAR")
                .when((short) 16, decode()
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 1), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 0)
                     .and(f.RDB$FIELD_SCALE.lt((short) 0)), "NUMERIC")
                    .when(f.RDB$FIELD_SUB_TYPE.eq((short) 2), "DECIMAL")
                    .otherwise("BIGINT"))
                .when((short) 27, "DOUBLE")
                .when((short) 35, "TIMESTAMP")
                .when((short) 37, "VARCHAR")
                .when((short) 40, "CSTRING")
                .when((short) 261, decode().value(f.RDB$FIELD_SUB_TYPE)
                    .when((short) 0, "BLOB")
                    .when((short) 1, "BLOB SUB_TYPE TEXT")
                    .otherwise("BLOB"))
                .otherwise("UNKNOWN");
    }

    static Field<Short> FIELD_SCALE(Rdb$fields f) {
        return f.RDB$FIELD_SCALE.neg();
    }

    static Field<Short> CHARACTER_LENGTH(Rdb$fields f) {
        return choose(f.RDB$FIELD_TYPE)
                .when((short) 261, (short) 0)
                .otherwise(f.RDB$CHARACTER_LENGTH);
    }

    boolean is30() {

        // [#4254] RDB$GENERATORS.RDB$INITIAL_VALUE was added in Firebird 3.0 only
        if (is30 == null)
            is30 = configuredDialectIsNotFamilyAndSupports(asList(FIREBIRD), () -> exists(RDB$GENERATORS.RDB$INITIAL_VALUE));

        return is30;
    }
}
