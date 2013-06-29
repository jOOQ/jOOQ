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

package org.jooq.impl;

import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.RecordType;

/**
 * A simple wrapper for <code>Field[]</code>, providing some useful lookup
 * methods
 *
 * @author Lukas Eder
 */
class Fields<R extends Record> extends AbstractQueryPart implements RecordType<R> {

    private static final long serialVersionUID = -6911012275707591576L;
    Field<?>[]                fields;

    Fields(Field<?>... fields) {
        this.fields = fields;
    }

    Fields(Collection<? extends Field<?>> fields) {
        this.fields = fields.toArray(new Field[fields.size()]);
    }

    @Override
    public final int size() {
        return fields.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> Field<T> field(Field<T> field) {
        if (field == null) {
            return null;
        }

        // [#1802] Try finding an exact match (e.g. exact matching qualified name)
        for (Field<?> f : fields) {
            if (f.equals(field)) {
                return (Field<T>) f;
            }
        }

        // In case no exact match was found, return the first field with matching name
        String name = field.getName();
        for (Field<?> f1 : fields) {
            if (f1.getName().equals(name)) {
                return (Field<T>) f1;
            }
        }

        return null;
    }

    @Override
    public final Field<?> field(String name) {
        if (name == null) {
            return null;
        }

        for (Field<?> f : fields) {
            if (f.getName().equals(name)) {
                return f;
            }
        }

        return null;
    }

    @Override
    public final Field<?> field(int index) {
        if (index >= 0 && index < fields.length) {
            return fields[index];
        }

        return null;
    }

    @Override
    public final Field<?>[] fields() {
        return fields;
    }

    @Override
    public final Field<?>[] fields(Field<?>... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final Field<?>[] fields(String... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final Field<?>[] fields(int... f) {
        Field<?>[] result = new Field[f.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = field(f[i]);
        }

        return result;
    }

    @Override
    public final int indexOf(Field<?> field) {

        // Get an exact match, or a field with a similar name
        Field<?> compareWith = field(field);

        if (compareWith != null) {
            int size = fields.length;

            for (int i = 0; i < size; i++) {
                if (fields[i].equals(compareWith))
                    return i;
            }
        }

        return -1;
    }

    @Override
    public final int indexOf(String fieldName) {
        return indexOf(field(fieldName));
    }

    @Override
    public final Class<?>[] types() {
        int size = fields.length;
        Class<?>[] result = new Class[size];

        for (int i = 0; i < size; i++) {
            result[i] = field(i).getType();
        }

        return result;
    }

    @Override
    public final Class<?> type(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getType() : null;
    }

    @Override
    public final Class<?> type(String fieldName) {
        return type(indexOf(fieldName));
    }

    @Override
    public final DataType<?>[] dataTypes() {
        int size = fields.length;
        DataType<?>[] result = new DataType[size];

        for (int i = 0; i < size; i++) {
            result[i] = field(i).getDataType();
        }

        return result;
    }

    @Override
    public final DataType<?> dataType(int fieldIndex) {
        return fieldIndex >= 0 && fieldIndex < size() ? field(fieldIndex).getDataType() : null;
    }

    @Override
    public final DataType<?> dataType(String fieldName) {
        return dataType(indexOf(fieldName));
    }

    final int[] indexesOf(Field<?>... f) {
        int[] result = new int[f.length];

        for (int i = 0; i < f.length; i++) {
            result[i] = indexOf(f[i]);
        }

        return result;
    }

    final int[] indexesOf(String... fieldNames) {
        int[] result = new int[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            result[i] = indexOf(fieldNames[i]);
        }

        return result;
    }


    @Override
    public final void toSQL(RenderContext context) {
        new QueryPartList<Field<?>>(fields).toSQL(context);
    }

    @Override
    public final void bind(BindContext context) {
        new QueryPartList<Field<?>>(fields).bind(context);
    }

    // -------------------------------------------------------------------------
    // XXX: List-like API
    // -------------------------------------------------------------------------

    final void add(Field<?> f) {
        Field<?>[] result = new Field[fields.length + 1];

        System.arraycopy(fields, 0, result, 0, fields.length);
        result[fields.length] = f;

        fields = result;
    }
}
