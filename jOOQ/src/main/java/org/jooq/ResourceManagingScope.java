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

import java.io.Closeable;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLXML;

/**
 * A scope that can manage resources.
 * <p>
 * This is a type of {@link Scope} that can manage resources on behalf of the
 * call site, and free / close those resources once the scope ends.
 * <p>
 * For example, {@link Binding} implementations may wish to create {@link Clob}
 * or {@link InputStream} or other kinds of resources in order to bind them to
 * JDBC. Instead of remembering to close them manually through some delicate
 * logic involving e.g. clever usage of {@link ThreadLocal}, implementations can
 * register their resources with the methods exposed here, and jOOQ will take
 * care of freeing / closing them at the right moment.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * class StreamingLobBinding implements Binding<String, File> {
 *     ...
 *     public void set(BindingSetStatementContext&lt;File> ctx) {
 *         ctx.statement()
 *            .setBinaryStream(ctx.index(), ctx.closeAfterExecution(new FileInputStream(ctx.value())));
 *     }
 * }
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface ResourceManagingScope extends Scope {

    /**
     * Register a {@link Array} for auto freeing after this scope ends.
     *
     * @return The argument array, for convenience.
     */
    Array autoFree(Array array);

    /**
     * Register a {@link Blob} for auto freeing after this scope ends.
     *
     * @return The argument blob, for convenience.
     */
    Blob autoFree(Blob blob);

    /**
     * Register a {@link Clob} for auto freeing after this scope ends.
     *
     * @return The argument clob, for convenience.
     */
    Clob autoFree(Clob clob);

    /**
     * Register a {@link SQLXML} for auto freeing after this scope ends.
     *
     * @return The argument xml, for convenience.
     */
    SQLXML autoFree(SQLXML xml);

    /**
     * Register a {@link Closeable} for auto closing after this scope ends.
     *
     * @return The argument closeable, for convenience.
     */
    <R extends Closeable> R autoClose(R closeable);


    /**
     * Register an {@link AutoCloseable} for auto closing after this scope ends.
     *
     * @return The argument closeable, for convenience.
     */
    <R extends AutoCloseable> R autoClose(R closeable);


}
