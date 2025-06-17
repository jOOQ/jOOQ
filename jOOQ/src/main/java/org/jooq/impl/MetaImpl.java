/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.TableOptions.TableType.MATERIALIZED_VIEW;
import static org.jooq.TableOptions.TableType.TABLE;
import static org.jooq.TableOptions.TableType.VIEW;
import static org.jooq.impl.AbstractNamed.findIgnoreCase;
import static org.jooq.impl.DSL.comment;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.MetaSQL.M_COMMENTS;
import static org.jooq.impl.MetaSQL.M_SEQUENCES;
import static org.jooq.impl.MetaSQL.M_SEQUENCES_INCLUDING_SYSTEM_SEQUENCES;
import static org.jooq.impl.MetaSQL.M_SOURCES;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.SMALLINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.EMPTY_OBJECT;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.flatMap;
import static org.jooq.impl.Tools.map;
import static org.jooq.tools.StringUtils.defaultIfEmpty;
import static org.jooq.tools.StringUtils.defaultString;
import static org.jooq.tools.StringUtils.isEmpty;
import static org.jooq.tools.jdbc.JDBCUtils.foreignKeyRule;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.jooq.Catalog;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.InverseForeignKey;
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
import org.jooq.TableOptions;
import org.jooq.TableOptions.TableType;
// ...
// ...
// ...
// ...
import org.jooq.UniqueKey;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.QOM.ForeignKeyRule;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;

import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the public {@link Meta} type.
 * <p>
 * This implementation implements {@link Serializable}, without taking care of
 * properly deserialising the referenced executor.
 *
 * @author Lukas Eder
 */
final class MetaImpl extends AbstractMeta {

