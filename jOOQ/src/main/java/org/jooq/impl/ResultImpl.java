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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Store;
import org.jooq.Table;
import org.jooq.exception.InvalidResultException;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;
import org.jooq.tools.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Lukas Eder
 * @author Ivan Dugic
 */
@SuppressWarnings("deprecation")
class ResultImpl<R extends Record> implements Result<R>, AttachableInternal {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 6416154375799578362L;

    private final FieldProvider fields;
    private final List<R>       records;
    private Configuration       configuration;

    ResultImpl(Configuration configuration, FieldProvider fields) {
        this.fields = fields;
        this.records = new ArrayList<R>();
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // XXX: Attachable API
    // -------------------------------------------------------------------------

    @Override
    public final <I> I internalAPI(Class<I> internalType) {
        return internalType.cast(this);
    }

    @Override
    public final void attach(Configuration c) {
        this.configuration = c;

        for (Attachable attachable : getAttachables()) {
            attachable.attach(c);
        }
    }

    @Override
    @Deprecated
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
        return configuration;
    }

    // -------------------------------------------------------------------------
    // XXX: Result API
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
    public final int getIndex(Field<?> field) {
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
    public final <T> T getValue(int index, Field<T> field) {
        return get(index).getValue(field);
    }

    @Override
    public final <T> T getValue(int index, Field<T> field, T defaultValue) {
        return get(index).getValue(field, defaultValue);
    }

    @Override
    public final Object getValue(int index, int fieldIndex) {
        return get(index).getValue(fieldIndex);
    }

    @Override
    public final Object getValue(int index, int fieldIndex, Object defaultValue) {
        return get(index).getValue(fieldIndex, defaultValue);
    }

    @Override
    public final Object getValue(int index, String fieldName) {
        return get(index).getValue(fieldName);
    }

    @Override
    public final Object getValue(int index, String fieldName, Object defaultValue) {
        return get(index).getValue(fieldName, defaultValue);
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field) {
        return get(index).getValueAsArray(field);
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(int index, Field<A> field, T[] defaultValue) {
        return get(index).getValueAsArray(field, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, Field<?> field) {
        return get(index).getValueAsString(field);
    }

    @Override
    public final String getValueAsString(int index, Field<?> field, String defaultValue) {
        return get(index).getValueAsString(field, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, int fieldIndex) {
        return get(index).getValueAsString(fieldIndex);
    }

    @Override
    public final String getValueAsString(int index, int fieldIndex, String defaultValue) {
        return get(index).getValueAsString(fieldIndex, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, Field<?> field) {
        return get(index).getValueAsByte(field);
    }

    @Override
    public final Byte getValueAsByte(int index, Field<?> field, Byte defaultValue) {
        return get(index).getValueAsByte(field, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, int fieldIndex) {
        return get(index).getValueAsByte(fieldIndex);
    }

    @Override
    public final Byte getValueAsByte(int index, int fieldIndex, Byte defaultValue) {
        return get(index).getValueAsByte(fieldIndex, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, Field<?> field) {
        return get(index).getValueAsShort(field);
    }

    @Override
    public final Short getValueAsShort(int index, Field<?> field, Short defaultValue) {
        return get(index).getValueAsShort(field, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, int fieldIndex) {
        return get(index).getValueAsShort(fieldIndex);
    }

    @Override
    public final Short getValueAsShort(int index, int fieldIndex, Short defaultValue) {
        return get(index).getValueAsShort(fieldIndex, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, Field<?> field) {
        return get(index).getValueAsInteger(field);
    }

    @Override
    public final Integer getValueAsInteger(int index, Field<?> field, Integer defaultValue) {
        return get(index).getValueAsInteger(field, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, int fieldIndex) {
        return get(index).getValueAsInteger(fieldIndex);
    }

    @Override
    public final Integer getValueAsInteger(int index, int fieldIndex, Integer defaultValue) {
        return get(index).getValueAsInteger(fieldIndex, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, Field<?> field) {
        return get(index).getValueAsLong(field);
    }

    @Override
    public final Long getValueAsLong(int index, Field<?> field, Long defaultValue) {
        return get(index).getValueAsLong(field, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, int fieldIndex) {
        return get(index).getValueAsLong(fieldIndex);
    }

    @Override
    public final Long getValueAsLong(int index, int fieldIndex, Long defaultValue) {
        return get(index).getValueAsLong(fieldIndex, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, Field<?> field) {
        return get(index).getValueAsBigInteger(field);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, Field<?> field, BigInteger defaultValue) {
        return get(index).getValueAsBigInteger(field, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, int fieldIndex) {
        return get(index).getValueAsBigInteger(fieldIndex);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, int fieldIndex, BigInteger defaultValue) {
        return get(index).getValueAsBigInteger(fieldIndex, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, Field<?> field) {
        return get(index).getValueAsFloat(field);
    }

    @Override
    public final Float getValueAsFloat(int index, Field<?> field, Float defaultValue) {
        return get(index).getValueAsFloat(field, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, int fieldIndex) {
        return get(index).getValueAsFloat(fieldIndex);
    }

    @Override
    public final Float getValueAsFloat(int index, int fieldIndex, Float defaultValue) {
        return get(index).getValueAsFloat(fieldIndex, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, Field<?> field) {
        return get(index).getValueAsDouble(field);
    }

    @Override
    public final Double getValueAsDouble(int index, Field<?> field, Double defaultValue) {
        return get(index).getValueAsDouble(field, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, int fieldIndex) {
        return get(index).getValueAsDouble(fieldIndex);
    }

    @Override
    public final Double getValueAsDouble(int index, int fieldIndex, Double defaultValue) {
        return get(index).getValueAsDouble(fieldIndex, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, Field<?> field) {
        return get(index).getValueAsBigDecimal(field);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, Field<?> field, BigDecimal defaultValue) {
        return get(index).getValueAsBigDecimal(field, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, int fieldIndex) {
        return get(index).getValueAsBigDecimal(fieldIndex);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, int fieldIndex, BigDecimal defaultValue) {
        return get(index).getValueAsBigDecimal(fieldIndex, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, Field<?> field) {
        return get(index).getValueAsBoolean(field);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, Field<?> field, Boolean defaultValue) {
        return get(index).getValueAsBoolean(field, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, int fieldIndex) {
        return get(index).getValueAsBoolean(fieldIndex);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, int fieldIndex, Boolean defaultValue) {
        return get(index).getValueAsBoolean(fieldIndex, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, Field<?> field) {
        return get(index).getValueAsTimestamp(field);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, Field<?> field, Timestamp defaultValue) {
        return get(index).getValueAsTimestamp(field, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, int fieldIndex) {
        return get(index).getValueAsTimestamp(fieldIndex);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, int fieldIndex, Timestamp defaultValue) {
        return get(index).getValueAsTimestamp(fieldIndex, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, Field<?> field) {
        return get(index).getValueAsDate(field);
    }

    @Override
    public final Date getValueAsDate(int index, Field<?> field, Date defaultValue) {
        return get(index).getValueAsDate(field, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, int fieldIndex) {
        return get(index).getValueAsDate(fieldIndex);
    }

    @Override
    public final Date getValueAsDate(int index, int fieldIndex, Date defaultValue) {
        return get(index).getValueAsDate(fieldIndex, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, Field<?> field) {
        return get(index).getValueAsTime(field);
    }

    @Override
    public final Time getValueAsTime(int index, Field<?> field, Time defaultValue) {
        return get(index).getValueAsTime(field, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, int fieldIndex) {
        return get(index).getValueAsTime(fieldIndex);
    }

    @Override
    public final Time getValueAsTime(int index, int fieldIndex, Time defaultValue) {
        return get(index).getValueAsTime(fieldIndex, defaultValue);
    }

    @Override
    public final String getValueAsString(int index, String fieldName) {
        return get(index).getValueAsString(fieldName);
    }

    @Override
    public final String getValueAsString(int index, String fieldName, String defaultValue) {
        return get(index).getValueAsString(fieldName, defaultValue);
    }

    @Override
    public final Byte getValueAsByte(int index, String fieldName) {
        return get(index).getValueAsByte(fieldName);
    }

    @Override
    public final Byte getValueAsByte(int index, String fieldName, Byte defaultValue) {
        return get(index).getValueAsByte(fieldName, defaultValue);
    }

    @Override
    public final Short getValueAsShort(int index, String fieldName) {
        return get(index).getValueAsShort(fieldName);
    }

    @Override
    public final Short getValueAsShort(int index, String fieldName, Short defaultValue) {
        return get(index).getValueAsShort(fieldName, defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(int index, String fieldName) {
        return get(index).getValueAsInteger(fieldName);
    }

    @Override
    public final Integer getValueAsInteger(int index, String fieldName, Integer defaultValue) {
        return get(index).getValueAsInteger(fieldName, defaultValue);
    }

    @Override
    public final Long getValueAsLong(int index, String fieldName) {
        return get(index).getValueAsLong(fieldName);
    }

    @Override
    public final Long getValueAsLong(int index, String fieldName, Long defaultValue) {
        return get(index).getValueAsLong(fieldName, defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, String fieldName) {
        return get(index).getValueAsBigInteger(fieldName);
    }

    @Override
    public final BigInteger getValueAsBigInteger(int index, String fieldName, BigInteger defaultValue) {
        return get(index).getValueAsBigInteger(fieldName, defaultValue);
    }

    @Override
    public final Float getValueAsFloat(int index, String fieldName) {
        return get(index).getValueAsFloat(fieldName);
    }

    @Override
    public final Float getValueAsFloat(int index, String fieldName, Float defaultValue) {
        return get(index).getValueAsFloat(fieldName, defaultValue);
    }

    @Override
    public final Double getValueAsDouble(int index, String fieldName) {
        return get(index).getValueAsDouble(fieldName);
    }

    @Override
    public final Double getValueAsDouble(int index, String fieldName, Double defaultValue) {
        return get(index).getValueAsDouble(fieldName, defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, String fieldName) {
        return get(index).getValueAsBigDecimal(fieldName);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(int index, String fieldName, BigDecimal defaultValue) {
        return get(index).getValueAsBigDecimal(fieldName, defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, String fieldName) {
        return get(index).getValueAsBoolean(fieldName);
    }

    @Override
    public final Boolean getValueAsBoolean(int index, String fieldName, Boolean defaultValue) {
        return get(index).getValueAsBoolean(fieldName, defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, String fieldName) {
        return get(index).getValueAsTimestamp(fieldName);
    }

    @Override
    public final Timestamp getValueAsTimestamp(int index, String fieldName, Timestamp defaultValue) {
        return get(index).getValueAsTimestamp(fieldName, defaultValue);
    }

    @Override
    public final Date getValueAsDate(int index, String fieldName) {
        return get(index).getValueAsDate(fieldName);
    }

    @Override
    public final Date getValueAsDate(int index, String fieldName, Date defaultValue) {
        return get(index).getValueAsDate(fieldName, defaultValue);
    }

    @Override
    public final Time getValueAsTime(int index, String fieldName) {
        return get(index).getValueAsTime(fieldName);
    }

    @Override
    public final Time getValueAsTime(int index, String fieldName, Time defaultValue) {
        return get(index).getValueAsTime(fieldName, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> List<T> getValues(Field<T> field) {
        return (List<T>) getValues(fields.getIndex(field));
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
        List<Object> result = new ArrayList<Object>(size());

        for (R record : this) {
            result.add(record.getValue(fieldIndex));
        }

        return result;
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
        List<BigDecimal> result = new ArrayList<BigDecimal>(size());

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
        List<BigInteger> result = new ArrayList<BigInteger>(size());

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
        List<Byte> result = new ArrayList<Byte>(size());

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
        List<Date> result = new ArrayList<Date>(size());

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
        List<Double> result = new ArrayList<Double>(size());

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
        List<Float> result = new ArrayList<Float>(size());

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
        List<Integer> result = new ArrayList<Integer>(size());

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
        List<Long> result = new ArrayList<Long>(size());

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
        List<Short> result = new ArrayList<Short>(size());

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
        List<String> result = new ArrayList<String>(size());

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
        List<Time> result = new ArrayList<Time>(size());

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
        List<Timestamp> result = new ArrayList<Timestamp>(size());

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
        final int COL_MIN_WIDTH = 4;
        final int COL_MAX_WIDTH = 50;

        // Numeric columns have greater max width because values are aligned
        final int NUM_COL_MAX_WIDTH = 100;

        // The max number of records that will be considered for formatting purposes
        final int MAX_RECORDS = min(50, maxRecords);

        // Get max decimal places for numeric type columns
        final int size = getFields().size();
        final int[] decimalPlaces = new int[size];
        final int[] widths = new int[size];

        for (int index = 0; index < size; index++) {
            Field<?> f = getField(index);

            if (Number.class.isAssignableFrom(f.getType())) {
                List<Integer> decimalPlacesList = new ArrayList<Integer>();

                // Initialize
                decimalPlacesList.add(0);

                // Collect all decimal places for the column values
                String value;
                for (int i = 0; i < min(MAX_RECORDS, size()); i++) {
                    value = format0(getValue(i, index));
                    decimalPlacesList.add(getDecimalPlaces(value));
                }

                // Find max
                decimalPlaces[index] = Collections.max(decimalPlacesList);
            }
        }

        // Get max column widths
        int colMaxWidth;
        for (int index = 0; index < size; index++) {
            Field<?> f = getField(index);

            // Is number column?
            boolean isNumCol = Number.class.isAssignableFrom(f.getType());

            colMaxWidth = isNumCol ? NUM_COL_MAX_WIDTH : COL_MAX_WIDTH;

            // Collect all widths for the column
            List<Integer> widthList = new ArrayList<Integer>();

            // Add column name width first
            widthList.add(min(colMaxWidth, max(COL_MIN_WIDTH, f.getName().length())));

            // Add column values width
            String value;
            for (int i = 0; i < min(MAX_RECORDS, size()); i++) {
                value = format0(getValue(i, index));
                // Align number values before width is calculated
                if (isNumCol) {
                    value = alignNumberValue(decimalPlaces[index], value);
                }

                widthList.add(min(colMaxWidth, value.length()));
            }

            // Find max
            widths[index] = Collections.max(widthList);
        }

        // Begin the writing
        // ---------------------------------------------------------------------
        StringBuilder sb = new StringBuilder();

        // Write top line
        sb.append("+");
        for (int index = 0; index < size; index++) {
            sb.append(rightPad("", widths[index], "-"));
            sb.append("+");
        }

        // Write headers
        sb.append("\n|");
        for (int index = 0; index < size; index++) {
            Field<?> f = getField(index);
            String padded;

            if (Number.class.isAssignableFrom(f.getType())) {
                padded = leftPad(f.getName(), widths[index]);
            }
            else {
                padded = rightPad(f.getName(), widths[index]);
            }

            sb.append(abbreviate(padded, widths[index]));
            sb.append("|");
        }

        // Write separator
        sb.append("\n+");
        for (int index = 0; index < size; index++) {
            sb.append(rightPad("", widths[index], "-"));
            sb.append("+");
        }

        // Write columns
        for (int i = 0; i < min(maxRecords, size()); i++) {
            sb.append("\n|");

            for (int index = 0; index < size; index++) {
                Field<?> f = getField(index);
                String value = format0(getValue(i, index)).replace("\n", "{lf}").replace("\r", "{cr}");

                String padded;
                if (Number.class.isAssignableFrom(f.getType())) {
                    // Align number value before left pad
                    value = alignNumberValue(decimalPlaces[index], value);

                    // Left pad
                    padded = leftPad(value, widths[index]);
                }
                else {
                    // Right pad
                    padded = rightPad(value, widths[index]);
                }

                sb.append(abbreviate(padded, widths[index]));
                sb.append("|");
            }
        }

        // Write bottom line
        if (size() > 0) {
            sb.append("\n+");

            for (int index = 0; index < size; index++) {
                sb.append(rightPad("", widths[index], "-"));
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

    private static final String alignNumberValue(Integer columnDecimalPlaces, String value) {
        if (!"{null}".equals(value) && columnDecimalPlaces != 0) {
            int decimalPlaces = getDecimalPlaces(value);
            int rightPadSize = value.length() + columnDecimalPlaces - decimalPlaces;

            if (decimalPlaces == 0) {
                // If integer value, add one for decimal point
                value = rightPad(value, rightPadSize + 1);
            }
            else {
                value = rightPad(value, rightPadSize);
            }
        }

        return value;
    }

    private static final int getDecimalPlaces(String value) {
        int decimalPlaces = 0;

        int dotIndex = value.indexOf(".");
        if (dotIndex != -1) {
            decimalPlaces = value.length() - dotIndex - 1;
        }

        return decimalPlaces;
    }

    @Override
    public final String formatHTML() {
        final int size = getFields().size();

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

            for (int index = 0; index < size; index++) {
                sb.append("<td>");
                sb.append(format0(record.getValue(index)));
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
        return formatCSV(',', "");
    }

    @Override
    public final String formatCSV(char delimiter) {
        return formatCSV(delimiter, "");
    }

    @Override
    public final String formatCSV(char delimiter, String nullString) {
        final int size = getFields().size();

        StringBuilder sb = new StringBuilder();

        String sep1 = "";
        for (Field<?> field : getFields()) {
            sb.append(sep1);
            sb.append(formatCSV0(field.getName(), ""));

            sep1 = Character.toString(delimiter);
        }

        sb.append("\n");

        for (Record record : this) {
            String sep2 = "";

            for (int index = 0; index < size; index++) {
                sb.append(sep2);
                sb.append(formatCSV0(record.getValue(index), nullString));

                sep2 = Character.toString(delimiter);
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private final String formatCSV0(Object value, String nullString) {

        // Escape null and empty strings
        if (value == null || "".equals(value)) {
            if (StringUtils.isEmpty(nullString)) {
                return "\"\"";
            }
            else {
                return nullString;
            }
        }

        String result = format0(value);

        if (StringUtils.containsAny(result, ',', ';', '\t', '"', '\n', '\r', '\'')) {
            return "\"" + result.replace("\"", "\"\"") + "\"";
        }
        else {
            return result;
        }
    }

    private static final String format0(Object value) {
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
        final int size = getFields().size();

        List<Map<String, String>> f = new ArrayList<Map<String, String>>();
        List<List<Object>> r = new ArrayList<List<Object>>();

        Map<String, String> fieldMap;
        for (Field<?> field : getFields()) {
            fieldMap = new LinkedHashMap<String, String>();
            fieldMap.put("name", field.getName());
            fieldMap.put("type", field.getDataType().getTypeName().toUpperCase());

            f.add(fieldMap);
        }

        for (Record record : this) {
            List<Object> list = new ArrayList<Object>();

            for (int index = 0; index < size; index++) {
                list.add(record.getValue(index));
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
        final int size = getFields().size();

        StringBuilder sb = new StringBuilder();

        sb.append("<result xmlns=\"http://www.jooq.org/xsd/jooq-export-2.6.0.xsd\">");
        sb.append("<fields>");

        for (Field<?> field : getFields()) {
            sb.append("<field name=\"");
            sb.append(escapeXML(field.getName()));
            sb.append("\" ");
            sb.append("type=\"");
            sb.append(field.getDataType().getTypeName().toUpperCase());
            sb.append("\"/>");
        }

        sb.append("</fields>");
        sb.append("<records>");

        for (Record record : this) {
            sb.append("<record>");

            for (int index = 0; index < size; index++) {
                Field<?> field = getField(index);
                Object value = record.getValue(index);

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
        sb.append("</result>");

        return sb.toString();
    }

    @Override
    public final Document exportXML() {
        return intoXML();
    }

    @Override
    public final Document intoXML() {
        final int size = getFields().size();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element eResult = document.createElement("result");
            eResult.setAttribute("xmlns", "http://www.jooq.org/xsd/jooq-export-2.6.0.xsd");
            document.appendChild(eResult);

            Element eFields = document.createElement("fields");
            eResult.appendChild(eFields);

            for (Field<?> field : getFields()) {
                Element eField = document.createElement("field");
                eField.setAttribute("name", field.getName());
                eField.setAttribute("type", field.getDataType().getTypeName().toUpperCase());
                eFields.appendChild(eField);
            }

            Element eRecords = document.createElement("records");
            eResult.appendChild(eRecords);

            for (Record record : this) {
                Element eRecord = document.createElement("record");
                eRecords.appendChild(eRecord);

                for (int index = 0; index < size; index++) {
                    Field<?> field = getField(index);
                    Object value = record.getValue(index);

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
    public final List<Map<String, Object>> intoMaps() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (R record : this) {
            list.add(record.intoMap());
        }

        return list;

    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K> Map<K, R> intoMap(Field<K> key) {
        int index = getIndex(key);
        Map<K, R> map = new LinkedHashMap<K, R>();

        for (R record : this) {
            if (map.put((K) record.getValue(index), record) != null) {
                throw new InvalidResultException("Key " + key + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K, V> Map<K, V> intoMap(Field<K> key, Field<V> value) {
        int kIndex = getIndex(key);
        int vIndex = getIndex(value);

        Map<K, V> map = new LinkedHashMap<K, V>();

        for (R record : this) {
            if (map.put((K) record.getValue(kIndex), (V) record.getValue(vIndex)) != null) {
                throw new InvalidResultException("Key " + key + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @Override
    public final Map<Record, R> intoMap(Field<?>[] keys) {
        if (keys == null) {
            keys = new Field[0];
        }

        Map<Record, R> map = new LinkedHashMap<Record, R>();
        FieldList keyList = new FieldList(keys);

        for (R record : this) {
            Record key = new RecordImpl(keyList);

            for (Field<?> field : keys) {
                Utils.setValue(key, field, record, field);
            }

            if (map.put(key, record) != null) {
                throw new InvalidResultException("Key list " + keyList + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @Override
    public final <E> Map<List<?>, E> intoMap(Field<?>[] keys, Class<? extends E> type) {
        if (keys == null) {
            keys = new Field[0];
        }

        Map<List<?>, E> map = new LinkedHashMap<List<?>, E>();

        for (R record : this) {
            List<Object> keyValueList = new ArrayList<Object>();
            for (Field<?> key : keys) {
                keyValueList.add(record.getValue(key));
            }

            if (map.put(keyValueList, record.into(type)) != null) {
                throw new InvalidResultException("Key list " + keyValueList + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K, E> Map<K, E> intoMap(Field<K> key, Class<? extends E> type) {
        int index = getIndex(key);
        Map<K, E> map = new LinkedHashMap<K, E>();

        for (R record : this) {
            if (map.put((K) record.getValue(index), record.into(type)) != null) {
                throw new InvalidResultException("Key " + key + " is not unique in Result for " + this);
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K> Map<K, Result<R>> intoGroups(Field<K> key) {
        int index = getIndex(key);
        Map<K, Result<R>> map = new LinkedHashMap<K, Result<R>>();

        for (R record : this) {
            K val = (K) record.getValue(index);
            Result<R> result = map.get(val);

            if (result == null) {
                result = new ResultImpl<R>(configuration, fields);
                map.put(val, result);
            }

            result.add(record);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K, V> Map<K, List<V>> intoGroups(Field<K> key, Field<V> value) {
        int kIndex = getIndex(key);
        int vIndex = getIndex(value);

        Map<K, List<V>> map = new LinkedHashMap<K, List<V>>();

        for (R record : this) {
            K k = (K) record.getValue(kIndex);
            V v = (V) record.getValue(vIndex);
            List<V> result = map.get(k);

            if (result == null) {
                result = new ArrayList<V>();
                map.put(k, result);
            }

            result.add(v);
        }

        return map;
    }

    @Override
    public final Map<Record, Result<R>> intoGroups(Field<?>[] keys) {
        if (keys == null) {
            keys = new Field[0];
        }

        Map<Record, Result<R>> map = new LinkedHashMap<Record, Result<R>>();
        FieldList keyList = new FieldList(keys);

        for (R record : this) {
            Record key = new RecordImpl(keyList);

            for (Field<?> field : keys) {
                Utils.setValue(key, field, record, field);
            }

            Result<R> result = map.get(key);
            if (result == null) {
                result = new ResultImpl<R>(getConfiguration(), this.fields);
                map.put(key, result);
            }

            result.add(record);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K, E> Map<K, List<E>> intoGroups(Field<K> key, Class<? extends E> type) {
        int index = getIndex(key);
        Map<K, List<E>> map = new LinkedHashMap<K, List<E>>();

        for (R record : this) {
            K keyVal = (K) record.getValue(index);

            List<E> list = map.get(keyVal);
            if (list == null) {
                list = new ArrayList<E>();
                map.put(keyVal, list);
            }

            list.add(record.into(type));
        }

        return map;
    }

    @Override
    public final <E> Map<Record, List<E>> intoGroups(Field<?>[] keys, Class<? extends E> type) {
        if (keys == null) {
            keys = new Field[0];
        }

        Map<Record, List<E>> map = new LinkedHashMap<Record, List<E>>();
        FieldList keyList = new FieldList(keys);

        for (R record : this) {
            Record key = new RecordImpl(keyList);

            for (Field<?> field : keys) {
                Utils.setValue(key, field, record, field);
            }

            List<E> list = map.get(key);
            if (list == null) {
                list = new ArrayList<E>();
                map.put(key, list);
            }

            list.add(record.into(type));
        }

        return map;
    }

    @Override
    public final Object[][] intoArray() {
        int size = size();
        Object[][] array = new Object[size][];

        for (int i = 0; i < size; i++) {
            array[i] = get(i).intoArray();
        }

        return array;
    }

    @Override
    public final Object[] intoArray(int fieldIndex) {
        Class<?> type = getField(fieldIndex).getType();
        List<?> list = getValues(fieldIndex);
        return list.toArray((Object[]) Array.newInstance(type, list.size()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] intoArray(int fieldIndex, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(fieldIndex), type);
    }

    @SuppressWarnings("cast")
    @Override
    public final <U> U[] intoArray(int fieldIndex, Converter<?, U> converter) {
        return (U[]) Convert.convertArray(intoArray(fieldIndex), converter);
    }

    @Override
    public final Object[] intoArray(String fieldName) {
        Class<?> type = getField(fieldName).getType();
        List<?> list = getValues(fieldName);
        return list.toArray((Object[]) Array.newInstance(type, list.size()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] intoArray(String fieldName, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(fieldName), type);
    }

    @SuppressWarnings("cast")
    @Override
    public final <U> U[] intoArray(String fieldName, Converter<?, U> converter) {
        return (U[]) Convert.convertArray(intoArray(fieldName), converter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] intoArray(Field<T> field) {
        return getValues(field).toArray((T[]) Array.newInstance(field.getType(), 0));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] intoArray(Field<?> field, Class<? extends T> type) {
        return (T[]) Convert.convertArray(intoArray(field), type);
    }

    @SuppressWarnings("cast")
    @Override
    public final <T, U> U[] intoArray(Field<T> field, Converter<? super T, U> converter) {
        return (U[]) Convert.convertArray(intoArray(field), converter);
    }

    @Override
    public final <T> List<T> into(Class<? extends T> type) {
        List<T> list = new ArrayList<T>(size());

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

    @Override
    public final <E> List<E> map(RecordMapper<? super R, E> mapper) {
        List<E> result = new ArrayList<E>();

        for (R record : this) {
            result.add(mapper.map(record));
        }

        return result;
    }

    @Override
    public final ResultSet intoResultSet() {
        return new ResultSetImpl(this);
    }

    @Override
    public final <T extends Comparable<? super T>> Result<R> sortAsc(Field<T> field) {
        return sortAsc(field, new NaturalComparator<T>());
    }

    @Override
    public final <T> Result<R> sortAsc(Field<T> field, Comparator<? super T> comparator) {
        return sortAsc(new RecordComparator<T, R>(getIndex(field), comparator));
    }

    @Override
    public final Result<R> sortAsc(Comparator<? super R> comparator) {
        Collections.sort(this, comparator);
        return this;
    }

    @Override
    public final <T extends Comparable<? super T>> Result<R> sortDesc(Field<T> field) {
        return sortAsc(field, Collections.reverseOrder(new NaturalComparator<T>()));
    }

    @Override
    public final <T> Result<R> sortDesc(Field<T> field, Comparator<? super T> comparator) {
        return sortAsc(field, Collections.reverseOrder(comparator));
    }

    @Override
    public final Result<R> sortDesc(Comparator<? super R> comparator) {
        return sortAsc(Collections.reverseOrder(comparator));
    }

    /**
     * A comparator for records, wrapping another comparator for &lt;T&gt;
     */
    private static class RecordComparator<T, R extends Record> implements Comparator<R> {

        private final Comparator<? super T> comparator;
        private final int fieldIndex;

        RecordComparator(int fieldIndex, Comparator<? super T> comparator) {
            this.fieldIndex = fieldIndex;
            this.comparator = comparator;
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(R record1, R record2) {
            return comparator.compare((T) record1.getValue(fieldIndex), (T) record2.getValue(fieldIndex));
        }
    }

    /**
     * A natural comparator
     */
    private static class NaturalComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            else if (o1 == null) {
                return -1;
            }
            else if (o2 == null) {
                return 1;
            }
            return o1.compareTo(o2);
        }
    }

    // -------------------------------------------------------------------------
    // XXX Object API
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
    // XXX: List API
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
