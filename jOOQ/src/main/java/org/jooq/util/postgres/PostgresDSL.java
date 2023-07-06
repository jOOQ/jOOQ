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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.util.postgres;

// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import org.jetbrains.annotations.NotNull;

/**
 * The {@link SQLDialect#POSTGRES} specific DSL.
 *
 * @author Lukas Eder
 */
public class PostgresDSL extends DSL {

    /**
     * No instances
     */
    protected PostgresDSL() {}

    // -------------------------------------------------------------------------
    // PostgreSQL-specific array functions
    // -------------------------------------------------------------------------

    /**
     * The PostgreSQL <code>array1 &amp;&amp; array2</code> overlap operator.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * true = array[1, 2, 3] &amp;&amp; array[3, 4, 5]
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14352] - Use
     *             {@link DSL#arrayOverlap(Object[], Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Condition arrayOverlap(T[] left, T[] right) {
        return arrayOverlap(val(left), val(right));
    }

    /**
     * The PostgreSQL <code>array1 &amp;&amp; array2</code> overlap operator.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * true = array[1, 2, 3] &amp;&amp; array[3, 4, 5]
     * </code>
     * </pre>
     *
     * @deprecated - 3.16.0 - [#14352] - Use
     *             {@link DSL#arrayOverlap(Object[], Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Condition arrayOverlap(T[] left, Field<T[]> right) {
        return arrayOverlap(val(left), right);
    }

    /**
     * The PostgreSQL <code>array1 &amp;&amp; array2</code> overlap operator.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * true = array[1, 2, 3] &amp;&amp; array[3, 4, 5]
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14352] - Use
     *             {@link DSL#arrayOverlap(Field, Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Condition arrayOverlap(Field<T[]> left, T[] right) {
        return arrayOverlap(left, val(right));
    }

    /**
     * The PostgreSQL <code>array1 &amp;&amp; array2</code> overlap operator.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * true = array[1, 2, 3] &amp;&amp; array[3, 4, 5]
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14352] - Use
     *             {@link DSL#arrayOverlap(Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Condition arrayOverlap(Field<T[]> left, Field<T[]> right) {
        return DSL.condition("{0} && {1}", left, right);
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayAppend(Object[], Object)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayAppend(T[] array, T value) {
        return arrayAppend0(val(array), val(value));
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayAppend(Object[], Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayAppend(T[] array, Field<T> value) {
        return arrayAppend0(val(array), value);
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayAppend(Field, Object)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayAppend(Field<T[]> array, T value) {
        return arrayAppend0(array, val(value));
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayAppend(Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayAppend(Field<T[]> array, Field<T> value) {
        return arrayAppend0(array, value);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayAppend0(Field<T[]> array, Field<T> value) {
        return function("array_append", nullSafeDataType(array), array, value);
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayPrepend(Object, Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayPrepend(T value, T[] array) {
        return arrayPrepend0(val(value), val(array));
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayPrepend(Field, Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayPrepend(Field<T> value, T[] array) {
        return arrayPrepend0(value, val(array));
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example: <pre><code>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </code></pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayPrepend(Object, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayPrepend(T value, Field<T[]> array) {
        return arrayPrepend0(val(value), array);
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayPrepend(Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayPrepend(Field<T> value, Field<T[]> array) {
        return arrayPrepend0(value, array);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayPrepend0(Field<T> value, Field<T[]> array) {
        return function("array_prepend", nullSafeDataType(array), value, array);
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayConcat(Object[], Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayCat(T[] array1, T[] array2) {
        return arrayCat(val(array1), val(array2));
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayConcat(Object[], Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayCat(T[] array1, Field<T[]> array2) {
        return arrayCat(val(array1), array2);
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayConcat(Field, Object[])} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayCat(Field<T[]> array1, T[] array2) {
        return arrayCat(array1, val(array2));
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayConcat(Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayCat(Field<T[]> array1, Field<T[]> array2) {
        return function("array_cat", nullSafeDataType(array1), array1, array2);
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayRemove(Object[], Object)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayRemove(T[] array, T element) {
        return arrayRemove0(val(array), val(element));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayRemove(Field, Object)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayRemove(Field<T[]> array, T element) {
        return arrayRemove0(nullSafe(array), val(element));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayRemove(Object[], Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayRemove(T[] array, Field<T> element) {
        return arrayRemove0(val(array), element);
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#14388] - Use
     *             {@link DSL#arrayRemove(Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayRemove(Field<T[]> array, Field<T> element) {
        return arrayRemove0(array, element);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayRemove0(Field<T[]> array, Field<T> element) {
        return function("array_remove", array.getDataType(), array, element);
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#11981] - Use
     *             {@link DSL#arrayReplace(Object[], Object, Object)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayReplace(T[] array, T search, T replace) {
        return arrayReplace0(val(array), val(search), val(replace));
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#11981] - Use
     *             {@link DSL#arrayReplace(Field, Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayReplace(T[] array, Field<T> search, Field<T> replace) {
        return arrayReplace0(val(array), search, replace);
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#11981] - Use
     *             {@link DSL#arrayReplace(Field, Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayReplace(Field<T[]> array, T search, T replace) {
        return arrayReplace0(nullSafe(array), val(search), val(replace));
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example:
     *
     * <pre>
     * <code>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </code>
     * </pre>
     *
     * @deprecated - 3.18.0 - [#11981] - Use
     *             {@link DSL#arrayReplace(Field, Field, Field)} instead.
     */
    @Deprecated
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayReplace(Field<T[]> array, Field<T> search, Field<T> replace) {
        return arrayReplace0(array, search, replace);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayReplace0(Field<T[]> array, Field<T> search, Field<T> replace) {
        return function("array_replace", array.getDataType(), array, search, replace);
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <pre><code>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(T value, Integer[] dimensions) {
        return arrayFill(val(value), val(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <pre><code>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(Field<T> value, Integer[] dimensions) {
        return arrayFill(nullSafe(value), val(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <pre><code>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(T value, Field<Integer[]> dimensions) {
        return arrayFill(val(value), dimensions);
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <pre><code>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(Field<T> value, Field<Integer[]> dimensions) {
        return function("array_fill", nullSafeDataType(value).getArrayDataType(), value, dimensions);
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <pre><code>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(T value, Integer[] dimensions, Integer[] bounds) {
        return arrayFill(val(value), val(dimensions), val(bounds));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <pre><code>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(Field<T> value, Integer[] dimensions, Integer[] bounds) {
        return arrayFill(nullSafe(value), val(dimensions), val(bounds));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <pre><code>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(T value, Field<Integer[]> dimensions, Field<Integer[]> bounds) {
        return arrayFill(val(value), dimensions, bounds);
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <pre><code>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static <T> Field<T[]> arrayFill(Field<T> value, Field<Integer[]> dimensions, Field<Integer[]> bounds) {
        return function("array_fill", nullSafeDataType(value).getArrayDataType(), value, dimensions, bounds);
    }

    /**
     * The PostgreSQL <code>array_length(anyarray, int)</code> function.
     * <p>
     * jOOQ currently doesn't support multi-dimensional arrays, so the dimension
     * will always be <code>1</code>.
     * <p>
     * Example: <pre><code>
     * 3 = array_length(array[1,2,3], 1)
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<Integer> arrayLength(Object[] array) {
        return arrayLength(val(array));
    }

    /**
     * The PostgreSQL <code>array_length(anyarray, int)</code> function.
     * <p>
     * jOOQ currently doesn't support multi-dimensional arrays, so the dimension
     * will always be <code>1</code>.
     * <p>
     * Example: <pre><code>
     * 3 = array_length(array[1,2,3], 1)
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<Integer> arrayLength(Field<? extends Object[]> array) {
        return field("{array_length}({0}, 1)", SQLDataType.INTEGER, array);
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String> arrayToString(Object[] array, String delimiter) {
        return arrayToString(val(array), val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String> arrayToString(Object[] array, Field<String> delimiter) {
        return arrayToString(val(array), delimiter);
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String> arrayToString(Field<? extends Object[]> array, String delimiter) {
        return arrayToString(array, val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String> arrayToString(Field<? extends Object[]> array, Field<String> delimiter) {
        return function("array_to_string", SQLDataType.VARCHAR, array, delimiter);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(String string, String delimiter) {
        return stringToArray(val(string, String.class), val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(String string, Field<String> delimiter) {
        return stringToArray(val(string, String.class), delimiter);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(Field<String> string, String delimiter) {
        return stringToArray(string, val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(Field<String> string, Field<String> delimiter) {
        return function("string_to_array", SQLDataType.VARCHAR.getArrayDataType(), string, delimiter);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(String string, String delimiter, String nullString) {
        return stringToArray(val(string, String.class), val(delimiter, String.class), val(nullString, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(String string, Field<String> delimiter, Field<String> nullString) {
        return stringToArray(val(string, String.class), delimiter, nullString);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(Field<String> string, String delimiter, String nullString) {
        return stringToArray(string, val(delimiter, String.class), val(nullString, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <pre><code>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES, YUGABYTEDB })
    public static Field<String[]> stringToArray(Field<String> string, Field<String> delimiter, Field<String> nullString) {
        return function("string_to_array", SQLDataType.VARCHAR.getArrayDataType(), string, delimiter, nullString);
    }

    // -------------------------------------------------------------------------
    // Other PostgreSQL-specific functions / clauses
    // -------------------------------------------------------------------------

    /**
     * Get the PostgreSQL-specific <code>ONLY [table]</code> clause for use with
     * table inheritance.
     * <p>
     * Example: <pre><code>
     * SELECT * FROM ONLY parent_table
     * </code></pre>
     */
    @NotNull
    @Support({ POSTGRES })
    public static Table<Record> only(Table<?> table) {
        return table("{only} {0}", table);
    }

    /**
     * Get the PostgreSQL-specific <code>[table].oid</code> column from any
     * table.
     *
     * @deprecated - [#12420] - 3.16.0 - Use actual <code>OID</code> column
     *             references in jOOQ-meta, instead.
     */
    @NotNull
    @Support({ POSTGRES })
    @Deprecated(forRemoval = true, since = "3.16")
    public static Field<Long> oid(Table<?> table) {
        return field("{0}.oid", Long.class, table);
    }
}
