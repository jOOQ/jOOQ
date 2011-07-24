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

import static org.jooq.util.db2.syscat.tables.Datatypes.DATATYPES;
import static org.jooq.util.db2.syscat.tables.Funcparms.FUNCPARMS;
import static org.jooq.util.db2.syscat.tables.Functions.FUNCTIONS;
import static org.jooq.util.db2.syscat.tables.Keycoluse.KEYCOLUSE;
import static org.jooq.util.db2.syscat.tables.References.REFERENCES;
import static org.jooq.util.db2.syscat.tables.Sequences.SEQUENCES;
import static org.jooq.util.db2.syscat.tables.Tabconst.TABCONST;
import static org.jooq.util.db2.syscat.tables.Tables.TABLES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.FunctionDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ProcedureDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.db2.syscat.SyscatFactory;
import org.jooq.util.db2.syscat.tables.Datatypes;
import org.jooq.util.db2.syscat.tables.Funcparms;
import org.jooq.util.db2.syscat.tables.Functions;
import org.jooq.util.db2.syscat.tables.Keycoluse;
import org.jooq.util.db2.syscat.tables.Procedures;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Factory create() {
        return new SyscatFactory(getConnection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            String key = record.getValue(Keycoluse.CONSTNAME);
            String tableName = record.getValue(Keycoluse.TABNAME);
            String columnName = record.getValue(Keycoluse.COLNAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            String key = record.getValue(Keycoluse.CONSTNAME);
            String tableName = record.getValue(Keycoluse.TABNAME);
            String columnName = record.getValue(Keycoluse.COLNAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) throws SQLException {
        return create().select(
                Keycoluse.CONSTNAME,
                Keycoluse.TABNAME,
                Keycoluse.COLNAME)
            .from(KEYCOLUSE)
            .join(TABCONST)
            .on(Keycoluse.TABSCHEMA.equal(Tabconst.TABSCHEMA))
            .and(Keycoluse.CONSTNAME.equal(Tabconst.CONSTNAME))
            .where(Keycoluse.TABSCHEMA.equal(getSchemaName()))
            .and(Tabconst.TYPE.equal(constraintType))
            .orderBy(
                Keycoluse.CONSTNAME.asc(),
                Keycoluse.COLSEQ.asc())
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    References.CONSTNAME,
                    References.TABNAME,
                    References.FK_COLNAMES,
                    References.REFKEYNAME)
                .from(REFERENCES)
                .where(References.TABSCHEMA.equal(getSchemaName()))
                .fetch()) {

            String foreignKey = record.getValue(References.CONSTNAME);
            String foreignKeyTableName = record.getValue(References.TABNAME);
            String foreignKeyColumn = record.getValue(References.FK_COLNAMES);
            String uniqueKey = record.getValue(References.REFKEYNAME);

            TableDefinition foreignKeyTable = getTable(foreignKeyTableName);

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

                    relations.addForeignKey(foreignKey, uniqueKey, column);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(Sequences.SEQNAME)
            .from(SEQUENCES)
            .where(Sequences.SEQSCHEMA.equal(getSchemaName()))
            .orderBy(Sequences.SEQNAME)
            .fetch(Sequences.SEQNAME)) {

            result.add(new DefaultSequenceDefinition(this, name));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        SelectQuery q = create().selectQuery();
        q.addFrom(TABLES);
        q.addSelect(Tables.TABNAME);
        q.addConditions(Tables.TABSCHEMA.equal(getSchemaName()));
        q.addConditions(Tables.TYPE.in("T", "V")); // tables and views
        q.addOrderBy(Tables.TABNAME);
        q.execute();

        for (Record record : q.getResult()) {
            String name = record.getValue(Tables.TABNAME);
            String comment = "";

            DB2TableDefinition table = new DB2TableDefinition(this, name, comment);
            result.add(table);

        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ProcedureDefinition> getProcedures0() throws SQLException {
        List<ProcedureDefinition> result = new ArrayList<ProcedureDefinition>();

        for (Record record : create().select(Procedures.PROCNAME)
            .from(Procedures.PROCEDURES)
            .where(Procedures.PROCSCHEMA.equal(getSchemaName()))
            .orderBy(Procedures.PROCNAME)
            .fetch()) {

            String name = record.getValue(Procedures.PROCNAME);
            result.add(new DB2ProcedureDefinition(this, null, name));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<FunctionDefinition> getFunctions0() throws SQLException {
        Map<String, DB2FunctionDefinition> functionMap = new HashMap<String, DB2FunctionDefinition>();

        SelectQuery q = create().selectQuery();
        q.addFrom(FUNCPARMS);
        q.addJoin(FUNCTIONS, Funcparms.FUNCSCHEMA.equal(Functions.FUNCSCHEMA),
            Funcparms.FUNCNAME.equal(Functions.FUNCNAME));
        q.addConditions(Funcparms.FUNCSCHEMA.equal(getSchemaName()));
        q.addConditions(Functions.ORIGIN.equal("Q"));
        q.addOrderBy(Funcparms.FUNCNAME);
        q.addOrderBy(Funcparms.ORDINAL);
        q.execute();

        for (Record record : q.getResult()) {
            String name = record.getValue(Funcparms.FUNCNAME);
            String rowType = record.getValue(Funcparms.ROWTYPE);
            String dataType = record.getValue(Funcparms.TYPENAME);
            Integer precision = record.getValue(Funcparms.LENGTH);
            Short scale = record.getValue(Funcparms.SCALE);
            int position = record.getValue(Funcparms.ORDINAL);
            String paramName = record.getValue(Funcparms.PARMNAME);

            DB2FunctionDefinition function = functionMap.get(name);
            if (function == null) {
                function = new DB2FunctionDefinition(this, null, name, null);
                functionMap.put(name, function);
            }

            if ("C".equals(rowType)) { // result after casting
                function.setReturnValue(dataType, precision, scale);
            }
            else if ("P".equals(rowType)) { // parameter
                function.addParameter(paramName, position, dataType, precision, scale);
            }
            else { // result before casting
                   // continue
            }
        }
        return new ArrayList<FunctionDefinition>(functionMap.values());
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

        for (String name : create().selectDistinct(Datatypes.TYPENAME)
            .from(DATATYPES)
            .where(Datatypes.TYPESCHEMA.equal(getSchemaName()))
            .orderBy(Datatypes.TYPENAME).fetch(Datatypes.TYPENAME)) {

            result.add(new DB2UDTDefinition(this, name, null));
        }

        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }
}
