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

package org.jooq.util.hana;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.util.hana.sys.Tables.CONSTRAINTS;
import static org.jooq.util.hana.sys.Tables.FUNCTIONS;
import static org.jooq.util.hana.sys.Tables.FUNCTION_PARAMETERS;
import static org.jooq.util.hana.sys.Tables.PROCEDURES;
import static org.jooq.util.hana.sys.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.hana.sys.Tables.SCHEMAS;
import static org.jooq.util.hana.sys.Tables.SEQUENCES;
import static org.jooq.util.hana.sys.Tables.TABLES;
import static org.jooq.util.hana.sys.Tables.VIEWS;

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
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.hana.sys.tables.Constraints;
import org.jooq.util.hana.sys.tables.FunctionParameters;
import org.jooq.util.hana.sys.tables.Functions;
import org.jooq.util.hana.sys.tables.Procedures;
import org.jooq.util.hana.sys.tables.ReferentialConstraints;

/**
 * The Hana database
 *
 * @author Lukas Eder
 */
public class HanaDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.HANA);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("TRUE")) {
            SchemaDefinition schema = getSchema(record.getValue(CONSTRAINTS.SCHEMA_NAME));
            String key = record.getValue(CONSTRAINTS.CONSTRAINT_NAME);
            String tableName = record.getValue(CONSTRAINTS.TABLE_NAME);
            String columnName = record.getValue(CONSTRAINTS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("FALSE")) {
            SchemaDefinition schema = getSchema(record.getValue(CONSTRAINTS.SCHEMA_NAME));
            String key = record.getValue(CONSTRAINTS.CONSTRAINT_NAME);
            String tableName = record.getValue(CONSTRAINTS.TABLE_NAME);
            String columnName = record.getValue(CONSTRAINTS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String isPrimaryKey) {
        Constraints c = CONSTRAINTS;

        return create()
            .select(
                c.SCHEMA_NAME,
                c.CONSTRAINT_NAME,
                c.TABLE_NAME,
                c.COLUMN_NAME)
            .from(c)
            .where(c.IS_PRIMARY_KEY.eq(isPrimaryKey))
            .and(c.IS_UNIQUE_KEY.eq("TRUE"))
            .and(c.SCHEMA_NAME.in(getInputSchemata()))
            .orderBy(
                c.SCHEMA_NAME.asc(),
                c.TABLE_NAME.asc(),
                c.CONSTRAINT_NAME.asc(),
                c.POSITION.asc())
            .fetch();
    }
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        ReferentialConstraints rc = REFERENTIAL_CONSTRAINTS;
        Constraints c = CONSTRAINTS;

        for (Record record : create()
            .select(
                rc.SCHEMA_NAME,
                rc.TABLE_NAME,
                rc.COLUMN_NAME,
                rc.CONSTRAINT_NAME,
                rc.REFERENCED_SCHEMA_NAME,
                rc.REFERENCED_CONSTRAINT_NAME
            )
            .from(rc)
            .join(c)
            .on(rc.REFERENCED_SCHEMA_NAME.eq(c.SCHEMA_NAME))
            .and(rc.REFERENCED_CONSTRAINT_NAME.eq(c.CONSTRAINT_NAME))
            .and(rc.REFERENCED_TABLE_NAME.eq(c.TABLE_NAME))
            .where(rc.SCHEMA_NAME.in(getInputSchemata()))) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(rc.SCHEMA_NAME));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(rc.REFERENCED_SCHEMA_NAME));

            String foreignKey = record.getValue(rc.CONSTRAINT_NAME);
            String foreignKeyTable = record.getValue(rc.TABLE_NAME);
            String foreignKeyColumn = record.getValue(rc.COLUMN_NAME);
            String uniqueKey = record.getValue(rc.REFERENCED_CONSTRAINT_NAME);

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

        for (String name : create().fetchValues(
                 select(SCHEMAS.SCHEMA_NAME)
                .from(SCHEMAS))) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    SEQUENCES.SCHEMA_NAME,
                    SEQUENCES.SEQUENCE_NAME)
                .from(SEQUENCES)
                .where(SEQUENCES.SCHEMA_NAME.in(getInputSchemata()))
                .orderBy(
                    SEQUENCES.SCHEMA_NAME,
                    SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(SEQUENCES.SCHEMA_NAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                "BIGINT"
            );

            result.add(new DefaultSequenceDefinition(
                schema, record.getValue(SEQUENCES.SEQUENCE_NAME), type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    TABLES.SCHEMA_NAME,
                    TABLES.TABLE_NAME)
                .from(TABLES)
                .where(TABLES.SCHEMA_NAME.in(getInputSchemata()))

                // CREATE TYPE .. AS TABLE generates such USER_DEFINED_TYPEs
                .and(TABLES.IS_USER_DEFINED_TYPE.ne(inline("TRUE")))
                .unionAll(
                 select(
                    VIEWS.SCHEMA_NAME,
                    VIEWS.VIEW_NAME)
                .from(VIEWS)
                .where(VIEWS.SCHEMA_NAME.in(getInputSchemata()))
                )
                .orderBy(1, 2)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(TABLES.SCHEMA_NAME));
            String name = record.getValue(TABLES.TABLE_NAME);
            String comment = "";

            result.add(new HanaTableDefinition(schema, name, comment));
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

        Procedures p = PROCEDURES;
        Functions f = FUNCTIONS;
        FunctionParameters fp = FUNCTION_PARAMETERS;

        for (Record record : create()
            .select(
                p.SCHEMA_NAME,
                p.PROCEDURE_NAME,
                p.PROCEDURE_OID,
                inline(null, String.class).as(fp.DATA_TYPE_NAME),
                inline(null, Integer.class).as(fp.LENGTH),
                inline(null, Integer.class).as(fp.SCALE))
            .from(p)
            .where(p.SCHEMA_NAME.in(getInputSchemata()))
            .unionAll(
                 select(
                    f.SCHEMA_NAME,
                    f.FUNCTION_NAME,
                    f.FUNCTION_OID,
                    fp.DATA_TYPE_NAME,
                    fp.LENGTH,
                    fp.SCALE)
                .from(f)
                .leftOuterJoin(fp)
                .on(f.FUNCTION_OID.eq(fp.FUNCTION_OID))
                .and(fp.PARAMETER_TYPE.eq(inline("RETURN")))
                .and(f.RETURN_VALUE_COUNT.eq(inline(1)))
                .where(f.SCHEMA_NAME.in(getInputSchemata()))
            )
            .orderBy(
                field(name(p.SCHEMA_NAME.getName())),
                field(name(p.PROCEDURE_NAME.getName()))
        )) {

            result.add(new HanaRoutineDefinition(
                getSchema(record.getValue(p.SCHEMA_NAME)),
                record.getValue(p.PROCEDURE_NAME),
                record.getValue(p.PROCEDURE_OID),
                record.getValue(fp.DATA_TYPE_NAME),
                record.getValue(fp.LENGTH),
                record.getValue(fp.SCALE)
            ));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
