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
package org.jooq.impl;

import static org.jooq.impl.Tools.EMTPY_SCHEMA;

import java.util.Collection;

import org.jooq.Configuration;
import org.jooq.Meta;
import org.jooq.MetaProvider;
import org.jooq.Schema;

/**
 * A {@link MetaProvider} that provides its meta data based on (possibly
 * generated) schemas.
 *
 * @author Lukas Eder
 */
public class SchemaMetaProvider implements MetaProvider {

    private final Configuration configuration;
    private final Schema[]      schemas;

    public SchemaMetaProvider(Configuration configuration, Schema... schemas) {
        this.configuration = configuration;
        this.schemas = schemas;
    }

    public SchemaMetaProvider(Configuration configuration, Collection<? extends Schema> schemas) {
        this(configuration, schemas.toArray(EMTPY_SCHEMA));
    }

    @Override
    public Meta provide() {
        return CatalogMetaImpl.filterSchemas(configuration, schemas);
    }
}