    private static final JooqLogger      log                              = JooqLogger.getLogger(MetaImpl.class);
    private static final Set<SQLDialect> INVERSE_SCHEMA_CATALOG           = SQLDialect.supportedBy(CLICKHOUSE, MARIADB, MYSQL);
    private static final Set<SQLDialect> CURRENT_TIMESTAMP_COLUMN_DEFAULT = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> EXPRESSION_COLUMN_DEFAULT        = SQLDialect.supportedBy(CLICKHOUSE, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, POSTGRES, SQLITE, YUGABYTEDB);
    private static final Set<SQLDialect> NO_SUPPORT_SCHEMAS               = SQLDialect.supportedBy(FIREBIRD, SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_INDEXES               = SQLDialect.supportedBy(TRINO);
    private static final Set<SQLDialect> SUPPORTS_CATALOGS                = SQLDialect.supportedBy(DUCKDB);
    private static final Set<SQLDialect> RTRIM_META_SQL_STRINGS           = SQLDialect.supportedBy(FIREBIRD);





    private static final Pattern         P_DUCKDB_IDENTITY_DEFAULT        = Pattern.compile("^(?i:nextval\\s*\\(.*\\)).*$");
    private static final Pattern         P_SYSINDEX_DERBY                 = Pattern.compile("^(?i:SQL\\d{10,}).*$");
    private static final Pattern         P_SYSINDEX_H2                    = Pattern.compile("^(?i:PRIMARY_KEY_|UK_INDEX_|FK_INDEX_|CONSTRAINT(_\\w+)?_INDEX_).*$");
    private static final Pattern         P_SYSINDEX_HSQLDB                = Pattern.compile("^(?i:SYS_IDX_).*$");
    private static final Pattern         P_SYSINDEX_SQLITE                = Pattern.compile("^(?i:sqlite_autoindex_).*$");

    private final DatabaseMetaData       databaseMetaData;
    private final boolean                inverseSchemaCatalog;

    MetaImpl(Configuration configuration, DatabaseMetaData databaseMetaData) {
        this(configuration, databaseMetaData, null, null);
    }

    private MetaImpl(
        Configuration configuration,
        DatabaseMetaData databaseMetaData,
        Predicate<? super Catalog> catalogFilter,
        Predicate<? super Schema> schemaFilter
    ) {
        super(configuration, catalogFilter, schemaFilter);

        this.databaseMetaData = databaseMetaData;
        this.inverseSchemaCatalog = INVERSE_SCHEMA_CATALOG.contains(dialect());
    }

    @Override
    final AbstractMeta filtered0(Predicate<? super Catalog> catalogFilter, Predicate<? super Schema> schemaFilter) {
        return new MetaImpl(
            configuration(),
            databaseMetaData,
            FilteredMeta.and(this.catalogFilter, catalogFilter),
            FilteredMeta.and(this.schemaFilter, schemaFilter)
        );
    }

    final boolean hasCatalog(Catalog catalog) {
        return catalog != null && !isEmpty(catalog.getName());
    }

    final <R> R catalogSchema(Catalog catalog, Schema schema, ThrowingBiFunction<String, String, R, SQLException> function) throws SQLException {
        return catalogSchema(
            catalog != null ? catalog.getName() : null,
            schema != null ? schema.getName() : null,
            function
        );
    }

    final <R> R catalogSchema(String catalog, String schema, ThrowingBiFunction<String, String, R, SQLException> function) throws SQLException {
        String c = defaultIfEmpty(catalog, null);
        String s = defaultIfEmpty(schema, null);

        // [#2760] MySQL JDBC confuses "catalog" and "schema"
        if (inverseSchemaCatalog)
            return function.apply(s, c);
        else
            return function.apply(c, s);
    }

    private final <R> R meta(
        Supplier<String> errorMessage,
        ThrowingFunction<DatabaseMetaData, R, SQLException> function
    ) {
        if (databaseMetaData == null)
            return dsl().connectionResult(connection -> meta0(errorMessage, function, connection.getMetaData()));
        else
            return meta0(errorMessage, function, databaseMetaData);
    }

    private static final <R> R meta0(
        Supplier<String> errorMessage,
        ThrowingFunction<DatabaseMetaData, R, SQLException> function,
        DatabaseMetaData databaseMetaData
    ) {
        try {
            return function.apply(databaseMetaData);
        }
        catch (SQLException e) {
            String m = errorMessage.get();

            if (StringUtils.isEmpty(m))
                throw new DataAccessException("Error querying DatabaseMetaData", e);
            else
                throw new DataAccessException("Error querying DatabaseMetaData: " + m, e);
        }
    }

    private static final <T, E extends Exception> T withCatalog(Catalog catalog, DSLContext ctx, ThrowingFunction<DSLContext, T, E> supplier) throws E {
        String previous = null;
        Exception e = null;

        try {














            return supplier.apply(ctx);
        }
        catch (Exception x) {
            e = x;
            throw (E) x;
        }
        finally {













        }
    }

    @Override
    final List<Catalog> getCatalogs0() {
        List<Catalog> result = new ArrayList<>();

        // [#7714] If the database system doesn't really support qualifying objects by their
        //         catalog, then we should not return any catalogs here.
        if (SUPPORTS_CATALOGS.contains(dialect())) {

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            if (!inverseSchemaCatalog) {
                Result<Record> catalogs = meta(() -> "Error while fetching catalogs", meta -> dsl().fetch(
                    meta.getCatalogs(),
                    SQLDataType.VARCHAR // TABLE_CATALOG
                ));

                for (String name : catalogs.getValues(0, String.class)) {
                    MetaCatalog c = new MetaCatalog(name);

                    if (catalogFilter == null || catalogFilter.test(c))
                        result.add(c);
                }
            }
        }

        // There should always be at least one (empty) catalog in a database
        if (result.isEmpty())
            result.add(new MetaCatalog(""));

        return result;
    }

    final Table<?> lookupTable(Schema schema, String tableName) {
        switch (family()) {

            // [#10741] A workaround for SQLite's case insensitivity where the
            //          case of declared vs referenced identifiers may differ
            // [#2656]  TODO: Solve this more thoroughly and globally
            case SQLITE:
                return findIgnoreCase(tableName, schema.getTables());

            default:
                return schema.getTable(tableName);
        }
    }

    @Override
    final List<Schema> getSchemas0() {
        return flatMap(getCatalogs(), c -> c.getSchemas());
    }

    @Override
    final List<Table<?>> getTables0() {
        return flatMap(getSchemas(), s -> s.getTables());
    }

    @Override
    final List<UniqueKey<?>> getPrimaryKeys0() {
        List<UniqueKey<?>> result = new ArrayList<>();

        for (Table<?> table : getTables()) {
            UniqueKey<?> pk = table.getPrimaryKey();

            if (pk != null)
                result.add(pk);
        }

        return result;
    }

    private static final record Schemas(Result<Record> schemas, boolean empty) {}

    private final class MetaCatalog extends CatalogImpl {

        MetaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            List<Schema> result = new ArrayList<>();
            boolean empty = false;










            if (!inverseSchemaCatalog) {
                Schemas schemas = meta(() -> "Error while fetching schemas for catalog: " + this, meta -> {
                    switch (family()) {






















                        case DUCKDB:

                            // [#9791] Include catalog in DuckDB searches
                            return new Schemas(dsl().fetch(meta.getSchemas(getName(), "%"), VARCHAR), false);

                        default:

                            // [#2681] Work around a flaw in the MySQL JDBC driver
                            return new Schemas(dsl().fetch(meta.getSchemas(), VARCHAR), false); // TABLE_SCHEM
                    }
                });

                for (String name : schemas.schemas().getValues(0, String.class))
                    result.add(new MetaSchema(name, MetaCatalog.this, false));

                empty = schemas.empty();
            }

            // [#2760] MySQL JDBC confuses "catalog" and "schema"
            else {
                Result<Record> schemas = meta(() -> "Error while fetching catalogs", meta -> dsl().fetch(
                    meta.getCatalogs(),
                    SQLDataType.VARCHAR  // TABLE_CATALOG
                ));

                for (String name : schemas.getValues(0, String.class))
                    result.add(new MetaSchema(name, MetaCatalog.this, false));
            }

            // There should always be at least one (empty) schema in a database
            if (result.isEmpty())
                result.add(new MetaSchema("", MetaCatalog.this, empty));

            return result;
        }
    }

    private final class MetaSchema extends SchemaImpl {
        private final boolean                                empty;
        private transient volatile Map<Name, Result<Record>> columnCache;
        private transient volatile Map<Name, Result<Record>> ukCache;
        private transient volatile Map<Name, Result<Record>> sequenceCache;
        private transient volatile Map<Name, String>         sourceCache;
        private transient volatile Map<Name, String>         commentCache;




        MetaSchema(String name, Catalog catalog, boolean empty) {
            super(name, catalog);

            this.empty = empty;
        }

