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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.SchemaFactory;

import org.jooq.Constants;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.JooqLogger.Level;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.util.jaxb.Catalog;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Matchers;
import org.jooq.util.jaxb.Property;
import org.jooq.util.jaxb.Schema;
import org.jooq.util.jaxb.Strategy;
import org.jooq.util.jaxb.Target;
// ...


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

    private static final JooqLogger log = JooqLogger.getLogger(GenerationTool.class);

    private ClassLoader             loader;
    private DataSource              dataSource;
    private Connection              connection;
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
     * If set, the configuration XML's <code>&lt;jdbc/></code> configuration is
     * ignored, and this connection is used for meta data inspection, instead.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * The JDBC data source to use with this generation tool.
     * <p>
     * If set, the configuration XML's <code>&lt;jdbc/></code> configuration is
     * ignored, and this connection is used for meta data inspection, instead.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("Usage : GenerationTool <configuration-file>");
            System.exit(-1);
            return;
        }

        for (String arg : args) {
            InputStream in = GenerationTool.class.getResourceAsStream(arg);

            try {

                // [#2932] Retry loading the file, if it wasn't found. This may be helpful
                // to some users who were unaware that this file is loaded from the classpath
                if (in == null && !arg.startsWith("/"))
                    in = GenerationTool.class.getResourceAsStream("/" + arg);

                // [#3668] Also check the local file system for configuration files
                if (in == null && new File(arg).exists())
                    in = new FileInputStream(new File(arg));

                if (in == null) {
                    log.error("Cannot find " + arg + " on classpath, or in directory " + new File(".").getCanonicalPath());
                    log.error("-----------");
                    log.error("Please be sure it is located");
                    log.error("  - on the classpath and qualified as a classpath location.");
                    log.error("  - in the local directory or at a global path in the file system.");

                    System.exit(-1);
                    return;
                }

                log.info("Initialising properties", arg);
                generate(load(in));
            }
            catch (Exception e) {
                log.error("Cannot read " + arg + ". Error : " + e.getMessage(), e);

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
        new GenerationTool().run(load(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
    }

    public static void generate(Configuration configuration) throws Exception {
        new GenerationTool().run(configuration);
    }

    @SuppressWarnings("unchecked")
    public void run(Configuration configuration) throws Exception {
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

        Jdbc j = configuration.getJdbc();
        org.jooq.util.jaxb.Generator g = configuration.getGenerator();
        if (g == null)
            throw new GeneratorException("The <generator/> tag is mandatory. For details, see " + Constants.NS_CODEGEN);

        // Some default values for optional elements to avoid NPE's
        if (g.getStrategy() == null)
            g.setStrategy(new Strategy());
        if (g.getTarget() == null)
            g.setTarget(new Target());

        try {

            // Initialise connection
            // ---------------------
            if (connection == null) {
                close = true;

                if (dataSource != null) {
                    connection = dataSource.getConnection();
                }
                else if (j != null) {
                    Class<? extends Driver> driver = (Class<? extends Driver>) loadClass(driverClass(j));

                    Properties properties = properties(j.getProperties());
                    if (!properties.containsKey("user"))
                        properties.put("user", defaultString(defaultString(j.getUser(), j.getUsername())));
                    if (!properties.containsKey("password"))
                        properties.put("password", defaultString(j.getPassword()));

                    connection = driver.newInstance().connect(defaultString(j.getUrl()), properties);
                }
            }

            j = defaultIfNull(j, new Jdbc());


            // Initialise generator
            // --------------------
            Class<Generator> generatorClass = (Class<Generator>) (!isBlank(g.getName())
                ? loadClass(trim(g.getName()))
                : JavaGenerator.class);
            Generator generator = generatorClass.newInstance();

            GeneratorStrategy strategy;

            Matchers matchers = g.getStrategy().getMatchers();
            if (matchers != null) {
                strategy = new MatcherStrategy(matchers);

                if (g.getStrategy().getName() != null) {
                    log.warn("WARNING: Matchers take precedence over custom strategy. Strategy ignored: " +
                        g.getStrategy().getName());
                }
            }
            else {
                Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (!isBlank(g.getStrategy().getName())
                    ? loadClass(trim(g.getStrategy().getName()))
                    : DefaultGeneratorStrategy.class);
                strategy = strategyClass.newInstance();
            }

            generator.setStrategy(strategy);

            org.jooq.util.jaxb.Database d = defaultIfNull(g.getDatabase(), new org.jooq.util.jaxb.Database());
            String databaseName = trim(d.getName());
            Class<? extends Database> databaseClass = !isBlank(databaseName)
                ? (Class<? extends Database>) loadClass(databaseName)
                : connection != null
                ? databaseClass(connection)
                : databaseClass(j);
            Database database = databaseClass.newInstance();
            database.setProperties(properties(d.getProperties()));

            List<Catalog> catalogs = d.getCatalogs();
            List<Schema> schemata = d.getSchemata();

            boolean catalogsEmpty = catalogs.isEmpty();
            boolean schemataEmpty = schemata.isEmpty();

            // For convenience, the catalog configuration can be set also directly in the <database/> element
            if (catalogsEmpty) {
                Catalog catalog = new Catalog();
                catalog.setInputCatalog(trim(d.getInputCatalog()));
                catalog.setOutputCatalog(trim(d.getOutputCatalog()));
                catalog.setOutputCatalogToDefault(d.isOutputCatalogToDefault());
                catalogs.add(catalog);

                if (!StringUtils.isBlank(catalog.getInputCatalog()))
                    catalogsEmpty = false;

                // For convenience and backwards-compatibility, the schema configuration can be set also directly
                // in the <database/> element
                if (schemataEmpty) {
                    Schema schema = new Schema();
                    schema.setInputSchema(trim(d.getInputSchema()));
                    schema.setOutputSchema(trim(d.getOutputSchema()));
                    schema.setOutputSchemaToDefault(d.isOutputSchemaToDefault());
                    catalog.getSchemata().add(schema);

                    if (!StringUtils.isBlank(schema.getInputSchema()))
                        schemataEmpty = false;
                }
                else {
                    catalog.getSchemata().addAll(schemata);

                    if (!StringUtils.isBlank(d.getInputSchema()))
                        log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/schemata");
                    if (!StringUtils.isBlank(d.getOutputSchema()))
                        log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/schemata");
                }
            }
            else {
                if (!StringUtils.isBlank(d.getInputCatalog()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputCatalog and /configuration/generator/database/catalogs");
                if (!StringUtils.isBlank(d.getOutputCatalog()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputCatalog and /configuration/generator/database/catalogs");
                if (!StringUtils.isBlank(d.getInputSchema()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/catalogs");
                if (!StringUtils.isBlank(d.getOutputSchema()))
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/catalogs");
                if (!schemataEmpty)
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/catalogs and /configuration/generator/database/schemata");
            }

            for (Catalog catalog : catalogs) {
                if ("".equals(catalog.getOutputCatalog()))
                    log.warn("WARNING: Empty <outputCatalog/> should not be used to model default outputCatalogs. Use <outputCatalogToDefault>true</outputCatalogToDefault>, instead. See also: https://github.com/jOOQ/jOOQ/issues/3018");

                // [#3018] If users want the output catalog to be "" then, ignore the actual <outputCatalog/> configuration
                if (TRUE.equals(catalog.isOutputCatalogToDefault()))
                    catalog.setOutputCatalog("");
                else if (catalog.getOutputCatalog() == null)
                    catalog.setOutputCatalog(trim(catalog.getInputCatalog()));






                for (Schema schema : catalog.getSchemata()) {
                    if (catalogsEmpty && schemataEmpty && StringUtils.isBlank(schema.getInputSchema())) {
                        if (!StringUtils.isBlank(j.getSchema()))
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
            database.setIncludes(new String[] { defaultString(d.getIncludes()) });
            database.setExcludes(new String[] { defaultString(d.getExcludes()) });
            database.setIncludeExcludeColumns(TRUE.equals(d.isIncludeExcludeColumns()));
            database.setIncludeForeignKeys(!FALSE.equals(d.isIncludeForeignKeys()));
            database.setIncludePackages(!FALSE.equals(d.isIncludePackages()));
            database.setIncludePrimaryKeys(!FALSE.equals(d.isIncludePrimaryKeys()));
            database.setIncludeRoutines(!FALSE.equals(d.isIncludeRoutines()));
            database.setIncludeSequences(!FALSE.equals(d.isIncludeSequences()));
            database.setIncludeTables(!FALSE.equals(d.isIncludeTables()));
            database.setIncludeUDTs(!FALSE.equals(d.isIncludeUDTs()));
            database.setIncludeUniqueKeys(!FALSE.equals(d.isIncludeUniqueKeys()));
            database.setRecordVersionFields(new String[] { defaultString(d.getRecordVersionFields()) });
            database.setRecordTimestampFields(new String[] { defaultString(d.getRecordTimestampFields()) });
            database.setSyntheticPrimaryKeys(new String[] { defaultString(d.getSyntheticPrimaryKeys()) });
            database.setOverridePrimaryKeys(new String[] { defaultString(d.getOverridePrimaryKeys()) });
            database.setSyntheticIdentities(new String[] { defaultString(d.getSyntheticIdentities()) });
            database.setConfiguredCustomTypes(d.getCustomTypes());
            database.setConfiguredEnumTypes(d.getEnumTypes());
            database.setConfiguredForcedTypes(d.getForcedTypes());

            if (d.getRegexFlags() != null)
                database.setRegexFlags(d.getRegexFlags());

            SchemaVersionProvider svp = null;
            CatalogVersionProvider cvp = null;

            if (!StringUtils.isBlank(d.getSchemaVersionProvider())) {
                try {
                    svp = (SchemaVersionProvider) Class.forName(d.getSchemaVersionProvider()).newInstance();
                    log.info("Using custom schema version provider : " + svp);
                }
                catch (Exception ignore) {
                    if (d.getSchemaVersionProvider().toLowerCase().startsWith("select")) {
                        svp = new SQLSchemaVersionProvider(connection, d.getSchemaVersionProvider());
                        log.info("Using SQL schema version provider : " + d.getSchemaVersionProvider());
                    }
                    else {
                        svp = new ConstantSchemaVersionProvider(d.getSchemaVersionProvider());
                    }
                }
            }

            if (!StringUtils.isBlank(d.getCatalogVersionProvider())) {
                try {
                    cvp = (CatalogVersionProvider) Class.forName(d.getCatalogVersionProvider()).newInstance();
                    log.info("Using custom catalog version provider : " + cvp);
                }
                catch (Exception ignore) {
                    if (d.getCatalogVersionProvider().toLowerCase().startsWith("select")) {
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

            if (d.getEnumTypes().size() > 0)
                log.warn("DEPRECATED", "The configuration property /configuration/generator/database/enumTypes is experimental and deprecated and will be removed in the future.");
            if (Boolean.TRUE.equals(d.isDateAsTimestamp()))
                log.warn("DEPRECATED", "The configuration property /configuration/generator/database/dateAsTimestamp is deprecated as it is superseded by custom bindings and converters. It will thus be removed in the future.");

            if (d.isDateAsTimestamp() != null)
                database.setDateAsTimestamp(d.isDateAsTimestamp());
            if (d.isUnsignedTypes() != null)
                database.setSupportsUnsignedTypes(d.isUnsignedTypes());
            if (d.isIgnoreProcedureReturnValues() != null)
                database.setIgnoreProcedureReturnValues(d.isIgnoreProcedureReturnValues());

            if (Boolean.TRUE.equals(d.isIgnoreProcedureReturnValues()))
                log.warn("DEPRECATED", "The <ignoreProcedureReturnValues/> flag is deprecated and used for backwards-compatibility only. It will be removed in the future.");

            if (StringUtils.isBlank(g.getTarget().getPackageName()))
                g.getTarget().setPackageName("org.jooq.generated");
            if (StringUtils.isBlank(g.getTarget().getDirectory()))
                g.getTarget().setDirectory("target/generated-sources/jooq");
            if (StringUtils.isBlank(g.getTarget().getEncoding()))
                g.getTarget().setEncoding("UTF-8");

            generator.setTargetPackage(g.getTarget().getPackageName());
            generator.setTargetDirectory(g.getTarget().getDirectory());
            generator.setTargetEncoding(g.getTarget().getEncoding());

            // [#1394] The <generate/> element should be optional
            if (g.getGenerate() == null)
                g.setGenerate(new Generate());
            if (g.getGenerate().isRelations() != null)
                generator.setGenerateRelations(g.getGenerate().isRelations());
            if (g.getGenerate().isDeprecated() != null)
                generator.setGenerateDeprecated(g.getGenerate().isDeprecated());
            if (g.getGenerate().isInstanceFields() != null)
                generator.setGenerateInstanceFields(g.getGenerate().isInstanceFields());
            if (g.getGenerate().isGeneratedAnnotation() != null)
                generator.setGenerateGeneratedAnnotation(g.getGenerate().isGeneratedAnnotation());
            if (g.getGenerate().isRoutines() != null)
                generator.setGenerateRoutines(g.getGenerate().isRoutines());
            if (g.getGenerate().isSequences() != null)
                generator.setGenerateSequences(g.getGenerate().isSequences());
            if (g.getGenerate().isUdts() != null)
                generator.setGenerateUDTs(g.getGenerate().isUdts());
            if (g.getGenerate().isTables() != null)
                generator.setGenerateTables(g.getGenerate().isTables());
            if (g.getGenerate().isRecords() != null)
                generator.setGenerateRecords(g.getGenerate().isRecords());
            if (g.getGenerate().isPojos() != null)
                generator.setGeneratePojos(g.getGenerate().isPojos());
            if (g.getGenerate().isImmutablePojos() != null)
                generator.setGenerateImmutablePojos(g.getGenerate().isImmutablePojos());
            if (g.getGenerate().isInterfaces() != null)
                generator.setGenerateInterfaces(g.getGenerate().isInterfaces());
            if (g.getGenerate().isImmutableInterfaces() != null)
                generator.setGenerateImmutableInterfaces(g.getGenerate().isImmutableInterfaces());
            if (g.getGenerate().isDaos() != null)
                generator.setGenerateDaos(g.getGenerate().isDaos());
            if (g.getGenerate().isJpaAnnotations() != null)
                generator.setGenerateJPAAnnotations(g.getGenerate().isJpaAnnotations());
            if (g.getGenerate().isValidationAnnotations() != null)
                generator.setGenerateValidationAnnotations(g.getGenerate().isValidationAnnotations());
            if (g.getGenerate().isSpringAnnotations() != null)
                generator.setGenerateSpringAnnotations(g.getGenerate().isSpringAnnotations());
            if (g.getGenerate().isQueues() != null)
                generator.setGenerateQueues(g.getGenerate().isQueues());
            if (g.getGenerate().isLinks() != null)
                generator.setGenerateLinks(g.getGenerate().isLinks());
            if (g.getGenerate().isGlobalLinkReferences() != null)
                generator.setGenerateGlobalLinkReferences(g.getGenerate().isGlobalLinkReferences());
            if (g.getGenerate().isGlobalObjectReferences() != null)
                generator.setGenerateGlobalObjectReferences(g.getGenerate().isGlobalObjectReferences());
            if (g.getGenerate().isGlobalCatalogReferences() != null)
                generator.setGenerateGlobalCatalogReferences(g.getGenerate().isGlobalCatalogReferences());
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
            if (g.getGenerate().isFluentSetters() != null)
                generator.setFluentSetters(g.getGenerate().isFluentSetters());
            if (g.getGenerate().isPojosEqualsAndHashCode() != null)
                generator.setGeneratePojosEqualsAndHashCode(g.getGenerate().isPojosEqualsAndHashCode());
            if (g.getGenerate().isPojosToString() != null)
                generator.setGeneratePojosToString(g.getGenerate().isPojosToString());
            if (g.getGenerate().getFullyQualifiedTypes() != null)
                generator.setGenerateFullyQualifiedTypes(g.getGenerate().getFullyQualifiedTypes());
            if (g.getGenerate().isJavaTimeTypes() != null)
                generator.setGenerateJavaTimeTypes(g.getGenerate().isJavaTimeTypes());
            if (g.getGenerate().isEmptyCatalogs() != null)
                generator.setGenerateEmptyCatalogs(g.getGenerate().isEmptyCatalogs());
            if (g.getGenerate().isEmptySchemas() != null)
                generator.setGenerateEmptySchemas(g.getGenerate().isEmptySchemas());


            // [#3669] Optional Database element
            if (g.getDatabase() == null)
                g.setDatabase(new org.jooq.util.jaxb.Database());
            if (!StringUtils.isBlank(g.getDatabase().getSchemaVersionProvider()))
                generator.setUseSchemaVersionProvider(true);
            if (!StringUtils.isBlank(g.getDatabase().getCatalogVersionProvider()))
                generator.setUseCatalogVersionProvider(true);
            if (g.getDatabase().isTableValuedFunctions() != null)
                generator.setGenerateTableValuedFunctions(g.getDatabase().isTableValuedFunctions());
            else {
                generator.setGenerateTableValuedFunctions(true);







            }

            // Generator properties that should in fact be strategy properties
            strategy.setInstanceFields(generator.generateInstanceFields());


            if (true)
                ;
            else

                if (g.getGenerate().isJavaTimeTypes() != null) {
                    log.warn("INVALID CONFIG", "The java.time API cannot be used in the Java 6 distribution of jOOQ 3.9+");
                    generator.setGenerateJavaTimeTypes(false);
                }

            generator.generate(database);
        }
        finally {

            // Close connection only if it was created by the GenerationTool
            if (close && connection != null)
                connection.close();
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
                try {
                    return Class.forName(className);
                }
                catch (ClassNotFoundException e) {
                    return Thread.currentThread().getContextClassLoader().loadClass(className);
                }
            }

            // Prefer the explicit class loader if available
            else {
                return loader.loadClass(className);
            }
        }

        // [#2801] [#4620]
        catch (ClassNotFoundException e) {
            if (className.startsWith("org.jooq.util.") && className.endsWith("Database")) {
                log.warn("Type not found",
                      "Your configured database type was not found. This can have several reasons:\n"
                    + "- You want to use a commercial jOOQ Edition, but you pulled the Open Source Edition from Maven Central.\n"
                    + "- You have mis-typed your class name.");
            }

            throw e;
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

        xml = xml.replace(
            "<configuration>",
            "<configuration xmlns=\"" + Constants.NS_CODEGEN + "\">");

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            javax.xml.validation.Schema schema = sf.newSchema(
                GenerationTool.class.getResource("/xsd/" + Constants.XSD_CODEGEN)
            );

            JAXBContext ctx = JAXBContext.newInstance(Configuration.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    log.warn("Unmarshal warning", event.getMessage());
                    return true;
                }
            });
            return (Configuration) unmarshaller.unmarshal(new StringReader(xml));
        }
        catch (Exception e) {
            throw new GeneratorException("Error while reading XML configuration", e);
        }
    }
}
