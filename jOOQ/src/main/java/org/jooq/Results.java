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
