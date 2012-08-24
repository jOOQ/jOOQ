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
package org.jooq.util.firebird;

import static org.jooq.util.firebird.rdb.Tables.RDB$GENERATORS;
import static org.jooq.util.firebird.rdb.Tables.RDB$INDEX_SEGMENTS;
import static org.jooq.util.firebird.rdb.Tables.RDB$REF_CONSTRAINTS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATIONS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATION_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
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
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.firebird.rdb.tables.Rdb$indexSegments;
import org.jooq.util.firebird.rdb.tables.Rdb$refConstraints;
import org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints;
import org.jooq.util.jaxb.Schema;

/**
 * @author sugi - Initial contribution
 */
public class FirebirdDatabase extends AbstractDatabase {

    public FirebirdDatabase() {

        // Firebird doesn't know schemata
        Schema schema = new Schema();
        schema.setInputSchema("");
        schema.setOutputSchema("");

        List<Schema> schemata = new ArrayList<Schema>();
        schemata.add(schema);

        setConfiguredSchemata(schemata);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            String tableName = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim());
            String fieldName = record.getValue(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim());
            String key = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim());

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null) {
                ColumnDefinition cd = td.getColumn(fieldName);
                r.addPrimaryKey(key, cd);
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            String tableName = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim());
            String fieldName = record.getValue(RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim());
            String key = record.getValue(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim());

            TableDefinition td = getTable(this.getSchemata().get(0), tableName);
            if (td != null) {
                ColumnDefinition cd = td.getColumn(fieldName);
                r.addUniqueKey(key, cd);
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) {
        return create()
            .select(
                RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME.trim(),
                RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME.trim(),
                RDB$INDEX_SEGMENTS.RDB$FIELD_NAME.trim())
            .from(RDB$RELATION_CONSTRAINTS)
            .join(RDB$INDEX_SEGMENTS)
            .on(RDB$INDEX_SEGMENTS.RDB$INDEX_NAME.eq(RDB$RELATION_CONSTRAINTS.RDB$INDEX_NAME))
            .where(RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE.eq(constraintType))
            .fetch();
    }
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Rdb$relationConstraints pk = RDB$RELATION_CONSTRAINTS.as("pk");
        Rdb$relationConstraints fk = RDB$RELATION_CONSTRAINTS.as("fk");
        Rdb$refConstraints rc = RDB$REF_CONSTRAINTS.as("rc");
        Rdb$indexSegments isp = RDB$INDEX_SEGMENTS.as("isp");
        Rdb$indexSegments isf = RDB$INDEX_SEGMENTS.as("isf");

        for (Record record : create()
                .selectDistinct(
                    fk.RDB$CONSTRAINT_NAME.trim().as("fk"),
                    fk.RDB$RELATION_NAME.trim().as("fkTable"),
                    isf.RDB$FIELD_NAME.trim().as("fkField"),
                    pk.RDB$CONSTRAINT_NAME.trim().as("pk"),
                    pk.RDB$RELATION_NAME.trim().as("pkTable"))
                .from(fk)
                .join(rc).on(fk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONSTRAINT_NAME))
                .join(pk).on(pk.RDB$CONSTRAINT_NAME.eq(rc.RDB$CONST_NAME_UQ))
                .join(isp).on(isp.RDB$INDEX_NAME.eq(pk.RDB$INDEX_NAME))
                .join(isf).on(isf.RDB$INDEX_NAME.eq(fk.RDB$INDEX_NAME))
                .where(isp.RDB$FIELD_POSITION.eq(isf.RDB$FIELD_POSITION))
                .orderBy(
                    fk.RDB$CONSTRAINT_NAME.asc(),
                    isf.RDB$FIELD_POSITION.asc())
                .fetch()) {

            String pkName = record.getValue("pk", String.class);
            String pkTable = record.getValue("pkTable", String.class);

            String fkName = record.getValue("fk", String.class);
            String fkTable = record.getValue("fkTable", String.class);
            String fkField = record.getValue("fkField", String.class);

            TableDefinition tdReferencing = getTable(getSchemata().get(0), fkTable, true);
            TableDefinition tdReferenced = getTable(getSchemata().get(0), pkTable, true);

            if (tdReferenced != null && tdReferencing != null) {
                ColumnDefinition referencingColumn = tdReferencing.getColumn(fkField);
                relations.addForeignKey(fkName, pkName, referencingColumn, getSchemata().get(0));
            }
        }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String sequenceName : create()
                .select(RDB$GENERATORS.RDB$GENERATOR_NAME.trim())
                .from(RDB$GENERATORS)
                .orderBy(1)
                .fetch(RDB$GENERATORS.RDB$GENERATOR_NAME.trim())) {

            SchemaDefinition schema = getSchemata().get(0);
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema, FirebirdDataType.BIGINT.getTypeName(), 0, 0, 0);

            result.add(new DefaultSequenceDefinition(schema, sequenceName, type ));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (String tableName : create()
                .select(RDB$RELATIONS.RDB$RELATION_NAME.trim())
                .from(RDB$RELATIONS)
                .orderBy(1)
                .fetch(0, String.class)) {

            TableDefinition tableDef = new FirebirdTableDefinition(getSchemata().get(0), tableName, "");
            result.add(tableDef);
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

    @Override
    protected Factory create0() {
        return new FirebirdFactory(getConnection());
    }
}
