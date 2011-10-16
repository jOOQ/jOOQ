/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import org.jooq.impl.Factory;

/**
 * A field used in tables and conditions
 *
 * @param <T> The field type
 * @author Lukas Eder
 */
public interface Field<T> extends NamedTypeProviderQueryPart<T>, AliasProvider<Field<T>> {

    // ------------------------------------------------------------------------
    // API
    // ------------------------------------------------------------------------

    /**
     * The name of the field.
     * <p>
     * The name is any of these:
     * <ul>
     * <li>The formal name of the field, if it is a <i>physical table/view field</i></li>
     * <li>The alias of an <i>aliased field</i></li>
     * <li>A generated / unspecified value for any other <i>expression</i></li>
     * </ul>
     */
    @Override
    String getName();

    /**
     * The Java type of the field.
     */
    @Override
    Class<? extends T> getType();

    /**
     * Create an alias for this field
     *
     * @param alias The alias name
     * @return The field alias
     */
    @Override
    Field<T> as(String alias);

    /**
     * Watch out! This is {@link Object#equals(Object)}, not a jOOQ feature! :-)
     */
    @Override
    boolean equals(Object other);

    /**
     * Whether this field represents a <code>null</code> literal.
     * <p>
     * This method is for JOOQ INTERNAL USE only!
     * <p>
     * This method was added to be able to recognise <code>null</code> literals
     * within jOOQ and handle them specially, as some SQL dialects have a rather
     * un-intuitive way of handling <code>null</code> values.
     */
    boolean isNullLiteral();

    // ------------------------------------------------------------------------
    // Type casts
    // ------------------------------------------------------------------------

    /**
     * Cast this field to the type of another field.
     * <p>
     * This results in the same as casting this field to
     * {@link Field#getCastTypeName()}
     *
     * @param <Z> The generic type of the cast field
     * @param field The field whose type is used for the cast
     * @return The cast field
     * @see #cast(DataType)
     */
    <Z> Field<Z> cast(Field<Z> field);

    /**
     * Cast this field to a dialect-specific data type.
     *
     * @param <Z> The generic type of the cast field
     * @param type
     */
    <Z> Field<Z> cast(DataType<Z> type);

    /**
     * Cast this field to another type
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
    <Z> Field<Z> cast(Class<? extends Z> type);

    // ------------------------------------------------------------------------
    // Conversion of field into a sort field
    // ------------------------------------------------------------------------

    /**
     * Create an ascending sort field from this field
     *
     * @return This field as an ascending sort field
     */
    SortField<T> asc();

    /**
     * Create a descending sort field from this field
     *
     * @return This field as a descending sort field
     */
    SortField<T> desc();

    /**
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
    SortField<Integer> sortAsc(Collection<T> sortList);

    /**
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
    SortField<Integer> sortAsc(T... sortList);

    /**
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
    SortField<Integer> sortDesc(Collection<T> sortList);

    /**
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
    SortField<Integer> sortDesc(T... sortList);

    /**
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
    <Z> SortField<Z> sort(Map<T, Z> sortMap);

    // ------------------------------------------------------------------------
    // Arithmetic expressions
    // ------------------------------------------------------------------------

    /**
     * Negate this field to get its negative value.
     * <p>
     * This renders the same on all dialects:
     * <code><pre>-[this]</pre></code>
     */
    Field<T> neg();

    /**
     * An arithmetic expression adding this to value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the value is added arithmetically</li>
     * <li>If this is a date time field, then [value] days are added to this date</li>
     * </ul>
     */
    Field<T> add(Number value);

    /**
     * An arithmetic expression adding this to value
     */
    Field<T> add(Field<? extends Number> value);

    /**
     * An arithmetic expression subtracting value from this.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the value is subtracted
     * arithmetically</li>
     * <li>If this is a date time field, then [value] days are subtracted to
     * this date</li>
     * </ul>
     */
    Field<T> sub(Number value);

    /**
     * An arithmetic expression subtracting value from this
     */
    Field<T> sub(Field<? extends Number> value);

    /**
     * An arithmetic expression multiplying this with value
     */
    Field<T> mul(Number value);

    /**
     * An arithmetic expression multiplying this with value
     */
    Field<T> mul(Field<? extends Number> value);

    /**
     * An arithmetic expression dividing this by value
     */
    Field<T> div(Number value);

