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

import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.jooq.util.jaxb.tools.MiniJAXB;

import javax.inject.Inject;

import groovy.lang.*;
import org.codehaus.groovy.runtime.*;

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

    public void configuration(Closure<?> closure) {
        MetaExtensions.ConfigurationExtension c = new MetaExtensions.ConfigurationExtension();
        closure = (Closure<?>) closure.clone();
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.setDelegate(c);
        closure.call(c);
        configuration(c);
    }

    @Override
    public String toString() {
        return "NamedConfiguration [" + name + ", " + configuration + "]";
    }
}
