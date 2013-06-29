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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.jooq.exception.DataTypeException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.tools.Convert;

import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A wrapper for database results returned by <code>{@link SelectQuery}</code>
 *
 * @param <R> The record type contained in this result
 * @author Lukas Eder
 * @see SelectQuery#getResult()
 */
public interface Result<R extends Record> extends List<R>, Attachable {

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
     * Get a specific field from this Result.
     *
     * @see Row#field(int)
     */
    Field<?> field(int index);

    /**
     * Get all fields from this Result.
     *
     * @see Row#fields()
     */
    Field<?>[] fields();

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
     */
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
     */
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
     */
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
     * @see Record#getValue(Field, Class)
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
     * @see Record#getValue(Field, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T, U> List<U> getValues(Field<T> field, Converter<? super T, U> converter) throws IllegalArgumentException;

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
     * @see Record#getValue(int, Class)
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
     * @see Record#getValue(int, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument fieldIndex is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> List<U> getValues(int fieldIndex, Converter<?, U> converter) throws IllegalArgumentException, DataTypeException;

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
     * @see Record#getValue(String, Class)
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
     * @see Record#getValue(String, Converter)
     * @see Convert#convert(Object, Converter)
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    <U> List<U> getValues(String fieldName, Converter<?, U> converter) throws IllegalArgumentException,
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
     * Get a simple formatted representation of this result.
     * <p>
     * This is the same as calling {@link #format(int)} with
     * <code>maxRows = 50</code>
     *
     * @return The formatted result
     */
    String format();

    /**
     * Get a simple formatted representation of this result.
     *
     * @param maxRecords The maximum number of records to include in the
     *            formatted result
     * @return The formatted result
     */
    String format(int maxRecords);

    /**
     * Get a simple formatted representation of this result as HTML.
     * <p>
     * The HTML code is formatted as follows: <code><pre>
     * &lt;table&gt;
     *   &lt;thead&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;field-1&lt;/th&gt;
     *       &lt;th&gt;field-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;field-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *   &lt;/thead&gt;
     *   &lt;tbody&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;value-1-1&lt;/th&gt;
     *       &lt;th&gt;value-1-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;value-1-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *     &lt;tr&gt;
     *       &lt;th&gt;value-2-1&lt;/th&gt;
     *       &lt;th&gt;value-2-2&lt;/th&gt;
     *       ...
     *       &lt;th&gt;value-2-n&lt;/th&gt;
     *     &lt;/tr&gt;
     *     ...
     *   &lt;/tbody&gt;
     * &lt;/table&gt;
     * </pre></code>
     *
     * @return The formatted result
     */
    String formatHTML();

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(',', "")</code>
     *
     * @return The formatted result
     */
    String formatCSV();

    /**
     * Get a simple formatted representation of this result as CSV.
     * <p>
     * This is the same as calling <code>formatCSV(delimiter, "")</code>
     *
     * @param delimiter The delimiter to use between records
     * @return The formatted result
     */
    String formatCSV(char delimiter);

    /**
     * Get a simple formatted representation of this result as CSV.
     *
     * @param delimiter The delimiter to use between records
     * @param nullString A special string for encoding <code>NULL</code> values.
     * @return The formatted result
     */
    String formatCSV(char delimiter, String nullString);

    /**
     * Get a simple formatted representation of this result as a JSON array of
     * array.
     * <p>
     * The format is the following: <code><pre>
     * {"fields":[{"name":"field-1","type":"type-1"},
     *            {"name":"field-2","type":"type-2"},
     *            ...,
     *            {"name":"field-n","type":"type-n"}],
     *  "records":[[value-1-1,value-1-2,...,value-1-n],
     *             [value-2-1,value-2-2,...,value-2-n]]}
     * </pre></code>
     *
     * @return The formatted result
     */
    String formatJSON();

    /**
     * Get this result formatted as XML.
     *
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-2.6.0.xsd">http://www.jooq.org/xsd/jooq-export-2.6.0.xsd</a>
     */
    String formatXML();

    /**
     * Get this result as XML.
     *
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-2.6.0.xsd">http://www.jooq.org/xsd/jooq-export-2.6.0.xsd</a>
     */
    Document intoXML();

    /**
     * Get this result as XML using a SAX <code>ContentHandler</code>.
     *
     * @param handler The custom content handler.
     * @return The argument content handler is returned for convenience.
     * @see #formatXML()
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-2.6.0.xsd">http://www.jooq.org/xsd/jooq-export-2.6.0.xsd</a>
     */
    <H extends ContentHandler> H intoXML(H handler) throws SAXException;

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
     * Return a {@link Map} with results grouped by the given key and mapped
     * into the given entity type.
     * <p>
     * An {@link InvalidResultException} is thrown, if the key is non-unique in
     * the result set. Use {@link #intoGroups(Field, Class)} instead, if your
     * key is non-unique.
     *
     * @param key The key. Client code must assure that key is unique in the
     *            result set.
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
     * Return a {@link Map} with results grouped by the given keys and mapped
     * into the given entity type.
     * <p>
     * Unlike {@link #intoMap(Field[], Class)}, this method allows for
     * non-unique keys in the result set.
     *
     * @param keys The keys. If this is <code>null</code> or an empty array, the
     *            resulting map will contain at most one entry.
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
     * <p>
     * This is the same as calling <code>into(Object[].class)</code>
     *
     * @return This result as an array of arrays
     * @see Record#intoArray()
     */
    Object[][] intoArray();

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
    <U> U[] intoArray(int fieldIndex, Converter<?, U> converter) throws IllegalArgumentException, DataTypeException;

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
    <U> U[] intoArray(String fieldName, Converter<?, U> converter) throws IllegalArgumentException, DataTypeException;

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
    <T, U> U[] intoArray(Field<T> field, Converter<? super T, U> converter) throws IllegalArgumentException,
        DataTypeException;

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
     */
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
     */
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
     */
    Result<R> intern(String... fieldNames);
}
