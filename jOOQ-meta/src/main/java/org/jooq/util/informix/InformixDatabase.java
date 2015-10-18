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
package org.jooq.util.informix;

import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.util.informix.sys.Tables.SYSCONSTRAINTS;
import static org.jooq.util.informix.sys.Tables.SYSINDEXES;
import static org.jooq.util.informix.sys.Tables.SYSREFERENCES;
import static org.jooq.util.informix.sys.Tables.SYSSEQUENCES;
import static org.jooq.util.informix.sys.Tables.SYSTABLES;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.informix.sys.tables.Sysconstraints;
import org.jooq.util.informix.sys.tables.Sysindexes;
import org.jooq.util.informix.sys.tables.Sysreferences;
import org.jooq.util.informix.sys.tables.Systables;

/**
 * Informix implementation of {@link AbstractDatabase}
 *
 * @author Lukas Eder
 */
public class InformixDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.INFORMIX);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.getValue(SYSTABLES.OWNER));
            String key = record.getValue(SYSCONSTRAINTS.CONSTRNAME);
            String tableName = record.getValue(SYSTABLES.TABNAME);

            for (int i = 3; i < record.size(); i++) {
                int colNo = record.getValue(i, int.class);

                if (colNo != 0) {
                    TableDefinition table = getTable(schema, tableName);

                    if (table != null) {
                        relations.addPrimaryKey(key, table.getColumns().get(colNo - 1));
                    }
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(SYSTABLES.OWNER));
            String key = record.getValue(SYSCONSTRAINTS.CONSTRNAME);
            String tableName = record.getValue(SYSTABLES.TABNAME);

            for (int i = 3; i < record.size(); i++) {
                int colNo = record.getValue(i, int.class);

                if (colNo != 0) {
                    TableDefinition table = getTable(schema, tableName);

                    if (table != null) {
                        relations.addUniqueKey(key, table.getColumns().get(colNo - 1));
                    }
                }
            }
        }
    }

    private Result<?> fetchKeys(String constraintType) {
        return
        create().select(
                    SYSTABLES.OWNER.trim().as(SYSTABLES.OWNER),
                    SYSTABLES.TABNAME.trim().as(SYSTABLES.TABNAME),
                    SYSCONSTRAINTS.CONSTRNAME.trim().as(SYSCONSTRAINTS.CONSTRNAME),
                    SYSINDEXES.PART1,
                    SYSINDEXES.PART2,
                    SYSINDEXES.PART3,
                    SYSINDEXES.PART4,
                    SYSINDEXES.PART5,
                    SYSINDEXES.PART6,
                    SYSINDEXES.PART7,
                    SYSINDEXES.PART8,
                    SYSINDEXES.PART9,
                    SYSINDEXES.PART10,
                    SYSINDEXES.PART11,
                    SYSINDEXES.PART12,
                    SYSINDEXES.PART13,
                    SYSINDEXES.PART14,
                    SYSINDEXES.PART15,
                    SYSINDEXES.PART16
                )
                .from(SYSCONSTRAINTS)
                .join(SYSTABLES)
                .on(SYSCONSTRAINTS.TABID.eq(SYSTABLES.TABID))
                .join(SYSINDEXES)
                .on(SYSCONSTRAINTS.OWNER.eq(SYSINDEXES.OWNER))
                .and(SYSCONSTRAINTS.IDXNAME.eq(SYSINDEXES.IDXNAME))
                .where(SYSCONSTRAINTS.OWNER.in(getInputSchemata()))
                .and(SYSCONSTRAINTS.CONSTRTYPE.equal(constraintType))
                .orderBy(
                    SYSCONSTRAINTS.OWNER.asc(),
                    SYSTABLES.TABNAME.asc(),
                    SYSCONSTRAINTS.CONSTRNAME.asc())
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Sysreferences r = SYSREFERENCES.as("r");
        Sysconstraints uk = SYSCONSTRAINTS.as("uk");
        Sysconstraints fk = SYSCONSTRAINTS.as("fk");
        Systables fkTable = SYSTABLES.as("fkTable");
        Sysindexes fkIndex = SYSINDEXES.as("fkIndex");

        for (Record record : create()
                .select(
                    uk.OWNER,
                    uk.CONSTRNAME,
                    fk.OWNER,
                    fk.CONSTRNAME,
                    fkTable.TABNAME,
                    fkIndex.PART1,
                    fkIndex.PART2,
                    fkIndex.PART3,
                    fkIndex.PART4,
                    fkIndex.PART5,
                    fkIndex.PART6,
                    fkIndex.PART7,
                    fkIndex.PART8,
                    fkIndex.PART9,
                    fkIndex.PART10,
                    fkIndex.PART11,
                    fkIndex.PART12,
                    fkIndex.PART13,
                    fkIndex.PART14,
                    fkIndex.PART15,
                    fkIndex.PART16
                )
                .from(r)
                .join(fk)
                .on(r.CONSTRID.eq(fk.CONSTRID))
                .join(fkTable)
                .on(fk.TABID.eq(fkTable.TABID))
                .join(fkIndex)
                .on(fk.OWNER.eq(fkIndex.OWNER))
                .and(fk.IDXNAME.eq(fkIndex.IDXNAME))
                .join(uk)
                .on(r.PRIMARY.eq(uk.CONSTRID))
                .where(fk.OWNER.in(getInputSchemata()))
                .and(uk.OWNER.in(getInputSchemata()))
                .orderBy(
                    fk.OWNER.asc(),
                    fk.CONSTRNAME.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(fk.OWNER).trim());
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(uk.OWNER).trim());

            String foreignKeyTableName = record.getValue(fkTable.TABNAME).trim();
            String foreignKey = record.getValue(fk.CONSTRNAME).trim();
            String uniqueKey = record.getValue(uk.CONSTRNAME).trim();

            for (int i = 5; i < record.size(); i++) {
                int foreignKeyColumnNo = record.getValue(i, int.class);

                if (foreignKeyColumnNo != 0) {
                    TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);

                    if (foreignKeyTable != null) {
                        ColumnDefinition referencingColumn = foreignKeyTable.getColumns().get(foreignKeyColumnNo - 1);

                        relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
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
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String owner : create().fetchValues(
                 selectDistinct(SYSTABLES.OWNER.trim())
                .from(SYSTABLES)
                .where(SYSTABLES.OWNER.in(getInputSchemata())))) {

            result.add(new SchemaDefinition(this, owner, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    SYSTABLES.OWNER.trim().as(SYSTABLES.OWNER),
                    SYSTABLES.TABNAME.trim().as(SYSTABLES.TABNAME),
                    SYSSEQUENCES.MAX_VAL
                )
                .from(SYSTABLES)
                .join(SYSSEQUENCES).on(SYSTABLES.TABID.eq(SYSSEQUENCES.TABID))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(SYSTABLES.OWNER));
            String name = record.getValue(SYSTABLES.TABNAME);
            DataTypeDefinition type = getDataTypeForMAX_VAL(schema, record.getValue(SYSSEQUENCES.MAX_VAL, BigInteger.class));

            result.add(new DefaultSequenceDefinition(schema, name, type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    SYSTABLES.OWNER.trim().as(SYSTABLES.OWNER),
                    SYSTABLES.TABNAME.trim().as(SYSTABLES.TABNAME))
                .from(SYSTABLES)
                .where(SYSTABLES.OWNER.in(getInputSchemata()))
                .and(SYSTABLES.TABTYPE.in("T", "V"))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue("owner", String.class).trim());

            result.add(new InformixTableDefinition(schema, record.getValue("tabname", String.class), ""));
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
