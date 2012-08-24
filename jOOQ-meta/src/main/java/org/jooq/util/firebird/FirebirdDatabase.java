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

import static org.jooq.util.firebird.rdb.Tables.RDB$DEPENDENCIES;
import static org.jooq.util.firebird.rdb.Tables.RDB$INDEX_SEGMENTS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATIONS;
import static org.jooq.util.firebird.rdb.Tables.RDB$RELATION_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Record;
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
import org.jooq.util.firebird.rdb.tables.Rdb$dependencies;
import org.jooq.util.firebird.rdb.tables.Rdb$indexSegments;
import org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints;
import org.jooq.util.firebird.rdb.tables.Rdb$relations;
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
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        Rdb$relations r = RDB$RELATIONS.as("r");
        Rdb$relationConstraints rc = RDB$RELATION_CONSTRAINTS.as("rc");
        Rdb$indexSegments is = RDB$INDEX_SEGMENTS.as("is");

        for (String tableName : create()
                .select(r.RDB$RELATION_NAME.trim())
                .from(r)
                .where(r.RDB$VIEW_BLR.isNull())
                .orderBy(1)
                .fetch(r.RDB$RELATION_NAME.trim())) {

            for (String fieldName : create()
                    .select(is.RDB$FIELD_NAME)
                    .from(rc)
                    .join(is).on(is.RDB$INDEX_NAME.eq(rc.RDB$INDEX_NAME))
                    .where(rc.RDB$RELATION_NAME.eq(tableName))
                    .and(rc.RDB$CONSTRAINT_TYPE.eq("PRIMARY KEY"))
                    .fetch(is.RDB$FIELD_NAME)) {

                String key = "PK_" + tableName + "_" + fieldName;

                TableDefinition td = this.getTable(this.getSchemata().get(0), tableName);
                if (td != null) {
                    ColumnDefinition cd = td.getColumn(fieldName);
                    relations.addPrimaryKey(key, cd);
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Rdb$relationConstraints rc = RDB$RELATION_CONSTRAINTS.as("rc");
        Rdb$dependencies d1 = RDB$DEPENDENCIES.as("d1");
        Rdb$dependencies d2 = RDB$DEPENDENCIES.as("d2");

        for (TableDefinition table : getTables(getSchemata().get(0))) {
            Map<String, Integer> map = new HashMap<String, Integer>();

            for (Record record : create()
                    .selectDistinct(
                        rc.RDB$CONSTRAINT_NAME,
                        rc.RDB$RELATION_NAME,
                        d1.RDB$FIELD_NAME,
                        d2.RDB$DEPENDED_ON_NAME,
                        d2.RDB$FIELD_NAME)
                    .from(rc)
                    .leftOuterJoin(d1).on(d1.RDB$DEPENDED_ON_NAME.eq(rc.RDB$RELATION_NAME))
                    .leftOuterJoin(d2).on(d1.RDB$DEPENDENT_NAME.eq(d2.RDB$DEPENDENT_NAME))
                    .where(rc.RDB$CONSTRAINT_TYPE.eq("FOREIGN KEY"))
                    .and(d1.RDB$DEPENDED_ON_NAME.ne(d2.RDB$DEPENDED_ON_NAME))
                    .and(d1.RDB$FIELD_NAME.ne(d2.RDB$FIELD_NAME))
                    .and(rc.RDB$RELATION_NAME.eq(table.getName()))
                    .fetch()) {

                String fkPrefix =
                    "FK_" + record.getValue(rc.RDB$RELATION_NAME) +
                    "_TO_" + record.getValue(d2.RDB$DEPENDED_ON_NAME);

                Integer sequence = map.get(fkPrefix);
                if (sequence == null) {
                    sequence = new Integer(0);
                    map.put(fkPrefix, sequence);
                } else {
                    sequence = sequence + 1;
                }

                String fkName = fkPrefix + "_" + sequence;
                String tableName = table.getName();
                String fieldName = record.getValue(d1.RDB$FIELD_NAME);
                String foreignTableName = record.getValue(d2.RDB$DEPENDED_ON_NAME);
                String foreignFieldName = record.getValue(d2.RDB$FIELD_NAME);

                TableDefinition tdReferencing = getTable(getSchemata().get(0), tableName, true);
                TableDefinition tdReferenced = getTable(getSchemata().get(0), foreignTableName, true);

                if (tdReferenced != null) {
                    String pkName = "PK_" + foreignTableName + "_" + foreignFieldName;

                    if (tdReferencing != null) {
                        ColumnDefinition referencingColumn = tdReferencing.getColumn(fieldName);
                        relations.addForeignKey(fkName, pkName, referencingColumn, getSchemata().get(0));
                    }
                }
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
        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record recTableName : create()
                .select(RDB$RELATIONS.RDB$RELATION_NAME.trim())
                .from(RDB$RELATIONS)
                .orderBy(1)
                .fetch()) {

            String tableName = recTableName.getValueAsString(0);

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
