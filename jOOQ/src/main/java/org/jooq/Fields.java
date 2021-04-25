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

import java.util.stream.Stream;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A common super type for various types that can provide a set of fields,
 * similar to a {@link Table} or a {@link Record}.
 * <p>
 * Not all implementations actually <em>know</em> their fields. For example,
 * when using plain SQL templates ({@link DSL#field(String)}) or tables
 * constructed from identifiers ({@link DSL#field(Name)}), then the fields are
 * unknown to jOOQ and empty lists or arrays are returned.
 *
 * @author Lukas Eder
 */
public interface Fields {

    /**
     * Get all fields known to this type.
     */
    @NotNull
    Field<?>[] fields();

    /**
     * Get all fields known to this type as a {@link Row}.
     */
    @NotNull
    Row fieldsRow();

    /**
     * Get all fields known to this type as a {@link Stream}.
     */
    @NotNull
    Stream<Field<?>> fieldStream();

    /**
     * Get a field known to this type by field reference.
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
     */
    @Nullable
    <T> Field<T> field(Field<T> field);

    /**
     * Get a field known to this type by unqualified name, or <code>null</code>
     * if no field is known to this type by this name.
     *
     * @param name The unqualified name of the field
     */
    @Nullable
    Field<?> field(String name);

    /**
     * Get a field known to this type by unqualified name coerced to
     * <code>type</code>, or <code>null</code> if no field is known to this type
     * by this name.
     *
     * @param name The unqualified name of the field
     * @param type The type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(String name, Class<T> type);

    /**
     * Get a field known to this type by unqualified name coerced to
     * <code>dataType</code>, or <code>null</code> if no field is known to this
     * type by this name.
     *
     * @param name The unqualified name of the field
     * @param dataType The data type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(String name, DataType<T> dataType);

    /**
     * Get a field known to this type by qualified name, or <code>null</code> if
     * no field is known to this type by this name.
     *
     * @param name The qualified name of the field
     */
    @Nullable
    Field<?> field(Name name);

    /**
     * Get a field known to this type by qualified name coerced to
     * <code>type</code>, or <code>null</code> if no field is known to this type
     * by this name.
     *
     * @param name The qualified name of the field
     * @param type The type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(Name name, Class<T> type);

    /**
     * Get a field known to this type by qualified name coerced to
     * <code>dataType</code>, or <code>null</code> if no field is known to this
     * type by this name.
     *
     * @param name The qualified name of the field
     * @param dataType The data type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(Name name, DataType<T> dataType);

    /**
     * Get a field known to this type by index, or <code>null</code> if no field
     * is available at the index.
     *
     * @param index The 0-based index of the field
     */
    @Nullable
    Field<?> field(int index);

    /**
     * Get a field known to this type by index coerced to <code>type</code>, or
     * <code>null</code> if no field is available at the index.
     *
     * @param index The 0-based index of the field
     * @param type The type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(int index, Class<T> type);

    /**
     * Get a field known to this type by index coerced to <code>dataType</code>,
     * or <code>null</code> if no field is available at the index.
     *
     * @param index The 0-based index of the field
     * @param dataType The data type to coerce the resulting field to
     */
    @Nullable
    <T> Field<T> field(int index, DataType<T> dataType);

    /**
     * Get all fields, filtering by some fields.
     *
     * @param fields The fields to include after looking them up via
     *            {@link #field(Field)}.
     * @see #field(Field)
     */
    @NotNull
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields known to this type, filtering by some unqualified field
     * names.
     *
     * @param names The unqualified field names to include after looking them up
     *            via {@link #field(String)}.
     * @see #field(String)
     */
    @NotNull
    Field<?>[] fields(String... names);

    /**
     * Get all fields known to this type, filtering by some qualified field
     * names.
     *
     * @param names The qualified field names to include after looking them up
     *            via {@link #field(Name)}.
     * @see #field(Name)
     */
    @NotNull
    Field<?>[] fields(Name... names);

    /**
     * Get all fields known to this type, filtering by some field indexes.
     *
     * @param names The 0-based field indexes to include after looking them up
     *            via {@link #field(int)}.
     * @see #field(int)
     */
    @NotNull
    Field<?>[] fields(int... indexes);

    /**
     * Get a field's index from this type.
     *
     * @param field The field to look for
     * @return The field's 0-based index or <code>-1</code> if the field is not
     *         known to this type.
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this type.
     *
     * @param name The unqualified field name to look for
     * @return The field's 0-based index or <code>-1</code> if the field is not
     *         known to this type.
     */
    int indexOf(String name);

    /**
     * Get a field's index from this type.
     *
     * @param name The qualified field name to look for
     * @return The field's 0-based index or <code>-1</code> if the field is not
     *         known to this type.
     */
    int indexOf(Name name);

    /**
     * Get an array of field types for fields known to this type.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getType()} for
     * the corresponding <code>Field</code> in {@link #fields()}
     */
    @NotNull
    Class<?>[] types();

    /**
     * Get the field type for a given field index, or <code>null</code> if no
     * field is available at the index.
     *
     * @param index The field's 0-based index
     */
    @Nullable
    Class<?> type(int index);

    /**
     * Get the field type for a given unqualified field name, or
     * <code>null</code> if no field is known to this type by this name.
     *
     * @param name The unqualified field name
     */
    @Nullable
    Class<?> type(String name);

    /**
     * Get the field type for a given qualified field name, or <code>null</code>
     * if no field is known to this type by this name.
     *
     * @param name The qualified field name
     */
    @Nullable
    Class<?> type(Name name);

    /**
     * Get an array of field data types for this type.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getDataType()}
     * for the corresponding <code>Field</code> in {@link #fields()}
     */
    @NotNull
    DataType<?>[] dataTypes();

    /**
     * Get the field data type for a given field index, or <code>null</code> if
     * no field is available at the index.
     *
     * @param index The field's 0-based index
     */
    @Nullable
    DataType<?> dataType(int index);

    /**
     * Get the field data type for a given qualified field name, or
     * <code>null</code> if no field is known to this type by this name.
     *
     * @param name The qualified field name
     */
    @Nullable
    DataType<?> dataType(String name);

    /**
     * Get the field data type for a given qualified field name, or
     * <code>null</code> if no field is known to this type by this name.
     *
     * @param name The qualified field name
     */
    @Nullable
    DataType<?> dataType(Name name);

}