        @Override
        public final synchronized List<Table<?>> getTables() {
            if (empty)
                return emptyList();

            Result<Record> tables = meta(() -> "Error while fetching tables for schema: " + this, meta -> {
                String[] types;

                switch (family()) {

                    // [#3977] [#11255] [#14368] PostgreSQL returns other object types, too


                    case POSTGRES:
                    case YUGABYTEDB:
                        types = new String[] {
                            "FOREIGN TABLE",
                            "MATERIALIZED VIEW",
                            "PARTITIONED TABLE",
                            "SYSTEM_TABLE",
                            "SYSTEM TABLE",
                            "SYSTEM_VIEW",
                            "SYSTEM VIEW",
                            "TABLE",
                            "TEMPORARY TABLE",
                            "TEMPORARY VIEW",
                            "VIEW"
                        };
                        break;








                    // [#2323] SQLite JDBC drivers have a bug. They return other
                    // object types, too: https://bitbucket.org/xerial/sqlite-jdbc/issue/68
                    case SQLITE:
                        types = new String[] { "TABLE", "VIEW" };
                        break;

                    default:
                        types = null;
                        break;
                }

                try (ResultSet rs = catalogSchema(getCatalog(), this, (c, s) -> {







                        return meta.getTables(c, s, "%", types);
                })) {
                    return dsl().fetch(
                        rs,

                        // [#2681] Work around a flaw in the MySQL JDBC driver
                        SQLDataType.VARCHAR, // TABLE_CAT
                        SQLDataType.VARCHAR, // TABLE_SCHEM
                        SQLDataType.VARCHAR, // TABLE_NAME
                        SQLDataType.VARCHAR, // TABLE_TYPE
                        SQLDataType.VARCHAR  // REMARKS
                    );
                }
            });

            addTables(tables);

            return Tools.map(

                // [#15325] Generic exclusion of SYNONYM types
                Tools.filter(
                    tables,
                    table ->
                        !"SYNONYM".equals(table.get(3, String.class)) &&
                        !"ALIAS".equals(table.get(3, String.class))
                ),
                table -> {
                    String catalog = table.get(0, String.class);
                    String schema = table.get(1, String.class);
                    String name = table.get(2, String.class);
                    String type = table.get(3, String.class);
                    String remarks = table.get(4, String.class);

                    TableType tableType =
                          "VIEW".equals(type)
                        ? TableType.VIEW
                        : "TEMPORARY VIEW".equals(type)
                        ? TableType.VIEW
                        : "SYSTEM_VIEW".equals(type) || "SYSTEM VIEW".equals(type)
                        ? TableType.VIEW
                        : "GLOBAL TEMPORARY".equals(type)
                        ? TableType.TEMPORARY
                        : "LOCAL TEMPORARY".equals(type)
                        ? TableType.TEMPORARY
                        : "TEMPORARY".equals(type)
                        ? TableType.TEMPORARY
                        : "MATERIALIZED VIEW".equals(type)
                        ? TableType.MATERIALIZED_VIEW
                        : TableType.TABLE;


                    switch (family()) {













                        case MYSQL:
                        case MARIADB: {

                            // [#17344] MySQL's INFORMATION_SCHEMA.TABLES.TABLE_COMMENT just adds a dummy 'VIEW' REMARK to all views, which we should ignore
                            if (tableType == TableType.VIEW)
                                remarks = null;

                            break;
                        }
                    }

                    return new MetaTable(
                        name,
                        this,
                        getColumns(catalog, schema, name),
                        getUks(catalog, schema, name),



                        remarks,
                        tableType
                    );
                }
            );
        }

        private final void addTables(Result<Record> tables) {



































        }

        private final Result<Record> getUks(String catalog, String schema, String table) {
            if (ukCache == null)
                if (family() == SQLITE)
                    initUksSQLite(catalog, schema);
                else
                    initUks(catalog, schema);

            if (ukCache != null)
                return ukCache.get(name(catalog, schema, table));
            else
                return null;
        }

        private final void initUks(String catalog, String schema) {
            init0(
                c -> ukCache = c,
                () -> ukCache,
                catalog, schema,
                MetaSQL::M_UNIQUE_KEYS,
                r -> r.field(0),
                r -> r.field(1),
                r -> r.field(2)
            );
        }

        private final void init0(
            Consumer<? super Map<Name, Result<Record>>> cacheInit,
            Supplier<? extends Map<Name, Result<Record>>> cache,
            String catalog,
            String schema,
            Function<? super SQLDialect, ? extends String> sqlF,
            Function<? super Result<?>, ? extends Field<?>> objectCatalog,
            Function<? super Result<?>, ? extends Field<?>> objectSchema,
            Function<? super Result<?>, ? extends Field<?>> objectName
        ) {
            String sql = sqlF.apply(dialect());

            if (sql != null) {
                Result<Record> result = meta(() -> "", meta ->
                    withCatalog(DSL.catalog(catalog), ctx(meta), ctx ->
                        ctx.resultQuery(
                            sql,
                            NO_SUPPORT_SCHEMAS.contains(dialect())
                                ? EMPTY_OBJECT
                                : inverseSchemaCatalog
                                ? new Object[] { catalog }
                                : new Object[] { schema }
                        ).fetch()
                    )
                );

                // TODO Support catalogs as well
                Map<Record, Result<Record>> groups = result.intoGroups(new Field<?>[] {
                    objectCatalog.apply(result),
                    objectSchema.apply(result),
                    objectName.apply(result)
                });

                cacheInit.accept(new LinkedHashMap<>());
                groups.forEach((k, v) -> {
                    cache.get().put(name(
                        catalog == null ? null : k.get(0, String.class),
                        k.get(1, String.class),
                        k.get(2, String.class)
                    ), v);
                });
            }
        }

