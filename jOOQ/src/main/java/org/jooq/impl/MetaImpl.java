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
package org.jooq.impl;

// ...
// ...
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.tools.StringUtils.defaultString;

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
import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConnectionCallable;
import org.jooq.Constraint;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Meta;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * An implementation of the public {@link Meta} type.
 * <p>
 * This implementation implements {@link Serializable}, without taking care of
 * properly deserialising the referenced executor.
 *
 * @author Lukas Eder
 */
final class MetaImpl extends AbstractMeta {

    private static final long            serialVersionUID                 = 3582980783173033809L;
    private static final JooqLogger      log                              = JooqLogger.getLogger(MetaImpl.class);
    private static final Set<SQLDialect> INVERSE_SCHEMA_CATALOG           = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> CURRENT_TIMESTAMP_COLUMN_DEFAULT = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> EXPRESSION_COLUMN_DEFAULT        = SQLDialect.supportedBy(H2);





    private final DatabaseMetaData       databaseMetaData;
    private final boolean                inverseSchemaCatalog;

    MetaImpl(Configuration configuration, DatabaseMetaData databaseMetaData) {
        super(configuration);

        this.databaseMetaData = databaseMetaData;
        this.inverseSchemaCatalog = INVERSE_SCHEMA_CATALOG.contains(configuration.family());
    }

    private interface MetaFunction {
        Result<Record> run(DatabaseMetaData meta) throws SQLException;
    }

    private final Result<Record> meta(final MetaFunction consumer) {
        if (databaseMetaData == null)
            return dsl().connectionResult(new ConnectionCallable<Result<Record>>() {
                @Override
                public Result<Record> run(Connection connection) throws SQLException {
                    return consumer.run(connection.getMetaData());
                }
            });
        else
            try {
                return consumer.run(databaseMetaData);
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while running MetaFunction", e);
            }
    }

    @Override
    protected final List<Catalog> getCatalogs0() {
        List<Catalog> result = new ArrayList<>();
























        // There should always be at least one (empty) catalog in a database
        if (result.isEmpty())
            result.add(new MetaCatalog(""));

        return result;
    }

    @Override
    protected final List<Schema> getSchemas0() {
        List<Schema> result = new ArrayList<>();

        for (Catalog catalog : getCatalogs())
            result.addAll(catalog.getSchemas());

        return result;
    }

    @Override
    protected final List<Table<?>> getTables0() {
        List<Table<?>> result = new ArrayList<>();

        for (Schema schema : getSchemas())
            result.addAll(schema.getTables());

        return result;
    }

    @Override
    protected final List<Sequence<?>> getSequences0() {
        List<Sequence<?>> result = new ArrayList<>();

        for (Schema schema : getSchemas())
            result.addAll(schema.getSequences());

        return result;
    }

