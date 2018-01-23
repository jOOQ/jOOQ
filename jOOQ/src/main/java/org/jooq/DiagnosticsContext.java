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

import java.sql.ResultSet;

/**
 * A parameter object that is passed to {@link DiagnosticsListener} methods.
 *
 * @author Lukas Eder
 */
public interface DiagnosticsContext {

    /**
     * The {@link ResultSet} available in this context, or <code>null</code>, if
     * there was no result set.
     */
    ResultSet resultSet();

    /**
     * The number of rows that were fetched from {@link #resultSet()}, or
     * <code>-1</code> if there was no result set.
     */
    int resultSetFetchedRows();

    /**
     * The number of rows that were actually available from
     * {@link #resultSet()}, or <code>-1</code> if there was no result set.
     * <p>
     * Calling this method will try to scroll to the end of the
     * {@link #resultSet()}, in order to count the number of rows, which incurs
     * overhead!
     * <p>
     * If the result set is still being consumed (i.e. prior to the
     * {@link ResultSet#close()} call), and scrolling back to the current row
     * after scrolling to the end of {@link #resultSet()} is not possible (e.g.
     * because the driver supports only {@link ResultSet#TYPE_FORWARD_ONLY}),
     * then this will return the same value as {@link #resultSetFetchedRows()}.
     */
    int resultSetActualRows();
}
