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

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.impl.Executor;
import org.jooq.tools.Convert;
import org.jooq.tools.reflect.Reflect;

/**
 * A wrapper for database result records returned by
 * <code>{@link SelectQuery}</code>
 *
 * @author Lukas Eder
 * @see SelectQuery#getResult()
 */
public interface Record extends FieldProvider, Attachable {

    /**
     * Get a value from this Record, providing a field.
     *
     * @param <T> The generic field parameter
     * @param field The field
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
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
     *             in {@link #getFields()}
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
     *             in {@link #getFields()}
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
     *             in {@link #getFields()}
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
     *             in {@link #getFields()}
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
     *             in {@link #getFields()}
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
     * Set all of this record's internal changed flags to the supplied value
     * <p>
     * If the <code>changed</code> argument is <code>false</code>, the
     * {@link #original()} values will be reset to the corresponding "current"
     * values as well
     *
     * @see #changed()
     * @see #changed(boolean, Field)
     * @see #changed(boolean, int)
     * @see #changed(boolean, String)
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
    void changed(boolean changed, Field<?> field);

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
    void changed(boolean changed, int fieldIndex);

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
    void changed(boolean changed, String fieldName);

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
     * The mapping algorithm is this:
     * <h3>If <code>type</code> is an array:</h3> The resulting array is of the
     * nature described in {@link #intoArray()}. Arrays more specific than
     * <code>Object[]</code> can be specified as well, e.g.
     * <code>String[]</code>. If conversion fails, a {@link MappingException} is
     * thrown, wrapping conversion exceptions.
     * <p>
     * <h3>If a default constructor is available and any JPA {@link Column}
     * annotations are found on the provided <code>type</code>, only those are
     * used:</h3>
     * <ul>
     * <li>If <code>type</code> contains public single-argument instance methods
     * annotated with <code>Column</code>, those methods are invoked</li>
     * <li>If <code>type</code> contains public no-argument instance methods
     * starting with <code>getXXX</code> or <code>isXXX</code>, annotated with
     * <code>Column</code>, then matching public <code>setXXX()</code> instance
     * methods are invoked</li>
     * <li>If <code>type</code> contains public instance member fields annotated
     * with <code>Column</code>, those members are set</li>
     * </ul>
     * Additional rules:
     * <ul>
     * <li>The same annotation can be re-used for several methods/members</li>
     * <li>{@link Column#name()} must match {@link Field#getName()}. All other
     * annotation attributes are ignored</li>
     * <li>Static methods / member fields are ignored</li>
     * <li>Final member fields are ignored</li>
     * </ul>
     * <h3>If a default constructor is available and if there are no JPA
     * <code>Column</code> annotations, or jOOQ can't find the
     * <code>javax.persistence</code> API on the classpath, jOOQ will map
     * <code>Record</code> values by naming convention:</h3> If
     * {@link Field#getName()} is <code>MY_field</code> (case-sensitive!), then
     * this field's value will be set on all of these:
     * <ul>
     * <li>Public single-argument instance method <code>MY_field(...)</code></li>
     * <li>Public single-argument instance method <code>myField(...)</code></li>
     * <li>Public single-argument instance method <code>setMY_field(...)</code></li>
     * <li>Public single-argument instance method <code>setMyField(...)</code></li>
     * <li>Public non-final instance member field <code>MY_field</code></li>
     * <li>Public non-final instance member field <code>myField</code></li>
     * </ul>
     * <h3>If no default constructor is available, but at least one constructor
     * annotated with <code>ConstructorProperties</code> is available, that one
     * is used</h3>
     * <ul>
     * <li>The standard JavaBeans {@link ConstructorProperties} annotation is
     * used to match constructor arguments against POJO members or getters.</li>
     * <li>If those POJO members or getters have JPA annotations, those will be
     * used according to the aforementioned rules, in order to map
     * <code>Record</code> values onto constructor arguments.</li>
     * <li>If those POJO members or getters don't have JPA annotations, the
     * aforementioned naming conventions will be used, in order to map
     * <code>Record</code> values onto constructor arguments.</li>
     * <li>When several annotated constructors are found, the first one is
     * chosen (as reported by {@link Class#getDeclaredConstructors()}</li>
     * <li>When invoking the annotated constructor, values are converted onto
     * constructor argument types</li>
     * </ul>
     * <h3>If no default constructor is available, but at least one "matching"
     * constructor is available, that one is used</h3>
     * <ul>
     * <li>A "matching" constructor is one with exactly as many arguments as
     * this record holds fields</li>
     * <li>When several "matching" constructors are found, the first one is
     * chosen (as reported by {@link Class#getDeclaredConstructors()}</li>
     * <li>When invoking the "matching" constructor, values are converted onto
     * constructor argument types</li>
     * </ul>
     * <h3>If the supplied type is an interface or an abstract class</h3>
     * Abstract types are instanciated using Java reflection {@link Proxy}
     * mechanisms. The returned proxy will wrap a {@link HashMap} containing
     * properties mapped by getters and setters of the supplied type. Methods
     * (even JPA-annotated ones) other than standard POJO getters and setters
     * are not supported. Details can be seen in {@link Reflect#as(Class)}.
     * <h3>Other restrictions</h3>
     * <ul>
     * <li><code>type</code> must provide a default or a "matching" constructor.
     * Non-public default constructors are made accessible using
     * {@link Constructor#setAccessible(boolean)}</li>
     * <li>primitive types are supported. If a value is <code>null</code>, this
     * will result in setting the primitive type's default value (zero for
     * numbers, or <code>false</code> for booleans). Hence, there is no way of
     * distinguishing <code>null</code> and <code>0</code> in that case.</li>
     * </ul>
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #from(Object)
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
     * <h3>jOOQ will map <code>Record</code> values by equal field names:</h3>
     * <ul>
     * <li>For every field in the <code>table</code> argument with
     * {@link Field#getName()} <code>"MY_field"</code> (case-sensitive!), a
     * corresponding field with the same name in this record will be searched.</li>
     * <li>If several fields in this record share the same
     * {@link Field#getName()}, then the first one returning true on
     * {@link Field#equals(Object)} will be returned. (e.g. qualified field
     * names match)</li>
     * </ul>
     * <h3>Other restrictions</h3>
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
     * You may use {@link Executor#fetch(ResultSet)} to unwind this wrapper
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
     * Map this record into a custom mapper callback
     *
     * @param mapper The mapper callback
     * @return The custom mapped record
     */
    <E> E map(RecordMapper<Record, E> mapper);

