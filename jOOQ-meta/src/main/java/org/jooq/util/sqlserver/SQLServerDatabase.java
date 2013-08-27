/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.util.sqlserver.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.sqlserver.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.sqlserver.information_schema.Tables.ROUTINES;
import static org.jooq.util.sqlserver.information_schema.Tables.SCHEMATA;
import static org.jooq.util.sqlserver.information_schema.Tables.SEQUENCES;
import static org.jooq.util.sqlserver.information_schema.Tables.TABLES;
import static org.jooq.util.sqlserver.information_schema.Tables.TABLE_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
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
import org.jooq.util.sqlserver.information_schema.tables.CheckConstraints;
import org.jooq.util.sqlserver.information_schema.tables.TableConstraints;

/**
 * The SQL Server database. This database uses generated artefacts from HSQLDB,
 * because HSQLDB's INFORMATION_SCHEMA is very similar to that of SQL Server,
 * whereas SQL Server's INFORMATION_SCHEMA is not self-contained, i.e. it cannot
 * be generated reading its own data.
 *
 * @author Lukas Eder
 */
public class SQLServerDatabase extends AbstractDatabase {

    private static Boolean is2012;

    private boolean is2012() {
        if (is2012 == null) {
            try {
                create().selectCount()
                        .from(SEQUENCES)
                        .fetch();

                is2012 = true;
            }
            catch (DataAccessException e) {
                is2012 = false;
            }
        }

        return is2012;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.SQLSERVER);
    }

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

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
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

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Result<?> result = create()
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

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        TableConstraints tc = TABLE_CONSTRAINTS.as("tc");
        CheckConstraints cc = CHECK_CONSTRAINTS.as("cc");

        for (Record record : create()
                .select(
                    tc.TABLE_SCHEMA,
                    tc.TABLE_NAME,
                    cc.CONSTRAINT_NAME,
                    cc.CHECK_CLAUSE
                 )
                .from(tc)
                .join(cc)
                .using(tc.CONSTRAINT_CATALOG, tc.CONSTRAINT_SCHEMA, tc.CONSTRAINT_NAME)
                .where(tc.TABLE_SCHEMA.in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(tc.TABLE_SCHEMA));
            TableDefinition table = getTable(schema, record.getValue(tc.TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.getValue(cc.CONSTRAINT_NAME),
                    record.getValue(cc.CHECK_CLAUSE)
                ));
            }
        }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(SCHEMATA.SCHEMA_NAME)
                .from(SCHEMATA)
                .fetch(SCHEMATA.SCHEMA_NAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        if (is2012()) {
            for (Record record : create()
                    .select(
                        SEQUENCES.SEQUENCE_SCHEMA,
                        SEQUENCES.SEQUENCE_NAME,
                        SEQUENCES.DATA_TYPE)
                    .from(SEQUENCES)
                    .where(SEQUENCES.SEQUENCE_SCHEMA.in(getInputSchemata()))
                    .fetch()) {

                SchemaDefinition schema = getSchema(record.getValue(SEQUENCES.SEQUENCE_SCHEMA));

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    record.getValue(SEQUENCES.DATA_TYPE)
                );

                result.add(new DefaultSequenceDefinition(
                    schema, record.getValue(SEQUENCES.SEQUENCE_NAME), type));
            }
        }

        return result;
    }

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

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
