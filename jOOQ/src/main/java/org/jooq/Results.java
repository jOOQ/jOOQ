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

import java.sql.SQLException;
import java.util.List;

import org.jooq.conf.Settings;
import org.jooq.conf.ThrowExceptions;

/**
 * A list of {@link Result} and update counts that can be returned by
 * {@link ResultQuery#fetchMany()} calls and other calls that produce multiple
 * cursors and update counts.
 * <p>
 * For backwards-compatibility (e.g. with {@link ResultQuery#fetchMany()}), this
 * type extends {@link List} containing only the {@link Result}, not the rows,
 * update counts, exceptions of interleaved updates. In order to get them all,
 * call {@link #resultsOrRows()} upon the results.
 * <p>
 * <h3>Exceptions</h3>
 * <p>
 * Some databases support raising several errors or exceptions per statement
 * batch (e.g. {@link SQLDialect#SQLSERVER}):
 * <p>
 * <code><pre>
 * INSERT INTO t VALUES (1),(2),(3);
 * RAISERROR('message 1', 16, 2, 3);
 * RAISERROR('message 2', 16, 2, 3);
 * SELECT * FROM t;
 * RAISERROR('message 3', 16, 2, 3);
 * </pre></code>
 * <p>
 * The above batch will produce:
 * <ul>
 * <li>An update count (3)</li>
 * <li>2 exceptions</li>
 * <li>A result set</li>
 * <li>1 exception</li>
 * </ul>
 * <p>
 * By default (or when explicitly specifying {@link ThrowExceptions#THROW_ALL}
 * in {@link Settings#getThrowExceptions()}), this particular batch will produce
 * a single exception corresponding to <code>"message 1"</code>, with additional
 * exceptions for <code>"message 2"</code> and <code>"message 3"</code> attached
 * to {@link SQLException#getNextException()}, recursively.
 * <p>
 * When specifying {@link ThrowExceptions#THROW_FIRST}, only
 * <code>"message 1"</code> is propagated. When specifying
 * {@link ThrowExceptions#THROW_NONE}, then all exceptions are collected as
 * results and are made available through {@link #resultsOrRows()} in
 * {@link ResultOrRows#exception()}.
 *
 * @author Lukas Eder
 */
public interface Results extends List<Result<Record>>, Attachable {

    // ------------------------------------------------------------------------
    // XXX: Additional, Results-specific methods
    // ------------------------------------------------------------------------

    /**
     * All the results or update counts in their order as fetched via JDBC.
     * <p>
     * While {@link #iterator()} and all the other methods inherited from the
     * {@link List} API return the {@link Result} objects only, this method also
     * includes update counts that may have occurred between two results.
     * <p>
     * It can be safely assumed that:
     * <code><pre>
     * result.resultsOrRows()
     *       .stream()
     *       .filter(r -> r.result() != null)
     *       .map(r -> r.result())
     *       .collect(Collectors.toList())
     *       .equals(result);
     * </pre></code>
     */
    List<ResultOrRows> resultsOrRows();

    // ------------------------------------------------------------------------
    // XXX: Specialisations of Attachable methods
    // ------------------------------------------------------------------------

    /**
     * Attach all results and all of their contained records to a new
     * {@link Configuration}.
     *
     * @param configuration A configuration or <code>null</code>, if you wish to
     *            detach this <code>Attachable</code> from its previous
     *            configuration.
     */
    @Override
    void attach(Configuration configuration);

    /**
     * Detach all results and all of their contained records from their current
     * {@link Configuration}.
     * <p>
     * This is the same as calling <code>attach(null)</code>.
     */
    @Override
    void detach();
}
