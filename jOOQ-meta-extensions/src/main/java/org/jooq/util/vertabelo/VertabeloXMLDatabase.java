/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util.vertabelo;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXB;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CheckConstraintDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
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
import org.jooq.util.vertabelo.jaxb.AlternateKey;
import org.jooq.util.vertabelo.jaxb.AlternateKeyColumn;
import org.jooq.util.vertabelo.jaxb.Column;
import org.jooq.util.vertabelo.jaxb.DatabaseModel;
import org.jooq.util.vertabelo.jaxb.Property;
import org.jooq.util.vertabelo.jaxb.Reference;
import org.jooq.util.vertabelo.jaxb.ReferenceColumn;
import org.jooq.util.vertabelo.jaxb.Sequence;
import org.jooq.util.vertabelo.jaxb.Table;
import org.jooq.util.vertabelo.jaxb.TableCheck;
import org.jooq.util.vertabelo.jaxb.View;
import org.jooq.util.xml.XMLDatabase;

/**
 * The Vertabelo XML Database
 *
 * @author Michał Kołodziejski
 */
public class VertabeloXMLDatabase extends AbstractDatabase {

    interface TableOperation {
        void invoke(Table table, String schemaName);
    }

    interface ViewOperation {
        void invoke(View view, String schemaName);
    }

    private static final JooqLogger log = JooqLogger.getLogger(VertabeloXMLDatabase.class);

    // XML additional properties
    private static final String SCHEMA_ADDITIONAL_PROPERTY_NAME = "Schema";
    private static final String PK_ADDITIONAL_PROPERTY_NAME = "Primary key name";


    protected DatabaseModel databaseModel;

    protected DatabaseModel databaseModel() {
        if(databaseModel == null) {
            databaseModel = JAXB.unmarshal(new File(getProperties().getProperty(XMLDatabase.P_XML_FILE)), DatabaseModel.class);
        }

        return databaseModel;
    }


    @Override
    protected DSLContext create0() {
        SQLDialect dialect = SQLDialect.DEFAULT;

        try {
            dialect = SQLDialect.valueOf(getProperties().getProperty(XMLDatabase.P_DIALECT));
        }
        catch (Exception ignore) {}

        return DSL.using(dialect);
    }


    @Override
    protected void loadPrimaryKeys(final DefaultRelations relations) throws SQLException {

        filterTablesBySchema(databaseModel().getTables(), new TableOperation() {
            @Override
            public void invoke(Table table, String schemaName) {

                SchemaDefinition schema = getSchema(schemaName);
                TableDefinition tableDefinition = getTable(schema, table.getName());

                if (tableDefinition != null) {
                    String pkName = getTablePkName(table);

                    // iterate through all columns and find PK columns
                    for (Column column : table.getColumns()) {
                        if (column.isPK()) {
                            relations.addPrimaryKey(pkName, tableDefinition.getColumn(column.getName()));
                        }
                    }
                }
            }
        });
    }


    private String getTablePkName(Table table) {
        Property pkAdditionalProperty = VertabeloXMLDatabase.findAdditionalProperty(PK_ADDITIONAL_PROPERTY_NAME,
            table.getProperties());
        String pkName = VertabeloXMLDatabase.getAdditionalPropertyValueOrEmpty(pkAdditionalProperty);
        if (StringUtils.isEmpty(pkName)) {
            pkName = table.getName().toUpperCase() + "_PK";
        }
        return pkName;
    }


    @Override
    protected void loadUniqueKeys(final DefaultRelations relations) throws SQLException {

        filterTablesBySchema(databaseModel().getTables(), new TableOperation() {
            @Override
            public void invoke(Table table, String schemaName) {
                SchemaDefinition schema = getSchema(schemaName);
                TableDefinition tableDefinition = getTable(schema, table.getName());

                if (tableDefinition != null) {
                    // iterate through all UNIQUE keys for this table
                    for (AlternateKey alternateKey : table.getAlternateKeys()) {

                        // iterate through all columns of this key
                        for (AlternateKeyColumn alternateKeyColumn : alternateKey.getColumns()) {
                            Column column = (Column) alternateKeyColumn.getColumn();
                            relations.addUniqueKey(alternateKey.getName(), tableDefinition.getColumn(column.getName()));
                        }

                    }
                }
            }
        });

    }


