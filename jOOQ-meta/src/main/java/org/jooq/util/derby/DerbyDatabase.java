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

package org.jooq.util.derby;

import static org.jooq.impl.DSL.field;
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

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
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
	        SchemaDefinition schema = getSchema(record.get(Sysschemas.SCHEMANAME));
	        String key = record.get(Sysconstraints.CONSTRAINTNAME);
            String tableName = record.get(Systables.TABLENAME);
            String descriptor = record.get(Sysconglomerates.DESCRIPTOR, String.class);

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
            SchemaDefinition schema = getSchema(record.get(Sysschemas.SCHEMANAME));
            String key = record.get(Sysconstraints.CONSTRAINTNAME);
            String tableName = record.get(Systables.TABLENAME);
            String descriptor = record.get(Sysconglomerates.DESCRIPTOR, String.class);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                for (int index : decode(descriptor)) {
                    relations.addUniqueKey(key, table.getColumn(index));
                }
            }
        }
    }

    private Result<Record5<String, String, String, String, String>> fetchKeys(String constraintType) {
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

	        SchemaDefinition foreignKeySchema = getSchema(record.get(fkSchema));
	        SchemaDefinition uniqueKeySchema = getSchema(record.get(ukSchema));

	        String foreignKeyName = record.get(fkName);
            String foreignKeyTableName = record.get(fkTable);
            List<Integer> foreignKeyIndexes = decode(record.get(fkDescriptor, String.class));
            String uniqueKeyName = record.get(ukName);

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
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
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
                .select(Sysschemas.SCHEMANAME)
                .from(SYSSCHEMAS)
                .fetch(Sysschemas.SCHEMANAME)) {

            result.add(new SchemaDefinition(this, name, ""));
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

            SchemaDefinition schema = getSchema(record.get(Sysschemas.SCHEMANAME));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                record.get(Syssequences.SEQUENCEDATATYPE)
            );

            result.add(new DefaultSequenceDefinition(
                schema,
                record.get(Syssequences.SEQUENCENAME, String.class),
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

		    SchemaDefinition schema = getSchema(record.get(Sysschemas.SCHEMANAME));
		    String name = record.get(Systables.TABLENAME);
		    String id = record.get(Systables.TABLEID);

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
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.DERBY);
    }
}
