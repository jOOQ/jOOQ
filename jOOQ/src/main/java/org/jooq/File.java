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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * A source of migration content.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @author Lukas Eder
 */
@Experimental
public interface File {

    /**
     * The path of the file within the repository.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    String path();

    /**
     * The name of the file.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    String name();

    /**
     * The file content.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @Nullable
    String content();

    /**
     * The file type.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    ContentType type();
}
