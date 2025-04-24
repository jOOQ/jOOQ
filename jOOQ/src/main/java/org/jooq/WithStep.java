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

import static org.jooq.SQLDialect.*;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;


/**
 * This type is part of the jOOQ DSL to create {@link Select}, {@link Insert},
 * {@link Update}, {@link Delete}, {@link Merge} statements prefixed with a
 * <code>WITH</code> clause and with {@link CommonTableExpression}s.
 * <p>
 * Example:
 * <pre><code>
 * DSL.with("table", "col1", "col2")
 *    .as(
 *        select(one(), two())
 *    )
 *    .select()
 *    .from("table")
 * </code></pre>
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface WithStep extends QueryPart {

    // -------------------------------------------------------------------------
    // XXX Add additional common table expressions
    // -------------------------------------------------------------------------

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(String alias);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(String alias, String... fieldAliases);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(String alias, Collection<String> fieldAliases);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(Name alias);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(Name alias, Name... fieldAliases);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(Name alias, Collection<? extends Name> fieldAliases);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     * <p>
     * This works in a similar way as {@link #with(String, String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns as input.
     *
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(String alias, Function<? super Field<?>, ? extends String> fieldNameFunction);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     * <p>
     * This works in a similar way as {@link #with(String, String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns and their column indexes as input.
     *
     * @deprecated - 3.14.0 - [#10156] - These methods will be removed without
     *             replacement from a future jOOQ. They offer convenience that
     *             is unidiomatic for jOOQ's DSL, without offering functionality
     *             that would not be possible otherwise - yet they add
     *             complexity in jOOQ's internals.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support
    WithAsStep with(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction);



    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep1 with(String alias, String fieldAlias1);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep2 with(String alias, String fieldAlias1, String fieldAlias2);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep3 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep4 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep5 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep6 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep7 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep8 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep9 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep10 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep11 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep12 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep13 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep14 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep15 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep16 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep17 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep18 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep19 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep20 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep21 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep22 with(String alias, String fieldAlias1, String fieldAlias2, String fieldAlias3, String fieldAlias4, String fieldAlias5, String fieldAlias6, String fieldAlias7, String fieldAlias8, String fieldAlias9, String fieldAlias10, String fieldAlias11, String fieldAlias12, String fieldAlias13, String fieldAlias14, String fieldAlias15, String fieldAlias16, String fieldAlias17, String fieldAlias18, String fieldAlias19, String fieldAlias20, String fieldAlias21, String fieldAlias22);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep1 with(Name alias, Name fieldAlias1);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep2 with(Name alias, Name fieldAlias1, Name fieldAlias2);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep3 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep4 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep5 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep6 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep7 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep8 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep9 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep10 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep11 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep12 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep13 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep14 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep15 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep16 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep17 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep18 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep19 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep20 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep21 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support
    WithAsStep22 with(Name alias, Name fieldAlias1, Name fieldAlias2, Name fieldAlias3, Name fieldAlias4, Name fieldAlias5, Name fieldAlias6, Name fieldAlias7, Name fieldAlias8, Name fieldAlias9, Name fieldAlias10, Name fieldAlias11, Name fieldAlias12, Name fieldAlias13, Name fieldAlias14, Name fieldAlias15, Name fieldAlias16, Name fieldAlias17, Name fieldAlias18, Name fieldAlias19, Name fieldAlias20, Name fieldAlias21, Name fieldAlias22);



    /**
     * Add another common table expression to the <code>WITH</code> clause.
     * <p>
     * Reusable {@link CommonTableExpression} types can be constructed through
     * <ul>
     * <li>{@link DSL#name(String...)}</li>
     * <li>{@link Name#fields(String...)}</li>
     * <li>
     * {@link DerivedColumnList#as(ResultQuery)}</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    WithStep with(CommonTableExpression<?>... tables);

    /**
     * Add another common table expression to the <code>WITH</code> clause.
     * <p>
     * Reusable {@link CommonTableExpression} types can be constructed through
     * <ul>
     * <li>{@link DSL#name(String...)}</li>
     * <li>{@link Name#fields(String...)}</li>
     * <li>
     * {@link DerivedColumnList#as(ResultQuery)}</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    WithStep with(Collection<? extends CommonTableExpression<?>> tables);

    // -------------------------------------------------------------------------
    // XXX Continue with the actual INSERT, UPDATE, DELETE, SELECT statement type
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL select statement, projecting the known columns from a
     * table.
     * <p>
     * This will project the known columns from the argument table querying
     * {@link Table#fields()}. If no known columns are available (e.g. because
     * the table has been created using {@link DSL#table(String)}), then
     * <code>SELECT *</code> is projected.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT table.col1, table.col2 FROM table
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support
    <R extends Record> SelectWhereStep<R> selectFrom(TableLike<R> table);

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(TableLike)}), this will project <code>SELECT *</code>.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT * FROM table
     * </code></pre>
     *
     * @see DSL#table(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectWhereStep<Record> selectFrom(Name table);

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(TableLike)}), this will project <code>SELECT *</code>.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT * FROM table
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectWhereStep<Record> selectFrom(SQL sql);

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(TableLike)}), this will project <code>SELECT *</code>.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT * FROM table
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see DSL#sql(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectWhereStep<Record> selectFrom(String sql);

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(TableLike)}), this will project <code>SELECT *</code>.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT * FROM table
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectWhereStep<Record> selectFrom(String sql, Object... bindings);

    /**
     * Create a new DSL select statement, projecting <code>*</code>.
     * <p>
     * Without knowing any columns from the argument table (see
     * {@link #selectFrom(TableLike)}), this will project <code>SELECT *</code>.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * SELECT * FROM table
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectWhereStep<Record> selectFrom(String sql, QueryPart... parts);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Collection)} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#select(Collection)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record> select(Collection<? extends SelectFieldOrAsterisk> fields);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectFieldOrAsterisk...)} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </code></pre>
     *
     * @see DSL#select(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record> select(SelectFieldOrAsterisk... fields);



    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1).as(subselect))
     *       .select(field1)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1> SelectSelectStep<Record1<T1>> select(SelectField<T1> field1);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2).as(subselect))
     *       .select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2> SelectSelectStep<Record2<T1, T2>> select(SelectField<T1> field1, SelectField<T2> field2);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3).as(subselect))
     *       .select(field1, field2, field3)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4).as(subselect))
     *       .select(field1, field2, field3, field4)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4, field5).as(subselect))
     *       .select(field1, field2, field3, field4, field5)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f5, f6).as(subselect))
     *       .select(field1, field2, field3, .., field5, field6)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f6, f7).as(subselect))
     *       .select(field1, field2, field3, .., field6, field7)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f7, f8).as(subselect))
     *       .select(field1, field2, field3, .., field7, field8)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f8, f9).as(subselect))
     *       .select(field1, field2, field3, .., field8, field9)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f9, f10).as(subselect))
     *       .select(field1, field2, field3, .., field9, field10)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f10, f11).as(subselect))
     *       .select(field1, field2, field3, .., field10, field11)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f11, f12).as(subselect))
     *       .select(field1, field2, field3, .., field11, field12)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f12, f13).as(subselect))
     *       .select(field1, field2, field3, .., field12, field13)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f13, f14).as(subselect))
     *       .select(field1, field2, field3, .., field13, field14)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f14, f15).as(subselect))
     *       .select(field1, field2, field3, .., field14, field15)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f15, f16).as(subselect))
     *       .select(field1, field2, field3, .., field15, field16)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f16, f17).as(subselect))
     *       .select(field1, field2, field3, .., field16, field17)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f17, f18).as(subselect))
     *       .select(field1, field2, field3, .., field17, field18)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f18, f19).as(subselect))
     *       .select(field1, field2, field3, .., field18, field19)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f19, f20).as(subselect))
     *       .select(field1, field2, field3, .., field19, field20)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f20, f21).as(subselect))
     *       .select(field1, field2, field3, .., field20, field21)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f21, f22).as(subselect))
     *       .select(field1, field2, field3, .., field21, field22)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22);



    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Collection)} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.selectDistinct(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(Collection)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record> selectDistinct(Collection<? extends SelectFieldOrAsterisk> fields);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectFieldOrAsterisk...)} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record> selectDistinct(SelectFieldOrAsterisk... fields);



    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1).as(subselect))
     *       .selectDistinct(field1)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1> SelectSelectStep<Record1<T1>> selectDistinct(SelectField<T1> field1);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2).as(subselect))
     *       .selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2> SelectSelectStep<Record2<T1, T2>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3).as(subselect))
     *       .selectDistinct(field1, field2, field3)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4).as(subselect))
     *       .selectDistinct(field1, field2, field3, field4)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4, field5).as(subselect))
     *       .selectDistinct(field1, field2, field3, field4, field5)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f5, f6).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field5, field6)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f6, f7).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field6, field7)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f7, f8).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field7, field8)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f8, f9).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field8, field9)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f9, f10).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field9, field10)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f10, f11).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field10, field11)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f11, f12).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field11, field12)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f12, f13).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field12, field13)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f13, f14).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field13, field14)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f14, f15).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field14, field15)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f15, f16).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field15, field16)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f16, f17).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field16, field17)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f17, f18).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field17, field18)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f18, f19).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field18, field19)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f19, f20).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field19, field20)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f20, f21).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field20, field21)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(SelectFieldOrAsterisk...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField, SelectField)} instead.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f21, f22).as(subselect))
     *       .selectDistinct(field1, field2, field3, .., field21, field22)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectDistinct(SelectFieldOrAsterisk...)
     * @see #selectDistinct(SelectFieldOrAsterisk...)
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> selectDistinct(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22);



    /**
     * Create a new DSL select statement for a constant <code>0</code> literal.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectZero()} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.selectZero()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#zero()
     * @see DSL#selectZero()
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record1<Integer>> selectZero();

    /**
     * Create a new DSL select statement for a constant <code>1</code> literal.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectOne()} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.selectOne()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#one()
     * @see DSL#selectOne()
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record1<Integer>> selectOne();

    /**
     * Create a new DSL select statement for <code>COUNT(*)</code>.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectCount()} instead.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.selectCount()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </code></pre>
     *
     * @see DSL#selectCount()
     */
    @NotNull @CheckReturnValue
    @Support
    SelectSelectStep<Record1<Integer>> selectCount();

    /**
     * Create a new DSL insert statement.
     * <p>
     * This type of insert may feel more convenient to some users, as it uses
     * the <code>UPDATE</code> statement's <code>SET a = b</code> syntax.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.insertInto(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .newRecord()
     *       .set(field1, value3)
     *       .set(field2, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> InsertSetStep<R> insertInto(Table<R> into);



    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1).as(subselect))
     *       .insertInto(table, field1)
     *       .values(field1)
     *       .values(field1)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1> InsertValuesStep1<R, T1> insertInto(Table<R> into, Field<T1> field1);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2).as(subselect))
     *       .insertInto(table, field1, field2)
     *       .values(field1, field2)
     *       .values(field1, field2)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2> InsertValuesStep2<R, T1, T2> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3).as(subselect))
     *       .insertInto(table, field1, field2, field3)
     *       .values(field1, field2, field3)
     *       .values(field1, field2, field3)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4).as(subselect))
     *       .insertInto(table, field1, field2, field3, field4)
     *       .values(field1, field2, field3, field4)
     *       .values(field1, field2, field3, field4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(field1, field2, field3, field4, field5).as(subselect))
     *       .insertInto(table, field1, field2, field3, field4, field5)
     *       .values(field1, field2, field3, field4, field5)
     *       .values(field1, field2, field3, field4, field5)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f5, f6).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field5, field6)
     *       .values(valueA1, valueA2, valueA3, .., valueA5, valueA6)
     *       .values(valueB1, valueB2, valueB3, .., valueB5, valueB6)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f6, f7).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field6, field7)
     *       .values(valueA1, valueA2, valueA3, .., valueA6, valueA7)
     *       .values(valueB1, valueB2, valueB3, .., valueB6, valueB7)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f7, f8).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field7, field8)
     *       .values(valueA1, valueA2, valueA3, .., valueA7, valueA8)
     *       .values(valueB1, valueB2, valueB3, .., valueB7, valueB8)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f8, f9).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field8, field9)
     *       .values(valueA1, valueA2, valueA3, .., valueA8, valueA9)
     *       .values(valueB1, valueB2, valueB3, .., valueB8, valueB9)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f9, f10).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field9, field10)
     *       .values(valueA1, valueA2, valueA3, .., valueA9, valueA10)
     *       .values(valueB1, valueB2, valueB3, .., valueB9, valueB10)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f10, f11).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field10, field11)
     *       .values(valueA1, valueA2, valueA3, .., valueA10, valueA11)
     *       .values(valueB1, valueB2, valueB3, .., valueB10, valueB11)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f11, f12).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field11, field12)
     *       .values(valueA1, valueA2, valueA3, .., valueA11, valueA12)
     *       .values(valueB1, valueB2, valueB3, .., valueB11, valueB12)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f12, f13).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field12, field13)
     *       .values(valueA1, valueA2, valueA3, .., valueA12, valueA13)
     *       .values(valueB1, valueB2, valueB3, .., valueB12, valueB13)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f13, f14).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field13, field14)
     *       .values(valueA1, valueA2, valueA3, .., valueA13, valueA14)
     *       .values(valueB1, valueB2, valueB3, .., valueB13, valueB14)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f14, f15).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field14, field15)
     *       .values(valueA1, valueA2, valueA3, .., valueA14, valueA15)
     *       .values(valueB1, valueB2, valueB3, .., valueB14, valueB15)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f15, f16).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field15, field16)
     *       .values(valueA1, valueA2, valueA3, .., valueA15, valueA16)
     *       .values(valueB1, valueB2, valueB3, .., valueB15, valueB16)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f16, f17).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field16, field17)
     *       .values(valueA1, valueA2, valueA3, .., valueA16, valueA17)
     *       .values(valueB1, valueB2, valueB3, .., valueB16, valueB17)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f17, f18).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field17, field18)
     *       .values(valueA1, valueA2, valueA3, .., valueA17, valueA18)
     *       .values(valueB1, valueB2, valueB3, .., valueB17, valueB18)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f18, f19).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field18, field19)
     *       .values(valueA1, valueA2, valueA3, .., valueA18, valueA19)
     *       .values(valueB1, valueB2, valueB3, .., valueB18, valueB19)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f19, f20).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field19, field20)
     *       .values(valueA1, valueA2, valueA3, .., valueA19, valueA20)
     *       .values(valueB1, valueB2, valueB3, .., valueB19, valueB20)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f20, f21).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field20, field21)
     *       .values(valueA1, valueA2, valueA3, .., valueA20, valueA21)
     *       .values(valueB1, valueB2, valueB3, .., valueB20, valueB21)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * using(configuration)
     *       .with(name("t").fields(f1, f2, f3, .., f21, f22).as(subselect))
     *       .insertInto(table, field1, field2, field3, .., field21, field22)
     *       .values(valueA1, valueA2, valueA3, .., valueA21, valueA22)
     *       .values(valueB1, valueB2, valueB3, .., valueB21, valueB22)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);



    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Field<?>... fields);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields);

    /**
     * Create a new DSL update statement.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.update(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </code></pre>
     * <p>
     * Note that some databases support table expressions more complex than
     * simple table references. In MySQL, for instance, you can write
     * <pre><code>
     * create.update(t1.join(t2).on(t1.id.eq(t2.id)))
     *       .set(t1.value, value1)
     *       .set(t2.value, value2)
     *       .where(t1.id.eq(10))
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> UpdateSetFirstStep<R> update(Table<R> table);

    /**
     * Create a new DSL SQL standard MERGE statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <th>dialect</th>
     * <th>support type</th>
     * <th>documentation</th>
     * </tr>
     * <tr>
     * <td>DB2</td>
     * <td>SQL:2008 standard and major enhancements</td>
     * <td><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.admin.doc/doc/r0010873.htm"
     * >http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.
     * ibm.db2.udb.admin.doc/doc/r0010873.htm</a></td>
     * </tr>
     * <tr>
     * <td>HSQLDB</td>
     * <td>SQL:2008 standard</td>
     * <td><a
     * href="http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA"
     * >http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA</a></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>SQL:2008 standard and minor enhancements</td>
     * <td><a href=
     * "http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/statements_9016.htm"
     * >http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/
     * statements_9016.htm</a></td>
     * </tr>
     * <tr>
     * <td>SQL Server</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href= "http://msdn.microsoft.com/de-de/library/bb510625.aspx"
     * >http://msdn.microsoft.com/de-de/library/bb510625.aspx</a></td>
     * </tr>
     * <tr>
     * <td>Sybase</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href=
     * "http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html"
     * >http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html</a></td>
     * </tr>
     * </table>
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.mergeInto(table)
     *       .using(select)
     *       .on(condition)
     *       .whenMatchedThenUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .whenNotMatchedThenInsert(field1, field2)
     *       .values(value1, value2)
     *       .execute();
     * </code></pre>
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table);



    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1> MergeKeyStep1<R, T1> mergeInto(Table<R> table, Field<T1> field1);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2> MergeKeyStep2<R, T1, T2> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href="https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     * @deprecated - [#10045] - 3.14.0 - Use the standard SQL MERGE API instead, via {@link #mergeInto(Table)}
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);



    /**
     * Create a new DSL merge statement (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "https://www.h2database.com/html/commands.html#merge_into"
     * >https://www.h2database.com/html/commands.html#merge_into</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Field<?>... fields);

    /**
     * Create a new DSL merge statement (H2-specific syntax).
     *
     * @see #mergeInto(Table, Field...)
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields);

    /**
     * Create a new DSL delete statement.
     * <p>
     * Example: <pre><code>
     * DSLContext create = DSL.using(configuration);
     *
     * create.delete(table)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </code></pre>
     * <p>
     * Some but not all databases support aliased tables in delete statements.
     */
    @NotNull @CheckReturnValue
    @Support({ POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> DeleteUsingStep<R> delete(Table<R> table);
}
