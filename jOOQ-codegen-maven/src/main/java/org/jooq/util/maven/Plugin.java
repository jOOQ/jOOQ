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
package org.jooq.util.maven;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jooq.util.GenerationTool;

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
    private MavenProject project;

    /**
     * The jdbc settings.
     *
     * @parameter
     */
    private Jdbc         jdbc;

    /**
     * The generator settings
     *
     * @parameter
     */
    private Generator    generator;

    @Override
    public void execute() throws MojoExecutionException {

        Properties props = new Properties();

        props.put("jdbc.Driver", jdbc.getDriver());
        props.put("jdbc.URL", jdbc.getUrl());
        props.put("jdbc.Schema", jdbc.getSchema());
        props.put("jdbc.User", jdbc.getUser());
        props.put("jdbc.Password", jdbc.getPassword());
        props.put("generator", generator.getName());
        props.put("generator.database", generator.getDatabase().getName());
        props.put("generator.database.includes", generator.getDatabase().getIncludes());
        props.put("generator.database.excludes", generator.getDatabase().getExcludes());
        props.put("generator.generate.relations", generator.getGenerate().getRelations());
        props.put("generator.generate.deprecated", generator.getGenerate().getDeprecated());
        props.put("generator.target.package", generator.getTarget().getPackageName());
        props.put("generator.target.directory", generator.getTarget().getDirectory());

        if (generator.getMasterDataTables() != null) {
            StringBuilder mdtList = new StringBuilder();
            for (MasterDataTable mdt : generator.getMasterDataTables()) {
                if (mdtList.length() > 0) {
                    mdtList.append(",");
                }
                mdtList.append(mdt.getName());

                props.put("generator.generate.master-data-table-literal." + mdt.getName(), mdt.getLiteral());
                props.put("generator.generate.master-data-table-description." + mdt.getName(), mdt.getDescription());

            }
            props.put("generator.generate.master-data-tables", mdtList.toString());
        }

        if (getLog().isDebugEnabled()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                props.store(stream, "passing these properties to jooq-codegen:");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            getLog().debug(stream.toString());
        }

        try {
            GenerationTool.main(props);
        }
        catch (Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }
        project.addCompileSourceRoot(generator.getTarget().getDirectory());
    }
}
