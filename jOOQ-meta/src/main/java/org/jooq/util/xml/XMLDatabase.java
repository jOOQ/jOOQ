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

package org.jooq.util.xml;

import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXB;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
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
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.Schema;
import org.jooq.util.xml.jaxb.Sequence;
import org.jooq.util.xml.jaxb.Table;
import org.jooq.util.xml.jaxb.TableConstraint;
import org.jooq.util.xml.jaxb.TableConstraintType;

/**
 * The XML Database.
 *
 * @author Lukas Eder
 */
public class XMLDatabase extends AbstractDatabase {

    InformationSchema info;

    private InformationSchema info() {
        if (info == null) {
            info = JAXB.unmarshal(new File(getProperties().getProperty("xml-file")), InformationSchema.class);
        }

        return info;
    }

    @Override
    protected DSLContext create0() {

        @SuppressWarnings("deprecation")
        SQLDialect dialect = SQLDialect.SQL99;

        try {
            dialect = SQLDialect.valueOf(getProperties().getProperty("dialect"));
        }
        catch (Exception ignore) {}

        return DSL.using(dialect);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) {
        for (KeyColumnUsage usage : keyColumnUsage(PRIMARY_KEY)) {
            SchemaDefinition schema = getSchema(usage.getConstraintSchema());
            String key = usage.getConstraintName();
            String tableName = usage.getTableName();
            String columnName = usage.getColumnName();

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) {
        for (KeyColumnUsage usage : keyColumnUsage(UNIQUE)) {
            SchemaDefinition schema = getSchema(usage.getConstraintSchema());
            String key = usage.getConstraintName();
            String tableName = usage.getTableName();
            String columnName = usage.getColumnName();

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<KeyColumnUsage> keyColumnUsage(TableConstraintType constraintType) {
        List<KeyColumnUsage> result = new ArrayList<KeyColumnUsage>();

        for (TableConstraint constraint : info().getTableConstraints()) {
            if (constraintType == constraint.getConstraintType()
                    && getInputSchemata().contains(constraint.getConstraintSchema())) {

                for (KeyColumnUsage usage : info().getKeyColumnUsages()) {
                    if (    StringUtils.equals(constraint.getConstraintCatalog(), usage.getConstraintCatalog())
                         && StringUtils.equals(constraint.getConstraintSchema(), usage.getConstraintSchema())
                         && StringUtils.equals(constraint.getConstraintName(), usage.getConstraintName())) {

                        result.add(usage);
                    }
                }
            }
        }

        Collections.sort(result, new Comparator<KeyColumnUsage>() {
            @Override
            public int compare(KeyColumnUsage o1, KeyColumnUsage o2) {
                int r = 0;

                r = defaultIfNull(o1.getConstraintCatalog(), "").compareTo(defaultIfNull(o2.getConstraintCatalog(), ""));
                if (r != 0)
                    return r;

                r = defaultIfNull(o1.getConstraintSchema(), "").compareTo(defaultIfNull(o2.getConstraintSchema(), ""));
                if (r != 0)
                    return r;

                r = defaultIfNull(o1.getConstraintName(), "").compareTo(defaultIfNull(o2.getConstraintName(), ""));
                if (r != 0)
                    return r;

                return Integer.valueOf(o1.getOrdinalPosition()).compareTo(o2.getOrdinalPosition());
            }
        });

        return result;
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) {
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) {
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (Schema schema : info().getSchemata()) {
            if (getInputSchemata().contains(schema.getSchemaName())) {
                result.add(new SchemaDefinition(this, schema.getSchemaName(), null));
            }
        }

        return result;
    }


    @Override
    protected List<SequenceDefinition> getSequences0() {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Sequence sequence : info().getSequences()) {
            if (getInputSchemata().contains(sequence.getSequenceSchema())) {
                SchemaDefinition schema = getSchema(sequence.getSequenceSchema());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    sequence.getDataType(),
                    sequence.getCharacterMaximumLength(),
                    sequence.getNumericPrecision(),
                    sequence.getNumericScale(),
                    false,
                    false
                );

                result.add(new DefaultSequenceDefinition(schema, sequence.getSequenceName(), type));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Table table : info().getTables()) {
            if (getInputSchemata().contains(table.getTableSchema())) {
                SchemaDefinition schema = getSchema(table.getTableSchema());

                result.add(new XMLTableDefinition(schema, info(), table));
            }
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    static int unbox(Integer i) {
        return i == null ? 0 : i.intValue();
    }

    static long unbox(Long l) {
        return l == null ? 0L : l.longValue();
    }
}
