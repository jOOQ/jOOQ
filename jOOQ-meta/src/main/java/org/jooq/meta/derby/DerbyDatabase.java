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

package org.jooq.meta.derby;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.derby.sys.Tables.*;
import static org.jooq.meta.derby.sys.Tables.SYSCONGLOMERATES;
import static org.jooq.meta.derby.sys.Tables.SYSCONSTRAINTS;
import static org.jooq.meta.derby.sys.Tables.SYSKEYS;
import static org.jooq.meta.derby.sys.Tables.SYSSCHEMAS;
import static org.jooq.meta.derby.sys.Tables.SYSSEQUENCES;
import static org.jooq.meta.derby.sys.Tables.SYSTABLES;
import static org.jooq.meta.derby.sys.Tables.SYSVIEWS;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Record4;
import org.jooq.Record5;
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
import org.jooq.meta.derby.sys.tables.Systriggers;

/**
 * @author Lukas Eder
 */
public class DerbyDatabase extends AbstractDatabase implements ResultQueryDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.get(SYSSCHEMAS.SCHEMANAME));
            String key = record.get(SYSCONSTRAINTS.CONSTRAINTNAME);
            String tableName = record.get(SYSTABLES.TABLENAME);
            String descriptor = record.get(SYSCONGLOMERATES.DESCRIPTOR, String.class);

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                for (int index : decode(descriptor))
                    relations.addPrimaryKey(key, table, table.getColumn(index));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.get(SYSSCHEMAS.SCHEMANAME));
            String key = record.get(SYSCONSTRAINTS.CONSTRAINTNAME);
            String tableName = record.get(SYSTABLES.TABLENAME);
            String descriptor = record.get(SYSCONGLOMERATES.DESCRIPTOR, String.class);

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                for (int index : decode(descriptor))
                    relations.addUniqueKey(key, table, table.getColumn(index));
        }
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return null;
    }

    private Result<Record5<String, String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                    SYSKEYS.sysconglomerates().systables().sysschemas().SCHEMANAME,
                    SYSKEYS.sysconglomerates().systables().TABLENAME,
                    SYSKEYS.sysconglomerates().systables().TABLEID,
                    SYSKEYS.sysconstraints().CONSTRAINTNAME,
                    SYSKEYS.sysconglomerates().DESCRIPTOR)
                .from(SYSKEYS)
                // [#6797] The casts are necessary if a non-standard collation is used
                .where(SYSKEYS.sysconglomerates().systables().sysschemas().SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
                .and(SYSKEYS.sysconstraints().TYPE.cast(VARCHAR(32672)).equal(constraintType))
                .orderBy(
                    SYSKEYS.sysconglomerates().systables().sysschemas().SCHEMANAME,
                    SYSKEYS.sysconglomerates().systables().TABLENAME,
                    SYSKEYS.sysconstraints().CONSTRAINTNAME)
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Field<String> fkName = field("fc.constraintname", String.class);
        Field<String> fkTable = field("ft.tablename", String.class);
        Field<String> fkSchema = field("fs.schemaname", String.class);
        Field<?> fkDescriptor = field("fg.descriptor");
        Field<String> ukName = field("pc.constraintname", String.class);
        Field<String> ukTable = field("pt.tablename", String.class);
        Field<String> ukSchema = field("ps.schemaname", String.class);

        for (Record record : create().select(
                fkName,
                fkTable,
                fkSchema,
                fkDescriptor,
                ukName,
                ukTable,
                ukSchema)
            .from("sys.sysconstraints   fc")
            .join("sys.sysforeignkeys   f ").on("f.constraintid = fc.constraintid")
            .join("sys.sysconglomerates fg").on("fg.conglomerateid = f.conglomerateid")
            .join("sys.systables        ft").on("ft.tableid = fg.tableid")
            .join("sys.sysschemas       fs").on("ft.schemaid = fs.schemaid")
            .join("sys.sysconstraints   pc").on("pc.constraintid = f.keyconstraintid")
            .join("sys.systables        pt").on("pt.tableid = pc.tableid")
            .join("sys.sysschemas       ps").on("ps.schemaid = pt.schemaid")
            // [#6797] The cast is necessary if a non-standard collation is used
            .where("cast(fc.type as varchar(32672)) = 'F'")
            .orderBy(fkSchema, fkTable, fkName)
        ) {

            SchemaDefinition foreignKeySchema = getSchema(record.get(fkSchema));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(ukSchema));

            String foreignKeyName = record.get(fkName);
            String foreignKeyTableName = record.get(fkTable);
            List<Integer> foreignKeyIndexes = decode(record.get(fkDescriptor, String.class));
            String uniqueKeyName = record.get(ukName);
            String uniqueKeyTableName = record.get(ukTable);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
            TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

            if (foreignKeyTable != null && uniqueKeyTable != null)
                for (int i = 0; i < foreignKeyIndexes.size(); i++)
                    relations.addForeignKey(
                        foreignKeyName,
                        foreignKeyTable,
                        foreignKeyTable.getColumn(foreignKeyIndexes.get(i)),
                        uniqueKeyName,
                        uniqueKeyTable
                    );
        }
    }

    /*
     * Unfortunately the descriptor interface is not exposed publicly Hence, the
     * toString() method is used and its results are parsed The results are
     * something like UNIQUE BTREE (index1, index2, ... indexN)
     */
    private List<Integer> decode(String descriptor) {
        List<Integer> result = new ArrayList<>();

        Pattern p = Pattern.compile(".*?\\((.*?)\\)");
        Matcher m = p.matcher(descriptor);

        while (m.find()) {
            String[] split = m.group(1).split(",");

            if (split != null)
                for (String index : split)
                    result.add(Integer.parseInt(index.trim()) - 1);
        }

        return result;
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        for (Record record : create()
            .select(
                SYSCHECKS.sysconstraints().systables().sysschemas().SCHEMANAME,
                SYSCHECKS.sysconstraints().systables().TABLENAME,
                SYSCHECKS.sysconstraints().CONSTRAINTNAME,
                SYSCHECKS.CHECKDEFINITION)
            .from(SYSCHECKS)
            .where(SYSCHECKS.sysconstraints().systables().sysschemas().SCHEMANAME.in(getInputSchemata()))
        ) {
            SchemaDefinition schema = getSchema(record.get(SYSCHECKS.sysconstraints().systables().sysschemas().SCHEMANAME));
            TableDefinition table = getTable(schema, record.get(SYSCHECKS.sysconstraints().systables().TABLENAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(SYSCHECKS.sysconstraints().CONSTRAINTNAME),
                    record.get(SYSCHECKS.CHECKDEFINITION)
                ));
            }
        }
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        indexLoop:
        for (Record record : create()
            .select(
                SYSCONGLOMERATES.systables().sysschemas().SCHEMANAME,
                SYSCONGLOMERATES.systables().TABLENAME,
                SYSCONGLOMERATES.CONGLOMERATENAME,
                SYSCONGLOMERATES.DESCRIPTOR)
            .from(SYSCONGLOMERATES)

            // [#6797] The cast is necessary if a non-standard collation is used
            .where(SYSCONGLOMERATES.systables().sysschemas().SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
            .and(SYSCONGLOMERATES.ISINDEX)
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : not(condition(SYSCONGLOMERATES.ISCONSTRAINT)))
            .orderBy(
                SYSCONGLOMERATES.systables().sysschemas().SCHEMANAME,
                SYSCONGLOMERATES.systables().TABLENAME,
                SYSCONGLOMERATES.CONGLOMERATENAME)
        ) {
            final SchemaDefinition tableSchema = getSchema(record.get(SYSCONGLOMERATES.systables().sysschemas().SCHEMANAME));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = record.get(SYSCONGLOMERATES.CONGLOMERATENAME);
            final String tableName = record.get(SYSCONGLOMERATES.systables().TABLENAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final String descriptor = record.get(SYSCONGLOMERATES.DESCRIPTOR);
            if (descriptor == null)
                continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, descriptor.toUpperCase().contains("UNIQUE")) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    List<Integer> columnIndexes = decode(descriptor);
                    for (int i = 0; i < columnIndexes.size(); i++) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(columnIndexes.get(i)),
                            SortOrder.ASC,
                            i + 1
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
        return
        create().select(SYSSCHEMAS.SCHEMANAME)
                .from(SYSSCHEMAS)
                .collect(mapping(r -> new SchemaDefinition(this, r.value1(), ""), toList()));
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).cast(VARCHAR).as("catalog"),
                SYSTABLES.sysschemas().SCHEMANAME,
                SYSTABLES.TABLENAME,
                SYSVIEWS.VIEWDEFINITION)
            .from(SYSTABLES)
            .leftJoin(SYSVIEWS)
                .on(SYSTABLES.TABLEID.eq(SYSVIEWS.TABLEID))
            // [#6797] The cast is necessary if a non-standard collation is used
            .where(SYSTABLES.sysschemas().SCHEMANAME.cast(VARCHAR(32672)).in(schemas))
            .orderBy(
                SYSTABLES.sysschemas().SCHEMANAME,
                SYSTABLES.TABLENAME);
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return create().select(
                    inline(null, VARCHAR).cast(VARCHAR).as("catalog"),
                    SYSSEQUENCES.sysschemas().SCHEMANAME,
                    SYSSEQUENCES.SEQUENCENAME,
                    SYSSEQUENCES.SEQUENCEDATATYPE,
                    inline(null, INTEGER).cast(INTEGER).as("numeric_precision"),
                    inline(null, INTEGER).cast(INTEGER).as("numeric_scale"),
                    nullif(SYSSEQUENCES.STARTVALUE, inline(1L)).as(SYSSEQUENCES.STARTVALUE),
                    nullif(SYSSEQUENCES.INCREMENT, inline(1L)).as(SYSSEQUENCES.INCREMENT),
                    nullif(SYSSEQUENCES.MINIMUMVALUE, case_(cast(SYSSEQUENCES.SEQUENCEDATATYPE, VARCHAR))
                        .when(inline("SMALLINT"), inline((long) Short.MIN_VALUE))
                        .when(inline("INTEGER"), inline((long) Integer.MIN_VALUE))
                        .when(inline("BIGINT"), inline(Long.MIN_VALUE))
                    ).coerce(NUMERIC).as(SYSSEQUENCES.MINIMUMVALUE),
                    nullif(SYSSEQUENCES.MAXIMUMVALUE, case_(cast(SYSSEQUENCES.SEQUENCEDATATYPE, VARCHAR))
                        .when(inline("SMALLINT"), inline((long) Short.MAX_VALUE))
                        .when(inline("INTEGER"), inline((long) Integer.MAX_VALUE))
                        .when(inline("BIGINT"), inline(Long.MAX_VALUE))
                    ).coerce(NUMERIC).as(SYSSEQUENCES.MAXIMUMVALUE),
                    SYSSEQUENCES.CYCLEOPTION.eq(inline("Y")).as(SYSSEQUENCES.CYCLEOPTION),
                    inline(null, BIGINT).cast(BIGINT).as("cache")
                )
                .from(SYSSEQUENCES)
                // [#6797] The cast is necessary if a non-standard collation is used
                .where(SYSSEQUENCES.sysschemas().SCHEMANAME.cast(VARCHAR(32672)).in(schemas))
                .orderBy(
                    SYSSEQUENCES.sysschemas().SCHEMANAME,
                    SYSSEQUENCES.SEQUENCENAME);
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : sequences(getInputSchemata())) {
            SchemaDefinition schema = getSchema(record.get(SYSSEQUENCES.sysschemas().SCHEMANAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get(SYSSEQUENCES.SEQUENCEDATATYPE)
            );

            result.add(new DefaultSequenceDefinition(
                schema,
                record.get(SYSSEQUENCES.SEQUENCENAME),
                type,
                null,
                record.get(SYSSEQUENCES.STARTVALUE),
                record.get(SYSSEQUENCES.INCREMENT),
                record.get(SYSSEQUENCES.MINIMUMVALUE),
                record.get(SYSSEQUENCES.MAXIMUMVALUE),
                record.get(SYSSEQUENCES.CYCLEOPTION, Boolean.class),
                null
            ));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                    SYSTABLES.sysschemas().SCHEMANAME,
                    SYSTABLES.TABLENAME,
                    SYSTABLES.TABLEID,
                    when(SYSTABLES.TABLETYPE.eq(inline("V")), inline(TableType.VIEW.name()))
                        .else_(inline(TableType.TABLE.name())).as("table_type"))
                .from(SYSTABLES)

                // [#6797] The cast is necessary if a non-standard collation is used
                .where(SYSTABLES.sysschemas().SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
                .orderBy(
                    SYSTABLES.sysschemas().SCHEMANAME,
                    SYSTABLES.TABLENAME)) {

            SchemaDefinition schema = getSchema(record.get(SYSTABLES.sysschemas().SCHEMANAME));
            String name = record.get(SYSTABLES.TABLENAME);
            String id = record.get(SYSTABLES.TABLEID);
            TableType tableType = record.get("table_type", TableType.class);

            DerbyTableDefinition table = new DerbyTableDefinition(schema, name, id, tableType, null);
            result.add(table);
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

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.DERBY);
    }
}
