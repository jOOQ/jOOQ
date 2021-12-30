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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import org.jetbrains.annotations.NotNull;

/**
 * A set of {@link Traverser} constructor methods.
 *
 * @author Lukas Eder
 */
public final class Traversers {

    /**
     * A traverser constructed from a {@link Collector}.
     */
    public static <T, R> Traverser<?, R> collecting(Collector<QueryPart, T, R> collector) {
        return Traverser.of(
            collector.supplier(),
            t -> false,
            p -> true,
            (t, p) -> {
                collector.accumulator().accept(t, p);
                return t;
            },
            (t, p) -> t,
            collector.finisher()
        );
    }

    /**
     * A traverser that checks whether a {@link QueryPart} contains another
     * {@link QueryPart}.
     *
     * @param part The part to find within the traversed {@link QueryPart} tree.
     */
    @NotNull
    public static Traverser<?, Boolean> containing(QueryPart part) {
        return Traverser.of(
            () -> false,
            b -> b,
            p -> true,
            (b, p) -> b || p.equals(part)
        );
    }

    /**
     * A traverser that finds any {@link QueryPart} that satisfies a predicate
     * within the traversed {@link QueryPart} tree.
     *
     * @param predicate The predicate to use to check the traversed tree for a
     *            {@link QueryPart} to find.
     */
    @NotNull
    public static Traverser<?, Optional<QueryPart>> findingAny(Predicate<? super QueryPart> predicate) {
        return Traverser.<QueryPart, Optional<QueryPart>>of(
            () -> null,
            p -> p != null,
            p -> true,
            (r, p) -> predicate.test(p) ? p : r,
            (r, p) -> r,
            Optional::ofNullable
        );
    }

    /**
     * A traverser that finds all {@link QueryPart}s that satisfy a predicate
     * within the traversed {@link QueryPart} tree.
     *
     * @param predicate The predicate to use to check the traversed tree for
     *            {@link QueryPart}s to find.
     */
    @NotNull
    public static Traverser<?, List<QueryPart>> findingAll(Predicate<? super QueryPart> predicate) {
        return Traverser.of(
            () -> new ArrayList<>(),
            (l, p) -> {
                if (predicate.test(p))
                    l.add(p);

                return l;
            },
            (l, p) -> l
        );
    }
    private Traversers() {}
}
