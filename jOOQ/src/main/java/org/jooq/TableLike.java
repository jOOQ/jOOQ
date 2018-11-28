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
import java.util.stream.Stream;

/**
 * An object that can behave like a table (a table-like object)
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface TableLike<R extends Record> extends QueryPart {

    /**
     * Get this table's fields as a {@link Row}, if this table knows its field
     * references.
     */
    Row fieldsRow();


    /**
     * Get this table's fields as a {@link Stream}, if this table knows its
     * field references.
     */
    Stream<Field<?>> fieldStream();


    /**
     * Get a specific field from this table, if this table knows its field
     * references.
     * <p>
     * This will return:
     * <ul>
     * <li>A field that is the same as the argument field (by identity
     * comparison).</li>
     * <li>A field that is equal to the argument field (exact matching fully
     * qualified name).</li>
     * <li>A field that is equal to the argument field (partially matching
     * qualified name).</li>
     * <li>A field whose name is equal to the name of the argument field.</li>
     * <li><code>null</code> otherwise.
     * </ul>
     * If several fields have the same name, the first one is returned and a
     * warning is logged.
     *
     * @see Row#field(Field)
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this table, if this table knows its field
     * references.
     *
     * @see Row#field(String)
     */
    Field<?> field(String name);

    /**
     * Get a specific field from this table and coerce it to <code>type</code>,
     * if this table knows its field references.
     *
     * @see Row#field(String, Class)
     */
    <T> Field<T> field(String name, Class<T> type);

    /**
     * Get a specific field from this table and coerce it to
     * <code>dataType</code>, if this table knows its field references.
     *
     * @see Row#field(String, DataType)
     */
    <T> Field<T> field(String name, DataType<T> dataType);

    /**
     * Get a specific field from this table, if this table knows its field
     * references.
     *
     * @see Row#field(Name)
     */
    Field<?> field(Name name);

    /**
     * Get a specific field from this table and coerce it to <code>type</code>,
     * if this table knows its field references.
     *
     * @see Row#field(Name, Class)
     */
    <T> Field<T> field(Name name, Class<T> type);

    /**
     * Get a specific field from this table and coerce it to
     * <code>dataType</code>, if this table knows its field references.
     *
     * @see Row#field(Name, DataType)
     */
    <T> Field<T> field(Name name, DataType<T> dataType);

    /**
     * Get a specific field from this table, if this table knows its field
     * references.
     *
     * @see Row#field(int)
     */
    Field<?> field(int index);

    /**
     * Get a specific field from this table and coerce it to <code>type</code>,
     * if this table knows its field references.
     *
     * @see Row#field(int, Class)
     */
    <T> Field<T> field(int index, Class<T> type);

    /**
     * Get a specific field from this table and coerce it to
     * <code>dataType</code>, if this table knows its field references.
     *
     * @see Row#field(int, DataType)
     */
    <T> Field<T> field(int index, DataType<T> dataType);

    /**
     * Get all fields from this table, if this table knows its field references,
     * or an empty array otherwise.
     *
     * @see Row#fields()
     */
    Field<?>[] fields();

    /**
     * Get all fields from this table, providing some fields, if this table
     * knows its field references.
     *
     * @return All available fields
     * @see Row#fields(Field...)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this table, providing some field names, if this table
     * knows its field references.
     *
     * @return All available fields
     * @see Row#fields(String...)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this table, providing some field names, if this table
     * knows its field references.
     *
     * @return All available fields
     * @see Row#fields(Name...)
     */
    Field<?>[] fields(Name... fieldNames);

    /**
     * Get all fields from this table, providing some field indexes, if this
     * table knows its field references.
     *
     * @return All available fields
     * @see Row#fields(int...)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get a field's index from this table.
     *
     * @param field The field to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this table.
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this table.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this table.
     */
    int indexOf(String fieldName);

    /**
     * Get a field's index from this table.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this table
     */
    int indexOf(Name fieldName);

    /**
     * The underlying table representation of this object.
     * <p>
     * This method is useful for things like
     * <code>SELECT * FROM (SELECT * FROM x WHERE x.a = '1') WHERE ... </code>
     */
    @Support
    Table<R> asTable();

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String)
     */
    @Support
    Table<R> asTable(String alias);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, String...)
     */
    @Support
    Table<R> asTable(String alias, String... fieldAliases);


    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, Function)
     */
    @Support
    Table<R> asTable(String alias, Function<? super Field<?>, ? extends String> aliasFunction);

    /**
     * The underlying aliased table representation of this object.
     *
     * @see Table#as(String, BiFunction)
     */
    @Support
    Table<R> asTable(String alias, BiFunction<? super Field<?>, ? super Integer, ? extends String> aliasFunction);

}
