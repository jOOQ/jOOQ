/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

package org.jooq.util.postgres;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.postgres.PostgresDSL.oid;
import static org.jooq.util.postgres.information_schema.Tables.ATTRIBUTES;
import static org.jooq.util.postgres.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.util.postgres.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;
import static org.jooq.util.postgres.information_schema.Tables.SEQUENCES;
import static org.jooq.util.postgres.information_schema.Tables.TABLES;
import static org.jooq.util.postgres.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_CLASS;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_DESCRIPTION;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_ENUM;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_INHERITS;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_PROC;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_TYPE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.hsqldb.HSQLDBDatabase;
import org.jooq.util.postgres.information_schema.tables.CheckConstraints;
import org.jooq.util.postgres.information_schema.tables.Routines;
import org.jooq.util.postgres.information_schema.tables.TableConstraints;
import org.jooq.util.postgres.pg_catalog.tables.PgClass;
import org.jooq.util.postgres.pg_catalog.tables.PgInherits;
import org.jooq.util.postgres.pg_catalog.tables.PgNamespace;

/**
 * Postgres uses the ANSI default INFORMATION_SCHEMA, but unfortunately ships
 * with a non-capitalised version of it: <code>information_schema</code>. Hence
 * the {@link HSQLDBDatabase} is not used here.
 *
 * @author Lukas Eder
 */
