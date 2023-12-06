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

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.jooq.util.jaxb.tools.MiniJAXB;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

import groovy.lang.*;
import org.codehaus.groovy.runtime.*;

/**
 * The configuration object of the jooq plugin extension.
 */
public class CodegenPluginExtension {

    final             Configuration                                  configuration;
    final             NamedDomainObjectContainer<NamedConfiguration> executions;
    private transient List<NamedConfiguration>                       configurations;

    @Inject
    public CodegenPluginExtension(ObjectFactory objects, ProviderFactory providers, ProjectLayout layout) {
        configuration = NamedConfiguration.newConfiguration();
        executions = objects.domainObjectContainer(NamedConfiguration.class,
            name -> objects.newInstance(NamedConfiguration.class, name)
        );
    }

    public void configuration(Configuration configuration) {
        MiniJAXB.append(this.configuration, configuration);
    }

    public void configuration(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MetaExtensions.ConfigurationExtension.class) Closure<?> closure) {
        MetaExtensions.ConfigurationExtension c = new MetaExtensions.ConfigurationExtension();
        closure = (Closure<?>) closure.clone();
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(c);
        closure.call(c);
        configuration(c);
    }

    public NamedDomainObjectContainer<NamedConfiguration> getExecutions() {
        return executions;
    }

    List<NamedConfiguration> configurations() {
        if (configurations == null) {
            if (executions.isEmpty())
                configurations = Arrays.asList(new NamedConfiguration("main", true, configuration));
            else
                configurations = executions.stream().map(c -> new NamedConfiguration(
                    c.name, false, MiniJAXB.append(
                        MiniJAXB.append(new Configuration(), copy(configuration)),
                        copy(c.configuration)
                    )
                )).collect(toList());
        }

        return configurations;
    }

    Configuration copy(Configuration configuration) {
        return MiniJAXB.unmarshal(MiniJAXB.marshal(configuration), Configuration.class);
    }
}
