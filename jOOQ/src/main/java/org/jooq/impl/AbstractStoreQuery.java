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

import static org.jooq.impl.Factory.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.StoreQuery;
import org.jooq.Table;

/**
 * A default implementation for store queries.
 *
 * @author Lukas Eder
 */
abstract class AbstractStoreQuery<R extends Record> extends AbstractQuery implements StoreQuery<R> {

    /**
     * Generated UID
     */
    private static final long                   serialVersionUID = 6864591335823160569L;

    private final Table<R>                      into;

    AbstractStoreQuery(Configuration configuration, Table<R> into) {
        super(configuration);

        this.into = into;
    }

    @Override
    public final List<Attachable> getAttachables() {
        List<Attachable> result = new ArrayList<Attachable>();
        result.addAll(getAttachables(into));
        result.addAll(getAttachables0());
        return result;
    }

    protected abstract List<Attachable> getAttachables0();
    protected abstract Map<Field<?>, Field<?>> getValues();

    final Table<R> getInto() {
        return into;
    }

    final <T> void addValue(R record, Field<T> field) {
        addValue(field, record.getValue(field));
    }

    @Override
    public final <T> void addValue(Field<T> field, T value) {
        addValue(field, val(value, field));
    }

    @Override
    public final <T> void addValue(Field<T> field, Field<T> value) {
        if (value == null) {
            getValues().put(field, val(null, field));
        }
        else {
            getValues().put(field, value);
        }
    }

    @Override
    public final <A extends ArrayRecord<T>, T> void addValueAsArray(Field<A> field, T... value) {
        if (value == null) {
            getValues().put(field, val(null, field));
        }
        else {
            addValueAsArray(field, Arrays.asList(value));
        }
    }

    @Override
    public final <A extends ArrayRecord<T>, T> void addValueAsArray(Field<A> field, List<T> value) {
        if (value == null) {
            getValues().put(field, val(null, field));
        }
        else {
            try {
                A record = Util.newArrayRecord(field.getType(), getConfiguration());
                record.setList(value);
                addValue(field, record);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Field is not a valid ArrayRecord field: " + field);
            }
        }
    }
}
