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

import java.sql.SQLException;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.jdbc.BatchedConnection;
import org.jooq.tools.jdbc.BatchedPreparedStatement;

/**
 * An operation that can run code and statements, buffering all consecutive and
 * identical SQL strings in a {@link BatchedPreparedStatement}, delaying their
 * execution.
 *
 * @author Lukas Eder
     * @see BatchedConnection BatchedConnection for details.
 */
@FunctionalInterface
public interface BatchedRunnable {

    /**
     * Run statements.
     * <p>
     * Implementations may freely use the argument {@link Configuration} to run
     * statements against the database.
     *
     * @param configuration The configuration configured with a {@link BatchedConnection}.
     * @throws Throwable Any exception, including {@link SQLException}, that
     *             will be propagated as an unchecked
     *             {@link DataAccessException}.
     * @see BatchedConnection BatchedConnection for details.
     */
    void run(Configuration configuration) throws Throwable;
}
