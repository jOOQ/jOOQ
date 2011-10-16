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

package org.jooq.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
abstract class AbstractRecord extends AbstractStore<Object> implements Record {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -6052512608911220404L;

    private final FieldProvider fields;
    private Value<?>[]          values;

    AbstractRecord(FieldProvider fields) {
        this(fields, null);
    }

    /**
     * @deprecated - 1.6.4 [#789] - Create attached records using
     *             {@link Factory#newRecord(Table)} instead. Detached records
     *             can be created using {@link #TableRecordImpl(Table)}
     */
    @Deprecated
    AbstractRecord(FieldProvider fields, Configuration configuration) {
        super(configuration);

        this.fields = fields;
    }

    final FieldProvider getMetaData() {
        return fields;
    }

    @Override
    public final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();

        for (Field<?> field : getFields()) {
            Object value = getValue(field);

            if (value instanceof Attachable) {
                result.add((Attachable) value);
            }
        }

        return result;
    }

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
    public final int size() {
        return getFields().size();
    }

    @SuppressWarnings("unchecked")
    final <T> Value<T> getValue0(Field<T> field) {
        return (Value<T>) getValues()[getIndex(field)];
    }

    final Value<?>[] getValues() {
        if (values == null) {
            init();
        }

        return values;
    }

    @Override
    public final int getIndex(Field<?> field) {
        return fields.getIndex(field);
    }

    private final void init() {
        values = new Value<?>[fields.getFields().size()];

        for (int i = 0; i < values.length; i++) {
            values[i] = new Value<Object>(null);
        }
    }

    @Override
    public final <T> T getValue(Field<T> field) throws IllegalArgumentException {
        return getValue0(field).getValue();
    }

    @Override
    public final <T> T getValue(Field<T> field, T defaultValue) throws IllegalArgumentException {
        return getValue0(field).getValue(defaultValue);
    }

    @Override
    public final <T> void setValue(Field<T> field, T value) {
        getValue0(field).setValue(value);
    }

    final <T> void setValue(Field<T> field, Value<T> value) {
        getValues()[getIndex(field)] = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [values=" + Arrays.asList(getValues()) + "]";
    }

    @Override
    public final String getValueAsString(Field<?> field) throws IllegalArgumentException {
        return getValueAsString(getIndex(field));
    }

    @Override
    public final String getValueAsString(Field<?> field, String defaultValue) throws IllegalArgumentException {
        return getValueAsString(getIndex(field), defaultValue);
    }

    @Override
    public final Byte getValueAsByte(Field<?> field) throws IllegalArgumentException {
        return getValueAsByte(getIndex(field));
    }

    @Override
    public final Byte getValueAsByte(Field<?> field, Byte defaultValue) throws IllegalArgumentException {
        return getValueAsByte(getIndex(field), defaultValue);
    }

    @Override
    public final Short getValueAsShort(Field<?> field) throws IllegalArgumentException {
        return getValueAsShort(getIndex(field));
    }

    @Override
    public final Short getValueAsShort(Field<?> field, Short defaultValue) throws IllegalArgumentException {
        return getValueAsShort(getIndex(field), defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(Field<?> field) throws IllegalArgumentException {
        return getValueAsInteger(getIndex(field));
    }

    @Override
    public final Integer getValueAsInteger(Field<?> field, Integer defaultValue) throws IllegalArgumentException {
        return getValueAsInteger(getIndex(field), defaultValue);
    }

    @Override
    public final Long getValueAsLong(Field<?> field) throws IllegalArgumentException {
        return getValueAsLong(getIndex(field));
    }

    @Override
    public final Long getValueAsLong(Field<?> field, Long defaultValue) throws IllegalArgumentException {
        return getValueAsLong(getIndex(field), defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(Field<?> field) throws IllegalArgumentException {
        return getValueAsBigInteger(getIndex(field));
    }

    @Override
    public final BigInteger getValueAsBigInteger(Field<?> field, BigInteger defaultValue)
        throws IllegalArgumentException {
        return getValueAsBigInteger(getIndex(field), defaultValue);
    }

    @Override
    public final Float getValueAsFloat(Field<?> field) throws IllegalArgumentException {
        return getValueAsFloat(getIndex(field));
    }

    @Override
    public final Float getValueAsFloat(Field<?> field, Float defaultValue) throws IllegalArgumentException {
        return getValueAsFloat(getIndex(field), defaultValue);
    }

    @Override
    public final Double getValueAsDouble(Field<?> field) throws IllegalArgumentException {
        return getValueAsDouble(getIndex(field));
    }

    @Override
    public final Double getValueAsDouble(Field<?> field, Double defaultValue) throws IllegalArgumentException {
        return getValueAsDouble(getIndex(field), defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(Field<?> field) throws IllegalArgumentException {
        return getValueAsBigDecimal(getIndex(field));
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(Field<?> field, BigDecimal defaultValue)
        throws IllegalArgumentException {
        return getValueAsBigDecimal(getIndex(field), defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(Field<?> field) throws IllegalArgumentException {
        return getValueAsBoolean(getIndex(field));
    }

    @Override
    public final Boolean getValueAsBoolean(Field<?> field, Boolean defaultValue) throws IllegalArgumentException {
        return getValueAsBoolean(getIndex(field), defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(Field<?> field) throws IllegalArgumentException {
        return getValueAsTimestamp(getIndex(field));
    }

    @Override
    public final Timestamp getValueAsTimestamp(Field<?> field, Timestamp defaultValue) throws IllegalArgumentException {
        return getValueAsTimestamp(getIndex(field), defaultValue);
    }

    @Override
    public final Date getValueAsDate(Field<?> field) throws IllegalArgumentException {
        return getValueAsDate(getIndex(field));
    }

    @Override
    public final Date getValueAsDate(Field<?> field, Date defaultValue) throws IllegalArgumentException {
        return getValueAsDate(getIndex(field), defaultValue);
    }

    @Override
    public final Time getValueAsTime(Field<?> field) throws IllegalArgumentException {
        return getValueAsTime(getIndex(field));
    }

    @Override
    public final Time getValueAsTime(Field<?> field, Time defaultValue) throws IllegalArgumentException {
        return getValueAsTime(getIndex(field), defaultValue);
    }

    @Override
    public final Object getValue(int index) throws IllegalArgumentException {
        return getValue(getField(index));
    }

    @Override
    public final Object getValue(String fieldName) throws IllegalArgumentException {
        return getValue(getField(fieldName));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Object getValue(String fieldName, Object defaultValue) throws IllegalArgumentException {
        return getValue((Field<Object>) getField(fieldName), defaultValue);
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(Field<A> field) throws IllegalArgumentException {
        A result = getValue(field);
        return result == null ? null : result.get();
    }

    @Override
    public final <A extends ArrayRecord<T>, T> T[] getValueAsArray(Field<A> field, T[] defaultValue)
        throws IllegalArgumentException {
        final T[] result = getValueAsArray(field);
        return result == null ? defaultValue : result;
    }

    @Override
    public final String getValueAsString(String fieldName) throws IllegalArgumentException {
        return getValueAsString(getField(fieldName));
    }

    @Override
    public final String getValueAsString(String fieldName, String defaultValue) throws IllegalArgumentException {
        return getValueAsString(getField(fieldName), defaultValue);
    }

    @Override
    public final Byte getValueAsByte(String fieldName) throws IllegalArgumentException {
        return getValueAsByte(getField(fieldName));
    }

    @Override
    public final Byte getValueAsByte(String fieldName, Byte defaultValue) throws IllegalArgumentException {
        return getValueAsByte(getField(fieldName), defaultValue);
    }

    @Override
    public final Short getValueAsShort(String fieldName) throws IllegalArgumentException {
        return getValueAsShort(getField(fieldName));
    }

    @Override
    public final Short getValueAsShort(String fieldName, Short defaultValue) throws IllegalArgumentException {
        return getValueAsShort(getField(fieldName), defaultValue);
    }

    @Override
    public final Integer getValueAsInteger(String fieldName) throws IllegalArgumentException {
        return getValueAsInteger(getField(fieldName));
    }

    @Override
    public final Integer getValueAsInteger(String fieldName, Integer defaultValue) throws IllegalArgumentException {
        return getValueAsInteger(getField(fieldName), defaultValue);
    }

    @Override
    public final Long getValueAsLong(String fieldName) throws IllegalArgumentException {
        return getValueAsLong(getField(fieldName));
    }

    @Override
    public final Long getValueAsLong(String fieldName, Long defaultValue) throws IllegalArgumentException {
        return getValueAsLong(getField(fieldName), defaultValue);
    }

    @Override
    public final BigInteger getValueAsBigInteger(String fieldName) throws IllegalArgumentException {
        return getValueAsBigInteger(getField(fieldName));
    }

    @Override
    public final BigInteger getValueAsBigInteger(String fieldName, BigInteger defaultValue)
        throws IllegalArgumentException {
        return getValueAsBigInteger(getField(fieldName), defaultValue);
    }

    @Override
    public final Float getValueAsFloat(String fieldName) throws IllegalArgumentException {
        return getValueAsFloat(getField(fieldName));
    }

    @Override
    public final Float getValueAsFloat(String fieldName, Float defaultValue) throws IllegalArgumentException {
        return getValueAsFloat(getField(fieldName), defaultValue);
    }

    @Override
    public final Double getValueAsDouble(String fieldName) throws IllegalArgumentException {
        return getValueAsDouble(getField(fieldName));
    }

    @Override
    public final Double getValueAsDouble(String fieldName, Double defaultValue) throws IllegalArgumentException {
        return getValueAsDouble(getField(fieldName), defaultValue);
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(String fieldName) throws IllegalArgumentException {
        return getValueAsBigDecimal(getField(fieldName));
    }

    @Override
    public final BigDecimal getValueAsBigDecimal(String fieldName, BigDecimal defaultValue)
        throws IllegalArgumentException {
        return getValueAsBigDecimal(getField(fieldName), defaultValue);
    }

    @Override
    public final Boolean getValueAsBoolean(String fieldName) throws IllegalArgumentException {
        return getValueAsBoolean(getField(fieldName));
    }

    @Override
    public final Boolean getValueAsBoolean(String fieldName, Boolean defaultValue) throws IllegalArgumentException {
        return getValueAsBoolean(getField(fieldName), defaultValue);
    }

    @Override
    public final Timestamp getValueAsTimestamp(String fieldName) throws IllegalArgumentException {
        return getValueAsTimestamp(getField(fieldName));
    }

    @Override
    public final Timestamp getValueAsTimestamp(String fieldName, Timestamp defaultValue)
        throws IllegalArgumentException {
        return getValueAsTimestamp(getField(fieldName), defaultValue);
    }

    @Override
    public final Date getValueAsDate(String fieldName) throws IllegalArgumentException {
        return getValueAsDate(getField(fieldName));
    }

    @Override
    public final Date getValueAsDate(String fieldName, Date defaultValue) throws IllegalArgumentException {
        return getValueAsDate(getField(fieldName), defaultValue);
    }

    @Override
    public final Time getValueAsTime(String fieldName) throws IllegalArgumentException {
        return getValueAsTime(getField(fieldName));
    }

    @Override
    public final Time getValueAsTime(String fieldName, Time defaultValue) throws IllegalArgumentException {
        return getValueAsTime(getField(fieldName), defaultValue);
    }

    @Override
    public final <T> T getValue(Field<?> field, Class<? extends T> type) throws IllegalArgumentException {
        return TypeUtils.convert(getValue(field), type);
    }

    @Override
    public final <T> T getValue(Field<?> field, Class<? extends T> type, T defaultValue) throws IllegalArgumentException {
        final T result = getValue(field, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <T> T getValue(String fieldName, Class<? extends T> type) throws IllegalArgumentException {
        return TypeUtils.convert(getValue(fieldName), type);
    }

    @Override
    public final <Z> Z getValue(String fieldName, Class<? extends Z> type, Z defaultValue) throws IllegalArgumentException {
        final Z result = getValue(fieldName, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <T> T into(Class<? extends T> type) {
        try {
            T result = type.newInstance();

            for (Field<?> field : getFields()) {
                List<java.lang.reflect.Field> members;
                List<java.lang.reflect.Method> methods;

                // Annotations are available and present
                if (JooqUtil.isJPAAvailable() && JooqUtil.hasColumnAnnotations(type)) {
                    members = JooqUtil.getAnnotatedMembers(type, field.getName());
                    methods = JooqUtil.getAnnotatedMethods(type, field.getName());
                }

                // No annotations are present
                else {
                    members = JooqUtil.getMatchingMembers(type, field.getName());
                    methods = JooqUtil.getMatchingMethods(type, field.getName());
                }

                for (java.lang.reflect.Field member : members) {
                    copyInto(result, member, field);
                }

                for (java.lang.reflect.Method method : methods) {
                    copyInto(result, method, field);
                }
            }

            return result;
        }

        // All reflection exceptions are intercepted
        catch (Exception e) {
            throw new IllegalStateException("An error ocurred when mapping record to " + type, e);
        }
    }

    private final void copyInto(Object result, Method method, Field<?> field)
        throws IllegalAccessException, InvocationTargetException {

        Class<?> mType = method.getParameterTypes()[0];

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                method.invoke(result, getValueAsByte(field, (byte) 0));
            }
            else if (mType == short.class) {
                method.invoke(result, getValueAsShort(field, (short) 0));
            }
            else if (mType == int.class) {
                method.invoke(result, getValueAsInteger(field, 0));
            }
            else if (mType == long.class) {
                method.invoke(result, getValueAsLong(field, 0L));
            }
            else if (mType == float.class) {
                method.invoke(result, getValueAsFloat(field, 0.0f));
            }
            else if (mType == double.class) {
                method.invoke(result, getValueAsDouble(field, 0.0));
            }
            else if (mType == boolean.class) {
                method.invoke(result, getValueAsBoolean(field, false));
            }
        }
        else {
            method.invoke(result, getValue(field, mType));
        }
    }

    private final void copyInto(Object result, java.lang.reflect.Field member, Field<?> field) throws IllegalAccessException {
        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                member.setByte(result, getValueAsByte(field, (byte) 0));
            }
            else if (mType == short.class) {
                member.setShort(result, getValueAsShort(field, (short) 0));
            }
            else if (mType == int.class) {
                member.setInt(result, getValueAsInteger(field, 0));
            }
            else if (mType == long.class) {
                member.setLong(result, getValueAsLong(field, 0L));
            }
            else if (mType == float.class) {
                member.setFloat(result, getValueAsFloat(field, 0.0f));
            }
            else if (mType == double.class) {
                member.setDouble(result, getValueAsDouble(field, 0.0));
            }
            else if (mType == boolean.class) {
                member.setBoolean(result, getValueAsBoolean(field, false));
            }
        }
        else {
            member.set(result, getValue(field, mType));
        }
    }
}
