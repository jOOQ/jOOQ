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
package org.jooq.util.maven;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXB;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * @goal generate
 * @phase generate-sources
 * @version $Id$
 * @author Sander Plas
 * @author Lukas Eder
 */
public class Plugin extends AbstractMojo {

    /**
     * The Maven project.
     *
     * @parameter property="project"
     * @required
     * @readonly
     */
    private MavenProject                 project;

    /**
     * The jdbc settings.
     *
     * @parameter
     */
    private org.jooq.util.jaxb.Jdbc      jdbc;

    /**
     * The generator settings
     *
     * @parameter
     */
    private org.jooq.util.jaxb.Generator generator;

    @Override
    public void execute() throws MojoExecutionException {
        try {

            // [#2887] Patch relative paths to take plugin execution basedir into accountcd joo
            String dir = generator.getTarget().getDirectory();
            if (!new File(dir).isAbsolute()) {
                generator.getTarget().setDirectory(project.getBasedir() + File.separator + dir);
            }

            Configuration configuration = new Configuration();
            configuration.setJdbc(jdbc);
            configuration.setGenerator(generator);

            StringWriter writer = new StringWriter();
            JAXB.marshal(configuration, writer);

            getLog().info("Using this configuration:\n" + writer.toString());
            GenerationTool.main(configuration);
        }
        catch (Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }
        project.addCompileSourceRoot(generator.getTarget().getDirectory());
    }
}
