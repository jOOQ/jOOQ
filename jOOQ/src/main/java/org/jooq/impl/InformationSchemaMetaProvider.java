/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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
package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.Source;
import org.jooq.util.jaxb.tools.MiniJAXB;
import org.jooq.util.xml.jaxb.InformationSchema;

/**
 * A {@link MetaProvider} that provides its meta data based on JAXB-annotated
 * {@link InformationSchema} meta information.
 *
 * @author Lukas Eder
 */
public class InformationSchemaMetaProvider implements MetaProvider {

    private final Configuration     configuration;
    private final InformationSchema schema;
    private final Source[]          sources;

    public InformationSchemaMetaProvider(Configuration configuration, Source... sources) {
        this.configuration = configuration;
        this.schema = null;
        this.sources = sources;
    }

    public InformationSchemaMetaProvider(Configuration configuration, InformationSchema schema) {
        this.configuration = configuration;
        this.schema = schema;
        this.sources = null;
    }

    @Override
    public Meta provide() {
        InformationSchema s = schema;

        if (s == null) {
            s = new InformationSchema();

            for (Source source : sources)
                MiniJAXB.append(s, MiniJAXB.unmarshal(source.reader(), InformationSchema.class));
        }

        return new InformationSchemaMetaImpl(configuration, s);
    }
}