    /**
     * An arithmetic expression dividing this by value
     */
    Field<T> div(Field<? extends Number> value);

    /**
     * An arithmetic expression getting the modulo of this divided by value
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code>
     * ... or the modulo function elsewhere:
     * <code><pre>mod([this], [value])</pre></code>
     */
    Field<T> mod(Number value);

    /**
     * An arithmetic expression getting the modulo of this divided by value
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code>
     * ... or the modulo function elsewhere:
     * <code><pre>mod([this], [value])</pre></code>
     */
    Field<T> mod(Field<? extends Number> value);

    // ------------------------------------------------------------------------
    // Mathematical functions created from this field
    // ------------------------------------------------------------------------

    /**
     * Get the sign of a numeric field: sign(field)
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([this])</pre></code>
     * ... or simulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] > 0 THEN 1
     *      WHEN [this] < 0 THEN -1
     *      ELSE 0
     * END
     */
    Field<Integer> sign();

    /**
     * Get the absolute value of a numeric field: abs(field)
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([this])</pre></code>
     */
    Field<T> abs();

    /**
     * Get rounded value of a numeric field: round(field)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this]) or
     * round([this], 0)</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    Field<T> round();

    /**
     * Get rounded value of a numeric field: round(field, decimals)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this], [decimals])</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    Field<T> round(int decimals);

    /**
     * Get the largest integer value not greater than [this]
     * <p>
     * This renders the floor function where available:
     * <code><pre>floor([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] - 0.499999999999999)</pre></code>
     */
    Field<T> floor();

    /**
     * Get the smallest integer value not less than [this]
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([this]) or
     * ceiling([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] + 0.499999999999999)</pre></code>
     */
    Field<T> ceil();

    /**
     * Get the sqrt(field) function
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([this])</pre></code> ... or simulates it elsewhere using
     * power (which in turn may also be simulated using ln and exp functions):
     * <code><pre>power([this], 0.5)</pre></code>
     */
    Field<BigDecimal> sqrt();

    /**
     * Get the exp(field) function, taking this field as the power of e
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([this])</pre></code>
     */
    Field<BigDecimal> exp();

    /**
     * Get the ln(field) function, taking the natural logarithm of this field
     * <p>
     * This renders the ln or log function where available:
     * <code><pre>ln([this]) or
     * log([this])</pre></code>
     */
    Field<BigDecimal> ln();

    /**
     * Get the log(field, base) function
     * <p>
     * This renders the log function where available:
     * <code><pre>log([this])</pre></code> ... or simulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([this]) / ln([base])</pre></code>
     */
    Field<BigDecimal> log(int base);

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([this], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([this]) * [exponent])</pre></code>
     */
    Field<BigDecimal> power(Number exponent);

    /**
     * Get the arc cosine(field) function
     * <p>
     * This renders the acos function where available:
     * <code><pre>acos([this])</pre></code>
     */
    Field<BigDecimal> acos();

    /**
     * Get the arc sine(field) function
     * <p>
     * This renders the asin function where available:
     * <code><pre>asin([this])</pre></code>
     */
    Field<BigDecimal> asin();

    /**
     * Get the arc tangent(field) function
     * <p>
     * This renders the atan function where available:
     * <code><pre>atan([this])</pre></code>
     */
    Field<BigDecimal> atan();

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    Field<BigDecimal> atan2(Number y);

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    Field<BigDecimal> atan2(Field<? extends Number> y);

    /**
     * Get the cosine(field) function
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([this])</pre></code>
     */
    Field<BigDecimal> cos();

    /**
     * Get the sine(field) function
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([this])</pre></code>
     */
    Field<BigDecimal> sin();

    /**
     * Get the tangent(field) function
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([this])</pre></code>
     */
    Field<BigDecimal> tan();

    /**
     * Get the cotangent(field) function
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([this])</pre></code> ... or simulates it elsewhere using
     * sin and cos: <code><pre>cos([this]) / sin([this])</pre></code>
     */
    Field<BigDecimal> cot();

    /**
     * Get the hyperbolic sine function: sinh(field)
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2))</pre></code>
     */
    Field<BigDecimal> sinh();

    /**
     * Get the hyperbolic cosine function: cosh(field)
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2))</pre></code>
     */
    Field<BigDecimal> cosh();

