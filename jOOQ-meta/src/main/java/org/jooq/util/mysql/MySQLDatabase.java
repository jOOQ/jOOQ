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

package org.jooq.util.mysql;

import static org.jooq.util.mysql.information_schema.tables.Columns.COLUMNS;
import static org.jooq.util.mysql.information_schema.tables.KeyColumnUsage.KEY_COLUMN_USAGE;
import static org.jooq.util.mysql.information_schema.tables.ReferentialConstraints.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.mysql.information_schema.tables.TableConstraints.TABLE_CONSTRAINTS;
import static org.jooq.util.mysql.information_schema.tables.Tables.TABLES;
import static org.jooq.util.mysql.mysql.tables.Proc.DB;
import static org.jooq.util.mysql.mysql.tables.Proc.PROC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.mysql.information_schema.tables.Columns;
import org.jooq.util.mysql.information_schema.tables.KeyColumnUsage;
import org.jooq.util.mysql.information_schema.tables.ReferentialConstraints;
import org.jooq.util.mysql.information_schema.tables.TableConstraints;
import org.jooq.util.mysql.information_schema.tables.Tables;
import org.jooq.util.mysql.mysql.tables.Proc;

/**
 * @author Lukas Eder
 */
public class MySQLDatabase extends AbstractDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.getValue(KeyColumnUsage.TABLE_SCHEMA));
            String constraintName = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.getValue(KeyColumnUsage.TABLE_SCHEMA));
            String constraintName = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            String key = getKeyName(tableName, constraintName);
            TableDefinition table = getTable(schema, tableName);

            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private String getKeyName(String tableName, String keyName) {
        return "KEY_" + tableName + "_" + keyName;
    }

    private List<Record> fetchKeys(String constraintType) {
        return create().select(
                KeyColumnUsage.TABLE_SCHEMA,
                KeyColumnUsage.CONSTRAINT_NAME,
                KeyColumnUsage.TABLE_NAME,
                KeyColumnUsage.COLUMN_NAME)
            .from(KEY_COLUMN_USAGE)
            .join(TABLE_CONSTRAINTS)
            .on(KeyColumnUsage.TABLE_SCHEMA.equal(TableConstraints.TABLE_SCHEMA))
            .and(KeyColumnUsage.TABLE_NAME.equal(TableConstraints.TABLE_NAME))
            .and(KeyColumnUsage.CONSTRAINT_NAME.equal(TableConstraints.CONSTRAINT_NAME))
            .where(TableConstraints.CONSTRAINT_TYPE.equal(constraintType))
            .and(KeyColumnUsage.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KeyColumnUsage.TABLE_SCHEMA.asc(),
                KeyColumnUsage.TABLE_NAME.asc(),
                KeyColumnUsage.ORDINAL_POSITION.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ReferentialConstraints.CONSTRAINT_SCHEMA,
                    ReferentialConstraints.CONSTRAINT_NAME,
                    ReferentialConstraints.TABLE_NAME,
                    ReferentialConstraints.REFERENCED_TABLE_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_NAME,
                    ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA,
                    KeyColumnUsage.COLUMN_NAME)
                .from(REFERENTIAL_CONSTRAINTS)
                .join(KEY_COLUMN_USAGE)
                .on(ReferentialConstraints.CONSTRAINT_SCHEMA.equal(KeyColumnUsage.CONSTRAINT_SCHEMA))
                .and(ReferentialConstraints.CONSTRAINT_NAME.equal(KeyColumnUsage.CONSTRAINT_NAME))
                .where(ReferentialConstraints.CONSTRAINT_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    KeyColumnUsage.CONSTRAINT_SCHEMA.asc(),
                    KeyColumnUsage.CONSTRAINT_NAME.asc(),
                    KeyColumnUsage.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(ReferentialConstraints.CONSTRAINT_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(ReferentialConstraints.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.getValue(ReferentialConstraints.CONSTRAINT_NAME);
            String foreignKeyColumn = record.getValue(KeyColumnUsage.COLUMN_NAME);
            String foreignKeyTableName = record.getValue(ReferentialConstraints.TABLE_NAME);
            String referencedKey = record.getValue(ReferentialConstraints.UNIQUE_CONSTRAINT_NAME);
            String referencedTableName = record.getValue(ReferentialConstraints.REFERENCED_TABLE_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);

            if (foreignKeyTable != null) {
                ColumnDefinition column = foreignKeyTable.getColumn(foreignKeyColumn);

                String key = getKeyName(referencedTableName, referencedKey);
                relations.addForeignKey(foreignKey, key, column, uniqueKeySchema);
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

        for (Record record : create().select(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME,
                Tables.TABLE_COMMENT)
            .from(TABLES)
            .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                Tables.TABLE_SCHEMA,
                Tables.TABLE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Tables.TABLE_SCHEMA));
            String name = record.getValue(Tables.TABLE_NAME);
            String comment = record.getValue(Tables.TABLE_COMMENT);

            MySQLTableDefinition table = new MySQLTableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        Result<Record> records = create()
            .select(
                Columns.TABLE_SCHEMA,
                Columns.COLUMN_COMMENT,
                Columns.TABLE_NAME,
                Columns.COLUMN_NAME,
                Columns.COLUMN_TYPE)
            .from(COLUMNS)
            .where(
                Columns.COLUMN_TYPE.like("enum(%)").and(
                Columns.TABLE_SCHEMA.in(getInputSchemata())))
            .orderBy(
                Columns.TABLE_SCHEMA.asc(),
                Columns.TABLE_NAME.asc(),
                Columns.COLUMN_NAME.asc())
            .fetch();

        for (Record record : records) {
            SchemaDefinition schema = getSchema(record.getValue(Columns.TABLE_SCHEMA));

            String comment = record.getValue(Columns.COLUMN_COMMENT);
            String table = record.getValue(Columns.TABLE_NAME);
            String column = record.getValue(Columns.COLUMN_NAME);
            String name = table + "_" + column;
            String columnType = record.getValue(Columns.COLUMN_TYPE);

            // [#1237] Don't generate enum classes for columns in MySQL tables
            // that are excluded from code generation
            TableDefinition tableDefinition = getTable(schema, table);
            if (tableDefinition != null) {
                ColumnDefinition columnDefinition = tableDefinition.getColumn(column);

                if (columnDefinition != null) {
                	
                	// [#1137] Avoid generating enum classes for enum types that
                	// are explicitly forced to another type
                    if (getConfiguredForcedType(columnDefinition) == null) {
                        DefaultEnumDefinition definition = new DefaultEnumDefinition(schema, name, comment);
                        for (String string : columnType.replaceAll("enum\\(|\\)", "").split(",")) {
                            definition.addLiteral(string.trim().replaceAll("'", ""));
                        }

                        result.add(definition);
                    }
                }
            }
        }

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

        for (Record record : create().select(
                    Proc.DB,
                    Proc.NAME,
                    Proc.COMMENT,
                    Proc.PARAM_LIST,
                    Proc.RETURNS)
                .from(PROC)
                .where(DB.in(getInputSchemata()))
                .orderBy(
                    DB,
                    Proc.NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(DB));
            String name = record.getValue(Proc.NAME);
            String comment = record.getValue(Proc.COMMENT);
            String params = new String(record.getValue(Proc.PARAM_LIST));
            String returns = new String(record.getValue(Proc.RETURNS));

            result.add(new MySQLRoutineDefinition(schema, name, comment, params, returns));
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
        return new MySQLFactory(getConnection());
    }
}
