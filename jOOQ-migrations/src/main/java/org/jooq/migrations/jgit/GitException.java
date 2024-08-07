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
package org.jooq.migrations.jgit;

import org.eclipse.jgit.api.Git;

/**
 * A {@link RuntimeException} wrapping any exceptions originating from
 * {@link Git}.
 */
public class GitException extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID   = 491834858363345767L;

    /**
     * Constructor for GitException.
     *
     * @param message the detail message
     */
    public GitException(String message) {
        super(message);
    }

    /**
     * Constructor for GitException.
     *
     * @param message the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public GitException(String message, Throwable cause) {
        super(message, cause);
    }
}
