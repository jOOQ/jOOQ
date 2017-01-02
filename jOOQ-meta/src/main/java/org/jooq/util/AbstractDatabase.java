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

package org.jooq.util;

import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.util.AbstractTypedElementDefinition.customType;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.Name;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVReader;
import org.jooq.util.jaxb.Catalog;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.RegexFlag;
import org.jooq.util.jaxb.Schema;
// ...

/**
 * A base implementation for all types of databases.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDatabase implements Database {

    private static final JooqLogger                                          log = JooqLogger.getLogger(AbstractDatabase.class);

    // -------------------------------------------------------------------------
    // Configuration elements
    // -------------------------------------------------------------------------

    private Properties                                                       properties;
    private SQLDialect                                                       dialect;
    private Connection                                                       connection;
    private List<RegexFlag>                                                  regexFlags;
    private List<Filter>                                                     filters;
    private String[]                                                         excludes;
    private String[]                                                         includes;
    private boolean                                                          includeExcludeColumns;
    private boolean                                                          includeTables            = true;
    private boolean                                                          includeRoutines          = true;
    private boolean                                                          includePackages          = true;
    private boolean                                                          includeUDTs              = true;
    private boolean                                                          includeSequences         = true;
    private boolean                                                          includePrimaryKeys       = true;
    private boolean                                                          includeUniqueKeys        = true;
    private boolean                                                          includeForeignKeys       = true;
    private String[]                                                         recordVersionFields;
    private String[]                                                         recordTimestampFields;
    private String[]                                                         syntheticPrimaryKeys;
    private String[]                                                         overridePrimaryKeys;
    private String[]                                                         syntheticIdentities;
    private boolean                                                          supportsUnsignedTypes;
    private boolean                                                          ignoreProcedureReturnValues;
    private boolean                                                          dateAsTimestamp;
    private List<Catalog>                                                    configuredCatalogs;
    private List<Schema>                                                     configuredSchemata;
    private List<CustomType>                                                 configuredCustomTypes;
    private List<EnumType>                                                   configuredEnumTypes;
    private List<ForcedType>                                                 configuredForcedTypes;
    private SchemaVersionProvider                                            schemaVersionProvider;
    private CatalogVersionProvider                                           catalogVersionProvider;

    // -------------------------------------------------------------------------
    // Loaded definitions
    // -------------------------------------------------------------------------

    private List<String>                                                     inputCatalogs;
    private List<String>                                                     inputSchemata;
    private Map<String, List<String>>                                        inputSchemataPerCatalog;
    private List<CatalogDefinition>                                          catalogs;
    private List<SchemaDefinition>                                           schemata;
    private List<SequenceDefinition>                                         sequences;
    private List<IdentityDefinition>                                         identities;
    private List<UniqueKeyDefinition>                                        uniqueKeys;
    private List<ForeignKeyDefinition>                                       foreignKeys;
    private List<CheckConstraintDefinition>                                  checkConstraints;
    private List<TableDefinition>                                            tables;
    private List<EnumDefinition>                                             enums;
    private List<DomainDefinition>                                           domains;
    private List<UDTDefinition>                                              udts;
    private List<ArrayDefinition>                                            arrays;
    private List<RoutineDefinition>                                          routines;
    private List<PackageDefinition>                                          packages;
    private Relations                                                        relations;
    private boolean                                                          includeRelations     = true;
    private boolean                                                          tableValuedFunctions = true;

    private transient Map<SchemaDefinition, List<SequenceDefinition>>        sequencesBySchema;
    private transient Map<SchemaDefinition, List<IdentityDefinition>>        identitiesBySchema;
    private transient Map<SchemaDefinition, List<UniqueKeyDefinition>>       uniqueKeysBySchema;
    private transient Map<SchemaDefinition, List<ForeignKeyDefinition>>      foreignKeysBySchema;
    private transient Map<SchemaDefinition, List<CheckConstraintDefinition>> checkConstraintsBySchema;
    private transient Map<SchemaDefinition, List<TableDefinition>>           tablesBySchema;
    private transient Map<SchemaDefinition, List<EnumDefinition>>            enumsBySchema;
    private transient Map<SchemaDefinition, List<UDTDefinition>>             udtsBySchema;
    private transient Map<SchemaDefinition, List<ArrayDefinition>>           arraysBySchema;
    private transient Map<SchemaDefinition, List<RoutineDefinition>>         routinesBySchema;
    private transient Map<SchemaDefinition, List<PackageDefinition>>         packagesBySchema;
    private transient boolean                                                initialised;

    // Other caches
    private final List<Definition>                                           all;
    private final List<Definition>                                           included;
    private final List<Definition>                                           excluded;
    private final Map<Table<?>, Boolean>                                     exists;
    private final Map<String, Pattern>                                       patterns;

    protected AbstractDatabase() {
        exists = new HashMap<Table<?>, Boolean>();
        patterns = new HashMap<String, Pattern>();
        filters = new ArrayList<Filter>();
        all = new ArrayList<Definition>();
        included = new ArrayList<Definition>();
        excluded = new ArrayList<Definition>();
    }

    @Override
    public final SQLDialect getDialect() {
        if (dialect == null) {
            dialect = create().configuration().dialect();
        }

        return dialect;
    }

    @Override
    public final void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public final Connection getConnection() {
        return connection;
    }

    @Override
    public final DSLContext create() {
        return create(false);
    }

    @SuppressWarnings("serial")
    protected final DSLContext create(boolean muteExceptions) {

        // [#3800] Make sure that faulty queries are logged in a formatted
        //         way to help users provide us with bug reports
        final Configuration configuration = create0().configuration();

        if (muteExceptions) {
            return DSL.using(configuration);
        }
        else {
            final Settings newSettings = SettingsTools.clone(configuration.settings()).withRenderFormatted(true);
            final ExecuteListenerProvider[] oldProviders = configuration.executeListenerProviders();
            final ExecuteListenerProvider[] newProviders = new ExecuteListenerProvider[oldProviders.length + 1];
            System.arraycopy(oldProviders, 0, newProviders, 0, oldProviders.length);
            newProviders[oldProviders.length] = new DefaultExecuteListenerProvider(new DefaultExecuteListener() {

                class SQLPerformanceWarning extends Exception {}

                @Override
                public void start(ExecuteContext ctx) {

                    // [#4974] Prevent any class loading effects from impacting below
                    //         SQLPerformanceWarning.
                    if (!initialised) {
                        DSL.using(configuration).selectOne().fetch();
                        initialised = true;
                    }
                }

                @Override
                public void executeStart(ExecuteContext ctx) {
                    ctx.data("org.jooq.util.AbstractDatabase.watch", new StopWatch());
                }

                @Override
                public void executeEnd(ExecuteContext ctx) {
                    StopWatch watch = (StopWatch) ctx.data("org.jooq.util.AbstractDatabase.watch");

                    if (watch.split() > TimeUnit.SECONDS.toNanos(5L)) {
                        watch.splitWarn("Slow SQL");

                        log.warn(
                            "Slow SQL",
                            "jOOQ Meta executed a slow query (slower than 5 seconds)"
                          + "\n\n"
                          + "Please report this bug here: https://github.com/jOOQ/jOOQ/issues/new\n\n"
                          + formatted(ctx.query()),
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
                      + "Please report this bug here: https://github.com/jOOQ/jOOQ/issues/new\n\n"
                      + formatted(ctx.query()));
                }

                private String formatted(Query query) {
                    return DSL.using(configuration.derive(newSettings)).renderInlined(query);
                }
            });
            return DSL.using(configuration.derive(newProviders));
        }
    }

    @Override
    public final boolean exists(Table<?> table) {
        Boolean result = exists.get(table);

        if (result == null) {
            try {
                create(true)
                    .selectOne()
                    .from(table)
                    .where(falseCondition())
                    .fetch();

                result = true;
            }
            catch (DataAccessException e) {
                result = false;
            }

            exists.put(table, result);
        }

        return result;
    }

    @Override
    public final boolean existAll(Table<?>... t) {
        for (Table<?> table : t)
            if (!exists(table))
                return false;

        return true;
    }

    final Pattern pattern(String regex) {
        Pattern pattern = patterns.get(regex);

        if (pattern == null) {
            int flags = 0;

            List<RegexFlag> list = new ArrayList<RegexFlag>(getRegexFlags());

            // [#3860] This should really be handled by JAXB, but apparently, @XmlList and @XmlElement(defaultValue=...)
            // cannot be combined: http://stackoverflow.com/q/27528698/521799
            if (list.isEmpty()) {
                list.add(RegexFlag.COMMENTS);
                list.add(RegexFlag.CASE_INSENSITIVE);
            }

            for (RegexFlag flag : list) {
                switch (flag) {
                    case CANON_EQ:                flags |= Pattern.CANON_EQ;                break;
                    case CASE_INSENSITIVE:        flags |= Pattern.CASE_INSENSITIVE;        break;
                    case COMMENTS:                flags |= Pattern.COMMENTS;                break;
                    case DOTALL:                  flags |= Pattern.DOTALL;                  break;
                    case LITERAL:                 flags |= Pattern.LITERAL;                 break;
                    case MULTILINE:               flags |= Pattern.MULTILINE;               break;
                    case UNICODE_CASE:            flags |= Pattern.UNICODE_CASE;            break;
                    case UNICODE_CHARACTER_CLASS: flags |= 0x100;                           break; // Pattern.UNICODE_CHARACTER_CLASS: Java 1.7 only
                    case UNIX_LINES:              flags |= Pattern.UNIX_LINES;              break;
                }
            }

            pattern = Pattern.compile(regex, flags);
            patterns.put(regex, pattern);
        }

        return pattern;
    }

    @Override
    public final List<CatalogDefinition> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<CatalogDefinition>();

            try {
                catalogs = getCatalogs0();
            }
            catch (Exception e) {
                log.error("Could not load catalogs", e);
            }

//            Iterator<CatalogDefinition> it = catalogs.iterator();
//            while (it.hasNext()) {
//                CatalogDefinition catalog = it.next();
//
//                // [#4794] Add support for schema mapping
//                // if (!getInputSchemata().contains(catalog.getName()))
//                //    it.remove();
//            }
        }

        return catalogs;
    }

    @Override
    public final CatalogDefinition getCatalog(String inputName) {
        for (CatalogDefinition catalog : getCatalogs()) {
            if (catalog.getName().equals(inputName)) {
                return catalog;
            }
        }

        return null;
    }

    @Override
    public final List<SchemaDefinition> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<SchemaDefinition>();

            try {
                schemata = getSchemata0();
            }
            catch (Exception e) {
                log.error("Could not load schemata", e);
            }

            Iterator<SchemaDefinition> it = schemata.iterator();
            while (it.hasNext()) {
                SchemaDefinition schema = it.next();

                if (!getInputSchemata().contains(schema.getName()))
                    it.remove();
            }

            if (schemata.isEmpty())
                log.warn(
                    "No schemata were loaded",
                    "Please check your connection settings, and whether your database (and your database version!) is really supported by jOOQ. Also, check the case-sensitivity in your configured <inputSchema/> elements : " + inputSchemataPerCatalog);
        }

        return schemata;
    }

    @Override
    public final List<SchemaDefinition> getSchemata(CatalogDefinition catalog) {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

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
            inputCatalogs = new ArrayList<String>();

            // [#1312] Allow for ommitting inputSchema configuration. Generate
            // All catalogs instead
            if (configuredCatalogs.size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getInputCatalog())) {
                try {
                    for (CatalogDefinition catalog : getCatalogs0())
                        inputCatalogs.add(catalog.getName());
                }
                catch (Exception e) {
                    log.error("Could not load catalogs", e);
                }
            }
            else {
                for (Catalog catalog : configuredCatalogs) {





                    inputCatalogs.add(catalog.getInputCatalog());
                }
            }
        }

        return inputCatalogs;
    }

    @Override
    public final List<String> getInputSchemata() {
        if (inputSchemataPerCatalog == null) {
            inputSchemata = new ArrayList<String>();
            inputSchemataPerCatalog = new LinkedHashMap<String, List<String>>();

            // [#1312] Allow for ommitting inputSchema configuration. Generate all schemata instead.
            if (configuredSchemata.size() == 1 && StringUtils.isBlank(configuredSchemata.get(0).getInputSchema())) {
                initAllSchemata();
            }
            else if (configuredCatalogs.size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getInputCatalog())
                  && configuredCatalogs.get(0).getSchemata().size() == 1 && StringUtils.isBlank(configuredCatalogs.get(0).getSchemata().get(0).getInputSchema())) {
                initAllSchemata();
            }
            else if (configuredCatalogs.isEmpty()) {
                inputSchemataPerCatalog.put("", inputSchemata);

                for (Schema schema : configuredSchemata) {








                    {
                        inputSchemata.add(schema.getInputSchema());
                    }
                }
            }
            else {
                for (Catalog catalog : configuredCatalogs) {
                    for (Schema schema : catalog.getSchemata()) {
                        String inputSchema;









                        {
                            inputSchema = schema.getInputSchema();
                        }

                        inputSchemata.add(inputSchema);
                        List<String> list = inputSchemataPerCatalog.get(catalog.getInputCatalog());

                        if (list == null) {
                            list = new ArrayList<String>();
                            inputSchemataPerCatalog.put(catalog.getInputCatalog(), list);
                        }

                        list.add(inputSchema);
                    }
                }
            }
        }

        return inputSchemata;
    }

    private void initAllSchemata() {
        try {
            for (SchemaDefinition schema : getSchemata0()) {
                inputSchemata.add(schema.getName());
                List<String> list = inputSchemataPerCatalog.get(schema.getCatalog().getName());

                if (list == null) {
                    list = new ArrayList<String>();
                    inputSchemataPerCatalog.put(schema.getCatalog().getName(), list);
                }

                list.add(schema.getName());
            }
        }
        catch (Exception e) {
            log.error("Could not load schemata", e);
        }
    }

    @Override
    public final List<String> getInputSchemata(CatalogDefinition catalog) {
        return getInputSchemata(catalog.getInputName());
    }

    @Override
    public final List<String> getInputSchemata(String catalog) {

        // Init if necessary
        getInputSchemata();
        return inputSchemataPerCatalog.containsKey(catalog)
            ? inputSchemataPerCatalog.get(catalog)
            : Collections.<String>emptyList();
    }

    @Override
    @Deprecated
    public String getOutputCatalog(String inputCatalog) {
        for (Catalog catalog : configuredCatalogs)
            if (inputCatalog.equals(catalog.getInputCatalog()))
                return catalog.getOutputCatalog();

        return inputCatalog;
    }

    @Override
    @Deprecated
    public String getOutputSchema(String inputSchema) {
        for (Schema schema : configuredSchemata)
            if (inputSchema.equals(schema.getInputSchema()))
                return schema.getOutputSchema();

        return inputSchema;
    }

    @Override
    public String getOutputSchema(String inputCatalog, String inputSchema) {
        for (Catalog catalog : configuredCatalogs)
            if (inputCatalog.equals(catalog.getInputCatalog()))
                for (Schema schema : catalog.getSchemata())
                    if (inputSchema.equals(schema.getInputSchema()))
                        return schema.getOutputSchema();

        return inputSchema;
    }

    @Override
    public final void setConfiguredCatalogs(List<Catalog> catalogs) {
        this.configuredCatalogs = catalogs;
    }

    @Override
    public final void setConfiguredSchemata(List<Schema> schemata) {
        this.configuredSchemata = schemata;
    }

    @Override
    public final void setProperties(Properties properties) {
        this.properties = properties;
    }

    public final Properties getProperties() {
        return properties;
    }

    @Override
    public final List<Filter> getFilters() {
        if (filters == null) {
            filters = new ArrayList<Filter>();
        }

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
        if (excludes == null) {
            excludes = new String[0];
        }

        return excludes;
    }

    @Override
    public final void setIncludes(String[] includes) {
        this.includes = includes;
    }

    @Override
    public final String[] getIncludes() {
        if (includes == null) {
            includes = new String[0];
        }

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
    public final boolean getIncludeTables() {
        return includeTables;
    }

    @Override
    public final void setIncludeTables(boolean includeTables) {
        this.includeTables = includeTables;
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
    public final boolean getIncludePackages() {
        return includePackages;
    }

    @Override
    public final void setIncludePackages(boolean includePackages) {
        this.includePackages = includePackages;
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
    public final boolean getIncludeSequences() {
        return includeSequences;
    }

    @Override
    public final void setIncludeSequences(boolean includeSequences) {
        this.includeSequences = includeSequences;
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
        this.regexFlags = regexFlags;
    }

    @Override
    public final List<RegexFlag> getRegexFlags() {
        if (regexFlags == null) {
            regexFlags = new ArrayList<RegexFlag>();
        }

        return regexFlags;
    }

    @Override
    public void setRecordVersionFields(String[] recordVersionFields) {
        this.recordVersionFields = recordVersionFields;
    }

    @Override
    public String[] getRecordVersionFields() {
        if (recordVersionFields == null) {
            recordVersionFields = new String[0];
        }

        return recordVersionFields;
    }

    @Override
    public void setRecordTimestampFields(String[] recordTimestampFields) {
        this.recordTimestampFields = recordTimestampFields;
    }

    @Override
    public String[] getRecordTimestampFields() {
        if (recordTimestampFields == null) {
            recordTimestampFields = new String[0];
        }

        return recordTimestampFields;
    }

    @Override
    public void setSyntheticPrimaryKeys(String[] syntheticPrimaryKeys) {
        this.syntheticPrimaryKeys = syntheticPrimaryKeys;
    }

    @Override
    public String[] getSyntheticPrimaryKeys() {
        if (syntheticPrimaryKeys == null) {
            syntheticPrimaryKeys = new String[0];
        }

        return syntheticPrimaryKeys;
    }

    @Override
    public void setOverridePrimaryKeys(String[] overridePrimaryKeys) {
        this.overridePrimaryKeys = overridePrimaryKeys;
    }

    @Override
    public String[] getOverridePrimaryKeys() {
        if (overridePrimaryKeys == null) {
            overridePrimaryKeys = new String[0];
        }

        return overridePrimaryKeys;
    }

    @Override
    public void setSyntheticIdentities(String[] syntheticIdentities) {
        this.syntheticIdentities = syntheticIdentities;
    }

    @Override
    public final String[] getSyntheticIdentities() {
        if (syntheticIdentities == null) {
            syntheticIdentities = new String[0];
        }
        return syntheticIdentities;
    }

    @Override
    public final void setConfiguredEnumTypes(List<EnumType> configuredEnumTypes) {
        this.configuredEnumTypes = configuredEnumTypes;
    }

    @Override
    public final List<EnumType> getConfiguredEnumTypes() {
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
        if (configuredCustomTypes == null) {
            configuredCustomTypes = new ArrayList<CustomType>();
        }

        return configuredCustomTypes;
    }

    @Override
    @Deprecated
    public final CustomType getConfiguredCustomType(String typeName) {

        // The user type name that is passed here can be null.
        if (typeName == null)
            return null;

        Iterator<CustomType> it1 = configuredCustomTypes.iterator();

        while (it1.hasNext()) {
            CustomType type = it1.next();

            if (type == null || (type.getName() == null && type.getType() == null)) {
                try {
                    StringWriter writer = new StringWriter();
                    JAXB.marshal(type, writer);
                    log.warn("Invalid custom type encountered: " + writer.toString());
                }
                catch (Exception e) {
                    log.warn("Invalid custom type encountered: " + type);
                }

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

            if (StringUtils.isBlank(type.getName())) {
                if (StringUtils.isBlank(type.getUserType())) {
                    log.warn("Bad configuration for <forcedType/>. Either <name/> or <userType/> is required: " + toString(type));

                    it2.remove();
                    continue;
                }

                if (StringUtils.isBlank(type.getBinding()) && StringUtils.isBlank(type.getConverter())) {
                    log.warn("Bad configuration for <forcedType/>. Either <binding/> or <converter/> is required: " + toString(type));

                    it2.remove();
                    continue;
                }
            }
            else {
                if (!StringUtils.isBlank(type.getUserType())) {
                    log.warn("Bad configuration for <forcedType/>. <userType/> is not allowed when <name/> is provided: " + toString(type));
                    type.setUserType(null);
                }
                if (!StringUtils.isBlank(type.getBinding())) {
                    log.warn("Bad configuration for <forcedType/>. <binding/> is not allowed when <name/> is provided: " + toString(type));
                    type.setBinding(null);
                }
                if (!StringUtils.isBlank(type.getConverter())) {
                    log.warn("Bad configuration for <forcedType/>. <converter/> is not allowed when <name/> is provided: " + toString(type));
                    type.setConverter(null);
                }
            }

            if (type.getUserType() != null && StringUtils.equals(type.getUserType(), typeName)) {
                return customType(this, type);
            }
        }

        return null;
    }

    private final String toString(ForcedType type) {
        StringWriter writer = new StringWriter();
        JAXB.marshal(type, writer);
        return writer.toString();
    }

    @Override
    public final void setConfiguredForcedTypes(List<ForcedType> configuredForcedTypes) {
        this.configuredForcedTypes = configuredForcedTypes;
    }

    @Override
    public final List<ForcedType> getConfiguredForcedTypes() {
        if (configuredForcedTypes == null) {
            configuredForcedTypes = new ArrayList<ForcedType>();
        }

        return configuredForcedTypes;
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
    public final void setSupportsUnsignedTypes(boolean supportsUnsignedTypes) {
        this.supportsUnsignedTypes = supportsUnsignedTypes;
    }

    @Override
    public final boolean supportsUnsignedTypes() {
        return supportsUnsignedTypes;
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
    public final void setIncludeRelations(boolean includeRelations) {
        this.includeRelations = includeRelations;
    }

    @Override
    public final boolean includeRelations() {
        return includeRelations;
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
    public final List<SequenceDefinition> getSequences(SchemaDefinition schema) {
        if (sequences == null) {
            sequences = new ArrayList<SequenceDefinition>();

            if (getIncludeSequences()) {
                try {
                    List<SequenceDefinition> s = getSequences0();

                    sequences = filterExcludeInclude(s);
                    log.info("Sequences fetched", fetchedSize(s, sequences));
                }
                catch (Exception e) {
                    log.error("Error while fetching sequences", e);
                }
            }
            else
                log.info("Sequences excluded");
        }

        if (sequencesBySchema == null)
            sequencesBySchema = new LinkedHashMap<SchemaDefinition, List<SequenceDefinition>>();

        return filterSchema(sequences, schema, sequencesBySchema);
    }

    @Override
    public final List<IdentityDefinition> getIdentities(SchemaDefinition schema) {
        if (identities == null) {
            identities = new ArrayList<IdentityDefinition>();

            for (SchemaDefinition s : getSchemata()) {
                for (TableDefinition table : getTables(s)) {
                    IdentityDefinition identity = table.getIdentity();

                    if (identity != null)
                        identities.add(identity);
                }
            }
        }

        if (identitiesBySchema == null)
            identitiesBySchema = new LinkedHashMap<SchemaDefinition, List<IdentityDefinition>>();

        return filterSchema(identities, schema, identitiesBySchema);
    }



    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
        if (uniqueKeys == null) {
            uniqueKeys = new ArrayList<UniqueKeyDefinition>();

            if (getIncludeUniqueKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        for (UniqueKeyDefinition uniqueKey : table.getUniqueKeys())
                            uniqueKeys.add(uniqueKey);
        }

        if (uniqueKeysBySchema == null)
            uniqueKeysBySchema = new LinkedHashMap<SchemaDefinition, List<UniqueKeyDefinition>>();

        return filterSchema(uniqueKeys, schema, uniqueKeysBySchema);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema) {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<ForeignKeyDefinition>();

            if (getIncludeForeignKeys())
                for (SchemaDefinition s : getSchemata())
                    for (TableDefinition table : getTables(s))
                        for (ForeignKeyDefinition foreignKey : table.getForeignKeys())
                            foreignKeys.add(foreignKey);
        }

        if (foreignKeysBySchema == null)
            foreignKeysBySchema = new LinkedHashMap<SchemaDefinition, List<ForeignKeyDefinition>>();

        return filterSchema(foreignKeys, schema, foreignKeysBySchema);
    }

    @Override
    public final List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema) {
        if (checkConstraints == null) {
            checkConstraints = new ArrayList<CheckConstraintDefinition>();

            for (SchemaDefinition s : getSchemata())
                for (TableDefinition table : getTables(s))
                    for (CheckConstraintDefinition checkConstraint : table.getCheckConstraints())
                        checkConstraints.add(checkConstraint);
        }

        if (checkConstraintsBySchema == null)
            checkConstraintsBySchema = new LinkedHashMap<SchemaDefinition, List<CheckConstraintDefinition>>();

        return filterSchema(checkConstraints, schema, checkConstraintsBySchema);
    }

    @Override
    public final List<TableDefinition> getTables(SchemaDefinition schema) {
        if (tables == null) {
            tables = new ArrayList<TableDefinition>();

            if (getIncludeTables()) {
                try {
                    List<TableDefinition> t = getTables0();

                    tables = filterExcludeInclude(t);
                    log.info("Tables fetched", fetchedSize(t, tables));
                }
                catch (Exception e) {
                    log.error("Error while fetching tables", e);
                }
            }
            else
                log.info("Tables excluded");
        }

        if (tablesBySchema == null)
            tablesBySchema = new LinkedHashMap<SchemaDefinition, List<TableDefinition>>();

        return filterSchema(tables, schema, tablesBySchema);
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
            enums = new ArrayList<EnumDefinition>();

            try {
                List<EnumDefinition> e = getEnums0();

                enums = filterExcludeInclude(e);
                enums.addAll(getConfiguredEnums());

                log.info("Enums fetched", fetchedSize(e, enums));
            }
            catch (Exception e) {
                log.error("Error while fetching enums", e);
            }
        }

        if (enumsBySchema == null)
            enumsBySchema = new LinkedHashMap<SchemaDefinition, List<EnumDefinition>>();

        return filterSchema(enums, schema, enumsBySchema);
    }

    private final List<EnumDefinition> getConfiguredEnums() {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        for (EnumType enumType : configuredEnumTypes) {
            String name = enumType.getName();
            DefaultEnumDefinition e = new DefaultEnumDefinition(getSchemata().get(0), name, null, true);

            String literals = enumType.getLiterals();

            try {
                @SuppressWarnings("resource")
                CSVReader reader = new CSVReader(new StringReader(literals));
                e.addLiterals(reader.readNext());
            } catch (IOException ignore) {}

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
        forcedTypeLoop:
        for (ForcedType forcedType : getConfiguredForcedTypes()) {
            String expression = forcedType.getExpression();

            if (forcedType.getExpressions() != null) {
                expression = forcedType.getExpressions();
                log.warn("DEPRECATED", "The <expressions/> element in <forcedType/> is deprecated. Use <expression/> instead");
            }

            String types = forcedType.getTypes();

            if (expression != null) {
                Pattern p = pattern(expression);

                if (     !p.matcher(definition.getName()).matches()
                      && !p.matcher(definition.getQualifiedName()).matches()) {
                    continue forcedTypeLoop;
                }
            }

            if (types != null && definedType != null) {
                Pattern p = pattern(types);

                if (    ( !p.matcher(definedType.getType()).matches() )
                     && (     definedType.getLength() == 0
                     ||   !p.matcher(definedType.getType() + "(" + definedType.getLength() + ")").matches())
                     && (     definedType.getScale() != 0
                     ||   !p.matcher(definedType.getType() + "(" + definedType.getPrecision() + ")").matches())
                     && ( !p.matcher(definedType.getType() + "(" + definedType.getPrecision() + "," + definedType.getScale() + ")").matches() )
                     && ( !p.matcher(definedType.getType() + "(" + definedType.getPrecision() + ", " + definedType.getScale() + ")").matches() )) {
                    continue forcedTypeLoop;
                }
            }

            return forcedType;
        }

        return null;
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
    public final List<DomainDefinition> getDomains(SchemaDefinition schema) {
        if (domains == null) {
            domains = new ArrayList<DomainDefinition>();

            try {
                List<DomainDefinition> e = getDomains0();

                domains = filterExcludeInclude(e);
                log.info("Domains fetched", fetchedSize(e, domains));
            }
            catch (Exception e) {
                log.error("Error while fetching domains", e);
            }
        }

        return domains;
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
            arrays = new ArrayList<ArrayDefinition>();

            if (getIncludeUDTs()) {
                try {
                    List<ArrayDefinition> a = getArrays0();

                    arrays = filterExcludeInclude(a);
                    log.info("ARRAYs fetched", fetchedSize(a, arrays));
                }
                catch (Exception e) {
                    log.error("Error while fetching ARRAYS", e);
                }
            }
            else
                log.info("ARRAYs excluded");
        }

        if (arraysBySchema == null)
            arraysBySchema = new LinkedHashMap<SchemaDefinition, List<ArrayDefinition>>();

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

    private final List<UDTDefinition> getAllUDTs(SchemaDefinition schema) {
        if (udts == null) {
            udts = new ArrayList<UDTDefinition>();

            if (getIncludeUDTs()) {
                try {
                    List<UDTDefinition> u = getUDTs0();

                    udts = filterExcludeInclude(u);
                    log.info("UDTs fetched", fetchedSize(u, udts));
                }
                catch (Exception e) {
                    log.error("Error while fetching udts", e);
                }
            }
            else
                log.info("UDTs excluded");
        }

        if (udtsBySchema == null)
            udtsBySchema = new LinkedHashMap<SchemaDefinition, List<UDTDefinition>>();

        return filterSchema(udts, schema, udtsBySchema);
    }

    private final List<UDTDefinition> ifInPackage(List<UDTDefinition> allUDTs, boolean expected) {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (UDTDefinition u : allUDTs)
            if ((u.getPackage() != null) == expected)
                result.add(u);

        return result;
    }

    @Override
    public final List<UDTDefinition> getUDTs(SchemaDefinition schema) {
        return getAllUDTs(schema);
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
        return ifInPackage(getAllUDTs(pkg.getSchema()), true);
    }

    @Override
    public final Relations getRelations() {
        if (relations == null) {
            relations = new DefaultRelations();

            // [#3559] If the code generator doesn't need relation information, we shouldn't
            // populate them here to avoid running potentially expensive queries.
            if (includeRelations) {
                try {
                    relations = getRelations0();
                }
                catch (Exception e) {
                    log.error("Error while fetching relations", e);
                }
            }
        }

        return relations;
    }

    @Override
    public final List<RoutineDefinition> getRoutines(SchemaDefinition schema) {
        if (routines == null) {
            routines = new ArrayList<RoutineDefinition>();

            if (getIncludeRoutines()) {
                try {
                    List<RoutineDefinition> r = getRoutines0();

                    routines = filterExcludeInclude(r);
                    log.info("Routines fetched", fetchedSize(r, routines));
                }
                catch (Exception e) {
                    log.error("Error while fetching functions", e);
                }
            }
            else
                log.info("Routines excluded");
        }

        if (routinesBySchema == null)
            routinesBySchema = new LinkedHashMap<SchemaDefinition, List<RoutineDefinition>>();

        return filterSchema(routines, schema, routinesBySchema);
    }

    @Override
    public final List<PackageDefinition> getPackages(SchemaDefinition schema) {
        if (packages == null) {
            packages = new ArrayList<PackageDefinition>();

            if (getIncludePackages()) {
                try {
                    List<PackageDefinition> p = getPackages0();

                    packages = filterExcludeInclude(p);
                    log.info("Packages fetched", fetchedSize(p, packages));
                }
                catch (Exception e) {
                    log.error("Error while fetching packages", e);
                }
            }
            else
                log.info("Packages excluded");
        }

        if (packagesBySchema == null)
            packagesBySchema = new LinkedHashMap<SchemaDefinition, List<PackageDefinition>>();

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
        for (D definition : definitions) {
            if ((ignoreCase && definition.getName().equalsIgnoreCase(name)) ||
                (!ignoreCase && definition.getName().equals(name))) {

                return definition;
            }
        }

        return null;
    }

    protected static final <D extends Definition> D getDefinition(List<D> definitions, Name name, boolean ignoreCase) {
        for (D definition : definitions) {
            if ((ignoreCase && definition.getQualifiedNamePart().equalsIgnoreCase(name)) ||
                (!ignoreCase && definition.getQualifiedNamePart().equals(name))) {

                return definition;
            }
        }

        return null;
    }

    protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema, Map<SchemaDefinition, List<T>> cache) {
        List<T> result = cache.get(schema);

        if (result == null) {
            result = filterSchema(definitions, schema);
            cache.put(schema, result);
        }

        return result;
    }

    protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema) {
        if (schema == null) {
            return definitions;
        }
        else {
            List<T> result = new ArrayList<T>();

            for (T definition : definitions)
                if (definition.getSchema().equals(schema))
                    result.add(definition);

            return result;
        }
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

    protected final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions, String[] e, String[] i, List<Filter> f) {
        List<T> result = new ArrayList<T>();

        definitionsLoop: for (T definition : definitions) {
            if (e != null) {
                for (String exclude : e) {
                    Pattern p = pattern(exclude);

                    if (exclude != null &&
                            (p.matcher(definition.getName()).matches() ||
                             p.matcher(definition.getQualifiedName()).matches())) {

                        if (log.isDebugEnabled())
                            log.debug("Exclude", "Excluding " + definition.getQualifiedName() + " because of pattern " + exclude);

                        continue definitionsLoop;
                    }
                }
            }

            if (i != null) {
                for (String include : i) {
                    Pattern p = pattern(include);

                    if (include != null &&
                            (p.matcher(definition.getName()).matches() ||
                             p.matcher(definition.getQualifiedName()).matches())) {

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
        DefaultRelations result = new DefaultRelations();

        try {
            loadPrimaryKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching primary keys", e);
        }

        try {
            loadUniqueKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching unique keys", e);
        }

        try {
            loadForeignKeys(result);
        }
        catch (Exception e) {
            log.error("Error while fetching foreign keys", e);
        }

        try {
            loadCheckConstraints(result);
        }
        catch (Exception e) {
            log.error("Error while fetching check constraints", e);
        }

        try {
            syntheticPrimaryKeys(result);
        }
        catch (Exception e) {
            log.error("Error while generating synthetic primary keys", e);
        }

        try {
            overridePrimaryKeys(result);
        }
        catch (Exception e) {
            log.error("Error while overriding primary keys", e);
        }

        return result;
    }

    @Override
    public final boolean isArrayType(String dataType) {
        switch (getDialect().family()) {



            case POSTGRES:
            case H2:
                return "ARRAY".equals(dataType);

            case HSQLDB:



                // TODO: Is there any more robust way to recognise these?
                // For instance, there could be a UDT that is called this way
                return dataType.endsWith(" ARRAY");
        }

        return false;
    }

    protected static final String fetchedSize(List<?> fetched, List<?> included) {
        return fetched.size() + " (" + included.size() + " included, " + (fetched.size() - included.size()) + " excluded)";
    }

    private final void syntheticPrimaryKeys(DefaultRelations r) {
        List<UniqueKeyDefinition> syntheticKeys = new ArrayList<UniqueKeyDefinition>();

        for (SchemaDefinition schema : getSchemata()) {
            for (TableDefinition table : schema.getTables()) {
                List<ColumnDefinition> columns = filterExcludeInclude(table.getColumns(), null, getSyntheticPrimaryKeys(), filters);

                if (!columns.isEmpty()) {
                    DefaultUniqueKeyDefinition syntheticKey = new DefaultUniqueKeyDefinition(schema, "SYNTHETIC_PK_" + table.getName(), table, true);
                    syntheticKey.getKeyColumns().addAll(columns);
                    syntheticKeys.add(syntheticKey);
                }
            }
        }

        log.info("Synthetic primary keys", fetchedSize(syntheticKeys, syntheticKeys));

        for (UniqueKeyDefinition key : syntheticKeys) {
            r.overridePrimaryKey(key);
        }
    }

    private final void overridePrimaryKeys(DefaultRelations r) {
        List<UniqueKeyDefinition> allKeys = r.getUniqueKeys();
        List<UniqueKeyDefinition> filteredKeys = filterExcludeInclude(allKeys, null, overridePrimaryKeys, filters);

        log.info("Overriding primary keys", fetchedSize(allKeys, filteredKeys));

        for (UniqueKeyDefinition key : filteredKeys) {
            r.overridePrimaryKey(key);
        }
    }

    /**
     * Create a new Factory
     */
    protected abstract DSLContext create0();

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

        if (BigInteger.valueOf(Byte.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 2, 0, false, (String) null);
        }
        else if (BigInteger.valueOf(Short.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 4, 0, false, (String) null);
        }
        else if (BigInteger.valueOf(Integer.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 9, 0, false, (String) null);
        }
        else if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(value) >= 0) {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 18, 0, false, (String) null);
        }
        else {
            type = new DefaultDataTypeDefinition(this, schema, SQLDataType.NUMERIC.getTypeName(), 0, 38, 0, false, (String) null);
        }

        return type;
    }
}
