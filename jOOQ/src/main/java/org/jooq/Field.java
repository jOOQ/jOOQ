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

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

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
     * <li>The name of a parameter if it is a named {@link Param}</li>
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
    @Support
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
     * @param type
     */
    @Support
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
    @Support
    <Z> Field<Z> cast(Class<? extends Z> type);

    // ------------------------------------------------------------------------
    // Conversion of field into a sort field
    // ------------------------------------------------------------------------

    /**
     * Create an ascending sort field from this field
     *
     * @return This field as an ascending sort field
     */
    @Support
    SortField<T> asc();

    /**
     * Create a descending sort field from this field
     *
     * @return This field as a descending sort field
     */
    @Support
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
    @Support
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
    @Support
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
    @Support
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
    @Support
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
    @Support
    <Z> SortField<Z> sort(Map<T, Z> sortMap);

    // ------------------------------------------------------------------------
    // Arithmetic operations
    // ------------------------------------------------------------------------

    /**
     * Negate this field to get its negative value.
     * <p>
     * This renders the same on all dialects:
     * <code><pre>-[this]</pre></code>
     */
    @Support
    Field<T> neg();

    /**
     * An arithmetic expression adding this to value.
     * <p>
     * <ul>
     * <li>If this is a numeric field, then the value is added arithmetically</li>
     * <li>If this is a date time field, then [value] days are added to this date</li>
     * </ul>
     */
    @Support
    Field<T> add(Number value);

    /**
     * An arithmetic expression adding this to value
     */
    @Support
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
    @Support
    Field<T> sub(Number value);

    /**
     * An arithmetic expression subtracting value from this
     */
    @Support
    Field<T> sub(Field<? extends Number> value);

    /**
     * An arithmetic expression multiplying this with value
     */
    @Support
    Field<T> mul(Number value);

    /**
     * An arithmetic expression multiplying this with value
     */
    @Support
    Field<T> mul(Field<? extends Number> value);

    /**
     * An arithmetic expression dividing this by value
     */
    @Support
    Field<T> div(Number value);

    /**
     * An arithmetic expression dividing this by value
     */
    @Support
    Field<T> div(Field<? extends Number> value);

    /**
     * An arithmetic expression getting the modulo of this divided by value
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code>
     * ... or the modulo function elsewhere:
     * <code><pre>mod([this], [value])</pre></code>
     */
    @Support
    Field<T> mod(Number value);

    /**
     * An arithmetic expression getting the modulo of this divided by value
     * <p>
     * This renders the modulo operation where available:
     * <code><pre>[this] % [value]</pre></code>
     * ... or the modulo function elsewhere:
     * <code><pre>mod([this], [value])</pre></code>
     */
    @Support
    Field<T> mod(Field<? extends Number> value);

    // ------------------------------------------------------------------------
    // Conditions created from this field
    // ------------------------------------------------------------------------

    /**
     * Create a condition to check this field against <code>null</code>.
     * <p>
     * SQL: <code>this is null</code>
     */
    @Support
    Condition isNull();

    /**
     * Create a condition to check this field against <code>null</code>.
     * <p>
     * SQL: <code>this is not null</code>
     */
    @Support
    Condition isNotNull();

    /**
     * Create a condition to check this field against known string literals for
     * <code>true</code>
     * <p>
     * SQL:
     * <code>lcase(this) in ("1", "y", "yes", "true", "on", "enabled")</code>
     */
    @Support
    Condition isTrue();

    /**
     * Create a condition to check this field against known string literals for
     * <code>false</code>
     * <p>
     * SQL:
     * <code>lcase(this) in ("0", "n", "no", "false", "off", "disabled")</code>
     */
    @Support
    Condition isFalse();

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this like value</code>
     */
    @Support
    Condition like(Field<T> value);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this like value escape 'e'</code>
     */
    @Support
    Condition like(Field<T> value, char escape);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this like value</code>
     */
    @Support
    Condition like(T value);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this like value escape 'e'</code>
     */
    @Support
    Condition like(T value, char escape);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this not like value</code>
     */
    @Support
    Condition notLike(Field<T> value);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this not like value escape 'e'</code>
     */
    @Support
    Condition notLike(Field<T> value, char escape);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this not like value</code>
     */
    @Support
    Condition notLike(T value);

    /**
     * Create a condition to pattern-check this field against a value
     * <p>
     * SQL: <code>this not like value escape 'e'</code>
     */
    @Support
    Condition notLike(T value, char escape);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\') || '%') escape '\'</code>
     *
     * @see Factory#escape(String, char)
     * @see #like(Object, char)
     */
    @Support
    Condition contains(T value);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\') || '%') escape '\'</code>
     *
     * @see Factory#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Condition contains(Field<T> value);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     *
     * @see Factory#escape(String, char)
     * @see #like(Object, char)
     */
    @Support
    Condition startsWith(T value);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     *
     * @see Factory#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Condition startsWith(Field<T> value);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     *
     * @see Factory#escape(String, char)
     * @see #like(Object, char)
     */
    @Support
    Condition endsWith(T value);

    /**
     * Convenience method for {@link #like(Object, char)} including proper
     * adding of wildcards and escaping
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     *
     * @see Factory#escape(Field, char)
     * @see #like(Field, char)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Condition endsWith(Field<T> value);

    /**
     * Create a condition to check this field against several values
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    Condition in(Collection<T> values);

    /**
     * Create a condition to check this field against several values
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    Condition in(T... values);

    /**
     * Create a condition to check this field against several values
     * <p>
     * SQL: <code>this in (values...)</code>
     */
    @Support
    Condition in(Field<?>... values);

    /**
     * Create a condition to check this field against a subquery
     * <p>
     * Note that the subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors on the database, if not used
     * correctly.
     * <p>
     * SQL: <code>this in (select...)</code>
     */
    @Support
    Condition in(Select<?> query);

    /**
     * Create a condition to check this field against several values
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    Condition notIn(Collection<T> values);

    /**
     * Create a condition to check this field against several values
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    Condition notIn(T... values);

    /**
     * Create a condition to check this field against several values
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (values...)</code>
     */
    @Support
    Condition notIn(Field<?>... values);

    /**
     * Create a condition to check this field against a subquery
     * <p>
     * Note that the subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors on the database, if not used
     * correctly.
     * <p>
     * Note that if any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     * <p>
     * SQL: <code>this not in (select...)</code>
     */
    @Support
    Condition notIn(Select<?> query);

    /**
     * Create a condition to check this field against some bounds
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    Condition between(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds
     * <p>
     * SQL: <code>this between minValue and maxValue</code>
     */
    @Support
    Condition between(Field<T> minValue, Field<T> maxValue);

    /**
     * Create a condition to check this field against some bounds
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    Condition notBetween(T minValue, T maxValue);

    /**
     * Create a condition to check this field against some bounds
     * <p>
     * SQL: <code>this not between minValue and maxValue</code>
     */
    @Support
    Condition notBetween(Field<T> minValue, Field<T> maxValue);

    /**
     * <code>this = value</code>
     * <p>
     * If <code>value == null</code>, then this will return a condition
     * equivalent to {@link #isNull()} for convenience. SQL's ternary
     * <code>NULL</code> logic is rarely of use for Java programmers.
     */
    @Support
    Condition equal(T value);

    /**
     * <code>this = field</code>
     */
    @Support
    Condition equal(Field<T> field);

    /**
     * <code>this = (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equal(Select<?> query);

    /**
     * <code>this = any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equalAny(Select<?> query);

    /**
     * <code>this = any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equalAny(T... array);

    /**
     * <code>this = any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition equalAny(Field<T[]> array);

    /**
     * <code>this = some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #equalAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equalSome(Select<?> query);

    /**
     * <code>this = all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equalAll(Select<?> query);

    /**
     * <code>this = all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition equalAll(T... array);

    /**
     * <code>this = all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition equalAll(Field<T[]> array);

    /**
     * <code>this != value</code>
     * <p>
     * If <code>value == null</code>, then this will return a condition
     * equivalent to {@link #isNotNull()} for convenience. SQL's ternary
     * <code>NULL</code> logic is rarely of use for Java programmers.
     */
    @Support
    Condition notEqual(T value);

    /**
     * <code>this != field</code>
     */
    @Support
    Condition notEqual(Field<T> field);

    /**
     * <code>this != (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqual(Select<?> query);

    /**
     * <code>this != any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqualAny(Select<?> query);

    /**
     * <code>this != any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqualAny(T... array);

    /**
     * <code>this != any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition notEqualAny(Field<T[]> array);

    /**
     * <code>this != some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #notEqualAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqualSome(Select<?> query);

    /**
     * <code>this != all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqualAll(Select<?> query);

    /**
     * <code>this != all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition notEqualAll(T... array);

    /**
     * <code>this != all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition notEqualAll(Field<T[]> array);

    /**
     * <code>this < value</code>
     */
    @Support
    Condition lessThan(T value);

    /**
     * <code>this < field</code>
     */
    @Support
    Condition lessThan(Field<T> field);

    /**
     * <code>this < (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThan(Select<?> query);

    /**
     * <code>this < any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThanAny(Select<?> query);

    /**
     * <code>this < any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThanAny(T... array);

    /**
     * <code>this < any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition lessThanAny(Field<T[]> array);

    /**
     * <code>this < some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #lessThanAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThanSome(Select<?> query);

    /**
     * <code>this < all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThanAll(Select<?> query);

    /**
     * <code>this < all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessThanAll(T... array);

    /**
     * <code>this < all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition lessThanAll(Field<T[]> array);

    /**
     * <code>this <= value</code>
     */
    @Support
    Condition lessOrEqual(T value);

    /**
     * <code>this <= field</code>
     */
    @Support
    Condition lessOrEqual(Field<T> field);

    /**
     * <code>this <= (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqual(Select<?> query);

    /**
     * <code>this <= any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqualAny(Select<?> query);

    /**
     * <code>this <= any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqualAny(T... array);

    /**
     * <code>this <= any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition lessOrEqualAny(Field<T[]> array);

    /**
     * <code>this <= some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #lessOrEqualAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqualSome(Select<?> query);

    /**
     * <code>this <= all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqualAll(Select<?> query);

    /**
     * <code>this <= all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition lessOrEqualAll(T... array);

    /**
     * <code>this <= all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition lessOrEqualAll(Field<T[]> array);

    /**
     * <code>this > value</code>
     */
    @Support
    Condition greaterThan(T value);

    /**
     * <code>this > field</code>
     */
    @Support
    Condition greaterThan(Field<T> field);

    /**
     * <code>this > (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThan(Select<?> query);

    /**
     * <code>this > any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThanAny(Select<?> query);

    /**
     * <code>this > any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThanAny(T... array);

    /**
     * <code>this > any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition greaterThanAny(Field<T[]> array);

    /**
     * <code>this > some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #greaterThanAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThanSome(Select<?> query);

    /**
     * <code>this > all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThanAll(Select<?> query);

    /**
     * <code>this > all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterThanAll(T... array);

    /**
     * <code>this > all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition greaterThanAll(Field<T[]> array);

    /**
     * <code>this >= value</code>
     */
    @Support
    Condition greaterOrEqual(T value);

    /**
     * <code>this >= field</code>
     */
    @Support
    Condition greaterOrEqual(Field<T> field);

    /**
     * <code>this >= (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqual(Select<?> query);

    /**
     * <code>this >= any (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqualAny(Select<?> query);

    /**
     * <code>this >= any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqualAny(T... array);

    /**
     * <code>this >= any (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition greaterOrEqualAny(Field<T[]> array);

    /**
     * <code>this >= some (Select<?> ...)</code>
     *
     * @deprecated - 2.0.2 - Use {@link #greaterOrEqualAny(Select)} instead
     */
    @Deprecated
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqualSome(Select<?> query);

    /**
     * <code>this >= all (Select<?> ...)</code>
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqualAll(Select<?> query);

    /**
     * <code>this >= all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Condition greaterOrEqualAll(T... array);

    /**
     * <code>this >= all (array)</code>
     * <p>
     * This is natively supported by {@link SQLDialect#POSTGRES}. Other dialects
     * will render a subselect unnesting the array.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    Condition greaterOrEqualAll(Field<T[]> array);

    // ------------------------------------------------------------------------
    // Pre-2.0 API. This API is maintained for backwards-compatibility. It will
    // be removed in the future. Consider using equivalent methods from
    // org.jooq.Factory
    // ------------------------------------------------------------------------

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sign(Field)
     */
    @Support
    Field<Integer> sign();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#abs(Field)
     */
    @Support
    Field<T> abs();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#round(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<T> round();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#round(Field, int)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<T> round(int decimals);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#floor(Field)
     */
    @Support
    Field<T> floor();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#ceil(Field)
     */
    @Support
    Field<T> ceil();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sqrt(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sqrt();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#exp(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> exp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#ln(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> ln();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#log(Field, int)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> log(int base);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#power(Field, Number)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> power(Number exponent);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#acos(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> acos();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#asin(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> asin();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#atan(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#atan2(Field, Number)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan2(Number y);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#atan2(Field, Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> atan2(Field<? extends Number> y);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#cos(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cos();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sin(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sin();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#tan(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> tan();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#cot(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cot();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sinh(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> sinh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#cosh(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> cosh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#tanh(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> tanh();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#coth(Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> coth();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#deg(Field)
     */
    @Support
    Field<BigDecimal> deg();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rad(Field)
     */
    @Support
    Field<BigDecimal> rad();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#count(Field)
     */
    @Support
    Field<Integer> count();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#countDistinct(Field)
     */
    @Support
    Field<Integer> countDistinct();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#max(Field)
     */
    @Support
    Field<T> max();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#min(Field)
     */
    @Support
    Field<T> min();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sum(Field)
     */
    @Support
    Field<BigDecimal> sum();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#avg(Field)
     */
    @Support
    Field<BigDecimal> avg();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#median(Field)
     */
    @Support({ HSQLDB, ORACLE, SYBASE })
    Field<BigDecimal> median();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#stddevPop(Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> stddevPop();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#stddevSamp(Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> stddevSamp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#varPop(Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> varPop();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#varSamp(Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<BigDecimal> varSamp();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#count(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<Integer> countOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#max(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<T> maxOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#min(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<T> minOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#sum(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> sumOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#avg(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> avgOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#firstValue(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowIgnoreNullsStep<T> firstValue();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lastValue(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SYBASE })
    WindowIgnoreNullsStep<T> lastValue();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lead(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lead();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lead(Field, int)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lead(int offset);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lead(Field, int, Object)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lead(int offset, T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lead(Field, int, Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lead(int offset, Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lag(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lag();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lag(Field, int)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lag(int offset);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lag(Field, int, Object)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lag(int offset, T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lag(Field, int, Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE })
    WindowIgnoreNullsStep<T> lag(int offset, Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#stddevPop(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> stddevPopOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#stddevSamp(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> stddevSampOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#varPop(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> varPopOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#varSamp(Field)
     * @see AggregateFunction#over()
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    WindowPartitionByStep<BigDecimal> varSampOver();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#upper(Field)
     */
    @Support
    Field<String> upper();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lower(Field)
     */
    @Support
    Field<String> lower();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#trim(Field)
     */
    @Support
    Field<String> trim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rtrim(Field)
     */
    @Support
    Field<String> rtrim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#ltrim(Field)
     */
    @Support
    Field<String> ltrim();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rpad(Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rpad(Field, int)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rpad(Field, Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(Field<? extends Number> length, Field<String> character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#rpad(Field, int, char)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> rpad(int length, char character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lpad(Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lpad(Field, int)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lpad(Field, Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(Field<? extends Number> length, Field<String> character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#lpad(Field, int, char)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> lpad(int length, char character);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#repeat(Field, int)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> repeat(Number count);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#repeat(Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<String> repeat(Field<? extends Number> count);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#replace(Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(Field<String> search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#replace(Field, String)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(String search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#replace(Field, Field, Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(Field<String> search, Field<String> replace);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#replace(Field, String, String)
     */
    @Support({ ASE, DB2, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE, SQLITE })
    Field<String> replace(String search, String replace);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#position(Field, String)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> position(String search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#position(Field, Field)
     */
    @Support({ ASE, DB2, DERBY, H2, HSQLDB, INGRES, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> position(Field<String> search);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#ascii(Field)
     */
    @Support({ ASE, DB2, H2, HSQLDB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
    Field<Integer> ascii();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#concat(Field...)
     */
    @Support
    Field<String> concat(Field<?>... fields);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#concat(String...)
     */
    @Support
    Field<String> concat(String... values);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#substring(Field, int)
     */
    @Support
    Field<String> substring(int startingPosition);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#substring(Field, Field)
     */
    @Support
    Field<String> substring(Field<? extends Number> startingPosition);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#substring(Field, int, int)
     */
    @Support
    Field<String> substring(int startingPosition, int length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#substring(Field, Field, Field)
     */
    @Support
    Field<String> substring(Field<? extends Number> startingPosition, Field<? extends Number> length);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#length(Field)
     */
    @Support
    Field<Integer> length();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#charLength(Field)
     */
    @Support
    Field<Integer> charLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#bitLength(Field)
     */
    @Support
    Field<Integer> bitLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#octetLength(Field)
     */
    @Support
    Field<Integer> octetLength();

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#extract(Field, DatePart)
     */
    @Support
    Field<Integer> extract(DatePart datePart);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#greatest(Field, Field...)
     */
    @Support
    Field<T> greatest(T... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#greatest(Field, Field...)
     */
    @Support
    Field<T> greatest(Field<?>... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#least(Field, Field...)
     */
    @Support
    Field<T> least(T... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#least(Field, Field...)
     */
    @Support
    Field<T> least(Field<?>... others);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nvl(Field, Object)
     */
    @Support
    Field<T> nvl(T defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nvl(Field, Field)
     */
    @Support
    Field<T> nvl(Field<T> defaultValue);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nvl2(Field, Object, Object)
     */
    @Support
    <Z> Field<Z> nvl2(Z valueIfNotNull, Z valueIfNull);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nvl2(Field, Field, Field)
     */
    @Support
    <Z> Field<Z> nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nullif(Field, Object)
     */
    @Support
    Field<T> nullif(T other);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#nullif(Field, Field)
     */
    @Support
    Field<T> nullif(Field<T> other);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#decode(Object, Object, Object)
     */
    @Support
    <Z> Field<Z> decode(T search, Z result);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#decode(Object, Object, Object, Object...)
     */
    @Support
    <Z> Field<Z> decode(T search, Z result, Object... more);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#decode(Field, Field, Field)
     */
    @Support
    <Z> Field<Z> decode(Field<T> search, Field<Z> result);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#decode(Field, Field, Field, Field...)
     */
    @Support
    <Z> Field<Z> decode(Field<T> search, Field<Z> result, Field<?>... more);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#coalesce(Object, Object...)
     */
    @Support
    Field<T> coalesce(T option, T... options);

    /**
     * This method is part of the pre-2.0 API. This API is maintained for
     * backwards-compatibility. It may be removed in the future. Consider using
     * equivalent methods from {@link Factory}
     *
     * @see Factory#coalesce(Field, Field...)
     */
    @Support
    Field<T> coalesce(Field<T> option, Field<?>... options);

}
