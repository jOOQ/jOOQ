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

package org.jooq.meta;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static org.jooq.Log.Level.ERROR;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.meta.AbstractTypedElementDefinition.customType;
import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultIfEmpty;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.Field;
import org.jooq.Log;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.Name;
import org.jooq.Param;
// ...
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions.TableType;
import org.jooq.conf.ParseWithMetaLookups;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.jaxb.CatalogMappingType;
import org.jooq.meta.jaxb.CommentType;
import org.jooq.meta.jaxb.CustomType;
import org.jooq.meta.jaxb.EmbeddableDefinitionType;
import org.jooq.meta.jaxb.EmbeddableField;
import org.jooq.meta.jaxb.EnumType;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.ForcedTypeObjectType;
import org.jooq.meta.jaxb.Nullability;
import org.jooq.meta.jaxb.OnError;
import org.jooq.meta.jaxb.RegexFlag;
import org.jooq.meta.jaxb.SchemaMappingType;
import org.jooq.meta.jaxb.SyntheticForeignKeyType;
import org.jooq.meta.jaxb.SyntheticIdentityType;
import org.jooq.meta.jaxb.SyntheticObjectsType;
import org.jooq.meta.jaxb.SyntheticPrimaryKeyType;
import org.jooq.meta.jaxb.SyntheticUniqueKeyType;
import org.jooq.meta.jaxb.SyntheticViewType;
// ...
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVReader;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A base implementation for all types of databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDatabase implements Database {

    private static final JooqLogger                                          log                                  = JooqLogger.getLogger(AbstractDatabase.class);
    private static final Set<SQLDialect>                                     NO_SUPPORT_SCHEMATA                  = SQLDialect.supportedBy(CUBRID, FIREBIRD, SQLITE);

    // -------------------------------------------------------------------------
    // Configuration elements
    // -------------------------------------------------------------------------

    private Properties                                                       properties;
    private String                                                           basedir;
    private SQLDialect                                                       dialect;
    private Connection                                                       connection;
    private boolean                                                          regexMatchesPartialQualification;
    private boolean                                                          sqlMatchesPartialQualification;
    private OnError                                                          onError                              = OnError.FAIL;
    private List<Filter>                                                     filters;
    private String[]                                                         excludes;
    private String[]                                                         includes                             = { ".*" };
    private boolean                                                          includeExcludeColumns                = false;
    private boolean                                                          includeExcludePackageRoutines        = false;
    private boolean                                                          includeInvisibleColumns              = true;
    private boolean                                                          includeTables                        = true;
    private boolean                                                          includeEmbeddables                   = true;
    private boolean                                                          includeRoutines                      = true;
    private boolean                                                          includeTriggerRoutines               = false;
    private boolean                                                          includePackages                      = true;
    private boolean                                                          includePackageRoutines               = true;
    private boolean                                                          includePackageUDTs                   = true;
    private boolean                                                          includePackageConstants              = true;
    private boolean                                                          includeUDTs                          = true;
    private boolean                                                          includeDomains                       = true;
    private boolean                                                          includeSequences                     = true;
    private boolean                                                          includeIndexes                       = true;
    private boolean                                                          includeCheckConstraints              = true;
    private boolean                                                          includeSystemTables                  = false;
    private boolean                                                          includeSystemIndexes                 = false;
    private boolean                                                          includeSystemCheckConstraints        = false;
    private boolean                                                          includeSystemSequences               = false;
    private boolean                                                          includeSystemUDTs                    = false;
    private boolean                                                          includePrimaryKeys                   = true;
    private boolean                                                          includeUniqueKeys                    = true;
    private boolean                                                          includeForeignKeys                   = true;
    private boolean                                                          forceIntegerTypesOnZeroScaleDecimals = true;
    private String[]                                                         recordVersionFields;
    private String[]                                                         recordTimestampFields;
    private String                                                           embeddablePrimaryKeys                = null;
    private String                                                           embeddableUniqueKeys                 = null;
    private String                                                           embeddableDomains                    = null;
    private boolean                                                          supportsUnsignedTypes;
    private boolean                                                          integerDisplayWidths;
    private boolean                                                          ignoreProcedureReturnValues;
    private boolean                                                          dateAsTimestamp;
    private boolean                                                          javaTimeTypes                        = true;
    private List<CatalogMappingType>                                         configuredCatalogs                   = new ArrayList<>();
    private List<SchemaMappingType>                                          configuredSchemata                   = new ArrayList<>();
    private List<CustomType>                                                 configuredCustomTypes                = new ArrayList<>();
    private List<EnumType>                                                   configuredEnumTypes                  = new ArrayList<>();
    private List<ForcedType>                                                 configuredForcedTypes;
    private Set<ForcedType>                                                  unusedForcedTypes                    = new HashSet<>();
    private List<EmbeddableDefinitionType>                                   configuredEmbeddables                = new ArrayList<>();
    private Set<EmbeddableDefinitionType>                                    unusedEmbeddables                    = new HashSet<>();
    private List<CommentType>                                                configuredComments                   = new ArrayList<>();
    private Set<CommentType>                                                 unusedComments                       = new HashSet<>();
    private List<SyntheticIdentityType>                                      configuredSyntheticIdentities        = new ArrayList<>();
    private Set<SyntheticIdentityType>                                       unusedSyntheticIdentities            = new HashSet<>();
    private List<SyntheticPrimaryKeyType>                                    configuredSyntheticPrimaryKeys       = new ArrayList<>();
    private Set<SyntheticPrimaryKeyType>                                     unusedSyntheticPrimaryKeys           = new HashSet<>();
    private List<SyntheticUniqueKeyType>                                     configuredSyntheticUniqueKeys        = new ArrayList<>();
    private Set<SyntheticUniqueKeyType>                                      unusedSyntheticUniqueKeys            = new HashSet<>();
    private List<SyntheticForeignKeyType>                                    configuredSyntheticForeignKeys       = new ArrayList<>();
    private Set<SyntheticForeignKeyType>                                     unusedSyntheticForeignKeys           = new HashSet<>();
    private List<SyntheticViewType>                                          configuredSyntheticViews             = new ArrayList<>();
    private Set<SyntheticViewType>                                           unusedSyntheticViews                 = new HashSet<>();
    private SchemaVersionProvider                                            schemaVersionProvider;
    private CatalogVersionProvider                                           catalogVersionProvider;
    private Comparator<Definition>                                           orderProvider;
    private boolean                                                          includeRelations                     = true;
    private boolean                                                          tableValuedFunctions                 = true;
    private int                                                              logSlowQueriesAfterSeconds;
    private int                                                              logSlowResultsAfterSeconds;

    // -------------------------------------------------------------------------
    // Loaded definitions
    // -------------------------------------------------------------------------

    private Map<Definition, String>                                          sources;
    private List<String>                                                     inputCatalogs;
    private List<String>                                                     inputSchemata;
    private Map<String, List<String>>                                        inputSchemataPerCatalog;
    private List<CatalogDefinition>                                          catalogs;
    private List<SchemaDefinition>                                           schemata;
    private List<SequenceDefinition>                                         sequences;
    private List<IdentityDefinition>                                         identities;
    private List<IndexDefinition>                                            indexes;
    private List<UniqueKeyDefinition>                                        primaryKeys;
    private List<UniqueKeyDefinition>                                        uniqueKeys;
    private List<UniqueKeyDefinition>                                        keys;
    private List<ForeignKeyDefinition>                                       foreignKeys;
    private List<CheckConstraintDefinition>                                  checkConstraints;
    private List<TableDefinition>                                            tables;
    private List<EmbeddableDefinition>                                       embeddables;
    private List<EnumDefinition>                                             enums;
    private List<DomainDefinition>                                           domains;
    private List<UDTDefinition>                                              udts;
    private List<ArrayDefinition>                                            arrays;
    private List<RoutineDefinition>                                          routines;
    private List<PackageDefinition>                                          packages;
    private Relations                                                        relations;

    private transient Map<SchemaDefinition, List<SequenceDefinition>>        sequencesBySchema;
    private transient Map<SchemaDefinition, List<IdentityDefinition>>        identitiesBySchema;
    private transient Map<SchemaDefinition, List<IndexDefinition>>           indexesBySchema;
    private transient Map<TableDefinition, List<IndexDefinition>>            indexesByTable;
    private transient Map<SchemaDefinition, List<UniqueKeyDefinition>>       primaryKeysBySchema;
    private transient Map<SchemaDefinition, List<UniqueKeyDefinition>>       uniqueKeysBySchema;
    private transient Map<SchemaDefinition, List<UniqueKeyDefinition>>       keysBySchema;
    private transient Map<SchemaDefinition, List<ForeignKeyDefinition>>      foreignKeysBySchema;
    private transient Map<SchemaDefinition, List<CheckConstraintDefinition>> checkConstraintsBySchema;
    private transient Map<SchemaDefinition, List<TableDefinition>>           tablesBySchema;
    private transient Map<SchemaDefinition, List<EmbeddableDefinition>>      embeddablesByDefiningSchema;
    private transient Map<TableDefinition, List<EmbeddableDefinition>>       embeddablesByDefiningTable;
    private transient Map<TableDefinition, List<EmbeddableDefinition>>       embeddablesByReferencingTable;
    private transient Map<SchemaDefinition, List<EnumDefinition>>            enumsBySchema;
    private transient Map<SchemaDefinition, List<DomainDefinition>>          domainsBySchema;
    private transient Map<SchemaDefinition, List<UDTDefinition>>             udtsBySchema;
    private transient Map<PackageDefinition, List<UDTDefinition>>            udtsByPackage;
    private transient Map<SchemaDefinition, List<ArrayDefinition>>           arraysBySchema;
    private transient Map<SchemaDefinition, List<RoutineDefinition>>         routinesBySchema;
    private transient Map<SchemaDefinition, List<PackageDefinition>>         packagesBySchema;
    private transient boolean                                                initialised;

    // Other caches
    private final List<Definition>                                           all;
    private final List<Definition>                                           included;
    private final List<Definition>                                           excluded;
    private final Map<Table<?>, Boolean>                                     existTables;
    private final Map<TableField<?, ?>, Boolean>                             existFields;
    private final Patterns                                                   patterns;
    private final Statements                                                 statements;

    protected AbstractDatabase() {
        existTables = new HashMap<>();
        existFields = new HashMap<>();
        patterns = new Patterns();
        statements = new Statements();
        filters = new ArrayList<>();
        all = new ArrayList<>();
        included = new ArrayList<>();
        excluded = new ArrayList<>();
        orderProvider = new DefaultOrderProvider();
    }

    @Override
    public final SQLDialect getDialect() {
        if (dialect == null)
            dialect = create().configuration().dialect();

        return dialect;
    }

    @Override
    public final void setDialect(SQLDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public final void setConnection(Connection connection) {
        this.connection = connection;
        this.statements.dslContext(create());
    }

    @Override
    public final Connection getConnection() {
        return connection;
    }

    @Override
    public final DSLContext create() {
        return create(false);
    }

    protected final DSLContext create(boolean muteExceptions) {

        // [#3800] Make sure that faulty queries are logged in a formatted
        //         way to help users provide us with bug reports
        final Configuration configuration;

        try {
            configuration = create0().configuration();









        }

        // [#6226] This is mostly due to a wrong Maven groupId
        catch (NoSuchFieldError e) {
            log.error("NoSuchFieldError may happen when the jOOQ Open Source Edition (Maven groupId 'org.jooq') is used with a commercial SQLDialect. Use an appropriate groupId instead: 'org.jooq.trial', 'org.jooq.trial-java-8', 'org.jooq.trial-java-11', 'org.jooq.pro', 'org.jooq.pro-java-8', or 'org.jooq.pro-java-11'. See also: https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-in-7-steps/jooq-in-7-steps-step1/");
            throw e;
        }

        // [#9511] In some cases, it's better not to quote identifiers from
        //         jOOQ-meta queries for better dialect interoperability. No
        //         cases where quoting would have been necessary were found in
        //         integration tests, or when looking for identifiers matching
        //         [A-Za-z_$#][A-Za-z0-9_$#]+ in generated jOOQ-meta code.
        configuration.settings().setRenderQuotedNames(getRenderQuotedNames());

        if (muteExceptions) {
            return DSL.using(configuration);
        }
        else {
            final ExecuteListener newListener = new DefaultExecuteListener() {

                class SQLPerformanceWarning extends Exception {}

                @Override
                public void start(ExecuteContext ctx) {

                    // [#4974] Prevent any class loading effects from impacting below
                    //         SQLPerformanceWarning.
                    if (!initialised) {
                        try {
                            DSL.using(configuration).selectOne().fetch();
                        }

                        // [#7248] Unsupported dialects might not be able to run queries on the DUAL table
                        catch (DataAccessException e) {
                            log.debug("Error while running init query", e);
                        }

                        initialised = true;
                    }
                }

                @Override
                public void executeStart(ExecuteContext ctx) {
                    ctx.data("org.jooq.meta.AbstractDatabase.SQLPerformanceWarning.execute", new StopWatch());
                }

                @Override
                public void executeEnd(ExecuteContext ctx) {
                    int s = getLogSlowQueriesAfterSeconds();
                    if (s <= 0)
                        return;

                    StopWatch watch = (StopWatch) ctx.data("org.jooq.meta.AbstractDatabase.SQLPerformanceWarning.execute");

                    if (watch.split() > TimeUnit.SECONDS.toNanos(s)) {
                        watch.splitWarn("Slow SQL");

                        log.warn(
                            "Slow SQL",
                            "jOOQ Meta executed a slow query (slower than " + s + " seconds, configured by configuration/generator/database/logSlowQueriesAfterSeconds)"
                          + "\n\n"
                          + "If you think this is a bug in jOOQ, please report it here: https://github.com/jOOQ/jOOQ/issues/new"
                          + "\n\n```sql\n"
                          + formatted(ctx.query())
                          + "```\n",
                            new SQLPerformanceWarning());
                    }
                }

                @Override
                public void fetchStart(ExecuteContext ctx) {
                    ctx.data("org.jooq.meta.AbstractDatabase.SQLPerformanceWarning.fetch", new StopWatch());
                }

                @Override
                public void fetchEnd(ExecuteContext ctx) {
                    int s = getLogSlowResultsAfterSeconds();
                    if (s <= 0)
                        return;

                    StopWatch watch = (StopWatch) ctx.data("org.jooq.meta.AbstractDatabase.SQLPerformanceWarning.fetch");

                    if (watch.split() > TimeUnit.SECONDS.toNanos(s)) {
                        watch.splitWarn("Slow Result Fetching");

                        log.warn(
                            "Slow Result Fetching",
                            "jOOQ Meta fetched a slow result (slower than " + s + " seconds, configured by configuration/generator/database/logSlowResultsAfterSeconds)"
                          + "\n\n"
                          + "If you think this is a bug in jOOQ, please report it here: https://github.com/jOOQ/jOOQ/issues/new"
                          + "\n\n```sql\n"
                          + formatted(ctx.query())
                          + "```\n",
                            new SQLPerformanceWarning());
                    }
                }

                @Override
                public void exception(ExecuteContext ctx) {
                    log.warn(
                        "SQL exception",
                        "Exception while executing meta query: "
                      + (ctx.sqlException() != null
                      ? ctx.sqlException().getMessage()
                      : ctx.exception() != null
                      ? ctx.exception().getMessage()
                      : "No exception available")
                      + "\n\n"
                      + "If you think this is a bug in jOOQ, please report it here: https://github.com/jOOQ/jOOQ/issues/new"
                      + "\n\n"
                      + "Note you can mute some exceptions using the configuration/onError flag"
                      + "\n\n```sql\n"
                      + formatted(ctx.query())
                      + "```\n");
                }

                private String formatted(Query query) {
                    return configuration.deriveSettings(s -> s.withRenderFormatted(true)).dsl().renderInlined(query);
                }
            };

            return configuration.deriveAppending(newListener).dsl();
        }
    }

    /**
     * Subclasses may override this.
     */
    protected RenderQuotedNames getRenderQuotedNames() {
        return RenderQuotedNames.NEVER;
    }

    /**
     * Check if the configured dialect is versioned explicitly and supports a
     * given dialect.
     * <p>
     * This can be used as an optimisation to check if a dialect supports e.g. a
     * {@link SQLDialect#POSTGRES_12} without needing to query the information
     * schema.
     */
    protected boolean configuredDialectIsNotFamilyAndSupports(List<SQLDialect> d, Supplier<Boolean> ifFamily) {
        return getDialect().isFamily() ? ifFamily.get() : d.stream().allMatch(getDialect()::supports);
    }

    @Override
    public final boolean exists(TableField<?, ?> field) {
        return existFields.computeIfAbsent(field, this::exists0);
    }

    /**
     * [#8972] Subclasses may override this method for a more efficient implementation.
     */
    protected boolean exists0(TableField<?, ?> field) {
        try {
            create(true)
                .select(field)
                .from(field.getTable())
                .where(falseCondition())
                .fetch();

            return true;
        }

        // Happens when MetaGeneration generates the SQL in MetaSQL
        catch (DetachedException e) {
            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }

    /**
     * A utility method to look up a field in a single dictionary view.
     *
     * @param find The field to look up
     * @param in The dictionary view
     * @param schemaQualifier The column in the dictionary view qualifying the schema
     * @param tableQualifier The column in the dictionary view qualifying the table
     * @param columnQualifier The column in the dictionary view qualifying the column
     */
    protected final <R extends Record> boolean exists1(
        TableField<?, ?> find,
        Table<R> in,
        TableField<R, String> schemaQualifier,
        TableField<R, String> tableQualifier,
        TableField<R, String> columnQualifier
    ) {
        Condition condition = columnQualifier.eq(find.getName());

        Table<?> table = find.getTable();
        condition = condition.and(tableQualifier.eq(table.getName()));

        Schema schema = table.getSchema();
        if (schema != null)
            condition = condition.and(schemaQualifier.eq(schema.getName()));

        return create().fetchExists(in, condition);
    }

    @Override
    public final boolean existAll(TableField<?, ?>... f) {
        return Stream.of(f).allMatch(this::exists);
    }

    @Override
    public final boolean exists(Table<?> table) {
        return existTables.computeIfAbsent(table, this::exists0);
    }

    /**
     * [#8972] Subclasses may override this method for a more efficient implementation.
     */
    protected boolean exists0(Table<?> table) {
        try {
            create(true)
                .selectOne()
                .from(table)
                .where(falseCondition())
                .fetch();

            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }

    /**
     * A utility method to look up a table in a single dictionary view.
     *
     * @param find The table to look up
     * @param in The dictionary view
     * @param schemaQualifier The column in the dictionary view qualifying the schema
     * @param tableQualifier The column in the dictionary view qualifying the table
     */
    protected final <R extends Record> boolean exists1(
        Table<?> find,
        Table<R> in,
        TableField<R, String> schemaQualifier,
        TableField<R, String> tableQualifier
    ) {
        Condition condition = tableQualifier.eq(find.getName());

        Schema schema = find.getSchema();
        if (schema != null)
            condition = condition.and(schemaQualifier.eq(schema.getName()));

        return create().fetchExists(in, condition);
    }

    @Override
    public final boolean existAll(Table<?>... t) {
        return Stream.of(t).allMatch(this::exists);
    }

    final boolean matches(Pattern pattern, Definition definition) {
        if (pattern == null)
            return false;

        if (!getRegexMatchesPartialQualification())
            return pattern.matcher(definition.getName()).matches()
                || pattern.matcher(definition.getQualifiedName()).matches();

        List<Name> parts = Arrays.asList(definition.getQualifiedNamePart().parts());

        for (int i = parts.size() - 1; i >= 0; i--)
            if (pattern.matcher(DSL.name(parts.subList(i, parts.size()).toArray(new Name[0])).unquotedName().toString()).matches())
                return true;

        return false;
    }

    final boolean matches(Set<?> set, Definition definition) {
        if (set == null)
            return false;

        if (!getSqlMatchesPartialQualification())
            return set.contains(definition.getName())
                || set.contains(definition.getQualifiedName());

        List<Name> parts = Arrays.asList(definition.getQualifiedNamePart().parts());

        for (int i = parts.size() - 1; i >= 0; i--)
            if (set.contains(DSL.name(parts.subList(i, parts.size()).toArray(new Name[0])).unquotedName().toString()))
                return true;

        return false;
    }

    @Override
    public final Map<Definition, String> getSources() {
        if (sources == null) {
            sources = new LinkedHashMap<>();
            onError(ERROR, "Could not load sources", () -> {
                sources = getSources0();
                log.info("Sequences fetched", fetchedSize(sources.values(), sources.values()));
            });
        }

        return sources;
    }

    @Override
    public final List<CatalogDefinition> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<>();

            onError(ERROR, "Could not load catalogs", () -> catalogs = sort(getCatalogs0()));
            boolean onlyDefaultCatalog = true;

            Iterator<CatalogDefinition> it = catalogs.iterator();
            while (it.hasNext()) {
                CatalogDefinition catalog = it.next();

                if (!StringUtils.isBlank(catalog.getName()))
                    onlyDefaultCatalog = false;

                if (!getInputCatalogs().contains(catalog.getName()))
                    it.remove();
            }

            if (catalogs.isEmpty())
                if (onlyDefaultCatalog)
                    log.warn(
                        "No catalogs were loaded",
                        "Your database reported only a default catalog, which was filtered by your <inputCatalog/> configurations. jOOQ does not support catalogs for all databases, in case of which <inputCatalog/> configurations will not work.");
                else
                    log.warn(
                        "No catalogs were loaded",
                        "Please check your connection settings, and whether your database (and your database version!) is really supported by jOOQ. Also, check the case-sensitivity in your configured <inputCatalog/> elements.");
        }

        return catalogs;
    }

    @Override
    public final CatalogDefinition getCatalog(String inputName) {
        for (CatalogDefinition catalog : getCatalogs())
            if (catalog.getName().equals(inputName))
                return catalog;

        return null;
    }

    @Override
    public final List<SchemaDefinition> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<>();

            onError(ERROR, "Could not load schemata", () -> schemata = sort(getSchemata0()));
            schemata.removeIf(schema -> !getInputSchemata().contains(schema.getName()));

            if (schemata.isEmpty()) {
                log.warn(
                    "No schemata were loaded",
                    "Please check your connection settings, and whether your database (and your database version!) is really supported by jOOQ. Also, check the case-sensitivity in your configured <inputSchema/> elements : " + inputSchemataPerCatalog);

                if (NO_SUPPORT_SCHEMATA.contains(getDialect().family()))
                    log.warn("No schemata were loaded", "The database you're using (" + getClass().getName() + ") does not support schemata. Consider removing all <inputSchema/> and related configuration : " + inputSchemataPerCatalog);
            }
        }

        return schemata;
    }

    @Override
    public final List<SchemaDefinition> getSchemata(CatalogDefinition catalog) {
        List<SchemaDefinition> result = new ArrayList<>();

        for (SchemaDefinition schema : getSchemata())
            if (catalog.equals(schema.getCatalog()))
                result.add(schema);

        return result;
    }

    @Override
    public final SchemaDefinition getSchema(String inputName) {
        for (SchemaDefinition schema : getSchemata())
            if (schema.getName().equals(inputName))
                return schema;

        return null;
    }

    @Override
    public final List<String> getInputCatalogs() {
        if (inputCatalogs == null) {
            inputCatalogs = new ArrayList<>();

            // [#1312] Allow for ommitting inputSchema configuration. Generate
            // All catalogs instead
            if (configuredCatalogs.size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getInputCatalog())) {
                onError(ERROR, "Could not load catalogs", () -> {
                    for (CatalogDefinition catalog : sort(getCatalogs0()))
                        inputCatalogs.add(catalog.getName());
                });
            }
            else {
                for (CatalogMappingType catalog : configuredCatalogs) {





                    inputCatalogs.add(catalog.getInputCatalog());
                }
            }
        }

        return inputCatalogs;
    }

    @Override
    public final List<String> getInputSchemata() {
        if (inputSchemataPerCatalog == null) {
            inputSchemata = new ArrayList<>();
            inputSchemataPerCatalog = new LinkedHashMap<>();

            // [#1312] Allow for omitting inputSchema configuration. Generate all schemata instead.
            if (configuredSchemata.size() == 1 && StringUtils.isBlank(configuredSchemata.get(0).getInputSchema())) {
                initAllSchemata();
            }
            else if (configuredCatalogs.size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getInputCatalog())
                  && configuredCatalogs.get(0).getSchemata().size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getSchemata().get(0).getInputSchema())) {
                initAllSchemata();
            }
            else if (configuredCatalogs.isEmpty()) {
                inputSchemataPerCatalog.put("", inputSchemata);

                for (SchemaMappingType schema : configuredSchemata) {








                    {
                        inputSchemata.add(schema.getInputSchema());
                    }
                }
            }
            else {
                for (CatalogMappingType catalog : configuredCatalogs) {
                    for (SchemaMappingType schema : catalog.getSchemata()) {
                        String inputSchema;









                        {
                            inputSchema = schema.getInputSchema();
                        }

                        inputSchemata.add(inputSchema);

                        // [#6064] If no input catalogs were configured, we need to register the input schema for each catalog
                        for (String inputCatalog :
                                (configuredCatalogs.size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getInputCatalog()))
                              ? getInputCatalogs()
                              : Collections.singletonList(catalog.getInputCatalog())
                        ) {
                            inputSchemataPerCatalog.computeIfAbsent(inputCatalog, c -> new ArrayList<>()).add(inputSchema);
                        }
                    }
                }
            }
        }

        return inputSchemata;
    }

    private void initAllSchemata() {
        onError(ERROR, "Could not load schemata", () -> {
            for (SchemaDefinition schema : sort(getSchemata0())) {
                inputSchemata.add(schema.getName());
                inputSchemataPerCatalog.computeIfAbsent(schema.getCatalog().getName(), c -> new ArrayList<>()).add(schema.getName());
            }
        });
    }

    @Override
    public final List<String> getInputSchemata(CatalogDefinition catalog) {
        return getInputSchemata(catalog.getInputName());
    }

    @Override
    public final List<String> getInputSchemata(String catalog) {

        // Init if necessary
        getInputSchemata();
        return inputSchemataPerCatalog.getOrDefault(catalog, emptyList());
    }

    @Override
    @Deprecated
    public String getOutputCatalog(String inputCatalog) {
        for (CatalogMappingType catalog : configuredCatalogs)
            if (inputCatalog.equals(catalog.getInputCatalog()))
                return catalog.getOutputCatalog();

        return inputCatalog;
    }

    @Override
    @Deprecated
    public String getOutputSchema(String inputSchema) {
        for (SchemaMappingType schema : configuredSchemata)
            if (inputSchema.equals(schema.getInputSchema()))
                return schema.getOutputSchema();

        return inputSchema;
    }

    @Override
    public String getOutputSchema(String inputCatalog, String inputSchema) {
        for (CatalogMappingType catalog : configuredCatalogs)
            if (inputCatalog.equals(catalog.getInputCatalog()))
                for (SchemaMappingType schema : catalog.getSchemata())
                    if (inputSchema.equals(schema.getInputSchema()))
                        return schema.getOutputSchema();

        return inputSchema;
    }

    @Override
    public final void setConfiguredCatalogs(List<CatalogMappingType> catalogs) {
        this.configuredCatalogs = catalogs;
    }

    @Override
    public final void setConfiguredSchemata(List<SchemaMappingType> schemata) {
        this.configuredSchemata = schemata;
    }

    @Override
    public final void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public final Properties getProperties() {
        return properties;
    }

    @Override
    public final void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    @Override
    public final String getBasedir() {
        return basedir == null ? new File(".").getAbsolutePath() : basedir;
    }

    @Override
    public final void setOnError(OnError onError) {
        this.onError = onError;
    }

    @Override
    public final OnError onError() {
        return onError == null ? OnError.FAIL : onError;
    }

    @Override
    public final List<Filter> getFilters() {
        if (filters == null)
            filters = new ArrayList<>();

        return Collections.unmodifiableList(filters);
    }

    @Override
    public final void addFilter(Filter filter) {
        filters.add(filter);
    }

    @Override
    public final void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    @Override
    public final String[] getExcludes() {
        if (excludes == null)
            excludes = new String[0];

        return excludes;
    }

    @Override
    public final void setIncludes(String[] includes) {
        this.includes = includes;
    }

    @Override
    public final String[] getIncludes() {
        if (includes == null)
            includes = new String[0];

        return includes;
    }

    @Override
    public final void setIncludeExcludeColumns(boolean includeExcludeColumns) {
        this.includeExcludeColumns = includeExcludeColumns;
    }

    @Override
    public final boolean getIncludeExcludeColumns() {
        return includeExcludeColumns;
    }

    @Override
    public final void setIncludeExcludePackageRoutines(boolean includeExcludePackageRoutines) {
        this.includeExcludePackageRoutines = includeExcludePackageRoutines;
    }

    @Override
    public final boolean getIncludeExcludePackageRoutines() {
        return includeExcludePackageRoutines;
    }

    @Override
    public final void setIncludeInvisibleColumns(boolean includeInvisibleColumns) {
        this.includeInvisibleColumns = includeInvisibleColumns;
    }

    @Override
    public final boolean getIncludeInvisibleColumns() {
        return includeInvisibleColumns;
    }

    @Override
    public final boolean getIncludeTables() {
        return includeTables;
    }

    @Override
    public final void setIncludeTables(boolean includeTables) {
        this.includeTables = includeTables;
    }

    @Override
    public final boolean getIncludeEmbeddables() {
        return includeEmbeddables;
    }

    @Override
    public final void setIncludeEmbeddables(boolean includeEmbeddables) {
        this.includeEmbeddables = includeEmbeddables;
    }

    @Override
    public final boolean getIncludeRoutines() {
        return includeRoutines;
    }

    @Override
    public final void setIncludeRoutines(boolean includeRoutines) {
        this.includeRoutines = includeRoutines;
    }

    @Override
    public void setIncludeTriggerRoutines(boolean includeTriggerRoutines) {
        this.includeTriggerRoutines = includeTriggerRoutines;
    }

    @Override
    public boolean getIncludeTriggerRoutines() {
        return includeTriggerRoutines;
    }

    @Override
    public final boolean getIncludePackages() {
        return includePackages;
    }

    @Override
    public final void setIncludePackages(boolean includePackages) {
        this.includePackages = includePackages;
    }

    @Override
    public final boolean getIncludePackageRoutines() {
        return includePackageRoutines;
    }

    @Override
    public final void setIncludePackageRoutines(boolean includePackageRoutines) {
        this.includePackageRoutines = includePackageRoutines;
    }

    @Override
    public final boolean getIncludePackageUDTs() {
        return includePackageUDTs;
    }

    @Override
    public final void setIncludePackageUDTs(boolean includePackageUDTs) {
        this.includePackageUDTs = includePackageUDTs;
    }

    @Override
    public final boolean getIncludePackageConstants() {
        return includePackageConstants;
    }

    @Override
    public final void setIncludePackageConstants(boolean includePackageConstants) {
        this.includePackageConstants = includePackageConstants;
    }

    @Override
    public final boolean getIncludeUDTs() {
        return includeUDTs;
    }

    @Override
    public final void setIncludeUDTs(boolean includeUDTs) {
        this.includeUDTs = includeUDTs;
    }

    @Override
    public final boolean getIncludeDomains() {
        return includeDomains;
    }

    @Override
    public final void setIncludeDomains(boolean includeDomains) {
        this.includeDomains = includeDomains;
    }

    @Override
    public final boolean getIncludeSequences() {
        return includeSequences;
    }

    @Override
    public final void setIncludeSequences(boolean includeSequences) {
        this.includeSequences = includeSequences;
    }

    @Override
    public final void setIncludeCheckConstraints(boolean includeCheckConstraints) {
        this.includeCheckConstraints = includeCheckConstraints;
    }

    @Override
    public final boolean getIncludeCheckConstraints() {
        return includeCheckConstraints;
    }

    @Override
    public final void setIncludeSystemTables(boolean includeSystemTables) {
        this.includeSystemTables = includeSystemTables;
    }

    @Override
    public final boolean getIncludeSystemTables() {
        return includeSystemTables;
    }

    @Override
    public final void setIncludeSystemIndexes(boolean includeSystemIndexes) {
        this.includeSystemIndexes = includeSystemIndexes;
    }

    @Override
    public final boolean getIncludeSystemIndexes() {
        return includeSystemIndexes;
    }

    @Override
    public final void setIncludeSystemCheckConstraints(boolean includeSystemCheckConstraints) {
        this.includeSystemCheckConstraints = includeSystemCheckConstraints;
    }

    @Override
    public final boolean getIncludeSystemCheckConstraints() {
        return includeSystemCheckConstraints;
    }

    @Override
    public final void setIncludeSystemSequences(boolean includeSystemSequences) {
        this.includeSystemSequences = includeSystemSequences;
    }

    @Override
    public final boolean getIncludeSystemSequences() {
        return includeSystemSequences;
    }

    @Override
    public final void setIncludeSystemUDTs(boolean includeSystemUDTs) {
        this.includeSystemUDTs = includeSystemUDTs;
    }

    @Override
    public final boolean getIncludeSystemUDTs() {
        return includeSystemUDTs;
    }

    @Override
    public final void setIncludeIndexes(boolean includeIndexes) {
        this.includeIndexes = includeIndexes;
    }

    @Override
    public final boolean getIncludeIndexes() {
        return includeIndexes;
    }

    @Override
    public final boolean getIncludePrimaryKeys() {
        return includePrimaryKeys;
    }

    @Override
    public final void setIncludePrimaryKeys(boolean includePrimaryKeys) {
        this.includePrimaryKeys = includePrimaryKeys;
    }

    @Override
    public final boolean getIncludeUniqueKeys() {
        return includeUniqueKeys;
    }

    @Override
    public final void setIncludeUniqueKeys(boolean includeUniqueKeys) {
        this.includeUniqueKeys = includeUniqueKeys;
    }

    @Override
    public final boolean getIncludeForeignKeys() {
        return includeForeignKeys;
    }

    @Override
    public final void setIncludeForeignKeys(boolean includeForeignKeys) {
        this.includeForeignKeys = includeForeignKeys;
    }

    @Override
    public final void setRegexFlags(List<RegexFlag> regexFlags) {
        this.patterns.setRegexFlags(regexFlags);
    }

    @Override
    public final List<RegexFlag> getRegexFlags() {
        return patterns.getRegexFlags();
    }

    @Override
    public final void setRegexMatchesPartialQualification(boolean regexMatchesPartialQualification) {
        this.regexMatchesPartialQualification = regexMatchesPartialQualification;
    }

    @Override
    public final boolean getRegexMatchesPartialQualification() {
        return regexMatchesPartialQualification;
    }

    @Override
    public final void setSqlMatchesPartialQualification(boolean sqlMatchesPartialQualification) {
        this.sqlMatchesPartialQualification = sqlMatchesPartialQualification;
    }

    @Override
    public final boolean getSqlMatchesPartialQualification() {
        return sqlMatchesPartialQualification;
    }

    @Override
    public void setRecordVersionFields(String[] recordVersionFields) {
        this.recordVersionFields = recordVersionFields;
    }

    @Override
    public String[] getRecordVersionFields() {
        if (recordVersionFields == null)
            recordVersionFields = new String[0];

        return recordVersionFields;
    }

    @Override
    public void setRecordTimestampFields(String[] recordTimestampFields) {
        this.recordTimestampFields = recordTimestampFields;
    }

    @Override
    public String[] getRecordTimestampFields() {
        if (recordTimestampFields == null)
            recordTimestampFields = new String[0];

        return recordTimestampFields;
    }

    @Override
    @Deprecated
    public void setSyntheticPrimaryKeys(String[] syntheticPrimaryKeys) {
        if (syntheticPrimaryKeys != null) {
            for (String syntheticPrimaryKey : syntheticPrimaryKeys) {
                if (!StringUtils.isBlank(syntheticPrimaryKey)) {
                    log.warn("DEPRECATION", "The <syntheticPrimaryKeys/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
                    getConfiguredSyntheticPrimaryKeys().add(new SyntheticPrimaryKeyType().withFields(syntheticPrimaryKey));
                }
            }
        }
    }

    @Override
    @Deprecated
    public String[] getSyntheticPrimaryKeys() {
        log.warn("DEPRECATION", "The <syntheticPrimaryKeys/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
        return new String[0];
    }

    @Override
    @Deprecated
    public void setOverridePrimaryKeys(String[] overridePrimaryKeys) {
        if (overridePrimaryKeys != null) {
            for (String overridePrimaryKey : overridePrimaryKeys) {
                if (!StringUtils.isBlank(overridePrimaryKey)) {
                    log.warn("DEPRECATION", "The <overridePrimaryKeys/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
                    getConfiguredSyntheticPrimaryKeys().add(new SyntheticPrimaryKeyType().withKey(overridePrimaryKey));
                }
            }
        }
    }

    @Override
    @Deprecated
    public String[] getOverridePrimaryKeys() {
        log.warn("DEPRECATION", "The <overridePrimaryKeys/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
        return new String[0];
    }

    @Override
    @Deprecated
    public void setSyntheticIdentities(String[] syntheticIdentities) {
        if (syntheticIdentities != null) {
            for (String syntheticIdentity : syntheticIdentities) {
                if (!StringUtils.isBlank(syntheticIdentity)) {
                    log.warn("DEPRECATION", "The <syntheticIdentities/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
                    getConfiguredSyntheticIdentities().add(new SyntheticIdentityType().withFields(syntheticIdentity));
                }
            }
        }
    }

    @Override
    @Deprecated
    public final String[] getSyntheticIdentities() {
        log.warn("DEPRECATION", "The <syntheticIdentities/> configuration element has been deprecated in jOOQ 3.14. Use <syntheticObjects/> only, instead.");
        return new String[0];
    }

    @Override
    public final void setConfiguredEnumTypes(List<EnumType> configuredEnumTypes) {
        this.configuredEnumTypes = configuredEnumTypes;
    }

    @Override
    public final List<EnumType> getConfiguredEnumTypes() {
        if (configuredEnumTypes == null)
            configuredEnumTypes = new ArrayList<>();

        return configuredEnumTypes;
    }

    @Override
    @Deprecated
    public final void setConfiguredCustomTypes(List<CustomType> configuredCustomTypes) {
        if (!configuredCustomTypes.isEmpty())
            log.warn("DEPRECATION", "The <customTypes/> configuration element has been deprecated in jOOQ 3.10. Use <forcedTypes/> only, instead.");

        this.configuredCustomTypes = configuredCustomTypes;
    }

    @Override
    @Deprecated
    public final List<CustomType> getConfiguredCustomTypes() {
        if (configuredCustomTypes == null)
            configuredCustomTypes = new ArrayList<>();

        return configuredCustomTypes;
    }

    @Override
    @Deprecated
    public final CustomType getConfiguredCustomType(String typeName) {

        // The user type name that is passed here can be null.
        if (typeName == null)
            return null;

        Iterator<CustomType> it1 = getConfiguredCustomTypes().iterator();

        while (it1.hasNext()) {
            CustomType type = it1.next();

            if (type == null || (type.getName() == null && type.getType() == null)) {
                log.warn("Invalid custom type encountered: " + type);
                it1.remove();
                continue;
            }

            if (StringUtils.equals(type.getType() != null ? type.getType() : type.getName(), typeName)) {
                return type;
            }
        }

        Iterator<ForcedType> it2 = configuredForcedTypes.iterator();

        while (it2.hasNext()) {
            ForcedType type = it2.next();

            if (type.getExpressions() != null) {
                type.setIncludeExpression(type.getExpressions());
                type.setExpressions(null);
                log.warn("DEPRECATED", "The <expressions/> element in <forcedType/> is deprecated. Use <includeExpression/> instead: " + type);
            }

            if (type.getExpression() != null) {
                type.setIncludeExpression(type.getExpression());
                type.setExpression(null);
                log.warn("DEPRECATED", "The <expression/> element in <forcedType/> is deprecated. Use <includeExpression/> instead: " + type);
            }

            if (type.getTypes() != null) {
                type.setIncludeTypes(type.getTypes());
                type.setTypes(null);
                log.warn("DEPRECATED", "The <types/> element in <forcedType/> is deprecated. Use <includeTypes/> instead: " + type);
            }

            if (StringUtils.isBlank(type.getName())) {
                if (StringUtils.isBlank(type.getUserType())) {
                    log.warn("Bad configuration for <forcedType/>. Either <name/> or <userType/> is required: " + type);

                    it2.remove();
                    continue;
                }

                if (StringUtils.isBlank(type.getBinding()) &&
                    StringUtils.isBlank(type.getConverter()) &&
                    !Boolean.TRUE.equals(type.isEnumConverter()) &&
                    type.getLambdaConverter() == null) {
                    log.warn("Bad configuration for <forcedType/>. Either <binding/> or <converter/> or <enumConverter/> or <lambdaConverter/> is required: " + type);

                    it2.remove();
                    continue;
                }
            }
            else {
                if (!StringUtils.isBlank(type.getUserType())) {
                    log.warn("Bad configuration for <forcedType/>. <userType/> is not allowed when <name/> is provided: " + type);
                    type.setUserType(null);
                }
                if (!StringUtils.isBlank(type.getBinding())) {
                    log.warn("Bad configuration for <forcedType/>. <binding/> is not allowed when <name/> is provided: " + type);
                    type.setBinding(null);
                }
                if (!StringUtils.isBlank(type.getConverter())) {
                    log.warn("Bad configuration for <forcedType/>. <converter/> is not allowed when <name/> is provided: " + type);
                    type.setConverter(null);
                }
                if (Boolean.TRUE.equals(type.isEnumConverter())) {
                    log.warn("Bad configuration for <forcedType/>. <enumConverter/> is not allowed when <name/> is provided: " + type);
                    type.setEnumConverter(null);
                }
                if (type.getLambdaConverter() != null) {
                    log.warn("Bad configuration for <forcedType/>. <lambdaConverter/> is not allowed when <name/> is provided: " + type);
                    type.setLambdaConverter(null);
                }
            }

            if (type.getUserType() != null && StringUtils.equals(type.getUserType(), typeName)) {
                return customType(this, type);
            }
        }

        return null;
    }

    @Override
    public void markUsed(ForcedType forcedType) {
        unusedForcedTypes.remove(forcedType);
    }

    @Override
    public List<ForcedType> getUnusedForcedTypes() {
        return new ArrayList<>(unusedForcedTypes);
    }

    @Override
    public final void setConfiguredForcedTypes(List<ForcedType> configuredForcedTypes) {

        // [#8512] Some implementation of this database may have already configured
        //         a forced type programmatically, so we must not set the list but
        //         append it.
        getConfiguredForcedTypes().addAll(configuredForcedTypes);
        unusedForcedTypes.addAll(configuredForcedTypes);
    }

    @Override
    public final List<ForcedType> getConfiguredForcedTypes() {
        if (configuredForcedTypes == null)
            configuredForcedTypes = new SortedList<>(new ArrayList<>(), comparing(f -> defaultIfNull(f.getPriority(), 0), (i1, i2) -> Integer.compare(i2, i1)));

        return configuredForcedTypes;
    }

    @Override
    public final int getLogSlowQueriesAfterSeconds() {
        return logSlowQueriesAfterSeconds;
    }

    @Override
    public final void setLogSlowQueriesAfterSeconds(int logSlowQueriesAfterSeconds) {
        this.logSlowQueriesAfterSeconds = logSlowQueriesAfterSeconds;
    }

    @Override
    public final int getLogSlowResultsAfterSeconds() {
        return logSlowResultsAfterSeconds;
    }

    @Override
    public final void setLogSlowResultsAfterSeconds(int logSlowResultsAfterSeconds) {
        this.logSlowResultsAfterSeconds = logSlowResultsAfterSeconds;
    }

    @Override
    public final SchemaVersionProvider getSchemaVersionProvider() {
        return schemaVersionProvider;
    }

    @Override
    public final void setSchemaVersionProvider(SchemaVersionProvider schemaVersionProvider) {
        this.schemaVersionProvider = schemaVersionProvider;
    }

    @Override
    public final CatalogVersionProvider getCatalogVersionProvider() {
        return catalogVersionProvider;
    }

    @Override
    public final void setCatalogVersionProvider(CatalogVersionProvider catalogVersionProvider) {
        this.catalogVersionProvider = catalogVersionProvider;
    }

    @Override
    public final Comparator<Definition> getOrderProvider() {
        return orderProvider;
    }

    @Override
    public final void setOrderProvider(Comparator<Definition> provider) {
        this.orderProvider = provider;
    }

    @Override
    public final void setSupportsUnsignedTypes(boolean supportsUnsignedTypes) {
        this.supportsUnsignedTypes = supportsUnsignedTypes;
    }

    @Override
    public final boolean supportsUnsignedTypes() {
        return supportsUnsignedTypes;
    }

    @Override
    public final void setIntegerDisplayWidths(boolean integerDisplayWidths) {
        this.integerDisplayWidths = integerDisplayWidths;
    }

    @Override
    public final boolean integerDisplayWidths() {
        return integerDisplayWidths;
    }

    @Override
    public final void setIgnoreProcedureReturnValues(boolean ignoreProcedureReturnValues) {
        this.ignoreProcedureReturnValues = ignoreProcedureReturnValues;
    }

    @Override
    public final boolean ignoreProcedureReturnValues() {
        return ignoreProcedureReturnValues;
    }

    @Override
    public final void setDateAsTimestamp(boolean dateAsTimestamp) {
        this.dateAsTimestamp = dateAsTimestamp;
    }

    @Override
    public final boolean dateAsTimestamp() {
        return dateAsTimestamp;
    }

    @Override
    public final void setJavaTimeTypes(boolean javaTimeTypes) {
        this.javaTimeTypes = javaTimeTypes;
    }

    @Override
    public final boolean javaTimeTypes() {
        return javaTimeTypes;
    }

    @Override
    public final void setIncludeRelations(boolean includeRelations) {
        this.includeRelations = includeRelations;
    }

    @Override
    public final boolean includeRelations() {
        return includeRelations;
    }

    @Override
    public void setForceIntegerTypesOnZeroScaleDecimals(boolean forceIntegerTypesOnZeroScaleDecimals) {
        this.forceIntegerTypesOnZeroScaleDecimals = forceIntegerTypesOnZeroScaleDecimals;
    }

    @Override
    public boolean getForceIntegerTypesOnZeroScaleDecimals() {
        return forceIntegerTypesOnZeroScaleDecimals;
    }

    @Override
    public final void setTableValuedFunctions(boolean tableValuedFunctions) {
        this.tableValuedFunctions = tableValuedFunctions;
    }

    @Override
    public final boolean tableValuedFunctions() {
        return tableValuedFunctions;
    }

    @Override
    public final List<SequenceDefinition> getSequences() {
        if (sequences == null) {
            sequences = new ArrayList<>();

            if (getIncludeSequences()) {
                onError(ERROR, "Error while fetching sequences", () -> {
                    List<SequenceDefinition> s = getSequences0();

                    sequences = sort(filterExcludeInclude(s));
                    log.info("Sequences fetched", fetchedSize(s, sequences));
                });
            }
            else
                log.info("Sequences excluded");
        }

        return sequences;
    }

    @Override
    public final List<SequenceDefinition> getSequences(SchemaDefinition schema) {
        if (sequencesBySchema == null)
            sequencesBySchema = new LinkedHashMap<>();

        return filterSchema(getSequences(), schema, sequencesBySchema);
    }

    @Override
    public final List<IdentityDefinition> getIdentities(SchemaDefinition schema) {
        if (identities == null) {
            identities = new ArrayList<>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    IdentityDefinition identity = table.getIdentity();

                    if (identity != null)
                        identities.add(identity);
                }
            }

            sort(identities);
        }

        if (identitiesBySchema == null)
            identitiesBySchema = new LinkedHashMap<>();

        return filterSchema(identities, schema, identitiesBySchema);
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys() {
        if (uniqueKeys == null) {
            uniqueKeys = new ArrayList<>();

            if (getIncludeUniqueKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        uniqueKeys.addAll(table.getUniqueKeys());

            sort(uniqueKeys);
        }

        return uniqueKeys;
    }

    @Override
    public final List<UniqueKeyDefinition> getPrimaryKeys(SchemaDefinition schema) {
        if (primaryKeysBySchema == null)
            primaryKeysBySchema = new LinkedHashMap<>();

        return filterSchema(getPrimaryKeys(), schema, primaryKeysBySchema);
    }

    @Override
    public final List<UniqueKeyDefinition> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<>();

            if (getIncludePrimaryKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        if (table.getPrimaryKey() != null)
                            primaryKeys.add(table.getPrimaryKey());

            sort(primaryKeys);
        }

        return primaryKeys;
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        if (uniqueKeysBySchema == null)
            uniqueKeysBySchema = new LinkedHashMap<>();

        return filterSchema(getUniqueKeys(), schema, uniqueKeysBySchema);
    }

    @Override
    public final List<UniqueKeyDefinition> getKeys() {
        if (keys == null) {
            keys = new ArrayList<>();

            if (getIncludeUniqueKeys() || getIncludePrimaryKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        keys.addAll(table.getKeys());

            sort(keys);
        }

        return keys;
    }

    @Override
    public final List<UniqueKeyDefinition> getKeys(SchemaDefinition schema) {
        if (keysBySchema == null)
            keysBySchema = new LinkedHashMap<>();

        return filterSchema(getKeys(), schema, keysBySchema);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<>();

            if (getIncludeForeignKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        foreignKeys.addAll(table.getForeignKeys());

            sort(foreignKeys);
        }

        return foreignKeys;
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema) {
        if (foreignKeysBySchema == null)
            foreignKeysBySchema = new LinkedHashMap<>();

        return filterSchema(getForeignKeys(), schema, foreignKeysBySchema);
    }

    @Override
    public final List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema) {
        if (checkConstraints == null) {
            checkConstraints = new ArrayList<>();

            if (getIncludeCheckConstraints())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        checkConstraints.addAll(table.getCheckConstraints());

            sort(checkConstraints);
        }

        if (checkConstraintsBySchema == null)
            checkConstraintsBySchema = new LinkedHashMap<>();

        return filterSchema(checkConstraints, schema, checkConstraintsBySchema);
    }

    @Override
    public final List<TableDefinition> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();

            if (getIncludeTables()) {
                onError(ERROR, "Error while fetching tables", () -> {
                    List<TableDefinition> t = getTables0();
                    syntheticViews(t);
                    tables = sort(filterExcludeInclude(t));
                    log.info("Tables fetched", fetchedSize(t, tables));
                });
            }
            else
                log.info("Tables excluded");
        }

        return tables;
    }

    @Override
    public final List<TableDefinition> getTables(SchemaDefinition schema) {
        if (tablesBySchema == null)
            tablesBySchema = new LinkedHashMap<>();

        return filterSchema(getTables(), schema, tablesBySchema);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name) {
        return getTable(schema, name, false);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getTables(schema), name, ignoreCase);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, Name name) {
        return getTable(schema, name, false);
    }

    @Override
    public final TableDefinition getTable(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return getDefinition(getTables(schema), name, ignoreCase);
    }

    @Override
    public final List<EnumDefinition> getEnums(SchemaDefinition schema) {
        if (enums == null) {
            enums = new ArrayList<>();

            onError(ERROR, "Error while fetching enums", () -> {
                List<EnumDefinition> e = getEnums0();

                enums = sort(filterExcludeInclude(e));
                enums.addAll(getConfiguredEnums());

                log.info("Enums fetched", fetchedSize(e, enums));
            });
        }

        if (enumsBySchema == null)
            enumsBySchema = new LinkedHashMap<>();

        return filterSchema(enums, schema, enumsBySchema);
    }

    private final List<EnumDefinition> getConfiguredEnums() {
        List<EnumDefinition> result = new ArrayList<>(getConfiguredEnumTypes().size());

        for (EnumType enumType : getConfiguredEnumTypes()) {
            String name = enumType.getName();
            DefaultEnumDefinition e = new DefaultEnumDefinition(getSchemata().get(0), name, null, true);

            String literals = enumType.getLiterals();

            try {
                CSVReader reader = new CSVReader(new StringReader(literals));
                e.addLiterals(reader.readNext());
            }
            catch (IOException ignore) {}

            result.add(e);
        }

        return result;
    }

    @Override
    public final ForcedType getConfiguredForcedType(Definition definition) {
        return getConfiguredForcedType(definition, null);
    }

    @Override
    public final ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType) {

        // [#5885] Only the first matching <forcedType/> is applied to the data type definition.
        forcedTypeLoop:
        for (ForcedType forcedType : getConfiguredForcedTypes()) {
            String excludeExpression = forcedType.getExcludeExpression();
            String includeExpression = StringUtils.firstNonNull(forcedType.getIncludeExpression(), forcedType.getExpression(), forcedType.getExpressions());
            String excludeTypes = forcedType.getExcludeTypes();
            String includeTypes = StringUtils.firstNonNull(forcedType.getIncludeTypes(), forcedType.getTypes());
            Nullability nullability = forcedType.getNullability();
            ForcedTypeObjectType objectType = forcedType.getObjectType();
            String sql = forcedType.getSql();

            if (     (objectType != null && objectType != ForcedTypeObjectType.ALL)
                 && ((objectType == ForcedTypeObjectType.ATTRIBUTE && !(definition instanceof AttributeDefinition))
                 ||  (objectType == ForcedTypeObjectType.COLUMN && !(definition instanceof ColumnDefinition))
                 ||  (objectType == ForcedTypeObjectType.ELEMENT && !(definition instanceof ArrayDefinition))
                 ||  (objectType == ForcedTypeObjectType.PARAMETER && !(definition instanceof ParameterDefinition))
                 ||  (objectType == ForcedTypeObjectType.SEQUENCE && !(definition instanceof SequenceDefinition))))
                continue forcedTypeLoop;

            if (     (nullability != null && nullability != Nullability.ALL && definedType != null)
                 && ((nullability == Nullability.NOT_NULL && definedType.isNullable())
                 ||  (nullability == Nullability.NULL && !definedType.isNullable())))
                continue forcedTypeLoop;

            if (excludeExpression != null && matches(patterns.pattern(excludeExpression), definition))
                continue forcedTypeLoop;

            if (includeExpression != null && !matches(patterns.pattern(includeExpression), definition))
                continue forcedTypeLoop;

            if (    (definedType != null && (excludeTypes != null || includeTypes != null))
                 && !typeMatchesExcludeInclude(definedType, excludeTypes, includeTypes))
                continue forcedTypeLoop;

            if (sql != null)
                if (!matches(statements.fetchSet(sql), definition))
                    continue forcedTypeLoop;

            return forcedType;
        }

        return null;
    }

    private boolean typeMatchesExcludeInclude(DataTypeDefinition type, String exclude, String include) {
        if (exclude != null && matches(type, patterns.pattern(exclude)))
            return false;

        return include == null || matches(type, patterns.pattern(include));
    }

    private boolean matches(DataTypeDefinition type, Pattern pattern) {
        return type.getMatchNames().stream().map(pattern::matcher).anyMatch(Matcher::matches);
    }

    @Override
    public final void markUsed(EmbeddableDefinitionType embeddable) {
        unusedEmbeddables.remove(embeddable);
    }

    @Override
    public final List<EmbeddableDefinitionType> getUnusedEmbeddables() {
        return new ArrayList<>(unusedEmbeddables);
    }

    @Override
    public final void setConfiguredEmbeddables(List<EmbeddableDefinitionType> configuredEmbeddables) {

        // [#8512] Some implementation of this database may have already
        // configured a forced type programmatically, so we must not set the
        // list but append it.
        getConfiguredEmbeddables().addAll(configuredEmbeddables);
        unusedEmbeddables.addAll(configuredEmbeddables);
    }

    @Override
    public final List<EmbeddableDefinitionType> getConfiguredEmbeddables() {
        if (configuredEmbeddables == null)
            configuredEmbeddables = new ArrayList<>();

        return configuredEmbeddables;
    }

    @Override
    public String embeddablePrimaryKeys() {
        return embeddablePrimaryKeys;
    }

    @SuppressWarnings("unused")
    @Override
    public void setEmbeddablePrimaryKeys(String embeddablePrimaryKeys) {



        if (!isBlank(embeddablePrimaryKeys))
            log.info("Commercial feature", "Embeddable primary and unique keys are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition");

        this.embeddablePrimaryKeys = embeddablePrimaryKeys;
    }

    @Override
    public String embeddableUniqueKeys() {
        return embeddableUniqueKeys;
    }

    @SuppressWarnings("unused")
    @Override
    public void setEmbeddableUniqueKeys(String embeddableUniqueKeys) {



        if (!isBlank(embeddableUniqueKeys))
            log.info("Commercial feature", "Embeddable primary and unique keys are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition");

        this.embeddableUniqueKeys = embeddableUniqueKeys;
    }

    @Override
    public String embeddableDomains() {
        return embeddableDomains;
    }

    @SuppressWarnings("unused")
    @Override
    public void setEmbeddableDomains(String embeddableDomains) {



        if (!isBlank(embeddableDomains))
            log.info("Commercial feature", "Embeddable domains are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition");

        this.embeddableDomains = embeddableDomains;
    }

    @Override
    public final List<EmbeddableDefinition> getEmbeddables() {
        if (embeddables == null) {
            embeddables = new ArrayList<>();

            if (getIncludeEmbeddables()) {
                onError(ERROR, "Error while fetching embeddables", () -> {
                    List<EmbeddableDefinition> r = getEmbeddables0();

                    embeddables = sort(r);
                    // indexes = sort(filterExcludeInclude(r)); TODO Support include / exclude for indexes (and constraints!)
                    log.info("Embeddables fetched", fetchedSize(r, embeddables));
                });
            }
            else
                log.info("Embeddables excluded");
        }

        return embeddables;
    }

    @Override
    public final List<EmbeddableDefinition> getEmbeddables(SchemaDefinition schema) {
        if (embeddablesByDefiningSchema == null)
            embeddablesByDefiningSchema = new LinkedHashMap<>();

        return filterSchema(getEmbeddables(), schema, embeddablesByDefiningSchema);
    }

    @Override
    public final List<EmbeddableDefinition> getEmbeddables(TableDefinition table) {
        if (embeddablesByDefiningTable == null)
            embeddablesByDefiningTable = new LinkedHashMap<>();

        return filterTable(getEmbeddables(table.getSchema()), table, embeddablesByDefiningTable);
    }

    @Override
    public final List<EmbeddableDefinition> getEmbeddablesByReferencingTable(TableDefinition table) {
        if (embeddablesByReferencingTable == null)
            embeddablesByReferencingTable = new LinkedHashMap<>();

        return filterReferencingTable(getEmbeddables(), table, embeddablesByReferencingTable);
    }

    private final List<EmbeddableDefinition> getEmbeddables0() {
        Map<Name, EmbeddableDefinition> result = new LinkedHashMap<>();

        for (TableDefinition table : getTables()) {

            embeddableLoop:
            for (EmbeddableDefinitionType embeddable : getConfiguredEmbeddables()) {
                if (embeddable.getTables() != null && !matches(patterns.pattern(embeddable.getTables()), table))
                    continue embeddableLoop;

                if (embeddable.getFields().isEmpty()) {
                    log.warn("Illegal embeddable", "An embeddable definition must have at least one field declaration");
                    continue embeddableLoop;
                }

                List<ColumnDefinition> columns = new ArrayList<>();
                List<String> names = new ArrayList<>();

                for (EmbeddableField embeddableField : embeddable.getFields()) {
                    boolean matched = false;

                    for (ColumnDefinition column : table.getColumns())
                        if (matches(patterns.pattern(embeddableField.getExpression()), column))
                            if (matched)
                                log.warn("EmbeddableField configuration matched several columns in table " + table + ": " + embeddableField);
                            else
                                matched = columns.add(column) && names.add(defaultIfEmpty(embeddableField.getName(), column.getName()));
                }

                if (columns.size() == embeddable.getFields().size()) {
                    CatalogDefinition catalog = getCatalog(embeddable.getCatalog());

                    SchemaDefinition schema = catalog != null
                        ? catalog.getSchema(embeddable.getSchema())
                        : getSchema(embeddable.getSchema());

                    if (schema == null)
                        schema = table.getSchema();

                    Name name = table.getQualifiedNamePart().append(embeddable.getName());

                    if (result.containsKey(name)) {
                        log.warn("Embeddable configuration", "Table " + table + " already has embeddable " + embeddable);
                    }
                    else {
                        result.put(
                            name,
                            new DefaultEmbeddableDefinition(
                                schema,
                                embeddable.getName(),
                                embeddable.getComment(),
                                table,
                                names,
                                defaultIfBlank(embeddable.getReferencingName(), embeddable.getName()),
                                defaultIfBlank(embeddable.getReferencingComment(), embeddable.getComment()),
                                table,
                                columns,
                                TRUE.equals(embeddable.isReplacesFields())
                            )
                        );

                        markUsed(embeddable);
                    }
                }
            }
        }














































































































        return new ArrayList<>(result.values());
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, String name) {
        return getEnum(schema, name, false);
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getEnums(schema), name, ignoreCase);
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, Name name) {
        return getEnum(schema, name, false);
    }

    @Override
    public final EnumDefinition getEnum(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return getDefinition(getEnums(schema), name, ignoreCase);
    }

    @Override
    public final List<DomainDefinition> getDomains() {
        if (domains == null) {
            domains = new ArrayList<>();

            if (getIncludeDomains()) {
                onError(ERROR, "Error while fetching domains", () -> {
                    List<DomainDefinition> e = getDomains0();

                    domains = sort(filterExcludeInclude(e));
                    log.info("Domains fetched", fetchedSize(e, domains));
                });
            }
            else
                log.info("Domains excluded");
        }

        return domains;
    }

    @Override
    public final List<DomainDefinition> getDomains(SchemaDefinition schema) {
        if (domainsBySchema == null)
            domainsBySchema = new LinkedHashMap<>();

        return filterSchema(getDomains(), schema, domainsBySchema);
    }

    @Override
    public final DomainDefinition getDomain(SchemaDefinition schema, String name) {
        return getDomain(schema, name, false);
    }

    @Override
    public final DomainDefinition getDomain(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getDomains(schema), name, ignoreCase);
    }

    @Override
    public final DomainDefinition getDomain(SchemaDefinition schema, Name name) {
        return getDomain(schema, name, false);
    }

    @Override
    public final DomainDefinition getDomain(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return getDefinition(getDomains(schema), name, ignoreCase);
    }

    @Override
    public final List<ArrayDefinition> getArrays(SchemaDefinition schema) {
        if (arrays == null) {
            arrays = new ArrayList<>();

            if (getIncludeUDTs()) {
                onError(ERROR, "Error while fetching ARRAYs", () -> {
                    List<ArrayDefinition> a = getArrays0();

                    arrays = sort(filterExcludeInclude(a));
                    log.info("ARRAYs fetched", fetchedSize(a, arrays));
                });
            }
            else
                log.info("ARRAYs excluded");
        }

        if (arraysBySchema == null)
            arraysBySchema = new LinkedHashMap<>();

        return filterSchema(arrays, schema, arraysBySchema);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, String name) {
        return getArray(schema, name, false);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getArrays(schema), name, ignoreCase);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, Name name) {
        return getArray(schema, name, false);
    }

    @Override
    public final ArrayDefinition getArray(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return getDefinition(getArrays(schema), name, ignoreCase);
    }

    @Override
    public final List<UDTDefinition> getUDTs() {
        if (udts == null) {
            udts = new ArrayList<>();

            if (getIncludeUDTs()) {
                onError(ERROR, "Error while fetching UDTs", () -> {
                    List<UDTDefinition> u = getUDTs0();

                    udts = sort(filterExcludeInclude(u));
                    log.info("UDTs fetched", fetchedSize(u, udts));
                });
            }
            else
                log.info("UDTs excluded");
        }

        return udts;
    }

    @Override
    public final List<UDTDefinition> getUDTs(SchemaDefinition schema) {
        if (udtsBySchema == null)
            udtsBySchema = new LinkedHashMap<>();

        return filterSchema(getUDTs(), schema, udtsBySchema);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, String name) {
        return getUDT(schema, name, false);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getUDTs(schema), name, ignoreCase);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, Name name) {
        return getUDT(schema, name, false);
    }

    @Override
    public final UDTDefinition getUDT(SchemaDefinition schema, Name name, boolean ignoreCase) {
        return getDefinition(getUDTs(schema), name, ignoreCase);
    }

    @Override
    public final List<UDTDefinition> getUDTs(PackageDefinition pkg) {
        if (udtsByPackage == null)
            udtsByPackage = new LinkedHashMap<>();

        return filterPackage(getUDTs(), pkg, udtsByPackage);
    }

    @Override
    public final Relations getRelations() {
        if (relations == null) {
            relations = new DefaultRelations();

            // [#3559] If the code generator doesn't need relation information, we shouldn't
            // populate them here to avoid running potentially expensive queries.
            if (includeRelations)
                onError(ERROR, "Error while fetching relations", () -> relations = getRelations0());
        }

        return relations;
    }

    @Override
    public final List<IndexDefinition> getIndexes(SchemaDefinition schema) {
        if (indexes == null) {
            indexes = new ArrayList<>();

            if (getIncludeIndexes()) {
                onError(ERROR, "Error while fetching indexes", () -> {
                    List<IndexDefinition> r = getIndexes0();

                    indexes = sort(r);
                    // indexes = sort(filterExcludeInclude(r)); TODO Support include / exclude for indexes (and constraints!)
                    log.info("Indexes fetched", fetchedSize(r, indexes));
                });
            }
            else
                log.info("Indexes excluded");
        }

        if (indexesBySchema == null)
            indexesBySchema = new LinkedHashMap<>();

        return filterSchema(indexes, schema, indexesBySchema);
    }

    @Override
    public final List<IndexDefinition> getIndexes(TableDefinition table) {
        if (indexesByTable == null)
            indexesByTable = new HashMap<>();

        List<IndexDefinition> list = indexesByTable.get(table);
        if (list == null) {
            indexesByTable.put(table, list = new ArrayList<>());

            for (TableDefinition otherTable : getTables(table.getSchema()))
                if (!indexesByTable.containsKey(otherTable))
                    indexesByTable.put(otherTable, new ArrayList<>());

            for (IndexDefinition index : getIndexes(table.getSchema()))
                indexesByTable.computeIfAbsent(index.getTable(), k -> new ArrayList<>()).add(index);
        }

        return list;
    }

    @Override
    public final List<RoutineDefinition> getRoutines(SchemaDefinition schema) {
        if (routines == null) {
            routines = new ArrayList<>();

            if (getIncludeRoutines()) {
                onError(ERROR, "Error while fetching routines", () -> {
                    List<RoutineDefinition> r = getRoutines0();

                    routines = sort(filterExcludeInclude(r));
                    log.info("Routines fetched", fetchedSize(r, routines));
                });
            }
            else
                log.info("Routines excluded");
        }

        if (routinesBySchema == null)
            routinesBySchema = new LinkedHashMap<>();

        return filterSchema(routines, schema, routinesBySchema);
    }

    @Override
    public final List<PackageDefinition> getPackages(SchemaDefinition schema) {
        if (packages == null) {
            packages = new ArrayList<>();

            if (getIncludePackages()) {
                onError(ERROR, "Error while fetching packages", () -> {
                    List<PackageDefinition> p = getPackages0();

                    packages = sort(filterExcludeInclude(p));
                    log.info("Packages fetched", fetchedSize(p, packages));
                });
            }
            else
                log.info("Packages excluded");
        }

        if (packagesBySchema == null)
            packagesBySchema = new LinkedHashMap<>();

        return filterSchema(packages, schema, packagesBySchema);
    }

    @Override
    public PackageDefinition getPackage(SchemaDefinition schema, String inputName) {
        for (PackageDefinition pkg : getPackages(schema))
            if (pkg.getName().equals(inputName))
                return pkg;

        return null;
    }

    protected static final <D extends Definition> D getDefinition(List<D> definitions, String name, boolean ignoreCase) {
        if (name == null)
            return null;

        for (D definition : definitions)
            if ((ignoreCase && definition.getName().equalsIgnoreCase(name)) ||
                (!ignoreCase && definition.getName().equals(name)))

                return definition;

        return null;
    }

    protected static final <D extends Definition> D getDefinition(List<D> definitions, Name name, boolean ignoreCase) {
        if (name == null)
            return null;

        for (D definition : definitions)
            if ((ignoreCase && definition.getQualifiedNamePart().equalsIgnoreCase(name)) ||
                (!ignoreCase && definition.getQualifiedNamePart().equals(name)))

                return definition;

        return null;
    }

    protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema, Map<SchemaDefinition, List<T>> cache) {
        return cache.computeIfAbsent(schema, s -> filterSchema(definitions, s));
    }

    protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema) {
        if (schema == null)
            return definitions;

        List<T> result = new ArrayList<>();

        for (T definition : definitions)
            if (definition.getSchema() != null && definition.getSchema().equals(schema))
                result.add(definition);

        return result;
    }

    protected final <T extends Definition> List<T> filterPackage(List<T> definitions, PackageDefinition pkg, Map<PackageDefinition, List<T>> cache) {
        return cache.computeIfAbsent(pkg, p -> filterPackage(definitions, p));
    }

    protected final <T extends Definition> List<T> filterPackage(List<T> definitions, PackageDefinition pkg) {
        if (pkg == null)
            return definitions;

        List<T> result = new ArrayList<>();

        for (T definition : definitions)
            if (definition.getPackage() != null && definition.getPackage().equals(pkg))
                result.add(definition);

        return result;
    }

    protected final <T extends TableElementDefinition> List<T> filterTable(List<T> definitions, TableDefinition table, Map<TableDefinition, List<T>> cache) {
        List<T> result = cache.get(table);

        if (result == null) {
            result = filterTable(definitions, table);
            cache.put(table, result);
        }

        return result;
    }

    protected final <T extends TableElementDefinition> List<T> filterTable(List<T> definitions, TableDefinition table) {
        if (table == null)
            return definitions;

        List<T> result = new ArrayList<>();

        for (T definition : definitions)
            if (definition.getTable().equals(table))
                result.add(definition);

        return result;
    }

    private final <T extends EmbeddableDefinition> List<T> filterReferencingTable(List<T> definitions, TableDefinition table, Map<TableDefinition, List<T>> cache) {
        List<T> result = cache.get(table);

        if (result == null) {
            result = filterReferencingTable(definitions, table);
            cache.put(table, result);
        }

        return result;
    }

    private final <T extends EmbeddableDefinition> List<T> filterReferencingTable(List<T> definitions, TableDefinition table) {
        if (table == null)
            return definitions;

        List<T> result = new ArrayList<>();

        for (T definition : definitions)
            if (definition.getReferencingTable().equals(table))
                result.add(definition);

        return result;
    }

    @Override
    public final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions) {
        List<T> result = filterExcludeInclude(definitions, excludes, includes, filters);

        this.all.addAll(definitions);
        this.included.addAll(result);
        this.excluded.addAll(definitions);
        this.excluded.removeAll(result);

        return result;
    }

    @Override
    public final <T extends Definition> List<T> sort(List<T> definitions) {
        if (orderProvider != null)
            definitions.sort(orderProvider);

        return definitions;
    }

    @Override
    public final List<Definition> getIncluded() {
        return Collections.unmodifiableList(included);
    }

    @Override
    public final List<Definition> getExcluded() {
        return Collections.unmodifiableList(excluded);
    }

    @Override
    public final List<Definition> getAll() {
        return Collections.unmodifiableList(all);
    }

    protected final <T extends Definition> List<T> filter(List<T> definitions, String include) {
        return filterExcludeInclude(definitions, null, include);
    }

    protected final <T extends Definition> List<T> filter(List<T> definitions, List<String> include) {
        List<T> result = new ArrayList<>();

        for (String i : include)
            result.addAll(filter(definitions, i));

        return result;
    }

    protected final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions, String e, String i) {
        return filterExcludeInclude(definitions, new String[] { e }, new String[] { i != null ? i : ".*" }, emptyList());
    }

    protected final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions, String[] e, String[] i, List<Filter> f) {
        List<T> result = new ArrayList<>();

        definitionsLoop: for (T definition : definitions) {
            if (e != null) {
                for (String exclude : e) {
                    if (exclude != null && matches(patterns.pattern(exclude), definition)) {
                        if (log.isDebugEnabled())
                            log.debug("Exclude", "Excluding " + definition.getQualifiedName() + " because of pattern " + exclude);

                        continue definitionsLoop;
                    }
                }
            }

            if (i != null) {
                for (String include : i) {
                    if (include != null && matches(patterns.pattern(include), definition)) {

                        // [#3488] This allows for filtering out additional objects, in case the applicable
                        // code generation configuration might cause conflicts in resulting code
                        // [#3526] Filters should be applied last, after <exclude/> and <include/>
                        for (Filter filter : f) {
                            if (filter.exclude(definition)) {

                                if (log.isDebugEnabled())
                                    log.debug("Exclude", "Excluding " + definition.getQualifiedName() + " because of filter " + filter);

                                continue definitionsLoop;
                            }
                        }

                        result.add(definition);

                        if (log.isDebugEnabled())
                            log.debug("Include", "Including " + definition.getQualifiedName() + " because of pattern " + include);

                        continue definitionsLoop;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Retrieve ALL relations from the database.
     */
    protected final Relations getRelations0() {
        final DefaultRelations result = relations instanceof DefaultRelations ? (DefaultRelations) relations : new DefaultRelations();

        if (getIncludePrimaryKeys())
            onError(ERROR, "Error while fetching primary keys", () -> loadPrimaryKeys(result));

        if (getIncludeUniqueKeys())
            onError(ERROR, "Error while fetching unique keys", () -> loadUniqueKeys(result));

        if (getIncludeCheckConstraints())
            onError(ERROR, "Error while fetching check constraints", () -> loadCheckConstraints(result));

        if (getIncludePrimaryKeys()) {
            onError(ERROR, "Error while generating synthetic primary keys", () -> syntheticPrimaryKeys(result));
            onError(ERROR, "Error while generating overridden primary keys", () -> overridePrimaryKeys(result));
        }








        if (getIncludeForeignKeys())
            onError(ERROR, "Error while fetching foreign keys", () -> loadForeignKeys(result));








        return result;
    }

    @Override
    public final boolean isArrayType(String dataType) {
        switch (getDialect().family()) {









            case POSTGRES:
            case H2:
                return "ARRAY".equals(dataType.toUpperCase());


            case HSQLDB:
            default:
                // TODO: Is there any more robust way to recognise these?
                // For instance, there could be a UDT that is called this way
                return dataType.toUpperCase().endsWith(" ARRAY");
        }
    }

    protected static final String fetchedSize(Collection<?> fetched, Collection<?> included) {
        return fetched.size() + " (" + included.size() + " included, " + (fetched.size() - included.size()) + " excluded)";
    }

    @SuppressWarnings("unused")
    @Override
    public void setConfiguredComments(List<CommentType> configuredComments) {
        if (configuredComments != null) {
            getConfiguredComments().addAll(configuredComments);
            unusedComments.addAll(configuredComments);






            if (!configuredComments.isEmpty())
                log.info("Commercial feature", "Comments are a commercial only feature. Please upgrade to the jOOQ Professional Edition");
        }
    }

    @Override
    public List<CommentType> getConfiguredComments() {
        if (configuredComments == null)
            configuredComments = new ArrayList<>();

        return configuredComments;
    }

    @Override
    public void markUsed(CommentType object) {
        unusedComments.remove(object);
    }

    @Override
    public List<CommentType> getUnusedComments() {
        return new ArrayList<>(unusedComments);
    }

    @SuppressWarnings("unused")
    @Override
    public void setConfiguredSyntheticObjects(SyntheticObjectsType configuredSyntheticObjects) {
        if (configuredSyntheticObjects != null) {
            // [#8512] Some implementation of this database may have already
            // configured things programmatically, so we must not set the
            // list but append it.

            getConfiguredSyntheticIdentities().addAll(configuredSyntheticObjects.getIdentities());
            getConfiguredSyntheticPrimaryKeys().addAll(configuredSyntheticObjects.getPrimaryKeys());
            getConfiguredSyntheticUniqueKeys().addAll(configuredSyntheticObjects.getUniqueKeys());
            getConfiguredSyntheticForeignKeys().addAll(configuredSyntheticObjects.getForeignKeys());
            getConfiguredSyntheticViews().addAll(configuredSyntheticObjects.getViews());

            unusedSyntheticIdentities.addAll(configuredSyntheticObjects.getIdentities());
            unusedSyntheticPrimaryKeys.addAll(configuredSyntheticObjects.getPrimaryKeys());
            unusedSyntheticUniqueKeys.addAll(configuredSyntheticObjects.getUniqueKeys());
            unusedSyntheticForeignKeys.addAll(configuredSyntheticObjects.getForeignKeys());
            unusedSyntheticViews.addAll(configuredSyntheticObjects.getViews());






            if (!configuredSyntheticObjects.getUniqueKeys().isEmpty())
                log.info("Commercial feature", "Synthetic unique keys are a commercial only feature. Please upgrade to the jOOQ Professional Edition");
            if (!configuredSyntheticObjects.getForeignKeys().isEmpty())
                log.info("Commercial feature", "Synthetic foreign keys are a commercial only feature. Please upgrade to the jOOQ Professional Edition");
        }
    }

    @Override
    public List<SyntheticIdentityType> getConfiguredSyntheticIdentities() {
        if (configuredSyntheticIdentities == null)
            configuredSyntheticIdentities = new ArrayList<>();

        return configuredSyntheticIdentities;
    }

    @Override
    public List<SyntheticPrimaryKeyType> getConfiguredSyntheticPrimaryKeys() {
        if (configuredSyntheticPrimaryKeys == null)
            configuredSyntheticPrimaryKeys = new ArrayList<>();

        return configuredSyntheticPrimaryKeys;
    }

    @Override
    public List<SyntheticUniqueKeyType> getConfiguredSyntheticUniqueKeys() {
        if (configuredSyntheticUniqueKeys == null)
            configuredSyntheticUniqueKeys = new ArrayList<>();

        return configuredSyntheticUniqueKeys;
    }

    @Override
    public List<SyntheticForeignKeyType> getConfiguredSyntheticForeignKeys() {
        if (configuredSyntheticForeignKeys == null)
            configuredSyntheticForeignKeys = new ArrayList<>();

        return configuredSyntheticForeignKeys;
    }

    @Override
    public List<SyntheticViewType> getConfiguredSyntheticViews() {
        if (configuredSyntheticViews == null)
            configuredSyntheticViews = new ArrayList<>();

        return configuredSyntheticViews;
    }

    @Override
    public void markUsed(SyntheticIdentityType identity) {
        unusedSyntheticIdentities.remove(identity);
    }

    @Override
    public void markUsed(SyntheticPrimaryKeyType primaryKey) {
        unusedSyntheticPrimaryKeys.remove(primaryKey);
    }

    @Override
    public void markUsed(SyntheticUniqueKeyType uniqueKey) {
        unusedSyntheticUniqueKeys.remove(uniqueKey);
    }

    @Override
    public void markUsed(SyntheticForeignKeyType foreignKey) {
        unusedSyntheticForeignKeys.remove(foreignKey);
    }

    @Override
    public void markUsed(SyntheticViewType view) {
        unusedSyntheticViews.remove(view);
    }

    @Override
    public List<SyntheticIdentityType> getUnusedSyntheticIdentities() {
        return new ArrayList<>(unusedSyntheticIdentities);
    }

    @Override
    public List<SyntheticPrimaryKeyType> getUnusedSyntheticPrimaryKeys() {
        return new ArrayList<>(unusedSyntheticPrimaryKeys);
    }

    @Override
    public List<SyntheticUniqueKeyType> getUnusedSyntheticUniqueKeys() {
        return new ArrayList<>(unusedSyntheticUniqueKeys);
    }

    @Override
    public List<SyntheticForeignKeyType> getUnusedSyntheticForeignKeys() {
        return new ArrayList<>(unusedSyntheticForeignKeys);
    }

    @Override
    public List<SyntheticViewType> getUnusedSyntheticViews() {
        return new ArrayList<>(unusedSyntheticViews);
    }

    private final void overridePrimaryKeys(DefaultRelations r) {

        keyLoop:
        for (SyntheticPrimaryKeyType key : getConfiguredSyntheticPrimaryKeys()) {
            if (key.getKey() == null)
                continue keyLoop;

            for (TableDefinition table : filter(getTables(), key.getTables())) {
                for (UniqueKeyDefinition uk : filter(table.getKeys(), key.getKey())) {
                    log.info("Overriding primary key", "" + uk);
                    r.overridePrimaryKey(uk);
                    markUsed(key);
                }
            }
        }
    }

    private final void syntheticPrimaryKeys(DefaultRelations r) {

        keyLoop:
        for (SyntheticPrimaryKeyType key : getConfiguredSyntheticPrimaryKeys()) {
            if (key.getKey() != null)
                continue keyLoop;

            for (TableDefinition table : filter(getTables(), key.getTables())) {
                String keyName = key.getName() != null ? key.getName() : "SYNTHETIC_PK_" + table.getName();

                List<ColumnDefinition> columns = filter(table.getColumns(), key.getFields());
                if (!columns.isEmpty()) {
                    markUsed(key);

                    DefaultUniqueKeyDefinition pk = new DefaultUniqueKeyDefinition(table.getSchema(), keyName, table, true);
                    pk.getKeyColumns().addAll(columns);
                    log.info("Synthetic primary key", "" + pk);
                    r.overridePrimaryKey(pk);
                }
            }
        }
    }



























































    private final DataTypeDefinition type(SchemaDefinition schema, Field<?> field) {
        return new DefaultDataTypeDefinition(
            schema.getDatabase(),
            schema,
            field.getDataType().getTypeName(),
            field.getDataType().length(),
            field.getDataType().precision(),
            field.getDataType().scale(),
            field.getDataType().nullable(),
            field.getDataType().defaultValue() == null ? null : create().renderInlined(field.getDataType().defaultValue())
        );
    }

    private final void syntheticViews(final List<TableDefinition> t) {

        viewLoop:
        for (final SyntheticViewType view : getConfiguredSyntheticViews()) {
            final CatalogDefinition catalog = StringUtils.isBlank(view.getCatalog()) ? getCatalogs().get(0) : getCatalog(view.getCatalog());
            if (catalog == null)
                continue viewLoop;

            final SchemaDefinition schema = StringUtils.isBlank(view.getSchema()) ? catalog.getSchemata().get(0) : catalog.getSchema(view.getSchema());
            if (schema == null)
                continue viewLoop;

            onError(ERROR, "Error while parsing view", () -> {

                // TODO: Add a Meta implementation that is based on jOOQ-meta
                final Meta meta = create().meta();
                final List<Param<?>> params = new ArrayList<>();
                final Configuration configuration = create()
                    .configuration()
                    .deriveSettings(s -> s.withParseWithMetaLookups(ParseWithMetaLookups.THROW_ON_FAILURE))
                    .derive((MetaProvider) () -> meta);

                // [#8722] [#11054] Before a public API is available, use this internal, undocumented
                //                  API to collect params from the parser
                configuration.data("org.jooq.parser.param-collector", (Consumer<Param<?>>) param -> {
                    if (!param.isInline())
                        params.add(param);
                });

                final Select<?> select = configuration
                    .dsl()
                    .parser()
                    .parseSelect(view.getSql());

                final RoutineDefinition routine = params.isEmpty() ? null : new AbstractRoutineDefinition(schema, null, view.getName(), view.getComment(), null) {
                    @Override
                    protected void init0() throws SQLException {
                        int i = 0;

                        for (Param<?> param : params) {
                            addParameter(InOutDefinition.IN, new DefaultParameterDefinition(
                                this,
                                param.getParamName(),
                                ++i,
                                type(schema, param)
                            ));
                        }
                    }
                };

                t.add(new AbstractTableDefinition(schema, view.getName(), view.getComment(), routine == null ? TableType.VIEW : TableType.FUNCTION, view.getSql()) {

                    @Override
                    public boolean isSynthetic() {
                        return true;
                    }

                    @Override
                    protected List<ColumnDefinition> getElements0() throws SQLException {
                        List<ColumnDefinition> result = new ArrayList<>();

                        int i = 0;
                        for (Field<?> field : select.getSelect()) {
                            result.add(new DefaultColumnDefinition(
                                this,
                                field.getName(),
                                ++i,
                                type(getSchema(), field),
                                false,
                                field.getComment()
                            ));
                        }

                        return result;
                    }

                    @Override
                    protected List<ParameterDefinition> getParameters0() {
                        List<ParameterDefinition> result = new ArrayList<>();

                        if (routine != null) {
                            result.addAll(routine.getInParameters());
                        }

                        return result;
                    }
                });

                log.info("Synthetic view added", view.getName());
            });

            // TODO: Can we really have unused views?
            markUsed(view);
        }
    }

    @Override
    public void close() {}

    /**
     * Create a new Factory
     */
    protected abstract DSLContext create0();

    /**
     * Retrieve ALL source code from the database.
     */
    protected Map<Definition, String> getSources0() throws SQLException {
        return new LinkedHashMap<>();
    }

    /**
     * Retrieve ALL indexes from the database
     */
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        return Collections.emptyList();
    }

    /**
     * Retrieve primary keys and store them to relations
     */
    protected abstract void loadPrimaryKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve non-primary unique keys and store them to relations
     */
    protected abstract void loadUniqueKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve foreign keys and store them to relations. Unique keys are
     * already loaded.
     */
    protected abstract void loadForeignKeys(DefaultRelations r) throws SQLException;

    /**
     * Retrieve <code>CHECK</code> constraints and store them to relations.
     */
    protected abstract void loadCheckConstraints(DefaultRelations r) throws SQLException;

    /**
     * Retrieve ALL catalogs from the database. This will be filtered in
     * {@link #getCatalogs()}
     */
    protected abstract List<CatalogDefinition> getCatalogs0() throws SQLException;

    /**
     * Retrieve ALL schemata from the database. This will be filtered in
     * {@link #getSchemata()}
     */
    protected abstract List<SchemaDefinition> getSchemata0() throws SQLException;

    /**
     * Retrieve ALL sequences from the database. This will be filtered in
     * {@link #getTables(SchemaDefinition)}
     */
    protected abstract List<SequenceDefinition> getSequences0() throws SQLException;

    /**
     * Retrieve ALL tables from the database. This will be filtered in
     * {@link #getTables(SchemaDefinition)}
     */
    protected abstract List<TableDefinition> getTables0() throws SQLException;

    /**
     * Retrieve ALL stored routines (functions and procedures) from the
     * database. This will be filtered in {@link #getRoutines(SchemaDefinition)}
     */
    protected abstract List<RoutineDefinition> getRoutines0() throws SQLException;

    /**
     * Retrieve ALL packages from the database. This will be filtered in
     * {@link #getPackages(SchemaDefinition)}
     */
    protected abstract List<PackageDefinition> getPackages0() throws SQLException;

    /**
     * Retrieve ALL enum UDTs from the database. This will be filtered in
     * {@link #getEnums(SchemaDefinition)}
     */
    protected abstract List<EnumDefinition> getEnums0() throws SQLException;

    /**
     * Retrieve ALL domain UDTs from the database. This will be filtered in
     * {@link #getDomains(SchemaDefinition)}
     */
    protected abstract List<DomainDefinition> getDomains0() throws SQLException;

    /**
     * Retrieve ALL UDTs from the database. This will be filtered in
     * {@link #getEnums(SchemaDefinition)}
     */
    protected abstract List<UDTDefinition> getUDTs0() throws SQLException;

    /**
     * Retrieve ALL ARRAYs from the database. This will be filtered in
     * {@link #getArrays(SchemaDefinition)}
     */
    protected abstract List<ArrayDefinition> getArrays0() throws SQLException;

    /**
     * Get the data type considering a known max value
     */
    protected final DataTypeDefinition getDataTypeForMAX_VAL(SchemaDefinition schema, BigInteger value) {
        DataTypeDefinition type;

        if (BigInteger.valueOf(Byte.MAX_VALUE).compareTo(value) >= 0)
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 2, 0, false, (String) null);
        else if (BigInteger.valueOf(Short.MAX_VALUE).compareTo(value) >= 0)
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 4, 0, false, (String) null);
        else if (BigInteger.valueOf(Integer.MAX_VALUE).compareTo(value) >= 0)
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 9, 0, false, (String) null);
        else if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(value) >= 0)
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 18, 0, false, (String) null);
        else
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 38, 0, false, (String) null);

        return type;
    }

    @FunctionalInterface
    private interface ExceptionRunnable {
        void run() throws Exception;
    }

    private void onError(Log.Level level, String message, ExceptionRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            switch (onError()) {
                case SILENT:
                    break;
                case LOG:
                    log.log(level, message, e);
                    break;
                case FAIL:
                    throw new RuntimeException(e);
            }
        }
    }
}
