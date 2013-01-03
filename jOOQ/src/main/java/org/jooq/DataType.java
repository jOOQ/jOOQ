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

import java.io.Serializable;
import java.sql.Types;
import java.util.Collection;
import java.util.List;

import org.jooq.exception.DataTypeException;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;

/**
 * A common interface to all dialect-specific data types
 *
 * @param <T> The Java type associated with this SQL data type
 * @author Lukas Eder
 */
public interface DataType<T> extends Serializable {

    /**
     * Get the standard SQL data type of this (dialect-specific) data type if
     * available.
     */
    DataType<T> getSQLDataType();

    /**
     * The dialect-specific data type representing this data type.
     */
    DataType<T> getDataType(Configuration configuration);

    /**
     * Get JDBC {@link Types} value
     */
    int getSQLType();

    /**
     * Retrieve the Java type associated with this data type
     */
    Class<T> getType();

    /**
     * Retrieve the Java type associated with ARRAYs of this data type
     */
    Class<T[]> getArrayType();

    /**
     * Retrieve the data type for an ARRAY of this data type
     */
    DataType<T[]> getArrayDataType();

    /**
     * Retrieve the data type for an Oracle-style ARRAY of this data type
     */
    <A extends ArrayRecord<T>> DataType<A> asArrayDataType(Class<A> arrayDataType);

    /**
     * Retrieve the data type for a given enum data type
     */
    <E extends EnumType> DataType<E> asEnumDataType(Class<E> enumDataType);

    /**
     * Retrieve the data type for a given converter
     */
    <U> DataType<U> asConvertedDataType(Converter<? super T, U> converter);

    /**
     * Retrieve the dialect-specific type name associated with this data type
     */
    String getTypeName();

    /**
     * Retrieve the dialect-specific type name associated with this data type
     */
    String getTypeName(Configuration configuration);

    /**
     * Retrieve the dialect-specific type name associated with this data type
     * used for casting
     * <p>
     * This is useful for some dialects that have specialised type names for
     * cast expressions. Other dialects require type-length binding when
     * casting, (e.g. VARCHAR(32767))
     */
    String getCastTypeName();

    /**
     * Retrieve the dialect-specific type name associated with this data type
     * used for casting
     * <p>
     * This is useful for some dialects that have specialised type names for
     * cast expressions. Other dialects require type-length binding when
     * casting, (e.g. VARCHAR(32767))
     */
    String getCastTypeName(Configuration configuration);

    /**
     * Retrieve the underlying {@link SQLDialect}
     */
    SQLDialect getDialect();

    /**
     * Convert an arbitrary object into <code>&lt;T&gt;</code>
     *
     * @param object The object to be converted
     * @return The converted object
     * @throws DataTypeException If conversion fails.
     */
    T convert(Object object);

    /**
     * Convert an arbitrary set of objects into <code>&lt;T&gt;</code>
     *
     * @param objects The objects to be converted
     * @return The converted objects
     * @throws DataTypeException If conversion fails.
     */
    T[] convert(Object... objects);

    /**
     * Convert an arbitrary set of objects into <code>&lt;T&gt;</code>
     *
     * @param objects The objects to be converted
     * @return The converted objects
     * @throws DataTypeException If conversion fails.
     */
    List<T> convert(Collection<?> objects);

    /**
     * Return a new data type like this, with a new precision value
     * <p>
     * This will have no effect if {@link #hasPrecision()} is <code>false</code>
     * <p>
     * This is the same as calling {@link #precision(int, int)} with
     * <code>scale == 0</code>
     *
     * @param precision The new precision value
     * @return The new data type
     */
    DataType<T> precision(int precision);

    /**
     * Return a new data type like this, with a new precision and scale value
     * <p>
     * This will have no effect if {@link #hasPrecision()} is <code>false</code>
     * , or if <code>scale > 0</code> and {@link #hasScale()} is
     * <code>false</code>
     *
     * @param precision The new precision value
     * @param scale The new scale value
     * @return The new data type
     */
    DataType<T> precision(int precision, int scale);

    /**
     * Get the precision of this data type
     *
     * @return The precision of this data type
     */
    int precision();

    /**
     * Whether this data type has a precision
     *
     * @return Whether this data type has a precision
     */
    boolean hasPrecision();

