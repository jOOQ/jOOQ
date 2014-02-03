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
package org.jooq.impl;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.name;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;

/**
 * An implementation of the public {@link Meta} type.
 * <p>
 * This implementation implements {@link Serializable}, without taking care of
 * properly deserialising the referenced executor.
 *
 * @author Lukas Eder
 */
class MetaImpl implements Meta, Serializable {

    /**
     * Generated UID
     */
    private static final long                   serialVersionUID = 3582980783173033809L;
    private static final JooqLogger             log              = JooqLogger.getLogger(MetaImpl.class);

    private final DSLContext                    create;
    private final Configuration                 configuration;
    private transient volatile DatabaseMetaData meta;

    MetaImpl(Configuration configuration) {
        this.create = DSL.using(configuration);
        this.configuration = configuration;
    }

    private final DatabaseMetaData meta() {
        if (meta == null) {
            ConnectionProvider provider = configuration.connectionProvider();
            Connection connection = null;

            try {
                connection = provider.acquire();
                meta = connection.getMetaData();
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
            finally {
                if (connection != null) {
                    provider.release(connection);
                }
            }
        }

        return meta;
    }

    @Override
    public final List<Catalog> getCatalogs() {
        try {
            List<Catalog> result = new ArrayList<Catalog>();

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            if (!asList(MYSQL, MARIADB).contains(configuration.dialect().family())) {
                Result<Record> catalogs = create.fetch(
                    meta().getCatalogs(),
                    SQLDataType.VARCHAR // TABLE_CATALOG
                );

                for (String name : catalogs.getValues(0, String.class)) {
                    result.add(new MetaCatalog(name));
                }
            }

            // There should always be at least one (empty) catalog in a database
            if (result.isEmpty()) {
                result.add(new MetaCatalog(""));
            }

            return result;
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while accessing DatabaseMetaData", e);
        }
    }

    @Override
    public final List<Schema> getSchemas() {
        List<Schema> result = new ArrayList<Schema>();

        for (Catalog catalog : getCatalogs()) {
            result.addAll(catalog.getSchemas());
        }

        return result;
    }

    @Override
    public final List<Table<?>> getTables() {
        List<Table<?>> result = new ArrayList<Table<?>>();

        for (Schema schema : getSchemas()) {
            result.addAll(schema.getTables());
        }

        return result;
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        List<UniqueKey<?>> result = new ArrayList<UniqueKey<?>>();

        for (Table<?> table : getTables()) {
            UniqueKey<?> pk = table.getPrimaryKey();

            if (pk != null) {
                result.add(pk);
            }
        }

        return result;
    }

    private class MetaCatalog extends CatalogImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2821093577201327275L;

        MetaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            try {
                List<Schema> result = new ArrayList<Schema>();

                /* [pro] xx
                xx xxxxxxxxxxx xxxxxxxxx xxxx xxx xxxxxxx xxxxxxx xxxxxxx xxxx xxxxxx
                xx xx xxxxx xxx xxx xx xxxxxx
                xx xxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                x

                xxxx
                xx [/pro] */

                if (!asList(MYSQL, MARIADB).contains(configuration.dialect().family())) {
                    Result<Record> schemas = create.fetch(
                        meta().getSchemas(),

                        // [#2681] Work around a flaw in the MySQL JDBC driver
                        SQLDataType.VARCHAR // TABLE_SCHEM
                    );

                    for (String name : schemas.getValues(0, String.class)) {
                        result.add(new MetaSchema(name));
                    }
                }

                // [#2760] MySQL JDBC confuses "catalog" and "schema"
                else {
                    Result<Record> schemas = create.fetch(
                        meta().getCatalogs(),
                        SQLDataType.VARCHAR  // TABLE_CATALOG
                    );

                    for (String name : schemas.getValues(0, String.class)) {
                        result.add(new MetaSchema(name));
                    }
                }

                // There should always be at least one (empty) schema in a database
                if (result.isEmpty()) {
                    result.add(new MetaSchema(""));
                }

                return result;
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }
    }

    private class MetaSchema extends SchemaImpl {

        /**
         * Generated UID
         */
        private static final long                            serialVersionUID = -2621899850912554198L;

        private transient volatile List<Table<?>>            tableCache;
        private transient volatile Map<Name, Result<Record>> columnCache;

        MetaSchema(String name) {
            super(name);
        }

