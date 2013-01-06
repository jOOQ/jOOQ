/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.util;

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

import javax.xml.bind.JAXB;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Property;
import org.jooq.util.jaxb.Schema;
import org.jooq.util.jaxb.Strategy;
import org.jooq.util.jaxb.Target;

/**
 * The GenerationTool takes care of generating Java code from a database schema. It
 * takes its configuration parameters from a standard Java properties file,
 * using this format:
 *
 * <ul>
 *
 * <li>jdbcDriver = [the JDBC driver to connect with. Make sure it is on the
 * classpath]</li>
 * <li>jdbcURL = [the JDBC URL to connect with]</li>
 * <li>jdbcUser = [the JDBC user to connect with]</li>
 * <li>jdbcPassword = [the JDBC password to connect with]</li>
 *
 * </ul>
 *
 * @author Lukas Eder
 */
public class GenerationTool {

    private static final JooqLogger log = JooqLogger.getLogger(GenerationTool.class);

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			error();
		}

		InputStream in = GenerationTool.class.getResourceAsStream(args[0]);

		if (in == null) {
			log.error("Cannot find " + args[0]);
			log.error("-----------");
			log.error("Please be sure it is located on the classpath and qualified as a classpath location.");
			log.error("If it is located at the current working directory, try adding a '/' to the path");
			error();
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

	@SuppressWarnings("unchecked")
    public static void main(Configuration configuration) throws Exception {
	    Jdbc j = configuration.getJdbc();
	    org.jooq.util.jaxb.Generator g = configuration.getGenerator();

	    // Some default values for optional elements to avoid NPE's
	    if (g.getStrategy() == null)
	        g.setStrategy(new Strategy());
	    if (g.getTarget() == null)
	        g.setTarget(new Target());

        Class.forName(j.getDriver());
        Connection connection = null;

        try {

            // Initialise connection
            // ---------------------
            Properties properties = new Properties();
            for (Property p : j.getProperties()) {
                properties.put(p.getKey(), p.getValue());
            }

            if (!properties.containsKey("user"))
                properties.put("user", defaultString(j.getUser()));
            if (!properties.containsKey("password"))
                properties.put("password", defaultString(j.getPassword()));

            connection = DriverManager.getConnection(defaultString(j.getUrl()), properties);

            // Initialise generator
            // --------------------
            Class<Generator> generatorClass = (Class<Generator>) (!isBlank(g.getName())
                ? Class.forName(trim(g.getName()))
                : JavaGenerator.class);
            Generator generator = generatorClass.newInstance();

            Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (!isBlank(g.getStrategy().getName())
                ? Class.forName(trim(g.getStrategy().getName()))
                : DefaultGeneratorStrategy.class);
            GeneratorStrategy strategy = strategyClass.newInstance();

            generator.setStrategy(strategy);

            Class<Database> databaseClass = (Class<Database>) Class.forName(trim(g.getDatabase().getName()));
            Database database = databaseClass.newInstance();

            List<Schema> schemata = g.getDatabase().getSchemata();
            if (schemata.isEmpty()) {
                Schema schema = new Schema();
                schema.setInputSchema(trim(g.getDatabase().getInputSchema()));
                schema.setOutputSchema(trim(g.getDatabase().getOutputSchema()));
                schemata.add(schema);
            }
            else {
                if (!StringUtils.isBlank(g.getDatabase().getInputSchema())) {
                    log.warn("WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/schemata");
                }
                if (!StringUtils.isBlank(g.getDatabase().getOutputSchema())) {
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

                if (StringUtils.isBlank(schema.getOutputSchema())) {
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
            database.setIncludes(new String[] { defaultString(g.getDatabase().getIncludes()) });
            database.setExcludes(new String[] { defaultString(g.getDatabase().getExcludes()) });
            database.setRecordVersionFields(new String[] { defaultString(g.getDatabase().getRecordVersionFields()) });
            database.setRecordTimestampFields(new String[] { defaultString(g.getDatabase().getRecordTimestampFields()) });
            database.setConfiguredCustomTypes(g.getDatabase().getCustomTypes());
            database.setConfiguredEnumTypes(g.getDatabase().getEnumTypes());
            database.setConfiguredForcedTypes(g.getDatabase().getForcedTypes());

            if (g.getDatabase().getEnumTypes().size() > 0) {
                log.warn("WARNING: The configuration property /configuration/generator/database/enumTypes is experimental and deprecated and will be removed in the future.");
            }

            if (g.getDatabase().isDateAsTimestamp() != null)
                database.setDateAsTimestamp(g.getDatabase().isDateAsTimestamp());
            if (g.getDatabase().isUnsignedTypes() != null)
                database.setSupportsUnsignedTypes(g.getDatabase().isUnsignedTypes());

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

            // Generator properties that should in fact be strategy properties
            strategy.setInstanceFields(generator.generateInstanceFields());


            generator.generate(database);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
	}

	private static String trim(String string) {
	    return (string == null ? null : string.trim());
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
            "<$1configuration xmlns$2=\"http://www.jooq.org/xsd/jooq-codegen-3.0.0.xsd\">");

        xml = xml.replace(
            "<configuration>",
            "<configuration xmlns=\"http://www.jooq.org/xsd/jooq-codegen-3.0.0.xsd\">");

        return JAXB.unmarshal(new StringReader(xml), Configuration.class);
    }
}