    @Override
    protected final List<UniqueKey<?>> getPrimaryKeys0() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Table<?> table : getTables()) {
            UniqueKey<?> pk = table.getPrimaryKey();

            if (pk != null)
                result.add(pk);
        }

        return result;
    }

    private final class MetaCatalog extends CatalogImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2821093577201327275L;

        MetaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            List<Schema> result = new ArrayList<>();










            if (!inverseSchemaCatalog) {
                Result<Record> schemas = meta(new MetaFunction() {
                    @Override
                    public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                        return dsl().fetch(
                            meta.getSchemas(),

                            // [#2681] Work around a flaw in the MySQL JDBC driver
                            SQLDataType.VARCHAR // TABLE_SCHEM
                        );
                    }
                });

                for (String name : schemas.getValues(0, String.class))
                    result.add(new MetaSchema(name, MetaCatalog.this));
            }

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            else {
                Result<Record> schemas = meta(new MetaFunction() {
                    @Override
                    public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                        return dsl().fetch(
                            meta.getCatalogs(),
                            SQLDataType.VARCHAR  // TABLE_CATALOG
                        );
                    }
                });

                for (String name : schemas.getValues(0, String.class))
                    result.add(new MetaSchema(name, MetaCatalog.this));
            }

            // There should always be at least one (empty) schema in a database
            if (result.isEmpty())
                result.add(new MetaSchema("", MetaCatalog.this));

            return result;
        }
    }

    private final class MetaSchema extends SchemaImpl {

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










                    if (!inverseSchemaCatalog)
                        rs = meta.getTables(null, getName(), "%", types);

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else
                        rs = meta.getTables(getName(), null, "%", types);

                    return dsl().fetch(
                        rs,

                        // [#2681] Work around a flaw in the MySQL JDBC driver
                        SQLDataType.VARCHAR, // TABLE_CAT
                        SQLDataType.VARCHAR, // TABLE_SCHEM
                        SQLDataType.VARCHAR, // TABLE_NAME
                        SQLDataType.VARCHAR  // TABLE_TYPE
                    );
                }
            });

            List<Table<?>> result = new ArrayList<>(tables.size());
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
            if (columnCache == null && configuration.family() != SQLITE) {
                Result<Record> columns = getColumns0(schema, "%");

                Field<String> tableCat   = (Field<String>) columns.field(0); // TABLE_CAT
                Field<String> tableSchem = (Field<String>) columns.field(1); // TABLE_SCHEM
                Field<String> tableName  = (Field<String>) columns.field(2); // TABLE_NAME

                Map<Record, Result<Record>> groups =
                columns.intoGroups(new Field[] {
                    inverseSchemaCatalog ? tableCat : tableSchem,
                    tableName
                });

                columnCache = new LinkedHashMap<>();

                for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                    Record key = entry.getKey();
                    Result<Record> value = entry.getValue();
                    columnCache.put(name(key.get(inverseSchemaCatalog ? tableCat : tableSchem), key.get(tableName)), value);
                }
            }

            if (columnCache != null)
                return columnCache.get(name(schema, table));
            else
                return getColumns0(schema, table);
        }

        private final Result<Record> getColumns0(final String schema, final String table) {
            return meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs;

                    if (!inverseSchemaCatalog)
                        rs = meta.getColumns(null, schema, table, "%");

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else
                        rs = meta.getColumns(schema, null, table, "%");

                    // Work around a bug in the SQL Server JDBC driver by
                    // coercing data types to the expected types
                    // The bug was reported here:
                    // https://connect.microsoft.com/SQLServer/feedback/details/775425/jdbc-4-0-databasemetadata-getcolumns-returns-a-resultset-whose-resultsetmetadata-is-inconsistent

                    // [#9740] TODO: Make this call lenient with respect to
                    //         column count, filling unavailable columns with
                    //         default values.
                    return rs.getMetaData().getColumnCount() < GET_COLUMNS_EXTENDED.length
                        ? dsl().fetch(rs, GET_COLUMNS_SHORT)
                        : dsl().fetch(rs, GET_COLUMNS_EXTENDED);
                }
            });
        }
    }

    // Columns available from JDBC 3.0+
    private static final Class<?>[] GET_COLUMNS_SHORT = {
        String.class,  // TABLE_CAT
        String.class,  // TABLE_SCHEM
        String.class,  // TABLE_NAME
        String.class,  // COLUMN_NAME
        int.class,     // DATA_TYPE

        String.class,  // TYPE_NAME
        int.class,     // COLUMN_SIZE
        String.class,  // BUFFER_LENGTH
        int.class,     // DECIMAL_DIGITS
        int.class,     // NUM_PREC_RADIX

        int.class,     // NULLABLE
        String.class,  // REMARKS
        String.class,  // COLUMN_DEF
    };

    // Columns available from JDBC 4.0+
    private static final Class<?>[] GET_COLUMNS_EXTENDED = {
        String.class,  // TABLE_CAT
        String.class,  // TABLE_SCHEM
        String.class,  // TABLE_NAME
        String.class,  // COLUMN_NAME
        int.class,     // DATA_TYPE

        String.class,  // TYPE_NAME
        int.class,     // COLUMN_SIZE
        String.class,  // BUFFER_LENGTH
        int.class,     // DECIMAL_DIGITS
        int.class,     // NUM_PREC_RADIX

        int.class,     // NULLABLE
        String.class,  // REMARKS
        String.class,  // COLUMN_DEF
        int.class,     // SQL_DATA_TYPE
        int.class,     // SQL_DATETIME_SUB

        int.class,     // CHAR_OCTET_LENGTH
        int.class,     // ORDINAL_POSITION
        String.class,  // IS_NULLABLE
        String.class,  // SCOPE_CATALOG
        String.class,  // SCOPE_SCHEMA

        String.class,  // SCOPE_TABLE
        String.class,  // SOURCE_DATA_TYPE
        String.class   // IS_AUTOINCREMENT
    };

    private final class MetaTable extends TableImpl<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4843841667753000233L;

        MetaTable(String name, Schema schema, Result<Record> columns) {
            super(name, schema);

            // Possible scenarios for columns being null:
            // - The "table" is in fact a SYNONYM
            if (columns != null)
                init(columns);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Identity<Record, ?> getIdentity() {
            for (Field<?> field : fields())
                if (field.getDataType().identity())
                    return new IdentityImpl<>(this, (TableField<Record, ?>) field);

            return null;
        }

        @Override
        public final List<Index> getIndexes() {
            final String schema = getSchema() == null ? null : getSchema().getName();
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs;

                    if (!inverseSchemaCatalog)
                        rs = meta.getIndexInfo(null, schema, getName(), false, true);

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else
                        rs = meta.getIndexInfo(schema, null, getName(), false, true);

                    return dsl().fetch(
                        rs,
                        String.class,  // TABLE_CAT
                        String.class,  // TABLE_SCHEM
                        String.class,  // TABLE_NAME
                        boolean.class, // NON_UNIQUE
                        String.class,  // INDEX_QUALIFIER
                        String.class,  // INDEX_NAME
                        int.class,     // TYPE
                        int.class,     // ORDINAL_POSITION
                        String.class,  // COLUMN_NAME
                        String.class,  // ASC_OR_DESC
                        long.class,    // CARDINALITY
                        long.class,    // PAGES
                        String.class   // FILTER_CONDITION
                    );
                }
            });

            // Sort by INDEX_NAME (5), ORDINAL_POSITION (7)
            result.sortAsc(7).sortAsc(5);
            return createIndexes(result);
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

                    if (!inverseSchemaCatalog)
                        rs = meta.getPrimaryKeys(null, schema, getName());

                    // [#2760] MySQL JDBC confuses "catalog" and "schema"
                    else
                        rs = meta.getPrimaryKeys(schema, null, getName());

                    return dsl().fetch(
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
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs = meta.getImportedKeys(null, getSchema().getName(), getName());
                    return dsl().fetch(
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

            Map<String, Schema> schemas = new HashMap<>();
            for (Schema schema : getSchemas())
                schemas.put(schema.getName(), schema);

            List<ForeignKey<Record, ?>> references = new ArrayList<>(groups.size());
            for (Entry<Record, Result<Record>> entry : groups.entrySet()) {

                // [#7377] The schema may be null instead of "" in some dialects
                Schema schema = schemas.get(defaultString(entry.getKey().get(1, String.class)));

                String fkName = entry.getKey().get(3, String.class);
                String pkName = entry.getKey().get(4, String.class);
                Table<Record> pkTable = (Table<Record>) schema.getTable(entry.getKey().get(2, String.class));
                TableField<Record, ?>[] pkFields = new TableField[entry.getValue().size()];
                TableField<Record, ?>[] fkFields = new TableField[entry.getValue().size()];

                for (int i = 0; i < entry.getValue().size(); i++) {
                    Record record = entry.getValue().get(i);
                    pkFields[i] = (TableField<Record, ?>) pkTable.field(record.get(3, String.class));
                    fkFields[i] = (TableField<Record, ?>)         field(record.get(7, String.class));
                }

                references.add(new ReferenceImpl<>(new MetaPrimaryKey(pkTable, pkName, pkFields), this, fkName, fkFields, true));
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

        private final List<Index> createIndexes(Result<Record> result) {
            List<Index> indexes = new ArrayList<>();
            List<SortField<?>> sortFields = new ArrayList<>();
            String previousIndexName = null;
            Name name = null;
            Condition where = null;
            boolean unique = false;

            for (int i = 0; i < result.size(); i++) {
                Record record = result.get(i);

                String indexName = record.get(5, String.class); // INDEX_NAME
                if (indexName == null)
                    continue;

                if (!indexName.equals(previousIndexName)) {
                    previousIndexName = indexName;
                    sortFields.clear();

                    name = DSL.name(
                        record.get(0, String.class), // TABLE_CAT
                        record.get(1, String.class), // TABLE_SCHEM
                        indexName
                    );

                    String filter = record.get(12, String.class); // FILTER_CONDITION
                    where = !StringUtils.isBlank(filter) ? condition(filter) : null;
                    unique = !record.get(3, boolean.class); // NON_UNIQUE
                }

                String columnName = record.get(8, String.class);
                Field<?> field = field(columnName); // COLUMN_NAME
                if (field == null)
                    field = DSL.field(columnName);

                boolean desc = "D".equalsIgnoreCase(record.get(9, String.class)); // ASC_OR_DESC
                sortFields.add(desc ? field.desc() : field.asc());

                if (i + 1 == result.size() || !result.get(i + 1).get(5, String.class).equals(previousIndexName))
                    indexes.add(new IndexImpl(name, this, sortFields.toArray(EMPTY_SORTFIELD), where, unique));
            }

            return indexes;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private final void init(Result<Record> columns) {
            for (Record column : columns) {
                String columnName = column.get(3, String.class);         // COLUMN_NAME
                String typeName = column.get(5, String.class);           // TYPE_NAME
                int precision = column.get(6, int.class);                // COLUMN_SIZE
                int scale = column.get(8, int.class);                    // DECIMAL_DIGITS
                int nullable = column.get(10, int.class);                // NULLABLE
                String remarks = column.get(11, String.class);           // REMARKS
                String defaultValue = column.get(12, String.class);      // COLUMN_DEF
                boolean isAutoIncrement = column.size() >= 23
                    ? column.get(22, boolean.class)                      // IS_AUTOINCREMENT
                    : false;

                // TODO: Exception handling should be moved inside SQLDataType
                DataType type = null;
                try {
                    type = DefaultDataType.getDataType(configuration.family(), typeName, precision, scale);

                    // JDBC doesn't distinguish between precision and length
                    type = type.precision(precision, scale);
                    type = type.length(precision);
                    type = type.identity(isAutoIncrement);

                    if (nullable == DatabaseMetaData.columnNoNulls)
                        type = type.nullable(false);

                    // [#6883] Default values may be present
                    if (!isAutoIncrement && !StringUtils.isEmpty(defaultValue)) {
                        try {

                            // [#7194] Some databases report all default values as expressions, not as values
                            if (EXPRESSION_COLUMN_DEFAULT.contains(configuration.family()))
                                type = type.defaultValue(DSL.field(defaultValue, type));

                            // [#5574] MySQL mixes constant value expressions with other column expressions here
                            else if (CURRENT_TIMESTAMP_COLUMN_DEFAULT.contains(configuration.family()) && "CURRENT_TIMESTAMP".equalsIgnoreCase(defaultValue))
                                type = type.defaultValue(DSL.field(defaultValue, type));
                            else
                                type = type.defaultValue(DSL.inline(defaultValue, type));
                        }

                        // [#8469] Rather than catching exceptions after conversions, we should use the
                        //         parser to parse default values, if they're expressions
                        catch (DataTypeException e) {
                            log.warn("Default value", "Could not load default value: " + defaultValue + " for type: " + type, e);
                        }
                    }
                }
                catch (SQLDialectNotSupportedException e) {
                    type = SQLDataType.OTHER;
                }

                createField(columnName, type, this, remarks);
            }
        }
    }

    private final class MetaPrimaryKey extends AbstractNamed implements UniqueKey<Record> {

        /**
         * Generated UID
         */
        private static final long             serialVersionUID = 6997258619475953490L;

        private final Table<Record>           pkTable;
        private final TableField<Record, ?>[] pkFields;

        MetaPrimaryKey(Table<Record> table, String pkName, TableField<Record, ?>[] fields) {
            super(pkName == null ? null : DSL.name(pkName), null);

            this.pkTable = table;
            this.pkFields = fields;
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
        public final boolean enforced() {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final List<ForeignKey<?, Record>> getReferences() {
            Result<Record> result = meta(new MetaFunction() {
                @Override
                public Result<Record> run(DatabaseMetaData meta) throws SQLException {
                    ResultSet rs = meta.getExportedKeys(null, pkTable.getSchema().getName(), pkTable.getName());

                    return dsl().fetch(
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

            Map<String, Schema> schemas = new HashMap<>();
            for (Schema schema : getSchemas())
                schemas.put(schema.getName(), schema);

            List<ForeignKey<?, Record>> references = new ArrayList<>(groups.size());
            for (Entry<Record, Result<Record>> entry : groups.entrySet()) {
                Record key = entry.getKey();
                Result<Record> value = entry.getValue();

                // [#7377] The schema may be null instead of "" in some dialects
                Schema schema = schemas.get(defaultString(key.get(1, String.class)));

                Table<Record> fkTable = (Table<Record>) schema.getTable(key.get(2, String.class));
                String fkName = key.get(3, String.class);
                TableField<Record, ?>[] fkFields = new TableField[value.size()];

                for (int i = 0; i < value.size(); i++)
                    fkFields[i] = (TableField<Record, ?>) fkTable.field(value.get(i).get(7, String.class));

                references.add(new ReferenceImpl<>(this, fkTable, fkName, fkFields, true));
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

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(getUnqualifiedName());
        }
    }

    @Override
    public String toString() {
        // [#9428] Prevent long running toString() calls
        return "MetaImpl";
    }
}
