/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq;

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.types.Interval;
import org.jooq.util.oracle.OracleDSL;

/**
 * A field used in tables and conditions
 * <p>
 * Note that all fields qualify as {@link GroupField}, i.e. they can always be
 * used in <code>GROUP BY</code> clauses
 *
 * @param <T> The field type
 * @author Lukas Eder
 */
@State(
    name = "Field",
    aliases = {
        "AliasedField",
        "ConstantExpression",
        "ArithmeticExpression",
        "StringFunction",
        "DateFunction",
        "MathFunction",
        "SystemFunction",
        "GroupingFunction",
        "Function",
        "WindowFunction",
        "ConnectByExpression",
        "BitwiseOperation"
    },
    terminal = true
)
public interface Field<T> extends GroupField {

    // ------------------------------------------------------------------------
    // API
    // ------------------------------------------------------------------------

    /**
     * The name of the field.
     * <p>
     * The name is any of these:
     * <ul>
     * <li>The formal name of the field, if it is a <i>physical table/view
     * field</i></li>
     * <li>The alias of an <i>aliased field</i></li>
     * <li>A generated / unspecified value for any other <i>expression</i></li>
     * <li>The name of a parameter if it is a named {@link Param}</li>
     * </ul>
     */
    String getName();

    /**
     * The Java type of the field.
     */
    Class<T> getType();

    /**
     * The type of this field (might not be dialect-specific).
     */
    DataType<T> getDataType();

    /**
     * The dialect-specific type of this field.
     */
    DataType<T> getDataType(Configuration configuration);

