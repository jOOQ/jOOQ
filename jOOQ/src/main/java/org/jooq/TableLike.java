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

import org.jetbrains.annotations.NotNull;

/**
 * An object that can behave like a table (a table-like object).
 * <p>
 * Instances of this type cannot be created directly, only of its subtypes.
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface TableLike<R extends Record> extends Fields, QueryPart {

    /**
     * The underlying table representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT * FROM (SELECT * FROM x WHERE x.a = '1') WHERE ... </code>
     */
    @NotNull
    @Support
    Table<R> asTable();

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String)
     */
    @NotNull
    @Support
    Table<R> asTable(String alias);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, String...)
     */
    @NotNull
    @Support
    Table<R> asTable(String alias, String... fieldAliases);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(Name)
     */
    @NotNull
    @Support
    Table<R> asTable(Name alias);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, String...)
     */
    @NotNull
    @Support
    Table<R> asTable(Name alias, Name... fieldAliases);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(Name)
     */
    @NotNull
    @Support
    Table<R> asTable(Table<?> alias);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, String...)
     */
    @NotNull
    @Support
    Table<R> asTable(Table<?> alias, Field<?>... fieldAliases);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, Function)
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, BiFunction)
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    @Support
    Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction);
}