        @Override
        public final synchronized List<Table<?>> getTables() {
            if (tableCache != null) {
                return tableCache;
            }

            try {
                String[] types = null;

                switch (configuration.dialect().family()) {

                    // [#2323] SQLite JDBC drivers have a bug. They return other
                    // object types, too: https://bitbucket.org/xerial/sqlite-jdbc/issue/68
                    case SQLITE:
                        types = new String[] { "TABLE", "VIEW" };
                        break;

                    /* [pro] xx
                    xx xxxxxxx xxxxx xxxxxxxxx xxxxxx xxxxx xxxxxxxxx
                    xx xxxxx xxxxxxxxxxxxx xxxxx xx xxx xxxxxxxxx xx xxxx xxx xxxx
                    xx xxxxxxxx xx xxxxxxx xx xxxxxx xxxx
                    xxxx xxxxxxx
                        xxxxx x xxx xxxxxxxx x xxxxxxxx xxxxxx xx
                        xxxxxx
                    xx [/pro] */
                }

                ResultSet rs;

                /* [pro] xx
                xx xxxxxxxx xxx xxxxxxxxx xxxxxx xxxxxxx xxx xxxxxxxx xxxx xx xxx xx xxxxxx
                xx xxxxxxxx xxxx xxxxxxxxxxx xxxxxx xx xxxxxx xx xxx xxxxxxxxxxx xxxxxx
                xx xxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xx x xxxxxxxxxxxxxxxxxxxxxx xxxxx xxxx xxxxxxx
                x
                xxxx

                xx [/pro] */

                if (!asList(MYSQL, MARIADB).contains(configuration.dialect().family())) {
                    rs = meta().getTables(null, getName(), "%", types);
                }

                // [#2760] MySQL JDBC confuses "catalog" and "schema"
                else {
                    rs = meta().getTables(getName(), null, "%", types);
                }

                List<Table<?>> result = new ArrayList<Table<?>>();
                Result<Record> tables = create.fetch(
                    rs,

                    // [#2681] Work around a flaw in the MySQL JDBC driver
                    SQLDataType.VARCHAR, // TABLE_CAT
                    SQLDataType.VARCHAR, // TABLE_SCHEM
                    SQLDataType.VARCHAR, // TABLE_NAME
                    SQLDataType.VARCHAR  // TABLE_TYPE
                );

                for (Record table : tables) {
//                  String catalog = table.getValue(0, String.class);
                    String schema = table.getValue(1, String.class);
                    String name = table.getValue(2, String.class);

                    result.add(new MetaTable(name, this, getColumns(schema, name)));

//                  TODO: Find a more efficient way to do this
//                  Result<Record> pkColumns = executor.fetch(meta().getPrimaryKeys(catalog, schema, name))
//                                                     .sortAsc("KEY_SEQ");
//
//                  result.add(new MetaTable(name, this, columnCache.get(name)));
                }

                return result;
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }

        @SuppressWarnings("unchecked")
        private final Result<Record> getColumns(String schema, String table) throws SQLException {

            // SQLite JDBC's DatabaseMetaData.getColumns() can only return a single
            // table's columns
            if (columnCache == null && configuration.dialect() != SQLITE) {
                Result<Record> columns = getColumns0(schema, "%");

                Field<String> tableSchem = (Field<String>) columns.field(1); // TABLE_SCHEM
                Field<String> tableName = (Field<String>) columns.field(2);  // TABLE_NAME

                Map<Record, Result<Record>> groups =
                columns.intoGroups(new Field[] {
                    tableSchem,
                    tableName
                });

                columnCache = new LinkedHashMap<Name, Result<Record>>();

                for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                    Record key = entry.getKey();
                    Result<Record> value = entry.getValue();
                    columnCache.put(name(key.getValue(tableSchem), key.getValue(tableName)), value);
                }
            }

            if (columnCache != null) {
                return columnCache.get(name(schema, table));
            }
            else {
                return getColumns0(schema, table);
            }
        }

        private final Result<Record> getColumns0(String schema, String table) throws SQLException {
            ResultSet rs;
            if (!asList(MYSQL, MARIADB).contains(configuration.dialect().family())) {
                rs = meta().getColumns(null, schema, table, "%");
            }

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            else {
                rs = meta().getColumns(schema, null, table, "%");
            }

            return create.fetch(
                rs,

                // Work around a bug in the SQL Server JDBC driver by
                // coercing data types to the expected types
                // The bug was reported here:
                // https://connect.microsoft.com/SQLServer/feedback/details/775425/jdbc-4-0-databasemetadata-getcolumns-returns-a-resultset-whose-resultsetmetadata-is-inconsistent
                String.class, // TABLE_CAT
                String.class, // TABLE_SCHEM
                String.class, // TABLE_NAME
                String.class, // COLUMN_NAME
                int.class,    // DATA_TYPE
                String.class, // TYPE_NAME
                int.class,    // COLUMN_SIZE
                String.class, // BUFFER_LENGTH
                int.class     // DECIMAL_DIGITS
            );
        }
    }

    private class MetaTable extends TableImpl<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4843841667753000233L;

