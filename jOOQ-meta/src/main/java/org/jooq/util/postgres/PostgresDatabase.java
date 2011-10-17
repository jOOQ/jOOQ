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

package org.jooq.util.postgres;

import static org.jooq.util.postgres.information_schema.tables.Attributes.ATTRIBUTES;
import static org.jooq.util.postgres.information_schema.tables.Attributes.UDT_NAME;
import static org.jooq.util.postgres.information_schema.tables.Attributes.UDT_SCHEMA;
import static org.jooq.util.postgres.information_schema.tables.KeyColumnUsage.KEY_COLUMN_USAGE;
import static org.jooq.util.postgres.information_schema.tables.Parameters.PARAMETERS;
import static org.jooq.util.postgres.information_schema.tables.ReferentialConstraints.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.postgres.information_schema.tables.Routines.ROUTINES;
import static org.jooq.util.postgres.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.util.postgres.information_schema.tables.TableConstraints.TABLE_CONSTRAINTS;
import static org.jooq.util.postgres.information_schema.tables.Tables.TABLES;
import static org.jooq.util.postgres.pg_catalog.tables.PgEnum.PG_ENUM;
import static org.jooq.util.postgres.pg_catalog.tables.PgType.PG_TYPE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.hsqldb.HSQLDBDatabase;
import org.jooq.util.postgres.information_schema.InformationSchemaFactory;
import org.jooq.util.postgres.information_schema.tables.KeyColumnUsage;
import org.jooq.util.postgres.information_schema.tables.Parameters;
import org.jooq.util.postgres.information_schema.tables.ReferentialConstraints;
import org.jooq.util.postgres.information_schema.tables.Routines;
import org.jooq.util.postgres.information_schema.tables.Sequences;
import org.jooq.util.postgres.information_schema.tables.TableConstraints;
import org.jooq.util.postgres.information_schema.tables.Tables;
import org.jooq.util.postgres.information_schema.tables.records.RoutinesRecord;
import org.jooq.util.postgres.pg_catalog.tables.PgEnum;
import org.jooq.util.postgres.pg_catalog.tables.PgType;

/**
 * Postgres uses the ANSI default INFORMATION_SCHEMA, but unfortunately ships
 * with a non-capitalised version of it: <code>information_schema</code>. Hence
 * the {@link HSQLDBDatabase} is not used here.
 *
 * @author Lukas Eder
 */
public class PostgresDatabase extends AbstractDatabase {

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
                KeyColumnUsage.CONSTRAINT_NAME.asc(),
                KeyColumnUsage.ORDINAL_POSITION.asc())
            .fetch();
    }

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

            result.add(new PostgresTableDefinition(this, name, comment));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(Sequences.SEQUENCE_NAME)
            .from(SEQUENCES)
            .where(Sequences.SEQUENCE_SCHEMA.equal(getSchemaName()))
            .orderBy(Sequences.SEQUENCE_NAME)
            .fetch(Sequences.SEQUENCE_NAME)) {

            result.add(new DefaultSequenceDefinition(this, name));
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        Result<Record> records = create()
            .select(PgType.TYPNAME, PgEnum.ENUMLABEL)
            .from(PG_ENUM)
            .join(PG_TYPE).on("pg_enum.enumtypid = pg_type.oid")
            .orderBy(PgEnum.ENUMTYPID).fetch();

        DefaultEnumDefinition definition = null;
        for (Record record : records) {
            String typeName = String.valueOf(record.getValue(PgType.TYPNAME));

            if (definition == null || !definition.getName().equals(typeName)) {
                definition = new DefaultEnumDefinition(this, typeName, null);
                result.add(definition);
            }

            definition.addLiteral(String.valueOf(record.getValue(PgEnum.ENUMLABEL)));
        }

        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (String name : create().selectDistinct(UDT_NAME)
            .from(ATTRIBUTES)
            .where(UDT_SCHEMA.equal(getSchemaName()))
            .orderBy(UDT_NAME).fetch(UDT_NAME)) {

            result.add(new PostgresUDTDefinition(this, name, null));
        }

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

        Table<RoutinesRecord> r1 = ROUTINES.as("r1");
        Table<RoutinesRecord> r2 = ROUTINES.as("r2");

        for (Record record : create().select(
                r1.getField(Routines.ROUTINE_NAME),
                r1.getField(Routines.SPECIFIC_NAME),

                // Ignore the data type when there is at least one out parameter
                create().decode()
                    .when(create().exists(create()
                        .selectOne()
                        .from(PARAMETERS)
                        .where(Parameters.SPECIFIC_SCHEMA.equal(r1.getField(Routines.SPECIFIC_SCHEMA)))
                        .and(Parameters.SPECIFIC_NAME.equal(r1.getField(Routines.SPECIFIC_NAME)))
                        .and(Parameters.PARAMETER_MODE.upper().notEqual("IN"))), create().val("void"))
                    .otherwise(r1.getField(Routines.DATA_TYPE)).as("data_type"),
                r1.getField(Routines.NUMERIC_PRECISION),
                r1.getField(Routines.NUMERIC_SCALE),
                r1.getField(Routines.TYPE_UDT_NAME),

                // Calculate overload index if applicable
                create().decode().when(
                create().exists(
                    create().selectOne()
                        .from(r2)
                        .where(r2.getField(Routines.ROUTINE_SCHEMA).equal(getSchemaName()))
                        .and(r2.getField(Routines.ROUTINE_NAME).equal(r1.getField(Routines.ROUTINE_NAME)))
                        .and(r2.getField(Routines.SPECIFIC_NAME).notEqual(r1.getField(Routines.SPECIFIC_NAME)))),
                    create().select(create().count())
                        .from(r2)
                        .where(r2.getField(Routines.ROUTINE_SCHEMA).equal(getSchemaName()))
                        .and(r2.getField(Routines.ROUTINE_NAME).equal(r1.getField(Routines.ROUTINE_NAME)))
                        .and(r2.getField(Routines.SPECIFIC_NAME).lessOrEqual(r1.getField(Routines.SPECIFIC_NAME))).asField()).as("overload"))
            .from(r1)
            .where(r1.getField(Routines.ROUTINE_SCHEMA).equal(getSchemaName()))
            .orderBy(r1.getField(Routines.ROUTINE_NAME).asc())
            .fetch()) {

            result.add(new PostgresRoutineDefinition(this, record));
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
        return new InformationSchemaFactory(getConnection());
    }
}
