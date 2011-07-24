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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * A wrapper for database results returned by <code>{@link SelectQuery}</code>
 *
 * @param <R> The record type contained in this result
 * @author Lukas Eder
 * @see SelectQuery#getResult()
 */
public interface Result<R extends Record> extends FieldProvider, List<R>, Attachable {

    /**
     * The resulting records
     *
     * @deprecated - 1.6.2 [#699] - Result now implements {@link List}, hence
     *             this method is no longer necessary.
     */
    @Deprecated
    List<R> getRecords();

    /**
     * Returns a record at a given index
     *
     * @param index The record's index
     * @return The Record
     * @throws IndexOutOfBoundsException
     * @deprecated - 1.6.2 [#699] - Result now implements {@link List}, hence
     *             this method is no longer necessary. Use {@link #get(int)}
     *             instead
     */
    @Deprecated
    R getRecord(int index) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <T> The value's field's generic type parameter
     * @param index The record's index
     * @param field The value's field
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    <T> T getValue(int index, Field<T> field) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <T> The value's field's generic type parameter
     * @param index The record's index
     * @param field The value's field
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    <T> T getValue(int index, Field<T> field, T defaultValue) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The value's field index
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    Object getValue(int index, int fieldIndex) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The value's field index
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    Object getValue(int index, int fieldIndex, Object defaultValue) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The value's field name
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    Object getValue(int index, String fieldName) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The value's field name
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    Object getValue(int index, String fieldName, Object defaultValue) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <A> The value's field's generic type parameter
     * @param <T> The {@link ArrayRecord} type parameter
     * @param index The record's index
     * @param field The value's field
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field) throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param <A> The value's field's generic type parameter
     * @param <T> The {@link ArrayRecord} type parameter
     * @param index The record's index
     * @param field The value's field
     * @param defaultValue The default value if the value was <code>null</code>
     * @return The value
     * @throws IndexOutOfBoundsException
     */
    <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field, T[] defaultValue)
        throws IndexOutOfBoundsException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    String getValueAsString(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    String getValueAsString(int index, Field<?> field, String defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    String getValueAsString(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    String getValueAsString(int index, int fieldIndex, String defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    String getValueAsString(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    String getValueAsString(int index, String fieldName, String defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Byte getValueAsByte(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Byte getValueAsByte(int index, Field<?> field, Byte defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Byte getValueAsByte(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Byte getValueAsByte(int index, int fieldIndex, Byte defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Byte getValueAsByte(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Byte getValueAsByte(int index, String fieldName, Byte defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Short getValueAsShort(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Short getValueAsShort(int index, Field<?> field, Short defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Short getValueAsShort(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Short getValueAsShort(int index, int fieldIndex, Short defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Short getValueAsShort(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Short getValueAsShort(int index, String fieldName, Short defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Integer getValueAsInteger(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Integer getValueAsInteger(int index, Field<?> field, Integer defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Integer getValueAsInteger(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Integer getValueAsInteger(int index, int fieldIndex, Integer defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Integer getValueAsInteger(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Integer getValueAsInteger(int index, String fieldName, Integer defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Long getValueAsLong(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Long getValueAsLong(int index, Field<?> field, Long defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Long getValueAsLong(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Long getValueAsLong(int index, int fieldIndex, Long defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Long getValueAsLong(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Long getValueAsLong(int index, String fieldName, Long defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    BigInteger getValueAsBigInteger(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    BigInteger getValueAsBigInteger(int index, Field<?> field, BigInteger defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    BigInteger getValueAsBigInteger(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    BigInteger getValueAsBigInteger(int index, int fieldIndex, BigInteger defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    BigInteger getValueAsBigInteger(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    BigInteger getValueAsBigInteger(int index, String fieldName, BigInteger defaultValue)
        throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Float getValueAsFloat(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Float getValueAsFloat(int index, Field<?> field, Float defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Float getValueAsFloat(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Float getValueAsFloat(int index, int fieldIndex, Float defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Float getValueAsFloat(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Float getValueAsFloat(int index, String fieldName, Float defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Double getValueAsDouble(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Double getValueAsDouble(int index, Field<?> field, Double defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Double getValueAsDouble(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Double getValueAsDouble(int index, int fieldIndex, Double defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Double getValueAsDouble(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Double getValueAsDouble(int index, String fieldName, Double defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    BigDecimal getValueAsBigDecimal(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    BigDecimal getValueAsBigDecimal(int index, Field<?> field, BigDecimal defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    BigDecimal getValueAsBigDecimal(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    BigDecimal getValueAsBigDecimal(int index, int fieldIndex, BigDecimal defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    BigDecimal getValueAsBigDecimal(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    BigDecimal getValueAsBigDecimal(int index, String fieldName, BigDecimal defaultValue)
        throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Boolean getValueAsBoolean(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Boolean getValueAsBoolean(int index, Field<?> field, Boolean defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Boolean getValueAsBoolean(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Boolean getValueAsBoolean(int index, int fieldIndex, Boolean defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Boolean getValueAsBoolean(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
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
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Boolean getValueAsBoolean(int index, String fieldName, Boolean defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Timestamp getValueAsTimestamp(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Timestamp getValueAsTimestamp(int index, Field<?> field, Timestamp defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Timestamp getValueAsTimestamp(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Timestamp getValueAsTimestamp(int index, int fieldIndex, Timestamp defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Timestamp getValueAsTimestamp(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Timestamp getValueAsTimestamp(int index, String fieldName, Timestamp defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Date getValueAsDate(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Date getValueAsDate(int index, Field<?> field, Date defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Date getValueAsDate(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Date getValueAsDate(int index, int fieldIndex, Date defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Date getValueAsDate(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Date getValueAsDate(int index, String fieldName, Date defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @return The converted value of a field contained in this record
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Time getValueAsTime(int index, Field<?> field) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param field The field
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field contained in this record, or
     *         defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument field is not contained
     *             in {@link #getFields()}
     */
    Time getValueAsTime(int index, Field<?> field, Time defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @return The converted value of a field's index contained in this record
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Time getValueAsTime(int index, int fieldIndex) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldIndex The field's index
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's index contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument index is not contained
     *             in the record
     */
    Time getValueAsTime(int index, int fieldIndex, Time defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @return The converted value of a field's name contained in this record
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Time getValueAsTime(int index, String fieldName) throws IllegalArgumentException;

    /**
     * Convenience method to fetch a value at a given position in the result.
     *
     * @param index The record's index
     * @param fieldName The field's name
     * @param defaultValue The default value instead of <code>null</code>
     * @return The converted value of a field's name contained in this record,
     *         or defaultValue, if <code>null</code>
     * @throws IllegalArgumentException If the argument name is not contained in
     *             the record
     */
    Time getValueAsTime(int index, String fieldName, Time defaultValue) throws IllegalArgumentException;

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param <T> The values' field's generic type parameter
     * @param field The values' field
     * @return The values
     */
    <T> List<T> getValues(Field<T> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The values
     */
    List<?> getValues(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The values
     */
    List<?> getValues(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<BigDecimal> getValuesAsBigDecimal(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<BigDecimal> getValuesAsBigDecimal(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<BigDecimal> getValuesAsBigDecimal(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<BigInteger> getValuesAsBigInteger(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<BigInteger> getValuesAsBigInteger(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<BigInteger> getValuesAsBigInteger(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Byte> getValuesAsByte(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Byte> getValuesAsByte(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Byte> getValuesAsByte(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Date> getValuesAsDate(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Date> getValuesAsDate(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Date> getValuesAsDate(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Double> getValuesAsDouble(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Double> getValuesAsDouble(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Double> getValuesAsDouble(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Float> getValuesAsFloat(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Float> getValuesAsFloat(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Float> getValuesAsFloat(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Integer> getValuesAsInteger(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Integer> getValuesAsInteger(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Integer> getValuesAsInteger(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Long> getValuesAsLong(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Long> getValuesAsLong(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Long> getValuesAsLong(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Short> getValuesAsShort(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Short> getValuesAsShort(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Short> getValuesAsShort(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<String> getValuesAsString(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<String> getValuesAsString(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<String> getValuesAsString(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Time> getValuesAsTime(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Time> getValuesAsTime(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Time> getValuesAsTime(String fieldName);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param field The values' field
     * @return The converted values
     */
    List<Timestamp> getValuesAsTimestamp(Field<?> field);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldIndex The values' field index
     * @return The converted values
     */
    List<Timestamp> getValuesAsTimestamp(int fieldIndex);

    /**
     * Convenience method to fetch all values for a given field. This is
     * especially useful, when selecting only a single field.
     *
     * @param fieldName The values' field name
     * @return The converted values
     */
    List<Timestamp> getValuesAsTimestamp(String fieldName);

    /**
     * The number of resulting records
     *
     * @deprecated - 1.6.2 [#699] - Result now implements {@link List}, hence
     *             this method is no longer necessary. Use {@link #size()}
     *             instead
     */
    @Deprecated
    int getNumberOfRecords();

    /**
     * Whether there are any records contained in this <code>Result</code>
     */
    @Override
    boolean isEmpty();

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
     * This is the same as calling <code>formatCSV(',')</code>
     *
     * @return The formatted result
     */
    String formatCSV();

    /**
     * Get a simple formatted representation of this result as CSV.
     *
     * @param delimiter The delimiter to use between records
     * @return The formatted result
     */
    String formatCSV(char delimiter);

    /**
     * Get a simple formatted representation of this result as a JSON array of
     * array. The format is the following: <code><pre>
     * {"fields":["field-1","field-2",...,"field-n"],
     *  "records":[[value-1-1,value-1-2,...,value-1-n],
     *             [value-2-1,value-2-2,...,value-2-n]]}
     * </pre></code>
     *
     * @return The formatted result
     */
    String formatJSON();

    /**
     * Get this result formatted as XML
     *
     * @see <a
     *      href="http://www.jooq.org/xsd/jooq-export-1.6.2.xsd">http://www.jooq.org/xsd/jooq-export-1.6.2.xsd</a>
     */
    String formatXML();
}