    /**
     * Load data into this record from a source. The mapping algorithm is this:
     * <h3>If <code>source</code> is an <code>array</code></h3>
     * <p>
     * Loading of data is delegated to {@link #fromArray(Object...)}
     * <h3>If <code>source</code> is a {@link Map}</h3>
     * <p>
     * Loading of data is delegated to {@link #fromMap(Map)}
     * <h3>If any JPA {@link Column} annotations are found on the {@link Class}
     * of the provided <code>source</code>, only those are used. Matching
     * candidates are:</h3>
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
     * <h3>If there are no JPA <code>Column</code> annotations, or jOOQ can't
     * find the <code>javax.persistence</code> API on the classpath, jOOQ will
     * map members by naming convention:</h3> If {@link Field#getName()} is
     * <code>MY_field</code> (case-sensitive!), then this field's value will be
     * fetched from the first of these:
     * <ul>
     * <li>Public no-argument instance method <code>MY_field()</code></li>
     * <li>Public no-argument instance method <code>myField()</code></li>
     * <li>Public no-argument instance method <code>getMY_field()</code></li>
     * <li>Public no-argument instance method <code>getMyField()</code></li>
     * <li>Public instance member field <code>MY_field</code></li>
     * <li>Public instance member field <code>myField</code></li>
     * </ul>
     * <h3>Other restrictions</h3>
     * <ul>
     * <li>primitive types are supported.</li>
     * </ul>
     * <h3>General notes</h3> The resulting record will have its internal
     * "changed" flags set to true for all values. This means that
     * {@link UpdatableRecord#store()} will perform an <code>INSERT</code>
     * statement. If you wish to store the record using an <code>UPDATE</code>
     * statement, use {@link Executor#executeUpdate(UpdatableRecord)} instead.
     *
     * @param source The source object to copy data from
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     */
    void from(Object source) throws MappingException;

    /**
     * Load data from a map into this record
     * <p>
     * The argument map is expected to hold field-name / value pairs where
     * field-names correspond to actual field names as provided by
     * {@link #getField(String)}. Missing fields will be left untouched. Excess
     * fields will be ignored.
     * <p>
     * This is the inverse operation to {@link #intoMap()}
     *
     * @see #intoMap()
     */
    void fromMap(Map<String, ?> map);

    /**
     * Load data from an array into this record
     * <p>
     * The argument array is expected to hold values for this record's field
     * indexes. Missing values will be left untouched. Excess values will be
     * ignored.
     * <p>
     * This is the inverse operation to {@link #intoArray()}
     */
    void fromArray(Object... array);

}
