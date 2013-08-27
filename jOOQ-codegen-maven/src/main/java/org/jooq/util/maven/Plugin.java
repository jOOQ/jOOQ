/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 *
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util.maven;

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
 */
public class Plugin extends AbstractMojo {

    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
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
