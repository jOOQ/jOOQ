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

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;

import org.jooq.conf.Settings;
import org.jooq.impl.Internal;
import org.jooq.impl.QOM.NotYetImplementedException;

import org.jetbrains.annotations.NotNull;

/**
 * The common base type for all objects that can be used for query composition.
 *
 * @author Lukas Eder
 */
public interface QueryPart extends Serializable {

    /**
     * Render a SQL string representation of this <code>QueryPart</code>.
     * <p>
     * For improved debugging, this renders a SQL string of this
     * <code>QueryPart</code> with inlined bind variables. If this
     * <code>QueryPart</code> is {@link Attachable}, then the attached
     * {@link Configuration} may be used for rendering the SQL string, including
     * {@link SQLDialect} and {@link Settings}. Do note that most
     * <code>QueryPart</code> instances are not attached to a
     * {@link Configuration}, and thus there is no guarantee that the SQL string
     * will make sense in the context of a specific database.
     *
     * @return A SQL string representation of this <code>QueryPart</code>
     */
    @Override
    String toString();

    /**
     * Check whether this <code>QueryPart</code> can be considered equal to
     * another <code>QueryPart</code>.
     * <p>
     * In general, <code>QueryPart</code> equality is defined in terms of
     * {@link #toString()} equality. In other words, two query parts are
     * considered equal if their rendered SQL (with inlined bind variables) is
     * equal. This means that the two query parts do not necessarily have to be
     * of the same type.
     * <p>
     * Some <code>QueryPart</code> implementations may choose to override this
     * behaviour for improved performance, as {@link #toString()} is an
     * expensive operation, if called many times.
     *
     * @param object The other <code>QueryPart</code>
     * @return Whether the two query parts are equal
     */
    @Override
    boolean equals(Object object);

    /**
     * Generate a hash code from this <code>QueryPart</code>.
     * <p>
     * In general, <code>QueryPart</code> hash codes are the same as the hash
     * codes generated from {@link #toString()}. This guarantees consistent
     * behaviour with {@link #equals(Object)}
     * <p>
     * Some <code>QueryPart</code> implementations may choose to override this
     * behaviour for improved performance, as {@link #toString()} is an
     * expensive operation, if called many times.
     *
     * @return The <code>QueryPart</code> hash code
     */
    @Override
    int hashCode();

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    /**
     * Traverser this {@link QueryPart} expression tree using a composable
     * {@link Traverser}, producing a result.
     * <p>
     * This offers a generic way to traverse expression trees to translate the
     * tree to arbitrary other data structures. The simplest traversal would
     * just count all the tree elements:
     * <p>
     * <code><pre>
     * int count = CUSTOMER.NAME.eq(1).$traverse(0, (i, p) -> i + 1);
     * </pre></code>
     * <p>
     * The same can be achieved by translating the JDK {@link Collector} API to
     * the {@link Traverser} API using {@link Traversers#collecting(Collector)}.
     * <p>
     * <code><pre>
     * CUSTOMER.NAME.eq(1).$traverse(Traversers.collecting(Collectors.counting()));
     * </pre></code>
     * <p>
     * Unlike a {@link Collector}, a {@link Traverser} is optimised for tree
     * traversal, not stream traversal:
     * <ul>
     * <li>Is not designed for parallelism</li>
     * <li>It can {@link Traverser#abort()} traversal early when the result can
     * be produced early (e.g. when running
     * {@link Traversers#containing(QueryPart)}, and a result has been
     * found).</li>
     * <li>It can decide whether to {@link Traverser#recurse()} into a
     * {@link QueryPart} subtree, or whether that is not necessary or even
     * undesirable, e.g. to prevent entering new subquery scopes.</li>
     * <li>Unlike a Collector, which can use its {@link Collector#accumulator()}
     * to accumulate each element only once, in tree traversal, it's desirable
     * to be able to distinguish between accumulating an item
     * {@link Traverser#before()} or {@link Traverser#after()} recursing into
     * it. This is useful e.g. to wrap each tree node in XML opening and closing
     * tags.</li>
     * </ul>
     * <p>
     * This is a commercial jOOQ edition only feature.
     */
    default <R> R $traverse(Traverser<?, R> traverser) {
        Internal.requireCommercial(() -> "Query object model traversal is available in the commercial jOOQ distribution only. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.");
        throw new NotYetImplementedException();
    }

    /**
     * Convenience method for {@link #$traverse(Traverser)}.
     */
    default <R> R $traverse(
        R init,
        BiFunction<? super R, ? super QueryPart, ? extends R> before,
        BiFunction<? super R, ? super QueryPart, ? extends R> after
    ) {
        return $traverse(Traverser.of(() -> init, before, after));
    }

    /**
     * Convenience method for {@link #$traverse(Traverser)}.
     */
    default <R> R $traverse(
        R init,
        BiFunction<? super R, ? super QueryPart, ? extends R> before
    ) {
        return $traverse(Traverser.of(() -> init, before));
    }

    /**
     * Convenience method for {@link #$traverse(Traverser)}.
     */
    default <R> R $traverse(
        R init,
        Predicate<? super R> abort,
        Predicate<? super QueryPart> recurse,
        BiFunction<? super R, ? super QueryPart, ? extends R> before,
        BiFunction<? super R, ? super QueryPart, ? extends R> after
    ) {
        return $traverse(Traverser.of(() -> init, abort, recurse, before, after));
    }

    /**
     * Convenience method for {@link #$traverse(Traverser)}.
     */
    default <R> R $traverse(
        R init,
        Predicate<? super R> abort,
        Predicate<? super QueryPart> recurse,
        BiFunction<? super R, ? super QueryPart, ? extends R> before
    ) {
        return $traverse(Traverser.of(() -> init, abort, recurse, before));
    }

    /**
     * Traverse a {@link QueryPart} hierarchy and recursively replace its
     * elements by alternatives.
     * <p>
     * This is a commercial jOOQ edition only feature.
     *
     * @param recurse A predicate to decide whether to recurse into a
     *            {@link QueryPart} subtree.
     * @param replacement The replacement function. Replacement continues
     *            recursively until the function returns null or its input for
     *            any given input.
     */
    @NotNull
    default QueryPart $replace(
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        Internal.requireCommercial(() -> "Query object model traversal is available in the commercial jOOQ distribution only. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.");
        throw new NotYetImplementedException();
    }

    /**
     * Convenience method for {@link #$replace(Predicate, Function1)}.
     */
    @NotNull
    default QueryPart $replace(Function1<? super QueryPart, ? extends QueryPart> replacement) {
        return $replace(p -> true, replacement);
    }
}
