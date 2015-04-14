/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test.all.listeners;

import static org.jooq.test.all.listeners.Lifecycle.METHOD_COMPARATOR;
import static org.jooq.test.all.listeners.Lifecycle.increment;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.TreeMap;

import org.jooq.ConnectionProvider;

/**
 * A lifecycle listener to check {@link ConnectionProvider} lifecycles in integration tests
 *
 * @author Lukas Eder
 */
public class ConnectionProviderLifecycleListener implements ConnectionProvider {

    public static final Map<Method, Integer> ACQUIRE_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);
    public static final Map<Method, Integer> RELEASE_COUNT = new TreeMap<Method, Integer>(METHOD_COMPARATOR);

    private final ConnectionProvider delegate;

    public ConnectionProviderLifecycleListener(ConnectionProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Connection acquire() {
        increment(ACQUIRE_COUNT);
        return delegate.acquire();
    }

    @Override
    public void release(Connection connection) {
        increment(RELEASE_COUNT);
        delegate.release(connection);
    }
}
