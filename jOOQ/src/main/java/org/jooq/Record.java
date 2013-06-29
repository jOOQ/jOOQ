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

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.Statement;
import java.util.Map;

import javax.persistence.Column;

import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.impl.DefaultRecordMapperProvider;
import org.jooq.tools.Convert;

/**
 * A database result record.
 * <p>
 * A record essentially combines a list of columns ({@link Field}) with a
 * corresponding list of values, each value being of the respective field's
 * type.
 * <p>
 * While records can be seen as generic column / value mappings, their concrete
 * implementations often specialise the above description in any of the
 * following ways:
 * <p>
 * <h5>Table records</h5>
 * <p>
 * Records originating from a concrete database table (or view) are modelled by
 * jOOQ as {@link TableRecord} or {@link UpdatableRecord}, if they contain a
 * primary key. If you're using jOOQ's code generator, you can generate even
 * more concrete types of table records, i.e. one table record per table.
 * <p>
 * <h5>UDT records</h5>
 * <p>
 * {@link SQLDialect#ORACLE} and {@link SQLDialect#POSTGRES} formally support
 * user defined types (UDT), which are modelled by jOOQ as {@link UDTRecord}. In
 * addition to being regular records (column / value mappings), they also
 * implement the JDBC {@link SQLData} API in order to be streamed to a JDBC
 * {@link PreparedStatement} or from a JDBC {@link ResultSet}
 * <p>
 * <h5>Records of well-defined degree</h5>
 * <p>
 * When projecting custom record types in SQL, new ad-hoc types of a certain
 * degree are formed on the fly. Records with degree &lt= 22 are reflected by
 * jOOQ through the {@link Record1}, {@link Record2}, ... {@link Record22}
 * classes, which cover the respective row value expressions {@link Row1},
 * {@link Row2}, ... {@link Row22}
 * <p>
 * Note that generated <code>TableRecords</code> and <code>UDTRecords</code>
 * also implement a <code>Record[N]</code> interface, if <code>N &lt;= 22</code>
 * <p>
 * <h5>Record implements Comparable</h5>
 * <p>
 * jOOQ records have a natural ordering implemented in the same way as this is
 * defined in the SQL standard. For more details, see the
 * {@link #compareTo(Record)} method
 *
 * @author Lukas Eder
 * @see Result
 */
public interface Record extends Attachable, Comparable<Record> {

    /**
     * Get this record's fields as a {@link Row}.
     */
    Row fieldsRow();

    /**
     * Get a specific field from this Record.
     *
     * @see Row#field(Field)
     */
    <T> Field<T> field(Field<T> field);

    /**
     * Get a specific field from this Record.
     *
     * @see Row#field(String)
     */
    Field<?> field(String name);

    /**
     * Get a specific field from this Record.
     *
     * @see Row#field(int)
     */
    Field<?> field(int index);

    /**
     * Get all fields from this Record.
     *
     * @see Row#fields()
     */
    Field<?>[] fields();

    /**
     * Get all fields from this Record, providing some fields.
     *
     * @return All available fields
     * @see Row#fields(Field...)
     */
    Field<?>[] fields(Field<?>... fields);

    /**
     * Get all fields from this Record, providing some field names.
     *
     * @return All available fields
     * @see Row#fields(String...)
     */
    Field<?>[] fields(String... fieldNames);

    /**
     * Get all fields from this Record, providing some field indexes.
     *
     * @return All available fields
     * @see Row#fields(int...)
     */
    Field<?>[] fields(int... fieldIndexes);

    /**
     * Get this record's values as a {@link Row}.
     */
    Row valuesRow();

    /**
     * Get a value from this Record, providing a field.
     *
     * @param <T> The generic field parameter
     * @param field The field
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> T getValue(Field<T> field) throws IllegalArgumentException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param <T> The generic field parameter
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field contained in this record, or defaultValue,
     *         if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     */
    <T> T getValue(Field<T> field, T defaultValue) throws IllegalArgumentException;

