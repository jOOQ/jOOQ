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
package org.jooq.meta.sqlite;

import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.list;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Internal.hash;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;
import static org.jooq.tools.StringUtils.isBlank;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Check;
import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Meta;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Record4;
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
import org.jooq.UniqueKey;
import org.jooq.exception.DataDefinitionException;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;
import org.jooq.impl.QOM;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
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
import org.jooq.meta.jaxb.SchemaMappingType;
import org.jooq.meta.sqlite.sqlite_master.SQLiteMaster;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * SQLite implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class SQLiteDatabase extends AbstractDatabase implements ResultQueryDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(SQLiteDatabase.class);

    public SQLiteDatabase() {

        // SQLite doesn't know schemata
        SchemaMappingType schema = new SchemaMappingType();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<SchemaMappingType> schemata = new ArrayList<>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.SQLITE);
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        final List<IndexDefinition> result = new ArrayList<>();

        final Field<String> fIndexName = field("il.name", String.class).as("index_name");
        final Field<Boolean> fUnique = field("il.\"unique\"", boolean.class).as("unique");
        final Field<Integer> fSeqno = field("ii.seqno", int.class).add(one()).as("seqno");
        final Field<String> fColumnName = field("ii.name", String.class).as("column_name");

        Map<Record, Result<Record>> indexes = create()
            .select(
                SQLiteMaster.NAME,
                fIndexName,
                fUnique,
                fSeqno,
                fColumnName)
            .from(
                SQLITE_MASTER,
                table("pragma_index_list({0})", SQLiteMaster.NAME).as("il"),
                table("pragma_index_info(il.name)").as("ii"))
            .where(SQLiteMaster.TYPE.eq(inline("table")))
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : field("il.origin", VARCHAR).notIn(inline("pk"), inline("u")))
            .orderBy(1, 2, 4)
            .fetchGroups(
                new Field[] {
                    SQLiteMaster.NAME,
                    fIndexName,
                    fUnique
                },
                new Field[] {
                    fColumnName,
                    fSeqno
                });

        indexLoop:
        for (Entry<Record, Result<Record>> entry : indexes.entrySet()) {
            final Record index = entry.getKey();
            final Result<Record> columns = entry.getValue();

            final SchemaDefinition tableSchema = getSchemata().get(0);
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = index.get(fIndexName);
            final String tableName = index.get(SQLiteMaster.NAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final boolean unique = index.get(fUnique);

            // [#6310] [#6620] Function-based indexes are not yet supported
            for (Record column : columns)
                if (table.getColumn(column.get(fColumnName)) == null)
                    continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, unique) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    for (Record column : columns) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(column.get(fColumnName)),
                            SortOrder.ASC,
                            column.get(fSeqno, int.class)
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

    private String keyName(UniqueKey<?> key) {
        String name = key.getName();
        return !isBlank(name)
             ? name
             : key.isPrimary()
             ? "pk_" + key.getTable().getName()
             : "uk_" + key.getTable().getName() + "_" + hash(list(key.getFields()));
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {

        // [#11294] Cannot use Meta.getPrimaryKeys() here, yet
        for (Table<?> t : snapshot().getTables()) {
            UniqueKey<?> pk = t.getPrimaryKey();

            if (pk != null) {
                TableDefinition table = getTable(getSchemata().get(0), pk.getTable().getName());

                if (table != null)
                    for (Field<?> f : pk.getFields())
                        relations.addPrimaryKey(keyName(pk), table, table.getColumn(f.getName()));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {

        // [#11294] Cannot use Meta.getUniqueKeys() here, yet
        for (Table<?> t : snapshot().getTables()) {
            for (UniqueKey<?> uk : t.getUniqueKeys()) {
                TableDefinition table = getTable(getSchemata().get(0), uk.getTable().getName());

                if (table != null)
                    for (Field<?> f : uk.getFields())
                        relations.addUniqueKey(keyName(uk), table, table.getColumn(f.getName()));
            }
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {


        // [#11294] Cannot use Meta.getUniqueKeys() here, yet
        for (Table<?> t : snapshot().getTables()) {

            fkLoop:
            for (ForeignKey<?, ?> fk : t.getReferences()) {
                UniqueKey<?> uk = fk.getKey();

                if (uk == null)
                    continue fkLoop;

                // SQLite mixes up cases from the actual declaration and the
                // reference definition! It's possible that a table is declared
                // in lower case, and the foreign key in upper case. Hence,
                // correct the foreign key
                TableDefinition fkTable = getTable(getSchemata().get(0), fk.getTable().getName(), true);
                TableDefinition ukTable = getTable(getSchemata().get(0), uk.getTable().getName(), true);

                if (fkTable == null || ukTable == null)
                    continue fkLoop;

                String ukName = keyName(uk);
                String fkName = StringUtils.isBlank(fk.getName())
                    ? "fk_" + fkTable.getName() +
                      "_" + ukName
                    : fk.getName();

                TableField<?, ?>[] fkFields = fk.getFieldsArray();
                TableField<?, ?>[] ukFields = fk.getKeyFieldsArray();

                for (int i = 0; i < fkFields.length; i++) {
                    relations.addForeignKey(
                        fkName,
                        fkTable,
                        fkTable.getColumn(fkFields[i].getName(), true),
                        ukName,
                        ukTable,
                        ukTable.getColumn(ukFields[i].getName(), true),
                        true
                    );
                }
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        DSLContext ctx = create()
            .configuration()
            .deriveSettings(s -> s.withInterpreterDelayForeignKeyDeclarations(true))
            .dsl();

        SchemaDefinition schema = getSchemata().get(0);

        for (Record record : ctx
                .select(SQLiteMaster.TBL_NAME, SQLiteMaster.SQL)
                .from(SQLITE_MASTER)
                .where(SQLiteMaster.SQL.likeIgnoreCase("%CHECK%"))
                .orderBy(SQLiteMaster.TBL_NAME)) {

            TableDefinition table = getTable(schema, record.get(SQLiteMaster.TBL_NAME));

            if (table != null) {
                String sql = record.get(SQLiteMaster.SQL);

                try {
                    Query query = ctx.parser().parseQuery(sql);

                    for (Table<?> t : ctx.meta(query).getTables(table.getName())) {
                        for (Check<?> check : t.getChecks()) {
                            r.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                                schema,
                                table,
                                check.getName(),
                                ctx.renderInlined(check.condition()),
                                check.enforced()
                            ));
                        }
                    }
                }
                catch (ParserException e) {
                    log.info("Cannot parse SQL: " + sql, e);
                }
                catch (DataDefinitionException e) {
                    log.info("Cannot interpret SQL: " + sql, e);
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
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    public ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas) {
        return create()
            .select(
                inline(null, VARCHAR).as("catalog"),
                inline(null, VARCHAR).as("schema"),
                SQLiteMaster.NAME,
                SQLiteMaster.SQL)
            .from(SQLITE_MASTER)
            .orderBy(SQLiteMaster.NAME);
    }

    @Override
    public ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas) {
        return null;
    }

    @Override
    public ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas) {
        return null;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>();

        CommonTableExpression<Record1<String>> virtualTables =
            name("virtual_tables").fields("name").as(
            select(coalesce(SQLiteMaster.NAME, inline("")))
            .from(SQLITE_MASTER)
            .where(SQLiteMaster.SQL.likeIgnoreCase(inline("%create virtual table%"))));

        for (Record record : create()
                .with(virtualTables)
                .select(
                    SQLiteMaster.NAME,
                    when(SQLiteMaster.TYPE.eq(inline("view")), inline(TableType.VIEW.name()))
                    .else_(inline(TableType.TABLE.name())).as("table_type"),
                    SQLiteMaster.SQL)
                .from(SQLITE_MASTER)
                .where(SQLiteMaster.TYPE.in("table", "view"))
                .and(getIncludeSystemTables()
                    ? noCondition()
                    : SQLiteMaster.NAME.notIn(inline("sqlite_sequence"), inline("sqlite_master"))
                    .and(SQLiteMaster.NAME.notLike(inline("sqlite!_stat%")).escape('!'))
                    .and(
                          SQLiteMaster.NAME.notLike(all(
                            inline("%!_content"),
                            inline("%!_segments"),
                            inline("%!_segdir"),
                            inline("%!_docsize"),
                            inline("%!_stat")
                          )).escape('!')
                            .or(SQLiteMaster.NAME.like(inline("%!_content" )).escape('!').and(replace(SQLiteMaster.NAME, inline("_content" )).notIn(selectFrom(virtualTables))))
                            .or(SQLiteMaster.NAME.like(inline("%!_segments")).escape('!').and(replace(SQLiteMaster.NAME, inline("_segments")).notIn(selectFrom(virtualTables))))
                            .or(SQLiteMaster.NAME.like(inline("%!_segdir"  )).escape('!').and(replace(SQLiteMaster.NAME, inline("_segdir"  )).notIn(selectFrom(virtualTables))))
                            .or(SQLiteMaster.NAME.like(inline("%!_docsize" )).escape('!').and(replace(SQLiteMaster.NAME, inline("_docsize" )).notIn(selectFrom(virtualTables))))
                            .or(SQLiteMaster.NAME.like(inline("%!_stat"    )).escape('!').and(replace(SQLiteMaster.NAME, inline("_stat"    )).notIn(selectFrom(virtualTables))))
                    )
                )
                .orderBy(SQLiteMaster.NAME)) {

            String name = record.get(SQLiteMaster.NAME);
            TableType tableType = record.get("table_type", TableType.class);
            String source = record.get(SQLiteMaster.SQL);

            result.add(new SQLiteTableDefinition(getSchemata().get(0), name, "", tableType, source));
        }

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

    private Meta snapshot;

    Meta snapshot() {
        if (snapshot == null)
            snapshot = create().meta().snapshot();

        return snapshot;
    }
}