    /**
     * Return a new data type like this, with a new scale value
     * <p>
     * This will have no effect if {@link #hasScale()} is <code>false</code>
     *
     * @param scale The new scale value
     * @return The new data type
     */
    DataType<T> scale(int scale);

    /**
     * Get the scale of this data type
     *
     * @return The scale of this data type
     */
    int scale();

    /**
     * Whether this data type has a scale
     *
     * @return Whether this data type has a scale
     */
    boolean hasScale();

    /**
     * Return a new data type like this, with a new length value
     * <p>
     * This will have no effect if {@link #hasLength()} is <code>false</code>
     *
     * @param length The new length value
     * @return The new data type
     */
    DataType<T> length(int length);

    /**
     * Get the length of this data type
     *
     * @return The length of this data type
     */
    int length();

    /**
     * Whether this data type has a length
     *
     * @return Whether this data type has a length
     */
    boolean hasLength();


    /**
     * Whether this data type is any numeric data type.
     * <p>
     * This applies to any of these types:
     * <ul>
     * <li> {@link SQLDataType#TINYINT}</li>
     * <li> {@link SQLDataType#SMALLINT}</li>
     * <li> {@link SQLDataType#INTEGER}</li>
     * <li> {@link SQLDataType#BIGINT}</li>
     * <li> {@link SQLDataType#FLOAT}</li>
     * <li> {@link SQLDataType#DOUBLE}</li>
     * <li> {@link SQLDataType#REAL}</li>
     * <li> {@link SQLDataType#DECIMAL}</li>
     * <li> {@link SQLDataType#DECIMAL_INTEGER}</li>
     * <li> {@link SQLDataType#NUMERIC}</li>
     * </ul>
     */
    boolean isNumeric();

    /**
     * Whether this data type is any character data type.
     * <p>
     * This applies to any of these types:
     * <ul>
     * <li> {@link SQLDataType#CHAR}</li>
     * <li> {@link SQLDataType#CLOB}</li>
     * <li> {@link SQLDataType#LONGNVARCHAR}</li>
     * <li> {@link SQLDataType#LONGVARCHAR}</li>
     * <li> {@link SQLDataType#NCHAR}</li>
     * <li> {@link SQLDataType#NCLOB}</li>
     * <li> {@link SQLDataType#NVARCHAR}</li>
     * <li> {@link SQLDataType#VARCHAR}</li>
     * </ul>
     */
    boolean isString();

    /**
     * Whether this data type is any date or time type.
     * <p>
     * This applies to any of these types.
     * <ul>
     * <li> {@link SQLDataType#DATE}</li>
     * <li> {@link SQLDataType#TIME}</li>
     * <li> {@link SQLDataType#TIMESTAMP}</li>
     * </ul>
     */
    boolean isDateTime();

    /**
     * Whether this data type is any date or time type.
     * <p>
     * This applies to any of these types.
     * <ul>
     * <li> {@link SQLDataType#DATE}</li>
     * <li> {@link SQLDataType#TIME}</li>
     * <li> {@link SQLDataType#TIMESTAMP}</li>
     * <li> {@link YearToMonth}</li>
     * <li> {@link DayToSecond}</li>
     * </ul>
     * <p>
     * This is a combination of {@link #isDateTime()} or {@link #isInterval()}
     */
    boolean isTemporal();

    /**
     * Whether this data type is any interval type.
     * <p>
     * This applies to any of these types.
     * <ul>
     * <li> {@link YearToMonth}</li>
     * <li> {@link DayToSecond}</li>
     * </ul>
     */
    boolean isInterval();

    /**
     * Whether this data type is any binary type.
     * <p>
     * This applies to any of these types.
     * <ul>
     * <li> {@link SQLDataType#BINARY}</li>
     * <li> {@link SQLDataType#BLOB}</li>
     * <li> {@link SQLDataType#LONGVARBINARY}</li>
     * <li> {@link SQLDataType#VARBINARY}</li>
     * </ul>
     */
    boolean isBinary();

    /**
     * Whether this data type is best deserialised as a <code>LOB</code>.
     * <p>
     * This applies to any of these types.
     * <ul>
     * <li> {@link SQLDataType#BLOB}</li>
     * <li> {@link SQLDataType#CLOB}</li>
     * <li> {@link SQLDataType#NCLOB}</li>
     * </ul>
     */
    boolean isLob();

    /**
     * Whether this data type is an array type.
     */
    boolean isArray();
}
