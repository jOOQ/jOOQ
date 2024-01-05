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
package org.jooq.codegen.gradle;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.jooq.util.jaxb.tools.MiniJAXB;
import org.jooq.codegen.gradle.MetaExtensions.*;

import javax.inject.Inject;

import groovy.lang.*;
import org.codehaus.groovy.runtime.*;

/**
 * A wrapper for a name, configuration pair.
 */
public class NamedConfiguration {

    final ObjectFactory objects;
    final Project       project;
    final String        name;
    boolean             unnamed;
    Configuration       configuration;

    @Inject
    public NamedConfiguration(ObjectFactory objects, Project project, String name) {
        this(objects, project, name, false, newConfiguration());
    }

    NamedConfiguration(ObjectFactory objects, Project project, String name, boolean unnamed, Configuration configuration) {
        this.objects = objects;
        this.project = project;
        this.name = name;
        this.unnamed = unnamed;
        this.configuration = configuration;
    }

    static final Configuration newConfiguration() {
        return new Configuration()
            .withGenerator(new Generator()
                .withTarget(new Target()));
    }

    public String getName() {
        return name;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    void configuration0(Configuration configuration) {
        if (!unnamed)
            MiniJAXB.append(this.configuration, project.getExtensions().getByType(CodegenPluginExtension.class).configuration);

        MiniJAXB.append(this.configuration, configuration);
    }

    public void configuration(Action<ConfigurationExtension> action) {
        ConfigurationExtension c = objects.newInstance(ConfigurationExtension.class, objects);
        action.execute(c);
        configuration0(c);
    }

    @Override
    public String toString() {
        return "NamedConfiguration [" + name + ", " + configuration + "]";
    }
}
