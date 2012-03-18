/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util.sqlite;

import static org.jooq.util.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
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
    public Factory create() {
        return new SQLiteFactory(getConnection());
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
                if (record.getValueAsBoolean("pk", false)) {
                    String columnName = record.getValueAsString("name");

                    // Generate a primary key name
                    String key = "pk_" + tableName + "_" + columnName;
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
                    "_" + record.getValue("table");

                Integer sequence = map.get(foreignKeyPrefix);
                if (sequence == null) {
                    sequence = 0;
                }

                if (0 == record.getValue("seq", Integer.class)) {
                    sequence = sequence + 1;
                }

                map.put(foreignKeyPrefix, sequence);

                String foreignKey =
                    "fk_" + table.getName() +
                    "_" + record.getValue("table") +
                    "_" + sequence;

                String foreignKeyTable = table.getName();
                String foreignKeyColumn = record.getValueAsString("from");

                // SQLite mixes up cases from the actual declaration and the
                // reference definition! It's possible that a table is declared
                // in lower case, and the foreign key in upper case. Hence,
                // correct the foreign key
                TableDefinition referencingTable = getTable(getSchemata().get(0), foreignKeyTable);
                TableDefinition referencedTable = getTable(getSchemata().get(0), record.getValueAsString("table"), true);

                if (referencedTable != null) {
                    String uniqueKey =
                        "pk_" + referencedTable.getName() +
                        "_" + referencedTable.getColumn(record.getValueAsString("to"), true).getName();

                    if (referencingTable != null) {
                        ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                        relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, getSchemata().get(0));
                    }
                }
            }
        }
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
