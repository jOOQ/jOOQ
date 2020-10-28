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

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * An abstraction over directed, acyclic graph models.
 * <p>
 * Examples of such models are {@link Version} / {@link Versions} or
 * {@link Commit} / {@link Commits}.
 *
 * @author Lukas Eder
 */
public interface Node<N extends Node<N>> {

    /**
     * The ID of the node, unique within the graph.
     */
    @NotNull
    String id();

    /**
     * The message associated with the node.
     */
    @NotNull
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
