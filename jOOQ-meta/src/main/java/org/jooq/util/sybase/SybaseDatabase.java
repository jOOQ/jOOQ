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
package org.jooq.util.sybase;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.sybase.sys.Tables.SYSFKEY;
import static org.jooq.util.sybase.sys.Tables.SYSIDX;
import static org.jooq.util.sybase.sys.Tables.SYSIDXCOL;
import static org.jooq.util.sybase.sys.Tables.SYSPROCEDURE;
import static org.jooq.util.sybase.sys.Tables.SYSSEQUENCE;
import static org.jooq.util.sybase.sys.Tables.SYSTABCOL;
import static org.jooq.util.sybase.sys.Tables.SYSTABLE;
import static org.jooq.util.sybase.sys.Tables.SYSUSER;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.types.UByte;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
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
import org.jooq.util.sybase.sys.tables.Sysidx;
import org.jooq.util.sybase.sys.tables.Systable;

/**
 * Sybase implementation of {@link AbstractDatabase} This implementation is
 * targeted at the Sybase SQLAnywhere 12 database engine.
 *
 * @see <a
 *      href="http://infocenter.sybase.com/help/index.jsp?topic=/com.sybase.help.sqlanywhere.12.0.0/dbreference/rf-system-views.html">Sybase
 *      documentation</a>
 * @author Espen Stromsnes
 */
