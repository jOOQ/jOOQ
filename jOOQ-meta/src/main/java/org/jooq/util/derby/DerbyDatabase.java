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

package org.jooq.util.derby;

import static org.jooq.impl.Factory.field;
import static org.jooq.util.derby.sys.tables.Sysconglomerates.SYSCONGLOMERATES;
import static org.jooq.util.derby.sys.tables.Sysconstraints.SYSCONSTRAINTS;
import static org.jooq.util.derby.sys.tables.Syskeys.SYSKEYS;
import static org.jooq.util.derby.sys.tables.Sysschemas.SYSSCHEMAS;
import static org.jooq.util.derby.sys.tables.Syssequences.SYSSEQUENCES;
import static org.jooq.util.derby.sys.tables.Systables.SYSTABLES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Field;
import org.jooq.Record;
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
import org.jooq.util.derby.sys.SysFactory;
import org.jooq.util.derby.sys.tables.Sysconglomerates;
import org.jooq.util.derby.sys.tables.Sysconstraints;
import org.jooq.util.derby.sys.tables.Syskeys;
import org.jooq.util.derby.sys.tables.Sysschemas;
import org.jooq.util.derby.sys.tables.Syssequences;
import org.jooq.util.derby.sys.tables.Systables;

/**
 * @author Lukas Eder
 */
public class DerbyDatabase extends AbstractDatabase {