    /**
     * Get a converted value from this Record, providing a field.
     *
     * @param <T> The conversion type parameter
     * @param field The field
     * @param type The conversion type
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(Field<?> field, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a converted value from this record, providing a field.
     *
     * @param <T> The conversion type parameter
     * @param field The field
     * @param type The conversion type
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field contained in this record, or defaultValue,
     *         if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(Field<?> field, Class<? extends T> type, T defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a converted value from this Record, providing a field.
     *
     * @param <T> The database type parameter
     * @param <U> The conversion type parameter
     * @param field The field
     * @param converter The data type converter
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <T, U> U getValue(Field<T> field, Converter<? super T, U> converter) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a converted value from this record, providing a field.
     *
     * @param <T> The database type parameter
     * @param <U> The conversion type parameter
     * @param field The field
     * @param converter The data type converter
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field contained in this record, or defaultValue,
     *         if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #fieldsRow()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <T, U> U getValue(Field<T> field, Converter<? super T, U> converter, U defaultValue)
        throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     */
    Object getValue(String fieldName) throws IllegalArgumentException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's name contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     */
    Object getValue(String fieldName, Object defaultValue) throws IllegalArgumentException;

    /**
     * Get a converted value from this Record, providing a field name.
     *
     * @param <T> The conversion type parameter
     * @param fieldName The field's name
     * @param type The conversion type
     * @return The value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(String fieldName, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a converted value from this record, providing a field name.
     *
     * @param <T> The conversion type parameter
     * @param fieldName The field's name
     * @param type The conversion type
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's name contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(String fieldName, Class<? extends T> type, T defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a converted value from this Record, providing a field name.
     *
     * @param <U> The conversion type parameter
     * @param fieldName The field's name
     * @param converter The data type converter
     * @return The value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <U> U getValue(String fieldName, Converter<?, U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a converted value from this record, providing a field name.
     *
     * @param <U> The conversion type parameter
     * @param fieldName The field's name
     * @param converter The data type converter
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's name contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <U> U getValue(String fieldName, Converter<?, U> converter, U defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this record, providing a field index.
     *
     * @param index The field's index
     * @return The value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Object getValue(int index) throws IllegalArgumentException;

    /**
     * Get a value from this record, providing a field index.
     *
     * @param index The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's index contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Object getValue(int index, Object defaultValue) throws IllegalArgumentException;

    /**
     * Get a converted value from this record, providing a field index.
     *
     * @param <T> The conversion type parameter
     * @param index The field's index
     * @param type The conversion type
     * @return The value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     * @throws DataTypeException wrapping data type conversion exception that
     *             might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(int index, Class<? extends T> type) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a converted value from this record, providing a field index.
     *
     * @param <T> The conversion type parameter
     * @param index The field's index
     * @param type The conversion type
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's index contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     * @throws DataTypeException wrapping data type conversion exception that
     *             might have occurred
     * @see Convert#convert(Object, Class)
     */
    <T> T getValue(int index, Class<? extends T> type, T defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a converted value from this record, providing a field index.
     *
     * @param <U> The conversion type parameter
     * @param index The field's index
     * @param converter The data type converter
     * @return The value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     * @throws DataTypeException wrapping data type conversion exception that
     *             might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <U> U getValue(int index, Converter<?, U> converter) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a converted value from this record, providing a field index.
     *
     * @param <U> The conversion type parameter
     * @param index The field's index
     * @param converter The data type converter
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field's index contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     * @throws DataTypeException wrapping data type conversion exception that
     *             might have occurred
     * @see Convert#convert(Object, Converter)
     */
    <U> U getValue(int index, Converter<?, U> converter, U defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Set a value into this record.
     *
     * @param <T> The generic field parameter
     * @param field The field
     * @param value The value
     */
    <T> void setValue(Field<T> field, T value);

    /**
     * Set a value into this record.
     *
     * @param <T> The generic field parameter
     * @param <U> The conversion type parameter
     * @param field The field
     * @param value The value
     * @param converter The converter used to convert <code>value</code> into an
     *            appropriate type
     */
    <T, U> void setValue(Field<T> field, U value, Converter<T, ? super U> converter);

    /**
     * Get the number of fields of this record.
     */
    int size();

    /**
     * Get this record containing the original values as fetched from the
     * database.
     * <p>
     * Record values can be freely modified after having fetched a record from
     * the database. Every record also references the originally fetched values.
     * This method returns a new record containing those original values.
     *
     * @see #original(Field)
     * @see #original(int)
     * @see #original(String)
     */
    Record original();

    /**
     * Get an original value from this record as fetched from the database.
     * <p>
     * Record values can be freely modified after having fetched a record from
     * the database. Every record also references the originally fetched values.
     * This method returns such an original value for a field.
     *
     * @see #original()
     */
    <T> T original(Field<T> field);

    /**
     * Get an original value from this record as fetched from the database.
     * <p>
     * Record values can be freely modified after having fetched a record from
     * the database. Every record also references the originally fetched values.
     * This method returns such an original value for a field.
     *
     * @see #original()
     */
    Object original(int fieldIndex);

    /**
     * Get an original value from this record as fetched from the database.
     * <p>
     * Record values can be freely modified after having fetched a record from
     * the database. Every record also references the originally fetched values.
     * This method returns such an original value for a field.
     *
     * @see #original()
     */
    Object original(String fieldName);

    /**
     * Check if this record has been changed from its original as fetched from
     * the database.
     * <p>
     * If this returns <code>false</code>, then it can be said that
     * <code>record.equals(record.original())</code> is true.
     *
     * @see #original()
     * @see #changed(Field)
     * @see #changed(int)
     * @see #changed(String)
     */
    boolean changed();

    /**
     * Check if a field's value has been changed from its original as fetched
     * from the database.
     *
     * @see #changed()
     * @see #original(Field)
     */
    boolean changed(Field<?> field);

    /**
     * Check if a field's value has been changed from its original as fetched
     * from the database.
     *
     * @see #changed()
     * @see #original(int)
     */
    boolean changed(int fieldIndex);

    /**
     * Check if a field's value has been changed from its original as fetched
     * from the database.
     *
     * @see #changed()
     * @see #original(String)
     */
    boolean changed(String fieldName);

    /**
     * Set all of this record's internal changed flags to the supplied value.
     * <p>
     * If the <code>changed</code> argument is <code>false</code>, the
     * {@link #original()} values will be reset to the corresponding "current"
     * values as well
     *
     * @see #changed()
     * @see #changed(Field, boolean)
     * @see #changed(int, boolean)
     * @see #changed(String, boolean)
     */
    void changed(boolean changed);

    /**
     * Set this record's internal changed flag to the supplied value for a given
     * field.
     * <p>
     * If the <code>changed</code> argument is <code>false</code>, the
     * {@link #original(Field)} value will be reset to the corresponding
     * "current" value as well
     *
     * @see #changed()
     * @see #changed(Field)
     */
    void changed(Field<?> field, boolean changed);

    /**
     * Set this record's internal changed flag to the supplied value for a given
     * field.
     * <p>
     * If the <code>changed</code> argument is <code>false</code>, the
     * {@link #original(int)} value will be reset to the corresponding "current"
     * value as well
     *
     * @see #changed()
     * @see #changed(int)
     */
    void changed(int fieldIndex, boolean changed);

    /**
     * Set this record's internal changed flag to the supplied value for a given
     * field.
     * <p>
     * If the <code>changed</code> argument is <code>false</code>, the
     * {@link #original(String)} value will be reset to the corresponding
     * "current" value as well
     *
     * @see #changed()
     * @see #changed(String)
     */
    void changed(String fieldName, boolean changed);

    /**
     * Reset all values to their {@link #original()} values and all
     * {@link #changed()} flags to <code>false</code>.
     */
    void reset();

    /**
     * Reset a given value to its {@link #original(Field)} value and its
     * {@link #changed(Field)} flag to <code>false</code>.
     */
    void reset(Field<?> field);

    /**
     * Reset a given value to its {@link #original(int)} value and its
     * {@link #changed(int)} flag to <code>false</code>.
     */
    void reset(int fieldIndex);

    /**
     * Reset a given value to its {@link #original(String)} value and its
     * {@link #changed(String)} flag to <code>false</code>.
     */
    void reset(String fieldName);

    /**
     * Convert this record into an array.
     * <p>
     * The resulting array has the same number of elements as this record has
     * fields. The resulting array contains data as such:
     * <p>
     * <code><pre>
     * // For arbitrary values of i
     * record.getValue(i) == record.intoArray()[i]
     * </pre></code>
     * <p>
     * This is the same as calling <code>into(Object[].class)</code>
     *
     * @return This record as an array
     * @see #fromArray(Object...)
     */
    Object[] intoArray();

    /**
     * Return this record as a name/value map.
     * <p>
     * This is the inverse operation to {@link #fromMap(Map)}
     *
     * @return This record as a map
     * @see #fromMap(Map)
     */
    Map<String, Object> intoMap();

    /**
     * Map resulting records onto a custom type.
     * <p>
     * This will map this record onto your custom type using a
     * {@link RecordMapper} as provided by
     * {@link Configuration#recordMapperProvider()}. If no custom provider is
     * specified, the {@link DefaultRecordMapperProvider} is used.
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #from(Object)
     * @see DefaultRecordMapper
     */
    <E> E into(Class<? extends E> type) throws MappingException;

    /**
     * Map resulting records onto a custom type.
     * <p>
     * This is the same as {@link #into(Class)}, except that no new object is
     * instanciated as a result. Instead, you can provide your own custom POJO
     * instance.
     *
     * @param <E> The generic entity type.
     * @param object The receiving bean.
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @throws NullPointerException if <code>object</code> is <code>null</code>
     * @see #from(Object)
     */
    <E> E into(E object) throws MappingException;

    /**
     * Map resulting records onto a custom record type.
     * <p>
     * The mapping algorithm is this:
     * <p>
     * <h5>jOOQ will map <code>Record</code> values by equal field names:</h5>
     * <p>
     * <ul>
     * <li>For every field in the <code>table</code> argument with
     * {@link Field#getName()} <code>"MY_field"</code> (case-sensitive!), a
     * corresponding field with the same name in this record will be searched.</li>
     * <li>If several fields in this record share the same
     * {@link Field#getName()}, then the first one returning true on
     * {@link Field#equals(Object)} will be returned. (e.g. qualified field
     * names match)</li>
     * </ul>
     * <p>
     * <h5>Other restrictions</h5>
     * <p>
     * <ul>
     * <li>{@link Table#getRecordType()} must return a class of type
     * {@link TableRecord}, which must provide a default constructor. Non-public
     * default constructors are made accessible using
     * {@link Constructor#setAccessible(boolean)}</li>
     * </ul>
     *
     * @param <R> The generic table record type.
     * @param table The table type.
     */
    <R extends Record> R into(Table<R> table);

    /**
     * Generate an in-memory JDBC {@link ResultSet} containing the data of this
     * <code>Record</code>.
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
     * <p>
     * This is the same as creating a new {@link Result} with this
     * <code>Record</code> only, and then calling {@link Result#intoResultSet()}
     * on that <code>Result</code>
     *
     * @return A wrapper JDBC <code>ResultSet</code>
     */
    ResultSet intoResultSet();

    /**
     * Map this record into a custom mapper callback.
     *
     * @param mapper The mapper callback
     * @return The custom mapped record
     */
    <E> E map(RecordMapper<Record, E> mapper);

    /**
     * Load data into this record from a source.
     * <p>
     * The mapping algorithm is this:
     * <p>
     * <h5>If <code>source</code> is an <code>array</code></h5>
     * <p>
     * Loading of data is delegated to {@link #fromArray(Object...)}
     * <p>
     * <h5>If <code>source</code> is a {@link Map}</h5>
     * <p>
     * <p>
     * Loading of data is delegated to {@link #fromMap(Map)}
     * <p>
     * <h5>If any JPA {@link Column} annotations are found on the {@link Class}
     * of the provided <code>source</code>, only those are used. Matching
     * candidates are:</h5>
     * <p>
     * <ul>
     * <li>Public no-argument instance methods annotated with
     * <code>Column</code></li>
     * <li>Public no-argument instance methods starting with <code>getXXX</code>
     * or <code>isXXX</code>, if there exists a matching public single-argument
     * <code>setXXX()</code> instance method that is annotated with
     * <code>Column</code></li>
     * <li>Public instance member fields annotated with <code>Column</code></li>
     * </ul>
     * Additional matching rules:
     * <ul>
     * <li>{@link Column#name()} must match {@link Field#getName()}. All other
     * annotation attributes are ignored</li>
     * <li>Only the first match per field is used</li>
     * <li>Matching methods have a higher priority than matching member fields</li>
     * <li>Explicitly matching methods have a higher priority than implicitly
     * matching methods (implicitly matching getter = setter is annotated)</li>
     * <li>Static methods / member fields are ignored</li>
     * </ul>
     * <p>
     * <h5>If there are no JPA <code>Column</code> annotations, or jOOQ can't
     * find the <code>javax.persistence</code> API on the classpath, jOOQ will
     * map members by naming convention:</h5>
     * <p>
     * If {@link Field#getName()} is <code>MY_field</code> (case-sensitive!),
     * then this field's value will be fetched from the first of these:
     * <ul>
     * <li>Public no-argument instance method <code>MY_field()</code></li>
     * <li>Public no-argument instance method <code>myField()</code></li>
     * <li>Public no-argument instance method <code>getMY_field()</code></li>
     * <li>Public no-argument instance method <code>getMyField()</code></li>
     * <li>Public instance member field <code>MY_field</code></li>
     * <li>Public instance member field <code>myField</code></li>
     * </ul>
     * <p>
     * <h5>Other restrictions</h5>
     * <p>
     * <ul>
     * <li>primitive types are supported.</li>
     * </ul>
     * <p>
     * <h5>General notes</h5>
     * <p>
     * The resulting record will have its internal "changed" flags set to true
     * for all values. This means that {@link UpdatableRecord#store()} will
     * perform an <code>INSERT</code> statement. If you wish to store the record
     * using an <code>UPDATE</code> statement, use
     * {@link DSLContext#executeUpdate(UpdatableRecord)} instead.
     * <p>
     * This is the same as calling
     * <code>record.from(source, record.fields())</code>
     *
     * @param source The source object to copy data from
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     * @see #from(Object, Field...)
     */
    void from(Object source) throws MappingException;

    /**
     * Load data into this record from a source, providing some fields.
     * <p>
     * This is the same as {@link #from(Object)}, except that only fields
     * contained in the <code>fields</code> argument will be mapped.
     *
     * @param source The source object to copy data from
     * @param fields The record's fields to use for mapping
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     * @see #from(Object)
     */
    void from(Object source, Field<?>... fields) throws MappingException;

    /**
     * Load data into this record from a source, providing some field names.
     * <p>
     * This is the same as {@link #from(Object)}, except that only fields
     * contained in the <code>fieldNames</code> argument will be mapped.
     *
     * @param source The source object to copy data from
     * @param fieldNames The record's fields names to use for mapping
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     * @see #from(Object)
     */
    void from(Object source, String... fieldNames) throws MappingException;

    /**
     * Load data into this record from a source, providing some field indexes.
     * <p>
     * This is the same as {@link #from(Object)}, except that only fields
     * contained in the <code>fieldIndexes</code> argument will be mapped.
     *
     * @param source The source object to copy data from
     * @param fieldIndexes The record's fields indexes to use for mapping
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     * @see #from(Object)
     */
    void from(Object source, int... fieldIndexes) throws MappingException;

    /**
     * Load data from a map into this record.
     * <p>
     * The argument map is expected to hold field-name / value pairs where
     * field-names correspond to actual field names as provided by
     * {@link #field(String)}. Missing fields will be left untouched. Excess
     * fields will be ignored.
     * <p>
     * This is the inverse operation to {@link #intoMap()}. This is the same as
     * calling <code>record.fromMap(map, record.fields())</code>
     *
     * @see #intoMap()
     * @see #fromMap(Map, Field...)
     */
    void fromMap(Map<String, ?> map);

    /**
     * Load data from a map into this record, providing some fields.
     * <p>
     * The argument map is expected to hold field-name / value pairs where
     * field-names correspond to actual field names as provided by
     * {@link #field(String)}. Missing fields will be left untouched. Excess
     * fields will be ignored.
     * <p>
     * This is the same as {@link #fromMap(Map)}, except that only fields
     * contained in the <code>fields</code> argument will be mapped.
     *
     * @see #intoMap()
     * @see #fromMap(Map)
     */
    void fromMap(Map<String, ?> map, Field<?>... fields);

    /**
     * Load data from a map into this record, providing some field names.
     * <p>
     * The argument map is expected to hold field-name / value pairs where
     * field-names correspond to actual field names as provided by
     * {@link #field(String)}. Missing fields will be left untouched. Excess
     * fields will be ignored.
     * <p>
     * This is the same as {@link #fromMap(Map)}, except that only fields
     * contained in the <code>fieldNames</code> argument will be mapped.
     *
     * @see #intoMap()
     * @see #fromMap(Map)
     */
    void fromMap(Map<String, ?> map, String... fieldNames);

    /**
     * Load data from a map into this record, providing some field indexes.
     * <p>
     * The argument map is expected to hold field-name / value pairs where
     * field-names correspond to actual field names as provided by
     * {@link #field(String)}. Missing fields will be left untouched. Excess
     * fields will be ignored.
     * <p>
     * This is the same as {@link #fromMap(Map)}, except that only fields
     * contained in the <code>fieldIndexes</code> argument will be mapped.
     *
     * @see #intoMap()
     * @see #fromMap(Map)
     */
    void fromMap(Map<String, ?> map, int... fieldIndexes);

    /**
     * Load data from an array into this record.
     * <p>
     * The argument array is expected to hold values for this record's field
     * indexes. Missing values will be left untouched. Excess values will be
     * ignored.
     * <p>
     * This is the inverse operation to {@link #intoArray()}
     *
     * @see #intoArray()
     * @see #fromArray(Object[], Field...)
     */
    void fromArray(Object... array);

    /**
     * Load data from an array into this record, providing some fields.
     * <p>
     * The argument array is expected to hold values for this record's field
     * indexes. Missing values will be left untouched. Excess values will be
     * ignored.
     * <p>
     * This is the same as {@link #fromArray(Object...)}, except that only
     * fields contained in the <code>fields</code> argument will be mapped.
     *
     * @see #intoArray()
     * @see #fromArray(Object...)
     */
    void fromArray(Object[] array, Field<?>... fields);

    /**
     * Load data from an array into this record, providing some fields names.
     * <p>
     * The argument array is expected to hold values for this record's field
     * indexes. Missing values will be left untouched. Excess values will be
     * ignored.
     * <p>
     * This is the same as {@link #fromArray(Object...)}, except that only
     * fields contained in the <code>fieldNames</code> argument will be mapped.
     *
     * @see #intoArray()
     * @see #fromArray(Object...)
     */
    void fromArray(Object[] array, String... fieldNames);

    /**
     * Load data from an array into this record, providing some fields indexes.
     * <p>
     * The argument array is expected to hold values for this record's field
     * indexes. Missing values will be left untouched. Excess values will be
     * ignored.
     * <p>
     * This is the same as {@link #fromArray(Object...)}, except that only
     * fields contained in the <code>fieldIndexes</code> argument will be
     * mapped.
     *
     * @see #intoArray()
     * @see #fromArray(Object...)
     */
    void fromArray(Object[] array, int... fieldIndexes);

    // -------------------------------------------------------------------------
    // Inherited methods
    // -------------------------------------------------------------------------

    /**
     * Get a hash code of this <code>Record</code>, based on the underlying row
     * value expression.
     * <p>
     * In order to fulfill the general contract of {@link Object#hashCode()} and
     * {@link Object#equals(Object)}, a <code>Record</code>'s hash code value
     * depends on all hash code values of this <code>Record</code>'s underlying
     * column values.
     *
     * @return A hash code value for this record
     * @see #equals(Object)
     */
    @Override
    int hashCode();

    /**
     * Compare this <code>Record</code> with another <code>Record</code> for
     * equality.
     * <p>
     * Two records are considered equal if
     * <ul>
     * <li>They have the same degree</li>
     * <li>For every <code>i BETWEEN 0 AND degree, r1[i] = r2[i]</code></li>
     * </ul>
     * <p>
     * Note, that the above rules correspond to the SQL comparison predicate
     * behaviour as illustrated in the following example: <code><pre>
     * -- A row value expression comparison predicate
     * SELECT *
     * FROM my_table
     * WHERE (1, 'A') = (1, 'A')
     * </pre></code>
     * <p>
     * Unlike SQL, jOOQ allows to compare also incompatible records, e.g.
     * records
     * <ul>
     * <li>... whose degrees are not equal (results in <code>false</code>)</li>
     * <li>... whose column types are not equal (results in <code>false</code>)</li>
     * <li>... whose record types are not equal (irrelevant for the result)</li>
     * </ul>
     * <p>
     * It can be said that for all R1, R2, if <code>R1.equal(R2)</code>, then
     * <code>R1.compareTo(R2) == 0</code>
     *
     * @param other The other record
     * @return Whether the two records are equal
     * @see #compareTo(Record)
     * @see #hashCode()
     */
    @Override
    boolean equals(Object other);

    /**
     * Compares this <code>Record</code> with another <code>Record</code>
     * according to their natural ordering.
     * <p>
     * jOOQ Records implement {@link Comparable} to allow for naturally ordering
     * Records in a "SQL way", i.e. according to the following rules:
     * <p>
     * <h5>Records being compared must have the same ROW type</h5>
     * <p>
     * Two Records are comparable if and only if they have the same
     * <code>ROW</code> type, i.e. if their {@link Record#fieldsRow()
     * fieldsRow()} methods return fields of the same type and degree.
     * <p>
     * <h5>Comparison rules</h5>
     * <p>
     * Assume the following notations:
     * <ul>
     * <li><code>X[i]</code> means <code>X.getValue(i)</code></li>
     * <li><code>X = Y</code> means <code>X.compareTo(Y) == 0</code></li>
     * <li><code>X &lt; Y</code> means <code>X.compareTo(Y) &lt; 0</code></li>
     * <li><code>X[i] = Y[i]</code> means
     * <code>(X[i] == null && Y[i] == null) || X[i].compareTo(Y[i]) &lt; 0</code>
     * </li>
     * <li><code>X[i] &lt; Y[i]</code> means
     * <code>Y[i] == null || X[i].compareTo(Y[i]) &lt; 0</code>. This
     * corresponds to the SQL <code>NULLS LAST</code> clause.</li>
     * </ul>
     * Then, for two comparable Records <code>r1</code> and <code>r2</code>,
     * <code>x = r1.compareTo(r2)</code> yields:
     * <ul>
     * <li><strong><code>x = -1</code></strong>: if <code><pre>
     *    (r1[0] &lt; r2[0])
     * OR (r1[0] = r2[0] AND r1[1] &lt; r2[1])
     * OR  ...
     * OR (r1[0] = r2[0] AND ... AND r1[N-1] = r2[N-1] AND r1[N] &lt; r2[N])</pre></code>
     * </li>
     * <li><strong><code>x = 0</code></strong>: if <code><pre>
     * OR (r1[0] = r2[0] AND ... AND r1[N-1] = r2[N-1] AND r1[N] = r2[N])</pre></code>
     * </li>
     * <li><strong><code>x = 1</code></strong>: if <code><pre>
     *    (r1[0] > r2[0])
     * OR (r1[0] = r2[0] AND r1[1] > r2[1])
     * OR  ...
     * OR (r1[0] = r2[0] AND ... AND r1[N-1] = r2[N-1] AND r1[N] > r2[N])</pre></code>
     * </li>
     * </ul>
     * <p>
     * Note, that the above rules correspond to the SQL ordering behaviour as
     * illustrated in the following examples: <code><pre>
     * -- A SQL ORDER BY clause, ordering all records by columns in their order
     * SELECT a, b, c
     * FROM my_table
     * ORDER BY 1, 2, 3
     *
     * -- A row value expression comparison predicate
     * SELECT *
     * FROM my_table
     * WHERE (a, b, c) &lt; (1, 2, 3)
     * </pre></code>
     * <p>
     * See {@link Row1#lessThan(Row1)}, {@link Row2#lessThan(Row2)}, ...,
     * {@link Row22#lessThan(Row22)} for more details about row value expression
     * comparison predicates
     * <p>
     * Alternative sorting behaviour can be achieved through
     * {@link Result#sortAsc(java.util.Comparator)} and similar methods.
     *
     * @throws NullPointerException If the argument record is <code>null</code>
     * @throws ClassCastException If the argument record is not comparable with
     *             this record according to the above rules.
     */
    @Override
    int compareTo(Record record);

}
