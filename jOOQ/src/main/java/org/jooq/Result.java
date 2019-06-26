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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.tools.Convert;

/**
 * A wrapper for database results returned by <code>{@link SelectQuery}</code>
 *
 * @param <R> The record type contained in this result
 * @author Lukas Eder
 * @see SelectQuery#getResult()
 */
public interface Result<R extends Record> extends List<R>, Attachable, Formattable {

    /**
     * Get this result's record type.
     */
    RecordType<R> recordType();

    /**
     * Get this result's fields as a {@link Row}.
     */
    Row fieldsRow();

    /**
     * Get a specific field from this Result.
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
     * Get a specific field from this Result.
     *
     * @see Row#field(String)
     */
    Field<?> field(String name);

    /**
     * Get a specific field from this Result, coerced to <code>type</code>.
     *
     * @see Row#field(String, Class)
     */
    <T> Field<T> field(String name, Class<T> type);

    /**
     * Get a specific field from this Result, coerced to <code>dataType</code>.
     *
     * @see Row#field(String, DataType)
     */
    <T> Field<T> field(String name, DataType<T> dataType);

    /**
     * Get a specific field from this Result.
     *
     * @see Row#field(Name)
     */
    Field<?> field(Name name);

    /**
     * Get a specific field from this Result, coerced to <code>type</code>.
     *
     * @see Row#field(Name, Class)
     */
    <T> Field<T> field(Name name, Class<T> type);

    /**
     * Get a specific field from this Result, coerced to <code>dataType</code>.
     *
     * @see Row#field(Name, DataType)
     */
    <T> Field<T> field(Name name, DataType<T> dataType);

    /**
     * Get a specific field from this Result.
     *
     * @see Row#field(int)
     */
    Field<?> field(int index);

    /**
     * Get a specific field from this Result, coerced to <code>type</code>.
     *
     * @see Row#field(int, Class)
     */
    <T> Field<T> field(int index, Class<T> type);

    /**
     * Get a specific field from this Result, coerced to <code>dataType</code>.
     *
     * @see Row#field(int, DataType)
     */
    <T> Field<T> field(int index, DataType<T> dataType);

    /**
     * Get all fields from this Result.
     *
     * @see Row#fields()
     */
    Field<?>[] fields();

    /**
     * Get all fields from this Result, providing some fields.
     *
     * @return All available fields
     * @see Row#fields(Field...)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this Result, providing some field names.
     *
     * @return All available fields
     * @see Row#fields(String...)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this Result, providing some field names.
     *
     * @return All available fields
     * @see Row#fields(Name...)
     */
    Field<?>[] fields(Name... fieldNames);

    /**
     * Get all fields from this Result, providing some field indexes.
     *
     * @return All available fields
     * @see Row#fields(int...)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get a field's index from this result.
     *
     * @param field The field to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this result.
     */
    int indexOf(Field<?> field);

    /**
     * Get a field's index from this result.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this result.
     */
    int indexOf(String fieldName);

