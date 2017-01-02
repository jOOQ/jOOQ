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
 */
package org.jooq.impl;

import java.util.Map;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.conf.Settings;

/**
 * @author Lukas Eder
 */
abstract class AbstractScope implements Scope {

    final Configuration       configuration;
    final Map<Object, Object> data;

    AbstractScope(Configuration configuration) {
        this(configuration, null);
    }

    AbstractScope(Configuration configuration, Map<Object, Object> data) {

        // The Configuration can be null when unattached objects are
        // executed or when unattached Records are stored...
        if (configuration == null) {
            configuration = new DefaultConfiguration();
        }

        if (data == null) {
            data = new DataMap();
        }

        this.configuration = configuration;
        this.data = data;
    }

    // ------------------------------------------------------------------------
    // XXX Scope API
    // ------------------------------------------------------------------------

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final Settings settings() {
        return Tools.settings(configuration());
    }

    @Override
    public final SQLDialect dialect() {
        return Tools.configuration(configuration()).dialect();
    }

    @Override
    public final SQLDialect family() {
        return dialect().family();
    }

    @Override
    public final Map<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }
}
