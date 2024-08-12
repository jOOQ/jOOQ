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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.codegen.gradle;

import org.gradle.api.Action;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.jooq.tools.JooqLogger;
import org.jooq.util.jaxb.tools.MiniJAXB;
import org.jooq.codegen.gradle.MetaExtensions.*;

import javax.inject.Inject;

import java.io.File;

/**
 * A wrapper for a name, configuration pair.
 */
public class NamedConfiguration {

    static final JooqLogger        log = JooqLogger.getLogger(NamedConfiguration.class);

    final ObjectFactory            objects;
    final ProjectLayout            projectLayout;
    final String                   name;
    final NamedConfiguration       defaultConfiguration;
    Action<ConfigurationExtension> action;
    boolean                        unnamed;
    private Configuration          configuration;
    private DirectoryProperty      outputDirectory;
    private boolean                outputDirectorySet;

    @Inject
    public NamedConfiguration(
        ObjectFactory objects,
        ProjectLayout projectLayout,
        String name,
        NamedConfiguration defaultConfiguration
    ) {
        this.objects = objects;
        this.projectLayout = projectLayout;
        this.name = name;
        this.unnamed = false;
        this.configuration = init(new Configuration());
        this.outputDirectory = objects.directoryProperty();
        this.defaultConfiguration = defaultConfiguration == null ? this : defaultConfiguration;
    }

    static final Configuration init(Configuration configuration) {
        return configuration
            .withGenerator(new Generator()
                .withTarget(new Target()));
    }

    public String getName() {
        return name;
    }

    DirectoryProperty getOutputDirectory() {
        executeAction();
        return outputDirectorySet ? outputDirectory : null;
    }

    public Configuration getConfiguration() {
        executeAction();
        return configuration;
    }

    void configuration0(Configuration configuration) {
        if (!unnamed) {
            defaultConfiguration.executeAction();
            this.configuration = MiniJAXB.append(copy(defaultConfiguration.configuration), copy(this.configuration));
        }

        this.configuration = MiniJAXB.append(copy(configuration), copy(this.configuration));
    }

    static Configuration copy(Configuration configuration) {
        return MiniJAXB.unmarshal(MiniJAXB.marshal(configuration), Configuration.class);
    }

    public void configuration(Action<ConfigurationExtension> action) {
        this.action = action;
        executeAction();
    }

    public void delayedConfiguration(Action<ConfigurationExtension> action) {
        log.info("Delayed configuration is an experimental feature, which is subject to change in the future. Please illustrate your use-case here to help better understand why this is needed: https://github.com/jOOQ/jOOQ/issues/16821");
        this.action = action;
    }

    void executeAction() {
        if (action != null) {
            ConfigurationExtension c = objects.newInstance(ConfigurationExtension.class, objects);
            init(c);
            action.execute(c);
            action = null;
            configuration0(c);

            // [#15944] Override default target directory
            Target target = configuration.getGenerator().getTarget();

            if (defaultTarget())
                target.setDirectory("build/generated-sources/jooq");

            String directory = target.getDirectory();

            // [#16133] Make sure the CodegenTask's OutputDirectory takes into account any basedir config
            if (configuration.getBasedir() == null)
                configuration.setBasedir(projectLayout.getProjectDirectory().getAsFile().getAbsolutePath());
            else if (!new File(directory).isAbsolute())
                directory = new File(configuration.getBasedir(), directory).getAbsolutePath();

            outputDirectory.value(projectLayout.getProjectDirectory().dir(directory));
            outputDirectorySet = true;
        }
    }

    private boolean defaultTarget() {

        // [#15944] Override default target directory
        Target target = configuration.getGenerator().getTarget();
        return target.getDirectory() == null || GenerationTool.DEFAULT_TARGET_DIRECTORY.equals(target.getDirectory());
    }

    @Override
    public String toString() {
        return "NamedConfiguration [" + name + ", " + configuration + "]";
    }
}
