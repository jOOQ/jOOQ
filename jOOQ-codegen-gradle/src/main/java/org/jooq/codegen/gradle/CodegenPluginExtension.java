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
