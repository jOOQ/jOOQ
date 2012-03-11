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

package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.sqlserver.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.sqlserver.information_schema.Tables.ROUTINES;
import static org.jooq.util.sqlserver.information_schema.Tables.TABLES;
import static org.jooq.util.sqlserver.information_schema.Tables.TABLE_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;

/**
 * The SQL Server database. This database uses generated artefacts from HSQLDB,
 * because HSQLDB's INFORMATION_SCHEMA is very similar to that of SQL Server,
 * whereas SQL Server's INFORMATION_SCHEMA is not self-contained, i.e. it cannot
 * be generated reading its own data.
 *
 * @author Lukas Eder
 */
public class SQLServerDatabase extends AbstractDatabase {

    @Override
    public Factory create() {
        return new SQLServerFactory(getConnection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.getValue(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.getValue(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) {
        return create()
            .select(
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(TABLE_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(TABLE_CONSTRAINTS.TABLE_SCHEMA.equal(KEY_COLUMN_USAGE.TABLE_SCHEMA))
            .and(TABLE_CONSTRAINTS.CONSTRAINT_NAME.equal(KEY_COLUMN_USAGE.CONSTRAINT_NAME))
            .where(TABLE_CONSTRAINTS.CONSTRAINT_TYPE.equal(constraintType))
            .and(TABLE_CONSTRAINTS.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Result<Record> result = create()
            .select(
                REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME,
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME,
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(REFERENTIAL_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA.equal(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA))
            .and(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME.equal(KEY_COLUMN_USAGE.CONSTRAINT_NAME))
            .where(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.getValue(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME);
            String foreignKeyTable = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String foreignKeyColumn = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);
            String uniqueKey = record.getValue(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

            if (referencingTable != null) {
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME)
                .from(TABLES)
                .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(TABLES.TABLE_SCHEMA));
            String name = record.getValue(TABLES.TABLE_NAME);
            String comment = "";

            result.add(new SQLServerTableDefinition(schema, name, comment));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
            .selectDistinct(
                ROUTINES.ROUTINE_SCHEMA,
                ROUTINES.ROUTINE_NAME,
                ROUTINES.SPECIFIC_NAME,
                ROUTINES.DATA_TYPE,
                ROUTINES.CHARACTER_MAXIMUM_LENGTH,
                ROUTINES.NUMERIC_PRECISION,
                ROUTINES.NUMERIC_SCALE)
            .from(ROUTINES)
            .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                ROUTINES.ROUTINE_SCHEMA,
                ROUTINES.ROUTINE_NAME)
            .fetch()) {

            SQLServerRoutineDefinition routine = new SQLServerRoutineDefinition(
                getSchema(record.getValue(ROUTINES.ROUTINE_SCHEMA)),
                record.getValue(ROUTINES.ROUTINE_NAME),
                record.getValue(ROUTINES.SPECIFIC_NAME),
                record.getValue(ROUTINES.DATA_TYPE),
                record.getValue(ROUTINES.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(ROUTINES.NUMERIC_PRECISION),
                record.getValue(ROUTINES.NUMERIC_SCALE));
            result.add(routine);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
