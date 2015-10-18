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
package org.jooq.util.db2;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.db2.syscat.Tables.DATATYPES;
import static org.jooq.util.db2.syscat.Tables.FUNCTIONS;
import static org.jooq.util.db2.syscat.Tables.KEYCOLUSE;
import static org.jooq.util.db2.syscat.Tables.REFERENCES;
import static org.jooq.util.db2.syscat.Tables.SCHEMATA;
import static org.jooq.util.db2.syscat.Tables.SEQUENCES;
import static org.jooq.util.db2.syscat.Tables.TABCONST;
import static org.jooq.util.db2.syscat.Tables.TABLES;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCNAME;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCSCHEMA;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCEDURES;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCNAME;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCSCHEMA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
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
import org.jooq.util.db2.syscat.tables.Datatypes;
import org.jooq.util.db2.syscat.tables.Keycoluse;
import org.jooq.util.db2.syscat.tables.References;
import org.jooq.util.db2.syscat.tables.Schemata;
import org.jooq.util.db2.syscat.tables.Sequences;
import org.jooq.util.db2.syscat.tables.Tabconst;
import org.jooq.util.db2.syscat.tables.Tables;

/**
 * DB2 implementation of {@link AbstractDatabase}
 *
 * @author Espen Stromsnes
 */
public class DB2Database extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.DB2);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.getValue(Keycoluse.TABSCHEMA.trim()));
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(Keycoluse.TABNAME);
            String columnName = record.getValue(Keycoluse.COLNAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(Keycoluse.TABSCHEMA.trim()));
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(Keycoluse.TABNAME);
            String columnName = record.getValue(Keycoluse.COLNAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                concat(Keycoluse.TABNAME, val("__"), Keycoluse.CONSTNAME).as("constraint_name"),
                Keycoluse.TABSCHEMA.trim(),
                Keycoluse.TABNAME,
                Keycoluse.COLNAME)
            .from(KEYCOLUSE)
            .join(TABCONST)
            .on(Keycoluse.TABSCHEMA.equal(Tabconst.TABSCHEMA))
            .and(Keycoluse.TABNAME.equal(Tabconst.TABNAME))
            .and(Keycoluse.CONSTNAME.equal(Tabconst.CONSTNAME))
            .where(Keycoluse.TABSCHEMA.in(getInputSchemata()))
            .and(Tabconst.TYPE.equal(constraintType))
            .orderBy(
                Keycoluse.TABSCHEMA.asc(),
                Keycoluse.TABNAME.asc(),
                Keycoluse.CONSTNAME.asc(),
                Keycoluse.COLSEQ.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    concat(References.TABNAME, val("__"), References.CONSTNAME).as("constraint_name"),
                    References.TABSCHEMA.trim(),
                    References.TABNAME,
                    References.FK_COLNAMES,
                    concat(References.REFTABNAME, val("__"), References.REFKEYNAME).as("referenced_constraint_name"),
                    References.REFTABSCHEMA.trim())
                .from(REFERENCES)
                .where(References.TABSCHEMA.in(getInputSchemata()))
                .orderBy(
                    References.TABSCHEMA,
                    References.TABNAME,
                    References.CONSTNAME,
                    References.FK_COLNAMES)
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(References.TABSCHEMA.trim()));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(References.REFTABSCHEMA.trim()));

            String foreignKey = record.getValue("constraint_name", String.class);
            String foreignKeyTableName = record.getValue(References.TABNAME);
            String foreignKeyColumn = record.getValue(References.FK_COLNAMES);
            String uniqueKey = record.getValue("referenced_constraint_name", String.class);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);

            if (foreignKeyTable != null) {
                /*
                 * If a foreign key consists of several columns, all the columns
                 * are contained in a single database column (delimited with
                 * space) here we split the combined string into individual
                 * columns
                 */
                String[] referencingColumnNames = foreignKeyColumn.trim().split("[ ]+");
                for (int i = 0; i < referencingColumnNames.length; i++) {
                    ColumnDefinition column = foreignKeyTable.getColumn(referencingColumnNames[i]);

                    relations.addForeignKey(foreignKey, uniqueKey, column, uniqueKeySchema);
                }
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .select(Schemata.SCHEMANAME.trim())
                .from(SCHEMATA)
                .fetch(Schemata.SCHEMANAME.trim())) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    Sequences.SEQSCHEMA.trim(),
                    Sequences.SEQNAME,
                    Sequences.SEQTYPE,
                    Datatypes.TYPENAME,
                    Sequences.PRECISION)
                .from(SEQUENCES)
                .join(DATATYPES)
                .on(Sequences.DATATYPEID.equal(Datatypes.TYPEID.cast(Integer.class)))
                .where(Sequences.SEQSCHEMA.in(getInputSchemata()))
                .orderBy(
                    Sequences.SEQSCHEMA,
                    Sequences.SEQNAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Sequences.SEQSCHEMA.trim()));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema,
                record.getValue(Datatypes.TYPENAME),
                0,
                record.getValue(Sequences.PRECISION),
                0,
                null,
                null
            );

            result.add(new DefaultSequenceDefinition(
                schema,
                record.getValue(Sequences.SEQNAME),
                type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        SelectQuery<?> q = create().selectQuery();
        q.addFrom(TABLES);
        q.addSelect(Tables.TABSCHEMA.trim());
        q.addSelect(Tables.TABNAME);
        q.addConditions(Tables.TABSCHEMA.in(getInputSchemata()));
        q.addOrderBy(Tables.TABNAME);
        q.execute();

        for (Record record : q.getResult()) {
            SchemaDefinition schema = getSchema(record.getValue(Tables.TABSCHEMA.trim()));
            String name = record.getValue(Tables.TABNAME);
            String comment = "";

            DB2TableDefinition table = new DB2TableDefinition(schema, name, comment);
            result.add(table);

        }
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .select().from(create()
                    .select(
                        PROCSCHEMA.trim().as("schema"),
                        PROCNAME.as("name"),
                        val(true).as("isProcedure"))
                    .from(PROCEDURES)
                    .where(PROCSCHEMA.in(getInputSchemata()))
                .unionAll(create()
                    .select(
                        FUNCSCHEMA.trim().as("schema"),
                        FUNCNAME.as("name"),
                        val(false).as("isProcedure"))
                    .from(FUNCTIONS)
                    .where(FUNCSCHEMA.in(getInputSchemata()))))
                .orderBy(two())
                .fetch()) {

            result.add(new DB2RoutineDefinition(
                getSchema(record.getValue("schema", String.class)),
                record.getValue("name", String.class),
                null,
                record.getValue("isProcedure", boolean.class)));
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

        for (Record record : create().selectDistinct(
                    Datatypes.TYPESCHEMA.trim(),
                    Datatypes.TYPENAME)
                .from(DATATYPES)
                .where(Datatypes.TYPESCHEMA.in(getInputSchemata()))
                .orderBy(Datatypes.TYPENAME)
                .fetch()) {

            result.add(new DB2UDTDefinition(
                getSchema(record.getValue(Datatypes.TYPESCHEMA.trim())),
                record.getValue(Datatypes.TYPENAME),
                null));
        }

        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }
}
