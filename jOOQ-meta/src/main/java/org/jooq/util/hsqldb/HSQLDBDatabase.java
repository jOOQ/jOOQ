/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.util.hsqldb;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.util.hsqldb.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.util.hsqldb.information_schema.Tables.ELEMENT_TYPES;
import static org.jooq.util.hsqldb.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.hsqldb.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.hsqldb.information_schema.Tables.ROUTINES;
import static org.jooq.util.hsqldb.information_schema.Tables.SCHEMATA;
import static org.jooq.util.hsqldb.information_schema.Tables.SEQUENCES;
import static org.jooq.util.hsqldb.information_schema.Tables.TABLES;
import static org.jooq.util.hsqldb.information_schema.Tables.TABLE_CONSTRAINTS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
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
import org.jooq.util.hsqldb.information_schema.tables.CheckConstraints;
import org.jooq.util.hsqldb.information_schema.tables.TableConstraints;

/**
 * The HSQLDB database
 *
 * @author Lukas Eder
 */
public class HSQLDBDatabase extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.HSQLDB);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.get(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.get(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.get(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.get(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.get(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.get(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);

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
            .from(TABLE_CONSTRAINTS
                .join(KEY_COLUMN_USAGE)
                .on(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA.equal(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA))
                .and(TABLE_CONSTRAINTS.CONSTRAINT_NAME.equal(KEY_COLUMN_USAGE.CONSTRAINT_NAME)))
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
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME,
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA,
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(REFERENTIAL_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA))
            .and(KEY_COLUMN_USAGE.CONSTRAINT_NAME.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME))
            .where(KEY_COLUMN_USAGE.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.get(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.get(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String foreignKeyTable = record.get(KEY_COLUMN_USAGE.TABLE_NAME);
            String foreignKeyColumn = record.get(KEY_COLUMN_USAGE.COLUMN_NAME);
            String uniqueKey = record.get(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME);

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

        // [#2808] [#3019] Workaround for bad handling of JOIN .. USING
        Field<String> constraintName = field(name(cc.CONSTRAINT_NAME.getName()), String.class);

        for (Record record : create()
                .select(
                    tc.TABLE_SCHEMA,
                    tc.TABLE_NAME,
                    constraintName,
                    cc.CHECK_CLAUSE
                 )
                .from(tc)
                .join(cc)
                .using(tc.CONSTRAINT_CATALOG, tc.CONSTRAINT_SCHEMA, tc.CONSTRAINT_NAME)
                .where(tc.TABLE_SCHEMA.in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(tc.TABLE_SCHEMA));
            TableDefinition table = getTable(schema, record.get(tc.TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(constraintName),
                    record.get(cc.CHECK_CLAUSE)
                ));
            }
        }
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<CatalogDefinition>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
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

        for (Record record : create()
                .select(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME,
                    SEQUENCES.DATA_TYPE)
                .from(SEQUENCES)
                .where(SEQUENCES.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(SEQUENCES.SEQUENCE_SCHEMA));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get(SEQUENCES.DATA_TYPE)
            );

            result.add(new DefaultSequenceDefinition(
                schema, record.get(SEQUENCES.SEQUENCE_NAME), type));
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

            SchemaDefinition schema = getSchema(record.get(TABLES.TABLE_SCHEMA));
            String name = record.get(TABLES.TABLE_NAME);
            String comment = "";

            result.add(new HSQLDBTableDefinition(schema, name, comment));
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

        for (Record record : create()
                .select(
                    ROUTINES.ROUTINE_SCHEMA,
                    ROUTINES.ROUTINE_NAME,
                    ROUTINES.SPECIFIC_NAME,
                    nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER, ROUTINES.DATA_TYPE).as("datatype"),
                    ROUTINES.NUMERIC_PRECISION,
                    ROUTINES.NUMERIC_SCALE,
                    field(ROUTINES.ROUTINE_DEFINITION.likeRegex(".*(?i:(\\w+\\s+)+aggregate\\s+function).*")).as("aggregate"))
                .from(ROUTINES)
                .leftOuterJoin(ELEMENT_TYPES)
                .on(ROUTINES.ROUTINE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
                .and(ROUTINES.ROUTINE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
                .and(ROUTINES.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
                .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    ROUTINES.ROUTINE_SCHEMA,
                    ROUTINES.ROUTINE_NAME)
                .fetch()) {

            String datatype = record.get("datatype", String.class);

            // [#3285] We currently do not recognise HSQLDB table-valued functions as such.
            if (datatype != null && datatype.toUpperCase().startsWith("ROW")) {
                JooqLogger.getLogger(getClass()).info("A row : " +datatype);
                datatype = "ROW";
            }

            result.add(new HSQLDBRoutineDefinition(
                getSchema(record.get(ROUTINES.ROUTINE_SCHEMA)),
                record.get(ROUTINES.ROUTINE_NAME),
                record.get(ROUTINES.SPECIFIC_NAME),
                datatype,
                record.get(ROUTINES.NUMERIC_PRECISION),
                record.get(ROUTINES.NUMERIC_SCALE),
                record.get("aggregate", boolean.class)));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }
}
