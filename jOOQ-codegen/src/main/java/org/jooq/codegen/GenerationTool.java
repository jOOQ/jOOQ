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
package org.jooq.codegen;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.defaultString;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.sql.DataSource;

import org.jooq.Constants;
import org.jooq.DSLContext;
import org.jooq.Log.Level;
import org.jooq.SQLDialect;
import org.jooq.Source;
import org.jooq.impl.DSL;
import org.jooq.meta.CatalogVersionProvider;
import org.jooq.meta.Database;
import org.jooq.meta.Databases;
import org.jooq.meta.Definition;
import org.jooq.meta.SchemaVersionProvider;
import org.jooq.meta.jaxb.CatalogMappingType;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Logging;
import org.jooq.meta.jaxb.Matchers;
import org.jooq.meta.jaxb.OnError;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.SchemaMappingType;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;
// ...
import org.jooq.tools.ClassUtils;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.reflect.CompileOptions;
import org.jooq.tools.reflect.Reflect;
import org.jooq.util.jaxb.tools.MiniJAXB;


/**
 * The GenerationTool takes care of generating Java code from a database schema.
 * <p>
 * It takes its configuration parameters from an XML file passed in either as a
 * JAXB-annotated {@link Configuration} object, or from the file system when
 * passed as an argument to {@link #main(String[])}.
 * <p>
 * See <a href="http://www.jooq.org/xsd/">http://www.jooq.org/xsd/</a> for the
 * latest XSD specification.
 *
 * @author Lukas Eder
 */
public class GenerationTool {

    public static final String      DEFAULT_TARGET_ENCODING    = "UTF-8";
    public static final String      DEFAULT_TARGET_DIRECTORY   = "target/generated-sources/jooq";
    public static final String      DEFAULT_TARGET_PACKAGENAME = "org.jooq.generated";

    private static final JooqLogger log                        = JooqLogger.getLogger(GenerationTool.class);
    private static final JooqLogger unusedLogger               = JooqLogger.getLogger(Unused.class);

    private static class Unused {}

    private ClassLoader             loader;
    private DataSource              dataSource;
    private Connection              connection;
    private DSLContext              ctx;
    private Boolean                 autoCommit;
    private boolean                 close;

    /**
     * The class loader to use with this generation tool.
     * <p>
     * If set, all classes are loaded with this class loader
     */
    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    /**
     * The JDBC connection to use with this generation tool.
     * <p>
     * If set, the configuration XML's <code>&lt;jdbc/&gt;</code> configuration is
     * ignored, and this connection is used for meta data inspection, instead.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.ctx = DSL.using(connection);
    }

    /**
     * The JDBC data source to use with this generation tool.
     * <p>
     * If set, the configuration XML's <code>&lt;jdbc/&gt;</code> configuration is
     * ignored, and this connection is used for meta data inspection, instead.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) throws Exception {
        JooqLogger.initSimpleFormatter();
        String[] files;

        if (args.length > 0) {
            files = args;
        }
        else {
            String property = System.getProperty("jooq.codegen.configurationFile");

            if (property != null) {
                files = new String[] { property };
            }
            else {
                log.error("Usage : GenerationTool <configuration-file>");
                System.exit(-1);
                return;
            }
        }

        for (String file : files) {
            InputStream in = GenerationTool.class.getResourceAsStream(file);

            try {

                // [#2932] Retry loading the file, if it wasn't found. This may be helpful
                // to some users who were unaware that this file is loaded from the classpath
                if (in == null && !file.startsWith("/"))
                    in = GenerationTool.class.getResourceAsStream("/" + file);

                // [#3668] Also check the local file system for configuration files
                if (in == null && new File(file).exists())
                    in = new FileInputStream(file);

                if (in == null) {
                    log.error("Cannot find " + file + " on classpath, or in directory " + new File(".").getCanonicalPath());
                    log.error("-----------");
                    log.error("Please be sure it is located");
                    log.error("  - on the classpath and qualified as a classpath location.");
                    log.error("  - in the local directory or at a global path in the file system.");

                    System.exit(-1);
                    return;
                }

                // [#10463] Make sure logging threshold is set, in this special case
                Configuration configuration = load(in);
                setGlobalLoggingThreshold(configuration);
                log.info("Initialising properties", file);
                generate(configuration);
            }
            catch (Exception e) {
                log.error("Error in file: " + file + ". Error : " + e.getMessage(), e);

                System.exit(-1);
                return;
            }
            finally {
                if (in != null)
                    in.close();
            }
        }
    }

    /**
     * @deprecated - Use {@link #generate(Configuration)} instead
     */
    @Deprecated
    public static void main(Configuration configuration) throws Exception {
        new GenerationTool().run(configuration);
    }

    public static void generate(String xml) throws Exception {
        new GenerationTool().run(load(new ByteArrayInputStream(xml.getBytes(DEFAULT_TARGET_ENCODING))));
    }

    public static void generate(Configuration configuration) throws Exception {
        new GenerationTool().run(configuration);
    }

