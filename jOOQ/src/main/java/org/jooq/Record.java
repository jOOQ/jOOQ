/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;

import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.tools.Convert;

/**
 * A wrapper for database result records returned by
 * <code>{@link SelectQuery}</code>
 *
 * @author Lukas Eder
 * @see SelectQuery#getResult()
 */
public interface Record extends FieldProvider, Store<Object> {

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
     * Get an array value from this Record, providing an {@link ArrayRecord}
     * field.
     *
     * @param <A> The generic field parameter
     * @param <T> The {@link ArrayRecord} type parameter
     * @param field The field
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    <A extends ArrayRecord<T>, T> T[] getValueAsArray(Field<A> field) throws IllegalArgumentException;

    /**
     * Get an array value from this Record, providing an {@link ArrayRecord}
     * field.
     *
     * @param <A> The generic field parameter
     * @param <T> The {@link ArrayRecord} type parameter
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    <A extends ArrayRecord<T>, T> T[] getValueAsArray(Field<A> field, T[] defaultValue) throws IllegalArgumentException;

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
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    String getValueAsString(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    String getValueAsString(Field<?> field, String defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    String getValueAsString(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    String getValueAsString(String fieldName, String defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Byte getValueAsByte(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Byte getValueAsByte(Field<?> field, Byte defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Byte getValueAsByte(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Byte getValueAsByte(String fieldName, Byte defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Short getValueAsShort(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Short getValueAsShort(Field<?> field, Short defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Short getValueAsShort(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Short getValueAsShort(String fieldName, Short defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Integer getValueAsInteger(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Integer getValueAsInteger(Field<?> field, Integer defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Integer getValueAsInteger(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Integer getValueAsInteger(String fieldName, Integer defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Long getValueAsLong(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Long getValueAsLong(Field<?> field, Long defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Long getValueAsLong(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Long getValueAsLong(String fieldName, Long defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigInteger getValueAsBigInteger(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigInteger getValueAsBigInteger(Field<?> field, BigInteger defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigInteger getValueAsBigInteger(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigInteger getValueAsBigInteger(String fieldName, BigInteger defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Float getValueAsFloat(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Float getValueAsFloat(Field<?> field, Float defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Float getValueAsFloat(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Float getValueAsFloat(String fieldName, Float defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Double getValueAsDouble(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Double getValueAsDouble(Field<?> field, Double defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Double getValueAsDouble(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Double getValueAsDouble(String fieldName, Double defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigDecimal getValueAsBigDecimal(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigDecimal getValueAsBigDecimal(Field<?> field, BigDecimal defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigDecimal getValueAsBigDecimal(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    BigDecimal getValueAsBigDecimal(String fieldName, BigDecimal defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     * <p>
     * boolean values for <code>true</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>1</code></li>
     * <li><code>y</code></li>
     * <li><code>yes</code></li>
     * <li><code>true</code></li>
     * <li><code>on</code></li>
     * <li><code>enabled</code></li>
     * </ul>
     * <p>
     * boolean values for <code>false</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>0</code></li>
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code>
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Boolean getValueAsBoolean(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     * <p>
     * boolean values for <code>true</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>1</code></li>
     * <li><code>y</code></li>
     * <li><code>yes</code></li>
     * <li><code>true</code></li>
     * <li><code>on</code></li>
     * <li><code>enabled</code></li>
     * </ul>
     * <p>
     * boolean values for <code>false</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>0</code></li>
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code>
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Boolean getValueAsBoolean(Field<?> field, Boolean defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     * <p>
     * boolean values for <code>true</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>1</code></li>
     * <li><code>y</code></li>
     * <li><code>yes</code></li>
     * <li><code>true</code></li>
     * <li><code>on</code></li>
     * <li><code>enabled</code></li>
     * </ul>
     * <p>
     * boolean values for <code>false</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>0</code></li>
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code>
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Boolean getValueAsBoolean(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     * <p>
     * boolean values for <code>true</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>1</code></li>
     * <li><code>y</code></li>
     * <li><code>yes</code></li>
     * <li><code>true</code></li>
     * <li><code>on</code></li>
     * <li><code>enabled</code></li>
     * </ul>
     * <p>
     * boolean values for <code>false</code> are any of these case-insensitive
     * strings:
     * <ul>
     * <li><code>0</code></li>
     * <li><code>n</code></li>
     * <li><code>no</code></li>
     * <li><code>false</code></li>
     * <li><code>off</code></li>
     * <li><code>disabled</code></li>
     * </ul>
     * <p>
     * All other values evaluate to <code>null</code>
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Boolean getValueAsBoolean(String fieldName, Boolean defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Timestamp getValueAsTimestamp(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Timestamp getValueAsTimestamp(Field<?> field, Timestamp defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Timestamp getValueAsTimestamp(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Timestamp getValueAsTimestamp(String fieldName, Timestamp defaultValue) throws IllegalArgumentException,
        DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Date getValueAsDate(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Date getValueAsDate(Field<?> field, Date defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Date getValueAsDate(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Date getValueAsDate(String fieldName, Date defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field.
     *
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Time getValueAsTime(Field<?> field) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field.
     *
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Time getValueAsTime(Field<?> field, Time defaultValue) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this Record, providing a field name.
     *
     * @param fieldName The field's name
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Time getValueAsTime(String fieldName) throws IllegalArgumentException, DataTypeException;

    /**
     * Get a value from this record, providing a field name.
     *
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument fieldName is not
     *             contained in the record
     * @throws DataTypeException wrapping any data type conversion exception
     *             that might have occurred
     */
    Time getValueAsTime(String fieldName, Time defaultValue) throws IllegalArgumentException, DataTypeException;

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
     * Set a value into this record.
     *
     * @param <T> The generic field parameter
     * @param field The field
     * @param value The value
     */
    <T> void setValue(Field<T> field, T value);

    /**
     * Map resulting records onto a custom type. The mapping algorithm is this:
     * <h3>If any JPA {@link Column} annotations are found on the provided
     * <code>type</code>, only those are used:</h3>
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
     * <h3>If there are no JPA <code>Column</code> annotations, or jOOQ can't
     * find the <code>javax.persistence</code> API on the classpath, jOOQ will
     * map <code>Record</code> values by naming convention:</h3> If
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
     * <h3>Other restrictions</h3>
     * <ul>
     * <li><code>type</code> must provide a default constructor. Non-public
     * default constructors are made accessible using
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
     * Map resulting records onto a custom record type. The mapping algorithm is
     * this: <h3>jOOQ will map <code>Record</code> values by equal field names:</h3>
     * If a field's value for {@link Field#getName()} is <code>MY_field</code>
     * (case-sensitive!), then there must be a field in <code>table</code> with
     * the exact same name. <h3>Other restrictions</h3>
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
     * Load data into this record from a source. The mapping algorithm is this:
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
     *
     * @param source The source object to copy data from
     * @throws MappingException wrapping any reflection exception that might
     *             have occurred while mapping records
     * @see #into(Class)
     */
    void from(Object source) throws MappingException;

}
