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

import java.io.IOException;

import org.jooq.exception.DataAccessException;

/**
 * An error that occurred during loading. Errors are only handled when they were
 * caused by {@link DataAccessException}'s. {@link IOException}'s and other
 * problems will abort loading fatally.
 *
 * @author Lukas Eder
 */
public interface LoaderError {

    /**
     * The underlying {@link DataAccessException} that caused the error.
     */
    DataAccessException exception();

    /**
     * The processed row index starting with <code>0</code> that caused the
     * error.
     * <p>
     * If queries were executed in batch mode, this will be the row index of the
     * last row added to the batch.
     */
    int rowIndex();

    /**
     * The row data that caused the error.
     * <p>
     * If queries were executed in batch mode, this will be the last row added
     * to the batch.
     */
    String[] row();

    /**
     * The query whose execution failed.
     */
    Query query();
}
