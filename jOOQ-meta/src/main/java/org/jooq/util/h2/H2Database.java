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
package org.jooq.util.h2;

import static org.jooq.util.h2.information_schema.tables.Constraints.CONSTRAINTS;
import static org.jooq.util.h2.information_schema.tables.CrossReferences.CROSS_REFERENCES;
import static org.jooq.util.h2.information_schema.tables.FunctionAliases.FUNCTION_ALIASES;
import static org.jooq.util.h2.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.util.h2.information_schema.tables.Tables.TABLES;
import static org.jooq.util.h2.information_schema.tables.TypeInfo.TYPE_INFO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.h2.information_schema.InformationSchemaFactory;
import org.jooq.util.h2.information_schema.tables.Constraints;
import org.jooq.util.h2.information_schema.tables.CrossReferences;
import org.jooq.util.h2.information_schema.tables.FunctionAliases;
import org.jooq.util.h2.information_schema.tables.Sequences;
import org.jooq.util.h2.information_schema.tables.Tables;
import org.jooq.util.h2.information_schema.tables.TypeInfo;

/**
 * H2 implementation of {@link AbstractDatabase}
 *
 * @author Espen Stromsnes
 */
public class H2Database extends AbstractDatabase {

    @Override
    public Factory create() {
        return new InformationSchemaFactory(getConnection());
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            String tableName = record.getValue(Constraints.TABLE_NAME);
            String columnList = record.getValue(Constraints.COLUMN_LIST);
            String primaryKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                String[] columnNames = columnList.split("[,]+");

                for (String columnName : columnNames) {
                    relations.addPrimaryKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            String tableName = record.getValue(Constraints.TABLE_NAME);
            String columnList = record.getValue(Constraints.COLUMN_LIST);
            String primaryKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                String[] columnNames = columnList.split("[,]+");

                for (String columnName : columnNames) {
                    relations.addUniqueKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) {
        return create().select(
                Constraints.TABLE_NAME,
                Constraints.COLUMN_LIST,
                Constraints.CONSTRAINT_NAME)
            .from(CONSTRAINTS)
            .where(Constraints.TABLE_SCHEMA.equal(getInputSchema()))
            .and(Constraints.CONSTRAINT_TYPE.equal(constraintType))
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    CrossReferences.FK_NAME,
                    CrossReferences.FKTABLE_NAME,
                    CrossReferences.FKCOLUMN_NAME,
                    Constraints.CONSTRAINT_NAME)
                .from(CROSS_REFERENCES)
                .join(CONSTRAINTS)
                .on(CrossReferences.PK_NAME.equal(Constraints.UNIQUE_INDEX_NAME))
                .and(CrossReferences.PKTABLE_NAME.equal(Constraints.TABLE_NAME))
                .and(CrossReferences.PKTABLE_SCHEMA.equal(Constraints.TABLE_SCHEMA))
                .where(CrossReferences.FKTABLE_SCHEMA.equal(getInputSchema()))
                .orderBy(
                    CrossReferences.FK_NAME.asc(),
                    CrossReferences.ORDINAL_POSITION.asc())
                .fetch()) {

            String foreignKeyTableName = record.getValue(CrossReferences.FKTABLE_NAME);
            String foreignKeyColumn = record.getValue(CrossReferences.FKCOLUMN_NAME);
            String foreignKey = record.getValue(CrossReferences.FK_NAME);
            String uniqueKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeyTableName);
            if (foreignKeyTable != null) {
                ColumnDefinition referencingColumn = foreignKeyTable.getColumn(foreignKeyColumn);

                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn);
            }
        }
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(Sequences.SEQUENCE_NAME)
                .from(SEQUENCES)
                .where(Sequences.SEQUENCE_SCHEMA.equal(getInputSchema()))
                .orderBy(Sequences.SEQUENCE_NAME)
                .fetch(Sequences.SEQUENCE_NAME)) {

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(this,
                H2DataType.BIGINT.getTypeName(), 0, 0);

            result.add(new DefaultSequenceDefinition(getSchema(), name, type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                Tables.TABLE_NAME,
                Tables.REMARKS)
            .from(TABLES)
            .where(Tables.TABLE_SCHEMA.equal(getInputSchema()))
            .orderBy(Tables.ID)
            .fetch()) {

            String name = record.getValue(Tables.TABLE_NAME);
            String comment = record.getValue(Tables.REMARKS);

            H2TableDefinition table = new H2TableDefinition(this, name, comment);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .select(
                    FunctionAliases.ALIAS_NAME,
                    FunctionAliases.REMARKS,
                    FunctionAliases.DATA_TYPE,
                    FunctionAliases.RETURNS_RESULT,
                    TypeInfo.TYPE_NAME,
                    TypeInfo.PRECISION,
                    TypeInfo.MAXIMUM_SCALE)
                .from(FUNCTION_ALIASES)
                .leftOuterJoin(TYPE_INFO)
                .on(FunctionAliases.DATA_TYPE.equal(TypeInfo.DATA_TYPE))
                .where(FunctionAliases.ALIAS_SCHEMA.equal(getInputSchema()))
                .and(FunctionAliases.RETURNS_RESULT.in((short) 1, (short) 2))
                .orderBy(FunctionAliases.ALIAS_NAME).fetch()) {

            String name = record.getValue(FunctionAliases.ALIAS_NAME);
            String comment = record.getValue(FunctionAliases.REMARKS);
            String typeName = record.getValue(TypeInfo.TYPE_NAME);
            Integer precision = record.getValue(TypeInfo.PRECISION);
            Short scale = record.getValue(TypeInfo.MAXIMUM_SCALE);

            result.add(new H2RoutineDefinition(this, name, comment, typeName, precision, scale));
        }

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
