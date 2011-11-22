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

import static org.jooq.util.oracle.sys.Tables.ALL_COLL_TYPES;
import static org.jooq.util.oracle.sys.Tables.ALL_CONSTRAINTS;
import static org.jooq.util.oracle.sys.Tables.ALL_CONS_COLUMNS;
import static org.jooq.util.oracle.sys.Tables.ALL_OBJECTS;
import static org.jooq.util.oracle.sys.Tables.ALL_SEQUENCES;
import static org.jooq.util.oracle.sys.Tables.ALL_TAB_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_TYPES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultArrayDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.oracle.sys.SysFactory;

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
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

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
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) {
        return create().select(
                ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.TABLE_NAME,
                ALL_CONS_COLUMNS.COLUMN_NAME)
            .from(ALL_CONS_COLUMNS)
            .join(ALL_CONSTRAINTS)
            .on(ALL_CONS_COLUMNS.CONSTRAINT_NAME.equal(ALL_CONSTRAINTS.CONSTRAINT_NAME))
            .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal(constraintType))
            .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.notLike("BIN$%"))
            .and(ALL_CONS_COLUMNS.OWNER.equal(getInputSchema()))
            .orderBy(
                ALL_CONSTRAINTS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.POSITION)
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ALL_CONSTRAINTS.CONSTRAINT_NAME,
                    ALL_CONSTRAINTS.TABLE_NAME,
                    ALL_CONS_COLUMNS.COLUMN_NAME,
                    ALL_CONSTRAINTS.R_CONSTRAINT_NAME)
                .from(ALL_CONSTRAINTS)
                .join(ALL_CONS_COLUMNS)
                .on(ALL_CONSTRAINTS.OWNER.equal(ALL_CONS_COLUMNS.OWNER))
                .and(ALL_CONSTRAINTS.TABLE_NAME.equal(ALL_CONS_COLUMNS.TABLE_NAME))
                .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.equal(ALL_CONS_COLUMNS.CONSTRAINT_NAME))
                .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal("R"))
                .and(ALL_CONSTRAINTS.OWNER.equal(getInputSchema()))
                .orderBy(
                    ALL_CONSTRAINTS.TABLE_NAME,
                    ALL_CONSTRAINTS.CONSTRAINT_NAME,
                    ALL_CONS_COLUMNS.POSITION)
                .fetch()) {

            String foreignKeyName = record.getValue(ALL_CONSTRAINTS.CONSTRAINT_NAME);
            String foreignKeyTableName = record.getValue(ALL_CONSTRAINTS.TABLE_NAME);
            String foreignKeyColumnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);
            String uniqueKeyName = record.getValue(ALL_CONSTRAINTS.R_CONSTRAINT_NAME);

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

        for (Record record : create().select(
                    ALL_SEQUENCES.SEQUENCE_NAME,
                    ALL_SEQUENCES.MAX_VALUE)
                .from(ALL_SEQUENCES)
                .where(ALL_SEQUENCES.SEQUENCE_OWNER.equal(getInputSchema()))
                .orderBy(ALL_SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            DataTypeDefinition type;

            BigInteger value = record.getValue(ALL_SEQUENCES.MAX_VALUE, BigInteger.class, BigInteger.valueOf(Long.MAX_VALUE));

            if (BigInteger.valueOf(Byte.MAX_VALUE).compareTo(value) >= 0) {
                type = new DefaultDataTypeDefinition(this, SQLDataType.NUMERIC.getTypeName(), 2, 0);
            }
            else if (BigInteger.valueOf(Short.MAX_VALUE).compareTo(value) >= 0) {
                type = new DefaultDataTypeDefinition(this, SQLDataType.NUMERIC.getTypeName(), 4, 0);
            }
            else if (BigInteger.valueOf(Integer.MAX_VALUE).compareTo(value) >= 0) {
                type = new DefaultDataTypeDefinition(this, SQLDataType.NUMERIC.getTypeName(), 9, 0);
            }
            else if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(value) >= 0) {
                type = new DefaultDataTypeDefinition(this, SQLDataType.NUMERIC.getTypeName(), 18, 0);
            }
            else {
                type = new DefaultDataTypeDefinition(this, SQLDataType.NUMERIC.getTypeName(), 38, 0);
            }

            result.add(new DefaultSequenceDefinition(getSchema(),
                record.getValue(ALL_SEQUENCES.SEQUENCE_NAME),
                type));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                ALL_TAB_COMMENTS.TABLE_NAME,
                ALL_TAB_COMMENTS.COMMENTS)
            .from(ALL_TAB_COMMENTS)
            .where(ALL_TAB_COMMENTS.OWNER.equal(getInputSchema()))
            .and(ALL_TAB_COMMENTS.TABLE_NAME.notLike("%$%"))
            .orderBy(ALL_TAB_COMMENTS.TABLE_NAME)
            .fetch()) {

            String name = record.getValue(ALL_TAB_COMMENTS.TABLE_NAME);
            String comment = record.getValue(ALL_TAB_COMMENTS.COMMENTS);

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

        for (String name : create().selectDistinct(ALL_TYPES.TYPE_NAME)
            .from(ALL_TYPES)
            .where(ALL_TYPES.OWNER.equal(getInputSchema()))
            .and(ALL_TYPES.TYPECODE.equal("OBJECT"))
            .orderBy(ALL_TYPES.TYPE_NAME)
            .fetch(ALL_TYPES.TYPE_NAME)) {

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
                ALL_COLL_TYPES.TYPE_NAME,
                ALL_COLL_TYPES.ELEM_TYPE_NAME,
                ALL_COLL_TYPES.PRECISION,
                ALL_COLL_TYPES.SCALE)
            .from(ALL_COLL_TYPES)
            .where(ALL_COLL_TYPES.OWNER.equal(getInputSchema()))
            .and(ALL_COLL_TYPES.COLL_TYPE.equal("VARYING ARRAY"))
            .orderBy(ALL_COLL_TYPES.TYPE_NAME)
            .fetch()) {

            String name = record.getValue(ALL_COLL_TYPES.TYPE_NAME);
            String dataType = record.getValue(ALL_COLL_TYPES.ELEM_TYPE_NAME);
            int precision = record.getValue(ALL_COLL_TYPES.PRECISION, BigDecimal.ZERO).intValue();
            int scale = record.getValue(ALL_COLL_TYPES.SCALE, BigDecimal.ZERO).intValue();

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(this, dataType, precision, scale);
            DefaultArrayDefinition array = new DefaultArrayDefinition(this, name, type);

            arrays.add(array);
        }

        return arrays;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create().select(ALL_OBJECTS.OBJECT_NAME, ALL_OBJECTS.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(ALL_OBJECTS.OWNER.equal(getInputSchema())
                .and(ALL_OBJECTS.OBJECT_TYPE.in("FUNCTION", "PROCEDURE")))
                .orderBy(ALL_OBJECTS.OBJECT_NAME, ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            String objectName = record.getValue(ALL_OBJECTS.OBJECT_NAME);
            BigDecimal objectId = record.getValue(ALL_OBJECTS.OBJECT_ID);
            result.add(new OracleRoutineDefinition(this, null, objectName, "", objectId, null));
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
        		    ALL_OBJECTS.OBJECT_NAME,
        		    ALL_OBJECTS.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(ALL_OBJECTS.OWNER.equal(getInputSchema())
                .and(ALL_OBJECTS.OBJECT_TYPE.equal("PACKAGE")))
                .orderBy(ALL_OBJECTS.OBJECT_NAME, ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            String name = record.getValue(ALL_OBJECTS.OBJECT_NAME);
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
