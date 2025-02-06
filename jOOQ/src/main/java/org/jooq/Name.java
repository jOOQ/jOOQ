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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import org.jooq.impl.QOM.TableAlias;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An identifier.
 * <p>
 * A <code>Name</code> or identifier is a {@link QueryPart} that renders a SQL
 * identifier according to the settings specified in
 * {@link Settings#getRenderQuotedNames()} and
 * {@link Settings#getRenderNameCase()}.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(
 *         field(name("FIRST_NAME"), SQLDataType.VARCHAR),
 *         field(name("LAST_NAME"), SQLDataType.VARCHAR))
 *    .from(table(name("ACTOR")))
 *    .fetch();
 * </code></pre>
 * <p>
 * Instances can be created using {@link DSL#name(String)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Name extends QueryPart, Comparable<Name> {

    /**
     * A flag indicating whether the name is quoted or not.
     * <p>
     * Quoting of names can be overridden by
     * {@link Settings#getRenderQuotedNames()}.
     *
     * @author Lukas Eder
     */
    enum Quoted {

        /**
         * The name is explicitly quoted.
         */
        QUOTED,

        /**
         * The name is explicitly not quoted.
         */
        UNQUOTED,

        /**
         * The name is a system name, and thus it is never quoted.
         */
        SYSTEM,

        /**
         * The name is not quoted explicitly.
         * <p>
         * The behaviour of this name's quoting is governed by
         * {@link RenderQuotedNames#EXPLICIT_DEFAULT_QUOTED} and
         * {@link RenderQuotedNames#EXPLICIT_DEFAULT_UNQUOTED}.
         */
        DEFAULT,

        /**
         * The {@link Name#qualified()} name has mixed values for individual
         * {@link Name#quoted()} flags.
         */
        MIXED
    }

    /**
     * Get the first segment of the qualified name (usually a {@link Catalog} or {@link Schema} name).
     */
    @Nullable
    String first();

    /**
     * Get the last segment of the qualified name (usually a {@link Table}, {@link Field}, or {@link Parameter} name).
     */
    @Nullable
    String last();

    /**
     * Whether this is the empty name.
     * <p>
     * An unnamed object will have an <code>""</code> empty string as its name.
     * If a table has no explicit schema, it will be located in the default
     * schema whose name is <code>""</code>.
     */
    boolean empty();

    /**
     * Whether this is a qualified name.
     * <p>
     * This is <code>true</code> as soon as {@link #getName()} has a length of
     * more than <code>1</code> and {@link #qualifier()} is not null.
     */
    boolean qualified();

    /**
     * Whether this is a qualified name and the {@link #qualifier()} is also a
     * qualified name.
     * <p>
     * This is <code>true</code> as soon as {@link #getName()} has a length of
     * more than <code>2</code> and {@link #qualifier()} is not null and its
     * {@link #qualified()} property is also true.
     */
    boolean qualifierQualified();

    /**
     * This name's qualifier (if it is {@link #qualified()}), or <code>null</code>.
     */
    @Nullable
    Name qualifier();

    /**
     * This name, unqualified.
     */
    @NotNull
    Name unqualifiedName();

    /**
     * Whether this is a quoted name.
     */
    @NotNull
    Quoted quoted();

    /**
     * This name, quoted.
     */
    @NotNull
    Name quotedName();

    /**
     * This name, unquoted.
     */
    @NotNull
    Name unquotedName();

    /**
     * Get the individual, unqualified name parts of this name.
     */
    @NotNull
    Name @NotNull [] parts();

    /**
     * Appends <code>name</code> to this name.
     */
    @NotNull
    Name append(String name);

    /**
     * Appends <code>name</code> (all of its {@link #parts()}) to this name.
     */
    @NotNull
    Name append(Name name);

    /**
     * The qualified name of this SQL identifier.
     */
    @NotNull
    String @NotNull [] getName();

    /**
     * Create an empty {@link WindowDefinition} from this name.
     * <p>
     * A window definition renders itself differently, depending on
     * {@link Context#declareWindows()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The window definition renders its window name
     * (<code>this</code>) along with the <code>AS (window specification)</code>
     * clause. This typically happens in <code>WINDOW</code> clauses.</li>
     * <li>Reference: The window definition renders its alias identifier. This
     * happens everywhere else.</li>
     * </ul>
     *
     * @see #as(WindowSpecification)
     */
    @NotNull
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    WindowDefinition as();

    /**
     * Create a {@link WindowDefinition} from this name.
     * <p>
     * This creates a window definition that can be
     * <ul>
     * <li>declared in the <code>WINDOW</code> clause (see
     * {@link SelectWindowStep#window(WindowDefinition...)}</li>
     * <li>referenced from the <code>OVER</code> clause (see
     * {@link AggregateFunction#over(WindowDefinition)}</li>
     * </ul>
     * <p>
     * A window definition renders itself differently, depending on
     * {@link Context#declareWindows()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The window definition renders its window name
     * (<code>this</code>) along with the <code>AS (window specification)</code>
     * clause. This typically happens in <code>WINDOW</code> clauses.</li>
     * <li>Reference: The window definition renders its alias identifier. This
     * happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ CUBRID, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    WindowDefinition as(WindowSpecification window);

    /**
     * Specify a subselect to refer to by the <code>Name</code> to form a common
     * table expression.
     * <p>
     * Column names are implicitly inherited from the <code>SELECT</code>
     * statement.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> as(ResultQuery<R> query);

    /**
     * Specify a materialized subselect to refer to by the <code>Name</code> to
     * form a common table expression.
     * <p>
     * This adds the PostgreSQL 12 <code>MATERIALIZED</code> hint to the common
     * table expression definition, or silently ignores it, if the hint is not
     * supported.
     * <p>
     * Column names are implicitly inherited from the <code>SELECT</code>
     * statement.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> asMaterialized(ResultQuery<R> query);

    /**
     * Specify a non-materialized subselect to refer to by the <code>Name</code>
     * to form a common table expression.
     * <p>
     * This adds the PostgreSQL 12 <code>NOT MATERIALIZED</code> hint to the
     * common table expression definition, or silently ignores it, if the hint
     * is not supported.
     * <p>
     * Column names are implicitly inherited from the <code>SELECT</code>
     * statement.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> asNotMaterialized(ResultQuery<R> query);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList fields(String... fieldNames);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList fields(Name... fieldNames);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     * <p>
     * This works in a similar way as {@link #fields(String...)}, except
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
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList fields(Function<? super Field<?>, ? extends String> fieldNameFunction);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     * <p>
     * This works in a similar way as {@link #fields(String...)}, except
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
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList fields(BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction);



    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList1 fields(String fieldName1);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList2 fields(String fieldName1, String fieldName2);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList3 fields(String fieldName1, String fieldName2, String fieldName3);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList4 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList5 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList6 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList7 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList8 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList9 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList10 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList11 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList12 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList13 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList14 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList15 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList16 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList17 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList18 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList19 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList20 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList21 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList22 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21, String fieldName22);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList1 fields(Name fieldName1);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList2 fields(Name fieldName1, Name fieldName2);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList3 fields(Name fieldName1, Name fieldName2, Name fieldName3);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList4 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList5 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList6 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList7 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList8 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList9 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList10 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList11 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList12 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList13 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList14 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList15 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList16 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList17 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList18 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList19 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList20 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList21 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    DerivedColumnList22 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21, Name fieldName22);



    @Override
    boolean equals(Object other);

    /**
     * Compare this name with another one ignoring case.
     */
    boolean equalsIgnoreCase(Name other);

    /**
     * Compare two {@link #unquotedName()} by their qualified string versions.
     * <p>
     * {@inheritDoc}
     */
    @Override
    int compareTo(Name o);
}
