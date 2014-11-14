/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import java.util.HashMap;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.conf.Settings;

/**
 * @author Lukas Eder
 */
abstract class AbstractScope implements Scope {

    private final Configuration       configuration;
    private final Map<Object, Object> data;

    AbstractScope(Configuration configuration) {
        this.data = new HashMap<Object, Object>();

        // The Configuration can be null when unattached objects are
        // executed or when unattached Records are stored...
        if (configuration == null) {
            configuration = new DefaultConfiguration();
        }

        this.configuration = configuration;
    }

    AbstractScope(AbstractScope scope) {
        this.data = scope.data;
        this.configuration = scope.configuration;
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
        return Utils.settings(configuration());
    }

    @Override
    public final SQLDialect dialect() {
        return Utils.configuration(configuration()).dialect();
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