    @Override
    protected void loadForeignKeys(final DefaultRelations relations) throws SQLException {

        for (final Reference reference : databaseModel().getReferences()) {
            final Table pkTable = (Table) reference.getPKTable();
            final Table fkTable = (Table) reference.getFKTable();

            filterTablesBySchema(Arrays.asList(pkTable), new TableOperation() {
                @Override
                public void invoke(Table table, String schemaName) {
                    SchemaDefinition schema = getSchema(schemaName);
                    TableDefinition pkTableDefinition = getTable(schema, pkTable.getName());
                    TableDefinition fkTableDefinition = getTable(schema, fkTable.getName());

                    // we need to find unique key among PK and all alternate keys...
                    String uniqueKeyName = findUniqueConstraintNameForReference(reference);
                    if(uniqueKeyName == null) {
                        // no matching key - ignore this foreign key
                        return;
                    }

                    for (ReferenceColumn referenceColumn : reference.getReferenceColumns()) {
                        Column fkColumn = (Column) referenceColumn.getFKColumn();
                        ColumnDefinition fkColumnDefinition = fkTableDefinition.getColumn(fkColumn.getName());

                        relations.addForeignKey(
                            reference.getName(),
                            uniqueKeyName,
                            fkColumnDefinition,
                            pkTableDefinition.getSchema());
                    }
                }
            });

        }
    }


    private String findUniqueConstraintNameForReference(Reference reference) {
        // list of referenced columns
        List<Column> uniqueKeyColumns = new ArrayList<Column>();
        for (ReferenceColumn referenceColumn : reference.getReferenceColumns()) {
            uniqueKeyColumns.add((Column) referenceColumn.getPKColumn());
        }


        // list of PK columns
        Table pkTable = (Table) reference.getPKTable();
        List<Column> pkColumns = new ArrayList<Column>();
        for (Column column : pkTable.getColumns()) {
            if (column.isPK()) {
                pkColumns.add(column);
            }
        }

        if (uniqueKeyColumns.equals(pkColumns)) {
            // PK matches FK
            log.info("Primary key constraint matches foreign key: " + reference.getName());
            return getTablePkName((Table) reference.getPKTable());
        }

        // need to check alternate keys
        for (AlternateKey alternateKey : pkTable.getAlternateKeys()) {
            List<Column> akColumns = new ArrayList<Column>();
            for (AlternateKeyColumn column : alternateKey.getColumns()) {
                akColumns.add((Column) column.getColumn());
            }

            if (uniqueKeyColumns.equals(akColumns)) {
                // AK matches FK
                log.info("Alternate key constraint matches foreign key: " + reference.getName());
                return alternateKey.getName();
            }
        }

        // no match
        log.info("No matching unique constraint for foreign key: " + reference.getName());
        return null;
    }



    @Override
    protected void loadCheckConstraints(final DefaultRelations relations) throws SQLException {

        filterTablesBySchema(databaseModel().getTables(), new TableOperation() {
            @Override
            public void invoke(Table table, String schemaName) {
                SchemaDefinition schema = getSchema(schemaName);
                TableDefinition tableDefinition = getTable(schema, table.getName());

                if (tableDefinition != null) {

                    // iterate through all table checks
                    for (TableCheck tableCheck : table.getTableChecks()) {
                        CheckConstraintDefinition checkConstraintDefinition = new DefaultCheckConstraintDefinition(
                            schema,
                            tableDefinition,
                            tableCheck.getName(),
                            tableCheck.getCheckExpression());

                        relations.addCheckConstraint(tableDefinition, checkConstraintDefinition);
                    }

                    // iterate through all columns and find columns with checks
                    for (Column column : table.getColumns()) {
                        if (! StringUtils.isBlank(column.getCheckExpression())) {
                            CheckConstraintDefinition checkConstraintDefinition = new DefaultCheckConstraintDefinition(
                                schema,
                                tableDefinition,
                                table.getName() + "_" + column.getName() + "_check",
                                column.getCheckExpression());

                            relations.addCheckConstraint(tableDefinition, checkConstraintDefinition);
                        }
                    }
                }
            }
        });
    }


    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();
        List<String> schemaNames = new ArrayList<String>();

