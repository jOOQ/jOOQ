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
package org.jooq.tools.jdbc;

import java.sql.Connection;

import org.jooq.ConnectionProvider;

/**
 * A mock connection provider.
 * <p>
 * This {@link ConnectionProvider} wraps a delegate
 * <code>ConnectionProvider</code> and wraps all acquired {@link Connection}
 * references in {@link MockConnection}.
 *
 * @author Lukas Eder
 */
public class MockConnectionProvider implements ConnectionProvider {

    private final ConnectionProvider delegate;
    private final MockDataProvider   provider;

    public MockConnectionProvider(ConnectionProvider delegate, MockDataProvider provider) {
        this.delegate = delegate;
        this.provider = provider;
    }

    @Override
    public final Connection acquire() {
        return new MockConnectionWrapper(delegate.acquire());
    }

    @Override
    public final void release(Connection connection) {
        if (connection instanceof MockConnectionWrapper)
            delegate.release(((MockConnectionWrapper) connection).connection);
        else
            throw new IllegalArgumentException("Argument connection must be a MockConnectionWrapper");
    }

    private class MockConnectionWrapper extends MockConnection {
        final Connection connection;

        public MockConnectionWrapper(Connection connection) {
            super(provider);
            this.connection = connection;
        }
    }
}