    /**
     * Get the hyperbolic tangent function: tanh(field)
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([this])</pre></code> ... or simulates it elsewhere using
     * exp:
     * <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2) + 1)</pre></code>
     */
    Field<BigDecimal> tanh();

    /**
     * Get the hyperbolic cotangent function: coth(field)
     * <p>
     * This is not supported by any RDBMS, but simulated using exp exp:
     * <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2) - 1)</pre></code>
     */
    Field<BigDecimal> coth();

    /**
     * Calculate degrees from radians from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * 180 / PI</pre></code>
     */
    Field<BigDecimal> deg();

    /**
     * Calculate radians from degrees from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * PI / 180</pre></code>
     */
    Field<BigDecimal> rad();

    // ------------------------------------------------------------------------
    // Aggregate functions created from this field
    // ------------------------------------------------------------------------

    /**
     * Get the count(field) function
     *
     * @see Factory#count()
     */
    Field<Integer> count();

    /**
     * Get the count(distinct field) function
     *
     * @see Factory#count()
     */
    Field<Integer> countDistinct();

    /**
     * Get the max value over a field: max(field)
     */
    Field<T> max();

    /**
     * Get the min value over a field: min(field)
     */
    Field<T> min();

    /**
     * Get the sum over a numeric field: sum(field)
     */
    Field<BigDecimal> sum();

    /**
     * Get the average over a numeric field: avg(field)
     */
    Field<BigDecimal> avg();

    /**
     * Get the median over a numeric field: median(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>HSQLDB</li>
     * <li>Oracle</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    Field<BigDecimal> median();

    /**
     * Get the population standard deviation of a numeric field: stddev_pop(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    Field<BigDecimal> stddevPop();

    /**
     * Get the sample standard deviation of a numeric field: stddev_samp(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    Field<BigDecimal> stddevSamp();

    /**
     * Get the population variance of a numeric field: var_pop(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    Field<BigDecimal> varPop();

    /**
     * Get the sample variance of a numeric field: var_samp(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (var)</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    Field<BigDecimal> varSamp();

    // ------------------------------------------------------------------------
    // Analytic (or window) functions created from this field
    // ------------------------------------------------------------------------

    /**
     * The <code>count(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<Integer> countOver();

    /**
     * The <code>max(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<T> maxOver();

    /**
     * The <code>min(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<T> minOver();

    /**
     * The <code>sum(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> sumOver();

    /**
     * The <code>avg(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> avgOver();

    /**
     * The <code>first_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> firstValue();

    /**
     * The <code>last_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lastValue();

    /**
     * The <code>lead(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lead();

    /**
     * The <code>lead(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lead(int offset);

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lead(int offset, T defaultValue);

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lead(int offset, Field<T> defaultValue);

    /**
     * The <code>lag(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lag();

    /**
     * The <code>lag(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lag(int offset);

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lag(int offset, T defaultValue);

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowIgnoreNullsStep<T> lag(int offset, Field<T> defaultValue);

    /**
     * The <code>stddev_pop(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> stddevPopOver();

    /**
     * The <code>stddev_samp(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> stddevSampOver();

    /**
     * The <code>var_pop(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> varPopOver();

    /**
     * The <code>var_samp(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    WindowPartitionByStep<BigDecimal> varSampOver();

    // ------------------------------------------------------------------------
    // String functions created from this field
    // ------------------------------------------------------------------------

    /**
     * Get the upper(field) function
     * <p>
     * This renders the upper function in all dialects:
     * <code><pre>upper([this])</pre></code>
     */
    Field<String> upper();

    /**
     * Get the lower(field) function
     * <p>
     * This renders the lower function in all dialects:
     * <code><pre>lower([this])</pre></code>
     */
    Field<String> lower();

    /**
     * Get the trim(field) function
     * <p>
     * This renders the trim function where available:
     * <code><pre>trim([this])</pre></code> ... or simulates it elsewhere using
     * rtrim and ltrim: <code><pre>ltrim(rtrim([this]))</pre></code>
     */
    Field<String> trim();

    /**
     * Get the rtrim(field) function
     * <p>
     * This renders the rtrim function in all dialects:
     * <code><pre>rtrim([this])</pre></code>
     */
    Field<String> rtrim();

