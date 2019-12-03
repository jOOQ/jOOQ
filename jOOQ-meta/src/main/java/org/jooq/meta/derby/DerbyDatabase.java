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
 *
 *
 *
 */

package org.jooq.meta.derby;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.meta.derby.sys.tables.Syschecks.SYSCHECKS;
import static org.jooq.meta.derby.sys.tables.Sysconglomerates.SYSCONGLOMERATES;
import static org.jooq.meta.derby.sys.tables.Sysconstraints.SYSCONSTRAINTS;
import static org.jooq.meta.derby.sys.tables.Syskeys.SYSKEYS;
import static org.jooq.meta.derby.sys.tables.Sysschemas.SYSSCHEMAS;
import static org.jooq.meta.derby.sys.tables.Syssequences.SYSSEQUENCES;
import static org.jooq.meta.derby.sys.tables.Systables.SYSTABLES;

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
import org.jooq.SortOrder;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultCheckConstraintDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.derby.sys.tables.Syschecks;
import org.jooq.meta.derby.sys.tables.Sysconglomerates;
import org.jooq.meta.derby.sys.tables.Sysconstraints;
import org.jooq.meta.derby.sys.tables.Syskeys;
import org.jooq.meta.derby.sys.tables.Sysschemas;
import org.jooq.meta.derby.sys.tables.Syssequences;
import org.jooq.meta.derby.sys.tables.Systables;

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
            if (table != null)
                for (int index : decode(descriptor))
                    relations.addPrimaryKey(key, table, table.getColumn(index));
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
            if (table != null)
                for (int index : decode(descriptor))
                    relations.addUniqueKey(key, table, table.getColumn(index));
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
                // [#6797] The casts are necessary if a non-standard collation is used
    	        .and(Sysschemas.SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
    	        .where(Sysconstraints.TYPE.cast(VARCHAR(32672)).equal(constraintType))
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
        Field<String> ukTable = field("pt.tablename", String.class);
	    Field<String> ukSchema = field("ps.schemaname", String.class);

	    for (Record record : create().select(
	            fkName,
	            fkTable,
	            fkSchema,
	            fkDescriptor,
	            ukName,
	            ukTable,
	            ukSchema)
	        .from("sys.sysconstraints   fc")
	        .join("sys.sysforeignkeys   f ").on("f.constraintid = fc.constraintid")
	        .join("sys.sysconglomerates fg").on("fg.conglomerateid = f.conglomerateid")
	        .join("sys.systables        ft").on("ft.tableid = fg.tableid")
	        .join("sys.sysschemas       fs").on("ft.schemaid = fs.schemaid")
	        .join("sys.sysconstraints   pc").on("pc.constraintid = f.keyconstraintid")
            .join("sys.systables        pt").on("pt.tableid = pc.tableid")
	        .join("sys.sysschemas       ps").on("ps.schemaid = pt.schemaid")
            // [#6797] The cast is necessary if a non-standard collation is used
	        .where("cast(fc.type as varchar(32672)) = 'F'")
	        .fetch()) {

	        SchemaDefinition foreignKeySchema = getSchema(record.get(fkSchema));
	        SchemaDefinition uniqueKeySchema = getSchema(record.get(ukSchema));

	        String foreignKeyName = record.get(fkName);
            String foreignKeyTableName = record.get(fkTable);
            List<Integer> foreignKeyIndexes = decode(record.get(fkDescriptor, String.class));
            String uniqueKeyName = record.get(ukName);
            String uniqueKeyTableName = record.get(ukTable);

	        TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
	        TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

            if (foreignKeyTable != null && uniqueKeyTable != null)
                for (int i = 0; i < foreignKeyIndexes.size(); i++)
                    relations.addForeignKey(
                        foreignKeyName,
                        foreignKeyTable,
                        foreignKeyTable.getColumn(foreignKeyIndexes.get(i)),
                        uniqueKeyName,
                        uniqueKeyTable
                    );
	    }
	}

    /*
     * Unfortunately the descriptor interface is not exposed publicly Hence, the
     * toString() method is used and its results are parsed The results are
     * something like UNIQUE BTREE (index1, index2, ... indexN)
     */
    private List<Integer> decode(String descriptor) {
        List<Integer> result = new ArrayList<>();

        Pattern p = Pattern.compile(".*?\\((.*?)\\)");
        Matcher m = p.matcher(descriptor);

        while (m.find()) {
            String[] split = m.group(1).split(",");

            if (split != null)
                for (String index : split)
                    result.add(Integer.valueOf(index.trim()) - 1);
        }

        return result;
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        for (Record record : create()
            .select(
                Sysschemas.SCHEMANAME,
                Systables.TABLENAME,
                Sysconstraints.CONSTRAINTNAME,
                Syschecks.CHECKDEFINITION)
            .from(SYSCHECKS)
            .join(SYSCONSTRAINTS)
                .on(Syschecks.CONSTRAINTID.eq(Sysconstraints.CONSTRAINTID))
            .join(SYSTABLES)
                .on(Systables.TABLEID.equal(Sysconstraints.TABLEID))
            .join(SYSSCHEMAS)
                .on(Sysschemas.SCHEMAID.equal(Systables.SCHEMAID))
            .where(Sysschemas.SCHEMANAME.in(getInputSchemata()))
        ) {
            SchemaDefinition schema = getSchema(record.get(Sysschemas.SCHEMANAME));
            TableDefinition table = getTable(schema, record.get(Systables.TABLENAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.get(Sysconstraints.CONSTRAINTNAME),
                    record.get(Syschecks.CHECKDEFINITION)
                ));
            }
        }
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<>();

        indexLoop:
        for (Record record : create()
            .select(
                Sysschemas.SCHEMANAME,
                Systables.TABLENAME,
                Sysconglomerates.CONGLOMERATENAME,
                Sysconglomerates.DESCRIPTOR)
            .from(SYSCONGLOMERATES)
            .join(SYSTABLES).on(Sysconglomerates.TABLEID.eq(Systables.TABLEID))
            .join(SYSSCHEMAS).on(Systables.SCHEMAID.eq(Sysschemas.SCHEMAID))

            // [#6797] The cast is necessary if a non-standard collation is used
            .where(Sysschemas.SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
            .and(Sysconglomerates.ISINDEX)
            .and(getIncludeSystemIndexes()
                ? noCondition()
                : not(condition(Sysconglomerates.ISCONSTRAINT)))
            .orderBy(
                Sysschemas.SCHEMANAME,
                Systables.TABLENAME,
                Sysconglomerates.CONGLOMERATENAME)
        ) {
            final SchemaDefinition tableSchema = getSchema(record.get(Sysschemas.SCHEMANAME));
            if (tableSchema == null)
                continue indexLoop;

            final String indexName = record.get(Sysconglomerates.CONGLOMERATENAME);
            final String tableName = record.get(Systables.TABLENAME);
            final TableDefinition table = getTable(tableSchema, tableName);
            if (table == null)
                continue indexLoop;

            final String descriptor = record.get(Sysconglomerates.DESCRIPTOR);
            if (descriptor == null)
                continue indexLoop;

            result.add(new AbstractIndexDefinition(tableSchema, indexName, table, descriptor.toUpperCase().contains("UNIQUE")) {
                List<IndexColumnDefinition> indexColumns = new ArrayList<>();

                {
                    List<Integer> columnIndexes = decode(descriptor);
                    for (int i = 0; i < columnIndexes.size(); i++) {
                        indexColumns.add(new DefaultIndexColumnDefinition(
                            this,
                            table.getColumn(columnIndexes.get(i)),
                            SortOrder.ASC,
                            i + 1
                        ));
                    }
                }

                @Override
                protected List<IndexColumnDefinition> getIndexColumns0() {
                    return indexColumns;
                }
            });
        }

        return result;
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<>();

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
        List<SequenceDefinition> result = new ArrayList<>();

        for (Record record : create().select(
                    Sysschemas.SCHEMANAME,
                    Syssequences.SEQUENCENAME,
                    Syssequences.SEQUENCEDATATYPE)
                .from(SYSSEQUENCES)
                .join(SYSSCHEMAS)
                .on(Sysschemas.SCHEMAID.equal(Syssequences.SCHEMAID))
                // [#6797] The cast is necessary if a non-standard collation is used
                .where(Sysschemas.SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
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
		List<TableDefinition> result = new ArrayList<>();

		for (Record record : create().select(
		            Sysschemas.SCHEMANAME,
		            Systables.TABLENAME,
		            Systables.TABLEID)
                .from(SYSTABLES)
                .join(SYSSCHEMAS)
                .on(Systables.SCHEMAID.equal(Sysschemas.SCHEMAID))
                // [#6797] The cast is necessary if a non-standard collation is used
                .where(Sysschemas.SCHEMANAME.cast(VARCHAR(32672)).in(getInputSchemata()))
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
        List<EnumDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.DERBY);
    }
}