        private final DSLContext ctx(DatabaseMetaData meta) throws SQLException {
            if (RTRIM_META_SQL_STRINGS.contains(dialect()))
                return DSL.using(meta.getConnection(), dialect(), new Settings().withFetchTrimmedCharValues(true));
            else
                return DSL.using(meta.getConnection(), dialect());
        }

        private final void initUksSQLite(String catalog, String schema) {
            ukCache = new LinkedHashMap<>();

            Map<String, String> tables =
            dsl().resultQuery(
                    """
                    select m.tbl_name, m.sql
                    from sqlite_master as m
                    where m.type = 'table'
                    and exists (
                      select 1
                      from pragma_index_list(m.name) as il
                      where il.origin = 'u'
                    )
                    order by m.tbl_name
                    """
                  )
                 .fetchMap(field("tbl_name", VARCHAR), field("sql", VARCHAR));

            // [#16675] Attempt to interpret the entire schema DDL in one go, to resolve foreign keys correctly
            StringBuilder sb = new StringBuilder();
            for (String sql : tables.values())
                sb.append(sql).append(';');

            Meta m = null;

            try {
                m = dsl()
                    .configuration()
                    .deriveSettings(s -> s.withInterpreterDelayForeignKeyDeclarations(true))
                    .dsl()
                    .meta(sb.toString());
            }
            catch (ParserException | DataDefinitionException e) {
                log.info("Cannot parse or interpret sql: " + sb.toString(), e);
            }

            Meta meta = m;

            tables.forEach((table, sql) -> {
                try {
                     Field<String> fCatalogName = field("catalog_name", VARCHAR);
                     Field<String> fSchemaName = field("schema_name", VARCHAR);
                     Field<String> fTableName = field("table_name", VARCHAR);
                     Field<String> fConstraintName = field("constraint_name", VARCHAR);
                     Field<String> fColumnName = field("column_name", VARCHAR);
                     Field<Integer> fSequenceNo = field("sequence_no", INTEGER);

                     Field<?>[] fields = {
                         fCatalogName, fSchemaName, fTableName, fConstraintName, fColumnName, fSequenceNo
                     };

                     // [#16675] If the previous schema wide DDL interpretation failed, fall back to table-by table interpretation
                     for (Table<?> t : (meta != null ? meta : dsl().meta(sql)).getTables(table)) {
                         Result<Record> result = dsl().newResult(fields);

                         int keyNo = 0;
                         for (UniqueKey<?> uk : t.getUniqueKeys()) {
                             int colNo = 0;

                             // [#16854] Generate a system name for unnamed constraints
                             String ukName = StringUtils.isEmpty(uk.getName())
                                 ? "uk_" + table + "_" + (++keyNo) + "_" + Internal.hash(row(uk.getFields()))
                                 : uk.getName();

                             for (Field<?> ukField : uk.getFields())
                                 result.add(dsl()
                                     .newRecord(fCatalogName, fSchemaName, fTableName, fConstraintName, fColumnName, fSequenceNo)
                                     .values(catalog, schema, table, ukName, ukField.getName(), colNo++)
                                 );
                         }

                         ukCache.put(name(catalog, schema, table), result);
                     }
                 }
                 catch (ParserException | DataDefinitionException e) {
                     log.info("Cannot parse or interpret sql for table " + table + ": " + sql, e);
                 }
            });
        }



























        @SuppressWarnings("unchecked")
        private final Result<Record> getColumns(String catalog, String schema, String table) {

            // SQLite JDBC's DatabaseMetaData.getColumns() can only return a single
            // table's columns
            if (columnCache == null && family() != SQLITE) {
                Result<Record> columns = getColumns0(catalog, schema, "%");

                Field<String> tableCat   = (Field<String>) columns.field(0); // TABLE_CAT
                Field<String> tableSchem = (Field<String>) columns.field(1); // TABLE_SCHEM
                Field<String> tableName  = (Field<String>) columns.field(2); // TABLE_NAME

                Map<Record, Result<Record>> groups = columns.intoGroups(new Field[] { tableCat, tableSchem, tableName });
                columnCache = new LinkedHashMap<>();

                groups.forEach((k, v) -> columnCache.put(name(k.get(tableCat), k.get(tableSchem), k.get(tableName)), v));
            }

            if (columnCache != null)
                return columnCache.get(name(catalog, schema, table));
            else
                return getColumns0(catalog, schema, table);
        }

