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
package org.jooq.util;

import static java.lang.Boolean.TRUE;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.defaultString;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.SchemaFactory;

import org.jooq.Constants;
import org.jooq.SQLDialect;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
// ...
import org.jooq.util.cubrid.CUBRIDDatabase;
// ...
import org.jooq.util.derby.DerbyDatabase;
import org.jooq.util.firebird.FirebirdDatabase;
import org.jooq.util.h2.H2Database;
import org.jooq.util.hsqldb.HSQLDBDatabase;
// ...
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Matchers;
import org.jooq.util.jaxb.Property;
import org.jooq.util.jaxb.Schema;
import org.jooq.util.jaxb.Strategy;
import org.jooq.util.jaxb.Target;
import org.jooq.util.jdbc.JDBCDatabase;
import org.jooq.util.mariadb.MariaDBDatabase;
import org.jooq.util.mysql.MySQLDatabase;
// ...
import org.jooq.util.postgres.PostgresDatabase;
import org.jooq.util.sqlite.SQLiteDatabase;
// ...
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

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            error();
        }

        InputStream in = GenerationTool.class.getResourceAsStream(args[0]);

        if (in == null) {

            // [#2932] Retry loading the file, if it wasn't found. This may be helpful
            // to some users who were unaware that this file is loaded from the classpath
            if (!args[0].startsWith("/"))
                in = GenerationTool.class.getResourceAsStream("/" + args[0]);

            if (in == null) {
                log.error("Cannot find " + args[0]);
                log.error("-----------");
                log.error("Please be sure it is located on the classpath and qualified as a classpath location.");
                log.error("If it is located at the current working directory, try adding a '/' to the path");
                error();
            }
        }

        log.info("Initialising properties", args[0]);

        try {
            main(load(in));
        } catch (Exception e) {
            log.error("Cannot read " + args[0] + ". Error : " + e.getMessage());
            e.printStackTrace();
            error();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static void main(Configuration configuration) throws Exception {
        new GenerationTool().run(configuration);
    }

    @SuppressWarnings("unchecked")
    public void run(Configuration configuration) throws Exception {
        Jdbc j = configuration.getJdbc();
        org.jooq.util.jaxb.Generator g = configuration.getGenerator();
        errorIfNull(g, "The <generator/> tag is mandatory.");

        // Some default values for optional elements to avoid NPE's
        if (g.getStrategy() == null)
            g.setStrategy(new Strategy());
        if (g.getTarget() == null)
            g.setTarget(new Target());

        try {

            // Initialise connection
            // ---------------------
            if (connection == null) {
                errorIfNull(j, "The <jdbc/> tag is mandatory.");
                loadClass(j.getDriver());

                Properties properties = new Properties();
                for (Property p : j.getProperties()) {
                    properties.put(p.getKey(), p.getValue());
                }

                if (!properties.containsKey("user"))
                    properties.put("user", defaultString(j.getUser()));
                if (!properties.containsKey("password"))
                    properties.put("password", defaultString(j.getPassword()));

                connection = DriverManager.getConnection(defaultString(j.getUrl()), properties);
                close = true;
            }
            else {
                j = defaultIfNull(j, new Jdbc());
            }


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

            org.jooq.util.jaxb.Database d = g.getDatabase();
            errorIfNull(d, "The <database/> tag is mandatory.");

            String databaseName = trim(d.getName());
            Class<? extends Database> databaseClass = isBlank(databaseName)
                ? databaseClass(j)
                : (Class<? extends Database>) loadClass(databaseName);
            Database database = databaseClass.newInstance();

            List<Schema> schemata = d.getSchemata();
            if (schemata.isEmpty()) {
                Schema schema = new Schema();
                schema.setInputSchema(trim(d.getInputSchema()));
                schema.setOutputSchema(trim(d.getOutputSchema()));
                schemata.add(schema);
            }
            else {
                if (!StringUtils.isBlank(d.getInputSchema())) {
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/schemata");
                }
                if (!StringUtils.isBlank(d.getOutputSchema())) {
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/schemata");
                }
            }

            for (Schema schema : schemata) {
                if (StringUtils.isBlank(schema.getInputSchema())) {
                    if (!StringUtils.isBlank(j.getSchema())) {
                        log.warn("WARNING: The configuration property jdbc.Schema is deprecated and will be removed in the future. Use /configuration/generator/database/inputSchema instead");
                    }

                    schema.setInputSchema(trim(j.getSchema()));
                }

                if (schema.getOutputSchema() == null) {
                    schema.setOutputSchema(trim(schema.getInputSchema()));
                }
            }

            if (schemata.size() == 1) {
                if (StringUtils.isBlank(schemata.get(0).getInputSchema())) {
                    log.info("No <inputSchema/> was provided. Generating ALL available schemata instead!");
                }
            }

            database.setConnection(connection);
            database.setConfiguredSchemata(schemata);
            database.setIncludes(new String[] { defaultString(d.getIncludes()) });
            database.setExcludes(new String[] { defaultString(d.getExcludes()) });
            database.setIncludeExcludeColumns(TRUE.equals(d.isIncludeExcludeColumns()));
            database.setRecordVersionFields(new String[] { defaultString(d.getRecordVersionFields()) });
            database.setRecordTimestampFields(new String[] { defaultString(d.getRecordTimestampFields()) });
            database.setOverridePrimaryKeys(new String[] { defaultString(d.getOverridePrimaryKeys()) });
            database.setConfiguredCustomTypes(d.getCustomTypes());
            database.setConfiguredEnumTypes(d.getEnumTypes());
            database.setConfiguredForcedTypes(d.getForcedTypes());

            if (d.getEnumTypes().size() > 0) {
                log.warn("WARNING: The configuration property /configuration/generator/database/enumTypes is experimental and deprecated and will be removed in the future.");
            }

            if (d.isDateAsTimestamp() != null)
                database.setDateAsTimestamp(d.isDateAsTimestamp());
            if (d.isUnsignedTypes() != null)
                database.setSupportsUnsignedTypes(d.isUnsignedTypes());

            if (StringUtils.isBlank(g.getTarget().getPackageName()))
                g.getTarget().setPackageName("org.jooq.generated");
            if (StringUtils.isBlank(g.getTarget().getDirectory()))
                g.getTarget().setDirectory("target/generated-sources/jooq");

            generator.setTargetPackage(g.getTarget().getPackageName());
            generator.setTargetDirectory(g.getTarget().getDirectory());

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
            if (g.getGenerate().isRecords() != null)
                generator.setGenerateRecords(g.getGenerate().isRecords());
            if (g.getGenerate().isPojos() != null)
                generator.setGeneratePojos(g.getGenerate().isPojos());
            if (g.getGenerate().isImmutablePojos() != null)
                generator.setGenerateImmutablePojos(g.getGenerate().isImmutablePojos());
            if (g.getGenerate().isInterfaces() != null)
                generator.setGenerateInterfaces(g.getGenerate().isInterfaces());
            if (g.getGenerate().isDaos() != null)
                generator.setGenerateDaos(g.getGenerate().isDaos());
            if (g.getGenerate().isJpaAnnotations() != null)
                generator.setGenerateJPAAnnotations(g.getGenerate().isJpaAnnotations());
            if (g.getGenerate().isValidationAnnotations() != null)
                generator.setGenerateValidationAnnotations(g.getGenerate().isValidationAnnotations());
            if (g.getGenerate().isGlobalObjectReferences() != null)
                generator.setGenerateGlobalObjectReferences(g.getGenerate().isGlobalObjectReferences());
            if (g.getGenerate().isFluentSetters() != null)
                generator.setFluentSetters(g.getGenerate().isFluentSetters());

            // Generator properties that should in fact be strategy properties
            strategy.setInstanceFields(generator.generateInstanceFields());


            generator.generate(database);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {

            // Close connection only if it was created by the GenerationTool
            if (close && connection != null) {
                connection.close();
            }
        }
    }

    private Class<? extends Database> databaseClass(Jdbc j) {
        SQLDialect dialect = JDBCUtils.dialect(j.getUrl());
        Class<? extends Database> result = JDBCDatabase.class;

        switch (dialect.family()) {
            /* [pro] xx
            xxxx xxxxxxx    xxxxxx x xxxxxxxxxxxxxxxxxxx      xxxxxx
            xxxx xxxx       xxxxxx x xxxxxxxxxxxxxxxxxx       xxxxxx
            xxxx xxxx       xxxxxx x xxxxxxxxxxxxxxxxxx       xxxxxx
            xxxx xxxxxxx    xxxxxx x xxxxxxxxxxxxxxxxxxxxx    xxxxxx
            xxxx xxxxxxx    xxxxxx x xxxxxxxxxxxxxxxxxxxxx    xxxxxx
            xxxx xxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
            xxxx xxxxxxx    xxxxxx x xxxxxxxxxxxxxxxxxxxxx    xxxxxx
            xx [/pro] */

            case CUBRID:    result = CUBRIDDatabase.class;    break;
            case DERBY:     result = DerbyDatabase.class;     break;
            case FIREBIRD:  result = FirebirdDatabase.class;  break;
            case H2:        result = H2Database.class;        break;
            case HSQLDB:    result = HSQLDBDatabase.class;    break;
            case MARIADB:   result = MariaDBDatabase.class;   break;
            case MYSQL:     result = MySQLDatabase.class;     break;
            case POSTGRES:  result = PostgresDatabase.class;  break;
            case SQLITE:    result = SQLiteDatabase.class;    break;
        }

        log.info("Database", "Inferring database " + result.getName() + " from URL " + j.getUrl());
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

        // [#2801]
        catch (ClassNotFoundException e) {
            if (className.startsWith("org.jooq.util.")) {
                log.warn("Licensing", "With jOOQ 3.2, licensing has changed, and your database may no longer be supported with jOOQ Open Source Edition. See http://www.jooq.org/licensing for details");
            }

            throw e;
        }
    }

    private static String trim(String string) {
        return (string == null ? null : string.trim());
    }

    private static void errorIfNull(Object o, String message) {
        if (o == null) {
            log.error(message + " For details, see http://www.jooq.org/xsd/jooq-codegen-3.3.0.xsd");
            System.exit(-1);
        }
    }

    private static void error() {
        log.error("Usage : GenerationTool <configuration-file>");
        System.exit(-1);
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
            throw new RuntimeException(e);
        }
    }
}
