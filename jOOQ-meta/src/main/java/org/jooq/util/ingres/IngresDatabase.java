/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.ingres;

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.trim;
import static org.jooq.util.ingres.ingres.Tables.IICONSTRAINTS;
import static org.jooq.util.ingres.ingres.Tables.IIDB_COMMENTS;
import static org.jooq.util.ingres.ingres.Tables.IIREF_CONSTRAINTS;
import static org.jooq.util.ingres.ingres.Tables.IISCHEMA;
import static org.jooq.util.ingres.ingres.Tables.IISEQUENCES;
import static org.jooq.util.ingres.ingres.Tables.IITABLES;
import static org.jooq.util.ingres.ingres.tables.Iikeys.IIKEYS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.ingres.ingres.tables.Iiconstraints;
import org.jooq.util.ingres.ingres.tables.IidbComments;
import org.jooq.util.ingres.ingres.tables.Iikeys;
import org.jooq.util.ingres.ingres.tables.IirefConstraints;
import org.jooq.util.ingres.ingres.tables.Iischema;
import org.jooq.util.ingres.ingres.tables.Iisequences;
import org.jooq.util.ingres.ingres.tables.Iitables;

/**
 * @author Lukas Eder
 */
public class IngresDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.INGRES);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.getValue(trim(Iiconstraints.SCHEMA_NAME)));
            String key = record.getValue(trim(Iiconstraints.CONSTRAINT_NAME));
            String tableName = record.getValue(trim(Iiconstraints.TABLE_NAME));
            String columnName = record.getValue(trim(Iikeys.COLUMN_NAME));

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(trim(Iiconstraints.SCHEMA_NAME)));
            String key = record.getValue(trim(Iiconstraints.CONSTRAINT_NAME));
            String tableName = record.getValue(trim(Iiconstraints.TABLE_NAME));
            String columnName = record.getValue(trim(Iikeys.COLUMN_NAME));

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                    trim(Iiconstraints.SCHEMA_NAME),
                    trim(Iiconstraints.TABLE_NAME),
                    trim(Iiconstraints.CONSTRAINT_NAME),
                    trim(Iikeys.COLUMN_NAME))
                .from(IICONSTRAINTS)
                .join(IIKEYS)
                .on(row(Iiconstraints.CONSTRAINT_NAME, Iiconstraints.SCHEMA_NAME)
                    .eq(Iikeys.CONSTRAINT_NAME, Iikeys.SCHEMA_NAME))
                .where(Iiconstraints.SCHEMA_NAME.in(getInputSchemata()))
                .and(Iiconstraints.CONSTRAINT_TYPE.trim().equal(constraintType))
                .orderBy(
                    Iiconstraints.SCHEMA_NAME.asc(),
                    Iiconstraints.TABLE_NAME.asc(),
                    Iiconstraints.CONSTRAINT_NAME.asc(),
                    Iikeys.KEY_POSITION.asc())
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Result<?> result = create()
            .select(
                trim(IirefConstraints.REF_SCHEMA_NAME),
                trim(IirefConstraints.REF_CONSTRAINT_NAME),
                trim(IirefConstraints.UNIQUE_CONSTRAINT_NAME),
                trim(IirefConstraints.UNIQUE_SCHEMA_NAME),
                trim(IirefConstraints.REF_TABLE_NAME),
                trim(Iikeys.COLUMN_NAME))
            .from(IICONSTRAINTS)
            .join(IIREF_CONSTRAINTS)
            .on(row(Iiconstraints.CONSTRAINT_NAME, Iiconstraints.SCHEMA_NAME)
                .eq(IirefConstraints.REF_CONSTRAINT_NAME, IirefConstraints.REF_SCHEMA_NAME))
            .join(IIKEYS)
            .on(row(IirefConstraints.REF_CONSTRAINT_NAME, IirefConstraints.REF_SCHEMA_NAME)
                .eq(Iikeys.CONSTRAINT_NAME, Iikeys.SCHEMA_NAME))
            .where(Iiconstraints.SCHEMA_NAME.in(getInputSchemata()))
            .and(Iiconstraints.CONSTRAINT_TYPE.equal("R"))
            .orderBy(
                IirefConstraints.REF_SCHEMA_NAME.asc(),
                IirefConstraints.REF_TABLE_NAME.asc(),
                IirefConstraints.REF_CONSTRAINT_NAME.asc(),
                Iikeys.KEY_POSITION.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.getValue(trim(IirefConstraints.REF_SCHEMA_NAME)));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(trim(IirefConstraints.UNIQUE_SCHEMA_NAME)));

            String foreignKey = record.getValue(trim(IirefConstraints.REF_CONSTRAINT_NAME));
            String foreignKeyTable = record.getValue(trim(IirefConstraints.REF_TABLE_NAME));
            String foreignKeyColumn = record.getValue(trim(Iikeys.COLUMN_NAME));
            String uniqueKey = record.getValue(trim(IirefConstraints.UNIQUE_CONSTRAINT_NAME));

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

            if (referencingTable != null) {
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
            }
        }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(trim(Iischema.SCHEMA_NAME))
                .from(IISCHEMA)
                .fetch(trim(Iischema.SCHEMA_NAME))) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    trim(Iisequences.SEQ_OWNER),
                    trim(Iisequences.SEQ_NAME),
                    trim(Iisequences.DATA_TYPE))
                .from(IISEQUENCES)
                .where(Iisequences.SEQ_OWNER.in(getInputSchemata()))
                .orderBy(
                    Iisequences.SEQ_OWNER,
                    Iisequences.SEQ_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(trim(Iisequences.SEQ_OWNER)));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema,
                record.getValue(trim(Iisequences.DATA_TYPE)), 0, 0, 0);

            result.add(new DefaultSequenceDefinition(
                schema, record.getValue(trim(Iisequences.SEQ_NAME)), type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                    trim(Iitables.TABLE_OWNER),
                    trim(Iitables.TABLE_NAME),
                    trim(IidbComments.LONG_REMARK))
                .from(IITABLES)
                .leftOuterJoin(IIDB_COMMENTS)
                .on(Iitables.TABLE_NAME.equal(IidbComments.OBJECT_NAME))
                .and(Iitables.TABLE_OWNER.equal(IidbComments.OBJECT_OWNER))
                .and(IidbComments.OBJECT_TYPE.equal("T"))
                .and(IidbComments.TEXT_SEQUENCE.equal(1L))
                .where(Iitables.TABLE_OWNER.in(getInputSchemata()))
                .orderBy(
                    trim(Iitables.TABLE_OWNER),
                    trim(Iitables.TABLE_NAME))
                .fetch()) {

            result.add(new IngresTableDefinition(
                getSchema(record.getValue(trim(Iitables.TABLE_OWNER))),
                record.getValue(trim(Iitables.TABLE_NAME)),
                record.getValue(trim(IidbComments.LONG_REMARK))));
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