    /**
     * Get the ltrim(field) function
     * <p>
     * This renders the ltrim function in all dialects:
     * <code><pre>ltrim([this])</pre></code>
     */
    Field<String> ltrim();

    /**
     * Get the rpad(field, length) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([this], repeat(' ', [length] - length([this])))</pre></code>
     */
    Field<String> rpad(Field<? extends Number> length);

    /**
     * Get the rpad(field, length) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([this], repeat(' ', [length] - length([this])))</pre></code>
     */
    Field<String> rpad(int length);

    /**
     * Get the rpad(field, length, character) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([this], repeat([character], [length] - length([this])))</pre></code>
     */
    Field<String> rpad(Field<? extends Number> length, Field<String> character);

    /**
     * Get the rpad(field, length, character) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([this], repeat([character], [length] - length([this])))</pre></code>
     */
    Field<String> rpad(int length, char character);

    /**
     * Get the lpad(field, length) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat(' ', [length] - length([this])), [this])</pre></code>
     */
    Field<String> lpad(Field<? extends Number> length);

    /**
     * Get the lpad(field, length) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat(' ', [length] - length([this])), [this])</pre></code>
     */
    Field<String> lpad(int length);

    /**
     * Get the lpad(field, length, character) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat([character], [length] - length([this])), [this])</pre></code>
     */
    Field<String> lpad(Field<? extends Number> length, Field<String> character);

    /**
     * Get the lpad(field, length, character) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([this], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat([character], [length] - length([this])), [this])</pre></code>
     */
    Field<String> lpad(int length, char character);

    /**
     * Get the repeat(count) function
     * <p>
     * This renders the repeat or replicate function where available:
     * <code><pre>repeat([this], [count]) or
     * replicate([this], [count])</pre></code> ... or simulates it elsewhere
     * using rpad and length, which may be simulated as well, depending on the
     * RDBMS:
     * <code><pre>rpad([this], length([this]) * [count], [this])</pre></code>
     */
    Field<String> repeat(Number count);

    /**
     * Get the repeat(field, count) function
     * <p>
     * This renders the repeat or replicate function where available:
     * <code><pre>repeat([this], [count]) or
     * replicate([this], [count])</pre></code> ... or simulates it elsewhere
     * using rpad and length, which may be simulated as well, depending on the
     * RDBMS:
     * <code><pre>rpad([this], length([this]) * [count], [this])</pre></code>
     */
    Field<String> repeat(Field<? extends Number> count);

    /**
     * Get the replace(in, search) function
     * <p>
     * This renders the replace or str_replace function where available:
     * <code><pre>replace([this], [search]) or
     * str_replace([this], [search])</pre></code> ... or simulates it elsewhere
     * using the three-argument replace function:
     * <code><pre>replace([this], [search], '')</pre></code>
     */
    Field<String> replace(Field<String> search);

    /**
     * Get the replace(in, search) function
     * <p>
     * This renders the replace or str_replace function where available:
     * <code><pre>replace([this], [search]) or
     * str_replace([this], [search])</pre></code> ... or simulates it elsewhere
     * using the three-argument replace function:
     * <code><pre>replace([this], [search], '')</pre></code>
     */
    Field<String> replace(String search);

    /**
     * Get the replace(in, search, replace) function
     * <p>
     * This renders the replace or str_replace function:
     * <code><pre>replace([this], [search]) or
     * str_replace([this], [search])</pre></code>
     */
    Field<String> replace(Field<String> search, Field<String> replace);

    /**
     * Get the replace(in, search, replace) function
     * <p>
     * This renders the replace or str_replace function:
     * <code><pre>replace([this], [search]) or
     * str_replace([this], [search])</pre></code>
     */
    Field<String> replace(String search, String replace);

    /**
     * Get the position(in, search) function
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [this]) or
     * locate([this], [search]) or
     * locate([search], [this]) or
     * instr([this], [search]) or
     * charindex([search], [this])</pre></code>
     */
    Field<Integer> position(String search);

    /**
     * Get the position(in, search) function
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [this]) or
     * locate([this], [search]) or
     * locate([search], [this]) or
     * instr([this], [search]) or
     * charindex([search], [this])</pre></code>
     */
    Field<Integer> position(Field<String> search);

    /**
     * Get the ascii(field) function
     * <p>
     * This renders the replace or str_replace function:
     * <code><pre>ascii([this])</pre></code>
     */
    Field<Integer> ascii();

