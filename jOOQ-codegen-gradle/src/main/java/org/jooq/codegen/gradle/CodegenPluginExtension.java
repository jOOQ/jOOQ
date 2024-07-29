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
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.jooq.codegen.gradle.MetaExtensions.ConfigurationExtension;

import javax.inject.Inject;

/**
 * The configuration object of the jooq plugin extension.
 */
public class CodegenPluginExtension {

    final ObjectFactory                                  objects;
    final Project                                        project;
    final NamedDomainObjectContainer<NamedConfiguration> executions;

    @Inject
    public CodegenPluginExtension(
        ObjectFactory objects,
        Project project,
        ProviderFactory providers,
        ProjectLayout layout
    ) {
        this.objects = objects;
        this.project = project;
        this.executions = objects.domainObjectContainer(NamedConfiguration.class,
            name -> objects.newInstance(NamedConfiguration.class, objects, name)
        );
    }

    NamedConfiguration defaultConfiguration() {
        return executions.getByName("");
    }

    public void configuration(Action<ConfigurationExtension> action) {
        defaultConfiguration().configuration(action);
    }

    public void delayedConfiguration(Action<ConfigurationExtension> action) {
        defaultConfiguration().delayedConfiguration(action);
    }

    public NamedDomainObjectContainer<NamedConfiguration> getExecutions() {
        return executions;
    }
}
