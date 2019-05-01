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
package org.jooq.meta.sqlite;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.table;
import static org.jooq.meta.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
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
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.jaxb.SchemaMappingType;
import org.jooq.meta.sqlite.sqlite_master.SQLiteMaster;

/**
 * SQLite implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class SQLiteDatabase extends AbstractDatabase {

    public SQLiteDatabase() {

        // SQLite doesn't know schemata
        SchemaMappingType schema = new SchemaMappingType();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<SchemaMappingType> schemata = new ArrayList<SchemaMappingType>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.SQLITE);
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        final List<IndexDefinition> result = new ArrayList<IndexDefinition>();

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
                List<IndexColumnDefinition> indexColumns = new ArrayList<IndexColumnDefinition>();

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

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (String tableName : create()
                .select(SQLiteMaster.NAME)
                .from(SQLITE_MASTER)
                .where(SQLiteMaster.TYPE.in("table"))
                .orderBy(SQLiteMaster.NAME)
                .fetch(SQLiteMaster.NAME)) {

            for (Record record : create().fetch("pragma table_info({0})", inline(tableName))) {
                if (record.get("pk", int.class) > 0) {
                    String columnName = record.get("name", String.class);

                    // Generate a primary key name
                    String key = "pk_" + tableName;
                    TableDefinition table = getTable(getSchemata().get(0), tableName);

                    if (table != null)
                        relations.addPrimaryKey(key, table, table.getColumn(columnName));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().fetch(
            "SELECT "
          + "  m.tbl_name AS table_name, "
          + "  il.name AS key_name, "
          + "  ii.name AS column_name "
          + "FROM "
          + "  sqlite_master AS m, "
          + "  pragma_index_list(m.name) AS il, "
          + "  pragma_index_info(il.name) AS ii "
          + "WHERE "
          + "  m.type = 'table' AND "
          + "  il.origin = 'u' "
          + "ORDER BY table_name, key_name, ii.seqno"
        )) {
            String tableName = record.get("table_name", String.class);
            String keyName = record.get("key_name", String.class);
            String columnName = record.get("column_name", String.class);

            TableDefinition table = getTable(getSchemata().get(0), tableName);
            if (table != null)
                relations.addUniqueKey(keyName, table, table.getColumn(columnName));
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (TableDefinition table : getTables(getSchemata().get(0))) {
            Map<String, Integer> map = new HashMap<String, Integer>();

            for (Record record : create().fetch("pragma foreign_key_list(" + table.getName() + ")")) {
                String foreignKeyPrefix =
                    "fk_" + table.getName() +
                    "_" + record.get("table");

                Integer sequence = map.get(foreignKeyPrefix);
                if (sequence == null) {
                    sequence = 0;
                }

                if (0 == record.get("seq", Integer.class)) {
                    sequence = sequence + 1;
                }

                map.put(foreignKeyPrefix, sequence);

                String foreignKey =
                    "fk_" + table.getName() +
                    "_" + record.get("table") +
                    "_" + sequence;

                String foreignKeyTableName = table.getName();
                String foreignKeyColumn = record.get("from", String.class);

                // SQLite mixes up cases from the actual declaration and the
                // reference definition! It's possible that a table is declared
                // in lower case, and the foreign key in upper case. Hence,
                // correct the foreign key
                TableDefinition foreignKeyTable = getTable(getSchemata().get(0), foreignKeyTableName);
                TableDefinition uniqueKeyTable = getTable(getSchemata().get(0), record.get("table", String.class), true);

                if (uniqueKeyTable != null) {
                    String uniqueKey =
                        "pk_" + uniqueKeyTable.getName();

                    if (foreignKeyTable != null)
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
        result.add(new SchemaDefinition(this, "", ""));
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

        for (String name : create().select(SQLiteMaster.NAME)
            .from(SQLITE_MASTER)
            .where(SQLiteMaster.TYPE.in("table", "view"))
            .orderBy(SQLiteMaster.NAME)
            .fetch(SQLiteMaster.NAME)) {

            SQLiteTableDefinition table = new SQLiteTableDefinition(getSchemata().get(0), name, "");
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
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
}