public class PostgresDatabase extends AbstractDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(PostgresDatabase.class);

    private static Boolean is84;
    private static Boolean canCastToEnumType;

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
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(TABLE_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA.equal(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA))
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

        // [#3520] PostgreSQL INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS contains incomplete information about foreign keys
        // The (CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME) tuple is non-unique, in case two tables share the
        // same CONSTRAINT_NAME.
        // The JDBC driver implements this correctly through the pg_catalog, although the sorting and column name casing is wrong, too.
        Result<Record> result = create()
            .fetch(getConnection().getMetaData().getExportedKeys(null, null, null))
            .sortAsc("key_seq")
            .sortAsc("fk_name")
            .sortAsc("fktable_name")
            .sortAsc("fktable_schem");

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.getValue("fktable_schem", String.class));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue("pktable_schem", String.class));

            String foreignKey = record.getValue("fk_name", String.class);
            String foreignKeyTable = record.getValue("fktable_name", String.class);
            String foreignKeyColumn = record.getValue("fkcolumn_name", String.class);
            String uniqueKey = record.getValue("pk_name", String.class);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

            if (referencingTable != null) {

                // [#986] Add the table name as a namespace prefix to the key
                // name. In Postgres, foreign key names are only unique per table
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKeyTable + "__" + foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
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
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();
        Map<Name, PostgresTableDefinition> map = new HashMap<Name, PostgresTableDefinition>();

        for (Record record : create()
                .select()
                .from(
                     select(
                        TABLES.TABLE_SCHEMA,
                        TABLES.TABLE_NAME,
                        TABLES.TABLE_NAME.as("specific_name"),
                        inline(false).as("table_valued_function"),
                        PG_DESCRIPTION.DESCRIPTION)
                    .from(TABLES)
                    .join(PG_NAMESPACE)
                        .on(TABLES.TABLE_SCHEMA.eq(PG_NAMESPACE.NSPNAME))
                    .join(PG_CLASS)
                        .on(PG_CLASS.RELNAME.eq(TABLES.TABLE_NAME))
                        .and(PG_CLASS.RELNAMESPACE.eq(oid(PG_NAMESPACE)))
                    .leftOuterJoin(PG_DESCRIPTION)
                        .on(PG_DESCRIPTION.OBJOID.eq(oid(PG_CLASS)))
                        .and(PG_DESCRIPTION.OBJSUBID.eq(0))
                    .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))

                // [#3375] [#3376] Include table-valued functions in the set of tables
                .unionAll(
                    select(
                        ROUTINES.ROUTINE_SCHEMA,
                        ROUTINES.ROUTINE_NAME,
                        ROUTINES.SPECIFIC_NAME,
                        inline(true).as("table_valued_function"),
                        inline(""))
                    .from(ROUTINES)
                    .join(PG_NAMESPACE).on(ROUTINES.SPECIFIC_SCHEMA.eq(PG_NAMESPACE.NSPNAME))
                    .join(PG_PROC).on(PG_PROC.PRONAMESPACE.eq(oid(PG_NAMESPACE)))
                                  .and(PG_PROC.PRONAME.concat("_").concat(oid(PG_PROC)).eq(ROUTINES.SPECIFIC_NAME))
                    .where(ROUTINES.ROUTINE_SCHEMA.in(getInputSchemata()))
                    .and(PG_PROC.PRORETSET))
                .asTable("tables"))
                .orderBy(1, 2)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(TABLES.TABLE_SCHEMA));
            String name = record.getValue(TABLES.TABLE_NAME);
            boolean tableValuedFunction = record.getValue("table_valued_function", boolean.class);
            String comment = record.getValue(PG_DESCRIPTION.DESCRIPTION, String.class);

            if (tableValuedFunction) {
                result.add(new PostgresTableValuedFunction(schema, name, record.getValue(ROUTINES.SPECIFIC_NAME), comment));
            }
            else {
                PostgresTableDefinition t = new PostgresTableDefinition(schema, name, comment);
                result.add(t);
                map.put(name(schema.getName(), name), t);
            }
        }

        PgClass ct = PG_CLASS.as("ct");
        PgNamespace cn = PG_NAMESPACE.as("cn");
        PgInherits i = PG_INHERITS.as("i");
        PgClass pt = PG_CLASS.as("pt");
        PgNamespace pn = PG_NAMESPACE.as("pn");

        // [#2916] If window functions are not supported (prior to PostgreSQL 8.4), then
        // don't execute the following query:
        if (is84()) {
            for (Record5<String, String, String, String, Integer> inheritance : create()
                    .select(
                        cn.NSPNAME,
                        ct.RELNAME,
                        pn.NSPNAME,
                        pt.RELNAME,
                        max(i.INHSEQNO).over().partitionBy(i.INHRELID).as("m")
                    )
                    .from(ct)
                    .join(cn).on(ct.RELNAMESPACE.eq(oid(cn)))
                    .join(i).on(i.INHRELID.eq(oid(ct)))
                    .join(pt).on(i.INHPARENT.eq(oid(pt)))
                    .join(pn).on(pt.RELNAMESPACE.eq(oid(pn)))
                    .fetch()) {

                Name child = name(inheritance.value1(), inheritance.value2());
                Name parent = name(inheritance.value3(), inheritance.value4());

                if (inheritance.value5() > 1) {
                    log.info("Multiple inheritance",
                        "Multiple inheritance is not supported by jOOQ: " +
                        child +
                        " inherits from " +
                        parent);
                }
                else {
                    PostgresTableDefinition childTable = map.get(child);
                    PostgresTableDefinition parentTable = map.get(parent);

                    if (childTable != null && parentTable != null) {
                        childTable.setParentTable(parentTable);
                        parentTable.getChildTables().add(childTable);
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        // [#1409] Shouldn't select from INFORMATION_SCHEMA.SCHEMATA, as that
        // would only return schemata of which CURRENT_USER is the owner
        for (String name : create()
                .select(PG_NAMESPACE.NSPNAME)
                .from(PG_NAMESPACE)
                .fetch(PG_NAMESPACE.NSPNAME)) {

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
                    SEQUENCES.DATA_TYPE,
                    SEQUENCES.NUMERIC_PRECISION,
                    SEQUENCES.NUMERIC_SCALE)
                .from(SEQUENCES)
                .where(SEQUENCES.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(SEQUENCES.SEQUENCE_SCHEMA));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema,
                record.getValue(SEQUENCES.DATA_TYPE),
                0,
                record.getValue(SEQUENCES.NUMERIC_PRECISION),
                record.getValue(SEQUENCES.NUMERIC_SCALE),
                false,
                false
            );

            result.add(new DefaultSequenceDefinition(schema, record.getValue(SEQUENCES.SEQUENCE_NAME), type));
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        // [#2736] This table is unavailable in Amazon Redshift
        if (exists(PG_ENUM)) {

            // [#2707] Fetch all enum type names first, in order to be able to
            // perform enumlabel::[typname] casts in the subsequent query for
            // cross-version compatible enum literal ordering
            Result<Record2<String, String>> types = create()
                .select(
                    PG_NAMESPACE.NSPNAME,
                    PG_TYPE.TYPNAME)
                .from(PG_TYPE)
                .join(PG_NAMESPACE).on(PG_TYPE.TYPNAMESPACE.eq(oid(PG_NAMESPACE)))
                .where(PG_NAMESPACE.NSPNAME.in(getInputSchemata()))
                .and(oid(PG_TYPE).in(select(PG_ENUM.ENUMTYPID).from(PG_ENUM)))
                .orderBy(
                    PG_NAMESPACE.NSPNAME,
                    PG_TYPE.TYPNAME)
                .fetch();

            for (Record2<String, String> type : types) {
                String nspname = type.getValue(PG_NAMESPACE.NSPNAME);
                String typname = type.getValue(PG_TYPE.TYPNAME);

                DefaultEnumDefinition definition = null;
                for (String label : enumLabels(nspname, typname)) {
                    SchemaDefinition schema = getSchema(nspname);
                    String typeName = String.valueOf(typname);

                    if (definition == null || !definition.getName().equals(typeName)) {
                        definition = new DefaultEnumDefinition(schema, typeName, null);
                        result.add(definition);
                    }

                    definition.addLiteral(label);
                }
            }
        }

        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        // [#2736] This table is unavailable in Amazon Redshift
        if (exists(ATTRIBUTES)) {
            for (Record record : create()
                    .selectDistinct(
                        ATTRIBUTES.UDT_SCHEMA,
                        ATTRIBUTES.UDT_NAME)
                    .from(ATTRIBUTES)
                    .where(ATTRIBUTES.UDT_SCHEMA.in(getInputSchemata()))
                    .orderBy(
                        ATTRIBUTES.UDT_SCHEMA,
                        ATTRIBUTES.UDT_NAME)
                    .fetch()) {

                SchemaDefinition schema = getSchema(record.getValue(ATTRIBUTES.UDT_SCHEMA));
                String name = record.getValue(ATTRIBUTES.UDT_NAME);

                result.add(new PostgresUDTDefinition(schema, name, null));
            }
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

        Routines r1 = ROUTINES.as("r1");
        Routines r2 = ROUTINES.as("r2");

        for (Record record : create().select(
                r1.ROUTINE_SCHEMA,
                r1.ROUTINE_NAME,
                r1.SPECIFIC_NAME,

                // Ignore the data type when there is at least one out parameter
                decode()
                    .when(DSL.exists(
                        selectOne()
                        .from(PARAMETERS)
                        .where(PARAMETERS.SPECIFIC_SCHEMA.eq(r1.SPECIFIC_SCHEMA))
                        .and(PARAMETERS.SPECIFIC_NAME.eq(r1.SPECIFIC_NAME))
                        .and(upper(PARAMETERS.PARAMETER_MODE).ne("IN"))),
                            val("void"))
                    .otherwise(r1.DATA_TYPE).as("data_type"),
                r1.CHARACTER_MAXIMUM_LENGTH,
                r1.NUMERIC_PRECISION,
                r1.NUMERIC_SCALE,
                r1.TYPE_UDT_NAME,

                // Calculate overload index if applicable
                decode().when(
                DSL.exists(
                    selectOne()
                    .from(r2)
                    .where(r2.ROUTINE_SCHEMA.in(getInputSchemata()))
                    .and(r2.ROUTINE_SCHEMA.eq(r1.ROUTINE_SCHEMA))
                    .and(r2.ROUTINE_NAME.eq(r1.ROUTINE_NAME))
                    .and(r2.SPECIFIC_NAME.ne(r1.SPECIFIC_NAME))),
                    select(count())
                    .from(r2)
                    .where(r2.ROUTINE_SCHEMA.in(getInputSchemata()))
                    .and(r2.ROUTINE_SCHEMA.eq(r1.ROUTINE_SCHEMA))
                    .and(r2.ROUTINE_NAME.eq(r1.ROUTINE_NAME))
                    .and(r2.SPECIFIC_NAME.le(r1.SPECIFIC_NAME)).asField())
                .as("overload"),
                PG_PROC.PROISAGG)
            .from(r1)

            // [#3375] Exclude table-valued functions as they're already generated as tables
            .join(PG_NAMESPACE).on(PG_NAMESPACE.NSPNAME.eq(r1.SPECIFIC_SCHEMA))
            .join(PG_PROC).on(PG_PROC.PRONAMESPACE.eq(oid(PG_NAMESPACE)))
                          .and(PG_PROC.PRONAME.concat("_").concat(oid(PG_PROC)).eq(r1.SPECIFIC_NAME))
            .where(r1.ROUTINE_SCHEMA.in(getInputSchemata()))
            .andNot(PG_PROC.PRORETSET)
            .orderBy(
                r1.ROUTINE_SCHEMA.asc(),
                r1.ROUTINE_NAME.asc())
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
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.POSTGRES);
    }

    private boolean is84() {
        if (is84 == null) {

            // [#2916] Window functions were introduced with PostgreSQL 9.0
            try {
                create().select(count().over()).fetch();

                is84 = true;
            }
            catch (DataAccessException e) {
                is84 = false;
            }
        }

        return is84;
    }

    private List<String> enumLabels(String nspname, String typname) {
        Field<Object> cast = field("{0}::{1}", PG_ENUM.ENUMLABEL, name(nspname, typname));

        if (canCastToEnumType == null) {

            // [#2917] Older versions of PostgreSQL don't support the above cast
            try {
                canCastToEnumType = true;
                return enumLabels(nspname, typname, cast);
            }
            catch (DataAccessException e) {
                canCastToEnumType = false;
            }
        }

        return canCastToEnumType ? enumLabels(nspname, typname, cast) : enumLabels(nspname, typname, PG_ENUM.ENUMLABEL);
    }

    private List<String> enumLabels(String nspname, String typname, Field<?> orderBy) {
        return
        create().select(PG_ENUM.ENUMLABEL)
                .from(PG_ENUM)
                .join(PG_TYPE).on(PG_ENUM.ENUMTYPID.eq(oid(PG_TYPE)))
                .join(PG_NAMESPACE).on(PG_TYPE.TYPNAMESPACE.eq(oid(PG_NAMESPACE)))
                .where(PG_NAMESPACE.NSPNAME.eq(nspname))
                .and(PG_TYPE.TYPNAME.eq(typname))
                .orderBy(orderBy)
                .fetch(PG_ENUM.ENUMLABEL);
    }
}
