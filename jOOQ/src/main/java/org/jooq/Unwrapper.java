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
package org.jooq;

import java.sql.Connection;
import java.sql.Wrapper;

/**
 * An unwrapper SPI that can be used to override the default unwrapping
 * algorithm.
 * <p>
 * In some cases, jOOQ needs to get access to the native JDBC driver APIs in
 * order to call vendor specific methods, such as Oracle's
 * <code>OracleConnection.createARRAY()</code>. jOOQ doesn't expect clients to
 * provide a native JDBC {@link Connection} through the
 * {@link ConnectionProvider} SPI. Implementations may well provide jOOQ with
 * some proxy that implements things like logging, thread-bound
 * transactionality, connection pooling, etc.
 * <p>
 * In order to access the native API, {@link Wrapper#unwrap(Class)} needs to be
 * called, or in some cases, when third party libraries do not properly
 * implement this contract, some specific methods are called reflectively,
 * including:
 * <p>
 * <ul>
 * <li><code>org.springframework.jdbc.datasource.ConnectionProxy#getTargetConnection()</code></li>
 * <li><code>org.apache.commons.dbcp.DelegatingConnection#getDelegate()</code></li>
 * <li><code>org.jboss.jca.adapters.jdbc.WrappedConnection#getUnderlyingConnection()</code></li>
 * <li>...</li>
 * </ul>
 * <p>
 * Not all such third party libraries are "known" to jOOQ, so clients can
 * implement their own unwrapper to support theirs.
 *
 * @author Lukas Eder
 */
public interface Unwrapper {

    /**
     * Unwrap a wrapped type from a JDBC {@link Wrapper}.
     */
    <T> T unwrap(Wrapper wrapper, Class<T> iface);
}
