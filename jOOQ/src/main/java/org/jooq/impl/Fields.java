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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * A simple wrapper for <code>Field[]</code>, providing some useful lookup
 * methods
 *
 * @author Lukas Eder
 */
class Fields extends AbstractQueryPart implements Iterable<Field<?>> {

    private static final long serialVersionUID = -6911012275707591576L;
    Field<?>[]                fields;

    Fields(Field<?>... fields) {
        this.fields = fields;
    }

    Fields(Collection<? extends Field<?>> fields) {
        this.fields = fields.toArray(new Field[fields.size()]);
    }

    @SuppressWarnings("unchecked")
    final <T> Field<T> field(Field<T> field) {
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

    final Field<?> field(String name) {
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

    final Field<?> field(int index) {
        if (index >= 0 && index < fields.length) {
            return fields[index];
        }

        return null;
    }

    final Field<?>[] fields() {
        return fields;
    }

    final int indexOf(Field<?> field) {

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

    final int indexOf(String fieldName) {
        return indexOf(field(fieldName));
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

    @Override
    public final Iterator<Field<?>> iterator() {
        return Arrays.asList(fields).iterator();
    }

    final void add(Field<?> f) {
        Field<?>[] result = new Field[fields.length + 1];

        System.arraycopy(fields, 0, result, 0, fields.length);
        result[fields.length] = f;

        fields = result;
    }
}
