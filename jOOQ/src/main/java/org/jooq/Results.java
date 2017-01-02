/*
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
package org.jooq;

import java.util.List;

/**
 * A list of {@link Result} and update counts that can be returned by
 * {@link ResultQuery#fetchMany()} calls and other calls that produce multiple
 * cursors and update counts.
 * <p>
 * For backwards-compatibility (e.g. with {@link ResultQuery#fetchMany()}), this
 * type extends {@link List} containing only the {@link Result}, not the rows /
 * update counts of interleaved updates. In order to get both, call
 * {@link #resultsOrRows()}.
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
