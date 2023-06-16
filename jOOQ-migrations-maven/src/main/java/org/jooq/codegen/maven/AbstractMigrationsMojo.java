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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.codegen.maven;

import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.jooq.CloseableDSLContext;
import org.jooq.Configuration;
import org.jooq.conf.MigrationSchema;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * A base class for jOOQ Migrations mojos.
 *
 * @author Lukas Eder
 */
abstract class AbstractMigrationsMojo extends AbstractMojo {

    /**
     * The Maven project.
     */
    @Parameter(
        property = "project",
        required = true,
        readonly = true
    )
    MavenProject project;

    /**
     * Whether to skip the execution of the Maven Plugin for this module.
     */
    @Parameter(property = "jooq.migrate.skip")
    boolean      skip;

    /**
     * The JDBC URL to connect to.
     */
    @Parameter(property = "jooq.migrate.jdbc")
    Jdbc        jdbc;

    /**
     * The migration script directory.
     */
    @Parameter(property = "jooq.migrate.directory")
    String       directory;

    /**
     * The script to run before the migration.
     */
    @Parameter(property = "jooq.migrate.setupScript")
    String       setupScript;

    /**
     * The script to run after the migration.
     */
    @Parameter(property = "jooq.migrate.cleanupScript")
    String       cleanupScript;

    /**
     * The schemata that are migrated.
     */
    @Parameter(property = "jooq.migrate.schemata")
    List<MigrationSchema> schemata;

    /**
     * Whether to create a migration schema if it does not exist.
     */
    @Parameter(property = "jooq.migrate.schemataCreateSchemaIfNotExists")
    boolean               schemataCreateSchemaIfNotExists;

    /**
     * The default catalog among the migrated schemata.
     */
    @Parameter(property = "jooq.migrate.defaultCatalog")
    String                defaultCatalog;

    /**
     * The default schema among the migrated schemata.
     */
    @Parameter(property = "jooq.migrate.defaultSchema")
    String                defaultSchema;

    /**
     * The catalog where the history tables are located.
     */
    @Parameter(property = "jooq.migrate.historyCatalog")
    String                historyCatalog;

    /**
     * The schema where the history tables are located.
     */
    @Parameter(property = "jooq.migrate.historySchema")
    String                historySchema;

    /**
     * Whether to create the history schema if it does not exist.
     */
    @Parameter(property = "jooq.migrate.historySchemaCreateSchemaIfNotExists")
    boolean               historySchemaCreateSchemaIfNotExists;

    @Override
    public final void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping jOOQ migrations");
            return;
        }

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        URLClassLoader pluginClassLoader = getClassLoader();

        try {

            // [#2886] Add the surrounding project's dependencies to the current classloader
            Thread.currentThread().setContextClassLoader(pluginClassLoader);

            if (jdbc == null || jdbc.url == null)
                throw new MojoExecutionException("JDBC URL is required");

            if (jdbc.driver != null)
                Class.forName(jdbc.driver);

            try (CloseableDSLContext ctx = DSL.using(jdbc.url, defaultIfNull(jdbc.user, jdbc.username), jdbc.password)) {

                // Initialise Settings
                // ---------------------------------------------------------------------
                ctx.settings().getMigrationSchemata().addAll(schemata);

                if (defaultCatalog != null || defaultSchema != null)
                    ctx.settings().setMigrationDefaultSchema(new MigrationSchema()
                        .withCatalog(defaultIfNull(defaultCatalog, ""))
                        .withSchema(defaultIfNull(defaultSchema, ""))
                    );

                if (historyCatalog != null || historySchema != null)
                    ctx.settings().setMigrationHistorySchema(new MigrationSchema()
                        .withCatalog(defaultIfNull(historyCatalog, ""))
                        .withSchema(defaultIfNull(historySchema, ""))
                    );


                ctx.settings().setMigrationSchemataCreateSchemaIfNotExists(schemataCreateSchemaIfNotExists);
                ctx.settings().setMigrationHistorySchemaCreateSchemaIfNotExists(historySchemaCreateSchemaIfNotExists);

                // Initialise connection
                // ---------------------------------------------------------------------
                if (!isBlank(defaultCatalog))
                    ctx.setCatalog(defaultCatalog).execute();

                if (!isBlank(defaultSchema))
                    ctx.setSchema(defaultSchema).execute();

                // Run migration
                // ---------------------------------------------------------------------
                if (setupScript != null)
                    ctx.execute(setupScript);

                execute0(ctx.configuration());

                if (cleanupScript != null)
                    ctx.execute(cleanupScript);
            }
        }
        catch (Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }
        finally {

            // [#2886] Restore old class loader
            Thread.currentThread().setContextClassLoader(oldCL);

            // [#7630] Close URLClassLoader to help free resources
            try {
                pluginClassLoader.close();
            }

            // Catch all possible errors to avoid suppressing the original exception
            catch (Throwable e) {
                getLog().error("Couldn't close the classloader.", e);
            }
        }
    }

    abstract void execute0(Configuration configuration) throws Exception;

    private URLClassLoader getClassLoader() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getRuntimeClasspathElements();
            URL urls[] = new URL[classpathElements.size()];

            for (int i = 0; i < urls.length; i++)
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();

            return new URLClassLoader(urls, getClass().getClassLoader());
        }
        catch (Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }
}
