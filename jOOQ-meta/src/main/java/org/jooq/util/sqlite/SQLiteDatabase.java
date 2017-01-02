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
package org.jooq.util.sqlite;

import static org.jooq.util.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.jaxb.Schema;
import org.jooq.util.sqlite.sqlite_master.SQLiteMaster;

/**
 * SQLite implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class SQLiteDatabase extends AbstractDatabase {

    public SQLiteDatabase() {

        // SQLite doesn't know schemata
        Schema schema = new Schema();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<Schema> schemata = new ArrayList<Schema>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.SQLITE);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (String tableName : create()
                .select(SQLiteMaster.NAME)
                .from(SQLITE_MASTER)
                .where(SQLiteMaster.TYPE.in("table"))
                .orderBy(SQLiteMaster.NAME)
                .fetch(SQLiteMaster.NAME)) {

            for (Record record : create().fetch("pragma table_info('" + tableName + "')")) {
                if (record.get("pk", int.class) > 0) {
                    String columnName = record.get("name", String.class);

                    // Generate a primary key name
                    String key = "pk_" + tableName;
                    TableDefinition table = getTable(getSchemata().get(0), tableName);

                    if (table != null) {
                        ColumnDefinition column = table.getColumn(columnName);
                        relations.addPrimaryKey(key, column);
                    }
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {

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

                String foreignKeyTable = table.getName();
                String foreignKeyColumn = record.get("from", String.class);

                // SQLite mixes up cases from the actual declaration and the
                // reference definition! It's possible that a table is declared
                // in lower case, and the foreign key in upper case. Hence,
                // correct the foreign key
                TableDefinition referencingTable = getTable(getSchemata().get(0), foreignKeyTable);
                TableDefinition referencedTable = getTable(getSchemata().get(0), record.get("table", String.class), true);

                if (referencedTable != null) {
                    String uniqueKey =
                        "pk_" + referencedTable.getName();

                    if (referencingTable != null) {
                        ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                        relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, getSchemata().get(0));
                    }
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
