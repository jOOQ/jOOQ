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
package org.jooq.impl;

import java.time.Instant;
import java.util.Map;

import org.jooq.CacheContext;
import org.jooq.Configuration;
import org.jooq.ConverterContext;
import org.jooq.DSLContext;
import org.jooq.GeneratorContext;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.conf.Settings;

/**
 * Unlike {@link AbstractScope}, this implementation lazy initialises its
 * {@link #data()} map in order to avoid allocation in very short lived
 * {@link Scope} implementations, such as e.g. {@link CacheContext},
 * {@link ConverterContext}, {@link GeneratorContext}.
 *
 * @author Lukas Eder
 */
abstract class AbstractLazyScope implements Scope {

    private final Configuration configuration;
    private Map<Object, Object> data;
    private final Instant       creationTime;

    AbstractLazyScope(Configuration configuration) {
        this(configuration, null);
    }

    AbstractLazyScope(Configuration configuration, Map<Object, Object> data) {

        // The Configuration can be null when unattached objects are
        // executed or when unattached Records are stored...
        if (configuration == null)
            configuration = new DefaultConfiguration();

        this.configuration = configuration;
        this.data = data;
        this.creationTime = configuration.clock().instant();
    }

    // ------------------------------------------------------------------------
    // XXX Scope API
    // ------------------------------------------------------------------------

    @Override
    public final Instant creationTime() {
        return creationTime;
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final DSLContext dsl() {
        return configuration.dsl();
    }

    @Override
    public final Settings settings() {
        return configuration.settings();
    }

    @Override
    public final SQLDialect dialect() {
        return configuration.dialect();
    }

    @Override
    public final SQLDialect family() {
        return configuration.family();
    }

    @Override
    public final Map<Object, Object> data() {
        if (data == null)
            data = new DataMap();

        return data;
    }

    @Override
    public final Object data(Object key) {
        return data().get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data().put(key, value);
    }
}