	@Override
	protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
	    for (Record record : fetchKeys("P")) {
	        SchemaDefinition schema = getSchema(record.getValue(Sysschemas.SCHEMANAME));
	        String key = record.getValue(Sysconstraints.CONSTRAINTNAME);
            String tableName = record.getValue(Systables.TABLENAME);
            String descriptor = record.getValueAsString(Sysconglomerates.DESCRIPTOR);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                for (int index : decode(descriptor)) {
                    relations.addPrimaryKey(key, table.getColumn(index));
                }
            }
	    }
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(Sysschemas.SCHEMANAME));
            String key = record.getValue(Sysconstraints.CONSTRAINTNAME);
            String tableName = record.getValue(Systables.TABLENAME);
            String descriptor = record.getValueAsString(Sysconglomerates.DESCRIPTOR);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                for (int index : decode(descriptor)) {
                    relations.addUniqueKey(key, table.getColumn(index));
                }
            }
        }
    }

    private List<Record> fetchKeys(String constraintType) {
        return create().select(
                    Sysschemas.SCHEMANAME,
    	            Systables.TABLENAME,
    	            Systables.TABLEID,
    	            Sysconstraints.CONSTRAINTNAME,
    	            Sysconglomerates.DESCRIPTOR)
    	        .from(SYSCONGLOMERATES)
    	        .join(SYSKEYS)
    	        .on(Syskeys.CONGLOMERATEID.equal(Sysconglomerates.CONGLOMERATEID))
    	        .join(SYSCONSTRAINTS)
    	        .on(Sysconstraints.CONSTRAINTID.equal(Syskeys.CONSTRAINTID))
    	        .join(SYSTABLES)
    	        .on(Systables.TABLEID.equal(Sysconglomerates.TABLEID))
    	        .join(SYSSCHEMAS)
    	        .on(Sysschemas.SCHEMAID.equal(Systables.SCHEMAID))
    	        .and(Sysschemas.SCHEMANAME.in(getInputSchemata()))
    	        .where(Sysconstraints.TYPE.equal(constraintType))
    	        .orderBy(
    	            Sysschemas.SCHEMANAME,
    	            Systables.TABLENAME,
    	            Sysconstraints.CONSTRAINTNAME)
    	        .fetch();
    }

	@Override
	protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Field<String> fkName = field("fc.constraintname", String.class);
	    Field<String> fkTable = field("ft.tablename", String.class);
	    Field<String> fkSchema = field("fs.schemaname", String.class);
	    Field<?> fkDescriptor = field("fg.descriptor");
	    Field<String> ukName = field("pc.constraintname", String.class);
	    Field<String> ukSchema = field("ps.schemaname", String.class);

	    for (Record record : create().select(
	            fkName,
	            fkTable,
	            fkSchema,
	            fkDescriptor,
	            ukName,
	            ukSchema)
	        .from("sys.sysconstraints   fc")
	        .join("sys.sysforeignkeys   f ").on("f.constraintid = fc.constraintid")
	        .join("sys.sysconglomerates fg").on("fg.conglomerateid = f.conglomerateid")
	        .join("sys.systables        ft").on("ft.tableid = fg.tableid")
	        .join("sys.sysschemas       fs").on("ft.schemaid = fs.schemaid")
	        .join("sys.sysconstraints   pc").on("pc.constraintid = f.keyconstraintid")
	        .join("sys.sysschemas       ps").on("pc.schemaid = ps.schemaid")
	        .where("fc.type = 'F'")
	        .fetch()) {

	        SchemaDefinition foreignKeySchema = getSchema(record.getValue(fkSchema));
	        SchemaDefinition uniqueKeySchema = getSchema(record.getValue(ukSchema));

	        String foreignKeyName = record.getValue(fkName);
            String foreignKeyTableName = record.getValue(fkTable);
            List<Integer> foreignKeyIndexes = decode(record.getValueAsString(fkDescriptor));
            String uniqueKeyName = record.getValue(ukName);

	        TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTableName);
            if (referencingTable != null) {
                for (int i = 0; i < foreignKeyIndexes.size(); i++) {
                    ColumnDefinition column = referencingTable.getColumn(foreignKeyIndexes.get(i));

                    relations.addForeignKey(foreignKeyName, uniqueKeyName, column, uniqueKeySchema);
                }
            }
	    }
	}

    /*
     * Unfortunately the descriptor interface is not exposed publicly Hence, the
     * toString() method is used and its results are parsed The results are
     * something like UNIQUE BTREE (index1, index2, ... indexN)
     */
    private List<Integer> decode(String descriptor) {
        List<Integer> result = new ArrayList<Integer>();

        Pattern p = Pattern.compile(".*?\\((.*?)\\)");
        Matcher m = p.matcher(descriptor);

        while (m.find()) {
            String[] split = m.group(1).split(",");

            if (split != null) {
                for (String index : split) {
                    result.add(Integer.valueOf(index.trim()) - 1);
                }
            }
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    Sysschemas.SCHEMANAME,
                    Syssequences.SEQUENCENAME,
                    Syssequences.SEQUENCEDATATYPE)
                .from(SYSSEQUENCES)
                .join(SYSSCHEMAS)
                .on(Sysschemas.SCHEMAID.equal(Syssequences.SCHEMAID))
                .where(Sysschemas.SCHEMANAME.in(getInputSchemata()))
                .orderBy(
                    Sysschemas.SCHEMANAME,
                    Syssequences.SEQUENCENAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Sysschemas.SCHEMANAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                record.getValue(Syssequences.SEQUENCEDATATYPE),
                0, 0);

            result.add(new DefaultSequenceDefinition(
                schema,
                record.getValueAsString(Syssequences.SEQUENCENAME),
                type));
        }

        return result;
    }

	@Override
	protected List<TableDefinition> getTables0() throws SQLException {
		List<TableDefinition> result = new ArrayList<TableDefinition>();

		for (Record record : create().select(
		            Sysschemas.SCHEMANAME,
		            Systables.TABLENAME,
		            Systables.TABLEID)
                .from(SYSTABLES)
                .join(SYSSCHEMAS)
                .on(Systables.SCHEMAID.equal(Sysschemas.SCHEMAID))
                .where(Sysschemas.SCHEMANAME.in(getInputSchemata()))
                .orderBy(
                    Sysschemas.SCHEMANAME,
                    Systables.TABLENAME)
    	        .fetch()) {

		    SchemaDefinition schema = getSchema(record.getValue(Sysschemas.SCHEMANAME));
		    String name = record.getValue(Systables.TABLENAME);
		    String id = record.getValue(Systables.TABLEID);

		    DerbyTableDefinition table = new DerbyTableDefinition(schema, name, id);
            result.add(table);
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
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    public Factory create() {
        return new SysFactory(getConnection());
    }
}
