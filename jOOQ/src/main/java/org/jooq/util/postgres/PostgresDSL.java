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
 */
package org.jooq.util.postgres;

import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

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
     * The PostgreSQL <code>array(select)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array(select 1 union select 2 union select 3)
     * </pre></code>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Support({ POSTGRES })
    public static <T> Field<T[]> array(Select<? extends Record1<T>> select) {
        return DSL.field("array({0})", (DataType) select.getSelect().get(0).getDataType().getArrayDataType(), select);
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayAppend(T[] array, T value) {
        return arrayAppend0(val(array), val(value));
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayAppend(T[] array, Field<T> value) {
        return arrayAppend0(val(array), value);
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayAppend(Field<T[]> array, T value) {
        return arrayAppend0(array, val(value));
    }

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayAppend(Field<T[]> array, Field<T> value) {
        return arrayAppend0(array, value);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayAppend0(Field<T[]> array, Field<T> value) {
        return field("{array_append}({0}, {1})", nullSafe(array).getDataType(), nullSafe(array), nullSafe(value));
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayPrepend(T value, T[] array) {
        return arrayPrepend0(val(value), val(array));
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayPrepend(Field<T> value, T[] array) {
        return arrayPrepend0(value, val(array));
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayPrepend(T value, Field<T[]> array) {
        return arrayPrepend0(val(value), array);
    }

    /**
     * The PostgreSQL <code>array_prepend(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_prepend(1, ARRAY[2, 3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayPrepend(Field<T> value, Field<T[]> array) {
        return arrayPrepend0(value, array);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayPrepend0(Field<T> value, Field<T[]> array) {
        return field("{array_prepend}({0}, {1})", nullSafe(array).getDataType(), nullSafe(value), nullSafe(array));
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayCat(T[] array1, T[] array2) {
        return arrayCat(val(array1), val(array2));
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayCat(T[] array1, Field<T[]> array2) {
        return arrayCat(val(array1), array2);
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayCat(Field<T[]> array1, T[] array2) {
        return arrayCat(array1, val(array2));
    }

    /**
     * The PostgreSQL <code>array_cat(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3, 4, 5} = array_cat(ARRAY[1, 2], ARRAY[3, 4, 5])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayCat(Field<T[]> array1, Field<T[]> array2) {
        return field("{array_cat}({0}, {1})", nullSafe(array1).getDataType(), nullSafe(array1), nullSafe(array2));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayRemove(T[] array, T element) {
        return arrayRemove0(val(array), val(element));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayRemove(Field<T[]> array, T element) {
        return arrayRemove0(nullSafe(array), val(element));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayRemove(T[] array, Field<T> element) {
        return arrayRemove0(val(array), nullSafe(element));
    }

    /**
     * The PostgreSQL <code>array_remove(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,3} = array_remove(ARRAY[1,2,3,2], 2)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayRemove(Field<T[]> array, Field<T> element) {
        return arrayRemove0(array, element);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayRemove0(Field<T[]> array, Field<T> element) {
        return field("{array_remove}({0}, {1})", array.getDataType(), array, element);
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayReplace(T[] array, T search, T replace) {
        return arrayReplace0(val(array), val(search), val(replace));
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayReplace(T[] array, Field<T> search, Field<T> replace) {
        return arrayReplace0(val(array), nullSafe(search), nullSafe(replace));
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayReplace(Field<T[]> array, T search, T replace) {
        return arrayReplace0(nullSafe(array), val(search), val(replace));
    }

    /**
     * The PostgreSQL
     * <code>array_replace(anyarray, anyelement, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1,2,3,4} = array_replace(ARRAY[1,2,5,4], 5, 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayReplace(Field<T[]> array, Field<T> search, Field<T> replace) {
        return arrayReplace0(array, search, replace);
    }

    // Java 8 is stricter than Java 7 with respect to generics and overload
    // resolution (http://stackoverflow.com/q/5361513/521799)
    static <T> Field<T[]> arrayReplace0(Field<T[]> array, Field<T> search, Field<T> replace) {
        return field("{array_replace}({0}, {1}, {2})", array.getDataType(), nullSafe(array), nullSafe(search), nullSafe(replace));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <code><pre>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(T value, Integer[] dimensions) {
        return arrayFill(val(value), val(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <code><pre>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(Field<T> value, Integer[] dimensions) {
        return arrayFill(nullSafe(value), val(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <code><pre>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(T value, Field<Integer[]> dimensions) {
        return arrayFill(val(value), nullSafe(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[])</code> function.
     * <p>
     * Example: <code><pre>
     * {7,7,7} = array_fill(7, ARRAY[3])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(Field<T> value, Field<Integer[]> dimensions) {
        return field("{array_fill}({0}, {1})", nullSafe(value).getDataType().getArrayDataType(), nullSafe(value), nullSafe(dimensions));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <code><pre>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(T value, Integer[] dimensions, Integer[] bounds) {
        return arrayFill(val(value), val(dimensions), val(bounds));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <code><pre>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(Field<T> value, Integer[] dimensions, Integer[] bounds) {
        return arrayFill(nullSafe(value), val(dimensions), val(bounds));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <code><pre>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(T value, Field<Integer[]> dimensions, Field<Integer[]> bounds) {
        return arrayFill(val(value), nullSafe(dimensions), nullSafe(bounds));
    }

    /**
     * The PostgreSQL <code>array_fill(anyelement, int[], int[])</code> function.
     * <p>
     * Example: <code><pre>
     * [2:4]={7,7,7} = array_fill(7, ARRAY[3], ARRAY[2])
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayFill(Field<T> value, Field<Integer[]> dimensions, Field<Integer[]> bounds) {
        return field("{array_fill}({0}, {1})", nullSafe(value).getDataType().getArrayDataType(), nullSafe(value), nullSafe(dimensions), nullSafe(bounds));
    }

    /**
     * The PostgreSQL <code>array_length(anyarray, int)</code> function.
     * <p>
     * jOOQ currently doesn't support multi-dimensional arrays, so the dimension
     * will always be <code>1</code>.
     * <p>
     * Example: <code><pre>
     * 3 = array_length(array[1,2,3], 1)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<Integer> arrayLength(Object[] array) {
        return arrayLength(val(array));
    }

    /**
     * The PostgreSQL <code>array_length(anyarray, int)</code> function.
     * <p>
     * jOOQ currently doesn't support multi-dimensional arrays, so the dimension
     * will always be <code>1</code>.
     * <p>
     * Example: <code><pre>
     * 3 = array_length(array[1,2,3], 1)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<Integer> arrayLength(Field<? extends Object[]> array) {
        return field("{array_length}({0}, 1)", SQLDataType.INTEGER, array);
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String> arrayToString(Object[] array, String delimiter) {
        return arrayToString(val(array), val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String> arrayToString(Object[] array, Field<String> delimiter) {
        return arrayToString(val(array), delimiter);
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String> arrayToString(Field<? extends Object[]> array, String delimiter) {
        return arrayToString(array, val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String> arrayToString(Field<? extends Object[]> array, Field<String> delimiter) {
        return field("{array_to_string}({0}, {1})", SQLDataType.VARCHAR, nullSafe(array), nullSafe(delimiter));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(String string, String delimiter) {
        return stringToArray(val(string, String.class), val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(String string, Field<String> delimiter) {
        return stringToArray(val(string, String.class), delimiter);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(Field<String> string, String delimiter) {
        return stringToArray(string, val(delimiter, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(Field<String> string, Field<String> delimiter) {
        return field("{string_to_array}({0}, {1})", SQLDataType.VARCHAR.getArrayDataType(), nullSafe(string), nullSafe(delimiter));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(String string, String delimiter, String nullString) {
        return stringToArray(val(string, String.class), val(delimiter, String.class), val(nullString, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(String string, Field<String> delimiter, Field<String> nullString) {
        return stringToArray(val(string, String.class), delimiter, nullString);
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(Field<String> string, String delimiter, String nullString) {
        return stringToArray(string, val(delimiter, String.class), val(nullString, String.class));
    }

    /**
     * The PostgreSQL <code>string_to_array(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * {xx,NULL,zz} = string_to_array('xx~^~yy~^~zz', '~^~', 'yy')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String[]> stringToArray(Field<String> string, Field<String> delimiter, Field<String> nullString) {
        return field("{string_to_array}({0}, {1}, {2})", SQLDataType.VARCHAR.getArrayDataType(), nullSafe(string), nullSafe(delimiter), nullSafe(nullString));
    }

    // -------------------------------------------------------------------------
    // Other PostgreSQL-specific functions / clauses
    // -------------------------------------------------------------------------

    /**
     * Get the PostgreSQL-specific <code>ONLY [table]</code> clause for use with
     * table inheritance.
     * <p>
     * Example: <code><pre>
     * SELECT * FROM ONLY parent_table
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Table<Record> only(Table<?> table) {
        return table("{only} {0}", table);
    }

    /**
     * Get the PostgreSQL-specific <code>[table].oid</code> column from any table.
     */
    @Support({ POSTGRES })
    public static Field<Long> oid(Table<?> table) {
        return field("{0}.oid", Long.class, table);
    }
}
