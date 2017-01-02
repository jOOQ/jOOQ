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
package org.jooq.impl;

import static java.util.Arrays.asList;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.ConnectionCallable;
import org.jooq.Constraint;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * An implementation of the public {@link Meta} type.
 * <p>
 * This implementation implements {@link Serializable}, without taking care of
 * properly deserialising the referenced executor.
 *
 * @author Lukas Eder
 */
final class MetaImpl implements Meta, Serializable {

    /**
     * Generated UID
     */
    private static final long                   serialVersionUID = 3582980783173033809L;

    private final DSLContext                    ctx;
    private final Configuration                 configuration;
    private final boolean                       inverseSchemaCatalog;

    MetaImpl(Configuration configuration) {
        this.ctx = DSL.using(configuration);
        this.configuration = configuration;
        this.inverseSchemaCatalog = asList(MYSQL, MARIADB).contains(configuration.family());
    }

    private interface MetaFunction {
        Result<Record> run(DatabaseMetaData meta) throws SQLException;
    }

    private final Result<Record> meta(final MetaFunction consumer) {
        return ctx.connectionResult(new ConnectionCallable<Result<Record>>() {
            @Override
            public Result<Record> run(Connection connection) throws SQLException {
                return consumer.run(connection.getMetaData());
            }
        });
    }

