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
package org.jooq;

import java.util.Map;

import org.jooq.conf.Settings;

/**
 * Scope implementations provide access to a variety of objects that are
 * available from a given scope.
 * <p>
 * The scope of the various objects contained in this type (e.g.
 * {@link #configuration()}, {@link #settings()}, etc.) are implementation
 * dependent and will be specified by the concrete subtype of
 * <code>Scope</code>. Examples of such scope types are:
 * <ul>
 * <li>{@link ExecuteContext}: A scope that covers a single execution of a
 * {@link Query}</li>
 * <li>{@link Context}: A scope that covers a single traversal of a
 * {@link QueryPart} expression tree to produce a SQL string and / or a list of
 * bind variables.</li>
 * <li>{@link VisitContext}: A scope that that covers a single traversal of a
 * {@link QueryPart} expression tree (just like {@link Context}), in the
 * presence of at least one {@link VisitListener}.</li>
 * <li>{@link RecordContext}: A scope that covers a single record operation,
 * such as {@link UpdatableRecord#store()}.</li>
 * <li>{@link TransactionContext}: A scope that covers the execution (or
 * nesting) of a single transaction.</li>
 * </ul>
 * <p>
 * One of <code>Scope</code>'s most interesting features for client code
 * implementing any SPI is the {@link #data()} map, which provides access to a
 * {@link Map} where client code can register user-defined values for the entire
 * lifetime of a scope. For instance, in an {@link ExecuteListener}
 * implementation that measures time for fetching data, it is perfectly possible
 * to store timestamps in that map:
 * <p>
 * <code><pre>
 * class FetchTimeMeasuringListener extends DefaultExecuteListener {
 *     &#64;Override
 *     public void fetchStart(ExecuteContext ctx) {
 *
 *         // Put any arbitrary object in this map:
 *         ctx.data("org.jooq.example.fetch-start-time", System.nanoTime());
 *     }
 *
 *     &#64;Override
 *     public void fetchEnd(ExecuteContext ctx) {
 *
 *         // Retrieve that object again in a later step:
 *         Long startTime = (Long) ctx.data("org.jooq.example.fetch-start-time");
 *         System.out.println("Time taken: " + (System.nanoTime() - startTime) / 1000 / 1000.0 + " ms");
 *     }
 * }
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface Scope {

    /**
     * The configuration of the current scope.
     */
    Configuration configuration();

    /**
     * The settings wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().settings()</code>.
     */
    Settings settings();

    /**
     * The {@link SQLDialect} wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().dialect()</code>.
     */
    SQLDialect dialect();

    /**
     * The {@link SQLDialect#family()} wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().dialect().family()</code>.
     */
    SQLDialect family();

    /**
     * Get all custom data from this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}.
     *
     * @return The custom data. This is never <code>null</code>
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>Scope</code>
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}.
     *
     * @param key A key to identify the custom data
     * @param value The custom data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     */
    Object data(Object key, Object value);
}
