/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util.vertica;

import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.util.vertica.v_catalog.Tables.ALL_TABLES;
import static org.jooq.util.vertica.v_catalog.Tables.CONSTRAINT_COLUMNS;
import static org.jooq.util.vertica.v_catalog.Tables.SCHEMATA;
import static org.jooq.util.vertica.v_catalog.Tables.SEQUENCES;

import java.math.BigInteger;
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
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.vertica.v_catalog.tables.ConstraintColumns;

/**
 * The Vertica database
 *
 * @author Lukas Eder
 */
public class VerticaDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.VERTICA);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("p")) {
            SchemaDefinition schema = getSchema(record.getValue(CONSTRAINT_COLUMNS.TABLE_SCHEMA));
            String key = record.getValue(CONSTRAINT_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(CONSTRAINT_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(CONSTRAINT_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("u")) {
            SchemaDefinition schema = getSchema(record.getValue(CONSTRAINT_COLUMNS.TABLE_SCHEMA));
            String key = record.getValue(CONSTRAINT_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(CONSTRAINT_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(CONSTRAINT_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create()
            .select(
                CONSTRAINT_COLUMNS.TABLE_SCHEMA,
                CONSTRAINT_COLUMNS.CONSTRAINT_NAME,
                CONSTRAINT_COLUMNS.TABLE_NAME,
                CONSTRAINT_COLUMNS.COLUMN_NAME)
            .from(CONSTRAINT_COLUMNS)
            .where(CONSTRAINT_COLUMNS.CONSTRAINT_TYPE.equal(constraintType))
            .and(CONSTRAINT_COLUMNS.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                CONSTRAINT_COLUMNS.TABLE_SCHEMA.asc(),
                CONSTRAINT_COLUMNS.TABLE_NAME.asc(),
                CONSTRAINT_COLUMNS.CONSTRAINT_NAME.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        ConstraintColumns fk = CONSTRAINT_COLUMNS.as("fk");
        ConstraintColumns pk = CONSTRAINT_COLUMNS.as("pk");

        Result<?> result = create()
            .select(
                pk.CONSTRAINT_NAME,
                pk.TABLE_SCHEMA,
                fk.CONSTRAINT_NAME,
                fk.TABLE_SCHEMA,
                fk.TABLE_NAME,
                fk.COLUMN_NAME)
            .from(fk)
            .join(pk)
                .on(fk.REFERENCE_TABLE_ID.eq(pk.TABLE_ID))
                .and(fk.REFERENCE_COLUMN_NAME.eq(pk.COLUMN_NAME))
            .where(fk.TABLE_SCHEMA.in(getInputSchemata()))
            .and(fk.CONSTRAINT_TYPE.eq("f"))
            .and(pk.CONSTRAINT_TYPE.eq("p"))
            .orderBy(
                fk.TABLE_SCHEMA.asc(),
                fk.TABLE_NAME.asc(),
                fk.CONSTRAINT_NAME.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.getValue(fk.TABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(pk.TABLE_SCHEMA));

            String foreignKey = record.getValue(fk.CONSTRAINT_NAME);
            String foreignKeyTable = record.getValue(fk.TABLE_NAME);
            String foreignKeyColumn = record.getValue(fk.COLUMN_NAME);
            String uniqueKey = record.getValue(pk.CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

            if (referencingTable != null) {
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
    }


    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(SCHEMATA.SCHEMA_NAME)
                .from(SCHEMATA)
                .orderBy(SCHEMATA.SCHEMA_NAME)
                .fetch(SCHEMATA.SCHEMA_NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME,
                    SEQUENCES.MAXIMUM)
                .from(SEQUENCES)
                .where(SEQUENCES.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(SEQUENCES.SEQUENCE_SCHEMA));

            BigInteger value = defaultIfNull(record.getValue(SEQUENCES.MAXIMUM, BigInteger.class), BigInteger.valueOf(Long.MAX_VALUE));
            DataTypeDefinition type = getDataTypeForMAX_VAL(getSchemata().get(0), value);

            result.add(new DefaultSequenceDefinition(
                schema, record.getValue(SEQUENCES.SEQUENCE_NAME), type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select()
                .from(ALL_TABLES)
                .where(ALL_TABLES.SCHEMA_NAME.in(getInputSchemata()))
                .orderBy(ALL_TABLES.SCHEMA_NAME, ALL_TABLES.TABLE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_TABLES.SCHEMA_NAME));
            String name = record.getValue(ALL_TABLES.TABLE_NAME);
            String comment = "";

            result.add(new VerticaTableDefinition(schema, name, comment));
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<DomainDefinition>();
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
}
