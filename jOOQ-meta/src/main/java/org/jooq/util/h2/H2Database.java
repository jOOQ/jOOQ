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
package org.jooq.util.h2;

import static org.jooq.impl.DSL.select;
import static org.jooq.util.h2.information_schema.tables.Columns.COLUMNS;
import static org.jooq.util.h2.information_schema.tables.Constraints.CONSTRAINTS;
import static org.jooq.util.h2.information_schema.tables.CrossReferences.CROSS_REFERENCES;
import static org.jooq.util.h2.information_schema.tables.FunctionAliases.FUNCTION_ALIASES;
import static org.jooq.util.h2.information_schema.tables.Indexes.INDEXES;
import static org.jooq.util.h2.information_schema.tables.Schemata.SCHEMATA;
import static org.jooq.util.h2.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.util.h2.information_schema.tables.Tables.TABLES;
import static org.jooq.util.h2.information_schema.tables.TypeInfo.TYPE_INFO;

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
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
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
import org.jooq.util.h2.information_schema.tables.Columns;
import org.jooq.util.h2.information_schema.tables.Constraints;
import org.jooq.util.h2.information_schema.tables.CrossReferences;
import org.jooq.util.h2.information_schema.tables.FunctionAliases;
import org.jooq.util.h2.information_schema.tables.Indexes;
import org.jooq.util.h2.information_schema.tables.Schemata;
import org.jooq.util.h2.information_schema.tables.Sequences;
import org.jooq.util.h2.information_schema.tables.Tables;
import org.jooq.util.h2.information_schema.tables.TypeInfo;

/**
 * H2 implementation of {@link AbstractDatabase}
 *
 * @author Espen Stromsnes
 */
