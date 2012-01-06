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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.Field;
import org.jooq.FieldProvider;

/**
 * @author Lukas Eder
 */
class FieldList extends NamedQueryPartList<Field<?>> implements FieldProvider {

    private static final long                serialVersionUID = -6911012275707591576L;

    private transient Map<Field<?>, Integer> indexes;

    FieldList() {
        super();
    }

    FieldList(Collection<? extends Field<?>> wrappedList) {
        super(wrappedList);
    }

    FieldList(Field<?>... wrappedList) {
        super(Arrays.asList(wrappedList));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> getField(Field<T> field) {
        if (field == null) {
            return null;
        }

        return (Field<T>) getField(field.getName());
    }

    @Override
    public final Field<?> getField(String name) {
        for (Field<?> f : this) {
            if (f.getName().equals(name)) {
                return f;
            }
        }

        return null;
    }

    @Override
    public final Field<?> getField(int index) {
        if (index >= 0 && index < size()) {
            return get(index);
        }

        return null;
    }

    @Override
    public final FieldList getFields() {
        return this;
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        if (indexes == null) {
            indexes = new LinkedHashMap<Field<?>, Integer>();

            for (int i = 0; i < size(); i++) {
                indexes.put(get(i), i);
            }
        }

        // Return the field's index itself, if it is contained
        if (indexes.containsKey(field)) {
            return indexes.get(field);
        }

        // Check if the field is an alias of a contained field
        if (indexes.containsKey(getField(field))) {
            return indexes.get(getField(field));
        }

        throw new IllegalArgumentException("Field " + field + " is not contained in list");
    }
}
