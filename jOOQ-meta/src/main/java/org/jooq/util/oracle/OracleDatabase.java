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

package org.jooq.util.oracle;

import static org.jooq.util.oracle.sys.tables.AllArguments.ALL_ARGUMENTS;
import static org.jooq.util.oracle.sys.tables.AllCollTypes.ALL_COLL_TYPES;
import static org.jooq.util.oracle.sys.tables.AllConsColumns.ALL_CONS_COLUMNS;
import static org.jooq.util.oracle.sys.tables.AllConstraints.ALL_CONSTRAINTS;
import static org.jooq.util.oracle.sys.tables.AllObjects.ALL_OBJECTS;
import static org.jooq.util.oracle.sys.tables.AllSequences.ALL_SEQUENCES;
import static org.jooq.util.oracle.sys.tables.AllSequences.SEQUENCE_NAME;
import static org.jooq.util.oracle.sys.tables.AllSequences.SEQUENCE_OWNER;
import static org.jooq.util.oracle.sys.tables.AllTabComments.ALL_TAB_COMMENTS;
import static org.jooq.util.oracle.sys.tables.AllTabComments.COMMENTS;
import static org.jooq.util.oracle.sys.tables.AllTabComments.OWNER;
import static org.jooq.util.oracle.sys.tables.AllTabComments.TABLE_NAME;
import static org.jooq.util.oracle.sys.tables.AllTypes.ALL_TYPES;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.impl.Factory;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultArrayDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.FunctionDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.ProcedureDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.oracle.sys.SysFactory;
import org.jooq.util.oracle.sys.tables.AllArguments;
import org.jooq.util.oracle.sys.tables.AllCollTypes;
import org.jooq.util.oracle.sys.tables.AllConsColumns;
import org.jooq.util.oracle.sys.tables.AllConstraints;
import org.jooq.util.oracle.sys.tables.AllObjects;
import org.jooq.util.oracle.sys.tables.AllTypes;

/**
 * @author Lukas Eder
 */
