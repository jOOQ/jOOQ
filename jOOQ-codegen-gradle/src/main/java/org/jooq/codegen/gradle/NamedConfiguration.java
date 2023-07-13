package org.jooq.codegen.gradle;

import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.jooq.util.jaxb.tools.MiniJAXB;

import javax.inject.Inject;

/**
 * A wrapper for a name, configuration pair.
 */
public class NamedConfiguration {

    final String        name;
    final boolean       unnamed;
    final Configuration configuration;

    @Inject
    public NamedConfiguration(String name) {
        this(name, false, newConfiguration());
    }

    NamedConfiguration(String name, boolean unnamed, Configuration configuration) {
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

    public void configuration(Configuration configuration) {
        MiniJAXB.append(this.configuration, configuration);
    }

    @Override
    public String toString() {
        return "NamedConfiguration [" + name + ", " + configuration + "]";
    }
}
