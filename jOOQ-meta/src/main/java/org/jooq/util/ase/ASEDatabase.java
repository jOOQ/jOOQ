/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.ase;

import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.ase.sys.tables.Sysindexes.SYSINDEXES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
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
import org.jooq.util.ase.sys.DboFactory;
import org.jooq.util.ase.sys.tables.Sysindexes;
import org.jooq.util.ase.sys.tables.Sysreferences;

/**
 * Sybase Adaptive Server implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class ASEDatabase extends AbstractDatabase {

    @Override
    public Factory create() {
        return new DboFactory(getConnection());
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(PK_INCL, PK_EXCL)) {
            String keyName = record.getValueAsString(0);
            TableDefinition table = getTable(record.getValueAsString(1));

            if (table != null) {
                for (int i = 0; i < 8; i++) {
                    if (record.getValue(2 + i) == null) {
                        break;
                    }

                    relations.addPrimaryKey(keyName, table.getColumn(record.getValueAsString(2 + i)));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(UK_INCL, UK_EXCL)) {
            String keyName = record.getValueAsString(0);
            TableDefinition table = getTable(record.getValueAsString(1));

            if (table != null) {
                for (int i = 0; i < 8; i++) {
                    if (record.getValue(2 + i) == null) {
                        break;
                    }

                    relations.addUniqueKey(keyName, table.getColumn(record.getValueAsString(2 + i)));
                }
            }
        }
    }

    private static final int PK_INCL = 2048;
    private static final int PK_EXCL = 64;
    private static final int UK_INCL = 4096 | 2;
    private static final int UK_EXCL = 2048 | 64;

    /**
     * The underlying query of this method was found here:
     * <p>
     * <a href=
     * "http://www.dbforums.com/sybase/1625012-sysindexes-question-testing-unique-clustered-indexes.html"
     * >http://www.dbforums.com/sybase/1625012-sysindexes-question-testing-
     * unique-clustered-indexes.html</a>
     */
    private List<Record> fetchKeys(int incl, int excl) {
        Field<String> table = field("object_name(id)", String.class);
        Field<String> key = field("name", String.class);

        return create().select(
                    concat(table, val("__"), key),
                    table,
                    field("index_col(object_name(id), indid, 1)", String.class),
                    field("index_col(object_name(id), indid, 2)", String.class),
                    field("index_col(object_name(id), indid, 3)", String.class),
                    field("index_col(object_name(id), indid, 4)", String.class),
                    field("index_col(object_name(id), indid, 5)", String.class),
                    field("index_col(object_name(id), indid, 6)", String.class),
                    field("index_col(object_name(id), indid, 7)", String.class),
                    field("index_col(object_name(id), indid, 8)", String.class))
            .from(SYSINDEXES)
            .where("status & ? = 0", excl)
            .and("status & ? <> 0", incl)
            .orderBy(Sysindexes.ID)
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Field<String> fkTable = field("object_name(tableid)", String.class);
        Field<String> fk = field("object_name(constrid)", String.class);
        Field<String> pkTable = field("object_name(reftabid)", String.class);
        Field<String> pk = field("index_name(pmrydbid, reftabid, indexid)", String.class);

        for (Record record : create().select(
                fkTable.as("fk_table"),
                concat(fkTable, val("__"), fk).as("fk"),
                concat(pkTable, val("__"), pk).as("pk"),
                field("col_name(tableid, fokey1)", String.class),
                field("col_name(tableid, fokey2)", String.class),
                field("col_name(tableid, fokey3)", String.class),
                field("col_name(tableid, fokey4)", String.class),
                field("col_name(tableid, fokey5)", String.class),
                field("col_name(tableid, fokey6)", String.class),
                field("col_name(tableid, fokey7)", String.class),
                field("col_name(tableid, fokey8)", String.class),
                field("col_name(tableid, fokey9)", String.class),
                field("col_name(tableid, fokey10)", String.class),
                field("col_name(tableid, fokey11)", String.class),
                field("col_name(tableid, fokey12)", String.class),
                field("col_name(tableid, fokey13)", String.class),
                field("col_name(tableid, fokey14)", String.class),
                field("col_name(tableid, fokey15)", String.class),
                field("col_name(tableid, fokey16)", String.class))
            .from(Sysreferences.SYSREFERENCES)
            .fetch()) {

            TableDefinition referencingTable = getTable(record.getValueAsString("fk_table"));
            if (referencingTable != null) {
                for (int i = 0; i < 16; i++) {
                    if (record.getValue(i + 3) == null) {
                        break;
                    }

                    String foreignKeyName = record.getValueAsString("fk");
                    String foreignKeyColumnName = record.getValueAsString(i + 3);
                    String uniqueKeyName = record.getValueAsString("pk");

                    ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                    relations.addForeignKey(foreignKeyName, uniqueKeyName, column);
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

        for (String name : fetchTableNames()) {
            result.add(new ASETableDefinition(this, name, null));
        }

        return result;
    }

    private List<String> fetchTableNames() {
        List<String> result = new ArrayList<String>();

        for (Record record : create().fetch("sp_help")) {
            if (Arrays.asList("view", "user table", "system table").contains(record.getValueAsString("Object_type"))) {
                if (getInputSchema().equals(record.getValueAsString("Owner"))) {
                    result.add(record.getValueAsString("Name"));
                }
            }
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