    /**
     * Get the concat(field[, field, ...]) function
     * <p>
     * This creates <code>this || fields[0] || fields[1] || ...</code> as an
     * expression, or <code>concat(this, fields[0], fields[1], ...)</code>,
     * depending on the dialect.
     * <p>
     * If any of the given fields is not a {@link String} field, they are cast
     * to <code>Field&lt;String&gt;</code> first using {@link #cast(Class)}
     */
    Field<String> concat(Field<?>... fields);

    /**
     * Get the concat(value[, value, ...]) function
     * <p>
     * This creates <code>this || fields[0] || fields[1] || ...</code> as an
     * expression, or <code>concat(this, fields[0], fields[1], ...)</code>,
     * depending on the dialect.
     */
    Field<String> concat(String... values);

    /**
     * Get the substring(field, startingPosition) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([this], [startingPosition]) or
     * substring([this], [startingPosition])</pre></code>
     */
    Field<String> substring(int startingPosition);

    /**
     * Get the substring(field, startingPosition) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([this], [startingPosition]) or
     * substring([this], [startingPosition])</pre></code>
     */
    Field<String> substring(Field<? extends Number> startingPosition);

    /**
     * Get the substring(field, startingPosition, length) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([this], [startingPosition], [length]) or
     * substring([this], [startingPosition], [length])</pre></code>
     */
    Field<String> substring(int startingPosition, int length);

