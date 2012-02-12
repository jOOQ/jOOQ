/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.db2;

import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.two;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.db2.syscat.tables.Datatypes.DATATYPES;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCNAME;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCSCHEMA;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCTIONS;
import static org.jooq.util.db2.syscat.tables.Keycoluse.KEYCOLUSE;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCEDURES;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCNAME;
import static org.jooq.util.db2.syscat.tables.Procedures.PROCSCHEMA;
import static org.jooq.util.db2.syscat.tables.References.REFERENCES;
import static org.jooq.util.db2.syscat.tables.Sequences.SEQUENCES;
import static org.jooq.util.db2.syscat.tables.Tabconst.TABCONST;
import static org.jooq.util.db2.syscat.tables.Tables.TABLES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
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
import org.jooq.util.db2.syscat.SyscatFactory;
import org.jooq.util.db2.syscat.tables.Datatypes;
import org.jooq.util.db2.syscat.tables.Keycoluse;
import org.jooq.util.db2.syscat.tables.References;
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
    public Factory create() {
        return new SyscatFactory(getConnection());
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

    private List<Record> fetchKeys(String constraintType) {
        return create().select(
                concat(Keycoluse.TABNAME, val("__"), Keycoluse.CONSTNAME).as("constraint_name"),
                Keycoluse.TABSCHEMA.trim(),
                Keycoluse.TABNAME,
                Keycoluse.COLNAME)
            .from(KEYCOLUSE)
            .join(TABCONST)
            .on(Keycoluse.TABSCHEMA.equal(Tabconst.TABSCHEMA))
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
                record.getValue(Sequences.PRECISION),
                0);

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

        SelectQuery q = create().selectQuery();
        q.addFrom(TABLES);
        q.addSelect(Tables.TABSCHEMA.trim());
        q.addSelect(Tables.TABNAME);
        q.addConditions(Tables.TABSCHEMA.in(getInputSchemata()));
        q.addConditions(Tables.TYPE.in("T", "V")); // tables and views
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
                getSchema(record.getValueAsString("schema")),
                record.getValueAsString("name"),
                null,
                record.getValueAsBoolean("isProcedure")));
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
