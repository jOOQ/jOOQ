/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A context object that provides information about a loader's current state to
 * {@link LoaderRowListener}.
 *
 * @author Lukas Eder
 */
public interface LoaderContext {

    /**
     * The loader's {@link Field} list.
     */
    Field<?> @NotNull [] fields();

    /**
     * Override the row that will be processed. Changing it has now effect on
     * the {@link LoaderListenerStep#onRowEnd(LoaderRowListener)} event.
     */
    @NotNull @CheckReturnValue
    LoaderContext row(Object[] row);

    /**
     * The row that will be or has been processed.
     */
    @Nullable @CheckReturnValue
    Object @NotNull [] row();

    /**
     * A list of errors that might have happened during the load.
     */
    @NotNull @CheckReturnValue
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
}