    /**
     * Create an alias for this field.
     * <p>
     * Note that the case-sensitivity of the returned field depends on
     * {@link Settings#getRenderNameStyle()}. By default, field aliases are
     * quoted, and thus case-sensitive!
     *
     * @param alias The alias name
     * @return The field alias
     */
    @Support
    @Transition(
        name = "AS",
        args = "String",
        to = "AliasedField"
    )
    Field<T> as(String alias);

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Watch out! This is {@link Object#equals(Object)}, not a jOOQ DSL
     * feature!</strong>
     */
    @Override
    boolean equals(Object other);

    // ------------------------------------------------------------------------
    // Type casts
    // ------------------------------------------------------------------------

    /**
     * Cast this field to the type of another field.
     * <p>
     * This results in the same as casting this field to
     * {@link DataType#getCastTypeName()}
     *
     * @param <Z> The generic type of the cast field
     * @param field The field whose type is used for the cast
     * @return The cast field
     * @see #cast(DataType)
     */
    @Support
    <Z> Field<Z> cast(Field<Z> field);

    /**
     * Cast this field to a dialect-specific data type.
     *
     * @param <Z> The generic type of the cast field
     * @param type The data type that is used for the cast
     * @return The cast field
     */
    @Support
    <Z> Field<Z> cast(DataType<Z> type);

    /**
     * Cast this field to another type.
     * <p>
     * The actual cast may not be accurate as the {@link DataType} has to be
     * "guessed" from the jOOQ-configured data types. Use
     * {@link #cast(DataType)} for more accurate casts.
     *
     * @param <Z> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     * @see #cast(DataType)
     */
    @Support
    <Z> Field<Z> cast(Class<Z> type);

    // ------------------------------------------------------------------------
    // Type coercion
    // ------------------------------------------------------------------------

    /**
     * Coerce this field to the type of another field.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <Z> The generic type of the coerced field
     * @param field The field whose type is used for the coercion
     * @return The coerced field
     * @see #coerce(DataType)
     * @see #cast(Field)
     */
    @Support
    <Z> Field<Z> coerce(Field<Z> field);

    /**
     * Coerce this field to a dialect-specific data type.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <Z> The generic type of the coerced field
     * @param type The data type that is used for the coercion
     * @return The coerced field
     * @see #cast(DataType)
     */
    @Support
    <Z> Field<Z> coerce(DataType<Z> type);

    /**
     * Coerce this field to another type.
     * <p>
     * Unlike with casting, coercing doesn't affect the way the database sees a
     * <code>Field</code>'s type. This is how coercing affects your SQL:
     * <h3>Bind values</h3> <code><pre>
     * // This binds an int value to a JDBC PreparedStatement
     * DSL.val(1).coerce(String.class);
     *
     * // This binds an int value to a JDBC PreparedStatement
     * // and casts it to VARCHAR in SQL
     * DSL.val(1).cast(String.class);
     * </pre></code>
     * <h3>Other Field types</h3> <code><pre>
     * // This fetches a String value for the BOOK.ID field from JDBC
     * BOOK.ID.coerce(String.class);
     *
     * // This fetches a String value for the BOOK.ID field from JDBC
     * // after casting it to VARCHAR in the database
     * BOOK.ID.cast(String.class);
     * </pre></code>
     *
     * @param <Z> The generic type of the coerced field
     * @param type The type that is used for the coercion
     * @return The coerced field
     * @see #coerce(DataType)
     * @see #cast(Class)
     */
    @Support
    <Z> Field<Z> coerce(Class<Z> type);

    // ------------------------------------------------------------------------
    // Conversion of field into a sort field
    // ------------------------------------------------------------------------

    /**
     * Create an ascending sort field from this field.
     * <p>
     * This is the same as calling {@link #sort(SortOrder)} with
     * {@link SortOrder#ASC}
     *
     * @return This field as an ascending sort field
     */
    @Support
    @Transition(
        name = "ASC"
    )
    SortField<T> asc();

    /**
     * Create a descending sort field from this field.
     * <p>
     * This is the same as calling {@link #sort(SortOrder)} with
     * {@link SortOrder#DESC}
     *
     * @return This field as a descending sort field
     */
    @Support
    @Transition(
        name = "DESC"
    )
    SortField<T> desc();

    /**
     * Create an ascending/descending sort field from this field.
     *
     * @param order The sort order
     * @return This field as an ascending/descending sort field.
     */
    @Support
    SortField<T> sort(SortOrder order);

    /**
     * Create an indirected sort field.
     * <p>
     * Create a sort field of the form <code><pre>
     * CASE [this] WHEN [sortList.get(0)] THEN 0
     *             WHEN [sortList.get(1)] THEN 1
     *             ...
     *             WHEN [sortList.get(n)] THEN n
     *                                    ELSE null
     * END ASC
     * </pre></code>
     * <p>
     * Note: You can use this in combination with {@link SortField#nullsFirst()}
     * or {@link SortField#nullsLast()} to specify whether the default should
     * have highest or lowest priority.
     *
     * @param sortList The list containing sort value preferences
     * @return The sort field
     */
    @Support
    SortField<Integer> sortAsc(Collection<T> sortList);

    /**
     * Create an indirected sort field.
     * <p>
     * Create a sort field of the form <code><pre>
     * CASE [this] WHEN [sortList[0]] THEN 0
     *             WHEN [sortList[1]] THEN 1
     *             ...
     *             WHEN [sortList[n]] THEN n
     *                                ELSE null
     * END ASC
     * </pre></code>
     * <p>
     * Note: You can use this in combination with {@link SortField#nullsFirst()}
     * or {@link SortField#nullsLast()} to specify whether the default should
     * have highest or lowest priority.
     *
     * @param sortList The list containing sort value preferences
     * @return The sort field
     */
    @Support
    SortField<Integer> sortAsc(T... sortList);

    /**
     * Create an indirected sort field.
     * <p>
     * Create a sort field of the form <code><pre>
     * CASE [this] WHEN [sortList.get(0)] THEN 0
     *             WHEN [sortList.get(1)] THEN 1
     *             ...
     *             WHEN [sortList.get(n)] THEN n
     *                                    ELSE null
     * END DESC
     * </pre></code>
     * <p>
     * Note: You can use this in combination with {@link SortField#nullsFirst()}
     * or {@link SortField#nullsLast()} to specify whether the default should
     * have highest or lowest priority.
     *
     * @param sortList The list containing sort value preferences
     * @return The sort field
     */
    @Support
    SortField<Integer> sortDesc(Collection<T> sortList);

    /**
     * Create an indirected sort field.
     * <p>
     * Create a sort field of the form <code><pre>
     * CASE [this] WHEN [sortList[0]] THEN 0
     *             WHEN [sortList[1]] THEN 1
     *             ...
     *             WHEN [sortList[n]] THEN n
     *                                    ELSE null
     * END DESC
     * </pre></code>
     * <p>
     * Note: You can use this in combination with {@link SortField#nullsFirst()}
     * or {@link SortField#nullsLast()} to specify whether the default should
     * have highest or lowest priority.
     *
     * @param sortList The list containing sort value preferences
     * @return The sort field
     */
    @Support
    SortField<Integer> sortDesc(T... sortList);

    /**
     * Create an indirected sort field.
     * <p>
     * Create a sort field of the form (in pseudo code)<code><pre>
     * CASE [this] WHEN [sortMap.key(0)] THEN sortMap.value(0)
     *             WHEN [sortMap.key(1)] THEN sortMap.value(1)
     *             ...
     *             WHEN [sortMap.key(n)] THEN sortMap.value(n)
     *                                   ELSE null
     * END DESC
     * </pre></code>
     * <p>
     * Note: You can use this in combination with {@link SortField#nullsFirst()}
     * or {@link SortField#nullsLast()} to specify whether the default should
     * have highest or lowest priority.
     *
     * @param sortMap The list containing sort value preferences
     * @return The sort field
     */
    @Support
    <Z> SortField<Z> sort(Map<T, Z> sortMap);

    // ------------------------------------------------------------------------
    // Arithmetic operations
    // ------------------------------------------------------------------------

    /**
     * Negate this field to get its negative value.
     * <p>
     * This renders the same on all dialects: <code><pre>-[this]</pre></code>
     */
    @Support
    @Transition(
        name = "NEG",
        to = "ArithmeticExpression"
    )
    Field<T> neg();

    /**
     * An arithmetic expression adding this to value.
     *
     * @see #add(Field)
     */
    @Support
    @Transition(
        name = "ADD",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> add(Number value);

    /**
     * An arithmetic expression to add value to this.
     * <p>
     * The behaviour of this operation is as follows:
     * <table border="1">
     * <tr>
     * <th>Operand 1</th>
     * <th>Operand 2</th>
     * <th>Result Type</th>
     * </tr>
     * <tr>
     * <td>Numeric</td>
     * <td>Numeric</td>
     * <td>Numeric</td>
     * </tr>
     * <tr>
     * <td>Date / Time</td>
     * <td>Numeric</td>
     * <td>Date / Time</td>
     * </tr>
     * <tr>
     * <td>Date / Time</td>
     * <td>Interval</td>
     * <td>Date / Time</td>
     * </tr>
     * <tr>
     * <td>Interval</td>
     * <td>Interval</td>
     * <td>Interval</td>
     * </tr>
     * </table>
     */
    @Support
    @Transition(
        name = "ADD",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> add(Field<?> value);

    /**
     * An alias for {@link #add(Number)}.
     *
     * @see #add(Number)
     */
    @Support
    @Transition(
        name = "PLUS",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> plus(Number value);

    /**
     * An alias for {@link #add(Field)}.
     *
     * @see #add(Field)
     */
    @Support
    @Transition(
        name = "PLUS",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> plus(Field<?> value);

    /**
     * An arithmetic expression subtracting value from this.
     *
     * @see #sub(Field)
     */
    @Support
    @Transition(
        name = "SUB",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> sub(Number value);

    /**
     * An arithmetic expression subtracting value from this.
     * <p>
     * <table border="1">
     * <tr>
     * <th>Operand 1</th>
     * <th>Operand 2</th>
     * <th>Result Type</th>
     * </tr>
     * <tr>
     * <td>Numeric</td>
     * <td>Numeric</td>
     * <td>Numeric</td>
     * </tr>
     * <tr>
     * <td>Date / Time</td>
     * <td>Numeric</td>
     * <td>Date / Time</td>
     * </tr>
     * <tr>
     * <td>Date / Time</td>
     * <td>Interval</td>
     * <td>Date / Time</td>
     * </tr>
     * <tr>
     * <td>Interval</td>
     * <td>Interval</td>
     * <td>Interval</td>
     * </tr>
     * </table>
     * <p>
     * In order to subtract one date time field from another, use any of these
     * methods:
     * <ul>
     * <li> {@link DSL#dateDiff(Field, Field)}</li>
     * <li> {@link DSL#timestampDiff(Field, Field)}</li>
     * </ul>
     */
    @Support
    @Transition(
        name = "SUB",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> sub(Field<?> value);

    /**
     * An alias for {@link #sub(Number)}.
     *
     * @see #sub(Number)
     */
    @Support
    @Transition(
        name = "SUBTRACT",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> subtract(Number value);

    /**
     * An alias for {@link #sub(Field)}.
     *
     * @see #sub(Field)
     */
    @Support
    @Transition(
        name = "SUBTRACT",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> subtract(Field<?> value);

    /**
     * An alias for {@link #sub(Number)}.
     *
     * @see #sub(Number)
     */
    @Support
    @Transition(
        name = "MINUS",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> minus(Number value);

    /**
     * An alias for {@link #sub(Field)}.
     *
     * @see #sub(Field)
     */
    @Support
    @Transition(
        name = "MINUS",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> minus(Field<?> value);

    /**
     * An arithmetic expression multiplying this with value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the result is a number of the same
     * type as this field.</li>
     * <li>If this is an <code>INTERVAL</code> field, then the result is also an
     * <code>INTERVAL</code> field (see {@link Interval})</li>
     * </ul>
     */
    @Support
    @Transition(
        name = "MUL",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> mul(Number value);

    /**
     * An arithmetic expression multiplying this with value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the result is a number of the same
     * type as this field.</li>
     * <li>If this is an <code>INTERVAL</code> field, then the result is also an
     * <code>INTERVAL</code> field (see {@link Interval})</li>
     * </ul>
     */
    @Support
    @Transition(
        name = "MUL",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> mul(Field<? extends Number> value);

    /**
     * An alias for {@link #mul(Number)}.
     *
     * @see #mul(Number)
     */
    @Support
    @Transition(
        name = "MULTIPLY",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> multiply(Number value);

    /**
     * An alias for {@link #mul(Field)}.
     *
     * @see #mul(Field)
     */
    @Support
    @Transition(
        name = "MULTIPLY",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> multiply(Field<? extends Number> value);

    /**
     * An arithmetic expression dividing this by value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the result is a number of the same
     * type as this field.</li>
     * <li>If this is an <code>INTERVAL</code> field, then the result is also an
     * <code>INTERVAL</code> field (see {@link Interval})</li>
     * </ul>
     */
    @Support
    @Transition(
        name = "DIV",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> div(Number value);

    /**
     * An arithmetic expression dividing this by value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the result is a number of the same
     * type as this field.</li>
     * <li>If this is an <code>INTERVAL</code> field, then the result is also an
     * <code>INTERVAL</code> field (see {@link Interval})</li>
     * </ul>
     */
    @Support
    @Transition(
        name = "DIV",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> div(Field<? extends Number> value);

    /**
     * An alias for {@link #div(Number)}.
     *
     * @see #div(Number)
     */
    @Support
    @Transition(
        name = "DIVIDE",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> divide(Number value);

    /**
     * An alias for {@link #div(Field)}.
     *
     * @see #div(Field)
     */
    @Support
    @Transition(
        name = "DIVIDE",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> divide(Field<? extends Number> value);

    /**
     * An arithmetic expression getting the modulo of this divided by value.
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code> ... or the modulo function
     * elsewhere: <code><pre>mod([this], [value])</pre></code>
     */
    @Support
    @Transition(
        name = "MOD",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> mod(Number value);

    /**
     * An arithmetic expression getting the modulo of this divided by value.
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code> ... or the modulo function
     * elsewhere: <code><pre>mod([this], [value])</pre></code>
     */
    @Support
    @Transition(
        name = "MOD",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> mod(Field<? extends Number> value);

    /**
     * An alias for {@link #mod(Number)}.
     *
     * @see #mod(Number)
     */
    @Support
    @Transition(
        name = "MODULO",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> modulo(Number value);

    /**
     * An alias for {@link #mod(Field)}.
     *
     * @see #mod(Field)
     */
    @Support
    @Transition(
        name = "MODULO",
        args = "Field",
        to = "ArithmeticExpression"
    )
    Field<T> modulo(Field<? extends Number> value);

    // ------------------------------------------------------------------------
    // NULL predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to check this field against <code>null</code>.
     * <p>
     * SQL: <code>this is null</code>
     */
    @Support
    @Transition(
        name = "IS NULL",
        to = "NullPredicate"
    )
    Condition isNull();

    /**
     * Create a condition to check this field against <code>null</code>.
     * <p>
     * SQL: <code>this is not null</code>
     */
    @Support
    @Transition(
        name = "IS NOT NULL",
        to = "NullPredicate"
    )
    Condition isNotNull();

    // ------------------------------------------------------------------------
    // DISTINCT predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to check if this field is <code>DISTINCT</code> from
     * another value.
     * <p>
     * If this is not supported by the underlying database, jOOQ will render
     * this instead: <code><pre>
     * CASE WHEN [this] IS     NULL AND [value] IS     NULL THEN FALSE
     *      WHEN [this] IS     NULL AND [value] IS NOT NULL THEN TRUE
     *      WHEN [this] IS NOT NULL AND [value] IS     NULL THEN TRUE
     *      WHEN [this] =               [value]             THEN FALSE
     *      ELSE                                                 TRUE
     * END
     * </pre></code> SQL: <code>this is distinct from value</code>
     */
    @Support
    @Transition(
        name = "IS DISTINCT FROM",
        args = "Field",
        to = "DistinctPredicate"
    )
    Condition isDistinctFrom(T value);

    /**
     * Create a condition to check if this field is <code>DISTINCT</code> from
     * another field.
     * <p>
     * If this is not supported by the underlying database, jOOQ will render
     * this instead: <code><pre>
     * CASE WHEN [this] IS     NULL AND [field] IS     NULL THEN FALSE
     *      WHEN [this] IS     NULL AND [field] IS NOT NULL THEN TRUE
     *      WHEN [this] IS NOT NULL AND [field] IS     NULL THEN TRUE
     *      WHEN [this] =               [field]             THEN FALSE
     *      ELSE                                                 TRUE
     * END
     * </pre></code> SQL: <code>this is distinct from field</code>
     */
    @Support
    @Transition(
        name = "IS DISTINCT FROM",
        args = "Field",
        to = "DistinctPredicate"
    )
    Condition isDistinctFrom(Field<T> field);

    /**
     * Create a condition to check if this field is <code>NOT DISTINCT</code>
     * from another value.
     * <p>
     * If this is not supported by the underlying database, jOOQ will render
     * this instead: <code><pre>
     * CASE WHEN [this] IS     NULL AND [value] IS     NULL THEN TRUE
     *      WHEN [this] IS     NULL AND [value] IS NOT NULL THEN FALSE
     *      WHEN [this] IS NOT NULL AND [value] IS     NULL THEN FALSE
     *      WHEN [this] =               [value]             THEN TRUE
     *      ELSE                                                 FALSE
     * END
     * </pre></code> SQL: <code>this is not distinct from value</code>
     */
    @Support
    @Transition(
        name = "IS NOT DISTINCT FROM",
        args = "Field",
        to = "DistinctPredicate"
    )
    Condition isNotDistinctFrom(T value);

    /**
     * Create a condition to check if this field is <code>NOT DISTINCT</code>
     * from another field.
     * <p>
     * If this is not supported by the underlying database, jOOQ will render
     * this instead: <code><pre>
     * CASE WHEN [this] IS     NULL AND [field] IS     NULL THEN TRUE
     *      WHEN [this] IS     NULL AND [field] IS NOT NULL THEN FALSE
     *      WHEN [this] IS NOT NULL AND [field] IS     NULL THEN FALSE
     *      WHEN [this] =               [value]             THEN TRUE
     *      ELSE                                                 FALSE
     * END
     * </pre></code> SQL: <code>this is not distinct from field</code>
     */
    @Support
    @Transition(
        name = "IS NOT DISTINCT FROM",
        args = "Field",
        to = "DistinctPredicate"
    )
    Condition isNotDistinctFrom(Field<T> field);

    // ------------------------------------------------------------------------
    // LIKE_REGEX predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to regex-pattern-check this field against a pattern.
     * <p>
     * The SQL:2008 standard specifies a <code>&lt;regex like predicate></code>
     * of the following form: <code><pre>
     * &lt;regex like predicate> ::=
     *   &lt;row value predicand> &lt;regex like predicate part 2>
     *
     * &lt;regex like predicate part 2> ::=
     *  [ NOT ] LIKE_REGEX &lt;XQuery pattern> [ FLAG &lt;XQuery option flag> ]
     * </pre></code>
     * <p>
     * This particular <code>LIKE_REGEX</code> operator comes in several
     * flavours for various databases. jOOQ supports regular expressions as
     * follows:
     * <table border="1">
     * <tr>
     * <th>SQL dialect</th>
     * <th>SQL syntax</th>
     * <th>Pattern syntax</th>
     * <th>Documentation</th>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#ASE}</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#CUBRID}</td>
     * <td><code>[search] REGEXP [pattern]</code></td>
     * <td>POSIX</td>
     * <td><a href=
     * "http://www.cubrid.org/manual/841/en/REGEXP%20Conditional%20Expression"
     * >http://www.cubrid.org/manual/841/en/REGEXP Conditional Expression</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#DB2}</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#DERBY}</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#H2}</td>
     * <td><code>[search] REGEXP [pattern]</code></td>
     * <td>Java</td>
     * <td><a href=
     * "http://www.h2database.com/html/grammar.html#condition_right_hand_side"
     * >http
     * ://www.h2database.com/html/grammar.html#condition_right_hand_side</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#HSQLDB}</td>
     * <td><code>REGEXP_MATCHES([search], [pattern])</code></td>
     * <td>Java</td>
     * <td><a
     * href="http://hsqldb.org/doc/guide/builtinfunctions-chapt.html#N13577"
     * >http://hsqldb.org/doc/guide/builtinfunctions-chapt.html#N13577</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#INGRES}</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#MYSQL}</td>
     * <td><code>[search] REGEXP [pattern]</code></td>
     * <td>POSIX</td>
     * <td><a href=
     * "http://dev.mysql.com/doc/refman/5.6/en/regexp.html">http://dev
     * .mysql.com/doc/refman/5.6/en/regexp.html</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#ORACLE}</td>
     * <td><code>REGEXP_LIKE([search], [pattern])</code></td>
     * <td>POSIX</td>
     * <td><a href=
     * "http://docs.oracle.com/cd/E14072_01/server.112/e10592/conditions007.htm#sthref1994"
     * >http://docs.oracle.com/cd/E14072_01/server.112/e10592/conditions007.htm#
     * sthref1994</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#POSTGRES}</td>
     * <td><code>[search] ~ [pattern]</code></td>
     * <td>POSIX</td>
     * <td><a href=
     * "http://www.postgresql.org/docs/9.1/static/functions-matching.html#FUNCTIONS-POSIX-REGEXP"
     * >http://www.postgresql.org/docs/9.1/static/functions-matching.html#
     * FUNCTIONS-POSIX-REGEXP</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#SQLITE}</td>
     * <td><code>[search] REGEXP [pattern]</code></td>
     * <td>? This module has to be loaded explicitly</td>
     * <td><a href="http://www.sqlite.org/lang_expr.html">http://www.sqlite.org/
     * lang_expr.html</a></td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#SQLSERVER}</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link SQLDialect#SYBASE}</td>
     * <td><code>[search] REGEXP [pattern]</code></td>
     * <td>Perl</td>
     * <td><a href=
     * "http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0.1/dbreference/like-regexp-similarto.html"
     * >http://infocenter.sybase.com/help/topic/com.sybase.help.sqlanywhere.12.0
     * .1/dbreference/like-regexp-similarto.html</a></td>
     * </tr>
     * </table>
     *
     * @see #likeRegex(String)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SYBASE })
    @Transition(
        name = "LIKE_REGEX",
        args = "Field",
        to = "LikePredicate"
    )
    Condition likeRegex(String pattern);

    /**
     * Create a condition to regex-pattern-check this field against a pattern.
     * <p>
     * See {@link #likeRegex(String)} for more details
     *
     * @see #likeRegex(String)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SYBASE })
    @Transition(
        name = "LIKE_REGEX",
        args = "Field",
        to = "LikePredicate"
    )
    Condition likeRegex(Field<String> pattern);

    /**
     * Create a condition to regex-pattern-check this field against a pattern.
     * <p>
     * See {@link #likeRegex(String)} for more details
     *
     * @see #likeRegex(String)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SYBASE })
    @Transition(
        name = "NOT LIKE_REGEX",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLikeRegex(String pattern);

    /**
     * Create a condition to regex-pattern-check this field against a pattern.
     * <p>
     * See {@link #likeRegex(String)} for more details
     *
     * @see #likeRegex(Field)
     */
    @Support({ CUBRID, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLITE, SYBASE })
    @Transition(
        name = "NOT LIKE_REGEX",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLikeRegex(Field<String> pattern);

    // ------------------------------------------------------------------------
    // LIKE predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this like value</code>
     */
    @Support
    @Transition(
        name = "LIKE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition like(Field<String> value);

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this like value escape 'e'</code>
     */
    @Support
    @Transition(
        name = "LIKE",
        args = {
            "Field",
            "Character",
        },
        to = "LikePredicate"
    )
    Condition like(Field<String> value, char escape);

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this like value</code>
     */
    @Support
    @Transition(
        name = "LIKE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition like(String value);

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this like value escape 'e'</code>
     */
    @Support
    @Transition(
        name = "LIKE",
        args = {
            "Field",
            "Character",
        },
        to = "LikePredicate"
    )
    Condition like(String value, char escape);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a field.
     * <p>
     * This translates to <code>this ilike field</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(field)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "LIKE IGNORE CASE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition likeIgnoreCase(Field<String> field);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a field.
     * <p>
     * This translates to <code>this ilike field</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(field)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "LIKE IGNORE CASE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition likeIgnoreCase(Field<String> field, char escape);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(value)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "LIKE IGNORE CASE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition likeIgnoreCase(String value);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(value)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "LIKE IGNORE CASE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition likeIgnoreCase(String value, char escape);

    /**
     * Create a condition to pattern-check this field against a field.
     * <p>
     * SQL: <code>this not like field</code>
     */
    @Support
    @Transition(
        name = "NOT LIKE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLike(Field<String> field);

    /**
     * Create a condition to pattern-check this field against a field.
     * <p>
     * SQL: <code>this not like field escape 'e'</code>
     */
    @Support
    @Transition(
        name = "NOT LIKE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition notLike(Field<String> field, char escape);

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this not like value</code>
     */
    @Support
    @Transition(
        name = "NOT LIKE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLike(String value);

    /**
     * Create a condition to pattern-check this field against a value.
     * <p>
     * SQL: <code>this not like value escape 'e'</code>
     */
    @Support
    @Transition(
        name = "NOT LIKE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition notLike(String value, char escape);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a field.
     * <p>
     * This translates to <code>this not ilike field</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(field)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "NOT LIKE IGNORE CASE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLikeIgnoreCase(Field<String> field);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a field.
     * <p>
     * This translates to <code>this not ilike field</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(field)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "NOT LIKE IGNORE CASE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition notLikeIgnoreCase(Field<String> field, char escape);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this not ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(value)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "NOT LIKE IGNORE CASE",
        args = "Field",
        to = "LikePredicate"
    )
    Condition notLikeIgnoreCase(String value);

    /**
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this not ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(value)</code> in all other dialects.
     */
    @Support
    @Transition(
        name = "NOT LIKE IGNORE CASE",
        args = {
            "Field",
            "Character"
        },
        to = "LikePredicate"
    )
    Condition notLikeIgnoreCase(String value, char escape);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).contains(13)</code>
     * <p>
     * If you're using {@link SQLDialect#POSTGRES}, then you can use this method
     * also to express the "ARRAY contains" operator. For example: <code><pre>
     * // Use this expression
     * val(new Integer[] { 1, 2, 3 }).contains(new Integer[] { 1, 2 })
     *
     * // ... to render this SQL
     * ARRAY[1, 2, 3] @> ARRAY[1, 2]
     * </pre></code>
     * <p>
     * Note, this does not correspond to the Oracle Text <code>CONTAINS()</code>
     * function. Refer to {@link OracleDSL#contains(Field, String)} instead.
     *
     * @see DSL#escape(String, char)
     * @see #like(String, char)
     */
    @Support
    @Transition(
        name = "CONTAINS",
        args = "Field",
        to = "LikePredicate"
    )
    Condition contains(T value);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).contains(13)</code>
     * <p>
     * If you're using {@link SQLDialect#POSTGRES}, then you can use this method
     * also to express the "ARRAY contains" operator. For example: <code><pre>
     * // Use this expression
     * val(new Integer[] { 1, 2, 3 }).contains(new Integer[] { 1, 2 })
     *
     * // ... to render this SQL
     * ARRAY[1, 2, 3] @> ARRAY[1, 2]
     * </pre></code>
     * <p>
     * Note, this does not correspond to the Oracle Text <code>CONTAINS()</code>
     * function. Refer to {@link OracleDSL#contains(Field, String)} instead.
     *
     * @see DSL#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    @Transition(
        name = "CONTAINS",
        args = "Field",
        to = "LikePredicate"
    )
    Condition contains(Field<T> value);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWith(11)</code>
     *
     * @see DSL#escape(String, char)
     * @see #like(String, char)
     */
    @Support
    @Transition(
        name = "STARTS WITH",
        args = "Field",
        to = "LikePredicate"
    )
    Condition startsWith(T value);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWith(11)</code>
     *
     * @see DSL#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    @Transition(
        name = "STARTS WITH",
        args = "Field",
        to = "LikePredicate"
    )
    Condition startsWith(Field<T> value);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWith(33)</code>
     *
     * @see DSL#escape(String, char)
     * @see #like(String, char)
     */
    @Support
    @Transition(
        name = "ENDS WITH",
        args = "Field",
        to = "LikePredicate"
    )
    Condition endsWith(T value);

    /**
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWith(33)</code>
     *
     * @see DSL#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    @Transition(
        name = "ENDS WITH",
        args = "Field",
        to = "LikePredicate"
    )
    Condition endsWith(Field<T> value);

    // ------------------------------------------------------------------------
    // IN predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to check this field against several values.
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    @Transition(
        name = "IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition in(Collection<T> values);

    /**
     * Create a condition to check this field against several values.
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    @Transition(
        name = "IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition in(T... values);

    /**
     * Create a condition to check this field against several values.
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    @Transition(
        name = "IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition in(Field<?>... values);

    /**
     * Create a condition to check this field against a subquery.
     * <p>
     * Note that the subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors in the database, if not used
     * correctly.
     * <p>
     * SQL: <code>this in (select...)</code>
     */
    @Support
    @Transition(
        name = "IN",
        args = "Select",
        to = "InPredicate"
    )
    Condition in(Select<? extends Record1<T>> query);

    /**
     * Create a condition to check this field against several values.
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    @Transition(
        name = "NOT IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition notIn(Collection<T> values);

    /**
     * Create a condition to check this field against several values.
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    @Transition(
        name = "NOT IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition notIn(T... values);

    /**
     * Create a condition to check this field against several values.
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    @Transition(
        name = "NOT IN",
        args = "Field+",
        to = "InPredicate"
    )
    Condition notIn(Field<?>... values);

    /**
     * Create a condition to check this field against a subquery.
     * <p>
     * Note that the subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors in the database, if not used
     * correctly.
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (select...)</code>
     */
    @Support
    @Transition(
        name = "NOT IN",
        args = "Select",
        to = "InPredicate"
    )
    Condition notIn(Select<? extends Record1<T>> query);

    // ------------------------------------------------------------------------
    // BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition between(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition between(Field<T> minValue, Field<T> maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN SYMMETRIC",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition betweenSymmetric(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN SYMMETRIC",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition betweenSymmetric(Field<T> minValue, Field<T> maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition notBetween(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition notBetween(Field<T> minValue, Field<T> maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this not between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN SYMMETRIC",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition notBetweenSymmetric(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * This is the same as calling
     * <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * SQL: <code>this not between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN SYMMETRIC",
        args = {
            "Field",
            "Field"
        },
        to = "BetweenPredicate"
    )
    Condition notBetweenSymmetric(Field<T> minValue, Field<T> maxValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN",
        args = "Field"
    )
    BetweenAndStep<T> between(T minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN",
        args = "Field"
    )
    BetweenAndStep<T> between(Field<T> minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN SYMMETRIC",
        args = "Field"
    )
    BetweenAndStep<T> betweenSymmetric(T minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "BETWEEN SYMMETRIC",
        args = "Field"
    )
    BetweenAndStep<T> betweenSymmetric(Field<T> minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN",
        args = "Field"
    )
    BetweenAndStep<T> notBetween(T minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN",
        args = "Field"
    )
    BetweenAndStep<T> notBetween(Field<T> minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this not between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN SYMMETRIC",
        args = "Field"
    )
    BetweenAndStep<T> notBetweenSymmetric(T minValue);

    /**
     * Create a condition to check this field against some bounds.
     * <p>
     * SQL: <code>this not between symmetric minValue and maxValue</code>
     */
    @Support
    @Transition(
        name = "NOT BETWEEN SYMMETRIC",
        args = "Field"
    )
    BetweenAndStep<T> notBetweenSymmetric(Field<T> minValue);

    // ------------------------------------------------------------------------
    // Dynamic comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this field with a value using a dynamic comparator.
     *
     * @param comparator The comparator to use for comparing this field with a
     *            value
     * @param value The value to compare this field with
     * @return A comparison predicate
     */
    @Support
    Condition compare(Comparator comparator, T value);

    /**
     * Compare this field with another field using a dynamic comparator.
     *
     * @param comparator The comparator to use for comparing this field with
     *            another field
     * @param field The field to compare this field with
     * @return A comparison predicate
     */
    @Support
    Condition compare(Comparator comparator, Field<T> field);

    /**
     * Compare this field with a subselect using a dynamic comparator.
     * <p>
     * Consider {@link Comparator#supportsSubselect()} to assess whether a
     * comparator can be used with this method.
     *
     * @param comparator The comparator to use for comparing this field with a
     *            subselect
     * @param query The subselect to compare this field with
     * @return A comparison predicate
     */
    @Support
    Condition compare(Comparator comparator, Select<? extends Record1<T>> query);

    /**
     * Compare this field with a quantified subselect using a dynamic
     * comparator.
     * <p>
     * Consider {@link Comparator#supportsQuantifier()} to assess whether a
     * comparator can be used with this method.
     *
     * @param comparator The comparator to use for comparing this field with a
     *            quantified subselect
     * @param query The quantified subselect to compare this field with
     * @return A comparison predicate
     */
    @Support
    Condition compare(Comparator comparator, QuantifiedSelect<? extends Record1<T>> query);

    // ------------------------------------------------------------------------
    // Comparison predicates
    // ------------------------------------------------------------------------

    /**
     * <code>this = value</code>.
     */
    @Support
    @Transition(
        name = "EQUAL",
        args = "Field",
        to = "ComparisonPredicate"
    )
    Condition equal(T value);

    /**
     * <code>this = field</code>.
     */
    @Support
    @Transition(
        name = "EQUAL",
        args = "Field",
        to = "ComparisonPredicate"
    )
    Condition equal(Field<T> field);

    /**
     * <code>this = (Select<?> ...)</code>.
     */
    @Support
    @Transition(
        name = "EQUAL",
        args = "Select",
        to = "ComparisonPredicate"
    )
    Condition equal(Select<? extends Record1<T>> query);

    /**
     * <code>this = [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "EQUAL",
        args = "QuantifiedSelect",
        to = "ComparisonPredicate"
    )
    Condition equal(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this = value</code>.
     *
     * @see #equal(Object)
     */
    @Support
    @Transition(
        name = "EQ",
        args = "Field",
        to = "ComparisonPredicate"
    )
    Condition eq(T value);

    /**
     * <code>this = field</code>.
     *
     * @see #equal(Field)
     */
    @Support
    @Transition(
        name = "EQ",
        args = "Field",
        to = "ComparisonPredicate"
    )
    Condition eq(Field<T> field);

    /**
     * <code>this = (Select<?> ...)</code>.
     *
     * @see #equal(Select)
     */
    @Support
    @Transition(
        name = "EQ",
        args = "Select",
        to = "ComparisonPredicate"
    )
    Condition eq(Select<? extends Record1<T>> query);

    /**
     * <code>this = [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    @Transition(
        name = "EQ",
        args = "QuantifiedSelect",
        to = "ComparisonPredicate"
    )
    Condition eq(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this != value</code>.
     */
    @Support
    Condition notEqual(T value);

    /**
     * <code>this != field</code>.
     */
    @Support
    Condition notEqual(Field<T> field);

    /**
     * <code>this != (Select<?> ...)</code>.
     */
    @Support
    Condition notEqual(Select<? extends Record1<T>> query);

    /**
     * <code>this != [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqual(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this != value</code>.
     *
     * @see #notEqual(Object)
     */
    @Support
    Condition ne(T value);

    /**
     * <code>this != field</code>.
     *
     * @see #notEqual(Field)
     */
    @Support
    @Transition(
        name = "NE",
        args = "Field",
        to = "ComparisonPredicate"
    )
    Condition ne(Field<T> field);

    /**
     * <code>this != (Select<?> ...)</code>.
     *
     * @see #notEqual(Select)
     */
    @Support
    Condition ne(Select<? extends Record1<T>> query);

    /**
     * <code>this != [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition ne(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this < value</code>.
     */
    @Support
    Condition lessThan(T value);

    /**
     * <code>this < field</code>.
     */
    @Support
    Condition lessThan(Field<T> field);

    /**
     * <code>this < (Select<?> ...)</code>.
     */
    @Support
    Condition lessThan(Select<? extends Record1<T>> query);

    /**
     * <code>this < [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThan(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this < value</code>.
     *
     * @see #lessThan(Object)
     */
    @Support
    Condition lt(T value);

    /**
     * <code>this < field</code>.
     *
     * @see #lessThan(Field)
     */
    @Support
    Condition lt(Field<T> field);

    /**
     * <code>this < (Select<?> ...)</code>.
     *
     * @see #lessThan(Select)
     */
    @Support
    Condition lt(Select<? extends Record1<T>> query);

    /**
     * <code>this < [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lt(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this <= value</code>.
     */
    @Support
    Condition lessOrEqual(T value);

    /**
     * <code>this <= field</code>.
     */
    @Support
    Condition lessOrEqual(Field<T> field);

    /**
     * <code>this <= (Select<?> ...)</code>.
     */
    @Support
    Condition lessOrEqual(Select<? extends Record1<T>> query);

    /**
     * <code>this <= [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqual(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this <= value</code>.
     *
     * @see #lessOrEqual(Object)
     */
    @Support
    Condition le(T value);

    /**
     * <code>this <= field</code>.
     *
     * @see #lessOrEqual(Field)
     */
    @Support
    Condition le(Field<T> field);

    /**
     * <code>this <= (Select<?> ...)</code>.
     *
     * @see #lessOrEqual(Select)
     */
    @Support
    Condition le(Select<? extends Record1<T>> query);

    /**
     * <code>this <= [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition le(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this > value</code>.
     */
    @Support
    Condition greaterThan(T value);

    /**
     * <code>this > field</code>.
     */
    @Support
    Condition greaterThan(Field<T> field);

    /**
     * <code>this > (Select<?> ...)</code>.
     */
    @Support
    Condition greaterThan(Select<? extends Record1<T>> query);

    /**
     * <code>this > [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThan(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this > value</code>.
     *
     * @see #greaterThan(Object)
     */
    @Support
    Condition gt(T value);

    /**
     * <code>this > field</code>.
     *
     * @see #greaterThan(Field)
     */
    @Support
    Condition gt(Field<T> field);

    /**
     * <code>this > (Select<?> ...)</code>.
     *
     * @see #greaterThan(Select)
     */
    @Support
    Condition gt(Select<? extends Record1<T>> query);

    /**
     * <code>this > [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition gt(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this >= value</code>.
     */
    @Support
    Condition greaterOrEqual(T value);

    /**
     * <code>this >= field</code>.
     */
    @Support
    Condition greaterOrEqual(Field<T> field);

    /**
     * <code>this >= (Select<?> ...)</code>.
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record1<T>> query);

    /**
     * <code>this >= [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqual(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * <code>this >= value</code>.
     *
     * @see #greaterOrEqual(Object)
     */
    @Support
    Condition ge(T value);

    /**
     * <code>this >= field</code>.
     *
     * @see #greaterOrEqual(Field)
     */
    @Support
    Condition ge(Field<T> field);

    /**
     * <code>this >= (Select<?> ...)</code>.
     *
     * @see #greaterOrEqual(Select)
     */
    @Support
    Condition ge(Select<? extends Record1<T>> query);

    /**
     * <code>this >= [quantifier] (Select<?> ...)</code>.
     *
     * @see DSL#all(Field)
     * @see DSL#all(Select)
     * @see DSL#all(Object...)
     * @see DSL#any(Field)
     * @see DSL#any(Select)
     * @see DSL#any(Object...)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition ge(QuantifiedSelect<? extends Record1<T>> query);

    /**
     * Create a condition to check this field against known string literals for
     * <code>true</code>.
     * <p>
     * SQL:
     * <code>lcase(this) in ("1", "y", "yes", "true", "on", "enabled")</code>
     */
    @Support
    Condition isTrue();

    /**
     * Create a condition to check this field against known string literals for
     * <code>false</code>.
     * <p>
     * SQL:
     * <code>lcase(this) in ("0", "n", "no", "false", "off", "disabled")</code>
     */
    @Support
    Condition isFalse();

    /**
     * <code>lower(this) = lower(value)</code>.
     */
    @Support
    Condition equalIgnoreCase(String value);

    /**
     * <code>lower(this) = lower(value)</code>.
     */
    @Support
    Condition equalIgnoreCase(Field<String> value);

    /**
     * <code>lower(this) != lower(value)</code>.
     */
    @Support
    Condition notEqualIgnoreCase(String value);

    /**
     * <code>lower(this) != lower(value)</code>.
     */
    @Support
    Condition notEqualIgnoreCase(Field<String> value);

    // ------------------------------------------------------------------------
    // Pre-2.0 API. This API is maintained for backwards-compatibility. It will
    // be removed in the future. Consider using equivalent methods from
    // org.jooq.impl.DSL
    // ------------------------------------------------------------------------

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sign(Field)
     */
    @Support
    Field<Integer> sign();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#abs(Field)
     */
    @Support
    Field<T> abs();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#round(Field)
     */
    @Support
    Field<T> round();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#round(Field, int)
     */
    @Support
    Field<T> round(int decimals);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#floor(Field)
     */
    @Support
    Field<T> floor();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#ceil(Field)
     */
    @Support
    Field<T> ceil();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sqrt(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sqrt();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#exp(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> exp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#ln(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> ln();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#log(Field, int)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> log(int base);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#power(Field, Number)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> pow(Number exponent);

    /**
     * An alias for {@link #power(Number)}.
     *
     * @see #power(Number)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> power(Number exponent);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#acos(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> acos();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#asin(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> asin();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#atan(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#atan2(Field, Number)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan2(Number y);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#atan2(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan2(Field<? extends Number> y);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#cos(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cos();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sin(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sin();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#tan(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> tan();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#cot(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cot();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sinh(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sinh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#cosh(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cosh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#tanh(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> tanh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#coth(Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> coth();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#deg(Field)
     */
    @Support
    Field<BigDecimal> deg();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rad(Field)
     */
    @Support
    Field<BigDecimal> rad();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#count(Field)
     */
    @Support
    Field<Integer> count();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#countDistinct(Field)
     */
    @Support
    Field<Integer> countDistinct();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#max(Field)
     */
    @Support
    Field<T> max();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#min(Field)
     */
    @Support
    Field<T> min();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sum(Field)
     */
    @Support
    Field<BigDecimal> sum();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#avg(Field)
     */
    @Support
    Field<BigDecimal> avg();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#median(Field)
     */
    @Support({ HSQLDB, ORACLE, SYBASE })
    Field<BigDecimal> median();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#stddevPop(Field)
     */
    @Support({ ASE, CUBRID, DB2, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> stddevPop();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#stddevSamp(Field)
     */
    @Support({ ASE, CUBRID, DB2, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> stddevSamp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#varPop(Field)
     */
    @Support({ ASE, CUBRID, DB2, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> varPop();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#varSamp(Field)
     */
    @Support({ ASE, CUBRID, DB2, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> varSamp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#count(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<Integer> countOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#max(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<T> maxOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#min(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<T> minOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#sum(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> sumOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#avg(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> avgOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#firstValue(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowIgnoreNullsStep<T> firstValue();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lastValue(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowIgnoreNullsStep<T> lastValue();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lead(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lead();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lead(Field, int)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lead(int offset);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lead(Field, int, Object)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lead(int offset, T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lead(Field, int, Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lead(int offset, Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lag(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lag();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lag(Field, int)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lag(int offset);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lag(Field, int, Object)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lag(int offset, T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lag(Field, int, Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER })
    WindowIgnoreNullsStep<T> lag(int offset, Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#stddevPop(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> stddevPopOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#stddevSamp(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> stddevSampOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#varPop(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> varPopOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#varSamp(Field)
     * @see AggregateFunction#over()
     */
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> varSampOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#upper(Field)
     */
    @Support
    @Transition(
        name = "UPPER",
        to = "StringFunction"
    )
    Field<String> upper();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lower(Field)
     */
    @Support
    @Transition(
        name = "LOWER",
        to = "StringFunction"
    )
    Field<String> lower();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#trim(Field)
     */
    @Support
    Field<String> trim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rtrim(Field)
     */
    @Support
    Field<String> rtrim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#ltrim(Field)
     */
    @Support
    Field<String> ltrim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rpad(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rpad(Field, int)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rpad(Field, Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(Field<? extends Number> length, Field<String> character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#rpad(Field, int, char)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(int length, char character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lpad(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lpad(Field, int)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lpad(Field, Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(Field<? extends Number> length, Field<String> character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#lpad(Field, int, char)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(int length, char character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#repeat(Field, int)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> repeat(Number count);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#repeat(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> repeat(Field<? extends Number> count);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#replace(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(Field<String> search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#replace(Field, String)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(String search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#replace(Field, Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(Field<String> search, Field<String> replace);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#replace(Field, String, String)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(String search, String replace);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#position(Field, String)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> position(String search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#position(Field, Field)
     */
    @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> position(Field<String> search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#ascii(Field)
     */
    @Support({ ASE, CUBRID, DB2, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> ascii();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#concat(Field...)
     */
    @Support
    Field<String> concat(Field<?>... fields);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#concat(String...)
     */
    @Support
    Field<String> concat(String... values);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#substring(Field, int)
     */
    @Support
    Field<String> substring(int startingPosition);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#substring(Field, Field)
     */
    @Support
    Field<String> substring(Field<? extends Number> startingPosition);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#substring(Field, int, int)
     */
    @Support
    Field<String> substring(int startingPosition, int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#substring(Field, Field, Field)
     */
    @Support
    Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#length(Field)
     */
    @Support
    Field<Integer> length();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#charLength(Field)
     */
    @Support
    Field<Integer> charLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#bitLength(Field)
     */
    @Support
    Field<Integer> bitLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#octetLength(Field)
     */
    @Support
    Field<Integer> octetLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#extract(Field, DatePart)
     */
    @Support
    Field<Integer> extract(DatePart datePart);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#greatest(Field, Field...)
     */
    @Support
    Field<T> greatest(T... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#greatest(Field, Field...)
     */
    @Support
    Field<T> greatest(Field<?>... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#least(Field, Field...)
     */
    @Support
    Field<T> least(T... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#least(Field, Field...)
     */
    @Support
    Field<T> least(Field<?>... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nvl(Field, Object)
     */
    @Support
    Field<T> nvl(T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nvl(Field, Field)
     */
    @Support
    Field<T> nvl(Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nvl2(Field, Object, Object)
     */
    @Support
    <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nvl2(Field, Field, Field)
     */
    @Support
    <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nullif(Field, Object)
     */
    @Support
    Field<T> nullif(T other);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#nullif(Field, Field)
     */
    @Support
    Field<T> nullif(Field<T> other);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#decode(Object, Object, Object)
     */
    @Support
    <Z> Field<Z> decode(T search, Z result);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#decode(Object, Object, Object, Object...)
     */
    @Support
    <Z> Field<Z> decode(T search, Z result, Object... more);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#decode(Field, Field, Field)
     */
    @Support
    <Z> Field<Z> decode(Field<T> search, Field<Z> result);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#decode(Field, Field, Field, Field...)
     */
    @Support
    <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#coalesce(Object, Object...)
     */
    @Support
    Field<T> coalesce(T option, T... options);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link DSLContext}
     *
     * @see DSL#coalesce(Field, Field...)
     */
    @Support
    Field<T> coalesce(Field<T> option, Field<?>... options);

}
