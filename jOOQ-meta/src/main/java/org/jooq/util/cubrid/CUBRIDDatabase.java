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

package org.jooq.util.cubrid;

import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.cubrid.dba.Tables.DB_CLASS;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX_KEY;
import static org.jooq.util.cubrid.dba.Tables.DB_SERIAL;

import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;

/**
 * @author Lukas Eder
 */
public class CUBRIDDatabase extends AbstractDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_UNIQUE.isTrue().and(DB_INDEX.IS_PRIMARY_KEY.isFalse()))) {
            SchemaDefinition schema = getSchema(record.getValue(DB_CLASS.OWNER_NAME));
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(DB_CLASS.CLASS_NAME);
            String columnName = record.getValue(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_PRIMARY_KEY.isTrue())) {
            SchemaDefinition schema = getSchema(record.getValue(DB_CLASS.OWNER_NAME));
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(DB_CLASS.CLASS_NAME);
            String columnName = record.getValue(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record> fetchKeys(Condition condition) {
        return
        create().select(
                    concat(DB_CLASS.CLASS_NAME, val("__"), DB_INDEX.INDEX_NAME).as("constraint_name"),
                    DB_INDEX_KEY.KEY_ATTR_NAME,
                    DB_CLASS.CLASS_NAME,
                    DB_CLASS.OWNER_NAME)
                .from(DB_INDEX)
                .join(DB_CLASS).on(DB_INDEX.CLASS_NAME.equal(DB_CLASS.CLASS_NAME))
                .join(DB_INDEX_KEY).on(
                    DB_INDEX_KEY.INDEX_NAME.equal(DB_INDEX.INDEX_NAME).and(
                    DB_INDEX_KEY.CLASS_NAME.equal(DB_INDEX.CLASS_NAME)))
                .where(condition)
                .and(DB_CLASS.OWNER_NAME.in(getInputSchemata()))
                .orderBy(
                    DB_CLASS.OWNER_NAME.asc(),
                    DB_INDEX.INDEX_NAME.asc())
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        DatabaseMetaData meta = create().getConnection().getMetaData();

        for (String table : create()
                .selectDistinct(DB_INDEX.CLASS_NAME)
                .from(DB_INDEX)
                .where(DB_INDEX.IS_FOREIGN_KEY.isTrue())
                .fetch(DB_INDEX.CLASS_NAME)) {

            for (Record record : create().fetch(meta.getImportedKeys(null, null, table))) {
                SchemaDefinition foreignKeySchema = getSchema(getInputSchemata().get(0));
                SchemaDefinition uniqueKeySchema = getSchema(getInputSchemata().get(0));

                String foreignKeyName =
                    record.getValue("FKTABLE_NAME", String.class) +
                    "__" +
                    record.getValue("FK_NAME", String.class);
                String foreignKeyTableName = record.getValue("FKTABLE_NAME", String.class);
                String foreignKeyColumnName = record.getValue("FKCOLUMN_NAME", String.class);
                String uniqueKeyName =
                    record.getValue("PKTABLE_NAME", String.class) +
                    "__" +
                    record.getValue("PK_NAME", String.class);

                TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTableName);
                if (referencingTable != null) {
                    ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                    relations.addForeignKey(foreignKeyName, uniqueKeyName, column, uniqueKeySchema);
                }
            }
        }
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    DB_SERIAL.NAME,
                    DB_SERIAL.MAX_VAL,
                    field("owner.name", String.class).as("owner"))
                .from(DB_SERIAL)
                .where(field("owner.name", String.class).in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue("owner", String.class));
            BigInteger value = record.getValue(DB_SERIAL.MAX_VAL, BigInteger.class, BigInteger.valueOf(Long.MAX_VALUE));
            DataTypeDefinition type = getDataTypeForMAX_VAL(schema, value);

            result.add(new DefaultSequenceDefinition(
                schema,
                record.getValue(DB_SERIAL.NAME),
                type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    DB_CLASS.OWNER_NAME,
                    DB_CLASS.CLASS_NAME)
                .from(DB_CLASS)
                .where(DB_CLASS.OWNER_NAME.in(getInputSchemata()))
                .orderBy(
                    DB_CLASS.OWNER_NAME.asc(),
                    DB_CLASS.CLASS_NAME.asc())
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(DB_CLASS.OWNER_NAME));
            String name = record.getValue(DB_CLASS.CLASS_NAME);

            CUBRIDTableDefinition table = new CUBRIDTableDefinition(schema, name, null);
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
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    public Factory create() {
        return new CUBRIDFactory(getConnection());
    }
}
