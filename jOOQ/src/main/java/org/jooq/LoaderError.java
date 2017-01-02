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