public class H2Database extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.H2);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                String tableName = record.get(Constraints.TABLE_NAME);
                String primaryKey = record.get(Constraints.CONSTRAINT_NAME);
                String columnName = record.get(Indexes.COLUMN_NAME);

                TableDefinition table = getTable(schema, tableName);
                if (table != null) {
                    relations.addPrimaryKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                String tableName = record.get(Constraints.TABLE_NAME);
                String primaryKey = record.get(Constraints.CONSTRAINT_NAME);
                String columnName = record.get(Indexes.COLUMN_NAME);

                TableDefinition table = getTable(schema, tableName);
                if (table != null) {
                    relations.addUniqueKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                    Constraints.TABLE_SCHEMA,
                    Constraints.TABLE_NAME,
                    Constraints.CONSTRAINT_NAME,
                    Indexes.COLUMN_NAME)
                .from(CONSTRAINTS)
                .join(INDEXES)
                .on(Constraints.TABLE_SCHEMA.eq(Indexes.TABLE_SCHEMA))
                .and(Constraints.TABLE_NAME.eq(Indexes.TABLE_NAME))
                .and(Constraints.UNIQUE_INDEX_NAME.eq(Indexes.INDEX_NAME))
                .where(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
                .and(Constraints.CONSTRAINT_TYPE.equal(constraintType))
                .orderBy(
                    Constraints.TABLE_SCHEMA,
                    Constraints.CONSTRAINT_NAME,
                    Indexes.ORDINAL_POSITION)
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    CrossReferences.FK_NAME,
                    CrossReferences.FKTABLE_NAME,
                    CrossReferences.FKTABLE_SCHEMA,
                    CrossReferences.FKCOLUMN_NAME,
                    Constraints.CONSTRAINT_NAME,
                    Constraints.CONSTRAINT_SCHEMA)
                .from(CROSS_REFERENCES)
                .join(CONSTRAINTS)
                .on(CrossReferences.PK_NAME.equal(Constraints.UNIQUE_INDEX_NAME))
                .and(CrossReferences.PKTABLE_NAME.equal(Constraints.TABLE_NAME))
                .and(CrossReferences.PKTABLE_SCHEMA.equal(Constraints.TABLE_SCHEMA))
                .where(CrossReferences.FKTABLE_SCHEMA.in(getInputSchemata()))
                .and(Constraints.CONSTRAINT_TYPE.in("PRIMARY KEY", "UNIQUE"))
                .orderBy(
                    CrossReferences.FKTABLE_SCHEMA.asc(),
                    CrossReferences.FK_NAME.asc(),
                    CrossReferences.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.get(CrossReferences.FKTABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.get(Constraints.CONSTRAINT_SCHEMA));

            if (foreignKeySchema != null && uniqueKeySchema != null) {
                String foreignKeyTableName = record.get(CrossReferences.FKTABLE_NAME);
                String foreignKeyColumn = record.get(CrossReferences.FKCOLUMN_NAME);
                String foreignKey = record.get(CrossReferences.FK_NAME);
                String uniqueKey = record.get(Constraints.CONSTRAINT_NAME);

                TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
                if (foreignKeyTable != null) {
                    ColumnDefinition referencingColumn = foreignKeyTable.getColumn(foreignKeyColumn);

                    relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
                }
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {

        // TODO: Should we really UNION INFORMATION_SCHEMA.COLUMNS.CHECK_CONSTRAINT?
        for (Record record : create()
            .select(
                Constraints.TABLE_SCHEMA,
                Constraints.TABLE_NAME,
                Constraints.CONSTRAINT_NAME,
                Constraints.CHECK_EXPRESSION
             )
            .from(CONSTRAINTS)
            .where(Constraints.CONSTRAINT_TYPE.eq("CHECK"))
            .and(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
            .union(
            select(
                Columns.TABLE_SCHEMA,
                Columns.TABLE_NAME,
                Columns.CHECK_CONSTRAINT,
                Columns.CHECK_CONSTRAINT
            )
            .from(COLUMNS)
            .where(Columns.CHECK_CONSTRAINT.nvl("").ne(""))
            .and(Columns.TABLE_SCHEMA.in(getInputSchemata())))
            .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Constraints.TABLE_SCHEMA));

            if (schema != null) {
                TableDefinition table = getTable(schema, record.get(Constraints.TABLE_NAME));

                if (table != null) {
                    relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                        schema,
                        table,
                        record.get(Constraints.CONSTRAINT_NAME),
                        record.get(Constraints.CHECK_EXPRESSION)
                    ));
                }
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

        for (Record record : create().select(
                    Schemata.SCHEMA_NAME,
                    Schemata.REMARKS)
                .from(SCHEMATA)
                .fetch()) {

            result.add(new SchemaDefinition(this,
                record.get(Schemata.SCHEMA_NAME),
                record.get(Schemata.REMARKS)));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME)
                .from(SEQUENCES)
                .where(Sequences.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Sequences.SEQUENCE_SCHEMA));

            if (schema != null) {
                String name = record.get(Sequences.SEQUENCE_NAME);

                DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    H2DataType.BIGINT.getTypeName()
                );

                result.add(new DefaultSequenceDefinition(schema, name, type));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                    Tables.TABLE_SCHEMA,
                    Tables.TABLE_NAME,
                    Tables.REMARKS)
                .from(TABLES)
                .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Tables.TABLE_SCHEMA,
                    Tables.ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.get(Tables.TABLE_SCHEMA));

            if (schema != null) {
                String name = record.get(Tables.TABLE_NAME);
                String comment = record.get(Tables.REMARKS);

                H2TableDefinition table = new H2TableDefinition(schema, name, comment);
                result.add(table);
            }
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .select(
                    FunctionAliases.ALIAS_SCHEMA,
                    FunctionAliases.ALIAS_NAME,
                    FunctionAliases.REMARKS,
                    FunctionAliases.DATA_TYPE,
                    FunctionAliases.RETURNS_RESULT,
                    TypeInfo.TYPE_NAME,
                    TypeInfo.PRECISION,
                    TypeInfo.MAXIMUM_SCALE)
                .from(FUNCTION_ALIASES)
                .leftOuterJoin(TYPE_INFO)
                .on(FunctionAliases.DATA_TYPE.equal(TypeInfo.DATA_TYPE))
                // [#1256] TODO implement this correctly. Not sure if POS = 0
                // is the right predicate to rule out duplicate entries in TYPE_INFO
                .and(TypeInfo.POS.equal(0))
                .where(FunctionAliases.ALIAS_SCHEMA.in(getInputSchemata()))
                .and(FunctionAliases.RETURNS_RESULT.in((short) 1, (short) 2))
                .orderBy(FunctionAliases.ALIAS_NAME).fetch()) {

            SchemaDefinition schema = getSchema(record.get(FunctionAliases.ALIAS_SCHEMA));

            if (schema != null) {
                String name = record.get(FunctionAliases.ALIAS_NAME);
                String comment = record.get(FunctionAliases.REMARKS);
                String typeName = record.get(TypeInfo.TYPE_NAME);
                Integer precision = record.get(TypeInfo.PRECISION);
                Short scale = record.get(TypeInfo.MAXIMUM_SCALE);

                result.add(new H2RoutineDefinition(schema, name, comment, typeName, precision, scale));
            }
        }

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
}
