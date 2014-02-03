/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.postgres;

import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
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
    private PostgresDSL() {}

    // -------------------------------------------------------------------------
    // PostgreSQL-specific array functions
    // -------------------------------------------------------------------------

    /**
     * The PostgreSQL <code>array_append(anyarray, anyelement)</code> function.
     * <p>
     * Example: <code><pre>
     * {1, 2, 3} = array_append(ARRAY[1, 2], 3)
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static <T> Field<T[]> arrayAppend(T[] array, T value) {
        return arrayAppend(val(array), val(value));
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
        return arrayAppend(val(array), value);
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
        return arrayAppend(array, val(value));
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
        return arrayPrepend(val(value), val(array));
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
        return arrayPrepend(value, val(array));
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
        return arrayPrepend(val(value), array);
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
     * The PostgreSQL <code>array_to_string(anyarray, delimiter)</code> function.
     * <p>
     * Example: <code><pre>
     * '1~^~2~^~3' = array_to_string(ARRAY[1, 2, 3], '~^~')
     * </pre></code>
     */
    @Support({ POSTGRES })
    public static Field<String> arrayToString(Object[] array, String delimiter) {
        return arrayToString(val(array), val(delimiter));
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
        return arrayToString(array, val(delimiter));
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
        return stringToArray(val(string), val(delimiter));
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
        return stringToArray(val(string), delimiter);
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
        return stringToArray(string, val(delimiter));
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
