/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq.impl;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.jooq.tools.StringUtils.abbreviate;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.StringUtils.rightPad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.Result;
import org.jooq.Store;
import org.jooq.Table;
import org.jooq.exception.MappingException;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;
import org.jooq.tools.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Lukas Eder
 */
class ResultImpl<R extends Record> implements Result<R>, AttachableInternal {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 6416154375799578362L;

    private final FieldProvider  fields;
    private final List<R>        records;
    private final AttachableImpl attachable;

    ResultImpl(Configuration configuration, FieldProvider fields) {
        this.fields = fields;
        this.records = new ArrayList<R>();
        this.attachable = new AttachableImpl(this, configuration);
    }

    // -------------------------------------------------------------------------
    // Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final <I> I internalAPI(Class<I> internalType) throws ClassCastException {
        return internalType.cast(this);
    }

    @Override
    public final void attach(Configuration configuration) {
        attachable.attach(configuration);
    }

    @Override
    public final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        for (Store<?> item : records) {
            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }

    @Override
    public final Configuration getConfiguration() {
        return attachable.getConfiguration();
    }

    // -------------------------------------------------------------------------
    // Result API
    // -------------------------------------------------------------------------

    @Override
    public final List<Field<?>> getFields() {
        return fields.getFields();
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return fields.getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return fields.getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return fields.getField(index);
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return fields.getIndex(field);
    }

    @Override
    public final boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public final boolean isNotEmpty() {
        return !records.isEmpty();
    }

    @Override
    public final <T> T getValue(int index, Field<T> field) throws IndexOutOfBoundsException {
        return get(index).getValue(field);
    }

    @Override
    public final <T> T getValue(int index, Field<T> field, T defaultValue) throws IndexOutOfBoundsException {
        return get(index).getValue(field, defaultValue);
    }

    @Override
    public final Object getValue(int index, int fieldIndex) throws IndexOutOfBoundsException {
        return get(index).getValue(fieldIndex);
    }

    @Override
    public final Object getValue(int index, int fieldIndex, Object defaultValue) throws IndexOutOfBoundsException {
        return get(index).getValue(fieldIndex, defaultValue);
    }

    @Override
    public final Object getValue(int index, String fieldName) throws IndexOutOfBoundsException {
        return get(index).getValue(fieldName);
    }

    @Override
    public final Object getValue(int index, String fieldName, Object defaultValue) throws IndexOutOfBoundsException {
        return get(index).getValue(fieldName, defaultValue);
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field)
        throws IndexOutOfBoundsException {
        return get(index).getValueAsArray(field);
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field, T[] defaultValue)
        throws IndexOutOfBoundsException {
        return get(index).getValueAsArray(field, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsString(field);
    }

    @Override
    public final String getValueAsString(int index, Field<?> field, String defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsString(field, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsString(fieldIndex);
    }

    @Override
    public final String getValueAsString(int index, int fieldIndex, String defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsString(fieldIndex, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsByte(field);
    }

    @Override
    public final Byte getValueAsByte(int index, Field<?> field, Byte defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsByte(field, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsByte(fieldIndex);
    }

    @Override
    public final Byte getValueAsByte(int index, int fieldIndex, Byte defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsByte(fieldIndex, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsShort(field);
    }

    @Override
    public final Short getValueAsShort(int index, Field<?> field, Short defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsShort(field, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsShort(fieldIndex);
    }

    @Override
    public final Short getValueAsShort(int index, int fieldIndex, Short defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsShort(fieldIndex, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsInteger(field);
    }

    @Override
    public final Integer getValueAsInteger(int index, Field<?> field, Integer defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsInteger(field, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsInteger(fieldIndex);
    }

    @Override
    public final Integer getValueAsInteger(int index, int fieldIndex, Integer defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsInteger(fieldIndex, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsLong(field);
    }

    @Override
    public final Long getValueAsLong(int index, Field<?> field, Long defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsLong(field, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsLong(fieldIndex);
    }

    @Override
    public final Long getValueAsLong(int index, int fieldIndex, Long defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsLong(fieldIndex, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(field);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, Field<?> field, BigInteger defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(field, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(fieldIndex);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, int fieldIndex, BigInteger defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(fieldIndex, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsFloat(field);
    }

    @Override
    public final Float getValueAsFloat(int index, Field<?> field, Float defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsFloat(field, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsFloat(fieldIndex);
    }

    @Override
    public final Float getValueAsFloat(int index, int fieldIndex, Float defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsFloat(fieldIndex, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsDouble(field);
    }

    @Override
    public final Double getValueAsDouble(int index, Field<?> field, Double defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDouble(field, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsDouble(fieldIndex);
    }

    @Override
    public final Double getValueAsDouble(int index, int fieldIndex, Double defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDouble(fieldIndex, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(field);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, Field<?> field, BigDecimal defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(field, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(fieldIndex);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, int fieldIndex, BigDecimal defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(fieldIndex, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(field);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, Field<?> field, Boolean defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(field, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(fieldIndex);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, int fieldIndex, Boolean defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(fieldIndex, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(field);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, Field<?> field, Timestamp defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(field, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(fieldIndex);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, int fieldIndex, Timestamp defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(fieldIndex, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsDate(field);
    }

    @Override
    public final Date getValueAsDate(int index, Field<?> field, Date defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDate(field, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsDate(fieldIndex);
    }

    @Override
    public final Date getValueAsDate(int index, int fieldIndex, Date defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDate(fieldIndex, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, Field<?> field) throws IllegalArgumentException {
        return get(index).getValueAsTime(field);
    }

    @Override
    public final Time getValueAsTime(int index, Field<?> field, Time defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsTime(field, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, int fieldIndex) throws IllegalArgumentException {
        return get(index).getValueAsTime(fieldIndex);
    }

    @Override
    public final Time getValueAsTime(int index, int fieldIndex, Time defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsTime(fieldIndex, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsString(fieldName);
    }

    @Override
    public final String getValueAsString(int index, String fieldName, String defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsString(fieldName, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsByte(fieldName);
    }

    @Override
    public final Byte getValueAsByte(int index, String fieldName, Byte defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsByte(fieldName, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsShort(fieldName);
    }

    @Override
    public final Short getValueAsShort(int index, String fieldName, Short defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsShort(fieldName, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsInteger(fieldName);
    }

    @Override
    public final Integer getValueAsInteger(int index, String fieldName, Integer defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsInteger(fieldName, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsLong(fieldName);
    }

    @Override
    public final Long getValueAsLong(int index, String fieldName, Long defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsLong(fieldName, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(fieldName);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, String fieldName, BigInteger defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigInteger(fieldName, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsFloat(fieldName);
    }

    @Override
    public final Float getValueAsFloat(int index, String fieldName, Float defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsFloat(fieldName, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsDouble(fieldName);
    }

    @Override
    public final Double getValueAsDouble(int index, String fieldName, Double defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDouble(fieldName, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(fieldName);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, String fieldName, BigDecimal defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsBigDecimal(fieldName, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(fieldName);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, String fieldName, Boolean defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsBoolean(fieldName, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(fieldName);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, String fieldName, Timestamp defaultValue)
        throws IllegalArgumentException {
        return get(index).getValueAsTimestamp(fieldName, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsDate(fieldName);
    }

    @Override
    public final Date getValueAsDate(int index, String fieldName, Date defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsDate(fieldName, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, String fieldName) throws IllegalArgumentException {
        return get(index).getValueAsTime(fieldName);
    }

    @Override
    public final Time getValueAsTime(int index, String fieldName, Time defaultValue) throws IllegalArgumentException {
        return get(index).getValueAsTime(fieldName, defaultValue);
    }
    @Override
    public final <T> List<T> getValues(Field<T> field) {
        List<T> result = new ArrayList<T>();

        for (R record : this) {
            result.add(record.getValue(field));
        }

        return result;
    }

    @Override
    public final <T> List<T> getValues(Field<?> field, Class<? extends T> type) {
        return Convert.convert(getValues(field), type);
    }

    @Override
    public final <T, U> List<U> getValues(Field<T> field, Converter<? super T, U> converter) {
        return Convert.convert(getValues(field), converter);
    }

    @Override
    public final List<?> getValues(int fieldIndex) {
        return getValues(getField(fieldIndex));
    }

    @Override
    public final <T> List<T> getValues(int fieldIndex, Class<? extends T> type) {
        return Convert.convert(getValues(fieldIndex), type);
    }

    @Override
    public final <U> List<U> getValues(int fieldIndex, Converter<?, U> converter) {
        return Convert.convert(getValues(fieldIndex), converter);
    }

    @Override
    public final List<?> getValues(String fieldName) {
        return getValues(getField(fieldName));
    }

    @Override
    public final <T> List<T> getValues(String fieldName, Class<? extends T> type) {
        return Convert.convert(getValues(fieldName), type);
    }

    @Override
    public final <U> List<U> getValues(String fieldName, Converter<?, U> converter) {
        return Convert.convert(getValues(fieldName), converter);
    }

    @Override
    public final List<BigDecimal> getValuesAsBigDecimal(Field<?> field) {
        List<BigDecimal> result = new ArrayList<BigDecimal>();

        for (R record : this) {
            result.add(record.getValueAsBigDecimal(field));
        }

        return result;
    }

    @Override
    public final List<BigDecimal> getValuesAsBigDecimal(int fieldIndex) {
        return getValuesAsBigDecimal(getField(fieldIndex));
    }

    @Override
    public final List<BigInteger> getValuesAsBigInteger(Field<?> field) {
        List<BigInteger> result = new ArrayList<BigInteger>();

        for (R record : this) {
            result.add(record.getValueAsBigInteger(field));
        }

        return result;
    }

    @Override
    public final List<BigInteger> getValuesAsBigInteger(int fieldIndex) {
        return getValuesAsBigInteger(getField(fieldIndex));
    }

    @Override
    public final List<Byte> getValuesAsByte(Field<?> field) {
        List<Byte> result = new ArrayList<Byte>();

        for (R record : this) {
            result.add(record.getValueAsByte(field));
        }

        return result;
    }

    @Override
    public final List<Byte> getValuesAsByte(int fieldIndex) {
        return getValuesAsByte(getField(fieldIndex));
    }

    @Override
    public final List<Date> getValuesAsDate(Field<?> field) {
        List<Date> result = new ArrayList<Date>();

        for (R record : this) {
            result.add(record.getValueAsDate(field));
        }

        return result;
    }

    @Override
    public final List<Date> getValuesAsDate(int fieldIndex) {
        return getValuesAsDate(getField(fieldIndex));
    }

    @Override
    public final List<Double> getValuesAsDouble(Field<?> field) {
        List<Double> result = new ArrayList<Double>();

        for (R record : this) {
            result.add(record.getValueAsDouble(field));
        }

        return result;
    }

    @Override
    public final List<Double> getValuesAsDouble(int fieldIndex) {
        return getValuesAsDouble(getField(fieldIndex));
    }

    @Override
    public final List<Float> getValuesAsFloat(Field<?> field) {
        List<Float> result = new ArrayList<Float>();

        for (R record : this) {
            result.add(record.getValueAsFloat(field));
        }

        return result;
    }

    @Override
    public final List<Float> getValuesAsFloat(int fieldIndex) {
        return getValuesAsFloat(getField(fieldIndex));
    }

    @Override
    public final List<Integer> getValuesAsInteger(Field<?> field) {
        List<Integer> result = new ArrayList<Integer>();

        for (R record : this) {
            result.add(record.getValueAsInteger(field));
        }

        return result;
    }

    @Override
    public final List<Integer> getValuesAsInteger(int fieldIndex) {
        return getValuesAsInteger(getField(fieldIndex));
    }

    @Override
    public final List<Long> getValuesAsLong(Field<?> field) {
        List<Long> result = new ArrayList<Long>();

        for (R record : this) {
            result.add(record.getValueAsLong(field));
        }

        return result;
    }

    @Override
    public final List<Long> getValuesAsLong(int fieldIndex) {
        return getValuesAsLong(getField(fieldIndex));
    }

    @Override
    public final List<Short> getValuesAsShort(Field<?> field) {
        List<Short> result = new ArrayList<Short>();

        for (R record : this) {
            result.add(record.getValueAsShort(field));
        }

        return result;
    }

    @Override
    public final List<Short> getValuesAsShort(int fieldIndex) {
        return getValuesAsShort(getField(fieldIndex));
    }

    @Override
    public final List<String> getValuesAsString(Field<?> field) {
        List<String> result = new ArrayList<String>();

        for (R record : this) {
            result.add(record.getValueAsString(field));
        }

        return result;
    }

    @Override
    public final List<String> getValuesAsString(int fieldIndex) {
        return getValuesAsString(getField(fieldIndex));
    }

    @Override
    public final List<Time> getValuesAsTime(Field<?> field) {
        List<Time> result = new ArrayList<Time>();

        for (R record : this) {
            result.add(record.getValueAsTime(field));
        }

        return result;
    }

    @Override
    public final List<Time> getValuesAsTime(int fieldIndex) {
        return getValuesAsTime(getField(fieldIndex));
    }

    @Override
    public final List<Timestamp> getValuesAsTimestamp(Field<?> field) {
        List<Timestamp> result = new ArrayList<Timestamp>();

        for (R record : this) {
            result.add(record.getValueAsTimestamp(field));
        }

        return result;
    }

    @Override
    public final List<Timestamp> getValuesAsTimestamp(int fieldIndex) {
        return getValuesAsTimestamp(getField(fieldIndex));
    }

    @Override
    public final List<BigDecimal> getValuesAsBigDecimal(String fieldName) {
        return getValuesAsBigDecimal(getField(fieldName));
    }

    @Override
    public final List<BigInteger> getValuesAsBigInteger(String fieldName) {
        return getValuesAsBigInteger(getField(fieldName));
    }

    @Override
    public final List<Byte> getValuesAsByte(String fieldName) {
        return getValuesAsByte(getField(fieldName));
    }

    @Override
    public final List<Date> getValuesAsDate(String fieldName) {
        return getValuesAsDate(getField(fieldName));
    }

    @Override
    public final List<Double> getValuesAsDouble(String fieldName) {
        return getValuesAsDouble(getField(fieldName));
    }

    @Override
    public final List<Float> getValuesAsFloat(String fieldName) {
        return getValuesAsFloat(getField(fieldName));
    }

    @Override
    public final List<Integer> getValuesAsInteger(String fieldName) {
        return getValuesAsInteger(getField(fieldName));
    }

    @Override
    public final List<Long> getValuesAsLong(String fieldName) {
        return getValuesAsLong(getField(fieldName));
    }

    @Override
    public final List<Short> getValuesAsShort(String fieldName) {
        return getValuesAsShort(getField(fieldName));
    }

    @Override
    public final List<String> getValuesAsString(String fieldName) {
        return getValuesAsString(getField(fieldName));
    }

    @Override
    public final List<Time> getValuesAsTime(String fieldName) {
        return getValuesAsTime(getField(fieldName));
    }

    @Override
    public final List<Timestamp> getValuesAsTimestamp(String fieldName) {
        return getValuesAsTimestamp(getField(fieldName));
    }

    final void addRecord(R record) {
        records.add(record);
    }

    @Override
    public final String format() {
        return format(50);
    }

    @Override
    public final String format(int maxRecords) {
        final int MAX_WIDTH = 50;
        final int MAX_RECORDS = 50;

        StringBuilder sb = new StringBuilder();
        Map<Field<?>, Integer> widths = new HashMap<Field<?>, Integer>();

        // Initialise widths
        for (Field<?> f : getFields()) {
            widths.put(f, 4);
        }

        // Find width for every field to satisfy formatting the first 50 records
        for (Field<?> f : getFields()) {
            widths.put(f, min(MAX_WIDTH, max(widths.get(f), f.getName().length())));

            for (int i = 0; i < min(MAX_RECORDS, size()); i++) {
                widths.put(f, min(MAX_WIDTH, max(widths.get(f), format0(getValue(i, f)).length())));
            }
        }

        // Begin the writing
        // ---------------------------------------------------------------------

        // Write top line
        sb.append("+");
        for (Field<?> f : getFields()) {
            sb.append(rightPad("", widths.get(f), "-"));
            sb.append("+");
        }

        // Write headers
        sb.append("\n|");
        for (Field<?> f : getFields()) {
            String padded;

            if (Number.class.isAssignableFrom(f.getType())) {
                padded = leftPad(f.getName(), widths.get(f));
            }
            else {
                padded = rightPad(f.getName(), widths.get(f));
            }

            sb.append(abbreviate(padded, widths.get(f)));
            sb.append("|");
        }

        // Write separator
        sb.append("\n+");
        for (Field<?> f : getFields()) {
            sb.append(rightPad("", widths.get(f), "-"));
            sb.append("+");
        }

        // Write columns
        for (int i = 0; i < min(maxRecords, size()); i++) {
            sb.append("\n|");
            for (Field<?> f : getFields()) {
                String value = format0(getValue(i, f)).replace("\n", "{lf}").replace("\r", "{cr}");
                String padded;

                if (Number.class.isAssignableFrom(f.getType())) {
                    padded = leftPad(value, widths.get(f));
                }
                else {
                    padded = rightPad(value, widths.get(f));
                }

                sb.append(abbreviate(padded, widths.get(f)));
                sb.append("|");
            }
        }

        // Write bottom line
        if (size() > 0) {
            sb.append("\n+");
            for (Field<?> f : getFields()) {
                sb.append(rightPad("", widths.get(f), "-"));
                sb.append("+");
            }
        }

        // Write truncation message, if applicable
        if (maxRecords < size()) {
            sb.append("\n|...");
            sb.append(size() - maxRecords);
            sb.append(" record(s) truncated...");
        }

        return sb.toString();
    }

    @Override
    public final String formatHTML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>");
        sb.append("<thead>");
        sb.append("<tr>");

        for (Field<?> field : getFields()) {
            sb.append("<th>");
            sb.append(field.getName());
            sb.append("</th>");
        }

        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");

        for (Record record : this) {
            sb.append("<tr>");

            for (Field<?> field : getFields()) {
                sb.append("<td>");
                sb.append(format0(record.getValue(field)));
                sb.append("</td>");
            }

            sb.append("</tr>");
        }

        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }

    @Override
    public final String formatCSV() {
        return formatCSV(',');
    }

    @Override
    public final String formatCSV(char delimiter) {
        StringBuilder sb = new StringBuilder();

        String sep1 = "";
        for (Field<?> field : getFields()) {
            sb.append(sep1);
            sb.append(formatCSV0(field.getName()));

            sep1 = Character.toString(delimiter);
        }

        sb.append("\n");

        for (Record record : this) {
            String sep2 = "";
            for (Field<?> field : getFields()) {
                sb.append(sep2);
                sb.append(formatCSV0(record.getValue(field)));

                sep2 = Character.toString(delimiter);
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private final String formatCSV0(Object value) {

        // Escape null and empty strings
        if (value == null || "".equals(value)) {
            return "\"\"";
        }

        String result = format0(value);

        if (StringUtils.containsAny(result, ',', ';', '\t', '"', '\n', '\r', '\'')) {
            return "\"" + result.replace("\"", "\"\"") + "\"";
        }
        else {
            return result;
        }
    }

    private final String format0(Object value) {
        String formatted;

        if (value == null) {
            formatted = "{null}";
        }
        else if (value.getClass() == byte[].class) {
            formatted = Arrays.toString((byte[]) value);
        }
        else if (value.getClass().isArray()) {
            formatted = Arrays.toString((Object[]) value);
        }
        else if (value instanceof EnumType) {
            formatted = ((EnumType) value).getLiteral();
        }
        else {
            formatted = value.toString();
        }

        return formatted;
    }

    @Override
    public final String formatJSON() {
        List<String> f = new ArrayList<String>();
        List<List<Object>> r = new ArrayList<List<Object>>();

        for (Field<?> field : getFields()) {
            f.add(field.getName());
        }

        for (Record record : this) {
            List<Object> list = new ArrayList<Object>();

            for (Field<?> field : getFields()) {
                list.add(record.getValue(field));
            }

            r.add(list);
        }

        Map<String, List<?>> map = new LinkedHashMap<String, List<?>>();

        map.put("fields", f);
        map.put("records", r);

        return JSONObject.toJSONString(map);
    }

    @Override
    public final String formatXML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<jooq-export:result xmlns:jooq-export=\"http://www.jooq.org/xsd/jooq-export-1.6.2.xsd\">");
        sb.append("<fields>");

        for (Field<?> field : getFields()) {
            sb.append("<field name=\"");
            sb.append(escapeXML(field.getName()));
            sb.append("\"/>");
        }

        sb.append("</fields>");
        sb.append("<records>");

        for (Record record : this) {
            sb.append("<record>");

            for (Field<?> field : getFields()) {
                Object value = record.getValue(field);

                sb.append("<value field=\"");
                sb.append(escapeXML(field.getName()));
                sb.append("\"");

                if (value == null) {
                    sb.append("/>");
                }
                else {
                    sb.append(">");
                    sb.append(escapeXML(format0(value)));
                    sb.append("</value>");
                }
            }

            sb.append("</record>");
        }

        sb.append("</records>");
        sb.append("</jooq-export:result>");

        return sb.toString();
    }

    @Override
    public final Document exportXML() {
        return intoXML();
    }

    @Override
    public final Document intoXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element eResult = document.createElement("jooq-export:result");
            eResult.setAttribute("xmlns:jooq-export", "http://www.jooq.org/xsd/jooq-export-1.6.2.xsd");
            document.appendChild(eResult);

            Element eFields = document.createElement("fields");
            eResult.appendChild(eFields);

            for (Field<?> field : getFields()) {
                Element eField = document.createElement("field");
                eField.setAttribute("name", field.getName());
                eFields.appendChild(eField);
            }

            Element eRecords = document.createElement("records");
            eResult.appendChild(eRecords);

            for (Record record : this) {
                Element eRecord = document.createElement("record");
                eRecords.appendChild(eRecord);

                for (Field<?> field : getFields()) {
                    Object value = record.getValue(field);

                    Element eValue = document.createElement("value");
                    eValue.setAttribute("field", field.getName());
                    eRecord.appendChild(eValue);

                    if (value != null) {
                        eValue.setTextContent(format0(value));
                    }
                }
            }

            return document;
        }
        catch (ParserConfigurationException ignore) {
            throw new RuntimeException(ignore);
        }
    }

    private final String escapeXML(String string) {
        return StringUtils.replaceEach(string,
            new String[] { "\"", "'", "<", ">", "&" },
            new String[] { "&quot;", "&apos;", "&lt;", "&gt;", "&amp;"});
    }

    @Override
    public final Object[][] intoArray() throws MappingException {
        int size = size();
        Object[][] array = new Object[size][];

        for (int i = 0; i < size; i++) {
            array[i] = get(i).intoArray();
        }

        return array;
    }

    @Override
    public final <T> List<T> into(Class<? extends T> type) {
        List<T> list = new ArrayList<T>();

        for (R record : this) {
            list.add(record.into(type));
        }

        return list;
    }

    @Override
    public final <Z extends Record> Result<Z> into(Table<Z> table) {
        Result<Z> list = new ResultImpl<Z>(getConfiguration(), table);

        for (R record : this) {
            list.add(record.into(table));
        }

        return list;
    }

    @Override
    public final <H extends RecordHandler<R>> H into(H handler) {
        for (R record : this) {
            handler.next(record);
        }

        return handler;
    }

    // -------------------------------------------------------------------------
    // Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return format();
    }

    @Override
    public int hashCode() {
        return records.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResultImpl) {
            ResultImpl<R> other = (ResultImpl<R>) obj;
            return records.equals(other.records);
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // List API
    // -------------------------------------------------------------------------

    @Override
    public final int size() {
        return records.size();
    }

    @Override
    public final boolean contains(Object o) {
        return records.contains(o);
    }

    @Override
    public final Object[] toArray() {
        return records.toArray();
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        return records.toArray(a);
    }

    @Override
    public final boolean add(R e) {
        return records.add(e);
    }

    @Override
    public final boolean remove(Object o) {
        return records.remove(o);
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        return records.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends R> c) {
        return records.addAll(c);
    }

    @Override
    public final boolean addAll(int index, Collection<? extends R> c) {
        return records.addAll(index, c);
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return records.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        return records.retainAll(c);
    }

    @Override
    public final void clear() {
        records.clear();
    }

    @Override
    public final R get(int index) {
        return records.get(index);
    }

    @Override
    public final R set(int index, R element) {
        return records.set(index, element);
    }

    @Override
    public final void add(int index, R element) {
        records.add(index, element);
    }

    @Override
    public final R remove(int index) {
        return records.remove(index);
    }

    @Override
    public final int indexOf(Object o) {
        return records.indexOf(o);
    }

    @Override
    public final int lastIndexOf(Object o) {
        return records.lastIndexOf(o);
    }

    @Override
    public final Iterator<R> iterator() {
        return records.iterator();
    }

    @Override
    public final ListIterator<R> listIterator() {
        return records.listIterator();
    }

    @Override
    public final ListIterator<R> listIterator(int index) {
        return records.listIterator(index);
    }

    @Override
    public final List<R> subList(int fromIndex, int toIndex) {
        return records.subList(fromIndex, toIndex);
    }
}
