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

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstraction over directed, acyclic graph models.
 * <p>
 * Examples of such models are {@link Version} or {@link Commit} /
 * {@link Commits}.
 *
 * @author Lukas Eder
 */
@Experimental
public interface Node<N extends Node<N>> extends Scope {

    /**
     * The name of the {@link #root()} node.
     */
    String ROOT = "root";

    /**
     * The ID of the node, unique within the graph.
     */
    @NotNull
    String id();

    /**
     * The message associated with the node.
     */
    @Nullable
    String message();

    /**
     * The root node of the graph.
     */
    @NotNull
    N root();

    /**
     * The parents of this node.
     */
    @NotNull
    List<N> parents();
}
