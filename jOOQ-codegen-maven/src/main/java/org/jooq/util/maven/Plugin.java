/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util.maven;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;
import static org.jooq.Constants.XSD_CODEGEN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.xml.bind.JAXB;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * The jOOQ Codegen Plugin
 *
 * @author Sander Plas
 * @author Lukas Eder
 */
@Mojo(
    name = "generate",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST
)
public class Plugin extends AbstractMojo {

    /**
     * The Maven project.
     */
    @Parameter(
        property = "project",
        required = true,
        readonly = true
    )
    private MavenProject                 project;

    /**
     * An external configuration file that overrides anything in the Maven configuration
     */
    @Parameter
    private String                       configurationFile;

    /**
     * Whether to skip the execution of the Maven Plugin for this module.
     */
    @Parameter
    private boolean                      skip;

    /**
     * The logging threshold.
     */
    @Parameter
    private org.jooq.util.jaxb.Logging   logging;

    /**
     * The jdbc settings.
     */
    @Parameter
    private org.jooq.util.jaxb.Jdbc      jdbc;

    /**
     * The generator settings
     */
    @Parameter
    private org.jooq.util.jaxb.Generator generator;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping jOOQ code generation");
            return;
        }

        if (configurationFile != null) {
            getLog().info("Reading external configuration");
            File file = new File(configurationFile);

            if (!file.isAbsolute())
                file = new File(project.getBasedir(), configurationFile);

            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                Configuration configuration = GenerationTool.load(in);
                generator = configuration.getGenerator();
                jdbc = configuration.getJdbc();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException ignore) {}
                }
            }
        }

        // [#5286] There are a variety of reasons why the generator isn't set up
        //         correctly at this point. We'll log them all here.
        if (generator == null) {
            getLog().error("Incorrect configuration of jOOQ code generation tool");
            getLog().error(
                  "\n"
                + "The jOOQ-codegen-maven module's generator configuration is not set up correctly.\n"
                + "This can have a variety of reasons, among which:\n"
                + "- Your pom.xml's <configuration> contains invalid XML according to " + XSD_CODEGEN + "\n"
                + "- There is a version or artifact mismatch between your pom.xml and your commandline");

            throw new MojoExecutionException("Incorrect configuration of jOOQ code generation tool. See error above for details.");
        }

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();

        try {
            // [#2886] Add the surrounding project's dependencies to the current classloader
            Thread.currentThread().setContextClassLoader(getClassLoader());

            // [#2887] Patch relative paths to take plugin execution basedir into account
            String dir = generator.getTarget().getDirectory();
            if (!new File(dir).isAbsolute()) {
                generator.getTarget().setDirectory(project.getBasedir() + File.separator + dir);
            }

            Configuration configuration = new Configuration();
            configuration.setLogging(logging);
            configuration.setJdbc(jdbc);
            configuration.setGenerator(generator);

            StringWriter writer = new StringWriter();
            JAXB.marshal(configuration, writer);

            getLog().debug("Using this configuration:\n" + writer.toString());
            GenerationTool.generate(configuration);
        }
        catch (Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }

        // [#2886] Restore old class loader
        finally {
            Thread.currentThread().setContextClassLoader(oldCL);
        }

        project.addCompileSourceRoot(generator.getTarget().getDirectory());
    }

    @SuppressWarnings("unchecked")
    private ClassLoader getClassLoader() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getRuntimeClasspathElements();
            URL urls[] = new URL[classpathElements.size()];

            for (int i = 0; i < urls.length; i++) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }

            return new URLClassLoader(urls, getClass().getClassLoader());
        }
        catch (Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }
}