    /**
     * Get the substring(field, startingPosition, length) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([this], [startingPosition], [length]) or
     * substring([this], [startingPosition], [length])</pre></code>
     */
    Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length);

    /**
     * Get the length of a <code>VARCHAR</code> type. This is a synonym for
     * {@link #charLength()}
     *
     * @see #charLength()
     */
    Field<Integer> length();

    /**
     * Get the char_length(field) function
     * <p>
     * This translates into any dialect
     */
    Field<Integer> charLength();

    /**
     * Get the bit_length(field) function
     * <p>
     * This translates into any dialect
     */
    Field<Integer> bitLength();

    /**
     * Get the octet_length(field) function
     * <p>
     * This translates into any dialect
     */
    Field<Integer> octetLength();

    /**
     * Get the extract(field, datePart) function
     * <p>
     * This translates into any dialect
     */
    Field<Integer> extract(DatePart datePart);

    // ------------------------------------------------------------------------
    // General functions created from this field
    // ------------------------------------------------------------------------

    /**
     * Find the greatest among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #greatest(Field...)
     */
    Field<T> greatest(T... others);

    /**
     * Find the greatest among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    Field<T> greatest(Field<?>... others);

    /**
     * Find the least among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #least(Field...)
     */
    Field<T> least(T... others);

    /**
     * Find the least among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    Field<T> least(Field<?>... others);

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     *
     * @see #nvl(Field)
     */
    Field<T> nvl(T defaultValue);

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     * <p>
     * Returns the dialect's equivalent to NVL:
     * <ul>
     * <li>DB2 <a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=/com.ibm.db2.luw.sql.ref.doc/doc/r0052627.html"
     * >NVL</a></li>
     * <li>Derby <a
     * href="http://db.apache.org/derby/docs/10.7/ref/rreffunccoalesce.html"
     * >COALESCE</a></li>
     * <li>H2 <a
     * href="http://www.h2database.com/html/functions.html#ifnull">IFNULL</a></li>
     * <li>HSQLDB <a
     * href="http://hsqldb.org/doc/2.0/guide/builtinfunctions-chapt.html"
     * >NVL</a></li>
     * <li>MySQL <a href=
     * "http://dev.mysql.com/doc/refman/5.0/en/control-flow-functions.html"
     * >IFNULL</a></li>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl.php">NVL</a></li>
     * <li>Postgres <a href=
     * "http://www.postgresql.org/docs/8.1/static/functions-conditional.html"
     * >COALESCE</a></li>
     * <li>SQLite <a
     * href="http://www.sqlite.org/lang_corefunc.html#ifnull">IFNULL</a></li>
     * </ul>
     */
    Field<T> nvl(Field<T> defaultValue);

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     *
     * @see #nvl2(Field, Field)
     */
    <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull);

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     * <p>
     * Returns the dialect's equivalent to NVL2:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl2.php">NVL2</a></li>
     * </ul>
     * <p>
     * Other dialects:
     * <code>CASE WHEN [value] IS NULL THEN [valueIfNull] ELSE [valueIfNotNull] END</code>
     */
    <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull);

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     * <p>
     * @see #nullif(Field)
     */
    Field<T> nullif(T other);

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     * <p>
     * Returns the dialect's equivalent to NULLIF:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nullif.php">NULLIF</a></li>
     * </ul>
     * <p>
     */
    Field<T> nullif(Field<T> other);

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see #decode(Field, Field, Field[])
     */
    <Z> Field<Z> decode(T search, Z result);

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see #decode(Field, Field, Field[])
     */
    <Z> Field<Z> decode(T search, Z result, Object... more);

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see #decode(Field, Field, Field[])
     */
    <Z> Field<Z> decode(Field<T> search, Field<Z> result);

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     * <p>
     * Returns the dialect's equivalent to DECODE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/decode.php">DECODE</a></li>
     * </ul>
     * <p>
     * Other dialects: <code><pre>
     * CASE WHEN [this = search] THEN [result],
     *     [WHEN more...         THEN more...]
     *     [ELSE more...]
     * END
     * </pre></code>
     *
     * @param search the mandatory first search parameter
     * @param result the mandatory first result parameter
     * @param more the optional parameters. If <code>more.length</code> is even,
     *            then it is assumed that it contains more search/result pairs.
     *            If <code>more.length</code> is odd, then it is assumed that it
     *            contains more search/result pairs plus a default at the end.
     */
    <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more);

    /**
     * Gets the Oracle-style <code>COALESCE(expr1, expr2, ... , exprn)</code>
     * function
     *
     * @see #coalesce(Field, Field...)
     */
    Field<T> coalesce(T option, T... options);

    /**
     * Gets the Oracle-style <code>COALESCE(expr1, expr2, ... , exprn)</code>
     * function
     * <p>
     * Returns the dialect's equivalent to COALESCE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/coalesce.php">COALESCE</a>
     * </li>
     * </ul>
     */
    Field<T> coalesce(Field<T> option, Field<?>... options);

    // ------------------------------------------------------------------------
    // Conditions created from this field
    // ------------------------------------------------------------------------

    /**
     * <code>this is null</code>
     */
    Condition isNull();

    /**
     * <code>this is not null</code>
     */
    Condition isNotNull();

    /**
     * <code>lcase(this) in ("1", "y", "yes", "true", "on", "enabled")</code>
     */
    Condition isTrue();

    /**
     * <code>lcase(this) in ("0", "n", "no", "false", "off", "disabled")</code>
     */
    Condition isFalse();

    /**
     * <code>this like value</code>
     */
    Condition like(T value);

    /**
     * <code>this not like value</code>
     */
    Condition notLike(T value);

    /**
     * <code>this in (values...)</code>
     */
    Condition in(Collection<T> values);

    /**
     * <code>this in (values...)</code>
     */
    Condition in(T... values);

    /**
     * <code>this in (values...)</code>
     */
    Condition in(Field<?>... values);

    /**
     * <code>this in (select...)</code>
     */
    Condition in(Select<?> query);

    /**
     * <code>this not in (values...)</code>
     */
    Condition notIn(Collection<T> values);

    /**
     * <code>this not in (values...)</code>
     */
    Condition notIn(T... values);

    /**
     * <code>this not in (values...)</code>
     */
    Condition notIn(Field<?>... values);

    /**
     * <code>this not in (select...)</code>
     */
    Condition notIn(Select<?> query);

    /**
     * <code>this between minValue and maxValue</code>
     */
    Condition between(T minValue, T maxValue);

    /**
     * <code>this between minValue and maxValue</code>
     */
    Condition between(Field<T> minValue, Field<T> maxValue);

    /**
     * <code>this = value</code>
     */
    Condition equal(T value);

    /**
     * <code>this = field</code>
     */
    Condition equal(Field<T> field);

    /**
     * <code>this = (Select<?> ...)</code>
     */
    Condition equal(Select<?> query);

    /**
     * <code>this = any (Select<?> ...)</code>
     */
    Condition equalAny(Select<?> query);

    /**
     * <code>this = some (Select<?> ...)</code>
     */
    Condition equalSome(Select<?> query);

    /**
     * <code>this = all (Select<?> ...)</code>
     */
    Condition equalAll(Select<?> query);

    /**
     * <code>this != value</code>
     */
    Condition notEqual(T value);

    /**
     * <code>this != field</code>
     */
    Condition notEqual(Field<T> field);

    /**
     * <code>this != (Select<?> ...)</code>
     */
    Condition notEqual(Select<?> query);

    /**
     * <code>this != any (Select<?> ...)</code>
     */
    Condition notEqualAny(Select<?> query);

    /**
     * <code>this != some (Select<?> ...)</code>
     */
    Condition notEqualSome(Select<?> query);

    /**
     * <code>this != all (Select<?> ...)</code>
     */
    Condition notEqualAll(Select<?> query);

    /**
     * <code>this < value</code>
     */
    Condition lessThan(T value);

    /**
     * <code>this < field</code>
     */
    Condition lessThan(Field<T> field);

    /**
     * <code>this < (Select<?> ...)</code>
     */
    Condition lessThan(Select<?> query);

    /**
     * <code>this < any (Select<?> ...)</code>
     */
    Condition lessThanAny(Select<?> query);

    /**
     * <code>this < some (Select<?> ...)</code>
     */
    Condition lessThanSome(Select<?> query);

    /**
     * <code>this < all (Select<?> ...)</code>
     */
    Condition lessThanAll(Select<?> query);

    /**
     * <code>this <= value</code>
     */
    Condition lessOrEqual(T value);

    /**
     * <code>this <= field</code>
     */
    Condition lessOrEqual(Field<T> field);

    /**
     * <code>this <= (Select<?> ...)</code>
     */
    Condition lessOrEqual(Select<?> query);

    /**
     * <code>this <= any (Select<?> ...)</code>
     */
    Condition lessOrEqualAny(Select<?> query);

    /**
     * <code>this <= some (Select<?> ...)</code>
     */
    Condition lessOrEqualSome(Select<?> query);

    /**
     * <code>this <= all (Select<?> ...)</code>
     */
    Condition lessOrEqualAll(Select<?> query);

    /**
     * <code>this <= any (Select<?> ...)</code>
     *
     * @deprecated - 1.6.3 [#732] - use {@link #lessOrEqualAny(Select)} instead
     */
    @Deprecated
    Condition lessOrEqualToAny(Select<?> query);

    /**
     * <code>this <= some (Select<?> ...)</code>
     *
     * @deprecated - 1.6.3 [#732] - use {@link #lessOrEqualSome(Select)} instead
     */
    @Deprecated
    Condition lessOrEqualToSome(Select<?> query);

    /**
     * <code>this <= all (Select<?> ...)</code>
     *
     * @deprecated - 1.6.3 [#732] - use {@link #lessOrEqualAll(Select)} instead
     */
    @Deprecated
    Condition lessOrEqualToAll(Select<?> query);

    /**
     * <code>this > value</code>
     */
    Condition greaterThan(T value);

    /**
     * <code>this > field</code>
     */
    Condition greaterThan(Field<T> field);

    /**
     * <code>this > (Select<?> ...)</code>
     */
    Condition greaterThan(Select<?> query);

    /**
     * <code>this > any (Select<?> ...)</code>
     */
    Condition greaterThanAny(Select<?> query);

    /**
     * <code>this > some (Select<?> ...)</code>
     */
    Condition greaterThanSome(Select<?> query);

    /**
     * <code>this > all (Select<?> ...)</code>
     */
    Condition greaterThanAll(Select<?> query);

    /**
     * <code>this >= value</code>
     */
    Condition greaterOrEqual(T value);

    /**
     * <code>this >= field</code>
     */
    Condition greaterOrEqual(Field<T> field);

    /**
     * <code>this >= (Select<?> ...)</code>
     */
    Condition greaterOrEqual(Select<?> query);

    /**
     * <code>this >= any (Select<?> ...)</code>
     */
    Condition greaterOrEqualAny(Select<?> query);

    /**
     * <code>this >= some (Select<?> ...)</code>
     */
    Condition greaterOrEqualSome(Select<?> query);

    /**
     * <code>this >= all (Select<?> ...)</code>
     */
    Condition greaterOrEqualAll(Select<?> query);

}
