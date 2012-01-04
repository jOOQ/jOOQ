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

import static org.jooq.impl.Factory.vals;
import static org.jooq.impl.Util.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.InsertQuery;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertValuesStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class InsertImpl<R extends Record>
    extends AbstractDelegatingQueryPart<InsertQuery<R>>
    implements

    // Cascading interface implementations for Insert behaviour
    InsertValuesStep<R>,
    InsertSetMoreStep<R>,
    InsertOnDuplicateSetMoreStep<R>,
    InsertResultStep<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 4222898879771679107L;

    private final List<Field<?>> fields;
    private final Table<R>       into;
    private boolean              onDuplicateKeyUpdate;

    InsertImpl(Configuration configuration, Table<R> into, Collection<? extends Field<?>> fields) {
        super(new InsertQueryImpl<R>(configuration, into));

        this.into = into;
        this.fields = new ArrayList<Field<?>>(fields);
    }

    // -------------------------------------------------------------------------
    // The Query API
    // -------------------------------------------------------------------------

    @Override
    public final int execute() {
        return getDelegate().execute();
    }

    @Override
    public final Query bind(String param, Object value) {
        return getDelegate().bind(param, value);
    }

    @Override
    public final Query bind(int index, Object value) {
        return getDelegate().bind(index, value);
    }

    // -------------------------------------------------------------------------
    // The DSL API
    // -------------------------------------------------------------------------

    @Override
    public final InsertImpl<R> values(Object... values) {
        return values0(vals(convert(getFields(), values)));
    }

    @Override
    public final InsertImpl<R> values(Field<?>... values) {
        return values0(Arrays.asList(values));
    }

    @Override
    public final InsertImpl<R> values(Collection<?> values) {
        return values0(vals(convert(getFields(), values.toArray())));
    }

    @SuppressWarnings("unchecked")
    private final InsertImpl<R> values0(List<Field<?>> values) {
        if (getFields().size() != values.size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields");
        }

        getDelegate().newRecord();
        for (int i = 0; i < getFields().size(); i++) {
            // javac has trouble when inferring Object for T. Use Void instead
            getDelegate().addValue((Field<Void>) getFields().get(i), (Field<Void>) values.get(i));
        }

        return this;
    }

    private final List<Field<?>> getFields() {

        // [#885] If this insert is called with an implicit field name set, take
        // the fields from the underlying table.
        if (fields.size() == 0) {
            fields.addAll(into.getFields());
        }

        return fields;
    }

    @Override
    public final InsertImpl<R> onDuplicateKeyUpdate() {
        onDuplicateKeyUpdate = true;
        getDelegate().onDuplicateKeyUpdate(true);
        return this;
    }

    @Override
    public final <T> InsertImpl<R> set(Field<T> field, T value) {
        if (onDuplicateKeyUpdate) {
            getDelegate().addValueForUpdate(field, value);
        }
        else {
            getDelegate().addValue(field, value);
        }

        return this;
    }

    @Override
    public final <T> InsertImpl<R> set(Field<T> field, Field<T> value) {
        if (onDuplicateKeyUpdate) {
            getDelegate().addValueForUpdate(field, value);
        }
        else {
            getDelegate().addValue(field, value);
        }

        return this;
    }

    @Override
    public final InsertImpl<R> set(Map<? extends Field<?>, ?> map) {
        if (onDuplicateKeyUpdate) {
            getDelegate().addValuesForUpdate(map);
        }
        else {
            getDelegate().addValues(map);
        }

        return this;
    }

    @Override
    public final InsertImpl<R> newRecord() {
        getDelegate().newRecord();
        return this;
    }

    @Override
    public final InsertImpl<R> returning() {
        getDelegate().setReturning();
        return this;
    }

    @Override
    public final InsertImpl<R> returning(Field<?>... f) {
        getDelegate().setReturning(f);
        return this;
    }

    @Override
    public final InsertImpl<R> returning(Collection<? extends Field<?>> f) {
        getDelegate().setReturning(f);
        return this;
    }

    @Override
    public final Result<R> fetch() {
        getDelegate().execute();
        return getDelegate().getReturnedRecords();
    }

    @Override
    public final R fetchOne() {
        getDelegate().execute();
        return getDelegate().getReturnedRecord();
    }
}