public class OracleDatabase extends AbstractDatabase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            String key = record.getValue(AllConsColumns.CONSTRAINT_NAME);
            String tableName = record.getValue(AllConsColumns.TABLE_NAME);
            String columnName = record.getValue(AllConsColumns.COLUMN_NAME);

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
            String key = record.getValue(AllConsColumns.CONSTRAINT_NAME);
            String tableName = record.getValue(AllConsColumns.TABLE_NAME);
            String columnName = record.getValue(AllConsColumns.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) throws SQLException {
        return create().select(
                AllConsColumns.CONSTRAINT_NAME,
                AllConsColumns.TABLE_NAME,
                AllConsColumns.COLUMN_NAME)
            .from(ALL_CONS_COLUMNS)
            .join(ALL_CONSTRAINTS)
            .on(AllConsColumns.CONSTRAINT_NAME.equal(AllConstraints.CONSTRAINT_NAME))
            .where(AllConstraints.CONSTRAINT_TYPE.equal(constraintType))
            .and(AllConstraints.CONSTRAINT_NAME.notLike("BIN$%"))
            .and(AllConsColumns.OWNER.equal(getSchemaName()))
            .orderBy(
                AllConstraints.CONSTRAINT_NAME,
                AllConsColumns.POSITION)
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    AllConstraints.CONSTRAINT_NAME,
                    AllConstraints.TABLE_NAME,
                    AllConsColumns.COLUMN_NAME,
                    AllConstraints.R_CONSTRAINT_NAME)
                .from(ALL_CONSTRAINTS)
                .join(ALL_CONS_COLUMNS)
                .on(AllConstraints.OWNER.equal(AllConsColumns.OWNER))
                .and(AllConstraints.TABLE_NAME.equal(AllConsColumns.TABLE_NAME))
                .and(AllConstraints.CONSTRAINT_NAME.equal(AllConsColumns.CONSTRAINT_NAME))
                .where(AllConstraints.CONSTRAINT_TYPE.equal("R"))
                .and(AllConstraints.OWNER.equal(getSchemaName()))
                .orderBy(
                    AllConstraints.TABLE_NAME,
                    AllConstraints.CONSTRAINT_NAME,
                    AllConsColumns.POSITION)
                .fetch()) {

            String foreignKeyName = record.getValue(AllConstraints.CONSTRAINT_NAME);
            String foreignKeyTableName = record.getValue(AllConstraints.TABLE_NAME);
            String foreignKeyColumnName = record.getValue(AllConsColumns.COLUMN_NAME);
            String uniqueKeyName = record.getValue(AllConstraints.R_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeyTableName);
            if (referencingTable != null) {
                ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                relations.addForeignKey(foreignKeyName, uniqueKeyName, column);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (String name : create().select(SEQUENCE_NAME)
            .from(ALL_SEQUENCES)
            .where(SEQUENCE_OWNER.equal(getSchemaName()))
            .orderBy(SEQUENCE_NAME)
            .fetch(SEQUENCE_NAME)) {

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

        for (Record record : create().select(TABLE_NAME, COMMENTS)
            .from(ALL_TAB_COMMENTS)
            .where(OWNER.equal(getSchemaName()))
            .and(TABLE_NAME.notLike("%$%"))
            .orderBy(TABLE_NAME)
            .fetch()) {

            String name = record.getValue(TABLE_NAME);
            String comment = record.getValue(COMMENTS);

            OracleTableDefinition table = new OracleTableDefinition(this, name, comment);
            result.add(table);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (String name : create().selectDistinct(AllTypes.TYPE_NAME)
            .from(ALL_TYPES)
            .where(AllTypes.OWNER.equal(getSchemaName()))
            .and(AllTypes.TYPECODE.equal("OBJECT"))
            .orderBy(AllTypes.TYPE_NAME)
            .fetch(AllTypes.TYPE_NAME)) {

            result.add(new OracleUDTDefinition(this, name, null));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> arrays = new ArrayList<ArrayDefinition>();

        for (Record record : create().select(
                AllCollTypes.TYPE_NAME,
                AllCollTypes.ELEM_TYPE_NAME,
                AllCollTypes.PRECISION,
                AllCollTypes.SCALE)
            .from(ALL_COLL_TYPES)
            .where(AllCollTypes.OWNER.equal(getSchemaName()))
            .and(AllCollTypes.COLL_TYPE.equal("VARYING ARRAY"))
            .orderBy(AllCollTypes.TYPE_NAME)
            .fetch()) {

            String name = record.getValue(AllCollTypes.TYPE_NAME);
            String dataType = record.getValue(AllCollTypes.ELEM_TYPE_NAME);
            int precision = record.getValue(AllCollTypes.PRECISION, BigDecimal.ZERO).intValue();
            int scale = record.getValue(AllCollTypes.SCALE, BigDecimal.ZERO).intValue();

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(this, dataType, precision, scale);
            DefaultArrayDefinition array = new DefaultArrayDefinition(this, name, type);

            arrays.add(array);
        }

        return arrays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ProcedureDefinition> getProcedures0() throws SQLException {
        List<ProcedureDefinition> result = new ArrayList<ProcedureDefinition>();

        for (Record record : create().select(
                    AllObjects.OBJECT_NAME,
                    AllObjects.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(AllObjects.OWNER.equal(getSchemaName())
                .and(AllObjects.OBJECT_TYPE.equal("PROCEDURE")))

                // #378 - Oracle permits functions with OUT parameters
                // Those functions are mapped to procedures by jOOQ
                .or(AllObjects.OBJECT_TYPE.equal("FUNCTION")
                .andExists(create().selectOne()
                                   .from(ALL_ARGUMENTS)
                                   .where(AllArguments.OWNER.equal(getSchemaName()))
                                   .and(AllArguments.OBJECT_NAME.equal(AllObjects.OBJECT_NAME))
                                   .and(AllArguments.OBJECT_ID.equal(AllObjects.OBJECT_ID))
                                   .and(AllArguments.IN_OUT.in("OUT", "IN/OUT"))
                                   .and(AllArguments.ARGUMENT_NAME.isNotNull())
                                   .and(AllArguments.POSITION.notEqual(BigDecimal.ZERO))))
                .orderBy(AllObjects.OBJECT_NAME, AllObjects.OBJECT_ID)
                .fetch()) {

            String objectName = record.getValue(AllObjects.OBJECT_NAME);
            BigDecimal objectId = record.getValue(AllObjects.OBJECT_ID);
            result.add(new OracleProcedureDefinition(this, null, objectName, "", objectId, null));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<FunctionDefinition> getFunctions0() throws SQLException {
        List<FunctionDefinition> result = new ArrayList<FunctionDefinition>();

        for (Record record : create().select(AllObjects.OBJECT_NAME, AllObjects.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(AllObjects.OWNER.equal(getSchemaName())
                .and(AllObjects.OBJECT_TYPE.equal("FUNCTION")))

                // #378 - Oracle permits functions with OUT parameters
                // Those functions are mapped to procedures by jOOQ
                .andNotExists(create().selectOne()
                                   .from(ALL_ARGUMENTS)
                                   .where(AllArguments.OWNER.equal(getSchemaName()))
                                   .and(AllArguments.OBJECT_NAME.equal(AllObjects.OBJECT_NAME))
                                   .and(AllArguments.OBJECT_ID.equal(AllObjects.OBJECT_ID))
                                   .and(AllArguments.IN_OUT.in("OUT", "IN/OUT"))
                                   .and(AllArguments.ARGUMENT_NAME.isNotNull())
                                   .and(AllArguments.POSITION.notEqual(BigDecimal.ZERO)))
                .orderBy(AllObjects.OBJECT_NAME, AllObjects.OBJECT_ID)
                .fetch()) {
            String objectName = record.getValue(AllObjects.OBJECT_NAME);
            BigDecimal objectId = record.getValue(AllObjects.OBJECT_ID);
            result.add(new OracleFunctionDefinition(this, null, objectName, "", objectId, null));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();

        for (Record record : create().select(
        		    AllObjects.OBJECT_NAME,
        		    AllObjects.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(AllObjects.OWNER.equal(getSchemaName())
                .and(AllObjects.OBJECT_TYPE.equal("PACKAGE")))
                .orderBy(AllObjects.OBJECT_NAME, AllObjects.OBJECT_ID)
                .fetch()) {

            String name = record.getValue(AllObjects.OBJECT_NAME);
            result.add(new OraclePackageDefinition(this, name, ""));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Factory create() {
        return new SysFactory(getConnection());
    }
}