    @Override
    public final List<Catalog> getCatalogs() {
        List<Catalog> result = new ArrayList<Catalog>();

        // [#2760] MySQL JDBC confuses "catalog" and "schema"
        if (!inverseSchemaCatalog) {
            Result<Record> catalogs = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    return ctx.fetch(
                        meta.getCatalogs(),
                        SQLDataType.VARCHAR // TABLE_CATALOG
                    );
                }
            });

            for (String name : catalogs.getValues(0, String.class))
                result.add(new MetaCatalog(name));
        }

        // There should always be at least one (empty) catalog in a database
        if (result.isEmpty())
            result.add(new MetaCatalog(""));

        return result;
    }

    @Override
    public final List<Schema> getSchemas() {
        List<Schema> result = new ArrayList<Schema>();

        for (Catalog catalog : getCatalogs())
            result.addAll(catalog.getSchemas());

        return result;
    }

    @Override
    public final List<Table<?>> getTables() {
        List<Table<?>> result = new ArrayList<Table<?>>();

        for (Schema schema : getSchemas())
            result.addAll(schema.getTables());

        return result;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        List<Sequence<?>> result = new ArrayList<Sequence<?>>();

        for (Schema schema : getSchemas())
            result.addAll(schema.getSequences());

        return result;
    }

    @Override
    public final List<UniqueKey<?>> getPrimaryKeys() {
        List<UniqueKey<?>> result = new ArrayList<UniqueKey<?>>();

        for (Table<?> table : getTables()) {
            UniqueKey<?> pk = table.getPrimaryKey();

            if (pk != null)
                result.add(pk);
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
            List<Schema> result = new ArrayList<Schema>();










            if (!inverseSchemaCatalog) {
                Result<Record> schemas = meta(new MetaFunction() {
                    @Override
                    public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                        return ctx.fetch(
                            meta.getSchemas(),

                            // [#2681] Work around a flaw in the MySQL JDBC driver
                            SQLDataType.VARCHAR // TABLE_SCHEM
                        );
                    }
                });


                for (String name : schemas.getValues(0, String.class)) {
                    result.add(new MetaSchema(name, MetaCatalog.this));
                }
            }

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            else {
                Result<Record> schemas = meta(new MetaFunction() {
                    @Override
                    public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                        return ctx.fetch(
                            meta.getCatalogs(),
                            SQLDataType.VARCHAR  // TABLE_CATALOG
                        );
                    }
                });

                for (String name : schemas.getValues(0, String.class)) {
                    result.add(new MetaSchema(name, MetaCatalog.this));
                }
            }

            // There should always be at least one (empty) schema in a database
            if (result.isEmpty()) {
                result.add(new MetaSchema("", MetaCatalog.this));
            }

            return result;
        }
    }

    private class MetaSchema extends SchemaImpl {

        /**
         * Generated UID
         */
        private static final long                            serialVersionUID = -2621899850912554198L;
        private transient volatile Map<Name, Result<Record>> columnCache;

        MetaSchema(String name, Catalog catalog) {
            super(name, catalog);
        }

        @Override
        public final synchronized List<Table<?>> getTables() {
            Result<Record> tables = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    String[] types = null;

                    switch (configuration.family()) {

                        // [#3977] PostgreSQL returns other object types, too
                        case POSTGRES:
                            types = new String[] { "TABLE", "VIEW", "SYSTEM_TABLE", "SYSTEM_VIEW", "MATERIALIZED VIEW" };
                            break;

                        // [#2323] SQLite JDBC drivers have a bug. They return other
                        // object types, too: https://bitbucket.org/xerial/sqlite-jdbc/issue/68
                        case SQLITE:
                            types = new String[] { "TABLE", "VIEW" };
                            break;









                    }

                    ResultSet rs;











                    if (!inverseSchemaCatalog) {
                        rs = meta.getTables(null, getName(), "%", types);
                    }

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else {
                        rs = meta.getTables(getName(), null, "%", types);
                    }

                    return ctx.fetch(
                        rs,

                        // [#2681] Work around a flaw in the MySQL JDBC driver
                        SQLDataType.VARCHAR, // TABLE_CAT
                        SQLDataType.VARCHAR, // TABLE_SCHEM
                        SQLDataType.VARCHAR, // TABLE_NAME
                        SQLDataType.VARCHAR  // TABLE_TYPE
                    );
                }
            });

            List<Table<?>> result = new ArrayList<Table<?>>();
            for (Record table : tables) {
                String catalog = table.get(0, String.class);
                String schema = table.get(1, String.class);
                String name = table.get(2, String.class);

                // [#2760] MySQL JDBC confuses "catalog" and "schema"
                result.add(new MetaTable(name, this, getColumns(
                    inverseSchemaCatalog ? catalog : schema,
                    name
                )));

//              TODO: Find a more efficient way to do this
//              Result<Record> pkColumns = executor.fetch(meta().getPrimaryKeys(catalog, schema, name))
//                                                 .sortAsc("KEY_SEQ");
//
//              result.add(new MetaTable(name, this, columnCache.get(name)));
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        private final Result<Record> getColumns(String schema, String table) {

            // SQLite JDBC's DatabaseMetaData.getColumns() can only return a single
            // table's columns
            if (columnCache == null && configuration.dialect() != SQLITE) {
                Result<Record> columns = getColumns0(schema, "%");

                Field<String> tableCat   = (Field<String>) columns.field(0); // TABLE_CAT
                Field<String> tableSchem = (Field<String>) columns.field(1); // TABLE_SCHEM
                Field<String> tableName  = (Field<String>) columns.field(2); // TABLE_NAME

                Map<Record, Result<Record>> groups =
                columns.intoGroups(new Field[] {
                    inverseSchemaCatalog ? tableCat : tableSchem,
                    tableName
                });

                columnCache = new LinkedHashMap<Name, Result<Record>>();

                for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                    Record key = entry.getKey();
                    Result<Record> value = entry.getValue();
                    columnCache.put(name(key.get(inverseSchemaCatalog ? tableCat : tableSchem), key.get(tableName)), value);
                }
            }

            if (columnCache != null) {
                return columnCache.get(name(schema, table));
            }
            else {
                return getColumns0(schema, table);
            }
        }

        private final Result<Record> getColumns0(final String schema, final String table) {
            return meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs;
                    if (!inverseSchemaCatalog) {
                        rs = meta.getColumns(null, schema, table, "%");
                    }

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else {
                        rs = meta.getColumns(schema, null, table, "%");
                    }

                    return ctx.fetch(
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
                        int.class,    // DECIMAL_DIGITS
                        int.class,    // NUM_PREC_RADIX
                        int.class     // NULLABLE
                    );
                }
            });
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

        @Override
        public final List<UniqueKey<Record>> getKeys() {
            UniqueKey<Record> pk = getPrimaryKey();
            return pk == null ? Collections.<UniqueKey<Record>>emptyList() : Collections.<UniqueKey<Record>>singletonList(pk);
        }

        @Override
        public final UniqueKey<Record> getPrimaryKey() {
            SQLDialect family = configuration.family();







            final String schema = getSchema() == null ? null : getSchema().getName();
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs;

                    if (!inverseSchemaCatalog) {
                        rs = meta.getPrimaryKeys(null, schema, getName());
                    }

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else {
                        rs = meta.getPrimaryKeys(schema, null, getName());
                    }

                    return
                    ctx.fetch(
                        rs,
                        String.class, // TABLE_CAT
                        String.class, // TABLE_SCHEM
                        String.class, // TABLE_NAME
                        String.class, // COLUMN_NAME
                        int.class,    // KEY_SEQ
                        String.class  // PK_NAME
                    );
                }
            });

            // Sort by KEY_SEQ
            result.sortAsc(4);
            return createPrimaryKey(result, 3);
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<ForeignKey<Record, ?>> getReferences() {
            List<ForeignKey<Record, ?>> references = new ArrayList<ForeignKey<Record, ?>>();
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs = meta.getImportedKeys(null, getSchema().getName(), getName());
                    return
                    ctx.fetch(
                        rs,
                        String.class,  // PKTABLE_CAT
                        String.class,  // PKTABLE_SCHEM
                        String.class,  // PKTABLE_NAME
                        String.class,  // PKCOLUMN_NAME
                        String.class,  // FKTABLE_CAT

                        String.class,  // FKTABLE_SCHEM
                        String.class,  // FKTABLE_NAME
                        String.class,  // FKCOLUMN_NAME
                        Short.class,   // KEY_SEQ
                        Short.class,   // UPDATE_RULE

                        Short.class,   // DELETE_RULE
                        String.class,  // FK_NAME
                        String.class   // PK_NAME
                    );
                }
            });

            Map<Record, Result<Record>> groups = result.intoGroups(new Field[] {
                result.field(inverseSchemaCatalog ? 1 : 0),
                result.field(inverseSchemaCatalog ? 0 : 1),
                result.field(2),
                result.field(11),
                result.field(12),
            });

            Map<String, Schema> schemas = new HashMap<String, Schema>();
            for (Schema schema : getSchemas()) {
                schemas.put(schema.getName(), schema);
            }

            for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                Schema schema = schemas.get(entry.getKey().get(1));

                String pkName = entry.getKey().get(4, String.class);
                Table<Record> pkTable = (Table<Record>) schema.getTable(entry.getKey().get(2, String.class));
                TableField<Record, ?>[] pkFields = new TableField[entry.getValue().size()];
                TableField<Record, ?>[] fkFields = new TableField[entry.getValue().size()];

                for (int i = 0; i < entry.getValue().size(); i++) {
                    Record record = entry.getValue().get(i);
                    pkFields[i] = (TableField<Record, ?>) pkTable.field(record.get(3, String.class));
                    fkFields[i] = (TableField<Record, ?>)         field(record.get(7, String.class));
                }

                references.add(new ReferenceImpl<Record, Record>(new MetaPrimaryKey(pkTable, pkName, pkFields), this, fkFields));
            }

            return references;
        }












































        @SuppressWarnings("unchecked")
        private final UniqueKey<Record> createPrimaryKey(Result<Record> result, int columnName) {
            if (result.size() > 0) {
                TableField<Record, ?>[] f = new TableField[result.size()];

                for (int i = 0; i < f.length; i++) {
                    String name = result.get(i).get(columnName, String.class);
                    f[i] = (TableField<Record, ?>) field(name);

                    // [#5097] Work around a bug in the Xerial JDBC driver for SQLite
                    if (f[i] == null && configuration.family() == SQLITE)

                        // [#2656] Use native support for case-insensitive column
                        //         lookup, once this is implemented
                        for (Field<?> field : fields())
                            if (field.getName().equalsIgnoreCase(name))
                                f[i] = (TableField<Record, ?>) field;
                }

                String indexName = result.get(0).get(5, String.class);
                return new MetaPrimaryKey(this, indexName, f);
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
                int nullable = column.getValue(10, int.class);        // NULLABLE

                // TODO: Exception handling should be moved inside SQLDataType
                DataType<?> type = null;
                try {
                    type = DefaultDataType.getDataType(configuration.family(), typeName, precision, scale);

                    // JDBC doesn't distinguish between precision and length
                    type = type.precision(precision, scale);
                    type = type.length(precision);

                    if (nullable == DatabaseMetaData.columnNoNulls)
                        type = type.nullable(false);
                }
                catch (SQLDialectNotSupportedException e) {
                    type = SQLDataType.OTHER;
                }

                createField(columnName, type, this);
            }
        }
    }

    private class MetaPrimaryKey implements UniqueKey<Record> {

        /**
         * Generated UID
         */
        private static final long             serialVersionUID = 6997258619475953490L;

        private final String                  pkName;
        private final Table<Record>           pkTable;
        private final TableField<Record, ?>[] pkFields;

        MetaPrimaryKey(Table<Record> table, String pkName, TableField<Record, ?>[] fields) {
            this.pkName = pkName;
            this.pkTable = table;
            this.pkFields = fields;
        }

        @Override
        public final String getName() {
            return pkName;
        }

        @Override
        public final Table<Record> getTable() {
            return pkTable;
        }

        @Override
        public final List<TableField<Record, ?>> getFields() {
            return Collections.unmodifiableList(Arrays.asList(pkFields));
        }

        @Override
        public final TableField<Record, ?>[] getFieldsArray() {
            return pkFields.clone();
        }

        @Override
        public final boolean isPrimary() {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final List<ForeignKey<?, Record>> getReferences() {
            List<ForeignKey<?, Record>> references = new ArrayList<ForeignKey<?, Record>>();
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs = meta.getExportedKeys(null, pkTable.getSchema().getName(), pkTable.getName());

                    return ctx.fetch(
                        rs,
                        String.class,  // PKTABLE_CAT
                        String.class,  // PKTABLE_SCHEM
                        String.class,  // PKTABLE_NAME
                        String.class,  // PKCOLUMN_NAME
                        String.class,  // FKTABLE_CAT

                        String.class,  // FKTABLE_SCHEM
                        String.class,  // FKTABLE_NAME
                        String.class,  // FKCOLUMN_NAME
                        Short.class,   // KEY_SEQ
                        Short.class,   // UPDATE_RULE

                        Short.class,   // DELETE_RULE
                        String.class,  // FK_NAME
                        String.class   // PK_NAME
                    );
                }
            });

            Map<Record, Result<Record>> groups = result.intoGroups(new Field[] {
                result.field(inverseSchemaCatalog ? 5 : 4),
                result.field(inverseSchemaCatalog ? 4 : 5),
                result.field(6),
                result.field(11),
                result.field(12),
            });

            Map<String, Schema> schemas = new HashMap<String, Schema>();
            for (Schema schema : getSchemas()) {
                schemas.put(schema.getName(), schema);
            }

            for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                Schema schema = schemas.get(entry.getKey().get(1));

                Table<Record> fkTable = (Table<Record>) schema.getTable(entry.getKey().get(2, String.class));
                TableField<Record, ?>[] fkFields = new TableField[entry.getValue().size()];

                for (int i = 0; i < entry.getValue().size(); i++) {
                    Record record = entry.getValue().get(i);
                    fkFields[i] = (TableField<Record, ?>) fkTable.field(record.get(7, String.class));
                }

                references.add(new ReferenceImpl<Record, Record>(this, fkTable, fkFields));
            }

            return references;
        }

        @Override
        public final Constraint constraint() {
            if (isPrimary())
                return DSL.constraint(getName()).primaryKey(getFieldsArray());
            else
                return DSL.constraint(getName()).unique(getFieldsArray());
        }
    }
}