        private final Result<Record> getColumns0(final String catalog, final String schema, final String table) {
            return meta(() -> "Error while fetching columns for table " + table + " in schema " + this, meta -> {
                try (ResultSet rs = catalogSchema(catalog, schema, (c, s) -> meta.getColumns(c, s, table, "%"))) {
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

        @SuppressWarnings("unchecked")
        @Override
        public List<Sequence<?>> getSequences() {
            if (empty)
                return emptyList();

            Result<Record> result = getSequences0();

            return result != null
                ? map(result, r -> Internal.createSequence(
                    r.get(2, String.class),
                    this,
                    (DataType<Number>) DefaultDataType.getDataType(
                        family(),
                        r.get(3, String.class),
                        r.get(4, int.class),
                        r.get(5, int.class),
                        !FALSE.equals(settings().isForceIntegerTypesOnZeroScaleDecimals())
                    ),
                    r.get(6, Long.class),
                    r.get(7, Long.class),
                    r.get(8, Long.class),
                    r.get(9, Long.class),
                    r.get(10, boolean.class),
                    r.get(11, Long.class)
                ))
              : new ArrayList<>();
        }

        private final Result<Record> getSequences0() {
            if (sequenceCache == null) {
                final String sql = TRUE.equals(settings().isMetaIncludeSystemSequences())
                    ? M_SEQUENCES_INCLUDING_SYSTEM_SEQUENCES(dialect())
                    : M_SEQUENCES(dialect());

                if (sql != null) {
                    Result<Record> result = meta(
                        () -> "Error while fetching sequences for schema: " + this,
                        meta -> ctx(meta).resultQuery(sql, MetaSchema.this.getName()).fetch()
                    );

                    // TODO Support catalogs as well
                    Map<Record, Result<Record>> groups = result.intoGroups(new Field[] { result.field(0), result.field(1) });
                    sequenceCache = new LinkedHashMap<>();

                    groups.forEach((k, v) -> sequenceCache.put(
                        name(k.get(0, String.class), k.get(1, String.class)), v
                    ));
                }
            }

            if (sequenceCache != null)
                return sequenceCache.get(name(MetaSchema.this.getCatalog().getName(), MetaSchema.this.getName()));
            else
                return null;
        }

        final String source(TableType type, String tableName) {
            if (sourceCache == null) {
                String sql = M_SOURCES(dialect());

                if (sql != null) {
                    Result<Record> result = meta(() -> "Error while fetching sources for schema " + this, meta ->
                        withCatalog(getCatalog(), ctx(meta), ctx ->
                            ctx.resultQuery(patchSchema(sql), MetaSchema.this.getName()).fetch()
                        )
                    );

                    // TODO Support catalogs as well
                    Map<Record, Result<Record>> groups = result.intoGroups(new Field[] { result.field(0), result.field(1), result.field(2) });
                    sourceCache = new LinkedHashMap<>();

                    groups.forEach((k, v) -> sourceCache.put(
                        name(k.get(1, String.class), k.get(2, String.class)),
                        Tools.apply(v.get(0).get(3, String.class), s ->
                              s.toLowerCase().startsWith("create")
                            ? s
                            : (type == VIEW ? "create view " : "create materialized view ")
                                + dsl().render(name(k.get(2, String.class))) + " as " + s
                        )
                    ));
                }
            }

            if (sourceCache != null)
                return sourceCache.get(name(MetaSchema.this.getName(), tableName));
            else
                return null;
        }

        final String comment(String tableName) {
            return comment(tableName, null);
        }

        final String comment(String tableName, String columnName) {
            if (commentCache == null) {
                String sql = M_COMMENTS(dialect());

                if (sql != null) {
                    Result<Record> result = meta(() -> "Error while fetching comments for schema: " + this, meta ->
                        withCatalog(getCatalog(), ctx(meta), ctx ->
                            ctx.resultQuery(sql, MetaSchema.this.getName()).fetch()
                        )
                    );

                    // TODO Support catalogs as well
                    Map<Record, Result<Record>> groups = result.intoGroups(new Field[] { result.field(0), result.field(1), result.field(2), result.field(3) });
                    commentCache = new LinkedHashMap<>();

                    groups.forEach((k, v) -> commentCache.put(
                        name(k.get(1, String.class), k.get(2, String.class), k.get(3, String.class)),
                        v.get(0).get(4, String.class)
                    ));
                }
            }

            if (commentCache != null)
                return commentCache.get(name(MetaSchema.this.getName(), tableName, columnName));
            else
                return null;
        }

        private final String patchSchema(String sql) {





            return sql;
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

    private static final TableOptions tableOption(DSLContext ctx, MetaSchema schema, String tableName, TableType tableType) {
        String sql = M_SOURCES(ctx.dialect());

        if (sql != null) {






            if (tableType == MATERIALIZED_VIEW)
                return TableOptions.materializedView(schema.source(tableType, tableName));
            else if (tableType == VIEW)
                return TableOptions.view(schema.source(tableType, tableName));
        }

        return TableOptions.of(tableType);
    }

    private final class MetaTable extends TableImpl<Record> {
        private final MetaSchema     schema;
        private final Result<Record> uks;





        MetaTable(
            String name,
            MetaSchema schema,
            Result<Record> columns,
            Result<Record> uks,



            String remarks,
            TableType tableType
        ) {
            super(
                name(name),
                schema,
                null, (ForeignKey<?, Record>) null, (InverseForeignKey<?, Record>) null, null, null,
                comment(remarks != null ? remarks : schema.comment(name)),
                tableOption(dsl(), schema, name, tableType),
                null
            );

            this.schema = schema;
            this.uks = uks;




            // Possible scenarios for columns being null:
            // - The "table" is in fact a SYNONYM
            if (columns != null)
                initColumns(columns);
        }

        @Override
        public final List<Index> getIndexes() {
            if (NO_SUPPORT_INDEXES.contains(dsl().dialect()))
                return emptyList();

            // See https://github.com/h2database/h2database/issues/3236
            return Tools.<List<Index>, RuntimeException>ignoreNPE(
                () -> {
                    Result<Record> result = removeSystemIndexes(meta(() -> "Error while fetching indexes for table: " + this, meta -> {
                        String tableName;







                        tableName = getName();

                        try (ResultSet rs = catalogSchema(getCatalog(), getSchema(), (c, s) -> meta.getIndexInfo(c, s, tableName, false, true))) {
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

                        catch (SQLFeatureNotSupportedException e) {
                            log.debug("Cannot call DatabaseMetaData::getIndexInfo for table " + this, e);

                            return dsl().newResult(asList(
                                DSL.field("TABLE_CAT", VARCHAR),
                                DSL.field("TABLE_SCHEM", VARCHAR),
                                DSL.field("TABLE_NAME", VARCHAR),
                                DSL.field("NON_UNIQUE", BOOLEAN),
                                DSL.field("INDEX_QUALIFIER", VARCHAR),

                                DSL.field("INDEX_NAME", VARCHAR),
                                DSL.field("TYPE", INTEGER),
                                DSL.field("ORDINAL_POSITION", INTEGER),
                                DSL.field("COLUMN_NAME", SMALLINT),
                                DSL.field("ASC_OR_DESC", SMALLINT),

                                DSL.field("CARDINALITY", BIGINT),
                                DSL.field("PAGES", BIGINT),
                                DSL.field("FILTER_CONDITION", VARCHAR)
                            ));
                        }
                    }));

                    // Sort by INDEX_NAME (5), ORDINAL_POSITION (7)
                    result.sortAsc(7).sortAsc(5);
                    return createIndexes(result);
                },
                () -> emptyList()
            );
        }

        private final Result<Record> removeSystemIndexes(Result<Record> result) {
            if (TRUE.equals(settings().isMetaIncludeSystemIndexes()))
                return result;

            // [#8655] [#9627] TODO Re-use more precise, dialect-specific logic from jOOQ-meta's Database::getIncludeSystemReferences
            Set<String> constraints = new HashSet<>();
            for (UniqueKey<?> key : getKeys())
                constraints.add(key.getName());
            for (ForeignKey<?, ?> key : getReferences())
                constraints.add(key.getName());

            Iterator<Record> it = result.iterator();
            while (it.hasNext()) {
                String indexName = it.next().get(5, String.class);

                // It's generally a good heuristic to assume an index that shares the name of the constraint is system generated
                if (constraints.contains(indexName))
                    it.remove();

                else switch (family()) {








                    case DERBY:
                        if (P_SYSINDEX_DERBY.matcher(indexName).matches()) it.remove();
                        break;
                    case H2:
                        if (P_SYSINDEX_H2.matcher(indexName).matches()) it.remove();
                        break;
                    case HSQLDB:
                        if (P_SYSINDEX_HSQLDB.matcher(indexName).matches()) it.remove();
                        break;
                    case SQLITE:
                        if (P_SYSINDEX_SQLITE.matcher(indexName).matches()) it.remove();
                        break;
                }
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final List<UniqueKey<Record>> getUniqueKeys() {
            List<UniqueKey<Record>> result = new ArrayList<>();

            if (uks != null) {
                Map<String, Result<Record>> groups = uks.intoGroups((Field<String>) uks.field(3));

                groups.forEach((k, v) -> {
                    v.sortAsc(5);
                    UniqueKey<Record> key = createUniqueKey(v, 4, 3, false);

                    if (key != null)
                        result.add(key);
                });
            }

            return result;
        }

        @Override
        public final UniqueKey<Record> getPrimaryKey() {






            Result<Record> result = meta(() -> "Error while fetching primary key for table: " + this, meta -> {
                try (ResultSet rs = catalogSchema(getCatalog(), getSchema(), (c, s) -> meta.getPrimaryKeys(c, s, getName()))) {
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
            return createUniqueKey(result, 3, 5, true);
        }

        @Override
        @SuppressWarnings("unchecked")
        public final List<ForeignKey<Record, ?>> getReferences() {
            Result<Record> result = meta(() -> "Error while fetching references for table: " + this, meta -> {
                try (ResultSet rs = catalogSchema(getCatalog(), getSchema(), (c, s) -> meta.getImportedKeys(c, s, getName()))) {
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
                catch (SQLFeatureNotSupportedException e) {
                    log.debug("Cannot call DatabaseMetaData::getImportedKeys for table " + this, e);

                    return dsl().newResult(asList(
                        DSL.field("PKTABLE_CAT", VARCHAR),
                        DSL.field("PKTABLE_SCHEM", VARCHAR),
                        DSL.field("PKTABLE_NAME", VARCHAR),
                        DSL.field("PKCOLUMN_NAME", VARCHAR),
                        DSL.field("FKTABLE_CAT", VARCHAR),

                        DSL.field("FKTABLE_SCHEM", VARCHAR),
                        DSL.field("FKTABLE_NAME", VARCHAR),
                        DSL.field("FKCOLUMN_NAME", VARCHAR),
                        DSL.field("KEY_SEQ", SMALLINT),
                        DSL.field("UPDATE_RULE", SMALLINT),

                        DSL.field("DELETE_RULE", SMALLINT),
                        DSL.field("FK_NAME", VARCHAR),
                        DSL.field("PK_NAME", VARCHAR)
                    ));
                }
            });

            Map<Record, Result<Record>> groups = result.intoGroups(new Field[] {
                result.field(inverseSchemaCatalog ? 1 : 0),
                result.field(inverseSchemaCatalog ? 0 : 1),
                result.field(2),
                result.field(9),
                result.field(10),
                result.field(11),
                result.field(12),
            });

            Map<Name, Schema> schemas = new HashMap<>();
            for (Schema s : getSchemas())
                schemas.put(s.getQualifiedName(), s);

            List<ForeignKey<Record, ?>> references = new ArrayList<>(groups.size());
            groups.forEach((k, v) -> {

                // [#7377] The schema may be null instead of "" in some dialects
                // [#12243] [#12240] Not all dialects have catalog support
                Schema s = schemas.get(
                    hasCatalog(getCatalog())
                    ? name(
                        defaultString(k.get(0, String.class)),
                        defaultString(k.get(1, String.class))
                    )
                    : name(defaultString(k.get(1, String.class)))
                );

                // [#17263] Schema could be filtered through cross schema references
                if (s != null) {
                    Table<Record> pkTable = (Table<Record>) lookupTable(s, k.get(2, String.class));

                    // [#16782] Dangling FK references are possible in some RDBMS that allow for
                    //          temporarily deactivating FK checks, e.g. MySQL via SET FOREIGN_KEY_CHECKS=0
                    if (pkTable != null) {
                        String fkName = k.get(5, String.class);
                        String pkName = k.get(6, String.class);

                        TableField<Record, ?>[] pkFields = new TableField[v.size()];
                        TableField<Record, ?>[] fkFields = new TableField[v.size()];

                        for (int i = 0; i < v.size(); i++) {
                            Record record = v.get(i);
                            String pkFieldName = record.get(3, String.class);
                            String fkFieldName = record.get(7, String.class);

                            pkFields[i] = (TableField<Record, ?>) pkTable.field(pkFieldName);
                            fkFields[i] = (TableField<Record, ?>)         field(fkFieldName);

                            // [#2656] TODO: Find a more generally reusable way to perform case insensitive lookups
                            if (pkFields[i] == null)
                                if ((pkFields[i] = lookup(pkTable, pkFieldName)) == null)
                                    return;

                            if (fkFields[i] == null)
                                if ((fkFields[i] = lookup(this, fkFieldName)) == null)
                                    return;
                        }

                        references.add(new ReferenceImpl<Record, Record>(
                            this,
                            name(fkName),
                            fkFields,
                            new MetaUniqueKey(pkTable, pkName, pkFields, true), // TODO: Can we know whether it is a PK or UK?
                            pkFields,
                            true,
                            foreignKeyRule(k.get(3, int.class)),
                            foreignKeyRule(k.get(4, int.class))
                        ));
                    }
                }
            });

            return references;
        }

        @SuppressWarnings("unchecked")
        private final TableField<Record, ?> lookup(Table<?> table, String fieldName) {
            for (Field<?> field : table.fields())
                if (field.getName().equalsIgnoreCase(fieldName))
                    return (TableField<Record, ?>) field;

            log.info("Could not look up key field : " + fieldName + " in table : " + table);
            return null;
        }















































































        @SuppressWarnings("unchecked")
        private final UniqueKey<Record> createUniqueKey(Result<Record> result, int columnName, int keyName, boolean isPrimary) {
            if (result.size() > 0) {
                TableField<Record, ?>[] f = new TableField[result.size()];

                for (int i = 0; i < f.length; i++) {
                    String name = result.get(i).get(columnName, String.class);
                    f[i] = (TableField<Record, ?>) field(name);

                    // [#5097] Work around a bug in the Xerial JDBC driver for SQLite
                    if (f[i] == null && family() == SQLITE)

                        // [#2656] Use native support for case-insensitive column
                        //         lookup, once this is implemented
                        for (Field<?> field : fields())
                            if (field.getName().equalsIgnoreCase(name))
                                f[i] = (TableField<Record, ?>) field;

                    // [#17288] Ignore the key if columns cannot be looked up.
                    if (f[i] == null) {
                        log.info("Cannot look up unique key column " + name + " in table: " + this + ". Consider reporting https://jooq.org/bug");
                        return null;
                    }
                }

                String indexName = result.get(0).get(keyName, String.class);
                return new MetaUniqueKey(this, indexName, f, isPrimary);
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

                    name = name(
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
        private final void initColumns(Result<Record> columns) {
            boolean hasAutoIncrement = false;

            for (Record column : columns) {
                String columnName = column.get(3, String.class);         // COLUMN_NAME
                String typeName = column.get(5, String.class);           // TYPE_NAME
                int precision = column.get(6, int.class);                // COLUMN_SIZE
                int scale = column.get(8, int.class);                    // DECIMAL_DIGITS
                int nullable = column.get(10, int.class);                // NULLABLE
                String remarks = column.get(11, String.class);           // REMARKS
                String defaultValue = column.get(12, String.class);      // COLUMN_DEF

                // [#10817] Some dialects may produce NULL (the expression) rather than NULL (the value)
                if ("null".equalsIgnoreCase(defaultValue))
                    defaultValue = null;

                boolean isAutoIncrement = column.size() >= 23
                    ? column.get(22, boolean.class)                      // IS_AUTOINCREMENT
                    : false;

                switch (family()) {



























                    // [#17284] Computed columns are reported as defaults
                    case DERBY:
                        if (defaultValue != null && defaultValue.toUpperCase().startsWith("GENERATED ALWAYS AS"))
                            defaultValue = null;

                        break;

                    case DUCKDB:
                        if (!isAutoIncrement && !isEmpty(defaultValue) && P_DUCKDB_IDENTITY_DEFAULT.matcher(defaultValue).matches()) {
                            isAutoIncrement = true;
                            defaultValue = null;
                        }

                        break;
                }

                // TODO: Exception handling should be moved inside SQLDataType
                DataType type;
                try {
                    type = DefaultDataType.getDataType(
                        family(),
                        typeName,
                        precision,
                        scale,
                        !FALSE.equals(settings().isForceIntegerTypesOnZeroScaleDecimals())
                    );
                }
                catch (SQLDialectNotSupportedException e) {

                    // [#15303] Register unknown data types in the type registry, to at least maintain
                    //          type information.
                    if (log.isDebugEnabled())
                        log.debug("Unknown type", "Registering unknown data type: " + typeName + " for column " + columnName + " of table " + this);

                    type = new DefaultDataType(family(), Object.class, typeName);
                }







                // [#10207] Ignore secondary identity columns, as allowed e.g. in PostgreSQL
                if (isAutoIncrement)
                    if (!hasAutoIncrement)
                        type = type.identity(hasAutoIncrement = isAutoIncrement);
                    else
                        log.info("Multiple identities", "jOOQ does not support tables with multiple identities. Identity is ignored on column " + columnName + " of table " + this);

                if (nullable == DatabaseMetaData.columnNoNulls)
                    type = type.nullable(false);

                // [#6883] Default values may be present
                if (!isAutoIncrement && !StringUtils.isEmpty(defaultValue)) {
                    try {

                        // [#7194] [#8469] Some databases report all default values as expressions, not as values
                        if (EXPRESSION_COLUMN_DEFAULT.contains(dialect())) {
                            if (FALSE.equals(settings().isParseMetaDefaultExpressions())) {
                                type = type.defaultValue(DSL.field(defaultValue, type));
                            }
                            else {
                                try {
                                    DSLContext ctx = dsl()
                                        .configuration()
                                        .deriveSettings(s -> s
                                            .withParseDialect(dialect())
                                            .withParseUnknownFunctions(ParseUnknownFunctions.IGNORE))
                                        .dsl();







                                    type = type.defaultValue(ctx.parser().parseField(defaultValue));
                                }
                                catch (ParserException e) {
                                    log.info("Cannot parse default expression (to skip parsing, use Settings.parseMetaViewDefaultExpressions): " + defaultValue + " of column " + columnName + " in table " + this, e);
                                    type = type.defaultValue(DSL.field(defaultValue, type));
                                }
                            }
                        }

                        // [#5574] MySQL mixes constant value expressions with other column expressions here
                        else if (CURRENT_TIMESTAMP_COLUMN_DEFAULT.contains(dialect()) && "CURRENT_TIMESTAMP".equalsIgnoreCase(defaultValue))
                            type = type.defaultValue(DSL.field(defaultValue, type));
                        else
                            type = type.defaultValue(DSL.inline(defaultValue, type));
                    }

                    // [#8469] Rather than catching exceptions after conversions, we should use the
                    //         parser to parse default values, if they're expressions
                    catch (DataTypeException e) {
                        log.warn("Default value", "Could not load default value: " + defaultValue + " for type: " + type + " of column " + columnName + " in table " + this, e);
                    }
                }

                createField(name(columnName), type, this, remarks != null ? remarks : schema.comment(getName(), columnName));
            }
        }
    }

    private final class MetaUniqueKey extends AbstractKey<Record> implements UniqueKey<Record> {
        private final boolean     isPrimary;

        MetaUniqueKey(Table<Record> table, String name, TableField<Record, ?>[] fields, boolean isPrimary) {
            super(table, name == null ? null : name(name), fields, true);

            this.isPrimary = isPrimary;
        }

        @Override
        public final boolean isPrimary() {
            return isPrimary;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final List<ForeignKey<?, Record>> getReferences() {
            Result<Record> result = meta(() -> "Error while fetching references for unique key: " + this, meta -> {
                try (ResultSet rs = catalogSchema(
                    getTable().getCatalog(),
                    getTable().getSchema(),
                    (c, s) -> meta.getExportedKeys(c, s, getTable().getName())
                )) {
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
                result.field(9),
                result.field(10),
                result.field(11),
                result.field(12),
            });

            Map<String, Schema> schemas = new HashMap<>();
            for (Schema schema : getSchemas())
                schemas.put(schema.getName(), schema);

            List<ForeignKey<?, Record>> references = new ArrayList<>(groups.size());
            groups.forEach((k, v) -> {

                // [#7377] The schema may be null instead of "" in some dialects
                Schema schema = schemas.get(defaultString(k.get(1, String.class)));
                Table<Record> fkTable = (Table<Record>) lookupTable(schema, k.get(2, String.class));

                references.add(new ReferenceImpl<Record, Record>(
                    fkTable,
                    name(k.get(5, String.class)),
                    map(v, f -> (TableField<Record, ?>) fkTable.field(f.get(7, String.class)), TableField[]::new),
                    this,
                    map(v, f -> (TableField<Record, ?>) getTable().field(f.get(3, String.class)), TableField[]::new),
                    true,
                    foreignKeyRule(k.get(3, int.class)),
                    foreignKeyRule(k.get(4, int.class))
                ));
            });

            return references;
        }

        @Override
        final ConstraintEnforcementStep constraint0() {
            if (isPrimary())
                return DSL.constraint(getName()).primaryKey(getFieldsArray());
            else
                return DSL.constraint(getName()).unique(getFieldsArray());
        }
    }

    @Override
    public String toString() {
        // [#9428] Prevent long running toString() calls
        return "MetaImpl";
    }
}
