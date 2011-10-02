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

package org.jooq.util.hsqldb;

import static org.jooq.util.hsqldb.information_schema.tables.ElementTypes.ELEMENT_TYPES;
import static org.jooq.util.hsqldb.information_schema.tables.KeyColumnUsage.KEY_COLUMN_USAGE;
import static org.jooq.util.hsqldb.information_schema.tables.ReferentialConstraints.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.hsqldb.information_schema.tables.Routines.ROUTINES;
import static org.jooq.util.hsqldb.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.util.hsqldb.information_schema.tables.TableConstraints.TABLE_CONSTRAINTS;
import static org.jooq.util.hsqldb.information_schema.tables.Tables.TABLES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
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
import org.jooq.util.hsqldb.information_schema.tables.ElementTypes;
import org.jooq.util.hsqldb.information_schema.tables.KeyColumnUsage;
import org.jooq.util.hsqldb.information_schema.tables.ReferentialConstraints;
import org.jooq.util.hsqldb.information_schema.tables.Routines;
import org.jooq.util.hsqldb.information_schema.tables.Sequences;
import org.jooq.util.hsqldb.information_schema.tables.TableConstraints;
import org.jooq.util.hsqldb.information_schema.tables.Tables;

/**
 * The HSQLDB database
 *
 * @author Lukas Eder
 */
public class HSQLDBDatabase extends AbstractDatabase {

    @Override
    public Factory create() {
        return new HSQLDBFactory(getConnection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            String key = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
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
            String key = record.getValue(KeyColumnUsage.CONSTRAINT_NAME);
            String tableName = record.getValue(KeyColumnUsage.TABLE_NAME);
            String columnName = record.getValue(KeyColumnUsage.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) throws SQLException {
        return create()
            .select(KeyColumnUsage.CONSTRAINT_NAME, KeyColumnUsage.TABLE_NAME, KeyColumnUsage.COLUMN_NAME)
            .from(TABLE_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(TableConstraints.CONSTRAINT_NAME.equal(KeyColumnUsage.CONSTRAINT_NAME))
            .where(TableConstraints.CONSTRAINT_TYPE.equal(constraintType))
            .and(TableConstraints.TABLE_SCHEMA.equal(getSchemaName()))
            .orderBy(
                KeyColumnUsage.TABLE_SCHEMA.asc(),
                KeyColumnUsage.TABLE_NAME.asc(),
                KeyColumnUsage.ORDINAL_POSITION.asc())
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Result<Record> result = create()
            .select(
                ReferentialConstraints.CONSTRAINT_NAME,
                ReferentialConstraints.UNIQUE_CONSTRAINT_NAME,
                KeyColumnUsage.TABLE_NAME,
                KeyColumnUsage.COLUMN_NAME)
            .from(REFERENTIAL_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(ReferentialConstraints.CONSTRAINT_NAME.equal(KeyColumnUsage.CONSTRAINT_NAME))
            .where(ReferentialConstraints.CONSTRAINT_SCHEMA.equal(getSchemaName()))
            .orderBy(
                KeyColumnUsage.TABLE_SCHEMA.asc(),
                KeyColumnUsage.TABLE_NAME.asc(),
                KeyColumnUsage.ORDINAL_POSITION.asc())
            .fetch();

        for (Record record : result) {
            String foreignKey = record.getValue(ReferentialConstraints.CONSTRAINT_NAME);
            String foreignKeyTable = record.getValue(KeyColumnUsage.TABLE_NAME);
            String foreignKeyColumn = record.getValue(KeyColumnUsage.COLUMN_NAME);
            String uniqueKey = record.getValue(ReferentialConstraints.UNIQUE_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeyTable);

            if (referencingTable != null) {
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create()
            .select(Sequences.SEQUENCE_NAME)
            .from(SEQUENCES)
            .where(Sequences.SEQUENCE_SCHEMA.equal(getSchemaName()))
            .orderBy(Sequences.SEQUENCE_NAME)
            .fetch(Sequences.SEQUENCE_NAME)) {

            result.add(new DefaultSequenceDefinition(this, name));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (String name : create()
            .select(Tables.TABLE_NAME)
            .from(TABLES)
            .where(Tables.TABLE_SCHEMA.equal(getSchemaName()))
            .orderBy(Tables.TABLE_NAME)
            .fetch(Tables.TABLE_NAME)) {

            String comment = "";

            result.add(new HSQLDBTableDefinition(this, name, comment));
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .select(
                    Routines.ROUTINE_NAME,
                    Routines.SPECIFIC_NAME,
                    ElementTypes.COLLECTION_TYPE_IDENTIFIER.nvl(Routines.DATA_TYPE).as("datatype"),
                    Routines.NUMERIC_PRECISION,
                    Routines.NUMERIC_SCALE)
                .from(ROUTINES)
                .leftOuterJoin(ELEMENT_TYPES)
                .on(Routines.ROUTINE_SCHEMA.equal(ElementTypes.OBJECT_SCHEMA))
                .and(Routines.ROUTINE_NAME.equal(ElementTypes.OBJECT_NAME))
                .and(Routines.DTD_IDENTIFIER.equal(ElementTypes.COLLECTION_TYPE_IDENTIFIER))
                .where(Routines.ROUTINE_SCHEMA.equal(getSchemaName()))
                .orderBy(Routines.ROUTINE_NAME)
                .fetch()) {

            String name = record.getValue(Routines.ROUTINE_NAME);
            String specificName = record.getValue(Routines.SPECIFIC_NAME);
            String dataType = record.getValueAsString("datatype");
            Long precision = record.getValue(Routines.NUMERIC_PRECISION);
            Long scale = record.getValue(Routines.NUMERIC_SCALE);

            HSQLDBRoutineDefinition function = new HSQLDBRoutineDefinition(this, name, specificName, dataType, precision, scale);
            result.add(function);
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
