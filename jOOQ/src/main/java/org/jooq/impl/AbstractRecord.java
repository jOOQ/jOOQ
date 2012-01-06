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

import static org.jooq.impl.Util.getAnnotatedGetter;
import static org.jooq.impl.Util.getAnnotatedMembers;
import static org.jooq.impl.Util.getAnnotatedSetters;
import static org.jooq.impl.Util.getMatchingGetter;
import static org.jooq.impl.Util.getMatchingMembers;
import static org.jooq.impl.Util.getMatchingSetters;
import static org.jooq.impl.Util.hasColumnAnnotations;
import static org.jooq.impl.Util.isJPAAvailable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.exception.MappingException;
import org.jooq.tools.Convert;

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
        UniqueKey<?> mainKey = getMainKey();
        Value<T> val = getValue0(field);

        // Normal fields' changed flag is always set to true
        if (mainKey == null || !mainKey.getFields().contains(field)) {
            val.setValue(value);
        }

        // The main key's changed flag might've been set previously
        else if (val.isChanged()) {
            val.setValue(value);
        }

        // [#979] If the main key is being changed, all other fields' flags need
        // to be set to true for in case this record is stored again, an INSERT
        // statement will thus be issued
        else {
            val.setValue(value, true);

            if (val.isChanged()) {
                for (Value<?> other : getValues()) {
                    other.setChanged(true);
                }
            }
        }
    }

    final <T> void setValue(Field<T> field, Value<T> value) {
        getValues()[getIndex(field)] = value;
    }

    /**
     * Subclasses may override this
     */
    UniqueKey<?> getMainKey() {
        return null;
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
        return Convert.convert(getValue(field), type);
    }

    @Override
    public final <T> T getValue(Field<?> field, Class<? extends T> type, T defaultValue) throws IllegalArgumentException {
        final T result = getValue(field, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final <T> T getValue(String fieldName, Class<? extends T> type) throws IllegalArgumentException {
        return Convert.convert(getValue(fieldName), type);
    }

    @Override
    public final <Z> Z getValue(String fieldName, Class<? extends Z> type, Z defaultValue) throws IllegalArgumentException {
        final Z result = getValue(fieldName, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public final Object[] intoArray() {
        return into(Object[].class);
    }

    @Override
    public final <T> T into(Class<? extends T> type) {
        try {
            if (type.isArray()) {
                return intoArray(type);
            }
            else {
                return intoPOJO(type);
            }
        }

        // All reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error ocurred when mapping record to " + type, e);
        }
    }

    @SuppressWarnings("unchecked")
    private final <T> T intoArray(Class<? extends T> type) {
        int size = getFields().size();
        Class<?> componentType = type.getComponentType();
        Object[] result = (Object[]) Array.newInstance(componentType, size);

        for (int i = 0; i < size; i++) {
            result[i] = Convert.convert(getValue(i), componentType);
        }

        return (T) result;
    }

    private final <T> T intoPOJO(Class<? extends T> type) throws Exception {
        T result = type.newInstance();
        boolean useAnnotations = isJPAAvailable() && hasColumnAnnotations(type);

        for (Field<?> field : getFields()) {
            List<java.lang.reflect.Field> members;
            List<java.lang.reflect.Method> methods;

            // Annotations are available and present
            if (useAnnotations) {
                members = getAnnotatedMembers(type, field.getName());
                methods = getAnnotatedSetters(type, field.getName());
            }

            // No annotations are present
            else {
                members = getMatchingMembers(type, field.getName());
                methods = getMatchingSetters(type, field.getName());
            }

            for (java.lang.reflect.Field member : members) {

                // [#935] Avoid setting final fields
                if ((member.getModifiers() & Modifier.FINAL) == 0) {
                    into(result, member, field);
                }
            }

            for (java.lang.reflect.Method method : methods) {
                method.invoke(result, getValue(field, method.getParameterTypes()[0]));
            }
        }

        return result;
    }

    @Override
    public final <R extends Record> R into(Table<R> table) {
        try {
            R result = Util.newRecord(table, getConfiguration());

            for (Field<?> sourceField : getFields()) {
                Field<?> targetField = result.getField(sourceField);

                if (targetField != null) {
                    Util.setValue(result, targetField, this, sourceField);
                }
            }

            return result;
        }

        // All reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error ocurred when mapping record to " + table, e);
        }
    }

    @Override
    public final void from(Object source) {
        if (source == null) return;

        Class<?> type = source.getClass();

        try {
            boolean useAnnotations = isJPAAvailable() && hasColumnAnnotations(type);

            for (Field<?> field : getFields()) {
                List<java.lang.reflect.Field> members;
                Method method;

                // Annotations are available and present
                if (useAnnotations) {
                    members = getAnnotatedMembers(type, field.getName());
                    method = getAnnotatedGetter(type, field.getName());
                }

                // No annotations are present
                else {
                    members = getMatchingMembers(type, field.getName());
                    method = getMatchingGetter(type, field.getName());
                }

                // Use only the first applicable method or member
                if (method != null) {
                    Util.setValue(this, field, method.invoke(source));
                }
                else if (members.size() > 0) {
                    from(source, members.get(0), field);
                }
            }
        }

        // All reflection exceptions are intercepted
        catch (Exception e) {
            throw new MappingException("An error ocurred when mapping record from " + type, e);
        }
    }

    /**
     * This method was implemented with [#799]. It may be useful to make it
     * public for broader use...?
     */
    protected final void from(Record source) {
        for (Field<?> field : getFields()) {
            Field<?> sourceField = source.getField(field);

            if (sourceField != null) {
                Util.setValue(this, field, source, sourceField);
            }
        }
    }

    private final void into(Object result, java.lang.reflect.Field member, Field<?> field) throws IllegalAccessException {
        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                member.setByte(result, getValue(field, byte.class));
            }
            else if (mType == short.class) {
                member.setShort(result, getValue(field, short.class));
            }
            else if (mType == int.class) {
                member.setInt(result, getValue(field, int.class));
            }
            else if (mType == long.class) {
                member.setLong(result, getValue(field, long.class));
            }
            else if (mType == float.class) {
                member.setFloat(result, getValue(field, float.class));
            }
            else if (mType == double.class) {
                member.setDouble(result, getValue(field, double.class));
            }
            else if (mType == boolean.class) {
                member.setBoolean(result, getValue(field, boolean.class));
            }
            else if (mType == char.class) {
                member.setChar(result, getValue(field, char.class));
            }
        }
        else {
            member.set(result, getValue(field, mType));
        }
    }

    private final void from(Object source, java.lang.reflect.Field member, Field<?> field)
        throws IllegalAccessException {

        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                Util.setValue(this, field, member.getByte(source));
            }
            else if (mType == short.class) {
                Util.setValue(this, field, member.getShort(source));
            }
            else if (mType == int.class) {
                Util.setValue(this, field, member.getInt(source));
            }
            else if (mType == long.class) {
                Util.setValue(this, field, member.getLong(source));
            }
            else if (mType == float.class) {
                Util.setValue(this, field, member.getFloat(source));
            }
            else if (mType == double.class) {
                Util.setValue(this, field, member.getDouble(source));
            }
            else if (mType == boolean.class) {
                Util.setValue(this, field, member.getBoolean(source));
            }
            else if (mType == char.class) {
                Util.setValue(this, field, member.getChar(source));
            }
        }
        else {
            Util.setValue(this, field, member.get(source));
        }
    }
}