        // search in tables
        for (Table table : databaseModel().getTables()) {
            Property additionalProperty = findAdditionalProperty(SCHEMA_ADDITIONAL_PROPERTY_NAME, table.getProperties());
            addUniqueSchemaName(additionalProperty, schemaNames);
        }

        // search in views
        for (View view : databaseModel().getViews()) {
            Property additionalProperty = findAdditionalProperty(SCHEMA_ADDITIONAL_PROPERTY_NAME, view.getProperties());
            addUniqueSchemaName(additionalProperty, schemaNames);
        }


        // transform
        for (String schemaName : schemaNames) {
            result.add(new SchemaDefinition(this, schemaName, null));
        }

        return result;
    }


    private void addUniqueSchemaName(Property additionalProperty, List<String> schemaNames) {
        String schemaName = ""; // default to empty string
        if (additionalProperty != null) {
            // additional property is set
            schemaName = additionalProperty.getValue();
        }

        if (!schemaNames.contains(schemaName)) {
            schemaNames.add(schemaName);
        }
    }


    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Sequence sequence : databaseModel().getSequences()) {
            Property additionalProperty = VertabeloXMLDatabase.findAdditionalProperty(SCHEMA_ADDITIONAL_PROPERTY_NAME,
                sequence.getProperties());
            String schemaName = VertabeloXMLDatabase.getAdditionalPropertyValueOrEmpty(additionalProperty);

            if (getInputSchemata().contains(schemaName)) {
                SchemaDefinition schema = getSchema(schemaName);

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    "BIGINT"
                );

                result.add(new DefaultSequenceDefinition(schema, sequence.getName(), type));
            }
        }

        return result;
    }


    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        final List<TableDefinition> result = new ArrayList<TableDefinition>();

        // tables
        filterTablesBySchema(databaseModel().getTables(), new TableOperation() {
            @Override
            public void invoke(Table table, String schemaName) {
                SchemaDefinition schema = getSchema(schemaName);
                result.add(new VertabeloXMLTableDefinition(schema, table));
            }
        });

        // views
        filterViewsBySchema(databaseModel().getViews(), new ViewOperation() {
            @Override
            public void invoke(View view, String schemaName) {
                SchemaDefinition schema = getSchema(schemaName);
                result.add(new VertabeloXMLTableDefinition(schema, view));
            }
        });

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


    protected void filterTablesBySchema(List<Table> tables, TableOperation operation) {
        for (Table table : tables) {
            Property schemaAdditionalProperty = VertabeloXMLDatabase.findAdditionalProperty(SCHEMA_ADDITIONAL_PROPERTY_NAME,
                table.getProperties());
            String schemaName = VertabeloXMLDatabase.getAdditionalPropertyValueOrEmpty(schemaAdditionalProperty);

            if (getInputSchemata().contains(schemaName)) {

                operation.invoke(table, schemaName);

            }
        }
    }


    protected void filterViewsBySchema(List<View> views, ViewOperation operation) {
        for (View view : views) {
            Property schemaAdditionalProperty = VertabeloXMLDatabase.findAdditionalProperty(SCHEMA_ADDITIONAL_PROPERTY_NAME,
                view.getProperties());
            String schemaName = VertabeloXMLDatabase.getAdditionalPropertyValueOrEmpty(schemaAdditionalProperty);

            if (getInputSchemata().contains(schemaName)) {

                operation.invoke(view, schemaName);

            }
        }
    }


    public static Property findAdditionalProperty(String name, List<Property> properties) {
        for (Property property : properties) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }
        return null;
    }

    public static String getAdditionalPropertyValueOrEmpty(Property additionalProperty) {
        if (additionalProperty != null) {
            return additionalProperty.getValue();
        }
        return "";
    }
}