public class SybaseDatabase extends AbstractDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(SybaseDatabase.class);

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.SYBASE);
    }


    private SchemaDefinition getSchema() {
        List<SchemaDefinition> schemata = getSchemata();

        if (schemata.size() > 1) {
            log.error("NOT SUPPORTED", "jOOQ does not support multiple schemata in Sybase ASE.");
            log.error("-----------------------------------------------------------------------");

            // TODO [#1102] Support this also for Sybase SQL Anywhere
        }

        return schemata.get(0);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                concat(SYSTABLE.TABLE_NAME, val("__"), SYSIDX.INDEX_NAME).as("indexName"),
                SYSTABLE.TABLE_NAME,
                SYSTABCOL.COLUMN_NAME)
            .from(SYSIDX)
            .join(SYSIDXCOL)
            .on(SYSIDX.TABLE_ID.equal(SYSIDXCOL.TABLE_ID))
            .and(SYSIDX.INDEX_ID.equal(SYSIDXCOL.INDEX_ID))
            .join(SYSTABLE)
            .on(SYSIDXCOL.TABLE_ID.equal(SYSTABLE.TABLE_ID))
            .join(SYSTABCOL)
            .on(SYSIDXCOL.TABLE_ID.equal(SYSTABCOL.TABLE_ID))
            .and(SYSIDXCOL.COLUMN_ID.equal(SYSTABCOL.COLUMN_ID))
            .where(SYSIDX.INDEX_CATEGORY.equal(UByte.valueOf((byte) 1)))
            .orderBy(SYSIDXCOL.SEQUENCE)
            .fetch()) {

            String key = record.getValue("indexName", String.class);
            String tableName = record.getValue(SYSTABLE.TABLE_NAME);
            String columnName = record.getValue(SYSTABCOL.COLUMN_NAME);

            TableDefinition table = getTable(getSchema(), tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }


    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
        for (Record record : create().select(
                concat(SYSTABLE.TABLE_NAME, val("__"), SYSIDX.INDEX_NAME).as("indexName"),
                SYSTABLE.TABLE_NAME,
                SYSTABCOL.COLUMN_NAME)
            .from(SYSIDX)
            .join(SYSIDXCOL)
            .on(SYSIDX.TABLE_ID.equal(SYSIDXCOL.TABLE_ID))
            .and(SYSIDX.INDEX_ID.equal(SYSIDXCOL.INDEX_ID))
            .join(SYSTABLE)
            .on(SYSIDXCOL.TABLE_ID.equal(SYSTABLE.TABLE_ID))
            .join(SYSTABCOL)
            .on(SYSIDXCOL.TABLE_ID.equal(SYSTABCOL.TABLE_ID))
            .and(SYSIDXCOL.COLUMN_ID.equal(SYSTABCOL.COLUMN_ID))
            .where(SYSIDX.INDEX_CATEGORY.equal(UByte.valueOf((byte) 3)))
            .and(SYSIDX.UNIQUE.equal(UByte.valueOf((byte) 2)))
            .orderBy(SYSIDXCOL.SEQUENCE)
            .fetch()) {

            String key = record.getValue("indexName", String.class);
            String tableName = record.getValue(SYSTABLE.TABLE_NAME);
            String columnName = record.getValue(SYSTABCOL.COLUMN_NAME);

            TableDefinition table = getTable(getSchema(), tableName);
            if (table != null) {
                r.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Sysidx fkIndex = SYSIDX.as("fkIndex");
        Sysidx ukIndex = SYSIDX.as("ukIndex");

        Systable fkTable = SYSTABLE.as("fkTable");
        Systable ukTable = SYSTABLE.as("ukTable");

        for (Record record : create().select(
                concat(fkTable.TABLE_NAME, val("__"), fkIndex.INDEX_NAME).as("fkIndexName"),
                fkTable.TABLE_NAME,
                SYSTABCOL.COLUMN_NAME,
                concat(ukTable.TABLE_NAME, val("__"), ukIndex.INDEX_NAME).as("ukIndexName"))
            .from(SYSFKEY)
            .join(fkIndex)
            .on(SYSFKEY.FOREIGN_INDEX_ID.equal(fkIndex.INDEX_ID))
            .and(SYSFKEY.FOREIGN_TABLE_ID.equal(fkIndex.TABLE_ID))
            .join(SYSIDXCOL)
            .on(fkIndex.INDEX_ID.equal(SYSIDXCOL.INDEX_ID))
            .and(fkIndex.TABLE_ID.equal(SYSIDXCOL.TABLE_ID))
            .join(fkTable)
            .on(SYSFKEY.FOREIGN_TABLE_ID.equal(fkTable.TABLE_ID))
            .join(SYSTABCOL)
            .on(SYSIDXCOL.TABLE_ID.equal(SYSTABCOL.TABLE_ID))
            .and(SYSIDXCOL.COLUMN_ID.equal(SYSTABCOL.COLUMN_ID))
            .join(ukIndex)
            .on(SYSFKEY.PRIMARY_INDEX_ID.equal(ukIndex.INDEX_ID))
            .and(SYSFKEY.PRIMARY_TABLE_ID.equal(ukIndex.TABLE_ID))
            .join(ukTable)
            .on(SYSFKEY.PRIMARY_TABLE_ID.equal(ukTable.TABLE_ID))
            .orderBy(
                fkTable.TABLE_NAME.asc(),
                fkIndex.INDEX_NAME.asc(),
                SYSIDXCOL.SEQUENCE.asc())
            .fetch()) {

            String foreignKey = record.getValue("fkIndexName", String.class);
            String foreignKeyTableName = record.getValue(SYSTABLE.TABLE_NAME);
            String foreignKeyColumn = record.getValue(SYSTABCOL.COLUMN_NAME);
            String referencedKey = record.getValue("ukIndexName", String.class);

            TableDefinition foreignKeyTable = getTable(getSchema(), foreignKeyTableName);

            if (foreignKeyTable != null) {
                ColumnDefinition referencingColumn = foreignKeyTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, referencedKey, referencingColumn, getSchema());
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
                .select(SYSUSER.USER_NAME)
                .from(SYSUSER)
                .fetch(SYSUSER.USER_NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(SYSSEQUENCE.SEQUENCE_NAME)
            .from(SYSSEQUENCE)
            .orderBy(SYSSEQUENCE.SEQUENCE_NAME)
            .fetch(SYSSEQUENCE.SEQUENCE_NAME)) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, getSchema(),
                SybaseDataType.NUMERIC.getTypeName(),
                0, 38, 0, false, false
            );

            result.add(new DefaultSequenceDefinition(getSchema(), name, type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                SYSTABLE.TABLE_NAME,
                SYSTABLE.REMARKS)
            .from(SYSTABLE)
            .fetch()) {

            String name = record.getValue(SYSTABLE.TABLE_NAME);
            String comment = record.getValue(SYSTABLE.REMARKS);

            SybaseTableDefinition table = new SybaseTableDefinition(getSchema(), name, comment);
            result.add(table);
        }

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

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create().select(SYSPROCEDURE.PROC_NAME)
                .from(SYSPROCEDURE)
                .orderBy(SYSPROCEDURE.PROC_NAME)
                .fetch()) {

            String name = record.getValue(SYSPROCEDURE.PROC_NAME);
            result.add(new SybaseRoutineDefinition(getSchema(), null, name));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
