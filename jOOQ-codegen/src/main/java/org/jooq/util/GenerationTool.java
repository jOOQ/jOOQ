/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXB;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.MasterDataTable;
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

	/**
	 * @param args
	 * @throws Exception
	 */
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
    		if (args[0].endsWith(".xml")) {
    		    main(JAXB.unmarshal(in, Configuration.class));
    		}
    		else {
    	        Properties properties = new Properties();
                properties.load(in);
    	        main(properties, args);
    		}
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

	public static void main(Properties properties, String... args) throws Exception {
	    Jdbc jdbc = new Jdbc();
	    jdbc.setDriver(properties.getProperty("jdbc.Driver"));
	    jdbc.setUrl(properties.getProperty("jdbc.URL"));
	    jdbc.setUser(properties.getProperty("jdbc.User"));
	    jdbc.setPassword(properties.getProperty("jdbc.Password"));
	    jdbc.setSchema(properties.getProperty("jdbc.Schema"));

	    Strategy strategy = new Strategy();
	    strategy.setName(properties.containsKey("generator.strategy") ? properties.getProperty("generator.strategy") : null);

	    List<MasterDataTable> masterDataTables = new ArrayList<MasterDataTable>();
	    for (String name : defaultString(properties.getProperty("generator.generate.master-data-tables")).split(",")) {
	        if (isBlank(name)) continue;

	        MasterDataTable table = new MasterDataTable();

	        table.setName(name);
	        table.setLiteral(properties.getProperty("generator.generate.master-data-table-literal." + name));
	        table.setDescription(properties.getProperty("generator.generate.master-data-table-description." + name));

	        masterDataTables.add(table);
	    }

        List<EnumType> enumTypes = new ArrayList<EnumType>();
        for (String property : properties.stringPropertyNames()) {
            if (property.startsWith("generator.database.enum-type.")) {
                String name = property.replace("generator.database.enum-type.", "");

                EnumType type = new EnumType();
                type.setName(name);
                type.setLiterals(properties.getProperty(property));
                enumTypes.add(type);
            }
        }

        List<ForcedType> forcedTypes = new ArrayList<ForcedType>();
        for (String property : properties.stringPropertyNames()) {
            if (property.startsWith("generator.database.forced-type.")) {
                String name = property.replace("generator.database.forced-type.", "");

                ForcedType type = new ForcedType();
                type.setName(name);
                type.setExpressions(properties.getProperty(property));
                forcedTypes.add(type);
            }
        }

	    org.jooq.util.jaxb.Database database = new org.jooq.util.jaxb.Database();
	    database.setName(properties.getProperty("generator.database"));
	    database.setIncludes(properties.containsKey("generator.database.includes") ? properties.getProperty("generator.database.includes") : null);
	    database.setExcludes(properties.containsKey("generator.database.excludes") ? properties.getProperty("generator.database.excludes") : null);
	    database.setDateAsTimestamp("true".equalsIgnoreCase(properties.getProperty("generator.database.date-as-timestamp")));
	    database.setUnsignedTypes(!"false".equalsIgnoreCase(properties.getProperty("generator.generate.unsigned-types")));
	    database.setInputSchema(properties.containsKey("generator.database.input-schema") ? properties.getProperty("generator.database.input-schema") : null);
	    database.setOutputSchema(properties.containsKey("generator.database.output-schema") ? properties.getProperty("generator.database.output-schema") : null);

	    // Avoid creating these empty elements when migrating
	    if (!masterDataTables.isEmpty())
	        database.getMasterDataTables().addAll(masterDataTables);

	    if (!enumTypes.isEmpty())
	        database.getEnumTypes().addAll(enumTypes);

	    if (!forcedTypes.isEmpty())
	        database.getForcedTypes().addAll(forcedTypes);

	    Target target = new Target();
	    target.setPackageName(properties.getProperty("generator.target.package"));
	    target.setDirectory(properties.getProperty("generator.target.directory"));

	    Generate generate = new Generate();
	    generate.setRelations("true".equalsIgnoreCase(properties.getProperty("generator.generate.relations")));
	    generate.setDeprecated(!"false".equalsIgnoreCase(properties.getProperty("generator.generate.deprecated")));
	    generate.setInstanceFields(!"false".equalsIgnoreCase(properties.getProperty("generator.generate.instance-fields")));
	    generate.setGeneratedAnnotation(!"false".equalsIgnoreCase(properties.getProperty("generator.generate.generated-annotation")));
	    generate.setPojos("true".equalsIgnoreCase(properties.getProperty("generator.generate.pojos")));
	    generate.setJpaAnnotations("true".equalsIgnoreCase(properties.getProperty("generator.generate.jpa-annotations")));

	    org.jooq.util.jaxb.Generator generator = new org.jooq.util.jaxb.Generator();

	    if (!isBlank(strategy.getName()))
	        generator.setStrategy(strategy);

	    generator.setDatabase(database);
        generator.setTarget(target);
        generator.setGenerate(generate);
	    generator.setName(properties.containsKey("generator") ? properties.getProperty("generator") : null);

	    Configuration configuration = new Configuration();
        configuration.setJdbc(jdbc);
        configuration.setGenerator(generator);

        if (args.length < 2) {
            log.warn("WARNING: jooq-codegen source code generation using .properties files is deprecated as of jOOQ 2.0.4");
            log.info("         Consider using XML configuration instead");
            log.info("         See http://www.jooq.org/manual/META/Configuration/ for more details");
            log.info("");
            log.info("Use GenerationTool to migrate your .properties file to XML (printed on System.out) :");
            log.info("Usage  : GenerationTool <configuration-file> migrate");
            log.info("");

            main(configuration);
        }
        else if ("migrate".equals(args[1])) {
            log.info("Migrating properties to XML");
            JAXB.marshal(configuration, System.out);
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
            connection = DriverManager.getConnection(
                    defaultString(j.getUrl()),
                    defaultString(j.getUser()),
                    defaultString(j.getPassword()));

            Class<Generator> generatorClass = (Class<Generator>) (!isBlank(g.getName())
                ? Class.forName(g.getName())
                : DefaultGenerator.class);
            Generator generator = generatorClass.newInstance();

            Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (!isBlank(g.getStrategy().getName())
                ? Class.forName(g.getStrategy().getName())
                : DefaultGeneratorStrategy.class);
            GeneratorStrategy strategy = strategyClass.newInstance();

            generator.setStrategy(strategy);

            Class<Database> databaseClass = (Class<Database>) Class.forName(g.getDatabase().getName());
            Database database = databaseClass.newInstance();

            List<Schema> schemata = g.getDatabase().getSchemata();
            if (schemata.isEmpty()) {
                Schema schema = new Schema();
                schema.setInputSchema(g.getDatabase().getInputSchema());
                schema.setOutputSchema(g.getDatabase().getOutputSchema());
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
                    log.warn("WARNING: The configuration property jdbc.Schema is deprecated and will be removed in the future. Use /configuration/generator/database/inputSchema instead");
                    schema.setInputSchema(j.getSchema());
                }

                if (StringUtils.isBlank(schema.getOutputSchema())) {
                    schema.setOutputSchema(schema.getInputSchema());
                }
            }

            database.setConnection(connection);
            database.setConfiguredSchemata(schemata);
            database.setIncludes(defaultString(g.getDatabase().getIncludes()).split(","));
            database.setExcludes(defaultString(g.getDatabase().getExcludes()).split(","));
            database.setConfiguredMasterDataTables(g.getDatabase().getMasterDataTables());
            database.setConfiguredEnumTypes(g.getDatabase().getEnumTypes());
            database.setConfiguredForcedTypes(g.getDatabase().getForcedTypes());

            if (g.getDatabase().isDateAsTimestamp() != null)
                database.setDateAsTimestamp(g.getDatabase().isDateAsTimestamp());
            if (g.getDatabase().isUnsignedTypes() != null)
                database.setSupportsUnsignedTypes(g.getDatabase().isUnsignedTypes());

            if (StringUtils.isBlank(g.getTarget().getPackageName()))
                g.getTarget().setPackageName("org.jooq.generated");
            if (StringUtils.isBlank(g.getTarget().getDirectory()))
                g.getTarget().setPackageName("target/generated-sources/jooq");

            generator.setTargetPackage(g.getTarget().getPackageName());
            generator.setTargetDirectory(g.getTarget().getDirectory());

            if (g.getGenerate().isRelations() != null)
                generator.setGenerateRelations(g.getGenerate().isRelations());
            if (g.getGenerate().isNavigationMethods() != null)
                generator.setGenerateNavigationMethods(g.getGenerate().isNavigationMethods());
            if (g.getGenerate().isDeprecated() != null)
                generator.setGenerateDeprecated(g.getGenerate().isDeprecated());
            if (g.getGenerate().isInstanceFields() != null)
                generator.setGenerateInstanceFields(g.getGenerate().isInstanceFields());
            if (g.getGenerate().isGeneratedAnnotation() != null)
                generator.setGenerateGeneratedAnnotation(g.getGenerate().isGeneratedAnnotation());
            if (g.getGenerate().isPojos() != null)
                generator.setGeneratePojos(g.getGenerate().isPojos());
            if (g.getGenerate().isJpaAnnotations() != null)
                generator.setGenerateJPAAnnotations(g.getGenerate().isJpaAnnotations());

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

	private static void error() {
		log.error("Usage : GenerationTool <configuration-file>");
		System.exit(-1);
	}
}
