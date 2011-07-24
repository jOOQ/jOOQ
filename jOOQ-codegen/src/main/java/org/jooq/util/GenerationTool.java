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

import static org.jooq.impl.StringUtils.defaultString;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.jooq.impl.JooqLogger;

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
		if (args.length != 1) {
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
		Properties properties = new Properties();

		try {
			properties.load(in);
		} catch (Exception e) {
			log.error("Cannot read " + args[0] + ". Error : " + e.getMessage());
			error();
		} finally {
			if (in != null) {
				in.close();
			}
		}

		main(properties);
	}

	@SuppressWarnings("unchecked")
	public static void main(Properties properties) throws Exception {
		Class.forName(properties.getProperty("jdbc.Driver"));
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					properties.getProperty("jdbc.URL"),
					properties.getProperty("jdbc.User"),
					properties.getProperty("jdbc.Password"));

			Class<Generator> generatorClass = (Class<Generator>) (properties.containsKey("generator")
			    ? Class.forName(properties.getProperty("generator"))
			    : DefaultGenerator.class);
			Generator generator = generatorClass.newInstance();

			Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (properties.containsKey("generator.strategy")
			    ? Class.forName(properties.getProperty("generator.strategy"))
			    : DefaultGeneratorStrategy.class);
			GeneratorStrategy strategy = strategyClass.newInstance();

			strategy.setMetaClassPrefix(defaultString(properties.getProperty("generator.strategy.meta-class-prefix")));
			strategy.setMetaClassSuffix(defaultString(properties.getProperty("generator.strategy.meta-class-suffix")));
			strategy.setRecordClassPrefix(defaultString(properties.getProperty("generator.strategy.record-class-prefix")));
			strategy.setRecordClassSuffix(defaultString(properties.getProperty("generator.strategy.record-class-suffix", "Record")));
			strategy.setMemberScheme(defaultString(properties.getProperty("generator.strategy.member-scheme", "camel-case")));

            generator.setStrategy(strategy);

			Class<Database> databaseClass = (Class<Database>) Class.forName(properties.getProperty("generator.database"));
			Database database = databaseClass.newInstance();

			database.setConnection(connection);
			database.setSchemaName(properties.getProperty("jdbc.Schema"));
			database.setIncludes(defaultString(properties.getProperty("generator.database.includes")).split(","));
			database.setExcludes(defaultString(properties.getProperty("generator.database.excludes")).split(","));
			database.setMasterDataTableNames(defaultString(properties.getProperty("generator.generate.master-data-tables")).split(","));
			database.setProperties(properties);

			generator.setTargetPackage(properties.getProperty("generator.target.package"));
			generator.setTargetDirectory(properties.getProperty("generator.target.directory"));
			generator.setGenerateRelations("true".equalsIgnoreCase(properties.getProperty("generator.generate.relations")));
			generator.setGenerateDeprecated(!"false".equalsIgnoreCase(properties.getProperty("generator.generate.deprecated")));

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
