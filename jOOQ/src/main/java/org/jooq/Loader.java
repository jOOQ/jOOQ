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
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * This type is the final type holding information about the outcome of the data
 * load.
 *
 * @author Lukas Eder
 */
public interface Loader<R extends Record> {

    /**
     * A list of errors that might have happened during the load.
     */
    List<LoaderError> errors();

    /**
     * The number of processed rows.
     */
    int processed();

    /**
     * The number of executed statements, bulk statements, or batch statements.
     */
    int executed();

    /**
     * The number of ignored rows.
     */
    int ignored();

    /**
     * The number of inserted or updated rows.
     */
    int stored();

    /**
     * The results that are also returned from {@link Loader}.
     */
    LoaderContext result();

}
