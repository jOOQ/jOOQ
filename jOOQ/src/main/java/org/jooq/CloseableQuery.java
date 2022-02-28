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

import java.sql.PreparedStatement;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Query} that holds a reference to the underlying
 * {@link PreparedStatement} without closing it, for reuse.
 * <p>
 * It was created via {@link Query#keepStatement(boolean)} and must be treated
 * as a resource, e.g. in a <code>try-with-resources</code> statement.
 *
 * @author Lukas Eder
 */
public interface CloseableQuery extends Query, AutoCloseable {

    @Override
    @NotNull
    CloseableQuery bind(String param, Object value) throws IllegalArgumentException, DataTypeException;

    @Override
    @NotNull
    CloseableQuery bind(int index, Object value) throws IllegalArgumentException, DataTypeException;

    // ------------------------------------------------------------------------
    // JDBC methods
    // ------------------------------------------------------------------------

    @Override
    @NotNull
    CloseableQuery poolable(boolean poolable);

    @Override
    @NotNull
    CloseableQuery queryTimeout(int seconds);

    @Override
    @NotNull
    CloseableQuery keepStatement(boolean keepStatement);

    /**
     * Close the underlying statement.
     * <p>
     * This closes the query's underlying {@link Statement} or
     * {@link PreparedStatement} if a previous call to
     * {@link #keepStatement(boolean)} indicated that jOOQ should keep
     * statements open after query execution. If there is no underlying open
     * statement, this call is simply ignored.
     *
     * @throws DataAccessException If something went wrong closing the statement
     * @see java.sql.Statement#close()
     */
    @Override
    void close() throws DataAccessException;

}