    /**
     * Get a field's index from this result.
     *
     * @param fieldName The field name to look for
     * @return The field's index or <code>-1</code> if the field is not
     *         contained in this result
     */
    int indexOf(Name fieldName);

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <T> The value's field's generic type parameter
     * @param index The record's index
     * @param field The value's field
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> T getValue(int index, Field<T> field) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <T> The value's field's generic type parameter
     * @param index The record's index
     * @param field The value's field
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @deprecated - 3.3.0 - [#2878] - This method will be removed in jOOQ 4.0
     */
    @Deprecated
    <T> T getValue(int index, Field<T> field, T defaultValue) throws IndexOutOfBoundsException,
        IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The value's field index
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     */
    Object getValue(int index, int fieldIndex) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The value's field index
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @deprecated - 3.3.0 - [#2878] - This method will be removed in jOOQ 4.0
     */
    @Deprecated
    Object getValue(int index, int fieldIndex, Object defaultValue) throws IndexOutOfBoundsException,
        IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The value's field name
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    Object getValue(int index, String fieldName) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The value's field name
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException if the index is out of range (
     *             <tt>index &lt; 0 || index &gt;= size()</tt>)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @deprecated - 3.3.0 - [#2878] - This method will be removed in jOOQ 4.0
     */
    @Deprecated
    Object getValue(int index, String fieldName, Object defaultValue) throws IndexOutOfBoundsException,
        IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param <T> The values' field's generic type parameter
     * @param field The values' field
     * @return The values
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> List<T> getValues(Field<T> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @param type The type used for type conversion
     * @return The values
     * @see Record#get(Field, Class)
     * @see Convert#convert(Object, Class)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> List<T> getValues(Field<?> field, Class<? extends T> type) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @param converter The data type converter used for type conversion
     * @return The values
     * @see Record#get(Field, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T, U> List<U> getValues(Field<T> field, Converter<? super T, ? extends U> converter) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The values
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     */
    List<?> getValues(int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @param type The type used for type conversion
     * @return The values
     * @see Record#get(int, Class)
     * @see Convert#convert(Object, Class)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> List<T> getValues(int fieldIndex, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @param converter The data type converter used for type conversion
     * @return The values
     * @see Record#get(int, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> List<U> getValues(int fieldIndex, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The values
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    List<?> getValues(String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @param type The type used for type conversion
     * @return The values
     * @see Record#get(String, Class)
     * @see Convert#convert(Object, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> List<T> getValues(String fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @param converter The data type converter used for type conversion
     * @return The values
     * @see Record#get(String, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> List<U> getValues(String fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The values
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    List<?> getValues(Name fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @param type The type used for type conversion
     * @return The values
     * @see Record#get(Name, Class)
     * @see Convert#convert(Object, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> List<T> getValues(Name fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @param converter The data type converter used for type conversion
     * @return The values
     * @see Record#get(Name, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> List<U> getValues(Name fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Whether there are any records contained in this <code>Result</code>.
     */
    @Override
    boolean isEmpty();

    /**
     * Whether there are any records contained in this <code>Result</code>.
     */
    boolean isNotEmpty();

    /**
     * Return the generated result as a list of name/value maps.
     *
     * @return The result.
     * @see Record#intoMap()
     */
    List<Map<String, Object>> intoMaps();

    /**
     * Return a {@link Map} with one of the result's columns as key and the
     * corresponding records as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(Field)} instead, if
     * your keys are non-unique
     *
     * @param <K> The key's generic field type
     * @param key The key field. Client code must assure that this field is
     *            unique in the result set.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    <K> Map<K, R> intoMap(Field<K> key) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and the
     * corresponding records as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(int)} instead, if
     * your keys are non-unique
     *
     * @param keyFieldIndex The key field index. Client code must assure that
     *            this field is unique in the result set.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument keyFieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, R> intoMap(int keyFieldIndex) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and the
     * corresponding records as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(String)} instead, if
     * your keys are non-unique
     *
     * @param keyFieldName The key field name. Client code must assure that this
     *            field is unique in the result set.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument keyFieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, R> intoMap(String keyFieldName) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and the
     * corresponding records as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(Name)} instead, if
     * your keys are non-unique
     *
     * @param keyFieldName The key field name. Client code must assure that this
     *            field is unique in the result set.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument keyFieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, R> intoMap(Name keyFieldName) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(Field, Field)}
     * instead, if your keys are non-unique
     *
     * @param <K> The key's generic field type
     * @param <V> The value's generic field type
     * @param key The key field. Client code must assure that this field is
     *            unique in the result set.
     * @param value The value field
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    <K, V> Map<K, V> intoMap(Field<K> key, Field<V> value) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(int, int)} instead,
     * if your keys are non-unique
     *
     * @param keyFieldIndex The key field index. Client code must assure that
     *            this field is unique in the result set.
     * @param valueFieldIndex The value field index
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, ?> intoMap(int keyFieldIndex, int valueFieldIndex) throws IllegalArgumentException,
        InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(String, String)}
     * instead, if your keys are non-unique
     *
     * @param keyFieldName The key field name. Client code must assure that this
     *            field is unique in the result set.
     * @param valueFieldName The value field name
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, ?> intoMap(String keyFieldName, String valueFieldName) throws IllegalArgumentException,
        InvalidResultException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value
     * <p>
     * An {@link InvalidResultException} is thrown, if the key turns out to be
     * non-unique in the result set. Use {@link #intoGroups(Name, Name)}
     * instead, if your keys are non-unique
     *
     * @param keyFieldName The key field name. Client code must assure that this
     *            field is unique in the result set.
     * @param valueFieldName The value field name
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key field returned two or more
     *             equal values from the result set.
     */
    Map<?, ?> intoMap(Name keyFieldName, Name valueFieldName) throws IllegalArgumentException,
        InvalidResultException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(Field, Class)} instead, if your
     * key is non-unique.
     *
     * @param key The key. Client code must assure that key is unique in the
     *            result set.
     * @param type The entity type.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, E> Map<K, E> intoMap(Field<K> key, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(int, Class)} instead, if your
     * key is non-unique.
     *
     * @param keyFieldIndex The key. Client code must assure that key is unique
     *            in the result set.
     * @param type The entity type.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field index is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(int keyFieldIndex, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(String, Class)} instead, if your
     * key is non-unique.
     *
     * @param keyFieldName The key. Client code must assure that key is unique
     *            in the result set.
     * @param type The entity type.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(String keyFieldName, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(Name, Class)} instead, if your
     * key is non-unique.
     *
     * @param keyFieldName The key. Client code must assure that key is unique
     *            in the result set.
     * @param type The entity type.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(Name keyFieldName, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(Field, Class)} instead, if your
     * key is non-unique.
     *
     * @param key The key. Client code must assure that key is unique in the
     *            result set.
     * @param mapper The mapper callback.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, E> Map<K, E> intoMap(Field<K> key, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(int, Class)} instead, if your key
     * is non-unique.
     *
     * @param keyFieldIndex The key. Client code must assure that key is unique
     *            in the result set.
     * @param mapper The mapper callback.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field index is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(String, Class)} instead, if your key
     * is non-unique.
     *
     * @param keyFieldName The key. Client code must assure that key is unique
     *            in the result set.
     * @param mapper The mapper callback.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(String keyFieldName, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(Name, Class)} instead, if your key
     * is non-unique.
     *
     * @param keyFieldName The key. Client code must assure that key is unique
     *            in the result set.
     * @param mapper The mapper callback.
     * @return A Map containing the result.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the key is non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, E> intoMap(Name keyFieldName, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Field[])} instead, if your keys
     * are non-unique.
     *
     * @param keys The keys. Client code must assure that keys are unique in the
     *            result set. If this is <code>null</code> or an empty array,
     *            the resulting map will contain at most one entry.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, R> intoMap(Field<?>[] keys) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(int[])} instead, if your keys
     * are non-unique.
     *
     * @param keyFieldIndexes The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, R> intoMap(int[] keyFieldIndexes) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(String[])} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, R> intoMap(String[] keyFieldNames) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Name[])} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, R> intoMap(Name[] keyFieldNames) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Field[], Field[])} instead, if your keys
     * are non-unique.
     *
     * @param keys The keys. Client code must assure that keys are unique in the
     *            result set. If this is <code>null</code> or an empty array,
     *            the resulting map will contain at most one entry.
     * @param values The values.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, Record> intoMap(Field<?>[] keys, Field<?>[] values) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(int[], int[])} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldIndexes The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param valueFieldIndexes The values.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, Record> intoMap(int[] keyFieldIndexes, int[] valueFieldIndexes) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(String[], String[])} instead,
     * if your keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param valueFieldNames The values.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, Record> intoMap(String[] keyFieldNames, String[] valueFieldNames) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given keys as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Name[], Name[])} instead, if
     * your keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param valueFieldNames The values.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    Map<Record, Record> intoMap(Name[] keyFieldNames, Name[] valueFieldNames) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Field[], Class)} instead, if
     * your keys are non-unique.
     *
     * @param keys The keys. Client code must assure that keys are unique in the
     *            result set. If this is <code>null</code> or an empty array,
     *            the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(Field<?>[] keys, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(int[], Class)} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldIndexes The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(String[], Class)} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(String[] keyFieldNames, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Name[], Class)} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Field[], Class)} instead, if
     * your keys are non-unique.
     *
     * @param keys The keys. Client code must assure that keys are unique in the
     *            result set. If this is <code>null</code> or an empty array,
     *            the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(int[], Class)} instead, if your
     * keys are non-unique.
     *
     * @param keyFieldIndexes The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(String[], Class)} instead, if
     * your keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped by
     * the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Name[], Class)} instead, if
     * your keys are non-unique.
     *
     * @param keyFieldNames The keys. Client code must assure that keys are
     *            unique in the result set. If this is <code>null</code> or an
     *            empty array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<List<?>, E> intoMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Class)} instead, if your keys
     * are non-unique.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K> Map<K, R> intoMap(Class<? extends K> keyType) throws MappingException, InvalidResultException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Class, Class)} instead, if your
     * keys are non-unique.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @param valueType The value type.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, V> intoMap(Class<? extends K> keyType, Class<? extends V> valueType)
        throws MappingException, InvalidResultException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Class, RecordMapper)} instead,
     * if your keys are non-unique.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @param valueMapper The value mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, V> intoMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper)
        throws InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(RecordMapper)} instead, if your
     * keys are non-unique.
     *
     * @param keyMapper The key mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K> Map<K, R> intoMap(RecordMapper<? super R, K> keyMapper) throws InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(RecordMapper, Class)} instead,
     * if your keys are non-unique.
     *
     * @param keyMapper The key mapper.
     * @param valueType The value type.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType)
        throws InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(RecordMapper, RecordMapper)}
     * instead, if your keys are non-unique.
     *
     * @param keyMapper The key mapper.
     * @param valueMapper The value mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, V> intoMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper)
        throws InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with the given key table as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Table)} instead, if your keys
     * are non-unique.
     *
     * @param table The key table. Client code must assure that keys are unique
     *            in the result set. May not be <code>null</code>.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    <S extends Record> Map<S, R> intoMap(Table<S> table) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with the given key table as a map key and the
     * corresponding record as value.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Table, Table)} instead, if your
     * keys are non-unique.
     *
     * @param keyTable The key table. Client code must assure that keys are
     *            unique in the result set. May not be <code>null</code>.
     * @param valueTable The value table. May not be <code>null</code>.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     */
    <S extends Record, T extends Record> Map<S, T> intoMap(Table<S> keyTable, Table<T> valueTable) throws IllegalArgumentException, InvalidResultException;

    /**
     * Return a {@link Map} with results grouped by the given key table and
     * mapped into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Table, Class)} instead, if your
     * keys are non-unique.
     *
     * @param table The key table. Client code must assure that keys are unique
     *            in the result set. May not be <code>null</code>.
     * @param type The entity type.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E, S extends Record> Map<S, E> intoMap(Table<S> table, Class<? extends E> type) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key table and
     * mapped by the given mapper.
     * <p>
     * An {@link InvalidResultException} is thrown, if the keys are non-unique
     * in the result set. Use {@link #intoGroups(Table, Class)} instead, if your
     * keys are non-unique.
     *
     * @param table The key table. Client code must assure that keys are unique
     *            in the result set. May not be <code>null</code>.
     * @param mapper The mapper callback.
     * @return A Map containing the results.
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws InvalidResultException if the keys are non-unique in the result
     *             set.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E, S extends Record> Map<S, E> intoMap(Table<S> table, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        InvalidResultException, MappingException;

    /**
     * Return a {@link Map} with one of the result's columns as key and a list
     * of corresponding records as value.
     * <p>
     * Unlike {@link #intoMap(Field)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param <K> The key's generic field type
     * @param key The key field.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <K> Map<K, Result<R>> intoGroups(Field<K> key) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and a list
     * of corresponding records as value.
     * <p>
     * Unlike {@link #intoMap(int)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param keyFieldIndex The key field index.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument field index is not
     *             contained in {@link #fieldsRow()}
     */
    Map<?, Result<R>> intoGroups(int keyFieldIndex) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and a list
     * of corresponding records as value.
     * <p>
     * Unlike {@link #intoMap(String)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param keyFieldName The key field name.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     */
    Map<?, Result<R>> intoGroups(String keyFieldName) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and a list
     * of corresponding records as value.
     * <p>
     * Unlike {@link #intoMap(Name)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param keyFieldName The key field name.
     * @return A Map containing the results
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     */
    Map<?, Result<R>> intoGroups(Name keyFieldName) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value.
     * <p>
     * Unlike {@link #intoMap(Field, Field)}, this method allows for non-unique
     * keys in the result set.
     *
     * @param <K> The key's generic field type
     * @param <V> The value's generic field type
     * @param key The key field.
     * @param value The value field
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     */
    <K, V> Map<K, List<V>> intoGroups(Field<K> key, Field<V> value) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value.
     * <p>
     * Unlike {@link #intoMap(int, int)}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keyFieldIndex The key field index.
     * @param valueFieldIndex The value field index.
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     */
    Map<?, List<?>> intoGroups(int keyFieldIndex, int valueFieldIndex) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value.
     * <p>
     * Unlike {@link #intoMap(String, String)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldName The key field name.
     * @param valueFieldName The value field name.
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<?, List<?>> intoGroups(String keyFieldName, String valueFieldName) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with one of the result's columns as key and another
     * one of the result's columns as value.
     * <p>
     * Unlike {@link #intoMap(Name, Name)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldName The key field name.
     * @param valueFieldName The value field name.
     * @return A Map containing the results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<?, List<?>> intoGroups(Name keyFieldName, Name valueFieldName) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     *
     * @param <K> The key's generic field type
     * @param <E> The generic entity type.
     * @param key The key field.
     * @param type The entity type.
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, E> Map<K, List<E>> intoGroups(Field<K> key, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     *
     * @param keyFieldIndex The key field index.
     * @param type The entity type.
     * @throws IllegalArgumentException If the argument field index is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, List<E>> intoGroups(int keyFieldIndex, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     *
     * @param keyFieldName The key field name.
     * @param type The entity type.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, List<E>> intoGroups(String keyFieldName, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     *
     * @param keyFieldName The key field name.
     * @param type The entity type.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<?, List<E>> intoGroups(Name keyFieldName, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     *
     * @param <K> The key's generic field type
     * @param <E> The generic entity type.
     * @param key The key field.
     * @param mapper The mapper callback.
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    <K, E> Map<K, List<E>> intoGroups(Field<K> key, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     *
     * @param keyFieldIndex The key field index.
     * @param mapper The mapper callback.
     * @throws IllegalArgumentException If the argument field index is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    <E> Map<?, List<E>> intoGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     *
     * @param keyFieldName The key field name.
     * @param mapper The mapper callback.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    <E> Map<?, List<E>> intoGroups(String keyFieldName, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key and mapped by
     * the given mapper.
     *
     * @param keyFieldName The key field name.
     * @param mapper The mapper callback.
     * @throws IllegalArgumentException If the argument field name is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    <E> Map<?, List<E>> intoGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(Field[])}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keys The keys. If this is <code>null</code> or an empty array, the
     *            resulting map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     */
    Map<Record, Result<R>> intoGroups(Field<?>[] keys) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(int[])}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keyFieldIndexes The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<R>> intoGroups(int[] keyFieldIndexes) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(String[])}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<R>> intoGroups(String[] keyFieldNames) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(Name[])}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<R>> intoGroups(Name[] keyFieldNames) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(Field[], Field[])}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keys The keys. If this is <code>null</code> or an empty array, the
     *            resulting map will contain at most one entry.
     * @param values The values.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     */
    Map<Record, Result<Record>> intoGroups(Field<?>[] keys, Field<?>[] values) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(int[], int[])}, this method allows for non-unique keys in
     * the result set.
     *
     * @param keyFieldIndexes The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param valueFieldIndexes The values.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field indexes is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<Record>> intoGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(String[], String[])}, this method allows for non-unique keys
     * in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param valueFieldNames The values.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<Record>> intoGroups(String[] keyFieldNames, String[] valueFieldNames) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given keys.
     * <p>
     * Unlike {@link #intoMap(Name[], Name[])}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param valueFieldNames The values.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument field names is
     *             not contained in {@link #fieldsRow()}
     */
    Map<Record, Result<Record>> intoGroups(Name[] keyFieldNames, Name[] valueFieldNames) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Field[], Class)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keys The keys. If this is <code>null</code> or an empty array, the
     *            resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(int[], Class)}, this method allows for non-unique
     * keys in the result set.
     *
     * @param keyFieldIndexes The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field indexes
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(String[], Class)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field names
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Name[], Class)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param type The entity type.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field names
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, Class<? extends E> type) throws IllegalArgumentException,
        MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Field[], RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keys The keys. If this is <code>null</code> or an empty array, the
     *            resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper)
        throws IllegalArgumentException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(int[], RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldIndexes The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field indexes
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper)
        throws IllegalArgumentException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(String[], RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field indexes
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper)
        throws IllegalArgumentException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Name[], RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyFieldNames The keys. If this is <code>null</code> or an empty
     *            array, the resulting map will contain at most one entry.
     * @param mapper The mapper callback.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument field indexes
     *             is not contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E> Map<Record, List<E>> intoGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper)
        throws IllegalArgumentException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(Class)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K> Map<K, Result<R>> intoGroups(Class<? extends K> keyType) throws MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(Class, Class)}, this method allows for non-unique
     * keys in the result set.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @param valueType The value type.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, Class<? extends V> valueType) throws MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(Class, RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyType The key type. If this is <code>null</code>, the resulting
     *            map will contain at most one entry.
     * @param valueMapper The value mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, List<V>> intoGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper)
        throws MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(RecordMapper, RecordMapper)}, this method allows
     * for non-unique keys in the result set.
     *
     * @param keyMapper The key mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K> Map<K, Result<R>> intoGroups(RecordMapper<? super R, K> keyMapper) throws MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(RecordMapper, Class)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keyMapper The key mapper.
     * @param valueType The value type.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) throws MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key entity and
     * mapped into the given entity type.
     * <p>
     * The grouping semantics is governed by the key type's
     * {@link Object#equals(Object)} and {@link Object#hashCode()}
     * implementation, not necessarily the values as fetched from the database.
     * <p>
     * Unlike {@link #intoMap(RecordMapper, RecordMapper)}, this method allows
     * for non-unique keys in the result set.
     *
     * @param keyMapper The key mapper.
     * @param valueMapper The value mapper.
     * @return A Map containing grouped results
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <K, V> Map<K, List<V>> intoGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper)
        throws MappingException;

    /**
     * Return a {@link Map} with the result grouped by the given key table.
     * <p>
     * Unlike {@link #intoMap(Table)}, this method allows for non-unique keys in
     * the result set.
     *
     * @param table The key table. May not be <code>null</code>.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     */
    <S extends Record> Map<S, Result<R>> intoGroups(Table<S> table) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with the result grouped by the given key table.
     * <p>
     * Unlike {@link #intoMap(Table, Table)}, this method allows for non-unique
     * keys in the result set.
     *
     * @param keyTable The key table. May not be <code>null</code>.
     * @param valueTable The value table. May not be <code>null</code>.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     */
    <S extends Record, T extends Record> Map<S, Result<T>> intoGroups(Table<S> keyTable, Table<T> valueTable) throws IllegalArgumentException;

    /**
     * Return a {@link Map} with results grouped by the given key table and
     * mapped into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Table, Class)}, this method allows for non-unique
     * keys in the result set.
     *
     * @param table The key table. May not be <code>null</code>.
     * @param type The entity type.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, Class<? extends E> type)
        throws IllegalArgumentException, MappingException;

    /**
     * Return a {@link Map} with results grouped by the given key table and
     * mapped into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Table, RecordMapper)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param table The key table. May not be <code>null</code>.
     * @param mapper The mapper callback.
     * @return A Map containing grouped results
     * @throws IllegalArgumentException If the any of the argument fields is not
     *             contained in {@link #fieldsRow()}
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    <E, S extends Record> Map<S, List<E>> intoGroups(Table<S> table, RecordMapper<? super R, E> mapper)
        throws IllegalArgumentException, MappingException;

    /**
     * @deprecated - 3.6.0 - [#3879] - Use {@link #intoArrays()} instead.
     */
    @Deprecated
    Object[][] intoArray();

    /**
     * Convert this result into an array of arrays.
     * <p>
     * The resulting array has the same number of first-dimension elements as
     * this result has records. It has the same number of second-dimension
     * elements as this result's records have fields. The resulting array
     * contains data as such:
     * <p>
     * <code><pre>
     * // For arbitrary values of i, j
     * result.getValue(i, j) == result.intoArray()[i][j]
     * </pre></code>
     *
     * @return This result as an array of arrays
     * @see Record#intoArray()
     */
    Object[][] intoArrays();

    /**
     * Return all values for a field index from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldIndex)[recordIndex]</pre></code>
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldIndex</code>'s actual type.
     * @see #getValues(int)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     */
    Object[] intoArray(int fieldIndex) throws IllegalArgumentException;

    /**
     * Return all values for a field index from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldIndex)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(int, Class)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> T[] intoArray(int fieldIndex, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field index from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldIndex)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(int, Converter)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> U[] intoArray(int fieldIndex, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldName</code>'s actual type.
     * @see #getValues(String)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    Object[] intoArray(String fieldName) throws IllegalArgumentException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(String, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> T[] intoArray(String fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(String, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> U[] intoArray(String fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldName</code>'s actual type.
     * @see #getValues(Name)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    Object[] intoArray(Name fieldName) throws IllegalArgumentException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(Name, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> T[] intoArray(Name fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(fieldName)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(Name, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> U[] intoArray(Name fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(field)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(Field)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> T[] intoArray(Field<T> field) throws IllegalArgumentException;

    /**
     * Return all values for a field from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(field)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(Field, Class)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> T[] intoArray(Field<?> field, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field from the result.
     * <p>
     * You can access data like this
     * <code><pre>result.intoArray(field)[recordIndex]</pre></code>
     *
     * @return The resulting values.
     * @see #getValues(Field, Converter)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T, U> U[] intoArray(Field<T> field, Converter<? super T, ? extends U> converter) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Map results into a custom mapper callback.
     *
     * @param mapper The mapper callback
     * @return The custom mapped records
     */
    <E> Set<E> intoSet(RecordMapper<? super R, E> mapper);

    /**
     * Return all values for a field index from the result.
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldIndex</code>'s actual type.
     * @see #getValues(int)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     */
    Set<?> intoSet(int fieldIndex) throws IllegalArgumentException;

    /**
     * Return all values for a field index from the result.
     *
     * @return The resulting values.
     * @see #getValues(int, Class)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> Set<T> intoSet(int fieldIndex, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field index from the result.
     *
     * @return The resulting values.
     * @see #getValues(int, Converter)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> Set<U> intoSet(int fieldIndex, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldName</code>'s actual type.
     * @see #getValues(String)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    Set<?> intoSet(String fieldName) throws IllegalArgumentException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values.
     * @see #getValues(String, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> Set<T> intoSet(String fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values.
     * @see #getValues(String, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> Set<U> intoSet(String fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values. This may be an array type more concrete
     *         than <code>Object[]</code>, depending on whether jOOQ has any
     *         knowledge about <code>fieldName</code>'s actual type.
     * @see #getValues(Name)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     */
    Set<?> intoSet(Name fieldName) throws IllegalArgumentException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values.
     * @see #getValues(Name, Class)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> Set<T> intoSet(Name fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field name from the result.
     *
     * @return The resulting values.
     * @see #getValues(Name, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> Set<U> intoSet(Name fieldName, Converter<?, ? extends U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field from the result.
     *
     * @return The resulting values.
     * @see #getValues(Field)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> Set<T> intoSet(Field<T> field) throws IllegalArgumentException;

    /**
     * Return all values for a field from the result.
     *
     * @return The resulting values.
     * @see #getValues(Field, Class)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T> Set<T> intoSet(Field<?> field, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Return all values for a field from the result.
     *
     * @return The resulting values.
     * @see #getValues(Field, Converter)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <T, U> Set<U> intoSet(Field<T> field, Converter<? super T, ? extends U> converter) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @param fields The fields of the new records
     * @return The new result
     */
    Result<Record> into(Field<?>... fields);



    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1> Result<Record1<T1>> into(Field<T1> field1);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2> Result<Record2<T1, T2>> into(Field<T1> field1, Field<T2> field2);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3> Result<Record3<T1, T2, T3>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4> Result<Record4<T1, T2, T3, T4>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5> Result<Record5<T1, T2, T3, T4, T5>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6> Result<Record6<T1, T2, T3, T4, T5, T6>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7> Result<Record7<T1, T2, T3, T4, T5, T6, T7>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8> Result<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> Result<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Result<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Result<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Result<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Result<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Result<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Result<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Result<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Result<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Result<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Result<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Result<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Result<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Copy all records from this result into a new result with new records
     * holding only a subset of the previous fields.
     *
     * @return The new result
     */
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Result<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> into(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);



    /**
     * Map resulting records onto a custom type.
     * <p>
     * This is the same as calling <code>record.into(type)</code> on every
     * record contained in this <code>Result</code>. See
     * {@link Record#into(Class)} for more details
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see Record#into(Class)
     * @see DefaultRecordMapper
     */
    <E> List<E> into(Class<? extends E> type) throws MappingException;

    /**
     * Map resulting records onto a custom record.
     * <p>
     * This is the same as calling <code>record.into(table)</code> on every
     * record contained in this <code>Result</code>. See
     * {@link Record#into(Table)} for more details
     *
     * @param <Z> The generic table record type.
     * @param table The table type.
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see Record#into(Table)
     */
    <Z extends Record> Result<Z> into(Table<Z> table) throws MappingException;

    /**
     * Map results into a custom handler callback.
     *
     * @param handler The handler callback
     * @return Convenience result, returning the parameter handler itself
     */
    <H extends RecordHandler<? super R>> H into(H handler);

    /**
     * Generate an in-memory JDBC {@link ResultSet} containing the data of this
     * <code>Result</code>.
     * <p>
     * Use this as an adapter for JDBC-compliant code that expects a
     * {@link ResultSet} to operate on, rather than a jOOQ {@link Result}. The
     * returned <code>ResultSet</code> allows for the following behaviour
     * according to the JDBC specification:
     * <ul>
     * <li> {@link ResultSet#CLOSE_CURSORS_AT_COMMIT}: The cursors (i.e.
     * {@link Statement} object) are no longer available</li>
     * <li> {@link ResultSet#CONCUR_READ_ONLY}: You cannot update the database
     * through this <code>ResultSet</code>, as the underlying {@link Result}
     * object does not hold any open database refences anymore</li>
     * <li> {@link ResultSet#FETCH_FORWARD}: The fetch direction is forward only,
     * and cannot be changed</li>
     * <li> {@link ResultSet#TYPE_SCROLL_INSENSITIVE}: You can use any of the
     * <code>ResultSet</code>'s scrolling methods, e.g. {@link ResultSet#next()}
     * or {@link ResultSet#previous()}, etc.</li>
     * </ul>
     * <p>
     * You may use {@link DSLContext#fetch(ResultSet)} to unwind this wrapper
     * again.
     *
     * @return A wrapper JDBC <code>ResultSet</code>
     */
    ResultSet intoResultSet();

    /**
     * Map results into a custom mapper callback.
     *
     * @param mapper The mapper callback
     * @return The custom mapped records
     */
    <E> List<E> map(RecordMapper<? super R, E> mapper);

    /**
     * Sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param field The sort field
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T extends Comparable<? super T>> Result<R> sortAsc(Field<T> field) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param field The sort field
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T extends Comparable<? super T>> Result<R> sortDesc(Field<T> field) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldIndex The sort field index
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(int fieldIndex) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldIndex The sort field index
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(int fieldIndex) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldName The sort field name
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(String fieldName) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldName The sort field name
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(String fieldName) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldName The sort field name
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(Name fieldName) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields.
     * <p>
     * <code>nulls</code> are sorted last by this method.
     *
     * @param fieldName The sort field name
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(Name fieldName) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields using a comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param field The sort field
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> Result<R> sortAsc(Field<T> field, java.util.Comparator<? super T> comparator) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields using a
     * comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param field The sort field
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> Result<R> sortDesc(Field<T> field, java.util.Comparator<? super T> comparator) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields using a comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldIndex The sort field index
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(int fieldIndex, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields using a
     * comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldIndex The sort field index
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(int fieldIndex, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields using a comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldName The sort field name
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(String fieldName, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields using a
     * comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldName The sort field name
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(String fieldName, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Sort this result by one of its contained fields using a comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldName The sort field name
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortAsc(Name fieldName, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Reverse-sort this result by one of its contained fields using a
     * comparator.
     * <p>
     * <code>null</code> sorting must be handled by the supplied
     * <code>comparator</code>.
     *
     * @param fieldName The sort field name
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    Result<R> sortDesc(Name fieldName, java.util.Comparator<?> comparator) throws IllegalArgumentException;

    /**
     * Sort this result using a comparator that can compare records.
     *
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     */
    Result<R> sortAsc(java.util.Comparator<? super R> comparator);

    /**
     * Reverse-sort this result using a comparator that can compare records.
     *
     * @param comparator The comparator used to sort this result.
     * @return The result itself
     */
    Result<R> sortDesc(java.util.Comparator<? super R> comparator);

    /**
     * Specify a set of fields whose values should be interned.
     * <p>
     * See {@link Result#intern(int...)} for more details.
     *
     * @param fields The fields whose values should be interned
     * @return The same result
     * @see Result#intern(Field...)
     * @see String#intern()
     *
     * @deprecated - 3.10 - [#6254] - This functionality is no longer supported
     *             and will be removed in 4.0
     */
    @Deprecated
    Result<R> intern(Field<?>... fields);

    /**
     * Specify a set of field indexes whose values should be interned.
     * <p>
     * This traverses all records and interns <code>String</code> values for a
     * given set of field indexes. Use this method to save memory when a large
     * result set contains many identical string literals.
     * <p>
     * Please refer to {@link String#intern()} and to publicly available
     * literature to learn more about potential side-effects of string
     * interning.
     * <p>
     * Future versions of jOOQ may also "intern" other data types, such as
     * {@link Integer}, {@link Long}, within a <code>Result</code> object.
     *
     * @param fieldIndexes The field indexes whose values should be interned
     * @return The same result
     * @see Result#intern(Field...)
     * @see String#intern()
     *
     * @deprecated - 3.10 - [#6254] - This functionality is no longer supported
     *             and will be removed in 4.0
     */
    @Deprecated
    Result<R> intern(int... fieldIndexes);

    /**
     * Specify a set of field names whose values should be interned.
     * <p>
     * See {@link Result#intern(int...)} for more details.
     *
     * @param fieldNames The field names whose values should be interned
     * @return The same result
     * @see Result#intern(Field...)
     * @see String#intern()
     *
     * @deprecated - 3.10 - [#6254] - This functionality is no longer supported
     *             and will be removed in 4.0
     */
    @Deprecated
    Result<R> intern(String... fieldNames);

    /**
     * Specify a set of field names whose values should be interned.
     * <p>
     * See {@link Result#intern(int...)} for more details.
     *
     * @param fieldNames The field names whose values should be interned
     * @return The same result
     * @see Result#intern(Field...)
     * @see String#intern()
     *
     * @deprecated - 3.10 - [#6254] - This functionality is no longer supported
     *             and will be removed in 4.0
     */
    @Deprecated
    Result<R> intern(Name... fieldNames);

    // ------------------------------------------------------------------------
    // Fetching of new results based on records in this result
    // ------------------------------------------------------------------------

    /**
     * Fetch parent records of this record, given a foreign key.
     *
     * @throws DataAccessException if something went wrong executing the query.
     * @see ForeignKey#fetchParent(Record)
     * @see ForeignKey#fetchParents(java.util.Collection)
     * @see ForeignKey#fetchParents(Record...)
     */
    <O extends UpdatableRecord<O>> Result<O> fetchParents(ForeignKey<R, O> key) throws DataAccessException;

    /**
     * Fetch child records of this record, given a foreign key.
     *
     * @throws DataAccessException if something went wrong executing the query.
     * @see ForeignKey#fetchChildren(java.util.Collection)
     * @see ForeignKey#fetchChildren(Record)
     * @see ForeignKey#fetchChildren(Record...)
     */
    <O extends TableRecord<O>> Result<O> fetchChildren(ForeignKey<O, R> key) throws DataAccessException;

    // ------------------------------------------------------------------------
    // Specialisations of Attachable methods
    // ------------------------------------------------------------------------

    /**
     * Attach this result and all of its contained records to a new
     * {@link Configuration}.
     *
     * @param configuration A configuration or <code>null</code>, if you wish to
     *            detach this <code>Attachable</code> from its previous
     *            configuration.
     */
    @Override
    void attach(Configuration configuration);

    /**
     * Detach this result and all of its contained records from their current
     * {@link Configuration}.
     * <p>
     * This is the same as calling <code>attach(null)</code>.
     */
    @Override
    void detach();

}
