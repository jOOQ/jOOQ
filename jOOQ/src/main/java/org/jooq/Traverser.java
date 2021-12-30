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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * An API for {@link QueryPart#$traverse(Traverser)} query part traversals.
 * <p>
 * Similar to a {@link Collector} for {@link Stream#collect(Collector)}, this
 * type wraps:
 * <p>
 * <ul>
 * <li>{@link #supplier()}</li>
 * <li>{@link #abort()}</li>
 * <li>{@link #recurse()}</li>
 * <li>{@link #before()}</li>
 * <li>{@link #after()}</li>
 * <li>{@link #finisher()}</li>
 * </ul>
 */
public interface Traverser<A, R> {

    /**
     * Convenience method to create a {@link Traverser} with a
     * {@link #supplier()} and {@link #before()}.
     */
    static <R> Traverser<R, R> of(
        Supplier<R> supplier,
        BiFunction<? super R, ? super QueryPart, ? extends R> before
    ) {
       return of(supplier, a -> false, q -> true, before, (a, q) -> a, a -> a);
    }

    /**
     * Convenience method to create a {@link Traverser} with a
     * {@link #supplier()}, {@link #before()}, and {@link #after()}.
     */
    static <R> Traverser<R, R> of(
        Supplier<R> supplier,
        BiFunction<? super R, ? super QueryPart, ? extends R> before,
        BiFunction<? super R, ? super QueryPart, ? extends R> after
    ) {
       return of(supplier, a -> false, q -> true, before, after, a -> a);
    }

    /**
     * Convenience method to create a {@link Traverser} with a
     * {@link #supplier()}, {@link #abort()}, {@link #recurse()}, and
     * {@link #before()}.
     */
    static <R> Traverser<R, R> of(
        Supplier<R> supplier,
        Predicate<? super R> abort,
        Predicate<? super QueryPart> recurse,
        BiFunction<? super R, ? super QueryPart, ? extends R> before
    ) {
       return of(supplier, abort, recurse, before, (a, q) -> a, a -> a);
    }

    /**
     * Convenience method to create a {@link Traverser} with a
     * {@link #supplier()}, {@link #abort()}, {@link #recurse()},
     * {@link #before()}, and {@link #after()}
     */
    static <R> Traverser<R, R> of(
        Supplier<R> supplier,
        Predicate<? super R> abort,
        Predicate<? super QueryPart> recurse,
        BiFunction<? super R, ? super QueryPart, ? extends R> before,
        BiFunction<? super R, ? super QueryPart, ? extends R> after
    ) {
       return of(supplier, abort, recurse, before, after, a -> a);
    }

    /**
     * Convenience method to create a {@link Traverser} with a
     * {@link #supplier()}, {@link #abort()}, {@link #recurse()},
     * {@link #before()}, {@link #after()}, and {@link #finisher()}.
     */
    static <A, R> Traverser<A, R> of(
        Supplier<A> supplier,
        Predicate<? super A> abort,
        Predicate<? super QueryPart> recurse,
        BiFunction<? super A, ? super QueryPart, ? extends A> before,
        BiFunction<? super A, ? super QueryPart, ? extends A> after,
        Function<? super A, ? extends R> finisher
    ) {

        // TODO: Perhaps we should accept only invariant functions?
        return new Traverser<A, R>() {
            @Override
            public Supplier<A> supplier() {
                return supplier;
            }

            @Override
            public Predicate<A> abort() {
                return (Predicate<A>) abort;
            }

            @Override
            public Predicate<QueryPart> recurse() {
                return (Predicate<QueryPart>) recurse;
            }

            @Override
            public BiFunction<A, QueryPart, A> before() {
                return (BiFunction<A, QueryPart, A>) before;
            }

            @Override
            public BiFunction<A, QueryPart, A> after() {
                return (BiFunction<A, QueryPart, A>) after;
            }

            @Override
            public Function<A, R> finisher() {
                return (Function<A, R>) finisher;
            }
        };
    }

    /**
     * A supplier for a temporary data structure to accumulate {@link QueryPart}
     * objects into during traversal.
     */
    Supplier<A> supplier();

    /**
     * An optional traversal abort condition to short circuit traversal e.g.
     * when the searched object has been found.
     */
    Predicate<A> abort();

    /**
     * An optional traversal abort condition to short circuit traversal e.g.
     * when the searched object has been found.
     */
    Predicate<QueryPart> recurse();

    /**
     * A callback that is invoked before recursing into a subtree.
     */
    BiFunction<A, QueryPart, A> before();

    /**
     * A callback that is invoked after recursing into a subtree.
     */
    BiFunction<A, QueryPart, A> after();

    /**
     * An optional transformation function to turn the temporary data structure
     * supplied by {@link #supplier()} into the final data structure.
     */
    Function<A, R> finisher();
}
