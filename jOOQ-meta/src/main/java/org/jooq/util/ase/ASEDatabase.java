/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.ase;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.ase.sys.tables.Sysindexes.SYSINDEXES;
import static org.jooq.util.ase.sys.tables.Sysusers.SYSUSERS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record10;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
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
import org.jooq.util.ase.sys.tables.Sysindexes;
import org.jooq.util.ase.sys.tables.Sysreferences;
import org.jooq.util.ase.sys.tables.Sysusers;

/**
 * Sybase Adaptive Server implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class ASEDatabase extends AbstractDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(ASEDatabase.class);

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.ASE);
    }

    private SchemaDefinition getSchema() {
        List<SchemaDefinition> schemata = getSchemata();

        if (schemata.size() > 1) {
            log.error("NOT SUPPORTED", "jOOQ does not support multiple schemata in Sybase ASE.");
            log.error("-----------------------------------------------------------------------");

            // TODO [#1098] Support this also for Sybase ASE
        }

        return schemata.get(0);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(PK_INCL, PK_EXCL)) {
            String keyName = record.getValue(0, String.class);
            TableDefinition table = getTable(getSchema(), record.getValue(1, String.class));

            if (table != null) {
                for (int i = 0; i < 8; i++) {
                    if (record.getValue(2 + i) == null) {
                        break;
                    }

                    relations.addPrimaryKey(keyName, table.getColumn(record.getValue(2 + i, String.class)));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(UK_INCL, UK_EXCL)) {
            String keyName = record.getValue(0, String.class);
            TableDefinition table = getTable(getSchema(), record.getValue(1, String.class));

            if (table != null) {
                for (int i = 0; i < 8; i++) {
                    if (record.getValue(2 + i) == null) {
                        break;
                    }

                    relations.addUniqueKey(keyName, table.getColumn(record.getValue(2 + i, String.class)));
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
    private Result<Record10<String, String, String, String, String, String, String, String, String, String>> fetchKeys(int incl, int excl) {
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
                field("col_name(tableid, fokey16)", String.class),
                field("object_owner_id(tableid)"))
            .from(Sysreferences.SYSREFERENCES)
            .fetch()) {

            TableDefinition referencingTable = getTable(getSchema(), record.getValue("fk_table", String.class));
            if (referencingTable != null) {
                for (int i = 0; i < 16; i++) {
                    if (record.getValue(i + 3) == null) {
                        break;
                    }

                    String foreignKeyName = record.getValue("fk", String.class);
                    String foreignKeyColumnName = record.getValue(i + 3, String.class);
                    String uniqueKeyName = record.getValue("pk", String.class);

                    ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                    relations.addForeignKey(foreignKeyName, uniqueKeyName, column, getSchema());
                }
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(Sysusers.NAME)
                .from(SYSUSERS)
                .fetch(Sysusers.NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

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

        for (Record record : fetchTables()) {
            SchemaDefinition schema = getSchema(record.getValue("Owner", String.class));
            String name = record.getValue("Name", String.class);

            result.add(new ASETableDefinition(schema, name, null));
        }

        return result;
    }

    private List<Record> fetchTables() {
        List<Record> result = new ArrayList<Record>();

        for (Record record : create().fetch("sp_help")) {
            if (asList("view", "user table", "system table").contains(record.getValue("Object_type", String.class))) {
                if (getInputSchemata().contains(record.getValue("Owner", String.class))) {
                    result.add(record);
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unused")
    private List<Record> fetchRoutines() {
        List<Record> result = new ArrayList<Record>();

        for (Record record : create().fetch("sp_help")) {
            if (asList("stored procedure").contains(record.getValue("Object_type", String.class))) {
                if (getInputSchemata().contains(record.getValue("Owner", String.class))) {
                    result.add(record);
                }
            }
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        // [#1507] This was contributed by Mark. It will be correctly
        // implemented at a later stage

//        for (Record record : fetchRoutines()) {
//            SchemaDefinition schema = getSchema(record.getValue("Owner", String.class));
//            String name = record.getValue("Name", String.class);
//            result.add(new ASERoutineDefinition(schema, null, name, null, null, null));
//        }

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
