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

import static org.jooq.util.sybase.sys.tables.Sysfkey.SYSFKEY;
import static org.jooq.util.sybase.sys.tables.Sysidx.SYSIDX;
import static org.jooq.util.sybase.sys.tables.Sysidxcol.SYSIDXCOL;
import static org.jooq.util.sybase.sys.tables.Systabcol.SYSTABCOL;
import static org.jooq.util.sybase.sys.tables.Systable.SYSTABLE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.sybase.sys.tables.Sysfkey;
import org.jooq.util.sybase.sys.tables.Sysidx;
import org.jooq.util.sybase.sys.tables.Sysidxcol;
import org.jooq.util.sybase.sys.tables.Sysprocedure;
import org.jooq.util.sybase.sys.tables.Syssequence;
import org.jooq.util.sybase.sys.tables.Systabcol;
import org.jooq.util.sybase.sys.tables.Systable;
import org.jooq.util.sybase.sys.tables.records.SysidxRecord;
import org.jooq.util.sybase.sys.tables.records.SystableRecord;

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
                Systable.TABLE_NAME.concat("_").concat(Sysidx.INDEX_NAME).as("indexName"),
                Systable.TABLE_NAME,
                Systabcol.COLUMN_NAME)
            .from(SYSIDX)
            .join(SYSIDXCOL)
            .on(Sysidx.TABLE_ID.equal(Sysidxcol.TABLE_ID))
            .and(Sysidx.INDEX_ID.equal(Sysidxcol.INDEX_ID))
            .join(SYSTABLE)
            .on(Sysidxcol.TABLE_ID.equal(Systable.TABLE_ID))
            .join(SYSTABCOL)
            .on(Sysidxcol.TABLE_ID.equal(Systabcol.TABLE_ID))
            .and(Sysidxcol.COLUMN_ID.equal(Systabcol.COLUMN_ID))
            .where(Sysidx.INDEX_CATEGORY.equal((byte) 1))
            .orderBy(Sysidxcol.SEQUENCE)
            .fetch()) {

            String key = record.getValueAsString("indexName");
            String tableName = record.getValue(Systable.TABLE_NAME);
            String columnName = record.getValue(Systabcol.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }


    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
        for (Record record : create().select(
                Systable.TABLE_NAME.concat("_").concat(Sysidx.INDEX_NAME).as("indexName"),
                Systable.TABLE_NAME,
                Systabcol.COLUMN_NAME)
            .from(SYSIDX)
            .join(SYSIDXCOL)
            .on(Sysidx.TABLE_ID.equal(Sysidxcol.TABLE_ID))
            .and(Sysidx.INDEX_ID.equal(Sysidxcol.INDEX_ID))
            .join(SYSTABLE)
            .on(Sysidxcol.TABLE_ID.equal(Systable.TABLE_ID))
            .join(SYSTABCOL)
            .on(Sysidxcol.TABLE_ID.equal(Systabcol.TABLE_ID))
            .and(Sysidxcol.COLUMN_ID.equal(Systabcol.COLUMN_ID))
            .where(Sysidx.INDEX_CATEGORY.equal((byte) 3))
            .and(Sysidx.UNIQUE.equal((byte) 2))
            .orderBy(Sysidxcol.SEQUENCE)
            .fetch()) {

            String key = record.getValueAsString("indexName");
            String tableName = record.getValue(Systable.TABLE_NAME);
            String columnName = record.getValue(Systabcol.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                r.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Table<SysidxRecord> fkIndex = SYSIDX.as("fkIndex");
        Table<SysidxRecord> ukIndex = SYSIDX.as("ukIndex");

        Table<SystableRecord> fkTable = SYSTABLE.as("fkTable");
        Table<SystableRecord> ukTable = SYSTABLE.as("ukTable");

        for (Record record : create().select(
                fkTable.getField(Systable.TABLE_NAME)
                    .concat("_")
                    .concat(fkIndex.getField(Sysidx.INDEX_NAME))
                    .as("fkIndexName"),
                fkTable.getField(Systable.TABLE_NAME),
                Systabcol.COLUMN_NAME,
                ukTable.getField(Systable.TABLE_NAME)
                    .concat("_")
                    .concat(ukIndex.getField(Sysidx.INDEX_NAME))
                    .as("ukIndexName"))
            .from(SYSFKEY)
            .join(fkIndex)
            .on(Sysfkey.FOREIGN_INDEX_ID.equal(fkIndex.getField(Sysidx.INDEX_ID)))
            .and(Sysfkey.FOREIGN_TABLE_ID.equal(fkIndex.getField(Sysidx.TABLE_ID)))
            .join(SYSIDXCOL)
            .on(fkIndex.getField(Sysidx.INDEX_ID).equal(Sysidxcol.INDEX_ID))
            .and(fkIndex.getField(Sysidx.TABLE_ID).equal(Sysidxcol.TABLE_ID))
            .join(fkTable)
            .on(Sysfkey.FOREIGN_TABLE_ID.equal(fkTable.getField(Systable.TABLE_ID)))
            .join(SYSTABCOL)
            .on(Sysidxcol.TABLE_ID.equal(Systabcol.TABLE_ID))
            .and(Sysidxcol.COLUMN_ID.equal(Systabcol.COLUMN_ID))
            .join(ukIndex)
            .on(Sysfkey.PRIMARY_INDEX_ID.equal(ukIndex.getField(Sysidx.INDEX_ID)))
            .and(Sysfkey.PRIMARY_TABLE_ID.equal(ukIndex.getField(Sysidx.TABLE_ID)))
            .join(ukTable)
            .on(Sysfkey.PRIMARY_TABLE_ID.equal(ukTable.getField(Systable.TABLE_ID)))
            .orderBy(
                fkTable.getField(Systable.TABLE_NAME).asc(),
                fkIndex.getField(Sysidx.INDEX_NAME).asc(),
                Sysidxcol.SEQUENCE.asc())
            .fetch()) {

            String foreignKey = record.getValueAsString("fkIndexName");
            String foreignKeyTableName = record.getValue(Systable.TABLE_NAME);
            String foreignKeyColumn = record.getValue(Systabcol.COLUMN_NAME);
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

        for (String name : create().select(Syssequence.SEQUENCE_NAME)
            .from(Syssequence.SYSSEQUENCE)
            .orderBy(Syssequence.SEQUENCE_NAME)
            .fetch(Syssequence.SEQUENCE_NAME)) {

            result.add(new DefaultSequenceDefinition(this, name));
        }
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                Systable.TABLE_NAME,
                Systable.REMARKS)
            .from(SYSTABLE)
            .fetch()) {

            String name = record.getValue(Systable.TABLE_NAME);
            String comment = record.getValue(Systable.REMARKS);

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

        for (Record record : create().select(Sysprocedure.PROC_NAME)
                .from(Sysprocedure.SYSPROCEDURE)
                .orderBy(Sysprocedure.PROC_NAME)
                .fetch()) {

            String name = record.getValue(Sysprocedure.PROC_NAME);
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
