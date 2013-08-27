/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.util.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
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

/**
 * The JDBC database
 *
 * @author Lukas Eder
 */
public class JDBCDatabase extends AbstractDatabase {

    private List<Schema> schemas;

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection());
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (UniqueKey<?> key : create().meta().getPrimaryKeys()) {
            Schema schema = key.getTable().getSchema();
            SchemaDefinition s = getSchema(schema == null ? "" : schema.getName());

            if (s != null) {
                TableDefinition t = getTable(s, key.getTable().getName());

                if (t != null) {
                    for (Field<?> field : key.getFields()) {
                        ColumnDefinition c = t.getColumn(field.getName());
                        relations.addPrimaryKey("PK_" + key.getTable().getName(), c);
                    }
                }
            }
        }
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            result.add(new SchemaDefinition(this, schema.getName(), ""));
        }

        return result;
    }

    private List<Schema> getSchemasFromMeta() {
        if (schemas == null) {
            schemas = create().meta().getSchemas();
        }

        return schemas;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            for (Sequence<?> sequence : schema.getSequences()) {
                SchemaDefinition sd = getSchema(schema.getName());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    sd,
                    sequence.getDataType().getTypeName()
                );

                result.add(new DefaultSequenceDefinition(
                    sd, sequence.getName(), type));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Schema schema : getSchemasFromMeta()) {
            SchemaDefinition sd = getSchema(schema.getName());

            if (sd != null) {
                for (Table<?> table : schema.getTables()) {
                    result.add(new JDBCTableDefinition(sd, table));
                }
            }
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
}