    public void run(Configuration configuration) throws Exception {
        try {
            run0(configuration);
        }
        catch (Exception e) {
            OnError onError = configuration.getOnError();
            if (onError == null) {
                onError = OnError.FAIL;
            }
            switch (onError) {
                case SILENT:
                    break;
                case LOG:
                    log.warn("Code generation failed", e);
                    break;
                case FAIL:
                    throw e;
            }
        }
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private void run0(Configuration configuration) throws Exception {
        // Trigger logging of jOOQ logo eagerly already here
        selectOne().toString();
        boolean propertyOverride = "true".equalsIgnoreCase(System.getProperty("jooq.codegen.propertyOverride"));

        if (configuration.getLogging() != null) {
            setGlobalLoggingThreshold(configuration);
        }
        else {
            String property = System.getProperty("jooq.codegen.logging");

            if (property != null) {
                try {
                    Logging.valueOf(property);
                }
                catch (IllegalArgumentException e) {
                    log.error("Unsupported property", "Unsupported value for system property jooq.codegen.logging: " + property + ". Supported values include: " + Arrays.asList(Logging.values()));
                }
            }
        }

        if (Boolean.getBoolean("jooq.codegen.skip")) {
            log.info("Skipping jOOQ code generation");
            return;
        }

        if (log.isDebugEnabled())
            log.debug("Input configuration", "" + configuration);

        // [#9727] The Maven plugin will have set the basedir to Maven's ${basedir}.
        //         Standalone code generation should use the JVM's working dir as basedir, by default.
        if (configuration.getBasedir() == null)
            configuration.setBasedir(new File(".").getAbsolutePath());

        Jdbc j = configuration.getJdbc();
        org.jooq.meta.jaxb.Generator g = configuration.getGenerator();
        if (g == null)
            throw new GeneratorException("The <generator/> tag is mandatory. For details, see " + Constants.NS_CODEGEN);

        // [#3669] Optional Database element
        if (g.getDatabase() == null)
            g.setDatabase(new org.jooq.meta.jaxb.Database());
        org.jooq.meta.jaxb.Database d = g.getDatabase();
        String databaseName = trim(d.getName());

        // [#1394] The <generate/> element and some others should be optional
        if (g.getGenerate() == null)
            g.setGenerate(new Generate());
        if (g.getStrategy() == null)
            g.setStrategy(new Strategy());
        if (g.getTarget() == null)
            g.setTarget(new Target());

        // [#9744] The <locale/> is also needed in GenerationTool:
        Locale locale = Locale.getDefault();
        if (!StringUtils.isBlank(g.getTarget().getLocale()))
            locale = Locale.forLanguageTag(g.getTarget().getLocale());

        Database database = null;

        try {

            // Initialise connection
            // ---------------------
            if (connection == null) {
                close = true;

                if (dataSource != null) {
                    setConnection(dataSource.getConnection());
                }
                else {
                    j = defaultIfNull(j, new Jdbc());

                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.driver", Jdbc::getDriver, Jdbc::setDriver);
                    set(j, propertyOverride, Jdbc::getUrlProperty, "jooq.codegen.jdbc.url", Jdbc::getUrl, Jdbc::setUrl);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.user", Jdbc::getUser, Jdbc::setUser);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.username", Jdbc::getUsername, Jdbc::setUsername);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.password", Jdbc::getPassword, Jdbc::setPassword);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.autoCommit", Jdbc::isAutoCommit, Jdbc::setAutoCommit, Boolean::valueOf);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.initScript", Jdbc::getInitScript, Jdbc::setInitScript);
                    set(j, propertyOverride, o -> null, "jooq.codegen.jdbc.initSeparator", Jdbc::getInitSeparator, Jdbc::setInitSeparator);

                    if (j != null && !StringUtils.isBlank(j.getUrl())) {
                        try {
                            Class<? extends Driver> driver = (Class<? extends Driver>) loadClass(driverClass(j));

                            Properties properties = properties(j.getProperties());
                            String u = defaultString(defaultString(j.getUser(), j.getUsername()));
                            String p = defaultString(j.getPassword());

                            if (!properties.containsKey("user") && !u.isEmpty())
                                properties.put("user", u);
                            if (!properties.containsKey("password") && !p.isEmpty())
                                properties.put("password", p);

                            Connection c = driver.newInstance().connect(defaultString(j.getUrl()), properties);

                            // [#12951] Some drivers may (illegally) return null if the URL is incorrect?
                            if (c == null)
                                throw new SQLException("Cannot connect to database using JDBC URL: " + j.getUrl() + ". Please review your JDBC configuration in the code generator configuration.");

                            setConnection(c);

                            // [#16823] TODO: Move execution logic into the core library
                            if (j.getInitScript() != null)
                                for (String sql : Source
                                    .resolve(j.getInitScript())
                                    .readString()
                                    .split(defaultIfBlank(j.getInitSeparator(), ";"))
                                )
                                    if (!StringUtils.isBlank(sql))
                                        ctx.execute(sql);
                        }
                        catch (Exception e) {
                            if (databaseName != null)
                                if (databaseName.contains("DDLDatabase") || databaseName.contains("XMLDatabase") || databaseName.contains("JPADatabase"))
                                    log.warn("Error while connecting to database. Note that file based database implementations do not need a <jdbc/> configuration in the code generator.", e);

                            throw e;
                        }
                    }
                }
            }

            j = defaultIfNull(j, new Jdbc());

            if (connection != null && j.isAutoCommit() != null) {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(j.isAutoCommit());
            }

            // Initialise generator
            // --------------------
            Class<Generator> generatorClass = (Class<Generator>) (
                  !isBlank(g.getJava())
                ? compile(g.getName(), g.getJava(), Generator.class)
                : !isBlank(g.getName())
                ? loadClass(trim(g.getName()))
                : JavaGenerator.class
            );
            Generator generator = generatorClass.newInstance();

            GeneratorStrategy strategy;

            Matchers matchers = g.getStrategy().getMatchers();
            if (matchers != null) {
                strategy = new MatcherStrategy(matchers);

                if (g.getStrategy().getName() != null) {

                    // [#7416] Depending on who is unmarshalling the Configuration (Maven / JAXB),
                    //         the XSD's default value might apply, which we can safely ignore.
                    if (!DefaultGeneratorStrategy.class.getName().equals(g.getStrategy().getName()))
                        log.warn("WARNING: Matchers take precedence over custom strategy. Strategy ignored: " +
                            g.getStrategy().getName());

                    g.getStrategy().setName(null);
                }
            }
            else {
                Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (
                      !isBlank(g.getStrategy().getJava())
                    ? compile(g.getStrategy().getName(), g.getStrategy().getJava(), GeneratorStrategy.class)
                    : !isBlank(g.getStrategy().getName())
                    ? loadClass(trim(g.getStrategy().getName()))
                    : DefaultGeneratorStrategy.class
                );

                strategy = strategyClass.newInstance();
            }

            generator.setStrategy(strategy);

            Class<? extends Database> databaseClass =
                  !isBlank(d.getJava())
                ? compile(databaseName, d.getJava(), Database.class)
                : !isBlank(databaseName)
                ? (Class<? extends Database>) loadClass(databaseName)
                : connection != null
                ? databaseClass(connection)
                : databaseClass(j);

            database = databaseClass.newInstance();







            database.setBasedir(configuration.getBasedir());
            database.setProperties(properties(d.getProperties()));
            database.setOnError(configuration.getOnError());

            List<CatalogMappingType> catalogs = d.getCatalogs();
            List<SchemaMappingType> schemata = d.getSchemata();

            boolean catalogsEmpty = catalogs.isEmpty();
            boolean schemataEmpty = schemata.isEmpty();

            // For convenience, the catalog configuration can be set also directly in the <database/> element
            if (catalogsEmpty) {
                if (isBlank(d.getInputCatalog()) && !isBlank(d.getOutputCatalog()))
                    log.warn("WARNING: /configuration/generator/database/outputCatalog must be paired with /configuration/generator/database/inputCatalog");

                CatalogMappingType catalog = new CatalogMappingType();
                catalog.setInputCatalog(trim(d.getInputCatalog()));
                catalog.setOutputCatalog(trim(d.getOutputCatalog()));
                catalog.setOutputCatalogToDefault(d.isOutputCatalogToDefault());
                catalogs.add(catalog);

                if (!isBlank(catalog.getInputCatalog()))
                    catalogsEmpty = false;

                // For convenience and backwards-compatibility, the schema configuration can be set also directly
                // in the <database/> element
                if (schemataEmpty) {
                    if (isBlank(d.getInputSchema()) && !isBlank(d.getOutputSchema()))
                        log.warn("WARNING: /configuration/generator/database/outputSchema must be paired with /configuration/generator/database/inputSchema");

                    SchemaMappingType schema = new SchemaMappingType();
                    schema.setInputSchema(trim(d.getInputSchema()));
                    schema.setOutputSchema(trim(d.getOutputSchema()));
                    schema.setOutputSchemaToDefault(d.isOutputSchemaToDefault());
                    catalog.getSchemata().add(schema);

                    if (!isBlank(schema.getInputSchema()))
                        schemataEmpty = false;
                }
                else {
                    catalog.getSchemata().addAll(schemata);

                    if (!isBlank(d.getInputSchema()))
                        log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/schemata");
                    if (!isBlank(d.getOutputSchema()))
                        log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/schemata");
                }
            }
            else {
                if (!isBlank(d.getInputCatalog()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputCatalog and /configuration/generator/database/catalogs");
                if (!isBlank(d.getOutputCatalog()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputCatalog and /configuration/generator/database/catalogs");
                if (!isBlank(d.getInputSchema()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/catalogs");
                if (!isBlank(d.getOutputSchema()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/catalogs");
                if (!schemataEmpty)
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/catalogs and /configuration/generator/database/schemata");
            }

            for (CatalogMappingType catalog : catalogs) {
                if ("".equals(catalog.getOutputCatalog()))
                    log.warn("WARNING: Empty <outputCatalog/> should not be used to model default outputCatalogs. Use <outputCatalogToDefault>true</outputCatalogToDefault>, instead. See also: https://github.com/jOOQ/jOOQ/issues/3018");

                // [#3018] If users want the output catalog to be "" then, ignore the actual <outputCatalog/> configuration
                if (TRUE.equals(catalog.isOutputCatalogToDefault()))
                    catalog.setOutputCatalog("");
                else if (catalog.getOutputCatalog() == null)
                    catalog.setOutputCatalog(trim(catalog.getInputCatalog()));






                for (SchemaMappingType schema : catalog.getSchemata()) {
                    if (catalogsEmpty && schemataEmpty && isBlank(schema.getInputSchema())) {
                        if (!isBlank(j.getSchema()))
                            log.warn("WARNING: The configuration property jdbc.Schema is deprecated and will be removed in the future. Use /configuration/generator/database/inputSchema instead");

                        schema.setInputSchema(trim(j.getSchema()));
                    }

                    // [#3018] Prior to <outputSchemaToDefault/>, empty <outputSchema/> elements meant that
                    // the outputSchema should be the default schema. This is a bit too clever, and doesn't
                    // work when Maven parses the XML configurations.
                    if ("".equals(schema.getOutputSchema()))
                        log.warn("WARNING: Empty <outputSchema/> should not be used to model default outputSchemas. Use <outputSchemaToDefault>true</outputSchemaToDefault>, instead. See also: https://github.com/jOOQ/jOOQ/issues/3018");

                    // [#3018] If users want the output schema to be "" then, ignore the actual <outputSchema/> configuration
                    if (TRUE.equals(schema.isOutputSchemaToDefault()))
                        schema.setOutputSchema("");
                    else if (schema.getOutputSchema() == null)
                        schema.setOutputSchema(trim(schema.getInputSchema()));











                }
            }

            if (catalogsEmpty)
                log.info("No <inputCatalog/> was provided. Generating ALL available catalogs instead.");
            if (catalogsEmpty && schemataEmpty)
                log.info("No <inputSchema/> was provided. Generating ALL available schemata instead.");

            database.setConnection(connection);
            database.setConfiguredCatalogs(catalogs);
            database.setConfiguredSchemata(schemata);

            if (!isBlank(d.getIncludes()))
                database.setIncludes(new String[] { d.getIncludes() });
            if (!isBlank(d.getExcludes()))
                database.setExcludes(new String[] { d.getExcludes() });

            database.setIncludeSql(d.getIncludeSql());
            database.setExcludeSql(d.getExcludeSql());

            // [#10763] Currently, the javaTimeTypes flag needs to be set before
            //          the forcedTypesForBuiltinDataTypeExtensions flag.
            if (d.isDateAsTimestamp() != null)
                database.setDateAsTimestamp(d.isDateAsTimestamp());
            if (g.getGenerate().isJavaTimeTypes() != null)
                database.setJavaTimeTypes(g.getGenerate().isJavaTimeTypes());
            if (d.isUnsignedTypes() != null)
                database.setSupportsUnsignedTypes(d.isUnsignedTypes());
            if (d.isIntegerDisplayWidths() != null)
                database.setIntegerDisplayWidths(d.isIntegerDisplayWidths());
            if (d.isIgnoreProcedureReturnValues() != null)
                database.setIgnoreProcedureReturnValues(d.isIgnoreProcedureReturnValues());

            database.setIncludeExcludeColumns(TRUE.equals(d.isIncludeExcludeColumns()));
            database.setIncludeExcludePackageRoutines(TRUE.equals(d.isIncludeExcludePackageRoutines()));
            database.setIncludeForeignKeys(!FALSE.equals(d.isIncludeForeignKeys()));
            database.setIncludePackages(!FALSE.equals(d.isIncludePackages()));
            database.setIncludePackageRoutines(!FALSE.equals(d.isIncludePackageRoutines()));
            database.setIncludePackageUDTs(!FALSE.equals(d.isIncludePackageUDTs()));
            database.setIncludePackageConstants(!FALSE.equals(d.isIncludePackageConstants()));
            database.setIncludeIndexes(!FALSE.equals(d.isIncludeIndexes()));
            database.setIncludeCheckConstraints(!FALSE.equals(d.isIncludeCheckConstraints()));
            database.setIncludeSystemTables(TRUE.equals(d.isIncludeSystemTables()));
            database.setIncludeSystemIndexes(TRUE.equals(d.isIncludeSystemIndexes()));
            database.setIncludeSystemCheckConstraints(TRUE.equals(d.isIncludeSystemCheckConstraints()));
            database.setIncludeSystemSequences(TRUE.equals(d.isIncludeSystemSequences()));
            database.setIncludeSystemUDTs(TRUE.equals(d.isIncludeSystemUDTs()));
            database.setIncludeInvisibleColumns(!FALSE.equals(d.isIncludeInvisibleColumns()));
            database.setInvisibleColumnsAsHidden(!FALSE.equals(d.isInvisibleColumnsAsHidden()));
            database.setIncludePrimaryKeys(!FALSE.equals(d.isIncludePrimaryKeys()));
            database.setIncludeRoutines(!FALSE.equals(d.isIncludeRoutines()));
            database.setIncludeDomains(!FALSE.equals(d.isIncludeDomains()));




            database.setIncludeSequences(!FALSE.equals(d.isIncludeSequences()));
            database.setIncludeTables(!FALSE.equals(d.isIncludeTables()));
            database.setIncludeEmbeddables(!FALSE.equals(d.isIncludeEmbeddables()));
            database.setIncludeTriggerRoutines(TRUE.equals(d.isIncludeTriggerRoutines()));
            database.setIncludeXMLSchemaCollections(!FALSE.equals(d.isIncludeXMLSchemaCollections()));
            database.setIncludeUDTs(!FALSE.equals(d.isIncludeUDTs()));
            database.setIncludeUniqueKeys(!FALSE.equals(d.isIncludeUniqueKeys()));
            database.setForceIntegerTypesOnZeroScaleDecimals(!FALSE.equals(d.isForceIntegerTypesOnZeroScaleDecimals()));
            database.setRecordVersionFields(new String[] { defaultString(d.getRecordVersionFields()) });
            database.setRecordTimestampFields(new String[] { defaultString(d.getRecordTimestampFields()) });
            database.setSyntheticPrimaryKeys(new String[] { defaultString(d.getSyntheticPrimaryKeys()) });
            database.setOverridePrimaryKeys(new String[] { defaultString(d.getOverridePrimaryKeys()) });
            database.setSyntheticIdentities(new String[] { defaultString(d.getSyntheticIdentities()) });
            database.setConfiguredCustomTypes(d.getCustomTypes());
            database.setConfiguredEnumTypes(d.getEnumTypes());
            database.setConfiguredForcedTypes(d.getForcedTypes());
            database.setForcedTypesForBuiltinDataTypeExtensions(d.isForcedTypesForBuiltinDataTypeExtensions());
            database.setForcedTypesForXMLSchemaCollections(d.isForcedTypesForXMLSchemaCollections());
            database.setConfiguredEmbeddables(d.getEmbeddables());
            database.setConfiguredComments(d.getComments());
            database.setConfiguredSyntheticObjects(d.getSyntheticObjects());
            database.setEmbeddablePrimaryKeys(d.getEmbeddablePrimaryKeys());
            database.setEmbeddableUniqueKeys(d.getEmbeddableUniqueKeys());
            database.setEmbeddableDomains(d.getEmbeddableDomains());
            database.setReadonlyIdentities(TRUE.equals(d.isReadonlyIdentities()));
            database.setReadonlyComputedColumns(!FALSE.equals(d.isReadonlyComputedColumns()));
            database.setReadonlyNonUpdatableColumns(!FALSE.equals(d.isReadonlyNonUpdatableColumns()));
            database.setLogSlowQueriesAfterSeconds(defaultIfNull(d.getLogSlowQueriesAfterSeconds(), 5));
            database.setLogSlowResultsAfterSeconds(defaultIfNull(d.getLogSlowResultsAfterSeconds(), 5));

            if (d.getRegexFlags() != null) {
                database.setRegexFlags(d.getRegexFlags());

                if (strategy instanceof MatcherStrategy s)
                    s.getPatterns().setRegexFlags(d.getRegexFlags());
            }

            database.setRegexMatchesPartialQualification(!FALSE.equals(d.isRegexMatchesPartialQualification()));
            database.setSqlMatchesPartialQualification(!FALSE.equals(d.isSqlMatchesPartialQualification()));

            SchemaVersionProvider svp = null;
            CatalogVersionProvider cvp = null;

            if (!isBlank(d.getSchemaVersionProvider())) {
                try {
                    svp = (SchemaVersionProvider) Class.forName(d.getSchemaVersionProvider()).newInstance();
                    log.info("Using custom schema version provider : " + svp);
                }
                catch (Exception ignore) {
                    if (d.getSchemaVersionProvider().toLowerCase(locale).startsWith("select")) {
                        svp = new SQLSchemaVersionProvider(connection, d.getSchemaVersionProvider());
                        log.info("Using SQL schema version provider : " + d.getSchemaVersionProvider());
                    }
                    else {
                        svp = new ConstantSchemaVersionProvider(d.getSchemaVersionProvider());
                    }
                }
            }

            if (!isBlank(d.getCatalogVersionProvider())) {
                try {
                    cvp = (CatalogVersionProvider) Class.forName(d.getCatalogVersionProvider()).newInstance();
                    log.info("Using custom catalog version provider : " + cvp);
                }
                catch (Exception ignore) {
                    if (d.getCatalogVersionProvider().toLowerCase(locale).startsWith("select")) {
                        cvp = new SQLCatalogVersionProvider(connection, d.getCatalogVersionProvider());
                        log.info("Using SQL catalog version provider : " + d.getCatalogVersionProvider());
                    }
                    else {
                        cvp = new ConstantCatalogVersionProvider(d.getCatalogVersionProvider());
                    }
                }
            }

            if (svp == null)
                svp = new ConstantSchemaVersionProvider(null);
            if (cvp == null)
                cvp = new ConstantCatalogVersionProvider(null);

            database.setSchemaVersionProvider(svp);
            database.setCatalogVersionProvider(cvp);

            if (!isBlank(d.getOrderProvider())) {
                Class<?> orderProvider = Class.forName(d.getOrderProvider());

                if (Comparator.class.isAssignableFrom(orderProvider))
                    database.setOrderProvider((Comparator<Definition>) orderProvider.newInstance());
                else
                    log.warn("Order provider must be of type java.util.Comparator: " + orderProvider);
            }

            if (d.getEnumTypes().size() > 0)
                log.warn("DEPRECATED", "The configuration property /configuration/generator/database/enumTypes is experimental and deprecated and will be removed in the future.");
            if (Boolean.TRUE.equals(d.isDateAsTimestamp()))
                log.warn("DEPRECATED", "The configuration property /configuration/generator/database/dateAsTimestamp is deprecated as it is superseded by custom bindings and converters. It will thus be removed in the future. More information here: https://www.jooq.org/doc/latest/manual/reference/reference-data-types/data-types-oracle-date/");
            if (Boolean.TRUE.equals(d.isIgnoreProcedureReturnValues()))
                log.warn("DEPRECATED", "The <ignoreProcedureReturnValues/> flag is deprecated and used for backwards-compatibility only. It will be removed in the future.");

            if (isBlank(g.getTarget().getPackageName()))
                g.getTarget().setPackageName(DEFAULT_TARGET_PACKAGENAME);
            if (isBlank(g.getTarget().getDirectory()))
                g.getTarget().setDirectory(DEFAULT_TARGET_DIRECTORY);
            if (isBlank(g.getTarget().getEncoding()))
                g.getTarget().setEncoding(DEFAULT_TARGET_ENCODING);

            set(g.getTarget(), propertyOverride, o -> null, "jooq.codegen.target.packageName", Target::getPackageName, Target::setPackageName, identity(), DEFAULT_TARGET_PACKAGENAME::equals);
            set(g.getTarget(), propertyOverride, o -> null, "jooq.codegen.target.directory", Target::getDirectory, Target::setDirectory, identity(), DEFAULT_TARGET_DIRECTORY::equals);
            set(g.getTarget(), propertyOverride, o -> null, "jooq.codegen.target.encoding", Target::getEncoding, Target::setEncoding, identity(), DEFAULT_TARGET_ENCODING::equals);
            set(g.getTarget(), propertyOverride, o -> null, "jooq.codegen.target.locale", Target::getLocale, Target::setLocale);

            // [#2887] [#9727] Patch relative paths to take plugin execution basedir into account
            if (!new File(g.getTarget().getDirectory()).isAbsolute())
                g.getTarget().setDirectory(new File(configuration.getBasedir(), g.getTarget().getDirectory()).getCanonicalPath());

            generator.setTargetPackage(g.getTarget().getPackageName());
            generator.setTargetDirectory(g.getTarget().getDirectory());
            generator.setTargetEncoding(g.getTarget().getEncoding());

            if (g.getTarget().isClean() != null)
                generator.setTargetClean(g.getTarget().isClean());
            generator.setTargetLocale(locale);

            if (g.getGenerate().isIndexes() != null)
                generator.setGenerateIndexes(g.getGenerate().isIndexes());
            if (g.getGenerate().isRelations() != null)
                generator.setGenerateRelations(g.getGenerate().isRelations());
            if (g.getGenerate().isUdtPaths() != null)
                generator.setGenerateUDTPaths(g.getGenerate().isUdtPaths());
            if (g.getGenerate().isImplicitJoinPathsToOne() != null)
                generator.setGenerateImplicitJoinPathsToOne(g.getGenerate().isImplicitJoinPathsToOne());
            if (g.getGenerate().isImplicitJoinPathsToMany() != null)
                generator.setGenerateImplicitJoinPathsToMany(g.getGenerate().isImplicitJoinPathsToMany());
            if (g.getGenerate().isImplicitJoinPathsManyToMany() != null)
                generator.setGenerateImplicitJoinPathsManyToMany(g.getGenerate().isImplicitJoinPathsManyToMany());
            if (g.getGenerate().isImplicitJoinPathTableSubtypes() != null)
                generator.setGenerateImplicitJoinPathTableSubtypes(g.getGenerate().isImplicitJoinPathTableSubtypes());
            if (g.getGenerate().isImplicitJoinPathUnusedConstructors() != null)
                generator.setGenerateImplicitJoinPathUnusedConstructors(g.getGenerate().isImplicitJoinPathUnusedConstructors());
            if (g.getGenerate().isImplicitJoinPathsAsKotlinProperties() != null)
                generator.setGenerateImplicitJoinPathsAsKotlinProperties(g.getGenerate().isImplicitJoinPathsAsKotlinProperties());
            if (g.getGenerate().isDeprecated() != null)
                generator.setGenerateDeprecated(g.getGenerate().isDeprecated());
            if (g.getGenerate().isDeprecationOnUnknownTypes() != null)
                generator.setGenerateDeprecationOnUnknownTypes(g.getGenerate().isDeprecationOnUnknownTypes());
            if (g.getGenerate().isInstanceFields() != null)
                generator.setGenerateInstanceFields(g.getGenerate().isInstanceFields());
            if (g.getGenerate().getVisibilityModifier() != null)
                generator.setGenerateVisibilityModifier(g.getGenerate().getVisibilityModifier());
            if (g.getGenerate().isGeneratedAnnotation() != null)
                generator.setGenerateGeneratedAnnotation(g.getGenerate().isGeneratedAnnotation());
            if (g.getGenerate().getGeneratedAnnotationType() != null)
                generator.setGenerateGeneratedAnnotationType(g.getGenerate().getGeneratedAnnotationType());
            if (g.getGenerate().isGeneratedAnnotationDate() != null)
                generator.setGenerateGeneratedAnnotationDate(g.getGenerate().isGeneratedAnnotationDate());
            if (g.getGenerate().isGeneratedAnnotationJooqVersion() != null)
                generator.setGenerateGeneratedAnnotationJooqVersion(g.getGenerate().isGeneratedAnnotationJooqVersion());
            if (g.getGenerate().isNonnullAnnotation() != null)
                generator.setGenerateNonnullAnnotation(g.getGenerate().isNonnullAnnotation());
            if (g.getGenerate().getNonnullAnnotationType() != null)
                generator.setGeneratedNonnullAnnotationType(g.getGenerate().getNonnullAnnotationType());
            if (g.getGenerate().isNullableAnnotation() != null)
                generator.setGenerateNullableAnnotation(g.getGenerate().isNullableAnnotation());
            if (g.getGenerate().isNullableAnnotationOnWriteOnlyNullableTypes() != null)
                generator.setGenerateNullableAnnotationOnWriteOnlyNullableTypes(g.getGenerate().isNullableAnnotationOnWriteOnlyNullableTypes());
            if (g.getGenerate().getNullableAnnotationType() != null)
                generator.setGeneratedNullableAnnotationType(g.getGenerate().getNullableAnnotationType());
            if (g.getGenerate().isConstructorPropertiesAnnotation() != null)
                generator.setGenerateConstructorPropertiesAnnotation(g.getGenerate().isConstructorPropertiesAnnotation());
            if (g.getGenerate().isConstructorPropertiesAnnotationOnPojos() != null)
                generator.setGenerateConstructorPropertiesAnnotationOnPojos(g.getGenerate().isConstructorPropertiesAnnotationOnPojos());
            if (g.getGenerate().isConstructorPropertiesAnnotationOnRecords() != null)
                generator.setGenerateConstructorPropertiesAnnotationOnRecords(g.getGenerate().isConstructorPropertiesAnnotationOnRecords());
            if (g.getGenerate().isRoutines() != null)
                generator.setGenerateRoutines(g.getGenerate().isRoutines());






            if (g.getGenerate().isSequences() != null)
                generator.setGenerateSequences(g.getGenerate().isSequences());
            if (g.getGenerate().isSequenceFlags() != null)
                generator.setGenerateSequenceFlags(g.getGenerate().isSequenceFlags());
            if (g.getGenerate().isUdts() != null)
                generator.setGenerateUDTs(g.getGenerate().isUdts());
            if (g.getGenerate().isTables() != null)
                generator.setGenerateTables(g.getGenerate().isTables());
            if (g.getGenerate().isEmbeddables() != null)
                generator.setGenerateEmbeddables(g.getGenerate().isEmbeddables());
            if (g.getGenerate().isRecords() != null)
                generator.setGenerateRecords(g.getGenerate().isRecords());
            if (g.getGenerate().getRecordsIncludes() != null)
                generator.setGenerateRecordsIncludes(g.getGenerate().getRecordsIncludes());
            if (g.getGenerate().getRecordsExcludes() != null)
                generator.setGenerateRecordsExcludes(g.getGenerate().getRecordsExcludes());
            if (g.getGenerate().isRecordsImplementingRecordN() != null)
                generator.setGenerateRecordsImplementingRecordN(g.getGenerate().isRecordsImplementingRecordN());
            if (g.getGenerate().isEnumsAsScalaSealedTraits() != null)
                generator.setGenerateEnumsAsScalaSealedTraits(g.getGenerate().isEnumsAsScalaSealedTraits());
            if (g.getGenerate().isEnumsAsScalaEnums() != null)
                generator.setGenerateEnumsAsScalaEnums(g.getGenerate().isEnumsAsScalaEnums());
            if (g.getGenerate().isPojos() != null)
                generator.setGeneratePojos(g.getGenerate().isPojos());
            if (g.getGenerate().getPojosIncludes() != null)
                generator.setGeneratePojosIncludes(g.getGenerate().getPojosIncludes());
            if (g.getGenerate().getPojosExcludes() != null)
                generator.setGeneratePojosExcludes(g.getGenerate().getPojosExcludes());
            if (g.getGenerate().isPojosAsJavaRecordClasses() != null)
                generator.setGeneratePojosAsJavaRecordClasses(g.getGenerate().isPojosAsJavaRecordClasses());
            if (g.getGenerate().isPojosAsScalaCaseClasses() != null)
                generator.setGeneratePojosAsScalaCaseClasses(g.getGenerate().isPojosAsScalaCaseClasses());
            if (g.getGenerate().isPojosAsKotlinDataClasses() != null)
                generator.setGeneratePojosAsKotlinDataClasses(g.getGenerate().isPojosAsKotlinDataClasses());
            if (g.getGenerate().isImmutablePojos() != null)
                generator.setGenerateImmutablePojos(g.getGenerate().isImmutablePojos());
            if (g.getGenerate().isSerializablePojos() != null)
                generator.setGenerateSerializablePojos(g.getGenerate().isSerializablePojos());
            if (g.getGenerate().isInterfaces() != null)
                generator.setGenerateInterfaces(g.getGenerate().isInterfaces());
            if (g.getGenerate().isImmutableInterfaces() != null)
                generator.setGenerateImmutableInterfaces(g.getGenerate().isImmutableInterfaces());
            if (g.getGenerate().isSerializableInterfaces() != null)
                generator.setGenerateSerializableInterfaces(g.getGenerate().isSerializableInterfaces());
            if (g.getGenerate().isDaos() != null)
                generator.setGenerateDaos(g.getGenerate().isDaos());
            if (g.getGenerate().getDaosIncludes() != null)
                generator.setGenerateDaosIncludes(g.getGenerate().getDaosIncludes());
            if (g.getGenerate().getDaosExcludes() != null)
                generator.setGenerateDaosExcludes(g.getGenerate().getDaosExcludes());
            if (g.getGenerate().isJooqVersionReference() != null)
                generator.setGenerateJooqVersionReference(g.getGenerate().isJooqVersionReference());
            if (g.getGenerate().isJpaAnnotations() != null)
                generator.setGenerateJPAAnnotations(g.getGenerate().isJpaAnnotations());
            if (g.getGenerate().getJpaVersion() != null)
                generator.setGenerateJPAVersion(g.getGenerate().getJpaVersion());
            if (g.getGenerate().isValidationAnnotations() != null)
                generator.setGenerateValidationAnnotations(g.getGenerate().isValidationAnnotations());
            if (g.getGenerate().isSpringAnnotations() != null)
                generator.setGenerateSpringAnnotations(g.getGenerate().isSpringAnnotations());
            if (g.getGenerate().isSpringDao() != null)
                generator.setGenerateSpringDao(g.getGenerate().isSpringDao());
            if (g.getGenerate().isKotlinSetterJvmNameAnnotationsOnIsPrefix() != null)
                generator.setGenerateKotlinSetterJvmNameAnnotationsOnIsPrefix(g.getGenerate().isKotlinSetterJvmNameAnnotationsOnIsPrefix());
            if (g.getGenerate().isKotlinNotNullPojoAttributes() != null)
                generator.setGenerateKotlinNotNullPojoAttributes(g.getGenerate().isKotlinNotNullPojoAttributes());
            if (g.getGenerate().isKotlinNotNullRecordAttributes() != null)
                generator.setGenerateKotlinNotNullRecordAttributes(g.getGenerate().isKotlinNotNullRecordAttributes());
            if (g.getGenerate().isKotlinNotNullInterfaceAttributes() != null)
                generator.setGenerateKotlinNotNullInterfaceAttributes(g.getGenerate().isKotlinNotNullInterfaceAttributes());
            if (g.getGenerate().isKotlinDefaultedNullablePojoAttributes() != null)
                generator.setGenerateKotlinDefaultedNullablePojoAttributes(g.getGenerate().isKotlinDefaultedNullablePojoAttributes());
            if (g.getGenerate().isKotlinDefaultedNullableRecordAttributes() != null)
                generator.setGenerateKotlinDefaultedNullableRecordAttributes(g.getGenerate().isKotlinDefaultedNullableRecordAttributes());
            if (g.getGenerate().getGeneratedSerialVersionUID() != null)
                generator.setGenerateGeneratedSerialVersionUID(g.getGenerate().getGeneratedSerialVersionUID());
            if (g.getGenerate().getMaxMembersPerInitialiser() != null)
                generator.setMaxMembersPerInitialiser(g.getGenerate().getMaxMembersPerInitialiser());
            if (g.getGenerate().isQueues() != null)
                generator.setGenerateQueues(g.getGenerate().isQueues());
            if (g.getGenerate().isLinks() != null)
                generator.setGenerateLinks(g.getGenerate().isLinks());
            if (g.getGenerate().isKeys() != null)
                generator.setGenerateKeys(g.getGenerate().isKeys());
            if (g.getGenerate().isGlobalObjectNames() != null)
                generator.setGenerateGlobalObjectNames(g.getGenerate().isGlobalObjectNames());
            if (g.getGenerate().isGlobalObjectReferences() != null)
                generator.setGenerateGlobalObjectReferences(g.getGenerate().isGlobalObjectReferences());
            if (g.getGenerate().isGlobalCatalogReferences() != null)
                generator.setGenerateGlobalCatalogReferences(g.getGenerate().isGlobalCatalogReferences());
            if (g.getGenerate().isGlobalDomainReferences() != null)
                generator.setGenerateGlobalDomainReferences(g.getGenerate().isGlobalDomainReferences());






            if (g.getGenerate().isGlobalSchemaReferences() != null)
                generator.setGenerateGlobalSchemaReferences(g.getGenerate().isGlobalSchemaReferences());
            if (g.getGenerate().isGlobalRoutineReferences() != null)
                generator.setGenerateGlobalRoutineReferences(g.getGenerate().isGlobalRoutineReferences());
            if (g.getGenerate().isGlobalSequenceReferences() != null)
                generator.setGenerateGlobalSequenceReferences(g.getGenerate().isGlobalSequenceReferences());
            if (g.getGenerate().isGlobalTableReferences() != null)
                generator.setGenerateGlobalTableReferences(g.getGenerate().isGlobalTableReferences());
            if (g.getGenerate().isGlobalUDTReferences() != null)
                generator.setGenerateGlobalUDTReferences(g.getGenerate().isGlobalUDTReferences());
            if (g.getGenerate().isGlobalQueueReferences() != null)
                generator.setGenerateGlobalQueueReferences(g.getGenerate().isGlobalQueueReferences());
            if (g.getGenerate().isGlobalLinkReferences() != null)
                generator.setGenerateGlobalLinkReferences(g.getGenerate().isGlobalLinkReferences());
            if (g.getGenerate().isGlobalKeyReferences() != null)
                generator.setGenerateGlobalKeyReferences(g.getGenerate().isGlobalKeyReferences());
            if (g.getGenerate().isGlobalIndexReferences() != null)
                generator.setGenerateGlobalIndexReferences(g.getGenerate().isGlobalIndexReferences());
            if (g.getGenerate().isDefaultCatalog() != null)
                generator.setGenerateDefaultCatalog(g.getGenerate().isDefaultCatalog());
            if (g.getGenerate().isDefaultSchema() != null)
                generator.setGenerateDefaultSchema(g.getGenerate().isDefaultSchema());
            if (g.getGenerate().isJavadoc() != null)
                generator.setGenerateJavadoc(g.getGenerate().isJavadoc());
            if (g.getGenerate().isComments() != null)
                generator.setGenerateComments(g.getGenerate().isComments());
            if (g.getGenerate().isCommentsOnAttributes() != null)
                generator.setGenerateCommentsOnAttributes(g.getGenerate().isCommentsOnAttributes());
            if (g.getGenerate().isCommentsOnCatalogs() != null)
                generator.setGenerateCommentsOnCatalogs(g.getGenerate().isCommentsOnCatalogs());
            if (g.getGenerate().isCommentsOnColumns() != null)
                generator.setGenerateCommentsOnColumns(g.getGenerate().isCommentsOnColumns());
            if (g.getGenerate().isCommentsOnKeys() != null)
                generator.setGenerateCommentsOnKeys(g.getGenerate().isCommentsOnKeys());
            if (g.getGenerate().isCommentsOnLinks() != null)
                generator.setGenerateCommentsOnLinks(g.getGenerate().isCommentsOnLinks());
            if (g.getGenerate().isCommentsOnPackages() != null)
                generator.setGenerateCommentsOnPackages(g.getGenerate().isCommentsOnPackages());
            if (g.getGenerate().isCommentsOnParameters() != null)
                generator.setGenerateCommentsOnParameters(g.getGenerate().isCommentsOnParameters());
            if (g.getGenerate().isCommentsOnQueues() != null)
                generator.setGenerateCommentsOnQueues(g.getGenerate().isCommentsOnQueues());
            if (g.getGenerate().isCommentsOnRoutines() != null)
                generator.setGenerateCommentsOnRoutines(g.getGenerate().isCommentsOnRoutines());
            if (g.getGenerate().isCommentsOnSchemas() != null)
                generator.setGenerateCommentsOnSchemas(g.getGenerate().isCommentsOnSchemas());
            if (g.getGenerate().isCommentsOnSequences() != null)
                generator.setGenerateCommentsOnSequences(g.getGenerate().isCommentsOnSequences());
            if (g.getGenerate().isCommentsOnDomains() != null)
                generator.setGenerateCommentsOnDomains(g.getGenerate().isCommentsOnDomains());
            if (g.getGenerate().isCommentsOnTables() != null)
                generator.setGenerateCommentsOnTables(g.getGenerate().isCommentsOnTables());
            if (g.getGenerate().isCommentsOnEmbeddables() != null)
                generator.setGenerateCommentsOnEmbeddables(g.getGenerate().isCommentsOnEmbeddables());
            if (g.getGenerate().isCommentsOnUDTs() != null)
                generator.setGenerateCommentsOnUDTs(g.getGenerate().isCommentsOnUDTs());
            if (g.getGenerate().isSources() != null)
                generator.setGenerateSources(g.getGenerate().isSources());
            if (g.getGenerate().isSourcesOnViews() != null)
                generator.setGenerateSourcesOnViews(g.getGenerate().isSourcesOnViews());
            if (g.getGenerate().isFluentSetters() != null)
                generator.setGenerateFluentSetters(g.getGenerate().isFluentSetters());
            if (g.getGenerate().isJavaBeansGettersAndSetters() != null)
                generator.setGenerateJavaBeansGettersAndSetters(g.getGenerate().isJavaBeansGettersAndSetters());
            if (g.getGenerate().isImplicitJoinPathsUseTableNameForUnambiguousFKs() != null)
                generator.setGenerateUseTableNameForUnambiguousFKs(g.getGenerate().isImplicitJoinPathsUseTableNameForUnambiguousFKs());
            if (g.getGenerate().isVarargSetters() != null)
                generator.setGenerateVarargsSetters(g.getGenerate().isVarargSetters());
            if (g.getGenerate().isPojosEqualsAndHashCode() != null)
                generator.setGeneratePojosEqualsAndHashCode(g.getGenerate().isPojosEqualsAndHashCode());
            if (g.getGenerate().isPojosEqualsAndHashCodeIncludePrimaryKeyOnly() != null)
                generator.setGeneratePojosEqualsAndHashCodePrimaryKeyOnly(g.getGenerate().isPojosEqualsAndHashCodeIncludePrimaryKeyOnly());
            if (g.getGenerate().getPojosEqualsAndHashCodeColumnIncludeExpression() != null)
                generator.setGeneratePojosEqualsAndHashCodeColumnIncludeExpression(g.getGenerate().getPojosEqualsAndHashCodeColumnIncludeExpression());
            if (g.getGenerate().getPojosEqualsAndHashCodeColumnExcludeExpression() != null)
                generator.setGeneratePojosEqualsAndHashCodeColumnExcludeExpression(g.getGenerate().getPojosEqualsAndHashCodeColumnExcludeExpression());
            if (g.getGenerate().isPojosToString() != null)
                generator.setGeneratePojosToString(g.getGenerate().isPojosToString());
            if (g.getGenerate().getFullyQualifiedTypes() != null)
                generator.setGenerateFullyQualifiedTypes(g.getGenerate().getFullyQualifiedTypes());
            if (g.getGenerate().isJavaTimeTypes() != null)
                generator.setGenerateJavaTimeTypes(g.getGenerate().isJavaTimeTypes());
            if (g.getGenerate().isSpatialTypes() != null)
                generator.setGenerateSpatialTypes(g.getGenerate().isSpatialTypes());
            if (g.getGenerate().isXmlTypes() != null)
                generator.setGenerateXmlTypes(g.getGenerate().isXmlTypes());
            if (g.getGenerate().isJsonTypes() != null)
                generator.setGenerateJsonTypes(g.getGenerate().isJsonTypes());
            if (g.getGenerate().isIntervalTypes() != null)
                generator.setGenerateIntervalTypes(g.getGenerate().isIntervalTypes());
            if (g.getGenerate().isDecfloatTypes() != null)
                generator.setGenerateDecfloatTypes(g.getGenerate().isDecfloatTypes());
            if (g.getGenerate().isEmptyCatalogs() != null)
                generator.setGenerateEmptyCatalogs(g.getGenerate().isEmptyCatalogs());
            if (g.getGenerate().isEmptySchemas() != null)
                generator.setGenerateEmptySchemas(g.getGenerate().isEmptySchemas());
            if (g.getGenerate().getNewline() != null)
                generator.setGenerateNewline(g.getGenerate().getNewline());
            if (g.getGenerate().getIndentation() != null)
                generator.setGenerateIndentation(g.getGenerate().getIndentation());
            if (g.getGenerate().getPrintMarginForBlockComment() != null)
                generator.setGeneratePrintMarginForBlockComment(g.getGenerate().getPrintMarginForBlockComment());
            if (g.getGenerate().getTextBlocks() != null)
                generator.setGenerateTextBlocks(g.getGenerate().getTextBlocks());
            if (g.getGenerate().isWhereMethodOverrides() != null)
                generator.setGenerateWhereMethodOverrides(g.getGenerate().isWhereMethodOverrides());
            if (g.getGenerate().isRenameMethodOverrides() != null)
                generator.setGenerateRenameMethodOverrides(g.getGenerate().isRenameMethodOverrides());
            if (g.getGenerate().isAsMethodOverrides() != null)
                generator.setGenerateAsMethodOverrides(g.getGenerate().isAsMethodOverrides());


            if (!isBlank(d.getSchemaVersionProvider()))
                generator.setUseSchemaVersionProvider(true);
            if (!isBlank(d.getCatalogVersionProvider()))
                generator.setUseCatalogVersionProvider(true);
            if (d.isTableValuedFunctions() != null)
                generator.setGenerateTableValuedFunctions(d.isTableValuedFunctions());
            else {
                generator.setGenerateTableValuedFunctions(true);







            }

            // Generator properties that should in fact be strategy properties
            strategy.setInstanceFields(generator.generateInstanceFields());
            strategy.setJavaBeansGettersAndSetters(generator.generateJavaBeansGettersAndSetters());
            strategy.setUseTableNameForUnambiguousFKs(generator.generateUseTableNameForUnambiguousFKs());

            verifyVersions();
            generator.generate(database);

            if (configuration.getOnUnused() != OnError.SILENT) {
                boolean anyUnused = false;

                anyUnused = anyUnused | logUnused("forced type", "forced types", database.getUnusedForcedTypes().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("embeddable", "embeddables", database.getUnusedEmbeddables().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("comment", "comments", database.getUnusedComments().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic column", "synthetic columns", database.getUnusedSyntheticColumns().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic readonly column", "synthetic readonly columns", database.getUnusedSyntheticReadonlyColumns().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic readonly rowid", "synthetic readonly rowids", database.getUnusedSyntheticReadonlyRowids().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic identity", "synthetic identities", database.getUnusedSyntheticIdentities().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic primary key", "synthetic primary keys", database.getUnusedSyntheticPrimaryKeys().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic unique key", "synthetic unique keys", database.getUnusedSyntheticUniqueKeys().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic foreign key", "synthetic foreign keys", database.getUnusedSyntheticForeignKeys().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));
                anyUnused = anyUnused | logUnused("synthetic view", "synthetic views", database.getUnusedSyntheticViews().stream().filter(e -> !TRUE.equals(e.isIgnoreUnused())).collect(toList()));

                if (anyUnused && configuration.getOnUnused() == OnError.FAIL)
                    throw new GeneratorException("Unused configuration elements encountered");
            }
        }
        finally {
            if (database != null)
                try {
                    database.close();
                }
                catch (Exception e) {
                    log.error("Error while closing database", e);
                }

            // Close connection only if it was created by the GenerationTool
            if (connection != null) {
                if (close) {

                    // [#11105] In case of misconfiguration, the ctx reference could be null as a side effect
                    if (ctx != null && ctx.family() == HSQLDB && dataSource == null)
                        ctx.execute("shutdown");

                    connection.close();
                }
                else if (autoCommit != null) {
                    connection.setAutoCommit(autoCommit);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> compile(String name, String java, Class<T> type) {
        if (isBlank(name))
            throw new GeneratorException("Type " + type.getName() + " must have explicit name configured: " + java);

        Class<?> result = Reflect.compile(name, java,
            new CompileOptions().classLoader(Thread.currentThread().getContextClassLoader())
        ).type();

        if (type.isAssignableFrom(result))
            return (Class<T>) result;
        else
            throw new GeneratorException("Type " + name + " must implement " + type.getName());
    }

    private <O> void set(
        O configurationObject,
        boolean override,
        Function<? super O, ? extends String> property,
        String defaultProperty,
        Function<? super O, ? extends String> get,
        BiConsumer<? super O, ? super String> set
    ) {
        set(configurationObject, override, property, defaultProperty, get, set, Function.identity());
    }

    private <O, T> void set(
        O configurationObject,
        boolean override,
        Function<? super O, ? extends String> property,
        String defaultProperty,
        Function<? super O, ? extends T> get,
        BiConsumer<? super O, ? super T> set,
        Function<? super String, ? extends T> convert
    ) {
        set(configurationObject, override, property, defaultProperty, get, set, convert, t -> t == null);
    }

    private <O, T> void set(
        O configurationObject,
        boolean override,
        Function<? super O, ? extends String> propertyGetter,
        String defaultProperty,
        Function<? super O, ? extends T> get,
        BiConsumer<? super O, ? super T> set,
        Function<? super String, ? extends T> convert,
        Predicate<? super T> checkDefault
    ) {
        String property = propertyGetter.apply(configurationObject);
        String p = null;

        if (property != null) {
            p = System.getProperty(property);

            if (p != null)
                override = true;
        }

        if (p == null)
            p = System.getProperty(defaultProperty);

        if (p != null && (override || checkDefault.test(get.apply(configurationObject))))
            set.accept(configurationObject, convert.apply(p));
    }

    private void verifyVersions() {

        // [#12488] Check if all of jOOQ, jOOQ-meta, jOOQ-codegen are using the same versions and editions
        try {
            Field[] f1 = org.jooq.Constants.class.getFields();
            Field[] f2 = org.jooq.meta.Constants.class.getFields();
            Field[] f3 = org.jooq.codegen.Constants.class.getFields();

            Arrays.sort(f1, comparing(Field::getName));
            Arrays.sort(f2, comparing(Field::getName));
            Arrays.sort(f3, comparing(Field::getName));

            if (f1.length != f2.length)
                log.warn("Version check", "org.jooq.Constants and org.jooq.meta.Constants contents mismatch. Check if you're using the same versions for org.jooq and org.jooq.meta");
            if (f1.length != f3.length)
                log.warn("Version check", "org.jooq.Constants and org.jooq.codegen.Constants contents mismatch. Check if you're using the same versions for org.jooq and org.jooq.meta");

            String v1 = org.jooq.Constants.FULL_VERSION;
            String v2 = org.jooq.meta.Constants.FULL_VERSION;
            String v3 = org.jooq.codegen.Constants.FULL_VERSION;

            for (int i = 0; i < f1.length && i < f2.length && i < f3.length; i++) {
                Object c1 = f1[i].get(org.jooq.Constants.class);
                Object c2 = f2[i].get(org.jooq.meta.Constants.class);
                Object c3 = f3[i].get(org.jooq.codegen.Constants.class);

                if (!Objects.equals(c1, c2))
                    log.warn("Version check", "org.jooq.Constants." + f1[i].getName() + " contents mismatch: " + c1 + " vs " + c2 + ". Check if you're using the same versions for org.jooq (" + v1 + ") and org.jooq.meta (" + v2 + ")");
                if (!Objects.equals(c1, c3))
                    log.warn("Version check", "org.jooq.Constants." + f1[i].getName() + " contents mismatch: " + c1 + " vs " + c3 + ". Check if you're using the same versions for org.jooq (" + v1 + ") and org.jooq.codegen (" + v3 + ")");
            }
        }
        catch (Throwable e) {
            log.warn("Version check", "Something went wrong when comparing versions of org.jooq, org.jooq.meta, and org.jooq.codegen", e);
        }
    }

    private boolean logUnused(String objectType, String objectTypes, List<?> list) {
        if (!list.isEmpty() && Boolean.parseBoolean(System.getProperty("jooq.codegen.logunused", "true"))) {
            unusedLogger.warn(
                  "Unused " + objectTypes,
                  "There are unused " + objectTypes + ", which have not been used by this generation run.\n"
                + "This can be because of misconfigurations, such as, for example:\n"
                + "- case sensitive regular expressions\n"
                + "- regular expressions depending on whitespace (Pattern.COMMENTS is turned on!)\n"
                + "- missing or inadequate object qualification\n"
                + "- the object to which the configuration was applied in the past has been dropped\n"
                + "Try turning on DEBUG logging (-X in Maven, --debug in Gradle, and <logging/> in jOOQ) to get additional info about the schema\n"
                + "To turn off individual warnings, specify <ignoreUnused>true</ignoreUnused> on the relevant object(s)."
            );

            for (Object o : list)
                unusedLogger.warn("Unused " + objectType, o);

            return true;
        }
        else
            return false;
    }

    private static void setGlobalLoggingThreshold(Configuration configuration) {
        if (configuration.getLogging() != null) {
            switch (configuration.getLogging()) {
                case TRACE:
                    JooqLogger.globalThreshold(Level.TRACE);
                    break;
                case DEBUG:
                    JooqLogger.globalThreshold(Level.DEBUG);
                    break;
                case INFO:
                    JooqLogger.globalThreshold(Level.INFO);
                    break;
                case WARN:
                    JooqLogger.globalThreshold(Level.WARN);
                    break;
                case ERROR:
                    JooqLogger.globalThreshold(Level.ERROR);
                    break;
                case FATAL:
                    JooqLogger.globalThreshold(Level.FATAL);
                    break;
            }
        }
    }

    private Properties properties(List<Property> properties) {
        Properties result = new Properties();

        for (Property p : properties)
            result.put(p.getKey(), p.getValue());

        return result;
    }

    private String driverClass(Jdbc j) {
        String result = j.getDriver();

        if (result == null) {
            result = JDBCUtils.driver(j.getUrl());
            log.info("Database", "Inferring driver " + result + " from URL " + j.getUrl());
        }

        return result;
    }

    private Class<? extends Database> databaseClass(Jdbc j) {
        return databaseClass(j.getUrl());
    }

    private Class<? extends Database> databaseClass(Connection c) {
        try {
            return databaseClass(c.getMetaData().getURL());
        }
        catch (SQLException e) {
            throw new GeneratorException("Error when reading URL from JDBC connection", e);
        }
    }

    private Class<? extends Database> databaseClass(String url) {
        if (isBlank(url))
            throw new GeneratorException("No JDBC URL configured.");

        Class<? extends Database> result = Databases.databaseClass(JDBCUtils.dialect(url));
        log.info("Database", "Inferring database " + result.getName() + " from URL " + url);
        return result;
    }

    private Class<?> loadClass(String className) throws ClassNotFoundException {
        try {

            // [#2283] If no explicit class loader was provided try loading the class
            // with "default" techniques
            if (loader == null) {
                return ClassUtils.loadClass(className);
            }

            // Prefer the explicit class loader if available
            else {
                return loader.loadClass(className);
            }
        }

        catch (ClassNotFoundException e) {
            String message = null;

            // [#7556] [#8781]
            if (className.startsWith("org.jooq.util.")) {
                String alternative = null;

                alternativeLoop:
                for (String pkg : new String[] { "org.jooq.meta", "org.jooq.meta.extensions", "org.jooq.codegen", "org.jooq.codegen.maven" }) {
                    try {
                        alternative = ClassUtils.loadClass(className.replace("org.jooq.util", pkg)).getName();
                        break alternativeLoop;
                    }
                    catch (ClassNotFoundException ignore) {}
                }

                log.warn("Type not found", message =
                    "Your configured " + className + " type was not found.\n"
                  + (alternative != null ? ("Did you mean " + alternative + "?\n") : "")
                  + "Do note that in jOOQ 3.11, jOOQ-meta and jOOQ-codegen packages have been renamed. New package names are:\n"
                  + "- org.jooq.meta\n"
                  + "- org.jooq.meta.extensions\n"
                  + "- org.jooq.codegen\n"
                  + "- org.jooq.codegen.maven\n"
                  + "See https://github.com/jOOQ/jOOQ/issues/7419 for details");
            }

            else if (className.equals("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")) {
                log.warn("Type not found", message =
                    "Your configured database type was not found: " + className + ".\n"
                  + "- Please make sure the jooq-meta-extensions-liquibase dependency is on your classpath.\n"
                  + "- In jOOQ 3.14, the dependency name has changed, see https://github.com/jOOQ/jOOQ/issues/10331");
            }

            else if (className.equals("org.jooq.meta.extensions.jpa.JPADatabase")) {
                log.warn("Type not found", message =
                    "Your configured database type was not found: " + className + ".\n"
                  + "- Please make sure the jooq-meta-extensions-hibernate dependency is on your classpath.\n"
                  + "- In jOOQ 3.14, the dependency name has changed, see https://github.com/jOOQ/jOOQ/issues/10331");
            }

            // [#2801] [#4620]
            else if (className.startsWith("org.jooq.meta.") && className.endsWith("Database")) {
                log.warn("Type not found", message =
                      """
                      Your configured database type was not found: {className}. This can have several reasons:
                      - You want to use a commercial jOOQ Edition, but you pulled the Open Source Edition from Maven Central. Maven groupIds are:
                        - org.jooq                for the Open Source Edition
                        - org.jooq.pro            for commercial editions with Java 21 support,
                        - org.jooq.pro-java-17    for commercial editions with Java 17 support,
                        - org.jooq.pro-java-11    for commercial editions with Java 11 support,
                        - org.jooq.pro-java-8     for commercial editions with Java 8 support,
                        - org.jooq.trial          for the free trial edition with Java 21 support,
                        - org.jooq.trial-java-17  for the free trial edition with Java 17 support,
                        - org.jooq.trial-java-11  for the free trial edition with Java 11 support,
                        - org.jooq.trial-java-8   for the free trial edition with Java 8 support
                      - You have mis-typed your class name.
                      """.replace("{className}", className)
                );
            }

            if (message == null)
                throw e;
            else
                throw new ClassNotFoundException(message, e);
        }
    }

    private static String trim(String string) {
        return (string == null ? null : string.trim());
    }

    /**
     * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input the <code>InputStream</code> to read from
     * @param output the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Load a jOOQ codegen configuration file from an input stream
     */
    public static Configuration load(InputStream in) throws IOException {
        // [#1149] If there is no namespace defined, add the default one
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyLarge(in, out);
        String xml = out.toString();

        // TODO [#1201] Add better error handling here
        xml = xml.replaceAll(
            "<(\\w+:)?configuration xmlns(:\\w+)?=\"http://www.jooq.org/xsd/jooq-codegen-\\d+\\.\\d+\\.\\d+.xsd\">",
            "<$1configuration xmlns$2=\"" + Constants.NS_CODEGEN + "\">");

        return MiniJAXB.unmarshal(xml, Configuration.class);
    }
}
