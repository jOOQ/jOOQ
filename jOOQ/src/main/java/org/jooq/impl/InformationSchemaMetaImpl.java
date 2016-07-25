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
package org.jooq.impl;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.ForeignKey;
import org.jooq.Meta;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * @author Lukas Eder
 */
final class InformationSchemaMetaImpl implements Meta {

    private final Configuration               configuration;
    private final List<Catalog>               catalogs;
    private final List<Schema>                schemas;
    private final Map<String, Schema>         schemasByName;
    private final Map<Catalog, List<Schema>>  schemasPerCatalog;
    private final List<Table<?>>              tables;
    private final Map<String, Table<?>>       tablesByName;
    private final Map<Schema, List<Table<?>>> tablesPerSchema;
    private final List<UniqueKey<?>>          primaryKeys;

    InformationSchemaMetaImpl(Configuration configuration, InformationSchema schema) {
        this.configuration = configuration;
        this.catalogs = new ArrayList<Catalog>();
        this.schemas = new ArrayList<Schema>();
        this.schemasByName = new HashMap<String, Schema>();
        this.schemasPerCatalog = new HashMap<Catalog, List<Schema>>();
        this.tables = new ArrayList<Table<?>>();
        this.tablesByName = new HashMap<String, Table<?>>();
        this.tablesPerSchema = new HashMap<Schema, List<Table<?>>>();
        this.primaryKeys = new ArrayList<UniqueKey<?>>();

        init(schema);
    }

    private final void init(InformationSchema meta) {

        // Initialise base collections
        for (org.jooq.util.xml.jaxb.Schema xs : meta.getSchemata()) {
            InformationSchemaCatalog catalog = new InformationSchemaCatalog(xs.getCatalogName());

            if (!catalogs.contains(catalog))
                catalogs.add(catalog);

            InformationSchemaSchema is = new InformationSchemaSchema(xs.getSchemaName(), catalog);
            schemas.add(is);
            schemasByName.put(xs.getSchemaName(), is);
        }

        for (org.jooq.util.xml.jaxb.Table xt : meta.getTables()) {
            InformationSchemaTable it = new InformationSchemaTable(xt.getTableName(), schemasByName.get(xt.getTableSchema()));
            tables.add(it);
            tablesByName.put(xt.getTableName(), it);
        }

        for (Column xc : meta.getColumns()) {
            String typeName = xc.getDataType();
            int precision = xc.getNumericPrecision() == null ? 0 : xc.getNumericPrecision();
            int scale = xc.getNumericScale() == null ? 0 : xc.getNumericScale();
            boolean nullable = xc.isIsNullable() == null ? true : xc.isIsNullable();

            // TODO: Exception handling should be moved inside SQLDataType
            DataType<?> type = null;
            try {
                type = DefaultDataType.getDataType(configuration.family(), typeName, precision, scale);

                // JDBC doesn't distinguish between precision and length
                type = type.precision(precision, scale);
                type = type.length(precision);
                type = type.nullable(nullable);
            }
            catch (SQLDialectNotSupportedException e) {
                type = SQLDataType.OTHER;
            }

            AbstractTable.createField(xc.getColumnName(), type, tablesByName.get(xc.getTableName()));
        }

        // Initialise indexes
        for (Schema s : schemas) {
            Catalog c = s.getCatalog();
            List<Schema> list = schemasPerCatalog.get(c);

            if (list == null) {
                list = new ArrayList<Schema>();
                schemasPerCatalog.put(c, list);
            }

            list.add(s);
        }

        for (Table<?> t : tables) {
            Schema s = t.getSchema();
            List<Table<?>> list = tablesPerSchema.get(s);

            if (list == null) {
                list = new ArrayList<Table<?>>();
                tablesPerSchema.put(s, list);
            }

            list.add(t);
        }
    }

    @Override
    public final List<Catalog> getCatalogs() {
        return unmodifiableList(catalogs);
    }

    @Override
    public final List<Schema> getSchemas() {
        return unmodifiableList(schemas);
    }

    @Override
    public final List<Table<?>> getTables() {
        return unmodifiableList(tables);
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        return unmodifiableList(primaryKeys);
    }

    private final class InformationSchemaCatalog extends CatalogImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 87038321849045492L;

        InformationSchemaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            return unmodifiableList(schemasPerCatalog.get(this));
        }
    }

    private final class InformationSchemaSchema extends SchemaImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7290709749127378187L;

        public InformationSchemaSchema(String name, Catalog catalog) {
            super(name, catalog);
        }

        @Override
        public final List<Table<?>> getTables() {
            return unmodifiableList(tablesPerSchema.get(this));
        }
    }

    private final class InformationSchemaTable extends TableImpl<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4314110578549768267L;

        public InformationSchemaTable(String name, Schema schema) {
            super(name, schema);
        }

        @Override
        public UniqueKey<Record> getPrimaryKey() {
            return super.getPrimaryKey();
        }

        @Override
        public List<UniqueKey<Record>> getKeys() {
            return super.getKeys();
        }

        @Override
        public List<ForeignKey<Record, ?>> getReferences() {
            return super.getReferences();
        }
    }
}
