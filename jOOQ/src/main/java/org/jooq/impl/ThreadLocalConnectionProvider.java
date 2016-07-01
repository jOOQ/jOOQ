/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import java.sql.Connection;

import org.jooq.ConnectionProvider;

/**
 * A connection provider that interoperates with the
 * {@link ThreadLocalTransactionProvider}.
 * <p>
 * This implementation is used implicitly and automatically by
 * {@link DefaultConfiguration}. Users should pass their own
 * {@link ConnectionProvider} to the {@link ThreadLocalTransactionProvider}.
 *
 * @author Lukas Eder
 */
final class ThreadLocalConnectionProvider implements ConnectionProvider {

    final ConnectionProvider      delegate;
    final ThreadLocal<Connection> tl;

    public ThreadLocalConnectionProvider(ConnectionProvider delegate) {
        this.delegate = delegate;
        this.tl = new ThreadLocal<Connection>();
    }

    @Override
    public final Connection acquire() {
        Connection result = tl.get();
        return result != null ? result : delegate.acquire();
    }

    @Override
    public final void release(Connection connection) {
        Connection previous = tl.get();

        if (previous == null)
            delegate.release(connection);
        else if (previous != connection)
            throw new IllegalStateException(
                "A different connection was released than the thread-bound one that was expected");
    }
}
