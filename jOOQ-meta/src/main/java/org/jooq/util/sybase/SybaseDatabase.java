/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
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
 * . Neither the name of the "jOOQ" nor the names of its contributors may be
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
package org.jooq.util.sybase;

import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.sybase.sys.Tables.SYSFKEY;
import static org.jooq.util.sybase.sys.Tables.SYSIDX;
import static org.jooq.util.sybase.sys.Tables.SYSIDXCOL;
import static org.jooq.util.sybase.sys.Tables.SYSPROCEDURE;
import static org.jooq.util.sybase.sys.Tables.SYSSEQUENCE;
import static org.jooq.util.sybase.sys.Tables.SYSTABCOL;
import static org.jooq.util.sybase.sys.Tables.SYSTABLE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
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
            .where(SYSIDX.INDEX_CATEGORY.equal((byte) 1))
            .orderBy(SYSIDXCOL.SEQUENCE)
            .fetch()) {

            String key = record.getValueAsString("indexName");
            String tableName = record.getValue(SYSTABLE.TABLE_NAME);
            String columnName = record.getValue(SYSTABCOL.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
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
            .where(SYSIDX.INDEX_CATEGORY.equal((byte) 3))
            .and(SYSIDX.UNIQUE.equal((byte) 2))
            .orderBy(SYSIDXCOL.SEQUENCE)
            .fetch()) {

            String key = record.getValueAsString("indexName");
            String tableName = record.getValue(SYSTABLE.TABLE_NAME);
            String columnName = record.getValue(SYSTABCOL.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
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

            String foreignKey = record.getValueAsString("fkIndexName");
            String foreignKeyTableName = record.getValue(SYSTABLE.TABLE_NAME);
            String foreignKeyColumn = record.getValue(SYSTABCOL.COLUMN_NAME);
            String referencedKey = record.getValueAsString("ukIndexName");

            TableDefinition foreignKeyTable = getTable(foreignKeyTableName);

            if (foreignKeyTable != null) {
                ColumnDefinition referencingColumn = foreignKeyTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, referencedKey, referencingColumn);
            }
        }
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(SYSSEQUENCE.SEQUENCE_NAME)
            .from(SYSSEQUENCE)
            .orderBy(SYSSEQUENCE.SEQUENCE_NAME)
            .fetch(SYSSEQUENCE.SEQUENCE_NAME)) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(this,
                SybaseDataType.NUMERIC.getTypeName(), 38, 0);

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

            SybaseTableDefinition table = new SybaseTableDefinition(this, name, comment);
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
            result.add(new SybaseRoutineDefinition(this, null, name));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    public Factory create() {
        return new Factory(getConnection(), SQLDialect.SYBASE);
    }
}
