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
package org.jooq;

import java.util.stream.Stream;

/**
 * A model type for a row value expression.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be emulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
public interface Row extends FieldOrRow {

    /**
     * Get the degree of this row value expression.
     */
    int size();


    /**
     * Get the fields from this row as a {@link Stream}.
     */
    Stream<Field<?>> fieldStream();


    /**
     * Get a specific field from this row.
     * <p>
     * Usually, this will return the field itself. However, if this is a row
     * from an aliased table, the field will be aliased accordingly.
     *
     * @param <T> The generic field type
     * @param field The field to fetch
     * @return The field itself or an aliased field
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this row.
     *
     * @param fieldName The field to fetch
     * @return The field with the given name
     */
    Field<?> field(String fieldName);

    /**
     * Get a specific field from this row and coerce it to <code>type</code>.
     *
     * @param fieldName The field to fetch
     * @param type The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(String fieldName, Class<T> type);

    /**
     * Get a specific field from this row and coerce it to <code>dataType</code>.
     *
     * @param fieldName The field to fetch
     * @param dataType The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(String fieldName, DataType<T> dataType);

    /**
     * Get a specific field from this row.
     *
     * @param fieldName The field to fetch
     * @return The field with the given name
     */
    Field<?> field(Name fieldName);

    /**
     * Get a specific field from this row and coerce it to <code>type</code>.
     *
     * @param fieldName The field to fetch
     * @param type The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(Name fieldName, Class<T> type);

    /**
     * Get a specific field from this row and coerce it to <code>dataType</code>.
     *
     * @param fieldName The field to fetch
     * @param dataType The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(Name fieldName, DataType<T> dataType);

    /**
     * Get a specific field from this row.
     *
     * @param fieldIndex The field's index of the field to fetch
     * @return The field with the given name
     */
    Field<?> field(int fieldIndex);

    /**
     * Get a specific field from this row and coerce it to <code>type</code>.
     *
     * @param fieldIndex The field's index of the field to fetch
     * @param type The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(int fieldIndex, Class<T> type);

    /**
     * Get a specific field from this row and coerce it to <code>dataType</code>.
     *
     * @param fieldIndex The field's index of the field to fetch
     * @param dataType The type to coerce the resulting field to
     * @return The field with the given name
     */
    <T> Field<T> field(int fieldIndex, DataType<T> dataType);

    /**
     * Get all fields from this row.
     *
     * @return All available fields
     */
    Field<?>[] fields();

    /**
     * Get all fields from this row, providing some fields.
     *
     * @return All available fields
     * @see #field(Field)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this row, providing some field names.
     *
     * @return All available fields
     * @see #field(String)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this row, providing some field names.
     *
     * @return All available fields
     * @see #field(Name)
     */
    Field<?>[] fields(Name... fieldNames);

    /**
     * Get all fields from this row, providing some field indexes.
     *
     * @return All available fields
     * @see #field(int)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get a field's index from this row.
     *
     * @param field The field to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this row.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(String fieldName);

    /**
     * Get a field's index from this row.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this <code>Row</code>
     */
    int indexOf(Name fieldName);

    /**
     * Get an array of types for this row.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getType()} for
     * the corresponding <code>Field</code> in {@link #fields()}
     */
    Class<?>[] types();

    /**
     * Get the type for a given field index.
     *
     * @param fieldIndex The field's index of the field's type to fetch
     * @return The field's type
     */
    Class<?> type(int fieldIndex);

    /**
     * Get the type for a given field name.
     *
     * @param fieldName The field's name of the field's type to fetch
     * @return The field's type
     */
    Class<?> type(String fieldName);

    /**
     * Get the type for a given field name.
     *
     * @param fieldName The field's name of the field's type to fetch
     * @return The field's type
     */
    Class<?> type(Name fieldName);

    /**
     * Get an array of data types for this row.
     * <p>
     * Entries in the resulting array correspond to {@link Field#getDataType()}
     * for the corresponding <code>Field</code> in {@link #fields()}
     */
    DataType<?>[] dataTypes();

    /**
     * Get the data type for a given field index.
     *
     * @param fieldIndex The field's index of the field's data type to fetch
     * @return The field's data type
     */
    DataType<?> dataType(int fieldIndex);

    /**
     * Get the data type for a given field name.
     *
     * @param fieldName The field's name of the field's data type to fetch
     * @return The field's data type
     */
    DataType<?> dataType(String fieldName);

    /**
     * Get the data type for a given field name.
     *
     * @param fieldName The field's name of the field's data type to fetch
     * @return The field's data type
     */
    DataType<?> dataType(Name fieldName);

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression contains only <code>NULL</code>
     * values.
     * <p>
     * Row NULL predicates can be emulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NULL</code> is
     * equivalent to <code>A IS NULL AND B IS NULL</code>
     */
    @Support
    Condition isNull();

    /**
     * Check if this row value expression contains no <code>NULL</code> values.
     * <p>
     * Row NOT NULL predicates can be emulated in those databases that do not
     * support such predicates natively: <code>(A, B) IS NOT NULL</code> is
     * equivalent to <code>A IS NOT NULL AND B IS NOT NULL</code>
     * <p>
     * Note that the two following predicates are NOT equivalent:
     * <ul>
     * <li><code>(A, B) IS NOT NULL</code>, which is the same as
     * <code>(A IS NOT NULL) AND (B IS NOT NULL)</code></li>
     * <li><code>NOT((A, B) IS NULL)</code>, which is the same as
     * <code>(A IS NOT NULL) OR (B IS NOT NULL)</code></li>
     * </ul>
     */
    @Support
    Condition isNotNull();

}