        MetaTable(String name, Schema schema, Result<Record> columns) {
            super(name, schema);

            // Possible scenarios for columns being null:
            // - The "table" is in fact a SYNONYM
            if (columns != null) {
                init(columns);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public final List<UniqueKey<Record>> getKeys() {
            return unmodifiableList(asList(getPrimaryKey()));
        }

        @Override
        public final UniqueKey<Record> getPrimaryKey() {
            SQLDialect family = configuration.dialect().family();

            /* [pro] xx
            xx xxxxxxx xx xxxxxxx x
                xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
            x
            xx [/pro] */

            String schema = getSchema() == null ? null : getSchema().getName();
            try {
                ResultSet rs;

                if (!asList(MYSQL, MARIADB).contains(family)) {
                    rs = meta().getPrimaryKeys(null, schema, getName());
                }

                // [#2760] MySQL JDBC confuses "catalog" and "schema"
                else {
                    rs = meta().getPrimaryKeys(schema, null, getName());
                }

                Result<Record> result =
                create.fetch(
                    rs,
                    String.class, // TABLE_CAT
                    String.class, // TABLE_SCHEM
                    String.class, // TABLE_NAME
                    String.class, // COLUMN_NAME
                    int.class,    // KEY_SEQ
                    String.class  // PK_NAME
                );

                // Sort by KEY_SEQ
                result.sortAsc(4);
                return createPrimaryKey(result, 3);
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }

        /* [pro] xx
        xxxxxxx xxxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxx x

            xx xxx xxxxxxxxx xxxxxx xxxx xxx xxxxxxxxx xxxxxx xxxxxxx xxxx xxx xx xxxxxxx
            xx xxxxxx xxxx xxx xxxxxxxxx xxxxxxx xxx xxxx xxxxx xxxx xxxx xxxxx xxxxxx
            xxx x
                xxxxxxxxx xx x xxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxx xxxxxx
                xxxxxxxxxxxxxx xxxxxx x
                xxxxxxxxxxxxx
                    xxx
                    xxxxxxxxxxxxx  xx xxxxxxxxx
                    xxxxxxxxxxxxx  xx xxxxxxxxxxx
                    xxxxxxxxxxxxx  xx xxxxxxxxxx
                    xxxxxxxxxxxxxx xx xxxxxxxxxx
                    xxxxxxxxxxxxx  xx xxxxxxxxxxxxxxx
                    xxxxxxxxxxxxx  xx xxxxxxxxxx
                    xxxxxxxxxxxxx  xx xxxx
                    xxxxxxxxxxxxx  xx xxxxxxxxxxxxxxxx
                    xxxxxxxxxxxx   xx xxxxxxxxxxx
                xx

                xx xxxxxx xxxx xxxxxx xxxxxxxx xxxx xxxxxxxxxx x xxxxx
                xxxxxxxxxxxxxxxx xx x xxxxxxxxxxxxxxxxxx
                xxxxx xxxxxxxxxxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxx
                        xxxxxxxxxxxx

                xx xxxxxx xxxx xxx xxxxx xxxxxx xxxxxx xxxx xxxxx xx xxxxxxxxxx
                xx xxxxxxxxxxxxxxxxxxxxx
                    xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                xx xxxx xx xxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxx

                xx xxxx xxx xxxxxxxxxxx
                xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxx
            x
            xxxxx xxxxxxxxxxxxx xx x
                xxxxxxxxxxxxxxx xxxxxx xxxxxxx xxxxxxxxxxxxxxxx
            x

            xxxxxx xxxxx
        x
        xx [/pro] */

        @SuppressWarnings("unchecked")
        private final UniqueKey<Record> createPrimaryKey(Result<Record> result, int columnName) {
            if (result.size() > 0) {
                TableField<Record, ?>[] fields = new TableField[result.size()];
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = (TableField<Record, ?>) field(result.get(i).getValue(columnName, String.class));
                }

                return AbstractKeys.createUniqueKey(this, fields);
            }
            else {
                return null;
            }
        }

        private final void init(Result<Record> columns) {
            for (Record column : columns) {
                String columnName = column.getValue(3, String.class); // COLUMN_NAME
                String typeName = column.getValue(5, String.class);   // TYPE_NAME
                int precision = column.getValue(6, int.class);        // COLUMN_SIZE
                int scale = column.getValue(8, int.class);            // DECIMAL_DIGITS

                // TODO: Exception handling should be moved inside SQLDataType
                DataType<?> type = null;
                try {
                    type = DefaultDataType.getDataType(configuration.dialect(), typeName, precision, scale);

                    // JDBC doesn't distinguish between precision and length
                    type = type.precision(precision, scale);
                    type = type.length(precision);
                }
                catch (SQLDialectNotSupportedException e) {
                    type = SQLDataType.OTHER;
                }

                createField(columnName, type, this);
            }
        }
    }
}
